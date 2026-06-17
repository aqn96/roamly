package com.roamly.app.data

/**
 * What: A recorded trip document (Firestore "users/{uid}/trips/{tripId}"). Stores the summary
 *       stats plus the full GPS route as an array of {lat,lng} points (the proposal's "Routes as
 *       arrays of GPS coordinates"). All fields default so Firestore can deserialize via toObject().
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
data class Trip(
    val id: String = "",
    val title: String = "",
    val date: String = "",
    val distanceKm: String = "",
    val durationMin: String = "",
    val routesUnlocked: Int = 0,
    val createdAt: Long = 0L,
    val path: List<Map<String, Double>> = emptyList(),
)
