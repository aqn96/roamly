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

// TODO: Feed algorithm — sort posts by:
//   1. Nearby users (Firestore geoquery using GeoFlutterFire or manual lat/lng bounding box)
//   2. Recency (createdAt timestamp descending)
//   3. Engagement score (likes + comments weight)
//   Similar to Instagram's interest graph + LinkedIn's proximity feed

private val feedFilters = listOf("For You", "Nearby", "Trending", "Following")

// Dummy data — TODO: replace with real Firestore posts query
private val dummyPosts = listOf(
    RoutePost(
        username = "aqn96", userLocation = "Tokyo, Japan", distanceAway = "2.4 km away",
        routeTitle = "Hidden Alley Route in Shibuya",
        description = "Discovered this amazing path through the back streets of Shibuya — way less crowded than the main crossing.",
        tags = listOf("solo", "urban", "japan"), distanceKm = "3.2 km", durationMin = "48 min",
        likeCount = 124, commentCount = 18, isUnlocked = true
    ),
    RoutePost(
        username = "traveler_maya", userLocation = "Kyoto, Japan", distanceAway = "5.1 km away",
        routeTitle = "Fushimi Inari at Sunrise",
        description = "Skip the crowds — start the torii gate trail at 5:30am. The light is incredible and you'll have it almost to yourself.",
        tags = listOf("hiking", "temples", "solo"), distanceKm = "6.8 km", durationMin = "2h 10min",
        likeCount = 342, commentCount = 47, isUnlocked = true
    ),
    RoutePost(
        username = "nomad_kris", userLocation = "Osaka, Japan", distanceAway = "12 km away",
        routeTitle = "Dotonbori Night Walk",
        description = "Best street food crawl in Osaka. Start from Namba, hit the canal, and end at Kuromon Market.",
        tags = listOf("food", "nightlife", "group"), distanceKm = "4.1 km", durationMin = "1h 30min",
        likeCount = 89, commentCount = 12, isUnlocked = false
    ),
    RoutePost(
        username = "roamer_jess", userLocation = "Nara, Japan", distanceAway = "28 km away",
        routeTitle = "Deer Park Loop Trail",
        description = "A peaceful loop through Nara Park past Todai-ji. The deer literally follow you if you have crackers.",
        tags = listOf("nature", "wildlife", "family"), distanceKm = "5.5 km", durationMin = "1h 20min",
        likeCount = 201, commentCount = 33, isUnlocked = false
    )
)

@Composable
fun DiscoverScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onPostClicked: (RoutePost) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("For You") }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = RoamlyMidnight,
        bottomBar = {
            RoamlyBottomNavBar(
                selectedTab = BottomNavTab.DISCOVER,
                onTabSelected = {
                    // TODO: Wire up tab navigation once NavGraph is set up
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
                // TODO: Each filter triggers a different Firestore query:
                //   "For You"   → algo-ranked feed (engagement + proximity)
                //   "Nearby"    → geoquery sorted by distance from user's current location
                //   "Trending"  → top likeCount + commentCount in last 7 days
                //   "Following" → posts from users in the current user's "following" subcollection
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
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(dummyPosts) { post ->
                    RoutePostCard(
                        post = post,
                        onLike = { /* TODO: update likeCount in Firestore */ },
                        onComment = { /* TODO: navigate to post comments screen */ },
                        onSave = { /* TODO: add to user's favorites subcollection */ }
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
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
