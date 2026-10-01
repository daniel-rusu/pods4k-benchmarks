package com.danrusu.pods4kBenchmarks.utils.predicates

/** Predicate that controls whether primitive values should be accepted. */
interface PrimitivePredicate {
    fun matches(value: Boolean): Boolean

    fun matches(value: Byte): Boolean

    fun matches(value: Char): Boolean

    fun matches(value: Short): Boolean

    fun matches(value: Int): Boolean

    fun matches(value: Float): Boolean

    fun matches(value: Long): Boolean

    fun matches(value: Double): Boolean
}
