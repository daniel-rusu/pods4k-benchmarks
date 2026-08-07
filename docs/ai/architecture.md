# Architecture

## Purpose

This repository benchmarks the published `pods4k` dependency through its public API. The current suite compares
`ImmutableArray` variants with `List`, `PersistentList`, and JVM array equivalents.

## Source-Set Boundary

- `src/main/kotlin`: reusable utilities and deterministic data builders. These types have no JMH dependency and are
  directly unit-testable.
- `src/test/kotlin`: JUnit 5 + Strikt tests for shared utilities and benchmark data.
- `src/jmh/kotlin`: JMH states, benchmark-only filtering/fixtures, and measured operations.

This split is the central architectural boundary: data construction belongs in `src/main` when it is reusable and
testable; JMH lifecycle and scenario-specific code stays in `src/jmh`.

## Data Flow

1. `CollectionBenchmark` defines the shared `Scope.Benchmark` state and uses `@Param` to supply both a
   `CollectionType` and a `DataType`, producing 36 parameter combinations per benchmark method.
2. A specialized benchmark base receives a fixed `numCollections` plus the generator and distribution factories for
   its data shape.
3. Its `@Setup(Level.Trial)` method calls the matching `*BenchmarkData.create` factory.
4. The data builder creates a constant-seed `RngFactory`, separates size/value/null/filter streams, and passes its
   single-collection construction logic to `CollectionBatch`, which materializes only the selected representation.
5. The benchmark helper dispatches to statically typed transforms and consumes every result with `Blackhole`.

All benchmark-data holders delegate erased storage to a `CollectionBatch`, which retains the active `CollectionType`,
logical element runtime class, and one `Array<*>` for the active parameter combination. The logical element is the value
operated on by a benchmark; in nested data it is the innermost value beneath `CollectionOwner` and its collection. The
batch owns outer-array allocation and validates the requested representation and logical element class through one
checked accessor before casting. The outer array's runtime component type validates stored collections and provides a
final representation guard. Each benchmark-data holder owns the statically typed representation-specific accessors
exposed to its benchmarks.

## Core Types

- `CollectionType`: `LIST`, `PERSISTENT_LIST`, `ARRAY`, and `IMMUTABLE_ARRAY`.
- `DataType`: `REFERENCE` plus the eight Kotlin primitive families.
- `RngFactory` and `BenchmarkGeneratorRngs`: deterministic, purpose-specific random streams.
- `CollectionBatch`: shared outer-array materialization, erased storage, and runtime validation for one trial-data
  representation.
- `DistributionFactory`: flat and nested collection-size models.
- `FieldGeneratorFactory` and `ObjectGeneratorFactory`: configurable element generation.
- `CollectionBenchmark`: shared JMH benchmark state that defines the `CollectionType` and `DataType` parameter axes.
- `FlatCollectionBenchmarkData`, `NullableFlatCollectionBenchmarkData`, `ObjectCollectionBenchmarkData`, and
  `NestedCollectionBenchmarkData`: typed trial-data builders. The nullable flat builder stores boxed nullable elements
  and is used by `FilterNotNullBenchmarks`.
- `FlatCollectionBenchmark`, `NullableFlatCollectionBenchmark`, `ObjectCollectionBenchmark`, and
  `NestedCollectionBenchmark`: specialized trial setup and statically typed dispatch helpers.
- `FlatDataFilter`: benchmark-only factories that generate a controlled predicate acceptance ratio.

## Invariants

- Changing value generation for one data type must not also change sizes, null positions, or filter decisions.
- Each invocation processes distinct prebuilt collections; setup cost is excluded unless construction is the scenario.
- `@OperationsPerInvocation` equals the number of operations performed by one method call. Pairwise helpers process
  `NUM_COLLECTIONS / 2` pairs.
- Comparisons use normal public APIs and equivalent operations across all representations supported by that benchmark.
- Generated JMH code and results under `build/` are disposable artifacts, not source.
