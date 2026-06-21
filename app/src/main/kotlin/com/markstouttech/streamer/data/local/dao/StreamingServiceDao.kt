package com.markstouttech.streamer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.markstouttech.streamer.data.local.entities.StreamingServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StreamingServiceDao {
    @Query("SELECT * FROM streaming_services")
    fun getAllServices(): Flow<List<StreamingServiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: StreamingServiceEntity)

    @Update
    suspend fun updateService(service: StreamingServiceEntity)
}
