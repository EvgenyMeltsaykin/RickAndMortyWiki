package com.wiki.cf_network.util.pagination

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}