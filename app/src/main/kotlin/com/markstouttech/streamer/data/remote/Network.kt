package com.markstouttech.streamer.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private const val TVMAZE_BASE_URL = "https://api.tvmaze.com/"
    
    val tvMazeService: TvMazeService by lazy {
        Retrofit.Builder()
            .baseUrl(TVMAZE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TvMazeService::class.java)
    }
}
