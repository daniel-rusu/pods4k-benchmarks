package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicyFactory

/** Uses [nullabilityPolicy] to decide whether to return null or ask [delegate] for the next object */
class NullableObjectGenerator<T : Any>(
    private val delegate: ObjectGenerator<T>,
    private val nullabilityPolicy: NullabilityPolicy,
) : ObjectGenerator<T?> {
    override val objectClass: Class<T> = delegate.objectClass

    override fun next(): T? = nullabilityPolicy.nullable { delegate.next() }
}

/** Creates a factory that uses a fresh policy to decide whether to return null or generate a non-null value. */
fun <T : Any> ObjectGeneratorFactory<T>.nullable(
    nullabilityPolicyFactory: NullabilityPolicyFactory,
): ObjectGeneratorFactory<T?> = object : ObjectGeneratorFactory<T?>() {
    override fun create(generatorRngs: GeneratorRngs): ObjectGenerator<T?> {
        return NullableObjectGenerator(
            delegate = this@nullable.create(generatorRngs),
            nullabilityPolicy = nullabilityPolicyFactory.create(generatorRngs),
        )
    }
}

/** Creates a factory that returns null according to [nullRatio]. */
fun <T : Any> ObjectGeneratorFactory<T>.nullable(
    nullRatio: Double,
): ObjectGeneratorFactory<T?> = nullable(NullabilityPolicyFactory.random(nullRatio))
