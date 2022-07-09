package com.wiki.cf_core.navigation.animation_transitions

import androidx.fragment.app.Fragment
import com.google.android.material.transition.platform.MaterialFadeThrough

fun Fragment.fadeTransaction() {
    exitTransition = MaterialFadeThrough()
    enterTransition = MaterialFadeThrough()
    reenterTransition = MaterialFadeThrough()
    returnTransition = MaterialFadeThrough()
}