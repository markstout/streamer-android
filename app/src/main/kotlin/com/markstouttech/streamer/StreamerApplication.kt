package com.markstouttech.streamer

import android.app.Application
import androidx.room.Room
import com.markstouttech.streamer.auth.GoogleAuthManager
import com.markstouttech.streamer.data.local.AppDatabase

class StreamerApplication : Application() {
    lateinit var database: AppDatabase
        private set
    
    lateinit var authManager: GoogleAuthManager
        private set

    override fun onCreate() {
        super.onCreate()
        authManager = GoogleAuthManager(this)
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "streamer_database"
        ).build()
    }
}
