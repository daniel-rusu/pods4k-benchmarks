package com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator

/** Generates primitive values. */
interface PrimitiveGenerator {
    /** Returns the next [Boolean] value. */
    fun nextBoolean(): Boolean

    /** Returns the next [Byte] value. */
    fun nextByte(): Byte

    /** Returns the next [Char] value. */
    fun nextChar(): Char

    /** Returns the next [Short] value. */
    fun nextShort(): Short

    /** Returns the next [Int] value. */
    fun nextInt(): Int

    /** Returns the next [Float] value. */
    fun nextFloat(): Float

    /** Returns the next [Long] value. */
    fun nextLong(): Long

    /** Returns the next [Double] value. */
    fun nextDouble(): Double
}
