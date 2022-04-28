package com.wiki.f_detail_character

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.EpisodeDto

sealed class DetailCharacterEvents : EventScreen {
    object OnNavigateBack : DetailCharacterEvents()
    data class NavigateToEpisode(val episode: EpisodeDto) : DetailCharacterEvents()
}