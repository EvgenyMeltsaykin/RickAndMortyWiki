package com.wiki.f_list_character

import android.view.View
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.navigation.SharedElementFragment
import com.wiki.cf_extensions.pagination
import com.wiki.cf_ui.controllers.MenuItem
import com.wiki.cf_ui.controllers.MenuType
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.ToolbarConfig
import com.wiki.f_general_adapter.CharacterAdapter
import com.wiki.f_list_character.databinding.FragmentCharacterListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterListFragment : BaseFragment<
    FragmentCharacterListBinding,
    CharacterListEvents,
    CharacterListState,
    CharacterListViewModel
    >(), SharedElementFragment {

    override val viewModel: CharacterListViewModel by viewModel()
    override var sharedView: View? = null
    private val characterAdapter: CharacterAdapter =
        CharacterAdapter(
            onCharacterClick = { character, view ->
                sharedView = view
                viewModel.onCharacterClick(character)
            },
            onPreviewLoaded = {
                startPostponedEnterTransition()
            }
        )

    override fun renderState(state: CharacterListState) {
        characterAdapter.submitListAndSaveState(state.characters, binding.rvCharacter)
        with(binding) {
            refresh.isRefreshing = state.isLoading
        }
    }

    override fun initView(initialState: CharacterListState) {
        postponeEnterTransition()
        with(binding) {
            rvCharacter.adapter = characterAdapter
            rvCharacter.pagination(
                loadThreshold = 5,
                loadNextPage = { viewModel.loadNextPage() }
            )
            refresh.setOnRefreshListener {
                viewModel.onRefresh()
            }
        }
    }

    override fun bindEvents(event: CharacterListEvents) {
        when (event) {
            is CharacterListEvents.NavigateToDetailCharacter -> {
                router.navigateTo(screenProvider.DetailCharacter(event.character))
            }
            is CharacterListEvents.NavigateToSearch -> {
                router.navigateTo(screenProvider.Search(event.feature))
            }
        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = true,
                isVisibleToolbar = true,
                toolbarConfig = ToolbarConfig(
                    title = getString(R.string.characters_toolbar_title),
                    menuItem = listOf(
                        MenuItem(
                            menuType = MenuType.SEARCH,
                            clickListener = { viewModel.onSearchClick() }
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