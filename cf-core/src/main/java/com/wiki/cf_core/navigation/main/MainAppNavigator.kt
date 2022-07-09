package com.wiki.cf_core.navigation.main

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import com.github.terrakok.cicerone.Command
import com.wiki.cf_core.navigation.TabKey
import com.wiki.cf_core.navigation.UiControl
import com.wiki.cf_core.navigation.base.BaseAppNavigator
import com.wiki.cf_core.navigation.commands.ChangeTab
import org.koin.core.component.KoinComponent

class MainAppNavigator(
    activity: FragmentActivity,
    containerId: Int,
    fragmentManager: FragmentManager = activity.supportFragmentManager,
    fragmentFactory: FragmentFactory = fragmentManager.fragmentFactory
) : BaseAppNavigator(activity, containerId, fragmentManager, fragmentFactory), KoinComponent {

    private val visibleFragment
        get() = fragmentManager.fragments.find { it.isVisible }

    override fun applyCommand(command: Command) {
        when (command) {
            is ChangeTab -> {
                changeTab(command.tabKey)
            }
            else -> {
                super.applyCommand(command)
            }
        }
    }

    private fun changeTab(tabKey: TabKey) {
        val currentFragment = visibleFragment
        val newFragment = fragmentManager.findFragmentByTag(tabKey.name)
        println("1234 currentFragment $currentFragment")
        println("1234 newFragment $newFragment")
        if (currentFragment != null && newFragment != null && currentFragment === newFragment) {
            println("1234 if main $newFragment")
            return
        }


        if (newFragment != null && newFragment is UiControl)
            (newFragment as UiControl).bindNavigationUi()

        with(fragmentManager.beginTransaction()) {
            newFragment?.let {
                show(it)
            } ?: run {
                val fr = screenProvider
                    .TabContainer(tabKey)
                    .createFragment(fragmentManager.fragmentFactory)
                add(containerId, fr, tabKey.name)
            }

            currentFragment?.let {
                hide(it)
            }
            commitNow()
        }
    }

}