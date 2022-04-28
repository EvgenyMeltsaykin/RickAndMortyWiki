package com.wiki.f_character_list.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wiki.cf_core.base.adapter.BaseAdapter
import com.wiki.cf_core.extensions.context
import com.wiki.cf_core.extensions.roundCorners
import com.wiki.cf_data.CharacterDto
import com.wiki.f_character_list.databinding.AdapterCharacterBinding

class CharacterAdapter(
    private val onCharacterClick: (CharacterDto) -> Unit
) : BaseAdapter<CharacterDto, AdapterCharacterBinding>() {

    override fun bind(item: CharacterDto, binding: AdapterCharacterBinding) {
        with(binding) {
            root.setOnClickListener { onCharacterClick(item) }
            tvName.text = item.name
            Glide.with(context)
                .load(item.imageUrl)
                .centerCrop()
                .apply(RequestOptions().roundCorners(16))
                .into(ivPreview)
        }
    }
}