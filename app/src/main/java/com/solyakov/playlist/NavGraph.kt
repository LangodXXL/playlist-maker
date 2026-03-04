package com.solyakov.playlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.solyakov.playlist.ui.Screen.AddPlaylistScreen
import com.solyakov.playlist.ui.Screen.MainScreen
import com.solyakov.playlist.ui.Screen.PlaylistsScreen
import com.solyakov.playlist.ui.Screen.SearchScreen
import com.solyakov.playlist.ui.Screen.SettingsScreen
import com.solyakov.playlist.ui.Screen.TrackScreen
import com.solyakov.playlist.ui.view_model.AddPlaylistScreenViewModel
import com.solyakov.playlist.ui.view_model.PlaylistsViewModel
import com.solyakov.playlist.ui.view_model.SearchScreenViewModel
import com.solyakov.playlist.ui.view_model.TrackViewModel

sealed class ScreenRoute(val route: String) {
    data object Start: ScreenRoute("start")
    data object Search: ScreenRoute("search")
    data object Settings: ScreenRoute("settings")

    data object Playlists: ScreenRoute("playlists")

    data object Track: ScreenRoute("track/{trackId}") {
        fun createRoute(trackId: Long): String {
            return "track/${trackId}"
        }
    }

    data object AddPlaylist: ScreenRoute("add_playlist")
}

class PlaylistHost(
    private val navController: NavHostController,
    private val searchViewModel: SearchScreenViewModel,
    private val playlistsViewModel: PlaylistsViewModel,
    private val trackViewModel: TrackViewModel,
    private val addPlaylistViewModel: AddPlaylistScreenViewModel
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

    private fun navigateToTrack(trackId: Long) {
        navController.navigate(ScreenRoute.Track.createRoute(trackId))
    }
    private fun navigateToAddPlaylist() {
        navController.navigate(ScreenRoute.AddPlaylist.route)
    }

    @Composable
    fun NavGraph() {
        NavHost(
            navController =  navController,
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
                    viewModel = searchViewModel,
                    onTrackClick = {
                        navigateToTrack(trackId = it)
                    }
                    )
            }
            composable(ScreenRoute.Settings.route) {
                SettingsScreen(onClick = { navigateBack() })
            }
            composable(ScreenRoute.Playlists.route) {
                PlaylistsScreen(
                    modifier = Modifier,
                    playlistsViewModel = playlistsViewModel,
                    addNewPlaylist = {
                        navigateToAddPlaylist()
                    },
                    navigateToPlaylist = { },
                    navigateBack = { navigateBack() }
                )
            }
            composable(ScreenRoute.Track.route) {
                val trackId = it.arguments?.getString("trackId")?.toLong() ?: 0
                LaunchedEffect(trackId) {
                    trackViewModel.getTrackById(trackId)
                }
                TrackScreen(
                    viewModel = trackViewModel,
                    onBackClick = { navigateBack() }
                )
            }

            composable(route = ScreenRoute.AddPlaylist.route) {
                AddPlaylistScreen(
                    viewModel = addPlaylistViewModel,
                    onBackClick = { navigateBack() }
                )
            }
        }
    }
}
