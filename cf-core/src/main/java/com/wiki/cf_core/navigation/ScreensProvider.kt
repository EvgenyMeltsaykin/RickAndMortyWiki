package com.wiki.cf_core.navigation

import androidx.fragment.app.DialogFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.wiki.cf_core.navigation.base.route.DialogRoute
import com.wiki.cf_core.navigation.base.route.FragmentRoute

interface ScreenProvider {
    fun TabContainer(tabKey: TabKey): FragmentScreen
    fun TabFragment(tabKey: TabKey): FragmentScreen

    fun byRoute(route: FragmentRoute): FragmentScreen
    fun byRoute(route: DialogRoute): DialogFragment
}
