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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
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
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.OperationsPerInvocation
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
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
 * For example, if [numCollections] is 500, and the collection type & data type pair being measured is
 * [CollectionType.LIST] & [DataType.BOOLEAN], then 500 List<Boolean> collections will be created.  When the subclass
 * calls [transformEachCollection], the provided boolean collection transform will be called for each of the 500
 * collections.
 */
@State(Scope.Benchmark)
abstract class FlatCollectionBenchmark(
    /**
     * The number of collections to benchmark against in order to avoid repeating the operation being measured from
     * being performed on the same collection repeatedly.  This number should be sufficiently large, like 1000, to
     * avoid misleading results from optimizations like caching etc.
     */
    private val numCollections: Int,
    /** Controls the sizes of the collections that will be generated */
    private val sizeDistributionFactory: DistributionFactory = DistributionFactory.ListSizeDistribution,
    /** Responsible for generating primitive field values that the collections will contain */
    private val fieldGeneratorFactory: FieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
    /** Responsible for generating reference values that the collections will contain */
    private val referenceGeneratorFactory: ObjectGeneratorFactory<String> = ObjectGeneratorFactory.randomStrings(),
) {
    /** Repeat the benchmark for each collection type */
    @Param
    protected lateinit var collectionType: CollectionType

    /** Repeat the benchmark for each of the 8 base data types plus a String reference type */
    @Param
    protected lateinit var dataType: DataType

    @PublishedApi
    internal lateinit var data: FlatCollectionBenchmarkData

    @Setup(Level.Trial)
    fun setupCollections() {
        data = FlatCollectionBenchmarkData.create(
            collectionType = collectionType,
            dataType = dataType,
            numCollections = numCollections,
            sizeDistributionFactory = sizeDistributionFactory,
            fieldGeneratorFactory = fieldGeneratorFactory,
            referenceGeneratorFactory = referenceGeneratorFactory,
        )
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
                REFERENCE -> data.listData<String>().forEach { bh.consume(transformList(it)) }
                BOOLEAN -> data.listData<Boolean>().forEach { bh.consume(transformBooleanList(it)) }
                BYTE -> data.listData<Byte>().forEach { bh.consume(transformByteList(it)) }
                CHAR -> data.listData<Char>().forEach { bh.consume(transformCharList(it)) }
                SHORT -> data.listData<Short>().forEach { bh.consume(transformShortList(it)) }
                INT -> data.listData<Int>().forEach { bh.consume(transformIntList(it)) }
                FLOAT -> data.listData<Float>().forEach { bh.consume(transformFloatList(it)) }
                LONG -> data.listData<Long>().forEach { bh.consume(transformLongList(it)) }
                DOUBLE -> data.listData<Double>().forEach { bh.consume(transformDoubleList(it)) }
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> data.persistentListData<String>().forEach { bh.consume(transformPersistentList(it)) }
                BOOLEAN -> data.persistentListData<Boolean>().forEach { bh.consume(transformPersistentBooleanList(it)) }
                BYTE -> data.persistentListData<Byte>().forEach { bh.consume(transformPersistentByteList(it)) }
                CHAR -> data.persistentListData<Char>().forEach { bh.consume(transformPersistentCharList(it)) }
                SHORT -> data.persistentListData<Short>().forEach { bh.consume(transformPersistentShortList(it)) }
                INT -> data.persistentListData<Int>().forEach { bh.consume(transformPersistentIntList(it)) }
                FLOAT -> data.persistentListData<Float>().forEach { bh.consume(transformPersistentFloatList(it)) }
                LONG -> data.persistentListData<Long>().forEach { bh.consume(transformPersistentLongList(it)) }
                DOUBLE -> data.persistentListData<Double>().forEach { bh.consume(transformPersistentDoubleList(it)) }
            }

            ARRAY -> when (dataType) {
                REFERENCE -> data.referenceArrays.forEach { bh.consume(transformArray(it)) }
                BOOLEAN -> data.booleanArrays.forEach { bh.consume(transformBooleanArray(it)) }
                BYTE -> data.byteArrays.forEach { bh.consume(transformByteArray(it)) }
                CHAR -> data.charArrays.forEach { bh.consume(transformCharArray(it)) }
                SHORT -> data.shortArrays.forEach { bh.consume(transformShortArray(it)) }
                INT -> data.intArrays.forEach { bh.consume(transformIntArray(it)) }
                FLOAT -> data.floatArrays.forEach { bh.consume(transformFloatArray(it)) }
                LONG -> data.longArrays.forEach { bh.consume(transformLongArray(it)) }
                DOUBLE -> data.doubleArrays.forEach { bh.consume(transformDoubleArray(it)) }
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> data.immutableReferenceArrays.forEach { bh.consume(transformImmutableArray(it)) }
                BOOLEAN -> data.immutableBooleanArrays.forEach { bh.consume(transformImmutableBooleanArray(it)) }
                BYTE -> data.immutableByteArrays.forEach { bh.consume(transformImmutableByteArray(it)) }
                CHAR -> data.immutableCharArrays.forEach { bh.consume(transformImmutableCharArray(it)) }
                SHORT -> data.immutableShortArrays.forEach { bh.consume(transformImmutableShortArray(it)) }
                INT -> data.immutableIntArrays.forEach { bh.consume(transformImmutableIntArray(it)) }
                FLOAT -> data.immutableFloatArrays.forEach { bh.consume(transformImmutableFloatArray(it)) }
                LONG -> data.immutableLongArrays.forEach { bh.consume(transformImmutableLongArray(it)) }
                DOUBLE -> data.immutableDoubleArrays.forEach { bh.consume(transformImmutableDoubleArray(it)) }
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
                REFERENCE -> consumePairs(bh, data.listData(), transformLists)
                BOOLEAN -> consumePairs(bh, data.listData(), transformBooleanLists)
                BYTE -> consumePairs(bh, data.listData(), transformByteLists)
                CHAR -> consumePairs(bh, data.listData(), transformCharLists)
                SHORT -> consumePairs(bh, data.listData(), transformShortLists)
                INT -> consumePairs(bh, data.listData(), transformIntLists)
                FLOAT -> consumePairs(bh, data.listData(), transformFloatLists)
                LONG -> consumePairs(bh, data.listData(), transformLongLists)
                DOUBLE -> consumePairs(bh, data.listData(), transformDoubleLists)
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> consumePairs(bh, data.persistentListData<String>(), transformPersistentLists)
                BOOLEAN -> consumePairs(bh, data.persistentListData<Boolean>(), transformPersistentBooleanLists)
                BYTE -> consumePairs(bh, data.persistentListData<Byte>(), transformPersistentByteLists)
                CHAR -> consumePairs(bh, data.persistentListData<Char>(), transformPersistentCharLists)
                SHORT -> consumePairs(bh, data.persistentListData<Short>(), transformPersistentShortLists)
                INT -> consumePairs(bh, data.persistentListData<Int>(), transformPersistentIntLists)
                FLOAT -> consumePairs(bh, data.persistentListData<Float>(), transformPersistentFloatLists)
                LONG -> consumePairs(bh, data.persistentListData<Long>(), transformPersistentLongLists)
                DOUBLE -> consumePairs(bh, data.persistentListData<Double>(), transformPersistentDoubleLists)
            }

            ARRAY -> when (dataType) {
                REFERENCE -> consumePairs(bh, data.referenceArrays, transformArrays)
                BOOLEAN -> consumePairs(bh, data.booleanArrays, transformBooleanArrays)
                BYTE -> consumePairs(bh, data.byteArrays, transformByteArrays)
                CHAR -> consumePairs(bh, data.charArrays, transformCharArrays)
                SHORT -> consumePairs(bh, data.shortArrays, transformShortArrays)
                INT -> consumePairs(bh, data.intArrays, transformIntArrays)
                FLOAT -> consumePairs(bh, data.floatArrays, transformFloatArrays)
                LONG -> consumePairs(bh, data.longArrays, transformLongArrays)
                DOUBLE -> consumePairs(bh, data.doubleArrays, transformDoubleArrays)
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> consumePairs(bh, data.immutableReferenceArrays, transformImmutableArrays)
                BOOLEAN -> consumePairs(bh, data.immutableBooleanArrays, transformImmutableBooleanArrays)
                BYTE -> consumePairs(bh, data.immutableByteArrays, transformImmutableByteArrays)
                CHAR -> consumePairs(bh, data.immutableCharArrays, transformImmutableCharArrays)
                SHORT -> consumePairs(bh, data.immutableShortArrays, transformImmutableShortArrays)
                INT -> consumePairs(bh, data.immutableIntArrays, transformImmutableIntArrays)
                FLOAT -> consumePairs(bh, data.immutableFloatArrays, transformImmutableFloatArrays)
                LONG -> consumePairs(bh, data.immutableLongArrays, transformImmutableLongArrays)
                DOUBLE -> consumePairs(bh, data.immutableDoubleArrays, transformImmutableDoubleArrays)
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
}
