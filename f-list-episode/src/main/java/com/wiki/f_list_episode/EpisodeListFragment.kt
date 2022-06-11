package com.wiki.f_list_episode

import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_extensions.pagination
import com.wiki.cf_ui.controllers.MenuItem
import com.wiki.cf_ui.controllers.MenuType
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.ToolbarConfig
import com.wiki.f_general_adapter.EpisodeAdapter
import com.wiki.f_list_episode.EpisodeListScreenFeature.*
import com.wiki.f_list_episode.databinding.FragmentEpisodeListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EpisodeListFragment : BaseFragment<
    FragmentEpisodeListBinding,
    State,
    Effects,
        Events,
    EpisodeListViewModel
    >() {

    override val viewModel: EpisodeListViewModel by viewModel()

    private val episodeAdapter = EpisodeAdapter(
        horizontalPadding = 16,
        onEpisodeClick = {
            sendEvent(Events.OnEpisodeClick(it))
        }
    )

    override fun renderState(state: State) {
        with(binding){
            rvEpisode.performIfChanged(state.episodes){
                episodeAdapter.submitListAndSaveState(state.episodes, rvEpisode)
            }
            refresh.performIfChanged(state.isLoading){
                isRefreshing = it
            }
        }
    }

    override fun initView() {
        with(binding) {
            rvEpisode.adapter = episodeAdapter
            rvEpisode.addItemDecoration(DividerItemDecoration(rvEpisode.context, LinearLayout.VERTICAL))
            rvEpisode.pagination(
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
            is Effects.OnNavigateToEpisode -> {
                router.navigateTo(screenProvider.DetailEpisode(effect.episode))
            }
            is Effects.NavigateToSearch -> {
                router.navigateTo(screenProvider.Search(effect.feature))
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

