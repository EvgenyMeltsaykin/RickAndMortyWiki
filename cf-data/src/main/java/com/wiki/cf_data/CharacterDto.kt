package com.wiki.cf_data

import com.wiki.cf_data.common.SimpleData

enum class LifeStatus(val status: String) {
    DEAD("Dead"),
    ALIVE("Alive"),
    UNKNOWN("Unknown"),
    OTHER("Other");

    companion object {
        fun getByStatus(status: String): LifeStatus {
            return when {
                status.contains(DEAD.status, ignoreCase = true) -> DEAD
                status.contains(ALIVE.status, ignoreCase = true) -> ALIVE
                status.contains(UNKNOWN.status, ignoreCase = true) -> UNKNOWN
                else -> OTHER
            }
        }
    }
}

data class CharacterDto(
    val id: Int,
    val name: String,
    val lifeStatus: LifeStatus,
    val species: String,
    val gender: String,
    val imageUrl: String,
    val originLocation: SimpleData,
    val lastKnownLocation: SimpleData,
    val episodeIds: List<String>
)