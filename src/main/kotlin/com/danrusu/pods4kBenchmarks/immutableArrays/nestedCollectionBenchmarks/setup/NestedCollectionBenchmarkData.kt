package com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks.setup

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
 * The strongly typed backing collections for one nested benchmark parameter combination.
 *
 * Only the property selected by the collection and data types passed to [create] is populated. Keeping the primitive
 * arrays strongly typed avoids introducing boxing or unchecked casts into benchmark execution.
 */
class NestedCollectionBenchmarkData internal constructor(
    val listData: Array<List<CollectionOwner<List<Any>>>> = emptyArray(),
    val persistentListData: Array<PersistentList<CollectionOwner<PersistentList<Any>>>> = emptyArray(),
    val referenceArrayData: Array<Array<CollectionOwner<Array<String>>>> = emptyArray(),
    val booleanArrayData: Array<Array<CollectionOwner<BooleanArray>>> = emptyArray(),
    val byteArrayData: Array<Array<CollectionOwner<ByteArray>>> = emptyArray(),
    val charArrayData: Array<Array<CollectionOwner<CharArray>>> = emptyArray(),
    val shortArrayData: Array<Array<CollectionOwner<ShortArray>>> = emptyArray(),
    val intArrayData: Array<Array<CollectionOwner<IntArray>>> = emptyArray(),
    val floatArrayData: Array<Array<CollectionOwner<FloatArray>>> = emptyArray(),
    val longArrayData: Array<Array<CollectionOwner<LongArray>>> = emptyArray(),
    val doubleArrayData: Array<Array<CollectionOwner<DoubleArray>>> = emptyArray(),
    val immutableReferenceArrayData: Array<ImmutableArray<CollectionOwner<ImmutableArray<String>>>> = emptyArray(),
    val immutableBooleanArrayData: Array<ImmutableArray<CollectionOwner<ImmutableBooleanArray>>> = emptyArray(),
    val immutableByteArrayData: Array<ImmutableArray<CollectionOwner<ImmutableByteArray>>> = emptyArray(),
    val immutableCharArrayData: Array<ImmutableArray<CollectionOwner<ImmutableCharArray>>> = emptyArray(),
    val immutableShortArrayData: Array<ImmutableArray<CollectionOwner<ImmutableShortArray>>> = emptyArray(),
    val immutableIntArrayData: Array<ImmutableArray<CollectionOwner<ImmutableIntArray>>> = emptyArray(),
    val immutableFloatArrayData: Array<ImmutableArray<CollectionOwner<ImmutableFloatArray>>> = emptyArray(),
    val immutableLongArrayData: Array<ImmutableArray<CollectionOwner<ImmutableLongArray>>> = emptyArray(),
    val immutableDoubleArrayData: Array<ImmutableArray<CollectionOwner<ImmutableDoubleArray>>> = emptyArray(),
) {
    init {
        val populatedArrayCount = listOf(
            listData,
            persistentListData,
            referenceArrayData,
            booleanArrayData,
            byteArrayData,
            charArrayData,
            shortArrayData,
            intArrayData,
            floatArrayData,
            longArrayData,
            doubleArrayData,
            immutableReferenceArrayData,
            immutableBooleanArrayData,
            immutableByteArrayData,
            immutableCharArrayData,
            immutableShortArrayData,
            immutableIntArrayData,
            immutableFloatArrayData,
            immutableLongArrayData,
            immutableDoubleArrayData,
        ).count { it.isNotEmpty() }

        require(populatedArrayCount == 1) {
            "Exactly one backing array must be populated (found $populatedArrayCount)"
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> typedListData(): Array<List<CollectionOwner<List<T>>>> {
        return listData as Array<List<CollectionOwner<List<T>>>>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> typedPersistentListData(): Array<PersistentList<CollectionOwner<PersistentList<T>>>> {
        return persistentListData as Array<PersistentList<CollectionOwner<PersistentList<T>>>>
    }

    companion object {
        /** Creates deterministic data for one nested benchmark parameter combination. */
        fun create(
            collectionType: CollectionType,
            dataType: DataType,
            numCollections: Int,
            topLevelSizeDistributionFactory: DistributionFactory,
            nestedCollectionSizeDistributionFactory: DistributionFactory,
            nestedFieldGeneratorFactory: FieldGeneratorFactory,
            nestedReferenceGeneratorFactory: ObjectGeneratorFactory<String>,
        ): NestedCollectionBenchmarkData {
            require(numCollections > 0) { "numCollections must be positive" }

            val rngFactory = RngFactory()
            val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
            val topLevelSizeDistribution = topLevelSizeDistributionFactory.create(rngFactory)
            val nestedSizeDistribution = nestedCollectionSizeDistributionFactory.create(rngFactory)
            val fields = nestedFieldGeneratorFactory.create(generatorRngs)
            val references = nestedReferenceGeneratorFactory.create(generatorRngs)

            return when (collectionType) {
                CollectionType.LIST -> createLists(
                    numCollections,
                    dataType,
                    topLevelSizeDistribution,
                    nestedSizeDistribution,
                    fields,
                    references,
                )
                CollectionType.PERSISTENT_LIST -> createPersistentLists(
                    numCollections,
                    dataType,
                    topLevelSizeDistribution,
                    nestedSizeDistribution,
                    fields,
                    references,
                )
                CollectionType.ARRAY -> createArrays(
                    numCollections,
                    dataType,
                    topLevelSizeDistribution,
                    nestedSizeDistribution,
                    fields,
                    references,
                )
                CollectionType.IMMUTABLE_ARRAY -> createImmutableArrays(
                    numCollections,
                    dataType,
                    topLevelSizeDistribution,
                    nestedSizeDistribution,
                    fields,
                    references,
                )
            }
        }

        private fun createLists(
            numCollections: Int,
            dataType: DataType,
            topLevelSizeDistribution: Distribution,
            nestedSizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): NestedCollectionBenchmarkData = NestedCollectionBenchmarkData(
            listData = Array(numCollections) {
                createList(topLevelSizeDistribution.nextValue()) {
                    when (dataType) {
                        DataType.REFERENCE ->
                            CollectionOwner(createList(nestedSizeDistribution.nextValue()) { references.next() })
                        DataType.BOOLEAN ->
                            CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextBoolean() })
                        DataType.BYTE ->
                            CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextByte() })
                        DataType.CHAR ->
                            CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextChar() })
                        DataType.SHORT ->
                            CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextShort() })
                        DataType.INT ->
                            CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextInt() })
                        DataType.FLOAT ->
                            CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextFloat() })
                        DataType.LONG ->
                            CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextLong() })
                        DataType.DOUBLE ->
                            CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextDouble() })
                    }
                }
            },
        )

        private fun createPersistentLists(
            numCollections: Int,
            dataType: DataType,
            topLevelSizeDistribution: Distribution,
            nestedSizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): NestedCollectionBenchmarkData = NestedCollectionBenchmarkData(
            persistentListData = Array(numCollections) {
                createPersistentList(topLevelSizeDistribution.nextValue()) {
                    when (dataType) {
                        DataType.REFERENCE -> CollectionOwner(
                            createPersistentList(nestedSizeDistribution.nextValue()) { references.next() }
                        )
                        DataType.BOOLEAN -> CollectionOwner(
                            createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextBoolean() }
                        )
                        DataType.BYTE -> CollectionOwner(
                            createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextByte() }
                        )
                        DataType.CHAR -> CollectionOwner(
                            createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextChar() }
                        )
                        DataType.SHORT -> CollectionOwner(
                            createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextShort() }
                        )
                        DataType.INT -> CollectionOwner(
                            createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextInt() }
                        )
                        DataType.FLOAT -> CollectionOwner(
                            createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextFloat() }
                        )
                        DataType.LONG -> CollectionOwner(
                            createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextLong() }
                        )
                        DataType.DOUBLE -> CollectionOwner(
                            createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextDouble() }
                        )
                    }
                }
            },
        )

        private fun createArrays(
            numCollections: Int,
            dataType: DataType,
            topLevelSizeDistribution: Distribution,
            nestedSizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): NestedCollectionBenchmarkData = when (dataType) {
            DataType.REFERENCE -> NestedCollectionBenchmarkData(
                referenceArrayData = Array(numCollections) {
                    Array(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(Array(nestedSizeDistribution.nextValue()) { references.next() })
                    }
                },
            )
            DataType.BOOLEAN -> NestedCollectionBenchmarkData(
                booleanArrayData = Array(numCollections) {
                    Array(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(BooleanArray(nestedSizeDistribution.nextValue()) { fields.nextBoolean() })
                    }
                },
            )
            DataType.BYTE -> NestedCollectionBenchmarkData(
                byteArrayData = Array(numCollections) {
                    Array(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(ByteArray(nestedSizeDistribution.nextValue()) { fields.nextByte() })
                    }
                },
            )
            DataType.CHAR -> NestedCollectionBenchmarkData(
                charArrayData = Array(numCollections) {
                    Array(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(CharArray(nestedSizeDistribution.nextValue()) { fields.nextChar() })
                    }
                },
            )
            DataType.SHORT -> NestedCollectionBenchmarkData(
                shortArrayData = Array(numCollections) {
                    Array(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(ShortArray(nestedSizeDistribution.nextValue()) { fields.nextShort() })
                    }
                },
            )
            DataType.INT -> NestedCollectionBenchmarkData(
                intArrayData = Array(numCollections) {
                    Array(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(IntArray(nestedSizeDistribution.nextValue()) { fields.nextInt() })
                    }
                },
            )
            DataType.FLOAT -> NestedCollectionBenchmarkData(
                floatArrayData = Array(numCollections) {
                    Array(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(FloatArray(nestedSizeDistribution.nextValue()) { fields.nextFloat() })
                    }
                },
            )
            DataType.LONG -> NestedCollectionBenchmarkData(
                longArrayData = Array(numCollections) {
                    Array(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(LongArray(nestedSizeDistribution.nextValue()) { fields.nextLong() })
                    }
                },
            )
            DataType.DOUBLE -> NestedCollectionBenchmarkData(
                doubleArrayData = Array(numCollections) {
                    Array(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(DoubleArray(nestedSizeDistribution.nextValue()) { fields.nextDouble() })
                    }
                },
            )
        }

        private fun createImmutableArrays(
            numCollections: Int,
            dataType: DataType,
            topLevelSizeDistribution: Distribution,
            nestedSizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): NestedCollectionBenchmarkData = when (dataType) {
            DataType.REFERENCE -> NestedCollectionBenchmarkData(
                immutableReferenceArrayData = Array(numCollections) {
                    ImmutableArray(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(ImmutableArray(nestedSizeDistribution.nextValue()) { references.next() })
                    }
                },
            )
            DataType.BOOLEAN -> NestedCollectionBenchmarkData(
                immutableBooleanArrayData = Array(numCollections) {
                    ImmutableArray(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(
                            ImmutableBooleanArray(nestedSizeDistribution.nextValue()) { fields.nextBoolean() }
                        )
                    }
                },
            )
            DataType.BYTE -> NestedCollectionBenchmarkData(
                immutableByteArrayData = Array(numCollections) {
                    ImmutableArray(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(ImmutableByteArray(nestedSizeDistribution.nextValue()) { fields.nextByte() })
                    }
                },
            )
            DataType.CHAR -> NestedCollectionBenchmarkData(
                immutableCharArrayData = Array(numCollections) {
                    ImmutableArray(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(ImmutableCharArray(nestedSizeDistribution.nextValue()) { fields.nextChar() })
                    }
                },
            )
            DataType.SHORT -> NestedCollectionBenchmarkData(
                immutableShortArrayData = Array(numCollections) {
                    ImmutableArray(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(ImmutableShortArray(nestedSizeDistribution.nextValue()) { fields.nextShort() })
                    }
                },
            )
            DataType.INT -> NestedCollectionBenchmarkData(
                immutableIntArrayData = Array(numCollections) {
                    ImmutableArray(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(ImmutableIntArray(nestedSizeDistribution.nextValue()) { fields.nextInt() })
                    }
                },
            )
            DataType.FLOAT -> NestedCollectionBenchmarkData(
                immutableFloatArrayData = Array(numCollections) {
                    ImmutableArray(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(ImmutableFloatArray(nestedSizeDistribution.nextValue()) { fields.nextFloat() })
                    }
                },
            )
            DataType.LONG -> NestedCollectionBenchmarkData(
                immutableLongArrayData = Array(numCollections) {
                    ImmutableArray(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(ImmutableLongArray(nestedSizeDistribution.nextValue()) { fields.nextLong() })
                    }
                },
            )
            DataType.DOUBLE -> NestedCollectionBenchmarkData(
                immutableDoubleArrayData = Array(numCollections) {
                    ImmutableArray(topLevelSizeDistribution.nextValue()) {
                        CollectionOwner(ImmutableDoubleArray(nestedSizeDistribution.nextValue()) { fields.nextDouble() })
                    }
                },
            )
        }
    }
}
