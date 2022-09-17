package com.wiki.i_episode.use_cases

import com.wiki.i_episode.EpisodeApiService
import com.wiki.i_episode.data.EpisodesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetAllEpisodesUseCase(
    private val apiService: EpisodeApiService
) {

    suspend operator fun invoke(page: Int): Result<Flow<EpisodesResponse>> {
        return Result.success(flowOf(apiService.getAllEpisodes(page)))
    }

}