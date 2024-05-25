package com.ssafy.wearable.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    primary = Blue,
    onPrimary = White,
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = White
)

private val LightColorPalette = lightColorScheme(
    primary = Blue,
    onPrimary = White,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black
)

@Composable
fun WearableAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
