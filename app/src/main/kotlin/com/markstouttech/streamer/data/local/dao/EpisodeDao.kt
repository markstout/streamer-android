package com.markstouttech.streamer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.markstouttech.streamer.data.local.entities.EpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {
    @Query("SELECT * FROM episodes WHERE title_id = :titleId")
    fun getEpisodesForTitle(titleId: String): Flow<List<EpisodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: EpisodeEntity)

    @Update
    suspend fun updateEpisode(episode: EpisodeEntity)

    @Delete
    suspend fun deleteEpisode(episode: EpisodeEntity)

    @Query("DELETE FROM episodes WHERE title_id = :titleId")
    suspend fun deleteEpisodesForTitle(titleId: String)

    @Query("DELETE FROM episodes WHERE watched = 1")
    suspend fun deleteWatchedEpisodes()
}
