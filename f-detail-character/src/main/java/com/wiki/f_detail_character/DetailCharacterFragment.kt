package com.wiki.f_detail_character

import com.wiki.cf_core.base.BaseFragment
import com.wiki.cf_core.delegates.fragmentArgument
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.f_detail_character.databinding.FragmentDetailCharacterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailCharacterFragment : BaseFragment<
    FragmentDetailCharacterBinding,
    DetailCharacterEvents,
    DetailCharacterState,
    DetailCharacterViewModel
    >() {

    companion object {
        fun newInstance(character: CharacterDto): DetailCharacterFragment = DetailCharacterFragment().apply {
            this.character = character
        }
    }

    private var character by fragmentArgument<CharacterDto>()

    override val viewModel: DetailCharacterViewModel by viewModel { parametersOf(character) }

    override fun initView() {
    }

    override fun bindNavigationUi() {
        setNavigationUiConfig(
            NavigationUiConfig(
                isVisibleBottomNavigation = false
            )
        )
    }

    override fun renderState(state: DetailCharacterState) {

    }

    override fun bindEvents(event: DetailCharacterEvents) {
        when (event) {

        }
    }
}