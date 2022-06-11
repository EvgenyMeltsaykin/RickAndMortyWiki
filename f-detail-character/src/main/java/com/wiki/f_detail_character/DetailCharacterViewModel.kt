package com.wiki.f_detail_character

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_data.CharacterDto
import com.wiki.f_detail_character.DetailCharacterScreenFeature.*
import com.wiki.i_episode.use_cases.GetEpisodesByIdsUseCase

class DetailCharacterViewModel(
    private val character: CharacterDto,
    private val getEpisodesByIdsUseCase: GetEpisodesByIdsUseCase
) : BaseViewModel<State, Effects, Events>(
    State(
        name = character.name,
        imageUrl = character.imageUrl,
        gender = character.gender,
        lifeStatus = character.lifeStatus,
        species = character.species,
        lastKnownLocation = character.lastKnownLocation.value,
        originLocation = character.originLocation.value
    )
) {

    init {
        getCharacterInfo()
    }

    private fun getCharacterInfo() {
        launchInternetRequest {
            getEpisodesByIdsUseCase(character.episodeIds).collect { response ->
                val firstSeenInLocation: String = response.firstOrNull()?.name ?: "Unknown"
                setState(
                    state.copy(
                        firstSeenInEpisodeName = firstSeenInLocation,
                        episodes = response
                    )
                )
            }
        }
    }

    override fun bindEvents(event: Events) {
        when (event) {
            is Events.OnEpisodeClick -> setEffect {
                Effects.NavigateToEpisode(event.episodeDto)
            }
            is Events.OnCloseClick -> setEffect { Effects.OnNavigateBack }
            is Events.OnOriginLocationClick -> setEffect { onOriginLocationClick() }
            is Events.OnLastKnownLocation -> setEffect { onLastKnownLocation() }
        }
    }

    private fun onOriginLocationClick(): Effects? {
        if (character.originLocation.id.isEmpty()) return null
        return Effects.NavigateToLocation(character.originLocation)
    }

    private fun onLastKnownLocation(): Effects? {
        if (character.lastKnownLocation.id.isEmpty()) return null
        return Effects.NavigateToLocation(character.lastKnownLocation)
    }

}