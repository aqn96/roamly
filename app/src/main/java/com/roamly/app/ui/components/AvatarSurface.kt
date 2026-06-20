package com.roamly.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.roamly.app.ui.theme.RoamlyElectric
import com.roamly.app.ui.theme.RoamlySlate

@Composable
fun AvatarSurface(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    borderColor: Color = RoamlyElectric,
    backgroundColor: Color = RoamlySlate,
    fallback: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .background(backgroundColor, CircleShape)
            .border(2.dp, borderColor, CircleShape)
            .clip(CircleShape)
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            fallback()
        }
    }
}
