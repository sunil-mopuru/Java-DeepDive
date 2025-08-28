# Module 14.2: Java 10 Features

## 🎯 **Overview**

Java 10 (March 2018) was a groundbreaking release that introduced **Local Variable Type Inference** with the `var` keyword, making Java code more concise while maintaining type safety. This module explores all Java 10 features with practical examples and best practices.

---

## 🚀 **Key Features**

### **🔥 Major Features**
- **Local Variable Type Inference (`var`)**: Reduce boilerplate code
- **Unmodifiable Collections**: Enhanced collection creation
- **Optional.orElseThrow()**: Simplified exception handling
- **Performance Improvements**: G1GC and JVM optimizations

### **📈 Performance & JVM**
- **G1 Parallel Full GC**: Improved garbage collection
- **Heap Allocation on Alternative Memory**: Better memory management
- **Thread-Local Handshakes**: Reduced stop-the-world pauses

---

## 📚 **Detailed Feature Guide**

### **1. Local Variable Type Inference (`var`)**

The most significant feature of Java 10 - type inference for local variables.

#### **Basic Usage**
```java
// Before Java 10 - verbose declarations
List<String> names = new ArrayList<String>();
Map<String, List<Integer>> complexMap = new HashMap<String, List<Integer>>();

// Java 10 - concise with var
var names = new ArrayList<String>();
var complexMap = new HashMap<String, List<Integer>>();
```

#### **Best Practices**
```java
// ✅ GOOD - Clear context
var users = userService.getAllUsers();
var config = new DatabaseConfiguration();
var stream = Files.lines(Paths.get("data.txt"));

// ❌ AVOID - Unclear types
var data = getData();           // What type is returned?
var flag = isValid();          // Boolean? Could be anything
var result = process();        // Too generic
```

#### **Restrictions and Rules**
```java
public class VarExamples {
    // ✅ Valid usage
    public void validUsage() {
        var name = "John";                    // String
        var age = 25;                         // int
        var price = 19.99;                    // double
        var numbers = List.of(1, 2, 3);       // List<Integer>
        
        // Loop variables
        for (var item : collection) { }
        for (var i = 0; i < 10; i++) { }
    }
    
    // ❌ Invalid usage - these won't compile
    public void invalidUsage() {
        // var field;                         // Fields not allowed
        // var methodParam(var param) { }     // Parameters not allowed
        // var method() { return null; }      // Return types not allowed
        // var x = null;                      // Cannot infer from null
        // var arr = {1, 2, 3};              // Array initializer not allowed
        // var lambda = () -> "hello";        // Lambda needs explicit type
    }
}
```

### **2. Unmodifiable Collections Enhancement**

Improved methods for creating unmodifiable collections.

```java
import java.util.stream.Collectors;

public class UnmodifiableCollections {
    public void demonstrateCollectors() {
        List<String> mutableList = Arrays.asList("a", "b", "c");
        
        // Java 10 - Direct unmodifiable collection
        List<String> unmodifiableList = mutableList.stream()
            .collect(Collectors.toUnmodifiableList());
            
        Set<String> unmodifiableSet = mutableList.stream()
            .collect(Collectors.toUnmodifiableSet());
            
        Map<String, Integer> unmodifiableMap = mutableList.stream()
            .collect(Collectors.toUnmodifiableMap(
                Function.identity(),
                String::length
            ));
        
        // These will throw UnsupportedOperationException
        // unmodifiableList.add("d");
        // unmodifiableSet.remove("a");
        // unmodifiableMap.put("key", 1);
    }
}
```

### **3. Optional Enhancement**

Simplified `orElseThrow()` method without parameters.

```java
public class OptionalEnhancements {
    public void demonstrateOrElseThrow() {
        Optional<String> optional = getOptionalValue();
        
        // Java 8 way
        String value1 = optional.orElseThrow(() -> new RuntimeException("No value"));
        
        // Java 10 way - simpler
        String value2 = optional.orElseThrow(); // Throws NoSuchElementException
        
        // Practical usage in service methods
        User user = userRepository.findById(userId).orElseThrow();
        Order order = orderService.getOrder(orderId).orElseThrow();
    }
    
    private Optional<String> getOptionalValue() {
        return Optional.of("Hello Java 10");
    }
}
```

### **4. Performance Improvements**

#### **G1 Parallel Full GC**
```java
// JVM Flag to enable G1GC with parallel full GC
// -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions
```

#### **Heap Allocation on Alternative Memory**
```java
// JVM can now allocate heap on alternative memory devices
// Useful for large memory systems
// -XX:AllocateHeapAt=<path>
```

---

## 🛠️ **Practical Examples**

### **Database Service with var**
```java
public class UserService {
    public List<User> getActiveUsers() {
        var query = "SELECT * FROM users WHERE active = true";
        var connection = dataSource.getConnection();
        var statement = connection.prepareStatement(query);
        var resultSet = statement.executeQuery();
        
        var users = new ArrayList<User>();
        while (resultSet.next()) {
            var user = new User(
                resultSet.getString("name"),
                resultSet.getInt("age"),
                resultSet.getString("email")
            );
            users.add(user);
        }
        
        return users.stream()
            .collect(Collectors.toUnmodifiableList());
    }
}
```

### **Configuration Management**
```java
public class ConfigurationManager {
    public void processConfiguration() {
        var properties = loadProperties();
        var serverConfig = extractServerConfig(properties);
        var databaseConfig = extractDatabaseConfig(properties);
        
        // Type is clearly inferred from method names
        var validatedConfig = validateConfiguration(serverConfig, databaseConfig);
        var activeEnvironment = determineEnvironment(validatedConfig);
        
        initializeServices(activeEnvironment);
    }
    
    private Properties loadProperties() { /* implementation */ }
    private ServerConfig extractServerConfig(Properties props) { /* implementation */ }
    private DatabaseConfig extractDatabaseConfig(Properties props) { /* implementation */ }
}
```

### **Stream Processing with var**
```java
public class DataProcessor {
    public void processOrderData() {
        var orders = orderRepository.findAll();
        
        var totalRevenue = orders.stream()
            .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
            .mapToDouble(Order::getTotal)
            .sum();
        
        var topCustomers = orders.stream()
            .collect(Collectors.groupingBy(
                Order::getCustomerId,
                Collectors.summingDouble(Order::getTotal)
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toUnmodifiableMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
        
        generateReport(totalRevenue, topCustomers);
    }
}
```

---

## ⚡ **Best Practices**

### **When to Use `var`**
- ✅ **Clear context**: Method names provide type information
- ✅ **Long generic types**: Reduce verbosity
- ✅ **Local scope**: Variable usage is immediately clear
- ✅ **Iteration**: Loop variables with obvious types

### **When NOT to Use `var`**
- ❌ **Unclear context**: Type is not obvious
- ❌ **Primitive operations**: `int`, `boolean` are short anyway
- ❌ **Public APIs**: Explicit types improve readability
- ❌ **Complex expressions**: Multiple method chains

### **Code Style Guidelines**
```java
public class VarStyleGuide {
    public void goodExamples() {
        // Good - factory methods make type clear
        var users = userService.getAllActiveUsers();
        var config = ConfigurationBuilder.newBuilder().build();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Good - constructor calls are explicit
        var connection = new DatabaseConnection(url, username, password);
        var processor = new OrderProcessor(config, validator);
        
        // Good - well-named variables
        var customerOrderHistory = getCustomerOrderHistory(customerId);
        var paymentValidationResult = validatePayment(payment);
    }
    
    public void avoidableExamples() {
        // Acceptable but not ideal - primitive types are short
        var count = 0;              // int count = 0; is just as clear
        var isValid = true;         // boolean isValid = true; is clearer
        
        // Avoid - unclear what getData returns
        var data = getData();
        
        // Avoid - generic method names don't help
        var result = process();
        var value = calculate();
    }
}
```

---

## 🔄 **Migration Guide**

### **Upgrading from Java 9**
1. **Gradual adoption**: Start using `var` in new code
2. **Refactor gradually**: Update existing verbose declarations
3. **Team guidelines**: Establish `var` usage standards
4. **IDE support**: Configure IDE for better `var` support

### **Common Migration Patterns**
```java
// Migration Pattern 1: Generic Collections
// Before
Map<String, List<CustomerOrder>> customerOrders = new HashMap<>();
List<String> activeUserNames = new ArrayList<>();

// After
var customerOrders = new HashMap<String, List<CustomerOrder>>();
var activeUserNames = new ArrayList<String>();

// Migration Pattern 2: Stream Operations
// Before
List<ProcessedOrder> processedOrders = orders.stream()
    .filter(order -> order.isValid())
    .map(this::processOrder)
    .collect(Collectors.toList());

// After
var processedOrders = orders.stream()
    .filter(Order::isValid)
    .map(this::processOrder)
    .collect(Collectors.toUnmodifiableList());
```

---

## 🧪 **Testing with Java 10 Features**

```java
@Test
public void testVarWithMockObjects() {
    // Setup
    var mockUserService = mock(UserService.class);
    var testUser = new User("John", 25, "john@test.com");
    when(mockUserService.findById(1L)).thenReturn(Optional.of(testUser));
    
    // Execute
    var userController = new UserController(mockUserService);
    var result = userController.getUser(1L);
    
    // Verify
    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo("John");
}
```

---

## 📋 **Quick Reference**

### **var Usage Rules**
| Scenario | Valid | Example |
|----------|-------|---------|
| **Local Variables** | ✅ | `var name = "John";` |
| **Loop Variables** | ✅ | `for (var item : list)` |
| **Try-with-resources** | ✅ | `try (var stream = ...)` |
| **Fields** | ❌ | `private var field;` |
| **Parameters** | ❌ | `method(var param)` |
| **Return Types** | ❌ | `var method() { }` |
| **Null Assignment** | ❌ | `var x = null;` |

### **Collector Methods (Java 10)**
```java
// New unmodifiable collectors
.collect(Collectors.toUnmodifiableList())
.collect(Collectors.toUnmodifiableSet())
.collect(Collectors.toUnmodifiableMap(keyMapper, valueMapper))
```

---

## 🎯 **What You'll Learn**

After completing this module, you'll understand:
- **Type Inference**: When and how to use `var` effectively
- **Code Clarity**: Writing more readable code with less boilerplate
- **Performance**: Java 10 JVM improvements and their impact
- **Best Practices**: Industry-standard `var` usage guidelines
- **Migration**: Smooth transition strategies from earlier Java versions

---

## 📚 **Prerequisites**

- **Java 9 Features**: Collection factories and module system basics
- **Lambda Expressions**: Java 8 functional programming
- **Stream API**: Collection processing with streams
- **Generics**: Understanding of Java generic types

---

**🚀 Ready to make your Java code more concise with type inference? Let's explore Java 10's powerful features!**

**Next:** [Java 11 Features](../14.3-Java11/README.md) | **Previous:** [Java 9 Features](../14.1-Java9/README.md)