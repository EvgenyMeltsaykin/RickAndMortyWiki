package com.wiki.f_detail_location

import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_data.CharacterDto

sealed class DetailLocationEvents : EventScreen {
    data class OnNavigateToCharacter(val character: CharacterDto) : DetailLocationEvents()
}