package com.wiki.f_general_adapter

import com.wiki.cf_core.delegates.adapter.AdapterDelegateItem
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto

sealed class GeneralAdapterUi : AdapterDelegateItem {
    data class Character(val character: CharacterDto) : GeneralAdapterUi()
    data class Episode(val episode: EpisodeDto) : GeneralAdapterUi()
    data class Location(val location: LocationDto) : GeneralAdapterUi()
}
