package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

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
    createList: (Random, size: Int) -> List<String>,
    createBooleanList: (Random, size: Int) -> List<Boolean>,
    createByteList: (Random, size: Int) -> List<Byte>,
    createCharList: (Random, size: Int) -> List<Char>,
    createShortList: (Random, size: Int) -> List<Short>,
    createIntList: (Random, size: Int) -> List<Int>,
    createFloatList: (Random, size: Int) -> List<Float>,
    createLongList: (Random, size: Int) -> List<Long>,
    createDoubleList: (Random, size: Int) -> List<Double>,
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
        when (dataType) {
            DataType.REFERENCE -> {
                referenceList = createList(random, size)
                check(referenceList.size == size)
            }

            DataType.BOOLEAN -> {
                booleanList = createBooleanList(random, size)
                check(booleanList.size == size)
            }

            DataType.BYTE -> {
                byteList = createByteList(random, size)
                check(byteList.size == size)
            }

            DataType.CHAR -> {
                charList = createCharList(random, size)
                check(charList.size == size)
            }

            DataType.SHORT -> {
                shortList = createShortList(random, size)
                check(shortList.size == size)
            }

            DataType.INT -> {
                intList = createIntList(random, size)
                check(intList.size == size)
            }

            DataType.FLOAT -> {
                floatList = createFloatList(random, size)
                check(floatList.size == size)
            }

            DataType.LONG -> {
                longList = createLongList(random, size)
                check(longList.size == size)
            }

            DataType.DOUBLE -> {
                doubleList = createDoubleList(random, size)
                check(doubleList.size == size)
            }
        }
    }
}
