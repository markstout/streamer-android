package com.markstout.streamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.markstout.streamer.auth.GoogleAuthManager
import com.markstout.streamer.data.local.entities.TitleEntity
import com.markstout.streamer.ui.dashboard.DashboardScreen
import com.markstout.streamer.ui.dashboard.viewmodel.DashboardViewModel
import com.markstout.streamer.ui.detail.DetailScreen
import com.markstout.streamer.ui.search.SearchScreen
import com.markstout.streamer.ui.theme.StreamerTheme

class MainActivity : ComponentActivity() {
    private lateinit var authManager: GoogleAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authManager = GoogleAuthManager(this)
        val database = (application as StreamerApplication).database

        setContent {
            StreamerTheme {
                var account by remember { mutableStateOf(authManager.getLastSignedInAccount()) }
                
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    try {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Optionally show a snackbar or toast
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (account == null) {
                        LoginScreen { launcher.launch(authManager.getGoogleSignInClient().signInIntent) }
                    } else {
                        StreamerApp(viewModel = viewModel { 
                            DashboardViewModel(database.titleDao(), database.episodeDao()) 
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onLoginClick) {
            Text("Sign in with Google")
        }
    }
}

@Composable
fun StreamerApp(viewModel: DashboardViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }
    var selectedTitle by remember { mutableStateOf<TitleEntity?>(null) }
    val titles by viewModel.titles.collectAsState()

    when (val screen = currentScreen) {
        is Screen.Dashboard -> {
            DashboardScreen(
                viewModel = viewModel,
                titles = titles,
                onAddClick = { currentScreen = Screen.Search },
                onTitleClick = { title ->
                    selectedTitle = title
                    currentScreen = Screen.Detail
                }
            )
        }
        is Screen.Search -> {
            SearchScreen(
                onBackClick = { currentScreen = Screen.Dashboard },
                onAddTitle = { /* TODO */ }
            )
        }
        is Screen.Detail -> {
            selectedTitle?.let { title ->
                DetailScreen(
                    title = title,
                    onBackClick = { currentScreen = Screen.Dashboard }
                )
            }
        }
    }
}

sealed class Screen {
    object Dashboard : Screen()
    object Search : Screen()
    object Detail : Screen()
}
