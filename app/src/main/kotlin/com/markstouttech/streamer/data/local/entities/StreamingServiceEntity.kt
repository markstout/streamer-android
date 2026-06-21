package com.markstouttech.streamer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streaming_services")
data class StreamingServiceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val enabled: Boolean
)
