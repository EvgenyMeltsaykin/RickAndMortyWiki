package com.wiki.cf_data

import java.io.Serializable

data class EpisodeDto(
    val id: String,
    val name: String,
    val airDate: String,
    val shortEpisode: String,
    val charactersIds: List<String>,
    val created: String,
) : Serializable