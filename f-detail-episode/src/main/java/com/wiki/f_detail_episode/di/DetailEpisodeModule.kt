package com.wiki.f_detail_episode.di

import com.wiki.cf_data.EpisodeDto
import com.wiki.f_detail_episode.DetailEpisodeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val DetailEpisodeModule = module {
    viewModel { (episode: EpisodeDto) -> DetailEpisodeViewModel(episode = episode, get()) }
}