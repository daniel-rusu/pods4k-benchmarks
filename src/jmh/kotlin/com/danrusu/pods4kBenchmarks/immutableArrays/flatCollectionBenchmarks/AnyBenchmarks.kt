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
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.withPredicateMatchProbability
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.withPredicateMatchProbability
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

private const val NUM_COLLECTIONS = BenchmarkBatchSize.SPARSE_ACCESS

// Inspects (1 / 0.02) = 50 elements on average
// The median is 35 elements because 1 - (1 - 0.02)^35 = ~50.7%.
private const val MATCH_PROBABILITY = 0.02

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class AnyBenchmarks : FlatCollectionBenchmark(
    numCollections = NUM_COLLECTIONS,
    primitiveGeneratorFactory = PrimitiveGeneratorFactory
        .randomValues()
        .withPredicateMatchProbability(MATCH_PROBABILITY, MedianValuePredicate),
    referenceGeneratorFactory = ObjectGeneratorFactory
        .randomStrings()
        .withPredicateMatchProbability(MATCH_PROBABILITY, MedianValuePredicate::matches),
) {
    @Benchmark
    fun any(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.any { MedianValuePredicate.matches(it) } },
            { list: List<Boolean> -> list.any { MedianValuePredicate.matches(it) } },
            { list: List<Byte> -> list.any { MedianValuePredicate.matches(it) } },
            { list: List<Char> -> list.any { MedianValuePredicate.matches(it) } },
            { list: List<Short> -> list.any { MedianValuePredicate.matches(it) } },
            { list: List<Int> -> list.any { MedianValuePredicate.matches(it) } },
            { list: List<Float> -> list.any { MedianValuePredicate.matches(it) } },
            { list: List<Long> -> list.any { MedianValuePredicate.matches(it) } },
            { list: List<Double> -> list.any { MedianValuePredicate.matches(it) } },

            // persistent lists
            { list: PersistentList<String> -> list.any { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Boolean> -> list.any { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Byte> -> list.any { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Char> -> list.any { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Short> -> list.any { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Int> -> list.any { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Float> -> list.any { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Long> -> list.any { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Double> -> list.any { MedianValuePredicate.matches(it) } },

            // arrays
            { array: Array<String> -> array.any { MedianValuePredicate.matches(it) } },
            { array: BooleanArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: ByteArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: CharArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: ShortArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: IntArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: FloatArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: LongArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: DoubleArray -> array.any { MedianValuePredicate.matches(it) } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.any { MedianValuePredicate.matches(it) } },
            { array: ImmutableBooleanArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: ImmutableByteArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: ImmutableCharArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: ImmutableShortArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: ImmutableIntArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: ImmutableFloatArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: ImmutableLongArray -> array.any { MedianValuePredicate.matches(it) } },
            { array: ImmutableDoubleArray -> array.any { MedianValuePredicate.matches(it) } },
        )
    }
}
