package com.wiki.rickandmorty.di

import com.github.terrakok.cicerone.Cicerone
import com.wiki.cf_core.BaseScreenEventBus
import com.wiki.cf_core.navigation.NavigationTabHolder
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.cf_core.navigation.main.MainAppRouter
import com.wiki.cf_core.utils.StringProvider
import com.wiki.rickandmorty.MainActivityViewModel
import com.wiki.rickandmorty.navigation.Screens
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val MainModule = module {
    single(named("CiceroneMainApp")) { Cicerone.create(MainAppRouter(get())) }
    single(named("CiceroneApp")) { Cicerone.create() }
    single { get<Cicerone<MainAppRouter>>(named("CiceroneMainApp")).router }
    single { NavigationTabHolder(get()) }
    single<ScreenProvider> { Screens() }
    viewModel { MainActivityViewModel(get(), get()) }
    single { BaseScreenEventBus() }
    single { StringProvider(get()) }
}