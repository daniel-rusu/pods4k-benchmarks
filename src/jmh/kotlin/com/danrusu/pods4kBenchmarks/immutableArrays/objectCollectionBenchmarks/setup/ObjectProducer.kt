package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4kBenchmarks.immutableArrays.setup.FlatDataProducer
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.FlatDataProducerFactory
import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import kotlin.random.Random

interface ObjectProducer<T> {
    val objectClass: Class<T & Any>

    fun nextObject(): T
}

interface ObjectProducerFactory<T> {
    fun create(seed: Long): ObjectProducer<T>
}

object CompoundElementProducerFactory : ObjectProducerFactory<CompoundElement> {
    override fun create(seed: Long): ObjectProducer<CompoundElement> = RandomCompoundElementProducer(Random(seed))
}

private class RandomCompoundElementProducer(
    private val random: Random,
) : ObjectProducer<CompoundElement> {
    override val objectClass: Class<CompoundElement> = CompoundElement::class.java

    override fun nextObject(): CompoundElement = CompoundElement(
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
class CompoundNullableValuesProducerFactory(
    val nullRatio: Double,
    private val flatDataProducerFactory: FlatDataProducerFactory,
) : ObjectProducerFactory<CompoundElementOfNullableValues> {
    init {
        require(nullRatio in 0.0..1.0)
    }

    override fun create(seed: Long): ObjectProducer<CompoundElementOfNullableValues> {
        val seedRandom = Random(seed)
        return CompoundNullableValuesProducer(
            nullRatio = nullRatio,
            nullabilityRandom = Random(seedRandom.nextLong()),
            flatDataProducer = flatDataProducerFactory.create(seedRandom.nextLong()),
        )
    }
}

private class CompoundNullableValuesProducer(
    private val nullRatio: Double,
    private val nullabilityRandom: Random,
    private val flatDataProducer: FlatDataProducer,
) : ObjectProducer<CompoundElementOfNullableValues> {
    override val objectClass: Class<CompoundElementOfNullableValues> = CompoundElementOfNullableValues::class.java

    override fun nextObject(): CompoundElementOfNullableValues = CompoundElementOfNullableValues(
        nullableReference = if (shouldBeNull()) null else flatDataProducer.nextReference(),
        nullableBoolean = if (shouldBeNull()) null else flatDataProducer.nextBoolean(),
        nullableByte = if (shouldBeNull()) null else flatDataProducer.nextByte(),
        nullableChar = if (shouldBeNull()) null else flatDataProducer.nextChar(),
        nullableShort = if (shouldBeNull()) null else flatDataProducer.nextShort(),
        nullableInt = if (shouldBeNull()) null else flatDataProducer.nextInt(),
        nullableFloat = if (shouldBeNull()) null else flatDataProducer.nextFloat(),
        nullableLong = if (shouldBeNull()) null else flatDataProducer.nextLong(),
        nullableDouble = if (shouldBeNull()) null else flatDataProducer.nextDouble(),
    )

    private fun shouldBeNull(): Boolean = nullabilityRandom.nextDouble() < nullRatio
}
