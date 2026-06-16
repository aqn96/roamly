package com.roamly.app.data

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * What: Bridges Google Play Services / Firebase callback-based Task<T> APIs into Kotlin
 *       coroutines so repositories can `await` them inside viewModelScope (course Topic 06
 *       coroutine style). Avoids pulling in an extra coroutines-play-services dependency.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
suspend fun <T> Task<T>.awaitResult(): T = suspendCancellableCoroutine { cont ->
    addOnSuccessListener { cont.resume(it) }
    addOnFailureListener { cont.resumeWithException(it) }
    addOnCanceledListener { cont.cancel() }
}
