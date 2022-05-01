package com.wiki.f_character_list

import android.view.View
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.navigation.SharedElementFragment
import com.wiki.cf_extensions.listenerLastElementVisible
import com.wiki.cf_extensions.pagination
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.f_character_list.databinding.FragmentCharacterListBinding
import com.wiki.f_general_adapter.CharacterAdapter
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
            rvCharacter.listenerLastElementVisible {
                viewModel.onLastElementVisible(it)
            }
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
        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = true
            )
        )
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}