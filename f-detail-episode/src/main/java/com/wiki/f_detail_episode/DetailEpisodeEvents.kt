package com.wiki.f_detail_episode

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.CharacterDto

sealed class DetailEpisodeEvents : EventScreen {
    data class OnNavigateToCharacter(val character: CharacterDto) : DetailEpisodeEvents()
}