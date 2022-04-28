package com.wiki.cf_ui.controllers

import androidx.annotation.ColorRes
import com.wiki.cf_ui.R

interface StatusBarController {
    fun setStatusBarColor(@ColorRes color: Int = R.color.white)
}
