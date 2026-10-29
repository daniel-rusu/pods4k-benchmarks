package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.CountingPrimitiveGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.FixedStringGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.TestObject
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ObjectGeneratorTest {
    @Test
    fun `initializer runs only on next and retains its supplied child generators`() {
        val primitive = CountingPrimitiveGenerator()
        val reference = FixedStringGenerator("Hello")
        val generator = objectGenerator<TestObject> {
            TestObject(primitive.nextInt(), reference.next())
        }

        expectThat(generator.objectClass).isEqualTo(TestObject::class.java)
        expectThat(primitive.numNextIntCalls).isEqualTo(0)
        expectThat(generator.next()).isEqualTo(TestObject(1, "Hello"))
        expectThat(generator.next()).isEqualTo(TestObject(2, "Hello"))
        expectThat(primitive.numNextIntCalls).isEqualTo(2)
    }
}
