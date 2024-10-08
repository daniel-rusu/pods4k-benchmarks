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
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.FlatDataProducer
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
    init {
        // IMPORTANT: This init block is defined above the properties because the data producer needs to be notified
        // that a new collection is about to be created before the properties are initialized.
        dataProducer.startNewCollection(size)
    }

    val immutableReferenceArray: ImmutableArray<String> = when (dataType) {
        DataType.REFERENCE -> ImmutableArray(size) { dataProducer.nextReference(it, random) }
        else -> EMPTY_IMMUTABLE_ARRAY
    }

    val immutableBooleanArray: ImmutableBooleanArray = when (dataType) {
        DataType.BOOLEAN -> ImmutableBooleanArray(size) { dataProducer.nextBoolean(it, random) }
        else -> EMPTY_IMMUTABLE_BOOLEAN_ARRAY
    }

    val immutableByteArray: ImmutableByteArray = when (dataType) {
        DataType.BYTE -> ImmutableByteArray(size) { dataProducer.nextByte(it, random) }
        else -> EMPTY_IMMUTABLE_BYTE_ARRAY
    }

    val immutableCharArray: ImmutableCharArray = when (dataType) {
        DataType.CHAR -> ImmutableCharArray(size) { dataProducer.nextChar(it, random) }
        else -> EMPTY_IMMUTABLE_CHAR_ARRAY
    }

    val immutableShortArray: ImmutableShortArray = when (dataType) {
        DataType.SHORT -> ImmutableShortArray(size) { dataProducer.nextShort(it, random) }
        else -> EMPTY_IMMUTABLE_SHORT_ARRAY
    }

    val immutableIntArray: ImmutableIntArray = when (dataType) {
        DataType.INT -> ImmutableIntArray(size) { dataProducer.nextInt(it, random) }
        else -> EMPTY_IMMUTABLE_INT_ARRAY
    }

    val immutableFloatArray: ImmutableFloatArray = when (dataType) {
        DataType.FLOAT -> ImmutableFloatArray(size) { dataProducer.nextFloat(it, random) }
        else -> EMPTY_IMMUTABLE_FLOAT_ARRAY
    }

    val immutableLongArray: ImmutableLongArray = when (dataType) {
        DataType.LONG -> ImmutableLongArray(size) { dataProducer.nextLong(it, random) }
        else -> EMPTY_IMMUTABLE_LONG_ARRAY
    }

    val immutableDoubleArray: ImmutableDoubleArray = when (dataType) {
        DataType.DOUBLE -> ImmutableDoubleArray(size) { dataProducer.nextDouble(it, random) }
        else -> EMPTY_IMMUTABLE_DOUBLE_ARRAY
    }
}
