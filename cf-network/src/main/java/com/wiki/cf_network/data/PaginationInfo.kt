package com.wiki.cf_network.data

import com.google.gson.annotations.SerializedName

data class PaginationInfo(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?
)