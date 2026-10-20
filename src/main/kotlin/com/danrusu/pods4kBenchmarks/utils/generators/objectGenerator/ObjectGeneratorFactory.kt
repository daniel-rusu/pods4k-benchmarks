package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.Percent
import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGeneratorFactory

/** Creates [ObjectGenerator] instances from benchmark RNG streams */
abstract class ObjectGeneratorFactory<T> {
    /** Creates an object generator using the provided RNG streams. */
    abstract fun create(generatorRngs: GeneratorRngs): ObjectGenerator<T>

    companion object {
        /** Creates a factory for objects initialized from primitive and reference generators. */
        inline fun <reified T : Any, R> of(
            primitiveGeneratorFactory: PrimitiveGeneratorFactory = PrimitiveGeneratorFactory.randomValues(),
            referenceGeneratorFactory: ObjectGeneratorFactory<R>,
            noinline initializer: (primitiveGenerator: PrimitiveGenerator, referenceGenerator: ObjectGenerator<R>) -> T,
        ): ObjectGeneratorFactory<T> = object : ObjectGeneratorFactory<T>() {
            override fun create(generatorRngs: GeneratorRngs): ObjectGenerator<T> = object : ObjectGenerator<T> {
                override val objectClass: Class<T> = T::class.java

                private val primitiveGenerator = primitiveGeneratorFactory.create(generatorRngs)
                private val referenceGenerator = referenceGeneratorFactory.create(generatorRngs)

                override fun next(): T = initializer(primitiveGenerator, referenceGenerator)
            }
        }

        /** Creates a factory for the common case where [String] is used for reference values. */
        inline fun <reified T : Any> of(
            primitiveGeneratorFactory: PrimitiveGeneratorFactory = PrimitiveGeneratorFactory.randomValues(),
            noinline initializer: (primitiveGenerator: PrimitiveGenerator, referenceGenerator: ObjectGenerator<String>) -> T,
        ): ObjectGeneratorFactory<T> = of(
            primitiveGeneratorFactory = primitiveGeneratorFactory,
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
 * Decorates this factory so each generated value independently matches [predicate] with probability [matchProbability].
 * The proportion of matching values in a collection is not fixed.
 */
fun <T> ObjectGeneratorFactory<T>.withPredicateMatchProbability(
    matchProbability: Percent,
    predicate: (T) -> Boolean,
): ObjectGeneratorFactory<T> {
    return object : ObjectGeneratorFactory<T>() {
        override fun create(generatorRngs: GeneratorRngs): ObjectGenerator<T> = PredicateMatchingObjectGenerator(
            source = this@withPredicateMatchProbability.create(generatorRngs),
            predicate = predicate,
            matchProbability = matchProbability,
            predicateOutcomeRng = generatorRngs.predicateOutcomeRng,
        )
    }
}
