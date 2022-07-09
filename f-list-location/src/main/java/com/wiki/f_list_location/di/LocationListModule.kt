package com.wiki.f_list_location.di

import com.wiki.f_list_location.LocationListFragment
import com.wiki.f_list_location.LocationListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val FeatureLocationListModule = module {
    scope<LocationListFragment> {
        viewModelOf(::LocationListViewModel)
    }
}