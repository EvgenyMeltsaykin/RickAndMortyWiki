package com.wiki.cf_core.navigation.animation_transitions

import androidx.fragment.app.Fragment
import com.google.android.material.transition.platform.MaterialSharedAxis

fun Fragment.modalTransaction() {
    exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
        duration = 300
    }
    reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
        duration = 300
    }
    returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false).apply {
        duration = 300
    }
}