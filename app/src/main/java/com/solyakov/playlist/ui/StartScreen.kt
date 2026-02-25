package com.solyakov.playlist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.solyakov.playlist.R


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPlaylistsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3772E7))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                modifier = Modifier.padding(start = 12.dp, top = 40.dp),
                text = stringResource(R.string.playlist_maker),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
            ScreenContent(
                onSearchClick = {onSearchClick()},
                onSettingsClick = {onSettingsClick()},
                onPlaylistsClick = { onPlaylistsClick() }
            )
    }
}

@Composable
fun ScreenContent(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPlaylistsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier.padding(top = 10.dp)
        ) {

            val context = LocalContext.current
            Element(
                screenIcon = Icons.Default.Search,
                text = stringResource(R.string.search),
                onClick = {
                    onSearchClick()
                }
            )

            Element(
                painterIcon = painterResource(R.drawable.vector),
                text = stringResource(R.string.playlist),
                onClick = { onPlaylistsClick() }
            )

            Element(
                screenIcon = Icons.Default.FavoriteBorder,
                text = stringResource(R.string.favourites),
                onClick = { }
            )

            Element(
                screenIcon = Icons.Default.Settings,
                text = stringResource(R.string.settings),
                onClick = {
                    onSettingsClick()
                }
            )
        }
    }
}




@Composable
fun Element(screenIcon: ImageVector, text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick ,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(0.dp)
    ) {
        ElementContent(
            icon = { Icon(imageVector = screenIcon, contentDescription = null, tint = Color.Black) },
            text = text
        )
    }
}

@Composable
fun Element(painterIcon: Painter, text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(0.dp)
    ) {
        ElementContent(
            icon = { Icon(painter = painterIcon, contentDescription = null, tint = Color.Black) },
            text = text
        )
    }
}

@Composable
private fun ElementContent(icon: @Composable () -> Unit, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon()
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Text(
                text = text,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = stringResource(R.string.back),
            tint = Color.Gray
        )
    }
}



