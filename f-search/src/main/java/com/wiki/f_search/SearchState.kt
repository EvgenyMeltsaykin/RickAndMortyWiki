package com.wiki.f_search

import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature

data class SearchState(
    val feature: SearchFeature,
    val characters: List<CharacterDto> = emptyList(),
    val episodes: List<EpisodeDto> = emptyList(),
    val locations: List<LocationDto> = emptyList(),
    val isLoading: Boolean = true,
    val page: Int = 1,
    val endReached: Boolean = false,
    val isVisibleNotFound: Boolean = false
) : StateScreen