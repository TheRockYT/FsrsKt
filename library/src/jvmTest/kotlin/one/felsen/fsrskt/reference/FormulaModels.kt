package one.felsen.fsrskt.reference

import kotlinx.serialization.Serializable
import one.felsen.fsrskt.fsrs6.FsrsRating

@Serializable
data class FormulaFile<I>(
    val function: String,
    val description: String,
    val pyFsrs: String,
    val kotlin: String,
    val cases: List<FormulaCase<I>>,
)

@Serializable
data class FormulaCase<I>(val params: String, val input: I, val expected: Double)

@Serializable
data class RatingInput(val rating: FsrsRating)

@Serializable
data class RetrievabilityInput(val elapsedDays: Int, val stability: Double)

@Serializable
data class NextDifficultyInput(val difficulty: Double, val rating: FsrsRating)

@Serializable
data class NextStabilityShortTermInput(val stability: Double, val rating: FsrsRating)

@Serializable
data class StabilityOnRecallInput(
    val difficulty: Double,
    val stability: Double,
    val retrievability: Double,
    val rating: FsrsRating,
)

@Serializable
data class StabilityOnLapseInput(val difficulty: Double, val stability: Double, val retrievability: Double)
