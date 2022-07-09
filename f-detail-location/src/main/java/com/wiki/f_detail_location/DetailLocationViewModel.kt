package com.wiki.f_detail_location

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.navigation.FragmentRouter
import com.wiki.cf_core.navigation.routes.DetailCharacterRoute
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.common.SimpleData
import com.wiki.f_detail_location.DetailLocationScreenFeature.*
import com.wiki.i_character.use_cases.GetCharactersByIdsUseCase
import com.wiki.i_location.use_cases.GetLocationInfoUseCase

class DetailLocationViewModel(
    private val router: FragmentRouter,
    location: LocationDto?,
    locationData: SimpleData?,
    private val getCharactersByIdsUseCase: GetCharactersByIdsUseCase,
    private val getLocationInfoUseCase: GetLocationInfoUseCase
) : BaseViewModel<State, Actions, Events>(
    State(
        name = location?.name ?: locationData?.value ?: "",
        type = location?.type ?: "",
        dimension = location?.dimension ?: ""
    )
) {

    init {
        when {
            location != null -> getResidentCharacters(location.residentCharactersIds)
            locationData != null -> getFullInfoLocation(locationData.id)
        }
    }

    private fun getResidentCharacters(residentCharactersIds: List<String>) {
        launchInternetRequest {
            getCharactersByIdsUseCase(residentCharactersIds).collect { characters ->
                renderState {
                    state.copy(residentCharacters = characters)
                }
            }
        }
    }

    private fun getFullInfoLocation(locationId: String) {
        launchInternetRequest {
            getLocationInfoUseCase(locationId).collect { location ->
                renderState {
                    state.copy(
                        name = location.name,
                        dimension = location.dimension,
                        type = location.type
                    )
                }
                getResidentCharacters(location.residentCharactersIds)
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
