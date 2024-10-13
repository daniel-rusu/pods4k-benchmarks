package com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.NestedCollectionWrapperForDataType
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

    protected inline fun transformEachList(bh: Blackhole, body: (List<ListWrapper>) -> Any?) {
        data.forEach { bh.consume(body(it.list)) }
    }

    protected inline fun transformEachArray(bh: Blackhole, body: (Array<ArrayWrapper>) -> Any?) {
        data.forEach { bh.consume(body(it.array)) }
    }

    protected inline fun transformEachImmutableArray(
        bh: Blackhole,
        body: (ImmutableArray<ImmutableArrayWrapper>) -> Any?
    ) {
        data.forEach { bh.consume(body(it.immutableArray)) }
    }
}
