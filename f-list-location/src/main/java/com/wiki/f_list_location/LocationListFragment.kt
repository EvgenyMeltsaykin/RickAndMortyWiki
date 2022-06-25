package com.wiki.f_list_location

import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_core.extensions.sendEvent
import com.wiki.cf_core.navigation.routes.DetailLocationRoute
import com.wiki.cf_core.navigation.routes.LocationListRoute
import com.wiki.cf_core.navigation.routes.SearchRoute
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

class LocationListFragment : BaseFragment<
        FragmentLocationListBinding,
        State,
        Effects,
        Events,
        LocationListViewModel,
        LocationListRoute>() {

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

    override fun bindEffects(effect: Effects) {
        when (effect) {
            is Effects.OnNavigateToLocation -> {
                val route = DetailLocationRoute(effect.location, null)
                router.navigateTo(
                    screenProvider.byRoute(route)
                )
            }
            is Effects.NavigateToSearch -> {
                val route = SearchRoute(effect.feature)
                router.navigateTo(screenProvider.byRoute(route))
            }
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

