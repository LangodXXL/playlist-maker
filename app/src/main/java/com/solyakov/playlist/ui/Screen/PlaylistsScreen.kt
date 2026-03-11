@file:OptIn(ExperimentalMaterial3Api::class)

package com.solyakov.playlist.ui.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.solyakov.playlist.data.playlist.Playlist
import com.solyakov.playlist.ui.view_model.PlaylistsViewModel

@Composable
fun PlaylistListItem(
    modifier: Modifier = Modifier,
    playlist: Playlist,
    tracksCount: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = {
                onClick()
            })
    ) {
        SubcomposeAsyncImage(
            model = playlist.image,
            contentDescription = "Плейлист ${playlist.name}",
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop,
            loading = {
                Image(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "Album",
                    modifier = Modifier
                        .background(Color.LightGray.copy(alpha = 0.5f))
                        .alpha(0.4f),
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
            }
        )
        Column {
            Text(
                text = playlist.name
            )

            Text(
                text = getTrackCountString(tracksCount),
                fontSize = 12.sp
            )
        }

    }
}
fun getTrackCountString(count: Int): String {
    val pre = "$count "
    return when {
        count % 10 == 1 && count % 100 != 11 -> pre + "трек"
        count % 10 in 2..4 && count % 100 !in 12..14 -> pre + "трека"
        else -> pre + "треков"
    }
}

@Composable
fun PlaylistsScreen(
    modifier: Modifier = Modifier,
    playlistsViewModel: PlaylistsViewModel,
    addNewPlaylist: () -> Unit,
    navigateToPlaylist: (Long) -> Unit,
    navigateBack: () -> Unit
) {
    val playlistsWithCounts by playlistsViewModel.playlistsWithCounts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Text(
                        modifier =  Modifier.padding(horizontal = 12.dp),
                        text = "Плейлисты",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(32.dp)
                            .clickable {
                                navigateBack()
                            },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addNewPlaylist()
                },
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new playlist"
                    )
                },
                shape = CircleShape
            )
        }
    ) {innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) {
            items(playlistsWithCounts) { item ->
                val (playlist, count) = item
                PlaylistListItem(
                    playlist = playlist,
                    onClick = {
                        navigateToPlaylist(playlist.playlistId)
                    },
                    tracksCount = count
                )
            }
        }
    }

}