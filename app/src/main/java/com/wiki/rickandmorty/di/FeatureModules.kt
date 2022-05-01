package com.wiki.rickandmorty.di

import com.wiki.f_character_list.di.FeatureCharacterListModule
import com.wiki.f_detail_character.di.FeatureDetailCharacterModule
import com.wiki.f_detail_episode.di.FeatureDetailEpisodeModule
import com.wiki.f_detail_location.di.FeatureDetailLocationModule
import com.wiki.f_episode_list.di.FeatureEpisodeListModule
import com.wiki.f_location_list.di.FeatureLocationListModule

val FeatureModules = listOf(
    FeatureCharacterListModule,
    FeatureDetailCharacterModule,
    FeatureEpisodeListModule,
    FeatureDetailEpisodeModule,
    FeatureLocationListModule,
    FeatureDetailLocationModule
)