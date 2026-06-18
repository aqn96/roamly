/**
 * What: Pure time/formatting helpers used by trip screens. Kept framework-free so they're unit-testable.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
package com.roamly.app.data

/** Formats an elapsed duration in milliseconds as "HH:MM:SS". */
fun formatElapsed(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return "%02d:%02d:%02d".format(h, m, s)
}
