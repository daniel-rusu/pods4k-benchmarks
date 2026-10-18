package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.CountingPrimitiveGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.FixedStringGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.RecordingRandom
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.TestGeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.TestObject
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGeneratorFactory
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class ObjectGeneratorFactoryTest {
    @Test
    fun `predicate decorator forwards rngs to a fresh source and uses the predicate outcome stream`() {
        var sourceCreations = 0
        val factory = object : ObjectGeneratorFactory<String>() {
            override fun create(generatorRngs: GeneratorRngs): ObjectGenerator<String> {
                sourceCreations++
                return object : ObjectGenerator<String> {
                    override val objectClass: Class<String> = String::class.java

                    override fun next(): String = generatorRngs.dataGenerationRng.nextInt().toString()
                }
            }
        }.withPredicateMatchProbability(1.0) { it.startsWith("-") }

        repeat(2) {
            val dataRng = RecordingRandom(nextIntValues = sequenceOf(1, -2, -3, 4))
            val nullRng = RecordingRandom()
            val outcomeRng = RecordingRandom(nextDoubleValues = sequenceOf(0.25, 0.75))
            val generator = factory.create(
                TestGeneratorRngs(
                    dataGenerationRng = dataRng,
                    nullabilityDecisionsRng = nullRng,
                    predicateOutcomeRng = outcomeRng,
                )
            )

            expectThat(List(2) { generator.next() }).isEqualTo(listOf("-2", "-3"))
            // 2 outcome decisions required to produce 2 values
            expectThat(outcomeRng.numNextDoubleCalls).isEqualTo(2)
            // 3 nextInt() calls required to find 2 values that meets the predicate at the specified match probability
            expectThat(dataRng.numNextIntCalls).isEqualTo(3)
            // 0 nullable decisions
            expectThat(nullRng.numNextDoubleCalls).isEqualTo(0)
        }
        expectThat(sourceCreations).isEqualTo(2)
    }

    @Test
    fun `predicate decorator rejects invalid probabilities`() {
        for (probability in listOf(-0.1, 1.1, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)) {
            expectThrows<IllegalArgumentException> {
                ObjectGeneratorFactory.randomStrings().withPredicateMatchProbability(probability) { true }
            }.message.isEqualTo("matchProbability ($probability) must be between 0.0 and 1.0")
        }
    }

    @Test
    fun `object generator factory wires primitive and reference generators`() {
        val factory = ObjectGeneratorFactory.of<TestObject, String>(
            primitiveGeneratorFactory = PrimitiveGeneratorFactory { CountingPrimitiveGenerator() },
            referenceGeneratorFactory = object : ObjectGeneratorFactory<String>() {
                override fun create(generatorRngs: GeneratorRngs): ObjectGenerator<String> =
                    FixedStringGenerator("Hello")
            },
        ) { primitiveGenerator, referenceGenerator ->
            TestObject(
                primitiveValue = primitiveGenerator.nextInt(),
                referenceValue = referenceGenerator.next(),
            )
        }

        val generator = factory.create(TestGeneratorRngs())

        expectThat(generator.objectClass)
            .isEqualTo(TestObject::class.java)

        expectThat(generator.next())
            .isEqualTo(TestObject(primitiveValue = 1, referenceValue = "Hello"))
    }

    @Test
    fun `random string factory passes configured string lengths to string generator`() {
        val generator = ObjectGeneratorFactory.randomStrings(minLength = 7, maxLength = 7)
            .create(TestGeneratorRngs(masterSeed = 123))

        expectThat(generator.next().length)
            .isEqualTo(7)
    }
}
