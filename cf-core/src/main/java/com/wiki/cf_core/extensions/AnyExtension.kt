package com.wiki.cf_core.extensions

inline fun <reified T> Any.convertToList(): List<T>? {
    val list = this as List<*>
    if (list.firstOrNull() !is T) return null
    return list.map { it as T }
}

inline fun <reified T> Any.isNeededClass(): Boolean {
    val list = this as List<*>
    if (list.firstOrNull() !is T) return false
    return true
}
