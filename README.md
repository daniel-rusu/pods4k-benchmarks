# pods4k-benchmarks

This repo contains benchmarks for the [pods4k library](https://github.com/daniel-rusu/pods4k). The benchmarks are in
this separate repo to resemble real environments where the library is added as a regular dependency without access to
any library internals.

The benchmarks use the [Java Microbenchmark Harness](https://github.com/openjdk/jmh), JMH, along with
the [JMH Gradle Plugin](https://github.com/melix/jmh-gradle-plugin). JMH helps us avoid JVM benchmarking pitfalls
and produce more realistic results.

## Running Benchmarks

1. Clone this repo
    * `git clone git@github.com:daniel-rusu/pods4k-benchmarks.git`
2. Ensure you have an appropriate JDK installed (from JDK 11 to JDK 23).
    * The [pods4k library](https://github.com/daniel-rusu/pods4k) runs on all JDK versions from 11 onwards.
    * This benchmarking repo is configured with Gradle 8.10 which supports up to JDK 23.
    * The benchmark results will vary depending on your selected JDK. Choose the version that's closest to what you
      expect to run in production in order to get the most representative results.
3. Choose the benchmarks to be run
    * The benchmark classes are located in `/src/jmh/kotlin/...` (not `/src/main/kotlin`).
    * Configure the jmh `includes` list in [build.gradle.kts](build.gradle.kts) to specify which benchmarks to run

   ```kotlin
   jmh {
       jmhVersion = libs.versions.jmh
   
       includes = listOf("immutableArrays.nestedCollectionBenchmarks.FlatMapBenchmarks")
   }
   ```
4. Prepare your machine
    * If using a laptop, make sure it's plugged in and the power profile set to performance.
    * Change your computer screen & sleep timeouts to `Never`.
    * Disable any screen saver.
    * Temporarily pause Windows updates.
    * Turn off unnecessary startup programs and services and restart your computer.
    * Close all applications and stop any unnecessary processes to minimize interference.
5. Compile and execute benchmarks
    * Using your Linux shell (or `Git Bash` on Windows), navigate to the directory where this repo is cloned
    * Run `./gradlew jmh`
6. Wait for results
    * A benchmark class can easily last 20 minutes even though each iteration might only be configured to last 1 second.
    * A class with 3 benchmarks, parameterized with 8 data types, configured with 10 warmup iterations, 5 benchmark
      iterations, and 3 JVM forks takes 3 * 8 * (10 + 5) * 3 * (1 second/iteration) = 1080 seconds = 18 minutes!
7. Analyze results
    * The results will be saved in [/build/results/jmh/results.csv](./build/results/jmh/results.csv)
    * Check the relative error of each benchmark. A good (or bad) result with a high relative error might be misleading.
    * I recommend re-running the benchmark if any relative error is greater than 5% of the value as you might have been
      unlucky with some sporadic interference from some other process (see step 4 above).

## Benchmark Configuration

The benchmarks are configured with the following JMH annotations:

| Annotation                 | Explanation                                                                                                                                                                                                                                                                                                                                                                                                             |
|:---------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `@Setup`                   | Marks the setup method that prepares the benchmarking dataset(s).<br/> - `Level.Trial`: Only perform the setup once for each benchmark.                                                                                                                                                                                                                                                                                 |
| `@Benchmark`               | Marks the method that contains the operation that we want to benchmark.                                                                                                                                                                                                                                                                                                                                                 |
| `@Param`                   | Parameterizes a variable with multiple values and repeats all the benchmarks for each value.                                                                                                                                                                                                                                                                                                                            |
| `@BenchmarkMode`           | Specifies what we're trying to measure. <br/> - `Mode.Throughput`: Measures the number of operations per time unit.                                                                                                                                                                                                                                                                                                     |
| `@OutputTimeUnit`          | Specifies the time unit to report the results in <br/> - `TimeUnit.SECONDS`: Report the results per second. <br/> - `TimeUnit.MILLISECONDS`: Report the results per millisecond.                                                                                                                                                                                                                                        |
| `@OperationsPerInvocation` | Specifies how many times the operation is being performed in 1 execution of the benchmark method. <br/> - This is used to avoid local runtime optimizations that produce misleadingly-fast results if we perform an operation on a small dataset repeatedly.<br/> - We create hundreds of randomized datasets and in a single benchmark invocation, loop through each dataset and perform the operation being measured. |
| `@Warmup`                  | Controls warmup behavior so the performance reaches a steady state before the benchmark begins.<br/> - `iterations`: The # of warmup iterations that should be performed. <br/> - `time`: The # of time units that each warmup iteration should last. <br/> - `timeUnit`: The time unit (eg. seconds, milliseconds, etc.)                                                                                               |
| `@Measurement`             | Controls how many measurements to take and how long each measurement should last.<br/> - The configuration is similar to `@Warmup` except that it controls the actual benchmark measurements.                                                                                                                                                                                                                           |
| `@Fork`                    | Controls how many JVM processes should be created to run each benchmark. <br/> - This should be set to at least 1 in order to avoid benchmarks from affecting each other. A value at least 2 is even better as it provides more information about repeatability and margin of error.                                                                                                                                    |
| `@State`                   | Marks the class as storing data that the benchmarks will operate on and controls how this data is shared. <br/> - `Scope.Benchmark` - The dataset will be shared across all threads.                                                                                                                                                                                                                                    |
