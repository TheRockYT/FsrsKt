package one.felsen.fsrskt.reference

import kotlinx.serialization.Serializable
import one.felsen.fsrskt.fsrs6.FsrsRating

@Serializable
data class ReviewSequenceFile(
    val description: String,
    val pyFsrs: String,
    val kotlin: String,
    val sequences: List<ReviewSequence>,
)

@Serializable
data class ReviewSequence(val id: String, val params: String, val steps: List<ReviewStep>)

@Serializable
data class ReviewStep(
    val reviewAt: String,
    val rating: FsrsRating,
    val elapsedDays: Int?,
    val before: MemoryState?,
    val after: MemoryState,
)

@Serializable
data class MemoryState(val difficulty: Double, val stability: Double)
