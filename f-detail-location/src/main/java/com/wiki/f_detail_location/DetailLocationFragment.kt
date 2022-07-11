package com.wiki.f_detail_location

import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.fragment.BaseFragment
import com.wiki.cf_core.delegates.adapter.AdapterDelegateItem
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_core.extensions.sendEvent
import com.wiki.cf_core.navigation.routes.DetailLocationRoute
import com.wiki.f_detail_location.DetailLocationScreenFeature.*
import com.wiki.f_detail_location.databinding.FragmentDetailLocationBinding
import com.wiki.f_general_adapter.GeneralAdapterUi
import com.wiki.f_general_adapter.getCharacterAdapter
import com.wiki.f_general_adapter.getGeneralAdaptersDiffCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailLocationFragment : BaseFragment<State, Actions, Events, DetailLocationViewModel, DetailLocationRoute>() {

    companion object {
        fun newInstance(route: DetailLocationRoute) = DetailLocationFragment().apply {
            this.route = route
        }
    }

    override val binding: FragmentDetailLocationBinding by viewBinding(CreateMethod.INFLATE)
    override val viewModel: DetailLocationViewModel by viewModel {
        parametersOf(route.location, route.locationData)
    }

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
        binding.btnBack.setOnClickListener {
            viewModel.sendEvent(Events.OnBackClick)
        }
    }

    override fun bindActions(action: Actions) {
        when (action) {
        }
    }

}

