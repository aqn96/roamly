package com.roamly.app.ui.screens.profile

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.roamly.app.ui.components.BottomNavTab
import com.roamly.app.ui.components.RoamlyBottomNavBar
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

data class UserProfile(
    val username: String,
    val fullName: String,
    val homeCountry: String,
    val favoriteDestination: String,
    val travelStyle: String,
    val travelFrequency: String,
    val totalTrips: Int,
    val totalDistanceKm: Int,
    val routesUnlocked: Int,
    val followerCount: Int,
    val followingCount: Int,
    val isOwnProfile: Boolean = true
)

data class SuggestedUser(
    val username: String,
    val homeCountry: String,
    val travelStyle: String,
    val mutualCount: Int
)

@Composable
fun ProfileScreen(
    profile: UserProfile = dummyProfile,
    onEditProfile: () -> Unit = {},
    onFollow: () -> Unit = {},
    onSuggestedUserClicked: (String) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {}
) {
    Scaffold(
        containerColor = RoamlyMidnight,
        bottomBar = {
            RoamlyBottomNavBar(
                selectedTab = BottomNavTab.HOME,
                onTabSelected = { tab ->
                    when (tab) {
                        BottomNavTab.HOME -> onNavigateToHome()
                        BottomNavTab.DISCOVER -> onNavigateToDiscover()
                        BottomNavTab.FAVORITES -> onNavigateToFavorites()
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            // ── Banner + avatar ───────────────────────────────────────────
            item {
                Box(modifier = Modifier.fillMaxWidth()) {

                    // Banner image
                    // TODO: Replace with actual banner photo from Firebase Storage:
                    //   Image(painter = rememberAsyncImagePainter(profile.bannerUrl),
                    //         contentScale = ContentScale.Crop,
                    //         modifier = Modifier.fillMaxWidth().height(160.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(RoamlyElectricDeep.copy(alpha = 0.6f), RoamlyElectric.copy(alpha = 0.3f), RoamlyMidnight)
                                )
                            )
                    ) {
                        // Edit banner button
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(RoamlyMidnight.copy(alpha = 0.6f))
                                .clickable {
                                    // TODO: Launch image picker for banner:
                                    //   launcher.launch("image/*")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Edit banner", tint = RoamlyTextLight, modifier = Modifier.size(16.dp))
                        }
                    }

                    // Profile picture — overlaps the banner
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = 20.dp, y = 44.dp)
                    ) {
                        // TODO: Replace with actual profile photo from Firebase Storage:
                        //   Image(painter = rememberAsyncImagePainter(profile.avatarUrl),
                        //         contentScale = ContentScale.Crop,
                        //         modifier = Modifier.size(88.dp).clip(CircleShape))
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(RoamlySlate)
                                .border(3.dp, RoamlyElectric, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profile.username.first().uppercaseChar().toString(),
                                color = RoamlyElectric,
                                fontFamily = MontserratFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 32.sp
                            )
                        }
                        // Edit avatar button
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(RoamlyElectric)
                                .clickable {
                                    // TODO: Launch image picker for avatar:
                                    //   launcher.launch("image/*")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Edit avatar", tint = RoamlyMidnight, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // ── Name + actions row ────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 16.dp, top = 52.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = profile.fullName,
                            fontFamily = MontserratFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = RoamlyTextLight
                        )
                        Text(
                            text = "@${profile.username}",
                            fontFamily = NunitoFamily,
                            fontSize = 13.sp,
                            color = RoamlyTextMuted
                        )
                    }

                    if (profile.isOwnProfile) {
                        OutlinedButton(
                            onClick = onEditProfile,
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RoamlyElectric)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = RoamlyElectric, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Edit", color = RoamlyElectric, fontFamily = NunitoFamily, fontSize = 13.sp)
                        }
                    } else {
                        Button(
                            onClick = onFollow,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RoamlyElectric)
                        ) {
                            Text(text = "Follow", color = RoamlyMidnight, fontFamily = NunitoFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            // ── Location + travel info ────────────────────────────────────
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    InfoChipRow(icon = Icons.Default.Flag, text = profile.homeCountry)
                    InfoChipRow(icon = Icons.Default.LocationOn, text = "Favorite: ${profile.favoriteDestination}")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TravelBadge(label = profile.travelStyle)
                        TravelBadge(label = profile.travelFrequency)
                    }
                }
            }

            // ── Follower / Following counts ───────────────────────────────
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${profile.followerCount}", fontFamily = MontserratFamily, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = RoamlyTextLight)
                        Text(text = "Followers", fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${profile.followingCount}", fontFamily = MontserratFamily, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = RoamlyTextLight)
                        Text(text = "Following", fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
                    }
                }
                Divider(color = RoamlySlateLight)
            }

            // ── Trip stats ────────────────────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "My Stats", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoamlyTextLight)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = RoamlySlate)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // TODO: Pull real values from Firestore user document
                            ProfileStatItem(value = "${profile.totalTrips}", label = "Total Trips", isHighlight = false)
                            ProfileStatItem(value = "${profile.totalDistanceKm} km", label = "Distance", isHighlight = false)
                            ProfileStatItem(value = "${profile.routesUnlocked}", label = "Unlocked", isHighlight = true)
                        }
                    }
                }
                Divider(color = RoamlySlateLight)
            }

            // ── Travel level ──────────────────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Travel Level", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoamlyTextLight)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = RoamlySlate)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(48.dp).clip(CircleShape)
                                    .background(RoamlyAurora.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = RoamlyAurora, modifier = Modifier.size(26.dp))
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                // TODO: Compute level from totalTrips + totalDistanceKm:
                                //   < 5 trips   → Wanderer
                                //   5–20 trips  → Explorer
                                //   20–50 trips → Nomad
                                //   50+ trips   → Legend
                                Text(text = "Explorer", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoamlyAurora)
                                Text(text = "${profile.totalTrips} trips · ${profile.totalDistanceKm} km logged", fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
                                Text(text = "8 more trips to reach Nomad 🏕", fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
                            }
                        }
                    }
                }
                Divider(color = RoamlySlateLight)
            }

            // ── Suggested travelers to follow ─────────────────────────────
            // TODO: Query Firestore for users with similar:
            //   - homeCountry, travelStyle, or nearby geoLocation
            //   - Exclude users already followed by current user
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Travelers to Follow", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoamlyTextLight)
                    Text(text = "Based on your travel style and destinations", fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
                }
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(dummySuggestedUsers) { user ->
                        SuggestedUserCard(user = user, onFollowClicked = { onSuggestedUserClicked(user.username) })
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ProfileStatItem(value: String, label: String, isHighlight: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontFamily = MontserratFamily, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = if (isHighlight) RoamlyAurora else RoamlyElectric)
        Text(text = label, fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
    }
}

@Composable
private fun InfoChipRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(icon, contentDescription = null, tint = RoamlyElectric, modifier = Modifier.size(14.dp))
        Text(text = text, fontFamily = NunitoFamily, fontSize = 13.sp, color = RoamlyTextMuted)
    }
}

@Composable
private fun TravelBadge(label: String) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(20.dp))
            .background(RoamlyElectric.copy(alpha = 0.12f))
            .border(1.dp, RoamlyElectric.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(text = label, color = RoamlyElectric, fontFamily = NunitoFamily, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun SuggestedUserCard(user: SuggestedUser, onFollowClicked: () -> Unit) {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = RoamlySlate)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // TODO: Replace with actual avatar from Firebase Storage
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape)
                    .background(RoamlyMidnight).border(2.dp, RoamlyElectric, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = RoamlyElectric, modifier = Modifier.size(28.dp))
            }
            Text(text = "@${user.username}", fontFamily = MontserratFamily, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = RoamlyTextLight)
            Text(text = user.homeCountry, fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted)
            TravelBadge(label = user.travelStyle)
            if (user.mutualCount > 0) {
                Text(text = "${user.mutualCount} mutual", fontFamily = NunitoFamily, fontSize = 10.sp, color = RoamlyTextMuted)
            }
            Button(
                onClick = onFollowClicked,
                modifier = Modifier.fillMaxWidth().height(32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoamlyElectric),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = RoamlyMidnight, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Follow", color = RoamlyMidnight, fontFamily = NunitoFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

// ── Dummy data ────────────────────────────────────────────────────────────────
private val dummyProfile = UserProfile(
    username = "aqn96",
    fullName = "An Nguyen",
    homeCountry = "United States 🇺🇸",
    favoriteDestination = "Tokyo, Japan",
    travelStyle = "Solo Trip",
    travelFrequency = "Frequent",
    totalTrips = 12,
    totalDistanceKm = 234,
    routesUnlocked = 8,
    followerCount = 142,
    followingCount = 67,
    isOwnProfile = true
)

private val dummySuggestedUsers = listOf(
    SuggestedUser("traveler_maya", "Canada 🇨🇦", "Solo Trip", 3),
    SuggestedUser("nomad_kris", "Germany 🇩🇪", "Nomad", 1),
    SuggestedUser("roamer_jess", "Australia 🇦🇺", "Group Trip", 0),
    SuggestedUser("wanderer_kai", "Japan 🇯🇵", "Solo Trip", 5)
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    RoamlyTheme {
        ProfileScreen()
    }
}
