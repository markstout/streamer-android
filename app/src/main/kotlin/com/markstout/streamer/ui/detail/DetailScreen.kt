package com.markstout.streamer.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.markstout.streamer.data.local.entities.TitleEntity

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
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = title.posterUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title.title,
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row {
                    Text(text = "Rating: ${title.rating ?: "N/A"}")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Runtime: ${title.runtime ?: "N/A"}")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = title.description ?: "No description available.",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                // Episodes list for TV shows would go here
            }
        }
    }
}
