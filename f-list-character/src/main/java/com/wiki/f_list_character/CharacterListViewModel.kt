package com.wiki.f_list_character

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.f_list_character.CharacterListScreenFeature.*
import com.wiki.i_character.use_cases.GetAllCharactersUseCase
import kotlinx.coroutines.flow.map

class CharacterListViewModel(
    private val getAllCharactersUseCase: GetAllCharactersUseCase
) : BaseViewModel<State, Effects, Events>(
    State()
) {

    private val pagination = DefaultPaginator(
        initialKey = state.page,
        onLoadUpdated = { isLoading ->
            setState(state.copy(isLoading = isLoading))
        },
        onRequest = { nextPage ->
            getAllCharactersUseCase(nextPage)
        },
        getNextKey = { state.page + 1 },
        onError = {
            showSnackBar(it?.messageError)
        },
        onSuccess = { items, newKey, isRefresh ->
            items.map { response ->
                setState(
                    state.copy(
                        endReached = response.info.next == null,
                        characters = if (isRefresh) emptyList() else state.characters
                    )
                )
                response.result.map { it.toCharacterDto() }
            }.collect { characters ->
                setState(
                    state.copy(
                        characters = state.characters + characters,
                        page = newKey,
                    )
                )
            }
        }
    )

    init {
        loadNextPage()
    }

    override fun bindEvents(event: Events) {
        when(event) {
            is Events.LoadNextPage -> loadNextPage()
            is Events.OnRefresh -> onRefresh()
            is Events.OnSearchClick -> setEffect {
                Effects.NavigateToSearch()
            }
            is Events.OnCharacterClick -> setEffect {
                Effects.NavigateToDetailCharacter(event.character)
            }
        }
    }

    private fun loadNextPage() {
        if (state.endReached) return
        launchInternetRequest {
            pagination.loadNextItems()
        }
    }

    private fun onRefresh() {
        setState(
            state.copy(
                page = 1,
                endReached = false
            )
        )
        pagination.reset()
        loadNextPage()
    }

}