package com.wiki.rickandmorty.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.wiki.cf_core.navigation.NavigationTabFragment
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.cf_core.navigation.TabKeys
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_data.common.SimpleData
import com.wiki.f_detail_character.DetailCharacterFragment
import com.wiki.f_detail_episode.DetailEpisodeFragment
import com.wiki.f_detail_location.DetailLocationFragment
import com.wiki.f_list_character.CharacterListFragment
import com.wiki.f_list_episode.EpisodeListFragment
import com.wiki.f_list_location.LocationListFragment
import com.wiki.f_search.SearchFragment

object Screens : ScreenProvider {
    override fun Characters() = FragmentScreen { CharacterListFragment() }

    override fun DetailCharacter(character: CharacterDto) =
        FragmentScreen { DetailCharacterFragment.newInstance(character) }

    override fun Episodes() = FragmentScreen { EpisodeListFragment() }

    override fun DetailEpisode(episode: EpisodeDto) = FragmentScreen { DetailEpisodeFragment.newInstance(episode) }

    override fun Locations() = FragmentScreen { LocationListFragment() }

    override fun DetailLocation(location: LocationDto?, locationData: SimpleData?): FragmentScreen =
        FragmentScreen { DetailLocationFragment.newInstance(location, locationData) }

    override fun Search(feature: SearchFeature) = FragmentScreen { SearchFragment.newInstance(feature) }

    override fun TabContainer(tabKey: TabKeys) = FragmentScreen {
        NavigationTabFragment.newInstance(tabKey)
    }

    override fun TabFragment(tabKey: TabKeys) = when (tabKey) {
        TabKeys.CHARACTERS -> Characters()
        TabKeys.EPISODES -> Episodes()
        TabKeys.LOCATIONS -> Locations()
    }
}