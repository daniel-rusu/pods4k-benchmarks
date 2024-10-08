package com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
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
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ArrayWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ImmutableArrayWrapperForDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers.ListWrapperForDataType
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
 * Represents a benchmark that measures the performance of operating on flat collections.  Note that the term collection
 * is used loosely to represent a [List], [Array], or [ImmutableArray] rather than the actual [Collection] interface.
 *
 * A flat collection represents a collection that stores one of the 8 base types, such as a collection of [Boolean]
 * values, or a collection containing simple reference types (with the most common reference type being [String]).
 *
 * Benchmarks are parameterized by each of the 8 base types, like [Boolean] / [Int] / [Float] / etc. plus [String].
 *
 * Subclasses should create 3 benchmark methods for measuring the performance of each of the 3 types of collections
 * and call the appropriate transformation function, like [transformEachList], providing the operations to be performed
 * for each collection of each data type.
 *
 * For example, if [numCollections] returns 500, and the data type being measured is [DataType.BOOLEAN], then 500
 * List<Boolean>, 500 BooleanArray, and 500 ImmutableBooleanArray collections will be created.  The provided boolean
 * collection transform, defined in the subclass benchmark, will be called for each of the 500 collections.
 */
@State(Scope.Benchmark)
abstract class FlatCollectionBenchmark {
    /**
     * Repeat the benchmark for each of the 8 base data types plus a String reference type.
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

    /**
     * Controls the sizes of the collections that will be generated.
     *
     * Try to use a distribution that represents sizes that are expected in the real world for the type of operation
     * being performed.
     */
    open val sizeDistribution: Distribution
        get() = Distribution.LIST_SIZE_DISTRIBUTION

    /**
     * Responsible for generated the element data that the collections will contain.
     */
    open val dataProducer: FlatDataProducer
        get() = FlatDataProducer.RandomDataProducer

    protected lateinit var arrays: Array<ArrayWrapperForDataType>

    protected lateinit var lists: Array<ListWrapperForDataType>

    protected lateinit var immutableArrays: Array<ImmutableArrayWrapperForDataType>

    @Setup(Level.Trial)
    fun setupCollections() {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)

        /*
        Add the appropriate collection-wrapper to each of the 3 arrays before continuing to add the next element so that
        none of them get an unfair cache boost from being populated more recently.  This is in contrast to creating
        an entire array of a particular collection wrapper

        Eg. Populate arrays[0], lists[0], immutableArrays[0], followed by arrays[1], lists[1], immutableArrays[1], etc.

        Since we c
         */
        @Suppress("UNCHECKED_CAST")
        arrays = arrayOfNulls<ArrayWrapperForDataType>(numCollections) as Array<ArrayWrapperForDataType>

        @Suppress("UNCHECKED_CAST")
        lists = arrayOfNulls<ListWrapperForDataType>(numCollections) as Array<ListWrapperForDataType>

        @Suppress("UNCHECKED_CAST")
        immutableArrays =
            arrayOfNulls<ImmutableArrayWrapperForDataType>(numCollections) as Array<ImmutableArrayWrapperForDataType>

        for (i in 0..<numCollections) {
            val size = sizeDistribution.nextValue(random)
            val arrayData = ArrayWrapperForDataType(
                size = size,
                random = random,
                dataType = dataType,
                dataProducer = dataProducer,
            )

            arrays[i] = arrayData
            immutableArrays[i] = ImmutableArrayWrapperForDataType(
                size = size,
                random = random,
                dataType = dataType,
                // copy the data from the regular array so that they are tested against identical data
                dataProducer = arrayData.copyData(),
            )
            lists[i] = ListWrapperForDataType(
                size = size,
                random = random,
                dataType = dataType,
                // copy the data from the regular array so that they are tested against identical data
                dataProducer = arrayData.copyData(),
            )
        }
    }

    @TearDown
    fun tearDown() {
    }

    /**
     * Loops through all the lists of the current [dataType] and performs the associated operation consuming each result
     * with the [Blackhole].
     *
     * E.g. If the current dataType is [DataType.BOOLEAN], then it calls [transformBooleanList] on each boolean list.
     */
    protected inline fun transformEachList(
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
    ) {
        when (dataType) {
            REFERENCE -> lists.forEach { bh.consume(transformList(it.referenceList)) }
            BOOLEAN -> lists.forEach { bh.consume(transformBooleanList(it.booleanList)) }
            BYTE -> lists.forEach { bh.consume(transformByteList(it.byteList)) }
            CHAR -> lists.forEach { bh.consume(transformCharList(it.charList)) }
            SHORT -> lists.forEach { bh.consume(transformShortList(it.shortList)) }
            INT -> lists.forEach { bh.consume(transformIntList(it.intList)) }
            FLOAT -> lists.forEach { bh.consume(transformFloatList(it.floatList)) }
            LONG -> lists.forEach { bh.consume(transformLongList(it.longList)) }
            DOUBLE -> lists.forEach { bh.consume(transformDoubleList(it.doubleList)) }
        }
    }

    /**
     * Loops through all the arrays of the current [dataType] and performs the associated operation consuming each
     * result with the [Blackhole].
     *
     * E.g. If the current dataType is [DataType.BOOLEAN], then it calls [transformBooleanArray] on each BooleanArray.
     */
    protected inline fun transformEachArray(
        bh: Blackhole,
        transformArray: (Array<String>) -> Any?,
        transformBooleanArray: (BooleanArray) -> Any?,
        transformByteArray: (ByteArray) -> Any?,
        transformCharArray: (CharArray) -> Any?,
        transformShortArray: (ShortArray) -> Any?,
        transformIntArray: (IntArray) -> Any?,
        transformFloatArray: (FloatArray) -> Any?,
        transformLongArray: (LongArray) -> Any?,
        transformDoubleArray: (DoubleArray) -> Any?,
    ) {
        when (dataType) {
            REFERENCE -> arrays.forEach { bh.consume(transformArray(it.referenceArray)) }
            BOOLEAN -> arrays.forEach { bh.consume(transformBooleanArray(it.booleanArray)) }
            BYTE -> arrays.forEach { bh.consume(transformByteArray(it.byteArray)) }
            CHAR -> arrays.forEach { bh.consume(transformCharArray(it.charArray)) }
            SHORT -> arrays.forEach { bh.consume(transformShortArray(it.shortArray)) }
            INT -> arrays.forEach { bh.consume(transformIntArray(it.intArray)) }
            FLOAT -> arrays.forEach { bh.consume(transformFloatArray(it.floatArray)) }
            LONG -> arrays.forEach { bh.consume(transformLongArray(it.longArray)) }
            DOUBLE -> arrays.forEach { bh.consume(transformDoubleArray(it.doubleArray)) }
        }
    }

    /**
     * Loops through all the immutable arrays for the current [dataType] and performs the associated operation
     * consuming each result with the [Blackhole].
     *
     * E.g. If the current dataType is [DataType.BOOLEAN], then it calls [transformImmutableBooleanArray] on each
     * ImmutableBooleanArray.
     */
    protected inline fun transformEachImmutableArray(
        bh: Blackhole,
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
        when (dataType) {
            REFERENCE -> immutableArrays.forEach { bh.consume(transformImmutableArray(it.immutableReferenceArray)) }
            BOOLEAN -> immutableArrays.forEach { bh.consume(transformImmutableBooleanArray(it.immutableBooleanArray)) }
            BYTE -> immutableArrays.forEach { bh.consume(transformImmutableByteArray(it.immutableByteArray)) }
            CHAR -> immutableArrays.forEach { bh.consume(transformImmutableCharArray(it.immutableCharArray)) }
            SHORT -> immutableArrays.forEach { bh.consume(transformImmutableShortArray(it.immutableShortArray)) }
            INT -> immutableArrays.forEach { bh.consume(transformImmutableIntArray(it.immutableIntArray)) }
            FLOAT -> immutableArrays.forEach { bh.consume(transformImmutableFloatArray(it.immutableFloatArray)) }
            LONG -> immutableArrays.forEach { bh.consume(transformImmutableLongArray(it.immutableLongArray)) }
            DOUBLE -> immutableArrays.forEach { bh.consume(transformImmutableDoubleArray(it.immutableDoubleArray)) }
        }
    }
}
