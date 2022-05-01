package com.wiki.i_episode.data

import com.google.gson.annotations.SerializedName
import com.wiki.cf_data.EpisodeDto
import com.wiki.cf_network.data.PaginationInfo
import com.wiki.cf_network.util.pagination.PaginationItem

data class EpisodesResponse(
    @SerializedName("results") val result: List<EpisodeInfoResponse>,
    @SerializedName("info") val info: PaginationInfo
) : PaginationItem {
    override val paginationInfo: PaginationInfo
        get() = info
}

data class EpisodeInfoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("air_date") val airDate: String,
    @SerializedName("episode") val shortEpisode: String,
    @SerializedName("characters") val charactersUrl: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String,
) {
    fun toEpisodeDto(): EpisodeDto = EpisodeDto(
        id = id.toString(),
        name = name,
        airDate = airDate,
        shortEpisode = shortEpisode,
        charactersIds = charactersUrl.map { it.substringAfterLast("/") },
        created = created
    )
}


