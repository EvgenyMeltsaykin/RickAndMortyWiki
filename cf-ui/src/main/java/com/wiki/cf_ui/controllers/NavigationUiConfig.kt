package com.wiki.cf_ui.controllers

import androidx.annotation.ColorRes
import com.wiki.cf_ui.R


data class MenuItem(
    val menuType: MenuType,
    val clickListener: () -> Unit
)

enum class MenuType {
    SEARCH
}

sealed class ToolbarType() {
    object Simple : ToolbarType()
    data class Search(val onTextChange: (String) -> Unit, val hint: String) : ToolbarType()
}

fun ToolbarType.isSimple(): Boolean = this is ToolbarType.Simple
fun ToolbarType.isSearch(): Boolean = this is ToolbarType.Search

data class ToolbarConfig(
    val toolbarType: ToolbarType = ToolbarType.Simple,
    val title: String = "",
    val isTextAllCaps: Boolean = false,
    val menuItem: List<MenuItem> = emptyList()
)

data class NavigationUiConfig(
    val isVisibleBottomNavigation: Boolean = false,
    val isVisibleToolbar: Boolean = false,
    val toolbarConfig: ToolbarConfig = ToolbarConfig(),
    @ColorRes val colorStatusBar: Int = R.color.white,
    @ColorRes val colorBackground: Int = colorStatusBar,
)
