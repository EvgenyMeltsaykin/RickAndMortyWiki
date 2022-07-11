package com.wiki.f_general_adapter

import androidx.core.view.updatePadding
import com.wiki.cf_core.base.adapter.bindAdapter
import com.wiki.cf_core.delegates.adapter.AdapterDelegateItem
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_ui.extensions.toPx
import com.wiki.f_general_adapter.databinding.AdapterEpisodeBinding

fun getEpisodeAdapter(
    onEpisodeClick: (EpisodeDto) -> Unit,
    horizontalPadding: Int = 0
) = bindAdapter<AdapterEpisodeBinding, AdapterDelegateItem, GeneralAdapterUi.Episode> {
    binding.root.setOnClickListener {
        onEpisodeClick(item.episode)
    }
    bind {
        with(binding) {
            root.updatePadding(
                left = horizontalPadding.toPx.toInt(),
                right = horizontalPadding.toPx.toInt()
            )
            tvReleaseDate.text = item.episode.airDate
            tvSeasonName.text = item.episode.name
            tvShortEpisodeName.text = item.episode.shortEpisode
        }
    }
}