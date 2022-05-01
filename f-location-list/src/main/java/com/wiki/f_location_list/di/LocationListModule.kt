package com.wiki.f_location_list.di

import com.wiki.f_location_list.LocationListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val FeatureLocationListModule = module {
    viewModel { LocationListViewModel(get()) }
}