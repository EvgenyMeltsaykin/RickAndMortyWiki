package com.wiki.cf_core.navigation.animation_transitions

import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialElevationScale

fun Fragment.simpleTransition(){
    exitTransition = MaterialElevationScale(false)
    reenterTransition = MaterialElevationScale(true)
    enterTransition = MaterialElevationScale(true)
}
