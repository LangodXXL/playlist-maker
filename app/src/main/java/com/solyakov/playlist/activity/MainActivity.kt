package com.solyakov.playlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.solyakov.playlist.domain.repository.SettingsRepository
import com.solyakov.playlist.presentation.navigation.PlaylistApp
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val settingsRepository: SettingsRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val isDarkTheme by settingsRepository.isDarkTheme.collectAsState(initial = false)
            val scope = rememberCoroutineScope()

            PlaylistApp(
                isDarkTheme = isDarkTheme,
                onThemeChanged = { newValue ->
                    scope.launch {
                        settingsRepository.saveTheme(newValue)
                    }
                }
            )
        }
    }
}