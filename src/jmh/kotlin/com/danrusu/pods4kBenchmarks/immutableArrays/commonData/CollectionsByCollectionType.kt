package com.danrusu.pods4kBenchmarks.immutableArrays.commonData

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.WrapperForCollectionType
import com.danrusu.pods4kBenchmarks.utils.Distribution
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
class CollectionsByCollectionType<T>(
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

    inline fun forEachList(body: (List<T>) -> Unit) {
        data.forEach { body(it.list) }
    }

    inline fun forEachArray(body: (Array<T>) -> Unit) {
        data.forEach { body(it.array) }
    }

    inline fun forEachImmutableArray(body: (ImmutableArray<T>) -> Unit) {
        data.forEach { body(it.immutableArray) }
    }
}
