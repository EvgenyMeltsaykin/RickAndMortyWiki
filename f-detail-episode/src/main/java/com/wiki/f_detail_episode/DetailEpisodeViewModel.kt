package com.wiki.f_detail_episode

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.navigation.FragmentRouter
import com.wiki.cf_core.navigation.routes.DetailCharacterRoute
import com.wiki.cf_data.EpisodeDto
import com.wiki.f_detail_episode.DetailEpisodeScreenFeature.*
import com.wiki.i_character.use_cases.GetCharactersByIdsUseCase

class DetailEpisodeViewModel(
    private val router: FragmentRouter,
    private val episode: EpisodeDto,
    private val getCharactersByIdsUseCase: GetCharactersByIdsUseCase
) : BaseViewModel<State, Actions, Events>(
    State(
        name = episode.name,
        releaseDate = episode.airDate,
        shortName = episode.shortEpisode
    )
) {

    init {
        launchInternetRequest {
            getCharactersByIdsUseCase(episode.charactersIds).collect { characters ->
                renderState { state.copy(characters = characters) }
            }
        }
    }

    override fun bindEvents(event: Events) {
        when (event) {
            is Events.OnCharacterClick -> onCharacterClick(event)
            is Events.OnBackClick -> router.back()
        }
    }

    private fun onCharacterClick(event: Events.OnCharacterClick) {
        val route = DetailCharacterRoute(event.character)
        router.navigateTo(route)
    }

}
