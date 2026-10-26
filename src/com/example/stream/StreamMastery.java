package com.example.stream;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// =========================================================================
// 1. Describing the Builder Pattern (Immutable Domain Object)
// =========================================================================
// The Builder Pattern decouples object construction from its representation,
// providing a fluent API for creating complex, immutable objects cleanly.
class Transaction {
    private final String id;
    private final String type;
    private final double amount;
    private final String status;

    private Transaction(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.amount = builder.amount;
        this.status = builder.status;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return String.format("Tx[id=%s, type=%s, amount=%.2f, status=%s]", id, type, amount, status);
    }

    public static class Builder {
        private String id;
        private String type;
        private double amount;
        private String status = "PENDING"; // Default value

        public Builder(String id) {
            this.id = id; // Required field
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}

public class StreamMastery {

    public static void main(String[] args) {

        // Constructing objects using the Builder pattern
        List<Transaction> transactions = List.of(
                new Transaction.Builder("TX101").type("CREDIT").amount(1200.00).status("COMPLETED").build(),
                new Transaction.Builder("TX102").type("DEBIT").amount(450.50).status("COMPLETED").build(),
                new Transaction.Builder("TX103").type("CREDIT").amount(80.00).status("FAILED").build(),
                new Transaction.Builder("TX104").type("DEBIT").amount(2100.00).status("COMPLETED").build(),
                new Transaction.Builder("TX105").type("CREDIT").amount(3500.00).status("COMPLETED").build()
        );


        // =========================================================================
        // 2. Iterating through a Collection using Lambda Syntax
        // =========================================================================
        System.out.println("--- 1. LAMBDA ITERATION ---");
        // Internal iteration using Iterable.forEach and a lambda expression
        transactions.forEach(tx -> System.out.println("Processing: " + tx.getId()));
        System.out.println();


        // =========================================================================
        // 3. Describing the Stream Interface & Defining Pipelines
        // =========================================================================
        // A Stream is a sequence of elements supporting sequential and parallel
        // aggregate operations. Streams do NOT store data; they convey data from
        // a source (e.g., Collection) through a pipeline.
        //
        // Pipeline Structure:
        // Source -> Intermediate Operations (Lazy) -> Terminal Operation (Eager)
        System.out.println("--- 2. STREAM PIPELINE: FILTERING & METHOD CHAINING ---");

        List<String> highValueCompletedIds = transactions.stream()
                // 4. Filtering using lambda expressions (Predicate)
                .filter(tx -> "COMPLETED".equals(tx.getStatus()))
                .filter(tx -> tx.getAmount() > 500.0)

                // 5. Chaining multiple methods together (Transform via Function)
                .map(Transaction::getId)

                // Terminal operation triggers execution and consumes the stream
                .collect(Collectors.toList());

        System.out.println("High Value Completed TX IDs: " + highValueCompletedIds);
        System.out.println();


        // =========================================================================
        // 6. Describing Reduction & Calculating Values using reduce()
        // =========================================================================
        // Reduction takes a sequence of input elements and combines them into a
        // single summary result (e.g., sum, min, max, count) by repeatedly applying
        // a combining operation.
        System.out.println("--- 3. REDUCTION MECHANICS ---");

        // 6a. Single-argument reduce (Returns Optional)
        Optional<Double> totalCreditAmount = transactions.stream()
                .filter(tx -> "CREDIT".equals(tx.getType()))
                .map(Transaction::getAmount)
                .reduce((a, b) -> a + b); // Lambda binary operator

        totalCreditAmount.ifPresent(sum -> System.out.println("Total Credit Sum: $" + sum));

        // 6b. Three-argument reduce (Identity, Accumulator, Combiner)
        // Useful when mapping and reducing in a single pass across different types
        Double totalVolume = transactions.stream()
                .reduce(
                        0.0,                                    // Identity element
                        (sum, tx) -> sum + tx.getAmount(),      // Accumulator (Double, Transaction) -> Double
                        Double::sum                             // Combiner (Double, Double) -> Double
                );

        System.out.println("Total Transaction Volume: $" + totalVolume);
        System.out.println();


        // =========================================================================
        // 7. Parallel Streams & Decomposing/Merging Work
        // =========================================================================
        // Parallel streams divide work across available processor cores using the
        // Fork/Join Framework (java.util.concurrent.ForkJoinPool.commonPool()).
        //
        // Process for Parallel Execution:
        // 1. Decomposition (Fork): The source Spliterator partitions the stream
        //    recursively into smaller chunks.
        // 2. Execution: Worker threads execute the pipeline on their chunks concurrently.
        // 3. Merging (Join): The reduction combiner merges thread results into a final value.
        System.out.println("--- 4. PARALLEL STREAMS (DECOMPOSE & MERGE) ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Making a stream execute in parallel using .parallel() or .parallelStream()
        int parallelSum = numbers.parallelStream()
                .peek(n -> System.out.printf("Thread [%s] processing number: %d\n",
                        Thread.currentThread().getName(), n))
                .reduce(
                        0,                   // Identity
                        Integer::sum,        // Accumulator
                        Integer::sum         // Combiner (Merges thread outputs)
                );

        System.out.println("Parallel Reduction Result: " + parallelSum);
        System.out.println();


        // =========================================================================
        // 8. Stream Builder Example
        // =========================================================================
        System.out.println("--- 5. STREAM BUILDER ---");
        // Building a stream on-the-fly without underlying collection storage
        Stream<String> dynamicStream = Stream.<String>builder()
                .add("Alpha")
                .add("Beta")
                .add("Gamma")
                .build();

        dynamicStream.forEach(System.out::println);
        //dynamicStream.forEach(s -> System.out.println(s));
    }
}