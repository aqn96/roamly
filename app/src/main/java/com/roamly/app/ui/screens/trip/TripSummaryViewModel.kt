package com.roamly.app.ui.screens.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roamly.app.data.AuthRepository
import com.roamly.app.data.ContentRepository
import com.roamly.app.data.Trip
import com.roamly.app.location.TripSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * What: The "brain" for the Trip Summary screen. On open it persists the just-finished trip
 *       (read from TripSession) to Firestore exactly once, then loads the user's trip history and
 *       all-time stats. Exposed as a single StateFlow UI state (course Topic 06 pattern).
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
data class TripSummaryUiState(
    val isLoading: Boolean = true,
    val trips: List<Trip> = emptyList(),
    val totalTrips: Int = 0,
    val totalDistanceKm: Int = 0,
    val routesUnlocked: Int = 0,
) {
    val latestTrip: Trip? get() = trips.firstOrNull()
}

class TripSummaryViewModel : ViewModel() {

    private val content = ContentRepository()
    private val authRepo = AuthRepository()

    private val _uiState = MutableStateFlow(TripSummaryUiState())
    val uiState: StateFlow<TripSummaryUiState> = _uiState.asStateFlow()

    init { saveThenLoad() }

    private fun saveThenLoad() = viewModelScope.launch {
        // Persist the freshly finished trip (if any) before reading the history back.
        if (TripSession.pendingSave) {
            val points = TripSession.points.value
            val distanceKm = TripSession.distanceMeters(points) / 1000.0
            content.saveTrip(points, distanceKm, TripSession.durationMs)
                .onSuccess { TripSession.markSaved() }
        }
        val trips = content.getTrips().getOrDefault(emptyList())
        val profile = authRepo.loadCurrentProfile().getOrNull()
        _uiState.value = TripSummaryUiState(
            isLoading = false,
            trips = trips,
            totalTrips = profile?.totalTrips ?: trips.size,
            totalDistanceKm = profile?.totalDistanceKm ?: 0,
            routesUnlocked = profile?.routesUnlocked ?: 0,
        )
    }
}
