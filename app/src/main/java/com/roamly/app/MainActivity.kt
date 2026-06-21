package com.roamly.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.roamly.app.navigation.RoamlyNavGraph
import com.roamly.app.ui.theme.RoamlyTheme

/**
 * What: The single Activity that hosts Roamly's Compose UI. It applies the Midnight Nomad
 *       theme and launches the type-safe navigation graph (RoamlyNavGraph), which owns every
 *       screen and transition.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoamlyTheme {
                RoamlyNavGraph()
            }
        }
    }
}
