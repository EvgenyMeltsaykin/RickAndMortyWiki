package com.wiki.cf_core.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import com.wiki.cf_core.BaseScreenEventBus
import com.wiki.cf_core.controllers.InternetStateErrorController
import com.wiki.cf_core.navigation.RouterProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<
        VB : ViewBinding,
        ViewStateFromScreen : StateScreen,
        EffectsFromScreen : EffectScreen,
        EventsViewModel : EventScreen,
        ViewModel : BaseViewModel<ViewStateFromScreen, EffectsFromScreen, EventsViewModel>
        >: AppCompatActivity(),
    ViewModelProvider<ViewStateFromScreen, EffectsFromScreen, EventsViewModel, ViewModel>,
    ScreenBinder<EffectsFromScreen, ViewStateFromScreen>, InternetStateErrorController {

    var _binding: VB? = null
    val binding get() = _binding!!

    private val baseScreenEventBus by inject<BaseScreenEventBus>()

    protected suspend fun bindBaseEvent() {
        baseScreenEventBus.events.collect { event ->
            when (event) {
                is BaseEffectScreen.ShowToast -> {
                    Toast.makeText(this, event.text, Toast.LENGTH_SHORT).show()
                }
                is BaseEffectScreen.ShowSnackBar -> {
                    //showSnackBar(context = context, text = event.text)
                }

                is BaseEffectScreen.InternetError -> {
                    showInternetError(isVisible = event.isVisible, text = event.text)
                }
            }
        }
    }

    protected fun subscribeState() {
        viewModel.stateFlow.onEach {
            if (_binding != null) renderState(it)
        }.launchIn(lifecycleScope)
    }

    protected fun subscribeEffects() {
        viewModel.effectFlow.onEach {
            bindEffects(it)
        }.launchIn(lifecycleScope)
    }

    fun sendEvent(event: EventsViewModel) {
        viewModel.viewModelScope.launch(Dispatchers.Main) { viewModel.eventChannel.send(event) }
    }

}