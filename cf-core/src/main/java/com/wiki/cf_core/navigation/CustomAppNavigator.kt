package com.wiki.cf_core.navigation

import androidx.fragment.app.*
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen


class CustomAppNavigator(
    activity: FragmentActivity,
    containerId: Int,
    fragmentManager: FragmentManager = activity.supportFragmentManager,
    fragmentFactory: FragmentFactory = fragmentManager.fragmentFactory
) : AppNavigator(activity, containerId, fragmentManager, fragmentFactory) {
    override fun setupFragmentTransaction(
        screen: FragmentScreen,
        fragmentTransaction: FragmentTransaction,
        currentFragment: Fragment?,
        nextFragment: Fragment
    ) {
        if (currentFragment is SharedElementFragment && nextFragment is SharedElementFragment) {
            (currentFragment as SharedElementFragment).sharedView?.let {
                fragmentTransaction.setReorderingAllowed(true)
                fragmentTransaction.addSharedElement(it, it.transitionName)
            }
        }
    }
}
