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

// Free public travel photos (Unsplash) assigned to auto-created posts so the feed shows real imagery.
private val ROUTE_PHOTOS = listOf(
    "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=800&q=70",
    "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=800&q=70",
    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&q=70",
    "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&q=70",
    "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=800&q=70",
    "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=800&q=70",
)

/**
 * What: The Firestore gateway for trips and posts - the "Contribute" + "Unlock" data flow.
 *       Saving a finished trip: (1) writes the trip + GPS route under the user, (2) increments the
 *       user's aggregate stats, and (3) publishes a public post so the route appears in other
 *       travelers' Discover feeds (give-to-get). Also reads trips and the global feed.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 *
 * Firebase handles are lazy so constructing this from a Preview never touches an
 * uninitialized FirebaseApp.
 */
class ContentRepository {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    private fun uid(): String? = auth.currentUser?.uid

    // Saves a finished trip and optionally shares it as a public post.
    // Returns how many recommendations this contribution unlocked.
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
                tags = listOfNotNull(profile?.travelStyle?.lowercase()?.replace(" ", "")?.ifBlank { null }).ifEmpty { listOf("travel") },
                distanceKm = distanceLabel,
                durationMin = durationLabel,
                mapImageUrl = ROUTE_PHOTOS.random(),
                isUnlocked = true,
                createdAt = System.currentTimeMillis(),
            )
            postRef.set(post).awaitResult()
        }

        unlocked
    }

    // All of the signed-in user's trips, newest first.
    suspend fun getTrips(): Result<List<Trip>> = runCatching {
        val uid = uid() ?: return@runCatching emptyList()
        db.collection("users").document(uid).collection("trips")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().awaitResult()
            .toObjects(Trip::class.java)
    }

    // Discover feed / posts (multi-user)

    // The global Discover feed: every traveler's posts, newest first.
    suspend fun getFeed(limit: Long = 50): Result<List<RoutePost>> = runCatching {
        db.collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit)
            .get().awaitResult()
            .toObjects(RoutePost::class.java)
    }

    suspend fun getPost(postId: String): Result<RoutePost?> = runCatching {
        db.collection("posts").document(postId).get().awaitResult().toObject(RoutePost::class.java)
    }

    // Whether the signed-in user has liked this post.
    suspend fun isLiked(postId: String): Result<Boolean> = runCatching {
        val uid = uid() ?: return@runCatching false
        db.collection("users").document(uid).collection("likes").document(postId)
            .get().awaitResult().exists()
    }

    // Toggles a like: tracks it per-user and keeps the post's likeCount in sync. Returns new state.
    suspend fun toggleLike(postId: String): Result<Boolean> = runCatching {
        val uid = uid() ?: error("Not signed in")
        val likeRef = db.collection("users").document(uid).collection("likes").document(postId)
        val postRef = db.collection("posts").document(postId)
        val alreadyLiked = likeRef.get().awaitResult().exists()
        if (alreadyLiked) {
            likeRef.delete().awaitResult()
            postRef.update("likeCount", FieldValue.increment(-1)).awaitResult()
            false
        } else {
            likeRef.set(mapOf("createdAt" to System.currentTimeMillis())).awaitResult()
            postRef.update("likeCount", FieldValue.increment(1)).awaitResult()
            true
        }
    }

    // Comments

    suspend fun getComments(postId: String): Result<List<Comment>> = runCatching {
        db.collection("posts").document(postId).collection("comments")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().awaitResult()
            .toObjects(Comment::class.java)
    }

    // Adds a comment authored by the signed-in user and bumps the post's commentCount.
    suspend fun addComment(postId: String, text: String): Result<Unit> = runCatching {
        val uid = uid() ?: error("Not signed in")
        val profile = db.collection("users").document(uid).get().awaitResult().toObject(RoamlyUser::class.java)
        val postRef = db.collection("posts").document(postId)
        val commentRef = postRef.collection("comments").document()
        val comment = Comment(
            id = commentRef.id,
            authorUid = uid,
            username = profile?.username?.ifBlank { "traveler" } ?: "traveler",
            text = text.trim(),
            createdAt = System.currentTimeMillis(),
        )
        commentRef.set(comment).awaitResult()
        postRef.update("commentCount", FieldValue.increment(1)).awaitResult()
        Unit
    }

    private fun formatDuration(ms: Long): String {
        val totalMinutes = ms / 60000
        val h = totalMinutes / 60
        val m = totalMinutes % 60
        return if (h > 0) "${h}h ${m}min" else "${m} min"
    }
}
