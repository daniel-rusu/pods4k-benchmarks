package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.toList
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
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
            expectThat(listData[0])
                .isA<ArrayList<String>>()
        }
        with(createData(CollectionType.PERSISTENT_LIST)) {
            expectThat(persistentListData[0])
                .isA<PersistentList<String>>()
        }
        with(createData(CollectionType.ARRAY)) {
            expectThat(arrayData[0])
                .isA<Array<String>>()
        }
        with(createData(CollectionType.IMMUTABLE_ARRAY)) {
            expectThat(immutableArrayData[0])
                .isA<ImmutableArray<String>>()
        }
    }

    @Test
    fun `all collection types contain identical data`() {
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
            listData.persistentListData
        }.message.isEqualTo("Requested PERSISTENT_LIST data, but the batch contains LIST data")

        expectThrows<IllegalStateException> {
            persistentListData.listData
        }.message.isEqualTo("Requested LIST data, but the batch contains PERSISTENT_LIST data")

        expectThrows<IllegalStateException> {
            arrayData.immutableArrayData
        }.message.isEqualTo("Requested IMMUTABLE_ARRAY data, but the batch contains ARRAY data")

        expectThrows<IllegalStateException> {
            immutableArrayData.arrayData
        }.message.isEqualTo("Requested ARRAY data, but the batch contains IMMUTABLE_ARRAY data")
    }

    @Test
    fun `requires a positive number of collections`() {
        expectThrows<IllegalArgumentException> {
            ObjectCollectionBenchmarkData.create(
                collectionType = CollectionType.LIST,
                numCollections = 0,
                sizeDistributionFactory = DistributionFactory.NestedListSizeDistribution,
                objectGeneratorFactory = ObjectGeneratorFactory.randomStrings(),
            )
        }.message.isEqualTo("numCollections must be positive")
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
            CollectionType.LIST -> listData.map { it.toList() }
            CollectionType.PERSISTENT_LIST -> persistentListData.map { it.toList() }
            CollectionType.ARRAY -> arrayData.map { it.toList() }
            CollectionType.IMMUTABLE_ARRAY -> immutableArrayData.map { it.toList() }
        }
    }
}
