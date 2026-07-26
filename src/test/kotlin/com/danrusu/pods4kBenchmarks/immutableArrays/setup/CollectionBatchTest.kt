package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.percent
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class CollectionBatchTest {
    @Test
    fun `returns collections using the requested representation and element type`() {
        val collections: Array<List<Int>> = arrayOf(listOf(1, 2), listOf(3))
        val batch = createBatch(collections)

        val result =
            batch.getCollections<List<Int>>(
                expectedCollectionType = CollectionType.LIST,
                expectedLogicalElementClass = Int::class.javaObjectType,
            )

        expectThat(result.asList()).isEqualTo(collections.asList())
        expectThat(result.javaClass.componentType).isEqualTo(List::class.java)
    }

    @Test
    fun `rejects the wrong collection representation with a descriptive error`() {
        val batch = createBatch(arrayOf(listOf(1)))

        expectThrows<IllegalStateException> {
            batch.getCollections<List<Int>>(
                expectedCollectionType = CollectionType.PERSISTENT_LIST,
                expectedLogicalElementClass = Int::class.javaObjectType,
            )
        }.message.isEqualTo("Requested PERSISTENT_LIST data, but the batch contains LIST data")
    }

    @Test
    fun `rejects the wrong element type with a descriptive error`() {
        val batch = createBatch(arrayOf(listOf(1)))

        expectThrows<IllegalStateException> {
            batch.getCollections<List<Long>>(
                expectedCollectionType = CollectionType.LIST,
                expectedLogicalElementClass = Long::class.javaObjectType,
            )
        }.message.isEqualTo(
            "Requested logical element class java.lang.Long, but the batch contains java.lang.Integer"
        )
    }

    @Test
    fun `requires a positive number of collections`() {
        expectThrows<IllegalArgumentException> {
            CollectionBatch.create(
                collectionType = CollectionType.LIST,
                logicalElementClass = Int::class.javaObjectType,
                collectionClass = List::class.java,
                numCollections = 0,
                sizeDistribution = fixedSizeDistribution(),
            ) {
                emptyList<Int>()
            }
        }.message.isEqualTo("numCollections must be positive")
    }

    private fun createBatch(collections: Array<List<Int>>): CollectionBatch {
        var index = 0
        return CollectionBatch.create(
            collectionType = CollectionType.LIST,
            logicalElementClass = Int::class.javaObjectType,
            collectionClass = List::class.java,
            numCollections = collections.size,
            sizeDistribution = fixedSizeDistribution(),
        ) {
            collections[index++]
        }
    }

    private fun fixedSizeDistribution(): Distribution {
        return Distribution(
            RngFactory(),
            100.percent inRange 1..1,
        )
    }
}
