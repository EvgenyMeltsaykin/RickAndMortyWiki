package com.wiki.f_location_list

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_data.LocationDto
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.i_location.use_cases.GetAllLocationsUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class LocationListViewModel(
    private val getAllLocationsUseCase: GetAllLocationsUseCase
) : BaseViewModel<LocationListEvents, LocationListState>(LocationListState()) {

    private val pagination = DefaultPaginator(
        initialKey = state.value.page,
        onLoadUpdated = { isLoading ->
            _state.update {
                it.copy(isLoading = isLoading)
            }
        },
        onRequest = { nextPage ->
            getAllLocationsUseCase(nextPage)
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
                        locations = if (isRefresh) emptyList() else it.locations
                    )
                }
                response.result.map { it.toLocationDto() }
            }.collect { episodes ->
                _state.update {
                    it.copy(
                        locations = it.locations + episodes,
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

    fun onLocationClick(location: LocationDto) {
        sendEvent(LocationListEvents.OnNavigateToLocation(location))
    }

    fun onRefresh() {
        _state.update {
            it.copy(endReached = false)
        }
        pagination.reset()
        loadNextPage()
    }

}
