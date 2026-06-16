package com.roamly.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roamly.app.ui.components.RoamlyButton
import com.roamly.app.ui.components.RoamlyTextField
import com.roamly.app.ui.theme.MontserratFamily
import com.roamly.app.ui.theme.NunitoFamily
import com.roamly.app.ui.theme.RoamlyElectric
import com.roamly.app.ui.theme.RoamlyMidnight
import com.roamly.app.ui.theme.RoamlySlate
import com.roamly.app.ui.theme.RoamlySlateLight
import com.roamly.app.ui.theme.RoamlyTextLight
import com.roamly.app.ui.theme.RoamlyTextMuted
import com.roamly.app.ui.theme.RoamlyTheme

private val travelStyles = listOf("Solo Trip", "Group Trip", "Nomad")
private val travelFrequencies = listOf("Occasional", "Regular", "Frequent")

@Composable
fun CreateProfileScreen(
    onProfileSaved: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var username by rememberSaveable { mutableStateOf("") }
    var homeCountry by rememberSaveable { mutableStateOf("") }
    var favoriteLocation by rememberSaveable { mutableStateOf("") }
    var selectedStyle by rememberSaveable { mutableStateOf("Solo Trip") }
    var selectedFrequency by rememberSaveable { mutableStateOf("Regular") }
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    // Once the profile is written to Firestore, enter the app.
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            authViewModel.resetState()
            onProfileSaved()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(RoamlyMidnight)) {

        // ── Hero section ─────────────────────────────────────────────────
        // TODO: Replace gradient with a real travel photo using:
        //   Image(painter = painterResource(R.drawable.hero_travel), contentScale = ContentScale.Crop)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.28f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(RoamlyElectric.copy(alpha = 0.3f), RoamlyMidnight)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // TODO: Replace with actual Roamly logo image using:
                //   Image(painter = painterResource(R.drawable.roamly_logo), ...)
                Text(
                    text = "Roamly",
                    color = RoamlyElectric,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = MontserratFamily
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tell us about yourself",
                    color = RoamlyTextMuted,
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp
                )
            }
        }

        // ── Profile setup card ────────────────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.80f)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = RoamlySlate),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Set Up Your Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = RoamlyTextLight,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "This helps us tailor recommendations for you",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RoamlyTextMuted,
                    modifier = Modifier.fillMaxWidth()
                )

                // ── Avatar picker ─────────────────────────────────────────
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(RoamlyMidnight)
                            .border(2.dp, RoamlyElectric, CircleShape)
                            .clickable {
                                // TODO: Launch image picker here using:
                                //   val launcher = rememberLauncherForActivityResult(
                                //       ActivityResultContracts.GetContent()
                                //   ) { uri -> /* handle selected image uri */ }
                                //   launcher.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // TODO: Once image is selected, replace Icon with:
                        //   Image(bitmap = selectedBitmap, contentScale = ContentScale.Crop,
                        //         modifier = Modifier.fillMaxSize().clip(CircleShape))
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Add profile photo",
                            tint = RoamlyElectric,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Tap to add photo", color = RoamlyTextMuted, fontFamily = NunitoFamily, fontSize = 12.sp)
                }

                RoamlyTextField(value = username, onValueChange = { username = it }, label = "Username")
                RoamlyTextField(value = homeCountry, onValueChange = { homeCountry = it }, label = "Home Country")

                SelectorRow(label = "Travel Style", options = travelStyles, selected = selectedStyle, onSelect = { selectedStyle = it })
                SelectorRow(label = "How Often Do You Travel?", options = travelFrequencies, selected = selectedFrequency, onSelect = { selectedFrequency = it })

                RoamlyTextField(value = favoriteLocation, onValueChange = { favoriteLocation = it }, label = "Favorite Destination (e.g. Tokyo, Japan)")

                if (uiState is AuthUiState.Error) {
                    Text(
                        text = (uiState as AuthUiState.Error).message,
                        color = Color(0xFFEF4444),
                        fontFamily = NunitoFamily,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                RoamlyButton(
                    text = if (uiState is AuthUiState.Loading) "Saving…" else "Get Started",
                    onClick = {
                        authViewModel.saveProfile(
                            username = username,
                            homeCountry = homeCountry,
                            favoriteDestination = favoriteLocation,
                            travelStyle = selectedStyle,
                            travelFrequency = selectedFrequency,
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun SelectorRow(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = RoamlyTextLight)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                val isSelected = selected == option
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .background(
                            color = if (isSelected) RoamlyElectric else RoamlyMidnight,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) RoamlyElectric else RoamlySlateLight,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { onSelect(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = if (isSelected) RoamlyMidnight else RoamlyTextMuted,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateProfileScreenPreview() {
    RoamlyTheme {
        CreateProfileScreen()
    }
}
