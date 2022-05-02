package com.wiki.f_list_location

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature

sealed class LocationListEvents : EventScreen {
    data class OnNavigateToLocation(val location: LocationDto) : LocationListEvents()
    data class NavigateToSearch(val feature: SearchFeature = SearchFeature.LOCATION) : LocationListEvents()
}