package com.wiki.f_search

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.extensions.convertToList
import com.wiki.cf_core.extensions.isNeededClass
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.f_search.SearchScreenFeature.*
import com.wiki.i_character.data.CharactersResponse
import com.wiki.i_character.use_cases.GetCharactersByNameUseCase
import com.wiki.i_episode.data.EpisodesResponse
import com.wiki.i_episode.use_cases.GetEpisodesByNameUseCase
import com.wiki.i_location.data.LocationsResponse
import com.wiki.i_location.use_cases.GetLocationsByNameUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map

class SearchViewModel(
    private val feature: SearchFeature,
    private val getCharactersByNameUseCase: GetCharactersByNameUseCase,
    private val getEpisodesByNameUseCase: GetEpisodesByNameUseCase,
    private val getLocationsByNameUseCase: GetLocationsByNameUseCase
) : BaseViewModel<State, Effects, Events>(
    State(
        feature = feature
    )
) {
    companion object {
        const val SEARCH_DELAY = 300L
    }

    private var searchJob: Job? = null
    private var searchText: String = ""
    private val pagination = DefaultPaginator(
        initialKey = state.page,
        onLoadUpdated = { isLoading ->
            setState(
                state.copy(isLoading = isLoading)
            )
        },
        onRequest = { nextPage ->
            when (feature) {
                SearchFeature.CHARACTER -> getCharactersByNameUseCase(
                    text = searchText,
                    page = nextPage
                )
                SearchFeature.EPISODE -> getEpisodesByNameUseCase(
                    text = searchText,
                    page = nextPage
                )
                SearchFeature.LOCATION -> getLocationsByNameUseCase(
                    text = searchText,
                    page = nextPage
                )
            }
        },
        getNextKey = { state.page + 1 },
        onError = {
            showSnackBar(it?.messageError)
        },
        onSuccess = { items, newKey, isRefresh ->
            items.map { response ->
                when (response) {
                    is CharactersResponse -> {
                        setState(
                            state.copy(
                                endReached = response.info.next == null,
                                characters = if (isRefresh) emptyList() else state.characters
                            )
                        )
                        response.result.map { it.toCharacterDto() }
                    }
                    is EpisodesResponse -> {
                        setState(
                            state.copy(
                                endReached = response.info.next == null,
                                episodes = if (isRefresh) emptyList() else state.episodes
                            )
                        )
                        response.result.map { it.toEpisodeDto() }
                    }
                    is LocationsResponse -> {
                        setState(
                            state.copy(
                                endReached = response.info.next == null,
                                locations = if (isRefresh) emptyList() else state.locations
                            )
                        )
                        response.result.map { it.toLocationDto() }
                    }
                    else -> {}
                }
            }.collect { result ->
                if (result.isNeededClass<CharacterDto>()) {
                    val characters = result.convertToList<CharacterDto>() ?: emptyList()
                    setState(
                        state.copy(
                            characters = state.characters + characters,
                            page = newKey,
                            isVisibleNotFound = false
                        )
                    )
                }
                if (result.isNeededClass<EpisodeDto>()) {
                    val episodes = result.convertToList<EpisodeDto>() ?: emptyList()
                    setState(
                        state.copy(
                            episodes = state.episodes + episodes,
                            page = newKey,
                            isVisibleNotFound = false
                        )
                    )
                }
                if (result.isNeededClass<LocationDto>()) {
                    val locations = result.convertToList<LocationDto>() ?: emptyList()
                    setState(
                        state.copy(
                            locations = state.locations + locations,
                            page = newKey,
                            isVisibleNotFound = false
                        )
                    )
                }
            }
        }
    )

    private fun loadNextPage() {
        if (state.endReached) return
        launchInternetRequest {
            pagination.loadNextItems()
        }
    }

    fun onChangeSearchText(text: String) {
        if (searchText == text) return
        searchText = text
        setState(
            state.copy(searchText = text)
        )
        pagination.reset()
        searchJob?.cancel()
        searchJob = launchInternetRequest(
            onNothingFoundError = {
                setState(
                    state.copy(
                        characters = emptyList(),
                        locations = emptyList(),
                        episodes = emptyList(),
                        isVisibleNotFound = true
                    )
                )
            }
        ) {
            delay(SEARCH_DELAY)
            pagination.loadNextItems()
        }

    }

    override fun bindEvents(event: Events) {
        when (event) {
            is Events.LoadNextPage -> loadNextPage()
            is Events.OnCharacterClick -> setEffect {
                Effects.OnNavigateToCharacter(event.character)
            }
            is Events.OnEpisodeClick -> setEffect {
                Effects.OnNavigateToEpisode(event.episode)
            }
            is Events.OnLocationClick -> setEffect {
                Effects.OnNavigateToLocation(event.location)
            }
            is Events.OnBackClick -> setEffect {
                Effects.OnNavigateToBack
            }
            is Events.OnChangeSearchText -> onChangeSearchText(event.text)
        }
    }

    fun onBackClick() {
        sendEvent(SearchEvents.OnBackClick)
    }
}
