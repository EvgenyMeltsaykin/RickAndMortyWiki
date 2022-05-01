package com.wiki.i_location.data

import com.google.gson.annotations.SerializedName
import com.wiki.cf_data.LocationDto
import com.wiki.cf_extensions.capitalize
import com.wiki.cf_network.data.PaginationInfo

data class LocationsResponse(
    @SerializedName("results") val result: List<LocationInfoResponse>,
    @SerializedName("info") val info: PaginationInfo
)

data class LocationInfoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("dimension") val dimension: String,
    @SerializedName("residents") val residentsUrl: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String,
) {
    fun toLocationDto(): LocationDto = LocationDto(
        id = id.toString(),
        name = name,
        type = type.capitalize(),
        dimension = dimension.capitalize(),
        residentCharactersIds = residentsUrl.map { it.substringAfterLast("/") },
        created = created
    )
}


