package com.wiki.cf_core.delegates.adapter

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class DelegateManager<T> : AdapterDelegatesManager<T>() {
    fun getDelegates(): SparseArrayCompat<AdapterDelegate<T>>? {
        return delegates
    }

    override fun addDelegate(delegate: AdapterDelegate<T>): DelegateManager<T> {
        return super.addDelegate(delegate) as DelegateManager<T>
    }
}

class DelegateAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val delegateManager: DelegateManager<List<T>>
) : AsyncListDifferDelegationAdapter<T>(diffCallback, delegateManager) {

    fun <B> getItemViewTypeByDelegateAdapter(delegateAdapter: AdapterDelegate<List<B>>): Int {
        val delegates = delegateManager.getDelegates() ?: error("Not found delegates")
        val delegatesCount: Int = delegates.size()

        for (i in 0 until delegatesCount) {
            delegates.valueAt(i)
            val delegate: AdapterDelegate<List<T>>? = delegates.valueAt(i)
            if (delegate == delegateAdapter) {
                return delegates.keyAt(i)
            }
        }
        error("Not found delegate adapter $delegateAdapter")
    }
}