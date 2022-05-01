package com.wiki.f_list_location

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.LocationDto

sealed class LocationListEvents : EventScreen {
    data class OnNavigateToLocation(val location: LocationDto) : LocationListEvents()
}