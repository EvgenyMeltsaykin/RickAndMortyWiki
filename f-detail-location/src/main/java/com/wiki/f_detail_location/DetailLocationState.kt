package com.wiki.f_detail_location

import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.CharacterDto

data class DetailLocationState(
    val name: String,
    val type: String = "",
    val dimension: String = "",
    val residentCharacters: List<CharacterDto> = emptyList()
) : StateScreen