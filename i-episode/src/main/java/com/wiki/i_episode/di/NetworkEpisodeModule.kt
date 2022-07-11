package com.wiki.i_episode.di

import com.wiki.i_episode.EpisodeApiService
import com.wiki.i_episode.use_cases.GetAllEpisodesUseCase
import com.wiki.i_episode.use_cases.GetEpisodeInfoUseCase
import com.wiki.i_episode.use_cases.GetEpisodesByIdsUseCase
import com.wiki.i_episode.use_cases.GetEpisodesByNameUseCase
import org.koin.dsl.module

val NetworkEpisodeModule = module {
    single { EpisodeApiService.create(okHttpClient = get()) }
    single { GetAllEpisodesUseCase(get()) }
    single { GetEpisodeInfoUseCase(get()) }
    single { GetEpisodesByIdsUseCase(get()) }
    single { GetEpisodesByNameUseCase(get()) }
}