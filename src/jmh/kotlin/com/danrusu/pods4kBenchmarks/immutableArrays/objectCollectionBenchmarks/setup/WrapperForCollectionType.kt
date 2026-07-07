package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
import com.danrusu.pods4kBenchmarks.utils.ArrayCreator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

private val EMPTY_LIST: List<Nothing> = emptyList()
private val EMPTY_PERSISTENT_LIST: PersistentList<Nothing> = persistentListOf()
private val EMPTY_ARRAY: Array<Any> = emptyArray()
private val EMPTY_IMMUTABLE_ARRAY: ImmutableArray<Nothing> = emptyImmutableArray()

/** Creates and stores a collection of the specified collection type */
class WrapperForCollectionType<T>(
    collectionSize: Int,
    collectionType: CollectionType,
    objectGenerator: ObjectGenerator<T>,
) {
    var list: List<T> = EMPTY_LIST
        private set

    var persistentList: PersistentList<T> = EMPTY_PERSISTENT_LIST
        private set

    @Suppress("UNCHECKED_CAST")
    var array: Array<T> = EMPTY_ARRAY as Array<T>
        private set

    var immutableArray: ImmutableArray<T> = EMPTY_IMMUTABLE_ARRAY
        private set

    init {
        val objectClass = objectGenerator.objectClass

        when (collectionType) {
            LIST -> {
                list = (1..collectionSize).map { objectGenerator.next() }
            }

            PERSISTENT_LIST -> {
                val builder = persistentListOf<T>().builder()
                repeat(collectionSize) { builder.add(objectGenerator.next()) }
                persistentList = builder.build()
            }

            ARRAY -> {
                array = ArrayCreator.createArray(objectClass, collectionSize) { objectGenerator.next() }
            }

            IMMUTABLE_ARRAY -> {
                immutableArray = ImmutableArray(collectionSize) { objectGenerator.next() }
            }
        }
    }
}
