package com.roamly.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.roamly.app.ui.screens.auth.LoginScreen
import com.roamly.app.ui.screens.auth.SignUpScreen
import com.roamly.app.ui.screens.auth.CreateProfileScreen
import com.roamly.app.ui.theme.RoamlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoamlyTheme {
//                LoginScreen()
//                SignUpScreen()
                CreateProfileScreen()
            }
        }
    }
}
