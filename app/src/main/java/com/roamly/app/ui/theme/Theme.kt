package com.roamly.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = RoamlyTerracotta,
    onPrimary = RoamlyWhite,
    secondary = RoamlyForestGreen,
    onSecondary = RoamlyWhite,
    background = RoamlySand,
    surface = RoamlyLightGray,
    onBackground = RoamlyDarkBrown,
    onSurface = RoamlyDarkBrown,
)

private val DarkColorScheme = darkColorScheme(
    primary = RoamlyTerracottaDark,
    onPrimary = RoamlyWhite,
    secondary = RoamlyForestGreenDark,
    onSecondary = RoamlyWhite,
    background = RoamlyDarkBrown,
    surface = Color(0xFF3D2418),
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
