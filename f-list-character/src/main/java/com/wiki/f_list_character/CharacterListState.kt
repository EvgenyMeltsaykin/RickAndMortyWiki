package com.wiki.f_list_character

import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.CharacterDto

data class CharacterListState(
    val characters: List<CharacterDto> = emptyList(),
    val isLoading: Boolean = true,
    val page: Int = 1,
    val endReached: Boolean = false
) : StateScreen