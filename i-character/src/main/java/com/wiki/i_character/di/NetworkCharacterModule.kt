package com.wiki.i_character.di

import com.wiki.i_character.CharactersApiService
import com.wiki.i_character.use_cases.GetAllCharactersUseCase
import com.wiki.i_character.use_cases.GetCharacterInfoUseCase
import com.wiki.i_character.use_cases.GetCharactersByIdsUseCase
import com.wiki.i_character.use_cases.GetCharactersByNameUseCase
import org.koin.dsl.module

val NetworkCharacterModule = module {
    single { CharactersApiService.create(okHttpClient = get()) }
    single { GetAllCharactersUseCase(get()) }
    single { GetCharacterInfoUseCase(get()) }
    single { GetCharactersByIdsUseCase(get()) }
    single { GetCharactersByNameUseCase(get()) }
}