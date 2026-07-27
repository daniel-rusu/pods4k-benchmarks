package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
import kotlinx.collections.immutable.PersistentList
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class CollectionFactoryTest {
    @Test
    fun `class for LIST collectionType is resolved correctly`() {
        for (dataType in DataType.entries) {
            expectThat(
                CollectionFactory.resolveCollectionClass(
                    collectionType = CollectionType.LIST,
                    dataType = dataType,
                    referenceElementClass = String::class.java,
                )
            ).isEqualTo(List::class.java)
        }
    }

    @Test
    fun `class for PERSISTENT_LIST collectionType is resolved correctly`() {
        for (dataType in DataType.entries) {
            expectThat(
                CollectionFactory.resolveCollectionClass(
                    collectionType = CollectionType.PERSISTENT_LIST,
                    dataType = dataType,
                    referenceElementClass = String::class.java,
                )
            ).isEqualTo(PersistentList::class.java)
        }
    }

    @Test
    fun `class for ARRAY collectionType is resolved correctly`() {
        for (dataType in DataType.entries) {
            expectThat(
                CollectionFactory.resolveCollectionClass(
                    collectionType = CollectionType.ARRAY,
                    dataType = dataType,
                    referenceElementClass = String::class.java,
                )
            ).isEqualTo(
                when (dataType) {
                    DataType.REFERENCE -> String::class.java.arrayType()
                    DataType.BOOLEAN -> BooleanArray::class.java
                    DataType.BYTE -> ByteArray::class.java
                    DataType.CHAR -> CharArray::class.java
                    DataType.SHORT -> ShortArray::class.java
                    DataType.INT -> IntArray::class.java
                    DataType.FLOAT -> FloatArray::class.java
                    DataType.LONG -> LongArray::class.java
                    DataType.DOUBLE -> DoubleArray::class.java
                }
            )
        }
    }

    @Test
    fun `class for IMMUTABLE_ARRAY collectionType is resolved correctly`() {
        for (dataType in DataType.entries) {
            expectThat(
                CollectionFactory.resolveCollectionClass(
                    collectionType = CollectionType.IMMUTABLE_ARRAY,
                    dataType = dataType,
                    referenceElementClass = String::class.java,
                )
            ).isEqualTo(
                when (dataType) {
                    DataType.REFERENCE -> ImmutableArray::class.java
                    DataType.BOOLEAN -> ImmutableBooleanArray::class.java
                    DataType.BYTE -> ImmutableByteArray::class.java
                    DataType.CHAR -> ImmutableCharArray::class.java
                    DataType.SHORT -> ImmutableShortArray::class.java
                    DataType.INT -> ImmutableIntArray::class.java
                    DataType.FLOAT -> ImmutableFloatArray::class.java
                    DataType.LONG -> ImmutableLongArray::class.java
                    DataType.DOUBLE -> ImmutableDoubleArray::class.java
                }
            )
        }
    }
}
