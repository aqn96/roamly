package com.roamly.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.roamly.app.ui.components.BottomNavTab
import com.roamly.app.ui.components.RoamlyBottomNavBar
import com.roamly.app.ui.theme.MontserratFamily
import com.roamly.app.ui.theme.NunitoFamily
import com.roamly.app.ui.theme.RoamlyAurora
import com.roamly.app.ui.theme.RoamlyElectric
import com.roamly.app.ui.theme.RoamlyElectricDeep
import com.roamly.app.ui.theme.RoamlyMidnight
import com.roamly.app.ui.theme.RoamlySlate
import com.roamly.app.ui.theme.RoamlyTextLight
import com.roamly.app.ui.theme.RoamlyTextMuted
import com.roamly.app.ui.theme.RoamlyTheme

@Composable
fun HomeScreen(
    onStartTrip: () -> Unit = {},
    onNavigateToTripStats: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(BottomNavTab.HOME) }

    Scaffold(
        containerColor = RoamlyMidnight,
        bottomBar = {
            RoamlyBottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    // TODO: Wire up tab navigation once NavGraph is set up:
                    //   BottomNavTab.DISCOVER -> onNavigateToDiscover()
                    //   BottomNavTab.FAVORITES -> onNavigateToFavorites()
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // ── Map placeholder ───────────────────────────────────────────
            // TODO: Replace with actual Google Maps Composable:
            //   val cameraPositionState = rememberCameraPositionState()
            //   GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0D2137),
                                Color(0xFF0F172A),
                                Color(0xFF0A1628)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Map View", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 14.sp)
            }

            // ── Top bar overlay ───────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar — top left
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(RoamlySlate)
                        .border(3.dp, RoamlyElectric, CircleShape)
                        .clickable {
                            // TODO: Navigate to profile/settings screen:
                            //   onNavigateToProfile()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // TODO: Replace with actual user profile photo:
                    //   Image(bitmap = userAvatarBitmap, contentScale = ContentScale.Crop,
                    //         modifier = Modifier.fillMaxSize().clip(CircleShape))
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = RoamlyElectric,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Text(
                    text = "Roamly",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = MontserratFamily,
                    color = RoamlyElectric
                )

                Spacer(modifier = Modifier.size(62.dp))
            }

            // ── Start Trip button — center ────────────────────────────────
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .shadow(12.dp, CircleShape)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(RoamlyElectric, RoamlyElectricDeep)
                            )
                        )
                        .clickable { onStartTrip() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.DirectionsWalk,
                            contentDescription = "Start Trip",
                            tint = RoamlyMidnight,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Start Trip",
                            color = RoamlyMidnight,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = MontserratFamily,
                            fontSize = 15.sp
                        )
                    }
                }
                Text(
                    text = "Tap to begin recording your route",
                    color = RoamlyTextMuted,
                    fontFamily = NunitoFamily,
                    fontSize = 13.sp
                )
            }

            // ── Trip stats card — bottom overlay ──────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = RoamlySlate),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My Stats",
                            fontFamily = MontserratFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = RoamlyTextLight
                        )
                        TextButton(onClick = onNavigateToTripStats) {
                            Text(
                                text = "See more →",
                                color = RoamlyElectric,
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // TODO: Replace dummy values with real data from Firestore
                        StatItem(value = "12", label = "Trips", highlight = false)
                        StatItem(value = "234 km", label = "Distance", highlight = false)
                        StatItem(value = "8", label = "Unlocked", highlight = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String, highlight: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontFamily = MontserratFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = if (highlight) RoamlyAurora else RoamlyElectric
        )
        Text(
            text = label,
            fontFamily = NunitoFamily,
            fontSize = 12.sp,
            color = RoamlyTextMuted
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    RoamlyTheme {
        HomeScreen()
    }
}
