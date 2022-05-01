package com.wiki.f_detail_episode

import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.CharacterDto

data class DetailEpisodeState(
    val name: String,
    val releaseDate: String,
    val shortName: String,
    val characters: List<CharacterDto> = emptyList()
) : StateScreen