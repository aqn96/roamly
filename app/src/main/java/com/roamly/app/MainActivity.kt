package com.roamly.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.roamly.app.ui.screens.auth.CreateProfileScreen
import com.roamly.app.ui.screens.auth.LoginScreen
import com.roamly.app.ui.screens.auth.SignUpScreen
import com.roamly.app.ui.screens.discover.DiscoverScreen
import com.roamly.app.ui.screens.discover.PostDetailScreen
import com.roamly.app.ui.screens.favorites.FavoritesScreen
import com.roamly.app.ui.screens.home.HomeScreen
import com.roamly.app.ui.screens.home.LocationPermissionScreen
import com.roamly.app.ui.screens.profile.ProfileScreen
import com.roamly.app.ui.screens.trip.ActiveTripScreen
import com.roamly.app.ui.screens.trip.TripSummaryScreen
import com.roamly.app.ui.theme.RoamlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoamlyTheme {
                LoginScreen()
//                SignUpScreen()
//                CreateProfileScreen()
//                HomeScreen()
//                LocationPermissionScreen()
//                DiscoverScreen()
//                PostDetailScreen()
//                ProfileScreen()
//                ActiveTripScreen()
//                TripSummaryScreen()
//                FavoritesScreen()
            }
        }
    }
}
