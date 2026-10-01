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

// Statistically, about half of the values will be on the left and the other half on the right on average.
private const val MATCH_RATIO = 0.5

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
        .withPredicateMatchRatio(MATCH_RATIO, MedianValuePredicate),
    referenceGeneratorFactory = ObjectGeneratorFactory
        .randomStrings()
        .withPredicateMatchRatio(MATCH_RATIO, MedianValuePredicate::matches),
) {
    @Benchmark
    fun partition(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: List<Boolean> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: List<Byte> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: List<Char> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: List<Short> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: List<Int> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: List<Float> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: List<Long> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: List<Double> -> list.partition { MedianValuePredicate.matches(it) } },

            // persistent lists
            { list: PersistentList<String> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Boolean> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Byte> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Char> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Short> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Int> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Float> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Long> -> list.partition { MedianValuePredicate.matches(it) } },
            { list: PersistentList<Double> -> list.partition { MedianValuePredicate.matches(it) } },

            // arrays
            { array: Array<String> -> array.partition { MedianValuePredicate.matches(it) } },
            { array: BooleanArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: ByteArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: CharArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: ShortArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: IntArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: FloatArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: LongArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: DoubleArray -> array.partition { MedianValuePredicate.matches(it) } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.partition { MedianValuePredicate.matches(it) } },
            { array: ImmutableBooleanArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: ImmutableByteArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: ImmutableCharArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: ImmutableShortArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: ImmutableIntArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: ImmutableFloatArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: ImmutableLongArray -> array.partition { MedianValuePredicate.matches(it) } },
            { array: ImmutableDoubleArray -> array.partition { MedianValuePredicate.matches(it) } },
        )
    }
}
