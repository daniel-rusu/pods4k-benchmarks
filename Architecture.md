# Benchmark Architecture

## 1. Overview

### Purpose and Scope

This project uses JMH to benchmark operations on [Immutable Arrays][immutable-arrays-url] with equivalent operations on
regular arrays, `ArrayList`, and [PersistentList][persistent-list-url].

### Architecture at a Glance

1. Benchmark classes define
    * Operations to benchmark across representations (eg. `List<Boolean>.filter{...}`, `BooleanArray.filter{...}`, ...)
    * Recipe for data generation (eg. size distribution, element generation, etc.)
2. JMH iterates through each combination of `CollectionType` and `DataType`
3. Create fixed-seed RNG streams and use the data-generation recipe to create collections for the current
   `CollectionType` & `DataType`
4. Invoke the operation on each collection and measure throughput

## 2. Benchmark Model

### Collection Representations

### Data Types

### Parameter Matrix

### Work per Invocation

## 3. Benchmark Categories

Benchmark families are organized by the structure of the data that is consumed.

### Flat Collections

For operating on data structured as `CollectionType<DataType>`

* E.g. `List<BOOLEAN>` when `CollectionType = LIST` & `DataType = BOOLEAN`
* E.g. `BooleanArray` when `CollectionType = ARRAY` & `DataType = BOOLEAN`

### Nullable Flat Collections

For operating on data structured as `CollectionType<DataType?>`

* E.g. `List<Double?>` when `CollectionType = LIST` & `DataType = DOUBLE`
* E.g. `Array<Float?>` when `CollectionType = ARRAY` & `DataType = FLOAT`

### Nested Collections

For operating on data structured as `CollectionType<CollectionOwner<CollectionType<DataType>>`. Represents nested
scenarios such as a list of orders with each order containing a list of products.

* E.g. `List<CollectionOwner<List<Boolean>>` when `CollectionType = LIST` & `DataType = BOOLEAN`
* E.g. `Array<CollectionOwner<BooleanArray>` when `CollectionType = ARRAY` & `DataType = BOOLEAN`

The nested collection is constructed from a separate size distribution to better model the real world as the number of
products in an order it usually smaller than the number of orders.

### Object Collections

For operating on data structured as `CollectionType<CustomType>`. Parameterized by `CollectionType`:

* E.g. `List<CustomType>` when `CollectionType = LIST`
* E.g. `Array<CustomType>` when `CollectionType = ARRAY`

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

A `*Benchmarks` class defines the recipe for measuring an operation across different collections. It extends from the
`*CollectionBenchmark` class in its benchmark category. The `*CollectionBenchmark` manages the JMH parameters and uses
the `*CollectionBenchmarkData` class to generate the benchmark data for the current trial.

For example, the `filter` operation is measured on flat collections and is structured as:

```text
src
├── jmh/kotlin/com/danrusu/pods4kBenchmarks/immutableArrays
│   └── flatCollectionBenchmarks
│       ├── setup
│       │   └── FlatCollectionBenchmark.kt      # Base class for all benchmarks that operate on flat collections
│       └── FilterBenchmarks.kt                 # Recipe for benchmarking the filter operation
├── main/kotlin/...
│   └── flatCollectionBenchmarks
│       └── setup
│           └── FlatCollectionBenchmarkData.kt  # Generates collections for the current benchmark trial
└── test/kotlin/...
    └── flatCollectionBenchmarks
        └── setup
            └── FlatCollectionBenchmarkDataTest.kt  # Validates generated data is identical across collection types etc.
```

The nested, nullable-flat, and object-collection benchmark categories follow the same general pattern.

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
    * Create a `CollectionBatch` with `numCollections` collections. Each collection is created with `CollectionFactory`
        * size sampled from the size `Distribution`
        * `CollectionType` & `DataType` controls the type of collection to be created
        * elements generated from the `FieldGenerator` or `ObjectGenerator` depending on the `DataType`

Although there are 27 `CollectionType` & `DataType` combinations, benchmarking data is only constructed for the current
combination.

* E.g. when `CollectionType = ARRAY` & `DataType = BOOLEAN`, the `CollectionBatch` will contain `Array<BooleanArray>`.
  The `drop` operation will be called each `BooleanArray` instance.

### Nullability Handling

### Predicate Handling

The object-collection benchmarks are the current exception to the common parameter matrix: their custom type is fixed
by the benchmark, so they vary only by `CollectionType`. Their construction flow is otherwise the same.

## 6. Measurement Invariants

- Public API Boundary
- Equivalent operations across representations
- Only current representation is materialized
    - Avoids unfair cache boost of last representation
- Operations per Invocation
    - Reduce L1 cache boost
    - Reduce CPU branch predictor boost
- All representations get identical data
    - Identical sequence of collection sizes
    - Identical element values
    - Primitive specialization
- Identical attributes across data types
    - nullability
    - predicates

[immutable-arrays-url]: https://github.com/daniel-rusu/pods4k/tree/main/immutable-arrays

[persistent-list-url]: https://github.com/Kotlin/kotlinx.collections.immutable
