package com.wiki.cf_core.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.wiki.cf_data.CharacterDto

interface ScreenProvider {
    fun Characters(): FragmentScreen
    fun DetailCharacter(character: CharacterDto): FragmentScreen
    fun TabContainer(tabKey: TabKeys): FragmentScreen
    fun TabFragment(tabKey: TabKeys): FragmentScreen
}
