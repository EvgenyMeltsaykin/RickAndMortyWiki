package com.wiki.f_character_list

import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.CharacterDto

data class CharacterListState(
    val characters: List<CharacterDto> = emptyList(),
    val isLoading: Boolean = true,
    val page: Int = 1,
    val endReached: Boolean = false,
    val isVisibleLastElementList: Boolean = true
) : StateScreen