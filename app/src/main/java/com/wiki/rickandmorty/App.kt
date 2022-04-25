package com.wiki.rickandmorty

import android.app.Application
import com.wiki.f_character_list.di.characterListModule
import com.wiki.i_character.di.networkCharacterModule
import com.wiki.i_episode.di.networkEpisodeModule
import com.wiki.rickandmorty.di.navigationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                navigationModule,
                networkEpisodeModule,
                networkCharacterModule,
                characterListModule
            )
        }

    }
}