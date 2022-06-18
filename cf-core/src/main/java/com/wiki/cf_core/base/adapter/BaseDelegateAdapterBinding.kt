package com.wiki.cf_core.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewBindingViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

inline fun <reified VB : ViewBinding,T, reified I : T, > bindAdapter(
    noinline block: AdapterDelegateViewBindingViewHolder<I, VB>.() -> Unit
): AdapterDelegate<List<T>> {
    return adapterDelegateViewBinding(
        { layoutInflater, root ->
            val vbClass = VB::class.java
            val method = vbClass.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
            method.invoke(null, layoutInflater, root, false) as VB
        },
        block = block
    )
}