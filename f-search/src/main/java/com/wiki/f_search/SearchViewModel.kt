package com.wiki.f_search

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.extensions.convertToList
import com.wiki.cf_core.extensions.isNeededClass
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.f_general_adapter.GeneralAdapterUi
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
    State(feature = feature)
) {
    companion object {
        const val SEARCH_DELAY = 300L
    }

    private var searchJob: Job? = null
    private var searchText: String = ""
    private val pagination = DefaultPaginator(
        initialKey = state.page,
        onLoadUpdated = { isLoading ->
            renderState {
                state.copy(isLoading = isLoading)
            }
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
            renderState { state.copy(isLoading = false) }
            showSnackBar(it?.messageError)
        },
        onSuccess = { items, newKey, isRefresh ->
            items.map { response ->
                when (response) {
                    is CharactersResponse -> {
                        renderState {
                            state.copy(
                                endReached = response.info.next == null,
                                searchResultUi = if (isRefresh) emptyList() else state.searchResultUi
                            )
                        }
                        response.result.map { it.toCharacterDto() }
                    }
                    is EpisodesResponse -> {
                        renderState {
                            state.copy(
                                endReached = response.info.next == null,
                                searchResultUi = if (isRefresh) emptyList() else state.searchResultUi
                            )
                        }
                        response.result.map { it.toEpisodeDto() }
                    }
                    is LocationsResponse -> {
                        renderState {
                            state.copy(
                                endReached = response.info.next == null,
                                searchResultUi = if (isRefresh) emptyList() else state.searchResultUi
                            )
                        }
                        response.result.map { it.toLocationDto() }
                    }
                    else -> {}
                }
            }.collect { result ->
                if (result.isNeededClass<CharacterDto>()) {
                    val characters = result.convertToList<CharacterDto>() ?: emptyList()
                    val searchUi = state.searchResultUi + characters.map { GeneralAdapterUi.Character(it) }
                    renderState {
                        state.copy(
                            searchResultUi = searchUi,
                            page = newKey,
                            isVisibleNotFound = false
                        )
                    }
                }
                if (result.isNeededClass<EpisodeDto>()) {
                    val episodes = result.convertToList<EpisodeDto>() ?: emptyList()
                    val searchUi = state.searchResultUi + episodes.map { GeneralAdapterUi.Episode(it) }
                    renderState {
                        state.copy(
                            searchResultUi = searchUi,
                            page = newKey,
                            isVisibleNotFound = false
                        )
                    }
                }
                if (result.isNeededClass<LocationDto>()) {
                    val locations = result.convertToList<LocationDto>() ?: emptyList()
                    val searchUi = state.searchResultUi + locations.map { GeneralAdapterUi.Location(it) }
                    renderState {
                        state.copy(
                            searchResultUi = searchUi,
                            page = newKey,
                            isVisibleNotFound = false
                        )
                    }
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
        renderState {
            state.copy(searchText = text)
        }
        pagination.reset()
        searchJob?.cancel()
        searchJob = launchInternetRequest(
            onNothingFoundError = {
                renderState {
                    state.copy(
                        searchResultUi = emptyList(),
                        isVisibleNotFound = true
                    )
                }
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

}
