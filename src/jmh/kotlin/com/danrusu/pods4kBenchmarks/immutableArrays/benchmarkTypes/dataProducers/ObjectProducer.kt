package com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.dataProducers

import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import kotlin.random.Random

interface ObjectProducer<T> {
    val objectClass: Class<T>

    /** Indicates that a new collection is about to be created that will store [size] number of elements */
    fun startNewCollection(size: Int)

    fun nextObject(index: Int, random: Random): T
}

object CompoundElementProducer : ObjectProducer<CompoundElement> {
    override val objectClass: Class<CompoundElement> = CompoundElement::class.java

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

/**
 * Produces [CompoundElementOfNullableValues] with each field being randomly null [nullRatio] proportion of the time.
 *
 * Note that each field is independently randomly chosen to be null or not.  While the number of nulls that each
 * [CompoundElementOfNullableValues] contains have will vary, statistically, [nullRatio] proportion of its fields
 * will be null on average.  Additionally, when looking at multiple [CompoundElementOfNullableValues] instances, any
 * particular field will be null [nullRatio] proportion of the time on average.
 *
 * E.g. If [nullRatio] is 0.4, each field will be randomly null 40% of the time and have some random value the other 60%
 * of the time.
 */
class CompoundElementOfNullableValuesProducer(
    val nullRatio: Double,
) : ObjectProducer<CompoundElementOfNullableValues> {
    init {
        require(nullRatio in 0.0..1.0)
    }

    override val objectClass: Class<CompoundElementOfNullableValues> = CompoundElementOfNullableValues::class.java

    override fun startNewCollection(size: Int) {}

    override fun nextObject(
        index: Int,
        random: Random
    ): CompoundElementOfNullableValues = CompoundElementOfNullableValues(
        nullableReference = if (shouldBeNull(random)) null else DataGenerator.randomString(random = random),
        nullableBoolean = if (shouldBeNull(random)) null else random.nextBoolean(),
        nullableByte = if (shouldBeNull(random)) null else DataGenerator.randomByte(random),
        nullableChar = if (shouldBeNull(random)) null else DataGenerator.randomChar(random),
        nullableShort = if (shouldBeNull(random)) null else DataGenerator.randomShort(random),
        nullableInt = if (shouldBeNull(random)) null else random.nextInt(),
        nullableFloat = if (shouldBeNull(random)) null else random.nextFloat(),
        nullableLong = if (shouldBeNull(random)) null else random.nextLong(),
        nullableDouble = if (shouldBeNull(random)) null else random.nextDouble(),
    )

    private fun shouldBeNull(random: Random): Boolean = random.nextDouble() < nullRatio
}
