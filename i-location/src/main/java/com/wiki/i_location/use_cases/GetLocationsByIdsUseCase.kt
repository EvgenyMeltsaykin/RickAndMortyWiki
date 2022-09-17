package com.wiki.i_location.use_cases

import com.wiki.cf_data.LocationDto
import com.wiki.i_location.LocationApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetLocationsByIdsUseCase(
    private val apiService: LocationApiService
) {

    suspend operator fun invoke(ids: List<String>): Flow<List<LocationDto>> {
        val idsString = ids.joinToString(separator = ",")
        return if (ids.size > 1) {
            flowOf(apiService.getLocations(idsString).map { it.toLocationDto() })
        } else {
            flowOf(listOf(apiService.getLocation(idsString).toLocationDto()))
        }
    }

}