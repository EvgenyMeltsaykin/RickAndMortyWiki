package com.wiki.f_general_adapter

import androidx.core.view.updatePadding
import com.wiki.cf_core.base.adapter.BaseAdapter
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_ui.extensions.toPx
import com.wiki.f_general_adapter.databinding.AdapterEpisodeBinding

class EpisodeAdapter(
    private val onEpisodeClick: (EpisodeDto) -> Unit,
    private val horizontalPadding: Int = 0
) : BaseAdapter<EpisodeDto, AdapterEpisodeBinding>() {

    override fun bind(item: EpisodeDto, binding: AdapterEpisodeBinding) {
        with(binding) {
            root.updatePadding(
                left = horizontalPadding.toPx.toInt(),
                right = horizontalPadding.toPx.toInt()
            )
            root.setOnClickListener { onEpisodeClick(item) }
            tvReleaseDate.text = item.airDate
            tvSeasonName.text = item.name
            tvShortEpisodeName.text = item.shortEpisode
        }
    }

}