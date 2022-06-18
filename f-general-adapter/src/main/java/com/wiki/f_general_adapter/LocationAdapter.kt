package com.wiki.f_general_adapter

import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.wiki.cf_core.base.adapter.BaseAdapter
import com.wiki.cf_core.base.adapter.bindAdapter
import com.wiki.cf_core.extensions.getString
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_ui.extensions.toPx
import com.wiki.f_general_adapter.databinding.AdapterEpisodeBinding
import com.wiki.f_general_adapter.databinding.AdapterLocationBinding

fun getLocationAdapter(
    onLocationClick: (LocationDto) -> Unit
) = bindAdapter<AdapterLocationBinding, GeneralAdapterUi, GeneralAdapterUi.Location> {
    binding.root.setOnClickListener {
        onLocationClick(item.location)
    }
    bind {
        with(binding){
            tvName.text = item.location.name
            tvType.isVisible = item.location.type.isNotEmpty()
            tvType.text = getString(R.string.adapter_type, item.location.type)
            tvDimension.isVisible = item.location.dimension.isNotEmpty()
            tvDimension.text = getString(R.string.adapter_dimension, item.location.dimension)
        }
    }
}