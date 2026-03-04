@file:OptIn(ExperimentalMaterial3Api::class)

package com.solyakov.playlist.ui.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.solyakov.playlist.ui.view_model.AddPlaylistScreenViewModel

@Composable
fun AddPlaylistScreen(
    viewModel: AddPlaylistScreenViewModel,
    onBackClick: () -> Unit
) {
    val playlistName by viewModel.playlistName.collectAsState()
    val playlistDescription by viewModel.playlistDescription.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Новый плейлист"
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            )
        }


    ) { innerPadding ->
        Column {
            TextField(
                modifier = Modifier.padding(innerPadding),
                value = playlistName,
                onValueChange = {
                    viewModel.setPlaylistName(it)
                }
            )



            TextField(
                modifier = Modifier.padding(innerPadding),
                value = playlistDescription,
                onValueChange = {
                    viewModel.setPlaylistDescription(it)
                }
            )


            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(8.dp)),
                onClick = {
                    viewModel.savePlaylist()
                    onBackClick()
                },
                content = {
                    Text(text = "Создать")
                }
            )
        }

    }
}