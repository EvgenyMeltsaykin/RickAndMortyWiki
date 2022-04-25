package com.wiki.cf_core.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.wiki.cf_core.R
import com.wiki.cf_core.databinding.FragmentTabContainerBinding
import org.koin.android.ext.android.inject

class NavigationTabFragment : Fragment(), RouterProvider, OnBackPressedListener {

    private var _binding: FragmentTabContainerBinding? = null
    private val binding: FragmentTabContainerBinding
        get() = _binding!!

    private val screensProvider: ScreenProvider by inject()

    companion object {
        private const val TAB_KEY = "TAB_KEY"
        fun newInstance(tabKey: TabKeys) = NavigationTabFragment().apply {
            arguments = Bundle().apply {
                putSerializable(TAB_KEY, tabKey)
            }
        }
    }

    private val navigator: Navigator by lazy {
        CustomAppNavigator(
            requireActivity(),
            R.id.ftc_container,
            childFragmentManager
        )
    }

    private val fragment
        get() = childFragmentManager.findFragmentById(binding.ftcContainer.id)

    private val navigationTabHolder: NavigationTabHolder by inject()

    private val tabKey: TabKeys
        get() = requireArguments().getSerializable(TAB_KEY) as TabKeys

    private val cicerone: Cicerone<Router>
        get() = navigationTabHolder.getCicerone(tabKey)
    override val router: Router
        get() = cicerone.router

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTabContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.findFragmentById(binding.ftcContainer.id) == null) {
            router.replaceScreen(screensProvider.TabFragment(tabKey))
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