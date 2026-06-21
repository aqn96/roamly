package com.roamly.app.data

/**
 * What: The Firestore document model for a Roamly traveler (collection "users/{uid}"). All
 *       fields default so Firestore can deserialize via toObject(); this is the user's profile,
 *       stats, and social counts.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
data class RoamlyUser(
    val uid: String = "",
    val username: String = "",
    val fullName: String = "",
    val email: String = "",
    val homeCountry: String = "",
    val favoriteDestination: String = "",
    val travelStyle: String = "",
    val travelFrequency: String = "",
    val avatarUrl: String = "",
    val totalTrips: Int = 0,
    val totalDistanceKm: Int = 0,
    val routesUnlocked: Int = 0,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
) {
    // Gamified travel level derived from trips logged (shown on the Profile screen).
    val travelLevel: String
        get() = when {
            totalTrips >= 50 -> "Legend"
            totalTrips >= 20 -> "Nomad"
            totalTrips >= 5 -> "Explorer"
            else -> "Wanderer"
        }
}
