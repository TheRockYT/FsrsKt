package one.felsen.fsrskt.helper

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

/**
 * Helper class for date and time related operations, particularly for determining if two Instants fall on the same day
 * based on a specified day cutoff hour.
 */
object DateTimeHelper {

    /**
     * Converts an Instant to a day number, considering a specified day cutoff hour.
     *
     * @param dayCutoffHour The hour at which the day is considered to start (default is 4 AM).
     * @param timeZone The time zone to use for the conversion (default is the system's current time zone).
     * @return The day number corresponding to the Instant.
     */
    fun Instant.toDayNumber(dayCutoffHour: Int = 4, timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        val adjustedInstant = this.minus(dayCutoffHour.hours)
        val adjustedDate = adjustedInstant.toLocalDateTime(timeZone).date

        return adjustedDate.toEpochDays()
    }

    /**
     * Calculates the elapsed days between two Instants.
     *
     * @param other The other Instant to compare with. Defaults to the current system time if not provided.
     * @return The number of whole days elapsed between the two Instants as a Double.
     */
    fun Instant.elapsedDays(other: Instant = Clock.System.now()): Double {
        return (this - other).inWholeDays.toDouble()
    }

    /**
     * Determines if two Instants fall on the same day, considering a specified day cutoff hour.
     *
     * @param other The other Instant to compare with.
     * @param dayCutoffHour The hour at which the day is considered to start (default is 4 AM).
     * @param timeZone The time zone to use for the comparison (default is the system's current time zone).
     * @return True if both Instants fall on the same day; otherwise, false.
     */
    fun Instant.isSameDay(other: Instant, dayCutoffHour: Int = 4, timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        return this.toDayNumber(dayCutoffHour, timeZone) == other.toDayNumber(dayCutoffHour, timeZone)
    }
}
