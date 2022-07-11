package com.wiki.f_search

import androidx.lifecycle.viewModelScope
import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.delegates.adapter.AdapterDelegateItem
import com.wiki.cf_core.extensions.convertToList
import com.wiki.cf_core.extensions.isNeededClass
import com.wiki.cf_core.navigation.FragmentRouter
import com.wiki.cf_core.navigation.routes.DetailCharacterRoute
import com.wiki.cf_core.navigation.routes.DetailEpisodeRoute
import com.wiki.cf_core.navigation.routes.DetailLocationRoute
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.f_general_adapter.GeneralAdapterUi
import com.wiki.f_search.SearchScreenFeature.*
import com.wiki.f_search.data.SearchItemUi
import com.wiki.i_character.data.CharactersResponse
import com.wiki.i_character.use_cases.GetCharactersByNameUseCase
import com.wiki.i_episode.data.EpisodesResponse
import com.wiki.i_episode.use_cases.GetEpisodesByNameUseCase
import com.wiki.i_location.data.LocationsResponse
import com.wiki.i_location.use_cases.GetLocationsByNameUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(
    private val router: FragmentRouter,
    private val feature: SearchFeature,
    private val getCharactersByNameUseCase: GetCharactersByNameUseCase,
    private val getEpisodesByNameUseCase: GetEpisodesByNameUseCase,
    private val getLocationsByNameUseCase: GetLocationsByNameUseCase
) : BaseViewModel<State, Actions, Events>(
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
                else -> Result.success(emptyFlow())
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
                                endReached = response.info?.next == null,
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
                    val searchUi =
                        state.searchResultUi + characters.map { SearchItemUi.Item(GeneralAdapterUi.Character(it)) }
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
                    val searchUi =
                        state.searchResultUi + episodes.map { SearchItemUi.Item(GeneralAdapterUi.Episode(it)) }
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
                    val searchUi =
                        state.searchResultUi + locations.map { SearchItemUi.Item(GeneralAdapterUi.Location(it)) }
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
            when (feature) {
                SearchFeature.ALL -> {}
                SearchFeature.LOCATION, SearchFeature.EPISODE, SearchFeature.CHARACTER -> {
                    pagination.loadNextItems()
                }
            }
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
        searchJob = when (feature) {
            SearchFeature.ALL -> getSearchRequestByAll()
            SearchFeature.LOCATION, SearchFeature.EPISODE, SearchFeature.CHARACTER -> {
                getSearchRequestByFeature()
            }
        }
    }

    private fun getSearchRequestByAll(): Job {
        return viewModelScope.launch {
            delay(SEARCH_DELAY)
            val charactersRequest = flowOf(getCharactersByNameUseCase(searchText))
                .catch {
                    println("1234 charactersRequest ")
                    emitAll(emptyFlow())
                }
                .map { request -> request.result.map { it.toCharacterDto() } }
                .map { charactersDto ->
                    charactersDto.map { GeneralAdapterUi.Character(it) }
                }

            val episodesRequest = flowOf(getEpisodesByNameUseCase(searchText))
                .catch { emitAll(emptyFlow()) }
                .map { request -> request.result.map { it.toEpisodeDto() } }
                .map { episodesDto ->
                    episodesDto.map { GeneralAdapterUi.Episode(it) }
                }

            val locationsRequest = flowOf(getLocationsByNameUseCase(searchText))
                .catch { emitAll(emptyFlow()) }
                .map { request -> request.result.map { it.toLocationDto() } }
                .map { locationsDto ->
                    locationsDto.map { GeneralAdapterUi.Location(it) }
                }

            combine(charactersRequest, episodesRequest, locationsRequest) { characters, episodes, locations ->
                println("1234 combine $characters")
                mutableListOf<AdapterDelegateItem>().apply {
                    addAll(characters.reversed().takeLast(10))
                    if (episodes.isNotEmpty()) {
                        add(SearchItemUi.Header("Episodes"))
                    }
                    addAll(episodes.reversed().takeLast(10))
                    if (locations.isNotEmpty()) {
                        add(SearchItemUi.Header("Locations"))
                    }
                    addAll(locations.reversed().takeLast(10))
                }
            }.collect {
                renderState { state.copy(searchResultUi = it) }
            }

        }
    }

    private fun getSearchRequestByFeature(): Job {
        return launchInternetRequest(
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
            is Events.OnCharacterClick -> onCharacterClick(event)
            is Events.OnEpisodeClick -> onEpisodeClick(event)
            is Events.OnLocationClick -> onLocationClick(event)
            is Events.OnBackClick -> router.back()
            is Events.OnChangeSearchText -> onChangeSearchText(event.text)
        }
    }

    private fun onCharacterClick(event: Events.OnCharacterClick) {
        val route = DetailCharacterRoute(event.character)
        router.navigateTo(route)
    }

    private fun onEpisodeClick(event: Events.OnEpisodeClick) {
        val route = DetailEpisodeRoute(event.episode)
        router.navigateTo(route)
    }

    private fun onLocationClick(event: Events.OnLocationClick) {
        val route = DetailLocationRoute(event.location, null)
        router.navigateTo(route)
    }

}
