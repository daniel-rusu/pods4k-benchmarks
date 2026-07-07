package com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableArray
import com.danrusu.pods4k.immutableArrays.toImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ImmutableArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.PersistentListWrapper
import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

/**
 * Creates and stores the active collection type with wrappers for its respective type.
 * The wrappers will themselves store a collection with size controlled by the nested-collection-size-distribution.
 *
 * Note that the wrapper layer is on purpose in order to more closely model the real world where we don't have a list
 * of lists directly but rather a list of objects which themselves contain lists (eg. list of Person objects and each
 * person has a list of friends).
 */
class NestedCollectionWrapper(
    numNestedCollections: Int,
    nestedSizeDistribution: Distribution,
    collectionType: CollectionType,
    dataType: DataType,
    fields: FieldGenerator,
    references: ObjectGenerator<String>,
) {
    val array: Array<out ArrayWrapper> = when (collectionType) {
        ARRAY -> ArrayWrapper.createWrappers(numNestedCollections, nestedSizeDistribution, dataType, fields, references)
        else -> emptyArray()
    }

    val list: List<ListWrapper> = when (collectionType) {
        LIST -> ListWrapper.createWrappers(
            count = numNestedCollections,
            sizeDistribution = nestedSizeDistribution,
            dataType = dataType,
            fields = fields,
            references = references,
        ).toList()

        else -> emptyList()
    }

    val persistentList: PersistentList<PersistentListWrapper> = when (collectionType) {
        PERSISTENT_LIST -> PersistentListWrapper.createWrappers(
            count = numNestedCollections,
            sizeDistribution = nestedSizeDistribution,
            dataType = dataType,
            fields = fields,
            references = references,
        ).toPersistentList()

        else -> persistentListOf()
    }

    val immutableArray: ImmutableArray<ImmutableArrayWrapper> = when (collectionType) {
        IMMUTABLE_ARRAY -> ImmutableArrayWrapper.createWrappers(
            count = numNestedCollections,
            sizeDistribution = nestedSizeDistribution,
            dataType = dataType,
            fields = fields,
            references = references,
        ).toImmutableArray()

        else -> emptyImmutableArray()
    }
}
