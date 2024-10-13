package com.danrusu.pods4kBenchmarks.immutableArrays

import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.flatMap
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.NestedCollections
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
    @Param
    private lateinit var dataType: DataType

    private lateinit var data: NestedCollections

    @Setup(Level.Trial)
    fun setupCollections() {
        data = NestedCollections(numCollections = NUM_COLLECTIONS, dataType = dataType)
    }

    @TearDown
    fun tearDown() {
    }

    @Benchmark
    fun listFlatMap(bh: Blackhole) {
        when (dataType) {
            REFERENCE -> data.transformEachList(bh) { list -> list.flatMap { it.referenceList } }
            BOOLEAN -> data.transformEachList(bh) { list -> list.flatMap { it.booleanList } }
            BYTE -> data.transformEachList(bh) { list -> list.flatMap { it.byteList } }
            CHAR -> data.transformEachList(bh) { list -> list.flatMap { it.charList } }
            SHORT -> data.transformEachList(bh) { list -> list.flatMap { it.shortList } }
            INT -> data.transformEachList(bh) { list -> list.flatMap { it.intList } }
            FLOAT -> data.transformEachList(bh) { list -> list.flatMap { it.floatList } }
            LONG -> data.transformEachList(bh) { list -> list.flatMap { it.longList } }
            DOUBLE -> data.transformEachList(bh) { list -> list.flatMap { it.doubleList } }
        }
    }

    @Benchmark
    fun arrayFlatMap(bh: Blackhole) {
        // array.flatMap requires a nested iterable, so we need to call asList() on each nested array
        // Note that asList() wraps the array without copying the data as it uses the same array as the backing data
        when (dataType) {
            REFERENCE -> data.transformEachArray(bh) { array -> array.flatMap { it.referenceArray.asList() } }
            BOOLEAN -> data.transformEachArray(bh) { array -> array.flatMap { it.booleanArray.asList() } }
            BYTE -> data.transformEachArray(bh) { array -> array.flatMap { it.byteArray.asList() } }
            CHAR -> data.transformEachArray(bh) { array -> array.flatMap { it.charArray.asList() } }
            SHORT -> data.transformEachArray(bh) { array -> array.flatMap { it.shortArray.asList() } }
            INT -> data.transformEachArray(bh) { array -> array.flatMap { it.intArray.asList() } }
            FLOAT -> data.transformEachArray(bh) { array -> array.flatMap { it.floatArray.asList() } }
            LONG -> data.transformEachArray(bh) { array -> array.flatMap { it.longArray.asList() } }
            DOUBLE -> data.transformEachArray(bh) { array -> array.flatMap { it.doubleArray.asList() } }
        }
    }

    @Benchmark
    fun immutableArrayFlatMap(bh: Blackhole) {
        when (dataType) {
            REFERENCE -> data.transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableReferenceArray } }
            BOOLEAN -> data.transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableBooleanArray } }
            BYTE -> data.transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableByteArray } }
            CHAR -> data.transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableCharArray } }
            SHORT -> data.transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableShortArray } }
            INT -> data.transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableIntArray } }
            FLOAT -> data.transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableFloatArray } }
            LONG -> data.transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableLongArray } }
            DOUBLE -> data.transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableDoubleArray } }
        }
    }
}
