/**
 * What: Unit tests for the pure elapsed-time formatter used on the trip screens.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
package com.roamly.app

import com.roamly.app.data.formatElapsed
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormatTest {

    @Test
    fun zero_isAllZeros() {
        assertEquals("00:00:00", formatElapsed(0))
    }

    @Test
    fun oneSecond() {
        assertEquals("00:00:01", formatElapsed(1_000))
    }

    @Test
    fun minutesAndSeconds() {
        assertEquals("00:01:01", formatElapsed(61_000))
    }

    @Test
    fun hoursMinutesSeconds() {
        assertEquals("01:01:01", formatElapsed(3_661_000))
    }

    @Test
    fun negativeClampsToZero() {
        assertEquals("00:00:00", formatElapsed(-5_000))
    }
}
