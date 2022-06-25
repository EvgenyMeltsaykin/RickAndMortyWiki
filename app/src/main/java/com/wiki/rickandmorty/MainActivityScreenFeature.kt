package com.wiki.rickandmorty

import com.wiki.cf_core.base.EffectScreen
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_core.navigation.TabKey

class MainActivityScreenFeature {

    sealed class Events : EventScreen {
        data class OnTabClick(val tabKey: TabKey) : Events()
    }

    sealed class Effects : EffectScreen {
    }

    data class State(
        val selectedTab:TabKey
    ) : StateScreen

}