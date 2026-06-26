# AI Context Map

Use this map to jump to the right files for common Codex tasks.

## Build And Dependencies

- Files: `settings.gradle.kts`, `build.gradle.kts`, `gradle/libs.versions.toml`, `gradle.properties`, `.github/workflows/ci.yml`.
- Boundaries: one Gradle root project named `pods4k-benchmarks`; no included subprojects in `settings.gradle.kts`.
- Inspect: `README.md` before changing JMH config.
- Pitfalls: `jmh.includes` controls what `./gradlew jmh` runs; changing it can turn a targeted run into a multi-hour run.

## Shared Utilities

- Files: `src/main/kotlin/com/danrusu/pods4kBenchmarks/utils/`.
- Tests: `src/test/kotlin/com/danrusu/pods4kBenchmarks/utils/`.
- Boundaries: utility source is manual, checked-in Kotlin. It is used by benchmark setup and should stay small/testable.
- Pitfalls: random data helpers affect benchmark comparability; preserve validation messages when tests assert exact text.

## Benchmark Setup Model

- Files: `src/jmh/kotlin/com/danrusu/pods4kBenchmarks/immutableArrays/setup/`.
- Key types: `CollectionType`, `DataType`, `FlatDataProducer`, `NullableDataProducer`, `FlatDataFilter`, collection wrappers.
- Boundaries: benchmark-only source set; not production library code.
- Tests/benchmarks to inspect: benchmark classes under `flatCollectionBenchmarks`, `objectCollectionBenchmarks`, and `nestedCollectionBenchmarks`.
- Pitfalls: wrapper properties throw by default; only access the property matching the active `CollectionType` and `DataType`.

## Flat Collection Benchmarks

- Files: `src/jmh/kotlin/.../immutableArrays/flatCollectionBenchmarks/`.
- Base: `flatCollectionBenchmarks/setup/FlatCollectionBenchmark.kt`.
- Benchmarks: operations such as `Any`, `Filter`, `Drop`, `Take`, `Plus`, `GroupBy`, `Sorted`, `ForEach`, `ForLoop`.
- Pitfalls: pairwise operations use `transformEachPairOfCollections` and need `@OperationsPerInvocation(NUM_COLLECTIONS / 2)`.

## Object Collection Benchmarks

- Files: `src/jmh/kotlin/.../immutableArrays/objectCollectionBenchmarks/`.
- Base: `objectCollectionBenchmarks/setup/ObjectCollectionBenchmark.kt`.
- Setup files: `ObjectProducer`, `WrapperForCollectionType`, `CompoundElement`, `CompoundElementOfNullableValues`.
- Pitfalls: avoid boxing/erasing immutable array variants in ways that make results no longer match normal public API usage.

## Nested Collection Benchmarks

- Files: `src/jmh/kotlin/.../immutableArrays/nestedCollectionBenchmarks/`.
- Base: `nestedCollectionBenchmarks/setup/NestedCollectionBenchmark.kt`.
- Setup files: `NestedCollectionWrapper`.
- Pitfalls: top-level and nested collection size distributions are separate; preserve this distinction.

## Generated And Local Artifacts

- Manual source: `src/main`, `src/test`, `src/jmh`.
- Generated/build output: `build/`, `.gradle/`, `.kotlin/`, JMH generated classes/results.
- Pitfalls: do not edit generated output or benchmark result CSVs as source.

## Documentation

- Files: `README.md`, `AGENTS.md`, `docs/ai/*.md`.
- Pitfalls: keep AI guidance concise and command-focused; do not duplicate benchmark implementations.
