# Benchmark Architecture

## Purpose

This project uses JMH to compare the published [Immutable Array][immutable-arrays-url] API with equivalent operations on
`List`, [PersistentList][persistent-list-url], and JVM arrays.

## Benchmark Matrix

`CollectionBenchmark` defines two JMH `@Param` axes:

- `CollectionType`
    - `LIST`: `ArrayList` exposed as `List<T>`
    - `PERSISTENT_LIST`: `kotlinx.collections.immutable.PersistentList<T>`
    - `ARRAY`: `Array<T>` for references and nullable values; primitive arrays for non-null primitives
    - `IMMUTABLE_ARRAY`: `ImmutableArray<T>` for references and nullable values; primitive-specialized immutable arrays
      for non-null primitives
- `DataType`: `REFERENCE`, `BOOLEAN`, `BYTE`, `CHAR`, `SHORT`, `INT`, `FLOAT`, `LONG`, and `DOUBLE`

Together, these axes produce **36 independent trials** per benchmark method.

## Runtime Flow

1. A benchmark class defines equivalent, statically typed operations for each collection representation and supplies a
   data-generation recipe.
2. JMH selects one `CollectionType` and `DataType` combination.
3. `@Setup(Level.Trial)` builds a deterministic batch for only that combination.
4. The benchmark invokes the operation on every collection in the batch.
5. JMH consumes each result with `Blackhole` and normalizes the score using `@OperationsPerInvocation`.

## Benchmark Categories

Benchmark categories describe the shape of the data consumed by an operation:

| Category      | Shape                                                       | Examples                                                                          |
|---------------|-------------------------------------------------------------|-----------------------------------------------------------------------------------|
| Flat          | `CollectionType<DataType>`                                  | `BooleanArray`<br/>`List<Boolean>`                                                |
| Nullable flat | `CollectionType<DataType?>`                                 | `Array<Double?>`<br/>`List<Double?>`                                              |
| Nested        | `CollectionType<CollectionOwner<CollectionType<DataType>>>` | `Array<CollectionOwner<BooleanArray>>`<br/>`List<CollectionOwner<List<Boolean>>>` |
| Object        | `CollectionType<CustomType>`                                | `Array<CustomType>`<br/>`List<CustomType>`                                        |

Nested outer and inner collection sizes use separate distributions. This models cases such as orders containing fewer
products than the total number of orders.

## 4. Code Organization

This repository is structured into 3 source-sets:

### Source-Set Boundary

| Source set        | Architectural responsibility                                                               |
|-------------------|--------------------------------------------------------------------------------------------|
| `src/main/kotlin` | Reusable utilities, shared infrastructure, and deterministic benchmark-data builders.      |
| `src/jmh/kotlin`  | JMH lifecycle, typed operation dispatch, benchmark-only fixtures, and measured operations. |
| `src/test/kotlin` | Tests for the utilities and benchmark-data builders in `src/main`.                         |

This keeps data generation and materialization directly unit-testable and prevents JMH lifecycle concerns from leaking
into reusable setup logic.

### Benchmark organization

An operation is benchmarked in `<Operation>Benchmarks` which defines equivalent operations across different collection
types along with the data-generation recipe for how collections should be created. It extends from
`<Category>CollectionBenchmark` depending on its benchmark category. `<Category>CollectionBenchmark` uses
`<Category>CollectionBenchmarkData` to generate the benchmark data for the current trial and invokes the appropriate
typed operation on each collection in the current batch.

For example, the `filter` operation is benchmarked in `FilterBenchmarks`. It extends from `FlatCollectionBenchmark`
because we're filtering flat data.  `FlatCollectionBenchmarkData` is used to generate the dataset for each
`CollectionType` & `DataType` combination.

## 5. Data Construction

This is the data generation flow for the `drop` operation. The nested, nullable-flat, and object-collection benchmark
categories follow the same general pattern:

1. `DropBenchmarks` specifies the data generation recipe:
    * `numCollections` to create
    * Default `DistributionFactory` for sampling the collection sizes
    * Default `ObjectGeneratorFactory<String>` & `FieldGeneratorFactory` for random strings & primitives
2. JMH iterates through every `CollectionType` & `DataType`, and creates `DropBenchmarks` with the current combination.
3. JMH calls `FlatCollectionBenchmark.setupBenchmarkData()` to begin data construction.
4. Data creation is delegated to `FlatCollectionBenchmarkData.create(...)` which performs the following actions
    * Create `SplittableRandom` RNG stream from constant seed
    * Split off separate RNG streams for each aspect of data generation (values, collection sizes, etc.)
    * Use the factories and associated RNG streams to create size `Distribution`, `FieldGenerator`, & `ObjectGenerator`
    * Create a `CollectionBatch` with an array of `numCollections` collections. Each collection is created with
      `CollectionFactory`
        * size sampled from the size `Distribution`
        * `CollectionType` & `DataType` controls the type of collection to be created
        * elements generated from the `FieldGenerator` or `ObjectGenerator` depending on the `DataType`

Although there are 36 `CollectionType` & `DataType` combinations, benchmarking data is only constructed for the current
combination. Eg. the `CollectionBatch` stores an array of `List<Boolean>` collections when `CollectionType = LIST` &
`DataType = BOOLEAN`.

### Nullability Handling

Benchmarks that deal with null values, such as `FilterNotNullBenchmarks`, specify factories that create `null` values
null-ratio portion of the time. During data generation, the next element will be null if a random `double` is less than
the null ratio.

### Predicate Handling

Benchmarks that deal with predicates, such as `FilterBenchmarks`, specify the acceptance ratio. Elements are accepted if
their value is smaller than the median value of their data type. When generating the next element, the RNG determines
whether it should be accepted by checking whether a random `double` is less than the acceptance ratio. If the next
element should be accepted then we repeatedly generate random values discarding them until we find one smaller than the
median value (and vice versa).

Note that predicate decisions are shifted to the data generation phase in order to remove the RNG overhead from the
benchmark, and instead focus on the performance of the operation.

## 6. Fair-Comparison Safeguards

### Comparable API Work

Each benchmark supplies equivalent, statically typed operations for every supported representation through its published
public API. JVM arrays and immutable arrays retain their primitive-specialized forms to represent real-world usage.

### Equivalent, Deterministic Inputs

Each parameterized trial starts with a new zero-seeded `RngFactory`. For a given `DataType`, every `CollectionType`
therefore receives the same collection sizes and element values.

A `SplittableRandom` RNG is split into separate streams for values, collection sizes, nullability, and
predicate-acceptance decisions. Generating String elements that consumes more random values cannot change collection
sizes, null positions, or which elements should satisfy a predicate. This makes throughput measurements directly
comparable across different collections libraries and also across different data types.

### Isolated Trial Data

`@Setup(Level.Trial)` constructs inputs before timed work and materializes only the active `CollectionType`/`DataType`
combination. Competing representations cannot add cache pressure or gain an advantage from their position in the setup
or benchmark sequence.

### Batched Work and Score Normalization

One invocation processes hundreds of distinct, prebuilt collections instead of repeatedly operating on one hot input.
This broadens the working set with varied sizes and values, reducing cache and branch-prediction bias.

Collection sizes are sampled from a distribution that models the mix of empty, small, medium, and occasional large
collections found in business workloads.

### JMH Measurement Controls

Every produced result is consumed by `Blackhole`, preventing unused work from being optimized away. Current benchmark
classes also use the same protocol: throughput mode, ten one-second warmup iterations, seven one-second measurement
iterations, and two independent JVM forks.

[immutable-arrays-url]: https://github.com/daniel-rusu/pods4k/tree/main/immutable-arrays

[persistent-list-url]: https://github.com/Kotlin/kotlinx.collections.immutable
