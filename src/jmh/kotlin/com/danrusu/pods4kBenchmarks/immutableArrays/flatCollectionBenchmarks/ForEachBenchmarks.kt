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
import kotlinx.collections.immutable.PersistentList
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
open class ForEachBenchmarks : FlatCollectionBenchmark(numCollections = NUM_COLLECTIONS) {
    @Benchmark
    fun forEach(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> ->
                list.forEach { bh.consume(it) }
            },
            { list: List<Boolean> ->
                list.forEach { bh.consume(it) }
            },
            { list: List<Byte> ->
                list.forEach { bh.consume(it) }
            },
            { list: List<Char> ->
                list.forEach { bh.consume(it) }
            },
            { list: List<Short> ->
                list.forEach { bh.consume(it) }
            },
            { list: List<Int> ->
                list.forEach { bh.consume(it) }
            },
            { list: List<Float> ->
                list.forEach { bh.consume(it) }
            },
            { list: List<Long> ->
                list.forEach { bh.consume(it) }
            },
            { list: List<Double> ->
                list.forEach { bh.consume(it) }
            },

            // persistent lists
            { list: PersistentList<String> ->
                list.forEach { bh.consume(it) }
            },
            { list: PersistentList<Boolean> ->
                list.forEach { bh.consume(it) }
            },
            { list: PersistentList<Byte> ->
                list.forEach { bh.consume(it) }
            },
            { list: PersistentList<Char> ->
                list.forEach { bh.consume(it) }
            },
            { list: PersistentList<Short> ->
                list.forEach { bh.consume(it) }
            },
            { list: PersistentList<Int> ->
                list.forEach { bh.consume(it) }
            },
            { list: PersistentList<Float> ->
                list.forEach { bh.consume(it) }
            },
            { list: PersistentList<Long> ->
                list.forEach { bh.consume(it) }
            },
            { list: PersistentList<Double> ->
                list.forEach { bh.consume(it) }
            },

            // arrays
            { array: Array<String> ->
                array.forEach { bh.consume(it) }
            },
            { array: BooleanArray ->
                array.forEach { bh.consume(it) }
            },
            { array: ByteArray ->
                array.forEach { bh.consume(it) }
            },
            { array: CharArray ->
                array.forEach { bh.consume(it) }
            },
            { array: ShortArray ->
                array.forEach { bh.consume(it) }
            },
            { array: IntArray ->
                array.forEach { bh.consume(it) }
            },
            { array: FloatArray ->
                array.forEach { bh.consume(it) }
            },
            { array: LongArray ->
                array.forEach { bh.consume(it) }
            },
            { array: DoubleArray ->
                array.forEach { bh.consume(it) }
            },

            // immutable arrays
            { array: ImmutableArray<String> ->
                array.forEach { bh.consume(it) }
            },
            { array: ImmutableBooleanArray ->
                array.forEach { bh.consume(it) }
            },
            { array: ImmutableByteArray ->
                array.forEach { bh.consume(it) }
            },
            { array: ImmutableCharArray ->
                array.forEach { bh.consume(it) }
            },
            { array: ImmutableShortArray ->
                array.forEach { bh.consume(it) }
            },
            { array: ImmutableIntArray ->
                array.forEach { bh.consume(it) }
            },
            { array: ImmutableFloatArray ->
                array.forEach { bh.consume(it) }
            },
            { array: ImmutableLongArray ->
                array.forEach { bh.consume(it) }
            },
            { array: ImmutableDoubleArray ->
                array.forEach { bh.consume(it) }
            },
        )
    }
}
