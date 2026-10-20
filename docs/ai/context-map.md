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
- Key types: `Percent`, `RngFactory`, `DistributionFactory`, `GeneratorRngs`, `CollectionType`, `DataType`,
  `BenchmarkGeneratorRngs`, `CollectionFactory`, `CollectionBatch`, and the four `*BenchmarkData` builders, including
  `NullableFlatCollectionBenchmarkData` for boxed nullable elements.
- Preserve purpose-specific RNG streams and runtime element-type checks; both protect comparability across parameter
  combinations.
- `utils/Percent.kt` owns validated percentage declarations (`50.percent()`), exact addition, and numeric weighting.
  Random decisions belong to `Random.nextBoolean(trueProbability: Percent)` in `utils/RandomExtensions.kt`.
  `utils/Distribution.kt` owns positive bucket weights, exact 100% totals, and selection using one Double threshold;
  `inRange` is a top-level extension there.

## JMH Benchmarks

- Root: `src/jmh/kotlin/com/danrusu/pods4kBenchmarks/immutableArrays/`.
- Shared state and the `CollectionType`/`DataType` parameters: `setup/CollectionBenchmark.kt`.
- Flat: `flatCollectionBenchmarks/`; specialized setup and dispatch in `setup/FlatCollectionBenchmark.kt`.
- Nullable flat: `nullableFlatCollectionBenchmarks/`; specialized setup and dispatch in
  `setup/NullableFlatCollectionBenchmark.kt`.
- Object: `objectCollectionBenchmarks/`; generic setup and dispatch in `setup/ObjectCollectionBenchmark.kt`.
- Nested: `nestedCollectionBenchmarks/`; specialized setup and dispatch in `setup/NestedCollectionBenchmark.kt`.
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
- Documentation: `README.md`, `AGENTS.md`, and `docs/ai/*.md`. Keep guidance focused on stable boundaries and commands
  rather than copying implementation details.
