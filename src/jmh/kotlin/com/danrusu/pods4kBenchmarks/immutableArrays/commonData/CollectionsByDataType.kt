package com.danrusu.pods4kBenchmarks.immutableArrays.commonData

import com.danrusu.pods4k.immutableArrays.toImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ArrayWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableArrayWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ListWrapperForDataType
import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import com.danrusu.pods4kBenchmarks.utils.Distribution
import kotlin.random.Random

/**
 * Creates and stores 3 arrays containing wrappers for the 3 collection types (list, array, & immutable array).  Each
 * wrapper stores a collection of the specified data type with size controlled by the size-distribution.
 *
 * For example, for a size of 10 with a data type of [DataType.BOOLEAN] then:
 * - [arrays]
 *   - will be an array containing 10 [ArrayWrapperForDataType] elements
 *   - the wrappers will each store a primitive BooleanArray with size controlled by the specified distribution
 * - [lists]
 *   - will be a list containing 10 [ListWrapperForDataType] elements
 *   - the wrappers will each store a List<Boolean> with contents copied from the array wrappers
 * - [immutableArrays]
 *   - will be an immutable array containing 10 [ImmutableArrayWrapperForDataType] elements
 *   - the wrappers will each store a ImmutableBooleanArray with contents copied from the array wrappers
 */
class CollectionsByDataType(
    numCollections: Int,
    dataType: DataType,
    sizeDistribution: Distribution = Distribution.LIST_SIZE_DISTRIBUTION,
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
    val arrays: Array<ArrayWrapperForDataType>
    val lists: Array<ListWrapperForDataType>
    val immutableArrays: Array<ImmutableArrayWrapperForDataType>

    init {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)

        arrays = Array(numCollections) {
            ArrayWrapperForDataType(
                random = random,
                size = sizeDistribution.nextValue(random),
                dataType = dataType,
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

        // copy the data from the regular array so that they are tested against identical data
        lists = arrays.map {
            ListWrapperForDataType(
                random = random,
                size = it.size,
                dataType = dataType,
                createList = { _, _ -> it.referenceArray.toList() },
                createBooleanList = { _, _ -> it.booleanArray.toList() },
                createByteList = { _, _ -> it.byteArray.toList() },
                createCharList = { _, _ -> it.charArray.toList() },
                createShortList = { _, _ -> it.shortArray.toList() },
                createIntList = { _, _ -> it.intArray.toList() },
                createFloatList = { _, _ -> it.floatArray.toList() },
                createLongList = { _, _ -> it.longArray.toList() },
                createDoubleList = { _, _ -> it.doubleArray.toList() },
            )
        }.toTypedArray()

        // copy the data from the regular array so that they are tested against identical data
        immutableArrays = arrays.map {
            ImmutableArrayWrapperForDataType(
                random = random,
                size = it.size,
                dataType = dataType,
                createImmutableArray = { _, _ -> it.referenceArray.toImmutableArray() },
                createImmutableBooleanArray = { _, _ -> it.booleanArray.toImmutableArray() },
                createImmutableByteArray = { _, _ -> it.byteArray.toImmutableArray() },
                createImmutableCharArray = { _, _ -> it.charArray.toImmutableArray() },
                createImmutableShortArray = { _, _ -> it.shortArray.toImmutableArray() },
                createImmutableIntArray = { _, _ -> it.intArray.toImmutableArray() },
                createImmutableFloatArray = { _, _ -> it.floatArray.toImmutableArray() },
                createImmutableLongArray = { _, _ -> it.longArray.toImmutableArray() },
                createImmutableDoubleArray = { _, _ -> it.doubleArray.toImmutableArray() },
            )
        }.toTypedArray()
    }

    inline fun forEachList(body: (ListWrapperForDataType) -> Unit) {
        lists.forEach { body(it) }
    }

    inline fun forEachArray(body: (ArrayWrapperForDataType) -> Unit) {
        arrays.forEach { body(it) }
    }

    inline fun forEachImmutableArray(body: (ImmutableArrayWrapperForDataType) -> Unit) {
        immutableArrays.forEach { body(it) }
    }
}
