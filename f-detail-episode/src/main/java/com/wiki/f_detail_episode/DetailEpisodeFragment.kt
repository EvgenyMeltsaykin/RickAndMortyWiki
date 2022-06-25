package com.wiki.f_detail_episode

import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_core.extensions.sendEvent
import com.wiki.cf_core.navigation.routes.DetailCharacterRoute
import com.wiki.cf_core.navigation.routes.DetailEpisodeRoute
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.ToolbarConfig
import com.wiki.f_detail_episode.DetailEpisodeScreenFeature.*
import com.wiki.f_detail_episode.databinding.FragmentDetailEpisodeBinding
import com.wiki.f_general_adapter.GeneralAdapterUi
import com.wiki.f_general_adapter.getCharacterAdapter
import com.wiki.f_general_adapter.getGeneralAdaptersDiffCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailEpisodeFragment : BaseFragment<
        FragmentDetailEpisodeBinding,
        State,
        Effects,
        Events,
        DetailEpisodeViewModel,
        DetailEpisodeRoute
        >() {

    companion object {
        fun newInstance(route: DetailEpisodeRoute) = DetailEpisodeFragment().apply {
            this.route = route
        }
    }

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
        }
    }

    override fun bindEffects(effect: Effects) {
        when (effect) {
            is Effects.OnNavigateToCharacter -> {
                val route = DetailCharacterRoute(effect.character)
                router.navigateTo(
                    screenProvider.byRoute(route)
                )
            }
        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleToolbar = true,
                isVisibleBackButton = true,
                isVisibleBottomNavigation = true,
                toolbarConfig = ToolbarConfig(
                    title = getString(R.string.detail_episode_toolbar_title)
                )
            )
        )
    }

}

