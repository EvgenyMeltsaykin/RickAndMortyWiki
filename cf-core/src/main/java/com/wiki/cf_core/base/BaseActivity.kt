package com.wiki.cf_core.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.wiki.cf_core.BaseScreenEventBus
import com.wiki.cf_core.base.fragment.ScreenBinder
import com.wiki.cf_core.base.fragment.ViewModelProvider
import com.wiki.cf_core.controllers.InternetStateErrorController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

abstract class BaseActivity<
    VB : ViewBinding,
    ViewStateFromScreen : StateScreen,
    ActionsFromScreen : ActionScreen,
    EventsViewModel : EventScreen,
    ViewModel : BaseViewModel<ViewStateFromScreen, ActionsFromScreen, EventsViewModel>
    > : AppCompatActivity(),
    ViewModelProvider<ViewStateFromScreen, ActionsFromScreen, EventsViewModel, ViewModel>,
    ScreenBinder<ActionsFromScreen, ViewStateFromScreen>, InternetStateErrorController {

    var _binding: VB? = null
    val binding get() = _binding!!

    private val baseScreenEventBus by inject<BaseScreenEventBus>()

    protected suspend fun bindBaseEvent() {
        baseScreenEventBus.events.collect { event ->
            when (event) {
                is BaseActionScreen.ShowToast -> {
                    Toast.makeText(this, event.text, Toast.LENGTH_SHORT).show()
                }
                is BaseActionScreen.ShowSnackBar -> {
                    //showSnackBar(context = context, text = event.text)
                }

                is BaseActionScreen.InternetError -> {
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

    protected fun subscribeActions() {
        viewModel.actionFlow.onEach {
            bindActions(it)
        }.launchIn(lifecycleScope)
    }

}