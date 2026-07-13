package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.emptyImmutableArray
import com.danrusu.pods4k.immutableArrays.toList
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ObjectCollectionBenchmarkDataTest {
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
    fun `requires exactly one populated backing array`() {
        assertThrows<IllegalArgumentException> {
            ObjectCollectionBenchmarkData<String>()
        }

        assertThrows<IllegalArgumentException> {
            ObjectCollectionBenchmarkData<String>(
                listData = arrayOf(emptyList()),
                immutableArrayData = arrayOf(emptyImmutableArray()),
            )
        }
    }

    @Test
    fun `requires a positive number of collections`() {
        assertThrows<IllegalArgumentException> {
            ObjectCollectionBenchmarkData.create(
                collectionType = CollectionType.LIST,
                numCollections = 0,
                sizeDistributionFactory = DistributionFactory.NestedListSizeDistribution,
                objectGeneratorFactory = ObjectGeneratorFactory.randomStrings(),
            )
        }
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
