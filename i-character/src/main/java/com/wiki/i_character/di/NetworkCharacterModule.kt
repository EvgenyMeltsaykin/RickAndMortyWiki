package com.wiki.i_character.di

import com.wiki.i_character.CharactersApiService
import com.wiki.i_character.use_cases.*
import org.koin.dsl.module

val networkCharacterModule = module {
    single { CharactersApiService.create(okHttpClient = get()) }
    single<GetAllCharactersUseCase> { GetAllCharactersUseCaseImpl(get()) }
    single<GetCharacterInfoUseCase> { GetCharacterInfoUseCaseImpl(get()) }
    single<GetCharactersByIdsUseCase> { GetCharactersByIdsUseCaseImpl(get()) }
}