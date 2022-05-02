package com.wiki.cf_data

import java.io.Serializable

enum class SearchFeature(val featureName: String) : Serializable {
    CHARACTER("character"),
    EPISODE("episode"),
    LOCATION("location");
}

fun SearchFeature.isCharacter(): Boolean = this == SearchFeature.CHARACTER
fun SearchFeature.isEpisode(): Boolean = this == SearchFeature.EPISODE
fun SearchFeature.isLocation(): Boolean = this == SearchFeature.LOCATION