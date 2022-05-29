package com.wiki.f_detail_character

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.transition.MaterialContainerTransform
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.delegates.fragmentArgument
import com.wiki.cf_core.extensions.roundCorners
import com.wiki.cf_core.navigation.SharedElementFragment
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.LifeStatus
import com.wiki.cf_extensions.getDrawable
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.extensions.blurMask
import com.wiki.cf_ui.extensions.setTextOrGone
import com.wiki.f_detail_character.databinding.FragmentDetailCharacterBinding
import com.wiki.f_general_adapter.EpisodeAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailCharacterFragment : BaseFragment<
    FragmentDetailCharacterBinding,
    DetailCharacterEvents,
    DetailCharacterState,
    DetailCharacterViewModel
    >(), SharedElementFragment {

    companion object {
        fun newInstance(character: CharacterDto) = DetailCharacterFragment().apply {
            this.character = character
        }
    }

    private val adapter: EpisodeAdapter = EpisodeAdapter(
        onEpisodeClick = { viewModel.onEpisodeClick(it) }
    )
    private var character by fragmentArgument<CharacterDto>()

    override val viewModel: DetailCharacterViewModel by viewModel { parametersOf(character) }
    override var sharedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
    }

    private fun setupTransition() {
        val transform = MaterialContainerTransform()
        transform.scrimColor = Color.TRANSPARENT
        sharedElementEnterTransition = transform
    }

    override fun renderState(state: DetailCharacterState) {
        with(binding) {
            tvCharacterName.text = state.name
            tvFirstSeenInEpisode.setTextOrGone(secondRowText = state.firstSeenInEpisodeName)
            tvOriginLocation.setTextOrGone(secondRowText = state.originLocation)
            tvLastLocation.setTextOrGone(secondRowText = state.lastKnownLocation)
            tvStatus.setTextOrGone(secondRowText = state.lifeStatus.status)
            tvSpecies.setTextOrGone(secondRowText = state.species)
            tvGender.setTextOrGone(secondRowText = state.gender)
            adapter.submitList(state.episodes)
            binding.tvEpisodesStatic.isVisible = state.episodes.isNotEmpty()
        }
    }

    override fun initView(initialState: DetailCharacterState) {
        with(binding) {
            rvEpisode.adapter = adapter
            rvEpisode.addItemDecoration(DividerItemDecoration(rvEpisode.context, LinearLayout.VERTICAL))
            ivPreview.transitionName = character.imageUrl
            sharedView = ivPreview

            Glide.with(requireContext())
                .load(initialState.imageUrl)
                .apply(RequestOptions().roundCorners(16))
                .into(ivPreview)
            ivStatus.setImageDrawable(getDrawable(getStatusDrawableId(initialState.lifeStatus)))

            tvOriginLocation.setOnClickListener {
                viewModel.onOriginLocationClick()
            }
            tvLastLocation.setOnClickListener {
                viewModel.onLastKnownLocation()
            }
        }
    }

    private fun setBlurRadiusOnText(blurRadius: Float) {
        with(binding) {
            tvFirstSeenInEpisode.blurRadius = blurRadius
            tvOriginLocation.blurRadius = blurRadius
            tvLastLocation.blurRadius = blurRadius
            tvEpisodesStatic.text = tvEpisodesStatic.text.toString().blurMask(blurRadius)
        }
    }

    @DrawableRes
    private fun getStatusDrawableId(status: LifeStatus): Int {
        return when (status) {
            LifeStatus.ALIVE -> R.drawable.ic_circle_green
            LifeStatus.DEAD -> R.drawable.ic_circle_red
            LifeStatus.UNKNOWN -> R.drawable.ic_circle_gray
            else -> R.drawable.ic_circle_gray
        }
    }

    override fun bindEvents(event: DetailCharacterEvents) {
        when (event) {
            is DetailCharacterEvents.OnNavigateBack -> router.exit()
            is DetailCharacterEvents.NavigateToEpisode -> {
                router.navigateTo(screenProvider.DetailEpisode(event.episode))
            }
            is DetailCharacterEvents.NavigateToLocation -> {
                router.navigateTo(
                    screenProvider.DetailLocation(
                        location = null,
                        locationData = event.locationData
                    )
                )
            }
        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = true
            )
        )
    }

}