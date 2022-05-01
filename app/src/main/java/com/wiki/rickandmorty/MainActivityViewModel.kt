package com.wiki.rickandmorty

import com.wiki.cf_core.base.BaseEventScreen
import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.base.StateScreen

object EmptyState : StateScreen

class MainActivityViewModel : BaseViewModel<BaseEventScreen, EmptyState>(
    EmptyState
) {

}