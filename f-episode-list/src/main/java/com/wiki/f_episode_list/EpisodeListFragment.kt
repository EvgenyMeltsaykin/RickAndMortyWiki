package com.wiki.f_episode_list

import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_extensions.pagination
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.f_episode_list.databinding.FragmentEpisodeListBinding
import com.wiki.f_general_adapter.EpisodeAdapter
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

