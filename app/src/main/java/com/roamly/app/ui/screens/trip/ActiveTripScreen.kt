/**
 * What: The live trip screen. On appear it starts the GPS Foreground Service via TripViewModel,
 *       observes the recorded route + distance as StateFlow, draws the path as a Canvas polyline
 *       inside a contained map card, ticks a live duration timer, and offers a button to hand
 *       navigation off to Google Maps (per the proposal: "Google Maps handles navigation, route
 *       logging is Roamly's own").
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
package com.roamly.app.ui.screens.trip

import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roamly.app.data.TrackPoint
import com.roamly.app.data.formatElapsed
import com.roamly.app.ui.theme.MontserratFamily
import com.roamly.app.ui.theme.NunitoFamily
import com.roamly.app.ui.theme.RoamlyAurora
import com.roamly.app.ui.theme.RoamlyElectric
import com.roamly.app.ui.theme.RoamlyMidnight
import com.roamly.app.ui.theme.RoamlySlate
import com.roamly.app.ui.theme.RoamlyTextLight
import com.roamly.app.ui.theme.RoamlyTextMuted
import com.roamly.app.ui.theme.RoamlyTheme

@Composable
fun ActiveTripScreen(
    onStopTrip: () -> Unit = {},
    onSeeMoreStats: () -> Unit = {},
    tripViewModel: TripViewModel = viewModel(),
) {
    val context = LocalContext.current
    val routePoints by tripViewModel.routePoints.collectAsStateWithLifecycle()
    val distanceKm by tripViewModel.distanceKm.collectAsStateWithLifecycle()
    val isRecording by tripViewModel.isRecording.collectAsStateWithLifecycle()

    // Start the Foreground Service exactly once when this screen first appears.
    LaunchedEffect(Unit) { tripViewModel.startTrip(context) }

    // Live duration ticker — recomputes once per second while recording.
    var elapsedMs by remember { mutableLongStateOf(0L) }
    LaunchedEffect(isRecording) {
        while (isRecording) {
            elapsedMs = SystemClock.elapsedRealtime() - com.roamly.app.location.TripSession.startElapsedMs
            kotlinx.coroutines.delay(1000)
        }
    }

    val durationText = formatElapsed(elapsedMs)
    val avgSpeedKmh = if (elapsedMs > 0) distanceKm / (elapsedMs / 3_600_000.0) else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RoamlyMidnight)
            .statusBarsPadding()
    ) {

        // ── Recording indicator — top bar ─────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Duration", fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted)
                Text(
                    text = durationText,
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = RoamlyTextLight
                )
            }

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
                    tint = if (isRecording) Color.Red else RoamlyTextMuted,
                    modifier = Modifier.size(10.dp)
                )
                Text(
                    text = if (isRecording) "Recording" else "Paused",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = RoamlyTextLight
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Distance", fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted)
                Text(
                    text = "%.2f km".format(distanceKm),
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = RoamlyElectric
                )
            }
        }

        // ── Contained map card with the live route polyline ───────────────
        // DEMO BUILD: the route is rendered on a Compose Canvas over a gradient to keep the app on
        // Firebase's free tier (no Google Maps API key / billing). In production this card would host
        // a Google Maps SDK MapView with a Polyline overlay following the same recorded GPS points.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0D2137), RoamlyMidnight, Color(0xFF0A1628))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (routePoints.size < 2) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Waiting for GPS…", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 14.sp)
                    Text(text = "Move to start drawing your route", color = RoamlyElectric.copy(alpha = 0.5f), fontFamily = NunitoFamily, fontSize = 12.sp)
                }
            } else {
                // Canvas is clipped to the card, so the route stays inside the map area.
                RoutePolyline(points = routePoints, modifier = Modifier.fillMaxSize().padding(28.dp))
            }

            // Foreground-service banner pinned to the top of the map card.
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(RoamlyMidnight.copy(alpha = 0.6f))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "🛰  Recording your route in the background",
                    fontFamily = NunitoFamily,
                    fontSize = 12.sp,
                    color = RoamlyElectric,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // ── Live stats card + actions — bottom ────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
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
                        LiveStatItem(value = "%.2f km".format(distanceKm), label = "Distance")
                        LiveStatItem(value = durationText, label = "Duration")
                        LiveStatItem(value = "%.1f km/h".format(avgSpeedKmh), label = "Avg Speed")
                        LiveStatItem(value = "${routePoints.size}", label = "GPS pts", highlight = true)
                    }
                }
            }

            // Hand navigation off to the Google Maps app (logging continues in our service).
            OutlinedButton(
                onClick = { context.startActivity(buildMapsIntent(routePoints)) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
            ) {
                Icon(Icons.Default.Map, contentDescription = null, tint = RoamlyElectric, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Navigate with Google Maps", color = RoamlyElectric, fontFamily = NunitoFamily, fontWeight = FontWeight.SemiBold)
            }

            // Stop Trip button — stops the service, then lets the NavGraph route to the summary.
            Button(
                onClick = {
                    tripViewModel.stopTrip(context)
                    onStopTrip()
                },
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

/**
 * Draws the recorded route as a connected polyline, normalising lat/lng into the canvas bounds.
 * Latitude is flipped because screen-Y grows downward while latitude grows upward (north).
 */
@Composable
private fun RoutePolyline(points: List<TrackPoint>, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val minLat = points.minOf { it.latitude }
        val maxLat = points.maxOf { it.latitude }
        val minLng = points.minOf { it.longitude }
        val maxLng = points.maxOf { it.longitude }
        val latSpan = (maxLat - minLat).takeIf { it > 0 } ?: 1.0
        val lngSpan = (maxLng - minLng).takeIf { it > 0 } ?: 1.0

        val offsets = points.map { p ->
            val x = ((p.longitude - minLng) / lngSpan) * size.width
            val y = (1.0 - (p.latitude - minLat) / latSpan) * size.height
            Offset(x.toFloat(), y.toFloat())
        }
        for (i in 1 until offsets.size) {
            drawLine(
                color = RoamlyElectric,
                start = offsets[i - 1],
                end = offsets[i],
                strokeWidth = 10f,
                cap = StrokeCap.Round,
            )
        }
        // Mark the current position.
        offsets.lastOrNull()?.let { drawCircle(color = RoamlyAurora, radius = 14f, center = it) }
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

/** Opens the Google Maps app centred on the latest GPS fix (or a neutral point if none yet). */
private fun buildMapsIntent(points: List<TrackPoint>): Intent {
    val last = points.lastOrNull()
    val uri = if (last != null) {
        Uri.parse("geo:${last.latitude},${last.longitude}?q=${last.latitude},${last.longitude}(You are here)")
    } else {
        Uri.parse("geo:0,0")
    }
    return Intent(Intent.ACTION_VIEW, uri).apply { setPackage("com.google.android.apps.maps") }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ActiveTripScreenPreview() {
    RoamlyTheme {
        ActiveTripScreen()
    }
}
