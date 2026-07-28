package com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.AlphanumericCharacters
import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicy
import kotlin.random.Random

/**
 * Generates random values for simple benchmark fields from a [Random] stream.
 */
class RandomFieldGenerator(
    private val random: Random,
    nullabilityPolicy: NullabilityPolicy? = null,
) : FieldGenerator(nullabilityPolicy) {
    override fun nextBoolean(): Boolean = random.nextBoolean()

    override fun nextByte(): Byte {
        return random.nextInt(from = Byte.MIN_VALUE.toInt(), until = Byte.MAX_VALUE.toInt() + 1).toByte()
    }

    override fun nextChar(): Char = AlphanumericCharacters.random(random)

    override fun nextShort(): Short {
        return random.nextInt(from = Short.MIN_VALUE.toInt(), until = Short.MAX_VALUE.toInt() + 1).toShort()
    }

    override fun nextInt(): Int = random.nextInt()

    override fun nextFloat(): Float = random.nextFloat()

    override fun nextLong(): Long = random.nextLong()

    override fun nextDouble(): Double = random.nextDouble()
}
