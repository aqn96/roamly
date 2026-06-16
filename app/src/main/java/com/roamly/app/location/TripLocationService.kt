package com.roamly.app.location

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.roamly.app.data.TrackPoint

/**
 * What: A Foreground Service that passively logs the device's GPS path while a trip is active,
 *       using FusedLocationProviderClient. It posts an ongoing notification (required for
 *       foreground services) so the user always knows recording is happening, and feeds each
 *       fix into TripSession for the UI to draw. This realises the proposal's "Sensor" +
 *       "Foreground Service" design.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 *
 * Mirrors the course Topic 06 NotificationChannel pattern (channel created before notifying).
 */
class TripLocationService : Service() {

    private lateinit var fusedClient: FusedLocationProviderClient

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.locations.forEach { loc ->
                TripSession.addPoint(
                    TrackPoint(loc.latitude, loc.longitude, System.currentTimeMillis()),
                )
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
    }

    // Permission is verified by LocationPermissionScreen before this service is ever started.
    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startAsForeground()

        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_MS)
            .setMinUpdateIntervalMillis(FASTEST_INTERVAL_MS)
            .setMinUpdateDistanceMeters(MIN_DISTANCE_M)
            .build()

        fusedClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
        // START_STICKY: if the OS kills us under memory pressure, restart so logging resumes.
        return START_STICKY
    }

    override fun onDestroy() {
        fusedClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    /** Creates the notification channel (idempotent) and promotes the service to foreground. */
    private fun startAsForeground() {
        val manager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Trip Recording",
            NotificationManager.IMPORTANCE_LOW, // low = no sound, but persistent in the shade
        ).apply { description = "Shown while Roamly is logging your route" }
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Roamly is recording your route")
            .setContentText("Logging your path in the background 🛰")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceCompat.startForeground(
                this, NOTIFICATION_ID, notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION,
            )
        } else {
            ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, 0)
        }
    }

    companion object {
        private const val CHANNEL_ID = "trip_recording"
        private const val NOTIFICATION_ID = 2001
        private const val UPDATE_INTERVAL_MS = 4000L
        private const val FASTEST_INTERVAL_MS = 2000L
        private const val MIN_DISTANCE_M = 5f
    }
}
