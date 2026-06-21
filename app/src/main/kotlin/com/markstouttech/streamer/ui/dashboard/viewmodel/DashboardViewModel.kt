package com.markstouttech.streamer.ui.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markstouttech.streamer.data.local.dao.EpisodeDao
import com.markstouttech.streamer.data.local.dao.TitleDao
import com.markstouttech.streamer.data.local.entities.TitleEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val titleDao: TitleDao,
    private val episodeDao: EpisodeDao
) : ViewModel() {

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    fun setSyncing(syncing: Boolean) {
        _isSyncing.value = syncing
    }

    val titles: StateFlow<List<TitleEntity>> = titleDao.getAllTitles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val titleCount: StateFlow<Int> = titleDao.getTitleCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val watchedCount: StateFlow<Int> = titleDao.getWatchedCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val unavailableCount: StateFlow<Int> = titleDao.getUnavailableCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun deleteWatched() {
        viewModelScope.launch {
            episodeDao.deleteWatchedEpisodes()
            // Also might want to update titles if they were fully watched
        }
    }
}
