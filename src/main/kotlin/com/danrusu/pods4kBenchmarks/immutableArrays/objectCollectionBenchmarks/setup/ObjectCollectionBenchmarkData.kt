package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkGeneratorRngs
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.utils.ArrayCreator
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * The strongly typed backing collections for one object benchmark parameter combination.
 *
 * Only the property selected by the collection type passed to [create] is populated.
 */
class ObjectCollectionBenchmarkData<T> internal constructor(
    val listData: Array<List<T>> = emptyArray(),
    val persistentListData: Array<PersistentList<T>> = emptyArray(),
    val arrayData: Array<Array<T>> = emptyArrayData(),
    val immutableArrayData: Array<ImmutableArray<T>> = emptyArray(),
) {
    init {
        val populatedArrayCount = listOf(
            listData,
            persistentListData,
            arrayData,
            immutableArrayData,
        ).count { it.isNotEmpty() }

        require(populatedArrayCount == 1) {
            "Exactly one backing array must be populated (found $populatedArrayCount)"
        }
    }

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

            return when (collectionType) {
                CollectionType.LIST -> ObjectCollectionBenchmarkData(
                    listData = Array(numCollections) {
                        List(sizeDistribution.nextValue()) { objectGenerator.next() }
                    },
                )

                CollectionType.PERSISTENT_LIST -> ObjectCollectionBenchmarkData(
                    persistentListData = Array(numCollections) {
                        val builder = persistentListOf<T>().builder()
                        repeat(sizeDistribution.nextValue()) { builder.add(objectGenerator.next()) }
                        builder.build()
                    },
                )

                CollectionType.ARRAY -> {
                    val arrays = arrayOfNulls<Array<*>>(numCollections)
                    repeat(numCollections) { index ->
                        arrays[index] = ArrayCreator.createArray(
                            objectGenerator.objectClass,
                            sizeDistribution.nextValue(),
                        ) {
                            objectGenerator.next()
                        }
                    }
                    @Suppress("UNCHECKED_CAST")
                    ObjectCollectionBenchmarkData(arrayData = arrays as Array<Array<T>>)
                }

                CollectionType.IMMUTABLE_ARRAY -> ObjectCollectionBenchmarkData(
                    immutableArrayData = Array(numCollections) {
                        ImmutableArray(sizeDistribution.nextValue()) { objectGenerator.next() }
                    },
                )
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T> emptyArrayData(): Array<Array<T>> = emptyArray<Array<*>>() as Array<Array<T>>
