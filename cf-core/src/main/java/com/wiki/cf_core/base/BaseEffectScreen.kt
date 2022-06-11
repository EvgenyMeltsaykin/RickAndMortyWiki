package com.wiki.cf_core.base

interface EffectScreen

sealed class BaseEffectScreen : EffectScreen {
    data class ShowToast(val text: String) : BaseEffectScreen()
    data class ShowSnackBar(val text: String?) : BaseEffectScreen()
    data class InternetError(val isVisible: Boolean, val text: String = "") : BaseEffectScreen()
}