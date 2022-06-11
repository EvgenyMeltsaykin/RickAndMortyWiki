package com.wiki.cf_core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiki.cf_core.BaseScreenEventBus
import com.wiki.cf_network.NetworkException
import com.wiki.cf_network.util.ConnectivityService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseViewModel<
    ViewStateFromScreen : StateScreen,
    EffectsFromScreen : EffectScreen,
    EventViewModel : EventScreen
    >(
    initialViewState: ViewStateFromScreen
) : ViewModel(), KoinComponent {
    private val effectChanel = Channel<EffectsFromScreen>()
    val effectFlow = effectChanel.receiveAsFlow()
    val stateFlow: MutableStateFlow<ViewStateFromScreen> = MutableStateFlow(initialViewState)
    protected val state: ViewStateFromScreen
        get() = stateFlow.value
    val eventChannel = Channel<EventViewModel>()
    private val eventFlow = eventChannel.receiveAsFlow()

    private val baseScreenEventBus: BaseScreenEventBus by inject()
    private val connectivityService: ConnectivityService by inject()

    init {
        subscribeEvents()
        setState(initialViewState)
    }

    private fun subscribeEvents() {
        eventFlow.onEach {
            bindEvents(it)
        }.launchIn(viewModelScope)
    }

    protected fun setEffect(builder: () -> EffectsFromScreen?){
        val effect = builder() ?: return
        viewModelScope.launch{ effectChanel.send(effect)}
    }

    protected fun setState(newState:ViewStateFromScreen){
        stateFlow.update { newState }
    }

    fun showSnackBar(text: String?) {
        viewModelScope.launch {
            baseScreenEventBus.invokeEvent(BaseEffectScreen.ShowSnackBar(text))
        }
    }

    private var job: Job? = null

    fun launchInternetRequest(onNothingFoundError: (() -> Unit?)? = null, block: suspend () -> Unit): Job {
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                if (connectivityService.isOffline()) throw NetworkException.NoConnectivity
                block()
                baseScreenEventBus.invokeEvent(BaseEffectScreen.InternetError(false))
            } catch (e: CancellationException) {

            } catch (e: NetworkException) {
                when {
                    e is NetworkException.NothingFound && onNothingFoundError != null -> onNothingFoundError()
                    e is NetworkException.NothingFound -> {}
                    else -> baseScreenEventBus.invokeEvent(BaseEffectScreen.InternetError(true, text = e.messageError))
                }
            }
        }
        return job as Job
    }

    abstract fun bindEvents(event:EventViewModel)
}