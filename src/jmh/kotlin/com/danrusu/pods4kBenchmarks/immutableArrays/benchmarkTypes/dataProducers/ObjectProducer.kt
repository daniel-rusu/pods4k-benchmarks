package com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.dataProducers

import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import kotlin.random.Random

interface ObjectProducer<T> {
    /** Indicates that a new collection is about to be created that will store [size] number of elements */
    fun startNewCollection(size: Int)

    fun nextObject(index: Int, random: Random): T

    object CompoundElementProducer : ObjectProducer<CompoundElement> {
        override fun startNewCollection(size: Int) {}

        override fun nextObject(index: Int, random: Random): CompoundElement = CompoundElement(
            referenceValue = DataGenerator.randomString(random = random),
            booleanValue = random.nextBoolean(),
            byteValue = DataGenerator.randomByte(random),
            charValue = DataGenerator.randomChar(random),
            shortValue = DataGenerator.randomShort(random),
            intValue = random.nextInt(),
            floatValue = random.nextFloat(),
            longValue = random.nextLong(),
            doubleValue = random.nextDouble(),
        )
    }
}
