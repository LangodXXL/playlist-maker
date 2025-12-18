package com.solyakov.playlist.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.solyakov.playlist.ui.screen.BottomSheetExample
import com.solyakov.playlist.ui.screen.FloatButtonExample
import com.solyakov.playlist.ui.theme.PlaylistTheme

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showBottomSheet  by remember { mutableStateOf(false) }
            PlaylistTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FloatButtonExample(modifier = Modifier, callback = {showBottomSheet = true})
                    BottomSheetExample(
                        modifier = Modifier.padding(innerPadding),
                        isShowPanel = showBottomSheet,
                        onDismissRequest = { showBottomSheet  = false },
                        content = "Это панель BottomSheet"
                    )
                }
            }
        }
    }
}