package com.wiki.cf_ui.controllers

import androidx.annotation.ColorRes
import com.wiki.cf_ui.R

data class NavigationUiConfig(
    val isVisibleBottomNavigation: Boolean = false,
    @ColorRes val colorStatusBar: Int = R.color.white,
    @ColorRes val colorBackground: Int = colorStatusBar,
)
