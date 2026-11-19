package one.felsen.fsrskt.fsrs6

/**
 * FsrsCalculator is responsible for calculating the next state of a flashcard based on the FSRS algorithm.
 *
 * @property fsrs An instance of the FSRS class that contains the FSRS algorithm logic.
 */
class FsrsCalculator(val fsrs: Fsrs = Fsrs()) {

    /**
     * Reviews a flashcard and calculates its new state based on the provided parameters.
     *
     * @param state The current state of the flashcard, or null if it's a new card.
     * @param rating The user's rating of their recall performance.
     * @param elapsedDays The number of days since the last review, or null if the time of the last review is unknown.
     * Less than one day uses the short-term stability. Otherwise, and if it is unknown, the long-term stability is used,
     * with a retrievability of 0 in the unknown case. Ignored for a new card.
     * Fractional days are used as they are; py-fsrs rounds down to whole days.
     * @return The new state of the flashcard after applying the FSRS algorithm.
     */
    fun review(state: FsrsState?, rating: FsrsRating, elapsedDays: Double?): FsrsState {
        // The state is null if the card is new, so we initialize it with default values based on the rating.
        if (state == null) {
            return FsrsState(fsrs.initialDifficulty(rating), fsrs.initialStability(rating))
        }

        val newDifficulty = fsrs.nextDifficulty(state.difficulty, rating)

        val newStability = if (elapsedDays != null && elapsedDays < 1.0) {
            fsrs.nextStabilityShortTerm(state.stability, rating)
        } else {
            fsrs.nextStability(
                state.difficulty, state.stability, fsrs.retrievability(elapsedDays, state.stability), rating
            )
        }

        return FsrsState(newDifficulty, newStability)
    }
}
