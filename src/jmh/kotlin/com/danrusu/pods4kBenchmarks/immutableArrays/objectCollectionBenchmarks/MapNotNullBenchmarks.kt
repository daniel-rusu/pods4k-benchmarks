package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.mapNotNull
import com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup.CompoundElementOfNullableValues
import com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup.ObjectCollectionBenchmark
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.nullable
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

private const val NUM_COLLECTIONS = 1000
private const val NULL_RATIO = 0.5

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class MapNotNullBenchmarks : ObjectCollectionBenchmark<CompoundElementOfNullableValues>(
    numCollections = NUM_COLLECTIONS,
    objectGeneratorFactory = ObjectGeneratorFactory.of<CompoundElementOfNullableValues, String?>(
        fieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
        referenceGeneratorFactory = ObjectGeneratorFactory.randomStrings().nullable(NULL_RATIO),
    ) { fieldGenerator, referenceGenerator ->
        // match reference null placement so that benchmarks are directly comparable across data types
        val reference = referenceGenerator.next()
        val isNull = reference == null

        CompoundElementOfNullableValues(
            referenceValue = reference,
            booleanValue = if (isNull) null else fieldGenerator.nextBoolean(),
            byteValue = if (isNull) null else fieldGenerator.nextByte(),
            charValue = if (isNull) null else fieldGenerator.nextChar(),
            shortValue = if (isNull) null else fieldGenerator.nextShort(),
            intValue = if (isNull) null else fieldGenerator.nextInt(),
            floatValue = if (isNull) null else fieldGenerator.nextFloat(),
            longValue = if (isNull) null else fieldGenerator.nextLong(),
            doubleValue = if (isNull) null else fieldGenerator.nextDouble(),
        )
    },
) {
    @Benchmark
    fun mapNotNullReference(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.referenceValue } },
            { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.referenceValue } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.referenceValue } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.referenceValue } },
        )
    }

    @Benchmark
    fun mapNotNullBoolean(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.booleanValue } },
            { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.booleanValue } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.booleanValue } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.booleanValue } },
        )
    }

    @Benchmark
    fun mapNotNullByte(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.byteValue } },
            { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.byteValue } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.byteValue } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.byteValue } },
        )
    }

    @Benchmark
    fun mapNotNullChar(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.charValue } },
            { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.charValue } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.charValue } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.charValue } },
        )
    }

    @Benchmark
    fun mapNotNullShort(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.shortValue } },
            { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.shortValue } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.shortValue } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.shortValue } },
        )
    }

    @Benchmark
    fun mapNotNullInt(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.intValue } },
            { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.intValue } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.intValue } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.intValue } },
        )
    }

    @Benchmark
    fun mapNotNullFloat(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.floatValue } },
            { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.floatValue } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.floatValue } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.floatValue } },
        )
    }

    @Benchmark
    fun mapNotNullLong(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.longValue } },
            { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.longValue } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.longValue } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.longValue } },
        )
    }

    @Benchmark
    fun mapNotNullDouble(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.doubleValue } },
            { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.doubleValue } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.doubleValue } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.doubleValue } },
        )
    }
}
