package com.wiki.rickandmorty.di

import com.github.terrakok.cicerone.Cicerone
import com.wiki.cf_core.navigation.NavigationTabHolder
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.rickandmorty.navigation.Screens
import org.koin.dsl.module

val navigationModule = module {
    single { Cicerone.create() }
    single { NavigationTabHolder() }
    single<ScreenProvider> { Screens }
}