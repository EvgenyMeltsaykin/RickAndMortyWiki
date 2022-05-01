package com.wiki.f_episode_list.di

import com.wiki.f_episode_list.EpisodeListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val EpisodeListModule = module {
    viewModel { EpisodeListViewModel(get()) }
}