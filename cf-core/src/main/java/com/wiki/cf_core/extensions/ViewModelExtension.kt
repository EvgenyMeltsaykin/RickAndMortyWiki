package com.wiki.cf_core.extensions

import androidx.lifecycle.viewModelScope
import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.base.EffectScreen
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun <ViewStateFromScreen : StateScreen, EffectsFromScreen : EffectScreen, EventViewModel : EventScreen> BaseViewModel<ViewStateFromScreen, EffectsFromScreen, EventViewModel>
        .sendEvent(event: EventViewModel) {
    val viewModel = this
    viewModel.viewModelScope.launch(Dispatchers.Main) {
        viewModel.eventChannel.send(event)
    }
}