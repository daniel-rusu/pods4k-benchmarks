package com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.BOOLEAN
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.BYTE
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.CHAR
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.DOUBLE
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.FLOAT
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.INT
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.LONG
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.REFERENCE
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.SHORT
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.BooleanArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.BooleanListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ByteArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ByteListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.CharArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.CharListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.DoubleArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.DoubleListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.FloatArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.FloatListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableBooleanArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableByteArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableCharArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableDoubleArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableFloatArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableIntArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableLongArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableReferenceArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableShortArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.IntArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.IntListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.LongArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.LongListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.NestedCollectionWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ReferenceArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ReferenceListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ShortArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ShortListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.FlatDataProducer
import com.danrusu.pods4kBenchmarks.utils.Distribution
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
    open val topLevelSizeDistribution: Distribution
        get() = Distribution.LIST_SIZE_DISTRIBUTION

    /** Controls the sizes of the nested collections that will be generated */
    open val nestedCollectionSizeDistribution: Distribution
        get() = Distribution.NESTED_LIST_SIZE_DISTRIBUTION

    /** Responsible for generating the element data that the nested collections will store */
    open val nestedDataProducer: FlatDataProducer
        get() = FlatDataProducer.RandomDataProducer

    protected lateinit var data: Array<NestedCollectionWrapperForDataType>

    @Setup(Level.Trial)
    fun setupCollections() {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)

        data = Array(numCollections) {
            val numElements = topLevelSizeDistribution.nextValue(random)

            NestedCollectionWrapperForDataType(
                size = numElements,
                random = random,
                dataType = dataType,
                nestedCollectionSizeDistribution = nestedCollectionSizeDistribution,
                dataProducer = nestedDataProducer,
            )
        }
    }

    @TearDown
    fun tearDown() {
    }

    protected inline fun transformEachList(
        bh: Blackhole,
        transformNestedLists: (List<ReferenceListWrapper>) -> Any?,
        transformNestedBooleanLists: (List<BooleanListWrapper>) -> Any?,
        transformNestedByteLists: (List<ByteListWrapper>) -> Any?,
        transformNestedCharLists: (List<CharListWrapper>) -> Any?,
        transformNestedShortLists: (List<ShortListWrapper>) -> Any?,
        transformNestedIntLists: (List<IntListWrapper>) -> Any?,
        transformNestedFloatLists: (List<FloatListWrapper>) -> Any?,
        transformNestedLongLists: (List<LongListWrapper>) -> Any?,
        transformNestedDoubleLists: (List<DoubleListWrapper>) -> Any?,
    ) {
        @Suppress("UNCHECKED_CAST")
        when (dataType) {
            REFERENCE -> {
                data.forEach { bh.consume(transformNestedLists(it.list as List<ReferenceListWrapper>)) }
            }

            BOOLEAN -> {
                data.forEach { bh.consume(transformNestedBooleanLists(it.list as List<BooleanListWrapper>)) }
            }

            BYTE -> {
                data.forEach { bh.consume(transformNestedByteLists(it.list as List<ByteListWrapper>)) }
            }

            CHAR -> {
                data.forEach { bh.consume(transformNestedCharLists(it.list as List<CharListWrapper>)) }
            }

            SHORT -> {
                data.forEach { bh.consume(transformNestedShortLists(it.list as List<ShortListWrapper>)) }
            }

            INT -> {
                data.forEach { bh.consume(transformNestedIntLists(it.list as List<IntListWrapper>)) }
            }

            FLOAT -> {
                data.forEach { bh.consume(transformNestedFloatLists(it.list as List<FloatListWrapper>)) }
            }

            LONG -> {
                data.forEach { bh.consume(transformNestedLongLists(it.list as List<LongListWrapper>)) }
            }

            DOUBLE -> {
                data.forEach { bh.consume(transformNestedDoubleLists(it.list as List<DoubleListWrapper>)) }
            }
        }
    }

    protected inline fun transformEachArray(
        bh: Blackhole,
        transformNestedArrays: (Array<ReferenceArrayWrapper>) -> Any?,
        transformNestedBooleanArrays: (Array<BooleanArrayWrapper>) -> Any?,
        transformNestedByteArrays: (Array<ByteArrayWrapper>) -> Any?,
        transformNestedCharArrays: (Array<CharArrayWrapper>) -> Any?,
        transformNestedShortArrays: (Array<ShortArrayWrapper>) -> Any?,
        transformNestedIntArrays: (Array<IntArrayWrapper>) -> Any?,
        transformNestedFloatArrays: (Array<FloatArrayWrapper>) -> Any?,
        transformNestedLongArrays: (Array<LongArrayWrapper>) -> Any?,
        transformNestedDoubleArrays: (Array<DoubleArrayWrapper>) -> Any?,
    ) {
        @Suppress("UNCHECKED_CAST")
        when (dataType) {
            REFERENCE -> {
                data.forEach { bh.consume(transformNestedArrays(it.array as Array<ReferenceArrayWrapper>)) }
            }

            BOOLEAN -> {
                data.forEach { bh.consume(transformNestedBooleanArrays(it.array as Array<BooleanArrayWrapper>)) }
            }

            BYTE -> {
                data.forEach { bh.consume(transformNestedByteArrays(it.array as Array<ByteArrayWrapper>)) }
            }

            CHAR -> {
                data.forEach { bh.consume(transformNestedCharArrays(it.array as Array<CharArrayWrapper>)) }
            }

            SHORT -> {
                data.forEach { bh.consume(transformNestedShortArrays(it.array as Array<ShortArrayWrapper>)) }
            }

            INT -> {
                data.forEach { bh.consume(transformNestedIntArrays(it.array as Array<IntArrayWrapper>)) }
            }

            FLOAT -> {
                data.forEach { bh.consume(transformNestedFloatArrays(it.array as Array<FloatArrayWrapper>)) }
            }

            LONG -> {
                data.forEach { bh.consume(transformNestedLongArrays(it.array as Array<LongArrayWrapper>)) }
            }

            DOUBLE -> {
                data.forEach { bh.consume(transformNestedDoubleArrays(it.array as Array<DoubleArrayWrapper>)) }
            }
        }
    }

    protected inline fun transformEachImmutableArray(
        bh: Blackhole,
        transformNestedArrays: (ImmutableArray<ImmutableReferenceArrayWrapper>) -> Any?,
        transformNestedBooleanArrays: (ImmutableArray<ImmutableBooleanArrayWrapper>) -> Any?,
        transformNestedByteArrays: (ImmutableArray<ImmutableByteArrayWrapper>) -> Any?,
        transformNestedCharArrays: (ImmutableArray<ImmutableCharArrayWrapper>) -> Any?,
        transformNestedShortArrays: (ImmutableArray<ImmutableShortArrayWrapper>) -> Any?,
        transformNestedIntArrays: (ImmutableArray<ImmutableIntArrayWrapper>) -> Any?,
        transformNestedFloatArrays: (ImmutableArray<ImmutableFloatArrayWrapper>) -> Any?,
        transformNestedLongArrays: (ImmutableArray<ImmutableLongArrayWrapper>) -> Any?,
        transformNestedDoubleArrays: (ImmutableArray<ImmutableDoubleArrayWrapper>) -> Any?,
    ) {
        @Suppress("UNCHECKED_CAST")
        when (dataType) {
            REFERENCE -> {
                data.forEach {
                    bh.consume(
                        transformNestedArrays(it.immutableArray as ImmutableArray<ImmutableReferenceArrayWrapper>)
                    )
                }
            }

            BOOLEAN -> {
                data.forEach {
                    bh.consume(
                        transformNestedBooleanArrays(it.immutableArray as ImmutableArray<ImmutableBooleanArrayWrapper>)
                    )
                }
            }

            BYTE -> {
                data.forEach {
                    bh.consume(
                        transformNestedByteArrays(it.immutableArray as ImmutableArray<ImmutableByteArrayWrapper>)
                    )
                }
            }

            CHAR -> {
                data.forEach {
                    bh.consume(
                        transformNestedCharArrays(it.immutableArray as ImmutableArray<ImmutableCharArrayWrapper>)
                    )
                }
            }

            SHORT -> {
                data.forEach {
                    bh.consume(
                        transformNestedShortArrays(it.immutableArray as ImmutableArray<ImmutableShortArrayWrapper>)
                    )
                }
            }

            INT -> {
                data.forEach {
                    bh.consume(
                        transformNestedIntArrays(it.immutableArray as ImmutableArray<ImmutableIntArrayWrapper>)
                    )
                }
            }

            FLOAT -> {
                data.forEach {
                    bh.consume(
                        transformNestedFloatArrays(it.immutableArray as ImmutableArray<ImmutableFloatArrayWrapper>)
                    )
                }
            }

            LONG -> {
                data.forEach {
                    bh.consume(
                        transformNestedLongArrays(it.immutableArray as ImmutableArray<ImmutableLongArrayWrapper>)
                    )
                }
            }

            DOUBLE -> {
                data.forEach {
                    bh.consume(
                        transformNestedDoubleArrays(it.immutableArray as ImmutableArray<ImmutableDoubleArrayWrapper>)
                    )
                }
            }
        }
    }
}
