package com.wiki.f_list_character

import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_extensions.pagination
import com.wiki.cf_ui.controllers.MenuItem
import com.wiki.cf_ui.controllers.MenuType
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.ToolbarConfig
import com.wiki.f_general_adapter.CharacterAdapter
import com.wiki.f_list_character.CharacterListScreenFeature.*
import com.wiki.f_list_character.databinding.FragmentCharacterListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterListFragment :
    BaseFragment<FragmentCharacterListBinding, State, Effects, Events, CharacterListViewModel>() {

    override val viewModel: CharacterListViewModel by viewModel()
    private val characterAdapter: CharacterAdapter =
        CharacterAdapter(
            onCharacterClick = { character, _ ->
                sendEvent(Events.OnCharacterClick(character))
            },
            onPreviewLoaded = {}
        )

    override fun renderState(state: State) {
        with(binding) {
            rvCharacter.performIfChanged(state.characters) {
                characterAdapter.submitListAndSaveState(it, rvCharacter)
            }
            refresh.performIfChanged(state.isLoading) {
                isRefreshing = it
            }
        }
    }

    override fun initView() {
        with(binding) {
            rvCharacter.adapter = characterAdapter
            rvCharacter.pagination(
                loadThreshold = 5,
                loadNextPage = {
                    sendEvent(Events.LoadNextPage)
                }
            )
            refresh.setOnRefreshListener {
                sendEvent(Events.OnRefresh)
            }
        }
    }

    override fun bindEffects(effect: Effects) {
        when (effect) {
            is Effects.NavigateToDetailCharacter -> {
                router.navigateTo(screenProvider.DetailCharacter(effect.character))
            }
            is Effects.NavigateToSearch -> {
                router.navigateTo(screenProvider.Search(effect.feature))
            }
        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = true,
                isVisibleToolbar = true,
                isVisibleBackButton = false,
                toolbarConfig = ToolbarConfig(
                    title = getString(R.string.characters_toolbar_title),
                    menuItem = listOf(
                        MenuItem(
                            menuType = MenuType.SEARCH,
                            clickListener = {
                                sendEvent(Events.OnSearchClick)
                            }
                        )
                    )
                )
            )
        )
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}