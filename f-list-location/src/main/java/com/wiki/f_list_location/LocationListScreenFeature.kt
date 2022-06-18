package com.wiki.f_list_location

import com.wiki.cf_core.base.EffectScreen
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature

class LocationListScreenFeature {

    sealed class Events : EventScreen {
        object LoadNextPage:Events()
        object OnRefresh:Events()
        object OnSearchClick:Events()
        data class OnLocationClick(val location: LocationDto):Events()
    }

    sealed class Effects : EffectScreen {
        data class OnNavigateToLocation(val location: LocationDto) : Effects()
        data class NavigateToSearch(val feature: SearchFeature = SearchFeature.LOCATION) : Effects()
    }

    data class State(
        val locations: List<LocationDto> = emptyList(),
        val isLoading: Boolean = true,
        val page: Int = 1,
        val endReached: Boolean = false,
    ) : StateScreen

}