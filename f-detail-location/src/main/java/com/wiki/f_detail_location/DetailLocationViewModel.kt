package com.wiki.f_detail_location

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.common.SimpleData
import com.wiki.i_character.use_cases.GetCharactersByIdsUseCase
import com.wiki.i_location.use_cases.GetLocationInfoUseCase
import kotlinx.coroutines.flow.update

class DetailLocationViewModel(
    private val location: LocationDto?,
    private val locationData: SimpleData?,
    private val getCharactersByIdsUseCase: GetCharactersByIdsUseCase,
    private val getLocationInfoUseCase: GetLocationInfoUseCase
) : BaseViewModel<DetailLocationEvents, DetailLocationState>(
    DetailLocationState(
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
                _state.update {
                    it.copy(residentCharacters = characters)
                }
            }
        }
    }

    private fun getFullInfoLocation(locationId: String) {
        launchInternetRequest {
            getLocationInfoUseCase(locationId).collect { location ->
                _state.update { state ->
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

    fun onCharacterClick(character: CharacterDto) {
        sendEvent(DetailLocationEvents.OnNavigateToCharacter(character))
    }

}
