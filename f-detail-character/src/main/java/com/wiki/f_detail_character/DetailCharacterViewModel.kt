package com.wiki.f_detail_character

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.navigation.FragmentRouter
import com.wiki.cf_core.navigation.routes.DetailEpisodeRoute
import com.wiki.cf_core.navigation.routes.DetailLocationRoute
import com.wiki.cf_data.CharacterDto
import com.wiki.f_detail_character.DetailCharacterScreenFeature.*
import com.wiki.i_episode.use_cases.GetEpisodesByIdsUseCase

class DetailCharacterViewModel(
    private val router: FragmentRouter,
    private val character: CharacterDto,
    private val getEpisodesByIdsUseCase: GetEpisodesByIdsUseCase
) : BaseViewModel<State, Actions, Events>(
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
                renderState {
                    state.copy(
                        firstSeenInEpisodeName = firstSeenInLocation,
                        episodes = response
                    )
                }
            }
        }
    }

    override fun bindEvents(event: Events) {
        when (event) {
            is Events.OnEpisodeClick -> onEpisodeClick(event)
            is Events.OnCloseClick -> {
                router.back()
            }
            is Events.OnOriginLocationClick -> onOriginLocationClick()
            is Events.OnLastKnownLocation -> onLastKnownLocation()
        }
    }

    private fun onEpisodeClick(event: Events.OnEpisodeClick) {
        val route = DetailEpisodeRoute(event.episodeDto)
        router.navigateTo(route)
    }

    private fun onOriginLocationClick() {
        if (character.originLocation.id.isEmpty()) return
        val route = DetailLocationRoute(location = null, character.originLocation)
        router.navigateTo(route)
    }

    private fun onLastKnownLocation() {
        if (character.lastKnownLocation.id.isEmpty()) return
        val route = DetailLocationRoute(location = null, character.lastKnownLocation)
        router.navigateTo(route)
    }

}