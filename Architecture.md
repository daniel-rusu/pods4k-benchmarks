# Benchmark Architecture

## 1. Overview

### Purpose and Scope

### Architecture at a Glance

## 2. Benchmark Model

### Collection Representations

### Data Types

### Parameter Matrix

### Work per Invocation

## 3. Benchmark Categories

### Flat Collections

### Nullable Flat Collections

### Object Collections

### Nested Collections

*Parent/child scenarios with separate size models for the outer and nested collections.*

## 4. Code Organization

### Benchmark Definitions (`src/jmh`)

#### Benchmark Classes

#### Base States and Parameter Dispatch

### Benchmark Data (`src/main/.../immutableArrays`)

#### Benchmark-Family Data Builders

#### Collection Construction and Storage

### Data-Generation Utilities (`src/main/.../utils`)

#### Distributions

#### Generators and Random Sources

### Tests (`src/test`)

### Build Configuration

## 5. Benchmark Lifecycle

### JMH Parameter Selection

### Trial Setup

### Data Generation and Materialization

### Typed Operation Dispatch

### Measurement and Result Consumption

## 6. Data Construction

### Deterministic Random Streams

### Collection Sizes and Element Values

### Collection Factory

### Collection Batch

*Shared storage for the active parameter combination, with typed access provided by each benchmark family.*

### Benchmark-Family Data Builders

## 7. Measurement Invariants

### Setup Versus Measured Work

### Equivalent Operations Across Representations

### Comparable Input Data

### Operations per Invocation

### Public API Boundary
