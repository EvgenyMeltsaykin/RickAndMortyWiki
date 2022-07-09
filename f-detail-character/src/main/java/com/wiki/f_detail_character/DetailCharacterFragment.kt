package com.wiki.f_detail_character

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.transition.MaterialContainerTransform
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.fragment.BaseFragment
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_core.extensions.roundCorners
import com.wiki.cf_core.extensions.sendEvent
import com.wiki.cf_core.navigation.routes.DetailCharacterRoute
import com.wiki.cf_data.LifeStatus
import com.wiki.cf_extensions.getDrawable
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.ToolbarConfig
import com.wiki.cf_ui.extensions.blurMask
import com.wiki.cf_ui.extensions.setTextOrGone
import com.wiki.f_detail_character.DetailCharacterScreenFeature.*
import com.wiki.f_detail_character.databinding.FragmentDetailCharacterBinding
import com.wiki.f_general_adapter.GeneralAdapterUi
import com.wiki.f_general_adapter.getEpisodeAdapter
import com.wiki.f_general_adapter.getGeneralAdaptersDiffCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailCharacterFragment : BaseFragment<State, Actions, Events, DetailCharacterViewModel, DetailCharacterRoute>() {

    override val binding: FragmentDetailCharacterBinding by viewBinding(CreateMethod.INFLATE)

    companion object {
        fun newInstance(route: DetailCharacterRoute) = DetailCharacterFragment().apply {
            this.route = route
        }
    }

    private val episodeAdapter = AsyncListDifferDelegationAdapter(
        getGeneralAdaptersDiffCallback(),
        AdapterDelegatesManager<List<GeneralAdapterUi>>()
            .addDelegate(
                getEpisodeAdapter(
                    onEpisodeClick = { viewModel.sendEvent(Events.OnEpisodeClick(it)) }
                )
            )
    )

    override val viewModel: DetailCharacterViewModel by viewModel { parametersOf(route.character) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
    }

    private fun setupTransition() {
        val transform = MaterialContainerTransform()
        transform.scrimColor = Color.TRANSPARENT
        sharedElementEnterTransition = transform
    }

    override fun renderState(state: State) {
        with(binding) {
            tvCharacterName.performIfChanged(state.name) {
                text = it
            }
            tvFirstSeenInEpisode.performIfChanged(state.firstSeenInEpisodeName) {
                setTextOrGone(secondRowText = state.firstSeenInEpisodeName)
            }
            tvOriginLocation.performIfChanged(state.originLocation) {
                setTextOrGone(secondRowText = state.originLocation)
            }
            tvLastLocation.performIfChanged(state.lastKnownLocation) {
                setTextOrGone(secondRowText = state.lastKnownLocation)
            }
            tvStatus.performIfChanged(state.lifeStatus.status) {
                setTextOrGone(secondRowText = state.lifeStatus.status)
            }
            tvSpecies.performIfChanged(state.species) {
                setTextOrGone(secondRowText = state.species)
            }
            tvGender.performIfChanged(state.gender) {
                setTextOrGone(secondRowText = state.gender)
            }
            rvEpisode.performIfChanged(state.episodes) { episodes ->
                episodeAdapter.items = episodes.map { GeneralAdapterUi.Episode(it) }
            }
            tvEpisodesStatic.performIfChanged(state.episodes.isNotEmpty()) {
                isVisible = it
            }
            ivPreview.performIfChanged(state.imageUrl) {
                Glide.with(requireContext())
                    .load(state.imageUrl)
                    .apply(RequestOptions().roundCorners(16))
                    .into(ivPreview)
            }
            ivStatus.performIfChanged(state.lifeStatus) {
                ivStatus.setImageDrawable(getDrawable(getStatusDrawableId(state.lifeStatus)))
            }

        }
    }

    override fun initView() {
        with(binding) {
            rvEpisode.adapter = episodeAdapter
            rvEpisode.addItemDecoration(
                DividerItemDecoration(
                    rvEpisode.context,
                    LinearLayout.VERTICAL
                )
            )

            tvOriginLocation.setOnClickListener {
                viewModel.sendEvent(Events.OnOriginLocationClick)
            }
            tvLastLocation.setOnClickListener {
                viewModel.sendEvent(Events.OnLastKnownLocation)
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

    override fun bindActions(action: Actions) {
        when (action) {

        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleToolbar = true,
                isVisibleBackButton = true,
                isVisibleBottomNavigation = true,
                toolbarConfig = ToolbarConfig(
                    title = getString(R.string.detail_character_toolbar_title)
                )
            )
        )
    }

}