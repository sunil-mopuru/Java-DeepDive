# Module 14.7: Java 15 Features

## 🎯 **Overview**

Java 15 (September 2020) introduced **Sealed Classes** as a preview feature and standardized **Text Blocks**, bringing more control over inheritance hierarchies and improving multi-line string handling. This module explores these features that enhance type safety and code readability.

---

## 🚀 **Key Features**

### **🔥 Major Features**
- **Sealed Classes (Preview)**: Control which classes can extend or implement a type
- **Text Blocks (Standard)**: Multi-line string literals are now production-ready
- **Hidden Classes**: Dynamic class loading for frameworks
- **Records (Second Preview)**: Enhanced record capabilities

### **📈 Performance & JVM**
- **ZGC Improvements**: Better memory management for large heaps
- **Shenandoah GC**: Production-ready low-latency garbage collector
- **Edwards-Curve Digital Signature Algorithm (EdDSA)**: Cryptographic improvements

---

## 📚 **Detailed Feature Guide**

### **1. Sealed Classes (Preview Feature)**

Control inheritance hierarchies by specifying which classes can extend or implement a type.

#### **Basic Sealed Class Syntax**
```java
// Sealed interface with permitted implementations
public sealed interface Shape permits Circle, Rectangle, Triangle {
    double area();
}

// Permitted implementations
public final class Circle implements Shape {
    private final double radius;
    
    public Circle(double radius) { this.radius = radius; }
    
    @Override
    public double area() { return Math.PI * radius * radius; }
}

public final class Rectangle implements Shape {
    private final double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() { return width * height; }
}

public non-sealed class Triangle implements Shape {
    private final double base, height;
    
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    
    @Override
    public double area() { return 0.5 * base * height; }
}
```

#### **Advanced Sealed Class Patterns**
```java
// Sealed class hierarchy for expression evaluation
public sealed class Expression 
    permits Literal, BinaryOp, UnaryOp {
    
    public abstract double evaluate();
}

public final class Literal extends Expression {
    private final double value;
    
    public Literal(double value) { this.value = value; }
    
    @Override
    public double evaluate() { return value; }
    
    public double getValue() { return value; }
}

public final class BinaryOp extends Expression {
    private final Expression left, right;
    private final Operator operator;
    
    public BinaryOp(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    
    @Override
    public double evaluate() {
        return switch (operator) {
            case ADD -> left.evaluate() + right.evaluate();
            case SUBTRACT -> left.evaluate() - right.evaluate();
            case MULTIPLY -> left.evaluate() * right.evaluate();
            case DIVIDE -> left.evaluate() / right.evaluate();
        };
    }
}

public enum Operator { ADD, SUBTRACT, MULTIPLY, DIVIDE }

// Usage with exhaustive pattern matching
public class ExpressionProcessor {
    public String describe(Expression expr) {
        return switch (expr) {
            case Literal lit -> "Value: " + lit.getValue();
            case BinaryOp binOp -> "Binary operation";
            case UnaryOp unOp -> "Unary operation";
        };
    }
}
```

### **2. Standardized Text Blocks**

Text blocks are now a standard feature, providing clean multi-line string literals.

```java
public class TextBlockExamples {
    
    public void demonstrateStandardFeatures() {
        // SQL queries
        String query = """
                SELECT p.name, p.price, c.name as category
                FROM products p
                JOIN categories c ON p.category_id = c.id
                WHERE p.active = true
                  AND p.price BETWEEN ? AND ?
                ORDER BY p.name
                """;
        
        // JSON templates
        String jsonTemplate = """
                {
                    "user": {
                        "id": %d,
                        "name": "%s",
                        "preferences": {
                            "theme": "dark",
                            "notifications": true
                        }
                    },
                    "timestamp": "%s"
                }
                """;
        
        // HTML emails
        String emailHtml = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Welcome Email</title>
                    <style>
                        .container { max-width: 600px; margin: 0 auto; }
                        .header { background: #007bff; color: white; padding: 20px; }
                        .content { padding: 30px; }
                        .footer { background: #f8f9fa; padding: 15px; text-align: center; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Welcome to Our Service!</h1>
                        </div>
                        <div class="content">
                            <p>Dear %s,</p>
                            <p>Thank you for joining our platform. We're excited to have you aboard!</p>
                        </div>
                        <div class="footer">
                            <p>&copy; 2024 Our Company. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """;
        
        System.out.println("Query: " + query);
        System.out.println("JSON: " + jsonTemplate.formatted(123, "John Doe", Instant.now()));
        System.out.println("Email: " + emailHtml.formatted("Customer Name"));
    }
}
```

### **3. Practical Applications**

Real-world usage combining sealed classes and text blocks for type-safe systems.

#### **Result Type with Sealed Classes**
```java
public sealed interface Result<T> permits Success, Failure {
    
    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }
    
    static <T> Result<T> failure(String error) {
        return new Failure<>(error);
    }
    
    default <U> Result<U> map(Function<T, U> mapper) {
        return switch (this) {
            case Success<T> success -> Result.success(mapper.apply(success.value()));
            case Failure<T> failure -> Result.failure(failure.error());
        };
    }
    
    default <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        return switch (this) {
            case Success<T> success -> mapper.apply(success.value());
            case Failure<T> failure -> Result.failure(failure.error());
        };
    }
}

public record Success<T>(T value) implements Result<T> {}
public record Failure<T>(String error) implements Result<T> {}

// Usage in service layer
public class UserService {
    public Result<User> findById(Long id) {
        if (id == null) {
            return Result.failure("ID cannot be null");
        }
        
        User user = repository.findById(id);
        return user != null 
            ? Result.success(user) 
            : Result.failure("User not found: " + id);
    }
    
    public String formatUserInfo(Long id) {
        return switch (findById(id)) {
            case Success<User> success -> {
                User user = success.value();
                yield """
                      User Information:
                      ID: %d
                      Name: %s
                      Email: %s
                      Status: %s
                      """.formatted(user.getId(), user.getName(), 
                                   user.getEmail(), user.getStatus());
            }
            case Failure<User> failure -> 
                "Error: " + failure.error();
        };
    }
}
```

#### **State Machine with Sealed Classes**
```java
public sealed interface OrderState permits Pending, Confirmed, Shipped, Delivered, Cancelled {
    
    default OrderState nextState(OrderEvent event) {
        return switch (this) {
            case Pending pending -> switch (event) {
                case CONFIRM -> new Confirmed(Instant.now());
                case CANCEL -> new Cancelled("Cancelled by customer", Instant.now());
                default -> this;
            };
            case Confirmed confirmed -> switch (event) {
                case SHIP -> new Shipped(generateTrackingNumber(), Instant.now());
                case CANCEL -> new Cancelled("Cancelled after confirmation", Instant.now());
                default -> this;
            };
            case Shipped shipped -> switch (event) {
                case DELIVER -> new Delivered(Instant.now());
                default -> this;
            };
            case Delivered delivered, Cancelled cancelled -> this; // Terminal states
        };
    }
}

public record Pending() implements OrderState {}
public record Confirmed(Instant confirmedAt) implements OrderState {}
public record Shipped(String trackingNumber, Instant shippedAt) implements OrderState {}
public record Delivered(Instant deliveredAt) implements OrderState {}
public record Cancelled(String reason, Instant cancelledAt) implements OrderState {}

public enum OrderEvent { CONFIRM, SHIP, DELIVER, CANCEL }
```

---

## ⚡ **Best Practices**

### **Sealed Classes Guidelines**
- ✅ **Use for closed hierarchies**: When you control all possible implementations
- ✅ **Combine with pattern matching**: Exhaustive switching over sealed types
- ✅ **Design for evolution**: Use `non-sealed` when future extension is needed
- ❌ **Don't overuse**: Regular inheritance is fine for open hierarchies

### **Text Blocks Best Practices**
- ✅ **Use for multi-line content**: SQL, JSON, HTML, XML, etc.
- ✅ **Combine with formatting**: Use `.formatted()` for dynamic content
- ✅ **Maintain readability**: Keep indentation consistent
- ❌ **Avoid for simple strings**: Single-line strings don't benefit

---

## 🧪 **Testing with Java 15 Features**

```java
@Test
public void testSealedClasses() {
    Expression expr = new BinaryOp(
        new Literal(10),
        Operator.ADD,
        new Literal(5)
    );
    
    assertEquals(15.0, expr.evaluate(), 0.001);
    
    String description = switch (expr) {
        case Literal lit -> "Literal value";
        case BinaryOp binOp -> "Binary operation";
        case UnaryOp unOp -> "Unary operation";
    };
    
    assertEquals("Binary operation", description);
}

@Test
public void testResultType() {
    Result<String> success = Result.success("Hello");
    Result<Integer> mapped = success.map(String::length);
    
    assertEquals(5, ((Success<Integer>) mapped).value());
    
    Result<String> failure = Result.failure("Error occurred");
    Result<Integer> mappedFailure = failure.map(String::length);
    
    assertTrue(mappedFailure instanceof Failure);
}
```

---

## 📋 **Quick Reference**

### **Sealed Class Syntax**
```java
// Sealed interface/class
public sealed interface Type permits Impl1, Impl2 {}
public sealed class Type permits Impl1, Impl2 {}

// Permitted implementations
public final class Impl1 implements Type {}      // final - no further extension
public sealed class Impl2 implements Type {}     // sealed - controlled extension
public non-sealed class Impl3 implements Type {} // non-sealed - open extension
```

### **Text Block Features**
```java
// Basic text block
String text = """
              Content here
              Multiple lines
              """;

// With escape sequences
String escaped = """
                 Line 1 \
                 continues here
                 Line 2 with \s preserved spaces
                 """;

// With formatting
String formatted = """
                   Hello %s!
                   Today is %s.
                   """.formatted(name, date);
```

---

## 🎯 **What You'll Learn**

After completing this module, you'll understand:
- **Sealed Classes**: Controlling inheritance hierarchies for better type safety
- **Text Blocks**: Writing readable multi-line strings for various formats
- **Pattern Matching**: Exhaustive switching over sealed class hierarchies
- **Type Safety**: Building more robust and maintainable applications

---

## 📚 **Prerequisites**

- **Java 14 Features**: Records and pattern matching basics
- **Java 13 Features**: Text blocks preview understanding
- **Object-Oriented Programming**: Inheritance and polymorphism concepts
- **Pattern Matching**: Switch expressions and instanceof patterns

---

**🚀 Ready to build more controlled and type-safe Java applications? Let's explore Java 15's powerful modeling capabilities!**

**Next:** [Java 16 Features](../14.8-Java16/README.md) | **Previous:** [Java 14 Features](../14.6-Java14/README.md)