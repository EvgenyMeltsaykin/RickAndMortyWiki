package com.wiki.cf_core.navigation.animation_transitions

import androidx.fragment.app.Fragment

fun Fragment.emptyTransaction() {
    exitTransition = null
    enterTransition = null
    reenterTransition = null
    returnTransition = null
}