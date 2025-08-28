# Java 8 Features - Quick Notes

## 🎯 **Lambda Expressions**

### **Basic Syntax**
```java
// Basic lambda syntax
(parameters) -> expression
(parameters) -> { statements; }

// Examples
() -> 42                          // No parameters
x -> x * 2                        // Single parameter (parentheses optional)
(x, y) -> x + y                   // Multiple parameters
(x, y) -> { return x + y; }       // Block body
(String s) -> s.length()          // Explicit type
```

### **Functional Interfaces**
```java
// Built-in functional interfaces
Predicate<T>     // T -> boolean
Function<T, R>   // T -> R  
Consumer<T>      // T -> void
Supplier<T>      // () -> T
BinaryOperator<T> // (T, T) -> T

// Usage examples
Predicate<String> isEmpty = String::isEmpty;
Function<String, Integer> length = String::length;
Consumer<String> print = System.out::println;
Supplier<String> supplier = () -> \"Hello\";
```

---

## 🌊 **Stream API**

### **Stream Creation**
```java
// From collections
List<String> list = Arrays.asList(\"a\", \"b\", \"c\");
Stream<String> stream1 = list.stream();

// From arrays
String[] array = {\"x\", \"y\", \"z\"};
Stream<String> stream2 = Arrays.stream(array);

// Generate streams
Stream<String> stream3 = Stream.of(\"a\", \"b\", \"c\");
Stream<Integer> stream4 = Stream.iterate(0, n -> n + 2).limit(10);
Stream<Double> stream5 = Stream.generate(Math::random).limit(5);
```

### **Intermediate Operations**
```java
// Transform and filter
list.stream()
    .filter(s -> s.length() > 2)     // Keep elements matching predicate
    .map(String::toUpperCase)         // Transform elements
    .distinct()                       // Remove duplicates
    .sorted()                         // Sort elements
    .limit(10)                        // Take first 10
    .skip(2);                         // Skip first 2

// FlatMap for nested structures
List<List<String>> nested = Arrays.asList(
    Arrays.asList(\"a\", \"b\"),
    Arrays.asList(\"c\", \"d\")
);
nested.stream()
    .flatMap(List::stream)            // Flatten: [\"a\", \"b\", \"c\", \"d\"]
    .collect(Collectors.toList());
```

### **Terminal Operations**
```java
// Collect results
List<String> result = stream.collect(Collectors.toList());
Set<String> set = stream.collect(Collectors.toSet());
String joined = stream.collect(Collectors.joining(\", \"));

// Reduce operations
Optional<String> max = stream.max(String::compareTo);
Optional<String> min = stream.min(String::compareTo);
long count = stream.count();

// Find operations
Optional<String> first = stream.findFirst();
Optional<String> any = stream.findAny();
boolean anyMatch = stream.anyMatch(s -> s.startsWith(\"A\"));
boolean allMatch = stream.allMatch(s -> s.length() > 0);
boolean noneMatch = stream.noneMatch(String::isEmpty);

// Custom reduction
Optional<String> reduced = stream.reduce((s1, s2) -> s1 + s2);
int sum = numbers.stream().reduce(0, Integer::sum);
```

### **Collectors**
```java
// Grouping
Map<Integer, List<String>> byLength = words.stream()
    .collect(Collectors.groupingBy(String::length));

// Partitioning
Map<Boolean, List<String>> partition = words.stream()
    .collect(Collectors.partitioningBy(s -> s.length() > 5));

// Statistics
IntSummaryStatistics stats = numbers.stream()
    .collect(Collectors.summarizingInt(Integer::intValue));

// Custom collectors
Collector<String, ?, String> customCollector = Collector.of(
    StringBuilder::new,               // Supplier
    StringBuilder::append,            // Accumulator
    StringBuilder::append,            // Combiner
    StringBuilder::toString           // Finisher
);
```

---

## 🎁 **Optional**

### **Creation**
```java
Optional<String> empty = Optional.empty();
Optional<String> of = Optional.of(\"value\");        // Throws if null
Optional<String> nullable = Optional.ofNullable(getValue()); // Safe
```

### **Transformation**
```java
Optional<String> optional = Optional.of(\"hello\");

// Map - transform value if present
Optional<Integer> length = optional.map(String::length);
Optional<String> upper = optional.map(String::toUpperCase);

// FlatMap - flatten nested Optionals
Optional<Optional<String>> nested = Optional.of(Optional.of(\"value\"));
Optional<String> flattened = nested.flatMap(Function.identity());

// Filter - keep value only if predicate matches
Optional<String> filtered = optional.filter(s -> s.length() > 3);
```

### **Value Extraction**
```java
// Get value with defaults
String value1 = optional.orElse(\"default\");
String value2 = optional.orElseGet(() -> computeDefault());
String value3 = optional.orElseThrow(() -> new RuntimeException(\"No value\"));

// Check presence
if (optional.isPresent()) {
    String value = optional.get();
}

// Functional approach
optional.ifPresent(System.out::println);
optional.ifPresentOrElse(
    System.out::println,
    () -> System.out.println(\"No value\")
);
```

---

## 🔗 **Method References**

### **Types of Method References**
```java
// Static method references
Function<String, Integer> parseInt = Integer::parseInt;
list.stream().map(Integer::parseInt);

// Instance method references
Predicate<String> isEmpty = String::isEmpty;
list.stream().filter(String::isEmpty);

// Constructor references
Supplier<List<String>> listSupplier = ArrayList::new;
Function<String, StringBuilder> sbConstructor = StringBuilder::new;

// Arbitrary object method references
Comparator<String> comparator = String::compareToIgnoreCase;
```

---

## 📅 **Date and Time API**

### **Core Classes**
```java
// Local date and time (no timezone)
LocalDate date = LocalDate.now();              // 2023-12-25
LocalTime time = LocalTime.now();              // 14:30:15.123
LocalDateTime dateTime = LocalDateTime.now();  // 2023-12-25T14:30:15.123

// With timezone
ZonedDateTime zoned = ZonedDateTime.now();     // 2023-12-25T14:30:15.123-05:00[America/New_York]
Instant instant = Instant.now();               // 2023-12-25T19:30:15.123Z
```

### **Creation and Manipulation**
```java
// Create specific dates
LocalDate specificDate = LocalDate.of(2023, 12, 25);
LocalTime specificTime = LocalTime.of(14, 30, 15);

// Parse from strings
LocalDate parsed = LocalDate.parse(\"2023-12-25\");
LocalDateTime parsedDT = LocalDateTime.parse(\"2023-12-25T14:30:15\");

// Manipulation (returns new instance)
LocalDate tomorrow = date.plusDays(1);
LocalDate nextMonth = date.plusMonths(1);
LocalDate lastYear = date.minusYears(1);

// With methods (returns new instance)
LocalDate withDay = date.withDayOfMonth(15);
LocalTime withHour = time.withHour(12);
```

### **Formatting and Parsing**
```java
// Built-in formatters
String formatted1 = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
String formatted2 = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

// Custom formatters
DateTimeFormatter formatter = DateTimeFormatter.ofPattern(\"dd/MM/yyyy HH:mm\");
String custom = dateTime.format(formatter);
LocalDateTime parsed = LocalDateTime.parse(\"25/12/2023 14:30\", formatter);
```

### **Duration and Period**
```java
// Duration - time-based amount
Duration duration = Duration.between(time1, time2);
long seconds = duration.getSeconds();
long nanos = duration.getNano();

// Period - date-based amount
Period period = Period.between(date1, date2);
int years = period.getYears();
int months = period.getMonths();
int days = period.getDays();

// Calculations
LocalDateTime future = dateTime.plus(duration);
LocalDate futureDate = date.plus(period);
```

---

## 🔧 **Interface Enhancements**

### **Default Methods**
```java
public interface MyInterface {
    // Abstract method
    void abstractMethod();
    
    // Default method with implementation
    default void defaultMethod() {
        System.out.println(\"Default implementation\");
    }
    
    // Static method
    static void staticMethod() {
        System.out.println(\"Static method in interface\");
    }
}
```

### **Conflict Resolution**
```java
interface A {
    default void method() { System.out.println(\"A\"); }
}

interface B {
    default void method() { System.out.println(\"B\"); }
}

// Must resolve conflict explicitly
class C implements A, B {
    @Override
    public void method() {
        A.super.method(); // Call A's implementation
        // or B.super.method(); // Call B's implementation
        // or provide custom implementation
    }
}
```

---

## ⚡ **Performance Tips**

### **Stream Performance**
```java
// Use primitive streams for better performance
int sum = IntStream.range(0, 1000000)
    .filter(i -> i % 2 == 0)
    .sum();

// Parallel streams for CPU-intensive operations
long count = largeList.parallelStream()
    .filter(expensivePredicate)
    .count();

// Avoid boxing/unboxing
OptionalInt max = numbers.stream()
    .mapToInt(Integer::intValue)
    .max();
```

### **Lambda Best Practices**
```java
// Prefer method references
list.stream().map(String::toUpperCase)  // Better
list.stream().map(s -> s.toUpperCase()) // Avoid

// Keep lambdas simple
list.stream().filter(this::isValid)     // Extract complex logic
list.stream().filter(item -> {          // Avoid complex inline logic
    // 20 lines of code here
});
```

---

## 📋 **Quick Reference Cheat Sheet**

| Operation | Syntax | Example |
|-----------|--------|----------|
| **Filter** | `filter(Predicate)` | `filter(s -> s.length() > 3)` |
| **Map** | `map(Function)` | `map(String::toUpperCase)` |
| **FlatMap** | `flatMap(Function)` | `flatMap(List::stream)` |
| **Collect** | `collect(Collector)` | `collect(Collectors.toList())` |
| **Reduce** | `reduce(BinaryOperator)` | `reduce(Integer::sum)` |
| **Optional** | `Optional.of(value)` | `Optional.ofNullable(getValue())` |
| **Lambda** | `(params) -> expression` | `(a, b) -> a + b` |

---

**Next:** [Java Version Evolution](../14-Java-Versions/README.md)"