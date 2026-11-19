package one.felsen.fsrskt.reference

import one.felsen.fsrskt.fsrs6.FsrsCalculator
import one.felsen.fsrskt.fsrs6.FsrsState
import one.felsen.fsrskt.helper.DateTimeHelper.elapsedDays
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals
import kotlin.time.Instant

/**
 * Compares [FsrsCalculator.review] against review sequences computed by py-fsrs.
 * Every review sequence becomes its own test in both factories.
 */
class ReviewSequenceReferenceTest {

    private val file = ReferenceFixtures.reviewSequenceFile

    /**
     * Starts every step from the py-fsrs state, so that a failure points to the step that causes a deviation.
     */
    @TestFactory
    fun everyStepFromReferenceState(): List<DynamicTest> = file.sequences.map { sequence ->
        dynamicTest("${sequence.id}, ${sequence.params}") {
            val calculator = calculator(sequence)
            sequence.steps.forEachIndexed { index, step ->
                val actual = calculator.review(
                    state = step.before?.let { FsrsState(it.difficulty, it.stability) },
                    rating = step.rating,
                    elapsedDays = step.elapsedDays?.toDouble(),
                )
                assertMatchesReference(step.after, actual) { describe(sequence, index, step) }
            }
        }
    }

    /**
     * Continues with the FsrsKt state after every step, so that deviations add up as they do in real use.
     */
    @TestFactory
    fun wholeSequenceWithOwnState(): List<DynamicTest> = file.sequences.map { sequence ->
        dynamicTest("${sequence.id}, ${sequence.params}") {
            val calculator = calculator(sequence)
            val actual = sequence.steps.fold(null as FsrsState?) { state, step ->
                calculator.review(state, step.rating, step.elapsedDays?.toDouble())
            }
            assertMatchesReference(sequence.steps.last().after, checkNotNull(actual)) {
                "${file.kotlin}(${sequence.id}, ${sequence.params}, end of ${sequence.steps.size} steps)"
            }
        }
    }

    /**
     * Compares the whole days between all consecutive reviews with the values computed by py-fsrs.
     */
    @Test
    fun elapsedDaysBetweenConsecutiveReviews() {
        file.sequences.forEach { sequence ->
            sequence.steps.zipWithNext().forEachIndexed { index, (previous, current) ->
                assertEquals(
                    current.elapsedDays?.toDouble(),
                    Instant.parse(previous.reviewAt).elapsedDays(Instant.parse(current.reviewAt)),
                    "${sequence.id}, step ${index + 2}/${sequence.steps.size}, " +
                        "${previous.reviewAt} to ${current.reviewAt}",
                )
            }
        }
    }

    private fun calculator(sequence: ReviewSequence) = FsrsCalculator(ReferenceFixtures.fsrs(sequence.params))

    private fun describe(sequence: ReviewSequence, index: Int, step: ReviewStep): String =
        "${file.kotlin}(${sequence.id}, ${sequence.params}, step ${index + 1}/${sequence.steps.size}, " +
            "reviewAt=${step.reviewAt}, before=${step.before}, ${step.rating}, " +
            "elapsedDays=${step.elapsedDays})"

    private fun assertMatchesReference(expected: MemoryState, actual: FsrsState, message: () -> String) {
        assertMatchesReference(expected.difficulty, actual.difficulty, { "${message()}, difficulty" })
        assertMatchesReference(expected.stability, actual.stability, { "${message()}, stability" })
    }
}
