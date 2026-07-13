package com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.toList
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class NestedCollectionBenchmarkDataTest {
    @Test
    fun `all collection types contain identical data`() {
        DataType.entries.forEach { dataType ->
            val expected = createData(CollectionType.LIST, dataType).normalized(CollectionType.LIST, dataType)

            CollectionType.entries.forEach { collectionType ->
                val actual = createData(collectionType, dataType).normalized(collectionType, dataType)

                expectThat(actual)
                    .describedAs("$collectionType with $dataType elements")
                    .isEqualTo(expected)
            }
        }
    }

    @Test
    fun `requires exactly one populated backing array`() {
        assertThrows<IllegalArgumentException> {
            NestedCollectionBenchmarkData()
        }

        assertThrows<IllegalArgumentException> {
            NestedCollectionBenchmarkData(
                booleanArrayData = arrayOf(emptyArray()),
                intArrayData = arrayOf(emptyArray()),
            )
        }
    }

    @Test
    fun `requires a positive number of collections`() {
        assertThrows<IllegalArgumentException> {
            NestedCollectionBenchmarkData.create(
                collectionType = CollectionType.LIST,
                dataType = DataType.INT,
                numCollections = 0,
                topLevelSizeDistributionFactory = DistributionFactory.ListSizeDistribution,
                nestedCollectionSizeDistributionFactory = DistributionFactory.NestedListSizeDistribution,
                nestedFieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
                nestedReferenceGeneratorFactory = ObjectGeneratorFactory.randomStrings(),
            )
        }
    }

    private fun createData(
        collectionType: CollectionType,
        dataType: DataType,
    ): NestedCollectionBenchmarkData = NestedCollectionBenchmarkData.create(
        collectionType = collectionType,
        dataType = dataType,
        numCollections = 5,
        topLevelSizeDistributionFactory = DistributionFactory.ListSizeDistribution,
        nestedCollectionSizeDistributionFactory = DistributionFactory.NestedListSizeDistribution,
        nestedFieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
        nestedReferenceGeneratorFactory = ObjectGeneratorFactory.randomStrings(),
    )

    private fun NestedCollectionBenchmarkData.normalized(
        collectionType: CollectionType,
        dataType: DataType,
    ): List<List<List<Any>>> = when (collectionType) {
        CollectionType.LIST -> listData.map { collection ->
            collection.map { it.nestedCollection.toList() }
        }
        CollectionType.PERSISTENT_LIST -> persistentListData.map { collection ->
            collection.map { it.nestedCollection.toList() }
        }
        CollectionType.ARRAY -> when (dataType) {
            DataType.REFERENCE -> referenceArrayData.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }
            DataType.BOOLEAN -> booleanArrayData.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }
            DataType.BYTE -> byteArrayData.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }
            DataType.CHAR -> charArrayData.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }
            DataType.SHORT -> shortArrayData.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }
            DataType.INT -> intArrayData.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }
            DataType.FLOAT -> floatArrayData.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }
            DataType.LONG -> longArrayData.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }
            DataType.DOUBLE -> doubleArrayData.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }
        }

        CollectionType.IMMUTABLE_ARRAY -> when (dataType) {
            DataType.REFERENCE -> immutableReferenceArrayData.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }
            DataType.BOOLEAN -> immutableBooleanArrayData.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }
            DataType.BYTE -> immutableByteArrayData.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }
            DataType.CHAR -> immutableCharArrayData.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }
            DataType.SHORT -> immutableShortArrayData.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }
            DataType.INT -> immutableIntArrayData.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }
            DataType.FLOAT -> immutableFloatArrayData.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }
            DataType.LONG -> immutableLongArrayData.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }
            DataType.DOUBLE -> immutableDoubleArrayData.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }
        }
    }
}
