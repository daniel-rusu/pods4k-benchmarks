package com.danrusu.pods4kBenchmarks.immutableArrays

import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.flatMap
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.NestedCollectionsByDataType
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

private const val NUM_COLLECTIONS = 250

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
@State(Scope.Benchmark)
open class FlatMapBenchmarks {
    @Param("REFERENCE", "BOOLEAN", "BYTE", "CHAR", "SHORT", "INT", "FLOAT", "LONG", "DOUBLE")
    private lateinit var dataType: DataType

    private lateinit var data: NestedCollectionsByDataType

    @Setup(Level.Trial)
    fun setupCollections() {
        data = NestedCollectionsByDataType(numCollections = NUM_COLLECTIONS, dataType = dataType)
    }

    @TearDown
    fun tearDown() {
    }

    @Benchmark
    fun listFlatMap(bh: Blackhole) {
        when (dataType) {
            REFERENCE -> data.forEachList { list -> bh.consume(list.flatMap { it.referenceList }) }
            BOOLEAN -> data.forEachList { list -> bh.consume(list.flatMap { it.booleanList }) }
            BYTE -> data.forEachList { list -> bh.consume(list.flatMap { it.byteList }) }
            CHAR -> data.forEachList { list -> bh.consume(list.flatMap { it.charList }) }
            SHORT -> data.forEachList { list -> bh.consume(list.flatMap { it.shortList }) }
            INT -> data.forEachList { list -> bh.consume(list.flatMap { it.intList }) }
            FLOAT -> data.forEachList { list -> bh.consume(list.flatMap { it.floatList }) }
            LONG -> data.forEachList { list -> bh.consume(list.flatMap { it.longList }) }
            DOUBLE -> data.forEachList { list -> bh.consume(list.flatMap { it.doubleList }) }
        }
    }

    @Benchmark
    fun arrayFlatMap(bh: Blackhole) {
        // array.flatMap requires a nested iterable, so we need to call asList() on each nested array
        // Note that asList() wraps the array without copying the data as it uses the same array as the backing data
        when (dataType) {
            REFERENCE -> data.forEachArray { array -> bh.consume(array.flatMap { it.referenceArray.asList() }) }
            BOOLEAN -> data.forEachArray { array -> bh.consume(array.flatMap { it.booleanArray.asList() }) }
            BYTE -> data.forEachArray { array -> bh.consume(array.flatMap { it.byteArray.asList() }) }
            CHAR -> data.forEachArray { array -> bh.consume(array.flatMap { it.charArray.asList() }) }
            SHORT -> data.forEachArray { array -> bh.consume(array.flatMap { it.shortArray.asList() }) }
            INT -> data.forEachArray { array -> bh.consume(array.flatMap { it.intArray.asList() }) }
            FLOAT -> data.forEachArray { array -> bh.consume(array.flatMap { it.floatArray.asList() }) }
            LONG -> data.forEachArray { array -> bh.consume(array.flatMap { it.longArray.asList() }) }
            DOUBLE -> data.forEachArray { array -> bh.consume(array.flatMap { it.doubleArray.asList() }) }
        }
    }

    @Benchmark
    fun immutableArrayFlatMap(bh: Blackhole) {
        when (dataType) {
            REFERENCE -> data.forEachImmutableArray { array ->
                bh.consume(array.flatMap { it.immutableReferenceArray })
            }

            BOOLEAN -> data.forEachImmutableArray { array -> bh.consume(array.flatMap { it.immutableBooleanArray }) }
            BYTE -> data.forEachImmutableArray { array -> bh.consume(array.flatMap { it.immutableByteArray }) }
            CHAR -> data.forEachImmutableArray { array -> bh.consume(array.flatMap { it.immutableCharArray }) }
            SHORT -> data.forEachImmutableArray { array -> bh.consume(array.flatMap { it.immutableShortArray }) }
            INT -> data.forEachImmutableArray { array -> bh.consume(array.flatMap { it.immutableIntArray }) }
            FLOAT -> data.forEachImmutableArray { array -> bh.consume(array.flatMap { it.immutableFloatArray }) }
            LONG -> data.forEachImmutableArray { array -> bh.consume(array.flatMap { it.immutableLongArray }) }
            DOUBLE -> data.forEachImmutableArray { array -> bh.consume(array.flatMap { it.immutableDoubleArray }) }
        }
    }
}
