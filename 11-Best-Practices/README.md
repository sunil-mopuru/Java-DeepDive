# Chapter 11: Best Practices and Design Patterns

## 📚 Table of Contents
1. [Code Quality Principles](#code-quality-principles)
2. [Design Patterns](#design-patterns)
3. [SOLID Principles](#solid-principles)
4. [Performance Best Practices](#performance-best-practices)
5. [Security Best Practices](#security-best-practices)
6. [Testing Best Practices](#testing-best-practices)
7. [Documentation and Code Style](#documentation-and-code-style)

---

## Code Quality Principles

### **Clean Code Principles:**

#### **1. Meaningful Names:**
```java
// Bad
int d; // elapsed time in days
List<int[]> list1 = new ArrayList<>();

// Good
int elapsedTimeInDays;
List<Customer> customers = new ArrayList<>();
```

#### **2. Small Functions:**
```java
// Bad - function doing too much
public void processUser(User user) {
    // Validate user
    if (user.getName() == null || user.getEmail() == null) {
        throw new IllegalArgumentException("Invalid user");
    }
    
    // Save to database
    Connection conn = DriverManager.getConnection("...");
    PreparedStatement stmt = conn.prepareStatement("INSERT INTO users...");
    stmt.setString(1, user.getName());
    stmt.executeUpdate();
    
    // Send email
    EmailService.sendWelcomeEmail(user.getEmail());
    
    // Log action
    logger.info("User processed: " + user.getName());
}

// Good - single responsibility
public void processUser(User user) {
    validateUser(user);
    saveUser(user);
    sendWelcomeEmail(user);
    logUserProcessing(user);
}

private void validateUser(User user) {
    if (user.getName() == null || user.getEmail() == null) {
        throw new IllegalArgumentException("Invalid user");
    }
}

private void saveUser(User user) {
    userRepository.save(user);
}
```

#### **3. Don't Repeat Yourself (DRY):**
```java
// Bad - repeated logic
public double calculateCircleArea(double radius) {
    return 3.14159 * radius * radius;
}

public double calculateCircleCircumference(double radius) {
    return 2 * 3.14159 * radius;
}

// Good - extract constants
public class GeometryUtils {
    private static final double PI = Math.PI;
    
    public static double calculateCircleArea(double radius) {
        return PI * radius * radius;
    }
    
    public static double calculateCircleCircumference(double radius) {
        return 2 * PI * radius;
    }
}
```

---

## Design Patterns

### **1. Singleton Pattern:**
```java
public class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        // Initialize connection
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
}

// Better: Use enum for thread-safe singleton
public enum DatabaseConnectionEnum {
    INSTANCE;
    
    private Connection connection;
    
    private DatabaseConnectionEnum() {
        // Initialize connection
    }
    
    public Connection getConnection() {
        return connection;
    }
}
```

### **2. Factory Pattern:**
```java
// Product interface
public interface Vehicle {
    void start();
    void stop();
}

// Concrete products
public class Car implements Vehicle {
    @Override
    public void start() { System.out.println("Car started"); }
    
    @Override
    public void stop() { System.out.println("Car stopped"); }
}

public class Motorcycle implements Vehicle {
    @Override
    public void start() { System.out.println("Motorcycle started"); }
    
    @Override
    public void stop() { System.out.println("Motorcycle stopped"); }
}

// Factory
public class VehicleFactory {
    public static Vehicle createVehicle(String type) {
        switch (type.toLowerCase()) {
            case "car":
                return new Car();
            case "motorcycle":
                return new Motorcycle();
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
}

// Usage
Vehicle car = VehicleFactory.createVehicle("car");
car.start();
```

### **3. Observer Pattern:**
```java
import java.util.*;

// Subject interface
public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

// Observer interface
public interface Observer {
    void update(String message);
}

// Concrete Subject
public class NewsAgency implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String news;
    
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(news);
        }
    }
    
    public void setNews(String news) {
        this.news = news;
        notifyObservers();
    }
}

// Concrete Observer
public class NewsChannel implements Observer {
    private String name;
    
    public NewsChannel(String name) {
        this.name = name;
    }
    
    @Override
    public void update(String news) {
        System.out.println(name + " received news: " + news);
    }
}
```

### **4. Builder Pattern:**
```java
public class Computer {
    // Required parameters
    private final String cpu;
    private final int ram;
    
    // Optional parameters
    private final String gpu;
    private final boolean hasWifi;
    private final boolean hasBluetooth;
    
    private Computer(ComputerBuilder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.gpu = builder.gpu;
        this.hasWifi = builder.hasWifi;
        this.hasBluetooth = builder.hasBluetooth;
    }
    
    public static class ComputerBuilder {
        // Required parameters
        private final String cpu;
        private final int ram;
        
        // Optional parameters with defaults
        private String gpu = "Integrated";
        private boolean hasWifi = false;
        private boolean hasBluetooth = false;
        
        public ComputerBuilder(String cpu, int ram) {
            this.cpu = cpu;
            this.ram = ram;
        }
        
        public ComputerBuilder gpu(String gpu) {
            this.gpu = gpu;
            return this;
        }
        
        public ComputerBuilder wifi(boolean hasWifi) {
            this.hasWifi = hasWifi;
            return this;
        }
        
        public ComputerBuilder bluetooth(boolean hasBluetooth) {
            this.hasBluetooth = hasBluetooth;
            return this;
        }
        
        public Computer build() {
            return new Computer(this);
        }
    }
}

// Usage
Computer gamingPC = new Computer.ComputerBuilder("Intel i9", 32)
    .gpu("RTX 4080")
    .wifi(true)
    .bluetooth(true)
    .build();
```

### **5. Strategy Pattern:**
```java
// Strategy interface
public interface PaymentStrategy {
    void pay(double amount);
}

// Concrete strategies
public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    
    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using credit card " + cardNumber);
    }
}

public class PayPalPayment implements PaymentStrategy {
    private String email;
    
    public PayPalPayment(String email) {
        this.email = email;
    }
    
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using PayPal account " + email);
    }
}

// Context
public class ShoppingCart {
    private PaymentStrategy paymentStrategy;
    
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    public void checkout(double amount) {
        paymentStrategy.pay(amount);
    }
}

// Usage
ShoppingCart cart = new ShoppingCart();
cart.setPaymentStrategy(new CreditCardPayment("1234-5678-9012-3456"));
cart.checkout(100.00);

cart.setPaymentStrategy(new PayPalPayment("user@example.com"));
cart.checkout(50.00);
```

---

## SOLID Principles

### **1. Single Responsibility Principle (SRP):**
```java
// Bad - multiple responsibilities
public class User {
    private String name;
    private String email;
    
    // User data responsibility
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    
    // Database responsibility (should be separate)
    public void save() {
        // Database logic
    }
    
    // Email responsibility (should be separate)
    public void sendEmail(String message) {
        // Email logic
    }
}

// Good - single responsibility
public class User {
    private String name;
    private String email;
    
    // Only user data responsibility
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}

public class UserRepository {
    public void save(User user) {
        // Database logic
    }
}

public class EmailService {
    public void sendEmail(String email, String message) {
        // Email logic
    }
}
```

### **2. Open/Closed Principle (OCP):**
```java
// Bad - modification required for new shapes
public class AreaCalculator {
    public double calculateArea(Object shape) {
        if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            return rect.getLength() * rect.getWidth();
        } else if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            return Math.PI * circle.getRadius() * circle.getRadius();
        }
        // Need to modify this method for new shapes
        return 0;
    }
}

// Good - open for extension, closed for modification
public abstract class Shape {
    public abstract double calculateArea();
}

public class Rectangle extends Shape {
    private double length;
    private double width;
    
    @Override
    public double calculateArea() {
        return length * width;
    }
}

public class Circle extends Shape {
    private double radius;
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

public class AreaCalculator {
    public double calculateArea(Shape shape) {
        return shape.calculateArea(); // No modification needed for new shapes
    }
}
```

### **3. Liskov Substitution Principle (LSP):**
```java
// Good - proper inheritance
public abstract class Bird {
    public abstract void eat();
    public abstract void makeSound();
}

public abstract class FlyingBird extends Bird {
    public abstract void fly();
}

public class Eagle extends FlyingBird {
    @Override
    public void eat() { System.out.println("Eagle eats fish"); }
    
    @Override
    public void makeSound() { System.out.println("Eagle screeches"); }
    
    @Override
    public void fly() { System.out.println("Eagle soars high"); }
}

public class Penguin extends Bird { // Not extending FlyingBird
    @Override
    public void eat() { System.out.println("Penguin eats fish"); }
    
    @Override
    public void makeSound() { System.out.println("Penguin squawks"); }
}
```

### **4. Interface Segregation Principle (ISP):**
```java
// Bad - fat interface
public interface Worker {
    void work();
    void eat();
    void sleep();
}

// Good - segregated interfaces
public interface Workable {
    void work();
}

public interface Eater {
    void eat();
}

public interface Sleeper {
    void sleep();
}

public class Human implements Workable, Eater, Sleeper {
    @Override
    public void work() { System.out.println("Human works"); }
    
    @Override
    public void eat() { System.out.println("Human eats"); }
    
    @Override
    public void sleep() { System.out.println("Human sleeps"); }
}

public class Robot implements Workable {
    @Override
    public void work() { System.out.println("Robot works"); }
    // Robot doesn't need to eat or sleep
}
```

### **5. Dependency Inversion Principle (DIP):**
```java
// Bad - high-level module depends on low-level module
public class EmailService {
    public void sendEmail(String message) {
        // Email implementation
    }
}

public class NotificationService {
    private EmailService emailService = new EmailService(); // Direct dependency
    
    public void send(String message) {
        emailService.sendEmail(message);
    }
}

// Good - depend on abstractions
public interface MessageService {
    void sendMessage(String message);
}

public class EmailService implements MessageService {
    @Override
    public void sendMessage(String message) {
        // Email implementation
    }
}

public class SMSService implements MessageService {
    @Override
    public void sendMessage(String message) {
        // SMS implementation
    }
}

public class NotificationService {
    private MessageService messageService;
    
    public NotificationService(MessageService messageService) {
        this.messageService = messageService; // Dependency injection
    }
    
    public void send(String message) {
        messageService.sendMessage(message);
    }
}
```

---

## Performance Best Practices

### **1. Collections and Data Structures:**
```java
// Use appropriate collection types
Map<String, User> users = new HashMap<>(); // O(1) lookup
Set<String> uniqueIds = new HashSet<>();   // O(1) contains check

// Initialize with proper capacity
List<String> items = new ArrayList<>(1000); // Avoid resizing
Map<String, Object> cache = new HashMap<>(256, 0.75f);

// Use StringBuilder for string concatenation
StringBuilder sb = new StringBuilder();
for (String item : items) {
    sb.append(item).append(", ");
}
String result = sb.toString();
```

### **2. Object Creation and Memory:**
```java
// Reuse objects when possible
private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

// Use object pools for expensive objects
public class DatabaseConnectionPool {
    private final Queue<Connection> pool = new LinkedList<>();
    
    public Connection borrowConnection() {
        return pool.poll();
    }
    
    public void returnConnection(Connection conn) {
        pool.offer(conn);
    }
}

// Prefer primitive collections for large datasets
TIntArrayList intList = new TIntArrayList(); // Trove4J library
// Instead of List<Integer> which uses boxing
```

### **3. Algorithm Optimization:**
```java
// Use efficient algorithms
public class SearchUtils {
    
    // Binary search for sorted arrays - O(log n)
    public static <T extends Comparable<T>> int binarySearch(T[] array, T target) {
        int left = 0, right = array.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = array[mid].compareTo(target);
            
            if (comparison == 0) return mid;
            else if (comparison < 0) left = mid + 1;
            else right = mid - 1;
        }
        
        return -1;
    }
    
    // Cache expensive computations
    private static final Map<Integer, Long> fibonacciCache = new HashMap<>();
    
    public static long fibonacci(int n) {
        return fibonacciCache.computeIfAbsent(n, key -> {
            if (key <= 1) return key;
            return fibonacci(key - 1) + fibonacci(key - 2);
        });
    }
}
```

---

## Security Best Practices

### **1. Input Validation:**
```java
public class SecurityUtils {
    
    public static String sanitizeInput(String input) {
        if (input == null) return null;
        
        return input.trim()
                   .replaceAll("[<>\"'&]", "")  // Remove potential XSS characters
                   .substring(0, Math.min(input.length(), 1000)); // Limit length
    }
    
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        return email != null && email.matches(regex);
    }
    
    public static void validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
    }
}
```

### **2. Secure Coding Practices:**
```java
public class SecureBankAccount {
    private double balance;
    
    // Always validate inputs
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalStateException("Insufficient funds");
        }
        
        balance -= amount;
    }
    
    // Use defensive copying for mutable objects
    private List<Transaction> transactions = new ArrayList<>();
    
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions); // Return copy, not original
    }
    
    // Don't expose internal state
    public double getBalance() {
        return balance; // Primitive, safe to return
    }
}
```

### **3. Exception Handling Security:**
```java
public class SecureFileReader {
    
    public String readFile(String filename) {
        // Validate file path to prevent directory traversal
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new SecurityException("Invalid file path");
        }
        
        try {
            return Files.readString(Paths.get("safe-directory", filename));
        } catch (IOException e) {
            // Don't expose system details in error messages
            logger.error("File read error for file: " + filename, e);
            throw new RuntimeException("Unable to read file");
        }
    }
}
```

---

## Testing Best Practices

### **1. Unit Testing:**
```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {
    private Calculator calculator;
    
    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }
    
    @Test
    void testAddition() {
        assertEquals(5, calculator.add(2, 3));
        assertEquals(-1, calculator.add(-2, 1));
        assertEquals(0, calculator.add(0, 0));
    }
    
    @Test
    void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> {
            calculator.divide(10, 0);
        });
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13})
    void testPositiveNumbers(int number) {
        assertTrue(number > 0);
    }
}
```

### **2. Test Organization:**
```java
// Follow AAA pattern: Arrange, Act, Assert
@Test
void testUserRegistration() {
    // Arrange
    UserService userService = new UserService();
    String email = "test@example.com";
    String password = "securePassword123";
    
    // Act
    User user = userService.register(email, password);
    
    // Assert
    assertNotNull(user);
    assertEquals(email, user.getEmail());
    assertTrue(user.getId() > 0);
}
```

---

## Documentation and Code Style

### **1. Javadoc Documentation:**
```java
/**
 * Utility class for mathematical operations.
 * 
 * @author John Doe
 * @version 1.0
 * @since 2024-01-01
 */
public class MathUtils {
    
    /**
     * Calculates the factorial of a given number.
     * 
     * @param n the number to calculate factorial for (must be non-negative)
     * @return the factorial of n
     * @throws IllegalArgumentException if n is negative
     * @see #fibonacci(int)
     */
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
```

### **2. Code Style Guidelines:**
```java
// Use consistent naming conventions
public class UserAccountService {  // PascalCase for classes
    private static final int MAX_ATTEMPTS = 3;  // UPPER_CASE for constants
    
    private UserRepository userRepository;  // camelCase for fields and methods
    
    public void processUserAccount(String userId) {  // camelCase for methods
        // Method implementation
    }
}

// Organize imports properly
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServlet;

import com.company.project.User;
import com.company.project.UserService;

// Use meaningful variable names
List<Customer> activeCustomers = customerService.findActiveCustomers();
for (Customer customer : activeCustomers) {
    processCustomerOrder(customer);
}
```

---

## Key Takeaways

### **Essential Best Practices:**
✅ **Clean Code** - Write readable, maintainable code  
✅ **Design Patterns** - Reusable solutions to common problems  
✅ **SOLID Principles** - Object-oriented design principles  
✅ **Performance** - Optimize algorithms and data structures  
✅ **Security** - Validate inputs and handle errors securely  
✅ **Testing** - Write comprehensive unit and integration tests  
✅ **Documentation** - Clear Javadoc and code comments  

### **Code Quality Checklist:**
- [ ] **Meaningful names** for classes, methods, and variables
- [ ] **Single responsibility** for each class and method
- [ ] **Proper error handling** with specific exceptions
- [ ] **Input validation** for all public methods
- [ ] **Unit tests** with good coverage
- [ ] **Documentation** for public APIs
- [ ] **Consistent code style** throughout the project
- [ ] **Performance considerations** for critical paths

### **Design Guidelines:**
- **Favor composition over inheritance**
- **Program to interfaces, not implementations**
- **Use dependency injection** for better testability
- **Keep classes and methods small and focused**
- **Handle exceptions at the appropriate level**
- **Use immutable objects** when possible

### **Final Thoughts:**
Writing good Java code is not just about making it work—it's about making it:
- **Readable** for other developers
- **Maintainable** for future changes
- **Testable** for quality assurance
- **Performant** for production use
- **Secure** against common vulnerabilities

---

**🎉 Congratulations! You have completed the Java Fundamentals course!**

## What's Next?
- **Practice** with real-world projects
- **Learn frameworks** like Spring, Hibernate
- **Explore advanced topics** like microservices, reactive programming
- **Contribute to open-source** projects
- **Keep learning** - Java and software development are constantly evolving

Happy Coding! 🚀