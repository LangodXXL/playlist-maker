@file:OptIn(ExperimentalMaterial3Api::class)

package com.solyakov.playlist.ui.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.solyakov.playlist.R
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.data.playlist.Playlist
import com.solyakov.playlist.ui.view_model.TrackScreenState
import com.solyakov.playlist.ui.view_model.TrackViewModel
import com.valentinilk.shimmer.shimmer



@SuppressLint("DefaultLocale")
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

    val currentPositionInTrack by viewModel.currentPosition
    val isPlaying by viewModel.isPlaying
    val durationInTrack by viewModel.duration

    var isDragging by remember { mutableStateOf(false) } // для ползунка
    var sliderPosition by remember { mutableStateOf(0f) }
    LaunchedEffect(currentPositionInTrack) {
        if (!isDragging) {
            sliderPosition = currentPositionInTrack
        }
    }


    when (trackScreenState) {
        is TrackScreenState.Error -> {
            Text(
                text = "Error: ${(trackScreenState as TrackScreenState.Error).message}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
            Log.d("TrackScreen", "Track: $track")
            LaunchedEffect(track.trackId) {
                isLoaded = false
            }
            PlaylistsSheet(
                track = track,
                showBottomSheet = showBottomSheet,
                sheetState = sheetState,
                onDismiss = {
                    showBottomSheet = false
                },
                playlists = playlists,
                addTrackToPlaylist = {
                        track, playlistId ->
                    viewModel.addTrackInPlaylist(track, playlistId)
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
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
                            },
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
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
                                onStart = {
                                    isLoaded = false
                                },
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
                    )
                }
                Text(
                    text = track.trackName,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = track.artistName,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Slider(
                        value = sliderPosition,
                        onValueChange = {
                            isDragging = true
                            sliderPosition = it
                        },
                        onValueChangeFinished = {
                            viewModel.seekTo(sliderPosition)
                            isDragging = false
                                                },
                        valueRange = 0f..durationInTrack,
                        modifier = Modifier.fillMaxWidth(),
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(MaterialTheme.colorScheme.onSurface, CircleShape)
                                    .shadow(elevation = 2.dp, shape = CircleShape),
                            )

                        },
                        track = { sliderState ->
                            SliderDefaults.Track(
                                sliderState = sliderState,
                                modifier = Modifier.height(4.dp),
                                thumbTrackGapSize = 0.dp,
                                trackInsideCornerSize = 2.dp,
                                colors = SliderDefaults.colors(
                                    activeTrackColor = Color.Blue,
                                    inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
                                )
                            )
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val currentMinutes = (sliderPosition.toLong() / 1000) / 60
                        val currentSeconds = (sliderPosition.toLong() / 1000) % 60
                        val currentTimeString = String.format("%d:%02d", currentMinutes, currentSeconds)

                        val totalMinutes = (durationInTrack.toLong() / 1000) / 60
                        val totalSeconds = (durationInTrack.toLong() / 1000) % 60
                        val totalTimeString = String.format("%d:%02d", totalMinutes, totalSeconds)

                        Text(
                            text = currentTimeString,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = totalTimeString,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)) // Легкий фон
                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CircleShape) // Тонкая обводка
                                .clickable { viewModel.playPrevious() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_media_previous),
                                contentDescription = "Previous",
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.size(24.dp))

                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .shadow(elevation = 8.dp, shape = CircleShape)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CircleShape)
                                .clickable { viewModel.playTrack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (!isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = "Play",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.size(24.dp))

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CircleShape)
                                .clickable { viewModel.playNext() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_media_next),
                                contentDescription = "Next",
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(Modifier.height(30.dp))

                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FloatingActionButton(
                        onClick = {
                            showBottomSheet = true
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.AddToPhotos,
                                contentDescription = "Add to favorites",
                                tint = Color.White
                            )
                        },
                        shape = CircleShape,
                        containerColor = Color.LightGray
                    )

                    FloatingActionButton(
                        onClick = {
                            viewModel.addTrackToFavorite(track)
                        },
                        content = {
                            Icon(
                                imageVector = if (track.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Add to favorites",
                                tint = Color.White
                            )
                        },
                        shape = CircleShape,
                        containerColor = Color.LightGray
                    )
                }
            }
        }

    }
}


@Composable
fun PlaylistsSheet(
    track: Track,
    showBottomSheet: Boolean,
    sheetState: SheetState,
    addTrackToPlaylist: (Track, Long) -> Unit,
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
                    .padding(24.dp),
            ) {
                items(count = playlists.size)
                    {
                    Column {
                        PlaylistItem(
                            playlist = playlists[it],
                            onClick = {
                                addTrackToPlaylist(track, playlists[it].playlistId)
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }

    ) {
        AsyncImage(
            model = playlist.image,
            placeholder = painterResource(R.drawable.vector),
            error = painterResource(R.drawable.vector),
            contentDescription = "Playlist",
            modifier = Modifier
                .size(45.dp),
            contentScale = ContentScale.Crop
        )
        Column {
            Text(
                text = playlist.name
            )
        }
    }
}