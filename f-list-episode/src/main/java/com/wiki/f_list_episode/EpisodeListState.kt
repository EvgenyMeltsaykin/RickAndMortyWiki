package com.wiki.f_list_episode

import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.EpisodeDto

data class EpisodeListState(
    val episodes: List<EpisodeDto> = emptyList(),
    val isLoading: Boolean = true,
    val page: Int = 1,
    val endReached: Boolean = false,
    val isVisibleLastElementList: Boolean = true
) : StateScreen