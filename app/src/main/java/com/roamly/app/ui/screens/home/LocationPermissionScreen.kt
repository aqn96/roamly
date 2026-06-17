/**
 * What: Pre-permission rationale screen explaining why Roamly needs location, then requesting the
 *       location (+ Android 13 notification) runtime permissions before starting trip recording.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
package com.roamly.app.ui.screens.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roamly.app.ui.theme.MontserratFamily
import com.roamly.app.ui.theme.NunitoFamily
import com.roamly.app.ui.theme.RoamlyAurora
import com.roamly.app.ui.theme.RoamlyElectric
import com.roamly.app.ui.theme.RoamlyElectricDeep
import com.roamly.app.ui.theme.RoamlyMidnight
import com.roamly.app.ui.theme.RoamlySlate
import com.roamly.app.ui.theme.RoamlySlateLight
import com.roamly.app.ui.theme.RoamlyTextLight
import com.roamly.app.ui.theme.RoamlyTextMuted
import com.roamly.app.ui.theme.RoamlyTheme

@Composable
fun LocationPermissionScreen(
    onAllowClicked: () -> Unit = {},
    onNotNowClicked: () -> Unit = {}
) {
    // Request location (+ notifications on Android 13+) at runtime. Whatever the user picks,
    // we proceed to the trip screen; the Foreground Service simply gets no fixes if denied.
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { onAllowClicked() }

    val requestedPermissions = buildList {
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }.toTypedArray()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RoamlyMidnight),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // ── GPS icon ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                RoamlyElectric.copy(alpha = 0.25f),
                                RoamlyMidnight
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.GpsFixed,
                    contentDescription = "Location",
                    tint = RoamlyElectric,
                    modifier = Modifier.size(52.dp)
                )
            }

            // ── Heading ───────────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Enable Location Access",
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = RoamlyTextLight,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Roamly passively records your route while you navigate with Google Maps — building your personal travel map in the background.",
                    fontFamily = NunitoFamily,
                    fontSize = 15.sp,
                    color = RoamlyTextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            // ── Reason cards ──────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RoamlySlate)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PermissionReasonRow(
                        icon = Icons.Default.MyLocation,
                        iconTint = RoamlyElectric,
                        title = "Passive Route Recording",
                        description = "Your GPS path is logged only while a trip is active. Recording stops the moment you tap Stop Trip."
                    )
                    PermissionReasonRow(
                        icon = Icons.Default.Map,
                        iconTint = RoamlyElectric,
                        title = "Your Travel Map, Only",
                        description = "Location data is used solely to build your route history and unlock community recommendations."
                    )
                    PermissionReasonRow(
                        icon = Icons.Default.Lock,
                        iconTint = RoamlyAurora,
                        title = "Your Data Stays Private",
                        description = "Routes are stored securely in your account and are never sold or shared with third parties."
                    )
                    PermissionReasonRow(
                        icon = Icons.Default.BatteryFull,
                        iconTint = RoamlyAurora,
                        title = "Battery-Optimized",
                        description = "Roamly uses a Foreground Service with minimal polling intervals to reduce battery drain while traveling."
                    )
                }
            }

            // ── Action buttons ────────────────────────────────────────────
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { permissionLauncher.launch(requestedPermissions) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RoamlyElectric)
                ) {
                    Text(
                        text = "Allow Location Access",
                        fontFamily = MontserratFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = RoamlyMidnight
                    )
                }

                TextButton(
                    onClick = onNotNowClicked,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Not Now",
                        fontFamily = NunitoFamily,
                        color = RoamlyTextMuted,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PermissionReasonRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    description: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(RoamlySlateLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                fontFamily = MontserratFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = RoamlyTextLight
            )
            Text(
                text = description,
                fontFamily = NunitoFamily,
                fontSize = 12.sp,
                color = RoamlyTextMuted,
                lineHeight = 17.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LocationPermissionScreenPreview() {
    RoamlyTheme {
        LocationPermissionScreen()
    }
}
