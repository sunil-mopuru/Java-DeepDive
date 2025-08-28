# Java 10 Features - Quick Notes

## 🎯 **Local Variable Type Inference (`var`)**

### **Basic Syntax**
```java
// Declaration with var
var variableName = initializer;

// Examples
var name = "John Doe";                    // String
var age = 30;                             // int
var price = 19.99;                        // double
var list = new ArrayList<String>();       // ArrayList<String>
var map = Map.of("key", "value");         // Map<String, String>
```

### **Valid Usage Scenarios**
```java
// Local variables
var message = "Hello World";
var numbers = Arrays.asList(1, 2, 3, 4, 5);

// Loop variables
for (var item : collection) {
    System.out.println(item);
}

for (var i = 0; i < 10; i++) {
    // process
}

// Try-with-resources
try (var scanner = new Scanner(file)) {
    // use scanner
}

// Complex generic types
var complexMap = new HashMap<String, List<CustomerOrder>>();
var processor = new OrderProcessor<Customer, Payment>();
```

### **Invalid Usage (Compilation Errors)**
```java
// ❌ Cannot be used for fields
public class Example {
    var field = "value";  // Compilation error
}

// ❌ Cannot be used for method parameters
public void method(var parameter) { }  // Compilation error

// ❌ Cannot be used for return types
public var getResult() { }  // Compilation error

// ❌ Cannot infer from null
var value = null;  // Compilation error

// ❌ Cannot use with array initializers
var array = {1, 2, 3};  // Compilation error

// ❌ Cannot use without initializer
var x;  // Compilation error
```

---

## 🔧 **Unmodifiable Collections**

### **New Collector Methods**
```java
import static java.util.stream.Collectors.*;

List<String> mutable = Arrays.asList("a", "b", "c");

// Collect to unmodifiable collections
List<String> immutableList = mutable.stream()
    .collect(toUnmodifiableList());

Set<String> immutableSet = mutable.stream()
    .collect(toUnmodifiableSet());

Map<String, Integer> immutableMap = mutable.stream()
    .collect(toUnmodifiableMap(
        Function.identity(),
        String::length
    ));
```

### **Comparison with Previous Approaches**
```java
// Java 8/9 approach
List<String> oldWay = mutable.stream()
    .collect(collectingAndThen(toList(), Collections::unmodifiableList));

// Java 10 approach - simpler
List<String> newWay = mutable.stream()
    .collect(toUnmodifiableList());
```

---

## 💎 **Optional Enhancement**

### **orElseThrow() Without Parameter**
```java
Optional<String> optional = Optional.of("value");

// Java 8/9 - verbose
String value1 = optional.orElseThrow(() -> new RuntimeException("No value"));

// Java 10 - concise (throws NoSuchElementException)
String value2 = optional.orElseThrow();

// Practical usage
User user = userRepository.findById(id).orElseThrow();
Config config = configService.getConfig().orElseThrow();
```

---

## 🚀 **Performance Improvements**

### **G1 Parallel Full GC**
```bash
# JVM flags for G1 with parallel full GC
-XX:+UseG1GC
-XX:+UnlockExperimentalVMOptions
-XX:G1UseAdaptiveIHOP=true
```

### **Heap Allocation on Alternative Memory**
```bash
# Allocate heap on alternative memory devices (e.g., NVDIMMs)
-XX:AllocateHeapAt=/path/to/alternative/memory
```

### **Thread-Local Handshakes**
- Enables stopping individual threads instead of all threads
- Reduces stop-the-world pause times
- Improves application responsiveness

---

## 📝 **Best Practices**

### **When to Use `var`**
```java
// ✅ GOOD - Clear from context
var users = userService.getAllUsers();
var config = DatabaseConfig.builder().build();
var stream = Files.lines(path);

// ✅ GOOD - Reduces verbosity for complex types
var orderMap = new ConcurrentHashMap<CustomerId, List<OrderItem>>();
var processor = new DataProcessor<Customer, Order, Invoice>();

// ✅ GOOD - Factory methods provide clear type information
var date = LocalDate.now();
var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
var connection = DriverManager.getConnection(url);
```

### **When to Avoid `var`**
```java
// ❌ AVOID - Type not clear
var data = getData();
var result = process();
var info = getInfo();

// ❌ AVOID - Primitives are already short
var count = 0;        // int count = 0; is clearer
var flag = true;      // boolean flag = true; is clearer
var index = -1;       // int index = -1; is clearer

// ❌ AVOID - When explicit type improves readability
var number = calculateComplexValue();  // What type is returned?
```

### **Code Style Guidelines**
```java
// Prefer descriptive variable names with var
var customerOrderHistory = getOrderHistory(customerId);
var validationResult = validatePayment(payment);
var configurationSettings = loadConfiguration();

// Use var for obviously typed expressions
var users = new ArrayList<User>();
var properties = new Properties();
var executor = Executors.newFixedThreadPool(10);

// Consider explicit types for method chains
Stream<Order> orderStream = orders.stream();  // Type helps readability
var processedOrders = orderStream
    .filter(Order::isValid)
    .collect(toUnmodifiableList());
```

---

## 🔄 **Migration Patterns**

### **Collection Declarations**
```java
// Before Java 10
List<CustomerOrder> orders = new ArrayList<>();
Map<String, Set<Permission>> permissions = new HashMap<>();
Queue<ProcessingTask> taskQueue = new LinkedList<>();

// Java 10 with var
var orders = new ArrayList<CustomerOrder>();
var permissions = new HashMap<String, Set<Permission>>();
var taskQueue = new LinkedList<ProcessingTask>();
```

### **Stream Operations**
```java
// Before Java 10
List<ProcessedData> results = rawData.stream()
    .filter(this::isValid)
    .map(this::process)
    .collect(Collectors.toList());

// Java 10 with var and new collectors
var results = rawData.stream()
    .filter(this::isValid)
    .map(this::process)
    .collect(toUnmodifiableList());
```

### **Exception Handling with Optional**
```java
// Before Java 10
public User getUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
}

// Java 10 - simpler for standard exceptions
public User getUser(Long id) {
    return userRepository.findById(id).orElseThrow();
}
```

---

## 🧪 **Testing Examples**

### **Unit Testing with var**
```java
@Test
void testUserService() {
    // Arrange
    var mockRepository = mock(UserRepository.class);
    var testUser = new User("John", "john@test.com");
    when(mockRepository.findByEmail("john@test.com"))
        .thenReturn(Optional.of(testUser));
    
    var userService = new UserService(mockRepository);
    
    // Act
    var result = userService.getUserByEmail("john@test.com");
    
    // Assert
    assertThat(result).hasValue(testUser);
    verify(mockRepository).findByEmail("john@test.com");
}
```

### **Integration Testing**
```java
@Test
void testDataProcessingPipeline() {
    // Setup test data
    var inputData = Arrays.asList(
        new RawData("item1", 100),
        new RawData("item2", 200),
        new RawData("item3", 150)
    );
    
    // Process
    var processor = new DataProcessor();
    var results = processor.process(inputData);
    
    // Verify
    assertThat(results).hasSize(3);
    assertThat(results).allMatch(ProcessedData::isValid);
}
```

---

## 🎯 **Common Use Cases**

### **Configuration Management**
```java
public class ConfigurationLoader {
    public AppConfig loadConfiguration() {
        var properties = new Properties();
        var configFile = Paths.get("application.properties");
        
        try (var input = Files.newInputStream(configFile)) {
            properties.load(input);
        }
        
        var dbConfig = createDatabaseConfig(properties);
        var serverConfig = createServerConfig(properties);
        var cacheConfig = createCacheConfig(properties);
        
        return new AppConfig(dbConfig, serverConfig, cacheConfig);
    }
}
```

### **Data Processing**
```java
public class OrderAnalyzer {
    public OrderSummary analyzeOrders(List<Order> orders) {
        var totalRevenue = orders.stream()
            .filter(Order::isPaid)
            .mapToDouble(Order::getAmount)
            .sum();
        
        var ordersByStatus = orders.stream()
            .collect(groupingBy(Order::getStatus, toUnmodifiableList()));
        
        var topCustomers = orders.stream()
            .collect(groupingBy(Order::getCustomerId, summingDouble(Order::getAmount)))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(10)
            .collect(toUnmodifiableList());
        
        return new OrderSummary(totalRevenue, ordersByStatus, topCustomers);
    }
}
```

---

## 📊 **Quick Reference Table**

| Feature | Syntax | Example |
|---------|--------|---------|
| **var declaration** | `var name = value;` | `var list = new ArrayList<>();` |
| **Loop variable** | `for (var item : collection)` | `for (var entry : map.entrySet())` |
| **Try-resource** | `try (var resource = ...)` | `try (var scanner = new Scanner(file))` |
| **Unmodifiable List** | `.collect(toUnmodifiableList())` | `stream.collect(toUnmodifiableList())` |
| **Unmodifiable Set** | `.collect(toUnmodifiableSet())` | `stream.collect(toUnmodifiableSet())` |
| **Unmodifiable Map** | `.collect(toUnmodifiableMap(...))` | `stream.collect(toUnmodifiableMap(k, v))` |
| **Optional throw** | `optional.orElseThrow()` | `repository.findById(id).orElseThrow()` |

---

**Next:** [Java 11 Features](../14.3-Java11/Notes.md) | **Previous:** [Java 9 Features](../14.1-Java9/Notes.md)