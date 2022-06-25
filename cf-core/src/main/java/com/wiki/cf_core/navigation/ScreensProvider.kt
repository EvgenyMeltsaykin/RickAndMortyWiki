package com.wiki.cf_core.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.wiki.cf_core.navigation.base.BaseRoute
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_data.common.SimpleData

interface ScreenProvider {
    fun TabContainer(tabKey: TabKey): FragmentScreen
    fun TabFragment(tabKey: TabKey): FragmentScreen

    fun byRoute(route:BaseRoute):FragmentScreen
}
