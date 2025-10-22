package com.solyakov.playlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.solyakov.playlist.ui.theme.PlaylistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistTheme {
                val navController = rememberNavController()
                val playlistHost = PlaylistHost(navController)
                playlistHost.NavGraph()
            }
        }
    }
}
