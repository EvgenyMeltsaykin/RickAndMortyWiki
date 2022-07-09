package com.wiki.f_list_location

import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.fragment.BaseFragment
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_core.extensions.sendEvent
import com.wiki.cf_core.navigation.routes.LocationListRoute
import com.wiki.cf_extensions.pagination
import com.wiki.cf_ui.controllers.MenuItem
import com.wiki.cf_ui.controllers.MenuType
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.ToolbarConfig
import com.wiki.f_general_adapter.GeneralAdapterUi
import com.wiki.f_general_adapter.getGeneralAdaptersDiffCallback
import com.wiki.f_general_adapter.getLocationAdapter
import com.wiki.f_list_location.LocationListScreenFeature.*
import com.wiki.f_list_location.databinding.FragmentLocationListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationListFragment : BaseFragment<State, Actions, Events, LocationListViewModel, LocationListRoute>() {

    override val binding: FragmentLocationListBinding by viewBinding(CreateMethod.INFLATE)

    companion object {
        fun newInstance(route: LocationListRoute): LocationListFragment =
            LocationListFragment().apply {
                this.route = route
            }
    }

    override val viewModel: LocationListViewModel by viewModel()

    private val locationAdapter = AsyncListDifferDelegationAdapter(
        getGeneralAdaptersDiffCallback(),
        AdapterDelegatesManager<List<GeneralAdapterUi>>()
            .addDelegate(
                getLocationAdapter(
                    onLocationClick = { viewModel.sendEvent(Events.OnLocationClick(it)) }
                )
            )
    )

    override fun renderState(state: State) {
        with(binding) {
            rvLocation.performIfChanged(state.locations) { locations ->
                locationAdapter.items = locations.map { location ->
                    GeneralAdapterUi.Location(location)
                }
            }
            refresh.performIfChanged(state.isLoading) {
                isRefreshing = it
            }
        }
    }

    override fun initView() {
        with(binding) {
            rvLocation.adapter = locationAdapter
            rvLocation.addItemDecoration(
                DividerItemDecoration(
                    rvLocation.context,
                    LinearLayout.VERTICAL
                )
            )
            rvLocation.pagination(
                loadThreshold = 5,
                loadNextPage = {
                    viewModel.sendEvent(Events.LoadNextPage)
                }
            )
            refresh.setOnRefreshListener {
                viewModel.sendEvent(Events.OnRefresh)
            }
        }

    }

    override fun bindActions(action: Actions) {
        when (action) {

        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = true,
                isVisibleToolbar = true,
                isVisibleBackButton = false,
                toolbarConfig = ToolbarConfig(
                    title = getString(R.string.locations_toolbar_title),
                    menuItem = listOf(
                        MenuItem(
                            menuType = MenuType.SEARCH,
                            clickListener = {
                                viewModel.sendEvent(Events.OnSearchClick)
                            }
                        )
                    )
                )
            )
        )
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}

