package com.wiki.cf_ui.extensions

import android.graphics.BlurMaskFilter
import android.graphics.MaskFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.style.MaskFilterSpan

fun String.blurMask(radius: Float, blurMaskFilter: BlurMaskFilter.Blur = BlurMaskFilter.Blur.NORMAL): SpannableString {
    val blurMask: MaskFilter = BlurMaskFilter(radius, blurMaskFilter)
    val string = SpannableString(this)
    string.setSpan(MaskFilterSpan(blurMask), 0, this.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return string
}