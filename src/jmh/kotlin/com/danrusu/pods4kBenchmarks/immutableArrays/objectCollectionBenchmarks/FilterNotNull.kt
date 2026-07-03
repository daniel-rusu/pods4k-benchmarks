package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.filterNotNull
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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.FlatDataProducerFactory
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.NullableDataProducer
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.NullableDataProducerFactory
import com.danrusu.pods4kBenchmarks.utils.ArrayCreator
import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OperationsPerInvocation
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

private const val NUM_COLLECTIONS = 1000

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
open class FilterNotNull {
    /** Repeat the benchmark for each collection type */
    @Param
    private lateinit var collectionType: CollectionType

    /** Repeat the benchmark for each of the 8 base data types plus a String reference type */
    @Param
    private lateinit var dataType: DataType

    private val sizeDistributionFactory = DistributionFactory.ListSizeDistribution

    /** Responsible for generating the element data that the collections will contain */
    private val dataProducerFactory = NullableDataProducerFactory(
        nullRatio = 0.5,
        flatDataProducerFactory = FlatDataProducerFactory.RandomDataProducerFactory,
    )

    /** Using a single list type for each value type as statically-typed lists will never make any difference*/
    private lateinit var listData: Array<List<Any?>>
    private lateinit var persistentListData: Array<PersistentList<Any?>>

    private lateinit var referenceArrays: Array<Array<String?>>
    private lateinit var booleanArrays: Array<Array<Boolean?>>
    private lateinit var byteArrays: Array<Array<Byte?>>
    private lateinit var charArrays: Array<Array<Char?>>
    private lateinit var shortArrays: Array<Array<Short?>>
    private lateinit var intArrays: Array<Array<Int?>>
    private lateinit var floatArrays: Array<Array<Float?>>
    private lateinit var longArrays: Array<Array<Long?>>
    private lateinit var doubleArrays: Array<Array<Double?>>

    private lateinit var immutableReferenceArrays: Array<ImmutableArray<String?>>
    private lateinit var immutableBooleanArrays: Array<ImmutableArray<Boolean?>>
    private lateinit var immutableByteArrays: Array<ImmutableArray<Byte?>>
    private lateinit var immutableCharArrays: Array<ImmutableArray<Char?>>
    private lateinit var immutableShortArrays: Array<ImmutableArray<Short?>>
    private lateinit var immutableIntArrays: Array<ImmutableArray<Int?>>
    private lateinit var immutableFloatArrays: Array<ImmutableArray<Float?>>
    private lateinit var immutableLongArrays: Array<ImmutableArray<Long?>>
    private lateinit var immutableDoubleArrays: Array<ImmutableArray<Double?>>

    @Setup(Level.Trial)
    fun setupCollections() {
        val rngFactory = RngFactory()
        val sizeDistribution = sizeDistributionFactory.create(rngFactory)
        val dataProducer = dataProducerFactory.create(rngFactory)

        when (collectionType) {
            LIST -> createLists(sizeDistribution, dataProducer)
            PERSISTENT_LIST -> createPersistentLists(sizeDistribution, dataProducer)
            ARRAY -> createArrays(sizeDistribution, dataProducer)
            IMMUTABLE_ARRAY -> createImmutableArrays(sizeDistribution, dataProducer)
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

    @Benchmark
    @Suppress("UNCHECKED_CAST")
    fun filterNotNull(bh: Blackhole) {
        when (collectionType) {
            LIST -> {
                // no need to check the dataType since we always use the same listData
                listData.forEach { bh.consume(it.filterNotNull()) }
            }

            PERSISTENT_LIST -> {
                // no need to check the dataType since we always use persistentListData
                persistentListData.forEach { bh.consume(it.filterNotNull()) }
            }

            ARRAY -> when (dataType) {
                REFERENCE -> referenceArrays.forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> booleanArrays.forEach { bh.consume(it.filterNotNull()) }
                BYTE -> byteArrays.forEach { bh.consume(it.filterNotNull()) }
                CHAR -> charArrays.forEach { bh.consume(it.filterNotNull()) }
                SHORT -> shortArrays.forEach { bh.consume(it.filterNotNull()) }
                INT -> intArrays.forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> floatArrays.forEach { bh.consume(it.filterNotNull()) }
                LONG -> longArrays.forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> doubleArrays.forEach { bh.consume(it.filterNotNull()) }
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> immutableReferenceArrays.forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> immutableBooleanArrays.forEach { bh.consume(it.filterNotNull()) }
                BYTE -> immutableByteArrays.forEach { bh.consume(it.filterNotNull()) }
                CHAR -> immutableCharArrays.forEach { bh.consume(it.filterNotNull()) }
                SHORT -> immutableShortArrays.forEach { bh.consume(it.filterNotNull()) }
                INT -> immutableIntArrays.forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> immutableFloatArrays.forEach { bh.consume(it.filterNotNull()) }
                LONG -> immutableLongArrays.forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> immutableDoubleArrays.forEach { bh.consume(it.filterNotNull()) }
            }
        }
    }

    private fun createLists(sizeDistribution: Distribution, dataProducer: NullableDataProducer) {
        listData = Array(NUM_COLLECTIONS) { index ->
            when (dataType) {
                REFERENCE -> createList(sizeDistribution.nextValue()) { dataProducer.nextReference() }
                BOOLEAN -> createList(sizeDistribution.nextValue()) { dataProducer.nextBoolean() }
                BYTE -> createList(sizeDistribution.nextValue()) { dataProducer.nextByte() }
                CHAR -> createList(sizeDistribution.nextValue()) { dataProducer.nextChar() }
                SHORT -> createList(sizeDistribution.nextValue()) { dataProducer.nextShort() }
                INT -> createList(sizeDistribution.nextValue()) { dataProducer.nextInt() }
                FLOAT -> createList(sizeDistribution.nextValue()) { dataProducer.nextFloat() }
                LONG -> createList(sizeDistribution.nextValue()) { dataProducer.nextLong() }
                DOUBLE -> createList(sizeDistribution.nextValue()) { dataProducer.nextDouble() }
            }
        }
    }

    private inline fun <T> createList(
        listSize: Int,
        initializer: () -> T,
    ): List<T> {
        val result = ArrayList<T>(listSize)
        repeat(listSize) { result.add(initializer()) }
        return result
    }

    private fun createPersistentLists(sizeDistribution: Distribution, dataProducer: NullableDataProducer) {
        persistentListData = Array(NUM_COLLECTIONS) { index ->
            when (dataType) {
                REFERENCE -> createPersistentList(sizeDistribution.nextValue()) { dataProducer.nextReference() }
                BOOLEAN -> createPersistentList(sizeDistribution.nextValue()) { dataProducer.nextBoolean() }
                BYTE -> createPersistentList(sizeDistribution.nextValue()) { dataProducer.nextByte() }
                CHAR -> createPersistentList(sizeDistribution.nextValue()) { dataProducer.nextChar() }
                SHORT -> createPersistentList(sizeDistribution.nextValue()) { dataProducer.nextShort() }
                INT -> createPersistentList(sizeDistribution.nextValue()) { dataProducer.nextInt() }
                FLOAT -> createPersistentList(sizeDistribution.nextValue()) { dataProducer.nextFloat() }
                LONG -> createPersistentList(sizeDistribution.nextValue()) { dataProducer.nextLong() }
                DOUBLE -> createPersistentList(sizeDistribution.nextValue()) { dataProducer.nextDouble() }
            }
        }
    }

    private inline fun <T> createPersistentList(listSize: Int, crossinline initializer: () -> T): PersistentList<T> {
        val builder = persistentListOf<T>().builder()
        repeat(listSize) { builder.add(initializer()) }
        return builder.build()
    }

    private fun createArrays(sizeDistribution: Distribution, dataProducer: NullableDataProducer) {
        if (dataType == REFERENCE) {
            referenceArrays = Array(NUM_COLLECTIONS) { index ->
                createArray(sizeDistribution.nextValue(), String::class.java) { dataProducer.nextReference() }
            }
        } else if (dataType == BOOLEAN) {
            booleanArrays = Array(NUM_COLLECTIONS) { index ->
                createArray(sizeDistribution.nextValue(), Boolean::class.java) { dataProducer.nextBoolean() }
            }
        } else if (dataType == BYTE) {
            byteArrays = Array(NUM_COLLECTIONS) { index ->
                createArray(sizeDistribution.nextValue(), Byte::class.java) { dataProducer.nextByte() }
            }
        } else if (dataType == CHAR) {
            charArrays = Array(NUM_COLLECTIONS) { index ->
                createArray(sizeDistribution.nextValue(), Char::class.java) { dataProducer.nextChar() }
            }
        } else if (dataType == SHORT) {
            shortArrays = Array(NUM_COLLECTIONS) { index ->
                createArray(sizeDistribution.nextValue(), Short::class.java) { dataProducer.nextShort() }
            }
        } else if (dataType == INT) {
            intArrays = Array(NUM_COLLECTIONS) { index ->
                createArray(sizeDistribution.nextValue(), Int::class.java) { dataProducer.nextInt() }
            }
        } else if (dataType == FLOAT) {
            floatArrays = Array(NUM_COLLECTIONS) { index ->
                createArray(sizeDistribution.nextValue(), Float::class.java) { dataProducer.nextFloat() }
            }
        } else if (dataType == LONG) {
            longArrays = Array(NUM_COLLECTIONS) { index ->
                createArray(sizeDistribution.nextValue(), Long::class.java) { dataProducer.nextLong() }
            }
        } else if (dataType == DOUBLE) {
            doubleArrays = Array(NUM_COLLECTIONS) { index ->
                createArray(sizeDistribution.nextValue(), Double::class.java) { dataProducer.nextDouble() }
            }
        }
    }

    private inline fun <T> createArray(
        size: Int,
        componentClass: Class<T & Any>,
        crossinline initializer: () -> T?,
    ): Array<T?> {
        return ArrayCreator.createArray(componentClass, size) { initializer() }
    }

    private fun createImmutableArrays(sizeDistribution: Distribution, dataProducer: NullableDataProducer) {
        if (dataType == REFERENCE) {
            immutableReferenceArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { dataProducer.nextReference() }
            }
        } else if (dataType == BOOLEAN) {
            immutableBooleanArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { dataProducer.nextBoolean() }
            }
        } else if (dataType == BYTE) {
            immutableByteArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { dataProducer.nextByte() }
            }
        } else if (dataType == CHAR) {
            immutableCharArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { dataProducer.nextChar() }
            }
        } else if (dataType == SHORT) {
            immutableShortArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { dataProducer.nextShort() }
            }
        } else if (dataType == INT) {
            immutableIntArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { dataProducer.nextInt() }
            }
        } else if (dataType == FLOAT) {
            immutableFloatArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { dataProducer.nextFloat() }
            }
        } else if (dataType == LONG) {
            immutableLongArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { dataProducer.nextLong() }
            }
        } else if (dataType == DOUBLE) {
            immutableDoubleArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { dataProducer.nextDouble() }
            }
        }
    }
}
