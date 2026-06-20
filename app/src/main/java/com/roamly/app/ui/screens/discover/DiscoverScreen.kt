/**
 * What: The Discover feed — browses every traveler's route posts from Firestore (via
 *       DiscoverViewModel) with like / save / search / Trending sort, and opens Post Detail on tap.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roamly.app.ui.components.BottomNavTab
import com.roamly.app.ui.components.RoutePost
import com.roamly.app.ui.components.RoutePostCard
import com.roamly.app.ui.components.RoamlyBottomNavBar
import com.roamly.app.ui.theme.MontserratFamily
import com.roamly.app.ui.theme.NunitoFamily
import com.roamly.app.ui.theme.RoamlyElectric
import com.roamly.app.ui.theme.RoamlyMidnight
import com.roamly.app.ui.theme.RoamlySlate
import com.roamly.app.ui.theme.RoamlySlateLight
import com.roamly.app.ui.theme.RoamlyTextLight
import com.roamly.app.ui.theme.RoamlyTextMuted
import com.roamly.app.ui.theme.RoamlyTheme

// Future feed ranking could sort by nearby routes, recency, and engagement.

private val feedFilters = listOf("For You", "Nearby", "Trending", "Following")

@Composable
fun DiscoverScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onPostClicked: (RoutePost) -> Unit = {},
    discoverViewModel: DiscoverViewModel = viewModel()
) {
    var selectedFilter by remember { mutableStateOf("For You") }
    var searchQuery by remember { mutableStateOf("") }
    val uiState by discoverViewModel.uiState.collectAsStateWithLifecycle()

    // Simple client-side filtering for the chips; "Trending" sorts by engagement.
    val visiblePosts = remember(uiState.posts, selectedFilter, searchQuery) {
        uiState.posts
            .let { if (selectedFilter == "Trending") it.sortedByDescending { p -> p.likeCount } else it }
            .filter { p ->
                searchQuery.isBlank() ||
                    p.routeTitle.contains(searchQuery, true) ||
                    p.userLocation.contains(searchQuery, true) ||
                    p.username.contains(searchQuery, true)
            }
    }

    Scaffold(
        containerColor = RoamlyMidnight,
        bottomBar = {
            RoamlyBottomNavBar(
                selectedTab = BottomNavTab.DISCOVER,
                onTabSelected = { tab ->
                    when (tab) {
                        BottomNavTab.HOME -> onNavigateToHome()
                        BottomNavTab.DISCOVER -> Unit // already here
                        BottomNavTab.FAVORITES -> onNavigateToFavorites()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // ── Top bar ───────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RoamlyMidnight)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Discover",
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = RoamlyTextLight
                )

                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search routes, places, travelers...", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = RoamlyTextMuted, modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.fillMaxWidth(),
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

                // Filter chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(feedFilters) { filter ->
                        val isSelected = selectedFilter == filter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) RoamlyElectric else RoamlySlate)
                                .border(1.dp, if (isSelected) RoamlyElectric else RoamlySlateLight, RoundedCornerShape(20.dp))
                                .clickable { selectedFilter = filter }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Text(
                                text = filter,
                                color = if (isSelected) RoamlyMidnight else RoamlyTextMuted,
                                fontFamily = NunitoFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // ── Feed ──────────────────────────────────────────────────────
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = RoamlyElectric)
                    }
                }
                visiblePosts.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No routes yet.\nRecord a trip to share the first one!",
                            color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 14.sp,
                            modifier = Modifier.padding(32.dp)
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(visiblePosts, key = { it.id }) { post ->
                            RoutePostCard(
                                post = post,
                                isLiked = post.id in uiState.likedPostIds,
                                isSaved = post.id in uiState.savedPostIds,
                                onClick = { onPostClicked(post) },
                                onComment = { onPostClicked(post) },
                                onLike = { discoverViewModel.toggleLike(post) },
                                onSave = { discoverViewModel.toggleSave(post) }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DiscoverScreenPreview() {
    RoamlyTheme {
        DiscoverScreen()
    }
}
