# Module 13: Java 8 Features Deep Dive

## 🎯 **Learning Objectives**

By the end of this module, you will:
- **Master Lambda Expressions**: Write concise functional-style code with lambdas
- **Stream API Expertise**: Process collections efficiently with functional operations
- **Optional Usage**: Handle null values safely and elegantly
- **Method References**: Use method references for cleaner code
- **Default Methods**: Understand interface evolution without breaking compatibility
- **Date/Time API**: Work with the modern java.time package
- **Functional Interfaces**: Create and use functional interfaces effectively

---

## 📚 **Topics Covered**

### **13.1 Lambda Expressions**
- **Syntax and Structure**: Lambda syntax, parameter types, return values
- **Functional Interfaces**: Single Abstract Method (SAM) interfaces
- **Capture and Scope**: Variable capture, effectively final, scope rules
- **Performance**: Understanding lambda performance characteristics

### **13.2 Stream API Mastery**
- **Stream Creation**: From collections, arrays, generators, files
- **Intermediate Operations**: filter, map, flatMap, distinct, sorted
- **Terminal Operations**: collect, reduce, forEach, findFirst, anyMatch
- **Parallel Streams**: Leveraging multi-core processing safely
- **Custom Collectors**: Building domain-specific collection strategies

### **13.3 Optional Pattern**
- **Creation and Usage**: of, ofNullable, empty methods
- **Transformation**: map, flatMap, filter operations on Optional
- **Value Extraction**: orElse, orElseGet, orElseThrow patterns
- **Best Practices**: When to use Optional and common anti-patterns

### **13.4 Method References**
- **Static Method References**: Class::staticMethod
- **Instance Method References**: object::instanceMethod
- **Constructor References**: Class::new
- **Arbitrary Object Method References**: Class::instanceMethod

### **13.5 Default and Static Methods in Interfaces**
- **Default Methods**: Providing default implementations
- **Static Methods**: Utility methods in interfaces
- **Multiple Inheritance**: Resolving conflicts and diamond problem
- **Interface Evolution**: Adding methods without breaking existing code

### **13.6 New Date and Time API**
- **Core Classes**: LocalDate, LocalTime, LocalDateTime, ZonedDateTime
- **Formatting and Parsing**: DateTimeFormatter patterns
- **Time Calculations**: Duration, Period, temporal arithmetic
- **Time Zone Handling**: ZoneId, ZoneOffset, daylight saving time

---

## 🛠️ **Practical Examples**

### **Lambda Expressions in Action**
```java
// Before Java 8
Collections.sort(people, new Comparator<Person>() {
    @Override
    public int compare(Person p1, Person p2) {
        return p1.getAge().compareTo(p2.getAge());
    }
});

// Java 8 with Lambda
Collections.sort(people, (p1, p2) -> p1.getAge().compareTo(p2.getAge()));

// Even more concise with method reference
Collections.sort(people, Comparator.comparing(Person::getAge));
```

### **Stream API Power**
```java
// Complex data processing pipeline
List<String> result = employees.stream()
    .filter(emp -> emp.getSalary() > 50000)
    .filter(emp -> emp.getDepartment().equals(\"Engineering\"))
    .map(Employee::getName)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());

// Grouping and aggregation
Map<Department, Double> avgSalaryByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.averagingDouble(Employee::getSalary)));
```

### **Optional Best Practices**
```java
// Safe null handling
Optional<User> user = userService.findById(userId);
String displayName = user
    .map(User::getName)
    .map(String::toUpperCase)
    .orElse(\"Unknown User\");

// Chaining Optional operations
Optional<Address> address = user
    .flatMap(User::getAddress)
    .filter(addr -> addr.getCountry().equals(\"USA\"));
```

---

## 🔧 **Advanced Patterns**

### **Custom Functional Interfaces**
```java
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
    
    // Default method for composition
    default <W> TriFunction<T, U, V, W> andThen(Function<? super R, ? extends W> after) {
        return (t, u, v) -> after.apply(apply(t, u, v));
    }
}
```

### **Custom Collectors**
```java
// Custom collector for statistical analysis
Collector<Employee, ?, EmployeeStatistics> employeeStats = Collector.of(
    EmployeeStatistics::new,
    EmployeeStatistics::accumulate,
    EmployeeStatistics::combine,
    Collector.Characteristics.UNORDERED
);
```

### **Parallel Processing Patterns**
```java
// CPU-intensive processing with parallel streams
long count = largeDataSet.parallelStream()
    .filter(this::isValid)
    .mapToLong(this::complexCalculation)
    .sum();

// Custom thread pool for parallel streams
ForkJoinPool customThreadPool = new ForkJoinPool(4);
try {
    long result = customThreadPool.submit(() ->
        data.parallelStream()
            .mapToLong(this::process)
            .sum()
    ).get();
} finally {
    customThreadPool.shutdown();
}
```

---

## 📊 **Performance Considerations**

### **Lambda Performance**
- **Method References**: Often faster than equivalent lambdas
- **Capture Costs**: Capturing variables has performance implications
- **Primitive Specializations**: Use IntStream, LongStream, DoubleStream for primitives

### **Stream Performance**
```java
// Efficient primitive streaming
int sum = IntStream.range(0, 1_000_000)
    .filter(i -> i % 2 == 0)
    .sum(); // Much faster than boxing to Integer

// Lazy evaluation benefits
Optional<String> first = veryLargeList.stream()
    .filter(expensive::predicate)
    .findFirst(); // Stops at first match, doesn't process entire list
```

### **Parallel Stream Guidelines**
```java
// Good candidates for parallel processing
- Large datasets (>10,000 elements)
- CPU-intensive operations
- Independent operations (no shared mutable state)

// Poor candidates
- Small datasets
- I/O bound operations
- Operations with significant synchronization
```

---

## 🎓 **Best Practices**

### **Lambda Expressions**
1. **Keep lambdas short** - prefer method references for complex logic
2. **Avoid side effects** - lambdas should be pure functions when possible
3. **Use appropriate functional interfaces** - Predicate, Function, Consumer, Supplier
4. **Consider performance** - method references often outperform lambdas

### **Stream API**
1. **Prefer streams over traditional loops** for collection processing
2. **Use parallel streams judiciously** - measure performance impact
3. **Leverage collectors** - use built-in collectors before creating custom ones
4. **Chain operations efficiently** - combine filter operations, use primitive streams

### **Optional Usage**
1. **Never use Optional as field type** - it's designed for return values
2. **Don't use Optional.get()** without checking isPresent()
3. **Prefer functional style** - use map, flatMap, orElse instead of imperative checks
4. **Use Optional.empty()** instead of null returns

### **Date/Time API**
1. **Use appropriate classes** - LocalDate for dates, Instant for timestamps
2. **Handle time zones properly** - use ZonedDateTime for zone-aware operations
3. **Use immutable operations** - all date/time operations return new instances
4. **Prefer ISO formats** - use standard ISO date/time formats when possible

---

## 🔗 **Migration Guide**

### **From Anonymous Classes to Lambdas**
```java
// Before: Anonymous class
Button button = new Button();
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(\"Button clicked!\");
    }
});

// After: Lambda expression
Button button = new Button();
button.addActionListener(e -> System.out.println(\"Button clicked!\"));
```

### **From Imperative to Functional**
```java
// Before: Imperative style
List<String> result = new ArrayList<>();
for (Person person : people) {
    if (person.getAge() >= 18) {
        String name = person.getName().toUpperCase();
        result.add(name);
    }
}
Collections.sort(result);

// After: Functional style
List<String> result = people.stream()
    .filter(person -> person.getAge() >= 18)
    .map(Person::getName)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());
```

---

## 🔗 **What's Next?**

After mastering Java 8 features, you'll be ready for:
- **[Module 14: Java Version Evolution](../14-Java-Versions/README.md)** - Features added in Java 9 through 21+
- **[Module 15: Joda-Time Deep Dive](../15-Joda-Time/README.md)** - Advanced date/time manipulation
- **Reactive Programming** - RxJava and reactive streams
- **Functional Programming Patterns** - Advanced functional design patterns

---

## 📁 **Module Structure**

```
13-Java8-Features/
├── README.md              # This comprehensive guide
├── Notes.md              # Quick reference and syntax guide
├── examples/             # Hands-on Java 8 examples
│   ├── LambdaExpressions.java
│   ├── StreamAPIDemo.java
│   ├── OptionalPatterns.java
│   ├── MethodReferences.java
│   ├── DateTimeAPIExamples.java
│   └── FunctionalInterfaces.java
└── exercises/            # Practice exercises and challenges
    └── README.md         # Detailed exercise instructions
```

---

**🚀 Ready to embrace functional programming with Java 8? Let's dive into lambda expressions!**"