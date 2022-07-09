package com.wiki.f_detail_episode

import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.fragment.BaseFragment
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_core.extensions.sendEvent
import com.wiki.cf_core.navigation.routes.DetailEpisodeRoute
import com.wiki.f_detail_episode.DetailEpisodeScreenFeature.*
import com.wiki.f_detail_episode.databinding.FragmentDetailEpisodeBinding
import com.wiki.f_general_adapter.GeneralAdapterUi
import com.wiki.f_general_adapter.getCharacterAdapter
import com.wiki.f_general_adapter.getGeneralAdaptersDiffCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailEpisodeFragment : BaseFragment<State, Actions, Events, DetailEpisodeViewModel, DetailEpisodeRoute>() {

    companion object {
        fun newInstance(route: DetailEpisodeRoute) = DetailEpisodeFragment().apply {
            this.route = route
        }
    }

    override val binding: FragmentDetailEpisodeBinding by viewBinding(CreateMethod.INFLATE)
    override val viewModel: DetailEpisodeViewModel by viewModel { parametersOf(route.episode) }

    private val characterAdapter = AsyncListDifferDelegationAdapter(
        getGeneralAdaptersDiffCallback(),
        AdapterDelegatesManager<List<GeneralAdapterUi>>()
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
            tvCharactersStatic.performIfChanged(state.characters.isNotEmpty()) {
                tvCharactersStatic.isVisible = it
            }
            tvEpisodeName.performIfChanged(state.name) {
                this.text = it
            }
            tvReleaseDate.performIfChanged(state.releaseDate) {
                this.text = it
            }
            tvEpisodeShortName.performIfChanged(state.shortName) {
                this.text = it
            }
            rvCharacters.performIfChanged(state.characters) { characters ->
                characterAdapter.items = characters.map { character ->
                    GeneralAdapterUi.Character(character)
                }
            }
        }
    }

    override fun initView() {
        with(binding) {
            rvCharacters.adapter = characterAdapter
            rvCharacters.addItemDecoration(
                DividerItemDecoration(
                    rvCharacters.context,
                    LinearLayout.VERTICAL
                )
            )
            btnBack.setOnClickListener {
                viewModel.sendEvent(Events.OnBackClick)
            }
        }
    }

    override fun bindActions(action: Actions) {
        when (action) {
        }
    }

}

