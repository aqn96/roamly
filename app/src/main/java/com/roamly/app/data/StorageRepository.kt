package com.roamly.app.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

/**
 * What: Minimal Firebase Storage gateway for uploading a profile photo and returning its
 *       downloadable URL. The rest of the app still reads/writes profile data through Firestore.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
class StorageRepository {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }

    suspend fun uploadProfilePhoto(imageUri: Uri): Result<String> = runCatching {
        val uid = auth.currentUser?.uid ?: error("Not signed in")
        val fileRef = storage.reference.child("profile_photos/$uid/${System.currentTimeMillis()}.jpg")
        fileRef.putFile(imageUri).awaitResult()
        fileRef.downloadUrl.awaitResult().toString()
    }
}
