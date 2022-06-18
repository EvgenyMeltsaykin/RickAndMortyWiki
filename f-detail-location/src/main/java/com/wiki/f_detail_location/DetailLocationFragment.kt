package com.wiki.f_detail_location

import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.delegates.fragmentNullableArgument
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.common.SimpleData
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.ToolbarConfig
import com.wiki.f_detail_location.DetailLocationScreenFeature.*
import com.wiki.f_detail_location.databinding.FragmentDetailLocationBinding
import com.wiki.f_general_adapter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailLocationFragment :
    BaseFragment<FragmentDetailLocationBinding, State, Effects, Events, DetailLocationViewModel>() {

    companion object {
        fun newInstance(location: LocationDto?, locationData: SimpleData?) = DetailLocationFragment().apply {
            this.location = location
            this.locationData = locationData
        }
    }

    override val viewModel: DetailLocationViewModel by viewModel { parametersOf(location, locationData) }

    private var location by fragmentNullableArgument<LocationDto>()
    private var locationData by fragmentNullableArgument<SimpleData>()

    private val characterAdapter = AsyncListDifferDelegationAdapter(
        getGeneralAdaptersDiffCallback(),
        AdapterDelegatesManager<List<GeneralAdapterUi>>()
            .addDelegate(
                getCharacterAdapter(
                    onCharacterClick = { character, _ ->
                        sendEvent(Events.OnCharacterClick(character))
                    }
                )
            )
    )

    override fun renderState(state: State) {
        with(binding) {
            rvCharacters.performIfChanged(state.residentCharacters){ characters->
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
                router.navigateTo(screenProvider.DetailCharacter(effect.character))
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

