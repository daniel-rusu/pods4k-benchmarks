package com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks.setup

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

class FlatCollectionBenchmarkDataTest {
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
            FlatCollectionBenchmarkData()
        }

        assertThrows<IllegalArgumentException> {
            FlatCollectionBenchmarkData(
                booleanArrays = arrayOf(booleanArrayOf()),
                intArrays = arrayOf(intArrayOf()),
            )
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
        CollectionType.LIST -> listData.map { it.toList() }
        CollectionType.PERSISTENT_LIST -> persistentListData.map { it.toList() }
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
