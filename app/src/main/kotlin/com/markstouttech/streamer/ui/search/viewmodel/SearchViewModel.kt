package com.markstouttech.streamer.ui.search.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markstouttech.streamer.data.local.dao.EpisodeDao
import com.markstouttech.streamer.data.local.dao.TitleDao
import com.markstouttech.streamer.data.local.entities.EpisodeEntity
import com.markstouttech.streamer.data.local.entities.TitleEntity
import com.markstouttech.streamer.data.remote.Network
import com.markstouttech.streamer.data.remote.TvMazeSearchResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val titleDao: TitleDao,
    private val episodeDao: EpisodeDao
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<TvMazeSearchResponse>>(emptyList())
    val searchResults: StateFlow<List<TvMazeSearchResponse>> = _searchResults

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    fun search(query: String, type: String) {
        if (query.isBlank()) return
        
        viewModelScope.launch {
            _isSearching.value = true
            try {
                if (type == "TV") {
                    val results = Network.tvMazeService.searchShows(query)
                    _searchResults.value = results
                } else {
                    // TODO: Implement Movie Search (TMDB)
                    _searchResults.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun addTitle(result: TvMazeSearchResponse) {
        val show = result.show
        viewModelScope.launch {
            val titleEntity = TitleEntity(
                id = show.id.toString(),
                title = show.name,
                type = "TV",
                status = "Plan to Watch",
                subscribed = false,
                posterUrl = show.image?.medium,
                rating = show.rating.average,
                runtime = null, // TVMaze has averageRuntime but we'll leave it for now
                releaseDate = show.premiered,
                description = show.summary?.replace(Regex("<.*?>"), ""),
                genres = show.genres.joinToString(", "),
                imdbId = show.externals.imdb,
                lastUpdated = System.currentTimeMillis()
            )
            titleDao.insertTitle(titleEntity)

            // Fetch and insert episodes
            try {
                val episodes = Network.tvMazeService.getEpisodes(show.id)
                episodes.forEach { ep ->
                    episodeDao.insertEpisode(
                        EpisodeEntity(
                            id = ep.id.toString(),
                            titleId = show.id.toString(),
                            season = ep.season,
                            episodeNumber = ep.number,
                            episodeTitle = ep.name,
                            airDate = ep.airdate,
                            watched = false,
                            description = ep.summary?.replace(Regex("<.*?>"), "")
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
