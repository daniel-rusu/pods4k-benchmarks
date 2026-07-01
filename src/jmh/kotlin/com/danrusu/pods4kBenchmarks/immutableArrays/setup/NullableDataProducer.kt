package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import kotlin.random.Random

class NullableDataProducer(
    val nullRatio: Double,
    private val nullnessRandom: Random,
    private val flatDataProducer: FlatDataProducer,
) {
    /** Returns the next reference element */
    fun nextReference(): String? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextReference()
    }

    /** Returns the next Boolean? element */
    fun nextBoolean(): Boolean? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextBoolean()
    }

    /** Returns the next Byte? element */
    fun nextByte(): Byte? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextByte()
    }

    /** Returns the next Char? element */
    fun nextChar(): Char? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextChar()
    }

    /** Returns the next Short? element */
    fun nextShort(): Short? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextShort()
    }

    /** Returns the next Int? element */
    fun nextInt(): Int? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextInt()
    }

    /** Returns the next Float? element */
    fun nextFloat(): Float? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextFloat()
    }

    /** Returns the next Long? element */
    fun nextLong(): Long? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextLong()
    }

    /** Returns the next Double? element */
    fun nextDouble(): Double? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextDouble()
    }

    /**
     * Determines whether the next value should be null.
     *
     * IMPORTANT:
     * This should use a standalone random number generator that's not shared with the one that generates data so that
     * the distribution of null values is identical across different data types so that they are directly comparable.
     */
    private fun shouldBeNull(random: Random): Boolean = random.nextDouble() < nullRatio
}

class NullableDataProducerFactory(
    private val nullRatio: Double,
    private val flatDataProducerFactory: FlatDataProducerFactory,
) {
    init {
        require(nullRatio in 0.0..1.0)
    }

    fun create(seed: Long): NullableDataProducer {
        val seedRandom = Random(seed)
        return NullableDataProducer(
            nullRatio = nullRatio,
            nullnessRandom = Random(seedRandom.nextLong()),
            flatDataProducer = flatDataProducerFactory.create(seedRandom.nextLong()),
        )
    }
}
