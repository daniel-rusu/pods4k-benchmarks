package com.danrusu.pods4kBenchmarks.immutableArrays.commonData

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableArray
import com.danrusu.pods4kBenchmarks.utils.Distribution
import kotlin.random.Random

/**
 * Creates a bunch of collections of the specified collection type and provides utilities for performing operations on
 * each collection.
 *
 * Note that only the appropriate factory will be invoked based on the specified collection type.
 *
 * @param numCollections The number of collections to be created
 * @param type The type of collection needed for the current benchmark
 * @param sizeDistribution The probability distribution controlling the collection sizes
 * @param createList The factory for creating a list of the specified size
 * @param createArray The factory for creating an array of the specified size
 * @param createImmutableArray The factory for creating an immutable array of the specified size
 */
class Collections<T>(
    numCollections: Int,
    type: CollectionType,
    sizeDistribution: Distribution,
    createList: (Random, size: Int) -> List<T>,
    createArray: (Random, size: Int) -> Array<T>,
    createImmutableArray: (Random, size: Int) -> ImmutableArray<T>
) {
    /**
     * Note: We're storing an array of [CollectionHolder] instances instead of 3 separate arrays for each collection
     * type.  This is because we want to avoid auto-boxing the immutable arrays as that would create misleading results
     * because immutable arrays are typically stored in strongly-typed variables of type [ImmutableArray].
     */
    val data: Array<CollectionHolder<T>>

    init {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)

        @Suppress("UNCHECKED_CAST")
        data = Array(numCollections) {
            val numElements = sizeDistribution.nextValue(random)

            var list = emptyList<Any>() as List<T>
            var array = emptyArray<Any>() as Array<T>
            var immutableArray = emptyImmutableArray<T>()

            when (type) {
                CollectionType.LIST -> {
                    list = createList(random, numElements)
                    check(list.size == numElements)
                }

                CollectionType.ARRAY -> {
                    array = createArray(random, numElements)
                    check(array.size == numElements)
                }

                CollectionType.IMMUTABLE_ARRAY -> {
                    immutableArray = createImmutableArray(random, numElements)
                    check(immutableArray.size == numElements)
                }
            }

            CollectionHolder(
                list = list,
                array = array,
                immutableArray = immutableArray,
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

class CollectionHolder<T>(
    val list: List<T>,
    val array: Array<T>,
    val immutableArray: ImmutableArray<T>,
)
