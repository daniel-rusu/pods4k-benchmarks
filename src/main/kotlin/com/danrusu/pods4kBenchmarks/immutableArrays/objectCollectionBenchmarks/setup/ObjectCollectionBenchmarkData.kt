package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkGeneratorRngs
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionBatch
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionFactory
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
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
    val listData: Array<List<T>>
        get() = batch.getCollections(CollectionType.LIST, elementClass)

    val persistentListData: Array<PersistentList<T>>
        get() = batch.getCollections(CollectionType.PERSISTENT_LIST, elementClass)

    val arrayData: Array<Array<T>>
        get() = batch.getCollections(CollectionType.ARRAY, elementClass)

    val immutableArrayData: Array<ImmutableArray<T>>
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
            val collectionClass = CollectionFactory.getCollectionClass(
                collectionType,
                DataType.REFERENCE,
                elementClass,
            )

            return ObjectCollectionBenchmarkData(
                batch = CollectionBatch.create(
                    collectionType,
                    elementClass,
                    collectionClass,
                    numCollections,
                    sizeDistribution
                ) { size ->
                    CollectionFactory.createCollection(size, collectionType, elementClass) {
                        objectGenerator.next()
                    }
                },
                elementClass = elementClass,
            )
        }
    }
}
