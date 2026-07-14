package com.solyakov.playlist.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1A1B22),
    secondary = PurpleGrey80,
    tertiary = Pink80,
    onSurface = Color.White,
    onSurfaceVariant = Color.White,
    surfaceContainer = Color.White,
    surfaceContainerHigh = SharedSearchText,
    onSecondaryContainer = Color.White,
    onTertiaryContainer = SearchHistoryClock
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    onSurface = Color.Black,
    onSurfaceVariant = Color(0xFFAEAFB4),
    surfaceContainer = Color(0xFFE6E8EB),
    surfaceContainerHigh = SharedSearchText,
    onSecondaryContainer = Color.Black,
    onTertiaryContainer = SearchHistoryClock

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun PlaylistTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}