package com.wiki.rickandmorty

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.navigation.TabKey
import com.wiki.cf_core.navigation.main.MainAppRouter
import com.wiki.cf_core.utils.StringProvider
import com.wiki.cf_core.utils.safePostDelay
import com.wiki.rickandmorty.MainActivityScreenFeature.*

class MainActivityViewModel(
    private val router: MainAppRouter,
    private val stringProvider: StringProvider
) : BaseViewModel<State, Actions, Events>(
    State(
        selectedTab = TabKey.CHARACTERS
    )
) {
    private var doubleBackToExitPressedOnce = false

    override fun bindEvents(event: Events) {
        when (event) {
            is Events.OnTabClick -> {
                router.changeTab(event.tabKey)
            }
            is Events.OnBackPress ->{
                if (doubleBackToExitPressedOnce){
                    router.finishChain()
                }else{
                    doubleBackToExitPressedOnce = true
                    showToast(stringProvider.getString(R.string.press_again_to_exit))
                }
                safePostDelay(2000){
                    doubleBackToExitPressedOnce = false
                }
            }

        }
    }

}