# Architecture

## Purpose

This repo benchmarks the published `pods4k` dependency in an environment that does not have access to library internals. The current benchmark surface focuses on `immutableArrays` and compares `pods4k` immutable arrays against Kotlin/JVM `List` and JVM arrays.

## Source Sets And Layout

- `src/main/kotlin`: shared utilities and deterministic benchmark-data builders tested by unit tests.
- `src/test/kotlin`: JUnit 5 + Strikt tests for shared utilities.
- `src/jmh/kotlin`: JMH benchmarks plus benchmark-only setup and wrapper code.
- `src/*/java` and `src/*/resources`: present as source-set directories but currently empty in the inspected tree.
- Gradle layout: single root project; Kotlin/JVM plugin, `jvm-test-suite`, and `me.champeau.jmh` plugin.

## Core Abstractions

- `CollectionType`: `LIST`, `PERSISTENT_LIST`, `ARRAY`, `IMMUTABLE_ARRAY`.
- `DataType`: reference plus primitive/value families: `BOOLEAN`, `BYTE`, `CHAR`, `SHORT`, `INT`, `FLOAT`, `LONG`, `DOUBLE`.
- `RngFactory`: deterministic source of independent RNG streams created from a constant master seed.
- `Distribution` and `DistributionFactory`: deterministic collection-size distributions.
- `FieldGenerator`, `ObjectGenerator`, and their factories: generator strategies for benchmark setup.
- `AlphanumericCharacters`: shared generated character domain and natural-order median used by char/string generators and filters.
- `FlatDataFilter`: filtered field and string generator factories for predicate-oriented flat benchmarks.
- `CollectionOwner`: models the intentional object layer around a nested collection, such as a person with friends.
- Benchmark bases: `FlatCollectionBenchmark`, `ObjectCollectionBenchmark`, and `NestedCollectionBenchmark`.
- `FlatCollectionBenchmarkData` and `NestedCollectionBenchmarkData`: strongly typed data for flat and nested benchmark
  trials, with deterministic construction in their companion objects.

## Generated-Code Model

- No generated source is checked in under `src`.
- JMH-generated code/classes and result files are build artifacts under `build/`.
- Treat `build/`, `.gradle/`, `.kotlin/`, and benchmark result files as disposable output.

## Public API Shape

- Benchmarks import `com.danrusu.pods4k.immutableArrays.*` from the configured dependency in `gradle/libs.versions.toml`.
- The benchmark intent is public API usage. Do not depend on internal `pods4k` implementation details.
- Keep API-facing comparisons symmetrical: the same operation should be represented for `List`, array, and immutable-array variants when the benchmark category expects all three.

## Important Invariants

- Benchmark setup uses constant master seeds so scenarios are comparable across collection and data types.
- Size distribution, filter acceptance, null placement, and generated values use separate RNG streams where needed so changing value generation for one data type does not also change predicate or null distributions.
- Benchmark methods consume results through `Blackhole`.
- `numCollections` overrides are expected to be fixed values.
- `@OperationsPerInvocation` represents collections processed per invocation; for pairwise benchmarks it should usually be `NUM_COLLECTIONS / 2`.
- `@Setup(Level.Trial)` prepares data; benchmark methods should measure the operation, not data construction unless that is the explicit scenario.
- Flat and nested benchmark setup replaces its data holder for each trial; benchmark states that retain mutable backing
  fields use `@TearDown` to clear large arrays and reduce cross-trial memory retention.

## Boundaries

- Manual utility code belongs in `src/main`; add or update tests in `src/test`.
- Benchmark scenarios and benchmark-only fixtures belong in `src/jmh`.
- Build configuration lives in Gradle files; do not bury benchmark selection rules in source comments when `jmh.includes` is the controlling mechanism.
- Benchmark results are evidence to report, not source to edit.
