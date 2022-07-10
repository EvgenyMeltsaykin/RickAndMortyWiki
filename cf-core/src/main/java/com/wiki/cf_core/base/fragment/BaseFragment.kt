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
import com.wiki.cf_core.navigation.NavigationTabFragment
import com.wiki.cf_core.navigation.OnBackPressedListener
import com.wiki.cf_core.navigation.RouterProvider
import com.wiki.cf_core.navigation.animation_transitions.*
import com.wiki.cf_core.navigation.base.route.BaseRoute
import com.wiki.cf_network.util.ConnectivityService
import com.wiki.cf_ui.controllers.BottomNavigationController
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
    OnBackPressedListener, RouteProvider<Route> {

    override var route: Route by fragmentArgument()

    abstract val binding: ViewBinding

    private val connectivityService: ConnectivityService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setBottomNavigationBarVisible(route.routeConfig.isVisibleBottomNavigation)
        super.onCreate(savedInstanceState)
        setTransitions()
        (activity as? BottomNavigationController)?.setBottomNavigationBarVisible(route.routeConfig.isVisibleBottomNavigation)
    }

    override fun onResume() {
        if (isCurrentVisibleTabFragment()) {
            setBottomNavigationBarVisible(route.routeConfig.isVisibleBottomNavigation)
        }
        super.onResume()
    }

    private fun isCurrentVisibleTabFragment(): Boolean {
        return this.isVisible && parentFragment is NavigationTabFragment && parentFragment?.isVisible == true
    }

    private fun setBottomNavigationBarVisible(isVisible: Boolean) {
        (activity as? BottomNavigationController)?.setBottomNavigationBarVisible(isVisible)
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
            TransitionType.FADE -> fadeTransaction()
            TransitionType.MODAL -> modalTransaction()
            TransitionType.TAB -> tabTransaction()
            TransitionType.SLIDE -> slideTransaction()
            TransitionType.SIMPLE -> simpleTransaction()
            TransitionType.NONE -> emptyTransaction()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showInternetError(isVisible = false)
        subscribeState()
        subscribeActions()
        initView()
        super.onViewCreated(view, savedInstanceState)
        renderState(viewModel.stateFlow.value)
    }

    private fun showInternetError(isVisible: Boolean, text: String = "") {
        (requireActivity() as? InternetStateErrorController)?.showInternetError(isVisible, text)
    }

    override fun onBackPressed(): Boolean {
        router.back()
        return true
    }

}