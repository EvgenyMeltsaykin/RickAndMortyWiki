package com.wiki.f_search

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto

sealed class SearchEvents : EventScreen {
    data class OnCharacterClick(val character: CharacterDto) : SearchEvents()
    data class OnEpisodeClick(val episode: EpisodeDto) : SearchEvents()
    data class OnLocationClick(val location: LocationDto) : SearchEvents()
}