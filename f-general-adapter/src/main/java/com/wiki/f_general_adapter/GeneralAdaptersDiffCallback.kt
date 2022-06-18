package com.wiki.f_general_adapter

import androidx.recyclerview.widget.DiffUtil

fun getGeneralAdaptersDiffCallback() = object : DiffUtil.ItemCallback<GeneralAdapterUi>() {
    override fun areItemsTheSame(oldItem: GeneralAdapterUi, newItem: GeneralAdapterUi):Boolean {
        if (oldItem is GeneralAdapterUi.Character && newItem is GeneralAdapterUi.Character){
            return oldItem.character.id == newItem.character.id
        }

        if (oldItem is GeneralAdapterUi.Location && newItem is GeneralAdapterUi.Location){
            return oldItem.location.id == newItem.location.id
        }

        if (oldItem is GeneralAdapterUi.Episode && newItem is GeneralAdapterUi.Episode){
            return oldItem.episode.id == newItem.episode.id
        }

        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: GeneralAdapterUi, newItem: GeneralAdapterUi):Boolean{
        return oldItem == newItem
    }
}