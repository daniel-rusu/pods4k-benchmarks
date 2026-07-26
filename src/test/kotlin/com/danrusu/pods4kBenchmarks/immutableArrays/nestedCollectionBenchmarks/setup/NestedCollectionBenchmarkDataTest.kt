package com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.toList
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.message

class NestedCollectionBenchmarkDataTest {
    @Test
    fun `all collection types are mapped to appropriate classes`() {
        with(createData(CollectionType.LIST, DataType.BOOLEAN)) {
            val lists = listData<Boolean>()
            // A List[] component type prevents benchmark loops from needing a per-collection cast to List.
            expectThat(lists.javaClass.componentType)
                .isEqualTo(List::class.java)

            val topLevelCollection = lists[0]
            expectThat(topLevelCollection)
                .isA<List<CollectionOwner<List<Boolean>>>>()

            val nestedCollection = topLevelCollection[0].nestedCollection
            expectThat(nestedCollection)
                .isA<List<Boolean>>()
        }
        with(createData(CollectionType.PERSISTENT_LIST, DataType.BOOLEAN)) {
            val topLevelCollection = persistentListData<Boolean>()[0]
            expectThat(topLevelCollection)
                .isA<PersistentList<CollectionOwner<PersistentList<Boolean>>>>()

            val nestedCollection = topLevelCollection[0].nestedCollection
            expectThat(nestedCollection)
                .isA<PersistentList<Boolean>>()
        }
        with(createData(CollectionType.ARRAY, DataType.BOOLEAN)) {
            val topLevelCollection = booleanArrayData[0]
            expectThat(topLevelCollection)
                .isA<Array<CollectionOwner<BooleanArray>>>()

            val nestedCollection = topLevelCollection[0].nestedCollection
            expectThat(nestedCollection)
                .isA<BooleanArray>()
        }
        with(createData(CollectionType.IMMUTABLE_ARRAY, DataType.BOOLEAN)) {
            val topLevelCollection = immutableBooleanArrayData[0]
            expectThat(topLevelCollection)
                .isA<ImmutableArray<CollectionOwner<ImmutableBooleanArray>>>()

            val nestedCollection = topLevelCollection[0].nestedCollection
            expectThat(nestedCollection)
                .isA<ImmutableBooleanArray>()
        }
    }

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

        expectThrows<IllegalStateException> {
            data.listData<Int>()
        }.message.isEqualTo(
            "Requested logical element class java.lang.Integer, but the batch contains java.lang.Boolean"
        )
    }

    @Test
    fun `rejects access using the wrong collection type in either direction`() {
        val listData = createData(CollectionType.LIST, DataType.BOOLEAN)
        val persistentListData = createData(CollectionType.PERSISTENT_LIST, DataType.BOOLEAN)
        val arrayData = createData(CollectionType.ARRAY, DataType.BOOLEAN)
        val immutableArrayData = createData(CollectionType.IMMUTABLE_ARRAY, DataType.BOOLEAN)

        expectThrows<IllegalStateException> {
            listData.persistentListData<Boolean>()
        }.message.isEqualTo("Requested PERSISTENT_LIST data, but the batch contains LIST data")

        expectThrows<IllegalStateException> {
            persistentListData.listData<Boolean>()
        }.message.isEqualTo("Requested LIST data, but the batch contains PERSISTENT_LIST data")

        expectThrows<IllegalStateException> {
            arrayData.immutableBooleanArrayData
        }.message.isEqualTo("Requested IMMUTABLE_ARRAY data, but the batch contains ARRAY data")

        expectThrows<IllegalStateException> {
            immutableArrayData.booleanArrayData
        }.message.isEqualTo("Requested ARRAY data, but the batch contains IMMUTABLE_ARRAY data")
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
