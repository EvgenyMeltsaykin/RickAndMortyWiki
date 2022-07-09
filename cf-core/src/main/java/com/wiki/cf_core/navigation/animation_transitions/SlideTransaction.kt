package com.wiki.cf_core.navigation.animation_transitions

import androidx.fragment.app.Fragment
import com.google.android.material.transition.platform.MaterialFade
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.google.android.material.transition.platform.MaterialSharedAxis

fun Fragment.slideTransaction() {
    exitTransition = MaterialFade().apply {
        duration = 200
    }
    enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
        duration = 500
    }
    reenterTransition = MaterialFadeThrough().apply {
        duration = 500
    }
    returnTransition = null
}