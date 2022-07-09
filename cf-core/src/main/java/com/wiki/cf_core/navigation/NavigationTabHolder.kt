package com.wiki.cf_core.navigation

import com.github.terrakok.cicerone.Cicerone

class NavigationTabHolder(
    private val screenProvider: ScreenProvider
) {
    private val cicerones = HashMap<TabKey, Cicerone<FragmentRouter>>()
    fun getCicerone(key: TabKey): Cicerone<FragmentRouter> = cicerones.getOrPut(key) {
        Cicerone.create(FragmentRouter(screenProvider))
    }
}
