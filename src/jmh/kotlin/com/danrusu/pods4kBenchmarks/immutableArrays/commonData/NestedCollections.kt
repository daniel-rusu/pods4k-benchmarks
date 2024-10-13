package com.danrusu.pods4kBenchmarks.immutableArrays.commonData

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.NestedCollectionWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.FlatDataProducer
import com.danrusu.pods4kBenchmarks.utils.Distribution
import org.openjdk.jmh.infra.Blackhole
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
class NestedCollections(
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

    inline fun transformEachList(bh: Blackhole, body: (List<ListWrapper>) -> Any?) {
        data.forEach { bh.consume(body(it.list)) }
    }

    inline fun transformEachArray(bh: Blackhole, body: (Array<ArrayWrapper>) -> Any?) {
        data.forEach { bh.consume(body(it.array)) }
    }

    inline fun transformEachImmutableArray(bh: Blackhole, body: (ImmutableArray<ImmutableArrayWrapper>) -> Any?) {
        data.forEach { bh.consume(body(it.immutableArray)) }
    }
}
