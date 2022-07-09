package com.wiki.cf_core.base

interface ActionScreen

sealed class BaseActionScreen : ActionScreen {
    data class ShowToast(val text: String) : BaseActionScreen()
    data class ShowSnackBar(val text: String?) : BaseActionScreen()
    data class InternetError(val isVisible: Boolean, val text: String = "") : BaseActionScreen()
}