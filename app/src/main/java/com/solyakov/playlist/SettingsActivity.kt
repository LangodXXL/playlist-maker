package com.solyakov.playlist

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.solyakov.playlist.presentation.MainScreen
import com.solyakov.playlist.presentation.SettingsScreen
import com.solyakov.playlist.ui.theme.PlaylistTheme

class SettingsActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistTheme { ->
                    SettingsScreen({})
            }
        }
    }
}