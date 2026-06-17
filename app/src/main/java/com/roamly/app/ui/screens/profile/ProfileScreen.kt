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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roamly.app.data.RoamlyUser
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

/**
 * What: Profile screen. Loads either the signed-in user's own profile or another traveler's (by
 *       uid) from Firestore via ProfileViewModel, shows their stats / travel level / social counts,
 *       and lets the user follow others (own profile shows Edit, others show Follow). The suggested
 *       travelers row drives multi-user discovery. State flows down; events flow up.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
@Composable
fun ProfileScreen(
    userId: String = "",
    onEditProfile: () -> Unit = {},
    onSuggestedUserClicked: (String) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    profileViewModel: ProfileViewModel = viewModel(),
) {
    val state by profileViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(userId) { profileViewModel.load(userId) }

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
        val profile = state.profile
        if (state.isLoading || profile == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                if (state.isLoading) CircularProgressIndicator(color = RoamlyElectric)
                else Text("Profile not found", color = RoamlyTextMuted, fontFamily = NunitoFamily)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // ── Banner + avatar ───────────────────────────────────────────
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(160.dp).background(
                            Brush.horizontalGradient(
                                colors = listOf(RoamlyElectricDeep.copy(alpha = 0.6f), RoamlyElectric.copy(alpha = 0.3f), RoamlyMidnight)
                            )
                        )
                    )
                    Box(
                        modifier = Modifier.align(Alignment.BottomStart).offset(x = 20.dp, y = 44.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(88.dp).clip(CircleShape).background(RoamlySlate).border(3.dp, RoamlyElectric, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profile.username.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                                color = RoamlyElectric, fontFamily = MontserratFamily, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp
                            )
                        }
                    }
                }
            }

            // ── Name + action (Edit / Follow) ─────────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 16.dp, top = 52.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = profile.fullName.ifBlank { profile.username },
                            fontFamily = MontserratFamily, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = RoamlyTextLight
                        )
                        Text(text = "@${profile.username}", fontFamily = NunitoFamily, fontSize = 13.sp, color = RoamlyTextMuted)
                    }
                    if (state.isOwnProfile) {
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
                            onClick = { profileViewModel.toggleFollow() },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (state.isFollowing) RoamlySlateLight else RoamlyElectric)
                        ) {
                            Text(
                                text = if (state.isFollowing) "Following" else "Follow",
                                color = if (state.isFollowing) RoamlyTextLight else RoamlyMidnight,
                                fontFamily = NunitoFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // ── Location + travel info ────────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (profile.homeCountry.isNotBlank()) InfoChipRow(icon = Icons.Default.Flag, text = profile.homeCountry)
                    if (profile.favoriteDestination.isNotBlank()) InfoChipRow(icon = Icons.Default.LocationOn, text = "Favorite: ${profile.favoriteDestination}")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (profile.travelStyle.isNotBlank()) TravelBadge(label = profile.travelStyle)
                        if (profile.travelFrequency.isNotBlank()) TravelBadge(label = profile.travelFrequency)
                    }
                }
            }

            // ── Follower / Following counts ───────────────────────────────
            item {
                Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    CountColumn(value = profile.followerCount, label = "Followers")
                    CountColumn(value = profile.followingCount, label = "Following")
                }
                Divider(color = RoamlySlateLight)
            }

            // ── Trip stats ────────────────────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Stats", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoamlyTextLight)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = RoamlySlate)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
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
                                modifier = Modifier.size(48.dp).clip(CircleShape).background(RoamlyAurora.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = RoamlyAurora, modifier = Modifier.size(26.dp))
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(text = profile.travelLevel, fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoamlyAurora)
                                Text(text = "${profile.totalTrips} trips · ${profile.totalDistanceKm} km logged", fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
                            }
                        }
                    }
                }
                Divider(color = RoamlySlateLight)
            }

            // ── Suggested travelers to follow ─────────────────────────────
            if (state.suggested.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "Travelers to Follow", fontFamily = MontserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoamlyTextLight)
                        Text(text = "Discover routes from other travelers", fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
                    }
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.suggested, key = { it.uid }) { user ->
                            SuggestedUserCard(
                                user = user,
                                isFollowed = user.uid in state.followedUserIds,
                                onFollowClicked = { profileViewModel.followSuggested(user.uid) },
                                onCardClicked = { onSuggestedUserClicked(user.uid) },
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun CountColumn(value: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "$value", fontFamily = MontserratFamily, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = RoamlyTextLight)
        Text(text = label, fontFamily = NunitoFamily, fontSize = 12.sp, color = RoamlyTextMuted)
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
private fun SuggestedUserCard(
    user: RoamlyUser,
    isFollowed: Boolean,
    onFollowClicked: () -> Unit,
    onCardClicked: () -> Unit,
) {
    Card(
        modifier = Modifier.width(140.dp).clickable { onCardClicked() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = RoamlySlate)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape).background(RoamlyMidnight).border(2.dp, RoamlyElectric, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = RoamlyElectric, modifier = Modifier.size(28.dp))
            }
            Text(text = "@${user.username}", fontFamily = MontserratFamily, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = RoamlyTextLight)
            if (user.homeCountry.isNotBlank()) {
                Text(text = user.homeCountry, fontFamily = NunitoFamily, fontSize = 11.sp, color = RoamlyTextMuted)
            }
            if (user.travelStyle.isNotBlank()) TravelBadge(label = user.travelStyle)
            Button(
                onClick = onFollowClicked,
                modifier = Modifier.fillMaxWidth().height(32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isFollowed) RoamlySlateLight else RoamlyElectric),
                contentPadding = PaddingValues(0.dp)
            ) {
                if (!isFollowed) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null, tint = RoamlyMidnight, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = if (isFollowed) "Following" else "Follow",
                    color = if (isFollowed) RoamlyTextLight else RoamlyMidnight,
                    fontFamily = NunitoFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    RoamlyTheme {
        ProfileScreen()
    }
}
