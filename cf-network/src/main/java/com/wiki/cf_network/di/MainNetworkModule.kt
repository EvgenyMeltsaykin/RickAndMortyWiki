package com.wiki.cf_network.di

import com.wiki.cf_network.HttpClient
import com.wiki.cf_network.interceptors.ConnectivityInterceptor
import com.wiki.cf_network.interceptors.HttpErrorInterceptor
import com.wiki.cf_network.util.ConnectivityService
import com.wiki.cf_network.util.ConnectivityServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val NetworkMainModule: Module = module(createdAtStart = true) {
    single { HttpClient.createHttpClient(connectivityInterceptor = get(), httpErrorInterceptor = get()) }
    single<ConnectivityService> { ConnectivityServiceImpl(get()) }
    single { ConnectivityInterceptor(get()) }
    single { HttpErrorInterceptor() }
}