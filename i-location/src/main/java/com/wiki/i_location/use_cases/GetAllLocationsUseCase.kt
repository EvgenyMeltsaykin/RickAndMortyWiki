package com.wiki.i_location.use_cases

import com.wiki.i_location.LocationApiService
import com.wiki.i_location.data.LocationsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GetAllLocationsUseCase {
    suspend operator fun invoke(page: Int = 0): Result<Flow<LocationsResponse>>
}

class GetAllLocationsUseCaseImpl(
    private val apiService: LocationApiService
) : GetAllLocationsUseCase {

    override suspend fun invoke(page: Int): Result<Flow<LocationsResponse>> {
        return Result.success(flowOf(apiService.getAllLocations(page)))
    }

}