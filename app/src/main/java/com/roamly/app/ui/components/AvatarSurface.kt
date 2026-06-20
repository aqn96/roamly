/**
 * What: A circular avatar surface. Loads the user's profile photo from its Firebase Storage URL
 *       with Coil's AsyncImage; when no URL is set it renders the caller-supplied fallback
 *       (initials or a person icon). Shared by Home and Profile so both stay in sync.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
package com.roamly.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.roamly.app.ui.theme.RoamlyElectric
import com.roamly.app.ui.theme.RoamlySlate
import com.roamly.app.ui.theme.RoamlyTheme

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

@Preview(showBackground = true)
@Composable
private fun AvatarSurfacePreview() {
    RoamlyTheme {
        // No URL → shows the fallback (person icon), the state seen before a photo is uploaded.
        AvatarSurface(
            imageUrl = null,
            modifier = Modifier.size(88.dp),
            fallback = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = RoamlyElectric,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        )
    }
}
