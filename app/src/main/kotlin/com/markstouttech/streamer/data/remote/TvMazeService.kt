package com.markstouttech.streamer.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvMazeService {
    @GET("search/shows")
    suspend fun searchShows(@Query("q") query: String): List<TvMazeSearchResponse>

    @GET("shows/{id}/episodes")
    suspend fun getEpisodes(@Path("id") showId: Int): List<TvMazeEpisode>
}

data class TvMazeSearchResponse(
    val score: Double,
    val show: TvMazeShow
)

data class TvMazeShow(
    val id: Int,
    val name: String,
    val type: String,
    val genres: List<String>,
    val status: String,
    val premiered: String?,
    val officialSite: String?,
    val rating: TvMazeRating,
    val externals: TvMazeExternals,
    val image: TvMazeImage?,
    val summary: String?
)

data class TvMazeRating(val average: Double?)
data class TvMazeExternals(val tvrage: Int?, val thetvdb: Int?, val imdb: String?)
data class TvMazeImage(val medium: String, val original: String)

data class TvMazeEpisode(
    val id: Int,
    val name: String,
    val season: Int,
    val number: Int,
    val airdate: String?,
    val summary: String?
)
