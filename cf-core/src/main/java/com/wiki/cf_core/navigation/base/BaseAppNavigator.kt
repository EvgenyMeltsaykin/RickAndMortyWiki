package com.wiki.cf_core.navigation.base

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.cf_core.navigation.commands.ShowDialog
import com.wiki.cf_core.navigation.main.MainAppRouter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseAppNavigator(
    activity: FragmentActivity,
    containerId: Int,
    fragmentManager: FragmentManager = activity.supportFragmentManager,
    fragmentFactory: FragmentFactory = fragmentManager.fragmentFactory
) : AppNavigator(activity, containerId, fragmentManager, fragmentFactory), KoinComponent {

    private val mainApplicationRouter: MainAppRouter by inject()
    protected val screenProvider: ScreenProvider by inject()

    protected fun showDialog(command: ShowDialog) {
        val dialog = screenProvider.byRoute(command.dialog)

        dialog.show(
            fragmentManager,
            command.dialog.getId()
        )
    }
}