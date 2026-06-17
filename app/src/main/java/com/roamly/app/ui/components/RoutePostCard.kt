/**
 * What: The RoutePost model (doubles as the Firestore "posts" document) and RoutePostCard — the
 *       feed item shown in Discover and Favorites, with like / comment / save actions.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
package com.roamly.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roamly.app.ui.theme.MontserratFamily
import com.roamly.app.ui.theme.NunitoFamily
import com.roamly.app.ui.theme.RoamlyAurora
import com.roamly.app.ui.theme.RoamlyElectric
import com.roamly.app.ui.theme.RoamlyMidnight
import com.roamly.app.ui.theme.RoamlySlate
import com.roamly.app.ui.theme.RoamlySlateLight
import com.roamly.app.ui.theme.RoamlyTextLight
import com.roamly.app.ui.theme.RoamlyTextMuted
import com.roamly.app.ui.theme.RoamlyTheme

/**
 * A shared route post. Doubles as the Firestore "posts/{id}" document model, so every field has a
 * default value (required for Firestore's toObject() deserialization). One user's posts become
 * another user's Discover feed — the multi-user core of the app.
 */
data class RoutePost(
    val id: String = "",
    val authorUid: String = "",
    val username: String = "",
    val userLocation: String = "",
    val distanceAway: String = "",
    val routeTitle: String = "",
    val description: String = "",
    val tags: List<String> = emptyList(),
    val distanceKm: String = "",
    val durationMin: String = "",
    val mapImageUrl: String = "",
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isUnlocked: Boolean = true,
    val createdAt: Long = 0L,
)

@Composable
fun RoutePostCard(
    post: RoutePost,
    onLike: () -> Unit = {},
    onComment: () -> Unit = {},
    onSave: () -> Unit = {},
    onClick: () -> Unit = {},
    showUnlockedBadge: Boolean = true,
    isLiked: Boolean = false,
    isSaved: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RoamlySlate)
    ) {
        Column {

            // ── Map thumbnail placeholder ─────────────────────────────────
            // TODO: Replace with real route map snapshot:
            //   Image(bitmap = routeMapBitmap, contentScale = ContentScale.Crop,
            //         modifier = Modifier.fillMaxWidth().height(160.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D2137), Color(0xFF0F172A))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = RoamlyElectric.copy(alpha = 0.5f),
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Route Map",
                        color = RoamlyTextMuted,
                        fontFamily = NunitoFamily,
                        fontSize = 12.sp
                    )
                }

                // Unlocked badge — only shown on Discover feed, not on Favorites
                if (showUnlockedBadge && post.isUnlocked) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(RoamlyAurora.copy(alpha = 0.9f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "✓ Unlocked",
                            color = RoamlyMidnight,
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                // Route stats overlay at bottom
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(RoamlyMidnight.copy(alpha = 0.7f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "📍 ${post.distanceKm}", color = RoamlyTextLight, fontSize = 11.sp, fontFamily = NunitoFamily)
                    Text(text = "⏱ ${post.durationMin}", color = RoamlyTextLight, fontSize = 11.sp, fontFamily = NunitoFamily)
                }
            }

            // ── Post body ─────────────────────────────────────────────────
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // User row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // TODO: Replace with actual user avatar image
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(RoamlySlateLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.username.first().uppercaseChar().toString(),
                            color = RoamlyElectric,
                            fontFamily = MontserratFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Column {
                        Text(
                            text = post.username,
                            fontFamily = MontserratFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = RoamlyTextLight
                        )
                        Text(
                            text = "${post.userLocation} · ${post.distanceAway}",
                            fontFamily = NunitoFamily,
                            fontSize = 11.sp,
                            color = RoamlyTextMuted
                        )
                    }
                }

                // Route title and description
                Text(
                    text = post.routeTitle,
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = RoamlyTextLight
                )
                Text(
                    text = post.description,
                    fontFamily = NunitoFamily,
                    fontSize = 13.sp,
                    color = RoamlyTextMuted,
                    lineHeight = 19.sp
                )

                // Tags
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    post.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(RoamlyElectric.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(text = "#$tag", color = RoamlyElectric, fontFamily = NunitoFamily, fontSize = 11.sp)
                        }
                    }
                }

                // Like / Comment / Save row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onLike, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (isLiked) Color(0xFFEF4444) else RoamlyTextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(text = "${post.likeCount}", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = onComment, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comment", tint = RoamlyTextMuted, modifier = Modifier.size(20.dp))
                        }
                        Text(text = "${post.commentCount}", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 12.sp)
                    }
                    IconButton(onClick = onSave, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Save",
                            tint = RoamlyElectric,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RoutePostCardPreview() {
    RoamlyTheme {
        RoutePostCard(
            post = RoutePost(
                username = "aqn96",
                userLocation = "Tokyo, Japan",
                distanceAway = "2.4 km away",
                routeTitle = "Hidden Alley Route in Shibuya",
                description = "Discovered this amazing path through the back streets of Shibuya — way less crowded than the main crossing.",
                tags = listOf("solo", "urban", "japan"),
                distanceKm = "3.2 km",
                durationMin = "48 min",
                likeCount = 124,
                commentCount = 18,
                isUnlocked = true
            )
        )
    }
}
