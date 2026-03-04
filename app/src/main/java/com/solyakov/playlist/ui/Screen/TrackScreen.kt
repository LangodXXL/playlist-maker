@file:OptIn(ExperimentalMaterial3Api::class)

package com.solyakov.playlist.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.solyakov.playlist.R
import com.solyakov.playlist.data.playlist.Playlist
import com.solyakov.playlist.ui.view_model.TrackScreenState
import com.solyakov.playlist.ui.view_model.TrackViewModel
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackScreen(
    viewModel: TrackViewModel,
    onBackClick: () -> Unit
) {
    val trackScreenState by viewModel.screenState.collectAsState()
    var isLoaded by remember { mutableStateOf(false) }

    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val playlists by viewModel.getAllPlaylists().collectAsState(emptyList())


    when (trackScreenState) {
        is TrackScreenState.Error -> {
            Text(text = "Error: ${(trackScreenState as TrackScreenState.Error).message}")
        }

        is TrackScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }

        is TrackScreenState.Success -> {
            val track = (trackScreenState as TrackScreenState.Success).track
            LaunchedEffect(track.trackId) {
                isLoaded = false
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            ) {
                TopAppBar(
                    title = {
                        Text(text = "")
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.clickable {
                                onBackClick()
                            }
                        )
                    }
                )

                Box(
                    modifier = Modifier
                        .padding(24.dp)
                        .size(312.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    if (!isLoaded) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmer()
                                .background(Color.Gray.copy(alpha = 0.3f))
                        )
                    }


                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(track.image)
                            .crossfade(true)
                            .listener(
                                onSuccess = { _, _ ->
                                    isLoaded = true
                                },
                                onError = { _, _ ->
                                    isLoaded = true
                                }
                            ).build(),
                        contentDescription = "Album",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(if (isLoaded) 1f else 0f)
                    )
                }
                Text(
                    text = track.trackName,
                    fontSize = 22.sp
                )

                Text(
                    text = track.artistName,
                    fontSize = 14.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FloatingActionButton(
                        onClick = {
                            showBottomSheet = true
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.AddToPhotos,
                                contentDescription = "Add to favorites"
                            )
                        }
                    )
                    PlaylistsSheet(
                        showBottomSheet = showBottomSheet,
                        sheetState = sheetState,
                        onDismiss = {
                            showBottomSheet = false
                        },
                        playlists = playlists
                    )
                    FloatingActionButton(
                        onClick = {
                            viewModel.addTrackToFavorite(track)
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Add to favorites"
                            )
                        }
                    )
                }
            }
        }

    }
}


@Composable
fun PlaylistsSheet(
    showBottomSheet: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    playlists: List<Playlist>
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
               onDismiss()
            },
            sheetState = sheetState,
            dragHandle = {
                BottomSheetDefaults.DragHandle()
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                items(count = playlists.size)
                    {
                    Column {
                        PlaylistItem(
                            playlist = playlists[it],
                            onClick = {
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PlaylistItem(
    playlist: Playlist,
    onClick: () -> Unit
) {
    Row() {
        AsyncImage(
            model = playlist.image,
            placeholder = painterResource(R.drawable.vector),
            contentDescription = "Playlist",
            modifier = Modifier
                .size(45.dp),
            contentScale = ContentScale.Crop
        )
        Column {
            Text(
                text = playlist.name
            )
            Text(
                text = "${playlist.tracks.size} Треков"
            )
        }
    }
}