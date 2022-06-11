package com.wiki.f_detail_episode

import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.delegates.fragmentArgument
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.ToolbarConfig
import com.wiki.f_detail_episode.databinding.FragmentDetailEpisodeBinding
import com.wiki.f_general_adapter.CharacterAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailEpisodeFragment : BaseFragment<
    FragmentDetailEpisodeBinding,
    DetailEpisodeEvents,
    DetailEpisodeState,
    DetailEpisodeViewModel>() {

    override val viewModel: DetailEpisodeViewModel by viewModel { parametersOf(episode) }

    private val characterAdapter: CharacterAdapter = CharacterAdapter(
        onPreviewLoaded = { },
        onCharacterClick = { character, view ->
            viewModel.onCharacterClick(character)
        }
    )

    companion object {
        fun newInstance(episode: EpisodeDto) = DetailEpisodeFragment().apply {
            this.episode = episode
        }
    }

    private var episode by fragmentArgument<EpisodeDto>()

    override fun renderState(state: DetailEpisodeState) {
        characterAdapter.submitList(state.characters)
        binding.tvCharactersStatic.isVisible = state.characters.isNotEmpty()
    }

    override fun initView(initialState: DetailEpisodeState) {
        with(binding) {
            rvCharacters.adapter = characterAdapter
            rvCharacters.addItemDecoration(DividerItemDecoration(rvCharacters.context, LinearLayout.VERTICAL))
            tvEpisodeName.text = initialState.name
            tvReleaseDate.text = initialState.releaseDate
            tvEpisodeShortName.text = initialState.shortName
        }
    }

    override fun bindEvents(event: DetailEpisodeEvents) {
        when (event) {
            is DetailEpisodeEvents.OnNavigateToCharacter -> router.navigateTo(screenProvider.DetailCharacter(event.character))
        }
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleToolbar = true,
                isVisibleBackButton = true,
                isVisibleBottomNavigation = true,
                toolbarConfig = ToolbarConfig(
                    title = getString(R.string.detail_episode_toolbar_title)
                )
            )
        )
    }
}

