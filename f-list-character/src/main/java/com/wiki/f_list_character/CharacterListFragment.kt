package com.wiki.f_list_character

import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.fragment.BaseFragment
import com.wiki.cf_core.delegates.adapter.AdapterDelegateItem
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_core.extensions.sendEvent
import com.wiki.cf_core.navigation.routes.CharacterListRoute
import com.wiki.cf_extensions.pagination
import com.wiki.f_general_adapter.GeneralAdapterUi
import com.wiki.f_general_adapter.getCharacterAdapter
import com.wiki.f_general_adapter.getGeneralAdaptersDiffCallback
import com.wiki.f_list_character.CharacterListScreenFeature.*
import com.wiki.f_list_character.databinding.FragmentCharacterListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterListFragment : BaseFragment<State, Actions, Events, CharacterListViewModel, CharacterListRoute>() {

    companion object {
        fun newInstance(route: CharacterListRoute): CharacterListFragment =
            CharacterListFragment().apply {
                this.route = route
            }
    }

    override val binding: FragmentCharacterListBinding by viewBinding(CreateMethod.INFLATE)
    override val viewModel: CharacterListViewModel by viewModel()

    private val characterAdapter = AsyncListDifferDelegationAdapter(
        getGeneralAdaptersDiffCallback(),
        AdapterDelegatesManager<List<AdapterDelegateItem>>()
            .addDelegate(
                getCharacterAdapter(
                    onCharacterClick = { character, _ ->
                        viewModel.sendEvent(Events.OnCharacterClick(character))
                    }
                )
            )
    )

    override fun renderState(state: State) {
        with(binding) {
            rvCharacter.performIfChanged(state.characters) { characters ->
                characterAdapter.items = characters.map { character ->
                    GeneralAdapterUi.Character(character)
                }
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
                    viewModel.sendEvent(Events.LoadNextPage)
                }
            )
            refresh.setOnRefreshListener {
                viewModel.sendEvent(Events.OnRefresh)
            }
            btnSearch.setOnClickListener {
                viewModel.sendEvent(Events.OnSearchClick)
            }
        }
    }

    override fun bindActions(action: Actions) {
        when (action) {

        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}