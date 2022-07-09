package com.wiki.f_detail_location

import com.wiki.cf_core.base.ActionScreen
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.CharacterDto

class DetailLocationScreenFeature {

    sealed class Events : EventScreen {
        data class OnCharacterClick(val character: CharacterDto) : Events()
    }

    sealed class Actions : ActionScreen {}

    data class State(
        val name: String,
        val type: String = "",
        val dimension: String = "",
        val residentCharacters: List<CharacterDto> = emptyList()
    ) : StateScreen

}