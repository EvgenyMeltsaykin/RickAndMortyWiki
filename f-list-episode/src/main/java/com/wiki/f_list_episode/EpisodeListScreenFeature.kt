package com.wiki.f_list_episode

import com.wiki.cf_core.base.EffectScreen
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.SearchFeature

class EpisodeListScreenFeature {

    sealed class Events : EventScreen {
        object LoadNextPage:Events()
        object OnRefresh:Events()
        object OnSearchClick:Events()
        data class OnEpisodeClick(val episode: EpisodeDto):Events()
    }

    sealed class Effects : EffectScreen {
        data class OnNavigateToEpisode(val episode: EpisodeDto) : Effects()
        data class NavigateToSearch(val feature: SearchFeature = SearchFeature.EPISODE) : Effects()
    }

    data class State(
        val episodes: List<EpisodeDto> = emptyList(),
        val isLoading: Boolean = true,
        val page: Int = 1,
        val endReached: Boolean = false,
    ) : StateScreen

}

