# Module 14.10: Java 18-21+ Features (Latest Innovations)

## 🎯 **Overview**

This module covers the cutting-edge features introduced in Java 18 through Java 21+, including the revolutionary **Virtual Threads**, **Pattern Matching for switch**, **Sequenced Collections**, and other modern innovations that are shaping the future of Java development.

---

## 🚀 **Java Version Timeline**

### **Java 18 (March 2022)**
- **Simple Web Server**: Built-in HTTP server for development
- **UTF-8 by Default**: Default charset across platforms
- **Code Snippets in JavaDoc**: Enhanced documentation
- **Vector API (Second Incubator)**: SIMD operations

### **Java 19 (September 2022)**
- **Virtual Threads (Preview)**: Lightweight concurrency
- **Pattern Matching for switch (Preview)**: Advanced pattern matching
- **Structured Concurrency (Incubator)**: Organized concurrent programming
- **Foreign Function & Memory API (Preview)**: Native integration

### **Java 20 (March 2023)**
- **Scoped Values (Incubator)**: Alternative to ThreadLocal
- **Record Patterns (Preview)**: Destructuring record values
- **Pattern Matching for switch (Second Preview)**: Enhanced patterns
- **Virtual Threads (Second Preview)**: Continued improvement

### **Java 21 LTS (September 2023)**
- **Virtual Threads**: Production-ready lightweight threads
- **Pattern Matching for switch**: Standardized advanced matching
- **Sequenced Collections**: Order-aware collection interfaces
- **Record Patterns**: Destructuring in pattern matching
- **String Templates (Preview)**: Safe string interpolation

---

## 📚 **Detailed Feature Guide**

### **1. Virtual Threads (Java 21 Standard)**

Revolutionary lightweight threads that enable millions of concurrent operations with minimal overhead.

#### **Basic Virtual Thread Usage**
```java
public class VirtualThreadExamples {
    
    public void basicVirtualThreads() throws InterruptedException {
        // Create and start virtual threads
        Thread virtualThread = Thread.ofVirtual().start(() -> {
            System.out.println("Running in virtual thread: " + Thread.currentThread());
        });
        
        virtualThread.join();
        
        // Factory for virtual threads
        ThreadFactory factory = Thread.ofVirtual().factory();
        Thread vt1 = factory.newThread(() -> processTask("Task 1"));
        Thread vt2 = factory.newThread(() -> processTask("Task 2"));
        
        vt1.start();
        vt2.start();
        vt1.join();
        vt2.join();
    }
    
    // Executor with virtual threads
    public void virtualThreadExecutor() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Submit thousands of tasks efficiently
            List<Future<String>> futures = new ArrayList<>();
            
            for (int i = 0; i < 10000; i++) {
                final int taskId = i;
                Future<String> future = executor.submit(() -> {
                    // Simulate I/O-bound work
                    Thread.sleep(Duration.ofMillis(100));
                    return "Task " + taskId + " completed on " + Thread.currentThread();
                });
                futures.add(future);
            }
            
            // Collect results
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // HTTP client with virtual threads
    public void httpClientWithVirtualThreads() {
        HttpClient client = HttpClient.newBuilder()
            .executor(Executors.newVirtualThreadPerTaskExecutor())
            .build();
        
        List<String> urls = List.of(
            "https://api.github.com/users/octocat",
            "https://jsonplaceholder.typicode.com/posts/1",
            "https://httpbin.org/json"
        );
        
        List<CompletableFuture<String>> futures = urls.stream()
            .map(url -> makeAsyncRequest(client, url))
            .toList();
        
        // Wait for all requests to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> {
                futures.forEach(future -> {
                    try {
                        System.out.println("Response length: " + future.get().length());
                    } catch (Exception e) {
                        System.err.println("Request failed: " + e.getMessage());
                    }
                });
            });
    }
    
    private CompletableFuture<String> makeAsyncRequest(HttpClient client, String url) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(10))
            .build();
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body);
    }
}
```

#### **Structured Concurrency (Incubator)**
```java
import java.util.concurrent.StructuredTaskScope;

public class StructuredConcurrencyExamples {
    
    // Structured concurrent execution
    public UserData fetchUserData(String userId) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Submit concurrent tasks
            var profileTask = scope.fork(() -> fetchUserProfile(userId));
            var preferencesTask = scope.fork(() -> fetchUserPreferences(userId));
            var historyTask = scope.fork(() -> fetchUserHistory(userId));
            
            // Wait for all tasks to complete
            scope.join();           // Wait for all tasks
            scope.throwIfFailed();  // Propagate any exceptions
            
            // Combine results
            return new UserData(
                profileTask.resultNow(),
                preferencesTask.resultNow(),
                historyTask.resultNow()
            );
        }
    }
    
    // Timeout handling with structured concurrency
    public SearchResults performSearch(String query) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var webResults = scope.fork(() -> searchWeb(query));
            var imageResults = scope.fork(() -> searchImages(query));
            var newsResults = scope.fork(() -> searchNews(query));
            
            // Set a timeout for all operations
            scope.join(Duration.ofSeconds(5));
            scope.throwIfFailed();
            
            return new SearchResults(
                webResults.resultNow(),
                imageResults.resultNow(),
                newsResults.resultNow()
            );
        }
    }
}
```

### **2. Pattern Matching for switch (Java 21 Standard)**

Advanced pattern matching that enables sophisticated data extraction and processing.

```java
public class AdvancedPatternMatching {
    
    // Record patterns for destructuring
    public String formatPerson(Object obj) {
        return switch (obj) {
            case Person(var name, var age) when age < 18 -> 
                "Minor: " + name + " (age " + age + ")";
            case Person(var name, var age) when age >= 65 -> 
                "Senior: " + name + " (age " + age + ")";
            case Person(var name, var age) -> 
                "Adult: " + name + " (age " + age + ")";
            case null -> "No person data";
            default -> "Unknown type: " + obj.getClass().getSimpleName();
        };
    }
    
    // Complex nested pattern matching
    public double calculateShippingCost(Order order) {
        return switch (order) {
            case Order(var items, var address) when items.isEmpty() -> 0.0;
            
            case Order(var items, Address(_, _, var country)) when "US".equals(country) -> {
                double weight = items.stream().mapToDouble(Item::weight).sum();
                yield weight < 1.0 ? 5.99 : weight * 2.50;
            }
            
            case Order(var items, Address(_, _, var country)) when "CA".equals(country) -> {
                double weight = items.stream().mapToDouble(Item::weight).sum();
                yield weight < 1.0 ? 7.99 : weight * 3.25;
            }
            
            case Order(var items, Address(_, _, var country)) -> {
                double weight = items.stream().mapToDouble(Item::weight).sum();
                yield 15.00 + weight * 5.00; // International shipping
            }
        };
    }
    
    // Pattern matching with collections
    public String processData(Object data) {
        return switch (data) {
            case String s when s.isEmpty() -> "Empty string";
            case String s when s.length() < 10 -> "Short string: " + s;
            case String s -> "Long string (" + s.length() + " chars)";
            
            case List<?> list when list.isEmpty() -> "Empty list";
            case List<?> list when list.size() == 1 -> "Single item: " + list.get(0);
            case List<?> list -> "List with " + list.size() + " items";
            
            case Map<?, ?> map when map.isEmpty() -> "Empty map";
            case Map<?, ?> map -> "Map with " + map.size() + " entries";
            
            case null -> "Null value";
            default -> "Unknown type: " + data.getClass().getName();
        };
    }
    
    // Pattern matching for validation
    public ValidationResult validateInput(Object input) {
        return switch (input) {
            case String s when s.matches("\\d{3}-\\d{3}-\\d{4}") -> 
                ValidationResult.valid("Valid phone number");
            
            case String s when s.matches("\\S+@\\S+\\.\\S+") -> 
                ValidationResult.valid("Valid email");
            
            case Integer i when i >= 0 && i <= 150 -> 
                ValidationResult.valid("Valid age");
            
            case String s when s.length() < 3 -> 
                ValidationResult.invalid("String too short");
            
            case null -> ValidationResult.invalid("Null input");
            
            default -> ValidationResult.invalid("Unrecognized format");
        };
    }
}

// Supporting records for examples
record Person(String name, int age) {}
record Address(String street, String city, String country) {}
record Item(String name, double weight, double price) {}
record Order(List<Item> items, Address address) {}

sealed interface ValidationResult permits ValidationResult.Valid, ValidationResult.Invalid {
    record Valid(String message) implements ValidationResult {}
    record Invalid(String reason) implements ValidationResult {}
    
    static ValidationResult valid(String message) { return new Valid(message); }
    static ValidationResult invalid(String reason) { return new Invalid(reason); }
}
```

### **3. Sequenced Collections (Java 21)**

New collection interfaces that provide well-defined encounter order operations.

```java
public class SequencedCollectionsExamples {
    
    public void demonstrateSequencedList() {
        // SequencedList provides first/last operations
        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));
        
        // Access first and last elements
        String first = names.getFirst();  // "Alice"
        String last = names.getLast();    // "Charlie"
        
        System.out.println("First: " + first + ", Last: " + last);
        
        // Add at beginning and end
        names.addFirst("Zoe");
        names.addLast("David");
        
        System.out.println("After additions: " + names);
        // Output: [Zoe, Alice, Bob, Charlie, David]
        
        // Remove from beginning and end
        String removedFirst = names.removeFirst();  // "Zoe"
        String removedLast = names.removeLast();    // "David"
        
        System.out.println("Removed first: " + removedFirst + ", last: " + removedLast);
        System.out.println("Final list: " + names);
    }
    
    public void demonstrateSequencedSet() {
        // LinkedHashSet implements SequencedSet
        SequencedSet<Integer> numbers = new LinkedHashSet<>();
        numbers.addAll(List.of(1, 3, 5, 7, 9));
        
        System.out.println("Original set: " + numbers);
        
        // Access first and last
        Integer first = numbers.getFirst();  // 1
        Integer last = numbers.getLast();    // 9
        
        System.out.println("First: " + first + ", Last: " + last);
        
        // Reverse view
        SequencedSet<Integer> reversed = numbers.reversed();
        System.out.println("Reversed view: " + reversed);
        // Output: [9, 7, 5, 3, 1]
        
        // Modifications through reversed view affect original
        reversed.addFirst(11);  // Adds to end of original
        System.out.println("After adding 11 to reversed: " + numbers);
    }
    
    public void demonstrateSequencedMap() {
        // LinkedHashMap implements SequencedMap
        SequencedMap<String, Integer> scores = new LinkedHashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);
        
        System.out.println("Original map: " + scores);
        
        // Access first and last entries
        Map.Entry<String, Integer> firstEntry = scores.firstEntry();
        Map.Entry<String, Integer> lastEntry = scores.lastEntry();
        
        System.out.println("First entry: " + firstEntry);
        System.out.println("Last entry: " + lastEntry);
        
        // Put at beginning and end
        scores.putFirst("Zoe", 98);
        scores.putLast("David", 85);
        
        System.out.println("After additions: " + scores);
        
        // Reverse view operations
        SequencedMap<String, Integer> reversed = scores.reversed();
        System.out.println("Reversed view: " + reversed);
        
        // Work with sequenced key and value collections
        SequencedSet<String> keys = scores.sequencedKeySet();
        SequencedCollection<Integer> values = scores.sequencedValues();
        
        System.out.println("Keys in order: " + keys);
        System.out.println("Values in order: " + values);
    }
    
    // Practical usage: LRU Cache implementation
    public class LRUCache<K, V> {
        private final int maxSize;
        private final SequencedMap<K, V> cache = new LinkedHashMap<>();
        
        public LRUCache(int maxSize) {
            this.maxSize = maxSize;
        }
        
        public V get(K key) {
            V value = cache.remove(key);
            if (value != null) {
                cache.putLast(key, value); // Move to end (most recent)
            }
            return value;
        }
        
        public void put(K key, V value) {
            cache.remove(key); // Remove if exists
            cache.putLast(key, value); // Add at end
            
            if (cache.size() > maxSize) {
                cache.pollFirstEntry(); // Remove least recently used
            }
        }
        
        public boolean containsKey(K key) {
            return cache.containsKey(key);
        }
        
        public int size() {
            return cache.size();
        }
    }
}
```

### **4. Simple Web Server (Java 18)**

Built-in HTTP server for development and testing purposes.

```java
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class SimpleWebServerExamples {
    
    public void startSimpleServer() throws IOException {
        // Create server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Add handlers for different paths
        server.createContext("/", this::handleRoot);
        server.createContext("/api/users", this::handleUsers);
        server.createContext("/api/health", this::handleHealth);
        
        // Set executor (null = default executor)
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        
        // Start server
        server.start();
        System.out.println("Server started at http://localhost:8080/");
    }
    
    private void handleRoot(HttpExchange exchange) throws IOException {
        String response = """
            <html>
            <body>
                <h1>Simple Java Web Server</h1>
                <p>Java 18+ built-in server</p>
                <ul>
                    <li><a href="/api/health">Health Check</a></li>
                    <li><a href="/api/users">Users API</a></li>
                </ul>
            </body>
            </html>
            """;
        
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, response.length());
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
    
    private void handleUsers(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response;
        
        switch (method) {
            case "GET" -> {
                response = """
                    [
                        {"id": 1, "name": "Alice", "email": "alice@example.com"},
                        {"id": 2, "name": "Bob", "email": "bob@example.com"}
                    ]
                    """;
                exchange.getResponseHeaders().set("Content-Type", "application/json");
            }
            case "POST" -> {
                // Read request body
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                response = """
                    {"status": "created", "data": %s}
                    """.formatted(requestBody);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
            }
            default -> {
                response = "Method not allowed";
                exchange.sendResponseHeaders(405, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                return;
            }
        }
        
        exchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
    
    private void handleHealth(HttpExchange exchange) throws IOException {
        String response = """
            {
                "status": "healthy",
                "timestamp": "%s",
                "version": "Java %s"
            }
            """.formatted(
                Instant.now().toString(),
                System.getProperty("java.version")
            );
        
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
```

---

## 🛠️ **Practical Applications**

### **High-Performance Web Service**
```java
public class ModernWebService {
    private final HttpClient httpClient;
    private final LRUCache<String, String> cache;
    
    public ModernWebService() {
        this.httpClient = HttpClient.newBuilder()
            .executor(Executors.newVirtualThreadPerTaskExecutor())
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        this.cache = new LRUCache<>(1000);
    }
    
    public CompletableFuture<ApiResponse> processRequest(ApiRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            // Pattern matching for request processing
            return switch (request) {
                case GetUserRequest(var userId) -> handleGetUser(userId);
                case CreateUserRequest(var userData) -> handleCreateUser(userData);
                case UpdateUserRequest(var userId, var updates) -> handleUpdateUser(userId, updates);
                case DeleteUserRequest(var userId) -> handleDeleteUser(userId);
            };
        }, Executors.newVirtualThreadPerTaskExecutor());
    }
    
    private ApiResponse handleGetUser(String userId) {
        // Check cache first
        String cached = cache.get(userId);
        if (cached != null) {
            return ApiResponse.success(cached);
        }
        
        // Fetch from database using virtual threads
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var userTask = scope.fork(() -> fetchUserFromDB(userId));
            var prefsTask = scope.fork(() -> fetchUserPreferences(userId));
            
            scope.join(Duration.ofSeconds(5));
            scope.throwIfFailed();
            
            var userData = combineUserData(userTask.resultNow(), prefsTask.resultNow());
            cache.put(userId, userData);
            
            return ApiResponse.success(userData);
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch user: " + e.getMessage());
        }
    }
}
```

---

## ⚡ **Performance Benefits**

### **Virtual Threads Performance**
- **Scalability**: Handle millions of concurrent requests
- **Resource Efficiency**: Minimal memory overhead per thread
- **Simplified Programming**: Write synchronous-looking async code
- **Better Throughput**: Ideal for I/O-bound applications

### **Pattern Matching Benefits**
- **Reduced Boilerplate**: Eliminate explicit casting and null checks
- **Type Safety**: Compiler-enforced exhaustiveness checking
- **Readability**: More expressive data processing logic
- **Performance**: Optimized switch implementations

---

## 🎯 **What You'll Learn**

After completing this module, you'll understand:
- **Virtual Threads**: Building highly scalable concurrent applications
- **Advanced Pattern Matching**: Sophisticated data processing patterns
- **Sequenced Collections**: Working with ordered collection operations
- **Modern Web Development**: Using built-in server capabilities
- **Cutting-Edge Features**: Latest Java innovations and their practical applications

---

## 📚 **Prerequisites**

- **Java 17 LTS Features**: Sealed classes, records, basic pattern matching
- **Concurrency**: Understanding of threads and concurrent programming
- **HTTP/Web Services**: Basic knowledge of web service development
- **Collections Framework**: Familiarity with Java collections

---

**🚀 Ready to build the next generation of Java applications? Let's explore the cutting-edge features that define modern Java development!**

**Previous:** [Java 17 Features](../14.9-Java17/README.md) | **Back to:** [Java Versions Overview](../README.md)