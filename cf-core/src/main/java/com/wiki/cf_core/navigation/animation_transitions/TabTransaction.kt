package com.wiki.cf_core.navigation.animation_transitions

import androidx.fragment.app.Fragment
import com.google.android.material.transition.platform.MaterialSharedAxis

fun Fragment.tabTransaction() {
    exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
        duration = 300
    }
    enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        .apply {
            duration = 300
        }
    reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
        duration = 300
    }
}