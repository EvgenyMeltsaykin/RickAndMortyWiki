package com.wiki.rickandmorty

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.*
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.wiki.cf_core.BaseScreenEventBus
import com.wiki.cf_core.base.BaseEventScreen
import com.wiki.cf_core.controllers.InternetStateErrorController
import com.wiki.cf_core.extensions.getContrastColor
import com.wiki.cf_core.extensions.safePostDelay
import com.wiki.cf_core.navigation.OnBackPressedListener
import com.wiki.cf_core.navigation.RouterProvider
import com.wiki.cf_core.navigation.TabKeys
import com.wiki.cf_core.navigation.UiControl
import com.wiki.cf_ui.controllers.*
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
    private var doubleBackToExitPressedOnce = false
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
        //setStatusBarColor(com.wiki.cf_ui.R.color.white)
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
        binding.statusBarBackground.updateLayoutParams {
            height = getStatusBarHeight()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        makeStatusBarTransparent()
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun Activity.makeStatusBarTransparent() {
        window?.apply {
            val flags = decorView.systemUiVisibility
            //clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
            //binding.root.fitsSystemWindows = true
            //binding.root.requestFitSystemWindows()
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        //window.setFlags(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        //WindowInsetsControllerCompat(window, binding.root).isAppearanceLightStatusBars = true
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
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                router.exit()
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
        clearMenu(navigationConfig.toolbarConfig.menuItem)
        binding.btnBack.isVisible = navigationConfig.isVisibleBackButton
        //setStatusBarColor(navigationConfig.colorStatusBar)
        setBackgroundColor(navigationConfig.colorBackground)
        setToolbarVisible(navigationConfig.isVisibleToolbar)
        setToolbarInfo(navigationConfig.toolbarConfig)
        setBottomNavigationBarVisible(navigationConfig.isVisibleBottomNavigation)
        makeStatusBarTransparent()
    }

    private fun clearMenu(menuItem: List<MenuItem>) {
        binding.btnSearch.isVisible = menuItem.any { it.menuType == MenuType.SEARCH }
    }

    private fun setToolbarInfo(toolbarConfig: ToolbarConfig) {
        val toolbarType = toolbarConfig.toolbarType
        when (toolbarType) {
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

