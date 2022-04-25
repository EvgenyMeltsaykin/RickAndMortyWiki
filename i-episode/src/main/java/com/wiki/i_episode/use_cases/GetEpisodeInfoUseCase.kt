package com.wiki.i_episode.use_cases

import com.wiki.cf_data.EpisodeDto
import com.wiki.i_episode.EpisodeApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GetEpisodeInfoUseCase {
    suspend operator fun invoke(id: String): Flow<EpisodeDto>
}

class GetEpisodeInfoUseCaseImpl(
    private val apiService: EpisodeApiService
) : GetEpisodeInfoUseCase {

    override suspend fun invoke(id: String): Flow<EpisodeDto> {
        return flowOf(apiService.getEpisode(id).toEpisodeDto())
    }

}