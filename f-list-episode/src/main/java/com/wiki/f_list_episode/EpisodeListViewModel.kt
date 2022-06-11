package com.wiki.f_list_episode

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.f_list_episode.EpisodeListScreenFeature.*
import com.wiki.i_episode.use_cases.GetAllEpisodesUseCase
import kotlinx.coroutines.flow.map

class EpisodeListViewModel(
    private val getAllEpisodesUseCase: GetAllEpisodesUseCase
) : BaseViewModel<State, Effects,Events>(State()) {

    private val pagination = DefaultPaginator(
        initialKey = state.page,
        onLoadUpdated = { isLoading ->
            setState(
                state.copy(isLoading = isLoading)
            )
        },
        onRequest = { nextPage ->
            getAllEpisodesUseCase(nextPage)
        },
        getNextKey = { state.page + 1 },
        onError = {
            showSnackBar(it?.messageError)
        },
        onSuccess = { items, newKey, isRefresh ->
            items.map { response ->
                setState(
                    state.copy(
                        endReached = response.info.next == null,
                        episodes = if (isRefresh) emptyList() else state.episodes
                    )
                )
                response.result.map { it.toEpisodeDto() }
            }.collect { episodes ->
                setState(
                    state.copy(
                        episodes = state.episodes + episodes,
                        page = newKey,
                    )
                )
            }
        }
    )

    init {
        loadNextPage()
    }

    override fun bindEvents(event: Events) {
        when(event){
            is Events.LoadNextPage -> loadNextPage()
            is Events.OnRefresh -> onRefresh()
            is Events.OnSearchClick -> setEffect {
                Effects.NavigateToSearch()
            }
            is Events.OnEpisodeClick ->setEffect{
                Effects.OnNavigateToEpisode(event.episode)
            }
        }
    }

    private fun loadNextPage() {
        if (state.endReached) return
        launchInternetRequest {
            pagination.loadNextItems()
        }
    }

    private fun onRefresh() {
        setState(
            state.copy(
                page = 1,
                endReached = false
            )
        )
        pagination.reset()
        loadNextPage()
    }

}
