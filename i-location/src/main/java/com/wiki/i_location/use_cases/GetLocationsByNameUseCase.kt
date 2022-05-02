package com.wiki.i_location.use_cases

import com.wiki.i_location.LocationApiService
import com.wiki.i_location.data.LocationsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GetLocationsByNameUseCase {
    suspend operator fun invoke(text: String, page: Int = 1): Result<Flow<LocationsResponse>>
}

class GetLocationsByNameUseCaseImpl(
    private val apiService: LocationApiService
) : GetLocationsByNameUseCase {

    override suspend fun invoke(text: String, page: Int): Result<Flow<LocationsResponse>> {
        return Result.success(flowOf(apiService.getLocationsByName(page = page, name = text)))
    }

}