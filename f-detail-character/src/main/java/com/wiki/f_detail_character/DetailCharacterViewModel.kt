package com.wiki.f_detail_character

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.i_episode.use_cases.GetEpisodesByIdsUseCase
import kotlinx.coroutines.flow.update

class DetailCharacterViewModel(
    private val character: CharacterDto,
    private val getEpisodesByIdsUseCase: GetEpisodesByIdsUseCase
) : BaseViewModel<DetailCharacterEvents, DetailCharacterState>(
    DetailCharacterState(
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
                _state.update {
                    it.copy(
                        firstSeenInEpisodeName = firstSeenInLocation,
                        episodes = response
                    )
                }
            }
        }
    }

    fun onEpisodeClick(episode: EpisodeDto) {
        sendEvent(DetailCharacterEvents.NavigateToEpisode(episode))
    }

    fun onCloseClick() {
        sendEvent(DetailCharacterEvents.OnNavigateBack)
    }

}