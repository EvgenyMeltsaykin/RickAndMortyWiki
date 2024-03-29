package com.wiki.f_list_character.di

import com.wiki.f_list_character.CharacterListFragment
import com.wiki.f_list_character.CharacterListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val FeatureCharacterListModule = module(createdAtStart = true) {
    scope<CharacterListFragment> {
        viewModelOf(::CharacterListViewModel)
    }
}