package com.wiki.cf_core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiki.cf_core.BaseScreenEventBus
import com.wiki.cf_network.NetworkException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseViewModel<EventsFromScreen : EventScreen, ViewStateFromScreen : StateScreen>(
    private val initialViewState: ViewStateFromScreen
) : ViewModel(), KoinComponent {
    private val eventChanel = Channel<EventsFromScreen>()
    protected val _state: MutableStateFlow<ViewStateFromScreen> = MutableStateFlow(initialViewState)
    val eventFlow = eventChanel.receiveAsFlow()
    val state: StateFlow<ViewStateFromScreen>
        get() = _state

    private val baseScreenEventBus: BaseScreenEventBus by inject()

    fun sendEvent(event: EventsFromScreen) {
        viewModelScope.launch {
            eventChanel.send(event)
        }
    }

    fun showSnackBar(text: String?) {
        viewModelScope.launch {
            baseScreenEventBus.invokeEvent(BaseEventScreen.ShowSnackBar(text))
        }
    }

    fun launchInternetRequest(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
                baseScreenEventBus.invokeEvent(BaseEventScreen.InternetError(false))
            } catch (e: NetworkException) {
                baseScreenEventBus.invokeEvent(BaseEventScreen.InternetError(true, text = e.messageError))
            }
        }
    }
}