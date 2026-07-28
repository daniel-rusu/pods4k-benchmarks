package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.RandomNullabilityPolicy

/** Uses [nullabilityPolicy] to decide whether to return null or ask [delegate] for the next object */
class NullableObjectGenerator<T : Any>(
    private val delegate: ObjectGenerator<T>,
    private val nullabilityPolicy: NullabilityPolicy,
) : ObjectGenerator<T?> {
    override val objectClass: Class<T> = delegate.objectClass

    override fun next(): T? = nullabilityPolicy.nullable { delegate.next() }
}

/** Creates a factory that uses [nullRatio] to decide whether to return null or generate the next non-null value */
fun <T : Any> ObjectGeneratorFactory<T>.nullable(
    nullRatio: Double,
): ObjectGeneratorFactory<T?> = object : ObjectGeneratorFactory<T?>() {
    override fun create(generatorRngs: GeneratorRngs): ObjectGenerator<T?> {
        return NullableObjectGenerator(
            delegate = this@nullable.create(generatorRngs),
            nullabilityPolicy = RandomNullabilityPolicy(nullRatio, generatorRngs.nullabilityDecisionsRng),
        )
    }
}
