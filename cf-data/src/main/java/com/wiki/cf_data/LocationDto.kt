package com.wiki.cf_data

import java.io.Serializable

data class LocationDto(
    val id: String,
    val name: String,
    val type: String,
    val dimension: String,
    val residentCharactersIds: List<String>,
    val created: String
) : Serializable