package com.roamly.app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.roamly.app.ui.components.RoutePost

/**
 * What: The Firestore gateway for social actions - saving/un-saving favorite posts and
 *       following/un-following other travelers - plus reading favorites, follow state, and
 *       suggested users. These power the multi-user features (Favorites screen, follow buttons).
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 *
 * Firebase handles are lazy so constructing this from a Preview never touches an
 * uninitialized FirebaseApp.
 */
class SocialRepository {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    private fun uid(): String? = auth.currentUser?.uid

    // Favorites (bookmarks)

    // Stores the full post under the user's favorites so the Favorites screen reads in one query.
    suspend fun toggleFavorite(post: RoutePost): Result<Boolean> = runCatching {
        val uid = uid() ?: error("Not signed in")
        val favRef = db.collection("users").document(uid).collection("favorites").document(post.id)
        val exists = favRef.get().awaitResult().exists()
        if (exists) {
            favRef.delete().awaitResult()
            false
        } else {
            favRef.set(post).awaitResult()
            true
        }
    }

    suspend fun getFavoriteIds(): Result<Set<String>> = runCatching {
        val uid = uid() ?: return@runCatching emptySet()
        db.collection("users").document(uid).collection("favorites")
            .get().awaitResult().documents.map { it.id }.toSet()
    }

    suspend fun getFavorites(): Result<List<RoutePost>> = runCatching {
        val uid = uid() ?: return@runCatching emptyList()
        db.collection("users").document(uid).collection("favorites")
            .get().awaitResult().toObjects(RoutePost::class.java)
    }

    // Follows

    suspend fun isFollowing(targetUid: String): Result<Boolean> = runCatching {
        val uid = uid() ?: return@runCatching false
        db.collection("users").document(uid).collection("following").document(targetUid)
            .get().awaitResult().exists()
    }

    // Follows/unfollows a user and keeps both follower/following counters in sync. Returns new state.
    suspend fun toggleFollow(targetUid: String): Result<Boolean> = runCatching {
        val uid = uid() ?: error("Not signed in")
        if (uid == targetUid) error("You can't follow yourself")
        val meRef = db.collection("users").document(uid)
        val targetRef = db.collection("users").document(targetUid)
        val followRef = meRef.collection("following").document(targetUid)
        val alreadyFollowing = followRef.get().awaitResult().exists()
        if (alreadyFollowing) {
            followRef.delete().awaitResult()
            meRef.update("followingCount", FieldValue.increment(-1)).awaitResult()
            targetRef.update("followerCount", FieldValue.increment(-1)).awaitResult()
            false
        } else {
            followRef.set(mapOf("createdAt" to System.currentTimeMillis())).awaitResult()
            meRef.update("followingCount", FieldValue.increment(1)).awaitResult()
            targetRef.update("followerCount", FieldValue.increment(1)).awaitResult()
            true
        }
    }

    // Suggested travelers to follow: other users, excluding self (by uid AND by username, so
    // accounts that share your username never appear), and anyone already followed.
    suspend fun getSuggestedUsers(limit: Int = 10): Result<List<RoamlyUser>> = runCatching {
        val uid = uid()
        val myUsername = if (uid != null) {
            db.collection("users").document(uid).get().awaitResult()
                .toObject(RoamlyUser::class.java)?.username
        } else null
        val following = if (uid != null) {
            db.collection("users").document(uid).collection("following")
                .get().awaitResult().documents.map { it.id }.toSet()
        } else emptySet()
        db.collection("users").limit(limit.toLong() + 10).get().awaitResult()
            .toObjects(RoamlyUser::class.java)
            .filter {
                it.uid != uid &&
                    it.uid !in following &&
                    it.username.isNotBlank() &&
                    !it.username.equals(myUsername, ignoreCase = true)
            }
            .take(limit)
    }
}
