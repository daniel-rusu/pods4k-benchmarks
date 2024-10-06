package com.danrusu.pods4kBenchmarks.immutableArrays.commonData

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ArrayWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableArrayWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ListWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.NestedCollectionWrapperForDataType
import com.danrusu.pods4kBenchmarks.utils.Distribution
import kotlin.random.Random

/**
 * Creates a bunch of collections which themselves contain nested collections of the specified data type.  This also
 * provides utilities for performing operations on each top-level collection.
 *
 * Note that only the appropriate factories will be invoked based on the specified data type.
 *
 * @param numCollections The number of collections to be created
 * @param dataType The type of data that the nested collection stores
 * @param topLevelSizeDistribution The probability distribution controlling the top-level collection sizes
 * @param nestedCollectionSizeDistribution The probability distribution controlling the nested collection sizes
 */
class NestedCollectionsByDataType(
    numCollections: Int,
    dataType: DataType,
    topLevelSizeDistribution: Distribution = Distribution.LIST_SIZE_DISTRIBUTION,
    nestedCollectionSizeDistribution: Distribution = Distribution.NESTED_LIST_SIZE_DISTRIBUTION,
    nestedDataProducer: FlatDataProducer = FlatDataProducer.RandomDataProducer,
) {
    val data: Array<NestedCollectionWrapperForDataType>

    init {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)

        data = Array(numCollections) {
            val numElements = topLevelSizeDistribution.nextValue(random)

            NestedCollectionWrapperForDataType(
                size = numElements,
                random = random,
                dataType = dataType,
                nestedCollectionSizeDistribution = nestedCollectionSizeDistribution,
                dataProducer = nestedDataProducer,
            )
        }
    }

    inline fun forEachList(body: (List<ListWrapperForDataType>) -> Unit) {
        data.forEach { body(it.list) }
    }

    inline fun forEachArray(body: (Array<ArrayWrapperForDataType>) -> Unit) {
        data.forEach { body(it.array) }
    }

    inline fun forEachImmutableArray(body: (ImmutableArray<ImmutableArrayWrapperForDataType>) -> Unit) {
        data.forEach { body(it.immutableArray) }
    }
}
