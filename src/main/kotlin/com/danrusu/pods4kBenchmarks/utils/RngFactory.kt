package com.danrusu.pods4kBenchmarks.utils

import java.util.*
import kotlin.random.Random

/**
 * Creates deterministic, independent random number generator streams from a single master seed.
 *
 * This class is not thread-safe.
 */
class RngFactory(masterSeed: Long = 0L) {
    private val rngSource = SplittableRandom(masterSeed)

    fun createRng(): Random {
        return SplittableKotlinRandom(rngSource.split())
    }
}

private class SplittableKotlinRandom(
    private val random: SplittableRandom,
) : Random() {
    override fun nextBits(bitCount: Int): Int {
        require(bitCount in 0..Int.SIZE_BITS) { "bitCount ($bitCount) must be between 0 and ${Int.SIZE_BITS}" }

        if (bitCount == 0) return 0

        return random.nextInt().ushr(Int.SIZE_BITS - bitCount)
    }

    override fun nextInt(): Int = random.nextInt()

    override fun nextInt(from: Int, until: Int): Int = random.nextInt(from, until)

    override fun nextLong(): Long = random.nextLong()

    override fun nextLong(from: Long, until: Long): Long = random.nextLong(from, until)

    override fun nextBoolean(): Boolean = random.nextBoolean()

    override fun nextDouble(): Double = random.nextDouble()

    override fun nextDouble(from: Double, until: Double): Double = random.nextDouble(from, until)
}
