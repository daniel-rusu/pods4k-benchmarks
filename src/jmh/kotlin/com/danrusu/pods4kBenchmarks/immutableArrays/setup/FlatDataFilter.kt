package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import kotlin.random.Random

/**
 * Filters data such that all values less than the median will be accepted and all other values will be rejected.
 *
 * This strategy is chosen to minimize filtering overhead so that the condition becomes as cheap as possible in order
 * to minimize benchmarking overhead.
 */
@Suppress("NOTHING_TO_INLINE") // to avoid any overhead on benchmarks
object FlatDataFilter {
    private const val MIN_STRING_LENGTH = 3
    private const val MAX_STRING_LENGTH = 10
    private val alphanumericCharacters = (('A'..'Z') + ('a'..'z') + ('0'..'9')).sorted()

    const val MEDIAN_STRING_LENGTH: Int = (MAX_STRING_LENGTH - MIN_STRING_LENGTH) / 2
    const val PASSING_BOOLEAN: Boolean = true
    const val MEDIAN_BYTE: Byte = 0
    val MEDIAN_CHAR: Char = alphanumericCharacters[alphanumericCharacters.size / 2]
    const val MEDIAN_SHORT: Short = 0
    const val MEDIAN_INT: Int = 0
    const val MEDIAN_FLOAT: Float = 0.5f
    const val MEDIAN_LONG: Long = 0L
    const val MEDIAN_DOUBLE: Double = 0.5

    inline fun shouldAccept(value: String): Boolean = value.length <= MEDIAN_STRING_LENGTH

    inline fun shouldAccept(value: Boolean): Boolean = value == PASSING_BOOLEAN

    inline fun shouldAccept(value: Byte): Boolean = value < MEDIAN_BYTE

    inline fun shouldAccept(value: Char): Boolean = value < MEDIAN_CHAR

    inline fun shouldAccept(value: Short): Boolean = value < MEDIAN_SHORT

    inline fun shouldAccept(value: Int): Boolean = value < MEDIAN_INT

    inline fun shouldAccept(value: Float): Boolean = value < MEDIAN_FLOAT

    inline fun shouldAccept(value: Long): Boolean = value < MEDIAN_LONG

    inline fun shouldAccept(value: Double): Boolean = value < MEDIAN_DOUBLE

    /**
     * Creates a [FlatDataProducer] that produces values which will be accepted with a ratio of [acceptRatio].
     *
     * acceptRatio = (# of accepted values) / (# of total values)
     */
    fun generateData(acceptRatio: Double): FlatDataProducer = object : FlatDataProducer {
        init {
            require(acceptRatio in 0.0..1.0)
        }

        override fun startNewCollection(size: Int) {}

        override fun nextReference(index: Int, random: Random): String {
            return generateAppropriateValue(random, ::shouldAccept) {
                val length = random.nextInt(from = MIN_STRING_LENGTH, until = MAX_STRING_LENGTH + 1)
                val randomChars = CharArray(length) { alphanumericCharacters.random(random) }
                String(randomChars)
            }
        }

        override fun nextBoolean(index: Int, random: Random): Boolean {
            return generateAppropriateValue(random, ::shouldAccept) { random.nextBoolean() }
        }

        override fun nextByte(index: Int, random: Random): Byte {
            return generateAppropriateValue(random, ::shouldAccept) { DataGenerator.randomByte(random) }
        }

        override fun nextChar(index: Int, random: Random): Char {
            return generateAppropriateValue(random, ::shouldAccept) { alphanumericCharacters.random(random) }
        }

        override fun nextShort(index: Int, random: Random): Short {
            return generateAppropriateValue(random, ::shouldAccept) { DataGenerator.randomShort(random) }
        }

        override fun nextInt(index: Int, random: Random): Int {
            return generateAppropriateValue(random, ::shouldAccept) { random.nextInt() }
        }

        override fun nextFloat(index: Int, random: Random): Float {
            return generateAppropriateValue(random, ::shouldAccept) { random.nextFloat() }
        }

        override fun nextLong(index: Int, random: Random): Long {
            return generateAppropriateValue(random, ::shouldAccept) { random.nextLong() }
        }

        override fun nextDouble(index: Int, random: Random): Double {
            return generateAppropriateValue(random, ::shouldAccept) { random.nextDouble() }
        }

        /**
         * Creates values that will be accepted [acceptRatio] proportion of the time.
         *
         * Data Generation Strategy:
         * 1. Randomly determine whether the new value should be accepted
         * 2. Repeatedly generate random values until a value is found that will be accepted / rejected based on the
         * previous step.
         *
         * Since the acceptance is based on the median value, with half of the potential values passing, the second step
         * is expected to terminate in a very short amount of iterations since each iteration has a 50% chance of
         * finding a valid value.  The probability of not finding a valid value after N attempts is equal to (1/2)^N
         * which drops off exponentially.  Note that this is true regardless of the acceptance ratio as a low acceptance
         * ratio just means that most of the generated values will be above the median so they'll be rejected.  So the
         * acceptance ratio doesn't represent the rarity of the generated values but rather how often values will be
         * below the median.
         */
        private inline fun <T> generateAppropriateValue(
            random: Random,
            accept: (T) -> Boolean,
            generate: () -> T
        ): T {
            val shouldBeAccepted = random.nextDouble() < acceptRatio

            var value = generate()
            while (accept(value) != shouldBeAccepted) {
                value = generate()
            }
            return value
        }
    }
}
