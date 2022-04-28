package com.wiki.cf_network.di

import com.wiki.cf_network.HttpClient
import com.wiki.cf_network.interceptors.ConnectivityInterceptor
import com.wiki.cf_network.util.ConnectivityService
import com.wiki.cf_network.util.ConnectivityServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val mainNetworkModule: Module = module(createdAtStart = true) {
    single { HttpClient.createHttpClient(connectivityInterceptor = get()) }
    single<ConnectivityService> { ConnectivityServiceImpl(get()) }
    single { ConnectivityInterceptor(get()) }
}