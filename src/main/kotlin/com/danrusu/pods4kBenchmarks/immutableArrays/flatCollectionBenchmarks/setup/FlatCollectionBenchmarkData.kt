package com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkGeneratorRngs
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionFactory.createList
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionFactory.createPersistentList
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList

/**
 * The strongly typed backing collections for one flat benchmark parameter combination.
 *
 * Only the property selected by the collection and data types passed to [create] is populated. Keeping the primitive
 * arrays strongly typed avoids introducing boxing or unchecked casts into benchmark execution.
 */
class FlatCollectionBenchmarkData internal constructor(
    val listData: Array<List<Any>> = emptyArray(),
    val persistentListData: Array<PersistentList<Any>> = emptyArray(),
    val referenceArrays: Array<Array<String>> = emptyArray(),
    val booleanArrays: Array<BooleanArray> = emptyArray(),
    val byteArrays: Array<ByteArray> = emptyArray(),
    val charArrays: Array<CharArray> = emptyArray(),
    val shortArrays: Array<ShortArray> = emptyArray(),
    val intArrays: Array<IntArray> = emptyArray(),
    val floatArrays: Array<FloatArray> = emptyArray(),
    val longArrays: Array<LongArray> = emptyArray(),
    val doubleArrays: Array<DoubleArray> = emptyArray(),
    val immutableReferenceArrays: Array<ImmutableArray<String>> = emptyArray(),
    val immutableBooleanArrays: Array<ImmutableBooleanArray> = emptyArray(),
    val immutableByteArrays: Array<ImmutableByteArray> = emptyArray(),
    val immutableCharArrays: Array<ImmutableCharArray> = emptyArray(),
    val immutableShortArrays: Array<ImmutableShortArray> = emptyArray(),
    val immutableIntArrays: Array<ImmutableIntArray> = emptyArray(),
    val immutableFloatArrays: Array<ImmutableFloatArray> = emptyArray(),
    val immutableLongArrays: Array<ImmutableLongArray> = emptyArray(),
    val immutableDoubleArrays: Array<ImmutableDoubleArray> = emptyArray(),
) {
    init {
        val populatedArrayCount = listOf(
            listData,
            persistentListData,
            referenceArrays,
            booleanArrays,
            byteArrays,
            charArrays,
            shortArrays,
            intArrays,
            floatArrays,
            longArrays,
            doubleArrays,
            immutableReferenceArrays,
            immutableBooleanArrays,
            immutableByteArrays,
            immutableCharArrays,
            immutableShortArrays,
            immutableIntArrays,
            immutableFloatArrays,
            immutableLongArrays,
            immutableDoubleArrays,
        ).count { it.isNotEmpty() }

        require(populatedArrayCount == 1) {
            "Exactly one backing array must be populated (found $populatedArrayCount)"
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> typedListData(): Array<List<T>> = listData as Array<List<T>>

    @Suppress("UNCHECKED_CAST")
    fun <T> typedPersistentListData(): Array<PersistentList<T>> {
        return persistentListData as Array<PersistentList<T>>
    }

    companion object {
        /** Creates deterministic data for one flat benchmark parameter combination. */
        fun create(
            collectionType: CollectionType,
            dataType: DataType,
            numCollections: Int,
            sizeDistributionFactory: DistributionFactory,
            fieldGeneratorFactory: FieldGeneratorFactory,
            referenceGeneratorFactory: ObjectGeneratorFactory<String>,
        ): FlatCollectionBenchmarkData {
            require(numCollections > 0) { "numCollections must be positive" }

            val rngFactory = RngFactory()
            val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
            val sizeDistribution = sizeDistributionFactory.create(rngFactory)
            val fields = fieldGeneratorFactory.create(generatorRngs)
            val references = referenceGeneratorFactory.create(generatorRngs)

            return when (collectionType) {
                CollectionType.LIST -> createLists(numCollections, dataType, sizeDistribution, fields, references)
                CollectionType.PERSISTENT_LIST ->
                    createPersistentLists(numCollections, dataType, sizeDistribution, fields, references)
                CollectionType.ARRAY -> createArrays(numCollections, dataType, sizeDistribution, fields, references)
                CollectionType.IMMUTABLE_ARRAY ->
                    createImmutableArrays(numCollections, dataType, sizeDistribution, fields, references)
            }
        }

        private fun createLists(
            numCollections: Int,
            dataType: DataType,
            sizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): FlatCollectionBenchmarkData = FlatCollectionBenchmarkData(
            listData = Array(numCollections) {
                when (dataType) {
                    DataType.REFERENCE -> createList(sizeDistribution.nextValue()) { references.next() }
                    DataType.BOOLEAN -> createList(sizeDistribution.nextValue()) { fields.nextBoolean() }
                    DataType.BYTE -> createList(sizeDistribution.nextValue()) { fields.nextByte() }
                    DataType.CHAR -> createList(sizeDistribution.nextValue()) { fields.nextChar() }
                    DataType.SHORT -> createList(sizeDistribution.nextValue()) { fields.nextShort() }
                    DataType.INT -> createList(sizeDistribution.nextValue()) { fields.nextInt() }
                    DataType.FLOAT -> createList(sizeDistribution.nextValue()) { fields.nextFloat() }
                    DataType.LONG -> createList(sizeDistribution.nextValue()) { fields.nextLong() }
                    DataType.DOUBLE -> createList(sizeDistribution.nextValue()) { fields.nextDouble() }
                }
            },
        )

        private fun createPersistentLists(
            numCollections: Int,
            dataType: DataType,
            sizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): FlatCollectionBenchmarkData = FlatCollectionBenchmarkData(
            persistentListData = Array(numCollections) {
                when (dataType) {
                    DataType.REFERENCE -> createPersistentList(sizeDistribution.nextValue()) { references.next() }
                    DataType.BOOLEAN -> createPersistentList(sizeDistribution.nextValue()) { fields.nextBoolean() }
                    DataType.BYTE -> createPersistentList(sizeDistribution.nextValue()) { fields.nextByte() }
                    DataType.CHAR -> createPersistentList(sizeDistribution.nextValue()) { fields.nextChar() }
                    DataType.SHORT -> createPersistentList(sizeDistribution.nextValue()) { fields.nextShort() }
                    DataType.INT -> createPersistentList(sizeDistribution.nextValue()) { fields.nextInt() }
                    DataType.FLOAT -> createPersistentList(sizeDistribution.nextValue()) { fields.nextFloat() }
                    DataType.LONG -> createPersistentList(sizeDistribution.nextValue()) { fields.nextLong() }
                    DataType.DOUBLE -> createPersistentList(sizeDistribution.nextValue()) { fields.nextDouble() }
                }
            },
        )

        private fun createArrays(
            numCollections: Int,
            dataType: DataType,
            sizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): FlatCollectionBenchmarkData = when (dataType) {
            DataType.REFERENCE -> FlatCollectionBenchmarkData(
                referenceArrays = Array(numCollections) {
                    Array(sizeDistribution.nextValue()) { references.next() }
                },
            )
            DataType.BOOLEAN -> FlatCollectionBenchmarkData(
                booleanArrays = Array(numCollections) {
                    BooleanArray(sizeDistribution.nextValue()) { fields.nextBoolean() }
                },
            )
            DataType.BYTE -> FlatCollectionBenchmarkData(
                byteArrays = Array(numCollections) { ByteArray(sizeDistribution.nextValue()) { fields.nextByte() } },
            )
            DataType.CHAR -> FlatCollectionBenchmarkData(
                charArrays = Array(numCollections) { CharArray(sizeDistribution.nextValue()) { fields.nextChar() } },
            )
            DataType.SHORT -> FlatCollectionBenchmarkData(
                shortArrays = Array(numCollections) { ShortArray(sizeDistribution.nextValue()) { fields.nextShort() } },
            )
            DataType.INT -> FlatCollectionBenchmarkData(
                intArrays = Array(numCollections) { IntArray(sizeDistribution.nextValue()) { fields.nextInt() } },
            )
            DataType.FLOAT -> FlatCollectionBenchmarkData(
                floatArrays = Array(numCollections) { FloatArray(sizeDistribution.nextValue()) { fields.nextFloat() } },
            )
            DataType.LONG -> FlatCollectionBenchmarkData(
                longArrays = Array(numCollections) { LongArray(sizeDistribution.nextValue()) { fields.nextLong() } },
            )
            DataType.DOUBLE -> FlatCollectionBenchmarkData(
                doubleArrays = Array(numCollections) { DoubleArray(sizeDistribution.nextValue()) { fields.nextDouble() } },
            )
        }

        private fun createImmutableArrays(
            numCollections: Int,
            dataType: DataType,
            sizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): FlatCollectionBenchmarkData = when (dataType) {
            DataType.REFERENCE -> FlatCollectionBenchmarkData(
                immutableReferenceArrays = Array(numCollections) {
                    ImmutableArray(sizeDistribution.nextValue()) { references.next() }
                },
            )
            DataType.BOOLEAN -> FlatCollectionBenchmarkData(
                immutableBooleanArrays = Array(numCollections) {
                    ImmutableBooleanArray(sizeDistribution.nextValue()) { fields.nextBoolean() }
                },
            )
            DataType.BYTE -> FlatCollectionBenchmarkData(
                immutableByteArrays = Array(numCollections) {
                    ImmutableByteArray(sizeDistribution.nextValue()) { fields.nextByte() }
                },
            )
            DataType.CHAR -> FlatCollectionBenchmarkData(
                immutableCharArrays = Array(numCollections) {
                    ImmutableCharArray(sizeDistribution.nextValue()) { fields.nextChar() }
                },
            )
            DataType.SHORT -> FlatCollectionBenchmarkData(
                immutableShortArrays = Array(numCollections) {
                    ImmutableShortArray(sizeDistribution.nextValue()) { fields.nextShort() }
                },
            )
            DataType.INT -> FlatCollectionBenchmarkData(
                immutableIntArrays = Array(numCollections) {
                    ImmutableIntArray(sizeDistribution.nextValue()) { fields.nextInt() }
                },
            )
            DataType.FLOAT -> FlatCollectionBenchmarkData(
                immutableFloatArrays = Array(numCollections) {
                    ImmutableFloatArray(sizeDistribution.nextValue()) { fields.nextFloat() }
                },
            )
            DataType.LONG -> FlatCollectionBenchmarkData(
                immutableLongArrays = Array(numCollections) {
                    ImmutableLongArray(sizeDistribution.nextValue()) { fields.nextLong() }
                },
            )
            DataType.DOUBLE -> FlatCollectionBenchmarkData(
                immutableDoubleArrays = Array(numCollections) {
                    ImmutableDoubleArray(sizeDistribution.nextValue()) { fields.nextDouble() }
                },
            )
        }
    }
}
