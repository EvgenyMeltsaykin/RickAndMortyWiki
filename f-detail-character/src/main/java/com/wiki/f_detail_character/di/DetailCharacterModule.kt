package com.wiki.f_detail_character.di

import com.wiki.cf_data.CharacterDto
import com.wiki.f_detail_character.DetailCharacterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailCharacterModule = module {
    viewModel { (character: CharacterDto) -> DetailCharacterViewModel(character = character, get()) }
}