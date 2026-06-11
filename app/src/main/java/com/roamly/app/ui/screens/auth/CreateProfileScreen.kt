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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.roamly.app.ui.theme.RoamlyBlue
import com.roamly.app.ui.theme.RoamlyLightGray
import com.roamly.app.ui.theme.RoamlyTeal
import com.roamly.app.ui.theme.RoamlyTheme

private val travelStyles = listOf("Solo Trip", "Group Trip", "Nomad")
private val travelFrequencies = listOf("Occasional", "Regular", "Frequent")

@Composable
fun CreateProfileScreen(
    onProfileSaved: () -> Unit = {}
) {
    var username by rememberSaveable { mutableStateOf("") }
    var homeCountry by rememberSaveable { mutableStateOf("") }
    var favoriteLocation by rememberSaveable { mutableStateOf("") }
    var selectedStyle by rememberSaveable { mutableStateOf("Solo Trip") }
    var selectedFrequency by rememberSaveable { mutableStateOf("Regular") }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Hero section ─────────────────────────────────────────────────
        // TODO: Replace gradient with a real travel photo using:
        //   Image(painter = painterResource(R.drawable.hero_travel), contentScale = ContentScale.Crop)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.28f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(RoamlyBlue, RoamlyTeal)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // TODO: Replace with actual Roamly logo image using:
                //   Image(painter = painterResource(R.drawable.roamly_logo), ...)
                Text(
                    text = "Roamly",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tell us about yourself",
                    color = Color.White.copy(alpha = 0.85f),
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
            colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "This helps us tailor recommendations for you",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )

                // ── Avatar picker ─────────────────────────────────────────
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(RoamlyLightGray)
                            .border(2.dp, RoamlyBlue, CircleShape)
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
                            tint = RoamlyBlue,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tap to add photo",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                RoamlyTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Username"
                )
                RoamlyTextField(
                    value = homeCountry,
                    onValueChange = { homeCountry = it },
                    label = "Home Country"
                )

                // ── Travel style selector ─────────────────────────────────
                SelectorRow(
                    label = "Travel Style",
                    options = travelStyles,
                    selected = selectedStyle,
                    onSelect = { selectedStyle = it }
                )

                // ── Travel frequency selector ─────────────────────────────
                SelectorRow(
                    label = "How Often Do You Travel?",
                    options = travelFrequencies,
                    selected = selectedFrequency,
                    onSelect = { selectedFrequency = it }
                )

                // ── Favorite location ─────────────────────────────────────
                RoamlyTextField(
                    value = favoriteLocation,
                    onValueChange = { favoriteLocation = it },
                    label = "Favorite Destination (e.g. Tokyo, Japan)"
                )

                Spacer(modifier = Modifier.height(4.dp))

                RoamlyButton(text = "Get Started", onClick = onProfileSaved)
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
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                val isSelected = selected == option
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .background(
                            color = if (isSelected) RoamlyBlue else Color.White,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) RoamlyBlue else Color.LightGray,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { onSelect(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = if (isSelected) Color.White else Color.Gray,
                        style = MaterialTheme.typography.labelMedium
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
