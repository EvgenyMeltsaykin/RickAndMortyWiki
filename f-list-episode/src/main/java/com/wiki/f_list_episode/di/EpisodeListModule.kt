package com.wiki.f_list_episode.di

import com.wiki.f_list_episode.EpisodeListFragment
import com.wiki.f_list_episode.EpisodeListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val FeatureEpisodeListModule = module {
    scope<EpisodeListFragment> {
        viewModelOf(::EpisodeListViewModel)
    }
}