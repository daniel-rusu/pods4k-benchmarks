package com.danrusu.pods4kBenchmarks.immutableArrays.commonData

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ArrayWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableArrayWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ListWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.NestedCollectionWrapperForDataType
import com.danrusu.pods4kBenchmarks.utils.DataGenerator
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
    createArray: (Random, size: Int) -> Array<String> = { random, size ->
        Array(size) { DataGenerator.randomString(random = random) }
    },
    createBooleanArray: (Random, size: Int) -> BooleanArray = { random, size ->
        BooleanArray(size) { random.nextBoolean() }
    },
    createByteArray: (Random, size: Int) -> ByteArray = { random, size ->
        ByteArray(size) { DataGenerator.randomByte(random) }
    },
    createCharArray: (Random, size: Int) -> CharArray = { random, size ->
        CharArray(size) { DataGenerator.randomChar(random) }
    },
    createShortArray: (Random, size: Int) -> ShortArray = { random, size ->
        ShortArray(size) { DataGenerator.randomShort(random) }
    },
    createIntArray: (Random, size: Int) -> IntArray = { random, size ->
        IntArray(size) { random.nextInt() }
    },
    createFloatArray: (Random, size: Int) -> FloatArray = { random, size ->
        FloatArray(size) { random.nextFloat() }
    },
    createLongArray: (Random, size: Int) -> LongArray = { random, size ->
        LongArray(size) { random.nextLong() }
    },
    createDoubleArray: (Random, size: Int) -> DoubleArray = { random, size ->
        DoubleArray(size) { random.nextDouble() }
    },
) {
    val data: Array<NestedCollectionWrapperForDataType>

    init {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)

        data = Array(numCollections) {
            val numElements = topLevelSizeDistribution.nextValue(random)

            NestedCollectionWrapperForDataType(
                random = random,
                size = numElements,
                dataType = dataType,
                nestedCollectionSizeDistribution = nestedCollectionSizeDistribution,
                createArray = createArray,
                createBooleanArray = createBooleanArray,
                createByteArray = createByteArray,
                createCharArray = createCharArray,
                createShortArray = createShortArray,
                createIntArray = createIntArray,
                createFloatArray = createFloatArray,
                createLongArray = createLongArray,
                createDoubleArray = createDoubleArray,
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
