package com.wiki.i_episode.use_cases

import com.wiki.cf_data.EpisodeDto
import com.wiki.i_episode.EpisodeApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetEpisodeInfoUseCase(
    private val apiService: EpisodeApiService
) {

    suspend operator fun invoke(id: String): Flow<EpisodeDto> {
        return flowOf(apiService.getEpisode(id).toEpisodeDto())
    }

}