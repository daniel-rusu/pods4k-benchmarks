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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.resolveElementClass
import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList

/**
 * Materialized parent and nested collections for one [CollectionType]/[DataType] trial.
 *
 * Storing only the active representation avoids unused empty fields and prevents benchmarks from accidentally operating
 * on unrelated empty arrays.
 *
 * Typed accessors validate the nested [elementClass], while the outer array's runtime component type prevents an
 * Array<ArrayList> from being treated as an Array<PersistentList> etc.
 */
class NestedCollectionBenchmarkData private constructor(
    @PublishedApi internal val elementClass: Class<*>,
    @PublishedApi internal val collectionData: Array<*>,
) {
    inline fun <reified T : Any> listData(): Array<ArrayList<CollectionOwner<ArrayList<T>>>> {
        return typedCollectionData<T, ArrayList<CollectionOwner<ArrayList<T>>>>()
    }

    inline fun <reified T : Any> persistentListData(): Array<PersistentList<CollectionOwner<PersistentList<T>>>> {
        return typedCollectionData<T, PersistentList<CollectionOwner<PersistentList<T>>>>()
    }

    val referenceArrayData: Array<Array<CollectionOwner<Array<String>>>>
        get() = typedCollectionData<String, Array<CollectionOwner<Array<String>>>>()

    val booleanArrayData: Array<Array<CollectionOwner<BooleanArray>>>
        get() = typedCollectionData<Boolean, Array<CollectionOwner<BooleanArray>>>()

    val byteArrayData: Array<Array<CollectionOwner<ByteArray>>>
        get() = typedCollectionData<Byte, Array<CollectionOwner<ByteArray>>>()

    val charArrayData: Array<Array<CollectionOwner<CharArray>>>
        get() = typedCollectionData<Char, Array<CollectionOwner<CharArray>>>()

    val shortArrayData: Array<Array<CollectionOwner<ShortArray>>>
        get() = typedCollectionData<Short, Array<CollectionOwner<ShortArray>>>()

    val intArrayData: Array<Array<CollectionOwner<IntArray>>>
        get() = typedCollectionData<Int, Array<CollectionOwner<IntArray>>>()

    val floatArrayData: Array<Array<CollectionOwner<FloatArray>>>
        get() = typedCollectionData<Float, Array<CollectionOwner<FloatArray>>>()

    val longArrayData: Array<Array<CollectionOwner<LongArray>>>
        get() = typedCollectionData<Long, Array<CollectionOwner<LongArray>>>()

    val doubleArrayData: Array<Array<CollectionOwner<DoubleArray>>>
        get() = typedCollectionData<Double, Array<CollectionOwner<DoubleArray>>>()

    val immutableReferenceArrayData: Array<ImmutableArray<CollectionOwner<ImmutableArray<String>>>>
        get() = typedCollectionData<String, ImmutableArray<CollectionOwner<ImmutableArray<String>>>>()

    val immutableBooleanArrayData: Array<ImmutableArray<CollectionOwner<ImmutableBooleanArray>>>
        get() = typedCollectionData<Boolean, ImmutableArray<CollectionOwner<ImmutableBooleanArray>>>()

    val immutableByteArrayData: Array<ImmutableArray<CollectionOwner<ImmutableByteArray>>>
        get() = typedCollectionData<Byte, ImmutableArray<CollectionOwner<ImmutableByteArray>>>()

    val immutableCharArrayData: Array<ImmutableArray<CollectionOwner<ImmutableCharArray>>>
        get() = typedCollectionData<Char, ImmutableArray<CollectionOwner<ImmutableCharArray>>>()

    val immutableShortArrayData: Array<ImmutableArray<CollectionOwner<ImmutableShortArray>>>
        get() = typedCollectionData<Short, ImmutableArray<CollectionOwner<ImmutableShortArray>>>()

    val immutableIntArrayData: Array<ImmutableArray<CollectionOwner<ImmutableIntArray>>>
        get() = typedCollectionData<Int, ImmutableArray<CollectionOwner<ImmutableIntArray>>>()

    val immutableFloatArrayData: Array<ImmutableArray<CollectionOwner<ImmutableFloatArray>>>
        get() = typedCollectionData<Float, ImmutableArray<CollectionOwner<ImmutableFloatArray>>>()

    val immutableLongArrayData: Array<ImmutableArray<CollectionOwner<ImmutableLongArray>>>
        get() = typedCollectionData<Long, ImmutableArray<CollectionOwner<ImmutableLongArray>>>()

    val immutableDoubleArrayData: Array<ImmutableArray<CollectionOwner<ImmutableDoubleArray>>>
        get() = typedCollectionData<Double, ImmutableArray<CollectionOwner<ImmutableDoubleArray>>>()

    /** Validates nested element type [T] and casts the array to the appropriate top-level collection type [C]. */
    @PublishedApi
    internal inline fun <reified T : Any, reified C : Any> typedCollectionData(): Array<C> {
        val requestedElementClass = T::class.javaObjectType
        check(elementClass === requestedElementClass) {
            "Requested ${requestedElementClass.name} elements, but the data contains ${elementClass.name} elements"
        }

        @Suppress("UNCHECKED_CAST")
        return collectionData as Array<C>
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

            val data = when (collectionType) {
                LIST -> createLists(
                    numCollections,
                    dataType,
                    topLevelSizeDistribution,
                    nestedSizeDistribution,
                    fields,
                    references,
                )

                PERSISTENT_LIST -> createPersistentLists(
                    numCollections,
                    dataType,
                    topLevelSizeDistribution,
                    nestedSizeDistribution,
                    fields,
                    references,
                )

                ARRAY -> createArrays(
                    numCollections,
                    dataType,
                    topLevelSizeDistribution,
                    nestedSizeDistribution,
                    fields,
                    references,
                )

                IMMUTABLE_ARRAY -> createImmutableArrays(
                    numCollections,
                    dataType,
                    topLevelSizeDistribution,
                    nestedSizeDistribution,
                    fields,
                    references,
                )
            }
            return NestedCollectionBenchmarkData(dataType.resolveElementClass(references.objectClass), data)
        }

        private fun createLists(
            numCollections: Int,
            dataType: DataType,
            topLevelSizeDistribution: Distribution,
            nestedSizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): Array<ArrayList<CollectionOwner<ArrayList<*>>>> {
            val generateElement = createElementGenerator(dataType, fields, references)
            return Array(numCollections) {
                createList(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(
                        createList(nestedSizeDistribution.nextValue()) { generateElement() }
                    )
                }
            }
        }

        private fun createPersistentLists(
            numCollections: Int,
            dataType: DataType,
            topLevelSizeDistribution: Distribution,
            nestedSizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): Array<PersistentList<CollectionOwner<PersistentList<*>>>> {
            val generateElement = createElementGenerator(dataType, fields, references)
            return Array(numCollections) {
                createPersistentList(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(
                        createPersistentList(nestedSizeDistribution.nextValue()) { generateElement() }
                    )
                }
            }
        }

        private fun createElementGenerator(
            dataType: DataType,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): () -> Any = when (dataType) {
            DataType.REFERENCE -> references::next
            DataType.BOOLEAN -> fields::nextBoolean
            DataType.BYTE -> fields::nextByte
            DataType.CHAR -> fields::nextChar
            DataType.SHORT -> fields::nextShort
            DataType.INT -> fields::nextInt
            DataType.FLOAT -> fields::nextFloat
            DataType.LONG -> fields::nextLong
            DataType.DOUBLE -> fields::nextDouble
        }

        private fun createArrays(
            numCollections: Int,
            dataType: DataType,
            topLevelSizeDistribution: Distribution,
            nestedSizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): Array<*> = when (dataType) {
            DataType.REFERENCE -> Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(Array(nestedSizeDistribution.nextValue()) { references.next() })
                }
            }

            DataType.BOOLEAN -> Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(BooleanArray(nestedSizeDistribution.nextValue()) { fields.nextBoolean() })
                }
            }

            DataType.BYTE -> Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ByteArray(nestedSizeDistribution.nextValue()) { fields.nextByte() })
                }
            }

            DataType.CHAR -> Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(CharArray(nestedSizeDistribution.nextValue()) { fields.nextChar() })
                }
            }

            DataType.SHORT -> Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ShortArray(nestedSizeDistribution.nextValue()) { fields.nextShort() })
                }
            }

            DataType.INT -> Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(IntArray(nestedSizeDistribution.nextValue()) { fields.nextInt() })
                }
            }

            DataType.FLOAT -> Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(FloatArray(nestedSizeDistribution.nextValue()) { fields.nextFloat() })
                }
            }

            DataType.LONG -> Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(LongArray(nestedSizeDistribution.nextValue()) { fields.nextLong() })
                }
            }

            DataType.DOUBLE -> Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(DoubleArray(nestedSizeDistribution.nextValue()) { fields.nextDouble() })
                }
            }
        }

        private fun createImmutableArrays(
            numCollections: Int,
            dataType: DataType,
            topLevelSizeDistribution: Distribution,
            nestedSizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): Array<*> = when (dataType) {
            DataType.REFERENCE -> Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableArray(nestedSizeDistribution.nextValue()) { references.next() })
                }
            }

            DataType.BOOLEAN -> Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(
                        ImmutableBooleanArray(nestedSizeDistribution.nextValue()) { fields.nextBoolean() }
                    )
                }
            }

            DataType.BYTE -> Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableByteArray(nestedSizeDistribution.nextValue()) { fields.nextByte() })
                }
            }

            DataType.CHAR -> Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableCharArray(nestedSizeDistribution.nextValue()) { fields.nextChar() })
                }
            }

            DataType.SHORT -> Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableShortArray(nestedSizeDistribution.nextValue()) { fields.nextShort() })
                }
            }

            DataType.INT -> Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableIntArray(nestedSizeDistribution.nextValue()) { fields.nextInt() })
                }
            }

            DataType.FLOAT -> Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableFloatArray(nestedSizeDistribution.nextValue()) { fields.nextFloat() })
                }
            }

            DataType.LONG -> Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableLongArray(nestedSizeDistribution.nextValue()) { fields.nextLong() })
                }
            }

            DataType.DOUBLE -> Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableDoubleArray(nestedSizeDistribution.nextValue()) { fields.nextDouble() })
                }
            }
        }
    }
}
