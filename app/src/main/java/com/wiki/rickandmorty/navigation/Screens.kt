package com.wiki.rickandmorty.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.wiki.cf_core.navigation.NavigationTabFragment
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.cf_core.navigation.TabKeys
import com.wiki.f_character_list.CharacterListFragment

object Screens : ScreenProvider {
    override fun Characters() = FragmentScreen { CharacterListFragment() }

    override fun TabContainer(tabKey: TabKeys) = FragmentScreen {
        NavigationTabFragment.newInstance(tabKey)
    }

    override fun TabFragment(tabKey: TabKeys) = when (tabKey) {
        TabKeys.CHARACTERS -> Characters()
        TabKeys.EPISODES -> Characters()
        TabKeys.LOCATIONS -> Characters()
    }
}