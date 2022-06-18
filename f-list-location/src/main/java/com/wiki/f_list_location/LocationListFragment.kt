package com.wiki.f_list_location

import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.extensions.performIfChanged
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

class LocationListFragment :
    BaseFragment<FragmentLocationListBinding, State, Effects, Events, LocationListViewModel>() {

    override val viewModel: LocationListViewModel by viewModel()

    private val locationAdapter = AsyncListDifferDelegationAdapter(
        getGeneralAdaptersDiffCallback(),
        AdapterDelegatesManager<List<GeneralAdapterUi>>()
            .addDelegate(
                getLocationAdapter(
                    onLocationClick = {
                        sendEvent(Events.OnLocationClick(it))
                    }
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
                    sendEvent(Events.LoadNextPage)
                }
            )
            refresh.setOnRefreshListener {
                sendEvent(Events.OnRefresh)
            }
        }

    }

    override fun bindEffects(effect: Effects) {
        when (effect) {
            is Effects.OnNavigateToLocation -> router.navigateTo(
                screenProvider.DetailLocation(effect.location)
            )
            is Effects.NavigateToSearch -> router.navigateTo(screenProvider.Search(effect.feature))
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
                                sendEvent(Events.OnSearchClick)
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

