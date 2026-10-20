package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.mapNotNull
import com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup.CompoundElementOfNullableValues
import com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup.ObjectCollectionBenchmark
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkBatchSize
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicyFactory
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.percent
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
private val NULL_PROBABILITY = 50.percent()

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class MapNotNullBenchmarks : ObjectCollectionBenchmark<CompoundElementOfNullableValues>(
    numCollections = NUM_COLLECTIONS,
    objectGeneratorFactory = NullableCompoundElementFactory(
        primitiveGeneratorFactory = PrimitiveGeneratorFactory.randomValues(),
        stringGeneratorFactory = ObjectGeneratorFactory.randomStrings(),
        nullabilityPolicyFactory = NullabilityPolicyFactory.random(NULL_PROBABILITY),
    ),
) {
    @Benchmark
    fun mapNotNull(bh: Blackhole) {
        when (dataType) {
            DataType.REFERENCE -> transformEachCollection(
                bh,
                { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.referenceValue } },
                { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.referenceValue } },
                { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.referenceValue } },
                { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.referenceValue } },
            )

            DataType.BOOLEAN -> transformEachCollection(
                bh,
                { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.booleanValue } },
                { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.booleanValue } },
                { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.booleanValue } },
                { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.booleanValue } },
            )

            DataType.BYTE -> transformEachCollection(
                bh,
                { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.byteValue } },
                { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.byteValue } },
                { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.byteValue } },
                { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.byteValue } },
            )

            DataType.CHAR -> transformEachCollection(
                bh,
                { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.charValue } },
                { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.charValue } },
                { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.charValue } },
                { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.charValue } },
            )

            DataType.SHORT -> transformEachCollection(
                bh,
                { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.shortValue } },
                { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.shortValue } },
                { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.shortValue } },
                { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.shortValue } },
            )

            DataType.INT -> transformEachCollection(
                bh,
                { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.intValue } },
                { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.intValue } },
                { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.intValue } },
                { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.intValue } },
            )

            DataType.FLOAT -> transformEachCollection(
                bh,
                { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.floatValue } },
                { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.floatValue } },
                { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.floatValue } },
                { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.floatValue } },
            )

            DataType.LONG -> transformEachCollection(
                bh,
                { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.longValue } },
                { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.longValue } },
                { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.longValue } },
                { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.longValue } },
            )

            DataType.DOUBLE -> transformEachCollection(
                bh,
                { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.doubleValue } },
                { list: PersistentList<CompoundElementOfNullableValues> -> list.mapNotNull { it.doubleValue } },
                { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.doubleValue } },
                { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.doubleValue } },
            )
        }
    }
}

class NullableCompoundElementFactory(
    val primitiveGeneratorFactory: PrimitiveGeneratorFactory,
    val stringGeneratorFactory: ObjectGeneratorFactory<String>,
    val nullabilityPolicyFactory: NullabilityPolicyFactory,
) : ObjectGeneratorFactory<CompoundElementOfNullableValues>() {
    override fun create(generatorRngs: GeneratorRngs): ObjectGenerator<CompoundElementOfNullableValues> {
        val primitiveGenerator = primitiveGeneratorFactory.create(generatorRngs)
        val referenceGenerator = stringGeneratorFactory.create(generatorRngs)
        val nullabilityPolicy = nullabilityPolicyFactory.create(generatorRngs)

        return object : ObjectGenerator<CompoundElementOfNullableValues> {
            override val objectClass: Class<CompoundElementOfNullableValues> =
                CompoundElementOfNullableValues::class.java

            override fun next(): CompoundElementOfNullableValues {
                val isNull = nullabilityPolicy.shouldGenerateNull()

                return CompoundElementOfNullableValues(
                    referenceValue = if (isNull) null else referenceGenerator.next(),
                    booleanValue = if (isNull) null else primitiveGenerator.nextBoolean(),
                    byteValue = if (isNull) null else primitiveGenerator.nextByte(),
                    charValue = if (isNull) null else primitiveGenerator.nextChar(),
                    shortValue = if (isNull) null else primitiveGenerator.nextShort(),
                    intValue = if (isNull) null else primitiveGenerator.nextInt(),
                    floatValue = if (isNull) null else primitiveGenerator.nextFloat(),
                    longValue = if (isNull) null else primitiveGenerator.nextLong(),
                    doubleValue = if (isNull) null else primitiveGenerator.nextDouble(),
                )
            }
        }
    }
}
