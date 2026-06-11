package com.roamly.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = RoamlyBlue,
    onPrimary = RoamlyWhite,
    secondary = RoamlyTeal,
    onSecondary = RoamlyWhite,
    background = RoamlyWhite,
    surface = RoamlyLightGray,
    onBackground = RoamlyNavy,
    onSurface = RoamlyNavy,
)

private val DarkColorScheme = darkColorScheme(
    primary = RoamlyBlueDark,
    onPrimary = RoamlyWhite,
    secondary = RoamlyTeal,
    onSecondary = RoamlyWhite,
    background = RoamlyNavy,
    surface = RoamlyNavyLight,
    onBackground = RoamlyWhite,
    onSurface = RoamlyWhite,
)

@Composable
fun RoamlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
