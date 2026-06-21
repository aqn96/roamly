/**
 * What: The Home screen - map placeholder, destination search, the central Start Trip button,
 *       a recommended-routes row, and bottom navigation.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
package com.roamly.app.ui.screens.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roamly.app.data.AuthRepository
import com.roamly.app.data.RoamlyUser
import com.roamly.app.ui.components.AvatarSurface
import com.roamly.app.ui.components.BottomNavTab
import com.roamly.app.ui.components.RoamlyBottomNavBar
import com.roamly.app.ui.theme.MontserratFamily
import com.roamly.app.ui.theme.NunitoFamily
import com.roamly.app.ui.theme.RoamlyElectric
import com.roamly.app.ui.theme.RoamlyElectricDeep
import com.roamly.app.ui.theme.RoamlyMidnight
import com.roamly.app.ui.theme.RoamlySlate
import com.roamly.app.ui.theme.RoamlySlateLight
import com.roamly.app.ui.theme.RoamlyTextLight
import com.roamly.app.ui.theme.RoamlyTextMuted
import com.roamly.app.ui.theme.RoamlyTheme

private data class RecommendedRoute(
    val title: String,
    val location: String,
    val distanceKm: String,
    val durationMin: String
)

private val dummyRecommendedRoutes = listOf(
    RecommendedRoute("Shibuya Back Alleys", "Tokyo, Japan", "3.2 km", "48 min"),
    RecommendedRoute("Fushimi Inari Trail", "Kyoto, Japan", "6.8 km", "2h 10min"),
    RecommendedRoute("Dotonbori Night Walk", "Osaka, Japan", "4.1 km", "1h 30min"),
    RecommendedRoute("Deer Park Loop", "Nara, Japan", "5.5 km", "1h 20min")
)

@Composable
fun HomeScreen(
    onStartTrip: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val authRepository = remember { AuthRepository() }
    val currentProfile by produceState<RoamlyUser?>(initialValue = null) {
        value = authRepository.loadCurrentProfile().getOrNull()
    }

    Scaffold(
        containerColor = RoamlyMidnight,
        bottomBar = {
            RoamlyBottomNavBar(
                selectedTab = BottomNavTab.HOME,
                onTabSelected = { tab ->
                    when (tab) {
                        BottomNavTab.HOME -> Unit // already here
                        BottomNavTab.DISCOVER -> onNavigateToDiscover()
                        BottomNavTab.FAVORITES -> onNavigateToFavorites()
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Map placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D2137), RoamlyMidnight, Color(0xFF0A1628))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Map View", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 14.sp)
            }

            // Top bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tapping the avatar opens the profile
                    AvatarSurface(
                        imageUrl = currentProfile?.avatarUrl,
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(6.dp, CircleShape)
                            .clickable { onNavigateToProfile() },
                        borderColor = RoamlyElectric,
                        backgroundColor = RoamlySlate,
                        fallback = {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = RoamlyElectric,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    )

                    Text(
                        text = "Roamly",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = MontserratFamily,
                        color = RoamlyElectric
                    )

                    Spacer(modifier = Modifier.size(52.dp))
                }

                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "Search a destination...",
                            color = RoamlyTextMuted,
                            fontFamily = NunitoFamily,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = RoamlyTextMuted, modifier = Modifier.size(20.dp))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoamlyElectric,
                        unfocusedBorderColor = RoamlySlateLight,
                        focusedContainerColor = RoamlySlate.copy(alpha = 0.9f),
                        unfocusedContainerColor = RoamlySlate.copy(alpha = 0.9f),
                        focusedTextColor = RoamlyTextLight,
                        unfocusedTextColor = RoamlyTextLight
                    ),
                    singleLine = true
                )
            }

            // Start Trip button - center
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .align(Alignment.Center)
                    .shadow(12.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(colors = listOf(RoamlyElectric, RoamlyElectricDeep)))
                    .clickable { onStartTrip() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.DirectionsWalk,
                        contentDescription = "Start Trip",
                        tint = RoamlyMidnight,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Start Trip",
                        color = RoamlyMidnight,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = MontserratFamily,
                        fontSize = 13.sp
                    )
                }
            }

            // Recommended routes - bottom card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = RoamlySlate),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Recommended Routes",
                        fontFamily = MontserratFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = RoamlyTextLight,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(dummyRecommendedRoutes) { route ->
                            RecommendedRouteCard(route = route)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendedRouteCard(route: RecommendedRoute) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = RoamlyMidnight)
    ) {
        Column {
            // Map thumbnail placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D2137), RoamlyMidnight)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = RoamlyElectric.copy(alpha = 0.4f), modifier = Modifier.size(20.dp))
            }
            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = route.title, fontFamily = MontserratFamily, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = RoamlyTextLight, maxLines = 1)
                Text(text = route.location, fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted, maxLines = 1)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = route.distanceKm, fontFamily = NunitoFamily, fontSize = 10.sp, color = RoamlyElectric)
                    Text(text = route.durationMin, fontFamily = NunitoFamily, fontSize = 10.sp, color = RoamlyTextMuted)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    RoamlyTheme {
        HomeScreen()
    }
}
