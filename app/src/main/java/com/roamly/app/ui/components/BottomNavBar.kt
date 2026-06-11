package com.roamly.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.roamly.app.ui.theme.RoamlyTerracotta
import com.roamly.app.ui.theme.RoamlyTheme

enum class BottomNavTab { HOME, DISCOVER, FAVORITES }

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
            colors = NavigationBarItemDefaults.colors(indicatorColor = RoamlyTerracotta.copy(alpha = 0.15f))
        )
        NavigationBarItem(
            selected = selectedTab == BottomNavTab.DISCOVER,
            onClick = { onTabSelected(BottomNavTab.DISCOVER) },
            icon = { Icon(Icons.Default.Explore, contentDescription = "Discover") },
            label = { Text("Discover") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = RoamlyTerracotta.copy(alpha = 0.15f))
        )
        NavigationBarItem(
            selected = selectedTab == BottomNavTab.FAVORITES,
            onClick = { onTabSelected(BottomNavTab.FAVORITES) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = RoamlyTerracotta.copy(alpha = 0.15f))
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
