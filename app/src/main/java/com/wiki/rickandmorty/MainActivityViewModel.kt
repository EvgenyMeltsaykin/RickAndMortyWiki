package com.wiki.rickandmorty

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.navigation.TabKey
import com.wiki.cf_core.navigation.main.MainAppRouter
import com.wiki.rickandmorty.MainActivityScreenFeature.*

class MainActivityViewModel(
    private val router:MainAppRouter
) : BaseViewModel<State,Effects, Events>(
    State(
        selectedTab = TabKey.CHARACTERS
    )
) {

    override fun bindEvents(event: Events) {
        when(event){
            is Events.OnTabClick ->{
                router.changeTab(event.tabKey)
            }
        }
    }

}