package com.wiki.f_character_list

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.extensions.listenerLastElementVisible
import com.wiki.cf_core.extensions.pagination
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.f_character_list.adapter.CharacterAdapter
import com.wiki.f_character_list.databinding.FragmentCharacterListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterListFragment : BaseFragment<
    FragmentCharacterListBinding,
    CharacterListEvents,
    CharacterListState,
    CharacterListViewModel
    >() {

    override val viewModel: CharacterListViewModel by viewModel()
    private val characterAdapter: CharacterAdapter = CharacterAdapter(
        onCharacterClick = { viewModel.onCharacterClick(it) }
    )

    override fun initView() {
        with(binding) {
            rvCharacter.adapter = characterAdapter
            rvCharacter.pagination(
                loadThreshold = 5,
                loadNextPage = { viewModel.loadNextPage() }
            )
            rvCharacter.listenerLastElementVisible {
                viewModel.onLastElementVisible(it)
            }
            Glide.with(requireContext())
                .load(com.wiki.cf_ui.R.drawable.portal_animation)
                .into(loader)
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