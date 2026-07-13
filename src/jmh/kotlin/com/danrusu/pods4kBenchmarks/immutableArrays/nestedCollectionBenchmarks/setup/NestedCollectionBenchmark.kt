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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionFactory.createList
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionFactory.createPersistentList
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.BOOLEAN
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.BYTE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.CHAR
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.DOUBLE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.FLOAT
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.INT
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.LONG
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.REFERENCE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.SHORT
import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.openjdk.jmh.infra.Blackhole

/**
 * Represents a benchmark that measures the performance of operating on nested collections, such as a list of orders
 * with each order containing a list of products. Note that the term collection is used loosely to represent a [List],
 * [Array], or [ImmutableArray] rather than the actual [Collection] interface.
 */
@State(Scope.Benchmark)
abstract class NestedCollectionBenchmark {
    /** Repeat the benchmarks for each collection type. */
    @Param
    protected lateinit var collectionType: CollectionType

    /** Repeat the benchmarks for each of the eight base data types plus a String reference type. */
    @Param
    protected lateinit var dataType: DataType

    /**
     * The number of collections to benchmark against in order to avoid repeatedly operating on the same collection.
     *
     * Contract: The subclass overriding this value must return a fixed value that never changes.
     */
    abstract val numCollections: Int

    /** Controls the sizes of the parent collections that will be generated. */
    open val topLevelSizeDistributionFactory: DistributionFactory
        get() = DistributionFactory.ListSizeDistribution

    /** Controls the sizes of the nested collections that will be generated. */
    open val nestedCollectionSizeDistributionFactory: DistributionFactory
        get() = DistributionFactory.NestedListSizeDistribution

    /** Creates simple field generators for primitive nested collection elements. */
    open val nestedFieldGeneratorFactory: FieldGeneratorFactory
        get() = FieldGeneratorFactory.withRandomFields()

    /** Creates reference generators for nested collection elements. */
    open val nestedReferenceGeneratorFactory: ObjectGeneratorFactory<String>
        get() = ObjectGeneratorFactory.randomStrings()

    // List element types are erased, so one data array is sufficient for all nine data types.
    // These fields are published internally because the protected benchmark helper is inline.
    @PublishedApi
    internal var listData: Array<List<CollectionOwner<List<Any>>>> = emptyArray()

    @PublishedApi
    internal var persistentListData: Array<PersistentList<CollectionOwner<PersistentList<Any>>>> = emptyArray()

    @PublishedApi
    internal var referenceArrayData: Array<Array<CollectionOwner<Array<String>>>> = emptyArray()

    @PublishedApi
    internal var booleanArrayData: Array<Array<CollectionOwner<BooleanArray>>> = emptyArray()

    @PublishedApi
    internal var byteArrayData: Array<Array<CollectionOwner<ByteArray>>> = emptyArray()

    @PublishedApi
    internal var charArrayData: Array<Array<CollectionOwner<CharArray>>> = emptyArray()

    @PublishedApi
    internal var shortArrayData: Array<Array<CollectionOwner<ShortArray>>> = emptyArray()

    @PublishedApi
    internal var intArrayData: Array<Array<CollectionOwner<IntArray>>> = emptyArray()

    @PublishedApi
    internal var floatArrayData: Array<Array<CollectionOwner<FloatArray>>> = emptyArray()

    @PublishedApi
    internal var longArrayData: Array<Array<CollectionOwner<LongArray>>> = emptyArray()

    @PublishedApi
    internal var doubleArrayData: Array<Array<CollectionOwner<DoubleArray>>> = emptyArray()

    @PublishedApi
    internal var immutableReferenceArrayData:
        Array<ImmutableArray<CollectionOwner<ImmutableArray<String>>>> = emptyArray()

    @PublishedApi
    internal var immutableBooleanArrayData:
        Array<ImmutableArray<CollectionOwner<ImmutableBooleanArray>>> = emptyArray()

    @PublishedApi
    internal var immutableByteArrayData:
        Array<ImmutableArray<CollectionOwner<ImmutableByteArray>>> = emptyArray()

    @PublishedApi
    internal var immutableCharArrayData:
        Array<ImmutableArray<CollectionOwner<ImmutableCharArray>>> = emptyArray()

    @PublishedApi
    internal var immutableShortArrayData:
        Array<ImmutableArray<CollectionOwner<ImmutableShortArray>>> = emptyArray()

    @PublishedApi
    internal var immutableIntArrayData:
        Array<ImmutableArray<CollectionOwner<ImmutableIntArray>>> = emptyArray()

    @PublishedApi
    internal var immutableFloatArrayData:
        Array<ImmutableArray<CollectionOwner<ImmutableFloatArray>>> = emptyArray()

    @PublishedApi
    internal var immutableLongArrayData:
        Array<ImmutableArray<CollectionOwner<ImmutableLongArray>>> = emptyArray()

    @PublishedApi
    internal var immutableDoubleArrayData:
        Array<ImmutableArray<CollectionOwner<ImmutableDoubleArray>>> = emptyArray()

    @Setup(Level.Trial)
    fun setupCollections() {
        val rngFactory = RngFactory()
        val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
        val topLevelSizeDistribution = topLevelSizeDistributionFactory.create(rngFactory)
        val nestedSizeDistribution = nestedCollectionSizeDistributionFactory.create(rngFactory)
        val fields = nestedFieldGeneratorFactory.create(generatorRngs)
        val references = nestedReferenceGeneratorFactory.create(generatorRngs)

        when (collectionType) {
            LIST -> createLists(topLevelSizeDistribution, nestedSizeDistribution, fields, references)
            PERSISTENT_LIST -> createPersistentLists(topLevelSizeDistribution, nestedSizeDistribution, fields, references)
            ARRAY -> createArrays(topLevelSizeDistribution, nestedSizeDistribution, fields, references)
            IMMUTABLE_ARRAY -> createImmutableArrays(topLevelSizeDistribution, nestedSizeDistribution, fields, references)
        }
    }

    @TearDown
    fun tearDown() {
        listData = emptyArray()
        persistentListData = emptyArray()

        referenceArrayData = emptyArray()
        booleanArrayData = emptyArray()
        byteArrayData = emptyArray()
        charArrayData = emptyArray()
        shortArrayData = emptyArray()
        intArrayData = emptyArray()
        floatArrayData = emptyArray()
        longArrayData = emptyArray()
        doubleArrayData = emptyArray()

        immutableReferenceArrayData = emptyArray()
        immutableBooleanArrayData = emptyArray()
        immutableByteArrayData = emptyArray()
        immutableCharArrayData = emptyArray()
        immutableShortArrayData = emptyArray()
        immutableIntArrayData = emptyArray()
        immutableFloatArrayData = emptyArray()
        immutableLongArrayData = emptyArray()
        immutableDoubleArrayData = emptyArray()
    }

    @Suppress("UNCHECKED_CAST")
    @PublishedApi
    internal fun <T> typedListData(): Array<List<CollectionOwner<List<T>>>> {
        return listData as Array<List<CollectionOwner<List<T>>>>
    }

    @Suppress("UNCHECKED_CAST")
    @PublishedApi
    internal fun <T> typedPersistentListData(): Array<PersistentList<CollectionOwner<PersistentList<T>>>> {
        return persistentListData as Array<PersistentList<CollectionOwner<PersistentList<T>>>>
    }

    /** Performs the associated operation on every top-level collection and consumes each result. */
    protected inline fun transformEachCollection(
        bh: Blackhole,

        // lists
        transformNestedLists: (List<CollectionOwner<List<String>>>) -> Any?,
        transformNestedBooleanLists: (List<CollectionOwner<List<Boolean>>>) -> Any?,
        transformNestedByteLists: (List<CollectionOwner<List<Byte>>>) -> Any?,
        transformNestedCharLists: (List<CollectionOwner<List<Char>>>) -> Any?,
        transformNestedShortLists: (List<CollectionOwner<List<Short>>>) -> Any?,
        transformNestedIntLists: (List<CollectionOwner<List<Int>>>) -> Any?,
        transformNestedFloatLists: (List<CollectionOwner<List<Float>>>) -> Any?,
        transformNestedLongLists: (List<CollectionOwner<List<Long>>>) -> Any?,
        transformNestedDoubleLists: (List<CollectionOwner<List<Double>>>) -> Any?,

        // persistent lists
        transformNestedPersistentLists: (PersistentList<CollectionOwner<PersistentList<String>>>) -> Any?,
        transformNestedPersistentBooleanLists: (PersistentList<CollectionOwner<PersistentList<Boolean>>>) -> Any?,
        transformNestedPersistentByteLists: (PersistentList<CollectionOwner<PersistentList<Byte>>>) -> Any?,
        transformNestedPersistentCharLists: (PersistentList<CollectionOwner<PersistentList<Char>>>) -> Any?,
        transformNestedPersistentShortLists: (PersistentList<CollectionOwner<PersistentList<Short>>>) -> Any?,
        transformNestedPersistentIntLists: (PersistentList<CollectionOwner<PersistentList<Int>>>) -> Any?,
        transformNestedPersistentFloatLists: (PersistentList<CollectionOwner<PersistentList<Float>>>) -> Any?,
        transformNestedPersistentLongLists: (PersistentList<CollectionOwner<PersistentList<Long>>>) -> Any?,
        transformNestedPersistentDoubleLists: (PersistentList<CollectionOwner<PersistentList<Double>>>) -> Any?,

        // arrays
        transformNestedArrays: (Array<CollectionOwner<Array<String>>>) -> Any?,
        transformNestedBooleanArrays: (Array<CollectionOwner<BooleanArray>>) -> Any?,
        transformNestedByteArrays: (Array<CollectionOwner<ByteArray>>) -> Any?,
        transformNestedCharArrays: (Array<CollectionOwner<CharArray>>) -> Any?,
        transformNestedShortArrays: (Array<CollectionOwner<ShortArray>>) -> Any?,
        transformNestedIntArrays: (Array<CollectionOwner<IntArray>>) -> Any?,
        transformNestedFloatArrays: (Array<CollectionOwner<FloatArray>>) -> Any?,
        transformNestedLongArrays: (Array<CollectionOwner<LongArray>>) -> Any?,
        transformNestedDoubleArrays: (Array<CollectionOwner<DoubleArray>>) -> Any?,

        // immutable arrays
        transformNestedImmutableArrays: (ImmutableArray<CollectionOwner<ImmutableArray<String>>>) -> Any?,
        transformNestedImmutableBooleanArrays: (ImmutableArray<CollectionOwner<ImmutableBooleanArray>>) -> Any?,
        transformNestedImmutableByteArrays: (ImmutableArray<CollectionOwner<ImmutableByteArray>>) -> Any?,
        transformNestedImmutableCharArrays: (ImmutableArray<CollectionOwner<ImmutableCharArray>>) -> Any?,
        transformNestedImmutableShortArrays: (ImmutableArray<CollectionOwner<ImmutableShortArray>>) -> Any?,
        transformNestedImmutableIntArrays: (ImmutableArray<CollectionOwner<ImmutableIntArray>>) -> Any?,
        transformNestedImmutableFloatArrays: (ImmutableArray<CollectionOwner<ImmutableFloatArray>>) -> Any?,
        transformNestedImmutableLongArrays: (ImmutableArray<CollectionOwner<ImmutableLongArray>>) -> Any?,
        transformNestedImmutableDoubleArrays: (ImmutableArray<CollectionOwner<ImmutableDoubleArray>>) -> Any?,
    ) {
        when (collectionType) {
            LIST -> when (dataType) {
                REFERENCE -> typedListData<String>().forEach { bh.consume(transformNestedLists(it)) }
                BOOLEAN -> typedListData<Boolean>().forEach { bh.consume(transformNestedBooleanLists(it)) }
                BYTE -> typedListData<Byte>().forEach { bh.consume(transformNestedByteLists(it)) }
                CHAR -> typedListData<Char>().forEach { bh.consume(transformNestedCharLists(it)) }
                SHORT -> typedListData<Short>().forEach { bh.consume(transformNestedShortLists(it)) }
                INT -> typedListData<Int>().forEach { bh.consume(transformNestedIntLists(it)) }
                FLOAT -> typedListData<Float>().forEach { bh.consume(transformNestedFloatLists(it)) }
                LONG -> typedListData<Long>().forEach { bh.consume(transformNestedLongLists(it)) }
                DOUBLE -> typedListData<Double>().forEach { bh.consume(transformNestedDoubleLists(it)) }
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> typedPersistentListData<String>().forEach {
                    bh.consume(transformNestedPersistentLists(it))
                }

                BOOLEAN -> typedPersistentListData<Boolean>().forEach {
                    bh.consume(transformNestedPersistentBooleanLists(it))
                }

                BYTE -> typedPersistentListData<Byte>().forEach {
                    bh.consume(transformNestedPersistentByteLists(it))
                }

                CHAR -> typedPersistentListData<Char>().forEach {
                    bh.consume(transformNestedPersistentCharLists(it))
                }

                SHORT -> typedPersistentListData<Short>().forEach {
                    bh.consume(transformNestedPersistentShortLists(it))
                }

                INT -> typedPersistentListData<Int>().forEach {
                    bh.consume(transformNestedPersistentIntLists(it))
                }

                FLOAT -> typedPersistentListData<Float>().forEach {
                    bh.consume(transformNestedPersistentFloatLists(it))
                }

                LONG -> typedPersistentListData<Long>().forEach {
                    bh.consume(transformNestedPersistentLongLists(it))
                }

                DOUBLE -> typedPersistentListData<Double>().forEach {
                    bh.consume(transformNestedPersistentDoubleLists(it))
                }
            }

            ARRAY -> when (dataType) {
                REFERENCE -> referenceArrayData.forEach { bh.consume(transformNestedArrays(it)) }
                BOOLEAN -> booleanArrayData.forEach { bh.consume(transformNestedBooleanArrays(it)) }
                BYTE -> byteArrayData.forEach { bh.consume(transformNestedByteArrays(it)) }
                CHAR -> charArrayData.forEach { bh.consume(transformNestedCharArrays(it)) }
                SHORT -> shortArrayData.forEach { bh.consume(transformNestedShortArrays(it)) }
                INT -> intArrayData.forEach { bh.consume(transformNestedIntArrays(it)) }
                FLOAT -> floatArrayData.forEach { bh.consume(transformNestedFloatArrays(it)) }
                LONG -> longArrayData.forEach { bh.consume(transformNestedLongArrays(it)) }
                DOUBLE -> doubleArrayData.forEach { bh.consume(transformNestedDoubleArrays(it)) }
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> immutableReferenceArrayData.forEach { bh.consume(transformNestedImmutableArrays(it)) }
                BOOLEAN -> immutableBooleanArrayData.forEach { bh.consume(transformNestedImmutableBooleanArrays(it)) }
                BYTE -> immutableByteArrayData.forEach { bh.consume(transformNestedImmutableByteArrays(it)) }
                CHAR -> immutableCharArrayData.forEach { bh.consume(transformNestedImmutableCharArrays(it)) }
                SHORT -> immutableShortArrayData.forEach { bh.consume(transformNestedImmutableShortArrays(it)) }
                INT -> immutableIntArrayData.forEach { bh.consume(transformNestedImmutableIntArrays(it)) }
                FLOAT -> immutableFloatArrayData.forEach { bh.consume(transformNestedImmutableFloatArrays(it)) }
                LONG -> immutableLongArrayData.forEach { bh.consume(transformNestedImmutableLongArrays(it)) }
                DOUBLE -> immutableDoubleArrayData.forEach { bh.consume(transformNestedImmutableDoubleArrays(it)) }
            }
        }
    }

    private fun createLists(
        topLevelSizeDistribution: Distribution,
        nestedSizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String>,
    ) {
        listData = Array(numCollections) {
            createList(topLevelSizeDistribution.nextValue()) {
                when (dataType) {
                    REFERENCE -> CollectionOwner(createList(nestedSizeDistribution.nextValue()) { references.next() })
                    BOOLEAN -> CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextBoolean() })
                    BYTE -> CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextByte() })
                    CHAR -> CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextChar() })
                    SHORT -> CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextShort() })
                    INT -> CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextInt() })
                    FLOAT -> CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextFloat() })
                    LONG -> CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextLong() })
                    DOUBLE -> CollectionOwner(createList(nestedSizeDistribution.nextValue()) { fields.nextDouble() })
                }
            }
        }
    }

    private fun createPersistentLists(
        topLevelSizeDistribution: Distribution,
        nestedSizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String>,
    ) {
        persistentListData = Array(numCollections) {
            createPersistentList(topLevelSizeDistribution.nextValue()) {
                when (dataType) {
                    REFERENCE -> CollectionOwner(
                        createPersistentList(nestedSizeDistribution.nextValue()) { references.next() }
                    )

                    BOOLEAN -> CollectionOwner(
                        createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextBoolean() }
                    )

                    BYTE -> CollectionOwner(createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextByte() })
                    CHAR -> CollectionOwner(createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextChar() })
                    SHORT -> CollectionOwner(createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextShort() })
                    INT -> CollectionOwner(createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextInt() })
                    FLOAT -> CollectionOwner(createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextFloat() })
                    LONG -> CollectionOwner(createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextLong() })
                    DOUBLE -> CollectionOwner(
                        createPersistentList(nestedSizeDistribution.nextValue()) { fields.nextDouble() }
                    )
                }
            }
        }
    }

    private fun createArrays(
        topLevelSizeDistribution: Distribution,
        nestedSizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String>,
    ) {
        when (dataType) {
            REFERENCE -> referenceArrayData = Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(Array(nestedSizeDistribution.nextValue()) { references.next() })
                }
            }

            BOOLEAN -> booleanArrayData = Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(BooleanArray(nestedSizeDistribution.nextValue()) { fields.nextBoolean() })
                }
            }

            BYTE -> byteArrayData = Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ByteArray(nestedSizeDistribution.nextValue()) { fields.nextByte() })
                }
            }

            CHAR -> charArrayData = Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(CharArray(nestedSizeDistribution.nextValue()) { fields.nextChar() })
                }
            }

            SHORT -> shortArrayData = Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ShortArray(nestedSizeDistribution.nextValue()) { fields.nextShort() })
                }
            }

            INT -> intArrayData = Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(IntArray(nestedSizeDistribution.nextValue()) { fields.nextInt() })
                }
            }

            FLOAT -> floatArrayData = Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(FloatArray(nestedSizeDistribution.nextValue()) { fields.nextFloat() })
                }
            }

            LONG -> longArrayData = Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(LongArray(nestedSizeDistribution.nextValue()) { fields.nextLong() })
                }
            }

            DOUBLE -> doubleArrayData = Array(numCollections) {
                Array(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(DoubleArray(nestedSizeDistribution.nextValue()) { fields.nextDouble() })
                }
            }
        }
    }

    private fun createImmutableArrays(
        topLevelSizeDistribution: Distribution,
        nestedSizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String>,
    ) {
        when (dataType) {
            REFERENCE -> immutableReferenceArrayData = Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableArray(nestedSizeDistribution.nextValue()) { references.next() })
                }
            }

            BOOLEAN -> immutableBooleanArrayData = Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableBooleanArray(nestedSizeDistribution.nextValue()) { fields.nextBoolean() })
                }
            }

            BYTE -> immutableByteArrayData = Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableByteArray(nestedSizeDistribution.nextValue()) { fields.nextByte() })
                }
            }

            CHAR -> immutableCharArrayData = Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableCharArray(nestedSizeDistribution.nextValue()) { fields.nextChar() })
                }
            }

            SHORT -> immutableShortArrayData = Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableShortArray(nestedSizeDistribution.nextValue()) { fields.nextShort() })
                }
            }

            INT -> immutableIntArrayData = Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableIntArray(nestedSizeDistribution.nextValue()) { fields.nextInt() })
                }
            }

            FLOAT -> immutableFloatArrayData = Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableFloatArray(nestedSizeDistribution.nextValue()) { fields.nextFloat() })
                }
            }

            LONG -> immutableLongArrayData = Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableLongArray(nestedSizeDistribution.nextValue()) { fields.nextLong() })
                }
            }

            DOUBLE -> immutableDoubleArrayData = Array(numCollections) {
                ImmutableArray(topLevelSizeDistribution.nextValue()) {
                    CollectionOwner(ImmutableDoubleArray(nestedSizeDistribution.nextValue()) { fields.nextDouble() })
                }
            }
        }
    }

}
