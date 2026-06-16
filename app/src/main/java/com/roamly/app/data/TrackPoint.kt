package com.roamly.app.data

/**
 * What: One recorded GPS sample on a trip route — the atomic unit of data Roamly logs.
 *       A trip is just an ordered list of these. Stored to Firestore as the route array.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
data class TrackPoint(
    val latitude: Double,
    val longitude: Double,
    val timestampMs: Long,
)
