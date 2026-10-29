package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicy

/** Uses [nullabilityPolicy] to decide whether to return null or ask [delegate] for the next object */
class NullableObjectGenerator<T : Any>(
    private val delegate: ObjectGenerator<T>,
    private val nullabilityPolicy: NullabilityPolicy,
) : ObjectGenerator<T?> {
    override val objectClass: Class<T> = delegate.objectClass

    override fun next(): T? = nullabilityPolicy.nullable { delegate.next() }
}

/** Uses the supplied policy to adapt this actual generator immediately. */
fun <T : Any> ObjectGenerator<T>.nullable(
    nullabilityPolicy: NullabilityPolicy,
): ObjectGenerator<T?> = NullableObjectGenerator(this, nullabilityPolicy)
