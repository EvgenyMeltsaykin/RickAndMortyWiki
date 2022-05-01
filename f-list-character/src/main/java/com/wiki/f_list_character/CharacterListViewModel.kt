package com.wiki.f_list_character

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.i_character.use_cases.GetAllCharactersUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class CharacterListViewModel(
    private val getAllCharactersUseCase: GetAllCharactersUseCase
) : BaseViewModel<CharacterListEvents, CharacterListState>(
    CharacterListState()
) {

    private val pagination = DefaultPaginator(
        initialKey = state.value.page,
        onLoadUpdated = { isLoading ->
            _state.update {
                it.copy(isLoading = isLoading)
            }
        },
        onRequest = { nextPage ->
            getAllCharactersUseCase(nextPage)
        },
        getNextKey = { state.value.page + 1 },
        onError = {
            showSnackBar(it?.messageError)
        },
        onSuccess = { items, newKey, isRefresh ->
            items.map { response ->
                _state.update {
                    it.copy(
                        endReached = response.info.next == null,
                        characters = if (isRefresh) emptyList() else it.characters
                    )
                }
                response.result.map { it.toCharacterDto() }
            }.collect { characters ->
                _state.update {
                    it.copy(
                        characters = it.characters + characters,
                        page = newKey,
                    )
                }
            }
        }
    )

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        if (state.value.endReached) return
        launchInternetRequest {
            pagination.loadNextItems()
        }
    }

    fun onCharacterClick(character: CharacterDto) {
        sendEvent(CharacterListEvents.NavigateToDetailCharacter(character))
    }

    fun onRefresh() {
        _state.update {
            it.copy(endReached = false)
        }
        pagination.reset()
        loadNextPage()
    }

}