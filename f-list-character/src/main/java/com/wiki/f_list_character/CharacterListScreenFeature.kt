package com.wiki.f_list_character

import com.wiki.cf_core.base.EffectScreen
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.SearchFeature

class CharacterListScreenFeature {

    sealed class Events : EventScreen {
        object LoadNextPage:Events()
        object OnRefresh:Events()
        object OnSearchClick:Events()
        data class OnCharacterClick(val character: CharacterDto):Events()
    }

    sealed class Effects : EffectScreen {
        data class NavigateToDetailCharacter(val character: CharacterDto) : Effects()
        data class NavigateToSearch(val feature: SearchFeature = SearchFeature.CHARACTER) :
            Effects()
    }

    data class State(
        val characters: List<CharacterDto> = emptyList(),
        val isLoading: Boolean = true,
        val page: Int = 1,
        val endReached: Boolean = false
    ) : StateScreen

}