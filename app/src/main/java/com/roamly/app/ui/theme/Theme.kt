package com.roamly.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Midnight Nomad is dark-first — no light scheme needed
private val MidnightColorScheme = darkColorScheme(
    primary = RoamlyElectric,
    onPrimary = RoamlyMidnight,
    secondary = RoamlyAurora,
    onSecondary = RoamlyMidnight,
    background = RoamlyMidnight,
    surface = RoamlySlate,
    onBackground = RoamlyTextLight,
    onSurface = RoamlyTextLight,
    outline = RoamlySlateLight,
)

@Composable
fun RoamlyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MidnightColorScheme,
        typography = Typography,
        content = content
    )
}
