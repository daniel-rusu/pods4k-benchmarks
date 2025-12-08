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

const val STRING_ELEMENT = ""
const val BOOLEAN_ELEMENT = true
const val BYTE_ELEMENT = Byte.MIN_VALUE
const val CHAR_ELEMENT = 'A'
const val SHORT_ELEMENT = Short.MIN_VALUE
const val INT_ELEMENT = Int.MIN_VALUE
const val FLOAT_ELEMENT = 0.0f
const val LONG_ELEMENT = Long.MIN_VALUE
const val DOUBLE_ELEMENT = 0.0

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class PlusElementBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun plusElement(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list + STRING_ELEMENT },
            { list: List<Boolean> -> list + BOOLEAN_ELEMENT },
            { list: List<Byte> -> list + BYTE_ELEMENT },
            { list: List<Char> -> list + CHAR_ELEMENT },
            { list: List<Short> -> list + SHORT_ELEMENT },
            { list: List<Int> -> list + INT_ELEMENT },
            { list: List<Float> -> list + FLOAT_ELEMENT },
            { list: List<Long> -> list + LONG_ELEMENT },
            { list: List<Double> -> list + DOUBLE_ELEMENT },

            // arrays
            { array: Array<String> -> array + STRING_ELEMENT },
            { array: BooleanArray -> array + BOOLEAN_ELEMENT },
            { array: ByteArray -> array + BYTE_ELEMENT },
            { array: CharArray -> array + CHAR_ELEMENT },
            { array: ShortArray -> array + SHORT_ELEMENT },
            { array: IntArray -> array + INT_ELEMENT },
            { array: FloatArray -> array + FLOAT_ELEMENT },
            { array: LongArray -> array + LONG_ELEMENT },
            { array: DoubleArray -> array + DOUBLE_ELEMENT },

            // immutable arrays
            { array: ImmutableArray<String> -> array + STRING_ELEMENT },
            { array: ImmutableBooleanArray -> array + BOOLEAN_ELEMENT },
            { array: ImmutableByteArray -> array + BYTE_ELEMENT },
            { array: ImmutableCharArray -> array + CHAR_ELEMENT },
            { array: ImmutableShortArray -> array + SHORT_ELEMENT },
            { array: ImmutableIntArray -> array + INT_ELEMENT },
            { array: ImmutableFloatArray -> array + FLOAT_ELEMENT },
            { array: ImmutableLongArray -> array + LONG_ELEMENT },
            { array: ImmutableDoubleArray -> array + DOUBLE_ELEMENT },
        )
    }
}
