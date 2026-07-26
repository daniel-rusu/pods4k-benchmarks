package com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks.setup

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

class FlatCollectionBenchmarkDataTest {
    @Test
    fun `all collection types are mapped to appropriate classes`() {
        with(createData(CollectionType.LIST, DataType.BOOLEAN)) {
            val lists = lists<Boolean>()
            // A List[] component type prevents benchmark loops from needing a per-collection cast to List.
            expectThat(lists.javaClass.componentType)
                .isEqualTo(List::class.java)
            expectThat(lists[0])
                .isA<List<Boolean>>()
        }
        with(createData(CollectionType.PERSISTENT_LIST, DataType.BOOLEAN)) {
            expectThat(persistentLists<Boolean>()[0])
                .isA<PersistentList<Boolean>>()
        }
        with(createData(CollectionType.ARRAY, DataType.BOOLEAN)) {
            expectThat(booleanArrays[0])
                .isA<BooleanArray>()
        }
        with(createData(CollectionType.IMMUTABLE_ARRAY, DataType.BOOLEAN)) {
            expectThat(immutableBooleanArrays[0])
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
    ): FlatCollectionBenchmarkData = FlatCollectionBenchmarkData.create(
        collectionType = collectionType,
        dataType = dataType,
        numCollections = 5,
        sizeDistributionFactory = DistributionFactory.ListSizeDistribution,
        fieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
        referenceGeneratorFactory = ObjectGeneratorFactory.randomStrings(),
    )

    private fun FlatCollectionBenchmarkData.normalized(
        collectionType: CollectionType,
        dataType: DataType,
    ): List<List<Any>> = when (collectionType) {
        CollectionType.LIST -> when (dataType) {
            DataType.REFERENCE -> lists<String>().map { it.toList() }
            DataType.BOOLEAN -> lists<Boolean>().map { it.toList() }
            DataType.BYTE -> lists<Byte>().map { it.toList() }
            DataType.CHAR -> lists<Char>().map { it.toList() }
            DataType.SHORT -> lists<Short>().map { it.toList() }
            DataType.INT -> lists<Int>().map { it.toList() }
            DataType.FLOAT -> lists<Float>().map { it.toList() }
            DataType.LONG -> lists<Long>().map { it.toList() }
            DataType.DOUBLE -> lists<Double>().map { it.toList() }
        }

        CollectionType.PERSISTENT_LIST -> when (dataType) {
            DataType.REFERENCE -> persistentLists<String>().map { it.toList() }
            DataType.BOOLEAN -> persistentLists<Boolean>().map { it.toList() }
            DataType.BYTE -> persistentLists<Byte>().map { it.toList() }
            DataType.CHAR -> persistentLists<Char>().map { it.toList() }
            DataType.SHORT -> persistentLists<Short>().map { it.toList() }
            DataType.INT -> persistentLists<Int>().map { it.toList() }
            DataType.FLOAT -> persistentLists<Float>().map { it.toList() }
            DataType.LONG -> persistentLists<Long>().map { it.toList() }
            DataType.DOUBLE -> persistentLists<Double>().map { it.toList() }
        }

        CollectionType.ARRAY -> when (dataType) {
            DataType.REFERENCE -> referenceArrays.map { it.toList() }
            DataType.BOOLEAN -> booleanArrays.map { it.toList() }
            DataType.BYTE -> byteArrays.map { it.toList() }
            DataType.CHAR -> charArrays.map { it.toList() }
            DataType.SHORT -> shortArrays.map { it.toList() }
            DataType.INT -> intArrays.map { it.toList() }
            DataType.FLOAT -> floatArrays.map { it.toList() }
            DataType.LONG -> longArrays.map { it.toList() }
            DataType.DOUBLE -> doubleArrays.map { it.toList() }
        }

        CollectionType.IMMUTABLE_ARRAY -> when (dataType) {
            DataType.REFERENCE -> immutableReferenceArrays.map { it.toList() }
            DataType.BOOLEAN -> immutableBooleanArrays.map { it.toList() }
            DataType.BYTE -> immutableByteArrays.map { it.toList() }
            DataType.CHAR -> immutableCharArrays.map { it.toList() }
            DataType.SHORT -> immutableShortArrays.map { it.toList() }
            DataType.INT -> immutableIntArrays.map { it.toList() }
            DataType.FLOAT -> immutableFloatArrays.map { it.toList() }
            DataType.LONG -> immutableLongArrays.map { it.toList() }
            DataType.DOUBLE -> immutableDoubleArrays.map { it.toList() }
        }
    }
}
