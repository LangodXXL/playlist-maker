package com.solyakov.playlist

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.solyakov.playlist.ui.MainScreen
import com.solyakov.playlist.ui.SearchScreen
import com.solyakov.playlist.ui.SettingsScreen

enum class ScreenRoute(val route: String) {
    Start("start"),
    Search("search"),
    Settings("settings")
}

class PlaylistHost(
    private val navController: NavHostController
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
    @Composable
    fun NavGraph() {
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Start.route
        ) {
            composable(ScreenRoute.Start.route) {
                MainScreen(
                    onSearchClick = { navigateToSearch() },
                    onSettingsClick = { navigateToSettings() }
                )
            }
            composable(ScreenRoute.Search.route) {
                SearchScreen(onClick = { navigateBack() })
            }
            composable(ScreenRoute.Settings.route) {
                SettingsScreen(onClick = { navigateBack() })
            }
        }
    }
}
