package com.wiki.rickandmorty.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.wiki.cf_core.navigation.NavigationTabFragment
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.cf_core.navigation.TabKeys
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.common.SimpleData
import com.wiki.f_character_list.CharacterListFragment
import com.wiki.f_detail_character.DetailCharacterFragment
import com.wiki.f_detail_episode.DetailEpisodeFragment
import com.wiki.f_detail_location.DetailLocationFragment
import com.wiki.f_episode_list.EpisodeListFragment
import com.wiki.f_location_list.LocationListFragment

object Screens : ScreenProvider {
    override fun Characters() = FragmentScreen { CharacterListFragment() }

    override fun DetailCharacter(character: CharacterDto) =
        FragmentScreen { DetailCharacterFragment.newInstance(character) }

    override fun Episodes() = FragmentScreen { EpisodeListFragment() }

    override fun DetailEpisode(episode: EpisodeDto) = FragmentScreen { DetailEpisodeFragment.newInstance(episode) }

    override fun Locations() = FragmentScreen { LocationListFragment() }

    override fun DetailLocation(location: LocationDto?, locationData: SimpleData?): FragmentScreen =
        FragmentScreen { DetailLocationFragment.newInstance(location, locationData) }

    override fun TabContainer(tabKey: TabKeys) = FragmentScreen {
        NavigationTabFragment.newInstance(tabKey)
    }

    override fun TabFragment(tabKey: TabKeys) = when (tabKey) {
        TabKeys.CHARACTERS -> Characters()
        TabKeys.EPISODES -> Episodes()
        TabKeys.LOCATIONS -> Locations()
    }
}