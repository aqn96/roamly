package com.roamly.app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

/**
 * What: The single gateway to Firebase Authentication (email/password) and the Firestore
 *       "users" collection. Handles sign-up, login, sign-out, and reading/writing the signed-in
 *       user's profile document. Suspend functions return Result so ViewModels can surface
 *       success/error cleanly.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 *
 * Firebase handles are lazy so constructing this (e.g. via a ViewModel in a @Preview) never
 * touches an uninitialized FirebaseApp.
 */
class AuthRepository {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val users by lazy { FirebaseFirestore.getInstance().collection("users") }

    val currentUid: String? get() = auth.currentUser?.uid
    val isLoggedIn: Boolean get() = auth.currentUser != null

    // Creates the account and seeds a partial user document with the name/email.
    suspend fun signUp(fullName: String, email: String, password: String): Result<Unit> = runCatching {
        val result = auth.createUserWithEmailAndPassword(email, password).awaitResult()
        val uid = result.user?.uid ?: error("Sign-up returned no user")
        users.document(uid).set(
            mapOf("uid" to uid, "fullName" to fullName, "email" to email),
            SetOptions.merge(),
        ).awaitResult()
        Unit
    }

    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email, password).awaitResult()
        Unit
    }

    // Merges profile fields entered on Create Profile into the signed-in user's document.
    suspend fun saveProfile(user: RoamlyUser): Result<Unit> = runCatching {
        val uid = currentUid ?: error("Not signed in")
        users.document(uid).set(user.copy(uid = uid), SetOptions.merge()).awaitResult()
        Unit
    }

    // Loads the signed-in user's profile (null if not signed in or no document yet).
    suspend fun loadCurrentProfile(): Result<RoamlyUser?> = runCatching {
        val uid = currentUid ?: return@runCatching null
        users.document(uid).get().awaitResult().toObject(RoamlyUser::class.java)
    }

    suspend fun loadProfile(uid: String): Result<RoamlyUser?> = runCatching {
        users.document(uid).get().awaitResult().toObject(RoamlyUser::class.java)
    }

    fun signOut() = auth.signOut()
}
