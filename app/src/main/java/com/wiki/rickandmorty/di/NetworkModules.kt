package com.wiki.rickandmorty.di

import com.wiki.cf_network.di.NetworkMainModule
import com.wiki.i_character.di.NetworkCharacterModule
import com.wiki.i_episode.di.NetworkEpisodeModule
import com.wiki.i_location.di.NetworkLocationModule

val NetworkModules = listOf(
    NetworkMainModule,
    NetworkEpisodeModule,
    NetworkCharacterModule,
    NetworkLocationModule,
)