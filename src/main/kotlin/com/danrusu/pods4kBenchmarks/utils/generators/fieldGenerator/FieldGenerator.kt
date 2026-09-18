package com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator

/** Generates primitive field values. */
interface FieldGenerator {
    /** Returns the next [Boolean] field value. */
    fun nextBoolean(): Boolean

    /** Returns the next [Byte] field value. */
    fun nextByte(): Byte

    /** Returns the next [Char] field value. */
    fun nextChar(): Char

    /** Returns the next [Short] field value. */
    fun nextShort(): Short

    /** Returns the next [Int] field value. */
    fun nextInt(): Int

    /** Returns the next [Float] field value. */
    fun nextFloat(): Float

    /** Returns the next [Long] field value. */
    fun nextLong(): Long

    /** Returns the next [Double] field value. */
    fun nextDouble(): Double
}
