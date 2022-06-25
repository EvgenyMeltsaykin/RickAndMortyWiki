package com.wiki.cf_core.utils

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

fun View.safePostDelay(
    duration: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(duration)
        block()
    }
}

fun ViewModel.safePostDelay(
    duration: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
):Job = viewModelScope.launch(dispatcher) {
    delay(duration)
    block()
}
