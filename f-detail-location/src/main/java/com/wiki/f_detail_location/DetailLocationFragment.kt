package com.wiki.f_detail_location

import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_core.extensions.sendEvent
import com.wiki.cf_core.navigation.routes.DetailCharacterRoute
import com.wiki.cf_core.navigation.routes.DetailLocationRoute
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.ToolbarConfig
import com.wiki.f_detail_location.DetailLocationScreenFeature.*
import com.wiki.f_detail_location.databinding.FragmentDetailLocationBinding
import com.wiki.f_general_adapter.GeneralAdapterUi
import com.wiki.f_general_adapter.getCharacterAdapter
import com.wiki.f_general_adapter.getGeneralAdaptersDiffCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailLocationFragment : BaseFragment<
        FragmentDetailLocationBinding,
        State,
        Effects,
        Events,
        DetailLocationViewModel,
        DetailLocationRoute>() {

    companion object {
        fun newInstance(route: DetailLocationRoute) = DetailLocationFragment().apply {
            this.route = route
        }
    }

    override val viewModel: DetailLocationViewModel by viewModel {
        parametersOf(
            route.location,
            route.locationData
        )
    }

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
            rvCharacters.performIfChanged(state.residentCharacters) { characters ->
                characterAdapter.items = characters.map { GeneralAdapterUi.Character(it) }
            }

            tvResidentsStatic.performIfChanged(state.residentCharacters.isNotEmpty()) {
                isVisible = it
            }
            tvLocationName.performIfChanged(state.name) {
                text = it
            }
            tvType.performIfChanged(state.type) {
                isVisible = it.isNotEmpty()
                text = getString(R.string.detail_location_type, state.type)
            }
            tvDimension.performIfChanged(state.dimension) {
                isVisible = it.isNotEmpty()
                text = getString(R.string.detail_location_dimension, state.dimension)
            }
        }
    }

    override fun initView() {
        with(binding.rvCharacters) {
            adapter = characterAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        }
    }

    override fun bindEffects(effect: Effects) {
        when (effect) {
            is Effects.OnNavigateToCharacter -> {
                val route = DetailCharacterRoute(effect.character)
                router.navigateTo(screenProvider.byRoute(route))
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
                    title = getString(R.string.detail_location_toolbar_title)
                )
            )
        )
    }

}

