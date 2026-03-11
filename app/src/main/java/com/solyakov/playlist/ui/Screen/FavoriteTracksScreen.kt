@file:OptIn(ExperimentalMaterial3Api::class)

package com.solyakov.playlist.ui.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.solyakov.playlist.ui.view_model.FavoriteTracksViewModel


@Composable
fun FavoriteTracksScreen(
    viewModel: FavoriteTracksViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Long) -> Unit
) {
    val tracks by viewModel.tracks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Избранные треки"
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            onBackClick()
                        },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            )
        },

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(
                items = tracks,
                key = { track -> track.trackId }
            ) { track ->
                TrackListItemIn(
                    track = track,
                    onClick = {
                        onTrackClick(it)
                    }
                )
            }
        }

    }
}





