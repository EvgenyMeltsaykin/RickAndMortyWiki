package com.wiki.rickandmorty

import android.app.Application
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.android.AppReducer
import com.github.terrakok.modo.backToLocalRoot
import com.wiki.rickandmorty.di.FeatureModules
import com.wiki.rickandmorty.di.MainModule
import com.wiki.rickandmorty.di.NetworkModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    companion object {
        lateinit var modo: Modo
            private set
    }

    private val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

    override fun onCreate() {
        super.onCreate()
        modo = Modo(AppReducer(this))
        modo.backToLocalRoot()
        router.setResultListener()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                listOf(MainModule) +
                    NetworkModules +
                    FeatureModules,
            )
        }
    }
}