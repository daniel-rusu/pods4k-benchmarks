package com.danrusu.pods4kBenchmarks.utils.generators.fixtures

import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator

internal data class TestObject(
    val fieldValue: Int,
    val referenceValue: String,
)

internal class CountingFieldGenerator(
    nullabilityPolicy: NullabilityPolicy?,
) : FieldGenerator(nullabilityPolicy) {
    var numNextIntCalls = 0
        private set

    override fun nextBoolean(): Boolean = throw UnsupportedOperationException()

    override fun nextByte(): Byte = throw UnsupportedOperationException()

    override fun nextChar(): Char = throw UnsupportedOperationException()

    override fun nextShort(): Short = throw UnsupportedOperationException()

    override fun nextInt(): Int {
        numNextIntCalls++
        return numNextIntCalls
    }

    override fun nextFloat(): Float = throw UnsupportedOperationException()

    override fun nextLong(): Long = throw UnsupportedOperationException()

    override fun nextDouble(): Double = throw UnsupportedOperationException()
}

class FixedStringGenerator(val value: String) : ObjectGenerator<String> {
    override val objectClass: Class<String> = String::class.javaObjectType

    override fun next(): String = value
}
