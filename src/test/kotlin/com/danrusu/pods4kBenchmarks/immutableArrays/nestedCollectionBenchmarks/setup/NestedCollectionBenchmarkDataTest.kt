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
            val lists = lists<Boolean>()
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
            val topLevelCollection = persistentLists<Boolean>()[0]
            expectThat(topLevelCollection)
                .isA<PersistentList<CollectionOwner<PersistentList<Boolean>>>>()

            val nestedCollection = topLevelCollection[0].nestedCollection
            expectThat(nestedCollection)
                .isA<PersistentList<Boolean>>()
        }
        with(createData(CollectionType.ARRAY, DataType.BOOLEAN)) {
            val topLevelCollection = booleanArrays[0]
            expectThat(topLevelCollection)
                .isA<Array<CollectionOwner<BooleanArray>>>()

            val nestedCollection = topLevelCollection[0].nestedCollection
            expectThat(nestedCollection)
                .isA<BooleanArray>()
        }
        with(createData(CollectionType.IMMUTABLE_ARRAY, DataType.BOOLEAN)) {
            val topLevelCollection = immutableBooleanArrays[0]
            expectThat(topLevelCollection)
                .isA<ImmutableArray<CollectionOwner<ImmutableBooleanArray>>>()

            val nestedCollection = topLevelCollection[0].nestedCollection
            expectThat(nestedCollection)
                .isA<ImmutableBooleanArray>()
        }
    }

    @Test
    fun `all collection representations contain identical data`() {
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
            data.lists<Int>()
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
            listData.persistentLists<Boolean>()
        }.message.isEqualTo("Requested PERSISTENT_LIST data, but the batch contains LIST data")

        expectThrows<IllegalStateException> {
            persistentListData.lists<Boolean>()
        }.message.isEqualTo("Requested LIST data, but the batch contains PERSISTENT_LIST data")

        expectThrows<IllegalStateException> {
            arrayData.immutableBooleanArrays
        }.message.isEqualTo("Requested IMMUTABLE_ARRAY data, but the batch contains ARRAY data")

        expectThrows<IllegalStateException> {
            immutableArrayData.booleanArrays
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
            DataType.REFERENCE -> lists<String>().normalize()
            DataType.BOOLEAN -> lists<Boolean>().normalize()
            DataType.BYTE -> lists<Byte>().normalize()
            DataType.CHAR -> lists<Char>().normalize()
            DataType.SHORT -> lists<Short>().normalize()
            DataType.INT -> lists<Int>().normalize()
            DataType.FLOAT -> lists<Float>().normalize()
            DataType.LONG -> lists<Long>().normalize()
            DataType.DOUBLE -> lists<Double>().normalize()
        }

        CollectionType.PERSISTENT_LIST -> when (dataType) {
            DataType.REFERENCE -> persistentLists<String>().normalize()
            DataType.BOOLEAN -> persistentLists<Boolean>().normalize()
            DataType.BYTE -> persistentLists<Byte>().normalize()
            DataType.CHAR -> persistentLists<Char>().normalize()
            DataType.SHORT -> persistentLists<Short>().normalize()
            DataType.INT -> persistentLists<Int>().normalize()
            DataType.FLOAT -> persistentLists<Float>().normalize()
            DataType.LONG -> persistentLists<Long>().normalize()
            DataType.DOUBLE -> persistentLists<Double>().normalize()
        }

        CollectionType.ARRAY -> when (dataType) {
            DataType.REFERENCE -> referenceArrays.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }

            DataType.BOOLEAN -> booleanArrays.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }

            DataType.BYTE -> byteArrays.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }

            DataType.CHAR -> charArrays.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }

            DataType.SHORT -> shortArrays.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }

            DataType.INT -> intArrays.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }

            DataType.FLOAT -> floatArrays.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }

            DataType.LONG -> longArrays.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }

            DataType.DOUBLE -> doubleArrays.map { collection ->
                collection.map { it.nestedCollection.toList() }
            }
        }

        CollectionType.IMMUTABLE_ARRAY -> when (dataType) {
            DataType.REFERENCE -> immutableReferenceArrays.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }

            DataType.BOOLEAN -> immutableBooleanArrays.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }

            DataType.BYTE -> immutableByteArrays.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }

            DataType.CHAR -> immutableCharArrays.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }

            DataType.SHORT -> immutableShortArrays.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }

            DataType.INT -> immutableIntArrays.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }

            DataType.FLOAT -> immutableFloatArrays.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }

            DataType.LONG -> immutableLongArrays.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }

            DataType.DOUBLE -> immutableDoubleArrays.map { collection ->
                collection.toList().map { it.nestedCollection.toList() }
            }
        }
    }

    private fun <T> Array<out List<CollectionOwner<List<T>>>>.normalize(): List<List<List<T>>> {
        return map { collection -> collection.map { it.nestedCollection.toList() } }
    }
}
