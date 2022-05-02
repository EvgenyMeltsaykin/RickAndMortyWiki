package com.wiki.i_episode.use_cases

import com.wiki.i_episode.EpisodeApiService
import com.wiki.i_episode.data.EpisodesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GetEpisodesByNameUseCase {
    suspend operator fun invoke(text: String, page: Int = 1): Result<Flow<EpisodesResponse>>
}

class GetEpisodesByNameUseCaseImpl(
    private val apiService: EpisodeApiService
) : GetEpisodesByNameUseCase {

    override suspend fun invoke(text: String, page: Int): Result<Flow<EpisodesResponse>> {
        return Result.success(flowOf(apiService.getEpisodesByName(page = page, name = text)))
    }

}