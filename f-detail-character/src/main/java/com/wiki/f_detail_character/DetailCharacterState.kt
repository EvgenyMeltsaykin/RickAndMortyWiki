package com.wiki.f_detail_character

import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LifeStatus

data class DetailCharacterState(
    val name: String,
    val imageUrl: String,
    val gender: String,
    val lifeStatus: LifeStatus,
    val species: String,
    val originLocation: String,
    val lastKnownLocation: String,
    val firstSeenInEpisodeName: String = "",
    val episodes: List<EpisodeDto> = emptyList(),
) : StateScreen