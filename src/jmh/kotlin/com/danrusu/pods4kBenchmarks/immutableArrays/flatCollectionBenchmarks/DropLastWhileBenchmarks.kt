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
import com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks.setup.standardFlatCollectionRecipe
import com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks.setup.withPredicateMatchProbability
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkBatchSize
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

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class DropLastWhileBenchmarks : FlatCollectionBenchmark(
    numCollections = NUM_COLLECTIONS,
    createDataRecipe = { rngs ->
        standardFlatCollectionRecipe(rngs).withPredicateMatchProbability(
            predicateOutcomeRng = rngs.predicateOutcomeRng,

            // Drops 0.98 / (1 - 0.98) = 49 elements on average.
            // The median is 34 elements dropped because 1 - (0.98)^35 = ~50.7%.
            matchProbability = 98.percent(),
            primitivePredicates = predicate,
            referencePredicate = { value -> predicate.matches(value) },
        )
    },
) {
    @Benchmark
    fun dropLastWhile(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.dropLastWhile { predicate.matches(it) } },
            { list: List<Boolean> -> list.dropLastWhile { predicate.matches(it) } },
            { list: List<Byte> -> list.dropLastWhile { predicate.matches(it) } },
            { list: List<Char> -> list.dropLastWhile { predicate.matches(it) } },
            { list: List<Short> -> list.dropLastWhile { predicate.matches(it) } },
            { list: List<Int> -> list.dropLastWhile { predicate.matches(it) } },
            { list: List<Float> -> list.dropLastWhile { predicate.matches(it) } },
            { list: List<Long> -> list.dropLastWhile { predicate.matches(it) } },
            { list: List<Double> -> list.dropLastWhile { predicate.matches(it) } },

            // persistent lists
            { list: PersistentList<String> -> list.dropLastWhile { predicate.matches(it) } },
            { list: PersistentList<Boolean> -> list.dropLastWhile { predicate.matches(it) } },
            { list: PersistentList<Byte> -> list.dropLastWhile { predicate.matches(it) } },
            { list: PersistentList<Char> -> list.dropLastWhile { predicate.matches(it) } },
            { list: PersistentList<Short> -> list.dropLastWhile { predicate.matches(it) } },
            { list: PersistentList<Int> -> list.dropLastWhile { predicate.matches(it) } },
            { list: PersistentList<Float> -> list.dropLastWhile { predicate.matches(it) } },
            { list: PersistentList<Long> -> list.dropLastWhile { predicate.matches(it) } },
            { list: PersistentList<Double> -> list.dropLastWhile { predicate.matches(it) } },

            // arrays
            { array: Array<String> -> array.dropLastWhile { predicate.matches(it) } },
            { array: BooleanArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: ByteArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: CharArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: ShortArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: IntArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: FloatArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: LongArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: DoubleArray -> array.dropLastWhile { predicate.matches(it) } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.dropLastWhile { predicate.matches(it) } },
            { array: ImmutableBooleanArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: ImmutableByteArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: ImmutableCharArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: ImmutableShortArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: ImmutableIntArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: ImmutableFloatArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: ImmutableLongArray -> array.dropLastWhile { predicate.matches(it) } },
            { array: ImmutableDoubleArray -> array.dropLastWhile { predicate.matches(it) } },
        )
    }
}
