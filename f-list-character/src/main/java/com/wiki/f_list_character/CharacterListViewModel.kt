package com.wiki.f_list_character

import com.wiki.cf_core.base.BaseViewModel
import com.wiki.cf_core.navigation.FragmentRouter
import com.wiki.cf_core.navigation.routes.DetailCharacterRoute
import com.wiki.cf_core.navigation.routes.SearchRoute
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_network.util.pagination.DefaultPaginator
import com.wiki.f_list_character.CharacterListScreenFeature.*
import com.wiki.i_character.use_cases.GetAllCharactersUseCase
import kotlinx.coroutines.flow.map

class CharacterListViewModel(
    private val router: FragmentRouter,
    private val getAllCharactersUseCase: GetAllCharactersUseCase
) : BaseViewModel<State, Actions, Events>(
    State()
) {

    private val pagination = DefaultPaginator(
        initialKey = state.page,
        onLoadUpdated = { isLoading ->
            renderState {
                state.copy(isLoading = isLoading)
            }
        },
        onRequest = { nextPage ->
            getAllCharactersUseCase(nextPage)
        },
        getNextKey = { state.page + 1 },
        onError = {
            renderState{
                state.copy(isLoading = false)
            }
            showSnackBar(it?.messageError)
        },
        onSuccess = { items, newKey, isRefresh ->
            items.map { response ->
                renderState {
                    state.copy(
                        endReached = response.info.next == null,
                        characters = if (isRefresh) emptyList() else state.characters
                    )
                }
                response.result.map { it.toCharacterDto() }
            }.collect { characters ->
                renderState {
                    state.copy(
                        characters = state.characters + characters,
                        page = newKey,
                    )
                }
            }
        }
    )

    init {
        loadNextPage()
    }

    override fun bindEvents(event: Events) {
        when (event) {
            is Events.LoadNextPage -> loadNextPage()
            is Events.OnRefresh -> onRefresh()
            is Events.OnSearchClick -> onSearchClick()
            is Events.OnCharacterClick -> onCharacterClick(event)
        }
    }

    private fun onSearchClick() {
        val route = SearchRoute(SearchFeature.ALL)
        router.navigateTo(route)
    }

    private fun onCharacterClick(event: Events.OnCharacterClick) {
        val route = DetailCharacterRoute(event.character)
        router.navigateTo(route)
    }

    private fun loadNextPage() {
        if (state.endReached) return
        launchInternetRequest {
            pagination.loadNextItems()
        }
    }

    private fun onRefresh() {
        renderState {
            state.copy(
                page = 1,
                endReached = false
            )
        }
        pagination.reset()
        loadNextPage()
    }

}