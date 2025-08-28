/**
 * Java 18-21+ Features Examples - Latest Java Features
 * 
 * Demonstrates cutting-edge Java features including Pattern Matching for Switch (Standard),
 * Virtual Threads, Structured Concurrency, Vector API, and Foreign Function & Memory API.
 */

import java.util.*;
import java.util.concurrent.*;
import java.time.*;
import java.util.stream.*;

public class LatestJavaExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Java 18-21+ Latest Features Examples ===");
        
        // BEGINNER LEVEL  
        demonstratePatternMatchingSwitch();
        showRecordPatterns();
        
        // INTERMEDIATE LEVEL
        exploreVirtualThreads();
        showStringEnhancements();
        
        // ADVANCED LEVEL
        demonstrateStructuredConcurrency();
        showcaseVectorAPI();
    }
    
    public static void demonstratePatternMatchingSwitch() {
        System.out.println("\n1. Pattern Matching for Switch (Standard)");
        System.out.println("==========================================");
        
        List<Object> data = List.of(
            "Java 21 LTS",
            42,
            List.of("virtual", "threads"),
            Map.of("feature", "pattern-matching"),
            new Person("Alice", 30),
            new Circle(5.0),
            Optional.of("Present")
        );
        
        System.out.println("Processing data with enhanced pattern matching:");
        data.forEach(LatestJavaExamples::processWithPatternMatching);
    }
    
    public static void processWithPatternMatching(Object obj) {
        String result = switch (obj) {
            case String s when s.contains("Java") -> 
                "Java version string: " + s + " (length: " + s.length() + ")";
            case String s -> 
                "Regular string: " + s;
            case Integer i when i > 50 -> 
                "Large integer: " + i;
            case Integer i -> 
                "Small integer: " + i + " (binary: " + Integer.toBinaryString(i) + ")";
            case List<?> list when list.size() > 2 -> 
                "Large list: " + list + " (size: " + list.size() + ")";
            case List<?> list -> 
                "Small list: " + list;
            case Map<?, ?> map -> 
                "Map data: " + map + " (keys: " + map.keySet() + ")";
            case Person(var name, var age) when age >= 18 -> 
                "Adult person: " + name + " (age: " + age + ")";
            case Person(var name, var age) -> 
                "Minor person: " + name + " (age: " + age + ")";
            case Circle(var radius) when radius > 10 -> 
                "Large circle with radius: " + radius;
            case Circle(var radius) -> 
                "Small circle with radius: " + radius;
            case Optional<?> opt when opt.isPresent() -> 
                "Present optional: " + opt.get();
            case Optional<?> opt -> 
                "Empty optional";
            case null -> 
                "Null value encountered";
            default -> 
                "Unknown type: " + obj.getClass().getSimpleName();
        };
        System.out.println(result);
    }
    
    public static void showRecordPatterns() {
        System.out.println("\n2. Record Patterns (Java 19+)");
        System.out.println("==============================");
        
        List<Shape> shapes = List.of(
            new Circle(7.5),
            new Rectangle(10, 5),
            new Triangle(new Point(0, 0), new Point(3, 0), new Point(0, 4))
        );
        
        System.out.println("Analyzing shapes with record patterns:");
        shapes.forEach(shape -> {
            String analysis = switch (shape) {
                case Circle(var radius) -> {
                    double area = Math.PI * radius * radius;
                    String size = radius < 5 ? "small" : radius < 10 ? "medium" : "large";
                    yield String.format("Circle: %s (radius=%.1f, area=%.2f)", size, radius, area);
                }
                case Rectangle(var width, var height) when width == height -> 
                    String.format("Square: side=%.1f, area=%.2f", width, width * height);
                case Rectangle(var width, var height) -> {
                    double area = width * height;
                    String orientation = width > height ? "landscape" : "portrait";
                    yield String.format("Rectangle: %s (%.1f×%.1f, area=%.2f)", 
                                       orientation, width, height, area);
                }
                case Triangle(Point(var x1, var y1), Point(var x2, var y2), Point(var x3, var y3)) -> {
                    double area = 0.5 * Math.abs((x1*(y2-y3) + x2*(y3-y1) + x3*(y1-y2)));
                    yield String.format("Triangle: vertices=(%d,%d)-(%d,%d)-(%d,%d), area=%.2f",
                                       x1, y1, x2, y2, x3, y3, area);
                }
            };
            System.out.println(analysis);
        });
        
        // Nested record patterns
        demonstrateNestedPatterns();
    }
    
    public static void demonstrateNestedPatterns() {
        System.out.println("\nNested record pattern matching:");
        
        List<Employee> employees = List.of(
            new Employee("Alice", new Address("123 Main St", new City("Boston", "MA"))),
            new Employee("Bob", new Address("456 Oak Ave", new City("Seattle", "WA"))),
            new Employee("Charlie", new Address("789 Pine St", new City("Austin", "TX")))
        );
        
        employees.forEach(emp -> {
            String info = switch (emp) {
                case Employee(var name, Address(var street, City(var cityName, "MA"))) ->
                    name + " works in Massachusetts (Boston area): " + street;
                case Employee(var name, Address(var street, City(var cityName, "WA"))) ->
                    name + " works in Washington (" + cityName + "): " + street;
                case Employee(var name, Address(var street, City(var cityName, var state))) ->
                    name + " works in " + cityName + ", " + state + ": " + street;
            };
            System.out.println(info);
        });
    }
    
    public static void exploreVirtualThreads() {
        System.out.println("\n3. Virtual Threads (Project Loom)");
        System.out.println("==================================");
        
        // Basic virtual thread creation
        demonstrateBasicVirtualThreads();
        
        // High-throughput server simulation
        demonstrateHighThroughputServer();
        
        // Virtual thread pools
        demonstrateVirtualThreadPools();
    }
    
    public static void demonstrateBasicVirtualThreads() {
        System.out.println("Creating and running virtual threads:");
        
        // Create virtual threads
        List<Thread> virtualThreads = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            Thread vthread = Thread.ofVirtual()
                                  .name("virtual-task-" + taskId)
                                  .start(() -> {
                                      System.out.println("Virtual thread " + taskId + 
                                                       " running on: " + Thread.currentThread());
                                      
                                      // Simulate some work
                                      try {
                                          Thread.sleep(Duration.ofMillis(100 + taskId * 50));
                                          System.out.println("Virtual thread " + taskId + " completed");
                                      } catch (InterruptedException e) {
                                          Thread.currentThread().interrupt();
                                      }
                                  });
            virtualThreads.add(vthread);
        }
        
        // Wait for all virtual threads to complete
        virtualThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        System.out.println("All virtual threads completed");
    }
    
    public static void demonstrateHighThroughputServer() {
        System.out.println("\nSimulating high-throughput server with virtual threads:");
        
        // Simulate handling many requests concurrently
        int requestCount = 1000;
        long startTime = System.currentTimeMillis();
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<String>> futures = new ArrayList<>();
            
            for (int i = 0; i < requestCount; i++) {
                final int requestId = i;
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    // Simulate request processing
                    try {
                        Thread.sleep(Duration.ofMillis(10)); // Simulate I/O wait
                        return "Request " + requestId + " processed by " + Thread.currentThread();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return "Request " + requestId + " interrupted";
                    }
                }, executor);
                
                futures.add(future);
            }
            
            // Wait for all requests to complete
            List<String> results = futures.stream()
                                         .map(CompletableFuture::join)
                                         .collect(Collectors.toList());
            
            long endTime = System.currentTimeMillis();
            System.out.printf("Processed %d requests in %d ms using virtual threads%n", 
                            requestCount, endTime - startTime);
            System.out.println("First few results: " + results.subList(0, 3));
        }
    }
    
    public static void demonstrateVirtualThreadPools() {
        System.out.println("\nVirtual thread executor services:");
        
        // Compare virtual threads vs platform threads
        compareThreadPerformance();
    }
    
    public static void compareThreadPerformance() {
        int taskCount = 100;
        
        // Virtual threads
        long startTime = System.currentTimeMillis();
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<Void>> virtualFutures = IntStream.range(0, taskCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(Duration.ofMillis(50));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }, virtualExecutor))
                .toList();
            
            virtualFutures.forEach(CompletableFuture::join);
        }
        long virtualTime = System.currentTimeMillis() - startTime;
        
        // Platform threads
        startTime = System.currentTimeMillis();
        try (ExecutorService platformExecutor = Executors.newFixedThreadPool(10)) {
            List<CompletableFuture<Void>> platformFutures = IntStream.range(0, taskCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(Duration.ofMillis(50));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }, platformExecutor))
                .toList();
            
            platformFutures.forEach(CompletableFuture::join);
        }
        long platformTime = System.currentTimeMillis() - startTime;
        
        System.out.printf("Virtual threads: %d ms%n", virtualTime);
        System.out.printf("Platform threads: %d ms%n", platformTime);
        System.out.printf("Performance ratio: %.2fx%n", (double) platformTime / virtualTime);
    }
    
    public static void showStringEnhancements() {
        System.out.println("\n4. String Enhancements (Java 18-21)");
        System.out.println("====================================");
        
        // String templates (Preview in Java 21)
        demonstrateStringTemplates();
        
        // Enhanced string methods
        demonstrateStringMethods();
    }
    
    public static void demonstrateStringTemplates() {
        System.out.println("String template examples:");
        
        String name = "Java";
        int version = 21;
        double performance = 1.5;
        
        // Traditional string formatting
        String traditional = String.format("Welcome to %s %d with %.1fx performance improvement", 
                                         name, version, performance);
        System.out.println("Traditional: " + traditional);
        
        // String templates (conceptual - preview feature)
        String template = "Welcome to " + name + " " + version + 
                         " with " + performance + "x performance improvement";
        System.out.println("Template: " + template);
        
        // Multi-line string with variables
        String report = String.format("""
                Performance Report
                ==================
                Language: %s
                Version: %d
                Improvement: %.1fx
                Status: %s
                """, name, version, performance, 
                performance > 1.0 ? "Enhanced" : "Standard");
        
        System.out.println("Report:");
        System.out.println(report);
    }
    
    public static void demonstrateStringMethods() {
        System.out.println("\nEnhanced string processing:");
        
        List<String> data = List.of(
            "  Java Programming  ",
            "virtual-threads-feature",
            "pattern_matching_switch",
            "STRUCTURED_CONCURRENCY"
        );
        
        System.out.println("Processing strings:");
        data.forEach(str -> {
            String processed = str.strip()                    // Remove whitespace
                                 .transform(s -> s.toLowerCase()) // Transform to lowercase
                                 .replace("-", " ")             // Replace hyphens
                                 .replace("_", " ");            // Replace underscores
            
            String[] words = processed.split(" ");
            String capitalized = Arrays.stream(words)
                                      .map(word -> word.substring(0, 1).toUpperCase() + 
                                                   word.substring(1))
                                      .collect(Collectors.joining(" "));
            
            System.out.printf("'%s' -> '%s'%n", str, capitalized);
        });
    }
    
    public static void demonstrateStructuredConcurrency() {
        System.out.println("\n5. Structured Concurrency (Preview)");
        System.out.println("====================================");
        
        // Simulate structured concurrency patterns
        simulateStructuredTasks();
        
        // Error handling in concurrent tasks
        demonstrateErrorHandling();
    }
    
    public static void simulateStructuredTasks() {
        System.out.println("Structured concurrency simulation:");
        
        // Simulate a task that requires multiple sub-tasks
        CompletableFuture<String> userDataFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(Duration.ofMillis(100));
                return "User: John Doe";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "User data failed";
            }
        });
        
        CompletableFuture<String> orderDataFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(Duration.ofMillis(150));
                return "Orders: 5 items";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Order data failed";
            }
        });
        
        CompletableFuture<String> preferenceFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(Duration.ofMillis(80));
                return "Preferences: Dark mode, Email notifications";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Preference data failed";
            }
        });
        
        // Combine all results
        CompletableFuture<String> combinedResult = CompletableFuture.allOf(
            userDataFuture, orderDataFuture, preferenceFuture
        ).thenApply(v -> {
            return String.format("Dashboard Data:%n- %s%n- %s%n- %s", 
                               userDataFuture.join(),
                               orderDataFuture.join(), 
                               preferenceFuture.join());
        });
        
        System.out.println(combinedResult.join());
    }
    
    public static void demonstrateErrorHandling() {
        System.out.println("\nStructured error handling:");
        
        List<CompletableFuture<String>> tasks = List.of(
            CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(50);
                    return "Task 1: Success";
                } catch (InterruptedException e) {
                    throw new RuntimeException("Task 1 interrupted");
                }
            }),
            CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(100);
                    throw new RuntimeException("Task 2: Simulated failure");
                } catch (InterruptedException e) {
                    throw new RuntimeException("Task 2 interrupted");
                }
            }),
            CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(75);
                    return "Task 3: Success";
                } catch (InterruptedException e) {
                    throw new RuntimeException("Task 3 interrupted");
                }
            })
        );
        
        // Handle partial failures
        List<String> results = tasks.stream()
            .map(future -> {
                try {
                    return future.join();
                } catch (Exception e) {
                    return "Failed: " + e.getMessage();
                }
            })
            .collect(Collectors.toList());
        
        System.out.println("Task results:");
        results.forEach(System.out::println);
    }
    
    public static void showcaseVectorAPI() {
        System.out.println("\n6. Vector API (Incubator)");
        System.out.println("==========================");
        
        // Simulate vector operations (conceptual implementation)
        demonstrateVectorizedOperations();
        
        // Performance comparison
        compareVectorPerformance();
    }
    
    public static void demonstrateVectorizedOperations() {
        System.out.println("Vectorized mathematical operations:");
        
        // Simulate SIMD operations
        int[] array1 = IntStream.range(1, 11).toArray();
        int[] array2 = IntStream.range(11, 21).toArray();
        
        System.out.println("Array 1: " + Arrays.toString(array1));
        System.out.println("Array 2: " + Arrays.toString(array2));
        
        // Vector addition simulation
        int[] result = new int[array1.length];
        for (int i = 0; i < array1.length; i++) {
            result[i] = array1[i] + array2[i];
        }
        System.out.println("Vector Addition: " + Arrays.toString(result));
        
        // Vector multiplication simulation
        int[] product = new int[array1.length];
        for (int i = 0; i < array1.length; i++) {
            product[i] = array1[i] * array2[i];
        }
        System.out.println("Vector Multiplication: " + Arrays.toString(product));
        
        // Dot product
        int dotProduct = IntStream.range(0, array1.length)
                                  .map(i -> array1[i] * array2[i])
                                  .sum();
        System.out.println("Dot Product: " + dotProduct);
    }
    
    public static void compareVectorPerformance() {
        System.out.println("\nVector operation performance simulation:");
        
        int size = 1_000_000;
        float[] a = new float[size];
        float[] b = new float[size];
        float[] result = new float[size];
        
        // Initialize arrays
        for (int i = 0; i < size; i++) {
            a[i] = i * 0.5f;
            b[i] = (size - i) * 0.3f;
        }
        
        // Scalar operation
        long startTime = System.nanoTime();
        for (int i = 0; i < size; i++) {
            result[i] = a[i] + b[i] * 2.0f;
        }
        long scalarTime = System.nanoTime() - startTime;
        
        // Simulated vector operation (would be much faster with actual Vector API)
        startTime = System.nanoTime();
        for (int i = 0; i < size; i += 8) { // Simulate 8-wide SIMD
            for (int j = 0; j < 8 && i + j < size; j++) {
                result[i + j] = a[i + j] + b[i + j] * 2.0f;
            }
        }
        long vectorTime = System.nanoTime() - startTime;
        
        System.out.printf("Scalar operation: %.2f ms%n", scalarTime / 1_000_000.0);
        System.out.printf("Vectorized operation: %.2f ms%n", vectorTime / 1_000_000.0);
        System.out.printf("Speedup ratio: %.2fx%n", (double) scalarTime / vectorTime);
        
        // Show sample results
        System.out.printf("Sample results: [%.2f, %.2f, %.2f, ...]%n", 
                         result[0], result[1], result[2]);
    }
    
    // Record definitions for examples
    public record Person(String name, int age) {}
    
    public sealed interface Shape permits Circle, Rectangle, Triangle {}
    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}
    public record Triangle(Point p1, Point p2, Point p3) implements Shape {}
    
    public record Point(int x, int y) {}
    
    public record Employee(String name, Address address) {}
    public record Address(String street, City city) {}
    public record City(String name, String state) {}
    
    // Performance monitoring utilities
    public static void measurePerformance(String operation, Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        System.out.printf("%s completed in %.2f ms%n", 
                         operation, (endTime - startTime) / 1_000_000.0);
    }
    
    // Utility for generating test data
    public static List<Integer> generateTestData(int size) {
        return IntStream.range(0, size)
                       .boxed()
                       .collect(Collectors.toList());
    }
}