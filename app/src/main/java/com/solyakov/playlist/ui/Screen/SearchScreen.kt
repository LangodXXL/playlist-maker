@file:OptIn(ExperimentalMaterial3Api::class)

package com.solyakov.playlist.ui.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.solyakov.playlist.R
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.ui.view_model.SearchScreenViewModel
import com.solyakov.playlist.ui.view_model.SearchState


@Composable
fun SearchScreen(onClick: () -> Unit,
                 modifier: Modifier,
                 viewModel: SearchScreenViewModel,
                 onTrackClick: (Long) -> Unit
) {
    val screenState by viewModel.searchScreenState.collectAsState()
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    val historyRequests by viewModel.historyRepository.getHistory().collectAsState(emptyList())
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val textFieldShape = if (inputText.text.isBlank() && isFocused && historyRequests.isNotEmpty()) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
    } else {
        RoundedCornerShape(size = 16.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus() }
            ),


    ) {
        TopAppBar(
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

        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isFocused = it.isFocused
                    },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchAndAddToHistory(inputText.text)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                value = inputText,
                placeholder = { Text(text = stringResource(R.string.search)) },
                onValueChange = {
                    inputText = it
                    viewModel.search(it.text)

                },
                trailingIcon = {
                    if (inputText.text.isNotEmpty()) {
                        Icon(
                            modifier = Modifier.clickable {
                                inputText = TextFieldValue("")
                                viewModel.clearQuery()
                            },
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            viewModel.search(inputText.text)
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
                shape = textFieldShape
            )
            if (inputText.text.isBlank() && isFocused && historyRequests.isNotEmpty()) {
                HistoryContent(
                    historyItems = historyRequests,
                    onHistoryItemClick = {
                        inputText = TextFieldValue(
                            text = it,
                            selection = TextRange(historyRequests.size)

                        )
                        viewModel.search(it)
                    }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
                when (screenState) {
                    is SearchState.Initial -> {
                        Box(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Введите строку для поиска")
                        }
                    }

                    is SearchState.Searching -> {
                        Box(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is SearchState.Success -> {
                        val tracks = (screenState as SearchState.Success).foundList
                        if (tracks.isEmpty()) {
                            Box(
                                modifier = modifier
                                    .fillMaxSize()
                                    .padding(start = 16.dp, end = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Ничего не нашлось")
                            }
                        } else {
                            LazyColumn(
                                modifier = modifier
                                    .fillMaxSize()
                                    .padding(start = 16.dp, end = 16.dp)
                            ) {
                                items(tracks.size) { index ->
                                    TrackListItem(
                                        track = tracks[index],
                                        onClick = { onTrackClick(tracks[index].trackId) }

                                    )
                                    HorizontalDivider(thickness = 0.5.dp)
                                }
                            }
                        }

                    }

                    is SearchState.Fail -> {
                        val error = (screenState as SearchState.Fail).error
                        Box(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Ошибка: $error", color = Color.Red)
                        }
                    }
                }
            }
        }
}



@Composable
fun TrackListItem(
    track: Track,
    onClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(track.trackId)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        SubcomposeAsyncImage(
            model = track.image,
            contentDescription = "Трек ${track.trackName}",
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop,
            loading = {
                Image(
                    painter = painterResource(id = R.drawable.vector),
                    contentDescription = "Album",
                    modifier = Modifier
                        .background(Color.LightGray.copy(alpha = 0.5f))
                        .alpha(0.4f),
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
            }
        )
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = track.trackName,
                fontSize = 16.sp
                )
            Row{
                Text(
                    text = track.artistName + "  -",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = track.trackTime,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.arrow),
            contentDescription = null
        )
    }
}

@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    query: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = "Search history item",
            tint = Color.Black
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = query,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Go to query",
            tint = Color.Gray
        )
    }
}


@Composable
fun HistoryContent(
    historyItems: List<String>,
    onHistoryItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = Color(0xFFE6E8EB),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
    ) {
        LazyColumn {
            items(historyItems.size) { index ->
                HistoryItem(
                    query = historyItems[index],
                    onClick = { onHistoryItemClick(historyItems[index]) }
                )
                if (index < historyItems.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}