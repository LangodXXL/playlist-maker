@file:OptIn(ExperimentalMaterial3Api::class)

package com.solyakov.playlist.ui.Screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.solyakov.playlist.R
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.ui.view_model.FavoriteTracksViewModel


@Composable
fun FavoriteTracksScreen(
    viewModel: FavoriteTracksViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Long) -> Unit,
    onLongTrackClick: (Track) -> Unit
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
            itemsIndexed(
                items = tracks,
                key = { _, track -> track.trackId },
            ) { index, track ->
                TrackListItemIn(
                    track = track,
                    onClick = {
                        viewModel.onTrackClick(tracks, index)
                        onTrackClick(it)
                    },
                    onLongTrackClick = {viewModel.toggleFavorite(track)}
                )
            }
        }

    }
}


@Composable
fun TrackListItemIn(
    track: Track,
    onClick: (Long) -> Unit,
    onLongTrackClick: (Track) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onClick(track.trackId)},
                onLongClick = {onLongTrackClick(track)
                }
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = track.image,
            placeholder = painterResource(R.drawable.vector),
            error = painterResource(R.drawable.vector),
            contentDescription = "Обложка трека",
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${track.artistName} • ${track.trackTime}",
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Image(
            painter = painterResource(R.drawable.arrow),
            contentDescription = null
        )
    }
}





