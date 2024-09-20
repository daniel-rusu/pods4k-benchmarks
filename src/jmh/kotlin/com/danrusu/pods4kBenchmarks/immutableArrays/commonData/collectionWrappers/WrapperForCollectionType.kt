package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.CollectionType
import kotlin.random.Random

private val emptyList = emptyList<Any>()
private val emptyArray = emptyArray<Any>()
private val emptyImmutableArray = emptyImmutableArray<Any>()

/**
 * Creates and stores a collection of the specified collection type by using the appropriate provided factory.
 */
@Suppress("UNCHECKED_CAST")
class WrapperForCollectionType<T>(
    random: Random,
    size: Int,
    collectionType: CollectionType,
    createList: (Random, size: Int) -> List<T>,
    createArray: (Random, size: Int) -> Array<T>,
    createImmutableArray: (Random, size: Int) -> ImmutableArray<T>,
) {
    val list: List<T> = when (collectionType) {
        CollectionType.LIST -> createList(random, size)
        else -> emptyList as List<T>
    }

    val array: Array<T> = when (collectionType) {
        CollectionType.ARRAY -> createArray(random, size)
        else -> emptyArray as Array<T>
    }

    val immutableArray: ImmutableArray<T> = when (collectionType) {
        CollectionType.IMMUTABLE_ARRAY -> createImmutableArray(random, size)
        else -> emptyImmutableArray as ImmutableArray<T>
    }
}
