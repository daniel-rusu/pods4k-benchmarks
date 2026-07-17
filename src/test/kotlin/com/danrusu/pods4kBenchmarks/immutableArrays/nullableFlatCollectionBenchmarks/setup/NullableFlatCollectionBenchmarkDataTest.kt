package com.danrusu.pods4kBenchmarks.immutableArrays.nullableFlatCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.toList
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.nullable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.assertions.isEqualTo

private const val NULL_RATIO = 0.5

class NullableFlatCollectionBenchmarkDataTest {
    @Test
    fun `all collection types contain identical nullable data`() {
        DataType.entries.forEach { dataType ->
            val expected = createData(CollectionType.LIST, dataType).normalized(CollectionType.LIST, dataType)

            CollectionType.entries.forEach { collectionType ->
                val actual = createData(collectionType, dataType).normalized(collectionType, dataType)

                expectThat(actual)
                    .describedAs("$collectionType with nullable $dataType elements")
                    .isEqualTo(expected)
            }
        }
    }

    @Test
    fun `rejects access using the wrong element type`() {
        val data = createData(CollectionType.ARRAY, DataType.BOOLEAN)

        assertThrows<IllegalStateException> {
            data.arrayData<Int>()
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
            arrayData.immutableArrayData<Boolean>()
        }
        assertThrows<ClassCastException> {
            immutableArrayData.arrayData<Boolean>()
        }
    }

    @Test
    fun `requires a positive number of collections`() {
        assertThrows<IllegalArgumentException> {
            NullableFlatCollectionBenchmarkData.create(
                collectionType = CollectionType.LIST,
                dataType = DataType.INT,
                numCollections = 0,
                sizeDistributionFactory = DistributionFactory.NestedListSizeDistribution,
                fieldGeneratorFactory = FieldGeneratorFactory.withRandomNullableFields(NULL_RATIO),
                referenceGeneratorFactory = ObjectGeneratorFactory.randomStrings().nullable(NULL_RATIO),
            )
        }
    }

    private fun createData(
        collectionType: CollectionType,
        dataType: DataType,
    ): NullableFlatCollectionBenchmarkData = NullableFlatCollectionBenchmarkData.create(
        collectionType = collectionType,
        dataType = dataType,
        numCollections = 5,
        sizeDistributionFactory = DistributionFactory.ListSizeDistribution,
        fieldGeneratorFactory = FieldGeneratorFactory.withRandomNullableFields(NULL_RATIO),
        referenceGeneratorFactory = ObjectGeneratorFactory.randomStrings().nullable(NULL_RATIO),
    )

    private fun NullableFlatCollectionBenchmarkData.normalized(
        collectionType: CollectionType,
        dataType: DataType,
    ): List<List<Any?>> = when (dataType) {
        DataType.REFERENCE -> normalized<String>(collectionType)
        DataType.BOOLEAN -> normalized<Boolean>(collectionType)
        DataType.BYTE -> normalized<Byte>(collectionType)
        DataType.CHAR -> normalized<Char>(collectionType)
        DataType.SHORT -> normalized<Short>(collectionType)
        DataType.INT -> normalized<Int>(collectionType)
        DataType.FLOAT -> normalized<Float>(collectionType)
        DataType.LONG -> normalized<Long>(collectionType)
        DataType.DOUBLE -> normalized<Double>(collectionType)
    }

    private inline fun <reified T : Any> NullableFlatCollectionBenchmarkData.normalized(
        collectionType: CollectionType,
    ): List<List<Any?>> = when (collectionType) {
        CollectionType.LIST -> listData<T>().map { it.toList() }
        CollectionType.PERSISTENT_LIST -> persistentListData<T>().map { it.toList() }
        CollectionType.ARRAY -> arrayData<T>().map { it.toList() }
        CollectionType.IMMUTABLE_ARRAY -> immutableArrayData<T>().map { it.toList() }
    }
}
