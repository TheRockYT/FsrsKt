package one.felsen.fsrskt.fsrs6

/**
 * Enum class representing the possible ratings for a flashcard review.
 *
 * @property value The integer value associated with the rating.
 */
enum class FsrsRating(val value: Int) {

    /**
     * Rating indicating that the user did not remember the flashcard.
     */
    AGAIN(1),

    /**
     * Rating indicating that the user found the flashcard difficult to remember.
     */
    HARD(2),

    /**
     * Rating indicating that the user remembered the flashcard well.
     */
    GOOD(3),

    /**
     * Rating indicating that the user found the flashcard very easy to remember.
     */
    EASY(4)
}
