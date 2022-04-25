package com.wiki.i_episode.use_cases

import com.wiki.i_episode.EpisodeApiService
import com.wiki.i_episode.data.EpisodesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GetAllEpisodesUseCase {
    suspend operator fun invoke(page: Int = 0): Result<Flow<EpisodesResponse>>
}

class GetAllEpisodesUseCaseImpl(
    private val apiService: EpisodeApiService
) : GetAllEpisodesUseCase {

    override suspend fun invoke(page: Int): Result<Flow<EpisodesResponse>> {
        return Result.success(flowOf(apiService.getAllEpisodes(page)))
    }

}