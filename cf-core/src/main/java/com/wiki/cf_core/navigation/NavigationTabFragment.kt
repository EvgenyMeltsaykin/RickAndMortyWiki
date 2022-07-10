package com.wiki.cf_core.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.wiki.cf_core.R
import com.wiki.cf_core.databinding.FragmentTabContainerBinding
import com.wiki.cf_core.delegates.fragmentArgument
import org.koin.android.ext.android.inject

class NavigationTabFragment : Fragment(), RouterProvider, OnBackPressedListener {

    companion object {
        fun newInstance(tabKey: TabKey) = NavigationTabFragment().apply {
            this.tabKey = tabKey
        }
    }

    private var tabKey: TabKey by fragmentArgument()

    private val binding: FragmentTabContainerBinding by viewBinding(CreateMethod.INFLATE)
    private val screensProvider: ScreenProvider by inject()
    private val navigationTabHolder: NavigationTabHolder by inject()

    private val cicerone: Cicerone<FragmentRouter>
        get() = navigationTabHolder.getCicerone(tabKey)
    override val router: FragmentRouter
        get() = cicerone.router

    private val navigator: Navigator by lazy {
        FragmentAppNavigator(
            activity = requireActivity(),
            containerId = R.id.ftc_container,
            fragmentManager = childFragmentManager
        )
    }

    private val fragment get() = childFragmentManager.findFragmentById(binding.ftcContainer.id)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.findFragmentById(binding.ftcContainer.id) == null) {
            router.replace(screensProvider.TabFragment(tabKey))
        }
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    override fun onBackPressed(): Boolean {
        return (fragment as? OnBackPressedListener)?.onBackPressed() ?: false
    }

}