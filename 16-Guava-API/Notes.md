# Guava API Quick Reference

## 🚀 **Quick Setup**
```xml
<!-- Maven -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>32.1.3-jre</version>
</dependency>
```

## 📋 **Essential Imports**
```java
// Collections
import com.google.common.collect.*;

// Base utilities
import com.google.common.base.*;

// I/O utilities
import com.google.common.io.*;

// Caching
import com.google.common.cache.*;

// Concurrent utilities
import com.google.common.util.concurrent.*;

// Math utilities
import com.google.common.math.*;

// Primitives
import com.google.common.primitives.*;
```

---

## 🗂️ **Collections Cheat Sheet**

### **Creating Collections**
```java
// Lists
Lists.newArrayList()
Lists.newArrayList("a", "b", "c")
Lists.newLinkedList()

// Sets  
Sets.newHashSet()
Sets.newHashSet("a", "b", "c")
Sets.newLinkedHashSet()
Sets.newTreeSet()

// Maps
Maps.newHashMap()
Maps.newLinkedHashMap()
Maps.newTreeMap()
Maps.newConcurrentMap()
```

### **Immutable Collections**
```java
// Immutable Lists
ImmutableList.of()
ImmutableList.of("a", "b", "c")
ImmutableList.copyOf(collection)
ImmutableList.builder().add("a").build()

// Immutable Sets
ImmutableSet.of("a", "b", "c")
ImmutableSet.copyOf(collection)

// Immutable Maps
ImmutableMap.of("key1", "value1", "key2", "value2")
ImmutableMap.builder().put("key", "value").build()
```

### **Collection Operations**
```java
// Filtering
Collections2.filter(collection, predicate)
Iterables.filter(iterable, predicate)

// Transforming
Collections2.transform(collection, function)
Iterables.transform(iterable, function)

// Set Operations
Sets.union(set1, set2)
Sets.intersection(set1, set2)
Sets.difference(set1, set2)
Sets.symmetricDifference(set1, set2)
```

---

## 🔗 **String Processing Cheat Sheet**

### **Joiner**
```java
// Basic joining
Joiner.on(", ").join(collection)
Joiner.on(", ").skipNulls().join(collection)
Joiner.on(", ").useForNull("N/A").join(collection)

// Map joining
Joiner.on(", ").withKeyValueSeparator("=").join(map)
```

### **Splitter**
```java
// Basic splitting
Splitter.on(',').split(string)
Splitter.on(',').omitEmptyStrings().split(string)
Splitter.on(',').trimResults().split(string)
Splitter.on(',').limit(3).split(string)

// Map splitting
Splitter.on(',').withKeyValueSeparator('=').split(string)
```

### **CharMatcher**
```java
// Predefined matchers
CharMatcher.whitespace().removeFrom(string)
CharMatcher.javaDigit().retainFrom(string)
CharMatcher.javaLetter().retainFrom(string)

// Custom matchers
CharMatcher.anyOf("aeiou").removeFrom(string)
CharMatcher.is('x').replaceFrom(string, 'y')
CharMatcher.inRange('a', 'z').matchesAllOf(string)
```

---

## 💾 **Caching Cheat Sheet**

### **Simple Cache**
```java
Cache<String, String> cache = CacheBuilder.newBuilder()
    .maximumSize(1000)
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build();

cache.put("key", "value");
String value = cache.getIfPresent("key");
```

### **Loading Cache**
```java
LoadingCache<String, String> cache = CacheBuilder.newBuilder()
    .maximumSize(1000)
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build(CacheLoader.from(key -> loadValue(key)));

String value = cache.get("key"); // Auto-loads if missing
```

### **Cache Configuration Options**
```java
CacheBuilder.newBuilder()
    .maximumSize(1000)                    // Max entries
    .maximumWeight(100000)                // Max weight
    .weigher((key, value) -> value.size()) // Custom weigher
    .expireAfterWrite(10, TimeUnit.MINUTES)   // Write expiry
    .expireAfterAccess(5, TimeUnit.MINUTES)   // Access expiry
    .refreshAfterWrite(1, TimeUnit.MINUTES)   // Refresh time
    .removalListener(notification -> {})      // Removal listener
    .recordStats()                        // Enable stats
    .build();
```

---

## ⚡ **Functional Programming Cheat Sheet**

### **Optional**
```java
// Creating Optional
Optional.of(value)
Optional.absent()
Optional.fromNullable(nullableValue)

// Using Optional
optional.isPresent()
optional.get()
optional.or(defaultValue)
optional.orNull()
optional.transform(function)
```

### **Predicates**
```java
// Basic predicates
Predicates.notNull()
Predicates.isNull()
Predicates.equalTo(value)

// Combining predicates
Predicates.and(predicate1, predicate2)
Predicates.or(predicate1, predicate2)
Predicates.not(predicate)
```

### **Functions**
```java
// Function composition
Functions.compose(function1, function2)
Functions.identity()
Functions.constant(value)
Functions.forMap(map, defaultValue)
```

---

## 🔄 **Concurrent Utilities Cheat Sheet**

### **ListenableFuture**
```java
// Creating executor
ListeningExecutorService executor = MoreExecutors.listeningDecorator(
    Executors.newFixedThreadPool(10)
);

// Submit task
ListenableFuture<String> future = executor.submit(() -> "result");

// Add callback
Futures.addCallback(future, new FutureCallback<String>() {
    public void onSuccess(String result) { /* handle success */ }
    public void onFailure(Throwable t) { /* handle failure */ }
}, executor);
```

### **Future Utilities**
```java
// Transform future
Futures.transform(future, function, executor)

// Combine futures
Futures.allAsList(future1, future2, future3)
Futures.successfulAsList(future1, future2, future3)

// Get with timeout
Futures.getWithTimeout(future, timeout, timeUnit)
```

---

## 📁 **I/O Utilities Cheat Sheet**

### **Files**
```java
// Read file
Files.asCharSource(file, charset).read()
Files.asCharSource(file, charset).readLines()

// Write file
Files.asCharSink(file, charset).write(content)

// Copy file
Files.copy(fromFile, toFile)

// File operations
Files.move(from, to)
Files.equal(file1, file2)
Files.getFileExtension(filename)
Files.getNameWithoutExtension(filename)
```

### **Resources**
```java
// Read from classpath
Resources.toString(url, charset)
Resources.readLines(url, charset)
Resources.getResource("filename")
```

### **Streams**
```java
// Byte streams
ByteStreams.copy(inputStream, outputStream)
ByteStreams.toByteArray(inputStream)

// Character streams  
CharStreams.toString(reader)
CharStreams.readLines(reader)
CharStreams.copy(reader, writer)
```

---

## 🔢 **Math Utilities Cheat Sheet**

### **IntMath**
```java
IntMath.gcd(a, b)               // Greatest common divisor
IntMath.factorial(n)            // Factorial
IntMath.isPowerOfTwo(n)         // Power of two check
IntMath.checkedAdd(a, b)        // Safe addition
IntMath.checkedMultiply(a, b)   // Safe multiplication
IntMath.pow(base, exp)          // Power function
IntMath.sqrt(n, roundingMode)   // Square root
```

### **LongMath / DoubleMath**
```java
LongMath.factorial(n)
LongMath.checkedPow(base, exp)

DoubleMath.isFinite(value)
DoubleMath.roundToInt(value, roundingMode)
DoubleMath.roundToLong(value, roundingMode)
```

### **Primitive Arrays**
```java
// Ints
Ints.concat(array1, array2)
Ints.contains(array, value)
Ints.min(array) / Ints.max(array)
Ints.toArray(list)
Ints.asList(array)
Ints.join(separator, array)

// Similar methods available for Longs, Doubles, etc.
```

---

## 🛠️ **Common Patterns**

### **Null-Safe Operations**
```java
// Instead of null checks
String result = Objects.firstNonNull(str1, str2);
String safe = MoreObjects.firstNonNull(nullable, "default");

// Null-safe equals
Objects.equal(obj1, obj2)  // handles nulls

// Null-safe hashCode  
Objects.hashCode(obj1, obj2, obj3)

// Null-safe toString
MoreObjects.toStringHelper(this)
    .add("field1", value1)
    .add("field2", value2)
    .toString()
```

### **Validation**
```java
// Preconditions
Preconditions.checkArgument(condition, "error message")
Preconditions.checkNotNull(object, "error message") 
Preconditions.checkState(condition, "error message")
Preconditions.checkElementIndex(index, size, "error message")
```

### **Comparison**
```java
// ComparisonChain for complex comparisons
public int compareTo(Person other) {
    return ComparisonChain.start()
        .compare(lastName, other.lastName)
        .compare(firstName, other.firstName)
        .compare(age, other.age)
        .result();
}

// Ordering
Ordering<String> byLength = Ordering.natural().onResultOf(String::length);
List<String> sorted = byLength.sortedCopy(list);
```

---

## 🎯 **Best Practices**

### ✅ **Do:**
- Use immutable collections when possible
- Leverage caching for expensive operations  
- Use Optional for null safety
- Apply functional transformations for cleaner code
- Use Preconditions for validation
- Prefer Guava utilities over manual implementations

### ❌ **Don't:**
- Mix Guava Optional with Java 8+ Optional
- Ignore cache statistics and tuning
- Use mutable collections when immutable would work
- Forget to handle exceptions in cache loaders
- Use Guava for everything - standard Java is often sufficient

---

## 📊 **Performance Tips**

1. **Immutable Collections**: Pre-sized and optimized
2. **Cache Tuning**: Monitor hit rates and adjust accordingly  
3. **Lazy Loading**: Use suppliers for expensive operations
4. **Primitive Collections**: Use when working with large amounts of primitives
5. **Stream vs Transform**: Choose based on Java version and use case

---

## 🔍 **Quick Troubleshooting**

### **Common Issues:**
- **OutOfMemoryError with Cache**: Set maximum size/weight
- **NullPointerException**: Use Optional and null-safe utilities
- **Performance Issues**: Profile cache hit rates and collection usage
- **Version Conflicts**: Ensure consistent Guava version across dependencies

### **Debug Tools:**
```java
// Cache stats
CacheStats stats = cache.stats();
System.out.println("Hit rate: " + stats.hitRate());

// Collection debugging  
System.out.println("Collection type: " + collection.getClass());

// Optional debugging
System.out.println("Optional present: " + optional.isPresent());
```