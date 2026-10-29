package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionBatch
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionFactory
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import kotlinx.collections.immutable.PersistentList

/**
 * Materialized collections for one object benchmark trial.
 *
 * Provides statically typed access to collections of the generated object type [T] across the four benchmark collection
 * representations.
 */
class ObjectCollectionBenchmarkData<T> private constructor(
    @PublishedApi internal val batch: CollectionBatch,
    private val elementClass: Class<T & Any>,
) {
    val lists: Array<List<T>>
        get() = batch.getCollections(CollectionType.LIST, elementClass)

    val persistentLists: Array<PersistentList<T>>
        get() = batch.getCollections(CollectionType.PERSISTENT_LIST, elementClass)

    val arrays: Array<Array<T>>
        get() = batch.getCollections(CollectionType.ARRAY, elementClass)

    val immutableArrays: Array<ImmutableArray<T>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, elementClass)

    companion object {
        /** Materializes data from the supplied recipe for one object benchmark parameter combination. */
        fun <T> create(
            collectionType: CollectionType,
            numCollections: Int,
            recipe: ObjectCollectionRecipe<T>,
        ): ObjectCollectionBenchmarkData<T> {
            val objectGenerator = recipe.objectGenerator
            val elementClass = objectGenerator.objectClass
            val collectionClass = CollectionFactory.resolveCollectionClass(
                collectionType = collectionType,
                dataType = DataType.REFERENCE,
                referenceElementClass = elementClass,
            )

            val batch = CollectionBatch.create(
                collectionType = collectionType,
                logicalElementClass = elementClass,
                collectionClass = collectionClass,
                numCollections = numCollections,
                sizeDistribution = recipe.sizeDistribution,
            ) { size ->
                CollectionFactory.createCollection(size, collectionType, elementClass) {
                    objectGenerator.next()
                }
            }
            return ObjectCollectionBenchmarkData(batch, elementClass)
        }
    }
}
