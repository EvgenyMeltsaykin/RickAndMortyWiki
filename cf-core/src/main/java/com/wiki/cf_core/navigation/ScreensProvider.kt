package com.wiki.cf_core.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen

interface ScreenProvider {
    fun Characters(): FragmentScreen
    fun TabContainer(tabKey: TabKeys): FragmentScreen
    fun TabFragment(tabKey: TabKeys): FragmentScreen
}
