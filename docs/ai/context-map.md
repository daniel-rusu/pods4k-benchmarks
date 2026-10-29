# AI Context Map

Use this map to locate the smallest relevant area before making a change.

## Build And Execution

- Configuration: `settings.gradle.kts`, `build.gradle.kts`, `gradle/libs.versions.toml`, `gradle.properties`, and
  `.github/workflows/ci.yml`.
- The repository is one Gradle project. `jmh.includes` controls what `./gradlew jmh` executes; broad filters can run for
  hours.
- User-facing run instructions belong in `README.md`; detailed agent workflow belongs in `benchmark-workflow.md`.

## Shared Data Infrastructure

- General utilities: `src/main/kotlin/com/danrusu/pods4kBenchmarks/utils/`.
- Immutable-array setup: `src/main/kotlin/com/danrusu/pods4kBenchmarks/immutableArrays/`.
- Tests mirror both areas under `src/test/kotlin/com/danrusu/pods4kBenchmarks/`.
- Key types: `Percent`, `RngFactory`, `Distribution`, `GeneratorRngs`, `CollectionType`, `DataType`,
  `BenchmarkGeneratorRngs`, `CollectionFactory`, `CollectionBatch`, and the four `*BenchmarkData` builders, including
  `NullableFlatCollectionBenchmarkData` for boxed nullable elements.
- Preserve purpose-specific RNG streams and runtime element-type checks; both protect comparability across parameter
  combinations.
- `immutableArrays/setup/BenchmarkGeneratorRngs.kt` owns eager, append-only stream allocation, including separate outer
  and nested collection sizes. Its matching test checks seeds, independence, setup order, and fixed sequence samples.
- `utils/Percent.kt` owns validated percentage declarations (`50.percent()`), exact addition, and numeric weighting.
  Random decisions belong to `Random.nextBoolean(trueProbability: Percent)` in `utils/RandomExtensions.kt`.
  `utils/Distribution.kt` owns positive bucket weights, exact 100% totals, and selection using one Double threshold;
  `inRange` is a top-level extension there. The public constructor binds the supplied RNG directly.
- `immutableArrays/setup/BenchmarkSizeDistributions.kt` owns `outerCollectionSizes` and `nestedCollectionSizes` presets.
- Each data builder has a matching `*CollectionRecipe` in its setup package. Recipes hold actual generation components;
  builders accept separate batch counts and parameter selections without allocating RNGs or resetting components.

## JMH Benchmarks

- Root: `src/jmh/kotlin/com/danrusu/pods4kBenchmarks/immutableArrays/`.
- Shared state and the `CollectionType`/`DataType` parameters: `setup/CollectionBenchmark.kt`.
- Flat: `flatCollectionBenchmarks/`; specialized setup and dispatch in `setup/FlatCollectionBenchmark.kt`.
- Nullable flat: `nullableFlatCollectionBenchmarks/`; specialized setup and dispatch in
  `setup/NullableFlatCollectionBenchmark.kt`.
- Object: `objectCollectionBenchmarks/`; generic setup and dispatch in `setup/ObjectCollectionBenchmark.kt`.
- Nested: `nestedCollectionBenchmarks/`; specialized setup and dispatch in `setup/NestedCollectionBenchmark.kt`.
- Each specialized base creates one fresh RNG bundle and invokes `createDataRecipe` once per trial setup. Standard flat
  and repeated predicate wiring live in `flatCollectionBenchmarks/setup/StandardFlatCollectionRecipe.kt`, called
  explicitly inside trial lambdas. Other recipes remain scenario-local.
- Shared batch-size defaults and cache-footprint guidance: `setup/BenchmarkBatchSize.kt`.
- Reusable predicate conditioning: `utils/generators/primitiveGenerator/PredicateMatchingPrimitiveGenerator.kt` and
  `utils/generators/objectGenerator/PredicateMatchingObjectGenerator.kt` in `src/main`.
- Shared midpoint scenario predicate: `utils/predicates/MedianValuePredicate.kt` in `src/main`.
- Pairwise flat operations use `transformEachPairOfCollections` and
  `@OperationsPerInvocation(NUM_COLLECTIONS / 2)`.

## Artifacts And Documentation

- Manual source: `src/main`, `src/test`, and `src/jmh`.
- Generated/local output: `build/`, `.gradle/`, `.kotlin/`, generated JMH classes, and result CSVs; never edit these as
  source.
- Documentation: `README.md`, `Architecture.md`, `AGENTS.md`, and `docs/ai/*.md`. Keep guidance focused on stable
  boundaries and commands rather than copying implementation details.
