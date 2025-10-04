package com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.FlatDataProducer
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.BooleanArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.BooleanListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ByteArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ByteListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.CharArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.CharListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.DoubleArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.DoubleListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.FloatArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.FloatListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ImmutableBooleanArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ImmutableByteArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ImmutableCharArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ImmutableDoubleArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ImmutableFloatArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ImmutableIntArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ImmutableLongArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ImmutableReferenceArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ImmutableShortArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.IntArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.IntListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.LongArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.LongListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ReferenceArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ReferenceListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ShortArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ShortListWrapper
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.openjdk.jmh.infra.Blackhole
import kotlin.random.Random

/**
 * Represents a benchmark that measures the performance of operating on nested collections, such as a list of orders
 * with each order containing a list of products.  Note that the term collection is used loosely to represent a [List],
 * [Array], or [ImmutableArray] rather than the actual [Collection] interface.
 */
@State(Scope.Benchmark)
abstract class NestedCollectionBenchmark {
    /** Repeat the benchmarks for each collection type */
    @Param
    protected lateinit var collectionType: CollectionType

    /**
     * Repeat the benchmarks for each of the 8 base data types plus a String reference type.  Each scenario will
     * prepare the data to have nested collections of this type.
     */
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

    /** Controls the sizes of the parent collections that will be generated */
    open val topLevelSizeDistributionFactory: DistributionFactory
        get() = DistributionFactory.ListSizeDistribution

    /** Controls the sizes of the nested collections that will be generated */
    open val nestedCollectionSizeDistributionFactory: DistributionFactory
        get() = DistributionFactory.NESTED_LIST_SIZE_DISTRIBUTION

    /** Responsible for generating the element data that the nested collections will store */
    open val nestedDataProducer: FlatDataProducer
        get() = FlatDataProducer.RandomDataProducer

    protected lateinit var data: Array<NestedCollectionWrapper>

    @Setup(Level.Trial)
    fun setupCollections() {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)
        val topLevelSizeDistribution = topLevelSizeDistributionFactory.create(random)
        val nestedSizeDistribution = nestedCollectionSizeDistributionFactory.create(random)

        data = Array(numCollections) {
            val numElements = topLevelSizeDistribution.nextValue()

            NestedCollectionWrapper(
                size = numElements,
                random = random,
                dataType = dataType,
                nestedCollectionSizeDistribution = nestedSizeDistribution,
                dataProducer = nestedDataProducer,
            )
        }
    }

    @TearDown
    fun tearDown() {
        data = emptyArray()
    }

    @Suppress("UNCHECKED_CAST")
    protected inline fun transformEachCollection(
        bh: Blackhole,

        // lists
        transformNestedLists: (List<ReferenceListWrapper>) -> Any?,
        transformNestedBooleanLists: (List<BooleanListWrapper>) -> Any?,
        transformNestedByteLists: (List<ByteListWrapper>) -> Any?,
        transformNestedCharLists: (List<CharListWrapper>) -> Any?,
        transformNestedShortLists: (List<ShortListWrapper>) -> Any?,
        transformNestedIntLists: (List<IntListWrapper>) -> Any?,
        transformNestedFloatLists: (List<FloatListWrapper>) -> Any?,
        transformNestedLongLists: (List<LongListWrapper>) -> Any?,
        transformNestedDoubleLists: (List<DoubleListWrapper>) -> Any?,

        // arrays
        transformNestedArrays: (Array<ReferenceArrayWrapper>) -> Any?,
        transformNestedBooleanArrays: (Array<BooleanArrayWrapper>) -> Any?,
        transformNestedByteArrays: (Array<ByteArrayWrapper>) -> Any?,
        transformNestedCharArrays: (Array<CharArrayWrapper>) -> Any?,
        transformNestedShortArrays: (Array<ShortArrayWrapper>) -> Any?,
        transformNestedIntArrays: (Array<IntArrayWrapper>) -> Any?,
        transformNestedFloatArrays: (Array<FloatArrayWrapper>) -> Any?,
        transformNestedLongArrays: (Array<LongArrayWrapper>) -> Any?,
        transformNestedDoubleArrays: (Array<DoubleArrayWrapper>) -> Any?,

        // immutable arrays
        transformNestedImmutableArrays: (ImmutableArray<ImmutableReferenceArrayWrapper>) -> Any?,
        transformNestedImmutableBooleanArrays: (ImmutableArray<ImmutableBooleanArrayWrapper>) -> Any?,
        transformNestedImmutableByteArrays: (ImmutableArray<ImmutableByteArrayWrapper>) -> Any?,
        transformNestedImmutableCharArrays: (ImmutableArray<ImmutableCharArrayWrapper>) -> Any?,
        transformNestedImmutableShortArrays: (ImmutableArray<ImmutableShortArrayWrapper>) -> Any?,
        transformNestedImmutableIntArrays: (ImmutableArray<ImmutableIntArrayWrapper>) -> Any?,
        transformNestedImmutableFloatArrays: (ImmutableArray<ImmutableFloatArrayWrapper>) -> Any?,
        transformNestedImmutableLongArrays: (ImmutableArray<ImmutableLongArrayWrapper>) -> Any?,
        transformNestedImmutableDoubleArrays: (ImmutableArray<ImmutableDoubleArrayWrapper>) -> Any?,
    ) = when (collectionType) {
        CollectionType.LIST -> when (dataType) {
            REFERENCE -> data.forEach { bh.consume(transformNestedLists(it.list as List<ReferenceListWrapper>)) }
            BOOLEAN -> data.forEach { bh.consume(transformNestedBooleanLists(it.list as List<BooleanListWrapper>)) }
            BYTE -> data.forEach { bh.consume(transformNestedByteLists(it.list as List<ByteListWrapper>)) }
            CHAR -> data.forEach { bh.consume(transformNestedCharLists(it.list as List<CharListWrapper>)) }
            SHORT -> data.forEach { bh.consume(transformNestedShortLists(it.list as List<ShortListWrapper>)) }
            INT -> data.forEach { bh.consume(transformNestedIntLists(it.list as List<IntListWrapper>)) }
            FLOAT -> data.forEach { bh.consume(transformNestedFloatLists(it.list as List<FloatListWrapper>)) }
            LONG -> data.forEach { bh.consume(transformNestedLongLists(it.list as List<LongListWrapper>)) }
            DOUBLE -> data.forEach { bh.consume(transformNestedDoubleLists(it.list as List<DoubleListWrapper>)) }
        }

        CollectionType.ARRAY -> when (dataType) {
            REFERENCE -> data.forEach { bh.consume(transformNestedArrays(it.array as Array<ReferenceArrayWrapper>)) }
            BOOLEAN -> data.forEach { bh.consume(transformNestedBooleanArrays(it.array as Array<BooleanArrayWrapper>)) }
            BYTE -> data.forEach { bh.consume(transformNestedByteArrays(it.array as Array<ByteArrayWrapper>)) }
            CHAR -> data.forEach { bh.consume(transformNestedCharArrays(it.array as Array<CharArrayWrapper>)) }
            SHORT -> data.forEach { bh.consume(transformNestedShortArrays(it.array as Array<ShortArrayWrapper>)) }
            INT -> data.forEach { bh.consume(transformNestedIntArrays(it.array as Array<IntArrayWrapper>)) }
            FLOAT -> data.forEach { bh.consume(transformNestedFloatArrays(it.array as Array<FloatArrayWrapper>)) }
            LONG -> data.forEach { bh.consume(transformNestedLongArrays(it.array as Array<LongArrayWrapper>)) }
            DOUBLE -> data.forEach { bh.consume(transformNestedDoubleArrays(it.array as Array<DoubleArrayWrapper>)) }
        }

        CollectionType.IMMUTABLE_ARRAY -> when (dataType) {
            REFERENCE -> data.forEach {
                val array = it.immutableArray as ImmutableArray<ImmutableReferenceArrayWrapper>
                bh.consume(transformNestedImmutableArrays(array))
            }

            BOOLEAN -> data.forEach {
                val array = it.immutableArray as ImmutableArray<ImmutableBooleanArrayWrapper>
                bh.consume(transformNestedImmutableBooleanArrays(array))
            }

            BYTE -> data.forEach {
                val array = it.immutableArray as ImmutableArray<ImmutableByteArrayWrapper>
                bh.consume(transformNestedImmutableByteArrays(array))
            }

            CHAR -> data.forEach {
                val array = it.immutableArray as ImmutableArray<ImmutableCharArrayWrapper>
                bh.consume(transformNestedImmutableCharArrays(array))
            }

            SHORT -> data.forEach {
                val array = it.immutableArray as ImmutableArray<ImmutableShortArrayWrapper>
                bh.consume(transformNestedImmutableShortArrays(array))
            }

            INT -> data.forEach {
                val array = it.immutableArray as ImmutableArray<ImmutableIntArrayWrapper>
                bh.consume(transformNestedImmutableIntArrays(array))
            }

            FLOAT -> data.forEach {
                val array = it.immutableArray as ImmutableArray<ImmutableFloatArrayWrapper>
                bh.consume(transformNestedImmutableFloatArrays(array))
            }

            LONG -> data.forEach {
                val array = it.immutableArray as ImmutableArray<ImmutableLongArrayWrapper>
                bh.consume(transformNestedImmutableLongArrays(array))
            }

            DOUBLE -> data.forEach {
                val array = it.immutableArray as ImmutableArray<ImmutableDoubleArrayWrapper>
                bh.consume(transformNestedImmutableDoubleArrays(array))
            }
        }
    }
}
