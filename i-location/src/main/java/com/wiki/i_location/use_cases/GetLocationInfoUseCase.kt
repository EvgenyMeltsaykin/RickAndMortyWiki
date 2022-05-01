package com.wiki.i_location.use_cases

import com.wiki.cf_data.LocationDto
import com.wiki.i_location.LocationApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GetLocationInfoUseCase {
    suspend operator fun invoke(id: String): Flow<LocationDto>
}

class GetLocationInfoUseCaseImpl(
    private val apiService: LocationApiService
) : GetLocationInfoUseCase {

    override suspend fun invoke(id: String): Flow<LocationDto> {
        return flowOf(apiService.getLocation(id).toLocationDto())
    }

}