package com.wiki.cf_core.extensions

import androidx.lifecycle.viewModelScope
import com.wiki.cf_core.base.ActionScreen
import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun <ViewStateFromScreen : StateScreen, ActionsFromScreen : ActionScreen, EventViewModel : EventScreen> BaseViewModel<ViewStateFromScreen, ActionsFromScreen, EventViewModel>.sendEvent(
    event: EventViewModel
) {
    val viewModel = this
    viewModel.viewModelScope.launch(Dispatchers.Main) {
        viewModel.eventChannel.send(event)
    }
}