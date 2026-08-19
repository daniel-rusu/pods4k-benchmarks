package com.danrusu.pods4kBenchmarks.immutableArrays.nullableFlatCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.filterNotNull
import com.danrusu.pods4kBenchmarks.immutableArrays.nullableFlatCollectionBenchmarks.setup.NullableFlatCollectionBenchmark
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkBatchSize
import com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.nullable
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
private const val NULL_RATIO = 0.5

/** Benchmarks `filterNotNull` across nullable reference and boxed primitive elements. */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class FilterNotNullBenchmarks : NullableFlatCollectionBenchmark(
    numCollections = NUM_COLLECTIONS,
    fieldGeneratorFactory = FieldGeneratorFactory.withRandomNullableFields(NULL_RATIO),
    referenceGeneratorFactory = ObjectGeneratorFactory.randomStrings().nullable(NULL_RATIO),
) {
    @Benchmark
    fun filterNotNull(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String?> -> list.filterNotNull() },
            { list: List<Boolean?> -> list.filterNotNull() },
            { list: List<Byte?> -> list.filterNotNull() },
            { list: List<Char?> -> list.filterNotNull() },
            { list: List<Short?> -> list.filterNotNull() },
            { list: List<Int?> -> list.filterNotNull() },
            { list: List<Float?> -> list.filterNotNull() },
            { list: List<Long?> -> list.filterNotNull() },
            { list: List<Double?> -> list.filterNotNull() },

            // persistent lists
            { list: PersistentList<String?> -> list.filterNotNull() },
            { list: PersistentList<Boolean?> -> list.filterNotNull() },
            { list: PersistentList<Byte?> -> list.filterNotNull() },
            { list: PersistentList<Char?> -> list.filterNotNull() },
            { list: PersistentList<Short?> -> list.filterNotNull() },
            { list: PersistentList<Int?> -> list.filterNotNull() },
            { list: PersistentList<Float?> -> list.filterNotNull() },
            { list: PersistentList<Long?> -> list.filterNotNull() },
            { list: PersistentList<Double?> -> list.filterNotNull() },

            // arrays
            { array: Array<String?> -> array.filterNotNull() },
            { array: Array<Boolean?> -> array.filterNotNull() },
            { array: Array<Byte?> -> array.filterNotNull() },
            { array: Array<Char?> -> array.filterNotNull() },
            { array: Array<Short?> -> array.filterNotNull() },
            { array: Array<Int?> -> array.filterNotNull() },
            { array: Array<Float?> -> array.filterNotNull() },
            { array: Array<Long?> -> array.filterNotNull() },
            { array: Array<Double?> -> array.filterNotNull() },

            // immutable arrays
            { array: ImmutableArray<String?> -> array.filterNotNull() },
            { array: ImmutableArray<Boolean?> -> array.filterNotNull() },
            { array: ImmutableArray<Byte?> -> array.filterNotNull() },
            { array: ImmutableArray<Char?> -> array.filterNotNull() },
            { array: ImmutableArray<Short?> -> array.filterNotNull() },
            { array: ImmutableArray<Int?> -> array.filterNotNull() },
            { array: ImmutableArray<Float?> -> array.filterNotNull() },
            { array: ImmutableArray<Long?> -> array.filterNotNull() },
            { array: ImmutableArray<Double?> -> array.filterNotNull() },
        )
    }
}
