package com.wiki.f_character_list

import android.view.View
import androidx.core.view.isVisible
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.navigation.SharedElementFragment
import com.wiki.cf_extensions.listenerLastElementVisible
import com.wiki.cf_extensions.pagination
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.f_character_list.adapter.CharacterAdapter
import com.wiki.f_character_list.databinding.FragmentCharacterListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterListFragment : BaseFragment<
    FragmentCharacterListBinding,
    CharacterListEvents,
    CharacterListState,
    CharacterListViewModel
    >(), SharedElementFragment {

    override val viewModel: CharacterListViewModel by viewModel()
    override var sharedView: View? = null
    private val characterAdapter: CharacterAdapter = CharacterAdapter(
        onCharacterClick = { character, view ->
            sharedView = view
            viewModel.onCharacterClick(character)
        },
        onPreviewLoaded = {
            startPostponedEnterTransition()
        }
    )

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

        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = true
            )
        )
    }

    override fun renderState(state: CharacterListState) {
        characterAdapter.submitList(state.characters)
        binding.loader.isVisible = state.isLoading && state.isVisibleLastElementList
    }

    override fun bindEvents(event: CharacterListEvents) {
        when (event) {
            is CharacterListEvents.NavigateToDetailCharacter -> {
                router.navigateTo(screenProvider.DetailCharacter(event.character))
            }
        }
    }

}