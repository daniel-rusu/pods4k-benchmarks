package com.danrusu.pods4kBenchmarks.immutableArrays.commonData

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableArray
import com.danrusu.pods4kBenchmarks.utils.Distribution
import kotlin.random.Random

/**
 * Creates a bunch of collections of the specified collection type and provides utilities for performing an operation
 * on each collection.
 *
 * The size of each collection will be randomly chosen based on the [Distribution.LIST_SIZE_DISTRIBUTION] in order to
 * replicate the kind of collection sizes that are expected in the real world.
 *
 * Note that only the appropriate factory will be invoked based on the specified collection type.
 *
 * @param numCollections The number of collections to be created
 * @param type The type of collection needed for the current benchmark
 * @param createList The factory for creating a list of the specified size
 * @param createArray The factory for creating an array of the specified size
 * @param createImmutableArray The factory for creating an immutable array of the specified size
 */
class CollectionsOfCompoundElements(
    numCollections: Int,
    type: GenericCollectionType,
    createList: (Random, size: Int) -> List<CompoundElement>,
    createArray: (Random, size: Int) -> Array<CompoundElement>,
    createImmutableArray: (Random, size: Int) -> ImmutableArray<CompoundElement>
) {
    /**
     * Note that we're storing the collections as an array of [CollectionHolder] instances instead of 3 separate arrays
     * containing each collection type.  This is because we want to avoid auto-boxing the immutable arrays as that
     * would create misleading results because immutable arrays are typically stored in strongly-typed variables of
     * type [ImmutableArray].
     */
    val data: Array<CollectionHolder>

    init {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)

        data = Array(numCollections) {
            val numElements = Distribution.LIST_SIZE_DISTRIBUTION.nextValue(random)

            var list = emptyList<CompoundElement>()
            var array = emptyArray<CompoundElement>()
            var immutableArray = emptyImmutableArray<CompoundElement>()

            when (type) {
                GenericCollectionType.LIST -> {
                    list = createList(random, numElements)
                    check(list.size == numElements)
                }

                GenericCollectionType.ARRAY -> {
                    array = createArray(random, numElements)
                    check(array.size == numElements)
                }

                GenericCollectionType.IMMUTABLE_ARRAY -> {
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

    inline fun forEachList(body: (List<CompoundElement>) -> Unit) {
        data.forEach { body(it.list) }
    }

    inline fun forEachArray(body: (Array<CompoundElement>) -> Unit) {
        data.forEach { body(it.array) }
    }

    inline fun forEachImmutableArray(body: (ImmutableArray<CompoundElement>) -> Unit) {
        data.forEach { body(it.immutableArray) }
    }
}

class CollectionHolder(
    val list: List<CompoundElement>,
    val array: Array<CompoundElement>,
    val immutableArray: ImmutableArray<CompoundElement>,
)
