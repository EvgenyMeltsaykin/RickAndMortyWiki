package com.wiki.cf_core.navigation.main

import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.cf_core.navigation.TabKey
import com.wiki.cf_core.navigation.base.BaseAppRouter
import com.wiki.cf_core.navigation.commands.ChangeTab

class MainAppRouter(
    screenProvider: ScreenProvider
) : BaseAppRouter(screenProvider) {

    fun changeTab(tabKey: TabKey) {
        println("1234 MainAppRouter tabKey $tabKey")
        executeCommands(ChangeTab(tabKey))
    }

}