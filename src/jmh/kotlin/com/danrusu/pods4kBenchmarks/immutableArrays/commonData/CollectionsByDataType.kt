package com.danrusu.pods4kBenchmarks.immutableArrays.commonData

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ArrayWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableArrayWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ListWrapperForDataType
import com.danrusu.pods4kBenchmarks.utils.Distribution
import org.openjdk.jmh.infra.Blackhole
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
    dataProducer: DataProducer = DataProducer.RandomDataProducer,
) {
    val arrays: Array<ArrayWrapperForDataType>
    val lists: Array<ListWrapperForDataType>
    val immutableArrays: Array<ImmutableArrayWrapperForDataType>

    init {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)

        arrays = Array(numCollections) {
            ArrayWrapperForDataType(
                size = sizeDistribution.nextValue(random),
                random = random,
                dataType = dataType,
                dataProducer = dataProducer,
            )
        }

        // copy the data from the regular array so that they are tested against identical data
        lists = arrays.map { arrayWrapper ->
            ListWrapperForDataType(
                size = arrayWrapper.size,
                random = random,
                dataType = dataType,
                dataProducer = arrayWrapper.copyData(),
            )
        }.toTypedArray()

        // copy the data from the regular array so that they are tested against identical data
        immutableArrays = arrays.map { arrayWrapper ->
            ImmutableArrayWrapperForDataType(
                size = arrayWrapper.size,
                random = random,
                dataType = dataType,
                dataProducer = arrayWrapper.copyData(),
            )
        }.toTypedArray()
    }

    /**
     * Loops through all the lists of the specified [dataType] and performs the associated operation consuming each
     * result with the [Blackhole].
     *
     * E.g. If the specified dataType is [DataType.BOOLEAN], then it loops through all the list wrappers and calls
     * [transformBooleanList] on each [ListWrapperForDataType.booleanList].
     */
    inline fun transformEachListOfDataType(
        bh: Blackhole,
        dataType: DataType,
        transformList: (List<String>) -> Any?,
        transformBooleanList: (List<Boolean>) -> Any?,
        transformByteList: (List<Byte>) -> Any?,
        transformCharList: (List<Char>) -> Any?,
        transformShortList: (List<Short>) -> Any?,
        transformIntList: (List<Int>) -> Any?,
        transformFloatList: (List<Float>) -> Any?,
        transformLongList: (List<Long>) -> Any?,
        transformDoubleList: (List<Double>) -> Any?,
    ) {
        when (dataType) {
            DataType.REFERENCE -> lists.forEach { bh.consume(transformList(it.referenceList)) }
            DataType.BOOLEAN -> lists.forEach { bh.consume(transformBooleanList(it.booleanList)) }
            DataType.BYTE -> lists.forEach { bh.consume(transformByteList(it.byteList)) }
            DataType.CHAR -> lists.forEach { bh.consume(transformCharList(it.charList)) }
            DataType.SHORT -> lists.forEach { bh.consume(transformShortList(it.shortList)) }
            DataType.INT -> lists.forEach { bh.consume(transformIntList(it.intList)) }
            DataType.FLOAT -> lists.forEach { bh.consume(transformFloatList(it.floatList)) }
            DataType.LONG -> lists.forEach { bh.consume(transformLongList(it.longList)) }
            DataType.DOUBLE -> lists.forEach { bh.consume(transformDoubleList(it.doubleList)) }
        }
    }

    /**
     * Loops through all the arrays of the specified [dataType] and performs the associated operation consuming each
     * result with the [Blackhole].
     *
     * E.g. If the specified dataType is [DataType.BOOLEAN], then it loops through all the array wrappers and calls
     * [transformBooleanArray] on each [ArrayWrapperForDataType.booleanArray].
     */
    inline fun transformEachArrayOfDataType(
        bh: Blackhole,
        dataType: DataType,
        transformArray: (Array<String>) -> Any?,
        transformBooleanArray: (BooleanArray) -> Any?,
        transformByteArray: (ByteArray) -> Any?,
        transformCharArray: (CharArray) -> Any?,
        transformShortArray: (ShortArray) -> Any?,
        transformIntArray: (IntArray) -> Any?,
        transformFloatArray: (FloatArray) -> Any?,
        transformLongArray: (LongArray) -> Any?,
        transformDoubleArray: (DoubleArray) -> Any?,
    ) {
        when (dataType) {
            DataType.REFERENCE -> arrays.forEach { bh.consume(transformArray(it.referenceArray)) }
            DataType.BOOLEAN -> arrays.forEach { bh.consume(transformBooleanArray(it.booleanArray)) }
            DataType.BYTE -> arrays.forEach { bh.consume(transformByteArray(it.byteArray)) }
            DataType.CHAR -> arrays.forEach { bh.consume(transformCharArray(it.charArray)) }
            DataType.SHORT -> arrays.forEach { bh.consume(transformShortArray(it.shortArray)) }
            DataType.INT -> arrays.forEach { bh.consume(transformIntArray(it.intArray)) }
            DataType.FLOAT -> arrays.forEach { bh.consume(transformFloatArray(it.floatArray)) }
            DataType.LONG -> arrays.forEach { bh.consume(transformLongArray(it.longArray)) }
            DataType.DOUBLE -> arrays.forEach { bh.consume(transformDoubleArray(it.doubleArray)) }
        }
    }

    /**
     * Loops through all the immutable arrays of the specified [dataType] and performs the associated operation
     * consuming each result with the [Blackhole].
     *
     * E.g. If the specified dataType is [DataType.BOOLEAN], then it loops through all the immutable-array wrappers
     * and calls [transformImmutableBooleanArray] on each [ImmutableArrayWrapperForDataType.immutableBooleanArray].
     */
    inline fun transformEachImmutableArrayOfDataType(
        bh: Blackhole,
        dataType: DataType,
        transformImmutableArray: (ImmutableArray<String>) -> Any?,
        transformImmutableBooleanArray: (ImmutableBooleanArray) -> Any?,
        transformImmutableByteArray: (ImmutableByteArray) -> Any?,
        transformImmutableCharArray: (ImmutableCharArray) -> Any?,
        transformImmutableShortArray: (ImmutableShortArray) -> Any?,
        transformImmutableIntArray: (ImmutableIntArray) -> Any?,
        transformImmutableFloatArray: (ImmutableFloatArray) -> Any?,
        transformImmutableLongArray: (ImmutableLongArray) -> Any?,
        transformImmutableDoubleArray: (ImmutableDoubleArray) -> Any?,
    ) {
        when (dataType) {
            DataType.REFERENCE -> immutableArrays.forEach { bh.consume(transformImmutableArray(it.immutableReferenceArray)) }
            DataType.BOOLEAN -> immutableArrays.forEach { bh.consume(transformImmutableBooleanArray(it.immutableBooleanArray)) }
            DataType.BYTE -> immutableArrays.forEach { bh.consume(transformImmutableByteArray(it.immutableByteArray)) }
            DataType.CHAR -> immutableArrays.forEach { bh.consume(transformImmutableCharArray(it.immutableCharArray)) }
            DataType.SHORT -> immutableArrays.forEach { bh.consume(transformImmutableShortArray(it.immutableShortArray)) }
            DataType.INT -> immutableArrays.forEach { bh.consume(transformImmutableIntArray(it.immutableIntArray)) }
            DataType.FLOAT -> immutableArrays.forEach { bh.consume(transformImmutableFloatArray(it.immutableFloatArray)) }
            DataType.LONG -> immutableArrays.forEach { bh.consume(transformImmutableLongArray(it.immutableLongArray)) }
            DataType.DOUBLE -> immutableArrays.forEach { bh.consume(transformImmutableDoubleArray(it.immutableDoubleArray)) }
        }
    }
}
