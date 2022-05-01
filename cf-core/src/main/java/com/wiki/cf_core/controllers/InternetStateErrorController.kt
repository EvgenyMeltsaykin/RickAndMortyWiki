package com.wiki.cf_core.controllers

interface InternetStateErrorController {
    fun showInternetError(isVisible: Boolean, text: String = "")
}