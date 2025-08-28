# Module 16: Guava API - Google's Core Libraries for Java

## 🎯 **Overview**

Google Guava is a set of core Java libraries that includes new collection types, immutable collections, a graph library, functional idioms, an in-memory cache, and APIs/utilities for concurrency, I/O, hashing, primitives, reflection, string processing, and much more.

This module provides a comprehensive deep dive into Guava API from basic to advanced usage with clear explanations and practical examples.

---

## 📚 **What is Google Guava?**

Google Guava is an open-source set of common libraries for Java, developed by Google. It provides utilities that are not available in the standard Java library, making Java development more productive and less error-prone.

### **Key Benefits:**
- **Reduced Boilerplate Code**: Simplifies common programming tasks
- **Immutable Collections**: Thread-safe and performance optimized
- **Null Safety**: Better handling of null values
- **Functional Programming**: Support for functional idioms
- **Enhanced Collections**: More powerful collection utilities
- **Caching**: In-memory caching solutions
- **String Processing**: Advanced string manipulation utilities

---

## 🗂️ **Module Contents**

### **📁 Core Topics Covered**

1. **[Basic Collections](#basic-collections)**
   - Lists, Sets, and Maps utilities
   - Collection creation and manipulation
   - Comparison with standard Java collections

2. **[Immutable Collections](#immutable-collections)**
   - Creating immutable collections
   - Benefits and use cases
   - Performance considerations

3. **[Functional Programming](#functional-programming)**
   - Functions and Predicates
   - Functional transformations
   - Optional and absent values

4. **[String Processing](#string-processing)**
   - Joiner and Splitter
   - CharMatcher utilities
   - String manipulation helpers

5. **[Caching](#caching)**
   - LoadingCache and Cache
   - Cache configuration
   - Eviction policies and statistics

6. **[Concurrent Utilities](#concurrent-utilities)**
   - ListenableFuture
   - Service framework
   - Atomic utilities

7. **[I/O Utilities](#io-utilities)**
   - Files and ByteStreams
   - Resources and CharStreams
   - Closer utilities

8. **[Math and Primitives](#math-and-primitives)**
   - Mathematical utilities
   - Primitive collections
   - Hashing utilities

---

## 🚀 **Getting Started with Guava**

### **Adding Guava to Your Project**

#### **Maven Dependency:**
```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>32.1.3-jre</version>
</dependency>
```

#### **Gradle Dependency:**
```gradle
implementation 'com.google.guava:guava:32.1.3-jre'
```

### **First Guava Example:**
```java
import com.google.common.collect.Lists;
import com.google.common.base.Joiner;

public class GuavaIntroduction {
    public static void main(String[] args) {
        // Create a list with Guava
        List<String> names = Lists.newArrayList("Alice", "Bob", "Charlie");
        
        // Join strings with Guava
        String joined = Joiner.on(", ").join(names);
        System.out.println("Names: " + joined);
        // Output: Names: Alice, Bob, Charlie
    }
}
```

---

## 📋 **Basic Collections**

### **Collection Creation Utilities**

#### **Lists:**
```java
import com.google.common.collect.Lists;

// Create mutable lists
List<String> list1 = Lists.newArrayList();
List<String> list2 = Lists.newArrayList("a", "b", "c");
List<Integer> list3 = Lists.newArrayListWithCapacity(100);

// Create immutable lists  
List<String> immutableList = Lists.newArrayList("x", "y", "z");
```

#### **Sets:**
```java
import com.google.common.collect.Sets;

// Create mutable sets
Set<String> set1 = Sets.newHashSet();
Set<String> set2 = Sets.newHashSet("a", "b", "c");
Set<String> set3 = Sets.newLinkedHashSet();

// Set operations
Set<Integer> set1 = Sets.newHashSet(1, 2, 3, 4);
Set<Integer> set2 = Sets.newHashSet(3, 4, 5, 6);

Sets.SetView<Integer> union = Sets.union(set1, set2);        // {1,2,3,4,5,6}
Sets.SetView<Integer> intersection = Sets.intersection(set1, set2); // {3,4}
Sets.SetView<Integer> difference = Sets.difference(set1, set2);     // {1,2}
```

#### **Maps:**
```java
import com.google.common.collect.Maps;

// Create mutable maps
Map<String, Integer> map1 = Maps.newHashMap();
Map<String, Integer> map2 = Maps.newLinkedHashMap();
Map<String, Integer> map3 = Maps.newTreeMap();

// Transform maps
Map<String, Integer> ages = Maps.newHashMap();
ages.put("Alice", 30);
ages.put("Bob", 25);

Map<String, String> ageStrings = Maps.transformValues(ages, 
    age -> age + " years old");
```

### **Collection Utilities**

```java
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

List<String> names = Lists.newArrayList("Alice", "Bob", "Charlie", "David");

// Filtering collections
Collection<String> shortNames = Collections2.filter(names, 
    name -> name.length() <= 3);

// Transform collections  
Collection<String> upperNames = Collections2.transform(names, 
    String::toUpperCase);

// Iterables utilities
String first = Iterables.getFirst(names, "Default");
String last = Iterables.getLast(names);
int size = Iterables.size(names);
```

---

## 🔒 **Immutable Collections**

### **Creating Immutable Collections**

#### **Immutable Lists:**
```java
import com.google.common.collect.ImmutableList;

// Various ways to create immutable lists
ImmutableList<String> list1 = ImmutableList.of();
ImmutableList<String> list2 = ImmutableList.of("a", "b", "c");
ImmutableList<String> list3 = ImmutableList.copyOf(Arrays.asList("x", "y", "z"));

// Builder pattern
ImmutableList<String> list4 = ImmutableList.<String>builder()
    .add("first")
    .add("second", "third")
    .addAll(Arrays.asList("fourth", "fifth"))
    .build();
```

#### **Immutable Sets:**
```java
import com.google.common.collect.ImmutableSet;

ImmutableSet<String> set1 = ImmutableSet.of("a", "b", "c");
ImmutableSet<String> set2 = ImmutableSet.copyOf(Arrays.asList("x", "y", "z"));

ImmutableSet<String> set3 = ImmutableSet.<String>builder()
    .add("element1")
    .addAll(Arrays.asList("element2", "element3"))
    .build();
```

#### **Immutable Maps:**
```java
import com.google.common.collect.ImmutableMap;

ImmutableMap<String, Integer> map1 = ImmutableMap.of(
    "Alice", 30,
    "Bob", 25,
    "Charlie", 35
);

ImmutableMap<String, Integer> map2 = ImmutableMap.<String, Integer>builder()
    .put("key1", 1)
    .put("key2", 2)
    .putAll(existingMap)
    .build();
```

### **Benefits of Immutable Collections**

1. **Thread Safety**: No synchronization needed
2. **Performance**: Optimized for memory and speed
3. **Predictability**: Cannot be modified accidentally
4. **Hash Code Caching**: Computed once and cached

---

## ⚡ **Functional Programming**

### **Predicates and Functions**

#### **Predicates:**
```java
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

// Simple predicates
Predicate<String> isLong = s -> s.length() > 5;
Predicate<String> isNotNull = Predicates.notNull();
Predicate<String> isNotEmpty = s -> !s.isEmpty();

// Combining predicates
Predicate<String> isLongAndNotEmpty = Predicates.and(isLong, isNotEmpty);
Predicate<String> isShortOrNull = Predicates.or(
    s -> s.length() <= 5, 
    Predicates.isNull()
);
```

#### **Functions:**
```java
import com.google.common.base.Function;
import com.google.common.base.Functions;

// Simple functions
Function<String, Integer> stringLength = String::length;
Function<String, String> toUpperCase = String::toUpperCase;

// Function composition
Function<String, String> lengthToString = Functions.compose(
    Object::toString,
    String::length
);
```

### **Optional Values**

```java
import com.google.common.base.Optional;

// Creating Optional values
Optional<String> present = Optional.of("Hello");
Optional<String> absent = Optional.absent();
Optional<String> nullable = Optional.fromNullable(getString());

// Using Optional values
if (present.isPresent()) {
    System.out.println(present.get());
}

String value = nullable.or("default");
String transformed = present.transform(String::toUpperCase).or("DEFAULT");
```

---

## 🔗 **String Processing**

### **Joiner**

```java
import com.google.common.base.Joiner;

List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Basic joining
String result1 = Joiner.on(", ").join(names);
// Result: "Alice, Bob, Charlie"

// Skip nulls
List<String> withNulls = Arrays.asList("Alice", null, "Bob", "Charlie");
String result2 = Joiner.on(", ").skipNulls().join(withNulls);
// Result: "Alice, Bob, Charlie"

// Replace nulls
String result3 = Joiner.on(", ").useForNull("Unknown").join(withNulls);
// Result: "Alice, Unknown, Bob, Charlie"

// Join map entries
Map<String, Integer> ages = ImmutableMap.of("Alice", 30, "Bob", 25);
String result4 = Joiner.on(", ").withKeyValueSeparator("=").join(ages);
// Result: "Alice=30, Bob=25"
```

### **Splitter**

```java
import com.google.common.base.Splitter;

String text = "Alice,Bob,,Charlie,";

// Basic splitting
Iterable<String> parts1 = Splitter.on(',').split(text);
// Result: ["Alice", "Bob", "", "Charlie", ""]

// Omit empty strings
Iterable<String> parts2 = Splitter.on(',').omitEmptyStrings().split(text);
// Result: ["Alice", "Bob", "Charlie"]

// Trim results
String textWithSpaces = "Alice, Bob , , Charlie,";
Iterable<String> parts3 = Splitter.on(',')
    .trimResults()
    .omitEmptyStrings()
    .split(textWithSpaces);
// Result: ["Alice", "Bob", "Charlie"]

// Limit splits
Iterable<String> parts4 = Splitter.on(',').limit(3).split(text);
// Result: ["Alice", "Bob", ",Charlie,"]

// Split into map
String keyValuePairs = "Alice=30,Bob=25,Charlie=35";
Map<String, String> map = Splitter.on(',')
    .withKeyValueSeparator('=')
    .split(keyValuePairs);
```

### **CharMatcher**

```java
import com.google.common.base.CharMatcher;

String text = "Hello, World! 123";

// Predefined matchers
String lettersOnly = CharMatcher.javaLetter().retainFrom(text);
// Result: "HelloWorld"

String digitsOnly = CharMatcher.javaDigit().retainFrom(text);
// Result: "123"

// Remove whitespace
String noWhitespace = CharMatcher.whitespace().removeFrom(text);
// Result: "Hello,World!123"

// Replace characters
String replaced = CharMatcher.is('l').replaceFrom(text, 'X');
// Result: "HeXXo, WorXd! 123"

// Custom matchers
CharMatcher vowels = CharMatcher.anyOf("aeiouAEIOU");
String noVowels = vowels.removeFrom(text);
// Result: "Hll, Wrld! 123"
```

---

## 💾 **Caching**

### **Basic Cache Usage**

```java
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheLoader;

// Simple cache
Cache<String, String> cache = CacheBuilder.newBuilder()
    .maximumSize(1000)
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build();

// Manually populate cache
cache.put("key", "value");
String value = cache.getIfPresent("key");

// Loading cache
LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
    .maximumSize(1000)
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build(new CacheLoader<String, String>() {
        @Override
        public String load(String key) throws Exception {
            return fetchFromDatabase(key); // Your loading logic
        }
    });

// Automatic loading
String cachedValue = loadingCache.get("key"); // Loads if not present
```

### **Advanced Cache Configuration**

```java
LoadingCache<String, ExpensiveObject> cache = CacheBuilder.newBuilder()
    .maximumSize(10000)                    // Maximum number of entries
    .maximumWeight(100000)                 // Maximum weight of entries
    .weigher((key, value) -> value.getSize()) // Custom weigher
    .expireAfterWrite(10, TimeUnit.MINUTES)   // Expire after write
    .expireAfterAccess(5, TimeUnit.MINUTES)   // Expire after last access
    .refreshAfterWrite(1, TimeUnit.MINUTES)   // Refresh after write
    .removalListener(notification -> {         // Removal listener
        System.out.println("Removed: " + notification.getKey());
    })
    .recordStats()                         // Enable statistics
    .build(new CacheLoader<String, ExpensiveObject>() {
        @Override
        public ExpensiveObject load(String key) throws Exception {
            return createExpensiveObject(key);
        }
        
        @Override
        public ExpensiveObject reload(String key, ExpensiveObject oldValue) {
            // Asynchronous refresh
            return refreshExpensiveObject(key, oldValue);
        }
    });

// Cache statistics
CacheStats stats = cache.stats();
System.out.println("Hit rate: " + stats.hitRate());
System.out.println("Load count: " + stats.loadCount());
System.out.println("Eviction count: " + stats.evictionCount());
```

---

## 🔄 **Concurrent Utilities**

### **ListenableFuture**

```java
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Futures;

ListeningExecutorService service = MoreExecutors.listeningDecorator(
    Executors.newFixedThreadPool(10)
);

// Submit task and get ListenableFuture
ListenableFuture<String> future = service.submit(() -> {
    // Some computation
    return "Result";
});

// Add callback
Futures.addCallback(future, new FutureCallback<String>() {
    @Override
    public void onSuccess(String result) {
        System.out.println("Success: " + result);
    }
    
    @Override
    public void onFailure(Throwable t) {
        System.out.println("Failure: " + t.getMessage());
    }
}, MoreExecutors.directExecutor());

// Transform futures
ListenableFuture<Integer> lengthFuture = Futures.transform(future, 
    String::length, MoreExecutors.directExecutor());

// Combine multiple futures
ListenableFuture<String> future2 = service.submit(() -> "Other Result");
ListenableFuture<List<String>> combined = Futures.allAsList(future, future2);
```

---

## 📁 **I/O Utilities**

### **Files and Resources**

```java
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.common.io.CharStreams;

// Read entire file
File file = new File("example.txt");
String content = Files.asCharSource(file, Charsets.UTF_8).read();

// Write to file
Files.asCharSink(file, Charsets.UTF_8).write("Hello, World!");

// Copy files
File source = new File("source.txt");
File destination = new File("destination.txt");
Files.copy(source, destination);

// Read from resources
URL resource = Resources.getResource("config.properties");
String resourceContent = Resources.toString(resource, Charsets.UTF_8);

// Process lines
Files.asCharSource(file, Charsets.UTF_8)
    .readLines()
    .forEach(System.out::println);
```

### **ByteStreams and CharStreams**

```java
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

// Copy streams
InputStream input = new FileInputStream("input.txt");
OutputStream output = new FileOutputStream("output.txt");
ByteStreams.copy(input, output);

// Read entire stream
byte[] bytes = ByteStreams.toByteArray(input);

// Character streams
Reader reader = new FileReader("example.txt");
String content = CharStreams.toString(reader);

List<String> lines = CharStreams.readLines(reader);
```

---

## 🔢 **Math and Primitives**

### **Mathematical Utilities**

```java
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.math.DoubleMath;

// Integer math
int gcd = IntMath.gcd(12, 18);           // Greatest common divisor
int factorial = IntMath.factorial(5);     // 120
boolean isPowerOfTwo = IntMath.isPowerOfTwo(16); // true

// Safe arithmetic (throws on overflow)
int sum = IntMath.checkedAdd(Integer.MAX_VALUE - 1, 1);
int product = IntMath.checkedMultiply(1000, 1000);

// Long math
long bigFactorial = LongMath.factorial(20);
long bigPower = LongMath.checkedPow(10, 9);

// Double math  
boolean isFinite = DoubleMath.isFinite(3.14);
int rounded = DoubleMath.roundToInt(3.7, RoundingMode.HALF_UP);
```

### **Primitive Collections**

```java
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Doubles;

// Array utilities
int[] array1 = {1, 2, 3};
int[] array2 = {4, 5, 6};
int[] combined = Ints.concat(array1, array2); // {1, 2, 3, 4, 5, 6}

// Convert collections
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
int[] array = Ints.toArray(list);

// Find min/max
int min = Ints.min(array); // 1
int max = Ints.max(array); // 5

// Check if contains
boolean contains = Ints.contains(array, 3); // true

// Join as string
String joined = Ints.join(",", array); // "1,2,3,4,5"
```

### **Hashing**

```java
import com.google.common.hash.Hashing;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;

// Simple hashing
HashCode hash1 = Hashing.md5().hashString("Hello", Charsets.UTF_8);
HashCode hash2 = Hashing.sha256().hashString("Hello", Charsets.UTF_8);

// Incremental hashing
Hasher hasher = Hashing.sha256().newHasher();
hasher.putString("Hello", Charsets.UTF_8);
hasher.putInt(42);
hasher.putLong(123L);
HashCode finalHash = hasher.hash();

// Hash objects
Person person = new Person("Alice", 30);
HashCode personHash = Hashing.sha256().hashObject(person, (person, sink) -> {
    sink.putString(person.getName(), Charsets.UTF_8);
    sink.putInt(person.getAge());
});
```

---

## 🎯 **Best Practices and Common Patterns**

### **1. Prefer Immutable Collections**
```java
// Good: Immutable and thread-safe
public class UserService {
    private final ImmutableList<String> validRoles = 
        ImmutableList.of("ADMIN", "USER", "GUEST");
    
    public boolean isValidRole(String role) {
        return validRoles.contains(role);
    }
}
```

### **2. Use Guava for Null Safety**
```java
// Good: Explicit null handling
public Optional<User> findUser(String id) {
    User user = database.findById(id);
    return Optional.fromNullable(user);
}

public String getUserName(String userId) {
    return findUser(userId)
        .transform(User::getName)
        .or("Unknown User");
}
```

### **3. Leverage Caching for Performance**
```java
public class ExpensiveService {
    private final LoadingCache<String, Result> cache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(new CacheLoader<String, Result>() {
            @Override
            public Result load(String key) throws Exception {
                return performExpensiveOperation(key);
            }
        });
    
    public Result getResult(String key) {
        return cache.getUnchecked(key);
    }
}
```

### **4. Use Functional Style for Collections**
```java
// Transform and filter in functional style
List<String> names = getUsers().stream()
    .filter(user -> user.isActive())
    .map(User::getName)
    .collect(toImmutableList());

// Use Guava's utilities for older Java versions
Collection<String> names = Collections2.transform(
    Collections2.filter(users, User::isActive),
    User::getName
);
```

---

## 📝 **Module Structure**

```
16-Guava-API/
├── README.md                 # This comprehensive guide
├── Notes.md                 # Quick reference and cheat sheet
├── examples/
│   ├── GuavaBasicsExamples.java
│   ├── CollectionsExamples.java
│   ├── ImmutableCollectionsExamples.java
│   ├── FunctionalExamples.java
│   ├── StringProcessingExamples.java
│   ├── CachingExamples.java
│   ├── ConcurrentUtilsExamples.java
│   ├── IOUtilitiesExamples.java
│   └── MathAndPrimitivesExamples.java
└── exercises/
    └── README.md            # Practice exercises and projects
```

---

## 🎯 **Learning Objectives**

By the end of this module, you will be able to:

1. ✅ **Understand Guava fundamentals** and when to use it over standard Java
2. ✅ **Create and manipulate collections** efficiently using Guava utilities  
3. ✅ **Build immutable collections** for thread-safe and performant code
4. ✅ **Apply functional programming patterns** with Guava's utilities
5. ✅ **Process strings effectively** using Joiner, Splitter, and CharMatcher
6. ✅ **Implement caching solutions** for performance optimization
7. ✅ **Use concurrent utilities** for asynchronous programming
8. ✅ **Handle I/O operations** efficiently with Guava's I/O utilities
9. ✅ **Apply mathematical operations** and work with primitive collections
10. ✅ **Follow best practices** for production Guava usage

---

## 🔗 **Additional Resources**

- **[Official Guava Documentation](https://github.com/google/guava/wiki)**
- **[Guava JavaDoc](https://guava.dev/releases/32.1.3-jre/api/docs/)**
- **[Guava GitHub Repository](https://github.com/google/guava)**
- **[Guava Release Notes](https://github.com/google/guava/releases)**

---

## ➡️ **Next Steps**

After mastering Guava API, consider exploring:
- **Apache Commons Libraries** - Alternative utility libraries
- **Vavr (Javaslang)** - Functional programming for Java
- **Project Reactor** - Reactive programming
- **Spring Framework** - Enterprise Java development

---

**🚀 Ready to supercharge your Java development with Google Guava? Let's dive into the examples and start building more efficient, readable, and maintainable Java applications!**