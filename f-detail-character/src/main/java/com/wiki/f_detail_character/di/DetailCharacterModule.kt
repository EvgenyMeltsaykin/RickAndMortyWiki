package com.wiki.f_detail_character.di

import com.wiki.f_detail_character.DetailCharacterFragment
import com.wiki.f_detail_character.DetailCharacterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val FeatureDetailCharacterModule = module {
    scope<DetailCharacterFragment> {
        viewModelOf(::DetailCharacterViewModel)
    }
}