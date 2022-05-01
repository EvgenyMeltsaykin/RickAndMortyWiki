package com.wiki.f_location_list

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.LocationDto

sealed class LocationListEvents : EventScreen {
    data class OnNavigateToLocation(val location: LocationDto) : LocationListEvents()
}