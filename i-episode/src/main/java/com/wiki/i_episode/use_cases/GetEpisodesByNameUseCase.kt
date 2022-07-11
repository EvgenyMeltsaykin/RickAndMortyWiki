package com.wiki.i_episode.use_cases

import com.wiki.cf_network.data.PaginationInfo
import com.wiki.i_episode.EpisodeApiService
import com.wiki.i_episode.data.EpisodesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class GetEpisodesByNameUseCase(
    private val apiService: EpisodeApiService
) {

    suspend operator fun invoke(text: String, page: Int = 1): Result<Flow<EpisodesResponse>> {
        return try {
            Result.success(flowOf(apiService.getEpisodesByName(page = page, name = text)))
        } catch (e: Exception) {
            Result.success(emptyFlow())
        }
    }

    suspend operator fun invoke(text: String): EpisodesResponse {
        return try {
            apiService.getEpisodesByName(page = 1, name = text)
        } catch (e: Exception) {
            EpisodesResponse(result = emptyList(), PaginationInfo())
        }
    }

}