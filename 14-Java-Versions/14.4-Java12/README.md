# Module 14.4: Java 12 Features

## 🎯 **Overview**

Java 12 (March 2019) introduced **Switch Expressions** as a preview feature, along with enhanced String methods and improved JVM performance. This module explores these innovations that made Java more expressive and efficient.

---

## 🚀 **Key Features**

### **🔥 Major Features**
- **Switch Expressions (Preview)**: More concise and safer switch statements
- **String Methods**: `indent()` and `transform()` for better string manipulation
- **Collectors.teeing()**: Combine two collectors for complex reductions
- **JVM Improvements**: Microbenchmark suite and G1 enhancements

### **📈 Performance & JVM**
- **Microbenchmark Suite**: Built-in performance testing capabilities
- **G1 Improvements**: Better mixed garbage collection
- **Shenandoah GC**: Low-latency garbage collector (experimental)

---

## 📚 **Detailed Feature Guide**

### **1. Switch Expressions (Preview Feature)**

Revolutionary improvement to switch statements with expressions and better pattern matching.

#### **Traditional Switch vs Switch Expressions**
```java
// Traditional switch (Java 11 and earlier)
public String getTraditionalDayType(DayOfWeek day) {
    String dayType;
    switch (day) {
        case MONDAY:
        case TUESDAY:
        case WEDNESDAY:
        case THURSDAY:
        case FRIDAY:
            dayType = "weekday";
            break;
        case SATURDAY:
        case SUNDAY:
            dayType = "weekend";
            break;
        default:
            dayType = "unknown";
            break;
    }
    return dayType;
}

// Java 12 switch expression (Preview)
public String getModernDayType(DayOfWeek day) {
    return switch (day) {
        case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "weekday";
        case SATURDAY, SUNDAY -> "weekend";
        default -> "unknown";
    };
}
```

#### **Advanced Switch Expression Patterns**
```java
public class SwitchExpressionExamples {
    
    // Multiple values with arrow syntax
    public int getQuarter(Month month) {
        return switch (month) {
            case JANUARY, FEBRUARY, MARCH -> 1;
            case APRIL, MAY, JUNE -> 2;
            case JULY, AUGUST, SEPTEMBER -> 3;
            case OCTOBER, NOVEMBER, DECEMBER -> 4;
        };
    }
    
    // Block syntax for complex logic
    public double calculateDiscount(CustomerType type, double amount) {
        return switch (type) {
            case PREMIUM -> {
                double discount = amount > 1000 ? 0.15 : 0.10;
                yield amount * discount;
            }
            case GOLD -> {
                double discount = amount > 500 ? 0.10 : 0.05;
                yield amount * discount;
            }
            case REGULAR -> amount * 0.02;
            default -> 0.0;
        };
    }
    
    // Switch expressions with enums
    public String getOperationSymbol(Operation op) {
        return switch (op) {
            case ADD -> "+";
            case SUBTRACT -> "-";
            case MULTIPLY -> "*";
            case DIVIDE -> "/";
            case MODULO -> "%";
        };
    }
    
    // Nested switch expressions
    public String processRequest(RequestType type, Priority priority) {
        return switch (type) {
            case GET -> switch (priority) {
                case HIGH -> "Fast GET processing";
                case MEDIUM -> "Standard GET processing";
                case LOW -> "Queued GET processing";
            };
            case POST -> switch (priority) {
                case HIGH -> "Priority POST handling";
                case MEDIUM, LOW -> "Standard POST handling";
            };
            case DELETE -> "Delete request processed";
        };
    }
}
```

#### **Benefits of Switch Expressions**
- **Exhaustiveness**: Compiler ensures all cases are covered
- **No fall-through**: Eliminates accidental fall-through bugs
- **Expression-based**: Can be used in assignments and returns
- **Conciseness**: Reduces boilerplate code significantly

### **2. Enhanced String Methods**

Java 12 introduced two powerful String methods for better text processing.

#### **String.indent() Method**
```java
public class StringIndentExamples {
    
    public void demonstrateIndent() {
        String text = "Hello World\nJava Programming\nString Methods";
        
        // Positive indent (add spaces)
        String indented = text.indent(4);
        System.out.println("Indented by 4:");
        System.out.println(indented);
        /*
        Output:
            Hello World
            Java Programming
            String Methods
        */
        
        // Zero indent (normalize line endings)
        String normalized = text.indent(0);
        System.out.println("Normalized:");
        System.out.println(normalized);
        
        // Negative indent (remove spaces if present)
        String preIndented = "    Line 1\n    Line 2\n    Line 3";
        String dedented = preIndented.indent(-2);
        System.out.println("Dedented by 2:");
        System.out.println(dedented);
    }
    
    // Practical usage: Code formatting
    public String formatCode(List<String> codeLines, int indentLevel) {
        return codeLines.stream()
            .map(line -> line.indent(indentLevel * 4))
            .collect(Collectors.joining());
    }
    
    // JSON formatting example
    public String formatJson(String jsonContent, int level) {
        return Arrays.stream(jsonContent.split("\n"))
            .map(line -> line.trim().indent(level * 2))
            .collect(Collectors.joining());
    }
}
```

#### **String.transform() Method**
```java
public class StringTransformExamples {
    
    public void demonstrateTransform() {
        String name = "john doe";
        
        // Chain transformations
        String formatted = name.transform(String::toUpperCase)
                              .transform(s -> s.replace(" ", "_"))
                              .transform(s -> "USER_" + s);
        
        System.out.println(formatted); // Output: USER_JOHN_DOE
        
        // More complex transformations
        String phoneNumber = "1234567890";
        String formattedPhone = phoneNumber
            .transform(s -> s.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "($1) $2-$3"));
        
        System.out.println(formattedPhone); // Output: (123) 456-7890
    }
    
    // Custom transformation functions
    public Function<String, String> titleCase() {
        return s -> Arrays.stream(s.split(" "))
                         .map(word -> word.substring(0, 1).toUpperCase() + 
                                     word.substring(1).toLowerCase())
                         .collect(Collectors.joining(" "));
    }
    
    public Function<String, String> removeSpecialChars() {
        return s -> s.replaceAll("[^a-zA-Z0-9\\s]", "");
    }
    
    public void practicalUsage() {
        String userInput = "  hello, WORLD! #2024  ";
        
        String cleaned = userInput
            .transform(String::trim)
            .transform(removeSpecialChars())
            .transform(titleCase());
        
        System.out.println(cleaned); // Output: "Hello World 2024"
    }
}
```

### **3. Collectors.teeing()**

Combine two collectors to perform multiple reductions in a single pass.

```java
import java.util.stream.Collectors;

public class TeeingCollectorExamples {
    
    public void demonstrateTeeing() {
        List<Product> products = Arrays.asList(
            new Product("Laptop", 999.99, Category.ELECTRONICS),
            new Product("Book", 29.99, Category.BOOKS),
            new Product("Phone", 699.99, Category.ELECTRONICS),
            new Product("Desk", 299.99, Category.FURNITURE)
        );
        
        // Calculate average price and count in single pass
        var statistics = products.stream()
            .collect(Collectors.teeing(
                Collectors.averagingDouble(Product::getPrice),
                Collectors.counting(),
                (avgPrice, count) -> new ProductStatistics(avgPrice, count)
            ));
        
        System.out.printf("Average price: $%.2f, Count: %d%n", 
                         statistics.getAveragePrice(), statistics.getCount());
    }
    
    // More complex teeing example
    public SalesAnalysis analyzeSales(List<Sale> sales) {
        return sales.stream()
            .collect(Collectors.teeing(
                // First collector: total revenue
                Collectors.summingDouble(Sale::getAmount),
                
                // Second collector: group by region
                Collectors.groupingBy(
                    Sale::getRegion,
                    Collectors.counting()
                ),
                
                // Combiner: create analysis object
                (totalRevenue, regionCounts) -> 
                    new SalesAnalysis(totalRevenue, regionCounts)
            ));
    }
    
    // Triple statistics example
    public OrderInsights getOrderInsights(List<Order> orders) {
        return orders.stream()
            .collect(Collectors.teeing(
                // Min and max order value
                Collectors.teeing(
                    Collectors.minBy(Comparator.comparing(Order::getValue)),
                    Collectors.maxBy(Comparator.comparing(Order::getValue)),
                    (min, max) -> new MinMax(
                        min.map(Order::getValue).orElse(0.0),
                        max.map(Order::getValue).orElse(0.0)
                    )
                ),
                
                // Average order value
                Collectors.averagingDouble(Order::getValue),
                
                // Combine results
                OrderInsights::new
            ));
    }
}
```

---

## 🛠️ **Practical Examples**

### **Request Processing with Switch Expressions**
```java
public class RequestProcessor {
    
    public Response processRequest(HttpRequest request) {
        return switch (request.getMethod()) {
            case "GET" -> handleGetRequest(request);
            case "POST" -> handlePostRequest(request);
            case "PUT" -> handlePutRequest(request);
            case "DELETE" -> handleDeleteRequest(request);
            default -> Response.error("Unsupported method: " + request.getMethod());
        };
    }
    
    public int getStatusCode(ResponseType type) {
        return switch (type) {
            case SUCCESS -> 200;
            case CREATED -> 201;
            case BAD_REQUEST -> 400;
            case UNAUTHORIZED -> 401;
            case NOT_FOUND -> 404;
            case INTERNAL_ERROR -> 500;
        };
    }
    
    private Response handleGetRequest(HttpRequest request) {
        return switch (request.getPath()) {
            case "/users" -> getUsersList();
            case "/products" -> getProductsList();
            default -> {
                if (request.getPath().startsWith("/users/")) {
                    yield getUserById(extractId(request.getPath()));
                }
                yield Response.notFound();
            }
        };
    }
}
```

### **Configuration Management with String Methods**
```java
public class ConfigurationManager {
    
    public String formatConfigFile(Map<String, String> config, int indentLevel) {
        return config.entrySet().stream()
            .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
            .map(line -> line.indent(indentLevel))
            .collect(Collectors.joining("\n"));
    }
    
    public String processConfigValue(String rawValue) {
        return rawValue
            .transform(String::trim)
            .transform(s -> s.toLowerCase())
            .transform(s -> s.replaceAll("\\s+", "_"))
            .transform(s -> s.replaceAll("[^a-z0-9_]", ""));
    }
    
    public List<String> formatYamlSection(String sectionName, 
                                          Map<String, Object> values) {
        List<String> lines = new ArrayList<>();
        lines.add(sectionName + ":");
        
        values.forEach((key, value) -> {
            String formattedLine = String.format("%s: %s", key, value)
                                        .indent(2)
                                        .stripTrailing();
            lines.add(formattedLine);
        });
        
        return lines;
    }
}
```

### **Data Analytics with Teeing**
```java
public class DataAnalytics {
    
    public EmployeeAnalysis analyzeEmployees(List<Employee> employees) {
        return employees.stream()
            .collect(Collectors.teeing(
                // Salary statistics
                Collectors.teeing(
                    Collectors.averagingDouble(Employee::getSalary),
                    Collectors.summarizingDouble(Employee::getSalary),
                    SalaryStats::new
                ),
                
                // Department distribution
                Collectors.groupingBy(
                    Employee::getDepartment,
                    Collectors.counting()
                ),
                
                // Combine into analysis
                EmployeeAnalysis::new
            ));
    }
    
    public CustomerSegmentation segmentCustomers(List<Customer> customers) {
        return customers.stream()
            .collect(Collectors.teeing(
                // High-value customers (top 20%)
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> list.stream()
                               .sorted(Comparator.comparing(Customer::getLifetimeValue).reversed())
                               .limit((long) (list.size() * 0.2))
                               .collect(Collectors.toUnmodifiableList())
                ),
                
                // Geographic distribution
                Collectors.groupingBy(
                    Customer::getRegion,
                    Collectors.averagingDouble(Customer::getLifetimeValue)
                ),
                
                CustomerSegmentation::new
            ));
    }
}
```

---

## ⚡ **Best Practices**

### **Switch Expressions Guidelines**
- ✅ **Use for multiple related values**: Perfect for enums and string constants
- ✅ **Prefer arrow syntax**: More concise and safer than traditional switch
- ✅ **Leverage exhaustiveness**: Let compiler ensure all cases are covered
- ❌ **Avoid complex logic**: Keep switch arms simple and readable

### **String Method Usage**
- ✅ **Chain transformations**: Use `transform()` for readable processing pipelines
- ✅ **Use `indent()` for formatting**: Perfect for code generation and text processing
- ❌ **Avoid excessive chaining**: Keep transformation chains reasonably short

### **Teeing Collector Best Practices**
- ✅ **Single-pass efficiency**: Use when you need multiple aggregations
- ✅ **Related computations**: Combine logically related statistics
- ❌ **Overuse**: Don't force teeing when separate operations are clearer

---

## 🧪 **Testing with Java 12 Features**

```java
@Test
public void testSwitchExpressions() {
    PaymentProcessor processor = new PaymentProcessor();
    
    // Test all payment methods
    assertEquals("Credit processing", processor.processPayment(PaymentType.CREDIT));
    assertEquals("Debit processing", processor.processPayment(PaymentType.DEBIT));
    assertEquals("Cash handling", processor.processPayment(PaymentType.CASH));
    
    // Test exhaustiveness (compiler ensures all cases covered)
    for (PaymentType type : PaymentType.values()) {
        assertNotNull(processor.processPayment(type));
    }
}

@Test
public void testStringMethods() {
    String text = "Hello\nWorld";
    
    // Test indent
    String indented = text.indent(4);
    assertTrue(indented.startsWith("    Hello"));
    
    // Test transform
    String transformed = "test".transform(String::toUpperCase);
    assertEquals("TEST", transformed);
}
```

---

## 📋 **Quick Reference**

### **Switch Expression Syntax**
```java
// Arrow syntax (preferred)
var result = switch (value) {
    case A, B -> "Group 1";
    case C -> "Group 2";
    default -> "Other";
};

// Block syntax for complex logic
var result = switch (value) {
    case A -> {
        // complex logic
        yield "Result A";
    }
    case B -> {
        // complex logic
        yield "Result B";
    }
};
```

### **String Methods**
```java
// indent(n) - adds n spaces to each line
String indented = text.indent(4);

// transform(function) - applies function to string
String result = text.transform(String::toUpperCase)
                   .transform(s -> s.replace(" ", "_"));
```

### **Teeing Collector**
```java
// Combine two collectors
var result = stream.collect(Collectors.teeing(
    collector1,
    collector2,
    (result1, result2) -> combineResults(result1, result2)
));
```

---

## 🎯 **What You'll Learn**

After completing this module, you'll understand:
- **Switch Expressions**: Modern switch syntax with safety and conciseness
- **String Processing**: Advanced string manipulation with `indent()` and `transform()`
- **Complex Reductions**: Efficient multiple aggregations with `teeing()`
- **Code Modernization**: Upgrading from traditional Java patterns

---

## 📚 **Prerequisites**

- **Java 11 Features**: String methods and HTTP client basics
- **Java 10 Features**: Local variable type inference (`var`)
- **Stream API**: Collection processing and collectors
- **Lambda Expressions**: Functional programming concepts

---

**🚀 Ready to write more expressive Java code with switch expressions? Let's explore Java 12's powerful features!**

**Next:** [Java 13 Features](../14.5-Java13/README.md) | **Previous:** [Java 11 Features](../14.3-Java11/README.md)