package com.roamly.app.ui.screens.auth

import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roamly.app.ui.components.RoamlyButton
import com.roamly.app.ui.components.RoamlyTextField
import com.roamly.app.ui.components.SocialSignInButton
import com.roamly.app.ui.theme.MontserratFamily
import com.roamly.app.ui.theme.NunitoFamily
import com.roamly.app.ui.theme.RoamlyElectric
import com.roamly.app.ui.theme.RoamlyMidnight
import com.roamly.app.ui.theme.RoamlySlate
import com.roamly.app.ui.theme.RoamlyTextLight
import com.roamly.app.ui.theme.RoamlyTextMuted
import com.roamly.app.ui.theme.RoamlyTheme

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    // When Firebase authentication succeeds, navigate onward (and reset the one-shot state).
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            authViewModel.resetState()
            onLoginSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(RoamlyMidnight)) {

        // ── Hero section ─────────────────────────────────────────────────
        // TODO: Replace gradient with a real travel photo using:
        //   Image(painter = painterResource(R.drawable.hero_travel), contentScale = ContentScale.Crop)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(RoamlyElectric.copy(alpha = 0.3f), RoamlyMidnight)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Explore the world with us",
                    color = RoamlyTextMuted,
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                // TODO: Replace with actual Roamly logo image using:
                //   Image(painter = painterResource(R.drawable.roamly_logo), ...)
                Text(
                    text = "Roamly",
                    color = RoamlyElectric,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = MontserratFamily
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = onNavigateToSignUp) {
                    Text(text = "Explore", color = RoamlyElectric, fontFamily = NunitoFamily)
                }
            }
        }

        // ── Login card ────────────────────────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Welcome back, traveler!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RoamlyTextMuted
                )
                Text(
                    text = "Log In",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = RoamlyTextLight
                )

                Spacer(modifier = Modifier.height(4.dp))

                RoamlyTextField(value = email, onValueChange = { email = it }, label = "Email")
                RoamlyTextField(value = password, onValueChange = { password = it }, label = "Password", isPassword = true)

                if (uiState is AuthUiState.Error) {
                    Text(
                        text = (uiState as AuthUiState.Error).message,
                        color = Color(0xFFEF4444),
                        fontFamily = NunitoFamily,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                RoamlyButton(
                    text = if (uiState is AuthUiState.Loading) "Logging in…" else "Log In",
                    onClick = { authViewModel.login(email, password) }
                )

                SocialSignInButton(provider = "Apple")
                SocialSignInButton(provider = "Google")
                SocialSignInButton(provider = "Facebook")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Don't have an account?", color = RoamlyTextMuted, fontFamily = NunitoFamily)
                    TextButton(onClick = onNavigateToSignUp) {
                        Text(text = "Sign Up", color = RoamlyElectric, fontFamily = NunitoFamily)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    RoamlyTheme {
        LoginScreen()
    }
}
