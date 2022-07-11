package com.wiki.i_location.use_cases

import com.wiki.cf_network.data.PaginationInfo
import com.wiki.i_location.LocationApiService
import com.wiki.i_location.data.LocationsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class GetLocationsByNameUseCase(
    private val apiService: LocationApiService
) {

    suspend operator fun invoke(text: String, page: Int): Result<Flow<LocationsResponse>> {
        return try {
            Result.success(flowOf(apiService.getLocationsByName(page = page, name = text)))
        } catch (e: Exception) {
            Result.success(emptyFlow())
        }
    }

    suspend operator fun invoke(text: String): LocationsResponse {
        return try {
            apiService.getLocationsByName(page = 1, name = text)
        } catch (e: Exception) {
            LocationsResponse(result = emptyList(), PaginationInfo())
        }
    }

}