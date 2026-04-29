package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.filterNotNull
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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.NullableDataProducer
import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
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
import kotlin.random.Random

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
    private val dataProducer = NullableDataProducer(nullRatio = 0.5, FlatDataProducer.RandomDataProducer)

    /** Using a single list type for each value type as statically-typed lists will never make any difference*/
    private lateinit var listData: Array<List<Any?>>

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
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val dataRandom = Random(0)
        val nullnessRandom = Random(dataRandom.nextLong())
        val sizeDistribution = sizeDistributionFactory.create(dataRandom)

        when (collectionType) {
            LIST -> createLists(nullnessRandom, dataRandom, sizeDistribution)
            ARRAY -> createArrays(nullnessRandom, dataRandom, sizeDistribution)
            IMMUTABLE_ARRAY -> createImmutableArrays(nullnessRandom, dataRandom, sizeDistribution)
        }
    }

    @TearDown
    fun tearDown() {
        listData = emptyArray()

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
            LIST -> when (dataType) {
                REFERENCE -> listData.forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> listData.forEach { bh.consume(it.filterNotNull()) }
                BYTE -> listData.forEach { bh.consume(it.filterNotNull()) }
                CHAR -> listData.forEach { bh.consume(it.filterNotNull()) }
                SHORT -> listData.forEach { bh.consume(it.filterNotNull()) }
                INT -> listData.forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> listData.forEach { bh.consume(it.filterNotNull()) }
                LONG -> listData.forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> listData.forEach { bh.consume(it.filterNotNull()) }
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

    private fun createLists(nullnessRandom: Random, dataRandom: Random, sizeDistribution: Distribution) {
        listData = Array(NUM_COLLECTIONS) {
            val size = sizeDistribution.nextValue()
            when (dataType) {
                REFERENCE -> createList(size) { dataProducer.nextReference(it, nullnessRandom, dataRandom) }
                BOOLEAN -> createList(size) { dataProducer.nextBoolean(it, nullnessRandom, dataRandom) }
                BYTE -> createList(size) { dataProducer.nextByte(it, nullnessRandom, dataRandom) }
                CHAR -> createList(size) { dataProducer.nextChar(it, nullnessRandom, dataRandom) }
                SHORT -> createList(size) { dataProducer.nextShort(it, nullnessRandom, dataRandom) }
                INT -> createList(size) { dataProducer.nextInt(it, nullnessRandom, dataRandom) }
                FLOAT -> createList(size) { dataProducer.nextFloat(it, nullnessRandom, dataRandom) }
                LONG -> createList(size) { dataProducer.nextLong(it, nullnessRandom, dataRandom) }
                DOUBLE -> createList(size) { dataProducer.nextDouble(it, nullnessRandom, dataRandom) }
            }
        }
    }

    private inline fun <T> createList(size: Int, initializer: (index: Int) -> T): List<T> {
        return ArrayList<T>(size).apply {
            repeat(size) { add(initializer(it)) }
        }
    }

    private fun createArrays(nullnessRandom: Random, dataRandom: Random, sizeDistribution: Distribution) {
        if (dataType == REFERENCE) {
            referenceArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                Array(size) { dataProducer.nextReference(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == BOOLEAN) {
            booleanArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                Array(size) { dataProducer.nextBoolean(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == BYTE) {
            byteArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                Array(size) { dataProducer.nextByte(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == CHAR) {
            charArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                Array(size) { dataProducer.nextChar(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == SHORT) {
            shortArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                Array(size) { dataProducer.nextShort(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == INT) {
            intArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                Array(size) { dataProducer.nextInt(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == FLOAT) {
            floatArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                Array(size) { dataProducer.nextFloat(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == LONG) {
            longArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                Array(size) { dataProducer.nextLong(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == DOUBLE) {
            doubleArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                Array(size) { dataProducer.nextDouble(it, nullnessRandom, dataRandom) }
            }
        }
    }

    private fun createImmutableArrays(nullnessRandom: Random, dataRandom: Random, sizeDistribution: Distribution) {
        if (dataType == REFERENCE) {
            immutableReferenceArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                ImmutableArray(size) { dataProducer.nextReference(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == BOOLEAN) {
            immutableBooleanArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                ImmutableArray(size) { dataProducer.nextBoolean(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == BYTE) {
            immutableByteArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                ImmutableArray(size) { dataProducer.nextByte(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == CHAR) {
            immutableCharArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                ImmutableArray(size) { dataProducer.nextChar(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == SHORT) {
            immutableShortArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                ImmutableArray(size) { dataProducer.nextShort(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == INT) {
            immutableIntArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                ImmutableArray(size) { dataProducer.nextInt(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == FLOAT) {
            immutableFloatArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                ImmutableArray(size) { dataProducer.nextFloat(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == LONG) {
            immutableLongArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                ImmutableArray(size) { dataProducer.nextLong(it, nullnessRandom, dataRandom) }
            }
        } else if (dataType == DOUBLE) {
            immutableDoubleArrays = Array(NUM_COLLECTIONS) {
                val size = sizeDistribution.nextValue()
                ImmutableArray(size) { dataProducer.nextDouble(it, nullnessRandom, dataRandom) }
            }
        }
    }
}
