package com.wiki.f_search

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.extensions.convertToList
import com.wiki.cf_core.extensions.isNeededClass
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.i_character.data.CharactersResponse
import com.wiki.i_character.use_cases.GetCharactersByNameUseCase
import com.wiki.i_episode.data.EpisodesResponse
import com.wiki.i_episode.use_cases.GetEpisodesByNameUseCase
import com.wiki.i_location.data.LocationsResponse
import com.wiki.i_location.use_cases.GetLocationsByNameUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class SearchViewModel(
    private val feature: SearchFeature,
    private val getCharactersByNameUseCase: GetCharactersByNameUseCase,
    private val getEpisodesByNameUseCase: GetEpisodesByNameUseCase,
    private val getLocationsByNameUseCase: GetLocationsByNameUseCase
) : BaseViewModel<SearchEvents, SearchState>(
    SearchState(
        feature = feature
    )
) {
    companion object {
        const val SEARCH_DELAY = 300L
    }

    private var searchJob: Job? = null
    private var searchText: String = ""
    private val pagination = DefaultPaginator(
        initialKey = state.value.page,
        onLoadUpdated = { isLoading ->
            _state.update {
                it.copy(isLoading = isLoading)
            }
        },
        onRequest = { nextPage ->
            when (feature) {
                SearchFeature.CHARACTER -> getCharactersByNameUseCase(text = searchText, page = nextPage)
                SearchFeature.EPISODE -> getEpisodesByNameUseCase(text = searchText, page = nextPage)
                SearchFeature.LOCATION -> getLocationsByNameUseCase(text = searchText, page = nextPage)
            }
        },
        getNextKey = { state.value.page + 1 },
        onError = {
            showSnackBar(it?.messageError)
        },
        onSuccess = { items, newKey, isRefresh ->
            items.map { response ->
                when (response) {
                    is CharactersResponse -> {
                        _state.update {
                            it.copy(
                                endReached = response.info.next == null,
                                characters = if (isRefresh) emptyList() else it.characters
                            )
                        }
                        response.result.map { it.toCharacterDto() }
                    }
                    is EpisodesResponse -> {
                        _state.update {
                            it.copy(
                                endReached = response.info.next == null,
                                episodes = if (isRefresh) emptyList() else it.episodes
                            )
                        }
                        response.result.map { it.toEpisodeDto() }
                    }
                    is LocationsResponse -> {
                        _state.update {
                            it.copy(
                                endReached = response.info.next == null,
                                locations = if (isRefresh) emptyList() else it.locations
                            )
                        }
                        response.result.map { it.toLocationDto() }
                    }
                    else -> {}
                }
            }.collect { result ->
                if (result.isNeededClass<CharacterDto>()) {
                    val characters = result.convertToList<CharacterDto>() ?: emptyList()
                    _state.update {
                        it.copy(
                            characters = it.characters + characters,
                            page = newKey,
                            isVisibleNotFound = false
                        )
                    }
                }
                if (result.isNeededClass<EpisodeDto>()) {
                    val episodes = result.convertToList<EpisodeDto>() ?: emptyList()
                    _state.update {
                        it.copy(
                            episodes = it.episodes + episodes,
                            page = newKey,
                            isVisibleNotFound = false
                        )
                    }
                }
                if (result.isNeededClass<LocationDto>()) {
                    val locations = result.convertToList<LocationDto>() ?: emptyList()
                    _state.update {
                        it.copy(
                            locations = it.locations + locations,
                            page = newKey,
                            isVisibleNotFound = false
                        )
                    }
                }
            }
        }
    )

    fun loadNextPage() {
        if (state.value.endReached) return
        launchInternetRequest {
            pagination.loadNextItems()
        }
    }

    fun onChangeSearchText(text: String) {
        if (searchText == text) return
        searchText = text
        pagination.reset()
        searchJob?.cancel()
        searchJob = launchInternetRequest(
            onNothingFoundError = {
                _state.update {
                    it.copy(
                        characters = emptyList(),
                        locations = emptyList(),
                        episodes = emptyList(),
                        isVisibleNotFound = true
                    )
                }
            }
        ) {
            delay(SEARCH_DELAY)
            pagination.loadNextItems()
        }

    }

    fun onCharacterClick(character: CharacterDto) {
        sendEvent(SearchEvents.OnCharacterClick(character))
    }

    fun onEpisodeClick(episode: EpisodeDto) {
        sendEvent(SearchEvents.OnEpisodeClick(episode))
    }

    fun onLocationClick(location: LocationDto) {
        sendEvent(SearchEvents.OnLocationClick(location))
    }
}
