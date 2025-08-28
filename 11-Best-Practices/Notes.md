# Best Practices - Quick Notes

## 🎯 **Core Programming Principles**

### **SOLID Principles**
- **S**ingle Responsibility: One class, one job
- **O**pen/Closed: Open for extension, closed for modification  
- **L**iskov Substitution: Subtypes must be substitutable
- **I**nterface Segregation: Many specific interfaces > one general
- **D**ependency Inversion: Depend on abstractions, not concretions

### **Clean Code Fundamentals**
- **Readable**: Code should tell a story
- **Simple**: Avoid unnecessary complexity
- **DRY**: Don't Repeat Yourself
- **KISS**: Keep It Simple, Stupid
- **YAGNI**: You Aren't Gonna Need It

---

## 🏗️ **Design Patterns**

### **Creational Patterns**

#### **Singleton**
```java
public class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    
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
}
```

#### **Builder**
```java
public class Computer {
    private final String cpu;
    private final int ram;
    
    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
    }
    
    public static class Builder {
        private String cpu;
        private int ram;
        
        public Builder cpu(String cpu) { this.cpu = cpu; return this; }
        public Builder ram(int ram) { this.ram = ram; return this; }
        public Computer build() { return new Computer(this); }
    }
}
```

#### **Factory**
```java
public class ShapeFactory {
    public static Shape createShape(String type) {
        switch (type.toLowerCase()) {
            case "circle": return new Circle();
            case "square": return new Square();
            default: throw new IllegalArgumentException("Unknown shape: " + type);
        }
    }
}
```

### **Behavioral Patterns**

#### **Observer**
```java
public interface Observer {
    void update(String message);
}

public class Subject {
    private List<Observer> observers = new ArrayList<>();
    
    public void addObserver(Observer observer) { observers.add(observer); }
    public void notifyObservers(String message) {
        observers.forEach(obs -> obs.update(message));
    }
}
```

#### **Strategy**
```java
public interface PaymentStrategy {
    void pay(double amount);
}

public class ShoppingCart {
    private PaymentStrategy paymentStrategy;
    
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }
    
    public void checkout(double amount) {
        paymentStrategy.pay(amount);
    }
}
```

---

## 📝 **Code Quality Guidelines**

### **Naming Conventions**
```java
// Classes: PascalCase
public class UserAccountManager

// Methods & Variables: camelCase  
public void calculateMonthlyPayment()
private int accountBalance

// Constants: UPPER_SNAKE_CASE
public static final int MAX_RETRY_COUNT = 3

// Packages: lowercase
package com.company.project.utils
```

### **Method Design**
```java
// Good: Single responsibility, clear name
public boolean isEligibleForDiscount(Customer customer) {
    return customer.getAge() > 65 || customer.isMember();
}

// Bad: Multiple responsibilities
public void processUserDataAndSendEmailAndLogActivity(User user) {
    // Too many responsibilities
}
```

### **Class Design**
```java
// Good: Focused, cohesive
public class EmailValidator {
    public boolean isValid(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }
}

// Bad: Multiple unrelated responsibilities
public class UserUtils {
    public void sendEmail() { }
    public void calculateTax() { }
    public void processPayment() { }
}
```

---

## 🔒 **Exception Handling Best Practices**

### **Do's**
```java
// Catch specific exceptions
try {
    processFile(filename);
} catch (FileNotFoundException e) {
    log.error("File not found: " + filename, e);
    showUserFriendlyMessage("File not found");
} catch (IOException e) {
    log.error("IO error processing file", e);
    showUserFriendlyMessage("Unable to process file");
}

// Use try-with-resources
try (FileReader reader = new FileReader(file)) {
    // File operations
} // Automatically closed

// Custom exceptions for business logic
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(double requested, double available) {
        super(String.format("Requested: $%.2f, Available: $%.2f", requested, available));
    }
}
```

### **Don'ts**
```java
// Don't catch and ignore
try {
    riskyOperation();
} catch (Exception e) {
    // Silent failure - BAD!
}

// Don't catch generic Exception unless necessary
try {
    specificOperation();
} catch (Exception e) { // Too broad
    // Handle
}
```

---

## 🚀 **Performance Best Practices**

### **String Operations**
```java
// Bad: Creates many objects
String result = "";
for (int i = 0; i < 1000; i++) {
    result += "item" + i;
}

// Good: Efficient string building
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append("item").append(i);
}
String result = sb.toString();
```

### **Collection Selection**
```java
// ArrayList: Fast random access, slow insertion/deletion
List<String> list = new ArrayList<>(); 

// LinkedList: Slow access, fast insertion/deletion
List<String> list = new LinkedList<>();

// HashMap: Fast key-based access
Map<String, String> map = new HashMap<>();

// Use appropriate initial capacity
List<String> list = new ArrayList<>(expectedSize);
```

### **Loop Optimization**
```java
// Good: Cache size
int size = list.size();
for (int i = 0; i < size; i++) {
    process(list.get(i));
}

// Better: Enhanced for loop (when index not needed)
for (String item : list) {
    process(item);
}

// Best: Streams for complex operations
list.stream()
    .filter(this::isValid)
    .map(this::transform)
    .collect(Collectors.toList());
```

---

## 💾 **Memory Management**

### **Object Creation**
```java
// Reuse objects when possible
private final StringBuilder buffer = new StringBuilder();

public String formatMessage(String template, Object... args) {
    buffer.setLength(0); // Clear buffer
    return buffer.append(template).append(args).toString();
}

// Use object pools for expensive objects
public class ConnectionPool {
    private final Queue<Connection> pool = new ConcurrentLinkedQueue<>();
    
    public Connection acquire() {
        Connection conn = pool.poll();
        return conn != null ? conn : createNewConnection();
    }
    
    public void release(Connection conn) {
        if (conn.isValid()) {
            pool.offer(conn);
        }
    }
}
```

### **Resource Management**
```java
// Always close resources
try (FileInputStream fis = new FileInputStream(file);
     BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
    
    return reader.readLine();
} // Automatic cleanup

// For manual management
Connection conn = null;
try {
    conn = getConnection();
    // Use connection
} finally {
    if (conn != null) {
        conn.close();
    }
}
```

---

## 🧪 **Testing Best Practices**

### **Unit Test Structure**
```java
@Test
public void calculateDiscount_seniorCustomer_returnsCorrectDiscount() {
    // Arrange
    Customer customer = new Customer("John", 70);
    DiscountCalculator calculator = new DiscountCalculator();
    
    // Act  
    double discount = calculator.calculateDiscount(customer);
    
    // Assert
    assertEquals(0.15, discount, 0.001);
}
```

### **Test Naming**
```java
// Pattern: methodName_condition_expectedResult
@Test public void withdraw_sufficientFunds_returnsTrue() { }
@Test public void withdraw_insufficientFunds_returnsFalse() { }
@Test public void withdraw_negativeAmount_throwsException() { }
```

---

## ⚡ **Quick Reference Checklist**

### **Code Quality ✅**
- [ ] Single responsibility per class/method
- [ ] Meaningful names (no abbreviations)
- [ ] Methods < 20 lines
- [ ] Classes < 300 lines  
- [ ] Consistent formatting
- [ ] Remove commented code
- [ ] Add JavaDoc for public APIs

### **Performance ✅**
- [ ] Use StringBuilder for string concatenation
- [ ] Choose appropriate collection types
- [ ] Cache expensive computations
- [ ] Use streams judiciously
- [ ] Avoid premature optimization
- [ ] Profile before optimizing

### **Security ✅**
- [ ] Validate all inputs
- [ ] Sanitize user data
- [ ] Use parameterized queries
- [ ] Don't log sensitive data
- [ ] Handle exceptions gracefully
- [ ] Use secure random generators

### **Maintainability ✅**
- [ ] Write self-documenting code
- [ ] Extract magic numbers to constants
- [ ] Use design patterns appropriately
- [ ] Follow SOLID principles
- [ ] Write comprehensive tests
- [ ] Keep dependencies minimal

---

## 🛡️ **Common Anti-Patterns to Avoid**

### **God Class**
```java
// BAD: Does everything
public class UserManager {
    public void createUser() { }
    public void sendEmail() { }
    public void processPayment() { }
    public void generateReports() { }
    public void manageDatabase() { }
}
```

### **Long Parameter List**
```java
// BAD: Too many parameters
public void createUser(String name, String email, int age, 
                      String address, String phone, String city, 
                      String country, String zipCode) { }

// GOOD: Use object
public void createUser(UserInfo userInfo) { }
```

### **Magic Numbers**
```java
// BAD: Magic numbers
if (user.getAge() > 65) { }

// GOOD: Named constants  
private static final int SENIOR_AGE_THRESHOLD = 65;
if (user.getAge() > SENIOR_AGE_THRESHOLD) { }
```

---

## 🎯 **Professional Development Tips**

1. **Code Reviews**: Always review and be reviewed
2. **Continuous Learning**: Stay updated with Java versions
3. **Documentation**: Write clear JavaDoc and README files  
4. **Version Control**: Use Git effectively with clear commit messages
5. **IDE Proficiency**: Master your development environment
6. **Debugging Skills**: Learn to use debuggers and profilers
7. **Team Collaboration**: Follow team coding standards

---

**🏆 Congratulations! You've completed the Java Fundamentals course!**