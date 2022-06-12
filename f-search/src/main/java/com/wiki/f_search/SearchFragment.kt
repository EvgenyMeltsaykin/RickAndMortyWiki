package com.wiki.f_search

import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.delegates.fragmentArgument
import com.wiki.cf_core.extensions.hideKeyboard
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_core.extensions.showKeyboard
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_extensions.capitalize
import com.wiki.cf_extensions.pagination
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.f_general_adapter.CharacterAdapter
import com.wiki.f_general_adapter.EpisodeAdapter
import com.wiki.f_general_adapter.LocationAdapter
import com.wiki.f_search.SearchScreenFeature.*
import com.wiki.f_search.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : BaseFragment<FragmentSearchBinding, State, Effects, Events, SearchViewModel>() {

    companion object {
        fun newInstance(feature: SearchFeature) = SearchFragment().apply {
            this.feature = feature
        }

        private const val HIDE_KEYBOARD_ON_SCROLL_THRESHOLD = 10

    }

    private val characterAdapter = CharacterAdapter(
        onPreviewLoaded = { },
        onCharacterClick = { character, _ ->
            sendEvent(Events.OnCharacterClick(character))
        }
    )

    private val episodeAdapter = EpisodeAdapter(
        horizontalPadding = 16,
        onEpisodeClick = {
            sendEvent(Events.OnEpisodeClick(it))
        }
    )

    private val locationAdapter = LocationAdapter(
        onLocationClick = {
            sendEvent(Events.OnLocationClick(it))
        }
    )

    private var feature by fragmentArgument<SearchFeature>()

    override val viewModel: SearchViewModel by viewModel { parametersOf(feature) }

    override fun renderState(state: State) {
        when (state.feature) {
            SearchFeature.CHARACTER -> characterAdapter.submitListAndSaveState(
                state.characters,
                binding.rvResult
            )
            SearchFeature.EPISODE -> episodeAdapter.submitListAndSaveState(
                state.episodes,
                binding.rvResult
            )
            SearchFeature.LOCATION -> locationAdapter.submitListAndSaveState(
                state.locations,
                binding.rvResult
            )
        }

        with(binding) {
            rvResult.performIfChanged(state.feature) {
                adapter = when (it) {
                    SearchFeature.CHARACTER -> characterAdapter
                    SearchFeature.EPISODE -> episodeAdapter
                    SearchFeature.LOCATION -> locationAdapter
                }
            }

            etSearch.performIfChanged(state.feature) {
                hint = getHintText(it)
            }

            tvNotFound.performIfChanged(state.feature, state.isVisibleNotFound) { feature, isVisible ->
                text = getNotFoundText(feature)
                this.isVisible = isVisible

            }
        }
    }

    override fun initView() {
        with(binding) {
            rvResult.addItemDecoration(
                DividerItemDecoration(
                    rvResult.context,
                    LinearLayout.VERTICAL
                )
            )
            rvResult.pagination(
                loadThreshold = 5,
                loadNextPage = {
                    sendEvent(Events.LoadNextPage)
                }
            )
            rvResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (kotlin.math.abs(dy) > HIDE_KEYBOARD_ON_SCROLL_THRESHOLD) {
                        etSearch.hideKeyboard()
                    }
                }
            })
            etSearch.doOnTextChanged { text, _, _, _ ->
                viewModel.onChangeSearchText(text.toString())
            }
            etSearch.showKeyboard()
            btnBack.setOnClickListener {
                sendEvent(Events.OnBackClick)
            }
        }

    }

    private fun getHintText(feature: SearchFeature): String {
        return getString(R.string.search_hint, feature.featureName)
    }

    private fun getNotFoundText(feature: SearchFeature): String {
        return getString(R.string.not_found, feature.featureName.capitalize())
    }

    override fun bindEffects(effect: Effects) {
        binding.etSearch.hideKeyboard()
        when (effect) {
            is Effects.OnNavigateToCharacter -> router.navigateTo(
                screenProvider.DetailCharacter(
                    effect.character
                )
            )
            is Effects.OnNavigateToEpisode -> router.navigateTo(screenProvider.DetailEpisode(effect.episode))
            is Effects.OnNavigateToLocation -> router.navigateTo(
                screenProvider.DetailLocation(
                    effect.location
                )
            )
            is Effects.OnNavigateToBack -> router.exit()

        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = false,
                isVisibleToolbar = false
            )
        )
    }
}

