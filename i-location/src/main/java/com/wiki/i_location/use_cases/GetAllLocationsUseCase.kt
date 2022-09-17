package com.wiki.i_location.use_cases

import com.wiki.i_location.LocationApiService
import com.wiki.i_location.data.LocationsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetAllLocationsUseCase(
    private val apiService: LocationApiService
) {

    suspend operator fun invoke(page: Int): Result<Flow<LocationsResponse>> {
        return Result.success(flowOf(apiService.getAllLocations(page)))
    }

}