package com.wiki.f_detail_episode

import com.wiki.cf_core.base.ActionScreen
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.CharacterDto

class DetailEpisodeScreenFeature {

    sealed class Events : EventScreen {
        data class OnCharacterClick(val character: CharacterDto) : Events()
        object OnBackClick : Events()
    }

    sealed class Actions : ActionScreen {}

    data class State(
        val name: String,
        val releaseDate: String,
        val shortName: String,
        val characters: List<CharacterDto> = emptyList()
    ) : StateScreen

}