package com.danrusu.pods4kBenchmarks.utils.predicates

import com.danrusu.pods4kBenchmarks.utils.generators.AlphanumericCharacters

/** Cheap midpoint predicates to minimize the overhead on benchmarks and focus on the performance of the operation. */
internal object MedianValuePredicate : PrimitivePredicate {
    private const val MIN_STRING_LENGTH = 3
    private const val MAX_STRING_LENGTH = 10

    const val MEDIAN_STRING_LENGTH: Int = (MIN_STRING_LENGTH + MAX_STRING_LENGTH) / 2
    val MEDIAN_CHAR: Char = AlphanumericCharacters.naturalOrderMedian

    fun matches(value: String): Boolean {
        // including the median string length in order to balance out the number of possibilities on each side of the
        // predicate as the computation of the median is truncated
        return value.length <= MEDIAN_STRING_LENGTH
    }

    override fun matches(value: Boolean): Boolean = value

    override fun matches(value: Byte): Boolean = value < 0

    override fun matches(value: Char): Boolean = value < MEDIAN_CHAR

    override fun matches(value: Short): Boolean = value < 0

    override fun matches(value: Int): Boolean = value < 0

    override fun matches(value: Float): Boolean = value < 0.5f

    override fun matches(value: Long): Boolean = value < 0L

    override fun matches(value: Double): Boolean = value < 0.5
}
