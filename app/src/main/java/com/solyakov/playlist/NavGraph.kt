package com.solyakov.playlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.solyakov.playlist.ui.Screen.AddPlaylistScreen
import com.solyakov.playlist.ui.Screen.FavoriteTracksScreen
import com.solyakov.playlist.ui.Screen.MainScreen
import com.solyakov.playlist.ui.Screen.PlaylistsScreen
import com.solyakov.playlist.ui.Screen.SearchScreen
import com.solyakov.playlist.ui.Screen.SettingsScreen
import com.solyakov.playlist.ui.Screen.TracksInPlaylistScreen
import com.solyakov.playlist.ui.Screen.TrackScreen
import com.solyakov.playlist.ui.view_model.AddPlaylistScreenViewModel
import com.solyakov.playlist.ui.view_model.FavoriteTracksViewModel
import com.solyakov.playlist.ui.view_model.PlaylistsViewModel
import com.solyakov.playlist.ui.view_model.SearchScreenViewModel
import com.solyakov.playlist.ui.view_model.TrackViewModel
import com.solyakov.playlist.ui.view_model.TracksInPlaylistViewModel
import org.koin.androidx.compose.koinViewModel

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
    data object TracksInPlaylist: ScreenRoute("tracks_in_playlist/{playlistId}") {
        fun createRoute(playlistId: Long): String {
            return "tracks_in_playlist/${playlistId}"
        }
    }

    data object AddPlaylist: ScreenRoute("add_playlist")

    data object FavoriteScreen: ScreenRoute("favorite_screen")
}

class PlaylistHost(
    private val navController: NavHostController,
    private val isDarkTheme: Boolean,
    private val onThemeChanged: (Boolean) -> Unit
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

    private fun navigateToTracksInPlaylist(playlistId: Long) {
        navController.navigate(ScreenRoute.TracksInPlaylist.createRoute(playlistId))
    }
    private fun navigateToFavoriteScreen() {
        navController.navigate(ScreenRoute.FavoriteScreen.route)
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
                    onPlaylistsClick = { navigateToPlaylists() },
                    onFavoriteScreenClick = { navigateToFavoriteScreen() }
                )
            }
            composable(ScreenRoute.Search.route) {
                val searchViewModel: SearchScreenViewModel = koinViewModel()
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
                SettingsScreen(
                    onClick = { navigateBack() },
                    isDarkTheme = isDarkTheme,
                    onThemeChanged = { onThemeChanged(it) }
                )
            }
            composable(ScreenRoute.Playlists.route) {
                val playlistsViewModel: PlaylistsViewModel = koinViewModel()
                PlaylistsScreen(
                    modifier = Modifier,
                    playlistsViewModel = playlistsViewModel,
                    addNewPlaylist = {
                        navigateToAddPlaylist()
                    },
                    navigateToPlaylist = {
                        navigateToTracksInPlaylist(playlistId = it)
                    },
                    navigateBack = { navigateBack() }
                )
            }
            composable(ScreenRoute.Track.route) {
                val trackId = it.arguments?.getString("trackId")?.toLong() ?: 0
                val trackViewModel: TrackViewModel = koinViewModel()
                LaunchedEffect(trackId) {
                    trackViewModel.getTrackById(trackId)
                }

                TrackScreen(
                    viewModel = trackViewModel,
                    onBackClick = { navigateBack() }
                )
            }

            composable(ScreenRoute.TracksInPlaylist.route) {
                val playlistId = it.arguments?.getString("playlistId")?.toLong() ?: 0
                val trackInPlaylistViewModel: TracksInPlaylistViewModel = koinViewModel()
                LaunchedEffect(playlistId) {
                    trackInPlaylistViewModel.getAllTracksInPlaylist(playlistId)
                }
                TracksInPlaylistScreen(
                    viewModel = trackInPlaylistViewModel,
                    playlistId = playlistId,
                    onBackClick = { navigateBack() },
                    onTrackClick = { navigateToTrack(it) }
                )
            }

            composable(route = ScreenRoute.AddPlaylist.route) {
                val addPlaylistViewModel: AddPlaylistScreenViewModel = koinViewModel()
                AddPlaylistScreen(
                    viewModel = addPlaylistViewModel,
                    onBackClick = { navigateBack() }
                )
            }

            composable(route = ScreenRoute.FavoriteScreen.route) {
                val favoriteTracksViewModel: FavoriteTracksViewModel = koinViewModel()
                FavoriteTracksScreen(
                    viewModel = favoriteTracksViewModel,
                    onBackClick = { navigateBack() },
                    onTrackClick = { navigateToTrack(it) },
                    onLongTrackClick = {
                        favoriteTracksViewModel.toggleFavorite(it)
                    }
                )
            }
        }
    }
}
