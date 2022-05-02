package com.wiki.cf_core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import com.github.terrakok.cicerone.Router
import com.google.android.material.transition.MaterialElevationScale
import com.wiki.cf_core.controllers.InternetStateErrorController
import com.wiki.cf_core.navigation.OnBackPressedListener
import com.wiki.cf_core.navigation.RouterProvider
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.cf_core.navigation.UiControl
import com.wiki.cf_network.util.ConnectivityService
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.NavigationUiControl
import com.wiki.cf_ui.controllers.SearchToolbarController
import com.wiki.cf_ui.controllers.isSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.lang.reflect.ParameterizedType

interface ViewModelProvider<
    EventsFromScreen : EventScreen,
    ViewStateFromScreen : StateScreen,
    VM : BaseViewModel<EventsFromScreen, ViewStateFromScreen>> {
    val viewModel: VM
}

interface ScreenBinder<EventFromScreen : EventScreen, State : StateScreen> {
    fun bindEvents(event: EventFromScreen)
    fun initView(initialState: State)
    fun renderState(state: State)
}

abstract class BaseFragment<
    VB : ViewBinding,
    EventsFromScreen : EventScreen,
    ViewStateFromScreen : StateScreen,
    VM : BaseViewModel<EventsFromScreen, ViewStateFromScreen>
    > : Fragment(),
    ViewModelProvider<EventsFromScreen, ViewStateFromScreen, VM>,
    ScreenBinder<EventsFromScreen, ViewStateFromScreen>,
    RouterProvider,
    OnBackPressedListener, UiControl {
    override val router: Router
        get() = (parentFragment as RouterProvider).router

    val screenProvider: ScreenProvider by inject()
    val connectivityService: ConnectivityService by inject()

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransitions()
    }

    private fun setTransitions() {
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
        enterTransition = MaterialElevationScale(true)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vbType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val vbClass = vbType as Class<VB>
        val method =
            vbClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        _binding = method.invoke(null, inflater, container, false) as VB
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            viewModel.eventFlow.collect {
                bindEvents(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindNavigationUi()
        showInternetError(isVisible = false)
        super.onViewCreated(view, savedInstanceState)
        initView(viewModel.state.value)
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            viewModel.state.collect {
                if (_binding != null) renderState(it)
            }
        }

        if (connectivityService.isOffline()) {
            startPostponedEnterTransition()
        }
    }

    private fun showInternetError(isVisible: Boolean, text: String = "") {
        (requireActivity() as? InternetStateErrorController)?.showInternetError(isVisible, text)
    }

    private val navigationConfig: NavigationUiConfig
        get() = (requireActivity() as NavigationUiControl).getNavigationUiConfig()

    protected fun setNavigationUiConfig(navigationUiConfig: NavigationUiConfig) {
        (requireActivity() as NavigationUiControl).setNavigationUiConfig(navigationUiConfig)
    }

    protected fun showSearchKeyboard() {
        if (!navigationConfig.toolbarConfig.toolbarType.isSearch()) return
        (requireActivity() as? SearchToolbarController)?.showKeyboard()
    }

    protected fun hideSearchKeyboard() {
        if (!navigationConfig.toolbarConfig.toolbarType.isSearch()) return
        (requireActivity() as? SearchToolbarController)?.hideKeyboard()
    }

    protected fun clearFocusSearchKeyboard() {
        if (!navigationConfig.toolbarConfig.toolbarType.isSearch()) return
        (requireActivity() as? SearchToolbarController)?.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (binding.root as ViewGroup).removeAllViews()
        _binding = null
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

}