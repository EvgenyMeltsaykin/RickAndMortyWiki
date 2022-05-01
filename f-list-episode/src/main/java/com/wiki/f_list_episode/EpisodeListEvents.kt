package com.wiki.f_list_episode

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.EpisodeDto

sealed class EpisodeListEvents : EventScreen {
    data class OnNavigateToEpisode(val episode: EpisodeDto) : EpisodeListEvents()
}