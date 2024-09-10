# pods4k-benchmarks

This repo contains benchmarks for the [pods4k library](https://github.com/daniel-rusu/pods4k). The benchmarks are in
this separate repo to resemble real environments where the library is added as a regular dependency without access to
any library internals.

The benchmarks use the [Java Microbenchmark Harness](https://github.com/openjdk/jmh), JMH, along with
the [JMH Gradle Plugin](https://github.com/melix/jmh-gradle-plugin). JMH helps us avoid JVM benchmarking pitfalls
producing more realistic results.
