package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkGeneratorRngs
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionBatch
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionFactory
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGeneratorFactory
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
        /** Creates deterministic data for one object benchmark parameter combination. */
        fun <T> create(
            collectionType: CollectionType,
            numCollections: Int,
            sizeDistributionFactory: DistributionFactory,
            objectGeneratorFactory: ObjectGeneratorFactory<T>,
        ): ObjectCollectionBenchmarkData<T> {
            val rngFactory = RngFactory()
            val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
            val sizeDistribution = sizeDistributionFactory.create(rngFactory)
            val objectGenerator = objectGeneratorFactory.create(generatorRngs)
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
                sizeDistribution = sizeDistribution,
            ) { size ->
                CollectionFactory.createCollection(size, collectionType, elementClass) {
                    objectGenerator.next()
                }
            }
            return ObjectCollectionBenchmarkData(batch, elementClass)
        }
    }
}
