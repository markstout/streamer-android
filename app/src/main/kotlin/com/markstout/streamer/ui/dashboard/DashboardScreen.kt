package com.markstout.streamer.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markstout.streamer.data.local.entities.TitleEntity
import com.markstout.streamer.ui.components.TitleCard
import com.markstout.streamer.ui.dashboard.viewmodel.DashboardViewModel
import com.markstout.streamer.util.BuildUtils
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.compose.ui.platform.LocalContext
import com.markstout.streamer.sync.SyncWorker
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    titles: List<TitleEntity>,
    onAddClick: () -> Unit,
    onTitleClick: (TitleEntity) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showAboutDialog by remember { mutableStateOf(false) }

    val titleCount by viewModel.titleCount.collectAsState()
    val watchedCount by viewModel.watchedCount.collectAsState()
    val unavailableCount by viewModel.unavailableCount.collectAsState()

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About Streamer") },
            text = {
                Column {
                    Text("Version: 1.0")
                    Text("Build: ${BuildUtils.getBuildTimestamp()}")
                    Text("Copyright 2026 Mark A. Stout")
                    Text("MIT License")
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Streamer Statistics", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                NavigationDrawerItem(
                    label = { Text("Total Titles: $titleCount") },
                    selected = false,
                    onClick = {}
                )
                NavigationDrawerItem(
                    label = { Text("Watched: $watchedCount") },
                    selected = false,
                    onClick = {}
                )
                NavigationDrawerItem(
                    label = { Text("Unavailable: $unavailableCount") },
                    selected = false,
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Delete Watched Episodes") },
                    icon = { Icon(Icons.Default.Delete, contentDescription = null) },
                    selected = false,
                    onClick = { 
                        viewModel.deleteWatched()
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("About App") },
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    selected = false,
                    onClick = { 
                        showAboutDialog = true
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Streamer") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                    IconButton(onClick = { 
                        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
                        WorkManager.getInstance(context).enqueue(syncRequest)
                    }) {
                        Icon(Icons.Default.Sync, contentDescription = "Sync")
                    }
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onAddClick) {
                    Icon(Icons.Default.Add, contentDescription = "Add Title")
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(titles) { title ->
                        TitleCard(title = title, onClick = { onTitleClick(title) })
                    }
                }
            }
        }
    }
}
