@file:OptIn(ExperimentalMaterial3Api::class)

package com.solyakov.playlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.solyakov.playlist.R
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.ui.view_model.SearchState
import com.solyakov.playlist.ui.view_model.SearchViewModel


@Composable
fun MySearchScreen(onClick: () -> Unit) {

    var inputText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    ) { TopAppBar(
        modifier = Modifier,
        title = {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(R.string.search),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(32.dp)
                    .clickable {
                        onClick()
                    },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            value = inputText,
            placeholder = {Text(text = stringResource(R.string.search))},
            onValueChange = { inputText = it },
            trailingIcon = {
                if (inputText.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable{
                            inputText = ""
                        },
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.clickable{

                    },
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE6E8EB),
                unfocusedContainerColor = Color(0xFFE6E8EB),

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLeadingIconColor = Color.Black,
                unfocusedLeadingIconColor = Color.Black,
                focusedTrailingIconColor = Color.Black,
                unfocusedTrailingIconColor = Color.Black
            ),
            shape = RoundedCornerShape(size = 8.dp))
    }
}



@Composable
fun TrackListItem(track: Track) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.vector),
            contentDescription = "Трек ${track.trackName}"
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(track.trackName, fontWeight = FontWeight.Bold)
            Text(track.artistName)
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(track.trackTime)
        }
    }
}




@Composable
fun SearchScreen(
    modifier: Modifier,
    viewModel: SearchViewModel
) {
    val screenState by viewModel.searchScreenState.collectAsState()
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        viewModel.search(text)
                    },
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        when (screenState) {
            is SearchState.Initial -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Введите строку для поиска")
                }
            }

            is SearchState.Searching -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is SearchState.Success -> {
                val tracks = (screenState as SearchState.Success).foundList
                LazyColumn(
                    modifier = modifier.fillMaxSize()
                ) {
                    items(tracks.size) { index ->
                        TrackListItem(track = tracks[index])
                        HorizontalDivider(thickness = 0.5.dp)
                    }
                }
            }

            is SearchState.Fail -> {
                val error = (screenState as SearchState.Fail).error
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Ошибка: $error", color = Color.Red)
                }
            }
        }
    }
}

