package com.roamly.app.ui.screens.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roamly.app.data.Trip
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
 * What: Trip Summary screen. Reads the just-recorded trip + the user's trip history and all-time
 *       stats from Firestore (via TripSummaryViewModel, which also persists the finished trip).
 *       Shows the latest route, an "unlocked recommendations" badge, aggregate stats, and the
 *       past-trips list.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripSummaryScreen(
    onBack: () -> Unit = {},
    onViewRecommendations: () -> Unit = {},
    tripSummaryViewModel: TripSummaryViewModel = viewModel(),
) {
    val state by tripSummaryViewModel.uiState.collectAsStateWithLifecycle()
    val latest = state.latestTrip

    Scaffold(
        containerColor = RoamlyMidnight,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Trip Summary", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, color = RoamlyTextLight)
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
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = RoamlyElectric)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            // Latest trip map (Canvas placeholder gradient)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Brush.verticalGradient(colors = listOf(Color(0xFF0D2137), RoamlyMidnight))),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = RoamlyElectric.copy(alpha = 0.5f), modifier = Modifier.size(36.dp))
                        Text(text = "Latest Route Map", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 13.sp)
                    }
                }
            }

            // Latest trip stats
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RoamlySlate)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = latest?.title ?: "No trips yet",
                            fontFamily = MontserratFamily, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = RoamlyTextLight,
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            TripStatItem(icon = Icons.Default.LocationOn, value = latest?.distanceKm ?: "—", label = "Distance")
                            TripStatItem(icon = Icons.Default.Timer, value = latest?.durationMin ?: "—", label = "Duration")
                            TripStatItem(icon = Icons.Default.CalendarToday, value = latest?.date ?: "—", label = "Date")
                        }
                    }
                }
            }

            // Unlocked badge
            if ((latest?.routesUnlocked ?: 0) > 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = RoamlyAurora.copy(alpha = 0.12f))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(12.dp)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Box(
                                modifier = Modifier.size(44.dp).clip(CircleShape).background(RoamlyAurora.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = RoamlyAurora, modifier = Modifier.size(24.dp))
                            }
                            Column(modifier = Modifier.clip(RoundedCornerShape(8.dp))) {
                                Text(text = "You unlocked ${latest?.routesUnlocked} new route(s)!", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = RoamlyAurora)
                                Text(
                                    text = "Tap to explore recommendations from nearby travelers",
                                    fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted,
                                    modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                                )
                            }
                        }
                    }
                }
            }

            // All-time stats
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "All Time Stats", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoamlyTextLight)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = RoamlySlate)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                            AllTimeStatItem(value = "${state.totalTrips}", label = "Total Trips")
                            AllTimeStatItem(value = "${state.totalDistanceKm} km", label = "Distance")
                            AllTimeStatItem(value = "${state.routesUnlocked}", label = "Unlocked", highlight = true)
                        }
                    }
                }
                Divider(color = RoamlySlateLight, modifier = Modifier.padding(horizontal = 16.dp))
            }

            // Past trips list
            item {
                Text(
                    text = "Past Trips",
                    fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoamlyTextLight,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            if (state.trips.isEmpty()) {
                item {
                    Text(
                        text = "Your recorded trips will appear here. Tap Start Trip on Home to log your first route!",
                        fontFamily = NunitoFamily, fontSize = 13.sp, color = RoamlyTextMuted,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            } else {
                items(state.trips, key = { it.id }) { trip ->
                    PastTripRow(trip = trip)
                }
            }
        }
    }
}

@Composable
private fun TripStatItem(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, contentDescription = null, tint = RoamlyElectric, modifier = Modifier.size(18.dp))
        Text(text = value, fontFamily = MontserratFamily, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = RoamlyTextLight)
        Text(text = label, fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted)
    }
}

@Composable
private fun AllTimeStatItem(value: String, label: String, highlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontFamily = MontserratFamily, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = if (highlight) RoamlyAurora else RoamlyElectric)
        Text(text = label, fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
    }
}

@Composable
private fun PastTripRow(trip: Trip) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(RoamlySlate),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = RoamlyElectric, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = trip.title, fontFamily = MontserratFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = RoamlyTextLight)
            Text(text = "${trip.date}  ·  ${trip.distanceKm}  ·  ${trip.durationMin}", fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted)
        }
        if (trip.routesUnlocked > 0) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(RoamlyAurora.copy(alpha = 0.15f)).padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "+${trip.routesUnlocked}", fontFamily = NunitoFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = RoamlyAurora)
            }
        }
    }
    Divider(color = RoamlySlateLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TripSummaryScreenPreview() {
    RoamlyTheme {
        TripSummaryScreen()
    }
}
