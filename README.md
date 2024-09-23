# pods4k-benchmarks

This repo contains benchmarks for the [pods4k library](https://github.com/daniel-rusu/pods4k). The benchmarks are in
this separate repo to resemble real environments where the library is added as a regular dependency without access to
any library internals.

The benchmarks use the [Java Microbenchmark Harness](https://github.com/openjdk/jmh), JMH, along with
the [JMH Gradle Plugin](https://github.com/melix/jmh-gradle-plugin). JMH helps us avoid JVM benchmarking pitfalls
and produce more realistic results.

## Benchmark Configuration

The benchmarks are configured with the following JMH annotations:

#### @Setup

* Marks the method that needs to be called to prepare the benchmarking dataset(s) before starting the benchmark
* `Level.Trial` - Only perform the setup once for each benchmark

#### @Benchmark

* Marks a method that we want to measure the performance of

#### @Param

* Parameterizes a variable with multiple values and repeats all the benchmarks for each value.

#### @BenchmarkMode

* `Mode.Throughput` - measures the number of operations per time unit

#### @OutputTimeUnit

* The time unit to report the results in (eg. seconds, milliseconds, etc.)

#### @OperationsPerInvocation

* Repeating an operation on the same data many times will result in the CPU optimizing subsequent operations by keeping
  the data in the fast L1 cache along with priming the branch predictor etc. producing misleadingly-fast results that
  aren't representative of the real world.
* To avoid this concern, instead of repeating the operation on the same data, we can create hundreds of randomized
  datasets. In a single benchmark invocation, we loop through each dataset and perform the operation being measured.
* This annotation specifies how many times the operation is being performed in 1 execution of the benchmark method.
  Since we're repeating the operation for each of the datasets, this value is equal to the number of datasets.

#### @Warmup

* The JVM JIT compiler optimizes and re-compiles the program on the fly after gathering sufficient information. This
  annotation controls the warmup behavior to ensure that the JIT compiler had sufficient time to bring the performance
  to a steady state before real measurements begin.
* `iterations` - The # of warmup iterations that should be performed before actual measurements begin
* `time` - The # of time units that each warmup iteration should last
* `timeUnit` - The time unit (eg. seconds, milliseconds, etc.)
* Example: `@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)`
    * Perform 10 warmups with each warmup lasting 1 second
    * During each warmup iteration, the benchmark method is called repeatedly until 1 second passes

#### @Measurement

* Controls how many measurements to take and how long each measurement should last
* The configuration is similar to `@Warmup` with `iterations`, `time`, & `timeUnit`

#### @Fork

* This controls how many JVM processes should be created to run each benchmark.
* This should be set to at least 1 in order to avoid benchmarks from affecting each other. A value at least 2 is even
  better as it provides more information about repeatability and margin of error.
* Example: Using `@Fork(2)` on a benchmark class with 3 benchmark methods will result in 6 benchmark runs, each in a
  separate JVM process.

#### @State

* Marks the class as storing data that the benchmarks will operate on and controls how this data is shared.
* `Scope.Benchmark` - The dataset will be shared across all threads (when configuring the benchmark to run concurrently
  on multiple threads).
