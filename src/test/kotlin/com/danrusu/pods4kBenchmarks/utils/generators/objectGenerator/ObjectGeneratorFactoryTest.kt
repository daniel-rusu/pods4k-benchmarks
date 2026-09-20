package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.CountingPrimitiveGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.FixedStringGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.TestGeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.TestObject
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ObjectGeneratorFactoryTest {
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
