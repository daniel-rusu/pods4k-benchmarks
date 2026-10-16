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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionBenchmark
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
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.OperationsPerInvocation
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.infra.Blackhole

/**
 * Base state for flat-collection benchmarks across every [CollectionType]/[DataType] combination.
 *
 * Each trial materializes [numCollections] collections containing strings or one of the eight primitive families.
 * Subclasses pass equivalent statically typed operations to [transformEachCollection] or
 * [transformEachPairOfCollections].
 */
abstract class FlatCollectionBenchmark(
    /** Number of distinct collections processed by each invocation, or paired without reuse. */
    private val numCollections: Int,
    /** Controls generated collection sizes. */
    private val sizeDistributionFactory: DistributionFactory = DistributionFactory.ListSizeDistribution,
    /** Creates primitive element values. */
    private val primitiveGeneratorFactory: PrimitiveGeneratorFactory = PrimitiveGeneratorFactory.randomValues(),
    /** Creates reference element values. */
    private val referenceGeneratorFactory: ObjectGeneratorFactory<String> = ObjectGeneratorFactory.randomStrings(),
) : CollectionBenchmark() {
    @PublishedApi
    internal lateinit var data: FlatCollectionBenchmarkData

    @Setup(Level.Trial)
    fun setupBenchmarkData() {
        data = FlatCollectionBenchmarkData.create(
            collectionType = collectionType,
            dataType = dataType,
            numCollections = numCollections,
            sizeDistributionFactory = sizeDistributionFactory,
            primitiveGeneratorFactory = primitiveGeneratorFactory,
            referenceGeneratorFactory = referenceGeneratorFactory,
        )
    }

    /**
     * Applies the transform selected by [collectionType] and [dataType] to every collection and consumes each result.
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
                REFERENCE -> for (collection in data.lists<String>()) bh.consume(transformList(collection))
                BOOLEAN -> for (collection in data.lists<Boolean>()) bh.consume(transformBooleanList(collection))
                BYTE -> for (collection in data.lists<Byte>()) bh.consume(transformByteList(collection))
                CHAR -> for (collection in data.lists<Char>()) bh.consume(transformCharList(collection))
                SHORT -> for (collection in data.lists<Short>()) bh.consume(transformShortList(collection))
                INT -> for (collection in data.lists<Int>()) bh.consume(transformIntList(collection))
                FLOAT -> for (collection in data.lists<Float>()) bh.consume(transformFloatList(collection))
                LONG -> for (collection in data.lists<Long>()) bh.consume(transformLongList(collection))
                DOUBLE -> for (collection in data.lists<Double>()) bh.consume(transformDoubleList(collection))
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> for (collection in data.persistentLists<String>()) bh.consume(transformPersistentList(collection))
                BOOLEAN -> for (collection in data.persistentLists<Boolean>()) bh.consume(transformPersistentBooleanList(collection))
                BYTE -> for (collection in data.persistentLists<Byte>()) bh.consume(transformPersistentByteList(collection))
                CHAR -> for (collection in data.persistentLists<Char>()) bh.consume(transformPersistentCharList(collection))
                SHORT -> for (collection in data.persistentLists<Short>()) bh.consume(transformPersistentShortList(collection))
                INT -> for (collection in data.persistentLists<Int>()) bh.consume(transformPersistentIntList(collection))
                FLOAT -> for (collection in data.persistentLists<Float>()) bh.consume(transformPersistentFloatList(collection))
                LONG -> for (collection in data.persistentLists<Long>()) bh.consume(transformPersistentLongList(collection))
                DOUBLE -> for (collection in data.persistentLists<Double>()) bh.consume(transformPersistentDoubleList(collection))
            }

            ARRAY -> when (dataType) {
                REFERENCE -> for (collection in data.referenceArrays) bh.consume(transformArray(collection))
                BOOLEAN -> for (collection in data.booleanArrays) bh.consume(transformBooleanArray(collection))
                BYTE -> for (collection in data.byteArrays) bh.consume(transformByteArray(collection))
                CHAR -> for (collection in data.charArrays) bh.consume(transformCharArray(collection))
                SHORT -> for (collection in data.shortArrays) bh.consume(transformShortArray(collection))
                INT -> for (collection in data.intArrays) bh.consume(transformIntArray(collection))
                FLOAT -> for (collection in data.floatArrays) bh.consume(transformFloatArray(collection))
                LONG -> for (collection in data.longArrays) bh.consume(transformLongArray(collection))
                DOUBLE -> for (collection in data.doubleArrays) bh.consume(transformDoubleArray(collection))
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> for (collection in data.immutableReferenceArrays) bh.consume(transformImmutableArray(collection))
                BOOLEAN -> for (collection in data.immutableBooleanArrays) bh.consume(transformImmutableBooleanArray(collection))
                BYTE -> for (collection in data.immutableByteArrays) bh.consume(transformImmutableByteArray(collection))
                CHAR -> for (collection in data.immutableCharArrays) bh.consume(transformImmutableCharArray(collection))
                SHORT -> for (collection in data.immutableShortArrays) bh.consume(transformImmutableShortArray(collection))
                INT -> for (collection in data.immutableIntArrays) bh.consume(transformImmutableIntArray(collection))
                FLOAT -> for (collection in data.immutableFloatArrays) bh.consume(transformImmutableFloatArray(collection))
                LONG -> for (collection in data.immutableLongArrays) bh.consume(transformImmutableLongArray(collection))
                DOUBLE -> for (collection in data.immutableDoubleArrays) bh.consume(transformImmutableDoubleArray(collection))
            }
        }
    }

    /**
     * Applies the transform selected by [collectionType] and [dataType] to adjacent collection pairs and consumes each
     * result.
     *
     * One invocation performs [numCollections] / 2 operations, which must match [OperationsPerInvocation].
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
                REFERENCE -> consumePairs(bh, data.lists(), transformLists)
                BOOLEAN -> consumePairs(bh, data.lists(), transformBooleanLists)
                BYTE -> consumePairs(bh, data.lists(), transformByteLists)
                CHAR -> consumePairs(bh, data.lists(), transformCharLists)
                SHORT -> consumePairs(bh, data.lists(), transformShortLists)
                INT -> consumePairs(bh, data.lists(), transformIntLists)
                FLOAT -> consumePairs(bh, data.lists(), transformFloatLists)
                LONG -> consumePairs(bh, data.lists(), transformLongLists)
                DOUBLE -> consumePairs(bh, data.lists(), transformDoubleLists)
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> consumePairs(bh, data.persistentLists<String>(), transformPersistentLists)
                BOOLEAN -> consumePairs(bh, data.persistentLists<Boolean>(), transformPersistentBooleanLists)
                BYTE -> consumePairs(bh, data.persistentLists<Byte>(), transformPersistentByteLists)
                CHAR -> consumePairs(bh, data.persistentLists<Char>(), transformPersistentCharLists)
                SHORT -> consumePairs(bh, data.persistentLists<Short>(), transformPersistentShortLists)
                INT -> consumePairs(bh, data.persistentLists<Int>(), transformPersistentIntLists)
                FLOAT -> consumePairs(bh, data.persistentLists<Float>(), transformPersistentFloatLists)
                LONG -> consumePairs(bh, data.persistentLists<Long>(), transformPersistentLongLists)
                DOUBLE -> consumePairs(bh, data.persistentLists<Double>(), transformPersistentDoubleLists)
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
