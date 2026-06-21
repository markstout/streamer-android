package com.markstouttech.streamer.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markstouttech.streamer.data.local.entities.TitleEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    title: TitleEntity,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(text = "Type: ${title.type}")
            Text(text = "Status: ${title.status}")
            Text(text = "Rating: ${title.rating ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title.description ?: "No description available.")
        }
    }
}
