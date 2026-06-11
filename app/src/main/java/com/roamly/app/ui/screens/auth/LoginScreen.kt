package com.roamly.app.ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.roamly.app.ui.theme.RoamlyTheme

@Composable
fun LoginScreen(onNavigateToSignUp: () -> Unit = {}, onLoginSuccess: () -> Unit = {}) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Login Screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    RoamlyTheme {
        LoginScreen()
    }
}
