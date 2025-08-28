# Advanced Topics - Quick Notes

## 🎯 **Key Advanced Features**

### **Generics** 
- **Type Safety**: Compile-time type checking
- **Type Erasure**: Generic info removed at runtime
- **Wildcards**: `?`, `? extends T`, `? super T`
- **Bounded Parameters**: `<T extends Number>`

### **Lambda Expressions**
- **Functional Programming**: Functions as first-class citizens
- **Syntax**: `(parameters) -> expression`
- **Functional Interfaces**: Single abstract method interfaces
- **Method References**: `Class::method`

### **Streams API**
- **Declarative**: Focus on what, not how
- **Lazy Evaluation**: Operations executed on terminal operation
- **Parallel Processing**: Easy parallel execution
- **Immutable**: Original data unchanged

---

## 🔧 **Generics**

### **Generic Classes**
```java
public class Box<T> {
    private T value;
    
    public void set(T value) { this.value = value; }
    public T get() { return value; }
}

Box<String> stringBox = new Box<>();
Box<Integer> intBox = new Box<>();
```

### **Generic Methods**
```java
public static <T> void swap(T[] array, int i, int j) {
    T temp = array[i];
    array[i] = array[j];
    array[j] = temp;
}
```

### **Bounded Type Parameters**
```java
public class NumberBox<T extends Number> {
    private T value;
    
    public double getDoubleValue() {
        return value.doubleValue(); // Can call Number methods
    }
}
```

### **Wildcards**
```java
// Upper bound wildcard
List<? extends Number> numbers = new ArrayList<Integer>();

// Lower bound wildcard  
List<? super Integer> integers = new ArrayList<Number>();

// Unbounded wildcard
List<?> unknownList = new ArrayList<String>();
```

---

## 🚀 **Lambda Expressions**

### **Basic Syntax**
```java
// No parameters
() -> System.out.println("Hello")

// One parameter  
x -> x * 2

// Multiple parameters
(x, y) -> x + y

// Block body
(x, y) -> {
    int result = x + y;
    return result;
}
```

### **Functional Interfaces**
```java
// Predicate<T> - test condition
Predicate<String> isEmpty = String::isEmpty;
Predicate<Integer> isPositive = x -> x > 0;

// Function<T,R> - transform input to output  
Function<String, Integer> length = String::length;
Function<Integer, String> toString = Object::toString;

// Consumer<T> - accept input, no output
Consumer<String> print = System.out::println;

// Supplier<T> - provide output, no input
Supplier<Double> random = Math::random;

// BiFunction<T,U,R> - two inputs, one output
BiFunction<Integer, Integer, Integer> add = (x, y) -> x + y;
```

### **Method References**
```java
// Static method reference
Function<String, Integer> parseInt = Integer::parseInt;

// Instance method reference  
Function<String, String> toUpper = String::toUpperCase;

// Constructor reference
Supplier<ArrayList<String>> listSupplier = ArrayList::new;
```

---

## 🌊 **Streams API**

### **Creating Streams**
```java
// From collection
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> stream = list.stream();

// From array
String[] array = {"a", "b", "c"};
Stream<String> stream = Arrays.stream(array);

// Generate streams
Stream<Integer> numbers = Stream.iterate(1, n -> n + 1);
Stream<Double> randoms = Stream.generate(Math::random);
```

### **Intermediate Operations**
```java
list.stream()
    .filter(s -> s.length() > 3)      // Keep only long strings
    .map(String::toUpperCase)          // Transform to uppercase
    .distinct()                        // Remove duplicates
    .sorted()                          // Sort elements
    .limit(10)                         // Take first 10
    .skip(5)                          // Skip first 5
    .peek(System.out::println)        // Debug/side effects
```

### **Terminal Operations**
```java
// Collect results
List<String> result = stream.collect(Collectors.toList());
Map<String, Integer> map = stream.collect(
    Collectors.toMap(s -> s, String::length));

// Aggregations
long count = stream.count();
Optional<String> max = stream.max(String::compareTo);
Optional<String> first = stream.findFirst();

// Reductions
String joined = stream.collect(Collectors.joining(", "));
int sum = numbers.stream().mapToInt(Integer::intValue).sum();
```

### **Parallel Streams**
```java
// Sequential
list.stream().map(this::process).collect(Collectors.toList());

// Parallel (automatically uses multiple cores)
list.parallelStream().map(this::process).collect(Collectors.toList());
```

---

## ⭐ **Optional Class**

### **Creating Optional**
```java
Optional<String> empty = Optional.empty();
Optional<String> value = Optional.of("Hello");
Optional<String> nullable = Optional.ofNullable(getString());
```

### **Using Optional**
```java
// Check if present
if (optional.isPresent()) {
    System.out.println(optional.get());
}

// Better approach
optional.ifPresent(System.out::println);

// Provide default values
String result = optional.orElse("Default");
String result2 = optional.orElseGet(() -> computeDefault());

// Chain operations
String result = optional
    .filter(s -> s.length() > 5)
    .map(String::toUpperCase)
    .orElse("DEFAULT");
```

---

## 📝 **Annotations**

### **Built-in Annotations**
```java
@Override           // Method overrides parent
@Deprecated         // Marks as deprecated  
@SuppressWarnings  // Suppress compiler warnings
@FunctionalInterface // Mark as functional interface
```

### **Custom Annotations**
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    String value() default "";
    int timeout() default 0;
}

// Usage
@Test(value = "testLogin", timeout = 5000)
public void testUserLogin() {
    // Test code
}
```

### **Processing Annotations**
```java
Method[] methods = TestClass.class.getDeclaredMethods();
for (Method method : methods) {
    if (method.isAnnotationPresent(Test.class)) {
        Test test = method.getAnnotation(Test.class);
        System.out.println("Test: " + test.value());
    }
}
```

---

## 🕒 **Date/Time API (Java 8+)**

### **Core Classes**
```java
LocalDate date = LocalDate.now();           // 2023-12-25
LocalTime time = LocalTime.now();           // 14:30:22
LocalDateTime dateTime = LocalDateTime.now(); // 2023-12-25T14:30:22
ZonedDateTime zoned = ZonedDateTime.now();  // With timezone

// Parsing
LocalDate parsed = LocalDate.parse("2023-12-25");
LocalDateTime parsed2 = LocalDateTime.parse("2023-12-25T14:30:22");
```

### **Date Operations**  
```java
LocalDate today = LocalDate.now();
LocalDate tomorrow = today.plusDays(1);
LocalDate nextWeek = today.plusWeeks(1);
LocalDate lastMonth = today.minusMonths(1);

// Calculations
Period period = Period.between(start, end);
Duration duration = Duration.between(startTime, endTime);
```

### **Formatting**
```java
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
String formatted = dateTime.format(formatter);
LocalDateTime parsed = LocalDateTime.parse("2023-12-25 14:30:22", formatter);
```

---

## ⚡ **Quick Reference**

| Feature | Syntax | Use Case |
|---------|---------|----------|
| **Generic Class** | `class Box<T>` | Type-safe containers |
| **Generic Method** | `<T> T method(T t)` | Type-safe methods |
| **Lambda** | `x -> x * 2` | Functional programming |
| **Method Reference** | `String::length` | Concise method calls |
| **Stream Filter** | `.filter(x -> x > 0)` | Data filtering |
| **Stream Map** | `.map(String::toUpperCase)` | Data transformation |
| **Optional** | `Optional.ofNullable(value)` | Null safety |
| **Annotation** | `@Override` | Metadata |

---

## 💡 **Best Practices**

### **Generics**
✅ Use generics for type safety  
✅ Prefer `List<String>` over raw `List`  
✅ Use wildcards for flexibility  
❌ Avoid raw types  

### **Lambdas & Streams**  
✅ Use method references when possible  
✅ Keep lambdas short and readable  
✅ Use parallel streams for CPU-intensive tasks  
❌ Don't use streams for simple iterations  

### **Optional**
✅ Use Optional for return values that might be null  
✅ Chain operations with map/filter  
❌ Don't use Optional for fields or parameters  

---

## 🔧 **Modern Java Features**

### **Local Variable Type Inference (Java 10+)**
```java
var list = new ArrayList<String>(); // Type inferred
var map = Map.of("key", "value");
```

### **Text Blocks (Java 15+)**
```java
String json = """
    {
        "name": "John",
        "age": 30
    }
    """;
```

### **Records (Java 14+)**
```java
public record Person(String name, int age) {}
// Automatically generates constructor, getters, equals, hashCode, toString
```

---

**Next:** [Best Practices](../11-Best-Practices/README.md)