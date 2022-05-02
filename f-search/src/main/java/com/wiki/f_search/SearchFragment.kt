package com.wiki.f_search

import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.delegates.fragmentArgument
import com.wiki.cf_core.navigation.SharedElementFragment
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_data.isCharacter
import com.wiki.cf_extensions.capitalize
import com.wiki.cf_extensions.pagination
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.ToolbarConfig
import com.wiki.cf_ui.controllers.ToolbarType
import com.wiki.f_general_adapter.CharacterAdapter
import com.wiki.f_general_adapter.EpisodeAdapter
import com.wiki.f_general_adapter.LocationAdapter
import com.wiki.f_search.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : BaseFragment<
    FragmentSearchBinding,
    SearchEvents,
    SearchState,
    SearchViewModel
    >(), SharedElementFragment {

    companion object {
        fun newInstance(feature: SearchFeature) = SearchFragment().apply {
            this.feature = feature
        }

        private const val HIDE_KEYBOARD_ON_SCROLL_THRESHOLD = 10

    }

    override var sharedView: View? = null
    private val characterAdapter = CharacterAdapter(
        onPreviewLoaded = {
            startPostponedEnterTransition()
        },
        onCharacterClick = { character, view ->
            sharedView = view
            viewModel.onCharacterClick(character)
        }
    )

    private val episodeAdapter = EpisodeAdapter(
        horizontalPadding = 16,
        onEpisodeClick = { viewModel.onEpisodeClick(it) }
    )

    private val locationAdapter = LocationAdapter(
        onLocationClick = { viewModel.onLocationClick(it) }
    )

    private var feature by fragmentArgument<SearchFeature>()

    override val viewModel: SearchViewModel by viewModel { parametersOf(feature) }

    override fun renderState(state: SearchState) {
        when (state.feature) {
            SearchFeature.CHARACTER -> characterAdapter.submitListAndSaveState(state.characters, binding.rvResult) {
                startPostponedEnterTransition()
            }
            SearchFeature.EPISODE -> episodeAdapter.submitListAndSaveState(state.episodes, binding.rvResult)
            SearchFeature.LOCATION -> locationAdapter.submitListAndSaveState(state.locations, binding.rvResult)
        }
        binding.tvNotFound.isVisible = state.isVisibleNotFound
    }


    override fun initView(initialState: SearchState) {
        if (feature.isCharacter()) postponeEnterTransition()
        with(binding) {
            rvResult.adapter = when (initialState.feature) {
                SearchFeature.CHARACTER -> characterAdapter
                SearchFeature.EPISODE -> episodeAdapter
                SearchFeature.LOCATION -> locationAdapter
            }
            rvResult.addItemDecoration(DividerItemDecoration(rvResult.context, LinearLayout.VERTICAL))
            rvResult.pagination(
                loadThreshold = 5,
                loadNextPage = { viewModel.loadNextPage() }
            )
            rvResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (kotlin.math.abs(dy) > HIDE_KEYBOARD_ON_SCROLL_THRESHOLD) {
                        hideSearchKeyboard()
                    }
                }
            })

            tvNotFound.text = getNotFoundText(initialState.feature)
            showSearchKeyboard()
        }

    }

    private fun getNotFoundText(feature: SearchFeature): String {
        return getString(R.string.not_found, feature.featureName.capitalize())
    }

    override fun bindEvents(event: SearchEvents) {
        hideSearchKeyboard()
        when (event) {
            is SearchEvents.OnCharacterClick -> router.navigateTo(screenProvider.DetailCharacter(event.character))
            is SearchEvents.OnEpisodeClick -> router.navigateTo(screenProvider.DetailEpisode(event.episode))
            is SearchEvents.OnLocationClick -> router.navigateTo(screenProvider.DetailLocation(event.location))
        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = false,
                isVisibleToolbar = true,
                toolbarConfig = ToolbarConfig(
                    toolbarType = ToolbarType.Search(
                        onTextChange = { viewModel.onChangeSearchText(it) },
                        hint = getHintText(feature)
                    )
                )
            )
        )
    }

    private fun getHintText(feature: SearchFeature): String {
        return getString(R.string.search_hint, feature.featureName)
    }
}

