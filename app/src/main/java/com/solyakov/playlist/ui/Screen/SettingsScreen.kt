@file:OptIn(ExperimentalMaterial3Api::class)

package com.solyakov.playlist.ui.Screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.solyakov.playlist.R

@Composable
fun SettingsScreen(
    isDarkTheme: Boolean = false,
    onClick: () -> Unit,
    onThemeChanged: (Boolean) -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    ) {
        TopAppBar(
            modifier = Modifier,
            title = {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(R.string.Settings),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
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
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

    SettingsElement(
        stringResource(R.string.dark_mode),
        isDarkTheme,
        onThemeChanged = { onThemeChanged(it) }
    )
    SettingsElement(
        onClick = {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT,"Зацени мое приложение")
            intent.type = "text/plain"
            context.startActivity(intent)
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.onSurface)
        } ,
        text = stringResource(R.string.share_app))
    SettingsElement(
        onClick = {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("yuracolicov@gmail.com"))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    context.getString(R.string.message_to_developer)
                )
                putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам за крутое приложение")
            }
            context.startActivity(intent)

        },
        icon = {
            Icon(
                painter = painterResource( R.drawable.ic_helper),
                contentDescription = "Support",
                tint = MaterialTheme.colorScheme.onSurface)
        },
        text = stringResource(R.string.write_to_support)
    )

    SettingsElement(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            }
            context.startActivity(intent)
        },
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Agreement",
                tint = MaterialTheme.colorScheme.onSurface)
        },
        text = stringResource(R.string.user_agreement)
    )



    }
}





@Composable
fun SettingsElement(icon: @Composable () -> Unit, text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                onClick()
            },

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 20.dp),
                text = text,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Box(modifier = Modifier.padding(end = 16.dp, top = 20.dp)){
                icon()
            }


        }
    }
}

@Composable
fun SettingsElement(text: String, isDarkTheme: Boolean, onThemeChanged: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { },

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 20.dp),
                text = text,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            var checked by rememberSaveable { mutableStateOf(false) }
            Switch(
                modifier = Modifier.padding(end = 12.dp, top = 14.dp),
                checked = isDarkTheme,
                onCheckedChange = {
                    onThemeChanged(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF3772E7),
                    uncheckedTrackColor = Color(0xFFAEAFB4),
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}

