package com.danrusu.pods4kBenchmarks.immutableArrays.data

import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import kotlin.random.Random

internal class CompoundElement(
    val referenceValue: Any,
    val booleanValue: Boolean,
    val byteValue: Byte,
    val charValue: Char,
    val shortValue: Short,
    val intValue: Int,
    val floatValue: Float,
    val longValue: Long,
    val doubleValue: Double,
) {
    companion object {
        fun create(random: Random): CompoundElement = CompoundElement(
            referenceValue = DataGenerator.randomString(random = random),
            booleanValue = random.nextBoolean(),
            byteValue = DataGenerator.randomByte(random),
            charValue = DataGenerator.randomChar(random),
            shortValue = DataGenerator.randomShort(random),
            intValue = random.nextInt(),
            floatValue = random.nextFloat(),
            longValue = random.nextLong(),
            doubleValue = random.nextDouble(),
        )
    }
}
