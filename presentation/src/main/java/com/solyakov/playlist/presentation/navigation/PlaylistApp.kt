package com.solyakov.playlist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.solyakov.playlist.presentation.navigation.PlaylistHost
import com.solyakov.playlist.presentation.theme.PlaylistTheme

@Composable
fun PlaylistApp(
    isDarkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    PlaylistTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()

        val playlistHost = PlaylistHost(
            navController = navController,
            isDarkTheme = isDarkTheme,
            onThemeChanged = onThemeChanged
        )

        playlistHost.NavGraph()
    }
}