package com.danrusu.pods4kBenchmarks.utils

import kotlin.math.roundToInt

private const val UNITS_PER_PERCENT = 100
private const val MAX_PERCENT_UNITS = 100 * UNITS_PER_PERCENT // Max percentage = 100%

/** A percentage from 0% through 100%, exactly represented to two decimal places. */
@JvmInline
value class Percent private constructor(private val units: Int) : Comparable<Percent> {
    init {
        // Although values out of this range are valid mathematically, this repo assumes percent is between 0 & 100%
        require(units in 0..MAX_PERCENT_UNITS) { "Percentage must be between 0% and 100%" }
    }

    operator fun plus(other: Percent): Percent {
        val result = units + other.units
        require(result <= MAX_PERCENT_UNITS) { "Percentage sum (${format(result)}) must not exceed 100%" }

        return Percent(result)
    }

    override fun compareTo(other: Percent): Int = units.compareTo(other.units)

    override fun toString(): String = format(units)

    /** Multiplies a quantity by this percentage, with ordinary Double rounding. */
    fun scale(value: Double): Double = value * (units.toDouble() / MAX_PERCENT_UNITS)

    companion object {
        /** Constructs a percentage from an integer in 0..100. */
        fun of(value: Int): Percent {
            require(value in 0..100) { "Percentage ($value) must be between 0 and 100" }
            return Percent(value * UNITS_PER_PERCENT)
        }

        /** Requires an exact round trip to a percentage with at most two decimal places, without tolerance. */
        fun of(value: Double): Percent {
            require(value in 0.0..100.0) { "Percentage ($value) must be between 0 and 100" }

            val candidate = (value * UNITS_PER_PERCENT).roundToInt()
            require(candidate / UNITS_PER_PERCENT.toDouble() == value) {
                "Percentage ($value) must be exactly representable with at most two decimal places"
            }
            return Percent(candidate)
        }

        private fun format(units: Int): String {
            val whole = units / UNITS_PER_PERCENT
            val remainder = units % UNITS_PER_PERCENT
            if (remainder == 0) return "$whole%"

            val formattedRemainder = remainder.toString().padStart(2, '0').trimEnd('0')
            return "$whole.$formattedRemainder%"
        }
    }
}

fun Int.percent(): Percent = Percent.of(this)

fun Double.percent(): Percent = Percent.of(this)
