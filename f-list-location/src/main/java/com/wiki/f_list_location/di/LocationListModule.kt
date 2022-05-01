package com.wiki.f_list_location.di

import com.wiki.f_list_location.LocationListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val FeatureLocationListModule = module {
    viewModel { LocationListViewModel(get()) }
}