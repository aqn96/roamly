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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roamly.app.ui.components.RoamlyButton
import com.roamly.app.ui.components.RoamlyTextField
import com.roamly.app.ui.theme.RoamlyBlue
import com.roamly.app.ui.theme.RoamlyTeal
import com.roamly.app.ui.theme.RoamlyTheme

val travelStyles = listOf("Backpacker", "Luxury", "Road Trip")

@Composable
fun CreateProfileScreen(
    onProfileSaved: () -> Unit = {}
) {
    var username by rememberSaveable { mutableStateOf("") }
    var homeCountry by rememberSaveable { mutableStateOf("") }
    var selectedStyle by rememberSaveable { mutableStateOf("Backpacker") }

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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Set Up Your Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "This helps us tailor recommendations for you",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

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

                // Travel style selector
                Text(
                    text = "Travel Style",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    travelStyles.forEach { style ->
                        val isSelected = selectedStyle == style
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
                                .clickable { selectedStyle = style },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = style,
                                color = if (isSelected) Color.White else Color.Gray,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                RoamlyButton(text = "Get Started", onClick = onProfileSaved)
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
