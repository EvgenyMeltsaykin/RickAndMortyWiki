package com.wiki.i_episode.use_cases

import com.wiki.cf_data.EpisodeDto
import com.wiki.i_episode.EpisodeApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetEpisodesByIdsUseCase(
    private val apiService: EpisodeApiService
) {

    suspend operator fun invoke(ids: List<String>): Flow<List<EpisodeDto>> {
        val idsString = ids.joinToString(separator = ",")
        return if (ids.size > 1) {
            flowOf(apiService.getEpisodes(idsString).map { it.toEpisodeDto() })
        } else {
            flowOf(listOf(apiService.getEpisode(idsString).toEpisodeDto()))
        }
    }

}