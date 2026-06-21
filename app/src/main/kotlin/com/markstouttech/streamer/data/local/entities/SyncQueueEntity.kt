package com.markstouttech.streamer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val entityType: String,
    val entityId: String,
    val operation: String, // INSERT, UPDATE, DELETE
    val timestamp: Long = System.currentTimeMillis()
)
