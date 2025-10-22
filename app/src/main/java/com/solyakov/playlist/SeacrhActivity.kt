package com.solyakov.playlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.solyakov.playlist.presentation.SearchScreen
import com.solyakov.playlist.ui.theme.PlaylistTheme

class SeacrhActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistTheme {
                SearchScreen({})
            }
        }
    }
}