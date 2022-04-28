package com.wiki.f_character_list.di

import com.wiki.f_character_list.CharacterListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val characterListModule = module(createdAtStart = true) {
    viewModel { CharacterListViewModel(get()) }
}