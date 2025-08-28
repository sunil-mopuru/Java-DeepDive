# Modern Java Features Interview Questions (10+ Years Experience)

## ☕ **Java 8-21+ Advanced Features**

### **Question 1: Stream API Mastery and Performance**
**Scenario**: Optimize this data processing pipeline for better performance and explain the trade-offs.

**Expected Answer**:
```java
// Inefficient version
public List<String> processOrdersInefficient(List<Order> orders) {
    return orders.stream()
        .filter(order -> order.getAmount() > 100)
        .map(order -> customerService.getCustomer(order.getCustomerId())) // N+1 problem!
        .filter(customer -> customer.isPremium())
        .map(Customer::getName)
        .map(String::toUpperCase)
        .sorted()
        .collect(Collectors.toList());
}

// Optimized version
public List<String> processOrdersOptimized(List<Order> orders) {
    // Pre-fetch customers to avoid N+1 problem
    Set<String> customerIds = orders.stream()
        .filter(order -> order.getAmount() > 100)
        .map(Order::getCustomerId)
        .collect(Collectors.toSet());
    
    Map<String, Customer> customerMap = customerService.getCustomersBatch(customerIds);
    
    return orders.parallelStream() // Use parallel for large datasets
        .filter(order -> order.getAmount() > 100)
        .map(Order::getCustomerId)
        .map(customerMap::get)
        .filter(Objects::nonNull)
        .filter(Customer::isPremium)
        .map(Customer::getName)
        .map(String::toUpperCase)
        .sorted()
        .collect(Collectors.toList());
}

// Custom collector for complex aggregations
public Map<CustomerTier, OrderStatistics> analyzeOrdersByTier(List<Order> orders) {
    return orders.stream()
        .collect(Collectors.groupingBy(
            this::determineCustomerTier,
            Collector.of(
                OrderStatistics::new,
                OrderStatistics::accumulate,
                OrderStatistics::combine,
                Collector.Characteristics.UNORDERED
            )
        ));
}

// Advanced: Parallel stream with custom fork-join pool
public List<ProcessedOrder> processOrdersWithCustomPool(List<Order> orders) {
    ForkJoinPool customPool = new ForkJoinPool(8); // Custom thread pool size
    
    try {
        return customPool.submit(() -> 
            orders.parallelStream()
                .map(this::processOrder)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        ).get();
    } catch (InterruptedException | ExecutionException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Processing failed", e);
    } finally {
        customPool.shutdown();
    }
}

// Advanced: Stream with stateful processing
public class StatefulOrderProcessor {
    private final AtomicInteger processedCount = new AtomicInteger(0);
    private final AtomicLong totalAmount = new AtomicLong(0);
    
    public List<ProcessedOrder> processOrders(List<Order> orders) {
        return orders.stream()
            .peek(order -> processedCount.incrementAndGet()) // Stateful operation
            .peek(order -> totalAmount.addAndGet(order.getAmount()))
            .map(this::processOrder)
            .collect(Collectors.toList());
    }
    
    public ProcessingStats getStats() {
        return new ProcessingStats(processedCount.get(), totalAmount.get());
    }
}

// Advanced: Reactive streams with Project Reactor
public Flux<ProcessedOrder> processOrdersReactive(Flux<Order> orders) {
    return orders
        .filter(order -> order.getAmount() > 100)
        .flatMap(order -> customerService.getCustomerReactive(order.getCustomerId()))
        .filter(Customer::isPremium)
        .map(Customer::getName)
        .map(String::toUpperCase)
        .sort() // Reactive sort
        .onBackpressureBuffer(1000) // Handle backpressure
        .doOnNext(processedOrder -> logger.info("Processed: {}", processedOrder))
        .doOnError(error -> logger.error("Processing error", error));
}
```

**Advanced Follow-up Questions**:
- When should you use parallel streams vs sequential streams?
- How does the fork-join pool work with parallel streams?
- What are the pitfalls of using stateful lambda expressions in streams?
- How do you handle exceptions in stream pipelines?
- What's the difference between intermediate and terminal operations?
- How do you implement custom collectors for complex aggregation scenarios?

---

### **Question 2: Optional Best Practices**
**Scenario**: Refactor this code using Optional effectively and explain anti-patterns.

**Expected Answer**:
```java
// Anti-patterns to avoid
public class OptionalAntiPatterns {
    
    // ❌ Don't use Optional for fields
    private Optional<String> name; // Bad!
    
    // ❌ Don't use Optional for parameters
    public void processUser(Optional<User> user) { } // Bad!
    
    // ❌ Don't call get() without checking
    public String getName() {
        return findUser().get().getName(); // Bad!
    }
    
    // ❌ Don't use Optional in collections
    private List<Optional<String>> names; // Bad!
    
    // ❌ Don't use Optional for primitive types without wrapper
    public Optional<Integer> getAge() {
        return Optional.ofNullable(age); // Prefer OptionalInt for primitives
    }
}

// Best practices
public class OptionalBestPractices {
    
    // ✅ Use Optional for return types that might be empty
    public Optional<User> findUser(String id) {
        return userRepository.findById(id);
    }
    
    // ✅ Chain operations effectively
    public String getUserDisplayName(String userId) {
        return findUser(userId)
            .map(User::getName)
            .filter(name -> !name.isEmpty())
            .orElse("Anonymous");
    }
    
    // ✅ Use flatMap for nested Optionals
    public Optional<String> getUserEmail(String userId) {
        return findUser(userId)
            .flatMap(User::getEmail); // User.getEmail() returns Optional<String>
    }
    
    // ✅ Exception handling with Optional
    public Optional<Integer> parseInteger(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    
    // ✅ Use Optional with streams
    public List<String> getValidUserEmails(List<String> userIds) {
        return userIds.stream()
            .map(this::findUser)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(User::getEmail)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
    
    // ✅ More elegant stream processing with flatMap
    public List<String> getValidUserEmailsElegant(List<String> userIds) {
        return userIds.stream()
            .map(this::findUser)
            .flatMap(Optional::stream) // Java 9+ method
            .map(User::getEmail)
            .flatMap(Optional::stream)
            .collect(Collectors.toList());
    }
    
    // ✅ Use Optional.or() for alternative values (Java 9+)
    public String getUserDisplayNameWithFallback(String userId) {
        return findUser(userId)
            .map(User::getName)
            .or(() -> findUserInCache(userId).map(User::getName))
            .or(() -> Optional.of("Default User"))
            .get();
    }
    
    // ✅ Use Optional.ifPresent() for side effects
    public void sendNotificationIfUserExists(String userId, String message) {
        findUser(userId)
            .ifPresent(user -> notificationService.send(user, message));
    }
    
    // ✅ Use Optional.ifPresentOrElse() for handling both cases (Java 9+)
    public void handleUserLookup(String userId) {
        findUser(userId)
            .ifPresentOrElse(
                user -> logger.info("Found user: {}", user.getName()),
                () -> logger.warn("User not found: {}", userId)
            );
    }
    
    // ✅ Use Optional with complex object creation
    public Optional<Order> createOrderFromRequest(OrderRequest request) {
        return findUser(request.getUserId())
            .filter(User::isActive)
            .map(user -> new Order(user, request.getItems(), request.getShippingAddress()));
    }
}

// Advanced: Optional in functional programming context
public class FunctionalOptionalPatterns {
    
    // Composing Optionals with functions
    public Function<String, Optional<String>> createEmailValidator() {
        return this::validateEmailFormat
            .andThen(this::checkEmailExists)
            .andThen(this::sanitizeEmail);
    }
    
    // Optional in monadic operations
    public Optional<UserProfile> buildUserProfile(String userId) {
        return findUser(userId)
            .flatMap(user -> getPreferences(user.getId()))
            .flatMap(prefs -> getSocialProfile(prefs.getSocialId()))
            .flatMap(social -> buildProfile(social, prefs));
    }
    
    // Optional with error handling
    public Either<Error, User> findUserWithErrorHandling(String userId) {
        return findUser(userId)
            .map(Either::<Error, User>right)
            .orElseGet(() -> Either.left(new UserNotFoundError(userId)));
    }
}
```

**Advanced Follow-up Questions**:
- What are the performance implications of using Optional vs null checks?
- How do Optional and reactive programming libraries like Reactor work together?
- What are the differences between Optional.or() and Optional.orElseGet()?
- How do you handle Optional in API design for public methods?
- What are the best practices for using Optional with collections?
- How does Optional interact with serialization frameworks like Jackson?

---

### **Question 3: Virtual Threads (Java 21+)**
**Scenario**: Implement a high-throughput web service using Virtual Threads.

**Expected Answer**:
```java
@RestController
public class VirtualThreadController {
    
    private final ExecutorService virtualExecutor = 
        Executors.newVirtualThreadPerTaskExecutor();
    
    // Traditional approach with platform threads (limited scalability)
    @GetMapping("/traditional/{id}")
    public CompletableFuture<UserResponse> getTraditional(@PathVariable String id) {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate I/O operation
            return processUser(id);
        });
    }
    
    // Virtual threads approach (massive scalability)
    @GetMapping("/virtual/{id}")
    public CompletableFuture<UserResponse> getVirtual(@PathVariable String id) {
        return CompletableFuture.supplyAsync(() -> {
            return processUser(id);
        }, virtualExecutor);
    }
    
    private UserResponse processUser(String id) {
        try {
            // Simulate database call (blocking I/O)
            Thread.sleep(100);
            User user = userService.findById(id);
            
            // Simulate external API call
            Thread.sleep(50);
            Profile profile = profileService.getProfile(user.getId());
            
            return new UserResponse(user, profile);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    // Advanced: Virtual threads with structured concurrency (Java 21+)
    public UserResponse processUserStructured(String id) throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Concurrently fetch user and profile
            StructuredTaskScope.Subtask<User> userTask = scope.fork(() -> userService.findById(id));
            StructuredTaskScope.Subtask<Profile> profileTask = scope.fork(() -> profileService.getProfile(id));
            
            // Wait for both tasks to complete or any to fail
            scope.join();
            scope.throwIfFailed();
            
            // Both completed successfully
            return new UserResponse(userTask.get(), profileTask.get());
        }
    }
    
    // Advanced: Virtual threads with scoped values (Java 21+)
    private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();
    
    @GetMapping("/scoped/{id}")
    public ResponseEntity<UserResponse> getWithScopedValue(@PathVariable String id, 
                                                          @RequestHeader("X-Request-ID") String requestId) {
        return ScopedValue.where(REQUEST_ID, requestId).call(() -> {
            logger.info("Processing request: {}", REQUEST_ID.get());
            UserResponse response = processUser(id);
            return ResponseEntity.ok(response);
        });
    }
}

// Performance comparison
@Component
public class VirtualThreadBenchmark {
    
    public void compareThroughput() {
        ExecutorService platformThreads = Executors.newFixedThreadPool(200);
        ExecutorService virtualThreads = Executors.newVirtualThreadPerTaskExecutor();
        
        int taskCount = 10000;
        
        // Platform threads benchmark
        long startTime = System.currentTimeMillis();
        List<Future<?>> platformFutures = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            platformFutures.add(platformThreads.submit(this::simulateIoTask));
        }
        waitForCompletion(platformFutures);
        long platformTime = System.currentTimeMillis() - startTime;
        
        // Virtual threads benchmark  
        startTime = System.currentTimeMillis();
        List<Future<?>> virtualFutures = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            virtualFutures.add(virtualThreads.submit(this::simulateIoTask));
        }
        waitForCompletion(virtualFutures);
        long virtualTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Platform threads: " + platformTime + "ms");
        System.out.println("Virtual threads: " + virtualTime + "ms");
        System.out.println("Improvement: " + ((double)platformTime / virtualTime) + "x");
        
        // Cleanup
        platformThreads.shutdown();
        virtualThreads.shutdown();
    }
    
    private void simulateIoTask() {
        try {
            // Simulate I/O-bound work
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void waitForCompletion(List<Future<?>> futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// Advanced: Virtual thread scheduling customization
public class CustomVirtualThreadConfig {
    
    @Bean
    public ExecutorService virtualThreadExecutor() {
        return Thread.ofVirtual()
            .name("custom-virtual-", 0)
            .factory()
            .newThread(() -> {
                // Custom initialization for virtual threads
                MDC.put("thread-type", "virtual");
                return Executors.newVirtualThreadPerTaskExecutor();
            })
            .executorService();
    }
    
    // Monitor virtual thread usage
    @ManagedOperation
    public String getVirtualThreadStats() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        if (threadBean instanceof com.sun.management.ThreadMXBean) {
            com.sun.management.ThreadMXBean sunThreadBean = 
                (com.sun.management.ThreadMXBean) threadBean;
            
            long totalThreads = sunThreadBean.getTotalThreadCount();
            long peakThreads = sunThreadBean.getPeakThreadCount();
            long daemonThreads = sunThreadBean.getDaemonThreadCount();
            
            return String.format("Total: %d, Peak: %d, Daemon: %d", 
                               totalThreads, peakThreads, daemonThreads);
        }
        return "Not supported on this JVM";
    }
}

// Advanced: Integration with reactive programming
@Service
public class ReactiveVirtualThreadService {
    
    private final ExecutorService virtualExecutor = 
        Executors.newVirtualThreadPerTaskExecutor();
    
    public Mono<UserResponse> getUserReactive(String id) {
        return Mono.fromCallable(() -> {
            // Blocking operation in virtual thread
            return processUserBlocking(id);
        })
        .subscribeOn(Schedulers.fromExecutor(virtualExecutor));
    }
    
    private UserResponse processUserBlocking(String id) {
        // Traditional blocking I/O operations
        User user = userRepository.findById(id);
        Profile profile = profileRepository.findByUserId(id);
        return new UserResponse(user, profile);
    }
}
```

**Advanced Follow-up Questions**:
- How do virtual threads differ from traditional thread pooling?
- What are the limitations of virtual threads compared to platform threads?
- How does structured concurrency work with virtual threads?
- What are the best practices for integrating virtual threads with existing frameworks?
- How do you monitor and debug applications using virtual threads?
- What are the implications of virtual threads on garbage collection?

---

### **Question 4: Records and Pattern Matching**
**Scenario**: Design an expression evaluator using Records and Pattern Matching.

**Expected Answer**:
```java
// Sealed interface for type safety
public sealed interface Expression 
    permits Constant, Addition, Multiplication, Variable {

    // Evaluate expression with variable values
    double evaluate(Map<String, Double> variables);
    
    // String representation
    String toString();
}

// Record for constant values
public record Constant(double value) implements Expression {
    @Override
    public double evaluate(Map<String, Double> variables) {
        return value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

// Record for variables
public record Variable(String name) implements Expression {
    @Override
    public double evaluate(Map<String, Double> variables) {
        return variables.getOrDefault(name, 0.0);
    }
    
    @Override
    public String toString() {
        return name;
    }
}

// Record for addition operations
public record Addition(Expression left, Expression right) implements Expression {
    @Override
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) + right.evaluate(variables);
    }
    
    @Override
    public String toString() {
        return "(" + left + " + " + right + ")";
    }
}

// Record for multiplication operations
public record Multiplication(Expression left, Expression right) implements Expression {
    @Override
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) * right.evaluate(variables);
    }
    
    @Override
    public String toString() {
        return "(" + left + " * " + right + ")";
    }
}

// Advanced: Pattern matching with switch expressions (Java 17+)
public class ExpressionEvaluator {
    
    public double evaluate(Expression expr, Map<String, Double> variables) {
        return switch (expr) {
            case Constant c -> c.value();
            case Variable v -> variables.getOrDefault(v.name(), 0.0);
            case Addition a -> evaluate(a.left(), variables) + evaluate(a.right(), variables);
            case Multiplication m -> evaluate(m.left(), variables) * evaluate(m.right(), variables);
        };
    }
    
    // Advanced: Pattern matching with guards (Java 21+)
    public String describe(Expression expr) {
        return switch (expr) {
            case Constant c when c.value() == 0 -> "zero";
            case Constant c when c.value() == 1 -> "one";
            case Constant c when c.value() > 0 -> "positive constant";
            case Constant c -> "negative constant";
            case Variable v -> "variable: " + v.name();
            case Addition a when isZero(a.left()) -> "addition with zero left";
            case Addition a when isZero(a.right()) -> "addition with zero right";
            case Addition a -> "addition";
            case Multiplication m when isOne(m.left()) -> "multiplication by one";
            case Multiplication m when isZero(m.left()) || isZero(m.right()) -> "multiplication by zero";
            case Multiplication m -> "multiplication";
        };
    }
    
    private boolean isZero(Expression expr) {
        return expr instanceof Constant c && c.value() == 0;
    }
    
    private boolean isOne(Expression expr) {
        return expr instanceof Constant c && c.value() == 1;
    }
    
    // Advanced: Expression simplification using pattern matching
    public Expression simplify(Expression expr) {
        return switch (expr) {
            // x + 0 = x
            case Addition(Variable x, Constant c) when c.value() == 0 -> x;
            case Addition(Constant c, Variable x) when c.value() == 0 -> x;
            
            // x * 1 = x
            case Multiplication(Variable x, Constant c) when c.value() == 1 -> x;
            case Multiplication(Constant c, Variable x) when c.value() == 1 -> x;
            
            // x * 0 = 0
            case Multiplication(Expression left, Constant c) when c.value() == 0 -> new Constant(0);
            case Multiplication(Constant c, Expression right) when c.value() == 0 -> new Constant(0);
            
            // General case - no simplification
            default -> expr;
        };
    }
    
    // Advanced: Expression differentiation using pattern matching
    public Expression differentiate(Expression expr, String variable) {
        return switch (expr) {
            // d/dx(c) = 0
            case Constant c -> new Constant(0);
            
            // d/dx(x) = 1, d/dx(y) = 0
            case Variable v -> v.name().equals(variable) ? new Constant(1) : new Constant(0);
            
            // d/dx(f + g) = d/dx(f) + d/dx(g)
            case Addition a -> new Addition(
                differentiate(a.left(), variable), 
                differentiate(a.right(), variable)
            );
            
            // d/dx(f * g) = f * d/dx(g) + g * d/dx(f)
            case Multiplication m -> new Addition(
                new Multiplication(m.left(), differentiate(m.right(), variable)),
                new Multiplication(m.right(), differentiate(m.left(), variable))
            );
            
            // Default case
            default -> throw new IllegalArgumentException("Unsupported expression type: " + expr);
        };
    }
}

// Advanced: Record with compact constructor for validation
public record EmailAddress(String value) {
    public EmailAddress {
        if (value == null || !value.contains("@")) {
            throw new IllegalArgumentException("Invalid email address: " + value);
        }
        // Normalize the email
        value = value.toLowerCase().trim();
    }
    
    public String domain() {
        return value.substring(value.indexOf('@') + 1);
    }
    
    public String localPart() {
        return value.substring(0, value.indexOf('@'));
    }
}

// Advanced: Record with static factory methods
public record Temperature(double value, TemperatureUnit unit) {
    
    // Factory methods for different units
    public static Temperature celsius(double value) {
        return new Temperature(value, TemperatureUnit.CELSIUS);
    }
    
    public static Temperature fahrenheit(double value) {
        return new Temperature(value, TemperatureUnit.FAHRENHEIT);
    }
    
    public static Temperature kelvin(double value) {
        return new Temperature(value, TemperatureUnit.KELVIN);
    }
    
    // Conversion methods
    public Temperature toCelsius() {
        return switch (unit) {
            case CELSIUS -> this;
            case FAHRENHEIT -> new Temperature((value - 32) * 5/9, TemperatureUnit.CELSIUS);
            case KELVIN -> new Temperature(value - 273.15, TemperatureUnit.CELSIUS);
        };
    }
    
    public Temperature toFahrenheit() {
        return switch (unit) {
            case CELSIUS -> new Temperature(value * 9/5 + 32, TemperatureUnit.FAHRENHEIT);
            case FAHRENHEIT -> this;
            case KELVIN -> new Temperature((value - 273.15) * 9/5 + 32, TemperatureUnit.FAHRENHEIT);
        };
    }
}

enum TemperatureUnit {
    CELSIUS, FAHRENHEIT, KELVIN
}
```

**Advanced Follow-up Questions**:
- How do records interact with serialization frameworks like Jackson?
- What are the limitations of records compared to traditional classes?
- How do you handle inheritance with sealed interfaces and records?
- What are the performance implications of using records vs regular classes?
- How do records work with reflection and annotation processing?
- How do you implement the builder pattern with records?

---

### **Question 5: Advanced Features in Java 17-21**
**Scenario**: Demonstrate understanding of newer Java features.

**Expected Answer**:
```java
// Pattern matching for instanceof (Java 16+)
public class PatternMatchingExample {
    
    // Old way
    public String processOldWay(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            return str.toUpperCase();
        }
        return "Not a string";
    }
    
    // New way with pattern matching
    public String processNewWay(Object obj) {
        if (obj instanceof String str) {
            return str.toUpperCase(); // No cast needed
        }
        return "Not a string";
    }
    
    // Advanced: Pattern matching in switch expressions (Java 21+)
    public String processSwitchPattern(Object obj) {
        return switch (obj) {
            case String s && s.length() > 10 -> "Long string: " + s.substring(0, 10) + "...";
            case String s -> "Short string: " + s;
            case Integer i && i > 100 -> "Large number: " + i;
            case Integer i -> "Small number: " + i;
            case List<?> list && !list.isEmpty() -> "Non-empty list with " + list.size() + " elements";
            case List<?> list -> "Empty list";
            case null -> "Null value";
            default -> "Unknown type: " + obj.getClass().getSimpleName();
        };
    }
}

// Sealed classes (Java 17+)
public abstract sealed class Shape
    permits Circle, Rectangle, Triangle {
    
    public abstract double area();
}

public final class Circle extends Shape {
    private final double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

public final class Rectangle extends Shape {
    private final double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() {
        return width * height;
    }
}

public final class Triangle extends Shape {
    private final double base, height;
    
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    
    @Override
    public double area() {
        return 0.5 * base * height;
    }
}

// Advanced: Sealed interfaces with permits
public sealed interface Vehicle 
    permits Car, Motorcycle, Truck {
    
    int getMaxPassengers();
}

public final class Car implements Vehicle {
    @Override
    public int getMaxPassengers() { return 5; }
}

public final class Motorcycle implements Vehicle {
    @Override
    public int getMaxPassengers() { return 2; }
}

public final class Truck implements Vehicle {
    @Override
    public int getMaxPassengers() { return 3; }
}

// Text blocks (Java 15+)
public class TextBlockExample {
    
    public String getHtmlTemplate() {
        return """
            <html>
                <head>
                    <title>%s</title>
                </head>
                <body>
                    <h1>%s</h1>
                    <p>%s</p>
                </body>
            </html>
            """.formatted("My Page", "Welcome", "This is a sample page");
    }
    
    public String getJsonExample() {
        return """
            {
                "name": "%s",
                "age": %d,
                "address": {
                    "street": "%s",
                    "city": "%s"
                }
            }
            """.formatted("John Doe", 30, "123 Main St", "New York");
    }
}

// Advanced: Helpful NullPointerExceptions (Java 14+)
public class HelpfulNPEExample {
    
    public String processUser(User user) {
        // With helpful NPEs, you get detailed information about which field was null
        return user.getProfile().getAddress().getStreet().toUpperCase();
        // Instead of generic NPE, you get: "Cannot invoke 'String.toUpperCase()' 
        // because the return value of 'Address.getStreet()' is null"
    }
}

// Advanced: Foreign Function & Memory API (Java 22+ preview)
public class ForeignFunctionExample {
    
    // Example of calling native library function
    public static void callNativeFunction() {
        // This is a preview feature showing how to interface with native code
        // without JNI overhead
        /*
        try (Arena arena = Arena.ofConfined()) {
            // Allocate native memory
            MemorySegment nativeString = arena.allocateUtf8String("Hello from Java");
            
            // Call native function (example)
            // int result = LIBRARY.my_native_function(nativeString);
        }
        */
    }
}

// Advanced: Vector API (Java 16+ incubator/preview)
public class VectorApiExample {
    
    // Example of SIMD operations for performance
    public void vectorComputation(float[] a, float[] b, float[] result) {
        /*
        // This is an incubator feature showing vectorized computation
        int vectorSize = VectorShape.preferredShape().vectorBitSize() / 32;
        IntVector species = IntVector.SPECIES_PREFERRED;
        
        for (int i = 0; i < a.length; i += species.length()) {
            IntVector va = IntVector.fromArray(species, a, i);
            IntVector vb = IntVector.fromArray(species, b, i);
            IntVector resultVector = va.add(vb);
            resultVector.intoArray(result, i);
        }
        */
    }
}
```

**Advanced Follow-up Questions**:
- How do sealed classes improve code maintainability?
- What are the performance benefits of pattern matching?
- How do text blocks handle indentation and line terminators?
- What are the implications of Helpful NullPointerExceptions for debugging?
- How do preview features like Vector API and Foreign Function API work?
- What are the best practices for using new Java features in production code?

---