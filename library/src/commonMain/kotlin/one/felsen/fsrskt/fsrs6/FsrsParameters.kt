package one.felsen.fsrskt.fsrs6

/**
 * Object containing default parameters for the FSRS6 algorithm.
 */
object FsrsParameters {

    /**
     * Lower bound for the stability of an item. (in days)
     */
    const val STABILITY_MIN = 0.001

    /**
     * Default parameters for the FSRS6 algorithm.
     */
    val defaultParameters = listOf(
        0.212,
        1.2931,
        2.3065,
        8.2956,
        6.4133,
        0.8334,
        3.0194,
        0.001,
        1.8722,
        0.1666,
        0.796,
        1.4835,
        0.0614,
        0.2629,
        1.6483,
        0.6014,
        1.8729,
        0.5425,
        0.0912,
        0.0658,
        0.1542
    )

    /**
     * Lower bounds of the parameters for the FSRS6 algorithm.
     */
    val lowerBoundsParameters = listOf(
        STABILITY_MIN,
        STABILITY_MIN,
        STABILITY_MIN,
        STABILITY_MIN,
        1.0,
        0.001,
        0.001,
        0.001,
        0.0,
        0.0,
        0.001,
        0.001,
        0.001,
        0.001,
        0.0,
        0.0,
        1.0,
        0.0,
        0.0,
        0.0,
        0.1
    )

    /**
     * Upper bounds of the parameters for the FSRS6 algorithm.
     */
    val upperBoundsParameters = listOf(
        100.0,
        100.0,
        100.0,
        100.0,
        10.0,
        4.0,
        4.0,
        0.75,
        4.5,
        0.8,
        3.5,
        5.0,
        0.25,
        0.9,
        4.0,
        1.0,
        6.0,
        2.0,
        2.0,
        0.8,
        0.8
    )
}
