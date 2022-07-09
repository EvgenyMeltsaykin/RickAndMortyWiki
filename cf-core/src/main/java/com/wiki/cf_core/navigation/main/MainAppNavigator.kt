package com.wiki.cf_core.navigation.main

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import com.github.terrakok.cicerone.Command
import com.wiki.cf_core.navigation.TabKey
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
        if (currentFragment != null && newFragment != null && currentFragment === newFragment) {
            return
        }

        with(fragmentManager.beginTransaction()) {
            newFragment?.let {
                attach(it)
            } ?: run {
                val fr = screenProvider
                    .TabContainer(tabKey)
                    .createFragment(fragmentManager.fragmentFactory)
                add(containerId, fr, tabKey.name)
            }

            currentFragment?.let {
                detach(it)
            }
            commitNow()
        }
    }

}