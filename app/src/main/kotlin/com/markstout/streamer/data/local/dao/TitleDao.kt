package com.markstout.streamer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.markstout.streamer.data.local.entities.TitleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TitleDao {
    @Query("SELECT * FROM titles")
    fun getAllTitles(): Flow<List<TitleEntity>>

    @Query("SELECT * FROM titles WHERE id = :id")
    suspend fun getTitleById(id: String): TitleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTitle(title: TitleEntity)

    @Update
    suspend fun updateTitle(title: TitleEntity)

    @Delete
    suspend fun deleteTitle(title: TitleEntity)

    @Query("DELETE FROM titles")
    suspend fun deleteAllTitles()

    @Query("SELECT COUNT(*) FROM titles")
    fun getTitleCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM titles WHERE status = 'Watched'")
    fun getWatchedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM titles WHERE status = 'Unavailable'")
    fun getUnavailableCount(): Flow<Int>
}
