package com.wiki.f_episode_list

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.EpisodeDto

sealed class EpisodeListEvents : EventScreen {
    data class OnNavigateToEpisode(val episode: EpisodeDto) : EpisodeListEvents()
}