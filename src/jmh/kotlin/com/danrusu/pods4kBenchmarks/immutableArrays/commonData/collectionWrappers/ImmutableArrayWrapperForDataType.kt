package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableByteArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableCharArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableIntArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableLongArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableShortArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import kotlin.random.Random

private val EMPTY_IMMUTABLE_ARRAY: ImmutableArray<Nothing> = emptyImmutableArray()
private val EMPTY_IMMUTABLE_BOOLEAN_ARRAY: ImmutableBooleanArray = emptyImmutableBooleanArray()
private val EMPTY_IMMUTABLE_BYTE_ARRAY: ImmutableByteArray = emptyImmutableByteArray()
private val EMPTY_IMMUTABLE_CHAR_ARRAY: ImmutableCharArray = emptyImmutableCharArray()
private val EMPTY_IMMUTABLE_SHORT_ARRAY: ImmutableShortArray = emptyImmutableShortArray()
private val EMPTY_IMMUTABLE_INT_ARRAY: ImmutableIntArray = emptyImmutableIntArray()
private val EMPTY_IMMUTABLE_FLOAT_ARRAY: ImmutableFloatArray = emptyImmutableFloatArray()
private val EMPTY_IMMUTABLE_LONG_ARRAY: ImmutableLongArray = emptyImmutableLongArray()
private val EMPTY_IMMUTABLE_DOUBLE_ARRAY: ImmutableDoubleArray = emptyImmutableDoubleArray()

/**
 * Creates and stores a single immutable array of the specified data type by using the appropriate provided factory.
 *
 * For example, if the data type is [DataType.BOOLEAN], then the createImmutableBooleanArray(random, size) factory will
 * be called.  All the other immutable array variables will be empty.
 *
 * IMPORTANT:
 * This class needs to have the same general structure as [ListWrapperForDataType] and [ArrayWrapperForDataType] so
 * that they have the same memory footprint since they're compared against each other in benchmarks that measure the
 * performance of the underlying collections.
 */
class ImmutableArrayWrapperForDataType(
    val size: Int,
    random: Random,
    dataType: DataType,
    createImmutableArray: (Random, size: Int) -> ImmutableArray<String>,
    createImmutableBooleanArray: (Random, size: Int) -> ImmutableBooleanArray,
    createImmutableByteArray: (Random, size: Int) -> ImmutableByteArray,
    createImmutableCharArray: (Random, size: Int) -> ImmutableCharArray,
    createImmutableShortArray: (Random, size: Int) -> ImmutableShortArray,
    createImmutableIntArray: (Random, size: Int) -> ImmutableIntArray,
    createImmutableFloatArray: (Random, size: Int) -> ImmutableFloatArray,
    createImmutableLongArray: (Random, size: Int) -> ImmutableLongArray,
    createImmutableDoubleArray: (Random, size: Int) -> ImmutableDoubleArray,
) {
    var immutableReferenceArray: ImmutableArray<String> = EMPTY_IMMUTABLE_ARRAY
        private set

    var immutableBooleanArray: ImmutableBooleanArray = EMPTY_IMMUTABLE_BOOLEAN_ARRAY
        private set

    var immutableByteArray: ImmutableByteArray = EMPTY_IMMUTABLE_BYTE_ARRAY
        private set

    var immutableCharArray: ImmutableCharArray = EMPTY_IMMUTABLE_CHAR_ARRAY
        private set

    var immutableShortArray: ImmutableShortArray = EMPTY_IMMUTABLE_SHORT_ARRAY
        private set

    var immutableIntArray: ImmutableIntArray = EMPTY_IMMUTABLE_INT_ARRAY
        private set

    var immutableFloatArray: ImmutableFloatArray = EMPTY_IMMUTABLE_FLOAT_ARRAY
        private set

    var immutableLongArray: ImmutableLongArray = EMPTY_IMMUTABLE_LONG_ARRAY
        private set

    var immutableDoubleArray: ImmutableDoubleArray = EMPTY_IMMUTABLE_DOUBLE_ARRAY
        private set

    init {
        when (dataType) {
            DataType.REFERENCE -> {
                immutableReferenceArray = createImmutableArray(random, size)
                check(immutableReferenceArray.size == size)
            }

            DataType.BOOLEAN -> {
                immutableBooleanArray = createImmutableBooleanArray(random, size)
                check(immutableReferenceArray.size == size)
            }

            DataType.BYTE -> {
                immutableByteArray = createImmutableByteArray(random, size)
                check(immutableReferenceArray.size == size)
            }

            DataType.CHAR -> {
                immutableCharArray = createImmutableCharArray(random, size)
                check(immutableReferenceArray.size == size)
            }

            DataType.SHORT -> {
                immutableShortArray = createImmutableShortArray(random, size)
                check(immutableReferenceArray.size == size)
            }

            DataType.INT -> {
                immutableIntArray = createImmutableIntArray(random, size)
                check(immutableReferenceArray.size == size)
            }

            DataType.FLOAT -> {
                immutableFloatArray = createImmutableFloatArray(random, size)
                check(immutableReferenceArray.size == size)
            }

            DataType.LONG -> {
                immutableLongArray = createImmutableLongArray(random, size)
                check(immutableReferenceArray.size == size)
            }

            DataType.DOUBLE -> {
                immutableDoubleArray = createImmutableDoubleArray(random, size)
                check(immutableReferenceArray.size == size)
            }
        }
    }
}
