# Architecture

## Purpose

This repository benchmarks the published `pods4k` dependency through its public API. The current suite compares
`ImmutableArray` variants with `List`, `PersistentList`, and JVM array equivalents.

## Source-Set Boundary

- `src/main/kotlin`: reusable utilities and deterministic data builders. These types have no JMH dependency and are
  directly unit-testable.
- `src/test/kotlin`: JUnit 5 + Strikt tests for shared utilities and benchmark data.
- `src/jmh/kotlin`: JMH states, scenario-only fixtures, and measured operations.

This split is the central architectural boundary: data construction belongs in `src/main` when it is reusable and
testable; JMH lifecycle and scenario-specific code stays in `src/jmh`.

## Data Flow

1. `CollectionBenchmark` defines the shared `Scope.Benchmark` state and uses `@Param` to supply both a
   `CollectionType` and a `DataType`, producing 36 parameter combinations per benchmark method.
2. A specialized benchmark base receives a separate fixed `numCollections` and one `createDataRecipe` lambda for
   flat, nullable-flat, object, or nested data.
3. Its `@Setup(Level.Trial)` creates a fresh `BenchmarkGeneratorRngs` with the default master seed (`0L`) and invokes
   the lambda exactly once, immediately before materialization. The lambda constructs all mutable generation components
   from the supplied purpose-specific streams without sampling or creating additional RNG bundles.
4. The matching `*BenchmarkData.create` accepts the actual recipe, separate batch count, `CollectionType`, and
   `DataType` (except for object data). It uses those components directly without resetting or reconstructing them,
   and delegates collection materialization to `CollectionBatch` and `CollectionFactory`.
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
- `RngFactory`: splits deterministic RNG streams from a master seed.
- `BenchmarkGeneratorRngs`: eagerly allocates value, nullability, predicate-outcome, outer-size, and nested-size streams
  from one `RngFactory`, exposing them through `GeneratorRngs` independently of setup order.
- `Percent`: a validated fixed-point value from 0% to 100% with two decimal places. `Int.percent()` and
  `Double.percent()` interpret inputs as percentages, so `50.percent()` means 50%. It owns exact addition, ordering,
  and numeric weighting through `scale`; consumers cannot access its representation.
- `Random.nextBoolean(trueProbability: Percent)` in `utils/RandomExtensions.kt`: probability decisions using
  `nextDouble() < trueProbability.scale(1.0)`, with ordinary Double precision.
- `CollectionBatch`: shared outer-array materialization, erased storage, and runtime validation for one trial-data
  representation.
- `Distribution`: generic RNG-bound size models using positive `Percent` bucket weights with an exact 100% total.
  Each value uses one Double threshold and then an inclusive range draw. The public constructor binds the supplied
  stream directly. Workload presets `outerCollectionSizes` and `nestedCollectionSizes` live in benchmark setup.
- `FlatCollectionRecipe`, `NullableFlatCollectionRecipe`, `ObjectCollectionRecipe<T>`, and `NestedCollectionRecipe`:
  independent containers with `val` references to actual distributions, generators, and policies. Their dependencies
  may be stateful; a recipe serves one materialization and contains no batch count. Reproducible builds require fresh,
  identically seeded recipes.
- `RandomPrimitiveGenerator`, `StringGenerator`, and `objectGenerator { ... }`: direct element generation. The
  initializer helper retains the runtime element class and invokes its supplied initializer only on `next()`.
- `PredicateMatchingPrimitiveGenerator` and `PredicateMatchingObjectGenerator`: adapters exposed through actual-generator
  `withPredicateMatchProbability` extensions taking an explicit outcome RNG. Each independently chooses the required
  predicate outcome, then rejection-samples the source; collection match proportions vary.
- `MedianValuePredicate`: the scenario predicate shared by predicate benchmark setup and measured operations.
- `RandomNullabilityPolicy`: constructed from the dedicated nullability stream in a recipe. Nullable-flat builders
  apply the supplied policy uniformly to references and boxed primitives. `ObjectGenerator.nullable(policy)` adapts an
  actual generator directly. Null choices suppress source generation. Nullable compound recipes draw one decision per
  object and apply it to all properties, preserving property-generation order.
- Nullability and predicate-match probabilities accept `Percent`. Each decision consumes one `nextDouble()` call,
  including at 0% and 100%; rejected predicate candidates do not consume further outcome decisions.
- `CollectionBenchmark`: shared JMH benchmark state that defines the `CollectionType` and `DataType` parameter axes.
- `BenchmarkBatchSize`: shared collection-count defaults chosen by the data each operation fetches or writes, rather
  than the total trial dataset size.
- `FlatCollectionBenchmarkData`, `NullableFlatCollectionBenchmarkData`, `ObjectCollectionBenchmarkData`, and
  `NestedCollectionBenchmarkData`: typed trial-data builders. The nullable flat builder stores boxed nullable elements
  and is used by `FilterNotNullBenchmarks`.
- `FlatCollectionBenchmark`, `NullableFlatCollectionBenchmark`, `ObjectCollectionBenchmark`, and
  `NestedCollectionBenchmark`: specialized trial setup and statically typed dispatch helpers.

Standard and repeated predicate flat configurations use `standardFlatCollectionRecipe` and
`withPredicateMatchProbability` in JMH setup, called explicitly inside trial lambdas. Other specialized recipes stay
in their JMH scenarios. Unit tests cover main-source builders, direct components, stateful test recipes, and datasets
captured before migration. They do not import JMH configurations or validate the trial lifecycle and shared helpers;
review that wiring and compile the generated harness with `build jmhJar --no-daemon`.

## Invariants

- Changing value generation for one data type must not also change sizes, null positions, or predicate outcomes.
- Keep stream allocation in `BenchmarkGeneratorRngs` append-only. `BenchmarkGeneratorRngsTest` checks seed handling,
  stream independence, size-distribution setup order, and fixed sequence samples. Reordering setup across different
  purposes preserves stream sequences; changing draws within a stream can still change its generated data.
- Each invocation processes distinct prebuilt collections; setup cost is excluded unless construction is the scenario.
- Benchmarks use the shared `BenchmarkBatchSize` tier matching their access and result pattern unless a documented
  benchmark-local override is warranted.
- `@OperationsPerInvocation` equals the number of operations performed by one method call. Pairwise helpers process
  `NUM_COLLECTIONS / 2` pairs.
- Comparisons use normal public APIs and equivalent operations across all representations supported by that benchmark.
- Generated JMH code and results under `build/` are disposable artifacts, not source.
