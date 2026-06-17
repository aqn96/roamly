package com.roamly.app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.roamly.app.ui.components.RoutePost
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/**
 * What: The Firestore gateway for trips and posts — the "Contribute" + "Unlock" data flow.
 *       Saving a finished trip: (1) writes the trip + GPS route under the user, (2) increments the
 *       user's aggregate stats, and (3) publishes a public post so the route appears in other
 *       travelers' Discover feeds (give-to-get). Also reads trips and the global feed.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 *
 * Firebase handles are lazy so constructing this from a Preview never touches an
 * uninitialized FirebaseApp.
 */
class ContentRepository {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    private fun uid(): String? = auth.currentUser?.uid

    /**
     * Persists a finished trip and (optionally) shares it as a public post.
     * @return the number of recommendations unlocked by this contribution.
     */
    suspend fun saveTrip(
        points: List<TrackPoint>,
        distanceKm: Double,
        durationMs: Long,
        shareAsPost: Boolean = true,
    ): Result<Int> = runCatching {
        val uid = uid() ?: error("Not signed in")
        val userRef = db.collection("users").document(uid)
        val profile = userRef.get().awaitResult().toObject(RoamlyUser::class.java)

        val dateLabel = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
        val distanceLabel = "%.2f km".format(distanceKm)
        val durationLabel = formatDuration(durationMs)
        val title = "Trip on $dateLabel"
        // Give-to-get: a real contribution (with movement) unlocks 1–2 recommendations.
        val unlocked = if (points.size >= 2) (1..2).random() else 0

        // 1) Trip document with the full GPS route.
        val tripRef = userRef.collection("trips").document()
        val trip = Trip(
            id = tripRef.id,
            title = title,
            date = dateLabel,
            distanceKm = distanceLabel,
            durationMin = durationLabel,
            routesUnlocked = unlocked,
            createdAt = System.currentTimeMillis(),
            path = points.map { mapOf("lat" to it.latitude, "lng" to it.longitude) },
        )
        tripRef.set(trip).awaitResult()

        // 2) Update aggregate stats on the user document.
        userRef.update(
            mapOf(
                "totalTrips" to FieldValue.increment(1),
                "totalDistanceKm" to FieldValue.increment(distanceKm.roundToInt().toLong()),
                "routesUnlocked" to FieldValue.increment(unlocked.toLong()),
            ),
        ).awaitResult()

        // 3) Publish a public post so other travelers can discover this route.
        if (shareAsPost) {
            val postRef = db.collection("posts").document()
            val post = RoutePost(
                id = postRef.id,
                authorUid = uid,
                username = profile?.username?.ifBlank { "traveler" } ?: "traveler",
                userLocation = profile?.favoriteDestination ?: "",
                routeTitle = title,
                description = "Just logged this route on Roamly. ${distanceLabel} in ${durationLabel}.",
                tags = listOfNotNull(profile?.travelStyle?.lowercase()?.replace(" ", "")).ifEmpty { listOf("travel") },
                distanceKm = distanceLabel,
                durationMin = durationLabel,
                isUnlocked = true,
                createdAt = System.currentTimeMillis(),
            )
            postRef.set(post).awaitResult()
        }

        unlocked
    }

    /** All of the signed-in user's trips, newest first. */
    suspend fun getTrips(): Result<List<Trip>> = runCatching {
        val uid = uid() ?: return@runCatching emptyList()
        db.collection("users").document(uid).collection("trips")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().awaitResult()
            .toObjects(Trip::class.java)
    }

    private fun formatDuration(ms: Long): String {
        val totalMinutes = ms / 60000
        val h = totalMinutes / 60
        val m = totalMinutes % 60
        return if (h > 0) "${h}h ${m}min" else "${m} min"
    }
}
