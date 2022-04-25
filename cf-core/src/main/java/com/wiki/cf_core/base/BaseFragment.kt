package com.wiki.cf_core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.github.terrakok.cicerone.Router
import com.wiki.cf_core.navigation.OnBackPressedListener
import com.wiki.cf_core.navigation.RouterProvider
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VB : ViewBinding> : Fragment(), RouterProvider, OnBackPressedListener {
    override val router: Router
        get() = (parentFragment as RouterProvider).router

    private var _binding: VB? = null
    val binding get() = _binding!!

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vbType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val vbClass = vbType as Class<VB>
        val method =
            vbClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        _binding = method.invoke(null, inflater, container, false) as VB
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (binding.root as ViewGroup).removeAllViews()
        _binding = null
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

}