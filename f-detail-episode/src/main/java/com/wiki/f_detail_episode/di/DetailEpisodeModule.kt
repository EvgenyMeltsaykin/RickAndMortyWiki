package com.wiki.f_detail_episode.di

import com.wiki.f_detail_episode.DetailEpisodeFragment
import com.wiki.f_detail_episode.DetailEpisodeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val FeatureDetailEpisodeModule = module {
    scope<DetailEpisodeFragment> {
        viewModelOf(::DetailEpisodeViewModel)
    }
}