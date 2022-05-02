package com.wiki.f_list_character

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.SearchFeature

sealed class CharacterListEvents : EventScreen {
    data class NavigateToDetailCharacter(val character: CharacterDto) : CharacterListEvents()
    data class NavigateToSearch(val feature: SearchFeature = SearchFeature.CHARACTER) : CharacterListEvents()
}