@file:OptIn(ExperimentalMaterial3Api::class)

package com.solyakov.playlist.presentation

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.solyakov.playlist.R

@Composable
fun SettingsScreen() {
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
                    text = "Настройки",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            navigationIcon = {
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(32.dp)
                        .clickable {},
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

    SettingsElement("Темная тема")
    SettingsElement(
        icon = {
            Icon(
//                modifier = it,
                imageVector = Icons.Filled.Share,
                contentDescription = "Share")
        } ,
        text ="Поделиться приложением")
    SettingsElement(
        icon = {
            Icon(
//                modifier = it,
                painter = painterResource( R.drawable.ic_helper),
                contentDescription = "Support")
        },
        text = "Написать в поддержку")
    SettingsElement(
        icon = {
            Icon(
//                modifier = it,
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Agreement")
        },
        text = "Пользовательское соглашение")



    }
}





@Composable
fun SettingsElement(icon: @Composable () -> Unit, text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable{},

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 20.dp),
                text = text,
                fontSize = 20.sp
            )

            Box(modifier = Modifier.padding(end = 16.dp, top = 20.dp)){
                icon()
            }


        }
    }
}

@Composable
fun SettingsElement(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {  },

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 20.dp),
                text = text,
                fontSize = 20.sp
            )
            var checked by rememberSaveable { mutableStateOf(false) }
            Switch(
                modifier = Modifier.padding(end = 12.dp, top = 14.dp),
                checked = checked,
                onCheckedChange = {
                    checked = it
                },
            )
        }
    }
}

