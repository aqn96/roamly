package com.roamly.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.roamly.app.ui.theme.RoamlyBlue
import com.roamly.app.ui.theme.RoamlyTheme

enum class BottomNavTab { HOME, FEED, PROFILE }

@Composable
fun RoamlyBottomNavBar(
    selectedTab: BottomNavTab = BottomNavTab.HOME,
    onTabSelected: (BottomNavTab) -> Unit = {}
) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = selectedTab == BottomNavTab.HOME,
            onClick = { onTabSelected(BottomNavTab.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = RoamlyBlue.copy(alpha = 0.15f))
        )
        NavigationBarItem(
            selected = selectedTab == BottomNavTab.FEED,
            onClick = { onTabSelected(BottomNavTab.FEED) },
            icon = { Icon(Icons.Default.Explore, contentDescription = "Feed") },
            label = { Text("Discover") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = RoamlyBlue.copy(alpha = 0.15f))
        )
        NavigationBarItem(
            selected = selectedTab == BottomNavTab.PROFILE,
            onClick = { onTabSelected(BottomNavTab.PROFILE) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = RoamlyBlue.copy(alpha = 0.15f))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavBarPreview() {
    RoamlyTheme {
        RoamlyBottomNavBar(selectedTab = BottomNavTab.HOME)
    }
}
