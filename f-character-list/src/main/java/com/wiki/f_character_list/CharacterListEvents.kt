package com.wiki.f_character_list

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.CharacterDto

sealed class CharacterListEvents : EventScreen {
    data class NavigateToDetailCharacter(val character: CharacterDto) : CharacterListEvents()
}