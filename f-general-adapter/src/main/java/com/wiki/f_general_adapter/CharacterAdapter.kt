package com.wiki.f_general_adapter

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wiki.cf_core.base.adapter.BaseAdapter
import com.wiki.cf_core.base.adapter.bindAdapter
import com.wiki.cf_core.extensions.context
import com.wiki.cf_core.extensions.requestListener
import com.wiki.cf_core.extensions.roundCorners
import com.wiki.cf_data.CharacterDto
import com.wiki.f_general_adapter.databinding.AdapterCharacterBinding

fun getCharacterAdapter(
    onCharacterClick: (CharacterDto, View) -> Unit,
    onPreviewLoaded: () -> Unit = {}
) = bindAdapter<AdapterCharacterBinding, GeneralAdapterUi, GeneralAdapterUi.Character> {
    binding.root.setOnClickListener {
        onCharacterClick(item.character, binding.ivPreview)
    }
    bind {
        with(binding){
            tvName.text = item.character.name
            ivPreview.transitionName = item.character.imageUrl

            Glide.with(context)
                .load(item.character.imageUrl)
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