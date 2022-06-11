package com.wiki.f_detail_episode

import com.wiki.cf_core.base.EffectScreen
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.CharacterDto

class DetailEpisodeScreenFeature {

    sealed class Events : EventScreen {
        data class OnCharacterClick(val character: CharacterDto) : Events()
    }

    sealed class Effects : EffectScreen {
        data class OnNavigateToCharacter(val character: CharacterDto) : Effects()
    }

    data class State(
        val name: String,
        val releaseDate: String,
        val shortName: String,
        val characters: List<CharacterDto> = emptyList()
    ) : StateScreen

}