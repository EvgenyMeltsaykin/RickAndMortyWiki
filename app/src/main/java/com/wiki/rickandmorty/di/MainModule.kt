package com.wiki.rickandmorty.di

import com.github.terrakok.cicerone.Cicerone
import com.wiki.cf_core.BaseScreenEventBus
import com.wiki.cf_core.navigation.NavigationTabHolder
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.rickandmorty.MainActivityViewModel
import com.wiki.rickandmorty.navigation.Screens
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MainModule = module {
    single { Cicerone.create() }
    single { NavigationTabHolder() }
    single<ScreenProvider> { Screens }
    viewModel { MainActivityViewModel() }
    single { BaseScreenEventBus() }
}