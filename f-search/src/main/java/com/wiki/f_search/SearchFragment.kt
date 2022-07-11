package com.wiki.f_search

import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wiki.cf_core.base.fragment.BaseFragment
import com.wiki.cf_core.delegates.adapter.AdapterDelegateItem
import com.wiki.cf_core.delegates.adapter.DelegateAdapter
import com.wiki.cf_core.delegates.adapter.DelegateManager
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
import com.wiki.f_search.adapter.getSearchHeaderAdapter
import com.wiki.f_search.data.SearchItemUi
import com.wiki.f_search.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.surfstudio.android.recycler.decorator.Decorator

class SearchFragment : BaseFragment<State, Actions, Events, SearchViewModel, SearchRoute>() {

    companion object {
        fun newInstance(route: SearchRoute) = SearchFragment().apply {
            this.route = route
        }

        private const val HIDE_KEYBOARD_ON_SCROLL_THRESHOLD = 10
    }

    override val binding: FragmentSearchBinding by viewBinding(CreateMethod.INFLATE)
    override val viewModel: SearchViewModel by viewModel { parametersOf(route.feature) }

    private val characterDelegateAdapter = getCharacterAdapter(
        onCharacterClick = { character, _ ->
            viewModel.sendEvent(Events.OnCharacterClick(character))
        }
    )
    private val episodeDelegateAdapter = getEpisodeAdapter(
        horizontalPadding = 16,
        onEpisodeClick = {
            viewModel.sendEvent(Events.OnEpisodeClick(it))
        }
    )

    private val locationDelegateAdapter = getLocationAdapter(
        onLocationClick = {
            viewModel.sendEvent(Events.OnLocationClick(it))
        }
    )

    private val searchDelegateAdapter = getSearchHeaderAdapter()

    private val searchAdapter = DelegateAdapter(
        getGeneralAdaptersDiffCallback(),
        DelegateManager<List<AdapterDelegateItem>>()
            .addDelegate(characterDelegateAdapter)
            .addDelegate(episodeDelegateAdapter)
            .addDelegate(locationDelegateAdapter)
            .addDelegate(searchDelegateAdapter)
    )

    override fun renderState(state: State) {
        with(binding) {
            rvResult.performIfChanged(state.searchResultUi) { results ->
                searchAdapter.setItems(results)
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
            rvResult.addItemDecoration(buildDecoration())
            rvResult.pagination(
                loadThreshold = 5,
                loadNextPage = {
                    viewModel.sendEvent(Events.LoadNextPage)
                }
            )
            rvResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleVisible = layoutManager.findFirstVisibleItemPosition()
                    if (searchAdapter.items.count() < firstVisibleVisible) return
                    val item = searchAdapter.items[firstVisibleVisible]
                    when (item) {
                        is GeneralAdapterUi.Character -> {
                            binding.tvTitle.text = "Characters"
                        }
                        is GeneralAdapterUi.Location -> {
                            binding.tvTitle.text = "Locations"
                        }
                        is GeneralAdapterUi.Episode -> {
                            binding.tvTitle.text = "Episodes"
                        }
                        is SearchItemUi.Header -> {
                            binding.tvTitle.text = item.text
                        }
                        else -> {
                        }
                    }
                }
            })
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

    private fun buildDecoration(): RecyclerView.ItemDecoration {
        return Decorator.Builder()
            .build()
    }

    override fun bindActions(action: Actions) {
        when (action) {

        }
    }

}

