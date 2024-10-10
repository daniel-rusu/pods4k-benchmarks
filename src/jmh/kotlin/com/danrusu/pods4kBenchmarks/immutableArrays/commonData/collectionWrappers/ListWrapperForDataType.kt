package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.FlatDataProducer
import kotlin.random.Random

private val EMPTY_LIST: List<Nothing> = emptyList()

/**
 * Creates and stores a single list of the specified data type by using the appropriate provided factory.
 *
 * For example, if the data type is [DataType.BOOLEAN], then the createBooleanList(random, size) factory will be
 * called.  All the other list variables will be empty.
 *
 * IMPORTANT:
 * This class needs to have the same general structure as [ArrayWrapperForDataType] and
 * [ImmutableArrayWrapperForDataType] so that they have the same memory footprint since they're compared against each
 * other in benchmarks that measure the performance of the underlying collections.
 */
class ListWrapperForDataType(
    val size: Int,
    random: Random,
    dataType: DataType,
    dataProducer: FlatDataProducer,
) : CollectionWrapperForDataType() {
    init {
        // IMPORTANT: This init block is defined above the properties because the data producer needs to be notified
        // that a new collection is about to be created before the properties are initialized.
        dataProducer.startNewCollection(size)
    }

    override val referenceList: List<String> = when (dataType) {
        DataType.REFERENCE -> (0..<size).map { dataProducer.nextReference(it, random) }
        else -> EMPTY_LIST
    }
    override val booleanList: List<Boolean> = when (dataType) {
        DataType.BOOLEAN -> (0..<size).map { dataProducer.nextBoolean(it, random) }
        else -> EMPTY_LIST
    }
    override val byteList: List<Byte> = when (dataType) {
        DataType.BYTE -> (0..<size).map { dataProducer.nextByte(it, random) }
        else -> EMPTY_LIST
    }
    override val charList: List<Char> = when (dataType) {
        DataType.CHAR -> (0..<size).map { dataProducer.nextChar(it, random) }
        else -> EMPTY_LIST
    }
    override val shortList: List<Short> = when (dataType) {
        DataType.SHORT -> (0..<size).map { dataProducer.nextShort(it, random) }
        else -> EMPTY_LIST
    }
    override val intList: List<Int> = when (dataType) {
        DataType.INT -> (0..<size).map { dataProducer.nextInt(it, random) }
        else -> EMPTY_LIST
    }
    override val floatList: List<Float> = when (dataType) {
        DataType.FLOAT -> (0..<size).map { dataProducer.nextFloat(it, random) }
        else -> EMPTY_LIST
    }
    override val longList: List<Long> = when (dataType) {
        DataType.LONG -> (0..<size).map { dataProducer.nextLong(it, random) }
        else -> EMPTY_LIST
    }
    override val doubleList: List<Double> = when (dataType) {
        DataType.DOUBLE -> (0..<size).map { dataProducer.nextDouble(it, random) }
        else -> EMPTY_LIST
    }
}
