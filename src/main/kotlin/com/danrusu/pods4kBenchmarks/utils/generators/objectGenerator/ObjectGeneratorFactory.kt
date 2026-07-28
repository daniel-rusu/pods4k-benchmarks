package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator.FieldGeneratorFactory

/** Creates [ObjectGenerator] instances from benchmark RNG streams */
abstract class ObjectGeneratorFactory<T> {
    /** Creates an object generator using the provided RNG streams. */
    abstract fun create(generatorRngs: GeneratorRngs): ObjectGenerator<T>

    companion object {
        /** Creates a factory for objects initialized from field and reference generators */
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
