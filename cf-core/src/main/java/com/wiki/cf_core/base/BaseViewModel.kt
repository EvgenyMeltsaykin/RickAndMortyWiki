package com.wiki.cf_core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiki.cf_network.NetworkException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<EventsFromScreen : EventScreen, ViewStateFromScreen : StateScreen>(
    private val initialViewState: ViewStateFromScreen
) : ViewModel() {
    private val eventChanel = Channel<EventsFromScreen>()
    protected val _state: MutableStateFlow<ViewStateFromScreen> = MutableStateFlow(initialViewState)
    val eventFlow = eventChanel.receiveAsFlow()
    val state: StateFlow<ViewStateFromScreen>
        get() = _state
    private val baseEventChanel = Channel<BaseEventScreen>()
    val baseEventFlow = baseEventChanel.receiveAsFlow()

    fun sendEvent(event: EventsFromScreen) {
        viewModelScope.launch {
            eventChanel.send(event)
        }
    }

    fun showToast(text: String) {
        viewModelScope.launch {
            baseEventChanel.send(BaseEventScreen.ShowToast(text))
        }
    }

    fun showSnackBar(text: String?) {
        viewModelScope.launch {
            baseEventChanel.send(BaseEventScreen.ShowSnackBar(text))
        }
    }

    fun launchInternetRequest(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: NetworkException) {
                //showSnackBar(e.messageError)
            }
        }
    }
}