package com.wiki.rickandmorty.di

import com.wiki.f_detail_character.di.FeatureDetailCharacterModule
import com.wiki.f_detail_episode.di.FeatureDetailEpisodeModule
import com.wiki.f_detail_location.di.FeatureDetailLocationModule
import com.wiki.f_list_character.di.FeatureCharacterListModule
import com.wiki.f_list_episode.di.FeatureEpisodeListModule
import com.wiki.f_list_location.di.FeatureLocationListModule
import com.wiki.f_search.di.FeatureSearchModule

val FeatureModules = listOf(
    FeatureCharacterListModule,
    FeatureDetailCharacterModule,
    FeatureEpisodeListModule,
    FeatureDetailEpisodeModule,
    FeatureLocationListModule,
    FeatureDetailLocationModule,
    FeatureSearchModule
)