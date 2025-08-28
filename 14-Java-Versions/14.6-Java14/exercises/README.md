# Java 14 Features - Exercises

## 🎯 **Learning Objectives**

Master Java 14 features through hands-on exercises covering Records (Preview), Pattern Matching for instanceof (Preview), Text Blocks enhancements, and Helpful NullPointerExceptions.

---

## 📋 **Exercise Categories**

### **🟢 Beginner Level**
Focus on basic record syntax and simple pattern matching usage.

### **🟡 Intermediate Level** 
Work with complex records and advanced pattern matching for real-world scenarios.

### **🔴 Advanced Level**
Build complete data modeling systems and type-safe applications using Java 14 features.

---

## 🟢 **Beginner Exercises**

### **Exercise 1: Records Fundamentals**

**Task**: Create and use records to replace traditional data classes.

```java
public class RecordBasics {
    
    /**
     * Exercise 1a: Basic record creation
     * Create a Student record with validation
     */
    public record Student(String name, int age, String major, double gpa) {
        // TODO: Add compact constructor with validation
        // - name cannot be null or empty
        // - age must be between 16 and 65
        // - major cannot be null
        // - gpa must be between 0.0 and 4.0
        public Student {
            // TODO: Implement validation
        }
        
        // TODO: Add custom methods
        public boolean isHonorStudent() {
            // TODO: Return true if GPA >= 3.5
            return false;
        }
        
        public String getClassification() {
            // TODO: Return classification based on age
            // 16-18: "Freshman", 19-20: "Sophomore", 21-22: "Junior", 23+: "Senior"
            return null;
        }
    }
    
    /**
     * Exercise 1b: Book inventory record
     * Create a Book record for library management
     */
    public record Book(String isbn, String title, String author, double price, int stock) {
        // TODO: Add validation and custom methods
        // - ISBN format validation (basic)
        // - Price must be positive
        // - Stock cannot be negative
        
        public boolean isAvailable() {
            // TODO: Return true if stock > 0
            return false;
        }
        
        public double calculateTotalValue() {
            // TODO: Return price * stock
            return 0.0;
        }
    }
    
    /**
     * Exercise 1c: Employee payroll record
     */
    public record Employee(String id, String name, String department, double salary, LocalDate hireDate) {
        // TODO: Implement with validation and methods
        
        public int getYearsOfService() {
            // TODO: Calculate years from hire date to now
            return 0;
        }
        
        public double getMonthlyPay() {
            // TODO: Return monthly salary
            return 0.0;
        }
        
        public boolean isEligibleForBonus() {
            // TODO: Eligible if more than 1 year of service
            return false;
        }
    }
}

// Test your records
class RecordTest {
    public static void main(String[] args) {
        // Test Student record
        try {
            Student student = new Student("Alice Johnson", 20, "Computer Science", 3.8);
            System.out.println("Student: " + student);
            System.out.println("Honor student: " + student.isHonorStudent());
            System.out.println("Classification: " + student.getClassification());
        } catch (Exception e) {
            System.out.println("Validation error: " + e.getMessage());
        }
        
        // TODO: Test other records
    }
}
```

### **Exercise 2: Pattern Matching Fundamentals**

**Task**: Use pattern matching for instanceof to simplify type checking and casting.

```java
public class PatternMatchingBasics {
    
    /**
     * Exercise 2a: Basic type processing
     * Process different types of objects using pattern matching
     */
    public static String processValue(Object value) {
        // TODO: Use pattern matching for instanceof to handle:
        // - String: return "Text: [value] (length: [length])"
        // - Integer: return "Number: [value] (even/odd)"
        // - List: return "List with [size] elements"
        // - Double: return "Decimal: [value] (rounded: [rounded])"
        // - Boolean: return "Boolean: [value]"
        // - null: return "Null value"
        // - default: return "Unknown type: [className]"
        
        return null;
    }
    
    /**
     * Exercise 2b: Shape area calculator
     * Calculate area using pattern matching
     */
    public static double calculateArea(Shape shape) {
        // TODO: Use pattern matching to calculate area for:
        // - Circle: π * r²
        // - Rectangle: width * height
        // - Triangle: 0.5 * base * height
        // - Square: side²
        
        return 0.0;
    }
    
    /**
     * Exercise 2c: Animal sound processor
     * Generate animal sounds using pattern matching
     */
    public static String makeSound(Animal animal) {
        // TODO: Use pattern matching to return appropriate sounds:
        // - Dog: "Woof! [name] is barking"
        // - Cat: "Meow! [name] is purring"
        // - Bird: "[name] is chirping at [frequency] Hz"
        // - Fish: "[name] is swimming silently"
        
        return null;
    }
    
    /**
     * Exercise 2d: File processor
     * Process different file types using pattern matching
     */
    public static String processFile(File file) {
        // TODO: Use pattern matching to handle:
        // - TextFile: "Processing text file: [name] ([lines] lines)"
        // - ImageFile: "Processing image: [name] ([width]x[height])"
        // - AudioFile: "Processing audio: [name] ([duration] seconds)"
        // - VideoFile: "Processing video: [name] ([duration]s, [resolution])"
        
        return null;
    }
}

// Supporting interfaces and records for exercises
interface Shape {}
record Circle(double radius) implements Shape {}
record Rectangle(double width, double height) implements Shape {}
record Triangle(double base, double height) implements Shape {}
record Square(double side) implements Shape {}

interface Animal {}
record Dog(String name, String breed) implements Animal {}
record Cat(String name, int age) implements Animal {}
record Bird(String name, int frequency) implements Animal {}
record Fish(String name, String species) implements Animal {}

interface File {}
record TextFile(String name, int lines) implements File {}
record ImageFile(String name, int width, int height) implements File {}
record AudioFile(String name, int duration) implements File {}
record VideoFile(String name, int duration, String resolution) implements File {}
```

---

## 🟡 **Intermediate Exercises**

### **Exercise 3: Advanced Records Usage**

**Task**: Build complex data models using records with relationships and collections.

```java
public class AdvancedRecords {
    
    /**
     * Exercise 3a: Banking system records
     * Create a complete banking system using records
     */
    
    public record Address(String street, String city, String state, String zipCode) {
        // TODO: Add validation for zip code format
    }
    
    public record Customer(String id, String name, Address address, LocalDate birthDate) {
        // TODO: Add methods for age calculation and validation
        
        public int getAge() {
            // TODO: Calculate age from birth date
            return 0;
        }
        
        public boolean isAdult() {
            // TODO: Return true if age >= 18
            return false;
        }
    }
    
    public record Account(String accountNumber, String type, double balance, Customer owner) {
        // TODO: Add account operations and validation
        
        public Account deposit(double amount) {
            // TODO: Return new Account with updated balance
            return null;
        }
        
        public Account withdraw(double amount) {
            // TODO: Return new Account with updated balance (if sufficient funds)
            return null;
        }
        
        public boolean canWithdraw(double amount) {
            // TODO: Check if withdrawal is possible
            return false;
        }
    }
    
    public record Transaction(String id, Account from, Account to, double amount, LocalDateTime timestamp, String type) {
        // TODO: Add transaction validation and processing
        
        public boolean isValid() {
            // TODO: Validate transaction rules
            return false;
        }
    }
    
    /**
     * Exercise 3b: E-commerce system
     * Build product catalog using records
     */
    
    public record Category(String id, String name, String description) {}
    
    public record Product(String sku, String name, Category category, double price, int stock, List<String> tags) {
        // TODO: Add inventory management methods
        
        public boolean isInStock() {
            return stock > 0;
        }
        
        public Product updateStock(int newStock) {
            // TODO: Return new Product with updated stock
            return null;
        }
        
        public boolean hasTag(String tag) {
            // TODO: Check if product has specific tag
            return false;
        }
    }
    
    public record OrderItem(Product product, int quantity, double unitPrice) {
        // TODO: Add calculation methods
        
        public double getTotalPrice() {
            return quantity * unitPrice;
        }
        
        public boolean isDiscounted() {
            // TODO: Check if unit price is less than product price
            return false;
        }
    }
    
    public record Order(String orderId, Customer customer, List<OrderItem> items, LocalDateTime orderDate, String status) {
        // TODO: Add order processing methods
        
        public double getTotalAmount() {
            // TODO: Sum all order items
            return 0.0;
        }
        
        public int getTotalItems() {
            // TODO: Count total quantity of all items
            return 0;
        }
        
        public Order updateStatus(String newStatus) {
            // TODO: Return new Order with updated status
            return null;
        }
    }
}
```

### **Exercise 4: Advanced Pattern Matching**

**Task**: Implement complex business logic using pattern matching.

```java
public class AdvancedPatternMatching {
    
    /**
     * Exercise 4a: Expression evaluator
     * Build a mathematical expression evaluator using pattern matching
     */
    
    public sealed interface Expression permits Number, BinaryOperation, UnaryOperation {}
    
    public record Number(double value) implements Expression {}
    
    public record BinaryOperation(Expression left, String operator, Expression right) implements Expression {}
    
    public record UnaryOperation(String operator, Expression operand) implements Expression {}
    
    public static double evaluate(Expression expr) {
        // TODO: Use pattern matching to evaluate expressions
        // Support: +, -, *, /, %, ^, unary -, sqrt, abs
        
        return 0.0;
    }
    
    public static String toString(Expression expr) {
        // TODO: Convert expression to readable string using pattern matching
        
        return null;
    }
    
    /**
     * Exercise 4b: JSON processor
     * Process JSON-like structures using pattern matching
     */
    
    public sealed interface JsonValue permits JsonString, JsonNumber, JsonBoolean, JsonArray, JsonObject, JsonNull {}
    
    public record JsonString(String value) implements JsonValue {}
    public record JsonNumber(double value) implements JsonValue {}
    public record JsonBoolean(boolean value) implements JsonValue {}
    public record JsonArray(List<JsonValue> elements) implements JsonValue {}
    public record JsonObject(Map<String, JsonValue> fields) implements JsonValue {}
    public record JsonNull() implements JsonValue {}
    
    public static String jsonToString(JsonValue json) {
        // TODO: Convert JSON structure to string using pattern matching
        
        return null;
    }
    
    public static JsonValue findValue(JsonValue json, String path) {
        // TODO: Find value at given path (e.g., "user.address.city") using pattern matching
        
        return null;
    }
    
    /**
     * Exercise 4c: State machine processor
     * Implement a state machine using pattern matching
     */
    
    public sealed interface State permits Idle, Processing, Completed, Error {}
    
    public record Idle() implements State {}
    public record Processing(String task, int progress) implements State {}
    public record Completed(String result) implements State {}
    public record Error(String message, Exception cause) implements State {}
    
    public sealed interface Event permits StartEvent, ProgressEvent, CompleteEvent, ErrorEvent {}
    
    public record StartEvent(String task) implements Event {}
    public record ProgressEvent(int progress) implements Event {}
    public record CompleteEvent(String result) implements Event {}
    public record ErrorEvent(String message, Exception cause) implements Event {}
    
    public static State transition(State currentState, Event event) {
        // TODO: Implement state transitions using pattern matching
        // Define valid transitions and return new state
        
        return currentState;
    }
    
    public static String getStateDescription(State state) {
        // TODO: Return human-readable state description using pattern matching
        
        return null;
    }
}
```

---

## 🔴 **Advanced Exercises**

### **Exercise 5: Complete Application Architecture**

**Task**: Build a complete application using records and pattern matching for a library management system.

```java
public class LibraryManagementSystem {
    
    /**
     * Exercise 5a: Library domain model
     * Design complete library system using records
     */
    
    // TODO: Design records for:
    // - Author (id, name, birthDate, nationality, biography)
    // - Publisher (id, name, address, establishedDate)
    // - Book (isbn, title, authors, publisher, publishDate, categories, pages, language)
    // - Member (id, name, address, phone, email, membershipDate, membershipType)
    // - Loan (id, book, member, loanDate, dueDate, returnDate, status)
    // - Fine (id, member, amount, reason, issueDate, paidDate, status)
    // - Reservation (id, book, member, reservationDate, expiryDate, status)
    
    /**
     * Exercise 5b: Library operations processor
     * Implement library operations using pattern matching
     */
    
    public sealed interface LibraryOperation 
        permits CheckoutBook, ReturnBook, ReserveBook, PayFine, RenewBook, SearchBooks {}
    
    // TODO: Define operation records
    
    public sealed interface OperationResult 
        permits Success, Failure, Warning {}
    
    // TODO: Define result records
    
    public static OperationResult processOperation(LibraryOperation operation, LibraryState state) {
        // TODO: Process operations using pattern matching
        // Implement business rules for each operation type
        
        return null;
    }
    
    /**
     * Exercise 5c: Library reporting system
     * Generate reports using records and pattern matching
     */
    
    public sealed interface Report 
        permits MembershipReport, BookUtilizationReport, FinancialReport, OverdueReport {}
    
    public static String generateReport(Report reportType, LocalDate startDate, LocalDate endDate, LibraryData data) {
        // TODO: Generate different types of reports using pattern matching
        // Include statistics, charts (ASCII), and recommendations
        
        return null;
    }
}
```

### **Exercise 6: Data Processing Pipeline**

**Task**: Build a data processing pipeline using Java 14 features.

```java
public class DataProcessingPipeline {
    
    /**
     * Exercise 6a: ETL pipeline using records
     * Extract, Transform, Load pipeline for various data sources
     */
    
    public sealed interface DataSource permits CSVSource, JSONSource, XMLSource, DatabaseSource {}
    
    // TODO: Define source records with connection details
    
    public sealed interface DataRecord permits CustomerRecord, ProductRecord, OrderRecord, TransactionRecord {}
    
    // TODO: Define data records for different entity types
    
    public sealed interface Transformation permits Filter, Map, Aggregate, Join, Sort {}
    
    // TODO: Define transformation records with parameters
    
    public static List<DataRecord> processData(DataSource source, List<Transformation> transformations) {
        // TODO: Implement ETL pipeline using pattern matching
        // Handle different source types and apply transformations
        
        return List.of();
    }
    
    /**
     * Exercise 6b: Validation and error handling
     * Implement comprehensive validation using pattern matching
     */
    
    public sealed interface ValidationRule permits NotNull, Range, Pattern, Custom {}
    
    public sealed interface ValidationResult permits Valid, Invalid {}
    
    public record ValidationError(String field, String message, Object value) {}
    
    public static ValidationResult validateData(DataRecord record, List<ValidationRule> rules) {
        // TODO: Validate data using pattern matching on rules and record types
        
        return new Valid();
    }
    
    /**
     * Exercise 6c: Data quality analyzer
     * Analyze data quality using records and pattern matching
     */
    
    public record QualityMetrics(
        double completeness,
        double accuracy,
        double consistency,
        double validity,
        Map<String, Integer> errorCounts
    ) {}
    
    public static QualityMetrics analyzeDataQuality(List<DataRecord> records) {
        // TODO: Analyze data quality using pattern matching
        // Calculate metrics for different record types
        
        return null;
    }
}
```

### **Exercise 7: Real-time Event Processing System**

**Task**: Build a real-time event processing system using Java 14 features.

```java
public class EventProcessingSystem {
    
    /**
     * Exercise 7a: Event modeling with records
     * Model different types of events in a system
     */
    
    public sealed interface Event permits UserEvent, SystemEvent, BusinessEvent {}
    
    public sealed interface UserEvent permits Login, Logout, PageView, Purchase, Registration {}
    
    public sealed interface SystemEvent permits ServerStart, ServerStop, DatabaseConnection, ErrorOccurred {}
    
    public sealed interface BusinessEvent permits OrderPlaced, PaymentProcessed, InventoryUpdated, CustomerRegistered {}
    
    // TODO: Define specific event records with relevant data
    
    /**
     * Exercise 7b: Event processor with pattern matching
     * Process events based on type and content
     */
    
    public sealed interface ProcessingRule permits Filter, Transform, Aggregate, Alert, Store {}
    
    public sealed interface ProcessingResult permits Processed, Ignored, Error, Retry {}
    
    public static List<ProcessingResult> processEvents(List<Event> events, List<ProcessingRule> rules) {
        // TODO: Process events using pattern matching
        // Apply different processing rules based on event types
        
        return List.of();
    }
    
    /**
     * Exercise 7c: Real-time analytics
     * Generate real-time analytics from event streams
     */
    
    public record EventMetrics(
        long totalEvents,
        Map<String, Long> eventTypeCounts,
        double eventsPerSecond,
        Map<String, Double> averageProcessingTime
    ) {}
    
    public static EventMetrics calculateMetrics(List<Event> events, Duration timeWindow) {
        // TODO: Calculate real-time metrics using pattern matching and records
        
        return null;
    }
    
    /**
     * Exercise 7d: Event sourcing system
     * Implement event sourcing using records and pattern matching
     */
    
    public sealed interface Command permits CreateUser, UpdateUser, DeleteUser, PlaceOrder, CancelOrder {}
    
    public sealed interface EventStore permits InMemoryStore, DatabaseStore, FileStore {}
    
    public static List<Event> handleCommand(Command command, List<Event> eventHistory) {
        // TODO: Generate events from commands using pattern matching
        // Validate commands against current state derived from event history
        
        return List.of();
    }
    
    public static Object projectState(List<Event> events, Class<?> stateType) {
        // TODO: Project current state from event history using pattern matching
        
        return null;
    }
}
```

---

## 🧪 **Testing Your Solutions**

### **Unit Test Template**

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class Java14FeaturesTest {
    
    @Test
    void testRecordValidation() {
        // Test Student record validation
        assertThrows(IllegalArgumentException.class, () -> 
            new Student("", 20, "CS", 3.5));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Student("John", 15, "CS", 3.5));
        
        Student validStudent = new Student("John Doe", 20, "Computer Science", 3.8);
        assertTrue(validStudent.isHonorStudent());
        assertEquals("Sophomore", validStudent.getClassification());
    }
    
    @Test
    void testPatternMatching() {
        // Test pattern matching for different types
        assertEquals("Text: Hello (length: 5)", 
                    PatternMatchingBasics.processValue("Hello"));
        
        assertEquals("Number: 42 (even)", 
                    PatternMatchingBasics.processValue(42));
        
        // Test shape area calculation
        Circle circle = new Circle(5.0);
        double area = PatternMatchingBasics.calculateArea(circle);
        assertEquals(Math.PI * 25, area, 0.001);
    }
    
    @Test
    void testAdvancedRecords() {
        // Test banking system records
        Address address = new Address("123 Main St", "Anytown", "ST", "12345");
        Customer customer = new Customer("CUST001", "John Doe", address, LocalDate.of(1990, 1, 1));
        
        assertTrue(customer.isAdult());
        assertTrue(customer.getAge() > 30);
        
        Account account = new Account("ACC001", "CHECKING", 1000.0, customer);
        Account updated = account.deposit(500.0);
        assertEquals(1500.0, updated.balance());
    }
}
```

---

## 📚 **Additional Challenges**

### **Challenge 1: Compiler Design**
Build a simple compiler/interpreter using records and pattern matching:
- Abstract Syntax Tree (AST) nodes as records
- Pattern matching for code generation
- Type checking using sealed interfaces

### **Challenge 2: Game Engine Architecture**
Design a game engine using Java 14 features:
- Game entities as records
- Event system with pattern matching
- State management using sealed interfaces

### **Challenge 3: Microservices Communication**
Build a microservices communication layer:
- Message types as sealed interfaces and records
- Protocol handling with pattern matching
- Service discovery and routing

---

## 🎯 **Success Criteria**

**Beginner Level**: ✅ Complete Exercises 1-2
- Master record syntax and validation
- Understand basic pattern matching usage
- Handle simple type checking scenarios

**Intermediate Level**: ✅ Complete Exercises 3-4
- Build complex data models with records
- Implement business logic with pattern matching
- Create domain-specific processing systems

**Advanced Level**: ✅ Complete Exercises 5-7
- Design complete application architectures
- Build data processing pipelines
- Implement real-time event processing systems

---

## 💡 **Tips for Success**

1. **Records**: Use compact constructors for validation, immutable by default
2. **Pattern Matching**: Reduces boilerplate code and improves readability
3. **Sealed Interfaces**: Perfect for modeling closed sets of types
4. **Performance**: Records are optimized for data carrying
5. **Testing**: Validate both positive and negative cases for records

---

**🚀 Ready to master Java 14's data modeling capabilities? Start with records and build your way up to complete application architectures!**