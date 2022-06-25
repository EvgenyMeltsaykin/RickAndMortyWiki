package com.wiki.cf_core.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_data.common.SimpleData

interface ScreenProvider {
    fun Characters(): FragmentScreen
    fun DetailCharacter(character: CharacterDto): FragmentScreen
    fun Episodes(): FragmentScreen
    fun DetailEpisode(episode: EpisodeDto): FragmentScreen
    fun Locations(): FragmentScreen
    fun DetailLocation(location: LocationDto?, locationData: SimpleData? = null): FragmentScreen
    fun Search(feature: SearchFeature): FragmentScreen
    fun TabContainer(tabKey: TabKey): FragmentScreen
    fun TabFragment(tabKey: TabKey): FragmentScreen
}
