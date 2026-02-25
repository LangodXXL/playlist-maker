package com.solyakov.playlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.solyakov.playlist.ui.MainScreen
import com.solyakov.playlist.ui.PlaylistsScreen
import com.solyakov.playlist.ui.SearchScreen
import com.solyakov.playlist.ui.SettingsScreen
import com.solyakov.playlist.ui.view_model.PlaylistsViewModel
import com.solyakov.playlist.ui.view_model.SearchScreenViewModel

enum class ScreenRoute(val route: String) {
    Start("start"),
    Search("search"),
    Settings("settings"),

    Playlists("playlists")
}

class PlaylistHost(
    private val navController: NavHostController,
    private val searchViewModel: SearchScreenViewModel,
    private val playlistsViewModel: PlaylistsViewModel
) {
    private fun navigateToSearch() {
        navController.navigate(ScreenRoute.Search.route)
    }

    private fun navigateToSettings() {
        navController.navigate(ScreenRoute.Settings.route)
    }

    private fun navigateBack() {
        navController.popBackStack()
    }

    private fun navigateToPlaylists() {
        navController.navigate(ScreenRoute.Playlists.route)
    }
    @Composable
    fun NavGraph() {
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Start.route
        ) {
            composable(ScreenRoute.Start.route) {
                MainScreen(
                    onSearchClick = { navigateToSearch() },
                    onSettingsClick = { navigateToSettings() },
                    onPlaylistsClick = { navigateToPlaylists() }
                )
            }
            composable(ScreenRoute.Search.route) {
                SearchScreen(
                    onClick = { navigateBack() },
                    modifier = Modifier.fillMaxSize(),
                    viewModel = searchViewModel)
            }
            composable(ScreenRoute.Settings.route) {
                SettingsScreen(onClick = { navigateBack() })
            }
            composable(ScreenRoute.Playlists.route) {
                PlaylistsScreen(
                    modifier = Modifier,
                    playlistsViewModel = playlistsViewModel,
                    addNewPlaylist = { },
                    navigateToPlaylist = { },
                    navigateBack = { navigateBack() }
                )

            }
        }
    }
}
