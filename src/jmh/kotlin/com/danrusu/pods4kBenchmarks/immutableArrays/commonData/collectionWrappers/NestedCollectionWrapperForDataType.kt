package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.toImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.FlatDataProducer
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.utils.Distribution
import kotlin.random.Random

/**
 * Creates and stores each of the 3 types of collections (list, array, & immutable array) with each storing wrappers
 * for their respective type.  The wrappers will themselves store a collection with size controlled by the
 * nested-collection-size-distribution.
 *
 * Note that the wrapper layer is on purpose in order to more closely model the real world where we don't have a list
 * of lists directly but rather a list of objects which themselves contain lists (eg. list of Person objects and each
 * person has a list of friends).
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
class NestedCollectionWrapperForDataType(
    size: Int,
    random: Random,
    dataType: DataType,
    nestedCollectionSizeDistribution: Distribution,
    dataProducer: FlatDataProducer,
) {
    val array: Array<ArrayWrapperForDataType> = Array(size) {
        dataProducer.startNewCollection(size)
        ArrayWrapperForDataType(
            size = nestedCollectionSizeDistribution.nextValue(random),
            random = random,
            dataType = dataType,
            dataProducer = dataProducer,
        )
    }

    // copy the data from the regular array so that they are tested against identical data
    val list: List<ListWrapperForDataType> = array.map { arrayWrapper ->
        ListWrapperForDataType(
            size = arrayWrapper.size,
            random = random,
            dataType = dataType,
            dataProducer = arrayWrapper.copyData(),
        )
    }

    // copy the data from the regular array so that they are tested against identical data
    val immutableArray: ImmutableArray<ImmutableArrayWrapperForDataType> = array.map { arrayWrapper ->
        ImmutableArrayWrapperForDataType(
            size = arrayWrapper.size,
            random = random,
            dataType = dataType,
            dataProducer = arrayWrapper.copyData(),
        )
    }.toImmutableArray()
}
