package com.wiki.f_detail_character

import com.wiki.cf_core.base.EffectScreen
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LifeStatus
import com.wiki.cf_data.common.SimpleData

class DetailCharacterScreenFeature {

    sealed class Events: EventScreen {
        data class OnEpisodeClick(val episodeDto: EpisodeDto): Events()
        object OnCloseClick:Events()
        object OnOriginLocationClick:Events()
        object OnLastKnownLocation:Events()
    }

    sealed class Effects : EffectScreen {
        object OnNavigateBack : Effects()
        data class NavigateToEpisode(val episode: EpisodeDto) : Effects()
        data class NavigateToLocation(val locationData: SimpleData) : Effects()
    }

    data class State(
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

}