# FsrsKt: A Kotlin library for FSRS

This project is a Kotlin Multiplatform library that implements the FSRS-6 (Free Spaced Repetition Scheduler) algorithm.

## Introduction to FSRS

You may have heard of spaced repetition systems (SRS) like Anki, which are designed to help you remember information
more effectively by scheduling reviews at optimal intervals.

This library implements the FSRS-6 algorithm, which is a free and open-source spaced repetition scheduling algorithm. It
is designed to be simple, efficient, and effective for learning and retaining information over time.

In case you are not familiar with FSRS, you can read
the [ABC of FSRS](https://github.com/open-spaced-repetition/awesome-fsrs/wiki/ABC-of-FSRS)

This library aims to provide a Kotlin implementation of the FSRS-6 algorithm that can be used in various applications,
including Android apps, desktop applications, and server-side applications.

## Getting Started

### Dependency

To get started with FsrsKt, you can add the library to your Kotlin Multiplatform project.

Currently, the library is not published to a public Maven repository, so you will need to clone the repository and
include it as a module in your project, or add it as a git submodule.

### Basic Implementation

The following is a basic implementation of the FSRS algorithm using the FsrsKt library. This example demonstrates how to
create a simple flashcard application that uses FSRS to schedule reviews.

1. Crate a data structure to hold the card information. In this case, we will create a `Flashcard` data class that
   contains the front and back of the card, as well as the FSRS state and due date. You can also add any other
   information you want to store for each card, such as the last review date or the number of times the card has been
   reviewed.

    ```kotlin
    import one.felsen.fsrskt.fsrs6.FsrsState
    import kotlin.time.Instant
    
    data class Flashcard(
        // Content of the card (may be changed to something else; doesnt matter for the FSRS algorithm)
        val front: String,
        val back: String,
        
        // The FSRS state of the card, which is used to track the scheduling information for spaced repetition.
        var fsrsState: FsrsState? = null,
        
        // The due date for the next review of the card. This is calculated based on the FSRS state and the user's performance.
        var dueDate: Instant? = null,
        
        // The day of the last review
        var lastReview: Instant? = null
    )
    ```

2. Create the flashcard and initialize the FSRS state.

    ```kotlin
    // Create a deck of flashcards
    private val deck = mutableListOf<Flashcard>()
    deck.add(Flashcard("1", "What is the capital of South-Korea?", "Seoul"))
    // ... add more cards
    ```

3. Get the next card for review.

    ```kotlin
    // Get a list of all due cards
    // A card can be considered due if it has been reviewed before (fsrsState != null) and the due date is less than or equal to now.
    val dueCard = deck
        .filter { it.fsrsState != null && it.dueDate != null && it.dueDate!! <= now }
        .minByOrNull { it.dueDate!! }

    // Return the due card if it exists, otherwise return a new card that has not been reviewed yet.
    if (dueCard != null) return dueCard

    // If there are no due cards, return a new card that has not been reviewed yet.
    return deck.firstOrNull { it.fsrsState == null }
    ```

4. Implement the review process and update the FSRS state based on the user's performance.

    ```kotlin
    import one.felsen.fsrskt.fsrs6.FsrsCalculator
    import one.felsen.fsrskt.fsrs6.FsrsRating
    import one.felsen.fsrskt.helper.DateTimeHelper.elapsedDays
    import one.felsen.fsrskt.helper.DateTimeHelper.isSameDay
   
    // Create an instance of the FsrsCalculator to handle the review process.
    val calc = FsrsCalculator()
   
    val card: Flashcard = ... // Get the card to review (see step 3)
    val rating: Rating = ... // Get the user's rating for the card (e.g., 0-5)
   
    // Use the calculator to calculate the new FSRS state based on the user's rating and the elapsed time since the last review.
    val newState = calc.review(
        // Provide the current FSRS state of the card
        state = card.fsrsState,
        // Provide the user's rating for the card
        rating = rating,
        // Provide the elapsed time since the last review of the card. 0.0 is used if the card has not been reviewed before.
        elapsedDays = card.lastReview?.elapsedDays(now) ?: 0.0,
        // Provide a boolean indicating whether the card was reviewed on the same day as the last review.
        // `isSameDay` concideres all time until 4 AM of the next day as the same day.
        sameDay = card.lastReview?.isSameDay(now) ?: false     
    )

    // Update the card's FSRS state and last review date with the new values calculated by the FsrsCalculator.
    card.fsrsState = newState
    // Update the last review date to the current time.
    card.lastReview = now

    // Precompute the due date for the next review based on the new FSRS state and the current time.
    card.dueDate = now.plus(newState.stability.days)
    ```

## Thanks

This project is based on the work of Jarrett Ye and other contributors to the FSRS algorithm. You can find more
information about the FSRS algorithm on the
wiki: [The Algorithm](https://github.com/open-spaced-repetition/awesome-fsrs/wiki/The-Algorithm#default-parameters).
