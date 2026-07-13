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
@Fork(3)
open class ForLoopBenchmarks : FlatCollectionBenchmark(numCollections = NUM_COLLECTIONS) {
    @Benchmark
    fun forLoop(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: List<Boolean> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: List<Byte> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: List<Char> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: List<Short> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: List<Int> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: List<Float> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: List<Long> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: List<Double> ->
                for (element in list) {
                    bh.consume(element)
                }
            },

            // persistent lists
            { list: PersistentList<String> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: PersistentList<Boolean> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: PersistentList<Byte> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: PersistentList<Char> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: PersistentList<Short> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: PersistentList<Int> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: PersistentList<Float> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: PersistentList<Long> ->
                for (element in list) {
                    bh.consume(element)
                }
            },
            { list: PersistentList<Double> ->
                for (element in list) {
                    bh.consume(element)
                }
            },

            // arrays
            { array: Array<String> ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: BooleanArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: ByteArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: CharArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: ShortArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: IntArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: FloatArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: LongArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: DoubleArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },

            // immutable arrays
            { array: ImmutableArray<String> ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: ImmutableBooleanArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: ImmutableByteArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: ImmutableCharArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: ImmutableShortArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: ImmutableIntArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: ImmutableFloatArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: ImmutableLongArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
            { array: ImmutableDoubleArray ->
                for (element in array) {
                    bh.consume(element)
                }
            },
        )
    }
}
