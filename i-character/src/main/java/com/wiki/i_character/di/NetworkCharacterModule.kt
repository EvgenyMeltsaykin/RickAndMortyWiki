package com.wiki.i_character.di

import com.wiki.i_character.CharactersApiService
import com.wiki.i_character.use_cases.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val NetworkCharacterModule = module {
    single { CharactersApiService.create(okHttpClient = get()) }
    singleOf(::GetAllCharactersUseCase)
    singleOf(::GetCharacterInfoUseCase)
    singleOf(::GetCharactersByIdsUseCase)
    singleOf(::GetCharactersByNameUseCase)
}