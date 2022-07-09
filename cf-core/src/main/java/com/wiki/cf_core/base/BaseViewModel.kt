package com.wiki.cf_core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiki.cf_core.BaseScreenEventBus
import com.wiki.cf_core.navigation.ScreenProvider
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
    ActionsFromScreen : ActionScreen,
    EventViewModel : EventScreen
    >(
    private val initialViewState: ViewStateFromScreen
) : ViewModel(), KoinComponent {
    private val actionChanel = Channel<ActionsFromScreen>()
    val actionFlow = actionChanel.receiveAsFlow()
    val stateFlow: MutableStateFlow<ViewStateFromScreen> = MutableStateFlow(initialViewState)
    protected val state: ViewStateFromScreen
        get() = stateFlow.value
    val eventChannel = Channel<EventViewModel>()
    private val eventFlow = eventChannel.receiveAsFlow()

    private val baseScreenEventBus: BaseScreenEventBus by inject()
    private val connectivityService: ConnectivityService by inject()
    protected val screenProvider: ScreenProvider by inject()

    init {
        subscribeEvents()
        renderState { initialViewState }
    }

    private fun subscribeEvents() {
        eventFlow.onEach {
            bindEvents(it)
        }.launchIn(viewModelScope)
    }

    protected fun setAction(builder: () -> ActionsFromScreen?) {
        val action = builder() ?: return
        viewModelScope.launch { actionChanel.send(action) }
    }

    protected fun renderState(builder: () -> ViewStateFromScreen) {
        val newState = builder()
        println("1234 this $this newState $newState")
        stateFlow.update { newState }
    }

    fun showSnackBar(text: String?) {
        viewModelScope.launch {
            baseScreenEventBus.invokeEvent(BaseActionScreen.ShowSnackBar(text))
        }
    }

    fun showToast(text: String) {
        viewModelScope.launch {
            baseScreenEventBus.invokeEvent(BaseActionScreen.ShowToast(text))
        }
    }

    private var job: Job? = null

    fun launchInternetRequest(onNothingFoundError: (() -> Unit?)? = null, block: suspend () -> Unit): Job {
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
                if (connectivityService.isOffline()) throw NetworkException.NoConnectivity
                baseScreenEventBus.invokeEvent(BaseActionScreen.InternetError(false))
            } catch (e: CancellationException) {

            } catch (e: NetworkException) {
                when {
                    e is NetworkException.NothingFound && onNothingFoundError != null -> onNothingFoundError()
                    e is NetworkException.NothingFound -> {}
                    else -> baseScreenEventBus.invokeEvent(BaseActionScreen.InternetError(true, text = e.messageError))
                }
            }
        }
        return job as Job
    }

    abstract fun bindEvents(event:EventViewModel)
}