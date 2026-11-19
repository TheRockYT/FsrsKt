package one.felsen.fsrskt.fsrs6

import one.felsen.fsrskt.reference.ReferenceFixtures
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import kotlin.math.nextDown
import kotlin.math.nextUp
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Tests the parameter validation of [Fsrs] against the bounds of py-fsrs.
 */
class FsrsParameterValidationTest {

    private val defaults = FsrsParameters.defaultParameters

    @Test
    fun defaultParametersAreAccepted() {
        assertEquals(defaults, Fsrs(defaults).w)
    }

    @Test
    fun wrongNumberOfParametersIsRejected() {
        listOf(0, 20, 24).forEach { size ->
            assertFailsWith<IllegalArgumentException>("$size parameters") {
                Fsrs(List(size) { 1.0 })
            }
        }
    }

    @Test
    fun allParametersOutOfBoundsAreReported() {
        val parameters = defaults.withValue(0, 0.0).withValue(3, 101.0)

        val message = assertFailsWith<IllegalArgumentException> { Fsrs(parameters) }.message.orEmpty()

        assertContains(message, "parameters[0]")
        assertContains(message, "parameters[3]")
    }

    @Test
    fun laterChangesToTheGivenListHaveNoEffect() {
        val parameters = defaults.toMutableList()
        val fsrs = Fsrs(parameters)

        parameters[20] = 100.0

        assertEquals(defaults, fsrs.w)
    }

    @Test
    fun nanIsRejected() {
        assertFailsWith<IllegalArgumentException> { Fsrs(defaults.withValue(5, Double.NaN)) }
    }

    @TestFactory
    fun boundsOfEveryParameter(): List<DynamicTest> {
        val file = ReferenceFixtures.parameterSetsFile

        return defaults.indices.map { index ->
            dynamicTest("w[$index]") {
                val lower = file.lowerBounds[index]
                val upper = file.upperBounds[index]

                assertEquals(lower, Fsrs(defaults.withValue(index, lower)).w[index])
                assertEquals(upper, Fsrs(defaults.withValue(index, upper)).w[index])
                assertFailsWith<IllegalArgumentException>("below $lower") {
                    Fsrs(defaults.withValue(index, lower.nextDown()))
                }
                assertFailsWith<IllegalArgumentException>("above $upper") {
                    Fsrs(defaults.withValue(index, upper.nextUp()))
                }
            }
        }
    }

    private fun List<Double>.withValue(index: Int, value: Double): List<Double> =
        toMutableList().also { it[index] = value }
}
