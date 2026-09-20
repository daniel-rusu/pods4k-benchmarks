package com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.AlphanumericCharacters
import kotlin.random.Random

/** Generates random primitive values from a [Random] stream. */
class RandomPrimitiveGenerator(private val random: Random) : PrimitiveGenerator {
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
