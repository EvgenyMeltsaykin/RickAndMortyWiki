package com.wiki.f_list_episode

import com.wiki.cf_core.base.ActionScreen
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.EpisodeDto

class EpisodeListScreenFeature {

    sealed class Events : EventScreen {
        object LoadNextPage : Events()
        object OnRefresh : Events()
        object OnSearchClick : Events()
        data class OnEpisodeClick(val episode: EpisodeDto) : Events()
    }

    sealed class Actions : ActionScreen {

    }

    data class State(
        val episodes: List<EpisodeDto> = emptyList(),
        val isLoading: Boolean = true,
        val page: Int = 1,
        val endReached: Boolean = false,
    ) : StateScreen

}

