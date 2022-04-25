package com.wiki.rickandmorty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.wiki.cf_core.navigation.RouterProvider
import com.wiki.cf_core.navigation.TabKeys
import com.wiki.rickandmorty.databinding.ActivityMainBinding
import com.wiki.rickandmorty.navigation.Screens.TabContainer
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), RouterProvider {

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
        super.onBackPressed()
        router.exit()
        /*
        if (fragment != null && fragment is OnBackPressedListener
            && (fragment as OnBackPressedListener).onBackPressed()
        ) return else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                router.exit()
            } else {
                doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Для выхода нажмите ещё раз", Toast.LENGTH_SHORT)
                    .show()
            }
        }

         */
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

}