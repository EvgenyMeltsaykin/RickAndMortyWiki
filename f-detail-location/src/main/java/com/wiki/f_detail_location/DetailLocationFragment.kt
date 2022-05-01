package com.wiki.f_detail_location

import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.delegates.fragmentNullableArgument
import com.wiki.cf_core.navigation.SharedElementFragment
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.common.SimpleData
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.f_detail_location.databinding.FragmentDetailLocationBinding
import com.wiki.f_general_adapter.CharacterAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailLocationFragment : BaseFragment<
    FragmentDetailLocationBinding,
    DetailLocationEvents,
    DetailLocationState,
    DetailLocationViewModel
    >(), SharedElementFragment {

    companion object {
        fun newInstance(location: LocationDto?, locationData: SimpleData?) = DetailLocationFragment().apply {
            this.location = location
            this.locationData = locationData
        }
    }

    override val viewModel: DetailLocationViewModel by viewModel { parametersOf(location, locationData) }

    override var sharedView: View? = null

    private var location by fragmentNullableArgument<LocationDto>()
    private var locationData by fragmentNullableArgument<SimpleData>()

    private val characterAdapter = CharacterAdapter(
        onPreviewLoaded = {
            startPostponedEnterTransition()
        },
        onCharacterClick = { character, view ->
            sharedView = view
            viewModel.onCharacterClick(character)
        }
    )

    override fun renderState(state: DetailLocationState) {
        characterAdapter.submitList(state.residentCharacters)
        binding.tvResidentsStatic.isVisible = state.residentCharacters.isNotEmpty()
        with(binding) {
            tvLocationName.text = state.name
            tvType.isVisible = state.type.isNotEmpty()
            tvType.text = getString(R.string.type, state.type)
            tvDimension.isVisible = state.dimension.isNotEmpty()
            tvDimension.text = getString(R.string.dimension, state.dimension)
        }
    }

    override fun initView(initialState: DetailLocationState) {
        postponeEnterTransition()
        with(binding) {
            rvCharacters.adapter = characterAdapter
            rvCharacters.addItemDecoration(DividerItemDecoration(rvCharacters.context, LinearLayout.VERTICAL))
        }
    }

    override fun bindEvents(event: DetailLocationEvents) {
        when (event) {
            is DetailLocationEvents.OnNavigateToCharacter -> router.navigateTo(screenProvider.DetailCharacter(event.character))
        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = false
            )
        )
    }
}

