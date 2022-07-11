package com.wiki.f_search.data

import com.wiki.cf_core.delegates.adapter.AdapterDelegateItem
import com.wiki.f_general_adapter.GeneralAdapterUi

sealed class SearchItemUi : AdapterDelegateItem {

    data class Header(val text: String) : SearchItemUi()

    data class Item(val generalItem: GeneralAdapterUi) : SearchItemUi()
}