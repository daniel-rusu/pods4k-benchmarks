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

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
open class FilterNotNull {
    /** Repeats the benchmark for every collection type. */
    @Param
    private lateinit var collectionType: CollectionType

    /** Repeats the benchmark for nullable references and all eight primitive wrapper types. */
    @Param
    private lateinit var dataType: DataType

    private lateinit var data: NullableFlatCollectionBenchmarkData

    @Setup(Level.Trial)
    fun setupCollections() {
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
                REFERENCE -> data.listData<String>().forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> data.listData<Boolean>().forEach { bh.consume(it.filterNotNull()) }
                BYTE -> data.listData<Byte>().forEach { bh.consume(it.filterNotNull()) }
                CHAR -> data.listData<Char>().forEach { bh.consume(it.filterNotNull()) }
                SHORT -> data.listData<Short>().forEach { bh.consume(it.filterNotNull()) }
                INT -> data.listData<Int>().forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> data.listData<Float>().forEach { bh.consume(it.filterNotNull()) }
                LONG -> data.listData<Long>().forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> data.listData<Double>().forEach { bh.consume(it.filterNotNull()) }
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> data.persistentListData<String>().forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> data.persistentListData<Boolean>().forEach { bh.consume(it.filterNotNull()) }
                BYTE -> data.persistentListData<Byte>().forEach { bh.consume(it.filterNotNull()) }
                CHAR -> data.persistentListData<Char>().forEach { bh.consume(it.filterNotNull()) }
                SHORT -> data.persistentListData<Short>().forEach { bh.consume(it.filterNotNull()) }
                INT -> data.persistentListData<Int>().forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> data.persistentListData<Float>().forEach { bh.consume(it.filterNotNull()) }
                LONG -> data.persistentListData<Long>().forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> data.persistentListData<Double>().forEach { bh.consume(it.filterNotNull()) }
            }

            ARRAY -> when (dataType) {
                REFERENCE -> data.arrayData<String>().forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> data.arrayData<Boolean>().forEach { bh.consume(it.filterNotNull()) }
                BYTE -> data.arrayData<Byte>().forEach { bh.consume(it.filterNotNull()) }
                CHAR -> data.arrayData<Char>().forEach { bh.consume(it.filterNotNull()) }
                SHORT -> data.arrayData<Short>().forEach { bh.consume(it.filterNotNull()) }
                INT -> data.arrayData<Int>().forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> data.arrayData<Float>().forEach { bh.consume(it.filterNotNull()) }
                LONG -> data.arrayData<Long>().forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> data.arrayData<Double>().forEach { bh.consume(it.filterNotNull()) }
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> data.immutableArrayData<String>().forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> data.immutableArrayData<Boolean>().forEach { bh.consume(it.filterNotNull()) }
                BYTE -> data.immutableArrayData<Byte>().forEach { bh.consume(it.filterNotNull()) }
                CHAR -> data.immutableArrayData<Char>().forEach { bh.consume(it.filterNotNull()) }
                SHORT -> data.immutableArrayData<Short>().forEach { bh.consume(it.filterNotNull()) }
                INT -> data.immutableArrayData<Int>().forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> data.immutableArrayData<Float>().forEach { bh.consume(it.filterNotNull()) }
                LONG -> data.immutableArrayData<Long>().forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> data.immutableArrayData<Double>().forEach { bh.consume(it.filterNotNull()) }
            }
        }
    }
}
