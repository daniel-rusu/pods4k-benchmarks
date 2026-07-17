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
 * Materialized collections for one [CollectionType]/[DataType] trial.
 *
 * Storing only the active representation avoids unused empty fields and prevents benchmarks from accidentally operating
 * on unrelated empty arrays.
 *
 * Typed accessors validate the nested [elementClass], while the outer array's runtime component type prevents an
 * Array<ArrayList> from being treated as an Array<PersistentList> etc.
 */
class FlatCollectionBenchmarkData private constructor(
    @PublishedApi internal val elementClass: Class<*>,
    @PublishedApi internal val collectionData: Array<*>,
) {
    inline fun <reified T : Any> listData(): Array<ArrayList<T>> {
        return typedCollectionData<T, ArrayList<T>>()
    }

    inline fun <reified T : Any> persistentListData(): Array<PersistentList<T>> {
        return typedCollectionData<T, PersistentList<T>>()
    }

    val referenceArrays: Array<Array<String>>
        get() = typedCollectionData<String, Array<String>>()

    val booleanArrays: Array<BooleanArray>
        get() = typedCollectionData<Boolean, BooleanArray>()

    val byteArrays: Array<ByteArray>
        get() = typedCollectionData<Byte, ByteArray>()

    val charArrays: Array<CharArray>
        get() = typedCollectionData<Char, CharArray>()

    val shortArrays: Array<ShortArray>
        get() = typedCollectionData<Short, ShortArray>()

    val intArrays: Array<IntArray>
        get() = typedCollectionData<Int, IntArray>()

    val floatArrays: Array<FloatArray>
        get() = typedCollectionData<Float, FloatArray>()

    val longArrays: Array<LongArray>
        get() = typedCollectionData<Long, LongArray>()

    val doubleArrays: Array<DoubleArray>
        get() = typedCollectionData<Double, DoubleArray>()

    val immutableReferenceArrays: Array<ImmutableArray<String>>
        get() = typedCollectionData<String, ImmutableArray<String>>()

    val immutableBooleanArrays: Array<ImmutableBooleanArray>
        get() = typedCollectionData<Boolean, ImmutableBooleanArray>()

    val immutableByteArrays: Array<ImmutableByteArray>
        get() = typedCollectionData<Byte, ImmutableByteArray>()

    val immutableCharArrays: Array<ImmutableCharArray>
        get() = typedCollectionData<Char, ImmutableCharArray>()

    val immutableShortArrays: Array<ImmutableShortArray>
        get() = typedCollectionData<Short, ImmutableShortArray>()

    val immutableIntArrays: Array<ImmutableIntArray>
        get() = typedCollectionData<Int, ImmutableIntArray>()

    val immutableFloatArrays: Array<ImmutableFloatArray>
        get() = typedCollectionData<Float, ImmutableFloatArray>()

    val immutableLongArrays: Array<ImmutableLongArray>
        get() = typedCollectionData<Long, ImmutableLongArray>()

    val immutableDoubleArrays: Array<ImmutableDoubleArray>
        get() = typedCollectionData<Double, ImmutableDoubleArray>()

    /**
     * Validates element type [T] and casts to collection type [C]. Every accessor uses this path so the overhead is
     * consistent across benchmarks, including for primitive arrays whose runtime type already encodes the element type.
     */
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
                LIST -> createLists(numCollections, dataType, sizeDistribution, fields, references)
                PERSISTENT_LIST -> createPersistentLists(numCollections, dataType, sizeDistribution, fields, references)
                ARRAY -> createArrays(numCollections, dataType, sizeDistribution, fields, references)
                IMMUTABLE_ARRAY -> createImmutableArrays(numCollections, dataType, sizeDistribution, fields, references)
            }
        }

        private fun createLists(
            numCollections: Int,
            dataType: DataType,
            sizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): FlatCollectionBenchmarkData {
            val generateElement = createElementGenerator(dataType, fields, references)
            val data: Array<ArrayList<*>> = Array(numCollections) {
                createList(sizeDistribution.nextValue()) { generateElement() }
            }
            return FlatCollectionBenchmarkData(dataType.resolveElementClass(references.objectClass), data)
        }

        private fun createPersistentLists(
            numCollections: Int,
            dataType: DataType,
            sizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): FlatCollectionBenchmarkData {
            val generateElement = createElementGenerator(dataType, fields, references)
            val data: Array<PersistentList<*>> = Array(numCollections) {
                createPersistentList(sizeDistribution.nextValue()) { generateElement() }
            }
            return FlatCollectionBenchmarkData(dataType.resolveElementClass(references.objectClass), data)
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
            sizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): FlatCollectionBenchmarkData {
            val data: Array<*> = when (dataType) {
                DataType.REFERENCE -> Array(numCollections) {
                    Array(sizeDistribution.nextValue()) { references.next() }
                }

                DataType.BOOLEAN -> Array(numCollections) {
                    BooleanArray(sizeDistribution.nextValue()) { fields.nextBoolean() }
                }

                DataType.BYTE -> Array(numCollections) {
                    ByteArray(sizeDistribution.nextValue()) { fields.nextByte() }
                }

                DataType.CHAR -> Array(numCollections) {
                    CharArray(sizeDistribution.nextValue()) { fields.nextChar() }
                }

                DataType.SHORT -> Array(numCollections) {
                    ShortArray(sizeDistribution.nextValue()) { fields.nextShort() }
                }

                DataType.INT -> Array(numCollections) {
                    IntArray(sizeDistribution.nextValue()) { fields.nextInt() }
                }

                DataType.FLOAT -> Array(numCollections) {
                    FloatArray(sizeDistribution.nextValue()) { fields.nextFloat() }
                }

                DataType.LONG -> Array(numCollections) {
                    LongArray(sizeDistribution.nextValue()) { fields.nextLong() }
                }

                DataType.DOUBLE -> Array(numCollections) {
                    DoubleArray(sizeDistribution.nextValue()) { fields.nextDouble() }
                }
            }
            return FlatCollectionBenchmarkData(dataType.resolveElementClass(references.objectClass), data)
        }

        private fun createImmutableArrays(
            numCollections: Int,
            dataType: DataType,
            sizeDistribution: Distribution,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): FlatCollectionBenchmarkData {
            val data: Array<*> = when (dataType) {
                DataType.REFERENCE -> Array(numCollections) {
                    ImmutableArray(sizeDistribution.nextValue()) { references.next() }
                }

                DataType.BOOLEAN -> Array(numCollections) {
                    ImmutableBooleanArray(sizeDistribution.nextValue()) { fields.nextBoolean() }
                }

                DataType.BYTE -> Array(numCollections) {
                    ImmutableByteArray(sizeDistribution.nextValue()) { fields.nextByte() }
                }

                DataType.CHAR -> Array(numCollections) {
                    ImmutableCharArray(sizeDistribution.nextValue()) { fields.nextChar() }
                }

                DataType.SHORT -> Array(numCollections) {
                    ImmutableShortArray(sizeDistribution.nextValue()) { fields.nextShort() }
                }

                DataType.INT -> Array(numCollections) {
                    ImmutableIntArray(sizeDistribution.nextValue()) { fields.nextInt() }
                }

                DataType.FLOAT -> Array(numCollections) {
                    ImmutableFloatArray(sizeDistribution.nextValue()) { fields.nextFloat() }
                }

                DataType.LONG -> Array(numCollections) {
                    ImmutableLongArray(sizeDistribution.nextValue()) { fields.nextLong() }
                }

                DataType.DOUBLE -> Array(numCollections) {
                    ImmutableDoubleArray(sizeDistribution.nextValue()) { fields.nextDouble() }
                }
            }
            return FlatCollectionBenchmarkData(dataType.resolveElementClass(references.objectClass), data)
        }
    }
}
