package com.roamly.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.roamly.app.data.AuthRepository
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

/**
 * What: The single NavHost that wires every Roamly screen together and owns all
 *       navigation transitions (auth flow, trip flow, bottom-nav tabs, detail screens).
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 *
 * State flows down into each screen; navigation events flow up via the screens' callbacks
 * (onLoginSuccess, onStartTrip, onPostClicked, ...) which this graph maps to NavController calls.
 */
@Composable
fun RoamlyNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    // Returning users (already signed in to Firebase) skip straight to Home.
    val startDestination: Any = if (AuthRepository().isLoggedIn) Home else Login

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // ── Auth flow ─────────────────────────────────────────────────────────
        composable<Login> {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(SignUp) },
                // Phase A: navigate straight to Home. Phase B swaps in real Firebase Auth.
                onLoginSuccess = {
                    navController.navigate(Home) { popUpTo(Login) { inclusive = true } }
                },
            )
        }
        composable<SignUp> {
            SignUpScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onSignUpSuccess = { navController.navigate(CreateProfile) },
            )
        }
        composable<CreateProfile> {
            CreateProfileScreen(
                onProfileSaved = {
                    navController.navigate(Home) { popUpTo(Login) { inclusive = true } }
                },
            )
        }

        // ── Main app (bottom-nav tabs) ─────────────────────────────────────────
        composable<Home> {
            HomeScreen(
                onStartTrip = { navController.navigate(LocationPermission) },
                onNavigateToProfile = { navController.navigate(Profile()) },
                onNavigateToDiscover = { navController.navigateTab(Discover) },
                onNavigateToFavorites = { navController.navigateTab(Favorites) },
            )
        }
        composable<Discover> {
            DiscoverScreen(
                onNavigateToHome = { navController.navigateTab(Home) },
                onNavigateToFavorites = { navController.navigateTab(Favorites) },
                onPostClicked = { post -> navController.navigate(PostDetail(post.id)) },
            )
        }
        composable<Favorites> {
            FavoritesScreen(
                onNavigateToHome = { navController.navigateTab(Home) },
                onNavigateToDiscover = { navController.navigateTab(Discover) },
                onPostClicked = { post -> navController.navigate(PostDetail(post.id)) },
            )
        }

        // ── Trip flow ──────────────────────────────────────────────────────────
        composable<LocationPermission> {
            LocationPermissionScreen(
                onAllowClicked = {
                    navController.navigate(ActiveTrip) { popUpTo(Home) }
                },
                onNotNowClicked = { navController.popBackStack() },
            )
        }
        composable<ActiveTrip> {
            ActiveTripScreen(
                onStopTrip = {
                    navController.navigate(TripSummary) { popUpTo(Home) }
                },
                onSeeMoreStats = { navController.navigate(TripSummary) { popUpTo(Home) } },
            )
        }
        composable<TripSummary> {
            TripSummaryScreen(
                onBack = { navController.popBackStack() },
                onViewRecommendations = { navController.navigateTab(Discover) },
            )
        }

        // ── Detail screens ──────────────────────────────────────────────────────
        composable<PostDetail> { entry ->
            val route = entry.toRoute<PostDetail>()
            PostDetailScreen(
                postId = route.postId,
                onBack = { navController.popBackStack() },
                onUserClicked = { userId -> navController.navigate(Profile(userId)) },
            )
        }
        composable<Profile> { entry ->
            val route = entry.toRoute<Profile>()
            ProfileScreen(
                userId = route.userId,
                onEditProfile = { navController.navigate(CreateProfile) },
                onLogout = {
                    navController.navigate(Login) { popUpTo(0) { inclusive = true } }
                },
                onSuggestedUserClicked = { userId -> navController.navigate(Profile(userId)) },
                onNavigateToHome = { navController.navigateTab(Home) },
                onNavigateToDiscover = { navController.navigateTab(Discover) },
                onNavigateToFavorites = { navController.navigateTab(Favorites) },
            )
        }
    }
}

/**
 * Switches between the three bottom-nav tabs the way users expect: a single instance per tab,
 * popping back to Home so the back stack never piles up duplicate tabs, while preserving each
 * tab's scroll/state via saveState/restoreState.
 */
private fun NavController.navigateTab(route: Any) {
    navigate(route) {
        popUpTo(Home) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
