package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.toImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.utils.Distribution
import kotlin.random.Random

/**
 * Creates and stores each of the 3 types of collections (list, array, & immutable array) with each storing wrappers
 * for their respective type.  The wrappers will themselves store a collection with size controlled by the
 * nested-collection-size-distribution.
 *
 * For example, for a size of 10 with a data type of [DataType.BOOLEAN] then:
 * - [array]
 *   - will be an array containing 10 [ArrayWrapperForDataType] elements
 *   - the wrappers will each store a primitive BooleanArray with size controlled by the specified distribution
 * - [list]
 *   - will be a list containing 10 [ListWrapperForDataType] elements
 *   - the wrappers will each store a List<Boolean> with contents copied from the array wrappers
 * - [immutableArray]
 *   - will be an immutable array containing 10 [ImmutableArrayWrapperForDataType] elements
 *   - the wrappers will each store a ImmutableBooleanArray with contents copied from the array wrappers
 */
class NestedCollectionWrapperForDataType<T>(
    random: Random,
    size: Int,
    dataType: DataType,
    nestedCollectionSizeDistribution: Distribution,
    createArray: (Random, size: Int) -> Array<T>,
    createBooleanArray: (Random, size: Int) -> BooleanArray,
    createByteArray: (Random, size: Int) -> ByteArray,
    createCharArray: (Random, size: Int) -> CharArray,
    createShortArray: (Random, size: Int) -> ShortArray,
    createIntArray: (Random, size: Int) -> IntArray,
    createFloatArray: (Random, size: Int) -> FloatArray,
    createLongArray: (Random, size: Int) -> LongArray,
    createDoubleArray: (Random, size: Int) -> DoubleArray,
) {
    val array: Array<ArrayWrapperForDataType<T>> = Array(size) {
        ArrayWrapperForDataType(
            random = random,
            size = nestedCollectionSizeDistribution.nextValue(random),
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
    val list: List<ListWrapperForDataType<T>> = array.map {
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
    }

    // copy the data from the regular array so that they are tested against identical data
    val immutableArray: ImmutableArray<ImmutableArrayWrapperForDataType<T>> = array.map {
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
    }.toImmutableArray()
}
