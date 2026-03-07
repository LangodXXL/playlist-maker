package com.solyakov.playlist.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.solyakov.playlist.PlaylistHost
import com.solyakov.playlist.ui.theme.PlaylistTheme
import com.solyakov.playlist.ui.view_model.AddPlaylistScreenViewModel
import com.solyakov.playlist.ui.view_model.PlaylistsViewModel
import com.solyakov.playlist.ui.view_model.SearchScreenViewModel
import com.solyakov.playlist.ui.view_model.TracksInPlaylistViewModel
import com.solyakov.playlist.ui.view_model.TrackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchScreenViewModel by viewModel()
    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private val trackViewModel: TrackViewModel by viewModel()
    private val addPlaylistViewModel: AddPlaylistScreenViewModel by viewModel()
    private val trackInPlaylistViewModel: TracksInPlaylistViewModel by viewModel()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistTheme {
                val navController = rememberNavController()
                val playlistHost = PlaylistHost(
                    navController,
                    searchViewModel,
                    playlistsViewModel,
                    trackViewModel,
                    addPlaylistViewModel,
                    trackInPlaylistViewModel
                )
                playlistHost.NavGraph()
            }
        }
    }
}
