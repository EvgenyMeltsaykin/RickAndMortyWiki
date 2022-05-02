package com.wiki.f_search.di

import com.wiki.cf_data.SearchFeature
import com.wiki.f_search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val FeatureSearchModule = module {
    viewModel { (feature: SearchFeature) -> SearchViewModel(feature = feature, get(), get(), get()) }
}