# Java 9 Features - Platform Modularity Revolution

## 🎯 **Overview**

Java 9 introduced the most significant change to the Java platform since Java 5: **Project Jigsaw** - the module system. This release fundamentally changed how Java applications are structured, deployed, and executed.

---

## 📚 **Major Features**

### **1. Module System (Project Jigsaw) - JEP 261**

The module system introduces a new level of abstraction above packages, enabling:
- **Strong encapsulation** of implementation details
- **Reliable configuration** with explicit dependencies
- **Scalable platform** with modular JDK

#### **Module Basics**
```java
// module-info.java
module com.example.myapp {
    // Declare dependencies
    requires java.base;           // Implicit, always present
    requires java.logging;
    requires transitive java.sql; // Transitive dependency
    
    // Export packages
    exports com.example.myapp.api;
    exports com.example.myapp.util to com.example.client;
    
    // Provide services
    provides com.example.service.MyService 
        with com.example.service.impl.MyServiceImpl;
    
    // Use services
    uses com.example.service.ExternalService;
    
    // Reflection access
    opens com.example.myapp.model to com.fasterxml.jackson.databind;
}
```

#### **Module Types**
```java
// Named module (explicit module-info.java)
module my.library {
    exports my.library.api;
}

// Automatic module (JAR without module-info.java on module path)
// Module name derived from JAR file name

// Unnamed module (classpath)
// For backward compatibility
```

### **2. JShell - Interactive Java REPL - JEP 222**

```bash
$ jshell
|  Welcome to JShell -- Version 9
|  For an introduction type: /help intro

jshell> int x = 10;
x ==> 10

jshell> String greeting = \"Hello, Java 9!\";
greeting ==> \"Hello, Java 9!\"

jshell> System.out.println(greeting + \" x=\" + x);
Hello, Java 9! x=10

jshell> /vars
|    int x = 10
|    String greeting = \"Hello, Java 9!\"

jshell> /methods

jshell> /imports
|    import java.io.*
|    import java.math.*
|    import java.net.*
|    import java.nio.file.*
|    import java.util.*
|    import java.util.concurrent.*
|    import java.util.function.*
|    import java.util.prefs.*
|    import java.util.regex.*
|    import java.util.stream.*
```

### **3. Collection Factory Methods - JEP 269**

#### **Immutable Collections**
```java
// Before Java 9
List<String> list = new ArrayList<>();
list.add(\"a\");
list.add(\"b\");
list.add(\"c\");
List<String> immutableList = Collections.unmodifiableList(list);

// Java 9 - Concise factory methods
List<String> list = List.of(\"a\", \"b\", \"c\");
Set<Integer> set = Set.of(1, 2, 3, 4, 5);
Map<String, Integer> map = Map.of(
    \"key1\", 1,
    \"key2\", 2,
    \"key3\", 3
);

// For larger maps
Map<String, Integer> largeMap = Map.ofEntries(
    Map.entry(\"key1\", 1),
    Map.entry(\"key2\", 2),
    Map.entry(\"key3\", 3)
);
```

### **4. Stream API Enhancements**

#### **New Stream Methods**
```java
// takeWhile() - take elements while condition is true
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);
List<Integer> result = numbers.stream()
    .takeWhile(n -> n < 5)
    .collect(Collectors.toList());
// Result: [1, 2, 3, 4]

// dropWhile() - drop elements while condition is true
List<Integer> result2 = numbers.stream()
    .dropWhile(n -> n < 5)
    .collect(Collectors.toList());
// Result: [5, 6, 7, 8]

// ofNullable() - create stream from nullable value
Stream<String> stream = Stream.ofNullable(getString()); // May return null

// iterate() with predicate
Stream.iterate(0, n -> n < 10, n -> n + 1)
    .forEach(System.out::println); // Prints 0 to 9
```

### **5. Optional Enhancements**

```java
// ifPresentOrElse() - action for both present and absent
Optional<String> optional = Optional.of(\"value\");
optional.ifPresentOrElse(
    value -> System.out.println(\"Present: \" + value),
    () -> System.out.println(\"Not present\")
);

// or() - alternative Optional if empty
Optional<String> result = optional
    .filter(s -> s.length() > 10)
    .or(() -> Optional.of(\"default\"));

// stream() - convert Optional to Stream
List<String> list = List.of(
    Optional.of(\"a\"),
    Optional.empty(),
    Optional.of(\"b\")
).stream()
 .flatMap(Optional::stream)
 .collect(Collectors.toList());
// Result: [\"a\", \"b\"]
```

---

## 🛠️ **Module System Examples**

### **Creating a Modular Application**

#### **Library Module**
```java
// src/my.library/module-info.java
module my.library {
    exports my.library.api;
    // Hide implementation packages
}

// src/my.library/my/library/api/Calculator.java
package my.library.api;

public interface Calculator {
    int add(int a, int b);
    int subtract(int a, int b);
}

// src/my.library/my/library/impl/CalculatorImpl.java
package my.library.impl; // Not exported - hidden

import my.library.api.Calculator;

public class CalculatorImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
    
    @Override
    public int subtract(int a, int b) {
        return a - b;
    }
}
```

#### **Application Module**
```java
// src/my.app/module-info.java
module my.app {
    requires my.library;
    requires java.logging;
}

// src/my.app/my/app/Main.java
package my.app;

import my.library.api.Calculator;
// import my.library.impl.CalculatorImpl; // Compile error - not accessible

public class Main {
    public static void main(String[] args) {
        // Must use service loading or factory pattern
        Calculator calc = CalculatorFactory.getInstance();
        System.out.println(\"5 + 3 = \" + calc.add(5, 3));
    }
}
```

### **Service Provider Interface**
```java
// Service provider pattern with modules
module my.library {
    exports my.library.api;
    provides my.library.api.Calculator 
        with my.library.impl.CalculatorImpl;
}

module my.app {
    requires my.library;
    uses my.library.api.Calculator;
}

// Loading services
ServiceLoader<Calculator> loader = ServiceLoader.load(Calculator.class);
Calculator calculator = loader.findFirst().orElseThrow();
```

---

## 🏗️ **Building and Running Modular Applications**

### **Compilation**
```bash
# Compile library module
javac --module-path . -d out \\n    src/my.library/module-info.java \\n    src/my.library/my/library/api/*.java \\n    src/my.library/my/library/impl/*.java

# Compile application module
javac --module-path out -d out \\n    src/my.app/module-info.java \\n    src/my.app/my/app/*.java
```

### **Execution**
```bash
# Run modular application
java --module-path out --module my.app/my.app.Main

# Or with custom module path
java --module-path out:lib --module my.app/my.app.Main
```

### **Creating Runtime Images**
```bash
# Create custom runtime image with jlink
jlink --module-path $JAVA_HOME/jmods:out \\n    --add-modules my.app \\n    --output myapp-runtime

# Run with custom runtime
./myapp-runtime/bin/java --module my.app/my.app.Main
```

---

## 📊 **Migration Strategies**

### **Bottom-Up Migration**
1. **Start with leaf dependencies** (libraries with no dependencies)
2. **Add module-info.java** to each JAR
3. **Move up the dependency tree** gradually
4. **Convert application last**

### **Top-Down Migration**
1. **Start with application** module
2. **Use automatic modules** for dependencies
3. **Gradually convert dependencies** to explicit modules

### **Mixed Approach**
```java
// Application can mix module path and classpath
java --module-path modular-libs \\n     --class-path legacy-libs \\n     --module my.app/my.app.Main
```

---

## 🎓 **Best Practices**

### **Module Design**
1. **Keep modules cohesive** - related functionality together
2. **Minimize dependencies** - reduce coupling
3. **Export minimal API** - hide implementation details
4. **Use services** for loose coupling
5. **Follow naming conventions** - reverse domain names

### **Migration Tips**
1. **Start small** - begin with new projects
2. **Use automatic modules** for gradual migration
3. **Test thoroughly** - module boundaries enforce encapsulation
4. **Monitor performance** - modules can impact startup time
5. **Plan dependencies** - avoid circular dependencies

---

## ⚠️ **Common Pitfalls**

### **Module Path vs Classpath**
```java
// Problem: Mixing module path and classpath incorrectly
// Solution: Understand when each is needed

// Automatic modules (on module path)
// - JAR without module-info.java
// - Module name derived from JAR name

// Unnamed module (on classpath)  
// - For backward compatibility
// - Can access all modules
```

### **Reflection Issues**
```java
// Problem: Reflection access denied
module my.module {
    // Solution: Open packages for reflection
    opens my.package to framework.module;
    // Or open entire module
    open module my.module {
        // All packages open for deep reflection
    }
}
```

---

**Next:** [Java 10 Features](../14.2-Java10/README.md)"