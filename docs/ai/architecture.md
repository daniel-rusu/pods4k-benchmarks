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

1. A JMH base state receives a fixed `numCollections` plus generator and distribution factories.
2. `@Param` supplies a `CollectionType` and, where relevant, a `DataType`.
3. `@Setup(Level.Trial)` calls the matching `*BenchmarkData.create` factory.
4. The data builder creates a constant-seed `RngFactory`, separates size/value/null/filter streams, and materializes
   only the selected representation.
5. The benchmark helper dispatches to statically typed transforms and consumes every result with `Blackhole`.

Flat and nested data holders retain one `Array<*>` for the active parameter combination. Their typed accessors validate
the element class before casting, preventing a benchmark from silently reading an unused or mismatched data field.
Object data is generic in its element type and uses the outer array's runtime component type to guard representation
casts.

## Core Types

- `CollectionType`: `LIST`, `PERSISTENT_LIST`, `ARRAY`, and `IMMUTABLE_ARRAY`.
- `DataType`: `REFERENCE` plus the eight Kotlin primitive families.
- `RngFactory` and `BenchmarkGeneratorRngs`: deterministic, purpose-specific random streams.
- `DistributionFactory`: flat and nested collection-size models.
- `FieldGeneratorFactory` and `ObjectGeneratorFactory`: configurable element generation.
- `FlatCollectionBenchmarkData`, `ObjectCollectionBenchmarkData`, and `NestedCollectionBenchmarkData`: typed trial-data
  builders.
- `FlatCollectionBenchmark`, `ObjectCollectionBenchmark`, and `NestedCollectionBenchmark`: JMH state and dispatch
  helpers.
- `FlatDataFilter`: benchmark-only factories that generate a controlled predicate acceptance ratio.

## Invariants

- Changing value generation for one data type must not also change sizes, null positions, or filter decisions.
- Each invocation processes distinct prebuilt collections; setup cost is excluded unless construction is the scenario.
- `@OperationsPerInvocation` equals the number of operations performed by one method call. Pairwise helpers process
  `NUM_COLLECTIONS / 2` pairs.
- Comparisons use normal public APIs and equivalent operations across all representations supported by that benchmark.
- Generated JMH code and results under `build/` are disposable artifacts, not source.
