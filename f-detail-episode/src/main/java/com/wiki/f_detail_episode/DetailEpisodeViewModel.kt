package com.wiki.f_detail_episode

import androidx.lifecycle.viewModelScope
import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.i_character.use_cases.GetCharactersByIdsUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailEpisodeViewModel(
    private val episode: EpisodeDto,
    private val getCharactersByIdsUseCase: GetCharactersByIdsUseCase
) : BaseViewModel<DetailEpisodeEvents, DetailEpisodeState>(
    DetailEpisodeState(
        name = episode.name,
        releaseDate = episode.airDate,
        shortName = episode.shortEpisode
    )
) {
    fun onCharacterClick(character: CharacterDto) {
        sendEvent(DetailEpisodeEvents.OnNavigateToCharacter(character))
    }

    init {
        viewModelScope.launch {
            getCharactersByIdsUseCase(episode.charactersIds).collect { characters ->
                _state.update {
                    it.copy(
                        characters = characters
                    )
                }
            }
        }
    }

}