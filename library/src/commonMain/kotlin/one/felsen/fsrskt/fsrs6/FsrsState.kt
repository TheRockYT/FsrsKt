package one.felsen.fsrskt.fsrs6

/**
 * Data class representing the state of a flashcard in the FSRS6 algorithm.
 *
 * @property difficulty The difficulty level of the flashcard.
 * @property stability The stability level of the flashcard.
 */
data class FsrsState(val difficulty: Double, val stability: Double)
