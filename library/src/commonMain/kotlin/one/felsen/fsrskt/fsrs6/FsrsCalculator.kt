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
     * @param elapsedDays The number of days since the last review.
     * @param sameDay A boolean indicating if the review is happening on the same day as the last review.
     * @return The new state of the flashcard after applying the FSRS algorithm.
     */
    fun review(state: FsrsState?, rating: FsrsRating, elapsedDays: Double, sameDay: Boolean): FsrsState {
        // The state is null if the card is new, so we initialize it with default values based on the rating.
        if (state == null) {
            return FsrsState(fsrs.initialDifficulty(rating), fsrs.initialStability(rating))
        }

        val newDifficulty = fsrs.nextDifficulty(state.difficulty, rating)

        val newStability = when {
            sameDay -> fsrs.nextStabilityShortTerm(state.stability, rating)
            rating == FsrsRating.AGAIN -> fsrs.stabilityOnLapse(
                state.difficulty, state.stability, fsrs.retrievability(elapsedDays, state.stability)
            )
            else -> fsrs.stabilityOnRecall(
                state.difficulty, state.stability, fsrs.retrievability(elapsedDays, state.stability), rating
            )
        }

        return FsrsState(newDifficulty, newStability)
    }
}
