package com.wiki.cf_core.extensions

import android.graphics.Color

fun Color.getContrastColor(): Color {
    val y: Float = (299 * this.red() + 587 * this.green() + 114 * this.blue()) / 1000
    return if (y >= 128) Color.valueOf(Color.BLACK) else Color.valueOf(Color.WHITE)
}

fun Int.getContrastColor(): Int {
    val y = (299 * Color.red(this) + 587 * Color.green(this) + 114 * Color.blue(this)) / 1000
    return if (y >= 128) com.wiki.cf_ui.R.color.black else com.wiki.cf_ui.R.color.white
}


