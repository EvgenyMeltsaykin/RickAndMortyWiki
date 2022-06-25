package com.wiki.cf_core.utils

import android.content.Context

class StringProvider(
    private val context:Context
) {

    fun getString(resId: Int): String {
        return context.getString(resId)
    }

    fun getString(resId: Int, vararg formatArgs: Any?): String {
        return context.getString(resId,formatArgs)
    }
}