package com.roamly.app.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roamly.app.ui.components.RoutePost
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

data class PostComment(
    val username: String,
    val text: String,
    val timeAgo: String,
    val likeCount: Int
)

data class SimilarPost(
    val username: String,
    val locationLabel: String,
    val distanceKm: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    post: RoutePost = dummyDetailPost,
    onBack: () -> Unit = {},
    onUserClicked: (String) -> Unit = {}
) {
    var commentText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = RoamlyMidnight,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Post",
                        fontFamily = MontserratFamily,
                        fontWeight = FontWeight.Bold,
                        color = RoamlyTextLight
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = RoamlyTextLight)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoamlyMidnight)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            // ── User header ───────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.clickable { onUserClicked(post.username) }
                    ) {
                        // TODO: Replace with actual user avatar image from Firebase Storage
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(RoamlySlate)
                                .border(2.dp, RoamlyElectric, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = post.username.first().uppercaseChar().toString(),
                                color = RoamlyElectric,
                                fontFamily = MontserratFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Column {
                            Text(
                                text = post.username,
                                fontFamily = MontserratFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = RoamlyTextLight
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = RoamlyElectric, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(text = post.userLocation, fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
                            }
                        }
                    }
                    // TODO: Follow state from Firestore user subcollection
                    TextButton(
                        onClick = { /* TODO: toggle follow in Firestore */ },
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(RoamlyElectric.copy(alpha = 0.15f))
                    ) {
                        Text(text = "Follow", color = RoamlyElectric, fontFamily = NunitoFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }

            // ── Route map image ───────────────────────────────────────────
            item {
                // TODO: Replace with actual route map snapshot or Google Maps static image:
                //   Image(painter = rememberAsyncImagePainter(post.mapImageUrl),
                //         contentScale = ContentScale.Crop,
                //         modifier = Modifier.fillMaxWidth().height(220.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(Brush.verticalGradient(colors = listOf(Color(0xFF0D2137), RoamlyMidnight))),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = RoamlyElectric.copy(alpha = 0.5f), modifier = Modifier.size(40.dp))
                        Text(text = "Route Map", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 13.sp)
                    }
                    Row(
                        modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(RoamlyMidnight.copy(alpha = 0.8f))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(text = "📍 ${post.distanceKm}", color = RoamlyTextLight, fontFamily = NunitoFamily, fontSize = 12.sp)
                        Text(text = "⏱ ${post.durationMin}", color = RoamlyTextLight, fontFamily = NunitoFamily, fontSize = 12.sp)
                    }
                }
            }

            // ── Post content ──────────────────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = post.routeTitle, fontFamily = MontserratFamily, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = RoamlyTextLight)
                    Text(text = post.description, fontFamily = NunitoFamily, fontSize = 14.sp, color = RoamlyTextMuted, lineHeight = 21.sp)

                    // Hashtags
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        post.tags.forEach { tag ->
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(6.dp))
                                    .background(RoamlyElectric.copy(alpha = 0.12f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(text = "#$tag", color = RoamlyElectric, fontFamily = NunitoFamily, fontSize = 12.sp)
                            }
                        }
                    }

                    // Like / Comment / Save
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(onClick = { /* TODO: toggle like in Firestore */ }, modifier = Modifier.size(36.dp)) {
                                Icon(Icons.Default.FavoriteBorder, contentDescription = "Like", tint = RoamlyTextMuted, modifier = Modifier.size(20.dp))
                            }
                            Text(text = "${post.likeCount}", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                                Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comment", tint = RoamlyTextMuted, modifier = Modifier.size(20.dp))
                            }
                            Text(text = "${post.commentCount}", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 13.sp)
                        }
                        IconButton(onClick = { /* TODO: save to favorites subcollection */ }, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.BookmarkBorder, contentDescription = "Save", tint = RoamlyElectric, modifier = Modifier.size(20.dp))
                        }
                    }
                }
                Divider(color = RoamlySlateLight, thickness = 1.dp)
            }

            // ── Comments section ──────────────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Comments", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoamlyTextLight)
                    Spacer(modifier = Modifier.height(4.dp))

                    // Comment input
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            placeholder = { Text("Add a comment...", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 13.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoamlyElectric,
                                unfocusedBorderColor = RoamlySlateLight,
                                focusedContainerColor = RoamlySlate,
                                unfocusedContainerColor = RoamlySlate,
                                focusedTextColor = RoamlyTextLight,
                                unfocusedTextColor = RoamlyTextLight
                            ),
                            singleLine = true
                        )
                        IconButton(
                            onClick = { /* TODO: post comment to Firestore comments subcollection */ },
                            modifier = Modifier.size(44.dp).clip(CircleShape).background(RoamlyElectric)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Post", tint = RoamlyMidnight, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            // Dummy comments — TODO: replace with Firestore comments subcollection query
            items(dummyComments) { comment ->
                CommentRow(comment = comment, onUserClicked = onUserClicked)
            }

            item { Divider(modifier = Modifier.padding(top = 8.dp), color = RoamlySlateLight) }

            // ── Similar posts from this location ──────────────────────────
            // TODO: Query Firestore for posts where locationTag == post.userLocation,
            //   ordered by likeCount desc, limit 10. Clicking a tile navigates to that post.
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "More from ${post.userLocation}",
                        fontFamily = MontserratFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = RoamlyTextLight
                    )
                    Text(
                        text = "Routes shared by other travelers in this area",
                        fontFamily = NunitoFamily,
                        fontSize = 12.sp,
                        color = RoamlyTextMuted
                    )
                }
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(dummySimilarPosts) { similar ->
                        SimilarPostTile(
                            similar = similar,
                            onClick = {
                                // TODO: onUserClicked(similar.username) to navigate to that user's post
                                onUserClicked(similar.username)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CommentRow(comment: PostComment, onUserClicked: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(RoamlySlate).border(1.dp, RoamlySlateLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = comment.username.first().uppercaseChar().toString(), color = RoamlyElectric, fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = comment.username,
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = RoamlyElectric,
                    modifier = Modifier.clickable { onUserClicked(comment.username) }
                )
                Text(text = comment.timeAgo, fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted)
            }
            Text(text = comment.text, fontFamily = NunitoFamily, fontSize = 13.sp, color = RoamlyTextLight, lineHeight = 18.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = RoamlyTextMuted, modifier = Modifier.size(14.dp))
            Text(text = "${comment.likeCount}", fontFamily = NunitoFamily, fontSize = 10.sp, color = RoamlyTextMuted)
        }
    }
}

@Composable
private fun SimilarPostTile(similar: SimilarPost, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(130.dp).clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // TODO: Replace with actual map snapshot image
        Box(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp))
                .background(Brush.verticalGradient(colors = listOf(Color(0xFF0D2137), RoamlySlate))),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = RoamlyElectric.copy(alpha = 0.4f), modifier = Modifier.size(24.dp))
            Box(
                modifier = Modifier.align(Alignment.BottomStart).padding(6.dp)
                    .clip(RoundedCornerShape(6.dp)).background(RoamlyMidnight.copy(alpha = 0.8f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = similar.distanceKm, color = RoamlyTextLight, fontFamily = NunitoFamily, fontSize = 10.sp)
            }
        }
        Text(text = "@${similar.username}", fontFamily = MontserratFamily, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = RoamlyElectric)
        Text(text = similar.locationLabel, fontFamily = NunitoFamily, fontSize = 10.sp, color = RoamlyTextMuted)
    }
}

// ── Dummy data ────────────────────────────────────────────────────────────────
private val dummyDetailPost = RoutePost(
    username = "aqn96", userLocation = "Tokyo, Japan", distanceAway = "2.4 km away",
    routeTitle = "Hidden Alley Route in Shibuya",
    description = "Discovered this amazing path through the back streets of Shibuya — way less crowded than the main crossing. Perfect for early morning before the tourists arrive.",
    tags = listOf("solo", "urban", "japan"), distanceKm = "3.2 km", durationMin = "48 min",
    likeCount = 124, commentCount = 18, isUnlocked = true
)

private val dummyComments = listOf(
    PostComment("traveler_maya", "This is incredible! I walked a similar path last spring. The alley near the old bookshop is magical.", "2h ago", 14),
    PostComment("nomad_kris", "Adding this to my list for next month 🙌", "5h ago", 7),
    PostComment("roamer_jess", "Do you know if it's accessible at night too?", "1d ago", 3)
)

private val dummySimilarPosts = listOf(
    SimilarPost("traveler_maya", "Shinjuku, Tokyo", "4.1 km"),
    SimilarPost("nomad_kris", "Harajuku, Tokyo", "2.8 km"),
    SimilarPost("roamer_jess", "Akihabara, Tokyo", "5.5 km"),
    SimilarPost("wanderer_kai", "Asakusa, Tokyo", "6.2 km"),
    SimilarPost("route_sam", "Ueno, Tokyo", "3.9 km")
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PostDetailScreenPreview() {
    RoamlyTheme {
        PostDetailScreen()
    }
}
