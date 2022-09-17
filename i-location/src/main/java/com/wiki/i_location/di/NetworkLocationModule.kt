package com.wiki.i_location.di

import com.wiki.i_location.LocationApiService
import com.wiki.i_location.use_cases.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val NetworkLocationModule = module {
    single { LocationApiService.create(okHttpClient = get()) }
    singleOf(::GetAllLocationsUseCase)
    singleOf(::GetLocationInfoUseCase)
    singleOf(::GetLocationsByIdsUseCase)
    singleOf(::GetLocationsByNameUseCase)
}