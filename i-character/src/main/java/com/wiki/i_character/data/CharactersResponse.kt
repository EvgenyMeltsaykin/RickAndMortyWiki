package com.wiki.i_character.data

import com.google.gson.annotations.SerializedName
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.LifeStatus
import com.wiki.cf_data.common.SimpleData
import com.wiki.cf_extensions.capitalize
import com.wiki.cf_network.data.PaginationInfo

data class CharactersResponse(
    @SerializedName("results") val result: List<CharacterInfoResponse>,
    @SerializedName("info") val info: PaginationInfo
)

data class SimpleResponse(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
) {
    fun toDtoSimpleData(): SimpleData = SimpleData(
        value = name,
        url = url
    )
}

data class CharacterInfoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("origin") val origin: SimpleResponse,
    @SerializedName("location") val location: SimpleResponse,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("episode") val episodeUrls: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String
) {
    fun toCharacterDto(): CharacterDto = CharacterDto(
        id = id,
        name = name,
        lifeStatus = LifeStatus.getByStatus(status),
        species = species.capitalize(),
        gender = gender.capitalize(),
        imageUrl = imageUrl,
        originLocation = origin.toDtoSimpleData(),
        lastKnownLocation = location.toDtoSimpleData(),
        episodeIds = episodeUrls.map { it.substringAfterLast("/") }
    )
}


