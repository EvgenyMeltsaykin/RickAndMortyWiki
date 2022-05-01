package com.wiki.f_list_location

import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_extensions.pagination
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.f_list_location.adapter.LocationAdapter
import com.wiki.f_list_location.databinding.FragmentLocationListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationListFragment : BaseFragment<
    FragmentLocationListBinding,
    LocationListEvents,
    LocationListState,
    LocationListViewModel
    >() {

    override val viewModel: LocationListViewModel by viewModel()

    private val locationAdapter: LocationAdapter = LocationAdapter {
        viewModel.onLocationClick(it)
    }

    override fun renderState(state: LocationListState) {
        locationAdapter.submitListAndSaveState(state.locations, binding.rvLocation)
        binding.refresh.isRefreshing = state.isLoading
    }

    override fun initView(initialState: LocationListState) {
        with(binding) {
            rvLocation.adapter = locationAdapter
            rvLocation.addItemDecoration(DividerItemDecoration(rvLocation.context, LinearLayout.VERTICAL))
            rvLocation.pagination(
                loadThreshold = 5,
                loadNextPage = { viewModel.loadNextPage() }
            )
            refresh.setOnRefreshListener {
                viewModel.onRefresh()
            }
        }

    }

    override fun bindEvents(event: LocationListEvents) {
        when (event) {
            is LocationListEvents.OnNavigateToLocation -> router.navigateTo(screenProvider.DetailLocation(event.location))
        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = true
            )
        )
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}

