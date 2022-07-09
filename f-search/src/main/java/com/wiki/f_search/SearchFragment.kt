package com.wiki.f_search

import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.wiki.cf_core.base.fragment.BaseFragment
import com.wiki.cf_core.extensions.hideKeyboard
import com.wiki.cf_core.extensions.performIfChanged
import com.wiki.cf_core.extensions.sendEvent
import com.wiki.cf_core.extensions.showKeyboard
import com.wiki.cf_core.navigation.routes.SearchRoute
import com.wiki.cf_data.SearchFeature
import com.wiki.cf_extensions.capitalize
import com.wiki.cf_extensions.pagination
import com.wiki.f_general_adapter.*
import com.wiki.f_search.SearchScreenFeature.*
import com.wiki.f_search.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : BaseFragment<State, Actions, Events, SearchViewModel, SearchRoute>() {

    companion object {
        fun newInstance(route: SearchRoute) = SearchFragment().apply {
            this.route = route
        }

        private const val HIDE_KEYBOARD_ON_SCROLL_THRESHOLD = 10
    }

    override val binding: FragmentSearchBinding by viewBinding(CreateMethod.INFLATE)
    override val viewModel: SearchViewModel by viewModel { parametersOf(route.feature) }

    private val searchAdapter = AsyncListDifferDelegationAdapter(
        getGeneralAdaptersDiffCallback(),
        AdapterDelegatesManager<List<GeneralAdapterUi>>()
            .addDelegate(
                getCharacterAdapter(
                    onCharacterClick = { character, _ ->
                        viewModel.sendEvent(Events.OnCharacterClick(character))
                    }
                )
            )
            .addDelegate(
                getEpisodeAdapter(
                    horizontalPadding = 16,
                    onEpisodeClick = {
                        viewModel.sendEvent(Events.OnEpisodeClick(it))
                    }
                )
            )
            .addDelegate(
                getLocationAdapter(
                    onLocationClick = {
                        viewModel.sendEvent(Events.OnLocationClick(it))
                    }
                )
            )
    )

    override fun renderState(state: State) {
        with(binding) {
            rvResult.performIfChanged(state.searchResultUi) { results ->
                searchAdapter.items = results
            }

            etSearch.performIfChanged(state.feature) {
                hint = getHintText(it)
            }

            tvNotFound.performIfChanged(state.feature, state.isVisibleNotFound) { feature, isVisible ->
                text = getNotFoundText(feature)
                this.isVisible = isVisible

            }
        }
    }

    override fun initView() {
        with(binding) {
            rvResult.adapter = searchAdapter
            rvResult.addItemDecoration(
                DividerItemDecoration(
                    rvResult.context,
                    LinearLayout.VERTICAL
                )
            )
            rvResult.pagination(
                loadThreshold = 5,
                loadNextPage = {
                    viewModel.sendEvent(Events.LoadNextPage)
                }
            )
            rvResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (kotlin.math.abs(dy) > HIDE_KEYBOARD_ON_SCROLL_THRESHOLD) {
                        etSearch.hideKeyboard()
                    }
                }
            })
            etSearch.doOnTextChanged { text, _, _, _ ->
                viewModel.onChangeSearchText(text.toString())
            }
            etSearch.showKeyboard()
            btnBack.setOnClickListener {
                viewModel.sendEvent(Events.OnBackClick)
            }
        }

    }

    private fun getHintText(feature: SearchFeature): String {
        return getString(R.string.search_hint, feature.featureName)
    }

    private fun getNotFoundText(feature: SearchFeature): String {
        return getString(R.string.not_found, feature.featureName.capitalize())
    }

    override fun bindActions(action: Actions) {
        when (action) {

        }
    }

}

