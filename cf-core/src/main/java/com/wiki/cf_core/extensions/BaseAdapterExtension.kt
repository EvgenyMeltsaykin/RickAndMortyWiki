package com.wiki.cf_core.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewbinding.ViewBinding
import com.wiki.cf_core.base.adapter.BaseAdapter

val <AdapterItem, VB : ViewBinding> BaseAdapter<AdapterItem, VB>.context: Context get() = itemView.context

fun <AdapterItem, VB : ViewBinding> BaseAdapter<AdapterItem, VB>.getString(
    @StringRes id: Int,
    vararg args: Any?
): String {
    return itemView.resources.getString(id, *args)
}

fun <AdapterItem, VB : ViewBinding> BaseAdapter<AdapterItem, VB>.getString(@StringRes id: Int): String {
    return itemView.resources.getString(id)
}

fun <AdapterItem, VB : ViewBinding> BaseAdapter<AdapterItem, VB>.getColor(@ColorRes id: Int): Int {
    return context.getColor(id)
}

fun <AdapterItem, VB : ViewBinding> BaseAdapter<AdapterItem, VB>.getDrawable(@DrawableRes id: Int): Drawable? {
    return AppCompatResources.getDrawable(context, id)
}

