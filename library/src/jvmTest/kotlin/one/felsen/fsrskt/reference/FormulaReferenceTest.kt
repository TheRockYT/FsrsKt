package one.felsen.fsrskt.reference

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

/**
 * Compares each formula of [one.felsen.fsrskt.fsrs6.Fsrs] against values computed by py-fsrs.
 * Every fixture case becomes its own test, named after its parameter set and input.
 */
class FormulaReferenceTest {

    @TestFactory
    fun initialStability(): List<DynamicTest> {
        val file = ReferenceFixtures.load<FormulaFile<RatingInput>>("formulas/initial-stability.json")

        return file.cases.map { case ->
            val name = "${case.params}, ${case.input.rating}"
            dynamicTest(name) {
                val actual = ReferenceFixtures.fsrs(case.params).initialStability(case.input.rating)
                assertMatchesReference(case.expected, actual, { "${file.kotlin}($name)" })
            }
        }
    }

    @TestFactory
    fun initialDifficulty(): List<DynamicTest> {
        val file = ReferenceFixtures.load<FormulaFile<RatingInput>>("formulas/initial-difficulty.json")

        return file.cases.map { case ->
            val name = "${case.params}, ${case.input.rating}"
            dynamicTest(name) {
                val actual = ReferenceFixtures.fsrs(case.params).initialDifficulty(case.input.rating)
                assertMatchesReference(case.expected, actual, { "${file.kotlin}($name)" })
            }
        }
    }

    @TestFactory
    fun retrievability(): List<DynamicTest> {
        val file = ReferenceFixtures.load<FormulaFile<RetrievabilityInput>>("formulas/retrievability.json")

        return file.cases.map { case ->
            val name = "${case.params}, t=${case.input.elapsedDays}, S=${case.input.stability}"
            dynamicTest(name) {
                val actual = ReferenceFixtures.fsrs(case.params)
                    .retrievability(case.input.elapsedDays.toDouble(), case.input.stability)
                assertMatchesReference(case.expected, actual, { "${file.kotlin}($name)" })
            }
        }
    }

    @TestFactory
    fun nextDifficulty(): List<DynamicTest> {
        val file = ReferenceFixtures.load<FormulaFile<NextDifficultyInput>>("formulas/next-difficulty.json")

        return file.cases.map { case ->
            val name = "${case.params}, D=${case.input.difficulty}, ${case.input.rating}"
            dynamicTest(name) {
                val actual = ReferenceFixtures.fsrs(case.params)
                    .nextDifficulty(case.input.difficulty, case.input.rating)
                assertMatchesReference(case.expected, actual, { "${file.kotlin}($name)" })
            }
        }
    }

    @TestFactory
    fun nextStabilityShortTerm(): List<DynamicTest> {
        val file = ReferenceFixtures.load<FormulaFile<NextStabilityShortTermInput>>("formulas/next-stability-short-term.json")

        return file.cases.map { case ->
            val name = "${case.params}, S=${case.input.stability}, ${case.input.rating}"
            dynamicTest(name) {
                val actual = ReferenceFixtures.fsrs(case.params)
                    .nextStabilityShortTerm(case.input.stability, case.input.rating)
                assertMatchesReference(case.expected, actual, { "${file.kotlin}($name)" })
            }
        }
    }

    @TestFactory
    fun stabilityOnRecall(): List<DynamicTest> {
        val file = ReferenceFixtures.load<FormulaFile<StabilityOnRecallInput>>("formulas/stability-on-recall.json")

        return file.cases.map { case ->
            val name = "${case.params}, D=${case.input.difficulty}, S=${case.input.stability}, " +
                "R=${case.input.retrievability}, ${case.input.rating}"
            dynamicTest(name) {
                val actual = ReferenceFixtures.fsrs(case.params).stabilityOnRecall(
                    case.input.difficulty,
                    case.input.stability,
                    case.input.retrievability,
                    case.input.rating,
                )
                assertMatchesReference(case.expected, actual, { "${file.kotlin}($name)" })
            }
        }
    }

    @TestFactory
    fun stabilityOnLapse(): List<DynamicTest> {
        val file = ReferenceFixtures.load<FormulaFile<StabilityOnLapseInput>>("formulas/stability-on-lapse.json")

        return file.cases.map { case ->
            val name = "${case.params}, D=${case.input.difficulty}, S=${case.input.stability}, " +
                "R=${case.input.retrievability}"
            dynamicTest(name) {
                val actual = ReferenceFixtures.fsrs(case.params).stabilityOnLapse(
                    case.input.difficulty,
                    case.input.stability,
                    case.input.retrievability,
                )
                assertMatchesReference(case.expected, actual, { "${file.kotlin}($name)" })
            }
        }
    }
}
