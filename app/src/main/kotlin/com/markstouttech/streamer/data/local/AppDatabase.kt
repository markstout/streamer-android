package com.markstouttech.streamer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.markstouttech.streamer.data.local.dao.EpisodeDao
import com.markstouttech.streamer.data.local.dao.TitleDao
import com.markstouttech.streamer.data.local.entities.EpisodeEntity
import com.markstouttech.streamer.data.local.entities.StreamingServiceEntity
import com.markstouttech.streamer.data.local.entities.SyncQueueEntity
import com.markstouttech.streamer.data.local.entities.TitleEntity
import com.markstouttech.streamer.data.local.dao.StreamingServiceDao

@Database(
    entities = [
        TitleEntity::class,
        EpisodeEntity::class,
        StreamingServiceEntity::class,
        SyncQueueEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun titleDao(): TitleDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun streamingServiceDao(): StreamingServiceDao
}
