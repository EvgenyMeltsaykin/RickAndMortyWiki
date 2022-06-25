package com.wiki.cf_core.navigation.animation_transitions

import androidx.fragment.app.Fragment

fun Fragment.emptyTransition(){
    exitTransition = null
    reenterTransition = null
    enterTransition = null
}
