# Module 14.6: Java 14 Features

## 🎯 **Overview**

Java 14 (March 2020) was a landmark release that introduced **Records** as a preview feature and standardized **Switch Expressions**. It also brought **Pattern Matching for instanceof** and **Text Blocks** improvements, making Java more concise and expressive.

---

## 🚀 **Key Features**

### **🔥 Major Features**
- **Records (Preview)**: Immutable data classes with automatic methods
- **Switch Expressions**: Standardized from preview (JEP 361)
- **Pattern Matching for instanceof (Preview)**: Eliminate casting boilerplate
- **Text Blocks (Second Preview)**: Enhanced multi-line string literals
- **Helpful NullPointerExceptions**: Better debugging information

### **📈 Performance & JVM**
- **NUMA-Aware Memory Allocation**: Better performance on multi-socket systems
- **JFR Event Streaming**: Real-time monitoring capabilities
- **Packaging Tool (Incubator)**: Native package generation

---

## 📚 **Detailed Feature Guide**

### **1. Records (Preview Feature)**

Records provide a compact way to create immutable data classes with automatic implementations of common methods.

#### **Basic Record Syntax**
```java
// Traditional Java class (verbose)
public final class PersonOld {
    private final String name;
    private final int age;
    private final String email;
    
    public PersonOld(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    
    public String name() { return name; }
    public int age() { return age; }
    public String email() { return email; }
    
    @Override
    public boolean equals(Object obj) {
        // 15+ lines of boilerplate
    }
    
    @Override
    public int hashCode() {
        // 3+ lines of boilerplate
    }
    
    @Override
    public String toString() {
        // 3+ lines of boilerplate
    }
}

// Java 14 Record (concise)
public record Person(String name, int age, String email) {
    // That's it! All methods are generated automatically
}
```

#### **Advanced Record Features**
```java
// Record with validation (compact constructor)
public record Employee(String name, int id, double salary, Department dept) {
    
    // Compact constructor for validation
    public Employee {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        if (dept == null) {
            throw new IllegalArgumentException("Department is required");
        }
        
        // Normalize name
        name = name.trim().toUpperCase();
    }
    
    // Additional methods (beyond automatic ones)
    public boolean isHighEarner() {
        return salary > 100000;
    }
    
    public String getDisplayName() {
        return String.format("%s (ID: %d)", name, id);
    }
    
    // Static factory methods
    public static Employee createIntern(String name, int id) {
        return new Employee(name, id, 0, Department.INTERN);
    }
    
    public static Employee createFromString(String data) {
        String[] parts = data.split(",");
        return new Employee(
            parts[0].trim(),
            Integer.parseInt(parts[1].trim()),
            Double.parseDouble(parts[2].trim()),
            Department.valueOf(parts[3].trim().toUpperCase())
        );
    }
}

// Records can implement interfaces
public record Point(double x, double y) implements Comparable<Point> {
    
    @Override
    public int compareTo(Point other) {
        int xComparison = Double.compare(this.x, other.x);
        return xComparison != 0 ? xComparison : Double.compare(this.y, other.y);
    }
    
    public double distanceFromOrigin() {
        return Math.sqrt(x * x + y * y);
    }
    
    public Point translate(double dx, double dy) {
        return new Point(x + dx, y + dy);
    }
}
```

#### **Records in Collections and Streams**
```java
public class RecordCollectionExamples {
    
    public void demonstrateRecordsInStreams() {
        List<Employee> employees = List.of(
            new Employee("Alice Johnson", 1001, 85000, Department.ENGINEERING),
            new Employee("Bob Smith", 1002, 120000, Department.ENGINEERING),
            new Employee("Carol Brown", 1003, 95000, Department.MARKETING),
            new Employee("Dave Wilson", 1004, 110000, Department.SALES)
        );
        
        // Find high earners by department
        Map<Department, List<Employee>> highEarnersByDept = employees.stream()
            .filter(Employee::isHighEarner)
            .collect(Collectors.groupingBy(Employee::dept));
        
        // Calculate average salary by department
        Map<Department, Double> avgSalaryByDept = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::dept,
                Collectors.averagingDouble(Employee::salary)
            ));
        
        // Create salary summary records
        List<SalarySummary> summaries = avgSalaryByDept.entrySet().stream()
            .map(entry -> new SalarySummary(entry.getKey(), entry.getValue()))
            .sorted(Comparator.comparing(SalarySummary::averageSalary).reversed())
            .collect(Collectors.toList());
        
        summaries.forEach(summary -> 
            System.out.printf("%s: $%.2f average%n", 
                            summary.department(), summary.averageSalary()));
    }
    
    // Nested records for complex data structures
    public record Order(String orderId, Customer customer, List<OrderItem> items, OrderStatus status) {
        
        public double totalAmount() {
            return items.stream().mapToDouble(OrderItem::totalPrice).sum();
        }
        
        public record OrderItem(String productId, String name, int quantity, double unitPrice) {
            public double totalPrice() {
                return quantity * unitPrice;
            }
        }
        
        public enum OrderStatus { PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED }
    }
}
```

### **2. Pattern Matching for instanceof (Preview)**

Eliminates the need for explicit casting after instanceof checks.

```java
public class PatternMatchingExamples {
    
    // Traditional instanceof (Java 13 and earlier)
    public String processShapeOld(Object shape) {
        if (shape instanceof Circle) {
            Circle circle = (Circle) shape;  // Explicit cast needed
            return "Circle with radius: " + circle.getRadius();
        } else if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;  // Explicit cast needed
            return "Rectangle: " + rect.getWidth() + "x" + rect.getHeight();
        } else if (shape instanceof Triangle) {
            Triangle triangle = (Triangle) shape;  // Explicit cast needed
            return "Triangle with base: " + triangle.getBase();
        }
        return "Unknown shape";
    }
    
    // Java 14 pattern matching (Preview)
    public String processShapeNew(Object shape) {
        if (shape instanceof Circle circle) {
            return "Circle with radius: " + circle.getRadius();
        } else if (shape instanceof Rectangle rect) {
            return "Rectangle: " + rect.getWidth() + "x" + rect.getHeight();
        } else if (shape instanceof Triangle triangle) {
            return "Triangle with base: " + triangle.getBase();
        }
        return "Unknown shape";
    }
    
    // More complex pattern matching
    public double calculateArea(Object shape) {
        return switch (shape) {
            case Circle circle -> Math.PI * circle.getRadius() * circle.getRadius();
            case Rectangle rect -> rect.getWidth() * rect.getHeight();
            case Triangle triangle -> 0.5 * triangle.getBase() * triangle.getHeight();
            default -> throw new IllegalArgumentException("Unknown shape: " + shape);
        };
    }
    
    // Pattern matching in methods
    public void processPayment(Object payment) {
        if (payment instanceof CreditCard cc && cc.isValid()) {
            processCreditCard(cc);
        } else if (payment instanceof DebitCard dc && dc.hasBalance()) {
            processDebitCard(dc);
        } else if (payment instanceof Cash cash && cash.getAmount() > 0) {
            processCash(cash);
        } else {
            throw new IllegalArgumentException("Invalid payment method");
        }
    }
    
    // Pattern matching with additional conditions
    public String categorizeEmployee(Object emp) {
        if (emp instanceof Employee employee) {
            if (employee.salary() > 150000) {
                return "Senior " + employee.dept();
            } else if (employee.salary() > 75000) {
                return "Mid-level " + employee.dept();
            } else {
                return "Junior " + employee.dept();
            }
        }
        return "Not an employee";
    }
}
```

### **3. Text Blocks Enhancement (Second Preview)**

Improved multi-line string literals with better formatting control.

```java
public class TextBlockExamples {
    
    public void demonstrateTextBlocks() {
        // HTML with text blocks
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Java 14 Demo</title>
            </head>
            <body>
                <h1>Welcome to Java 14</h1>
                <p>Text blocks make multi-line strings easy!</p>
            </body>
            </html>
            """;
        
        // JSON with text blocks
        String json = """
            {
                "name": "Java 14",
                "features": [
                    "Records",
                    "Pattern Matching",
                    "Text Blocks",
                    "Switch Expressions"
                ],
                "releaseDate": "2020-03-17"
            }
            """;
        
        // SQL queries
        String query = """
            SELECT e.name, e.salary, d.department_name
            FROM employees e
            JOIN departments d ON e.dept_id = d.id
            WHERE e.salary > 100000
            ORDER BY e.salary DESC
            """;
        
        System.out.println("HTML:");
        System.out.println(html);
        System.out.println("\nJSON:");
        System.out.println(json);
        System.out.println("\nSQL:");
        System.out.println(query);
    }
    
    // Text blocks with string methods (Java 12+)
    public String formatCode(String className, List<String> methods) {
        String template = """
            public class %s {
            %s
            }
            """;
        
        String methodsCode = methods.stream()
            .map(method -> ("public void " + method + "() { }").indent(4))
            .collect(Collectors.joining("\n"));
        
        return String.format(template, className, methodsCode);
    }
}
```

### **4. Helpful NullPointerExceptions**

Enhanced NPE messages that show exactly which variable was null.

```java
public class NullPointerExamples {
    
    public void demonstrateHelpfulNPEs() {
        Person person = new Person("John", 30, null);  // email is null
        
        try {
            // This will throw NPE with detailed message
            int length = person.email().toLowerCase().length();
        } catch (NullPointerException e) {
            // Java 14 shows: "Cannot invoke String.toLowerCase() because 
            // the return value of Person.email() is null"
            System.out.println("Helpful NPE: " + e.getMessage());
        }
        
        // Nested object access
        Order order = new Order("ORD123", null, List.of(), Order.OrderStatus.PENDING);
        try {
            String customerName = order.customer().name();  // customer is null
        } catch (NullPointerException e) {
            // Shows exactly which part of the chain is null
            System.out.println("Chain NPE: " + e.getMessage());
        }
    }
    
    // Example with array access
    public void arrayNPEExample() {
        String[] array = null;
        try {
            int length = array.length;  // array is null
        } catch (NullPointerException e) {
            // Shows: "Cannot read the array length because \"array\" is null"
            System.out.println("Array NPE: " + e.getMessage());
        }
    }
}
```

---

## 🛠️ **Practical Examples**

### **Data Transfer Objects with Records**
```java
// API Response DTOs
public record ApiResponse<T>(boolean success, T data, String message, long timestamp) {
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "Success", System.currentTimeMillis());
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message, System.currentTimeMillis());
    }
}

// Configuration records
public record DatabaseConfig(
    String host,
    int port,
    String database,
    String username,
    String password,
    int maxConnections,
    Duration connectionTimeout
) {
    
    // Compact constructor with validation
    public DatabaseConfig {
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("Host cannot be blank");
        }
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Invalid port: " + port);
        }
        if (maxConnections < 1) {
            throw new IllegalArgumentException("Max connections must be positive");
        }
        
        // Default values
        if (connectionTimeout == null) {
            connectionTimeout = Duration.ofSeconds(30);
        }
    }
    
    public String getConnectionUrl() {
        return String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
    }
}
```

### **Event Handling System**
```java
// Event hierarchy with records
public sealed interface Event permits UserEvent, SystemEvent, ErrorEvent {
    long timestamp();
    String source();
}

public record UserEvent(
    long timestamp,
    String source,
    String userId,
    String action,
    Map<String, Object> metadata
) implements Event {
    
    public UserEvent {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID is required");
        }
        timestamp = System.currentTimeMillis();
        metadata = Map.copyOf(metadata); // Ensure immutability
    }
}

public record SystemEvent(
    long timestamp,
    String source,
    String component,
    String status
) implements Event {
    
    public SystemEvent {
        timestamp = System.currentTimeMillis();
    }
}

// Event processor using pattern matching
public class EventProcessor {
    
    public void processEvent(Event event) {
        if (event instanceof UserEvent userEvent) {
            processUserAction(userEvent.userId(), userEvent.action(), userEvent.metadata());
        } else if (event instanceof SystemEvent systemEvent) {
            updateSystemStatus(systemEvent.component(), systemEvent.status());
        } else if (event instanceof ErrorEvent errorEvent) {
            handleError(errorEvent.error(), errorEvent.severity());
        }
    }
}
```

---

## ⚡ **Best Practices**

### **Record Guidelines**
- ✅ **Use for data carriers**: Perfect for DTOs, value objects, and configuration
- ✅ **Add validation**: Use compact constructors for input validation
- ✅ **Keep them simple**: Records should primarily hold data
- ❌ **Avoid mutable fields**: Records are designed to be immutable

### **Pattern Matching Best Practices**
- ✅ **Combine with type checks**: Use when you need both type and cast
- ✅ **Add conditions**: Use `&&` for additional validation
- ❌ **Overuse**: Don't force pattern matching where simple methods suffice

---

## 🎯 **What You'll Learn**

After completing this module, you'll understand:
- **Records**: Creating concise, immutable data classes
- **Pattern Matching**: Eliminating casting boilerplate
- **Switch Expressions**: Modern switch syntax (now standard)
- **Text Blocks**: Multi-line string management
- **Enhanced Debugging**: Better NPE messages

---

**🚀 Ready to write more concise Java code with Records? Let's explore Java 14's revolutionary features!**

**Next:** [Java 15 Features](../14.7-Java15/README.md) | **Previous:** [Java 13 Features](../14.5-Java13/README.md)