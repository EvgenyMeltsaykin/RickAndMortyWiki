package com.wiki.cf_core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.lang.reflect.ParameterizedType

interface ViewModelProvider<
        ViewStateFromScreen : StateScreen,
        EffectsFromScreen : EffectScreen,
        EventsViewModel : EventScreen,
        ViewModel : BaseViewModel<ViewStateFromScreen, EffectsFromScreen, EventsViewModel>> {
    val viewModel: ViewModel
}

interface ScreenBinder<EffectFromScreen : EffectScreen, State : StateScreen> {
    fun bindEffects(effect: EffectFromScreen)
    fun initView()
    fun renderState(state: State)
}

enum class TransitionType {
    SIMPLE,
    NONE
}

abstract class BaseFragment<
        VB : ViewBinding,
        ViewStateFromScreen : StateScreen,
        EffectsFromScreen : EffectScreen,
        EventsViewModel : EventScreen,
        ViewModel : BaseViewModel<ViewStateFromScreen, EffectsFromScreen, EventsViewModel>
        >(private val transitionType: TransitionType = TransitionType.SIMPLE) : Fragment(),
    ViewModelProvider<ViewStateFromScreen, EffectsFromScreen, EventsViewModel, ViewModel>,
    ScreenBinder<EffectsFromScreen, ViewStateFromScreen>, RouterProvider,
    OnBackPressedListener, UiControl {

    override val router: Router get() = (parentFragment as RouterProvider).router

    protected val screenProvider: ScreenProvider by inject()
    private val connectivityService: ConnectivityService by inject()

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransitions()
    }

    private fun subscribeState() {
        viewModel.stateFlow.onEach {
            if (_binding != null) renderState(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun subscribeEffects() {
        viewModel.effectFlow.onEach {
            bindEffects(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setTransitions() {
        when (transitionType) {
            TransitionType.SIMPLE -> {
                exitTransition = MaterialElevationScale(false)
                reenterTransition = MaterialElevationScale(true)
                enterTransition = MaterialElevationScale(true)
            }
            TransitionType.NONE -> {
                exitTransition = null
                reenterTransition = null
                enterTransition = null
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindNavigationUi()
        val vbType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val vbClass = vbType as Class<VB>
        val method =
            vbClass.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
        _binding = method.invoke(null, inflater, container, false) as VB
        subscribeState()
        subscribeEffects()
        initView()
        renderState(viewModel.stateFlow.value)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showInternetError(isVisible = false)
        super.onViewCreated(view, savedInstanceState)
    }

    fun sendEvent(event: EventsViewModel) {
        viewModel.viewModelScope.launch(Dispatchers.Main) { viewModel.eventChannel.send(event) }
    }

    private fun showInternetError(isVisible: Boolean, text: String = "") {
        (requireActivity() as? InternetStateErrorController)?.showInternetError(isVisible, text)
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