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
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.FlatDataProducer
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
    dataProducer: FlatDataProducer,
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
        dataProducer.startNewCollection(size)
        when (dataType) {
            DataType.REFERENCE -> {
                immutableReferenceArray = ImmutableArray(size) { dataProducer.nextReference(it, random) }
            }

            DataType.BOOLEAN -> {
                immutableBooleanArray = ImmutableBooleanArray(size) { dataProducer.nextBoolean(it, random) }
            }

            DataType.BYTE -> {
                immutableByteArray = ImmutableByteArray(size) { dataProducer.nextByte(it, random) }
            }

            DataType.CHAR -> {
                immutableCharArray = ImmutableCharArray(size) { dataProducer.nextChar(it, random) }
            }

            DataType.SHORT -> {
                immutableShortArray = ImmutableShortArray(size) { dataProducer.nextShort(it, random) }
            }

            DataType.INT -> {
                immutableIntArray = ImmutableIntArray(size) { dataProducer.nextInt(it, random) }
            }

            DataType.FLOAT -> {
                immutableFloatArray = ImmutableFloatArray(size) { dataProducer.nextFloat(it, random) }
            }

            DataType.LONG -> {
                immutableLongArray = ImmutableLongArray(size) { dataProducer.nextLong(it, random) }
            }

            DataType.DOUBLE -> {
                immutableDoubleArray = ImmutableDoubleArray(size) { dataProducer.nextDouble(it, random) }
            }
        }
    }
}
