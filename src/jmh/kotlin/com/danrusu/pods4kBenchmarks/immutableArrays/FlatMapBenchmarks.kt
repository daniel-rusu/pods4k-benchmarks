package com.danrusu.pods4kBenchmarks.immutableArrays

import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.flatMap
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.NestedCollectionBenchmark
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
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OperationsPerInvocation
import org.openjdk.jmh.annotations.OutputTimeUnit
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
open class FlatMapBenchmarks : NestedCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun listFlatMap(bh: Blackhole) {
        when (dataType) {
            REFERENCE -> transformEachList(bh) { list -> list.flatMap { it.referenceList } }
            BOOLEAN -> transformEachList(bh) { list -> list.flatMap { it.booleanList } }
            BYTE -> transformEachList(bh) { list -> list.flatMap { it.byteList } }
            CHAR -> transformEachList(bh) { list -> list.flatMap { it.charList } }
            SHORT -> transformEachList(bh) { list -> list.flatMap { it.shortList } }
            INT -> transformEachList(bh) { list -> list.flatMap { it.intList } }
            FLOAT -> transformEachList(bh) { list -> list.flatMap { it.floatList } }
            LONG -> transformEachList(bh) { list -> list.flatMap { it.longList } }
            DOUBLE -> transformEachList(bh) { list -> list.flatMap { it.doubleList } }
        }
    }

    @Benchmark
    fun arrayFlatMap(bh: Blackhole) {
        // array.flatMap requires a nested iterable, so we need to call asList() on each nested array
        // Note that asList() wraps the array without copying the data as it uses the same array as the backing data
        when (dataType) {
            REFERENCE -> transformEachArray(bh) { array -> array.flatMap { it.referenceArray.asList() } }
            BOOLEAN -> transformEachArray(bh) { array -> array.flatMap { it.booleanArray.asList() } }
            BYTE -> transformEachArray(bh) { array -> array.flatMap { it.byteArray.asList() } }
            CHAR -> transformEachArray(bh) { array -> array.flatMap { it.charArray.asList() } }
            SHORT -> transformEachArray(bh) { array -> array.flatMap { it.shortArray.asList() } }
            INT -> transformEachArray(bh) { array -> array.flatMap { it.intArray.asList() } }
            FLOAT -> transformEachArray(bh) { array -> array.flatMap { it.floatArray.asList() } }
            LONG -> transformEachArray(bh) { array -> array.flatMap { it.longArray.asList() } }
            DOUBLE -> transformEachArray(bh) { array -> array.flatMap { it.doubleArray.asList() } }
        }
    }

    @Benchmark
    fun immutableArrayFlatMap(bh: Blackhole) {
        when (dataType) {
            REFERENCE -> transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableReferenceArray } }
            BOOLEAN -> transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableBooleanArray } }
            BYTE -> transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableByteArray } }
            CHAR -> transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableCharArray } }
            SHORT -> transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableShortArray } }
            INT -> transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableIntArray } }
            FLOAT -> transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableFloatArray } }
            LONG -> transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableLongArray } }
            DOUBLE -> transformEachImmutableArray(bh) { array -> array.flatMap { it.immutableDoubleArray } }
        }
    }
}
