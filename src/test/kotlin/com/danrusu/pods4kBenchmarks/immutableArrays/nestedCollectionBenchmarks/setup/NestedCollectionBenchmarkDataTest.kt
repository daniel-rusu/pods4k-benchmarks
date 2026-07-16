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
    fun `rejects access using the wrong element type`() {
        val data = createData(CollectionType.LIST, DataType.BOOLEAN)

        assertThrows<IllegalStateException> {
            data.listData<Int>()
        }
    }

    @Test
    fun `rejects access using the wrong collection type in either direction`() {
        val listData = createData(CollectionType.LIST, DataType.BOOLEAN)
        val persistentListData = createData(CollectionType.PERSISTENT_LIST, DataType.BOOLEAN)
        val arrayData = createData(CollectionType.ARRAY, DataType.BOOLEAN)
        val immutableArrayData = createData(CollectionType.IMMUTABLE_ARRAY, DataType.BOOLEAN)

        assertThrows<ClassCastException> {
            listData.persistentListData<Boolean>()
        }

        assertThrows<ClassCastException> {
            persistentListData.listData<Boolean>()
        }

        assertThrows<ClassCastException> {
            arrayData.immutableBooleanArrayData
        }

        assertThrows<ClassCastException> {
            immutableArrayData.booleanArrayData
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
        CollectionType.LIST -> when (dataType) {
            DataType.REFERENCE -> listData<String>().normalize()
            DataType.BOOLEAN -> listData<Boolean>().normalize()
            DataType.BYTE -> listData<Byte>().normalize()
            DataType.CHAR -> listData<Char>().normalize()
            DataType.SHORT -> listData<Short>().normalize()
            DataType.INT -> listData<Int>().normalize()
            DataType.FLOAT -> listData<Float>().normalize()
            DataType.LONG -> listData<Long>().normalize()
            DataType.DOUBLE -> listData<Double>().normalize()
        }

        CollectionType.PERSISTENT_LIST -> when (dataType) {
            DataType.REFERENCE -> persistentListData<String>().normalize()
            DataType.BOOLEAN -> persistentListData<Boolean>().normalize()
            DataType.BYTE -> persistentListData<Byte>().normalize()
            DataType.CHAR -> persistentListData<Char>().normalize()
            DataType.SHORT -> persistentListData<Short>().normalize()
            DataType.INT -> persistentListData<Int>().normalize()
            DataType.FLOAT -> persistentListData<Float>().normalize()
            DataType.LONG -> persistentListData<Long>().normalize()
            DataType.DOUBLE -> persistentListData<Double>().normalize()
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

    private fun <T> Array<out List<CollectionOwner<List<T>>>>.normalize(): List<List<List<T>>> {
        return map { collection -> collection.map { it.nestedCollection.toList() } }
    }
}
