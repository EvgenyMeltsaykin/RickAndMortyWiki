package com.wiki.cf_network.util.pagination

import com.wiki.cf_network.NetworkException

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<Item>,
    private inline val getNextKey: suspend () -> Key,
    private inline val onError: suspend (NetworkException?) -> Unit,
    private inline val onSuccess: suspend (items: Item, newKey: Key, isRefresh: Boolean) -> Unit,
) : Paginator<Key, Item> {

    private var currentKey: Key = initialKey
    private var isMakingRequest: Boolean = false
    private var isRefreshing: Boolean = false
    override suspend fun loadNextItems() {
        try {
            if (isMakingRequest) return
            isMakingRequest = true
            onLoadUpdated(true)
            val result = onRequest(currentKey)
            isMakingRequest = false
            val items: Item = result.getOrElse {
                val networkException = (it as? NetworkException) ?: NetworkException.Other
                onError(networkException)
                onLoadUpdated(false)
                return
            }
            currentKey = getNextKey()
            onSuccess(items, currentKey, isRefreshing)
            isRefreshing = false
            onLoadUpdated(false)
        } catch (networkException: NetworkException) {
            onLoadUpdated(false)
            isMakingRequest = false
            throw networkException
        }
    }

    override fun reset() {
        isRefreshing = true
        currentKey = initialKey
    }
}