package com.wiki.f_search.di

import com.wiki.f_search.SearchFragment
import com.wiki.f_search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val FeatureSearchModule = module {
    scope<SearchFragment> {
        viewModelOf(::SearchViewModel)
    }

}