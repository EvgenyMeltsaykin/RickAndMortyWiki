package com.wiki.cf_core.navigation

import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import com.wiki.cf_core.R

enum class TabKey(@IdRes val menuRes: Int) {
    CHARACTERS(R.id.bottom_menu_item_characters),
    EPISODES(R.id.bottom_menu_item_episodes),
    LOCATIONS(R.id.bottom_menu_item_locations);

    companion object{
        fun getByMenuRes(@IdRes menuRes: Int):TabKey{
            return values().find { it.menuRes ==  menuRes } ?: error("Not found tab key")
        }
    }
}