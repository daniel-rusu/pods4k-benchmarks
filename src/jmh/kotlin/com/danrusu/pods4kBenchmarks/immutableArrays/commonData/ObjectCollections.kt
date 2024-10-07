package com.danrusu.pods4kBenchmarks.immutableArrays.commonData

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.WrapperForCollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.ObjectProducer
import com.danrusu.pods4kBenchmarks.utils.Distribution
import org.openjdk.jmh.infra.Blackhole
import kotlin.random.Random

/**
 * Creates a bunch of collections of the specified collection type and provides utilities for performing operations on
 * each collection.
 *
 * @param numCollections The number of collections to be created
 * @param type The type of collection needed for the current benchmark
 * @param sizeDistribution The probability distribution controlling the collection sizes
 * @param objectProducer Produces the objects that the collections will store
 * @param objectClass The class of the objects being produced.  Needed when instantiating arrays.
 */
class ObjectCollections<T>(
    numCollections: Int,
    type: CollectionType,
    sizeDistribution: Distribution = Distribution.LIST_SIZE_DISTRIBUTION,
    objectProducer: ObjectProducer<T>,
    objectClass: Class<T>,
) {
    /**
     * Note: We're storing an array of [WrapperForCollectionType] instances with each wrapper storing the appropriate
     * collection instead of 3 separate arrays of collections.  This is because we want to avoid auto-boxing the
     * immutable arrays as that would create misleading results because immutable arrays are typically stored in
     * strongly-typed variables of type [ImmutableArray].
     */
    val data: Array<WrapperForCollectionType<T>>

    init {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)

        data = Array(numCollections) {
            val numElements = sizeDistribution.nextValue(random)

            WrapperForCollectionType(
                random = random,
                size = numElements,
                collectionType = type,
                objectProducer = objectProducer,
                objectClass = objectClass,
            )
        }
    }

    /**
     * Loops through all the collections of the specified [collectionType] and performs the associated operation
     * consuming each result with the [Blackhole].
     *
     * E.g. If the specified collectionType is [CollectionType.ARRAY], then it loops through all the collection
     * wrappers and calls [transformArray] on each [WrapperForCollectionType.array].
     */
    inline fun transformEachCollectionOfCollectionType(
        bh: Blackhole,
        collectionType: CollectionType,
        transformList: (List<T>) -> Any?,
        transformArray: (Array<T>) -> Any?,
        transformImmutableArray: (ImmutableArray<T>) -> Any?,
    ) {
        when (collectionType) {
            LIST -> data.forEach { bh.consume(transformList(it.list)) }
            ARRAY -> data.forEach { bh.consume(transformArray(it.array)) }
            IMMUTABLE_ARRAY -> data.forEach { bh.consume(transformImmutableArray(it.immutableArray)) }
        }
    }
}
