package com.wiki.cf_core.base.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import com.wiki.cf_core.base.KoinScopeProvider
import com.wiki.cf_core.navigation.FragmentRouter
import com.wiki.cf_core.navigation.RouterProvider
import org.koin.androidx.scope.fragmentScope
import org.koin.core.scope.Scope

abstract class BaseScopeFragment : Fragment(), KoinScopeProvider, RouterProvider {

    override val scope: Scope by fragmentScope()
    override val router: FragmentRouter get() = (parentFragment as RouterProvider).router

    override fun onAttach(context: Context) {
        scope.declare(router, allowOverride = true)
        super.onAttach(context)
    }

    override fun onResume() {
        if (scope.closed) {
            scope.declare(router, allowOverride = true)
        }
        super.onResume()
    }

    override fun onDestroyView() {
        scope.close()
        super.onDestroyView()
    }
}