package com.markstouttech.streamer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "titles")
data class TitleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: String, // "TV" or "Movie"
    val status: String,
    val subscribed: Boolean,
    val posterUrl: String?,
    val rating: Double?,
    val runtime: String?,
    val releaseDate: String?,
    val description: String?,
    val genres: String?,
    val imdbId: String?,
    val lastUpdated: Long
)
