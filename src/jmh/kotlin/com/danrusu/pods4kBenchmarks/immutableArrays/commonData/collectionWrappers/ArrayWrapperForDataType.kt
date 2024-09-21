package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import kotlin.random.Random

private val EMPTY_ARRAY: Array<Any> = emptyArray()
private val EMPTY_BOOLEAN_ARRAY: BooleanArray = booleanArrayOf()
private val EMPTY_BYTE_ARRAY: ByteArray = byteArrayOf()
private val EMPTY_CHAR_ARRAY: CharArray = charArrayOf()
private val EMPTY_SHORT_ARRAY: ShortArray = shortArrayOf()
private val EMPTY_INT_ARRAY: IntArray = intArrayOf()
private val EMPTY_FLOAT_ARRAY: FloatArray = floatArrayOf()
private val EMPTY_LONG_ARRAY: LongArray = longArrayOf()
private val EMPTY_DOUBLE_ARRAY: DoubleArray = doubleArrayOf()

/**
 * Creates and stores a single array of the specified data type by using the appropriate provided factory.
 *
 * For example, if the data type is [DataType.BOOLEAN], then the createBooleanArray(random, size) factory will be
 * called.  All the other array variables will be empty.
 *
 * IMPORTANT:
 * This class needs to have the same general structure as [ListWrapperForDataType] and
 * [ImmutableArrayWrapperForDataType] so that they have the same memory footprint since they're compared against each
 * other in benchmarks that measure the performance of the underlying collections.
 */
@Suppress("UNCHECKED_CAST")
class ArrayWrapperForDataType<T>(
    val size: Int,
    random: Random,
    dataType: DataType,
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
    var referenceArray: Array<T> = EMPTY_ARRAY as Array<T>
        private set

    var booleanArray: BooleanArray = EMPTY_BOOLEAN_ARRAY
        private set

    var byteArray: ByteArray = EMPTY_BYTE_ARRAY
        private set

    var charArray: CharArray = EMPTY_CHAR_ARRAY
        private set

    var shortArray: ShortArray = EMPTY_SHORT_ARRAY
        private set

    var intArray: IntArray = EMPTY_INT_ARRAY
        private set

    var floatArray: FloatArray = EMPTY_FLOAT_ARRAY
        private set

    var longArray: LongArray = EMPTY_LONG_ARRAY
        private set

    var doubleArray: DoubleArray = EMPTY_DOUBLE_ARRAY
        private set

    init {
        when (dataType) {
            DataType.REFERENCE -> {
                referenceArray = createArray(random, size)
                check(referenceArray.size == size)
            }

            DataType.BOOLEAN -> {
                booleanArray = createBooleanArray(random, size)
                check(booleanArray.size == size)
            }

            DataType.BYTE -> {
                byteArray = createByteArray(random, size)
                check(byteArray.size == size)
            }

            DataType.CHAR -> {
                charArray = createCharArray(random, size)
                check(charArray.size == size)
            }

            DataType.SHORT -> {
                shortArray = createShortArray(random, size)
                check(shortArray.size == size)
            }

            DataType.INT -> {
                intArray = createIntArray(random, size)
                check(intArray.size == size)
            }

            DataType.FLOAT -> {
                floatArray = createFloatArray(random, size)
                check(floatArray.size == size)
            }

            DataType.LONG -> {
                longArray = createLongArray(random, size)
                check(longArray.size == size)
            }

            DataType.DOUBLE -> {
                doubleArray = createDoubleArray(random, size)
                check(doubleArray.size == size)
            }
        }
    }
}
