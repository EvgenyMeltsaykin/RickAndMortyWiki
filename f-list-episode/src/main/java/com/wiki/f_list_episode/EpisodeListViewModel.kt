package com.wiki.f_list_episode

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.i_episode.use_cases.GetAllEpisodesUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class EpisodeListViewModel(
    private val getAllEpisodesUseCase: GetAllEpisodesUseCase
) : BaseViewModel<EpisodeListEvents, EpisodeListState>(EpisodeListState()) {

    private val pagination = DefaultPaginator(
        initialKey = state.value.page,
        onLoadUpdated = { isLoading ->
            _state.update {
                it.copy(isLoading = isLoading)
            }
        },
        onRequest = { nextPage ->
            getAllEpisodesUseCase(nextPage)
        },
        getNextKey = { state.value.page + 1 },
        onError = {
            showSnackBar(it?.messageError)
        },
        onSuccess = { items, newKey, isRefresh ->
            items.map { response ->
                _state.update {
                    it.copy(
                        endReached = response.info.next == null,
                        episodes = if (isRefresh) emptyList() else it.episodes
                    )
                }
                response.result.map { it.toEpisodeDto() }
            }.collect { episodes ->
                _state.update {
                    it.copy(
                        episodes = it.episodes + episodes,
                        page = newKey,
                    )
                }
            }
        }
    )

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        if (state.value.endReached) return
        launchInternetRequest {
            pagination.loadNextItems()
        }
    }

    fun onEpisodeClick(episode: EpisodeDto) {
        sendEvent(EpisodeListEvents.OnNavigateToEpisode(episode))
    }

    fun onRefresh() {
        _state.update {
            it.copy(endReached = false)
        }
        pagination.reset()
        loadNextPage()
    }


}
