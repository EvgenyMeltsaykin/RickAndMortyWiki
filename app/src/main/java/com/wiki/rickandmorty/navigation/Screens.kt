package com.wiki.rickandmorty.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.wiki.cf_core.navigation.NavigationTabFragment
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.cf_core.navigation.TabKey
import com.wiki.cf_core.navigation.base.BaseRoute
import com.wiki.cf_core.navigation.routes.*
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_data.common.SimpleData
import com.wiki.f_detail_character.DetailCharacterFragment
import com.wiki.f_detail_episode.DetailEpisodeFragment
import com.wiki.f_detail_location.DetailLocationFragment
import com.wiki.f_detail_location.DetailLocationViewModel
import com.wiki.f_list_character.CharacterListFragment
import com.wiki.f_list_episode.EpisodeListFragment
import com.wiki.f_list_location.LocationListFragment
import com.wiki.f_search.SearchFragment

object Screens : ScreenProvider {
    override fun byRoute(route: BaseRoute):FragmentScreen {
        return when(route){
            is DetailCharacterRoute -> {
                FragmentScreen { DetailCharacterFragment.newInstance(route) }
            }
            is DetailEpisodeRoute -> {
                FragmentScreen { DetailEpisodeFragment.newInstance(route) }
            }
            is DetailLocationRoute -> {
                FragmentScreen { DetailLocationFragment.newInstance(route) }
            }
            is CharacterListRoute ->{
                FragmentScreen { CharacterListFragment.newInstance(route) }
            }
            is EpisodeListRoute ->{
                FragmentScreen { EpisodeListFragment.newInstance(route) }
            }
            is LocationListRoute ->{
                FragmentScreen { LocationListFragment.newInstance(route) }
            }
            is SearchRoute ->{
                FragmentScreen { SearchFragment.newInstance(route) }
            }
            else -> error("Not found fragment for route: ${route.javaClass.simpleName}")
        }
    }

    override fun TabContainer(tabKey: TabKey) = FragmentScreen {
        NavigationTabFragment.newInstance(tabKey)
    }

    override fun TabFragment(tabKey: TabKey) = when (tabKey) {
        TabKey.CHARACTERS -> byRoute(CharacterListRoute())
        TabKey.EPISODES -> byRoute(EpisodeListRoute())
        TabKey.LOCATIONS -> byRoute(LocationListRoute())
    }
}