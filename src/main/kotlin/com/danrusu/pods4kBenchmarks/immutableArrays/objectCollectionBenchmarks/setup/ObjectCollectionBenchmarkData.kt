package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkGeneratorRngs
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionFactory
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
import com.danrusu.pods4kBenchmarks.utils.ArrayCreator
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList

/**
 * Materialized collections for one object benchmark trial.
 *
 * Storing only the active representation avoids unused empty fields and prevents benchmarks from accidentally operating
 * on unrelated empty arrays.
 *
 * Accessors cast the array to the selected collection representation. Arrays store the component type, preventing an
 * Array<ArrayList> from being treated as an Array<PersistentList> etc.
 */
class ObjectCollectionBenchmarkData<T> private constructor(
    private val collectionData: Array<*>,
) {
    @Suppress("UNCHECKED_CAST")
    val listData: Array<ArrayList<T>>
        get() = collectionData as Array<ArrayList<T>>

    @Suppress("UNCHECKED_CAST")
    val persistentListData: Array<PersistentList<T>>
        get() = collectionData as Array<PersistentList<T>>

    @Suppress("UNCHECKED_CAST")
    val arrayData: Array<Array<T>>
        get() = collectionData as Array<Array<T>>

    @Suppress("UNCHECKED_CAST")
    val immutableArrayData: Array<ImmutableArray<T>>
        get() = collectionData as Array<ImmutableArray<T>>

    companion object {
        /** Creates deterministic data for one object benchmark parameter combination. */
        fun <T> create(
            collectionType: CollectionType,
            numCollections: Int,
            sizeDistributionFactory: DistributionFactory,
            objectGeneratorFactory: ObjectGeneratorFactory<T>,
        ): ObjectCollectionBenchmarkData<T> {
            require(numCollections > 0) { "numCollections must be positive" }

            val rngFactory = RngFactory()
            val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
            val sizeDistribution = sizeDistributionFactory.create(rngFactory)
            val objectGenerator = objectGeneratorFactory.create(generatorRngs)

            val data: Array<*> = when (collectionType) {
                LIST -> Array(numCollections) {
                    CollectionFactory.createList(sizeDistribution.nextValue()) {
                        objectGenerator.next()
                    }
                }

                PERSISTENT_LIST -> Array(numCollections) {
                    CollectionFactory.createPersistentList(sizeDistribution.nextValue()) {
                        objectGenerator.next()
                    }
                }

                ARRAY -> ArrayCreator.createNestedArrays(objectGenerator.objectClass, numCollections) {
                    ArrayCreator.createArray(objectGenerator.objectClass, sizeDistribution.nextValue()) {
                        objectGenerator.next()
                    }
                }

                IMMUTABLE_ARRAY -> Array(numCollections) {
                    ImmutableArray(sizeDistribution.nextValue()) {
                        objectGenerator.next()
                    }
                }
            }
            return ObjectCollectionBenchmarkData(data)
        }
    }
}
