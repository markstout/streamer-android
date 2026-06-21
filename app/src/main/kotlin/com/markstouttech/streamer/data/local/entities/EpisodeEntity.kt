package com.markstouttech.streamer.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "episodes",
    foreignKeys = [
        ForeignKey(
            entity = TitleEntity::class,
            parentColumns = ["id"],
            childColumns = ["title_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["title_id"])]
)
data class EpisodeEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title_id") val titleId: String,
    val season: Int,
    val episodeNumber: Int,
    val episodeTitle: String,
    val airDate: String?,
    val watched: Boolean,
    val description: String?
)
