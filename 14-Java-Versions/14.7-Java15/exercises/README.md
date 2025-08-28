# Java 15 Features - Exercises

## 🎯 **Learning Objectives**

Master Java 15 features through hands-on exercises covering Sealed Classes (Preview), standardized Text Blocks, and enhanced Records (Second Preview) for building type-safe and maintainable applications.

---

## 📋 **Exercise Categories**

### **🟢 Beginner Level**
Focus on basic sealed class syntax and text block usage.

### **🟡 Intermediate Level** 
Work with complex sealed hierarchies and advanced text block formatting.

### **🔴 Advanced Level**
Build complete type-safe systems using sealed classes and records.

---

## 🟢 **Beginner Exercises**

### **Exercise 1: Basic Sealed Classes**

**Task**: Create sealed class hierarchies to model different domains with controlled inheritance.

```java
public class SealedClassBasics {
    
    /**
     * Exercise 1a: Weather condition hierarchy
     * Create a sealed interface for weather conditions
     */
    public sealed interface WeatherCondition permits Sunny, Rainy, Snowy, Cloudy {
        String getDescription();
        boolean isOutdoorFriendly();
    }
    
    // TODO: Implement the permitted classes
    public record Sunny(int temperature, int uvIndex) implements WeatherCondition {
        // TODO: Implement methods
        @Override
        public String getDescription() {
            return null; // TODO: Return description with temperature and UV index
        }
        
        @Override
        public boolean isOutdoorFriendly() {
            return false; // TODO: Sunny is outdoor friendly if temp is reasonable
        }
    }
    
    public record Rainy(int temperature, int rainfall) implements WeatherCondition {
        // TODO: Implement methods
    }
    
    public record Snowy(int temperature, int snowfall) implements WeatherCondition {
        // TODO: Implement methods
    }
    
    public record Cloudy(int temperature, int cloudCover) implements WeatherCondition {
        // TODO: Implement methods
    }
    
    /**
     * Exercise 1b: Transportation mode hierarchy
     */
    public sealed interface Transportation permits Car, Bus, Train, Bicycle, Walking {
        int getEstimatedTime(int distanceKm);
        double getCostPer10Km();
    }
    
    // TODO: Implement transportation records with appropriate fields and methods
    
    /**
     * Exercise 1c: Game character hierarchy
     */
    public sealed interface GameCharacter permits Warrior, Mage, Archer, Healer {
        int getHealth();
        int attack();
        String getSpecialAbility();
    }
    
    // TODO: Implement character records with stats and abilities
    
    /**
     * Exercise 1d: Processing methods using pattern matching
     */
    public static String processWeather(WeatherCondition weather) {
        // TODO: Use pattern matching to return appropriate activity recommendations
        return null;
    }
    
    public static double calculateTripCost(Transportation transport, int distance) {
        // TODO: Calculate total trip cost using pattern matching
        return 0.0;
    }
    
    public static String battleAction(GameCharacter character) {
        // TODO: Return battle description using pattern matching
        return null;
    }
}

// Test your sealed classes
class SealedClassTest {
    public static void main(String[] args) {
        // Test weather conditions
        WeatherCondition sunny = new Sunny(25, 7);
        WeatherCondition rainy = new Rainy(18, 5);
        
        System.out.println("Weather: " + sunny.getDescription());
        System.out.println("Outdoor friendly: " + sunny.isOutdoorFriendly());
        System.out.println("Activity: " + SealedClassBasics.processWeather(sunny));
        
        // TODO: Test other hierarchies
    }
}
```

### **Exercise 2: Text Block Applications**

**Task**: Use standardized text blocks to generate various formatted content.

```java
public class TextBlockApplications {
    
    /**
     * Exercise 2a: SQL query builder
     * Create complex SQL queries using text blocks
     */
    public static String buildUserAnalyticsQuery(String tableName, String dateColumn, int daysPast) {
        // TODO: Build a comprehensive analytics query using text blocks
        // Include: user activity, engagement metrics, trending data
        // Use parameters for table name, date column, and time period
        return null;
    }
    
    /**
     * Exercise 2b: HTML report generator
     * Generate HTML reports with embedded CSS using text blocks
     */
    public static String generateUserReport(User user, List<Activity> activities) {
        // TODO: Create a complete HTML report with:
        // - Inline CSS styling
        // - User information section
        // - Activity summary table
        // - Charts (ASCII art or simple HTML)
        return null;
    }
    
    /**
     * Exercise 2c: Configuration file generator
     * Generate different configuration formats
     */
    public static String generateYamlConfig(AppConfig config) {
        // TODO: Generate YAML configuration using text blocks
        // Include all config sections with proper indentation
        return null;
    }
    
    public static String generateDockerfile(String baseImage, String appName, int port) {
        // TODO: Generate a complete Dockerfile using text blocks
        // Include: base image, dependencies, app setup, health checks
        return null;
    }
    
    /**
     * Exercise 2d: Email template system
     */
    public static String generateWelcomeEmail(String userName, String companyName) {
        // TODO: Create a professional HTML welcome email
        return null;
    }
    
    public static String generateInvoiceEmail(Invoice invoice) {
        // TODO: Create an invoice email with itemized billing
        return null;
    }
    
    public static String generatePasswordResetEmail(String userName, String resetToken) {
        // TODO: Create a password reset email with security instructions
        return null;
    }
}

// Supporting classes to implement
class User {
    private String name, email, role;
    private LocalDate joinDate;
    // TODO: Implement constructors and getters
}

class Activity {
    private String action, description;
    private LocalDateTime timestamp;
    // TODO: Implement
}

class AppConfig {
    private String appName, environment;
    private int port;
    private DatabaseConfig database;
    // TODO: Implement with nested config objects
}

class Invoice {
    private String invoiceNumber, customerName;
    private List<InvoiceItem> items;
    private double totalAmount;
    // TODO: Implement
}
```

---

## 🟡 **Intermediate Exercises**

### **Exercise 3: Advanced Sealed Class Design**

**Task**: Design complex systems using sealed classes for type safety and exhaustive pattern matching.

```java
public class AdvancedSealedDesign {
    
    /**
     * Exercise 3a: HTTP API response system
     * Design a comprehensive API response hierarchy
     */
    public sealed interface ApiResponse<T> permits ApiSuccess, ApiError, ApiRedirect {}
    
    public record ApiSuccess<T>(T data, Map<String, String> headers, int statusCode) implements ApiResponse<T> {}
    
    public sealed interface ApiError<T> permits ValidationError, AuthenticationError, ServerError implements ApiResponse<T> {}
    
    public record ValidationError<T>(List<FieldError> errors, String message) implements ApiError<T> {}
    public record AuthenticationError<T>(String reason, String loginUrl) implements ApiError<T> {}
    public record ServerError<T>(String error, String requestId, int retryAfter) implements ApiError<T> {}
    
    public record ApiRedirect<T>(String location, int statusCode) implements ApiResponse<T> {}
    
    // TODO: Implement processing methods
    public static <T> String handleResponse(ApiResponse<T> response) {
        // TODO: Use pattern matching to handle all response types
        return null;
    }
    
    /**
     * Exercise 3b: State machine for order processing
     * Build a complete order state machine using sealed classes
     */
    public sealed interface OrderState permits Draft, Submitted, Processing, Shipped, Delivered, Cancelled, Returned {}
    
    // TODO: Implement state records with relevant data
    
    public sealed interface OrderEvent permits SubmitOrder, StartProcessing, ShipOrder, DeliverOrder, CancelOrder, ReturnOrder {}
    
    // TODO: Implement event records
    
    public static OrderState processEvent(OrderState currentState, OrderEvent event) {
        // TODO: Implement state transitions using pattern matching
        // Define valid transitions for each state-event combination
        return null;
    }
    
    /**
     * Exercise 3c: Expression parser and evaluator
     * Build a mathematical expression system
     */
    public sealed interface Expression permits Number, Variable, BinaryOperation, UnaryOperation, FunctionCall {}
    
    // TODO: Implement expression hierarchy
    
    public static double evaluate(Expression expr, Map<String, Double> variables) {
        // TODO: Evaluate expressions with variable substitution
        return 0.0;
    }
    
    public static Expression simplify(Expression expr) {
        // TODO: Simplify expressions (e.g., 0 + x = x, x * 1 = x)
        return expr;
    }
    
    public static Expression derivative(Expression expr, String variable) {
        // TODO: Calculate symbolic derivatives
        return new Number(0);
    }
}
```

### **Exercise 4: Complex Text Block Systems**

**Task**: Build sophisticated text generation systems using text blocks.

```java
public class ComplexTextSystems {
    
    /**
     * Exercise 4a: Code generator framework
     * Generate various types of code using text blocks
     */
    public static String generateJavaClass(ClassDefinition classDef) {
        // TODO: Generate complete Java class with:
        // - Package and imports
        // - Class annotations
        // - Fields with proper types
        // - Constructors
        // - Getters/setters
        // - Business methods
        // - toString, equals, hashCode
        return null;
    }
    
    public static String generateRestController(String entityName, List<String> operations) {
        // TODO: Generate Spring Boot REST controller
        // Include all CRUD operations, error handling, validation
        return null;
    }
    
    public static String generateDatabaseSchema(List<TableDefinition> tables) {
        // TODO: Generate SQL schema with:
        // - Table definitions
        // - Indexes
        // - Foreign key constraints
        // - Triggers
        return null;
    }
    
    /**
     * Exercise 4b: Documentation generator
     */
    public static String generateApiDocumentation(List<ApiEndpoint> endpoints) {
        // TODO: Generate comprehensive API documentation
        // Include: overview, authentication, endpoints, examples, error codes
        return null;
    }
    
    public static String generateUserGuide(Application app, List<Feature> features) {
        // TODO: Generate user guide with:
        // - Table of contents
        // - Feature descriptions
        // - Step-by-step tutorials
        // - Troubleshooting section
        return null;
    }
    
    /**
     * Exercise 4c: Report generation system
     */
    public static String generateFinancialReport(FinancialData data, ReportPeriod period) {
        // TODO: Generate financial report with:
        // - Executive summary
        // - Revenue analysis
        // - Expense breakdown
        // - Profit/loss statements
        // - Charts (ASCII art)
        return null;
    }
    
    public static String generatePerformanceReport(List<Metric> metrics, TimeRange range) {
        // TODO: Generate performance report with:
        // - Key performance indicators
        // - Trend analysis
        // - Bottleneck identification
        // - Recommendations
        return null;
    }
}

// Supporting classes for exercises
class ClassDefinition {
    private String packageName, className;
    private List<Field> fields;
    private List<Method> methods;
    // TODO: Implement
}

class ApiEndpoint {
    private String path, method, description;
    private List<Parameter> parameters;
    // TODO: Implement
}
```

---

## 🔴 **Advanced Exercises**

### **Exercise 5: Complete Application Architecture**

**Task**: Design a complete e-commerce system using sealed classes and records.

```java
public class ECommerceSystem {
    
    /**
     * Exercise 5a: Product catalog hierarchy
     */
    public sealed interface Product permits PhysicalProduct, DigitalProduct, ServiceProduct {}
    
    public sealed interface PhysicalProduct permits Electronics, Clothing, Books, HomeGoods {}
    
    // TODO: Design complete product hierarchy with:
    // - Specific attributes for each product type
    // - Pricing strategies
    // - Shipping requirements
    // - Inventory management
    
    /**
     * Exercise 5b: Order processing system
     */
    public sealed interface OrderItem permits StandardItem, BundleItem, SubscriptionItem {}
    
    public sealed interface PaymentMethod permits CreditCard, PayPal, BankTransfer, Cryptocurrency {}
    
    public sealed interface PaymentResult permits PaymentSuccess, PaymentPending, PaymentFailed {}
    
    // TODO: Implement complete order processing with:
    // - Order validation
    // - Inventory checking
    // - Payment processing
    // - Shipping calculation
    // - Order fulfillment
    
    public static OrderProcessingResult processOrder(Order order) {
        // TODO: Implement end-to-end order processing
        return null;
    }
    
    /**
     * Exercise 5c: Recommendation engine
     */
    public sealed interface RecommendationType permits PersonalizedRecs, TrendingRecs, SimilarProductRecs {}
    
    public static List<ProductRecommendation> generateRecommendations(
            User user, 
            RecommendationType type, 
            int maxResults) {
        // TODO: Generate product recommendations using pattern matching
        return List.of();
    }
    
    /**
     * Exercise 5d: Inventory management
     */
    public sealed interface InventoryEvent permits StockReceived, ItemSold, ItemReturned, StockAdjusted {}
    
    public sealed interface InventoryAlert permits LowStock, OutOfStock, OverStock, ExpiryWarning {}
    
    public static List<InventoryAlert> processInventoryEvents(List<InventoryEvent> events) {
        // TODO: Process inventory events and generate alerts
        return List.of();
    }
}
```

### **Exercise 6: Real-time Event Processing**

**Task**: Build a real-time event processing system using sealed classes.

```java
public class EventProcessingSystem {
    
    /**
     * Exercise 6a: Event type hierarchy
     */
    public sealed interface Event permits UserEvent, SystemEvent, BusinessEvent, SecurityEvent {}
    
    public sealed interface UserEvent permits Login, Logout, PageView, Purchase, Registration {}
    public sealed interface SystemEvent permits ServerStart, ServerStop, DatabaseConnection, ServiceHealth {}
    public sealed interface BusinessEvent permits OrderPlaced, PaymentProcessed, InventoryUpdate {}
    public sealed interface SecurityEvent permits LoginAttempt, AccessDenied, SuspiciousActivity {}
    
    // TODO: Design complete event hierarchy with relevant data
    
    /**
     * Exercise 6b: Event processor
     */
    public sealed interface EventAction permits LogEvent, AlertAdmin, UpdateMetrics, TriggerWorkflow {}
    
    public static List<EventAction> processEvent(Event event) {
        // TODO: Process events and determine actions using pattern matching
        return List.of();
    }
    
    /**
     * Exercise 6c: Real-time analytics
     */
    public sealed interface MetricType permits Counter, Gauge, Histogram, Timer {}
    
    public static EventMetrics calculateRealTimeMetrics(List<Event> events, Duration window) {
        // TODO: Calculate real-time metrics using sealed class pattern matching
        return null;
    }
    
    /**
     * Exercise 6d: Alert system
     */
    public sealed interface Alert permits InfoAlert, WarningAlert, CriticalAlert, SecurityAlert {}
    
    public static List<Alert> generateAlerts(EventMetrics metrics, List<Threshold> thresholds) {
        // TODO: Generate alerts based on metrics and thresholds
        return List.of();
    }
}
```

### **Exercise 7: Code Analysis and Generation Framework**

**Task**: Build a code analysis and generation framework.

```java
public class CodeAnalysisFramework {
    
    /**
     * Exercise 7a: Abstract Syntax Tree (AST)
     */
    public sealed interface ASTNode permits ClassNode, MethodNode, FieldNode, StatementNode, ExpressionNode {}
    
    public sealed interface StatementNode permits Assignment, IfStatement, WhileLoop, ForLoop, Return {}
    public sealed interface ExpressionNode permits Literal, Variable, MethodCall, BinaryOperation {}
    
    // TODO: Design complete AST hierarchy
    
    /**
     * Exercise 7b: Code analyzer
     */
    public sealed interface CodeIssue permits SyntaxError, LogicError, PerformanceIssue, SecurityVulnerability {}
    
    public static List<CodeIssue> analyzeCode(ASTNode ast) {
        // TODO: Analyze code and identify issues using pattern matching
        return List.of();
    }
    
    /**
     * Exercise 7c: Code generator
     */
    public static String generateCode(ASTNode ast, CodeStyle style) {
        // TODO: Generate code from AST using pattern matching and text blocks
        return null;
    }
    
    /**
     * Exercise 7d: Code refactoring engine
     */
    public sealed interface RefactoringRule permits ExtractMethod, InlineVariable, RenameClass, MoveMethod {}
    
    public static ASTNode applyRefactoring(ASTNode ast, RefactoringRule rule) {
        // TODO: Apply refactoring rules using pattern matching
        return ast;
    }
}
```

---

## 🧪 **Testing Your Solutions**

### **Unit Test Template**

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class Java15FeaturesTest {
    
    @Test
    void testSealedClasses() {
        // Test weather condition
        WeatherCondition sunny = new Sunny(25, 5);
        assertTrue(sunny.isOutdoorFriendly());
        assertEquals("Sunny day with temperature 25°C", sunny.getDescription());
        
        // Test pattern matching
        String activity = SealedClassBasics.processWeather(sunny);
        assertNotNull(activity);
        assertTrue(activity.contains("outdoor"));
    }
    
    @Test
    void testTextBlocks() {
        String query = TextBlockApplications.buildUserAnalyticsQuery("users", "created_date", 30);
        
        assertThat(query).contains("SELECT");
        assertThat(query).contains("users");
        assertThat(query).contains("created_date");
        assertThat(query).contains("30");
    }
    
    @Test
    void testApiResponseHandling() {
        ApiResponse<String> success = new ApiSuccess<>("Hello World", Map.of(), 200);
        ApiResponse<String> error = new ValidationError<>(List.of(), "Invalid input");
        
        String successResult = AdvancedSealedDesign.handleResponse(success);
        String errorResult = AdvancedSealedDesign.handleResponse(error);
        
        assertTrue(successResult.contains("Success"));
        assertTrue(errorResult.contains("Error"));
    }
    
    @Test
    void testOrderStateMachine() {
        OrderState state = new Draft();
        OrderEvent submitEvent = new SubmitOrder();
        
        OrderState newState = AdvancedSealedDesign.processEvent(state, submitEvent);
        assertTrue(newState instanceof Submitted);
    }
}
```

---

## 📚 **Additional Challenges**

### **Challenge 1: Compiler Frontend**
Build a compiler frontend using sealed classes:
- Lexical analysis with token types
- Syntax analysis with AST nodes
- Semantic analysis with type checking
- Error reporting with detailed messages

### **Challenge 2: Game Engine Architecture**
Design a game engine using Java 15 features:
- Entity component system with sealed interfaces
- Event system for game events
- State management for game states
- Resource management with type safety

### **Challenge 3: Configuration Management System**
Build a configuration system:
- Multiple configuration formats (YAML, JSON, TOML)
- Environment-specific overrides
- Validation with detailed error reporting
- Hot reloading with change detection

---

## 🎯 **Success Criteria**

**Beginner Level**: ✅ Complete Exercises 1-2
- Master sealed class syntax and basic usage
- Understand text block formatting and applications
- Handle simple pattern matching scenarios

**Intermediate Level**: ✅ Complete Exercises 3-4
- Design complex sealed class hierarchies
- Build sophisticated text generation systems
- Implement state machines and expression evaluators

**Advanced Level**: ✅ Complete Exercises 5-7
- Create complete application architectures
- Build real-time processing systems
- Implement code analysis and generation frameworks

---

## 💡 **Tips for Success**

1. **Sealed Classes**: Use for closed type hierarchies where you control all implementations
2. **Pattern Matching**: Leverage exhaustive checking for type safety
3. **Text Blocks**: Perfect for multi-line content like SQL, HTML, JSON
4. **Type Safety**: Sealed classes prevent unwanted inheritance
5. **Testing**: Verify exhaustive pattern matching covers all cases

---

**🚀 Ready to build type-safe and maintainable Java applications? Master sealed classes and text blocks to create robust system architectures!**