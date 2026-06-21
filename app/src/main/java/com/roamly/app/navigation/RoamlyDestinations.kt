package com.roamly.app.navigation

import kotlinx.serialization.Serializable

/**
 * What: Type-safe navigation destinations for the whole Roamly app. Each screen is a
 *       @Serializable route object/class consumed by Navigation Compose's typed API
 *       (composable<T> / navController.navigate(T) / entry.toRoute<T>()).
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 *
 * Using typed routes (instead of String paths) follows the course Topic 06/07 pattern and
 * lets us pass arguments without stringly-typed keys.
 */

// Auth flow
@Serializable object Login
@Serializable object SignUp
@Serializable object CreateProfile

// Main app (bottom-nav tabs)
@Serializable object Home
@Serializable object Discover
@Serializable object Favorites

// Trip flow
@Serializable object LocationPermission
@Serializable object ActiveTrip
@Serializable object TripSummary

// Detail screens (carry arguments)
// A single shared route post. postId is empty in Phase A (dummy data); wired to Firestore doc id in Phase B.
@Serializable data class PostDetail(val postId: String = "")

// A user profile. userId null/blank => the signed-in user's own profile; otherwise another traveler's.
@Serializable data class Profile(val userId: String = "")
