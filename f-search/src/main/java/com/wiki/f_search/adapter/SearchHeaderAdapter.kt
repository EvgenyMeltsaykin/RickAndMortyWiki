package com.wiki.f_search.adapter

import com.wiki.cf_core.base.adapter.bindAdapter
import com.wiki.cf_core.delegates.adapter.AdapterDelegateItem
import com.wiki.f_search.data.SearchItemUi
import com.wiki.f_search.databinding.AdapterSearchHeaderBinding

fun getSearchHeaderAdapter() = bindAdapter<AdapterSearchHeaderBinding, AdapterDelegateItem, SearchItemUi.Header> {
    bind {
    }
}