package com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.WrapperForCollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.ObjectProducer
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
 * Represents a benchmark that measures the performance of operating on collections that store objects (not the 8 base
 * types).  Note that the term collection is used loosely to represent a [List], [Array], or [ImmutableArray] rather
 * than the actual [Collection] interface.
 *
 * Benchmarks are parameterized by each of the 3 [CollectionType] values.
 *
 * Subclasses should create 9 benchmark methods for measuring the performance of an operation for each of the 8 base
 * types (like Boolean, Int, etc.) plus the most common reference type, String.  The benchmark methods should call
 * [transformEachCollection] providing the operations to be performed for each collection type.
 *
 * For example, if [numCollections] returns 500, and the data type being measured is [DataType.BOOLEAN], then 500
 * List<Boolean>, 500 BooleanArray, and 500 ImmutableBooleanArray collections will be created.  The provided list
 * transform, defined in [transformEachCollection], will be called for each of the 500 lists.
 */
@State(Scope.Benchmark)
abstract class ObjectCollectionBenchmark<T> {
    /**
     * Repeat the benchmark for each of the 8 base data types plus a String reference type.
     */
    @Param
    protected lateinit var collectionType: CollectionType

    /**
     * The number of collections to benchmark against in order to avoid repeating the operation being measured from
     * being performed on the same collection repeatedly.  This number should be sufficiently large, like 1000, to
     * avoid misleading results from optimizations like caching etc.
     *
     * Contract: The subclass overriding this value must return a fixed value that never changes.
     */
    abstract val numCollections: Int

    /** Controls the sizes of the collections that will be generated */
    open val sizeDistribution: Distribution
        get() = Distribution.LIST_SIZE_DISTRIBUTION

    /** Responsible for generated the element data that the collections will contain */
    abstract val elementProducer: Pair<ObjectProducer<T>, Class<T>>

    /**
     * Note: We're storing an array of [WrapperForCollectionType] instances with each wrapper storing the appropriate
     * collection instead of 3 separate arrays of collections.  This is because we want to avoid auto-boxing the
     * immutable arrays as that would create misleading results because immutable arrays are typically stored in
     * strongly-typed variables of type [ImmutableArray].
     */
    lateinit var data: Array<WrapperForCollectionType<T>>

    @Setup(Level.Trial)
    fun setupCollections() {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)

        val (objectProducer, objectClass) = elementProducer

        data = Array(numCollections) {
            val numElements = sizeDistribution.nextValue(random)

            WrapperForCollectionType(
                random = random,
                size = numElements,
                collectionType = collectionType,
                objectProducer = objectProducer,
                objectClass = objectClass,
            )
        }
    }

    @TearDown
    fun tearDown() {
    }

    /**
     * Loops through all the collections of the current [collectionType] and performs the associated operation
     * consuming each result with the [Blackhole].
     *
     * E.g. If the current collectionType is [CollectionType.ARRAY], then it calls [transformArray] on each array.
     */
    protected inline fun transformEachCollection(
        bh: Blackhole,
        transformList: (List<T>) -> Any?,
        transformArray: (Array<T>) -> Any?,
        transformImmutableArray: (ImmutableArray<T>) -> Any?,
    ) {
        when (collectionType) {
            LIST -> data.forEach { bh.consume(transformList(it.list)) }
            ARRAY -> data.forEach { bh.consume(transformArray(it.array)) }
            IMMUTABLE_ARRAY -> data.forEach { bh.consume(transformImmutableArray(it.immutableArray)) }
        }
    }
}
