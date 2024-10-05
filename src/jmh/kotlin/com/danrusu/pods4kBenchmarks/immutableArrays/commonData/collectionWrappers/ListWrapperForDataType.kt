package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.DataProducer
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
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
    dataProducer: DataProducer,
) {
    var referenceList: List<String> = EMPTY_LIST
        private set

    var booleanList: List<Boolean> = EMPTY_LIST
        private set

    var byteList: List<Byte> = EMPTY_LIST
        private set

    var charList: List<Char> = EMPTY_LIST
        private set

    var shortList: List<Short> = EMPTY_LIST
        private set

    var intList: List<Int> = EMPTY_LIST
        private set

    var floatList: List<Float> = EMPTY_LIST
        private set

    var longList: List<Long> = EMPTY_LIST
        private set

    var doubleList: List<Double> = EMPTY_LIST
        private set

    init {
        dataProducer.startNewCollection(size)
        when (dataType) {
            DataType.REFERENCE -> {
                referenceList = (0..<size).map { dataProducer.nextReference(it, random) }
            }

            DataType.BOOLEAN -> {
                booleanList = (0..<size).map { dataProducer.nextBoolean(it, random) }
            }

            DataType.BYTE -> {
                byteList = (0..<size).map { dataProducer.nextByte(it, random) }
            }

            DataType.CHAR -> {
                charList = (0..<size).map { dataProducer.nextChar(it, random) }
            }

            DataType.SHORT -> {
                shortList = (0..<size).map { dataProducer.nextShort(it, random) }
            }

            DataType.INT -> {
                intList = (0..<size).map { dataProducer.nextInt(it, random) }
            }

            DataType.FLOAT -> {
                floatList = (0..<size).map { dataProducer.nextFloat(it, random) }
            }

            DataType.LONG -> {
                longList = (0..<size).map { dataProducer.nextLong(it, random) }
            }

            DataType.DOUBLE -> {
                doubleList = (0..<size).map { dataProducer.nextDouble(it, random) }
            }
        }
    }
}
