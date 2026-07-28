package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.toList
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.message

class ObjectCollectionBenchmarkDataTest {
    @Test
    fun `all collection types are mapped to appropriate classes`() {
        with(createData(CollectionType.LIST)) {
            // A List[] component type prevents benchmark loops from needing a per-collection cast to List.
            expectThat(lists.javaClass.componentType)
                .isEqualTo(List::class.java)
            expectThat(lists[0])
                .isA<List<String>>()
        }
        with(createData(CollectionType.PERSISTENT_LIST)) {
            expectThat(persistentLists[0])
                .isA<PersistentList<String>>()
        }
        with(createData(CollectionType.ARRAY)) {
            expectThat(arrays[0])
                .isA<Array<String>>()
        }
        with(createData(CollectionType.IMMUTABLE_ARRAY)) {
            expectThat(immutableArrays[0])
                .isA<ImmutableArray<String>>()
        }
    }

    @Test
    fun `all collection representations contain identical data`() {
        val expected = createData(CollectionType.LIST).normalized(CollectionType.LIST)

        CollectionType.entries.forEach { collectionType ->
            val actual = createData(collectionType).normalized(collectionType)

            expectThat(actual)
                .describedAs("$collectionType")
                .isEqualTo(expected)
        }
    }

    @Test
    fun `rejects access using the wrong collection type in either direction`() {
        val listData = createData(CollectionType.LIST)
        val persistentListData = createData(CollectionType.PERSISTENT_LIST)
        val arrayData = createData(CollectionType.ARRAY)
        val immutableArrayData = createData(CollectionType.IMMUTABLE_ARRAY)

        expectThrows<IllegalStateException> {
            listData.persistentLists
        }.message.isEqualTo("Requested PERSISTENT_LIST data, but the batch contains LIST data")

        expectThrows<IllegalStateException> {
            persistentListData.lists
        }.message.isEqualTo("Requested LIST data, but the batch contains PERSISTENT_LIST data")

        expectThrows<IllegalStateException> {
            arrayData.immutableArrays
        }.message.isEqualTo("Requested IMMUTABLE_ARRAY data, but the batch contains ARRAY data")

        expectThrows<IllegalStateException> {
            immutableArrayData.arrays
        }.message.isEqualTo("Requested ARRAY data, but the batch contains IMMUTABLE_ARRAY data")
    }

    private fun createData(collectionType: CollectionType): ObjectCollectionBenchmarkData<String> {
        return ObjectCollectionBenchmarkData.create(
            collectionType = collectionType,
            numCollections = 5,
            sizeDistributionFactory = DistributionFactory.ListSizeDistribution,
            objectGeneratorFactory = ObjectGeneratorFactory.randomStrings(),
        )
    }

    private fun ObjectCollectionBenchmarkData<String>.normalized(collectionType: CollectionType): List<List<String>> {
        return when (collectionType) {
            CollectionType.LIST -> lists.map { it.toList() }
            CollectionType.PERSISTENT_LIST -> persistentLists.map { it.toList() }
            CollectionType.ARRAY -> arrays.map { it.toList() }
            CollectionType.IMMUTABLE_ARRAY -> immutableArrays.map { it.toList() }
        }
    }
}
