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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roamly.app.data.Comment
import com.roamly.app.ui.theme.MontserratFamily
import com.roamly.app.ui.theme.NunitoFamily
import com.roamly.app.ui.theme.RoamlyElectric
import com.roamly.app.ui.theme.RoamlyMidnight
import com.roamly.app.ui.theme.RoamlySlate
import com.roamly.app.ui.theme.RoamlySlateLight
import com.roamly.app.ui.theme.RoamlyTextLight
import com.roamly.app.ui.theme.RoamlyTextMuted
import com.roamly.app.ui.theme.RoamlyTheme

/**
 * What: Post Detail screen. Loads a single post + its comments from Firestore (via
 *       PostDetailViewModel), and lets the signed-in user like the post, follow the author, and
 *       add comments - the multi-user interaction surface. State flows down; events flow up.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId: String = "",
    onBack: () -> Unit = {},
    onUserClicked: (String) -> Unit = {},
    postDetailViewModel: PostDetailViewModel = viewModel(),
) {
    val state by postDetailViewModel.uiState.collectAsStateWithLifecycle()
    var commentText by remember { mutableStateOf("") }

    LaunchedEffect(postId) { if (postId.isNotBlank()) postDetailViewModel.load(postId) }

    Scaffold(
        containerColor = RoamlyMidnight,
        topBar = {
            TopAppBar(
                title = { Text(text = "Post", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, color = RoamlyTextLight) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = RoamlyTextLight)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoamlyMidnight)
            )
        }
    ) { innerPadding ->
        val post = state.post
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = RoamlyElectric)
                }
            }
            post == null -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text("Post not found", color = RoamlyTextMuted, fontFamily = NunitoFamily)
                }
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Author header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.clickable { onUserClicked(post.authorUid) }
                        ) {
                            Box(
                                modifier = Modifier.size(46.dp).clip(CircleShape).background(RoamlySlate).border(2.dp, RoamlyElectric, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = post.username.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                                    color = RoamlyElectric, fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp
                                )
                            }
                            Column {
                                Text(text = post.username, fontFamily = MontserratFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = RoamlyTextLight)
                                if (post.userLocation.isNotBlank()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = RoamlyElectric, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(text = post.userLocation, fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
                                    }
                                }
                            }
                        }
                        TextButton(
                            onClick = { postDetailViewModel.toggleFollow() },
                            modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(RoamlyElectric.copy(alpha = 0.15f))
                        ) {
                            Text(
                                text = if (state.isFollowingAuthor) "Following" else "Follow",
                                color = RoamlyElectric, fontFamily = NunitoFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp
                            )
                        }
                    }
                }

                // Route map placeholder
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(220.dp)
                            .background(Brush.verticalGradient(colors = listOf(Color(0xFF0D2137), RoamlyMidnight))),
                        contentAlignment = Alignment.Center
                    ) {
                        if (post.mapImageUrl.isNotBlank()) {
                            AsyncImage(
                                model = post.mapImageUrl,
                                contentDescription = post.routeTitle,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = RoamlyElectric.copy(alpha = 0.5f), modifier = Modifier.size(40.dp))
                                Text(text = "Route Map", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 13.sp)
                            }
                        }
                        Row(
                            modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
                                .clip(RoundedCornerShape(8.dp)).background(RoamlyMidnight.copy(alpha = 0.8f))
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(text = "📍 ${post.distanceKm}", color = RoamlyTextLight, fontFamily = NunitoFamily, fontSize = 12.sp)
                            Text(text = "⏱ ${post.durationMin}", color = RoamlyTextLight, fontFamily = NunitoFamily, fontSize = 12.sp)
                        }
                    }
                }

                // Post content
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(text = post.routeTitle, fontFamily = MontserratFamily, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = RoamlyTextLight)
                        Text(text = post.description, fontFamily = NunitoFamily, fontSize = 14.sp, color = RoamlyTextMuted, lineHeight = 21.sp)

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            post.tags.forEach { tag ->
                                Box(
                                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(RoamlyElectric.copy(alpha = 0.12f)).padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(text = "#$tag", color = RoamlyElectric, fontFamily = NunitoFamily, fontSize = 12.sp)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(onClick = { postDetailViewModel.toggleLike() }, modifier = Modifier.size(36.dp)) {
                                    Icon(
                                        imageVector = if (state.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Like",
                                        tint = if (state.isLiked) Color(0xFFEF4444) else RoamlyTextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(text = "${post.likeCount}", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comments", tint = RoamlyTextMuted, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "${post.commentCount}", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 13.sp)
                            }
                            IconButton(onClick = { postDetailViewModel.toggleSave() }, modifier = Modifier.size(36.dp)) {
                                Icon(
                                    imageVector = if (state.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Save",
                                    tint = RoamlyElectric,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                    Divider(color = RoamlySlateLight, thickness = 1.dp)
                }

                // Comments header + input
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Comments", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoamlyTextLight)
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
                                onClick = {
                                    postDetailViewModel.addComment(commentText)
                                    commentText = ""
                                },
                                modifier = Modifier.size(44.dp).clip(CircleShape).background(RoamlyElectric)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Post", tint = RoamlyMidnight, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }

                if (state.comments.isEmpty()) {
                    item {
                        Text(
                            text = "Be the first to comment.",
                            fontFamily = NunitoFamily, fontSize = 13.sp, color = RoamlyTextMuted,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    items(state.comments, key = { it.id }) { comment ->
                        CommentRow(comment = comment, onUserClicked = onUserClicked)
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentRow(comment: Comment, onUserClicked: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(RoamlySlate).border(1.dp, RoamlySlateLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = comment.username.firstOrNull()?.uppercaseChar()?.toString() ?: "?", color = RoamlyElectric, fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = comment.username, fontFamily = MontserratFamily, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = RoamlyElectric,
                    modifier = Modifier.clickable { onUserClicked(comment.authorUid) }
                )
                Text(text = relativeTime(comment.createdAt), fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted)
            }
            Text(text = comment.text, fontFamily = NunitoFamily, fontSize = 13.sp, color = RoamlyTextLight, lineHeight = 18.sp)
        }
    }
}

// Coarse "x ago" label from an epoch-millis timestamp.
private fun relativeTime(createdAt: Long): String {
    if (createdAt <= 0L) return ""
    val diff = System.currentTimeMillis() - createdAt
    val mins = diff / 60000
    val hours = mins / 60
    val days = hours / 24
    return when {
        mins < 1 -> "just now"
        mins < 60 -> "${mins}m ago"
        hours < 24 -> "${hours}h ago"
        else -> "${days}d ago"
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PostDetailScreenPreview() {
    RoamlyTheme {
        PostDetailScreen()
    }
}
