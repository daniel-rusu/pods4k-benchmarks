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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.CollectionWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ImmutableArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers.ListWrapper
import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.OperationsPerInvocation
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

    /** Responsible for generating the element data that the collections will contain */
    open val dataProducer: FlatDataProducer
        get() = FlatDataProducer.RandomDataProducer

    protected lateinit var data: Array<out CollectionWrapper>

    @Setup(Level.Trial)
    fun setupCollections() {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)
        val sizeDistribution = sizeDistributionFactory.create(random)

        data = when (collectionType) {
            LIST -> createLists(random, sizeDistribution)
            ARRAY -> createArrays(random, sizeDistribution)
            IMMUTABLE_ARRAY -> createImmutableArrays(random, sizeDistribution)
        }
    }

    @TearDown
    fun tearDown() {
        data = emptyArray()
    }

    private fun createLists(
        random: Random,
        sizeDistribution: Distribution
    ): Array<ListWrapper> = Array(numCollections) {
        ListWrapper.create(
            random = random,
            dataType = dataType,
            size = sizeDistribution.nextValue(),
            dataProducer = dataProducer,
        )
    }

    private fun createArrays(
        random: Random,
        sizeDistribution: Distribution
    ): Array<ArrayWrapper> = Array(numCollections) {
        ArrayWrapper.create(
            random = random,
            dataType = dataType,
            size = sizeDistribution.nextValue(),
            dataProducer = dataProducer,
        )
    }

    private fun createImmutableArrays(
        random: Random,
        sizeDistribution: Distribution
    ): Array<ImmutableArrayWrapper> = Array(numCollections) {
        ImmutableArrayWrapper.create(
            random = random,
            dataType = dataType,
            size = sizeDistribution.nextValue(),
            dataProducer = dataProducer,
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
                REFERENCE -> data.forEach { bh.consume(transformList(it.referenceList)) }
                BOOLEAN -> data.forEach { bh.consume(transformBooleanList(it.booleanList)) }
                BYTE -> data.forEach { bh.consume(transformByteList(it.byteList)) }
                CHAR -> data.forEach { bh.consume(transformCharList(it.charList)) }
                SHORT -> data.forEach { bh.consume(transformShortList(it.shortList)) }
                INT -> data.forEach { bh.consume(transformIntList(it.intList)) }
                FLOAT -> data.forEach { bh.consume(transformFloatList(it.floatList)) }
                LONG -> data.forEach { bh.consume(transformLongList(it.longList)) }
                DOUBLE -> data.forEach { bh.consume(transformDoubleList(it.doubleList)) }
            }

            ARRAY -> when (dataType) {
                REFERENCE -> data.forEach { bh.consume(transformArray(it.referenceArray)) }
                BOOLEAN -> data.forEach { bh.consume(transformBooleanArray(it.booleanArray)) }
                BYTE -> data.forEach { bh.consume(transformByteArray(it.byteArray)) }
                CHAR -> data.forEach { bh.consume(transformCharArray(it.charArray)) }
                SHORT -> data.forEach { bh.consume(transformShortArray(it.shortArray)) }
                INT -> data.forEach { bh.consume(transformIntArray(it.intArray)) }
                FLOAT -> data.forEach { bh.consume(transformFloatArray(it.floatArray)) }
                LONG -> data.forEach { bh.consume(transformLongArray(it.longArray)) }
                DOUBLE -> data.forEach { bh.consume(transformDoubleArray(it.doubleArray)) }
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> data.forEach { bh.consume(transformImmutableArray(it.immutableReferenceArray)) }
                BOOLEAN -> data.forEach { bh.consume(transformImmutableBooleanArray(it.immutableBooleanArray)) }
                BYTE -> data.forEach { bh.consume(transformImmutableByteArray(it.immutableByteArray)) }
                CHAR -> data.forEach { bh.consume(transformImmutableCharArray(it.immutableCharArray)) }
                SHORT -> data.forEach { bh.consume(transformImmutableShortArray(it.immutableShortArray)) }
                INT -> data.forEach { bh.consume(transformImmutableIntArray(it.immutableIntArray)) }
                FLOAT -> data.forEach { bh.consume(transformImmutableFloatArray(it.immutableFloatArray)) }
                LONG -> data.forEach { bh.consume(transformImmutableLongArray(it.immutableLongArray)) }
                DOUBLE -> data.forEach { bh.consume(transformImmutableDoubleArray(it.immutableDoubleArray)) }
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
                REFERENCE -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformLists(data[i].referenceList, data[i + 1].referenceList))
                    }
                }

                BOOLEAN -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformBooleanLists(data[i].booleanList, data[i + 1].booleanList))
                    }
                }

                BYTE -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformByteLists(data[i].byteList, data[i + 1].byteList))
                    }
                }

                CHAR -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformCharLists(data[i].charList, data[i + 1].charList))
                    }
                }

                SHORT -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformShortLists(data[i].shortList, data[i + 1].shortList))
                    }
                }

                INT -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformIntLists(data[i].intList, data[i + 1].intList))
                    }
                }

                FLOAT -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformFloatLists(data[i].floatList, data[i + 1].floatList))
                    }
                }

                LONG -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformLongLists(data[i].longList, data[i + 1].longList))
                    }
                }

                DOUBLE -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformDoubleLists(data[i].doubleList, data[i + 1].doubleList))
                    }
                }
            }

            ARRAY -> when (dataType) {
                REFERENCE -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformArrays(data[i].referenceArray, data[i + 1].referenceArray))
                    }
                }

                BOOLEAN -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformBooleanArrays(data[i].booleanArray, data[i + 1].booleanArray))
                    }
                }

                BYTE -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformByteArrays(data[i].byteArray, data[i + 1].byteArray))
                    }
                }

                CHAR -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformCharArrays(data[i].charArray, data[i + 1].charArray))
                    }
                }

                SHORT -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformShortArrays(data[i].shortArray, data[i + 1].shortArray))
                    }
                }

                INT -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformIntArrays(data[i].intArray, data[i + 1].intArray))
                    }
                }

                FLOAT -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformFloatArrays(data[i].floatArray, data[i + 1].floatArray))
                    }
                }

                LONG -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformLongArrays(data[i].longArray, data[i + 1].longArray))
                    }
                }

                DOUBLE -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(transformDoubleArrays(data[i].doubleArray, data[i + 1].doubleArray))
                    }
                }
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(
                            transformImmutableArrays(
                                data[i].immutableReferenceArray,
                                data[i + 1].immutableReferenceArray,
                            )
                        )
                    }
                }

                BOOLEAN -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(
                            transformImmutableBooleanArrays(
                                data[i].immutableBooleanArray,
                                data[i + 1].immutableBooleanArray,
                            )
                        )
                    }
                }

                BYTE -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(
                            transformImmutableByteArrays(
                                data[i].immutableByteArray,
                                data[i + 1].immutableByteArray,
                            )
                        )
                    }
                }

                CHAR -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(
                            transformImmutableCharArrays(
                                data[i].immutableCharArray,
                                data[i + 1].immutableCharArray,
                            )
                        )
                    }
                }

                SHORT -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(
                            transformImmutableShortArrays(
                                data[i].immutableShortArray,
                                data[i + 1].immutableShortArray,
                            )
                        )
                    }
                }

                INT -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(
                            transformImmutableIntArrays(
                                data[i].immutableIntArray,
                                data[i + 1].immutableIntArray,
                            )
                        )
                    }
                }

                FLOAT -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(
                            transformImmutableFloatArrays(
                                data[i].immutableFloatArray,
                                data[i + 1].immutableFloatArray,
                            )
                        )
                    }
                }

                LONG -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(
                            transformImmutableLongArrays(
                                data[i].immutableLongArray,
                                data[i + 1].immutableLongArray,
                            )
                        )
                    }
                }

                DOUBLE -> {
                    for (i in 0..<data.lastIndex step 2) { // exclude last index since we add 1
                        bh.consume(
                            transformImmutableDoubleArrays(
                                data[i].immutableDoubleArray,
                                data[i + 1].immutableDoubleArray,
                            )
                        )
                    }
                }
            }
        }
    }
}
