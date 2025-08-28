# Advanced Topics - Practice Exercises

## 📝 Instructions
- Master generics, lambda expressions, and streams API
- Practice functional programming concepts
- Work with modern Java features (Optional, annotations, etc.)
- Focus on type safety and code expressiveness
- Learn advanced collection operations and transformations

---

## Exercise 1: Generic Data Structure Library
**Difficulty: Beginner-Intermediate**

Create a comprehensive library of generic data structures with type safety and flexibility.

### Requirements:
- **Generic Stack/Queue**: Implement with different backing stores
- **Generic Tree**: Binary tree, AVL tree, or B-tree with type parameters
- **Generic Graph**: Support different vertex and edge types
- **Utility Methods**: Generic algorithms for searching, sorting, filtering
- **Type Bounds**: Use bounded type parameters for numeric operations

### Implementation:
```java
// Generic stack with different implementations
public interface Stack<T> {
    void push(T item);
    T pop();
    T peek();
    boolean isEmpty();
    int size();
}

public class ArrayStack<T> implements Stack<T> {
    private T[] array;
    private int top;
    
    @SuppressWarnings("unchecked")
    public ArrayStack(int capacity) {
        array = (T[]) new Object[capacity];
        top = -1;
    }
    
    // Implementation...
}

// Generic tree with bounded types for comparable elements
public class BinarySearchTree<T extends Comparable<T>> {
    private Node<T> root;
    
    private static class Node<T> {
        T data;
        Node<T> left, right;
        
        Node(T data) { this.data = data; }
    }
    
    public void insert(T data) { /* Implementation */ }
    public boolean search(T data) { /* Implementation */ }
    public List<T> inorderTraversal() { /* Implementation */ }
}

// Generic utility class with wildcard methods
public class DataStructureUtils {
    public static <T extends Number> double sum(List<T> numbers) {
        return numbers.stream().mapToDouble(Number::doubleValue).sum();
    }
    
    public static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        // Generic sorting algorithm
    }
}
```

### Usage Examples:
```java
// Type-safe data structures
Stack<String> stringStack = new ArrayStack<>(100);
Stack<Integer> intStack = new LinkedStack<>();

BinarySearchTree<String> nameTree = new BinarySearchTree<>();
BinarySearchTree<Double> scoreTree = new BinarySearchTree<>();

// Generic utility usage
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
double total = DataStructureUtils.sum(numbers);

List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
DataStructureUtils.sort(names);
```

---

## Exercise 2: Functional Programming Toolkit
**Difficulty: Intermediate**

Build a comprehensive toolkit for functional programming patterns using lambdas and streams.

### Requirements:
- **Function Composition**: Chain functions together
- **Currying**: Transform multi-parameter functions
- **Memoization**: Cache function results for performance
- **Lazy Evaluation**: Defer computation until needed
- **Monadic Operations**: Optional chaining, error handling

### Functional Toolkit:
```java
// Function composition utilities
public class FunctionUtils {
    public static <T, R, V> Function<T, V> compose(
            Function<T, R> f1, Function<R, V> f2) {
        return t -> f2.apply(f1.apply(t));
    }
    
    public static <T, R> Function<T, R> memoize(Function<T, R> function) {
        Map<T, R> cache = new ConcurrentHashMap<>();
        return input -> cache.computeIfAbsent(input, function);
    }
    
    // Currying example: convert (a, b) -> c to a -> (b -> c)
    public static <A, B, C> Function<A, Function<B, C>> curry(
            BiFunction<A, B, C> biFunction) {
        return a -> b -> biFunction.apply(a, b);
    }
}

// Lazy evaluation wrapper
public class Lazy<T> {
    private Supplier<T> supplier;
    private volatile T value;
    private volatile boolean computed = false;
    
    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }
    
    public T get() {
        if (!computed) {
            synchronized (this) {
                if (!computed) {
                    value = supplier.get();
                    computed = true;
                    supplier = null; // Allow GC
                }
            }
        }
        return value;
    }
    
    public <R> Lazy<R> map(Function<T, R> mapper) {
        return Lazy.of(() -> mapper.apply(get()));
    }
}

// Functional data processing pipeline
public class DataPipeline<T> {
    private final Stream<T> stream;
    
    private DataPipeline(Stream<T> stream) {
        this.stream = stream;
    }
    
    public static <T> DataPipeline<T> of(Collection<T> data) {
        return new DataPipeline<>(data.stream());
    }
    
    public <R> DataPipeline<R> transform(Function<T, R> mapper) {
        return new DataPipeline<>(stream.map(mapper));
    }
    
    public DataPipeline<T> filter(Predicate<T> predicate) {
        return new DataPipeline<>(stream.filter(predicate));
    }
    
    public <R> R reduce(R identity, BinaryOperator<R> accumulator, 
                       Function<T, R> mapper) {
        return stream.map(mapper).reduce(identity, accumulator);
    }
    
    public List<T> collect() {
        return stream.collect(Collectors.toList());
    }
}
```

### Usage Examples:
```java
// Function composition
Function<String, String> trim = String::trim;
Function<String, String> uppercase = String::toUpperCase;
Function<String, String> process = FunctionUtils.compose(trim, uppercase);

String result = process.apply("  hello world  "); // "HELLO WORLD"

// Memoized expensive computation
Function<Integer, BigInteger> factorial = FunctionUtils.memoize(n -> {
    System.out.println("Computing factorial of " + n);
    return calculateFactorial(n);
});

// Lazy evaluation
Lazy<ExpensiveObject> expensive = Lazy.of(() -> {
    System.out.println("Creating expensive object...");
    return new ExpensiveObject();
});

// Only created when first accessed
ExpensiveObject obj = expensive.get();
```

---

## Exercise 3: Stream Processing Analytics Engine
**Difficulty: Intermediate-Advanced**

Create a powerful analytics engine using streams for complex data processing and analysis.

### Requirements:
- **Data Aggregation**: Group, count, sum, average by multiple criteria
- **Window Operations**: Time-based and count-based windowing
- **Complex Transformations**: Multi-step data transformations
- **Statistical Analysis**: Percentiles, standard deviation, correlation
- **Custom Collectors**: Build domain-specific collectors

### Analytics Engine:
```java
// Analytics engine with fluent API
public class AnalyticsEngine<T> {
    private final Stream<T> dataStream;
    
    private AnalyticsEngine(Stream<T> stream) {
        this.dataStream = stream;
    }
    
    public static <T> AnalyticsEngine<T> from(Collection<T> data) {
        return new AnalyticsEngine<>(data.stream());
    }
    
    public static <T> AnalyticsEngine<T> from(Stream<T> stream) {
        return new AnalyticsEngine<>(stream);
    }
    
    // Group by multiple criteria
    public <K1, K2> Map<K1, Map<K2, List<T>>> groupBy(
            Function<T, K1> classifier1, Function<T, K2> classifier2) {
        return dataStream.collect(
            Collectors.groupingBy(classifier1,
                Collectors.groupingBy(classifier2, Collectors.toList()))
        );
    }
    
    // Statistical analysis
    public <N extends Number> Statistics computeStatistics(Function<T, N> extractor) {
        DoubleSummaryStatistics stats = dataStream
            .mapToDouble(item -> extractor.apply(item).doubleValue())
            .summaryStatistics();
            
        return new Statistics(stats);
    }
    
    // Custom aggregations
    public <R> R aggregate(Collector<T, ?, R> collector) {
        return dataStream.collect(collector);
    }
    
    // Windowing operations
    public List<List<T>> window(int windowSize) {
        return dataStream.collect(WindowingCollector.fixedWindow(windowSize));
    }
}

// Custom collectors
public class CustomCollectors {
    // Collect to frequency map
    public static <T> Collector<T, ?, Map<T, Long>> toFrequencyMap() {
        return Collectors.groupingBy(Function.identity(), 
                                   Collectors.counting());
    }
    
    // Collect top N elements
    public static <T> Collector<T, ?, List<T>> topN(int n, Comparator<T> comparator) {
        return Collector.of(
            () -> new PriorityQueue<>(comparator),
            (queue, item) -> {
                queue.offer(item);
                if (queue.size() > n) {
                    queue.poll();
                }
            },
            (queue1, queue2) -> {
                queue1.addAll(queue2);
                return queue1.stream()
                      .sorted(comparator)
                      .limit(n)
                      .collect(Collectors.toCollection(
                          () -> new PriorityQueue<>(comparator)));
            },
            queue -> queue.stream()
                         .sorted(comparator.reversed())
                         .collect(Collectors.toList())
        );
    }
    
    // Partition by predicate with counts
    public static <T> Collector<T, ?, PartitionedResult<T>> 
            partitionWithCounts(Predicate<T> predicate) {
        return Collector.of(
            PartitionedResult::new,
            (result, item) -> {
                if (predicate.test(item)) {
                    result.addMatching(item);
                } else {
                    result.addNonMatching(item);
                }
            },
            PartitionedResult::combine
        );
    }
}

// Sample data processing
public class SalesAnalytics {
    public static class Sale {
        private final String product;
        private final String region;
        private final double amount;
        private final LocalDate date;
        
        // Constructor and getters...
    }
    
    public void analyzeSales(List<Sale> sales) {
        AnalyticsEngine<Sale> analytics = AnalyticsEngine.from(sales);
        
        // Group sales by region and product
        Map<String, Map<String, List<Sale>>> regionProductSales = 
            analytics.groupBy(Sale::getRegion, Sale::getProduct);
        
        // Calculate statistics for sales amounts
        Statistics amountStats = analytics.computeStatistics(Sale::getAmount);
        
        // Find top 5 products by total sales
        Map<String, Double> productTotals = sales.stream()
            .collect(Collectors.groupingBy(Sale::getProduct,
                    Collectors.summingDouble(Sale::getAmount)));
        
        List<String> topProducts = productTotals.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(5)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        // Monthly sales trend
        Map<YearMonth, Double> monthlySales = sales.stream()
            .collect(Collectors.groupingBy(
                sale -> YearMonth.from(sale.getDate()),
                Collectors.summingDouble(Sale::getAmount)
            ));
    }
}
```

---

## Exercise 4: Annotation-Based Configuration Framework
**Difficulty: Advanced**

Build a configuration framework that uses annotations and reflection for automatic configuration.

### Requirements:
- **Custom Annotations**: Create annotations for configuration metadata
- **Annotation Processing**: Process annotations at runtime using reflection
- **Type Conversion**: Automatic conversion from strings to various types
- **Validation**: Validate configuration values using annotation constraints
- **Configuration Sources**: Support multiple sources (properties, JSON, environment)

### Configuration Framework:
```java
// Configuration annotations
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigValue {
    String key();
    String defaultValue() default "";
    boolean required() default false;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateRange {
    double min() default Double.MIN_VALUE;
    double max() default Double.MAX_VALUE;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidatePattern {
    String pattern();
    String message() default "Invalid format";
}

// Configuration processor
public class ConfigurationProcessor {
    private final Map<String, String> properties;
    
    public ConfigurationProcessor(Properties properties) {
        this.properties = new HashMap<>();
        properties.forEach((key, value) -> 
            this.properties.put(key.toString(), value.toString()));
    }
    
    public <T> T configure(Class<T> configClass) throws ConfigurationException {
        try {
            T instance = configClass.getDeclaredConstructor().newInstance();
            
            for (Field field : configClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(ConfigValue.class)) {
                    configureField(instance, field);
                }
            }
            
            validateConfiguration(instance);
            return instance;
            
        } catch (Exception e) {
            throw new ConfigurationException("Failed to configure " + 
                                           configClass.getSimpleName(), e);
        }
    }
    
    private <T> void configureField(T instance, Field field) 
            throws IllegalAccessException {
        ConfigValue annotation = field.getAnnotation(ConfigValue.class);
        String key = annotation.key();
        String defaultValue = annotation.defaultValue();
        boolean required = annotation.required();
        
        String value = properties.get(key);
        if (value == null) {
            if (required) {
                throw new ConfigurationException("Required property missing: " + key);
            }
            value = defaultValue;
        }
        
        if (!value.isEmpty()) {
            field.setAccessible(true);
            Object convertedValue = convertValue(value, field.getType());
            field.set(instance, convertedValue);
        }
    }
    
    private Object convertValue(String value, Class<?> targetType) {
        if (targetType == String.class) return value;
        if (targetType == int.class || targetType == Integer.class) 
            return Integer.parseInt(value);
        if (targetType == double.class || targetType == Double.class) 
            return Double.parseDouble(value);
        if (targetType == boolean.class || targetType == Boolean.class) 
            return Boolean.parseBoolean(value);
        if (targetType == long.class || targetType == Long.class) 
            return Long.parseLong(value);
        if (targetType.isEnum()) 
            return Enum.valueOf((Class<? extends Enum>) targetType, value);
        
        throw new ConfigurationException("Unsupported type: " + targetType);
    }
    
    private <T> void validateConfiguration(T instance) throws IllegalAccessException {
        for (Field field : instance.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(instance);
            
            // Range validation
            if (field.isAnnotationPresent(ValidateRange.class) && value instanceof Number) {
                ValidateRange range = field.getAnnotation(ValidateRange.class);
                double numValue = ((Number) value).doubleValue();
                if (numValue < range.min() || numValue > range.max()) {
                    throw new ConfigurationException(String.format(
                        "Value %s for field %s is outside range [%f, %f]",
                        value, field.getName(), range.min(), range.max()));
                }
            }
            
            // Pattern validation
            if (field.isAnnotationPresent(ValidatePattern.class) && value instanceof String) {
                ValidatePattern pattern = field.getAnnotation(ValidatePattern.class);
                if (!((String) value).matches(pattern.pattern())) {
                    throw new ConfigurationException(pattern.message() + 
                                                   ": " + field.getName());
                }
            }
        }
    }
}

// Example configuration class
public class DatabaseConfiguration {
    @ConfigValue(key = "db.url", required = true)
    private String url;
    
    @ConfigValue(key = "db.username", defaultValue = "admin")
    private String username;
    
    @ConfigValue(key = "db.password", required = true)
    private String password;
    
    @ConfigValue(key = "db.port", defaultValue = "5432")
    @ValidateRange(min = 1, max = 65535)
    private int port;
    
    @ConfigValue(key = "db.timeout", defaultValue = "30")
    @ValidateRange(min = 1, max = 300)
    private int timeoutSeconds;
    
    @ConfigValue(key = "db.ssl", defaultValue = "false")
    private boolean sslEnabled;
    
    // Getters...
}
```

---

## Exercise 5: Fluent API Builder Framework
**Difficulty: Advanced**

Create a framework for building fluent APIs with type safety and method chaining.

### Requirements:
- **Type-Safe Builders**: Ensure required fields are set before building
- **Conditional Steps**: Different builder paths based on choices
- **Validation**: Validate builder state at each step
- **Immutable Results**: Produce immutable objects from builders
- **Documentation**: Generate builder documentation automatically

### Fluent Builder Framework:
```java
// Base builder interface
public interface FluentBuilder<T> {
    T build();
}

// Query builder example using phantom types for type safety
public class QueryBuilder {
    
    // Phantom types for compile-time state tracking
    public interface HasTable {}
    public interface HasWhere {}
    public interface HasOrderBy {}
    
    public static class QueryStep<State> {
        protected final StringBuilder query;
        protected final List<Object> parameters;
        
        protected QueryStep() {
            this.query = new StringBuilder();
            this.parameters = new ArrayList<>();
        }
        
        protected QueryStep(StringBuilder query, List<Object> parameters) {
            this.query = new StringBuilder(query);
            this.parameters = new ArrayList<>(parameters);
        }
    }
    
    public static class SelectStep extends QueryStep<Void> {
        public SelectStep(String... columns) {
            query.append("SELECT ");
            query.append(columns.length == 0 ? "*" : String.join(", ", columns));
        }
    }
    
    public static class FromStep<State> extends QueryStep<State> {
        private FromStep(StringBuilder query, List<Object> parameters) {
            super(query, parameters);
        }
        
        public WhereStep<HasTable> from(String table) {
            query.append(" FROM ").append(table);
            return new WhereStep<>(query, parameters);
        }
    }
    
    public static class WhereStep<State> extends QueryStep<State> {
        private WhereStep(StringBuilder query, List<Object> parameters) {
            super(query, parameters);
        }
        
        public WhereStep<HasWhere> where(String condition, Object... params) {
            query.append(" WHERE ").append(condition);
            parameters.addAll(Arrays.asList(params));
            return new WhereStep<>(query, parameters);
        }
        
        public WhereStep<HasWhere> and(String condition, Object... params) {
            query.append(" AND ").append(condition);
            parameters.addAll(Arrays.asList(params));
            return new WhereStep<>(query, parameters);
        }
        
        public OrderByStep<HasOrderBy> orderBy(String column) {
            query.append(" ORDER BY ").append(column);
            return new OrderByStep<>(query, parameters);
        }
        
        public Query build() {
            return new Query(query.toString(), parameters);
        }
    }
    
    public static class OrderByStep<State> extends QueryStep<State> {
        private OrderByStep(StringBuilder query, List<Object> parameters) {
            super(query, parameters);
        }
        
        public OrderByStep<State> asc() {
            query.append(" ASC");
            return this;
        }
        
        public OrderByStep<State> desc() {
            query.append(" DESC");
            return this;
        }
        
        public Query build() {
            return new Query(query.toString(), parameters);
        }
    }
    
    public static SelectStep select(String... columns) {
        return new SelectStep(columns);
    }
}

// HTTP client builder with fluent API
public class HttpRequestBuilder {
    
    public static class MethodStep {
        public UrlStep get() { return new UrlStep("GET"); }
        public UrlStep post() { return new UrlStep("POST"); }
        public UrlStep put() { return new UrlStep("PUT"); }
        public UrlStep delete() { return new UrlStep("DELETE"); }
    }
    
    public static class UrlStep {
        private final String method;
        
        private UrlStep(String method) { this.method = method; }
        
        public HeaderStep url(String url) {
            return new HeaderStep(method, url);
        }
    }
    
    public static class HeaderStep {
        private final String method;
        private final String url;
        private final Map<String, String> headers = new HashMap<>();
        
        private HeaderStep(String method, String url) {
            this.method = method;
            this.url = url;
        }
        
        public HeaderStep header(String name, String value) {
            headers.put(name, value);
            return this;
        }
        
        public HeaderStep contentType(String contentType) {
            return header("Content-Type", contentType);
        }
        
        public HeaderStep authorization(String token) {
            return header("Authorization", "Bearer " + token);
        }
        
        public BodyStep body(String body) {
            return new BodyStep(method, url, headers, body);
        }
        
        public HttpRequest build() {
            return new HttpRequest(method, url, headers, null);
        }
    }
    
    public static class BodyStep {
        private final String method, url, body;
        private final Map<String, String> headers;
        
        private BodyStep(String method, String url, Map<String, String> headers, String body) {
            this.method = method;
            this.url = url;
            this.headers = new HashMap<>(headers);
            this.body = body;
        }
        
        public HttpRequest build() {
            return new HttpRequest(method, url, headers, body);
        }
    }
    
    public static MethodStep create() {
        return new MethodStep();
    }
}
```

### Usage Examples:
```java
// Type-safe query building
Query query = QueryBuilder
    .select("name", "email")
    .from("users")
    .where("age > ?", 18)
    .and("active = ?", true)
    .orderBy("name")
    .asc()
    .build();

// HTTP request building
HttpRequest request = HttpRequestBuilder
    .create()
    .post()
    .url("https://api.example.com/users")
    .contentType("application/json")
    .authorization("token123")
    .body("{\"name\": \"John\"}")
    .build();
```

---

## Exercise 6: Reactive Streams Implementation
**Difficulty: Expert**

Implement a basic reactive streams library with backpressure and async processing.

### Requirements:
- **Publisher/Subscriber Pattern**: Implement reactive streams specification
- **Backpressure Handling**: Control flow when consumers are slower than producers
- **Operator Chaining**: Support map, filter, reduce, and other operators
- **Async Processing**: Non-blocking operations with CompletableFuture
- **Error Handling**: Proper error propagation and recovery

### Reactive Streams:
```java
// Basic reactive stream interfaces
public interface Publisher<T> {
    void subscribe(Subscriber<T> subscriber);
}

public interface Subscriber<T> {
    void onSubscribe(Subscription subscription);
    void onNext(T item);
    void onError(Throwable error);
    void onComplete();
}

public interface Subscription {
    void request(long n);
    void cancel();
}

// Simple publisher implementation
public class SimplePublisher<T> implements Publisher<T> {
    private final List<T> items;
    
    public SimplePublisher(List<T> items) {
        this.items = new ArrayList<>(items);
    }
    
    @Override
    public void subscribe(Subscriber<T> subscriber) {
        SimpleSubscription subscription = new SimpleSubscription(subscriber, items);
        subscriber.onSubscribe(subscription);
    }
    
    private class SimpleSubscription implements Subscription {
        private final Subscriber<T> subscriber;
        private final Iterator<T> iterator;
        private volatile boolean cancelled = false;
        
        private SimpleSubscription(Subscriber<T> subscriber, List<T> items) {
            this.subscriber = subscriber;
            this.iterator = items.iterator();
        }
        
        @Override
        public void request(long n) {
            if (cancelled || n <= 0) return;
            
            CompletableFuture.runAsync(() -> {
                try {
                    long remaining = n;
                    while (remaining > 0 && iterator.hasNext() && !cancelled) {
                        subscriber.onNext(iterator.next());
                        remaining--;
                    }
                    
                    if (!iterator.hasNext() && !cancelled) {
                        subscriber.onComplete();
                    }
                } catch (Exception e) {
                    if (!cancelled) {
                        subscriber.onError(e);
                    }
                }
            });
        }
        
        @Override
        public void cancel() {
            cancelled = true;
        }
    }
}

// Operator implementations
public class StreamOperators {
    
    public static <T, R> Publisher<R> map(Publisher<T> source, Function<T, R> mapper) {
        return subscriber -> source.subscribe(new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscriber.onSubscribe(subscription);
            }
            
            @Override
            public void onNext(T item) {
                try {
                    R mapped = mapper.apply(item);
                    subscriber.onNext(mapped);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
            
            @Override
            public void onError(Throwable error) {
                subscriber.onError(error);
            }
            
            @Override
            public void onComplete() {
                subscriber.onComplete();
            }
        });
    }
    
    public static <T> Publisher<T> filter(Publisher<T> source, Predicate<T> predicate) {
        return subscriber -> source.subscribe(new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscriber.onSubscribe(subscription);
            }
            
            @Override
            public void onNext(T item) {
                try {
                    if (predicate.test(item)) {
                        subscriber.onNext(item);
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
            
            @Override
            public void onError(Throwable error) {
                subscriber.onError(error);
            }
            
            @Override
            public void onComplete() {
                subscriber.onComplete();
            }
        });
    }
}
```

---

## 🎯 Challenge Projects

### Project A: Type-Safe Query DSL
Create a domain-specific language for building SQL queries with:
- Complete type safety at compile time
- Support for joins, subqueries, and complex conditions
- Automatic SQL generation for different databases
- Query optimization and caching
- Integration with connection pools

### Project B: Functional Data Processing Library
Build a comprehensive library for functional data processing:
- Lazy evaluation with stream-like operations
- Parallel processing with automatic work distribution
- Memory-efficient processing of large datasets
- Integration with various data sources
- Custom collector implementations

### Project C: Annotation-Based Testing Framework
Develop a testing framework similar to JUnit using annotations:
- Custom test annotations with metadata
- Automatic test discovery and execution
- Parameterized tests with data providers
- Test lifecycle management
- Reporting and result aggregation

---

## 📚 Testing Guidelines

### Generic Type Testing:
- Test with different type parameters
- Verify compile-time type safety
- Test wildcard usage scenarios
- Validate type bounds and constraints

### Lambda and Stream Testing:
- Test with various data sizes and types
- Verify lazy evaluation behavior
- Test parallel stream performance
- Validate exception handling in streams

### Annotation Processing:
- Test annotation inheritance
- Verify reflection-based processing
- Test performance with large numbers of annotations
- Validate error handling for malformed annotations

### Performance Testing:
- Benchmark functional vs imperative implementations
- Test memory usage with large datasets
- Measure stream operation performance
- Profile generic type erasure overhead

---

**Next:** [Best Practices Exercises](../../11-Best-Practices/exercises/)