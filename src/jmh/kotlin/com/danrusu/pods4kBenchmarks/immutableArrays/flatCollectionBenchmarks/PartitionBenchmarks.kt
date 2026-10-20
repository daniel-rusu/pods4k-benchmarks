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

private val predicate = MedianValuePredicate

// Statistically, about half of the values will be on the left and the other half on the right on average.
private val MATCH_PROBABILITY = 50.percent() // probability of matching the predicate

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class PartitionBenchmarks : FlatCollectionBenchmark(
    numCollections = NUM_COLLECTIONS,
    primitiveGeneratorFactory = PrimitiveGeneratorFactory
        .randomValues()
        .withPredicateMatchProbability(MATCH_PROBABILITY, predicate),
    referenceGeneratorFactory = ObjectGeneratorFactory
        .randomStrings()
        .withPredicateMatchProbability(MATCH_PROBABILITY, predicate::matches),
) {
    @Benchmark
    fun partition(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.partition { predicate.matches(it) } },
            { list: List<Boolean> -> list.partition { predicate.matches(it) } },
            { list: List<Byte> -> list.partition { predicate.matches(it) } },
            { list: List<Char> -> list.partition { predicate.matches(it) } },
            { list: List<Short> -> list.partition { predicate.matches(it) } },
            { list: List<Int> -> list.partition { predicate.matches(it) } },
            { list: List<Float> -> list.partition { predicate.matches(it) } },
            { list: List<Long> -> list.partition { predicate.matches(it) } },
            { list: List<Double> -> list.partition { predicate.matches(it) } },

            // persistent lists
            { list: PersistentList<String> -> list.partition { predicate.matches(it) } },
            { list: PersistentList<Boolean> -> list.partition { predicate.matches(it) } },
            { list: PersistentList<Byte> -> list.partition { predicate.matches(it) } },
            { list: PersistentList<Char> -> list.partition { predicate.matches(it) } },
            { list: PersistentList<Short> -> list.partition { predicate.matches(it) } },
            { list: PersistentList<Int> -> list.partition { predicate.matches(it) } },
            { list: PersistentList<Float> -> list.partition { predicate.matches(it) } },
            { list: PersistentList<Long> -> list.partition { predicate.matches(it) } },
            { list: PersistentList<Double> -> list.partition { predicate.matches(it) } },

            // arrays
            { array: Array<String> -> array.partition { predicate.matches(it) } },
            { array: BooleanArray -> array.partition { predicate.matches(it) } },
            { array: ByteArray -> array.partition { predicate.matches(it) } },
            { array: CharArray -> array.partition { predicate.matches(it) } },
            { array: ShortArray -> array.partition { predicate.matches(it) } },
            { array: IntArray -> array.partition { predicate.matches(it) } },
            { array: FloatArray -> array.partition { predicate.matches(it) } },
            { array: LongArray -> array.partition { predicate.matches(it) } },
            { array: DoubleArray -> array.partition { predicate.matches(it) } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.partition { predicate.matches(it) } },
            { array: ImmutableBooleanArray -> array.partition { predicate.matches(it) } },
            { array: ImmutableByteArray -> array.partition { predicate.matches(it) } },
            { array: ImmutableCharArray -> array.partition { predicate.matches(it) } },
            { array: ImmutableShortArray -> array.partition { predicate.matches(it) } },
            { array: ImmutableIntArray -> array.partition { predicate.matches(it) } },
            { array: ImmutableFloatArray -> array.partition { predicate.matches(it) } },
            { array: ImmutableLongArray -> array.partition { predicate.matches(it) } },
            { array: ImmutableDoubleArray -> array.partition { predicate.matches(it) } },
        )
    }
}
