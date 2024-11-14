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
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class DropLastBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun dropLast(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.dropLast(list.size / 2) },
            { list: List<Boolean> -> list.dropLast(list.size / 2) },
            { list: List<Byte> -> list.dropLast(list.size / 2) },
            { list: List<Char> -> list.dropLast(list.size / 2) },
            { list: List<Short> -> list.dropLast(list.size / 2) },
            { list: List<Int> -> list.dropLast(list.size / 2) },
            { list: List<Float> -> list.dropLast(list.size / 2) },
            { list: List<Long> -> list.dropLast(list.size / 2) },
            { list: List<Double> -> list.dropLast(list.size / 2) },

            // arrays
            { array: Array<String> -> array.dropLast(array.size / 2) },
            { array: BooleanArray -> array.dropLast(array.size / 2) },
            { array: ByteArray -> array.dropLast(array.size / 2) },
            { array: CharArray -> array.dropLast(array.size / 2) },
            { array: ShortArray -> array.dropLast(array.size / 2) },
            { array: IntArray -> array.dropLast(array.size / 2) },
            { array: FloatArray -> array.dropLast(array.size / 2) },
            { array: LongArray -> array.dropLast(array.size / 2) },
            { array: DoubleArray -> array.dropLast(array.size / 2) },

            // immutable arrays
            { array: ImmutableArray<String> -> array.dropLast(array.size / 2) },
            { array: ImmutableBooleanArray -> array.dropLast(array.size / 2) },
            { array: ImmutableByteArray -> array.dropLast(array.size / 2) },
            { array: ImmutableCharArray -> array.dropLast(array.size / 2) },
            { array: ImmutableShortArray -> array.dropLast(array.size / 2) },
            { array: ImmutableIntArray -> array.dropLast(array.size / 2) },
            { array: ImmutableFloatArray -> array.dropLast(array.size / 2) },
            { array: ImmutableLongArray -> array.dropLast(array.size / 2) },
            { array: ImmutableDoubleArray -> array.dropLast(array.size / 2) },
        )
    }
}
