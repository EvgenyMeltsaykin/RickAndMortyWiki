package com.wiki.cf_network.data

import com.google.gson.annotations.SerializedName

data class PaginationInfo(
    @SerializedName("count") val count: Int? = null,
    @SerializedName("pages") val pages: Int? = null,
    @SerializedName("next") val next: String? = null,
    @SerializedName("prev") val prev: String? = null
)