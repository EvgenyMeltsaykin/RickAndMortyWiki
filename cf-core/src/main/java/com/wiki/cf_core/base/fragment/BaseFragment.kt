package com.wiki.cf_core.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.wiki.cf_core.base.ActionScreen
import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_core.controllers.InternetStateErrorController
import com.wiki.cf_core.delegates.fragmentArgument
import com.wiki.cf_core.navigation.OnBackPressedListener
import com.wiki.cf_core.navigation.RouterProvider
import com.wiki.cf_core.navigation.UiControl
import com.wiki.cf_core.navigation.animation_transitions.TransitionType
import com.wiki.cf_core.navigation.animation_transitions.emptyTransition
import com.wiki.cf_core.navigation.animation_transitions.simpleTransition
import com.wiki.cf_core.navigation.base.route.BaseRoute
import com.wiki.cf_network.util.ConnectivityService
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.NavigationUiControl
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

interface ViewModelProvider<
    ViewStateFromScreen : StateScreen,
    ActionsFromScreen : ActionScreen,
    EventsViewModel : EventScreen,
    ViewModel : BaseViewModel<ViewStateFromScreen, ActionsFromScreen, EventsViewModel>> {
    val viewModel: ViewModel
}

interface ScreenBinder<ActionFromScreen : ActionScreen, State : StateScreen> {
    fun bindActions(action: ActionFromScreen)
    fun initView()
    fun renderState(state: State)
}

interface RouteProvider<Route : BaseRoute<*>> {
    var route: Route
}

abstract class BaseFragment<
    ViewStateFromScreen : StateScreen,
    ActionsFromScreen : ActionScreen,
    EventsViewModel : EventScreen,
    ViewModel : BaseViewModel<ViewStateFromScreen, ActionsFromScreen, EventsViewModel>,
    Route : BaseRoute<*>
    >() : BaseScopeFragment(),
    ViewModelProvider<ViewStateFromScreen, ActionsFromScreen, EventsViewModel, ViewModel>,
    ScreenBinder<ActionsFromScreen, ViewStateFromScreen>, RouterProvider,
    OnBackPressedListener, UiControl, RouteProvider<Route> {

    override var route: Route by fragmentArgument()

    abstract val binding: ViewBinding

    private val connectivityService: ConnectivityService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindNavigationUi()
        setTransitions()
    }

    private fun subscribeState() {
        viewModel.stateFlow.onEach {
            if (view != null) {
                renderState(it)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun subscribeActions() {
        viewModel.actionFlow.onEach {
            bindActions(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setTransitions() {
        when (route.routeConfig.animation) {
            TransitionType.SIMPLE -> simpleTransition()
            TransitionType.NONE -> emptyTransition()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeState()
        subscribeActions()
        initView()
        super.onViewCreated(view, savedInstanceState)
        showInternetError(isVisible = false)
        renderState(viewModel.stateFlow.value)
    }

    private fun showInternetError(isVisible: Boolean, text: String = "") {
        (requireActivity() as? InternetStateErrorController)?.showInternetError(isVisible, text)
    }

    private val navigationConfig: NavigationUiConfig
        get() = (requireActivity() as NavigationUiControl).getNavigationUiConfig()

    protected fun setNavigationUiConfig(navigationUiConfig: NavigationUiConfig) {
        (requireActivity() as NavigationUiControl).setNavigationUiConfig(navigationUiConfig)
    }

    override fun onBackPressed(): Boolean {
        router.back()
        return true
    }

}