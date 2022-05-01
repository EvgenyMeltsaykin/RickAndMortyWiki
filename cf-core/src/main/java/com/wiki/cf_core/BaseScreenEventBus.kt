package com.wiki.cf_core

import com.wiki.cf_core.base.BaseEventScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class BaseScreenEventBus {
    private val _events = MutableSharedFlow<BaseEventScreen>()
    val events = _events.asSharedFlow()

    suspend fun invokeEvent(event: BaseEventScreen) = _events.emit(event)
}