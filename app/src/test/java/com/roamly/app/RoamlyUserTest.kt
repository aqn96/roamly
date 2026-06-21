/**
 * What: Unit tests for the RoamlyUser.travelLevel gamification thresholds.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
package com.roamly.app

import com.roamly.app.data.RoamlyUser
import org.junit.Assert.assertEquals
import org.junit.Test

class RoamlyUserTest {

    private fun levelFor(trips: Int) = RoamlyUser(totalTrips = trips).travelLevel

    @Test
    fun fewTrips_isWanderer() {
        assertEquals("Wanderer", levelFor(0))
        assertEquals("Wanderer", levelFor(4))
    }

    @Test
    fun midTrips_isExplorer() {
        assertEquals("Explorer", levelFor(5))
        assertEquals("Explorer", levelFor(19))
    }

    @Test
    fun moreTrips_isNomad() {
        assertEquals("Nomad", levelFor(20))
        assertEquals("Nomad", levelFor(49))
    }

    @Test
    fun manyTrips_isLegend() {
        assertEquals("Legend", levelFor(50))
        assertEquals("Legend", levelFor(120))
    }

    @Test
    fun newUser_hasSafeDefaults() {
        val u = RoamlyUser()
        assertEquals(0, u.totalTrips)
        assertEquals("Wanderer", u.travelLevel)
    }
}
