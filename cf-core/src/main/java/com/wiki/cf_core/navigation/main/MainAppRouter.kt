package com.wiki.cf_core.navigation.main

import com.wiki.cf_core.navigation.ChangeTab
import com.wiki.cf_core.navigation.TabKey
import com.wiki.cf_core.navigation.base.BaseAppRouter

class MainAppRouter :BaseAppRouter(){

    fun changeTab(tabKey: TabKey){
        println("1234 MainAppRouter tabKey $tabKey")
        executeCommands(ChangeTab(tabKey))
    }

}