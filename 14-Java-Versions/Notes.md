# Java Version Evolution - Quick Notes

## 🎯 **Java Version Timeline**

### **Java 9 (September 2017)**
- **Module System (Jigsaw)**: `module-info.java`, `requires`, `exports`
- **JShell**: Interactive REPL tool
- **Collection Factory Methods**: `List.of()`, `Set.of()`, `Map.of()`
- **Stream API**: `takeWhile()`, `dropWhile()`, `ofNullable()`
- **Optional**: `ifPresentOrElse()`, `or()`, `stream()`

### **Java 10 (March 2018)**
- **Local Variable Type Inference**: `var` keyword
- **Unmodifiable Collections**: `Collectors.toUnmodifiableList()`
- **Optional.orElseThrow()**: No-argument version
- **Performance**: G1GC improvements, parallel full GC

### **Java 11 (September 2018) - LTS**
- **HTTP Client API**: Standard `java.net.http` package
- **String Methods**: `isBlank()`, `lines()`, `strip()`, `repeat()`
- **Files Methods**: `readString()`, `writeString()`
- **Lambda**: `var` in lambda parameters
- **Nest-Based Access Control**: Better reflection support

### **Java 12 (March 2019)**
- **Switch Expressions** (Preview): Enhanced switch with expressions
- **String Methods**: `indent()`, `transform()`
- **Collectors**: `teeing()` collector
- **JVM**: Microbenchmark suite, G1 improvements

### **Java 13 (September 2019)**
- **Text Blocks** (Preview): Multi-line string literals `"""`
- **Switch Expressions** (Second Preview)
- **Socket API**: New NIO-based implementation
- **ZGC**: Uncommit unused memory

### **Java 14 (March 2020)**
- **Records** (Preview): `record Person(String name, int age) {}`
- **Switch Expressions**: Standardized (no longer preview)
- **Pattern Matching**: `instanceof` pattern matching (preview)
- **Text Blocks** (Second Preview)
- **Helpful NPEs**: Better NullPointerException messages

### **Java 15 (September 2020)**
- **Sealed Classes** (Preview): `sealed class Shape permits Circle, Rectangle`
- **Text Blocks**: Standardized feature
- **Records** (Second Preview)
- **Pattern Matching** (Second Preview)
- **Hidden Classes**: For dynamic class generation

### **Java 16 (March 2021)**
- **Records**: Standardized feature
- **Pattern Matching**: `instanceof` standardized
- **Sealed Classes** (Second Preview)
- **Vector API** (Incubator): SIMD operations
- **Foreign Linker API** (Incubator): Native code integration

### **Java 17 (September 2021) - LTS**
- **Sealed Classes**: Standardized feature
- **Pattern Matching**: Enhanced capabilities
- **Foreign Function & Memory API** (Incubator)
- **Context-Specific Deserialization Filters**
- **Strong Encapsulation**: JDK internals strongly encapsulated

### **Java 18 (March 2022)**
- **Simple Web Server**: `jwebserver` command
- **UTF-8 by Default**: Default charset is UTF-8
- **Code Snippets in JavaDoc**: `@snippet` tag
- **Vector API** (Second Incubator)

### **Java 19 (September 2022)**
- **Virtual Threads** (Preview): Lightweight threads
- **Pattern Matching for switch** (Preview): Enhanced switch patterns
- **Structured Concurrency** (Incubator)
- **Foreign Function & Memory API** (Preview)

### **Java 20 (March 2023)**
- **Scoped Values** (Incubator): Alternative to ThreadLocal
- **Record Patterns** (Preview): Destructuring records in patterns
- **Pattern Matching for switch** (Second Preview)
- **Virtual Threads** (Second Preview)

### **Java 21 (September 2023) - LTS**
- **Virtual Threads**: Standardized feature
- **Pattern Matching for switch**: Standardized
- **Sequenced Collections**: New collection interfaces
- **Record Patterns**: Standardized
- **String Templates** (Preview)

---

## 🚀 **Key Features by Category**

### **Language Features**
```java
// Java 9: Collection Factories
List<String> list = List.of("a", "b", "c");
Set<Integer> set = Set.of(1, 2, 3);
Map<String, Integer> map = Map.of("a", 1, "b", 2);

// Java 10: var keyword
var name = "John";        // String
var numbers = List.of(1, 2, 3);  // List<Integer>

// Java 11: String methods
String text = "  hello world  ";
boolean blank = text.isBlank();     // false
String stripped = text.strip();     // "hello world"
String repeated = "Ha".repeat(3);   // "HaHaHa"

// Java 14: Records
record Person(String name, int age) {}
Person person = new Person("Alice", 30);

// Java 14: Pattern matching for instanceof
if (obj instanceof String s) {
    System.out.println(s.toUpperCase());
}

// Java 15: Text blocks
String html = """
    <html>
        <body>
            <h1>Hello World</h1>
        </body>
    </html>
    """;

// Java 17: Sealed classes
sealed class Shape permits Circle, Rectangle {}
final class Circle extends Shape {}
final class Rectangle extends Shape {}

// Java 21: Pattern matching for switch
String result = switch (shape) {
    case Circle(var radius) -> "Circle with radius " + radius;
    case Rectangle(var width, var height) -> "Rectangle " + width + "x" + height;
};
```

### **API Enhancements**
```java
// Java 9: Stream API
Stream.of(1, 2, 3, 4, 5)
    .takeWhile(n -> n < 4)  // [1, 2, 3]
    .collect(Collectors.toList());

// Java 11: HTTP Client
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com"))
    .build();
HttpResponse<String> response = client.send(request, 
    HttpResponse.BodyHandlers.ofString());

// Java 11: Files methods
String content = Files.readString(Path.of("file.txt"));
Files.writeString(Path.of("output.txt"), "Hello World");

// Java 12: String methods
String indented = "Hello".indent(4);     // "    Hello"
String transformed = "hello".transform(String::toUpperCase); // "HELLO"

// Java 16: Stream.toList()
List<String> result = stream
    .filter(s -> s.length() > 3)
    .toList();  // No need for .collect(Collectors.toList())
```

---

## 🏗️ **Module System (Java 9+)**

### **Basic Module Structure**
```java
// module-info.java
module com.example.myapp {
    requires java.base;           // Implicit
    requires java.logging;
    requires transitive java.sql;
    
    exports com.example.api;
    exports com.example.util to com.example.client;
    
    provides com.example.service.MyService 
        with com.example.service.impl.MyServiceImpl;
    
    uses com.example.service.ExternalService;
}
```

### **Module Commands**
```bash
# Compile modules
javac --module-path mods -d mods src/module-info.java src/com/example/*.java

# Run modular application
java --module-path mods --module com.example.myapp/com.example.Main

# Create runtime image
jlink --module-path mods --add-modules com.example.myapp --output myapp-runtime
```

---

## 🎯 **Migration Guide**

### **Java 8 → Java 11**
```java
// Replace deprecated APIs
// Old: sun.misc.BASE64Encoder
// New: java.util.Base64
String encoded = Base64.getEncoder().encodeToString("Hello".getBytes());

// Use new String methods
String text = "  hello  ";
String clean = text.strip();  // Better than trim() for Unicode

// HTTP Client migration
// Old: HttpUrlConnection or third-party library
// New: java.net.http.HttpClient
HttpClient client = HttpClient.newHttpClient();
```

### **Java 11 → Java 17**
```java
// Use Records for data classes
// Old: Verbose class with getters/setters
// New: Concise record
record Point(int x, int y) {}

// Pattern matching for instanceof
// Old: Cast after instanceof check
if (obj instanceof String) {
    String s = (String) obj;
    // use s
}

// New: Pattern matching
if (obj instanceof String s) {
    // use s directly
}

// Sealed classes for controlled inheritance
sealed interface Expr permits ConstantExpr, PlusExpr, TimesExpr {}
```

### **Java 17 → Java 21**
```java
// Virtual Threads
// Old: Platform threads
ExecutorService executor = Executors.newFixedThreadPool(100);

// New: Virtual threads
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

// Pattern matching for switch
// Old: Multiple instanceof checks
String result;
if (obj instanceof Integer i) {
    result = "Integer: " + i;
} else if (obj instanceof String s) {
    result = "String: " + s;
}

// New: Pattern matching switch
String result = switch (obj) {
    case Integer i -> "Integer: " + i;
    case String s -> "String: " + s;
    default -> "Unknown";
};
```

---

## ⚡ **Performance Improvements**

### **JVM Enhancements by Version**
- **Java 9**: G1GC improvements, Compact Strings
- **Java 10**: Parallel Full GC for G1, Class-Data Sharing
- **Java 11**: ZGC (experimental), Epsilon GC
- **Java 12**: Shenandoah GC, G1 improvements
- **Java 14**: NUMA-aware G1, JFR event streaming
- **Java 15**: ZGC production ready
- **Java 17**: Context-specific deserialization filters
- **Java 21**: Generational ZGC, Virtual thread optimizations

### **Startup Performance**
```bash
# Class Data Sharing (Java 10+)
java -Xshare:dump  # Create CDS archive
java -Xshare:on MyApp  # Use CDS archive

# Application Class Data Sharing (Java 10+)
java -XX:+UseAppCDS -XX:SharedArchiveFile=app.jsa MyApp
```

---

## 🎓 **Adoption Recommendations**

### **LTS Version Strategy**
- **Java 8**: Legacy support (extended support until 2030)
- **Java 11**: First LTS after Java 8, widely adopted
- **Java 17**: Current standard LTS for new projects
- **Java 21**: Latest LTS with modern features

### **Feature Adoption Priority**
1. **High Priority**: Records, Pattern Matching, HTTP Client, var
2. **Medium Priority**: Sealed Classes, Text Blocks, Module System
3. **Advanced**: Virtual Threads, Foreign Function API

---

**Next:** [Joda-Time Deep Dive](../15-Joda-Time/README.md)