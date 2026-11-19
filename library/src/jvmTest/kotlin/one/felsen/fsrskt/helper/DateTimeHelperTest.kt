package one.felsen.fsrskt.helper

import kotlinx.datetime.TimeZone
import one.felsen.fsrskt.helper.DateTimeHelper.elapsedCalendarDays
import one.felsen.fsrskt.helper.DateTimeHelper.elapsedDays
import one.felsen.fsrskt.helper.DateTimeHelper.isSameDay
import one.felsen.fsrskt.helper.DateTimeHelper.toDayNumber
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class DateTimeHelperTest {

    private val berlin = TimeZone.of("Europe/Berlin")

    private fun instant(value: String) = Instant.parse(value)

    @Test
    fun elapsedDaysOfExactlyThreeDays() {
        assertEquals(3.0, instant("2024-06-12T12:00:00Z").elapsedDays(instant("2024-06-15T12:00:00Z")))
    }

    @Test
    fun elapsedDaysRoundsDownToWholeDays() {
        assertEquals(2.0, instant("2024-06-12T12:00:00Z").elapsedDays(instant("2024-06-15T11:00:00Z")))
    }

    @Test
    fun elapsedDaysDefaultsToCurrentTime() {
        assertEquals(3.0, (Clock.System.now() - 3.days).elapsedDays())
    }

    @Test
    fun toDayNumberInUtc() {
        assertEquals(19888L, instant("2024-06-15T03:59:00Z").toDayNumber(4, TimeZone.UTC))
        assertEquals(19889L, instant("2024-06-15T04:00:00Z").toDayNumber(4, TimeZone.UTC))
    }

    @Test
    fun toDayNumberInBerlin() {
        assertEquals(19888L, instant("2024-06-15T01:59:00Z").toDayNumber(4, berlin))
        assertEquals(19889L, instant("2024-06-15T02:00:00Z").toDayNumber(4, berlin))
    }

    @Test
    fun toDayNumberWithOtherCutoff() {
        assertEquals(19888L, instant("2024-06-14T23:59:00Z").toDayNumber(0, TimeZone.UTC))
        assertEquals(19889L, instant("2024-06-15T00:00:00Z").toDayNumber(0, TimeZone.UTC))
    }

    @Test
    fun toDayNumberRejectsCutoffOutsideOfDay() {
        listOf(-1, 24).forEach { cutoff ->
            assertFailsWith<IllegalArgumentException>("cutoff $cutoff") {
                instant("2024-06-15T12:00:00Z").toDayNumber(cutoff, TimeZone.UTC)
            }
        }
    }

    @Test
    fun toDayNumberAtSpringClockChange() {
        assertEquals(19812L, instant("2024-03-31T01:59:00Z").toDayNumber(4, berlin))
        assertEquals(19813L, instant("2024-03-31T02:00:00Z").toDayNumber(4, berlin))
    }

    @Test
    fun toDayNumberAtAutumnClockChange() {
        assertEquals(20022L, instant("2024-10-27T02:00:00Z").toDayNumber(4, berlin))
        assertEquals(20023L, instant("2024-10-27T03:00:00Z").toDayNumber(4, berlin))
    }

    @Test
    fun isSameDayBeforeCutoffOfNextDay() {
        assertTrue(instant("2024-06-14T23:00:00Z").isSameDay(instant("2024-06-15T03:00:00Z"), 4, TimeZone.UTC))
    }

    @Test
    fun isNotSameDayAfterCutoffOfNextDay() {
        assertFalse(instant("2024-06-14T23:00:00Z").isSameDay(instant("2024-06-15T05:00:00Z"), 4, TimeZone.UTC))
    }

    @Test
    fun elapsedCalendarDaysBeforeCutoffOfNextDay() {
        assertEquals(0.0, elapsedCalendarDays("2024-06-14T23:00:00Z", "2024-06-15T03:00:00Z"))
    }

    @Test
    fun elapsedCalendarDaysAfterCutoffOfNextDay() {
        assertEquals(1.0, elapsedCalendarDays("2024-06-14T23:00:00Z", "2024-06-15T05:00:00Z"))
    }

    @Test
    fun elapsedCalendarDaysOfLessThanTwentyFourHours() {
        assertEquals(1.0, elapsedCalendarDays("2024-06-14T20:00:00Z", "2024-06-15T19:30:00Z"))
    }

    @Test
    fun elapsedCalendarDaysAcrossCutoffWithinMinutes() {
        assertEquals(1.0, elapsedCalendarDays("2024-06-15T03:59:00Z", "2024-06-15T04:01:00Z"))
    }

    @Test
    fun elapsedCalendarDaysOfSeveralDays() {
        assertEquals(3.0, elapsedCalendarDays("2024-01-01T12:00:00Z", "2024-01-04T11:00:00Z"))
    }

    private fun elapsedCalendarDays(from: String, to: String): Double =
        instant(from).elapsedCalendarDays(instant(to), dayCutoffHour = 4, timeZone = TimeZone.UTC)
}
