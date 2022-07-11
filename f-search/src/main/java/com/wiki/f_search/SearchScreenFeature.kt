package com.wiki.f_search

import com.wiki.cf_core.base.ActionScreen
import com.wiki.cf_core.base.EventScreen
import com.wiki.cf_core.base.StateScreen
import com.wiki.cf_core.delegates.adapter.AdapterDelegateItem
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.SearchFeature

class SearchScreenFeature {

    sealed class Events : EventScreen {
        data class OnCharacterClick(val character: CharacterDto) : Events()
        data class OnEpisodeClick(val episode: EpisodeDto) : Events()
        data class OnLocationClick(val location: LocationDto) : Events()
        object OnBackClick : Events()
        object LoadNextPage : Events()
        data class OnChangeSearchText(val text: String) : Events()
    }

    sealed class Actions : ActionScreen {}

    data class State(
        val feature: SearchFeature,
        val searchResultUi: List<AdapterDelegateItem> = emptyList(),
        val isLoading: Boolean = true,
        val page: Int = 1,
        val endReached: Boolean = false,
        val isVisibleNotFound: Boolean = false,
        val searchText: String = ""
    ) : StateScreen

}