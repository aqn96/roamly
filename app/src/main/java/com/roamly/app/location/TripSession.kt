package com.roamly.app.location

import android.location.Location
import android.os.SystemClock
import com.roamly.app.data.TrackPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * What: Process-wide source of truth for the trip currently being recorded. The Foreground
 *       Service (TripLocationService) writes GPS points here; ViewModels/UI observe the
 *       StateFlows. Kept as a single object so the Service and the UI share one stream without
 *       needing dependency injection.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
object TripSession {

    private val _points = MutableStateFlow<List<TrackPoint>>(emptyList())
    // Ordered GPS samples of the active trip.
    val points: StateFlow<List<TrackPoint>> = _points.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    // True while the Foreground Service is actively logging.
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    // elapsedRealtime() at the moment recording started, used to compute trip duration.
    @Volatile
    var startElapsedMs: Long = 0L
        private set

    @Volatile
    private var endElapsedMs: Long = 0L

    // True after a trip ends until the Trip Summary screen persists it (guards against double-save).
    @Volatile
    var pendingSave: Boolean = false
        private set

    // Total recorded duration in ms (start to stop).
    val durationMs: Long get() = (endElapsedMs - startElapsedMs).coerceAtLeast(0L)

    // Begins a fresh recording, clearing any previous route.
    fun begin() {
        _points.value = emptyList()
        startElapsedMs = SystemClock.elapsedRealtime()
        _isRecording.value = true
        pendingSave = false
    }

    // Appends a newly received GPS sample. Creates a new list so StateFlow emits.
    fun addPoint(point: TrackPoint) {
        _points.update { it + point }
    }

    // Stops recording (the points remain available so the summary screen can read + persist them).
    fun end() {
        _isRecording.value = false
        endElapsedMs = SystemClock.elapsedRealtime()
        pendingSave = true
    }

    // Marks the finished trip as persisted so it isn't saved twice.
    fun markSaved() {
        pendingSave = false
    }

    // Total route length in meters via great-circle distance between consecutive points.
    fun distanceMeters(pts: List<TrackPoint> = _points.value): Double {
        if (pts.size < 2) return 0.0
        var total = 0.0
        val out = FloatArray(1)
        for (i in 1 until pts.size) {
            Location.distanceBetween(
                pts[i - 1].latitude, pts[i - 1].longitude,
                pts[i].latitude, pts[i].longitude,
                out,
            )
            total += out[0]
        }
        return total
    }
}
