package com.wiki.f_list_episode.di

import com.wiki.f_list_episode.EpisodeListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val FeatureEpisodeListModule = module {
    viewModel { EpisodeListViewModel(get()) }
}