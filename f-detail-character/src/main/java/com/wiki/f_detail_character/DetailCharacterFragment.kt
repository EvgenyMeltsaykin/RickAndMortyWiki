package com.wiki.f_detail_character

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
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
import com.wiki.cf_ui.extensions.onTransitionCompeteListener
import com.wiki.cf_ui.extensions.progressChangeListener
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

    private var saveStateMotion: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
    }

    override fun onPause() {
        saveStateMotion = binding.root.onSaveInstanceState()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.root.onRestoreInstanceState(saveStateMotion)
    }

    private fun setupTransition() {
        val transform = MaterialContainerTransform()
        transform.scrimColor = Color.TRANSPARENT
        sharedElementEnterTransition = transform
    }

    override fun renderState(state: DetailCharacterState) {
        with(binding) {
            tvFirstSeenInEpisode.secondRowText = state.firstSeenInEpisodeName
            adapter.submitList(state.episodes)
        }
    }

    override fun initView(initialState: DetailCharacterState) {
        with(binding) {
            rvEpisode.adapter = adapter
            rvEpisode.addItemDecoration(DividerItemDecoration(rvEpisode.context, LinearLayout.VERTICAL))
            root.progressChangeListener { progress ->
                val blurRadius = (1 - progress) * 10f
                setBlurRadiusOnText(blurRadius)
            }
            root.onTransitionCompeteListener {
                if (it == R.id.end) {
                    setBlurRadiusOnText(0f)
                }
            }
            btnClose.setOnClickListener {
                viewModel.onCloseClick()
            }
            ivPreview.transitionName = character.imageUrl
            sharedView = ivPreview

            tvCharacterName.text = initialState.name
            tvStatus.secondRowText = initialState.lifeStatus.status
            tvSpecies.secondRowText = initialState.species
            tvGender.secondRowText = initialState.gender
            Glide.with(requireContext())
                .load(initialState.imageUrl)
                .apply(RequestOptions().roundCorners(16))
                .into(ivPreview)
            ivStatus.setImageDrawable(getDrawable(getStatusDrawableId(initialState.lifeStatus)))
            tvOriginLocation.secondRowText = initialState.originLocation
            tvLastLocation.secondRowText = initialState.lastKnownLocation
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
        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = false
            )
        )
    }

}