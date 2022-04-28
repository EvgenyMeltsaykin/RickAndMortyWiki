package com.wiki.rickandmorty

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.wiki.cf_core.extensions.getContrastColor
import com.wiki.cf_core.navigation.OnBackPressedListener
import com.wiki.cf_core.navigation.RouterProvider
import com.wiki.cf_core.navigation.TabKeys
import com.wiki.cf_core.navigation.UiControl
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.NavigationUiControl
import com.wiki.cf_ui.controllers.StatusBarController
import com.wiki.rickandmorty.databinding.ActivityMainBinding
import com.wiki.rickandmorty.navigation.Screens.TabContainer
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(), RouterProvider, NavigationUiControl, StatusBarController {

    private var navigationConfig: NavigationUiConfig = NavigationUiConfig()
    private val cicerone: Cicerone<Router> by inject()
    override val router = cicerone.router
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    private val visibleFragment
        get() = supportFragmentManager.fragments.find { it.isVisible }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectTab(TabKeys.CHARACTERS)
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_menu_item_characters -> {
                    selectTab(TabKeys.CHARACTERS)
                    true
                }
                R.id.bottom_menu_item_episodes -> {
                    selectTab(TabKeys.EPISODES)
                    true
                }
                R.id.bottom_menu_item_locations -> {
                    selectTab(TabKeys.LOCATIONS)
                    true
                }
                else -> false
            }
        }

    }

    override fun onBackPressed() {
        val fragment = visibleFragment

        if (fragment != null && fragment is OnBackPressedListener
            && (fragment as OnBackPressedListener).onBackPressed()
        ) return else {
            super.onBackPressed()
            router.exit()
            /*
             if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                router.exit()
            } else {
                doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Для выхода нажмите ещё раз", Toast.LENGTH_SHORT)
                    .show()
            }
             */

        }

    }


    private fun selectTab(tabKey: TabKeys) {
        val fm = supportFragmentManager

        val currentFragment = visibleFragment
        val newFragment = fm.findFragmentByTag(tabKey.name)

        if (currentFragment != null && newFragment != null && currentFragment === newFragment)
            return

        if (newFragment != null && newFragment is UiControl)
            (newFragment as UiControl).bindNavigationUi()


        with(fm.beginTransaction()) {
            newFragment?.let {
                show(it)
            } ?: run {
                val fr = TabContainer(tabKey).createFragment(fm.fragmentFactory)
                add(binding.navHostFragment.id, fr, tabKey.name)
            }

            currentFragment?.let {
                hide(it)
            }
            commitNow()
        }
    }

    override fun setNavigationUiConfig(config: NavigationUiConfig) {
        navigationConfig = config
        setupToolbar(config)
    }

    override fun getNavigationUiConfig(): NavigationUiConfig {
        return navigationConfig
    }

    private fun setupToolbar(navigationConfig: NavigationUiConfig) {
        setStatusBarColor(navigationConfig.colorStatusBar)
        setBackgroundColor(navigationConfig.colorBackground)
        setBottomNavigationBarVisible(navigationConfig.isVisibleBottomNavigation)
    }

    private fun setBackgroundColor(@ColorRes color: Int) {
        binding.root.setBackgroundColor(getColor(color))
    }

    private fun setBottomNavigationBarVisible(isVisible: Boolean) {
        binding.bottomNavigation.isVisible = isVisible
    }

    override fun setStatusBarColor(color: Int) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.isAppearanceLightStatusBars = color.getContrastColor() != Color.BLACK
        }
        val window: Window = this.window
        window.statusBarColor = ContextCompat.getColor(this, color)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }

}