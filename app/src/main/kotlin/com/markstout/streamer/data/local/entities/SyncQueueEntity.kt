package com.markstout.streamer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val action: String, // INSERT, UPDATE, DELETE
    val entityType: String, // TITLE, EPISODE
    val entityId: String,
    val timestamp: Long
)
