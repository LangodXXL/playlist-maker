package com.solyakov.playlist.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.solyakov.playlist.PlaylistHost
import com.solyakov.playlist.ui.theme.PlaylistTheme
import com.solyakov.playlist.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistTheme {
                val navController = rememberNavController()
                val playlistHost = PlaylistHost(navController, searchViewModel)
                playlistHost.NavGraph()
            }
        }
    }
}
