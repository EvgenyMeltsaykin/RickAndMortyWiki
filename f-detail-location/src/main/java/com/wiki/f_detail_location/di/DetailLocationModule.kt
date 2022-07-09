package com.wiki.f_detail_location.di

import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.common.SimpleData
import com.wiki.f_detail_location.DetailLocationFragment
import com.wiki.f_detail_location.DetailLocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val FeatureDetailLocationModule = module {
    scope<DetailLocationFragment> {
        viewModel { (location: LocationDto, locationData: SimpleData) ->
            DetailLocationViewModel(
                location = location,
                locationData = locationData,
                getCharactersByIdsUseCase = get(),
                getLocationInfoUseCase = get(),
                router = get()
            )
        }
    }
}