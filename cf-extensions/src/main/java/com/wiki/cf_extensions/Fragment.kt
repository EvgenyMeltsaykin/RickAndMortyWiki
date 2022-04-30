package com.wiki.cf_extensions

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment

fun Fragment.getDrawable(@DrawableRes resId: Int): Drawable? {
    return AppCompatResources.getDrawable(requireContext(), resId)
}