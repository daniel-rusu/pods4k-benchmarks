package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType
import kotlin.random.Random

private val EMPTY_LIST: List<Nothing> = emptyList()
private val EMPTY_ARRAY: Array<Any> = emptyArray()
private val EMPTY_IMMUTABLE_ARRAY: ImmutableArray<Nothing> = emptyImmutableArray()

/**
 * Creates and stores a collection of the specified collection type by using the appropriate provided factory.
 */
class WrapperForCollectionType<T>(
    random: Random,
    size: Int,
    collectionType: CollectionType,
    createList: (Random, size: Int) -> List<T>,
    createArray: (Random, size: Int) -> Array<T>,
    createImmutableArray: (Random, size: Int) -> ImmutableArray<T>,
) {
    var list: List<T> = EMPTY_LIST
        private set

    @Suppress("UNCHECKED_CAST")
    var array: Array<T> = EMPTY_ARRAY as Array<T>
        private set

    var immutableArray: ImmutableArray<T> = EMPTY_IMMUTABLE_ARRAY
        private set

    init {
        when (collectionType) {
            CollectionType.LIST -> {
                list = createList(random, size)
                check(list.size == size)
            }

            CollectionType.ARRAY -> {
                array = createArray(random, size)
                check(array.size == size)
            }

            CollectionType.IMMUTABLE_ARRAY -> {
                immutableArray = createImmutableArray(random, size)
                check(immutableArray.size == size)
            }
        }
    }
}
