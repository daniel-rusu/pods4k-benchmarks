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
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

/**
 * Represents a benchmark that measures the performance of operating on nested collections, such as a list of orders
 * with each order containing a list of products. Note that the term collection is used loosely to represent a [List],
 * [Array], or [ImmutableArray] rather than the actual [Collection] interface.
 */
@State(Scope.Benchmark)
abstract class NestedCollectionBenchmark(
    /**
     * The number of collections to benchmark against in order to avoid repeatedly operating on the same collection.
     */
    private val numCollections: Int,
    /** Controls the sizes of the parent collections that will be generated. */
    private val topLevelSizeDistributionFactory: DistributionFactory = DistributionFactory.ListSizeDistribution,
    /** Controls the sizes of the nested collections that will be generated. */
    private val nestedCollectionSizeDistributionFactory: DistributionFactory =
        DistributionFactory.NestedListSizeDistribution,
    /** Creates simple field generators for primitive nested collection elements. */
    private val nestedFieldGeneratorFactory: FieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
    /** Creates reference generators for nested collection elements. */
    private val nestedReferenceGeneratorFactory: ObjectGeneratorFactory<String> = ObjectGeneratorFactory.randomStrings(),
) {
    /** Repeat the benchmarks for each collection type. */
    @Param
    protected lateinit var collectionType: CollectionType

    /** Repeat the benchmarks for each of the eight base data types plus a String reference type. */
    @Param
    protected lateinit var dataType: DataType

    @PublishedApi
    internal lateinit var data: NestedCollectionBenchmarkData

    @Setup(Level.Trial)
    fun setupCollections() {
        data = NestedCollectionBenchmarkData.create(
            collectionType = collectionType,
            dataType = dataType,
            numCollections = numCollections,
            topLevelSizeDistributionFactory = topLevelSizeDistributionFactory,
            nestedCollectionSizeDistributionFactory = nestedCollectionSizeDistributionFactory,
            nestedFieldGeneratorFactory = nestedFieldGeneratorFactory,
            nestedReferenceGeneratorFactory = nestedReferenceGeneratorFactory,
        )
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
                REFERENCE -> data.typedListData<String>().forEach { bh.consume(transformNestedLists(it)) }
                BOOLEAN -> data.typedListData<Boolean>().forEach { bh.consume(transformNestedBooleanLists(it)) }
                BYTE -> data.typedListData<Byte>().forEach { bh.consume(transformNestedByteLists(it)) }
                CHAR -> data.typedListData<Char>().forEach { bh.consume(transformNestedCharLists(it)) }
                SHORT -> data.typedListData<Short>().forEach { bh.consume(transformNestedShortLists(it)) }
                INT -> data.typedListData<Int>().forEach { bh.consume(transformNestedIntLists(it)) }
                FLOAT -> data.typedListData<Float>().forEach { bh.consume(transformNestedFloatLists(it)) }
                LONG -> data.typedListData<Long>().forEach { bh.consume(transformNestedLongLists(it)) }
                DOUBLE -> data.typedListData<Double>().forEach { bh.consume(transformNestedDoubleLists(it)) }
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> data.typedPersistentListData<String>().forEach {
                    bh.consume(transformNestedPersistentLists(it))
                }

                BOOLEAN -> data.typedPersistentListData<Boolean>().forEach {
                    bh.consume(transformNestedPersistentBooleanLists(it))
                }

                BYTE -> data.typedPersistentListData<Byte>().forEach {
                    bh.consume(transformNestedPersistentByteLists(it))
                }

                CHAR -> data.typedPersistentListData<Char>().forEach {
                    bh.consume(transformNestedPersistentCharLists(it))
                }

                SHORT -> data.typedPersistentListData<Short>().forEach {
                    bh.consume(transformNestedPersistentShortLists(it))
                }

                INT -> data.typedPersistentListData<Int>().forEach {
                    bh.consume(transformNestedPersistentIntLists(it))
                }

                FLOAT -> data.typedPersistentListData<Float>().forEach {
                    bh.consume(transformNestedPersistentFloatLists(it))
                }

                LONG -> data.typedPersistentListData<Long>().forEach {
                    bh.consume(transformNestedPersistentLongLists(it))
                }

                DOUBLE -> data.typedPersistentListData<Double>().forEach {
                    bh.consume(transformNestedPersistentDoubleLists(it))
                }
            }

            ARRAY -> when (dataType) {
                REFERENCE -> data.referenceArrayData.forEach { bh.consume(transformNestedArrays(it)) }
                BOOLEAN -> data.booleanArrayData.forEach { bh.consume(transformNestedBooleanArrays(it)) }
                BYTE -> data.byteArrayData.forEach { bh.consume(transformNestedByteArrays(it)) }
                CHAR -> data.charArrayData.forEach { bh.consume(transformNestedCharArrays(it)) }
                SHORT -> data.shortArrayData.forEach { bh.consume(transformNestedShortArrays(it)) }
                INT -> data.intArrayData.forEach { bh.consume(transformNestedIntArrays(it)) }
                FLOAT -> data.floatArrayData.forEach { bh.consume(transformNestedFloatArrays(it)) }
                LONG -> data.longArrayData.forEach { bh.consume(transformNestedLongArrays(it)) }
                DOUBLE -> data.doubleArrayData.forEach { bh.consume(transformNestedDoubleArrays(it)) }
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> data.immutableReferenceArrayData.forEach {
                    bh.consume(transformNestedImmutableArrays(it))
                }
                BOOLEAN -> data.immutableBooleanArrayData.forEach {
                    bh.consume(transformNestedImmutableBooleanArrays(it))
                }
                BYTE -> data.immutableByteArrayData.forEach { bh.consume(transformNestedImmutableByteArrays(it)) }
                CHAR -> data.immutableCharArrayData.forEach { bh.consume(transformNestedImmutableCharArrays(it)) }
                SHORT -> data.immutableShortArrayData.forEach { bh.consume(transformNestedImmutableShortArrays(it)) }
                INT -> data.immutableIntArrayData.forEach { bh.consume(transformNestedImmutableIntArrays(it)) }
                FLOAT -> data.immutableFloatArrayData.forEach { bh.consume(transformNestedImmutableFloatArrays(it)) }
                LONG -> data.immutableLongArrayData.forEach { bh.consume(transformNestedImmutableLongArrays(it)) }
                DOUBLE -> data.immutableDoubleArrayData.forEach { bh.consume(transformNestedImmutableDoubleArrays(it)) }
            }
        }
    }

}
