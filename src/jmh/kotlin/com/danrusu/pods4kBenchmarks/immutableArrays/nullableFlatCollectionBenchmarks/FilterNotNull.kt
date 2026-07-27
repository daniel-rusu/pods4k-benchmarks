package com.danrusu.pods4kBenchmarks.immutableArrays.nullableFlatCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.filterNotNull
import com.danrusu.pods4kBenchmarks.immutableArrays.nullableFlatCollectionBenchmarks.setup.NullableFlatCollectionBenchmarkData
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
import com.danrusu.pods4kBenchmarks.utils.generators.nullable
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
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

private const val NUM_COLLECTIONS = 1000
private const val NULL_RATIO = 0.5

/** Benchmarks `filterNotNull` across nullable reference and boxed primitive elements. */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class FilterNotNull {
    /** Repeats the benchmark for every collection representation. */
    @Param
    private lateinit var collectionType: CollectionType

    /** Repeats the benchmark for nullable references and all eight primitive wrapper types. */
    @Param
    private lateinit var dataType: DataType

    private lateinit var data: NullableFlatCollectionBenchmarkData

    @Setup(Level.Trial)
    fun setupBenchmarkData() {
        data = NullableFlatCollectionBenchmarkData.create(
            collectionType = collectionType,
            dataType = dataType,
            numCollections = NUM_COLLECTIONS,
            sizeDistributionFactory = DistributionFactory.ListSizeDistribution,
            fieldGeneratorFactory = FieldGeneratorFactory.withRandomNullableFields(NULL_RATIO),
            referenceGeneratorFactory = ObjectGeneratorFactory.randomStrings().nullable(NULL_RATIO),
        )
    }

    @Benchmark
    fun filterNotNull(bh: Blackhole) {
        when (collectionType) {
            LIST -> when (dataType) {
                REFERENCE -> data.lists<String>().forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> data.lists<Boolean>().forEach { bh.consume(it.filterNotNull()) }
                BYTE -> data.lists<Byte>().forEach { bh.consume(it.filterNotNull()) }
                CHAR -> data.lists<Char>().forEach { bh.consume(it.filterNotNull()) }
                SHORT -> data.lists<Short>().forEach { bh.consume(it.filterNotNull()) }
                INT -> data.lists<Int>().forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> data.lists<Float>().forEach { bh.consume(it.filterNotNull()) }
                LONG -> data.lists<Long>().forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> data.lists<Double>().forEach { bh.consume(it.filterNotNull()) }
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> data.persistentLists<String>().forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> data.persistentLists<Boolean>().forEach { bh.consume(it.filterNotNull()) }
                BYTE -> data.persistentLists<Byte>().forEach { bh.consume(it.filterNotNull()) }
                CHAR -> data.persistentLists<Char>().forEach { bh.consume(it.filterNotNull()) }
                SHORT -> data.persistentLists<Short>().forEach { bh.consume(it.filterNotNull()) }
                INT -> data.persistentLists<Int>().forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> data.persistentLists<Float>().forEach { bh.consume(it.filterNotNull()) }
                LONG -> data.persistentLists<Long>().forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> data.persistentLists<Double>().forEach { bh.consume(it.filterNotNull()) }
            }

            ARRAY -> when (dataType) {
                REFERENCE -> data.arrays<String>().forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> data.arrays<Boolean>().forEach { bh.consume(it.filterNotNull()) }
                BYTE -> data.arrays<Byte>().forEach { bh.consume(it.filterNotNull()) }
                CHAR -> data.arrays<Char>().forEach { bh.consume(it.filterNotNull()) }
                SHORT -> data.arrays<Short>().forEach { bh.consume(it.filterNotNull()) }
                INT -> data.arrays<Int>().forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> data.arrays<Float>().forEach { bh.consume(it.filterNotNull()) }
                LONG -> data.arrays<Long>().forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> data.arrays<Double>().forEach { bh.consume(it.filterNotNull()) }
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> data.immutableArrays<String>().forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> data.immutableArrays<Boolean>().forEach { bh.consume(it.filterNotNull()) }
                BYTE -> data.immutableArrays<Byte>().forEach { bh.consume(it.filterNotNull()) }
                CHAR -> data.immutableArrays<Char>().forEach { bh.consume(it.filterNotNull()) }
                SHORT -> data.immutableArrays<Short>().forEach { bh.consume(it.filterNotNull()) }
                INT -> data.immutableArrays<Int>().forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> data.immutableArrays<Float>().forEach { bh.consume(it.filterNotNull()) }
                LONG -> data.immutableArrays<Long>().forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> data.immutableArrays<Double>().forEach { bh.consume(it.filterNotNull()) }
            }
        }
    }
}
