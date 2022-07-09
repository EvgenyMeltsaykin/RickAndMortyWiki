package com.wiki.cf_data

import java.io.Serializable

enum class SearchFeature(val featureName: String) : Serializable {
    CHARACTER("character"),
    EPISODE("episode"),
    LOCATION("location");

    fun isCharacter(): Boolean = this == CHARACTER
    fun isEpisode(): Boolean = this == EPISODE
    fun isLocation(): Boolean = this == LOCATION
}

