package com.wiki.cf_core.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

class NavigationTabHolder {
    private val cicerones = HashMap<TabKey, Cicerone<Router>>()
    fun getCicerone(key: TabKey): Cicerone<Router> = cicerones.getOrPut(key) {
        Cicerone.create()
    }
}
