package com.wiki.rickandmorty

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Cicerone
import com.wiki.cf_core.base.BaseActivity
import com.wiki.cf_core.controllers.InternetStateErrorController
import com.wiki.cf_core.extensions.getContrastColor
import com.wiki.cf_core.extensions.sendEvent
import com.wiki.cf_core.navigation.OnBackPressedListener
import com.wiki.cf_core.navigation.TabKey
import com.wiki.cf_core.navigation.main.MainAppNavigator
import com.wiki.cf_core.navigation.main.MainAppRouter
import com.wiki.cf_ui.controllers.BottomNavigationController
import com.wiki.cf_ui.controllers.StatusBarController
import com.wiki.rickandmorty.MainActivityScreenFeature.*
import com.wiki.rickandmorty.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class MainActivity : BaseActivity<ActivityMainBinding, State, Actions, Events, MainActivityViewModel
    >(), StatusBarController, InternetStateErrorController, BottomNavigationController {

    private val cicerone: Cicerone<MainAppRouter> by inject(qualifier = named("CiceroneMainApp"))
    override val viewModel by viewModel<MainActivityViewModel>()

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
        subscribeActions()
        subscribeState()
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            bindBaseEvent()
        }
        initView()
        renderState(viewModel.stateFlow.value)
    }

    override fun bindActions(action: Actions) {}

    override fun initView() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                TabKey.CHARACTERS.menuRes -> {
                    viewModel.sendEvent(Events.OnTabClick(TabKey.CHARACTERS))
                    true
                }
                TabKey.EPISODES.menuRes -> {
                    viewModel.sendEvent(Events.OnTabClick(TabKey.EPISODES))
                    true
                }
                TabKey.LOCATIONS.menuRes -> {
                    viewModel.sendEvent(Events.OnTabClick(TabKey.LOCATIONS))
                    true
                }
                else -> false
            }
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
            viewModel.sendEvent(Events.OnBackPress)
        }
    }

    override fun setBottomNavigationBarVisible(isVisible: Boolean) {
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

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        cicerone.getNavigatorHolder().removeNavigator()
    }

}

