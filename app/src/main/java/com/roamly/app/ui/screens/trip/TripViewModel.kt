package com.roamly.app.ui.screens.trip

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roamly.app.data.TrackPoint
import com.roamly.app.location.TripLocationService
import com.roamly.app.location.TripSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * What: The "brain" for the active-trip screen. It starts/stops the GPS Foreground Service and
 *       exposes the live route + derived distance as StateFlow so the UI can observe and redraw
 *       (course Topic 06 ViewModel + StateFlow pattern). The recorded points survive into the
 *       Trip Summary screen via TripSession.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
class TripViewModel : ViewModel() {

    /** Live, ordered GPS samples of the active trip. */
    val routePoints: StateFlow<List<TrackPoint>> = TripSession.points

    /** Whether the Foreground Service is currently logging. */
    val isRecording: StateFlow<Boolean> = TripSession.isRecording

    /** Derived state: total distance in kilometres, recomputed whenever the route grows. */
    val distanceKm: StateFlow<Double> = TripSession.points
        .map { TripSession.distanceMeters(it) / 1000.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    /** Begins a recording and launches the Foreground Service. */
    fun startTrip(context: Context) {
        if (isRecording.value) return
        TripSession.begin()
        ContextCompat.startForegroundService(
            context, Intent(context, TripLocationService::class.java),
        )
    }

    /** Stops recording and tears down the Foreground Service. Points stay in TripSession. */
    fun stopTrip(context: Context) {
        TripSession.end()
        context.stopService(Intent(context, TripLocationService::class.java))
        // Phase B: persist the recorded route to Firestore here.
    }
}
