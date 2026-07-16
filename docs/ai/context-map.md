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
- Key types: `RngFactory`, `DistributionFactory`, `GeneratorRngs`, `CollectionType`, `DataType`,
  `BenchmarkGeneratorRngs`, and the three `*BenchmarkData` builders.
- Preserve purpose-specific RNG streams and runtime element-type checks; both protect comparability across parameter
  combinations.

## JMH Benchmarks

- Root: `src/jmh/kotlin/com/danrusu/pods4kBenchmarks/immutableArrays/`.
- Flat: `flatCollectionBenchmarks/`; base state in `setup/FlatCollectionBenchmark.kt`.
- Object: `objectCollectionBenchmarks/`; generic base state in `setup/ObjectCollectionBenchmark.kt`.
- Nested: `nestedCollectionBenchmarks/`; base state in `setup/NestedCollectionBenchmark.kt`.
- Shared benchmark-only filtering: `setup/FlatDataFilter.kt`.
- Pairwise flat operations use `transformEachPairOfCollections` and
  `@OperationsPerInvocation(NUM_COLLECTIONS / 2)`.

## Artifacts And Documentation

- Manual source: `src/main`, `src/test`, and `src/jmh`.
- Generated/local output: `build/`, `.gradle/`, `.kotlin/`, generated JMH classes, and result CSVs; never edit these as
  source.
- Documentation: `README.md`, `AGENTS.md`, and `docs/ai/*.md`. Keep guidance focused on stable boundaries and commands
  rather than copying implementation details.
