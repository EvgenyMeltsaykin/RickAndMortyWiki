package com.wiki.f_location_list.adapter

import androidx.core.view.isVisible
import com.wiki.cf_core.base.adapter.BaseAdapter
import com.wiki.cf_core.extensions.getString
import com.wiki.cf_data.LocationDto
import com.wiki.f_location_list.R
import com.wiki.f_location_list.databinding.AdapterLocationBinding

class LocationAdapter(
    private val onLocationClick: (LocationDto) -> Unit
) : BaseAdapter<LocationDto, AdapterLocationBinding>() {
    override fun bind(item: LocationDto, binding: AdapterLocationBinding) {
        with(binding) {
            root.setOnClickListener { onLocationClick(item) }
            tvName.text = item.name
            tvType.isVisible = item.type.isNotEmpty()
            tvType.text = getString(R.string.type, item.type)
            tvDimension.isVisible = item.dimension.isNotEmpty()
            tvDimension.text = getString(R.string.dimension, item.dimension)
        }
    }
}