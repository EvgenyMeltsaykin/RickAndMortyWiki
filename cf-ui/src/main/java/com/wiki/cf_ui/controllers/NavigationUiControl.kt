package com.wiki.cf_ui.controllers

interface NavigationUiControl {
    fun setNavigationUiConfig(config: NavigationUiConfig)
    fun getNavigationUiConfig(): NavigationUiConfig
}
