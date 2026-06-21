package com.markstouttech.streamer.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.markstouttech.streamer.StreamerApplication
import com.markstouttech.streamer.data.remote.google.GoogleSheetsManager
import com.markstouttech.streamer.data.local.entities.TitleEntity
import com.markstouttech.streamer.data.local.entities.EpisodeEntity
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val app = applicationContext as StreamerApplication
        val database = app.database
        val authManager = app.authManager
        
        val account = authManager.getLastSignedInAccount() ?: return@withContext Result.failure()
        val credential = authManager.getCredential(account)
        val sheetsManager = GoogleSheetsManager(credential)
        
        try {
            val spreadsheetId = sheetsManager.getOrCreateSpreadsheetId()
            Log.d("SyncWorker", "Using spreadsheet ID: $spreadsheetId")
            
            // 1. Fetch remote data
            val remoteTitles = sheetsManager.fetchTitles(spreadsheetId)
            val remoteEpisodes = sheetsManager.fetchEpisodes(spreadsheetId)
            Log.d("SyncWorker", "Fetched ${remoteTitles.size} titles and ${remoteEpisodes.size} episodes")

            // 2. Sync Titles
            Log.d("SyncWorker", "Syncing ${remoteTitles.size} remote titles")
            remoteTitles.forEachIndexed { index, row ->
                if (row.isNotEmpty()) {
                    Log.d("SyncWorker", "Processing title row $index: $row")
                    val title = TitleEntity(
                        id = row.getOrNull(0)?.toString() ?: return@forEachIndexed,
                        title = row.getOrNull(1)?.toString() ?: "Unknown",
                        type = row.getOrNull(2)?.toString() ?: "TV",
                        status = row.getOrNull(3)?.toString() ?: "Plan to Watch",
                        subscribed = row.getOrNull(4)?.toString()?.equals("TRUE", ignoreCase = true) ?: false,
                        posterUrl = row.getOrNull(5)?.toString(),
                        rating = row.getOrNull(6)?.toString()?.toDoubleOrNull(),
                        runtime = row.getOrNull(7)?.toString(),
                        releaseDate = row.getOrNull(8)?.toString(),
                        description = row.getOrNull(9)?.toString(),
                        genres = row.getOrNull(10)?.toString(),
                        imdbId = row.getOrNull(11)?.toString(),
                        lastUpdated = row.getOrNull(12)?.toString()?.toLongOrNull() ?: System.currentTimeMillis()
                    )
                    database.titleDao().insertTitle(title)
                }
            }

            // 3. Sync Episodes
            remoteEpisodes.forEachIndexed { index, row ->
                if (row.isNotEmpty()) {
                    val episode = EpisodeEntity(
                        id = row.getOrNull(0)?.toString() ?: return@forEachIndexed,
                        titleId = row.getOrNull(1)?.toString() ?: return@forEachIndexed,
                        season = row.getOrNull(2)?.toString()?.toIntOrNull() ?: 1,
                        episodeNumber = row.getOrNull(3)?.toString()?.toIntOrNull() ?: 1,
                        episodeTitle = row.getOrNull(4)?.toString() ?: "",
                        airDate = row.getOrNull(5)?.toString(),
                        watched = row.getOrNull(6)?.toString()?.equals("TRUE", ignoreCase = true) ?: false,
                        description = row.getOrNull(7)?.toString()
                    )
                    database.episodeDao().insertEpisode(episode)
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
