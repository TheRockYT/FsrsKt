package one.felsen.fsrskt.fsrs6

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests [Fsrs.retrievability] with elapsed times that the reference fixtures do not cover.
 */
class FsrsRetrievabilityTest {

    private val fsrs = Fsrs()

    @Test
    fun retrievabilityIsOneWhenElapsedTimeIsOneDayNegative() {
        assertEquals(1.0, fsrs.retrievability(-1.0, 2.0))
    }

    @Test
    fun retrievabilityIsOneWhenElapsedTimeIsFiveDaysNegative() {
        assertEquals(1.0, fsrs.retrievability(-5.0, 2.0))
    }

    @Test
    fun retrievabilityIsZeroWhenElapsedTimeIsUnknown() {
        assertEquals(0.0, fsrs.retrievability(null, 2.0))
    }
}
