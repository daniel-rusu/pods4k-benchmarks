package com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
import com.danrusu.pods4k.immutableArrays.plus
import com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks.setup.FlatCollectionBenchmark
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

private const val NUM_COLLECTIONS = 1000

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS / 2)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
open class PlusCollectionBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun plusCollection(bh: Blackhole) {
        transformEachPairOfCollections(
            bh,
            // lists
            { first: List<String>, second: List<String> -> first + second },
            { first: List<Boolean>, second: List<Boolean> -> first + second },
            { first: List<Byte>, second: List<Byte> -> first + second },
            { first: List<Char>, second: List<Char> -> first + second },
            { first: List<Short>, second: List<Short> -> first + second },
            { first: List<Int>, second: List<Int> -> first + second },
            { first: List<Float>, second: List<Float> -> first + second },
            { first: List<Long>, second: List<Long> -> first + second },
            { first: List<Double>, second: List<Double> -> first + second },

            // arrays
            { first: Array<String>, second: Array<String> -> first + second },
            { first: BooleanArray, second: BooleanArray -> first + second },
            { first: ByteArray, second: ByteArray -> first + second },
            { first: CharArray, second: CharArray -> first + second },
            { first: ShortArray, second: ShortArray -> first + second },
            { first: IntArray, second: IntArray -> first + second },
            { first: FloatArray, second: FloatArray -> first + second },
            { first: LongArray, second: LongArray -> first + second },
            { first: DoubleArray, second: DoubleArray -> first + second },

            // immutable arrays
            { first: ImmutableArray<String>, second: ImmutableArray<String> -> first + second },
            { first: ImmutableBooleanArray, second: ImmutableBooleanArray -> first + second },
            { first: ImmutableByteArray, second: ImmutableByteArray -> first + second },
            { first: ImmutableCharArray, second: ImmutableCharArray -> first + second },
            { first: ImmutableShortArray, second: ImmutableShortArray -> first + second },
            { first: ImmutableIntArray, second: ImmutableIntArray -> first + second },
            { first: ImmutableFloatArray, second: ImmutableFloatArray -> first + second },
            { first: ImmutableLongArray, second: ImmutableLongArray -> first + second },
            { first: ImmutableDoubleArray, second: ImmutableDoubleArray -> first + second },
        )
    }
}
