package com.wiki.f_list_location

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.f_list_location.LocationListScreenFeature.*
import com.wiki.i_location.use_cases.GetAllLocationsUseCase
import kotlinx.coroutines.flow.map

class LocationListViewModel(
    private val getAllLocationsUseCase: GetAllLocationsUseCase
) : BaseViewModel<State, Effects, Events>(State()) {

    private val pagination = DefaultPaginator(
        initialKey = state.page,
        onLoadUpdated = { isLoading ->
            renderState {
                state.copy(isLoading = isLoading)
            }
        },
        onRequest = { nextPage ->
            getAllLocationsUseCase(nextPage)
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
                        locations = if (isRefresh) emptyList() else state.locations
                    )
                }
                response.result.map { it.toLocationDto() }
            }.collect { locations ->
                renderState {
                    state.copy(
                        locations = state.locations + locations,
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
        when(event){
            is Events.LoadNextPage -> loadNextPage()
            is Events.OnRefresh -> onRefresh()
            is Events.OnSearchClick -> setEffect {
                Effects.NavigateToSearch()
            }
            is Events.OnLocationClick ->setEffect{
                Effects.OnNavigateToLocation(event.location)
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
