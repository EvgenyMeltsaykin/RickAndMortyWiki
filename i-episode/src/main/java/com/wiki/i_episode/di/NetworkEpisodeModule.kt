package com.wiki.i_episode.di

import com.wiki.i_episode.EpisodeApiService
import com.wiki.i_episode.use_cases.*
import org.koin.dsl.module

val networkEpisodeModule = module {
    single { EpisodeApiService.create(okHttpClient = get()) }
    single<GetAllEpisodesUseCase> { GetAllEpisodesUseCaseImpl(get()) }
    single<GetEpisodeInfoUseCase> { GetEpisodeInfoUseCaseImpl(get()) }
    single<GetEpisodesByIdsUseCase> { GetEpisodesByIdsUseCaseImpl(get()) }
}