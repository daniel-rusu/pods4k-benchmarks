package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import kotlin.random.Random

class NullableDataProducer(val nullRatio: Double, val flatDataProducer: FlatDataProducer) {
    /** Indicates that a new collection is about to be created that will store [size] number of elements */
    fun startNewCollection(size: Int) {
        flatDataProducer.startNewCollection(size)
    }

    /** Returns the next reference element that will be the [index]th element into the collection being created */
    fun nextReference(index: Int, nullnessRandom: Random, dataRandom: Random): String? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextReference(index, dataRandom)
    }

    /** Returns the next [Boolean] element that will be the [index]th element into the collection being created */
    fun nextBoolean(index: Int, nullnessRandom: Random, dataRandom: Random): Boolean? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextBoolean(index, dataRandom)
    }

    /** Returns the next [Byte] element that will be the [index]th element into the collection being created */
    fun nextByte(index: Int, nullnessRandom: Random, dataRandom: Random): Byte? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextByte(index, dataRandom)
    }

    /** Returns the next [Char] element that will be the [index]th element into the collection being created */
    fun nextChar(index: Int, nullnessRandom: Random, dataRandom: Random): Char? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextChar(index, dataRandom)
    }

    /** Returns the next [Short] element that will be the [index]th element into the collection being created */
    fun nextShort(index: Int, nullnessRandom: Random, dataRandom: Random): Short? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextShort(index, dataRandom)
    }

    /** Returns the next [Int] element that will be the [index]th element into the collection being created */
    fun nextInt(index: Int, nullnessRandom: Random, dataRandom: Random): Int? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextInt(index, dataRandom)
    }

    /** Returns the next [Float] element that will be the [index]th element into the collection being created */
    fun nextFloat(index: Int, nullnessRandom: Random, dataRandom: Random): Float? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextFloat(index, dataRandom)
    }

    /** Returns the next [Long] element that will be the [index]th element into the collection being created */
    fun nextLong(index: Int, nullnessRandom: Random, dataRandom: Random): Long? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextLong(index, dataRandom)
    }

    /** Returns the next [Double] element that will be the [index]th element into the collection being created */
    fun nextDouble(index: Int, nullnessRandom: Random, dataRandom: Random): Double? = when {
        shouldBeNull(nullnessRandom) -> null
        else -> flatDataProducer.nextDouble(index, dataRandom)
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
