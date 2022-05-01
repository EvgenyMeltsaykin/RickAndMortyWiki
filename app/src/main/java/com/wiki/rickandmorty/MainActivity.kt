package com.wiki.rickandmorty

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.wiki.cf_core.BaseScreenEventBus
import com.wiki.cf_core.base.BaseEventScreen
import com.wiki.cf_core.controllers.InternetStateErrorController
import com.wiki.cf_core.extensions.getContrastColor
import com.wiki.cf_core.navigation.OnBackPressedListener
import com.wiki.cf_core.navigation.RouterProvider
import com.wiki.cf_core.navigation.TabKeys
import com.wiki.cf_ui.controllers.NavigationUiConfig
import com.wiki.cf_ui.controllers.NavigationUiControl
import com.wiki.cf_ui.controllers.StatusBarController
import com.wiki.rickandmorty.databinding.ActivityMainBinding
import com.wiki.rickandmorty.navigation.Screens.TabContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), RouterProvider, NavigationUiControl, StatusBarController,
    InternetStateErrorController {

    private var navigationConfig: NavigationUiConfig = NavigationUiConfig()
    private val cicerone: Cicerone<Router> by inject()
    override val router = cicerone.router
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private val viewModel by viewModel<MainActivityViewModel>()

    private val baseScreenEventBus by inject<BaseScreenEventBus>()

    private val heightBottomNavigation: Float by lazy {
        resources.getDimension(com.wiki.cf_ui.R.dimen.bottom_navigation_height)
    }
    private val visibleFragment
        get() = supportFragmentManager.fragments.find { it.isVisible }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
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

        viewModel.viewModelScope.launch(Dispatchers.Main) {
            bindBaseEvent()
        }

    }

    private fun setupSplashScreen() {
        val splashScreen = installSplashScreen()

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.view.animate()
                .alpha(0F)
                .setInterpolator(LinearOutSlowInInterpolator())
                .withEndAction { splashScreenView.remove() }
                .start()
        }
    }


    private suspend fun bindBaseEvent() {
        baseScreenEventBus.events.collect { event ->
            when (event) {
                is BaseEventScreen.ShowToast -> {
                    Toast.makeText(this, event.text, Toast.LENGTH_SHORT).show()
                }
                is BaseEventScreen.ShowSnackBar -> {
                    //showSnackBar(context = context, text = event.text)
                }

                is BaseEventScreen.InternetError -> {
                    showInternetError(isVisible = event.isVisible, text = event.text)
                }
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
        if (isVisible) {
            binding.navHostFragment.updatePadding(bottom = heightBottomNavigation.toInt())
        } else {
            binding.navHostFragment.updatePadding(bottom = 0)
        }
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

    override fun showInternetError(isVisible: Boolean, text: String) {
        with(binding.tvError) {
            this.isVisible = isVisible
            this.text = text
        }
    }
}