# Java 17 LTS Features - Exercises

## 🎯 **Learning Objectives**

Master Java 17 LTS features through hands-on exercises covering Sealed Classes (Standard), enhanced Pattern Matching, Switch Expression improvements, and new APIs.

---

## 📋 **Exercise Categories**

### **🟢 Beginner Level**
Focus on basic sealed classes and simple pattern matching usage.

### **🟡 Intermediate Level** 
Work with complex sealed hierarchies and advanced switch expressions for business logic.

### **🔴 Advanced Level**
Build complete enterprise systems using Java 17 LTS features for production applications.

---

## 🟢 **Beginner Exercises**

### **Exercise 1: Sealed Classes Fundamentals**

**Task**: Create sealed class hierarchies for type-safe domain modeling.

```java
public class SealedClassBasics {
    
    /**
     * Exercise 1a: Transportation system
     * Create a sealed interface for different vehicle types
     */
    public sealed interface Vehicle permits Car, Motorcycle, Truck {
        double getFuelEfficiency();
        String getVehicleInfo();
    }
    
    // TODO: Implement the permitted classes
    public record Car(String brand, String model, int doors, double engineSize) implements Vehicle {
        // TODO: Implement methods
        @Override
        public double getFuelEfficiency() {
            // Calculate fuel efficiency based on engine size and type
            return 0.0;
        }
        
        @Override
        public String getVehicleInfo() {
            return null;
        }
    }
    
    // TODO: Complete Motorcycle and Truck records
    
    /**
     * Exercise 1b: Payment processing system
     * Create sealed classes for different payment methods
     */
    public sealed interface PaymentMethod permits CreditCard, DebitCard, DigitalWallet {
        boolean processPayment(double amount);
        String getPaymentDetails();
    }
    
    // TODO: Implement payment method records with validation
    
    /**
     * Exercise 1c: Employee hierarchy
     * Create sealed classes for different employee types
     */
    public sealed interface Employee permits Manager, Developer, Designer, Tester {
        double calculateSalary();
        String getDepartment();
        List<String> getResponsibilities();
    }
    
    // TODO: Implement employee records with specific attributes
}
```

### **Exercise 2: Pattern Matching Basics**

**Task**: Use enhanced pattern matching for data processing.

```java
public class PatternMatchingBasics {
    
    /**
     * Exercise 2a: Data type processor
     * Process different data types using pattern matching
     */
    public static String analyzeData(Object data) {
        // TODO: Use pattern matching with guards to analyze:
        // - Strings: length analysis, content type
        // - Numbers: range analysis, even/odd
        // - Collections: size analysis, type detection
        // - Maps: key-value analysis
        return null;
    }
    
    /**
     * Exercise 2b: Shape calculator
     * Calculate properties of different shapes
     */
    public static double calculateArea(Shape shape) {
        // TODO: Use pattern matching to calculate area
        // Support Circle, Rectangle, Triangle with validation
        return 0.0;
    }
    
    public static String classifyShape(Shape shape) {
        // TODO: Classify shapes based on properties
        // Use guards for special cases (e.g., square, equilateral triangle)
        return null;
    }
    
    /**
     * Exercise 2c: Response handler
     * Handle HTTP responses with pattern matching
     */
    public static String handleResponse(HttpResponse response) {
        // TODO: Handle different response types:
        // - Success responses (200-299): extract data
        // - Client errors (400-499): show user-friendly message
        // - Server errors (500-599): log and show generic message
        return null;
    }
}

// Supporting sealed interfaces
sealed interface Shape permits Circle, Rectangle, Triangle {}
record Circle(double radius) implements Shape {}
record Rectangle(double width, double height) implements Shape {}
record Triangle(double a, double b, double c) implements Shape {}

sealed interface HttpResponse permits SuccessResponse, ClientErrorResponse, ServerErrorResponse {}
record SuccessResponse(int code, String data) implements HttpResponse {}
record ClientErrorResponse(int code, String message) implements HttpResponse {}
record ServerErrorResponse(int code, String error) implements HttpResponse {}
```

---

## 🟡 **Intermediate Exercises**

### **Exercise 3: Business Logic with Sealed Classes**

**Task**: Implement complex business systems using sealed class hierarchies.

```java
public class BusinessLogicSystem {
    
    /**
     * Exercise 3a: Order management system
     */
    public sealed interface Order permits StandardOrder, ExpressOrder, 
                                       SubscriptionOrder, BulkOrder {
        String getOrderId();
        double calculateTotal();
        Duration getProcessingTime();
        List<String> getRequiredServices();
    }
    
    // TODO: Implement order types with specific business logic
    public record StandardOrder(String orderId, List<OrderItem> items, 
                               Address shippingAddress) implements Order {
        // TODO: Implement business methods
    }
    
    public record ExpressOrder(String orderId, List<OrderItem> items, 
                              Address shippingAddress, Duration requestedDelivery) implements Order {
        // TODO: Add express handling logic
    }
    
    // TODO: Complete other order types
    
    /**
     * Exercise 3b: Financial transaction system
     */
    public sealed interface Transaction permits Payment, Refund, Transfer, 
                                             Chargeback, Fee {
        String getTransactionId();
        double getAmount();
        TransactionResult process();
        boolean requiresApproval();
    }
    
    // TODO: Implement transaction types with validation and processing logic
    
    /**
     * Exercise 3c: User permission system
     */
    public sealed interface Permission permits ReadPermission, WritePermission, 
                                            AdminPermission, CustomPermission {
        boolean isAllowed(String resource, String action);
        Set<String> getAllowedActions();
        PermissionLevel getLevel();
    }
    
    // TODO: Implement permission types with hierarchy validation
}
```

### **Exercise 4: Advanced Pattern Matching**

**Task**: Build sophisticated pattern matching logic for complex scenarios.

```java
public class AdvancedPatternMatching {
    
    /**
     * Exercise 4a: Configuration processor
     * Process different configuration formats
     */
    public static ConfigResult processConfiguration(Object config) {
        // TODO: Use pattern matching to handle:
        // - JSON objects (Maps)
        // - YAML objects (nested structures)
        // - Properties (key-value pairs)
        // - Environment variables (String patterns)
        return null;
    }
    
    /**
     * Exercise 4b: Message router
     * Route messages based on type and content
     */
    public static RoutingResult routeMessage(Message message) {
        return switch (message) {
            // TODO: Implement routing logic with guards
            // - Route based on message type, priority, destination
            // - Handle special cases (emergency, bulk, scheduled)
            case EmailMessage email when email.priority() == Priority.HIGH -> 
                new RoutingResult("express-email-queue", Duration.ofMinutes(1));
            // TODO: Complete other cases
            default -> new RoutingResult("default-queue", Duration.ofMinutes(5));
        };
    }
    
    /**
     * Exercise 4c: Data validation engine
     */
    public static ValidationResult validateData(Object data, ValidationRule rule) {
        // TODO: Implement comprehensive validation using pattern matching
        // Support different data types and validation rules
        return null;
    }
    
    /**
     * Exercise 4d: State machine processor
     */
    public static StateTransitionResult processStateTransition(
            ApplicationState currentState, Event event) {
        // TODO: Implement state machine logic using pattern matching
        // Handle complex state transitions with validation
        return null;
    }
}

// Supporting types
sealed interface Message permits EmailMessage, SMSMessage, PushNotification, WebhookMessage {}
record EmailMessage(String to, String subject, String body, Priority priority) implements Message {}
record SMSMessage(String phoneNumber, String text, boolean international) implements Message {}
// TODO: Complete other message types

enum Priority { LOW, MEDIUM, HIGH, CRITICAL }

record RoutingResult(String queue, Duration processingTime) {}
record ValidationResult(boolean valid, List<String> errors) {}
record StateTransitionResult(ApplicationState newState, List<String> actions) {}
record ConfigResult(Map<String, Object> config, List<String> warnings) {}
```

---

## 🔴 **Advanced Exercises**

### **Exercise 5: Enterprise Application Architecture**

**Task**: Build a complete enterprise application using Java 17 LTS features.

```java
public class EnterpriseApplication {
    
    /**
     * Exercise 5a: Microservices communication
     */
    public class ServiceCommunicationLayer {
        
        public sealed interface ServiceRequest permits QueryRequest, CommandRequest, 
                                                   EventRequest, HealthCheckRequest {}
        
        public sealed interface ServiceResponse permits SuccessResponse, ErrorResponse, 
                                                     PartialResponse, AsyncResponse {}
        
        // TODO: Implement comprehensive service communication system
        public ServiceResponse handleRequest(ServiceRequest request) {
            return switch (request) {
                // TODO: Handle different request types with proper routing
                case QueryRequest query -> processQuery(query);
                case CommandRequest command -> processCommand(command);
                case EventRequest event -> processEvent(event);
                case HealthCheckRequest health -> processHealthCheck();
            };
        }
        
        // TODO: Implement request processors
    }
    
    /**
     * Exercise 5b: Event sourcing system
     */
    public class EventSourcingSystem {
        
        public sealed interface DomainEvent permits UserCreated, OrderPlaced, 
                                                 PaymentProcessed, InventoryUpdated {}
        
        public sealed interface EventResult permits EventAccepted, EventRejected, 
                                                 EventDeferred {}
        
        // TODO: Implement event sourcing with sealed classes
        public EventResult processEvent(DomainEvent event) {
            // TODO: Process events with business rule validation
            return null;
        }
        
        public List<DomainEvent> replayEvents(String aggregateId, Instant from) {
            // TODO: Replay events for aggregate reconstruction
            return List.of();
        }
    }
    
    /**
     * Exercise 5c: CQRS implementation
     */
    public class CQRSSystem {
        
        public sealed interface Query permits FindByIdQuery, SearchQuery, 
                                           AggregateQuery, ReportQuery {}
        
        public sealed interface Command permits CreateCommand, UpdateCommand, 
                                             DeleteCommand, BulkCommand {}
        
        // TODO: Implement CQRS pattern with sealed classes
        public <T> QueryResult<T> executeQuery(Query query) {
            // TODO: Execute queries with proper result handling
            return null;
        }
        
        public CommandResult executeCommand(Command command) {
            // TODO: Execute commands with validation and event generation
            return null;
        }
    }
}
```

### **Exercise 6: High-Performance Data Processing**

**Task**: Build high-performance data processing systems using Java 17 features.

```java
public class HighPerformanceDataProcessing {
    
    /**
     * Exercise 6a: Stream processing pipeline
     */
    public class StreamProcessor {
        
        public sealed interface DataStream permits RealTimeStream, BatchStream, 
                                               HistoricalStream {}
        
        public sealed interface ProcessingResult permits Success, PartialFailure, 
                                                     CompleteFailure, Retry {}
        
        // TODO: Implement high-throughput stream processing
        public ProcessingResult processStream(DataStream stream, 
                                           List<ProcessingStage> stages) {
            // TODO: Process data streams with error handling and performance monitoring
            return null;
        }
        
        // TODO: Implement parallel processing with sealed classes
        public List<ProcessingResult> processParallel(List<DataStream> streams) {
            // TODO: Use parallel streams with proper resource management
            return List.of();
        }
    }
    
    /**
     * Exercise 6b: Caching system
     */
    public class CacheSystem {
        
        public sealed interface CacheEntry permits HotEntry, WarmEntry, ColdEntry {}
        
        public sealed interface CacheOperation permits Get, Put, Evict, Refresh {}
        
        // TODO: Implement intelligent caching with sealed classes
        public <T> Optional<T> performOperation(CacheOperation operation, String key) {
            return switch (operation) {
                // TODO: Handle cache operations with performance optimization
                case Get get -> handleGet(key);
                case Put put -> handlePut(key, put.value());
                case Evict evict -> handleEvict(key);
                case Refresh refresh -> handleRefresh(key);
            };
        }
    }
    
    /**
     * Exercise 6c: Analytics engine
     */
    public class AnalyticsEngine {
        
        public sealed interface MetricType permits Counter, Gauge, Timer, 
                                               Histogram, Summary {}
        
        public sealed interface AnalysisResult permits Trend, Anomaly, 
                                                   Correlation, Forecast {}
        
        // TODO: Implement real-time analytics with sealed classes
        public List<AnalysisResult> analyzeMetrics(List<Metric> metrics, 
                                                 AnalysisConfiguration config) {
            // TODO: Perform complex analytics using pattern matching
            return List.of();
        }
    }
}
```

### **Exercise 7: Production-Ready Systems**

**Task**: Build production-ready systems with comprehensive error handling and monitoring.

```java
public class ProductionSystems {
    
    /**
     * Exercise 7a: Monitoring and alerting system
     */
    public class MonitoringSystem {
        
        public sealed interface Alert permits InfoAlert, WarningAlert, 
                                           ErrorAlert, CriticalAlert {}
        
        public sealed interface AlertResult permits Sent, Throttled, 
                                                Failed, Ignored {}
        
        // TODO: Implement comprehensive monitoring with sealed classes
        public List<AlertResult> processAlerts(List<Alert> alerts) {
            return alerts.stream()
                        .map(this::processAlert)
                        .toList();
        }
        
        private AlertResult processAlert(Alert alert) {
            return switch (alert) {
                // TODO: Handle different alert types with appropriate actions
                case InfoAlert info -> handleInfoAlert(info);
                case WarningAlert warning -> handleWarningAlert(warning);
                case ErrorAlert error -> handleErrorAlert(error);
                case CriticalAlert critical -> handleCriticalAlert(critical);
            };
        }
    }
    
    /**
     * Exercise 7b: Circuit breaker pattern
     */
    public class CircuitBreakerSystem {
        
        public sealed interface CircuitState permits Closed, Open, HalfOpen {}
        
        public sealed interface OperationResult permits Success, Failure, 
                                                    Timeout, CircuitOpen {}
        
        // TODO: Implement circuit breaker with state management
        public <T> OperationResult executeWithCircuitBreaker(
                Supplier<T> operation, String serviceName) {
            // TODO: Execute operations with circuit breaker protection
            return null;
        }
    }
    
    /**
     * Exercise 7c: Distributed tracing
     */
    public class TracingSystem {
        
        public sealed interface TraceEvent permits SpanStart, SpanEnd, 
                                               LogEvent, MetricEvent {}
        
        public sealed interface TraceResult permits TraceCompleted, TracePartial, 
                                                TraceFailed {}
        
        // TODO: Implement distributed tracing with sealed classes
        public TraceResult processTrace(List<TraceEvent> events) {
            // TODO: Process trace events and generate insights
            return null;
        }
    }
}
```

---

## 🧪 **Testing Your Solutions**

### **Unit Test Template**

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class Java17LTSTest {
    
    @Test
    void testSealedClasses() {
        // Test sealed class functionality
        Vehicle car = new Car("Toyota", "Camry", 4, 2.0);
        
        String info = switch (car) {
            case Car c -> "Car: " + c.brand();
            case Motorcycle m -> "Motorcycle: " + m.brand();
            case Truck t -> "Truck: " + t.brand();
        };
        
        assertTrue(info.contains("Toyota"));
        assertTrue(car.getFuelEfficiency() > 0);
    }
    
    @Test
    void testPatternMatchingWithGuards() {
        Object data = "Java 17 LTS";
        
        String result = switch (data) {
            case String s when s.length() > 10 -> "Long string: " + s;
            case String s -> "Short string: " + s;
            case Integer i when i > 100 -> "Large number: " + i;
            case Integer i -> "Small number: " + i;
            default -> "Unknown type";
        };
        
        assertTrue(result.startsWith("Long string"));
    }
    
    @Test
    void testRandomGenerators() {
        RandomGenerator rng = RandomGenerator.getDefault();
        
        int[] numbers = rng.ints(10, 1, 100).toArray();
        assertEquals(10, numbers.length);
        
        for (int num : numbers) {
            assertTrue(num >= 1 && num < 100);
        }
    }
}
```

---

## 🎯 **Success Criteria**

**Beginner Level**: ✅ Complete Exercises 1-2
- Master sealed classes for type safety
- Use basic pattern matching effectively
- Handle simple business scenarios

**Intermediate Level**: ✅ Complete Exercises 3-4
- Build complex business logic systems
- Implement advanced pattern matching
- Create enterprise-grade solutions

**Advanced Level**: ✅ Complete Exercises 5-7
- Design production-ready applications
- Build high-performance systems
- Implement comprehensive monitoring and error handling

---

## 💡 **Tips for Success**

1. **Sealed Classes**: Use for closed hierarchies and type safety
2. **Pattern Matching**: Leverage guards for complex conditions
3. **Switch Expressions**: Prefer over traditional switch statements
4. **Performance**: Java 17 LTS offers significant performance improvements
5. **Production**: Focus on monitoring, error handling, and scalability

---

**🚀 Ready to master Java 17 LTS? Build production-ready applications with enhanced type safety and performance!**