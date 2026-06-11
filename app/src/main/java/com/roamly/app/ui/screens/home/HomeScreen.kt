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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roamly.app.ui.components.BottomNavTab
import com.roamly.app.ui.components.RoamlyBottomNavBar
import com.roamly.app.ui.theme.RoamlyBlue
import com.roamly.app.ui.theme.RoamlyLightGray
import com.roamly.app.ui.theme.RoamlyTeal
import com.roamly.app.ui.theme.RoamlyTheme

@Composable
fun HomeScreen(
    onStartTrip: () -> Unit = {},
    onNavigateToFeed: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(BottomNavTab.HOME) }

    Scaffold(
        bottomBar = {
            RoamlyBottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    // TODO: Wire up tab navigation once NavGraph is set up:
                    //   BottomNavTab.FEED -> onNavigateToFeed()
                    //   BottomNavTab.PROFILE -> onNavigateToProfile()
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
                                Color(0xFFDCEEFA),
                                Color(0xFFB8DDF0),
                                Color(0xFF9ECFE8)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Map View",
                    color = Color(0xFF7AAFCC),
                    fontSize = 16.sp
                )
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
                        .size(44.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, RoamlyBlue, CircleShape)
                        .clickable {
                            // TODO: Navigate to profile/settings screen:
                            //   onNavigateToProfile()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // TODO: Replace Icon with actual user profile photo:
                    //   Image(bitmap = userAvatarBitmap, contentScale = ContentScale.Crop,
                    //         modifier = Modifier.fillMaxSize().clip(CircleShape))
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = RoamlyBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // App name centered
                Text(
                    text = "Roamly",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoamlyBlue
                )

                // Spacer to balance the row
                Spacer(modifier = Modifier.size(44.dp))
            }

            // ── Start Trip button — center of screen ──────────────────────
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(RoamlyBlue, RoamlyTeal)
                            )
                        )
                        .clickable { onStartTrip() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.DirectionsWalk,
                            contentDescription = "Start Trip",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Start Trip",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
                Text(
                    text = "Tap to begin recording your route",
                    color = Color.White,
                    fontSize = 12.sp
                )
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
