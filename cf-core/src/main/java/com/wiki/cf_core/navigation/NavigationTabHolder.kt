package com.wiki.cf_core.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

class NavigationTabHolder {
    private val cicerones = HashMap<TabKeys, Cicerone<Router>>()
    fun getCicerone(key: TabKeys): Cicerone<Router> = cicerones.getOrPut(key) {
        Cicerone.create()
    }
}
