package com.wiki.rickandmorty

import com.wiki.cf_core.base.BaseEffectScreen
import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen

object EmptyState : StateScreen

class MainActivityViewModel : BaseViewModel<EmptyState,BaseEffectScreen, EventScreen>(
    EmptyState
) {
    override fun bindEvents(event: EventScreen) { }
}