package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import kotlin.random.Random

interface FlatDataProducer {
    /** Returns the next reference element */
    fun nextReference(): String

    /** Returns the next [Boolean] element */
    fun nextBoolean(): Boolean

    /** Returns the next [Byte] element */
    fun nextByte(): Byte

    /** Returns the next [Char] element */
    fun nextChar(): Char

    /** Returns the next [Short] element */
    fun nextShort(): Short

    /** Returns the next [Int] element */
    fun nextInt(): Int

    /** Returns the next [Float] element */
    fun nextFloat(): Float

    /** Returns the next [Long] element */
    fun nextLong(): Long

    /** Returns the next [Double] element */
    fun nextDouble(): Double
}

interface FlatDataProducerFactory {
    fun create(seed: Long): FlatDataProducer

    object RandomDataProducerFactory : FlatDataProducerFactory {
        override fun create(seed: Long): FlatDataProducer = RandomFlatDataProducer(Random(seed))
    }
}

private class RandomFlatDataProducer(
    private val random: Random,
) : FlatDataProducer {
    override fun nextReference(): String = DataGenerator.randomString(random = random)

    override fun nextBoolean(): Boolean = random.nextBoolean()

    override fun nextByte(): Byte = DataGenerator.randomByte(random)

    override fun nextChar(): Char = DataGenerator.randomChar(random)

    override fun nextShort(): Short = DataGenerator.randomShort(random)

    override fun nextInt(): Int = random.nextInt()

    override fun nextFloat(): Float = random.nextFloat()

    override fun nextLong(): Long = random.nextLong()

    override fun nextDouble(): Double = random.nextDouble()
}
