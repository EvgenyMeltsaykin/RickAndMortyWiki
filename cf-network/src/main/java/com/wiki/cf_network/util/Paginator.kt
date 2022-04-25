package com.wiki.cf_network.util

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}