package com.wiki.rickandmorty.navigation

import androidx.fragment.app.DialogFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.wiki.cf_core.navigation.NavigationTabFragment
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.cf_core.navigation.TabKey
import com.wiki.cf_core.navigation.base.route.DialogRoute
import com.wiki.cf_core.navigation.base.route.FragmentRoute
import com.wiki.cf_core.navigation.routes.*
import com.wiki.f_detail_character.DetailCharacterFragment
import com.wiki.f_detail_episode.DetailEpisodeFragment
import com.wiki.f_detail_location.DetailLocationFragment
import com.wiki.f_list_character.CharacterListFragment
import com.wiki.f_list_episode.EpisodeListFragment
import com.wiki.f_list_location.LocationListFragment
import com.wiki.f_search.SearchFragment

class Screens() : ScreenProvider {
    override fun byRoute(route: FragmentRoute): FragmentScreen {
        return when (route) {
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
            is LocationListRoute -> {
                FragmentScreen { LocationListFragment.newInstance(route) }
            }
            is SearchRoute -> {
                FragmentScreen { SearchFragment.newInstance(route) }
            }
            else -> error("Not found fragment for route: ${route.javaClass.simpleName}")
        }
    }

    override fun byRoute(route: DialogRoute): DialogFragment {
        return when (route) {
            else -> error("Not found dialog for route: ${route.javaClass.simpleName}")
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