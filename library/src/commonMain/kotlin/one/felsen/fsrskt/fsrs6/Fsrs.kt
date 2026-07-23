package one.felsen.fsrskt.fsrs6

import kotlin.math.exp
import kotlin.math.min
import kotlin.math.pow

/**
 * FSRS6 implementation based on https://github.com/open-spaced-repetition/awesome-fsrs/wiki/The-Algorithm
 *
 * @param w The parameters for the FSRS algorithm.
 */
class Fsrs(val w: List<Double> = FsrsParameters.defaultParameters) {

    init {
        /**
         * Ensures that the parameters list contains exactly 21 values. Required by the FSRS6 algorithm for proper calculations.
         * See: https://github.com/open-spaced-repetition/awesome-fsrs/wiki/The-Algorithm#default-parameters
         */
        require(w.size == 21) { "Parameters must contain exactly 21 values." }
    }

    /**
     * The factor used in the retrievability calculation.
     */
    private val factor = 0.9.pow(-1.0 / w[20]) - 1.0

    /**
     * Calculates the initial stability for a given rating.
     *
     * @param rating The rating for which to calculate the initial stability.
     * @return The initial stability value.
     */
    fun initialStability(rating: FsrsRating): Double = w[rating.value - 1]

    /**
     * Calculates the initial difficulty for a given rating.
     *
     * @param rating The rating for which to calculate the initial difficulty.
     * @return The initial difficulty value, clamped between 1.0 and 10.0.
     */
    fun initialDifficulty(rating: FsrsRating): Double = (w[4] - exp(w[5] * (rating.value - 1)) + 1.0).coerceIn(1.0, 10.0)


    /**
     * Calculates the retrievability based on elapsed time and stability.
     *
     * @param t The elapsed time since the last review. (in days)
     * @param s The current stability of the item. (in days)
     * @return The retrievability value, which indicates how likely the item is to be recalled.
     */
    fun retrievability(t: Double, s: Double): Double {
        val r = (1.0 + factor * (t / s)).pow(-w[20])
        return r
    }

    /**
     * Calculates the next stability based on the current stability, difficulty, and rating.
     *
     * @param difficulty The current difficulty of the item. (1.0 to 10.0)
     * @param rating The rating given by the user after reviewing the item.
     * @return The next stability value, which indicates how long until the item should be reviewed again.
     */
    fun nextDifficulty(difficulty: Double, rating: FsrsRating): Double {
        val deltaD = -w[6] * (rating.value - 3.0)
        val dPrime = difficulty + deltaD * ((10.0 - difficulty) / 9.0)
        val dDoublePrime = w[7] * initialDifficulty(FsrsRating.EASY) + (1.0 - w[7]) * dPrime
        return dDoublePrime.coerceIn(1.0, 10.0)
    }

    /**
     * Calculates the next stability for short-term reviews based on the current stability and rating.
     *
     * @param stability The current stability of the item. (in days)
     * @param rating The rating given by the user after reviewing the item.
     * @return The next stability value, which indicates how long until the item should be reviewed again.
     */
    fun nextStabilityShortTerm(stability: Double, rating: FsrsRating): Double {
        val sPrime = stability * exp(w[17] * (rating.value - 3.0 + w[18])) * stability.pow(-w[19])
        return if (rating.value >= 3) maxOf(sPrime, stability) else sPrime
    }

    /**
     * Calculates the next stability for long-term reviews based on the current difficulty, stability, retrievability, and rating.
     * In case the rating is AGAIN, the stabilityOnLapse function should be used instead.
     *
     * @param difficulty The current difficulty of the item. (1.0 to 10.0)
     * @param stability The current stability of the item. (in days)
     * @param retrievability The current retrievability of the item. (0.0 to 1.0)
     * @param rating The rating given by the user after reviewing the item.
     * @return The next stability value, which indicates how long until the item should be reviewed again.
     */
    fun stabilityOnRecall(difficulty: Double, stability: Double, retrievability: Double, rating: FsrsRating): Double {
        val w15 = if (rating == FsrsRating.HARD) w[15] else 1.0
        val w16 = if (rating == FsrsRating.EASY) w[16] else 1.0

        val sPrime =
            stability * (exp(w[8]) * (11.0 - difficulty) * stability.pow(-w[9]) * (exp(w[10] * (1.0 - retrievability)) - 1.0) * w15 * w16 + 1.0)
        return sPrime
    }

    /**
     * Calculates the next stability for items that have lapsed (i.e., rated as AGAIN) based on the current difficulty, stability, and retrievability.
     *
     * @param difficulty The current difficulty of the item. (1.0 to 10.0)
     * @param stability The current stability of the item. (in days)
     * @param retrievability The current retrievability of the item. (0.0 to 1.0)
     * @return The next stability value, which indicates how long until the item should be reviewed again.
     */
    fun stabilityOnLapse(difficulty: Double, stability: Double, retrievability: Double): Double {
        val sPrime =
            (w[11] * difficulty.pow(-w[12]) * ((stability + 1.0).pow(w[13]) - 1.0) * exp(w[14] * (1.0 - retrievability)))

        return min(sPrime, stability)
    }
}
