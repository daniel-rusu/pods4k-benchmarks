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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkBatchSize
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.withPredicateMatchRatio
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.withPredicateMatchRatio
import com.danrusu.pods4kBenchmarks.utils.predicates.MedianValuePredicate
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

private const val NUM_COLLECTIONS = BenchmarkBatchSize.DEFAULT

// Drops 0.98 / (1 - 0.98) = 49 elements on average.
// The median is 34 elements dropped because 1 - (0.98)^35 = ~50.7%.
private const val MATCH_RATIO = 0.98

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class DropWhileBenchmarks : FlatCollectionBenchmark(
    numCollections = NUM_COLLECTIONS,
    primitiveGeneratorFactory = PrimitiveGeneratorFactory
        .randomValues()
        .withPredicateMatchRatio(MATCH_RATIO, MedianValuePredicate),
    referenceGeneratorFactory = ObjectGeneratorFactory
        .randomStrings()
        .withPredicateMatchRatio(MATCH_RATIO, MedianValuePredicate::matches),
) {
    @Benchmark
    fun dropWhile(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: List<Boolean> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: List<Byte> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: List<Char> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: List<Short> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: List<Int> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: List<Float> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: List<Long> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: List<Double> -> list.dropWhile { MedianValuePredicate.matches(it) } },

            // persistent lists
            { list: PersistentList<String> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Boolean> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Byte> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Char> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Short> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Int> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Float> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Long> -> list.dropWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Double> -> list.dropWhile { MedianValuePredicate.matches(it) } },

            // arrays
            { array: Array<String> -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: BooleanArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: ByteArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: CharArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: ShortArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: IntArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: FloatArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: LongArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: DoubleArray -> array.dropWhile { MedianValuePredicate.matches(it) } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableBooleanArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableByteArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableCharArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableShortArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableIntArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableFloatArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableLongArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableDoubleArray -> array.dropWhile { MedianValuePredicate.matches(it) } },
        )
    }
}
