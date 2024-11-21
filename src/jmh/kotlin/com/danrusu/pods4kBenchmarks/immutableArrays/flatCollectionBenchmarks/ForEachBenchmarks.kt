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
open class ForEachBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun forEach(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> ->
                var result = 0
                list.forEach { string ->
                    result += string.length
                }
                result
            },
            { list: List<Boolean> ->
                var result = false
                list.forEach { value ->
                    result = result xor value
                }
                result
            },
            { list: List<Byte> ->
                var result = 0
                list.forEach { value ->
                    result += value
                }
                result
            },
            { list: List<Char> ->
                var result = 0
                list.forEach { value ->
                    result += value.code
                }
                result
            },
            { list: List<Short> ->
                var result = 0
                list.forEach { value ->
                    result += value
                }
                result
            },
            { list: List<Int> ->
                var result = 0
                list.forEach { value ->
                    result += value
                }
                result
            },
            { list: List<Float> ->
                var result = 0.0f
                list.forEach { value ->
                    result += value
                }
                result
            },
            { list: List<Long> ->
                var result = 0L
                list.forEach { value ->
                    result += value
                }
                result
            },
            { list: List<Double> ->
                var result = 0.0
                list.forEach { value ->
                    result += value
                }
                result
            },

            // arrays
            { array: Array<String> ->
                var result = 0
                array.forEach { string ->
                    result += string.length
                }
                result
            },
            { array: BooleanArray ->
                var result = false
                array.forEach { value ->
                    result = result xor value
                }
                result
            },
            { array: ByteArray ->
                var result = 0
                array.forEach { value ->
                    result += value
                }
                result
            },
            { array: CharArray ->
                var result = 0
                array.forEach { value ->
                    result += value.code
                }
                result
            },
            { array: ShortArray ->
                var result = 0
                array.forEach { value ->
                    result += value
                }
                result
            },
            { array: IntArray ->
                var result = 0
                array.forEach { value ->
                    result += value
                }
                result
            },
            { array: FloatArray ->
                var result = 0.0f
                array.forEach { value ->
                    result += value
                }
                result
            },
            { array: LongArray ->
                var result = 0L
                array.forEach { value ->
                    result += value
                }
                result
            },
            { array: DoubleArray ->
                var result = 0.0
                array.forEach { value ->
                    result += value
                }
                result
            },

            // immutable arrays
            { array: ImmutableArray<String> ->
                var result = 0
                array.forEach { string ->
                    result += string.length
                }
                result
            },
            { array: ImmutableBooleanArray ->
                var result = false
                array.forEach { value ->
                    result = result xor value
                }
                result
            },
            { array: ImmutableByteArray ->
                var result = 0
                array.forEach { value ->
                    result += value
                }
                result
            },
            { array: ImmutableCharArray ->
                var result = 0
                array.forEach { value ->
                    result += value.code
                }
                result
            },
            { array: ImmutableShortArray ->
                var result = 0
                array.forEach { value ->
                    result += value
                }
                result
            },
            { array: ImmutableIntArray ->
                var result = 0
                array.forEach { value ->
                    result += value
                }
                result
            },
            { array: ImmutableFloatArray ->
                var result = 0.0f
                array.forEach { value ->
                    result += value
                }
                result
            },
            { array: ImmutableLongArray ->
                var result = 0L
                array.forEach { value ->
                    result += value
                }
                result
            },
            { array: ImmutableDoubleArray ->
                var result = 0.0
                array.forEach { value ->
                    result += value
                }
                result
            },
        )
    }
}
