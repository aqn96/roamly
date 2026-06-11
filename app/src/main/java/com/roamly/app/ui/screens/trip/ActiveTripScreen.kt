package com.roamly.app.ui.screens.trip

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@Composable
fun ActiveTripScreen(
    onStopTrip: () -> Unit = {},
    onSeeMoreStats: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RoamlyMidnight)
    ) {

        // ── Map with live route ───────────────────────────────────────────
        // TODO: Replace with live Google Maps + Polyline drawn from FusedLocationProvider:
        //   GoogleMap(modifier = Modifier.fillMaxSize(), ...) {
        //     Polyline(points = routePoints, color = RoamlyElectric, width = 8f)
        //   }
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
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Live Map", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 14.sp)
                Text(text = "Route drawing here", color = RoamlyElectric.copy(alpha = 0.4f), fontFamily = NunitoFamily, fontSize = 12.sp)
            }
        }

        // ── Recording indicator — top bar ─────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Timer — TODO: drive with a LaunchedEffect + ticker coroutine
            Column {
                Text(text = "Duration", fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted)
                Text(
                    text = "00:12:34",
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = RoamlyTextLight
                )
            }

            // Recording pulse indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(RoamlySlate)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FiberManualRecord,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(10.dp)
                )
                Text(
                    text = "Recording",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = RoamlyTextLight
                )
            }

            // Distance
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Distance", fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted)
                Text(
                    text = "2.3 km",
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = RoamlyElectric
                )
            }
        }

        // ── Foreground service banner ─────────────────────────────────────
        // TODO: This banner reflects the active ForegroundService notification.
        //   Shown while Google Maps is in the foreground and Roamly logs GPS in background.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 110.dp, start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(RoamlyElectric.copy(alpha = 0.15f))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = "🛰  Roamly is recording your route in the background",
                fontFamily = NunitoFamily,
                fontSize = 12.sp,
                color = RoamlyElectric
            )
        }

        // ── Live stats card + Stop button — bottom ────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = RoamlySlate),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "My Stats", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = RoamlyTextLight)
                        TextButton(onClick = onSeeMoreStats) {
                            Text(text = "See more →", color = RoamlyElectric, fontFamily = NunitoFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // TODO: Replace with real-time values from ForegroundService location updates
                        LiveStatItem(value = "2.3 km", label = "Distance")
                        LiveStatItem(value = "00:12:34", label = "Duration")
                        LiveStatItem(value = "11.5 km/h", label = "Avg Speed")
                        LiveStatItem(value = "8", label = "Unlocked", highlight = true)
                    }
                }
            }

            // Stop Trip button
            Button(
                onClick = onStopTrip,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
            ) {
                Icon(Icons.Default.Stop, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Stop Trip",
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun LiveStatItem(value: String, label: String, highlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontFamily = MontserratFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = if (highlight) RoamlyAurora else RoamlyElectric
        )
        Text(text = label, fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ActiveTripScreenPreview() {
    RoamlyTheme {
        ActiveTripScreen()
    }
}
