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
import com.danrusu.pods4kBenchmarks.utils.percent
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
private val MATCH_PROBABILITY = 98.percent()

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class DropLastWhileBenchmarks : FlatCollectionBenchmark(
    numCollections = NUM_COLLECTIONS,
    primitiveGeneratorFactory = PrimitiveGeneratorFactory
        .randomValues()
        .withPredicateMatchProbability(MATCH_PROBABILITY, MedianValuePredicate),
    referenceGeneratorFactory = ObjectGeneratorFactory
        .randomStrings()
        .withPredicateMatchProbability(MATCH_PROBABILITY, MedianValuePredicate::matches),
) {
    @Benchmark
    fun dropLastWhile(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: List<Boolean> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: List<Byte> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: List<Char> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: List<Short> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: List<Int> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: List<Float> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: List<Long> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: List<Double> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },

            // persistent lists
            { list: PersistentList<String> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Boolean> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Byte> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Char> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Short> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Int> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Float> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Long> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Double> -> list.dropLastWhile { MedianValuePredicate.matches(it) } },

            // arrays
            { array: Array<String> -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: BooleanArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: ByteArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: CharArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: ShortArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: IntArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: FloatArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: LongArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: DoubleArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableBooleanArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableByteArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableCharArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableShortArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableIntArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableFloatArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableLongArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
            { array: ImmutableDoubleArray -> array.dropLastWhile { MedianValuePredicate.matches(it) } },
        )
    }
}
