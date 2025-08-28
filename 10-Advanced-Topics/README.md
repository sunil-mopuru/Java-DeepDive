# Chapter 10: Advanced Topics

## 📚 Table of Contents
1. [Generics](#generics)
2. [Lambda Expressions](#lambda-expressions)
3. [Stream API](#stream-api)
4. [Optional Class](#optional-class)
5. [Annotations](#annotations)
6. [Reflection](#reflection)
7. [Best Practices](#best-practices)

---

## Generics

### **What are Generics?**
Generics enable types (classes and interfaces) to be parameters when defining classes, interfaces, and methods.

### **Benefits:**
- **Type Safety** - Compile-time type checking
- **Elimination of Casts** - No need for explicit casting
- **Code Reusability** - Same code works with different types

### **Generic Classes:**
```java
// Generic class definition
public class Box<T> {
    private T content;
    
    public void set(T content) {
        this.content = content;
    }
    
    public T get() {
        return content;
    }
}

// Usage
Box<String> stringBox = new Box<>();
stringBox.set("Hello");
String value = stringBox.get(); // No casting needed

Box<Integer> intBox = new Box<>();
intBox.set(42);
Integer number = intBox.get();
```

### **Generic Methods:**
```java
public class Utility {
    // Generic method
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    // Multiple type parameters
    public static <T, U> void print(T first, U second) {
        System.out.println("First: " + first + ", Second: " + second);
    }
}

// Usage
String[] names = {"Alice", "Bob", "Charlie"};
Utility.swap(names, 0, 2); // Alice and Charlie swapped

Integer[] numbers = {1, 2, 3};
Utility.swap(numbers, 0, 1); // 1 and 2 swapped

Utility.print("Hello", 42); // T=String, U=Integer
```

### **Bounded Type Parameters:**
```java
// Upper bound - T must extend Number
public class NumberBox<T extends Number> {
    private T value;
    
    public void setValue(T value) {
        this.value = value;
    }
    
    public double getDoubleValue() {
        return value.doubleValue(); // Can call Number methods
    }
}

// Usage
NumberBox<Integer> intBox = new NumberBox<>();
NumberBox<Double> doubleBox = new NumberBox<>();
// NumberBox<String> stringBox = new NumberBox<>(); // Compile error!

// Multiple bounds
public class BoundedBox<T extends Number & Comparable<T>> {
    // T must extend Number AND implement Comparable
}
```

### **Wildcards:**
```java
import java.util.*;

public class WildcardExamples {
    
    // Upper bounded wildcard (? extends)
    public static double sum(List<? extends Number> numbers) {
        double total = 0.0;
        for (Number n : numbers) {
            total += n.doubleValue();
        }
        return total;
    }
    
    // Lower bounded wildcard (? super)
    public static void addNumbers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }
    
    // Unbounded wildcard (?)
    public static void printList(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }
}

// Usage
List<Integer> integers = Arrays.asList(1, 2, 3);
List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);

double intSum = WildcardExamples.sum(integers);   // Works
double doubleSum = WildcardExamples.sum(doubles); // Works

List<Number> numbers = new ArrayList<>();
WildcardExamples.addNumbers(numbers); // Can add integers to Number list
```

---

## Lambda Expressions

### **What are Lambda Expressions?**
Lambda expressions provide a concise way to represent functional interfaces (interfaces with single abstract method).

### **Syntax:**
```java
// Basic syntax: (parameters) -> expression
(int x, int y) -> x + y

// Single parameter (parentheses optional)
x -> x * 2

// No parameters
() -> System.out.println("Hello")

// Multiple statements (braces required)
(x, y) -> {
    int sum = x + y;
    return sum * 2;
}
```

### **Functional Interfaces:**
```java
@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b);
}

public class LambdaExample {
    public static void main(String[] args) {
        // Lambda expressions
        Calculator add = (a, b) -> a + b;
        Calculator multiply = (a, b) -> a * b;
        Calculator subtract = (a, b) -> a - b;
        
        System.out.println(add.calculate(5, 3));      // 8
        System.out.println(multiply.calculate(5, 3)); // 15
        System.out.println(subtract.calculate(5, 3)); // 2
    }
}
```

### **Built-in Functional Interfaces:**
```java
import java.util.function.*;

public class BuiltInFunctionalInterfaces {
    public static void main(String[] args) {
        
        // Predicate<T> - boolean test(T t)
        Predicate<String> isEmpty = s -> s.isEmpty();
        Predicate<Integer> isEven = n -> n % 2 == 0;
        
        System.out.println(isEmpty.test(""));     // true
        System.out.println(isEven.test(4));      // true
        
        // Function<T, R> - R apply(T t)
        Function<String, Integer> length = s -> s.length();
        Function<Integer, String> toString = n -> "Number: " + n;
        
        System.out.println(length.apply("Hello")); // 5
        System.out.println(toString.apply(42));    // "Number: 42"
        
        // Consumer<T> - void accept(T t)
        Consumer<String> print = s -> System.out.println("Output: " + s);
        print.accept("Hello World"); // Output: Hello World
        
        // Supplier<T> - T get()
        Supplier<Double> random = () -> Math.random();
        System.out.println(random.get()); // Random number
        
        // BiFunction<T, U, R> - R apply(T t, U u)
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println(add.apply(3, 4)); // 7
    }
}
```

### **Method References:**
```java
import java.util.*;

public class MethodReferences {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        
        // Static method reference
        names.forEach(System.out::println); // Equivalent to: s -> System.out.println(s)
        
        // Instance method reference
        String prefix = "Hello ";
        Function<String, String> greeter = prefix::concat; // Equivalent to: s -> prefix.concat(s)
        
        // Constructor reference
        Supplier<List<String>> listSupplier = ArrayList::new; // Equivalent to: () -> new ArrayList<>()
        
        // Method reference on arbitrary object
        List<String> words = Arrays.asList("apple", "banana", "cherry");
        words.sort(String::compareToIgnoreCase); // Equivalent to: (a, b) -> a.compareToIgnoreCase(b)
    }
}
```

---

## Stream API

### **What is Stream API?**
Stream API provides a functional approach to processing collections of objects.

### **Stream Characteristics:**
- **Not a data structure** - Pipeline of operations
- **Functional in nature** - Operations don't modify source
- **Lazy evaluation** - Computed only when needed
- **Possibly unbounded** - Can work with infinite data

### **Creating Streams:**
```java
import java.util.stream.*;
import java.util.*;

// From collections
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> stream1 = list.stream();
Stream<String> parallelStream = list.parallelStream();

// From arrays
String[] array = {"x", "y", "z"};
Stream<String> stream2 = Arrays.stream(array);

// Using Stream.of()
Stream<Integer> stream3 = Stream.of(1, 2, 3, 4, 5);

// Infinite streams
Stream<Double> randomNumbers = Stream.generate(Math::random);
Stream<Integer> evenNumbers = Stream.iterate(0, n -> n + 2);

// From ranges
IntStream range = IntStream.range(1, 11); // 1 to 10
IntStream rangeClosed = IntStream.rangeClosed(1, 10); // 1 to 10 inclusive
```

### **Intermediate Operations:**
```java
List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry");

// Filter - keep elements matching predicate
List<String> longWords = words.stream()
    .filter(word -> word.length() > 5)
    .collect(Collectors.toList()); // [banana, cherry, elderberry]

// Map - transform elements
List<Integer> lengths = words.stream()
    .map(String::length)
    .collect(Collectors.toList()); // [5, 6, 6, 4, 10]

// FlatMap - flatten nested structures
List<List<String>> nested = Arrays.asList(
    Arrays.asList("a", "b"),
    Arrays.asList("c", "d", "e")
);
List<String> flattened = nested.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList()); // [a, b, c, d, e]

// Distinct - remove duplicates
List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3);
List<Integer> unique = numbers.stream()
    .distinct()
    .collect(Collectors.toList()); // [1, 2, 3]

// Sorted - sort elements
List<String> sorted = words.stream()
    .sorted()
    .collect(Collectors.toList()); // [apple, banana, cherry, date, elderberry]

// Limit and Skip
List<String> limited = words.stream()
    .limit(3)
    .collect(Collectors.toList()); // [apple, banana, cherry]

List<String> skipped = words.stream()
    .skip(2)
    .collect(Collectors.toList()); // [cherry, date, elderberry]
```

### **Terminal Operations:**
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// forEach - perform action on each element
numbers.stream().forEach(System.out::println);

// collect - gather results into collection
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());

Map<Boolean, List<Integer>> partitioned = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));

// reduce - combine elements
int sum = numbers.stream()
    .reduce(0, Integer::sum); // 55

Optional<Integer> max = numbers.stream()
    .reduce(Integer::max); // Optional[10]

// find operations
Optional<Integer> first = numbers.stream()
    .filter(n -> n > 5)
    .findFirst(); // Optional[6]

Optional<Integer> any = numbers.stream()
    .filter(n -> n > 5)
    .findAny(); // Optional[6] (or any matching element)

// match operations
boolean anyMatch = numbers.stream().anyMatch(n -> n > 8);    // true
boolean allMatch = numbers.stream().allMatch(n -> n > 0);    // true
boolean noneMatch = numbers.stream().noneMatch(n -> n < 0);  // true

// count
long count = numbers.stream()
    .filter(n -> n % 2 == 0)
    .count(); // 5
```

### **Advanced Stream Operations:**
```java
// Grouping
List<String> words = Arrays.asList("apple", "apricot", "banana", "blueberry", "cherry");

Map<Integer, List<String>> groupedByLength = words.stream()
    .collect(Collectors.groupingBy(String::length));
// {5=[apple], 6=[banana, cherry], 7=[apricot], 9=[blueberry]}

Map<Character, List<String>> groupedByFirst = words.stream()
    .collect(Collectors.groupingBy(word -> word.charAt(0)));
// {a=[apple, apricot], b=[banana, blueberry], c=[cherry]}

// Parallel processing
List<Integer> largeList = IntStream.range(1, 1_000_000)
    .boxed()
    .collect(Collectors.toList());

// Sequential processing
long sequentialSum = largeList.stream()
    .mapToLong(Integer::longValue)
    .sum();

// Parallel processing
long parallelSum = largeList.parallelStream()
    .mapToLong(Integer::longValue)
    .sum();
```

---

## Optional Class

### **What is Optional?**
Optional is a container that may or may not contain a value, helping to avoid NullPointerException.

### **Creating Optional:**
```java
import java.util.Optional;

// Empty Optional
Optional<String> empty = Optional.empty();

// Optional with value
Optional<String> optional = Optional.of("Hello");

// Optional that may be null
String nullableString = null;
Optional<String> nullable = Optional.ofNullable(nullableString);
```

### **Using Optional:**
```java
Optional<String> optional = Optional.of("Hello World");

// Check if value is present
if (optional.isPresent()) {
    System.out.println("Value: " + optional.get());
}

// Safer approach - use ifPresent
optional.ifPresent(value -> System.out.println("Value: " + value));

// Get with default value
String value1 = optional.orElse("Default");
String value2 = optional.orElseGet(() -> "Generated Default");

// Throw exception if empty
String value3 = optional.orElseThrow(() -> new RuntimeException("No value"));

// Transform value if present
Optional<Integer> length = optional.map(String::length);
Optional<String> upperCase = optional
    .filter(s -> s.length() > 5)
    .map(String::toUpperCase);

// Chaining optionals
Optional<String> result = optional
    .filter(s -> s.contains("World"))
    .map(s -> s.replace("World", "Universe"));
```

### **Optional in Practice:**
```java
public class UserRepository {
    private Map<Long, User> users = new HashMap<>();
    
    // Return Optional instead of null
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
    
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
            .filter(user -> email.equals(user.getEmail()))
            .findFirst();
    }
}

// Usage
UserRepository repo = new UserRepository();

// Safe handling
repo.findById(1L)
    .ifPresentOrElse(
        user -> System.out.println("Found: " + user.getName()),
        () -> System.out.println("User not found")
    );

// Chaining operations
String userName = repo.findById(1L)
    .map(User::getName)
    .orElse("Unknown User");
```

---

## Annotations

### **What are Annotations?**
Annotations provide metadata about the program that doesn't affect program execution.

### **Built-in Annotations:**
```java
public class AnnotationExamples {
    
    @Override
    public String toString() {
        return "AnnotationExample";
    }
    
    @Deprecated(since = "1.5", forRemoval = true)
    public void oldMethod() {
        // This method is deprecated
    }
    
    @SuppressWarnings("unchecked")
    public void methodWithWarnings() {
        List list = new ArrayList(); // Raw type warning suppressed
    }
    
    @SafeVarargs
    public final <T> void safeVarargsMethod(T... args) {
        // Safe use of varargs
    }
}
```

### **Custom Annotations:**
```java
import java.lang.annotation.*;

// Custom annotation definition
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Benchmark {
    String value() default "";
    int iterations() default 1;
}

// Usage
public class ServiceClass {
    
    @Benchmark(value = "Fast operation", iterations = 1000)
    public void quickOperation() {
        // Implementation
    }
    
    @Benchmark("Slow operation")
    public void slowOperation() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

---

## Reflection

### **What is Reflection?**
Reflection allows inspection and manipulation of classes, methods, and fields at runtime.

### **Basic Reflection:**
```java
import java.lang.reflect.*;

public class ReflectionExample {
    
    public static void analyzeClass(Class<?> clazz) {
        System.out.println("Class: " + clazz.getName());
        
        // Get constructors
        Constructor<?>[] constructors = clazz.getConstructors();
        System.out.println("Constructors: " + constructors.length);
        
        // Get methods
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            System.out.println("Method: " + method.getName());
        }
        
        // Get fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("Field: " + field.getName() + 
                             " (" + field.getType().getSimpleName() + ")");
        }
    }
    
    public static void main(String[] args) {
        analyzeClass(String.class);
        analyzeClass(ArrayList.class);
    }
}
```

### **Dynamic Object Creation:**
```java
public class DynamicCreation {
    
    public static <T> T createInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create instance", e);
        }
    }
    
    public static void main(String[] args) {
        // Create objects dynamically
        ArrayList<String> list = createInstance(ArrayList.class);
        HashMap<String, Object> map = createInstance(HashMap.class);
        
        System.out.println("Created: " + list.getClass().getSimpleName());
        System.out.println("Created: " + map.getClass().getSimpleName());
    }
}
```

---

## Best Practices

### **Generics Best Practices:**
```java
// Use bounded wildcards for flexibility
public static double sum(List<? extends Number> numbers) { }

// Use descriptive type parameter names
public class Repository<EntityType, IdType> { }

// Prefer generic methods over generic classes when possible
public static <T> List<T> singletonList(T item) {
    return Arrays.asList(item);
}
```

### **Lambda Best Practices:**
```java
// Keep lambdas short and readable
list.stream()
    .filter(item -> item.isActive())
    .map(Item::getName)
    .collect(Collectors.toList());

// Use method references when possible
list.forEach(System.out::println); // Instead of: item -> System.out.println(item)

// Avoid side effects in lambdas
// Bad: list.stream().forEach(item -> counter++);
// Good: int count = (int) list.stream().count();
```

### **Stream Best Practices:**
```java
// Use parallel streams for CPU-intensive operations on large datasets
largeList.parallelStream()
    .filter(this::expensiveOperation)
    .collect(Collectors.toList());

// Prefer specialized streams for primitives
IntStream.range(1, 100)
    .sum(); // More efficient than Stream<Integer>

// Close streams that use resources
try (Stream<String> lines = Files.lines(Paths.get("file.txt"))) {
    return lines.filter(line -> line.contains("keyword"))
                .collect(Collectors.toList());
}
```

---

## Key Takeaways

### **Essential Advanced Concepts:**
✅ **Generics** - Type safety and code reusability  
✅ **Lambda Expressions** - Functional programming in Java  
✅ **Stream API** - Functional data processing  
✅ **Optional** - Null safety and better error handling  
✅ **Annotations** - Metadata and code documentation  
✅ **Reflection** - Runtime class inspection and manipulation  

### **Modern Java Development:**
- **Embrace functional programming** with lambdas and streams
- **Use Optional** to handle null values gracefully
- **Leverage generics** for type-safe collections and methods
- **Apply annotations** for better code documentation
- **Use reflection judiciously** - prefer compile-time solutions

### **Performance Considerations:**
- **Parallel streams** for CPU-intensive operations on large data
- **Specialized primitive streams** (IntStream, LongStream, DoubleStream)
- **Method references** often more efficient than lambdas
- **Avoid excessive boxing/unboxing** in streams

### **Next Steps:**
- Practice with [exercises](./exercises/)
- Explore reactive programming with frameworks like RxJava
- Move to [Best Practices and Design Patterns](../11-Best-Practices/README.md)

---

**Continue to: [Chapter 11: Best Practices →](../11-Best-Practices/README.md)**