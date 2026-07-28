package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.RandomNullabilityPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator.FieldGeneratorFactory

/**
 * Generates object or reference values for benchmark setup.
 */
interface ObjectGenerator<T> {
    /** Runtime class used for array component creation, even when [T] is nullable. */
    val objectClass: Class<T & Any>

    /** Returns the next generated object value. */
    fun next(): T
}

/**
 * Uses [nullabilityPolicy] to decide whether to return null or ask [delegate] for the next object.
 */
class NullableObjectGenerator<T : Any>(
    private val delegate: ObjectGenerator<T>,
    private val nullabilityPolicy: NullabilityPolicy,
) : ObjectGenerator<T?> {
    override val objectClass: Class<T> = delegate.objectClass

    override fun next(): T? = nullabilityPolicy.nullable { delegate.next() }
}

/**
 * Creates [ObjectGenerator] instances from benchmark RNG streams.
 */
abstract class ObjectGeneratorFactory<T> {
    /** Creates an object generator using the provided RNG streams. */
    abstract fun create(generatorRngs: GeneratorRngs): ObjectGenerator<T>

    companion object {
        /**
         * Creates a factory for objects initialized from field and reference generators.
         */
        inline fun <reified T : Any, R> of(
            fieldGeneratorFactory: FieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
            referenceGeneratorFactory: ObjectGeneratorFactory<R>,
            noinline initializer: (fieldGenerator: FieldGenerator, referenceGenerator: ObjectGenerator<R>) -> T,
        ): ObjectGeneratorFactory<T> = object : ObjectGeneratorFactory<T>() {
            override fun create(generatorRngs: GeneratorRngs): ObjectGenerator<T> = object : ObjectGenerator<T> {
                override val objectClass: Class<T> = T::class.java

                private val fieldGenerator = fieldGeneratorFactory.create(generatorRngs)
                private val referenceGenerator = referenceGeneratorFactory.create(generatorRngs)

                override fun next(): T = initializer(fieldGenerator, referenceGenerator)
            }
        }

        /** Creates a factory for the common case where [String] is used for reference fields. */
        inline fun <reified T : Any> of(
            fieldGeneratorFactory: FieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
            noinline initializer: (fieldGenerator: FieldGenerator, referenceGenerator: ObjectGenerator<String>) -> T,
        ): ObjectGeneratorFactory<T> = of(
            fieldGeneratorFactory = fieldGeneratorFactory,
            referenceGeneratorFactory = randomStrings(),
            initializer = initializer,
        )

        /** Creates a factory for random string generators with configurable string lengths. */
        fun randomStrings(
            minLength: Int = 3,
            maxLength: Int = 10,
        ): ObjectGeneratorFactory<String> = object : ObjectGeneratorFactory<String>() {
            override fun create(generatorRngs: GeneratorRngs): StringGenerator =
                StringGenerator(
                    random = generatorRngs.dataGenerationRng,
                    minLength = minLength,
                    maxLength = maxLength,
                )
        }
    }
}

/**
 * Creates a factory that uses [nullRatio] to decide whether to return null or generate the next non-null value.
 */
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
