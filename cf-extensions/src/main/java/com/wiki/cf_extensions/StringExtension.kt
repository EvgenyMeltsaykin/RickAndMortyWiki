package com.wiki.cf_extensions

import java.util.*

fun String.capitalize(): String {
    return try {
        replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    } catch (e: Exception) {
        this
    }

}