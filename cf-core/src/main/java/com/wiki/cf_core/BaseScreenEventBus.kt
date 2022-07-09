package com.wiki.cf_core

import com.wiki.cf_core.base.BaseActionScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext

class BaseScreenEventBus {
    private val _events = MutableSharedFlow<BaseActionScreen>()
    val events = _events.asSharedFlow()

    suspend fun invokeEvent(event: BaseActionScreen) = withContext(Dispatchers.Main) {
        _events.emit(event)
    }
}