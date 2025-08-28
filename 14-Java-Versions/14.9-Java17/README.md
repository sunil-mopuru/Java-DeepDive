# Module 14.9: Java 17 Features (LTS)

## 🎯 **Overview**

Java 17 (September 2021) is the latest **Long Term Support (LTS)** release, bringing together several major innovations including **Sealed Classes**, **Enhanced Pattern Matching**, and significant **JVM improvements**. This module covers the comprehensive feature set that makes Java 17 the recommended version for production applications.

---

## 🚀 **Key Features**

### **🔥 Major Language Features**
- **Sealed Classes**: Controlled inheritance hierarchies
- **Pattern Matching for switch (Preview)**: Advanced pattern matching
- **Records (Standard)**: Immutable data classes now standard
- **Text Blocks (Standard)**: Multi-line strings now standard

### **📈 Platform & Performance**
- **Strong Encapsulation**: JDK internals strongly encapsulated by default
- **Foreign Function & Memory API (Incubator)**: Native code integration
- **Vector API (Second Incubator)**: SIMD operations support
- **Context-Specific Deserialization Filters**: Enhanced security

### **🛠️ JVM & Runtime Improvements**
- **New macOS Rendering Pipeline**: Better graphics performance
- **Deprecate Security Manager**: Preparing for future removal
- **Remove RMI Activation**: Simplifying the platform
- **Enhanced Pseudo-Random Generators**: Better randomness APIs

---

## 📚 **Detailed Feature Guide**

### **1. Sealed Classes (Standard Feature)**

Sealed classes provide fine-grained control over which classes can extend or implement them, enabling more secure and maintainable inheritance hierarchies.

#### **Basic Sealed Class Syntax**
```java
// Define a sealed interface with permitted implementations
public sealed interface Shape 
    permits Circle, Rectangle, Triangle {
    double area();
    double perimeter();
}

// Permitted classes must be final, sealed, or non-sealed
public final class Circle implements Shape {
    private final double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }
    
    public double getRadius() { return radius; }
}

public final class Rectangle implements Shape {
    private final double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() {
        return width * height;
    }
    
    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
    
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}

// Non-sealed allows further extension
public non-sealed class Triangle implements Shape {
    private final double a, b, c; // sides
    
    public Triangle(double a, double b, double c) {
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException("Invalid triangle sides");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public double area() {
        double s = (a + b + c) / 2; // semi-perimeter
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
    
    @Override
    public double perimeter() {
        return a + b + c;
    }
}

// Since Triangle is non-sealed, we can extend it
public class RightTriangle extends Triangle {
    public RightTriangle(double base, double height) {
        super(base, height, Math.sqrt(base * base + height * height));
    }
}
```

#### **Advanced Sealed Class Patterns**
```java
// Sealed class hierarchies for domain modeling
public sealed class Vehicle 
    permits Car, Truck, Motorcycle {
    
    protected final String make;
    protected final String model;
    protected final int year;
    
    protected Vehicle(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }
    
    // Common methods
    public abstract double getFuelEfficiency();
    public abstract int getMaxPassengers();
    
    public String getDescription() {
        return String.format("%d %s %s", year, make, model);
    }
}

public final class Car extends Vehicle {
    private final int doors;
    private final boolean isElectric;
    
    public Car(String make, String model, int year, int doors, boolean isElectric) {
        super(make, model, year);
        this.doors = doors;
        this.isElectric = isElectric;
    }
    
    @Override
    public double getFuelEfficiency() {
        return isElectric ? 120.0 : 28.5; // MPGe for electric, MPG for gas
    }
    
    @Override
    public int getMaxPassengers() {
        return doors == 2 ? 2 : 5;
    }
    
    public boolean isElectric() { return isElectric; }
}

public sealed class Truck extends Vehicle 
    permits PickupTruck, SemiTruck {
    
    protected final double payloadCapacity;
    
    protected Truck(String make, String model, int year, double payloadCapacity) {
        super(make, model, year);
        this.payloadCapacity = payloadCapacity;
    }
    
    public double getPayloadCapacity() { return payloadCapacity; }
}

public final class PickupTruck extends Truck {
    private final boolean hasFourWheelDrive;
    
    public PickupTruck(String make, String model, int year, 
                      double payloadCapacity, boolean hasFourWheelDrive) {
        super(make, model, year, payloadCapacity);
        this.hasFourWheelDrive = hasFourWheelDrive;
    }
    
    @Override
    public double getFuelEfficiency() {
        return hasFourWheelDrive ? 18.0 : 22.0;
    }
    
    @Override
    public int getMaxPassengers() {
        return 5;
    }
}
```

#### **Sealed Classes with Records**
```java
// Combining sealed classes with records for data modeling
public sealed interface Result<T> 
    permits Result.Success, Result.Error {
    
    record Success<T>(T value) implements Result<T> { }
    record Error<T>(String message, Throwable cause) implements Result<T> { 
        public Error(String message) {
            this(message, null);
        }
    }
    
    // Convenience factory methods
    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }
    
    static <T> Result<T> error(String message) {
        return new Error<>(message);
    }
    
    static <T> Result<T> error(String message, Throwable cause) {
        return new Error<>(message, cause);
    }
}

// Usage with pattern matching
public class ResultProcessor {
    
    public <T> void processResult(Result<T> result) {
        switch (result) {
            case Result.Success<T> success -> {
                System.out.println("Success: " + success.value());
                handleSuccess(success.value());
            }
            case Result.Error<T> error -> {
                System.err.println("Error: " + error.message());
                if (error.cause() != null) {
                    error.cause().printStackTrace();
                }
                handleError(error);
            }
        }
    }
    
    public <T> T getValueOrDefault(Result<T> result, T defaultValue) {
        return switch (result) {
            case Result.Success<T> success -> success.value();
            case Result.Error<T> error -> {
                System.err.println("Using default due to error: " + error.message());
                yield defaultValue;
            }
        };
    }
}
```

### **2. Enhanced Pattern Matching**

Java 17 continues to evolve pattern matching capabilities, building on the foundation from Java 14-16.

```java
public class EnhancedPatternMatching {
    
    // Pattern matching with sealed classes
    public String describeShape(Shape shape) {
        return switch (shape) {
            case Circle c -> "Circle with radius " + c.getRadius();
            case Rectangle r -> String.format("Rectangle %.1fx%.1f", r.getWidth(), r.getHeight());
            case Triangle t -> "Triangle with perimeter " + t.perimeter();
        };
    }
    
    // Pattern matching with guards (conditions)
    public String categorizeVehicle(Vehicle vehicle) {
        return switch (vehicle) {
            case Car car when car.isElectric() -> "Electric Car";
            case Car car when !car.isElectric() -> "Gas Car";
            case PickupTruck truck when truck.getPayloadCapacity() > 2000 -> "Heavy Duty Truck";
            case PickupTruck truck -> "Light Duty Truck";
            case SemiTruck semi -> "Commercial Semi";
            case Motorcycle motorcycle -> "Motorcycle";
        };
    }
    
    // Complex pattern matching scenarios
    public double calculateInsuranceRate(Vehicle vehicle, int driverAge, int yearsLicensed) {
        return switch (vehicle) {
            case Car car when driverAge < 25 -> {
                double baseRate = car.isElectric() ? 800 : 1200;
                yield baseRate * (yearsLicensed < 3 ? 1.5 : 1.2);
            }
            case Car car when driverAge >= 65 -> {
                double baseRate = car.isElectric() ? 600 : 900;
                yield baseRate * 0.9; // Senior discount
            }
            case Car car -> {
                double baseRate = car.isElectric() ? 700 : 1000;
                yield baseRate;
            }
            case Truck truck when truck.getPayloadCapacity() > 5000 -> 2500;
            case Truck truck -> 1800;
            case Motorcycle motorcycle -> 1500;
        };
    }
}
```

### **3. Strong Encapsulation**

Java 17 strongly encapsulates JDK internals by default, improving security and maintainability.

```java
public class EncapsulationExamples {
    
    public void demonstrateEncapsulation() {
        // These would fail in Java 17 (previously accessible)
        try {
            // This will throw IllegalAccessError
            // sun.misc.Unsafe unsafe = sun.misc.Unsafe.getUnsafe();
            System.out.println("JDK internals are now properly encapsulated");
        } catch (Exception e) {
            System.out.println("Access denied: " + e.getMessage());
        }
        
        // Use public APIs instead
        usePublicAPIs();
    }
    
    private void usePublicAPIs() {
        // Use java.lang.foreign for native access (incubator)
        // Use java.util.concurrent for concurrency
        // Use java.nio for I/O operations
        // Use public reflection APIs for metadata access
        
        var threadFactory = Thread.ofVirtual().factory();
        var executor = Executors.newThreadPerTaskExecutor(threadFactory);
        
        System.out.println("Using proper public APIs");
    }
}
```

### **4. Enhanced Random Number Generators**

Java 17 introduces new random number generator interfaces and implementations.

```java
import java.util.random.*;

public class RandomGeneratorExamples {
    
    public void demonstrateNewRandomAPIs() {
        // Different random number generator algorithms
        RandomGenerator defaultRng = RandomGenerator.getDefault();
        RandomGenerator secureRng = RandomGeneratorFactory.of("SecureRandom").create();
        RandomGenerator xoshiroRng = RandomGeneratorFactory.of("Xoshiro256PlusPlus").create();
        
        // Generate different types of random values
        System.out.println("Random int: " + defaultRng.nextInt());
        System.out.println("Random double [0,1): " + defaultRng.nextDouble());
        System.out.println("Random boolean: " + defaultRng.nextBoolean());
        
        // Generate bounded random values
        System.out.println("Random int [0,100): " + defaultRng.nextInt(100));
        System.out.println("Random double [10,20): " + defaultRng.nextDouble(10, 20));
        
        // Generate streams of random numbers
        defaultRng.ints(5, 1, 101)
                 .forEach(i -> System.out.print(i + " "));
        System.out.println();
    }
    
    // Custom random generator for specific use cases
    public void demonstrateCustomRandomUsage() {
        var rng = RandomGeneratorFactory.of("Xoroshiro128PlusPlus").create(12345L);
        
        // Generate test data
        List<String> testData = rng.ints(10, 65, 91) // ASCII A-Z
            .mapToObj(i -> String.valueOf((char) i))
            .collect(Collectors.toList());
        
        System.out.println("Test data: " + testData);
        
        // Shuffle collections
        List<Integer> numbers = IntStream.rangeClosed(1, 10)
            .boxed()
            .collect(Collectors.toList());
        
        Collections.shuffle(numbers, rng);
        System.out.println("Shuffled: " + numbers);
    }
}
```

---

## 🛠️ **Practical Examples**

### **State Machine with Sealed Classes**
```java
public sealed interface OrderState 
    permits Draft, Submitted, Confirmed, Shipped, Delivered, Cancelled {
}

public record Draft(String customerId, List<OrderItem> items) implements OrderState {
    public Submitted submit() {
        return new Submitted(customerId, items, Instant.now());
    }
}

public record Submitted(String customerId, List<OrderItem> items, Instant submittedAt) implements OrderState {
    public Confirmed confirm(String confirmationNumber) {
        return new Confirmed(customerId, items, submittedAt, confirmationNumber, Instant.now());
    }
    
    public Cancelled cancel(String reason) {
        return new Cancelled(customerId, items, reason, Instant.now());
    }
}

// State machine processor
public class OrderStateMachine {
    
    public String getStateDescription(OrderState state) {
        return switch (state) {
            case Draft draft -> "Draft order with " + draft.items().size() + " items";
            case Submitted submitted -> "Order submitted at " + submitted.submittedAt();
            case Confirmed confirmed -> "Order confirmed: " + confirmed.confirmationNumber();
            case Shipped shipped -> "Order shipped via " + shipped.carrier();
            case Delivered delivered -> "Order delivered at " + delivered.deliveredAt();
            case Cancelled cancelled -> "Order cancelled: " + cancelled.reason();
        };
    }
    
    public boolean canTransitionTo(OrderState current, Class<? extends OrderState> target) {
        return switch (current) {
            case Draft draft -> target == Submitted.class;
            case Submitted submitted -> target == Confirmed.class || target == Cancelled.class;
            case Confirmed confirmed -> target == Shipped.class || target == Cancelled.class;
            case Shipped shipped -> target == Delivered.class;
            case Delivered delivered, Cancelled cancelled -> false;
        };
    }
}
```

### **HTTP Client with Result Type**
```java
public class HttpClientExample {
    private final HttpClient httpClient;
    
    public HttpClientExample() {
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    }
    
    public Result<String> fetchData(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(
                request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return Result.success(response.body());
            } else {
                return Result.error("HTTP " + response.statusCode());
            }
            
        } catch (Exception e) {
            return Result.error("Failed to fetch data", e);
        }
    }
    
    public void processApiResponse(String url) {
        var result = fetchData(url);
        
        switch (result) {
            case Result.Success<String> success -> {
                System.out.println("Data received: " + success.value().length() + " characters");
                processData(success.value());
            }
            case Result.Error<String> error -> {
                System.err.println("Failed to fetch data: " + error.message());
                if (error.cause() != null) {
                    System.err.println("Caused by: " + error.cause().getMessage());
                }
            }
        }
    }
}
```

---

## ⚡ **Migration and Best Practices**

### **Migration from Java 11 to Java 17**
```java
public class MigrationGuide {
    
    // Replace instanceof casting with pattern matching
    public void modernizeInstanceofChecks() {
        Object obj = getObject();
        
        // Old way (Java 11)
        if (obj instanceof String) {
            String str = (String) obj;
            System.out.println(str.toUpperCase());
        }
        
        // New way (Java 17)
        if (obj instanceof String str) {
            System.out.println(str.toUpperCase());
        }
    }
    
    // Use sealed classes for controlled hierarchies
    public void createControlledHierarchies() {
        // Define your domain models with sealed classes
        // This provides better type safety and exhaustiveness checking
    }
    
    // Leverage records for data classes
    public void useRecordsForData() {
        // Replace verbose POJOs with records
        // Automatic equals, hashCode, toString implementations
        // Immutability by default
    }
}
```

### **Best Practices for Java 17**
- ✅ **Use sealed classes** for controlled inheritance hierarchies
- ✅ **Leverage pattern matching** to eliminate casting boilerplate
- ✅ **Adopt records** for immutable data classes
- ✅ **Use text blocks** for multi-line strings
- ✅ **Utilize new random generators** for better randomness
- ❌ **Don't access JDK internals** - use public APIs instead

---

## 🎯 **What You'll Learn**

After completing this module, you'll understand:
- **Sealed Classes**: Creating controlled inheritance hierarchies
- **Advanced Pattern Matching**: Eliminating boilerplate with sophisticated matching
- **LTS Benefits**: Why Java 17 is the recommended production version
- **Platform Security**: Strong encapsulation and security improvements
- **Modern APIs**: Enhanced random generators and other new capabilities

---

## 📚 **Prerequisites**

- **Java 14-16 Features**: Records, pattern matching basics, text blocks
- **Java 11 Features**: HTTP client, string methods
- **Design Patterns**: Understanding of inheritance and polymorphism
- **Stream API**: Collection processing and modern Java patterns

---

**🚀 Ready to build secure, modern applications with Java 17 LTS? Let's master the latest Long Term Support features!**

**Next:** [Java 18-21+ Features](../14.10-Java18-21+/README.md) | **Previous:** [Java 16 Features](../14.8-Java16/README.md)