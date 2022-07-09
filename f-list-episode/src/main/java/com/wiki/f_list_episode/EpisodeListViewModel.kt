package com.wiki.f_list_episode

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.navigation.FragmentRouter
import com.wiki.cf_core.navigation.routes.DetailEpisodeRoute
import com.wiki.cf_core.navigation.routes.SearchRoute
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.f_list_episode.EpisodeListScreenFeature.*
import com.wiki.i_episode.use_cases.GetAllEpisodesUseCase
import kotlinx.coroutines.flow.map

class EpisodeListViewModel(
    private val router: FragmentRouter,
    private val getAllEpisodesUseCase: GetAllEpisodesUseCase
) : BaseViewModel<State, Actions, Events>(State()) {

    private val pagination = DefaultPaginator(
        initialKey = state.page,
        onLoadUpdated = { isLoading ->
            renderState {
                state.copy(isLoading = isLoading)
            }
        },
        onRequest = { nextPage ->
            getAllEpisodesUseCase(nextPage)
        },
        getNextKey = { state.page + 1 },
        onError = {
            renderState { state.copy(isLoading = false) }
            showSnackBar(it?.messageError)
        },
        onSuccess = { items, newKey, isRefresh ->
            items.map { response ->
                renderState {
                    state.copy(
                        endReached = response.info.next == null,
                        episodes = if (isRefresh) emptyList() else state.episodes
                    )
                }
                response.result.map { it.toEpisodeDto() }
            }.collect { episodes ->
                renderState {
                    state.copy(
                        episodes = state.episodes + episodes,
                        page = newKey,
                    )
                }
            }
        }
    )

    init {
        loadNextPage()
    }

    override fun bindEvents(event: Events) {
        when (event) {
            is Events.LoadNextPage -> loadNextPage()
            is Events.OnRefresh -> onRefresh()
            is Events.OnSearchClick -> onSearchClick()
            is Events.OnEpisodeClick -> onEpisodeClick(event)
        }
    }

    private fun onSearchClick() {
        val route = SearchRoute(SearchFeature.EPISODE)
        router.navigateTo(route)
    }

    private fun onEpisodeClick(event: Events.OnEpisodeClick) {
        val route = DetailEpisodeRoute(event.episode)
        router.navigateTo(route)
    }

    private fun loadNextPage() {
        if (state.endReached) return
        launchInternetRequest {
            pagination.loadNextItems()
        }
    }

    private fun onRefresh() {
        renderState {
            state.copy(
                page = 1,
                endReached = false
            )
        }
        pagination.reset()
        loadNextPage()
    }

}
