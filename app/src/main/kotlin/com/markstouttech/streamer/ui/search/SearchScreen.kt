package com.markstouttech.streamer.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.markstouttech.streamer.ui.search.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBackClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var searchType by remember { mutableStateOf("TV") } // "TV" or "Movie"
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search ${if (searchType == "TV") "TV Shows" else "Movies"}") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { viewModel.search(query, searchType) }),
                        trailingIcon = {
                            IconButton(onClick = { viewModel.search(query, searchType) }) {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = searchType == "TV",
                    onClick = { searchType = "TV" },
                    label = { Text("TV Shows") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = searchType == "Movie",
                    onClick = { searchType = "Movie" },
                    label = { Text("Movies") }
                )
            }

            if (isSearching) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (searchType == "Movie" && searchResults.isEmpty() && !isSearching) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Movie search via TMDB is coming soon!")
                }
            }

            LazyColumn {
                items(searchResults) { result ->
                    ListItem(
                        headlineContent = { Text(result.show.name) },
                        supportingContent = { Text(result.show.status) },
                        trailingContent = {
                            IconButton(onClick = { viewModel.addTitle(result) }) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    )
                }
            }
        }
    }
}
