package com.danrusu.pods4kBenchmarks.immutableArrays.nullableFlatCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.toList
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.nullable
import kotlinx.collections.immutable.PersistentList
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.message

private const val NULL_RATIO = 0.5

class NullableFlatCollectionBenchmarkDataTest {
    @Test
    fun `all collection types are mapped to appropriate classes`() {
        with(createData(CollectionType.LIST, DataType.BOOLEAN)) {
            val lists = lists<Boolean>()
            // A List[] component type prevents benchmark loops from needing a per-collection cast to List.
            expectThat(lists.javaClass.componentType)
                .isEqualTo(List::class.java)
            expectThat(lists[0])
                .isA<List<Boolean?>>()
        }
        with(createData(CollectionType.PERSISTENT_LIST, DataType.BOOLEAN)) {
            expectThat(persistentLists<Boolean>()[0])
                .isA<PersistentList<Boolean?>>()
        }
        with(createData(CollectionType.ARRAY, DataType.BOOLEAN)) {
            expectThat(arrays<Boolean>()[0])
                .isA<Array<Boolean?>>()
        }
        with(createData(CollectionType.IMMUTABLE_ARRAY, DataType.BOOLEAN)) {
            expectThat(immutableArrays<Boolean>()[0])
                .isA<ImmutableArray<Boolean?>>()
        }
    }

    @Test
    fun `all collection representations contain identical nullable data`() {
        for (dataType in DataType.entries) {
            val expected = createData(CollectionType.LIST, dataType).normalized(CollectionType.LIST, dataType)

            for (collectionType in CollectionType.entries) {
                val actual = createData(collectionType, dataType).normalized(collectionType, dataType)

                expectThat(actual)
                    .describedAs("$collectionType with nullable $dataType elements")
                    .isEqualTo(expected)
            }
        }
    }

    @Test
    fun `all data types have nulls in identical locations`() {
        val expectedNullability = createData(CollectionType.LIST, DataType.REFERENCE)
            .normalized<String>(CollectionType.LIST)
            .map { collection -> collection.map { it == null } }

        expectThat(expectedNullability.flatten().count { it })
            .describedAs("number of null elements in the reference data")
            .isGreaterThan(0)

        for (dataType in DataType.entries) {
            val actualNullability = createData(CollectionType.LIST, dataType)
                .normalized(CollectionType.LIST, dataType)
                .map { collection -> collection.map { it == null } }

            expectThat(actualNullability)
                .describedAs("null locations for $dataType elements")
                .isEqualTo(expectedNullability)
        }
    }

    @Test
    fun `rejects access using the wrong element type`() {
        val data = createData(CollectionType.ARRAY, DataType.BOOLEAN)

        expectThrows<IllegalStateException> {
            data.arrays<Int>()
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
            arrayData.immutableArrays<Boolean>()
        }.message.isEqualTo("Requested IMMUTABLE_ARRAY data, but the batch contains ARRAY data")

        expectThrows<IllegalStateException> {
            immutableArrayData.arrays<Boolean>()
        }.message.isEqualTo("Requested ARRAY data, but the batch contains IMMUTABLE_ARRAY data")
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
        CollectionType.LIST -> lists<T>().map { it.toList() }
        CollectionType.PERSISTENT_LIST -> persistentLists<T>().map { it.toList() }
        CollectionType.ARRAY -> arrays<T>().map { it.toList() }
        CollectionType.IMMUTABLE_ARRAY -> immutableArrays<T>().map { it.toList() }
    }
}
