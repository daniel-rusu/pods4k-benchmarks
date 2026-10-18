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

private const val NUM_COLLECTIONS = BenchmarkBatchSize.DEFAULT

// Statistically, this will filter half of the values on average.
private const val MATCH_PROBABILITY = 0.5

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class FilterBenchmarks : FlatCollectionBenchmark(
    numCollections = NUM_COLLECTIONS,
    primitiveGeneratorFactory = PrimitiveGeneratorFactory
        .randomValues()
        .withPredicateMatchProbability(MATCH_PROBABILITY, MedianValuePredicate),
    referenceGeneratorFactory = ObjectGeneratorFactory
        .randomStrings()
        .withPredicateMatchProbability(MATCH_PROBABILITY, MedianValuePredicate::matches),
) {
    @Benchmark
    fun filter(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: List<Boolean> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: List<Byte> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: List<Char> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: List<Short> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: List<Int> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: List<Float> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: List<Long> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: List<Double> -> list.filter { MedianValuePredicate.matches(it) } },

            // persistent lists
            { list: PersistentList<String> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Boolean> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Byte> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Char> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Short> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Int> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Float> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Long> -> list.filter { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Double> -> list.filter { MedianValuePredicate.matches(it) } },

            // arrays
            { array: Array<String> -> array.filter { MedianValuePredicate.matches(it) } },
            { array: BooleanArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: ByteArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: CharArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: ShortArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: IntArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: FloatArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: LongArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: DoubleArray -> array.filter { MedianValuePredicate.matches(it) } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.filter { MedianValuePredicate.matches(it) } },
            { array: ImmutableBooleanArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: ImmutableByteArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: ImmutableCharArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: ImmutableShortArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: ImmutableIntArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: ImmutableFloatArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: ImmutableLongArray -> array.filter { MedianValuePredicate.matches(it) } },
            { array: ImmutableDoubleArray -> array.filter { MedianValuePredicate.matches(it) } },
        )
    }
}
