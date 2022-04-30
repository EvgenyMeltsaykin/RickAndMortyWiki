package com.wiki.cf_core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import com.github.terrakok.cicerone.Router
import com.wiki.cf_core.navigation.OnBackPressedListener
import com.wiki.cf_core.navigation.RouterProvider
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.cf_core.navigation.UiControl
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.NavigationUiControl
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

    private var _binding: VB? = null
    val binding get() = _binding!!

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
            bindBaseEvent()
        }
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            viewModel.eventFlow.collect {
                bindEvents(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(viewModel.state.value)
        bindNavigationUi()
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            viewModel.state.collect {
                renderState(it)
            }
        }
    }

    private suspend fun bindBaseEvent() {
        viewModel.baseEventFlow.collect { event ->
            when (event) {
                is BaseEventScreen.ShowToast -> {
                    Toast.makeText(context, event.text, Toast.LENGTH_SHORT).show()
                }
                is BaseEventScreen.ShowSnackBar -> {
                    //showSnackBar(context = context, text = event.text)
                }
            }
        }
    }

    private val navigationConfig: NavigationUiConfig
        get() = (requireActivity() as NavigationUiControl).getNavigationUiConfig()

    protected fun setNavigationUiConfig(navigationUiConfig: NavigationUiConfig) {
        (requireActivity() as NavigationUiControl).setNavigationUiConfig(navigationUiConfig)
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