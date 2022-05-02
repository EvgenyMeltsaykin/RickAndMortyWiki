package com.wiki.cf_core.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

interface BindBaseViewHolder<M, VB : ViewBinding> {
    fun bind(item: M, binding: VB)
}

class BaseViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

abstract class BaseAdapter<AdapterItem, VB : ViewBinding> :
    ListAdapter<AdapterItem, BaseViewHolder<VB>>(EqualsDiffCallback { a, b -> a == b }),
    BindBaseViewHolder<AdapterItem, VB> {

    lateinit var itemView: View

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val vbType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
        val vbClass = vbType as Class<VB>
        val method =
            vbClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        val binding = method.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        itemView = holder.itemView
        bind(getItem(position), holder.binding)
    }

    fun submitListAndSaveState(list: List<AdapterItem>?, recyclerView: RecyclerView, afterSubmit: () -> Unit = {}) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val recyclerViewState = layoutManager.onSaveInstanceState()
        submitList(list) {
            (recyclerView.layoutManager as LinearLayoutManager).onRestoreInstanceState(recyclerViewState)
            afterSubmit()
        }
    }

}