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
import com.danrusu.pods4k.immutableArrays.groupBy
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
@Warmup(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
open class GroupByBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun groupBy(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.groupBy { it.toGroup() } },
            { list: List<Boolean> -> list.groupBy { it.toGroup() } },
            { list: List<Byte> -> list.groupBy { it.toGroup() } },
            { list: List<Char> -> list.groupBy { it.toGroup() } },
            { list: List<Short> -> list.groupBy { it.toGroup() } },
            { list: List<Int> -> list.groupBy { it.toGroup() } },
            { list: List<Float> -> list.groupBy { it.toGroup() } },
            { list: List<Long> -> list.groupBy { it.toGroup() } },
            { list: List<Double> -> list.groupBy { it.toGroup() } },

            // arrays
            { array: Array<String> -> array.groupBy { it.toGroup() } },
            { array: BooleanArray -> array.groupBy { it.toGroup() } },
            { array: ByteArray -> array.groupBy { it.toGroup() } },
            { array: CharArray -> array.groupBy { it.toGroup() } },
            { array: ShortArray -> array.groupBy { it.toGroup() } },
            { array: IntArray -> array.groupBy { it.toGroup() } },
            { array: FloatArray -> array.groupBy { it.toGroup() } },
            { array: LongArray -> array.groupBy { it.toGroup() } },
            { array: DoubleArray -> array.groupBy { it.toGroup() } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.groupBy { it.toGroup() } },
            { array: ImmutableBooleanArray -> array.groupBy { it.toGroup() } },
            { array: ImmutableByteArray -> array.groupBy { it.toGroup() } },
            { array: ImmutableCharArray -> array.groupBy { it.toGroup() } },
            { array: ImmutableShortArray -> array.groupBy { it.toGroup() } },
            { array: ImmutableIntArray -> array.groupBy { it.toGroup() } },
            { array: ImmutableFloatArray -> array.groupBy { it.toGroup() } },
            { array: ImmutableLongArray -> array.groupBy { it.toGroup() } },
            { array: ImmutableDoubleArray -> array.groupBy { it.toGroup() } },
        )
    }
}

private inline fun Int.toGroup(): Int {
    // mod 16 since N mod (2^x) = N and (2^x - 1)
    return this and 15
}

private inline fun String.toGroup(): Int = length.toGroup()

private inline fun Boolean.toGroup(): Boolean = this

private inline fun Byte.toGroup(): Int = toInt().toGroup()

private inline fun Char.toGroup(): Int = code.toGroup()

private inline fun Short.toGroup(): Int = toInt().toGroup()

private inline fun Float.toGroup(): Int = (this * 16.0f).toInt().toGroup()

private inline fun Long.toGroup(): Long = this and 15L

private inline fun Double.toGroup(): Int = (this * 16.0).toInt().toGroup()
