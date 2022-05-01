package com.wiki.f_detail_character.adapter

import com.wiki.cf_core.base.adapter.BaseAdapter
import com.wiki.cf_data.EpisodeDto
import com.wiki.f_detail_character.databinding.AdapterEpisodeBinding

class EpisodeAdapter(
    private val onEpisodeClick: (EpisodeDto) -> Unit
) : BaseAdapter<EpisodeDto, AdapterEpisodeBinding>() {

    override fun bind(item: EpisodeDto, binding: AdapterEpisodeBinding) {
        with(binding) {
            root.setOnClickListener { onEpisodeClick(item) }
            tvReleaseDate.text = item.airDate
            tvSeasonName.text = item.name
            tvShortEpisodeName.text = item.shortEpisode
        }
    }

}