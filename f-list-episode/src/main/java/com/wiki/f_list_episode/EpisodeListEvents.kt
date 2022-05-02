package com.wiki.f_list_episode

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.SearchFeature

sealed class EpisodeListEvents : EventScreen {
    data class OnNavigateToEpisode(val episode: EpisodeDto) : EpisodeListEvents()
    data class NavigateToSearch(val feature: SearchFeature = SearchFeature.EPISODE) : EpisodeListEvents()
}