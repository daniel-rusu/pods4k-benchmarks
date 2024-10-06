package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.ObjectProducer
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType
import com.danrusu.pods4kBenchmarks.utils.ArrayCreator
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
    objectProducer: ObjectProducer<T>,
    objectClass: Class<T>,
) {
    var list: List<T> = EMPTY_LIST
        private set

    @Suppress("UNCHECKED_CAST")
    var array: Array<T> = EMPTY_ARRAY as Array<T>
        private set

    var immutableArray: ImmutableArray<T> = EMPTY_IMMUTABLE_ARRAY
        private set

    init {
        objectProducer.startNewCollection(size)
        when (collectionType) {
            CollectionType.LIST -> {
                list = (0..<size).map { objectProducer.nextObject(it, random) }
            }

            CollectionType.ARRAY -> {
                array = ArrayCreator.createArray(objectClass, size) { objectProducer.nextObject(it, random) }
            }

            CollectionType.IMMUTABLE_ARRAY -> {
                immutableArray = ImmutableArray(size) { objectProducer.nextObject(it, random) }
            }
        }
    }
}
