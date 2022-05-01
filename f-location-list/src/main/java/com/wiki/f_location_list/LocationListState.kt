package com.wiki.f_location_list

import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.LocationDto

data class LocationListState(
    val locations: List<LocationDto> = emptyList(),
    val isLoading: Boolean = true,
    val page: Int = 1,
    val endReached: Boolean = false,
    val isVisibleLastElementList: Boolean = true
) : StateScreen