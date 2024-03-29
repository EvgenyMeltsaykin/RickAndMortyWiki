package com.wiki.f_list_episode

import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.fragment.BaseFragment
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_core.extensions.sendEvent
import com.wiki.cf_core.navigation.routes.EpisodeListRoute
import com.wiki.cf_extensions.pagination
import com.wiki.f_general_adapter.GeneralAdapterUi
import com.wiki.f_general_adapter.getEpisodeAdapter
import com.wiki.f_general_adapter.getGeneralAdaptersDiffCallback
import com.wiki.f_list_episode.EpisodeListScreenFeature.*
import com.wiki.f_list_episode.databinding.FragmentEpisodeListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EpisodeListFragment : BaseFragment<State, Actions, Events, EpisodeListViewModel, EpisodeListRoute>() {

    companion object {
        fun newInstance(route: EpisodeListRoute) =
            EpisodeListFragment().apply {
                this.route = route
            }
    }

    override val binding: FragmentEpisodeListBinding by viewBinding(CreateMethod.INFLATE)
    override val viewModel: EpisodeListViewModel by viewModel()

    private val episodeAdapter = AsyncListDifferDelegationAdapter(
        getGeneralAdaptersDiffCallback(),
        AdapterDelegatesManager<List<GeneralAdapterUi>>()
            .addDelegate(
                getEpisodeAdapter(
                    horizontalPadding = 16,
                    onEpisodeClick = {
                        viewModel.sendEvent(Events.OnEpisodeClick(it))
                    }
                )
            )
    )

    override fun renderState(state: State) {
        with(binding) {
            rvEpisode.performIfChanged(state.episodes) { episodes ->
                episodeAdapter.items = episodes.map { episode ->
                    GeneralAdapterUi.Episode(episode)
                }
            }
            refresh.performIfChanged(state.isLoading) {
                isRefreshing = it
            }
        }
    }

    override fun initView() {
        with(binding) {
            rvEpisode.adapter = episodeAdapter
            rvEpisode.addItemDecoration(
                DividerItemDecoration(
                    rvEpisode.context,
                    LinearLayout.VERTICAL
                )
            )
            rvEpisode.pagination(
                loadThreshold = 5,
                loadNextPage = {
                    viewModel.sendEvent(Events.LoadNextPage)
                }
            )
            refresh.setOnRefreshListener {
                viewModel.sendEvent(Events.OnRefresh)
            }

            btnSearch.setOnClickListener {
                viewModel.sendEvent(Events.OnSearchClick)
            }
        }
    }

    override fun bindActions(action: Actions) {
        when (action) {
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}

