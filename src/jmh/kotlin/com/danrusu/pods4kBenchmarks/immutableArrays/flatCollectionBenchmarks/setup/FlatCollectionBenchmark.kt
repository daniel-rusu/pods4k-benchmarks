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
import org.openjdk.jmh.annotations.OperationsPerInvocation
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.openjdk.jmh.infra.Blackhole

/**
 * Represents a benchmark that measures the performance of operating on flat collections.  Note that the term collection
 * is used loosely to represent a [List], [Array], or [ImmutableArray] rather than the actual [Collection] interface.
 *
 * A flat collection represents a collection that stores one of the 8 base types, such as a collection of [Boolean]
 * values, or a collection containing simple reference types (with the most common reference type being [String]).
 *
 * Benchmarks are parameterized by every combination of [CollectionType] and [DataType].  Subclasses should create a
 * benchmark method that calls [transformEachCollection] to measure the performance of each scenario.
 *
 * For example, if [numCollections] returns 500, and the collection type & data type pair being measured is
 * [CollectionType.LIST] & [DataType.BOOLEAN], then 500 List<Boolean> collections will be created.  When the subclass
 * calls [transformEachCollection], the provided boolean collection transform will be called for each of the 500
 * collections.
 */
@State(Scope.Benchmark)
abstract class FlatCollectionBenchmark {
    /** Repeat the benchmark for each collection type */
    @Param
    protected lateinit var collectionType: CollectionType

    /** Repeat the benchmark for each of the 8 base data types plus a String reference type */
    @Param
    protected lateinit var dataType: DataType

    /**
     * The number of collections to benchmark against in order to avoid repeating the operation being measured from
     * being performed on the same collection repeatedly.  This number should be sufficiently large, like 1000, to
     * avoid misleading results from optimizations like caching etc.
     *
     * Contract: The subclass overriding this value must return a fixed value that never changes.
     */
    abstract val numCollections: Int

    /** Controls the sizes of the collections that will be generated */
    open val sizeDistributionFactory: DistributionFactory
        get() = DistributionFactory.ListSizeDistribution

    /** Responsible for generating primitive field values that the collections will contain */
    open val fieldGeneratorFactory: FieldGeneratorFactory
        get() = FieldGeneratorFactory.withRandomFields()

    /** Responsible for generating reference values that the collections will contain */
    open val referenceGeneratorFactory: ObjectGeneratorFactory<String>
        get() = ObjectGeneratorFactory.randomStrings()

    // List element types are erased, so one data array is sufficient for all nine data types.
    // These fields are published internally because the protected benchmark helpers are inline.
    @PublishedApi
    internal var listData: Array<List<Any>> = emptyArray()

    @PublishedApi
    internal var persistentListData: Array<PersistentList<Any>> = emptyArray()

    @PublishedApi
    internal var referenceArrays: Array<Array<String>> = emptyArray()

    @PublishedApi
    internal var booleanArrays: Array<BooleanArray> = emptyArray()

    @PublishedApi
    internal var byteArrays: Array<ByteArray> = emptyArray()

    @PublishedApi
    internal var charArrays: Array<CharArray> = emptyArray()

    @PublishedApi
    internal var shortArrays: Array<ShortArray> = emptyArray()

    @PublishedApi
    internal var intArrays: Array<IntArray> = emptyArray()

    @PublishedApi
    internal var floatArrays: Array<FloatArray> = emptyArray()

    @PublishedApi
    internal var longArrays: Array<LongArray> = emptyArray()

    @PublishedApi
    internal var doubleArrays: Array<DoubleArray> = emptyArray()

    @PublishedApi
    internal var immutableReferenceArrays: Array<ImmutableArray<String>> = emptyArray()

    @PublishedApi
    internal var immutableBooleanArrays: Array<ImmutableBooleanArray> = emptyArray()

    @PublishedApi
    internal var immutableByteArrays: Array<ImmutableByteArray> = emptyArray()

    @PublishedApi
    internal var immutableCharArrays: Array<ImmutableCharArray> = emptyArray()

    @PublishedApi
    internal var immutableShortArrays: Array<ImmutableShortArray> = emptyArray()

    @PublishedApi
    internal var immutableIntArrays: Array<ImmutableIntArray> = emptyArray()

    @PublishedApi
    internal var immutableFloatArrays: Array<ImmutableFloatArray> = emptyArray()

    @PublishedApi
    internal var immutableLongArrays: Array<ImmutableLongArray> = emptyArray()

    @PublishedApi
    internal var immutableDoubleArrays: Array<ImmutableDoubleArray> = emptyArray()

    @Setup(Level.Trial)
    fun setupCollections() {
        val rngFactory = RngFactory()
        val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
        val sizeDistribution = sizeDistributionFactory.create(rngFactory)
        val fields = fieldGeneratorFactory.create(generatorRngs)
        val references = referenceGeneratorFactory.create(generatorRngs)

        when (collectionType) {
            LIST -> createLists(sizeDistribution, fields, references)
            PERSISTENT_LIST -> createPersistentLists(sizeDistribution, fields, references)
            ARRAY -> createArrays(sizeDistribution, fields, references)
            IMMUTABLE_ARRAY -> createImmutableArrays(sizeDistribution, fields, references)
        }
    }

    @TearDown
    fun tearDown() {
        listData = emptyArray()
        persistentListData = emptyArray()

        referenceArrays = emptyArray()
        booleanArrays = emptyArray()
        byteArrays = emptyArray()
        charArrays = emptyArray()
        shortArrays = emptyArray()
        intArrays = emptyArray()
        floatArrays = emptyArray()
        longArrays = emptyArray()
        doubleArrays = emptyArray()

        immutableReferenceArrays = emptyArray()
        immutableBooleanArrays = emptyArray()
        immutableByteArrays = emptyArray()
        immutableCharArrays = emptyArray()
        immutableShortArrays = emptyArray()
        immutableIntArrays = emptyArray()
        immutableFloatArrays = emptyArray()
        immutableLongArrays = emptyArray()
        immutableDoubleArrays = emptyArray()
    }

    @Suppress("UNCHECKED_CAST")
    @PublishedApi
    internal fun <T> typedListData(): Array<List<T>> = listData as Array<List<T>>

    @Suppress("UNCHECKED_CAST")
    @PublishedApi
    internal fun <T> typedPersistentListData(): Array<PersistentList<T>> {
        return persistentListData as Array<PersistentList<T>>
    }

    /**
     * Loops through all the collections of the current [CollectionType] and current [dataType] and performs the
     * associated operation consuming each result with the [Blackhole].
     *
     * E.g. If the current collectionType is [CollectionType.LIST] and dataType is [DataType.BOOLEAN], then it calls
     * [transformBooleanList] on each boolean list.
     */
    protected inline fun transformEachCollection(
        bh: Blackhole,
        transformList: (List<String>) -> Any?,
        transformBooleanList: (List<Boolean>) -> Any?,
        transformByteList: (List<Byte>) -> Any?,
        transformCharList: (List<Char>) -> Any?,
        transformShortList: (List<Short>) -> Any?,
        transformIntList: (List<Int>) -> Any?,
        transformFloatList: (List<Float>) -> Any?,
        transformLongList: (List<Long>) -> Any?,
        transformDoubleList: (List<Double>) -> Any?,
        transformPersistentList: (PersistentList<String>) -> Any?,
        transformPersistentBooleanList: (PersistentList<Boolean>) -> Any?,
        transformPersistentByteList: (PersistentList<Byte>) -> Any?,
        transformPersistentCharList: (PersistentList<Char>) -> Any?,
        transformPersistentShortList: (PersistentList<Short>) -> Any?,
        transformPersistentIntList: (PersistentList<Int>) -> Any?,
        transformPersistentFloatList: (PersistentList<Float>) -> Any?,
        transformPersistentLongList: (PersistentList<Long>) -> Any?,
        transformPersistentDoubleList: (PersistentList<Double>) -> Any?,
        transformArray: (Array<String>) -> Any?,
        transformBooleanArray: (BooleanArray) -> Any?,
        transformByteArray: (ByteArray) -> Any?,
        transformCharArray: (CharArray) -> Any?,
        transformShortArray: (ShortArray) -> Any?,
        transformIntArray: (IntArray) -> Any?,
        transformFloatArray: (FloatArray) -> Any?,
        transformLongArray: (LongArray) -> Any?,
        transformDoubleArray: (DoubleArray) -> Any?,
        transformImmutableArray: (ImmutableArray<String>) -> Any?,
        transformImmutableBooleanArray: (ImmutableBooleanArray) -> Any?,
        transformImmutableByteArray: (ImmutableByteArray) -> Any?,
        transformImmutableCharArray: (ImmutableCharArray) -> Any?,
        transformImmutableShortArray: (ImmutableShortArray) -> Any?,
        transformImmutableIntArray: (ImmutableIntArray) -> Any?,
        transformImmutableFloatArray: (ImmutableFloatArray) -> Any?,
        transformImmutableLongArray: (ImmutableLongArray) -> Any?,
        transformImmutableDoubleArray: (ImmutableDoubleArray) -> Any?,
    ) {
        when (collectionType) {
            LIST -> when (dataType) {
                REFERENCE -> typedListData<String>().forEach { bh.consume(transformList(it)) }
                BOOLEAN -> typedListData<Boolean>().forEach { bh.consume(transformBooleanList(it)) }
                BYTE -> typedListData<Byte>().forEach { bh.consume(transformByteList(it)) }
                CHAR -> typedListData<Char>().forEach { bh.consume(transformCharList(it)) }
                SHORT -> typedListData<Short>().forEach { bh.consume(transformShortList(it)) }
                INT -> typedListData<Int>().forEach { bh.consume(transformIntList(it)) }
                FLOAT -> typedListData<Float>().forEach { bh.consume(transformFloatList(it)) }
                LONG -> typedListData<Long>().forEach { bh.consume(transformLongList(it)) }
                DOUBLE -> typedListData<Double>().forEach { bh.consume(transformDoubleList(it)) }
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> typedPersistentListData<String>().forEach { bh.consume(transformPersistentList(it)) }
                BOOLEAN -> typedPersistentListData<Boolean>().forEach { bh.consume(transformPersistentBooleanList(it)) }
                BYTE -> typedPersistentListData<Byte>().forEach { bh.consume(transformPersistentByteList(it)) }
                CHAR -> typedPersistentListData<Char>().forEach { bh.consume(transformPersistentCharList(it)) }
                SHORT -> typedPersistentListData<Short>().forEach { bh.consume(transformPersistentShortList(it)) }
                INT -> typedPersistentListData<Int>().forEach { bh.consume(transformPersistentIntList(it)) }
                FLOAT -> typedPersistentListData<Float>().forEach { bh.consume(transformPersistentFloatList(it)) }
                LONG -> typedPersistentListData<Long>().forEach { bh.consume(transformPersistentLongList(it)) }
                DOUBLE -> typedPersistentListData<Double>().forEach { bh.consume(transformPersistentDoubleList(it)) }
            }

            ARRAY -> when (dataType) {
                REFERENCE -> referenceArrays.forEach { bh.consume(transformArray(it)) }
                BOOLEAN -> booleanArrays.forEach { bh.consume(transformBooleanArray(it)) }
                BYTE -> byteArrays.forEach { bh.consume(transformByteArray(it)) }
                CHAR -> charArrays.forEach { bh.consume(transformCharArray(it)) }
                SHORT -> shortArrays.forEach { bh.consume(transformShortArray(it)) }
                INT -> intArrays.forEach { bh.consume(transformIntArray(it)) }
                FLOAT -> floatArrays.forEach { bh.consume(transformFloatArray(it)) }
                LONG -> longArrays.forEach { bh.consume(transformLongArray(it)) }
                DOUBLE -> doubleArrays.forEach { bh.consume(transformDoubleArray(it)) }
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> immutableReferenceArrays.forEach { bh.consume(transformImmutableArray(it)) }
                BOOLEAN -> immutableBooleanArrays.forEach { bh.consume(transformImmutableBooleanArray(it)) }
                BYTE -> immutableByteArrays.forEach { bh.consume(transformImmutableByteArray(it)) }
                CHAR -> immutableCharArrays.forEach { bh.consume(transformImmutableCharArray(it)) }
                SHORT -> immutableShortArrays.forEach { bh.consume(transformImmutableShortArray(it)) }
                INT -> immutableIntArrays.forEach { bh.consume(transformImmutableIntArray(it)) }
                FLOAT -> immutableFloatArrays.forEach { bh.consume(transformImmutableFloatArray(it)) }
                LONG -> immutableLongArrays.forEach { bh.consume(transformImmutableLongArray(it)) }
                DOUBLE -> immutableDoubleArrays.forEach { bh.consume(transformImmutableDoubleArray(it)) }
            }
        }
    }

    /**
     * Loops through all pairs of collections of the current [CollectionType] and current [dataType] and performs the
     * associated operation consuming each result with the [Blackhole].
     *
     * E.g. If the current collectionType is [CollectionType.LIST] and dataType is [DataType.BOOLEAN], then it calls
     * [transformBooleanLists] on each pair of boolean lists.
     *
     * IMPORTANT:
     * Since this processes 2 collections at a time without re-using any of them, the number of operations per
     * benchmark iteration is half of [numCollections] so [OperationsPerInvocation] should be divided by 2.
     */
    protected inline fun transformEachPairOfCollections(
        bh: Blackhole,
        transformLists: (List<String>, List<String>) -> Any?,
        transformBooleanLists: (List<Boolean>, List<Boolean>) -> Any?,
        transformByteLists: (List<Byte>, List<Byte>) -> Any?,
        transformCharLists: (List<Char>, List<Char>) -> Any?,
        transformShortLists: (List<Short>, List<Short>) -> Any?,
        transformIntLists: (List<Int>, List<Int>) -> Any?,
        transformFloatLists: (List<Float>, List<Float>) -> Any?,
        transformLongLists: (List<Long>, List<Long>) -> Any?,
        transformDoubleLists: (List<Double>, List<Double>) -> Any?,
        transformPersistentLists: (PersistentList<String>, PersistentList<String>) -> Any?,
        transformPersistentBooleanLists: (PersistentList<Boolean>, PersistentList<Boolean>) -> Any?,
        transformPersistentByteLists: (PersistentList<Byte>, PersistentList<Byte>) -> Any?,
        transformPersistentCharLists: (PersistentList<Char>, PersistentList<Char>) -> Any?,
        transformPersistentShortLists: (PersistentList<Short>, PersistentList<Short>) -> Any?,
        transformPersistentIntLists: (PersistentList<Int>, PersistentList<Int>) -> Any?,
        transformPersistentFloatLists: (PersistentList<Float>, PersistentList<Float>) -> Any?,
        transformPersistentLongLists: (PersistentList<Long>, PersistentList<Long>) -> Any?,
        transformPersistentDoubleLists: (PersistentList<Double>, PersistentList<Double>) -> Any?,
        transformArrays: (Array<String>, Array<String>) -> Any?,
        transformBooleanArrays: (BooleanArray, BooleanArray) -> Any?,
        transformByteArrays: (ByteArray, ByteArray) -> Any?,
        transformCharArrays: (CharArray, CharArray) -> Any?,
        transformShortArrays: (ShortArray, ShortArray) -> Any?,
        transformIntArrays: (IntArray, IntArray) -> Any?,
        transformFloatArrays: (FloatArray, FloatArray) -> Any?,
        transformLongArrays: (LongArray, LongArray) -> Any?,
        transformDoubleArrays: (DoubleArray, DoubleArray) -> Any?,
        transformImmutableArrays: (ImmutableArray<String>, ImmutableArray<String>) -> Any?,
        transformImmutableBooleanArrays: (ImmutableBooleanArray, ImmutableBooleanArray) -> Any?,
        transformImmutableByteArrays: (ImmutableByteArray, ImmutableByteArray) -> Any?,
        transformImmutableCharArrays: (ImmutableCharArray, ImmutableCharArray) -> Any?,
        transformImmutableShortArrays: (ImmutableShortArray, ImmutableShortArray) -> Any?,
        transformImmutableIntArrays: (ImmutableIntArray, ImmutableIntArray) -> Any?,
        transformImmutableFloatArrays: (ImmutableFloatArray, ImmutableFloatArray) -> Any?,
        transformImmutableLongArrays: (ImmutableLongArray, ImmutableLongArray) -> Any?,
        transformImmutableDoubleArrays: (ImmutableDoubleArray, ImmutableDoubleArray) -> Any?,
    ) {
        when (collectionType) {
            LIST -> when (dataType) {
                REFERENCE -> consumePairs(bh, typedListData(), transformLists)
                BOOLEAN -> consumePairs(bh, typedListData(), transformBooleanLists)
                BYTE -> consumePairs(bh, typedListData(), transformByteLists)
                CHAR -> consumePairs(bh, typedListData(), transformCharLists)
                SHORT -> consumePairs(bh, typedListData(), transformShortLists)
                INT -> consumePairs(bh, typedListData(), transformIntLists)
                FLOAT -> consumePairs(bh, typedListData(), transformFloatLists)
                LONG -> consumePairs(bh, typedListData(), transformLongLists)
                DOUBLE -> consumePairs(bh, typedListData(), transformDoubleLists)
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> consumePairs(bh, typedPersistentListData<String>(), transformPersistentLists)
                BOOLEAN -> consumePairs(bh, typedPersistentListData<Boolean>(), transformPersistentBooleanLists)
                BYTE -> consumePairs(bh, typedPersistentListData<Byte>(), transformPersistentByteLists)
                CHAR -> consumePairs(bh, typedPersistentListData<Char>(), transformPersistentCharLists)
                SHORT -> consumePairs(bh, typedPersistentListData<Short>(), transformPersistentShortLists)
                INT -> consumePairs(bh, typedPersistentListData<Int>(), transformPersistentIntLists)
                FLOAT -> consumePairs(bh, typedPersistentListData<Float>(), transformPersistentFloatLists)
                LONG -> consumePairs(bh, typedPersistentListData<Long>(), transformPersistentLongLists)
                DOUBLE -> consumePairs(bh, typedPersistentListData<Double>(), transformPersistentDoubleLists)
            }

            ARRAY -> when (dataType) {
                REFERENCE -> consumePairs(bh, referenceArrays, transformArrays)
                BOOLEAN -> consumePairs(bh, booleanArrays, transformBooleanArrays)
                BYTE -> consumePairs(bh, byteArrays, transformByteArrays)
                CHAR -> consumePairs(bh, charArrays, transformCharArrays)
                SHORT -> consumePairs(bh, shortArrays, transformShortArrays)
                INT -> consumePairs(bh, intArrays, transformIntArrays)
                FLOAT -> consumePairs(bh, floatArrays, transformFloatArrays)
                LONG -> consumePairs(bh, longArrays, transformLongArrays)
                DOUBLE -> consumePairs(bh, doubleArrays, transformDoubleArrays)
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> consumePairs(bh, immutableReferenceArrays, transformImmutableArrays)
                BOOLEAN -> consumePairs(bh, immutableBooleanArrays, transformImmutableBooleanArrays)
                BYTE -> consumePairs(bh, immutableByteArrays, transformImmutableByteArrays)
                CHAR -> consumePairs(bh, immutableCharArrays, transformImmutableCharArrays)
                SHORT -> consumePairs(bh, immutableShortArrays, transformImmutableShortArrays)
                INT -> consumePairs(bh, immutableIntArrays, transformImmutableIntArrays)
                FLOAT -> consumePairs(bh, immutableFloatArrays, transformImmutableFloatArrays)
                LONG -> consumePairs(bh, immutableLongArrays, transformImmutableLongArrays)
                DOUBLE -> consumePairs(bh, immutableDoubleArrays, transformImmutableDoubleArrays)
            }
        }
    }

    @PublishedApi
    internal inline fun <T> consumePairs(
        bh: Blackhole,
        data: Array<T>,
        transform: (T, T) -> Any?,
    ) {
        for (i in 0..<data.lastIndex step 2) {
            bh.consume(transform(data[i], data[i + 1]))
        }
    }

    private fun createLists(
        sizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String>,
    ) {
        listData = Array(numCollections) {
            when (dataType) {
                REFERENCE -> createList(sizeDistribution.nextValue()) { references.next() }
                BOOLEAN -> createList(sizeDistribution.nextValue()) { fields.nextBoolean() }
                BYTE -> createList(sizeDistribution.nextValue()) { fields.nextByte() }
                CHAR -> createList(sizeDistribution.nextValue()) { fields.nextChar() }
                SHORT -> createList(sizeDistribution.nextValue()) { fields.nextShort() }
                INT -> createList(sizeDistribution.nextValue()) { fields.nextInt() }
                FLOAT -> createList(sizeDistribution.nextValue()) { fields.nextFloat() }
                LONG -> createList(sizeDistribution.nextValue()) { fields.nextLong() }
                DOUBLE -> createList(sizeDistribution.nextValue()) { fields.nextDouble() }
            }
        }
    }

    private fun createPersistentLists(
        sizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String>,
    ) {
        persistentListData = Array(numCollections) {
            when (dataType) {
                REFERENCE -> createPersistentList(sizeDistribution.nextValue()) { references.next() }
                BOOLEAN -> createPersistentList(sizeDistribution.nextValue()) { fields.nextBoolean() }
                BYTE -> createPersistentList(sizeDistribution.nextValue()) { fields.nextByte() }
                CHAR -> createPersistentList(sizeDistribution.nextValue()) { fields.nextChar() }
                SHORT -> createPersistentList(sizeDistribution.nextValue()) { fields.nextShort() }
                INT -> createPersistentList(sizeDistribution.nextValue()) { fields.nextInt() }
                FLOAT -> createPersistentList(sizeDistribution.nextValue()) { fields.nextFloat() }
                LONG -> createPersistentList(sizeDistribution.nextValue()) { fields.nextLong() }
                DOUBLE -> createPersistentList(sizeDistribution.nextValue()) { fields.nextDouble() }
            }
        }
    }

    private fun createArrays(
        sizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String>,
    ) {
        when (dataType) {
            REFERENCE -> referenceArrays = Array(numCollections) {
                Array(sizeDistribution.nextValue()) { references.next() }
            }

            BOOLEAN -> booleanArrays = Array(numCollections) {
                BooleanArray(sizeDistribution.nextValue()) { fields.nextBoolean() }
            }

            BYTE -> byteArrays = Array(numCollections) {
                ByteArray(sizeDistribution.nextValue()) { fields.nextByte() }
            }

            CHAR -> charArrays = Array(numCollections) {
                CharArray(sizeDistribution.nextValue()) { fields.nextChar() }
            }

            SHORT -> shortArrays = Array(numCollections) {
                ShortArray(sizeDistribution.nextValue()) { fields.nextShort() }
            }

            INT -> intArrays = Array(numCollections) {
                IntArray(sizeDistribution.nextValue()) { fields.nextInt() }
            }

            FLOAT -> floatArrays = Array(numCollections) {
                FloatArray(sizeDistribution.nextValue()) { fields.nextFloat() }
            }

            LONG -> longArrays = Array(numCollections) {
                LongArray(sizeDistribution.nextValue()) { fields.nextLong() }
            }

            DOUBLE -> doubleArrays = Array(numCollections) {
                DoubleArray(sizeDistribution.nextValue()) { fields.nextDouble() }
            }
        }
    }

    private fun createImmutableArrays(
        sizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String>,
    ) {
        when (dataType) {
            REFERENCE -> immutableReferenceArrays = Array(numCollections) {
                ImmutableArray(sizeDistribution.nextValue()) { references.next() }
            }

            BOOLEAN -> immutableBooleanArrays = Array(numCollections) {
                ImmutableBooleanArray(sizeDistribution.nextValue()) { fields.nextBoolean() }
            }

            BYTE -> immutableByteArrays = Array(numCollections) {
                ImmutableByteArray(sizeDistribution.nextValue()) { fields.nextByte() }
            }

            CHAR -> immutableCharArrays = Array(numCollections) {
                ImmutableCharArray(sizeDistribution.nextValue()) { fields.nextChar() }
            }

            SHORT -> immutableShortArrays = Array(numCollections) {
                ImmutableShortArray(sizeDistribution.nextValue()) { fields.nextShort() }
            }

            INT -> immutableIntArrays = Array(numCollections) {
                ImmutableIntArray(sizeDistribution.nextValue()) { fields.nextInt() }
            }

            FLOAT -> immutableFloatArrays = Array(numCollections) {
                ImmutableFloatArray(sizeDistribution.nextValue()) { fields.nextFloat() }
            }

            LONG -> immutableLongArrays = Array(numCollections) {
                ImmutableLongArray(sizeDistribution.nextValue()) { fields.nextLong() }
            }

            DOUBLE -> immutableDoubleArrays = Array(numCollections) {
                ImmutableDoubleArray(sizeDistribution.nextValue()) { fields.nextDouble() }
            }
        }
    }
}
