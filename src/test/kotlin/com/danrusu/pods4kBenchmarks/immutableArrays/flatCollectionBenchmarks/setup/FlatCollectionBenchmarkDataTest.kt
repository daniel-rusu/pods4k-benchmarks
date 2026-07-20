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
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class FlatCollectionBenchmarkDataTest {
    @Test
    fun `all collection types are mapped to appropriate classes`() {
        with(createData(CollectionType.LIST, DataType.BOOLEAN)) {
            expectThat(listData<Boolean>()[0])
                .isA<ArrayList<Boolean>>()
        }
        with(createData(CollectionType.PERSISTENT_LIST, DataType.BOOLEAN)) {
            expectThat(persistentListData<Boolean>()[0])
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
            arrayData.immutableBooleanArrays
        }

        assertThrows<ClassCastException> {
            immutableArrayData.booleanArrays
        }
    }

    @Test
    fun `requires a positive number of collections`() {
        assertThrows<IllegalArgumentException> {
            FlatCollectionBenchmarkData.create(
                collectionType = CollectionType.LIST,
                dataType = DataType.INT,
                numCollections = 0,
                sizeDistributionFactory = DistributionFactory.NestedListSizeDistribution,
                fieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
                referenceGeneratorFactory = ObjectGeneratorFactory.randomStrings(),
            )
        }
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
            DataType.REFERENCE -> listData<String>().map { it.toList() }
            DataType.BOOLEAN -> listData<Boolean>().map { it.toList() }
            DataType.BYTE -> listData<Byte>().map { it.toList() }
            DataType.CHAR -> listData<Char>().map { it.toList() }
            DataType.SHORT -> listData<Short>().map { it.toList() }
            DataType.INT -> listData<Int>().map { it.toList() }
            DataType.FLOAT -> listData<Float>().map { it.toList() }
            DataType.LONG -> listData<Long>().map { it.toList() }
            DataType.DOUBLE -> listData<Double>().map { it.toList() }
        }

        CollectionType.PERSISTENT_LIST -> when (dataType) {
            DataType.REFERENCE -> persistentListData<String>().map { it.toList() }
            DataType.BOOLEAN -> persistentListData<Boolean>().map { it.toList() }
            DataType.BYTE -> persistentListData<Byte>().map { it.toList() }
            DataType.CHAR -> persistentListData<Char>().map { it.toList() }
            DataType.SHORT -> persistentListData<Short>().map { it.toList() }
            DataType.INT -> persistentListData<Int>().map { it.toList() }
            DataType.FLOAT -> persistentListData<Float>().map { it.toList() }
            DataType.LONG -> persistentListData<Long>().map { it.toList() }
            DataType.DOUBLE -> persistentListData<Double>().map { it.toList() }
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
