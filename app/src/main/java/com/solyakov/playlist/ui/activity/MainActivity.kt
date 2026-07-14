package com.solyakov.playlist.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.solyakov.playlist.PlaylistHost
import com.solyakov.playlist.data.database.SettingsRepository
import com.solyakov.playlist.ui.theme.PlaylistTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingsRepository: SettingsRepository by inject()
        val initialTheme = runBlocking { settingsRepository.isDarkTheme.first() }

        enableEdgeToEdge()
        setContent {
            val isDarkTheme by settingsRepository.isDarkTheme.collectAsState(initial = initialTheme)
            val scope = rememberCoroutineScope()
            PlaylistTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                val playlistHost = PlaylistHost(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    onThemeChanged = { newValue ->
                        scope.launch {
                            settingsRepository.saveTheme(newValue, applicationContext) }
                    }
                        )
                        playlistHost.NavGraph()
                    }
        }
    }
}
