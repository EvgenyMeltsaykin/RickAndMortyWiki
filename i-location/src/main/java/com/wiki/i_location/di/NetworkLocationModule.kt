package com.wiki.i_location.di

import com.wiki.i_location.LocationApiService
import com.wiki.i_location.use_cases.*
import org.koin.dsl.module

val NetworkLocationModule = module {
    single { LocationApiService.create(okHttpClient = get()) }
    single<GetAllLocationsUseCase> { GetAllLocationsUseCaseImpl(get()) }
    single<GetLocationInfoUseCase> { GetLocationInfoUseCaseImpl(get()) }
    single<GetLocationsByIdsUseCase> { GetLocationsByIdsUseCaseImpl(get()) }
    single<GetLocationsByNameUseCase> { GetLocationsByNameUseCase(get()) }
}