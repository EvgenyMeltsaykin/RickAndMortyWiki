package com.wiki.f_list_episode

import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_extensions.pagination
import com.wiki.cf_ui.controllers.MenuItem
import com.wiki.cf_ui.controllers.MenuType
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.ToolbarConfig
import com.wiki.f_general_adapter.EpisodeAdapter
import com.wiki.f_list_episode.databinding.FragmentEpisodeListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EpisodeListFragment : BaseFragment<
    FragmentEpisodeListBinding,
    EpisodeListEvents,
    EpisodeListState,
    EpisodeListViewModel
    >() {

    override val viewModel: EpisodeListViewModel by viewModel()

    private val episodeAdapter = EpisodeAdapter(
        horizontalPadding = 16,
        onEpisodeClick = {
            viewModel.onEpisodeClick(it)
        }
    )

    override fun renderState(state: EpisodeListState) {
        episodeAdapter.submitListAndSaveState(state.episodes, binding.rvEpisode)
        binding.refresh.isRefreshing = state.isLoading
    }

    override fun initView(initialState: EpisodeListState) {
        with(binding) {
            rvEpisode.adapter = episodeAdapter
            rvEpisode.addItemDecoration(DividerItemDecoration(rvEpisode.context, LinearLayout.VERTICAL))
            rvEpisode.pagination(
                loadThreshold = 5,
                loadNextPage = { viewModel.loadNextPage() }
            )
            refresh.setOnRefreshListener {
                viewModel.onRefresh()
            }
        }
    }

    override fun bindEvents(event: EpisodeListEvents) {
        when (event) {
            is EpisodeListEvents.OnNavigateToEpisode -> {
                router.navigateTo(screenProvider.DetailEpisode(event.episode))
            }
            is EpisodeListEvents.NavigateToSearch -> {
                router.navigateTo(screenProvider.Search(event.feature))
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
                    title = getString(R.string.episodes_toolbar_title),
                    menuItem = listOf(
                        MenuItem(
                            menuType = MenuType.SEARCH,
                            clickListener = { viewModel.onSearchClick() }
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

