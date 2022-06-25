package com.wiki.rickandmorty

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
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
import com.wiki.cf_core.base.BaseActivity
import com.wiki.cf_core.base.BaseEffectScreen
import com.wiki.cf_core.controllers.InternetStateErrorController
import com.wiki.cf_core.extensions.getContrastColor
import com.wiki.cf_core.extensions.safePostDelay
import com.wiki.cf_core.navigation.OnBackPressedListener
import com.wiki.cf_core.navigation.RouterProvider
import com.wiki.cf_core.navigation.TabKey
import com.wiki.cf_core.navigation.UiControl
import com.wiki.cf_core.navigation.main.MainAppNavigator
import com.wiki.cf_core.navigation.main.MainAppRouter
import com.wiki.cf_ui.controllers.*
import com.wiki.rickandmorty.MainActivityScreenFeature.*
import com.wiki.rickandmorty.databinding.ActivityMainBinding
import com.wiki.rickandmorty.navigation.Screens.TabContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class MainActivity : BaseActivity<
        ActivityMainBinding,
        State, Effects, Events,
        MainActivityViewModel
        >(), NavigationUiControl, StatusBarController,
    InternetStateErrorController {

    private var navigationConfig: NavigationUiConfig = NavigationUiConfig()
    private val cicerone: Cicerone<MainAppRouter> by inject(qualifier = named("CiceroneMainApp"))
    override val viewModel by viewModel<MainActivityViewModel>()
    private var doubleBackToExitPressedOnce = false

    private val heightBottomNavigation: Float by lazy {
        resources.getDimension(com.wiki.cf_ui.R.dimen.bottom_navigation_height)
    }
    private val visibleFragment
        get() = supportFragmentManager.fragments.find { it.isVisible }

    private val navigator:MainAppNavigator by lazy {
        MainAppNavigator(
            activity = this,
            containerId = binding.navHostFragment.id,
            fragmentManager = this.supportFragmentManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeEffects()
        subscribeState()
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            bindBaseEvent()
        }
        initView()
        renderState(viewModel.stateFlow.value)
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        cicerone.getNavigatorHolder().removeNavigator()
    }

    override fun bindEffects(effect: Effects) {

    }

    override fun initView() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                TabKey.CHARACTERS.menuRes -> {
                    sendEvent(Events.OnTabClick(TabKey.CHARACTERS))
                    true
                }
                TabKey.EPISODES.menuRes -> {
                    sendEvent(Events.OnTabClick(TabKey.EPISODES))
                    true
                }
                TabKey.LOCATIONS.menuRes -> {
                    sendEvent(Events.OnTabClick(TabKey.LOCATIONS))
                    true
                }
                else -> false
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun renderState(state: State) {
        binding.bottomNavigation.selectedItemId = state.selectedTab.menuRes
    }

    private fun setupSplashScreen() {
        val splashScreen = installSplashScreen()

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.view.animate()
                .alpha(0F)
                .setInterpolator(LinearOutSlowInInterpolator())
                .withEndAction { }
                .start()
        }
    }

    override fun onBackPressed() {
        val fragment = visibleFragment

        if (fragment != null && fragment is OnBackPressedListener
            && (fragment as OnBackPressedListener).onBackPressed()
        ) return else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                //router.exit()
            } else {
                doubleBackToExitPressedOnce = true
                Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT)
                    .show()
            }
            binding.root.safePostDelay(2000) {
                doubleBackToExitPressedOnce = false
            }
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
        clearMenu(navigationConfig.toolbarConfig.menuItem)
        binding.btnBack.isVisible = navigationConfig.isVisibleBackButton
        //setStatusBarColor(navigationConfig.colorStatusBar)
        setBackgroundColor(navigationConfig.colorBackground)
        setToolbarVisible(navigationConfig.isVisibleToolbar)
        setToolbarInfo(navigationConfig.toolbarConfig)
        setBottomNavigationBarVisible(navigationConfig.isVisibleBottomNavigation)
    }

    private fun clearMenu(menuItem: List<MenuItem>) {
        binding.btnSearch.isVisible = menuItem.any { it.menuType == MenuType.SEARCH }
    }

    private fun setToolbarInfo(toolbarConfig: ToolbarConfig) {
        when (toolbarConfig.toolbarType) {
            is ToolbarType.Simple -> {
                with(binding.tvTitle) {
                    isVisible = true
                    text = toolbarConfig.title
                    isAllCaps = toolbarConfig.isTextAllCaps
                }
            }
        }

        toolbarConfig.menuItem.forEach { menuItem ->
            when (menuItem.menuType) {
                MenuType.SEARCH -> {
                    binding.btnSearch.setOnClickListener {
                        menuItem.clickListener()
                    }
                }
            }
        }
    }

    private fun setToolbarVisible(isVisible: Boolean) {
        binding.toolbar.isVisible = isVisible
        binding.divider.isVisible = isVisible
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

