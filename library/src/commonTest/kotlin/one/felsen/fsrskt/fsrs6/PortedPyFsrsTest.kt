package one.felsen.fsrskt.fsrs6

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests ported from py-fsrs `tests/test_basic.py`.
 * Reference: https://github.com/open-spaced-repetition/py-fsrs/blob/9446cb06605c597a063aeee49f7d188d42e34dc2/tests/test_basic.py
 */
class PortedPyFsrsTest {

    private val calculator = FsrsCalculator()

    /**
     * Port of `test_memo_state` in py-fsrs `tests/test_basic.py`.
     */
    @Test
    fun memoState() {
        val ratings = listOf(
            FsrsRating.AGAIN,
            FsrsRating.GOOD,
            FsrsRating.GOOD,
            FsrsRating.GOOD,
            FsrsRating.GOOD,
            FsrsRating.GOOD,
        )
        val intervals = listOf(0.0, 0.0, 1.0, 3.0, 8.0, 21.0)

        var state: FsrsState? = null
        ratings.zip(intervals).forEach { (rating, interval) ->
            state = calculator.review(state, rating, interval)
        }

        assertEquals(53.62691, checkNotNull(state).stability, absoluteTolerance = 1e-4)
        assertEquals(6.3574867, checkNotNull(state).difficulty, absoluteTolerance = 1e-4)
    }

    /**
     * Port of `test_repeated_correct_reviews` in py-fsrs `tests/test_basic.py`.
     */
    @Test
    fun repeatedCorrectReviews() {
        var state = calculator.review(null, FsrsRating.EASY, elapsedDays = null)
        repeat(9) {
            state = calculator.review(state, FsrsRating.EASY, elapsedDays = 0.0)
        }

        assertEquals(1.0, state.difficulty)
    }

    /**
     * Port of `test_same_day_hard_review_does_not_decrease_stability` in py-fsrs `tests/test_basic.py`.
     */
    @Test
    fun sameDayHardReviewDoesNotDecreaseStability() {
        val first = calculator.review(null, FsrsRating.GOOD, elapsedDays = null)
        val second = calculator.review(first, FsrsRating.HARD, elapsedDays = 0.0)

        assertEquals(first.stability, second.stability)
    }

    /**
     * Port of `test_stability_lower_bound` in py-fsrs `tests/test_basic.py`.
     */
    @Test
    fun stabilityLowerBound() {
        var state: FsrsState? = null
        repeat(1000) { index ->
            val next = calculator.review(state, FsrsRating.AGAIN, elapsedDays = 1.0)
            assertTrue(next.stability >= FsrsParameters.STABILITY_MIN, "Stability after review ${index + 1} was ${next.stability}")
            state = next
        }
    }
}
