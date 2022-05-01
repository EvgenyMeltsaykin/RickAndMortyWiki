package com.wiki.f_general_adapter

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wiki.cf_core.base.adapter.BaseAdapter
import com.wiki.cf_core.extensions.context
import com.wiki.cf_core.extensions.requestListener
import com.wiki.cf_core.extensions.roundCorners
import com.wiki.cf_data.CharacterDto
import com.wiki.f_general_adapter.databinding.AdapterCharacterBinding

class CharacterAdapter(
    private val onCharacterClick: (CharacterDto, View) -> Unit,
    private val onPreviewLoaded: () -> Unit
) : BaseAdapter<CharacterDto, AdapterCharacterBinding>() {

    override fun bind(item: CharacterDto, binding: AdapterCharacterBinding) {
        with(binding) {
            root.setOnClickListener { onCharacterClick(item, ivPreview) }
            tvName.text = item.name
            ivPreview.transitionName = item.imageUrl

            Glide.with(context)
                .load(item.imageUrl)
                .centerCrop()
                .apply(RequestOptions().roundCorners(16))
                .requestListener(onLoadFailed = { _, _, _, _ ->
                    onPreviewLoaded()
                }, onResourceReady = { _, _, _, _, _ ->
                    onPreviewLoaded()
                })
                .into(ivPreview)
        }
    }
}