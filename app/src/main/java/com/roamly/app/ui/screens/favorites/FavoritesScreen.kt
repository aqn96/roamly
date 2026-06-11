package com.roamly.app.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.roamly.app.ui.theme.RoamlyTextLight
import com.roamly.app.ui.theme.RoamlyTextMuted
import com.roamly.app.ui.theme.RoamlyTheme

// TODO: Populate from Firestore user favorites subcollection:
//   db.collection("users").document(userId).collection("favorites")
//   Each document stores a postId reference — fetch full post data from "posts" collection
private val dummyFavorites = listOf(
    RoutePost(
        username = "traveler_maya", userLocation = "Kyoto, Japan", distanceAway = "5.1 km away",
        routeTitle = "Fushimi Inari at Sunrise",
        description = "Skip the crowds — start the torii gate trail at 5:30am.",
        tags = listOf("hiking", "temples", "solo"), distanceKm = "6.8 km", durationMin = "2h 10min",
        likeCount = 342, commentCount = 47, isUnlocked = true
    ),
    RoutePost(
        username = "nomad_kris", userLocation = "Osaka, Japan", distanceAway = "12 km away",
        routeTitle = "Dotonbori Night Walk",
        description = "Best street food crawl in Osaka. Start from Namba, hit the canal.",
        tags = listOf("food", "nightlife", "group"), distanceKm = "4.1 km", durationMin = "1h 30min",
        likeCount = 89, commentCount = 12, isUnlocked = true
    )
)

@Composable
fun FavoritesScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {}
) {
    Scaffold(
        containerColor = RoamlyMidnight,
        bottomBar = {
            RoamlyBottomNavBar(
                selectedTab = BottomNavTab.FAVORITES,
                onTabSelected = {
                    // TODO: wire up tab navigation once NavGraph is set up
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
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Saved Routes",
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = RoamlyTextLight
                )
                Text(
                    text = "Routes you bookmarked from the Discover feed",
                    fontFamily = NunitoFamily,
                    fontSize = 13.sp,
                    color = RoamlyTextMuted
                )
            }

            if (dummyFavorites.isEmpty()) {
                // ── Empty state ───────────────────────────────────────────
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = RoamlyElectric.copy(alpha = 0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "No saved routes yet",
                            fontFamily = MontserratFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = RoamlyTextMuted
                        )
                        Text(
                            text = "Tap the bookmark icon on any post\nin Discover to save it here",
                            fontFamily = NunitoFamily,
                            fontSize = 14.sp,
                            color = RoamlyTextMuted,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            } else {
                // ── Saved posts list ──────────────────────────────────────
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(dummyFavorites) { post ->
                        RoutePostCard(
                            post = post,
                            showUnlockedBadge = false,
                            onLike = { /* TODO: toggle like in Firestore */ },
                            onComment = { /* TODO: navigate to post detail comments */ },
                            onSave = { /* TODO: remove from favorites subcollection */ }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FavoritesScreenPreview() {
    RoamlyTheme {
        FavoritesScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Empty State")
@Composable
private fun FavoritesEmptyPreview() {
    RoamlyTheme {
        FavoritesScreen()
    }
}
