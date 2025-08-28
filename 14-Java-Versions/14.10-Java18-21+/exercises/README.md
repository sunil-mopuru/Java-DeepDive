# Java 18-21+ Latest Features - Exercises

## 🎯 **Learning Objectives**

Master cutting-edge Java features through hands-on exercises covering Pattern Matching for Switch (Standard), Virtual Threads, Structured Concurrency, and latest API enhancements.

---

## 📋 **Exercise Categories**

### **🟢 Beginner Level**
Focus on basic pattern matching enhancements and simple virtual thread usage.

### **🟡 Intermediate Level** 
Work with complex pattern matching, virtual thread pools, and structured concurrency patterns.

### **🔴 Advanced Level**
Build high-performance systems using the latest Java features for production applications.

---

## 🟢 **Beginner Exercises**

### **Exercise 1: Enhanced Pattern Matching**

**Task**: Use advanced pattern matching for switch expressions and record patterns.

```java
public class EnhancedPatternMatching {
    
    /**
     * Exercise 1a: Data type analyzer
     * Analyze different data types using enhanced pattern matching
     */
    public static String analyzeDataType(Object data) {
        // TODO: Use pattern matching with guards to analyze:
        // - Strings: check length, content patterns, special characters
        // - Numbers: check ranges, mathematical properties
        // - Collections: check size, element types, emptiness
        // - Records: extract and analyze components
        // - Optional: check presence and analyze contents
        return null;
    }
    
    /**
     * Exercise 1b: Geometric shape processor
     * Process shapes with record pattern matching
     */
    public static GeometryResult processShape(Shape shape) {
        return switch (shape) {
            // TODO: Implement pattern matching for different shapes
            // - Circle: calculate area, circumference, classify size
            // - Rectangle: check if square, calculate properties
            // - Triangle: determine type (equilateral, isosceles, scalene)
            // - Point: handle degenerate case
            case null -> new GeometryResult("null", 0, 0, "Invalid shape");
            // TODO: Add remaining cases
            default -> new GeometryResult("unknown", 0, 0, "Unsupported shape");
        };
    }
    
    /**
     * Exercise 1c: Employee payroll system
     * Process employee data with nested record patterns
     */
    public static PayrollResult calculatePayroll(Employee employee) {
        return switch (employee) {
            // TODO: Use nested pattern matching to extract:
            // - Employee details and department information
            // - Calculate salary based on role and location
            // - Apply bonuses and deductions based on performance
            case Employee(var name, Role(var title, var level), 
                         Department(var deptName, Location(var city, var country))) -> {
                // TODO: Implement salary calculation logic
                yield null;
            }
        };
    }
    
    /**
     * Exercise 1d: Message routing system
     */
    public static RoutingDecision routeMessage(Message message) {
        return switch (message) {
            // TODO: Route messages based on type, priority, and content
            // - Email: route based on recipient domain and content type
            // - SMS: route based on country code and message length
            // - Push notification: route based on device type and urgency
            case EmailMessage(var to, var subject, var body, var priority) 
                when priority == Priority.URGENT -> 
                new RoutingDecision("express-email", Duration.ofMinutes(1));
            // TODO: Complete other message types
            default -> new RoutingDecision("default-queue", Duration.ofMinutes(5));
        };
    }
}

// Supporting records and classes
sealed interface Shape permits Circle, Rectangle, Triangle, Point {}
record Circle(double radius) implements Shape {}
record Rectangle(double width, double height) implements Shape {}
record Triangle(Point a, Point b, Point c) implements Shape {}
record Point(double x, double y) implements Shape {}

record GeometryResult(String shapeType, double area, double perimeter, String classification) {}

record Employee(String name, Role role, Department department) {}
record Role(String title, int level) {}
record Department(String name, Location location) {}
record Location(String city, String country) {}
record PayrollResult(double baseSalary, double bonus, double deductions, double netPay) {}

sealed interface Message permits EmailMessage, SMSMessage, PushNotification {}
record EmailMessage(String to, String subject, String body, Priority priority) implements Message {}
record SMSMessage(String phoneNumber, String text, String countryCode) implements Message {}
record PushNotification(String deviceId, String title, String body, boolean urgent) implements Message {}

enum Priority { LOW, NORMAL, HIGH, URGENT }
record RoutingDecision(String queue, Duration processingTime) {}
```

### **Exercise 2: Virtual Threads Basics**

**Task**: Create and manage virtual threads for concurrent operations.

```java
public class VirtualThreadBasics {
    
    /**
     * Exercise 2a: Basic virtual thread creation
     * Create virtual threads for simple parallel tasks
     */
    public static List<String> processTasksWithVirtualThreads(List<String> tasks) {
        // TODO: Create virtual threads to process tasks concurrently
        // Each task should simulate some work (e.g., string processing)
        // Return results in the same order as input
        return List.of();
    }
    
    /**
     * Exercise 2b: I/O intensive operations
     * Simulate I/O-bound operations using virtual threads
     */
    public static List<WebResponse> fetchDataConcurrently(List<String> urls) {
        // TODO: Use virtual threads to simulate web requests
        // Each request should have different response times
        // Handle timeouts and failures gracefully
        return List.of();
    }
    
    /**
     * Exercise 2c: Producer-consumer with virtual threads
     */
    public static void runProducerConsumer(int producerCount, int consumerCount, int itemCount) {
        // TODO: Implement producer-consumer pattern using virtual threads
        // - Producers generate data items
        // - Consumers process data items
        // - Use appropriate synchronization mechanisms
        
        BlockingQueue<WorkItem> queue = new LinkedBlockingQueue<>();
        
        // TODO: Create producer virtual threads
        // TODO: Create consumer virtual threads
        // TODO: Wait for completion and report statistics
    }
    
    /**
     * Exercise 2d: Thread pool comparison
     */
    public static ComparisonResult compareThreadPerformance(int taskCount, Duration taskDuration) {
        // TODO: Compare virtual threads vs platform threads
        // Measure execution time, memory usage, and scalability
        // Return comparison results
        return null;
    }
}

// Supporting classes
record WebResponse(String url, int statusCode, String content, Duration responseTime) {}
record WorkItem(int id, String data, Instant timestamp) {}
record ComparisonResult(Duration virtualThreadTime, Duration platformThreadTime, 
                       double memoryUsageVirtual, double memoryUsagePlatform) {}
```

---

## 🟡 **Intermediate Exercises**

### **Exercise 3: Structured Concurrency Patterns**

**Task**: Implement structured concurrency for coordinated task execution.

```java
public class StructuredConcurrencyPatterns {
    
    /**
     * Exercise 3a: Microservice aggregation
     * Coordinate multiple service calls with structured concurrency
     */
    public static AggregatedResult aggregateServiceData(String userId) {
        // TODO: Implement structured concurrency to call multiple services:
        // - User service: get user profile
        // - Order service: get user orders  
        // - Recommendation service: get recommendations
        // - Payment service: get payment methods
        // Handle partial failures and timeouts
        
        try {
            // TODO: Create structured task scope
            // TODO: Submit concurrent tasks
            // TODO: Wait for completion or handle failures
            // TODO: Aggregate results
            return null;
        } catch (Exception e) {
            return new AggregatedResult(null, null, null, null, 
                                      List.of("Aggregation failed: " + e.getMessage()));
        }
    }
    
    /**
     * Exercise 3b: Parallel data processing pipeline
     */
    public static ProcessingResult processDataPipeline(List<RawData> data) {
        // TODO: Create a structured processing pipeline:
        // Stage 1: Data validation (parallel)
        // Stage 2: Data transformation (parallel)  
        // Stage 3: Data enrichment (parallel)
        // Stage 4: Data aggregation (sequential)
        // Handle errors at each stage appropriately
        
        return null;
    }
    
    /**
     * Exercise 3c: Distributed computation
     */
    public static ComputationResult performDistributedComputation(ComputationTask task) {
        // TODO: Break down computation into subtasks
        // Execute subtasks concurrently with structured concurrency
        // Implement work-stealing or load balancing
        // Combine results from all subtasks
        
        return null;
    }
    
    /**
     * Exercise 3d: Timeout and cancellation handling
     */
    public static TaskResult executeWithTimeoutAndCancellation(
            List<LongRunningTask> tasks, Duration timeout) {
        // TODO: Execute tasks with structured concurrency
        // Implement proper timeout handling
        // Support graceful cancellation of running tasks
        // Return partial results if some tasks complete
        
        return null;
    }
}

// Supporting records and classes
record AggregatedResult(UserProfile user, List<Order> orders, 
                       List<Recommendation> recommendations, List<PaymentMethod> payments,
                       List<String> errors) {}

record UserProfile(String id, String name, String email) {}
record Order(String id, double amount, String status) {}  
record Recommendation(String productId, double score) {}
record PaymentMethod(String id, String type, boolean isDefault) {}

record RawData(String id, Map<String, Object> data, Instant timestamp) {}
record ProcessingResult(List<ProcessedData> results, ProcessingStats stats, List<String> errors) {}
record ProcessedData(String id, Map<String, Object> processedData, List<String> validationResults) {}
record ProcessingStats(int totalProcessed, int successful, int failed, Duration totalTime) {}

record ComputationTask(String id, Map<String, Object> parameters, int complexity) {}
record ComputationResult(String taskId, Map<String, Object> results, List<SubTaskResult> subResults) {}
record SubTaskResult(String subTaskId, Object result, Duration executionTime) {}

record LongRunningTask(String id, Duration estimatedDuration, Callable<String> operation) {}
record TaskResult(List<String> completedResults, List<String> failedTasks, Duration actualDuration) {}
```

### **Exercise 4: Advanced Pattern Matching Applications**

**Task**: Build complex systems using advanced pattern matching features.

```java
public class AdvancedPatternApplications {
    
    /**
     * Exercise 4a: Configuration management system
     */
    public static ConfigurationResult processConfiguration(ConfigSource source) {
        return switch (source) {
            // TODO: Handle different configuration sources with pattern matching
            // - File-based configs (JSON, YAML, Properties)
            // - Environment variables
            // - Database configurations
            // - Remote configuration services
            // Apply validation, transformation, and merging logic
            
            case FileConfig(var path, var format) when format.equals("json") -> {
                // TODO: Process JSON configuration
                yield null;
            }
            case FileConfig(var path, var format) when format.equals("yaml") -> {
                // TODO: Process YAML configuration  
                yield null;
            }
            // TODO: Add other configuration source types
            default -> new ConfigurationResult(Map.of(), List.of("Unsupported configuration source"));
        };
    }
    
    /**
     * Exercise 4b: Event processing system
     */
    public static List<ProcessedEvent> processEventStream(List<Event> events) {
        return events.stream()
                    .map(event -> switch (event) {
                        // TODO: Process different event types
                        // - User events: login, logout, action tracking
                        // - System events: startup, shutdown, health checks
                        // - Business events: orders, payments, notifications
                        // Apply event-specific processing logic
                        
                        case UserEvent(var userId, var action, var timestamp, var metadata) 
                            when action.equals("login") -> {
                            // TODO: Process login event
                            yield null;
                        }
                        // TODO: Add other event processing patterns
                        default -> new ProcessedEvent("unknown", "Unhandled event type", 
                                                    Instant.now(), Map.of());
                    })
                    .collect(Collectors.toList());
    }
    
    /**
     * Exercise 4c: Data validation engine
     */
    public static ValidationResult validateComplexData(Object data, ValidationSchema schema) {
        // TODO: Use pattern matching to validate complex data structures
        // Support nested objects, arrays, conditional validation
        // Return detailed validation results with field-level errors
        
        return switch (data) {
            case Map<?, ?> map -> validateMapData(map, schema);
            case List<?> list -> validateListData(list, schema); 
            case String str -> validateStringData(str, schema);
            case Number num -> validateNumberData(num, schema);
            // TODO: Add more validation patterns
            case null -> new ValidationResult(false, List.of("Null data not allowed"), Map.of());
            default -> new ValidationResult(false, 
                                          List.of("Unsupported data type: " + data.getClass()), 
                                          Map.of());
        };
    }
    
    /**
     * Exercise 4d: Query processing system
     */
    public static QueryResult processQuery(Query query, DataSource dataSource) {
        return switch (query) {
            // TODO: Process different query types with pattern matching
            // - Simple filters and projections
            // - Joins and aggregations  
            // - Complex nested queries
            // - Real-time vs batch queries
            
            case SelectQuery(var fields, var filters, var orderBy, var limit) -> {
                // TODO: Process SELECT query
                yield null;
            }
            case AggregateQuery(var groupBy, var aggregates, var having) -> {
                // TODO: Process aggregate query
                yield null;  
            }
            // TODO: Add other query types
            default -> new QueryResult(List.of(), 0, List.of("Unsupported query type"));
        };
    }
}

// Configuration system records
sealed interface ConfigSource permits FileConfig, EnvConfig, DatabaseConfig, RemoteConfig {}
record FileConfig(String path, String format) implements ConfigSource {}
record EnvConfig(String prefix) implements ConfigSource {}
record DatabaseConfig(String connectionString, String table) implements ConfigSource {}  
record RemoteConfig(String url, Map<String, String> headers) implements ConfigSource {}
record ConfigurationResult(Map<String, Object> config, List<String> errors) {}

// Event system records  
sealed interface Event permits UserEvent, SystemEvent, BusinessEvent {}
record UserEvent(String userId, String action, Instant timestamp, Map<String, Object> metadata) implements Event {}
record SystemEvent(String component, String event, Instant timestamp, Map<String, Object> data) implements Event {}
record BusinessEvent(String type, String entityId, Instant timestamp, Map<String, Object> payload) implements Event {}
record ProcessedEvent(String type, String result, Instant processedAt, Map<String, Object> enrichedData) {}

// Validation system records
record ValidationSchema(Map<String, FieldValidation> fieldRules, List<CrossFieldValidation> crossFieldRules) {}
record FieldValidation(String fieldType, boolean required, Map<String, Object> constraints) {}
record CrossFieldValidation(List<String> fields, String rule, Map<String, Object> parameters) {}
record ValidationResult(boolean isValid, List<String> globalErrors, Map<String, List<String>> fieldErrors) {}

// Query system records
sealed interface Query permits SelectQuery, AggregateQuery, UpdateQuery, DeleteQuery {}
record SelectQuery(List<String> fields, Map<String, Object> filters, 
                  List<String> orderBy, Integer limit) implements Query {}
record AggregateQuery(List<String> groupBy, Map<String, String> aggregates, 
                     Map<String, Object> having) implements Query {}
record UpdateQuery(Map<String, Object> updates, Map<String, Object> filters) implements Query {}
record DeleteQuery(Map<String, Object> filters) implements Query {}
record QueryResult(List<Map<String, Object>> rows, long totalCount, List<String> warnings) {}

interface DataSource {
    // DataSource interface for query execution
}
```

---

## 🔴 **Advanced Exercises**

### **Exercise 5: High-Performance Computing System**

**Task**: Build a high-performance computing system using latest Java features.

```java
public class HighPerformanceComputingSystem {
    
    /**
     * Exercise 5a: Parallel matrix operations
     */
    public class MatrixProcessor {
        
        public static MatrixResult multiplyMatricesParallel(Matrix a, Matrix b) {
            // TODO: Implement parallel matrix multiplication using virtual threads
            // Use structured concurrency for coordinating parallel tasks
            // Optimize for different matrix sizes and available cores
            // Include performance metrics and memory usage tracking
            
            return null;
        }
        
        public static MatrixResult performComplexOperations(List<MatrixOperation> operations) {
            // TODO: Execute a series of matrix operations in parallel
            // Support operations: multiply, add, transpose, invert
            // Use pattern matching to handle different operation types
            // Implement operation pipelining and dependency management
            
            return null;
        }
    }
    
    /**
     * Exercise 5b: Scientific computation pipeline
     */
    public class ScientificComputationPipeline {
        
        public static ComputationResult runSimulation(SimulationConfig config) {
            // TODO: Implement scientific simulation using virtual threads
            // Break simulation into parallel time steps or spatial regions
            // Use structured concurrency for coordinating computation phases
            // Include real-time progress monitoring and checkpointing
            
            return null;
        }
        
        public static AnalysisResult analyzeResults(ComputationResult results) {
            // TODO: Analyze simulation results using pattern matching
            // Support different analysis types based on result patterns
            // Generate statistical summaries and visualizations
            // Detect anomalies and convergence patterns
            
            return null;
        }
    }
    
    /**
     * Exercise 5c: Machine learning training system  
     */
    public class MLTrainingSystem {
        
        public static TrainingResult trainModelParallel(MLModel model, Dataset dataset) {
            // TODO: Implement parallel model training using virtual threads
            // Support different training algorithms and optimization strategies  
            // Use structured concurrency for batch processing and validation
            // Include hyperparameter tuning and cross-validation
            
            return null;
        }
        
        public static PredictionResult performInference(MLModel model, List<InputData> inputs) {
            // TODO: Perform parallel inference using virtual threads
            // Batch inputs for optimal throughput
            // Handle different input types using pattern matching
            // Include confidence scoring and uncertainty quantification
            
            return null;
        }
    }
}

// Supporting records for HPC system
record Matrix(int rows, int cols, double[][] data) {}
record MatrixOperation(String operation, Matrix operand1, Matrix operand2, Map<String, Object> parameters) {}
record MatrixResult(Matrix result, Duration computationTime, long memoryUsed) {}

record SimulationConfig(String algorithm, Map<String, Object> parameters, int iterations, Duration timeStep) {}
record ComputationResult(Map<String, Object> results, List<Double> convergenceHistory, Duration totalTime) {}
record AnalysisResult(Map<String, Object> statistics, List<String> insights, List<Anomaly> anomalies) {}
record Anomaly(String type, String description, Map<String, Object> details) {}

record MLModel(String algorithm, Map<String, Object> parameters, ModelState state) {}
record Dataset(List<InputData> features, List<OutputData> labels, DatasetMetadata metadata) {}
record InputData(Map<String, Object> features) {}
record OutputData(Map<String, Object> labels) {}
record DatasetMetadata(int size, List<String> featureNames, String description) {}
record TrainingResult(MLModel trainedModel, TrainingMetrics metrics, List<ValidationResult> validationResults) {}
record TrainingMetrics(double accuracy, double loss, Duration trainingTime, int epochs) {}
record ValidationResult(double score, String metric, Map<String, Object> details) {}
record PredictionResult(List<Prediction> predictions, Duration inferenceTime, ModelMetrics metrics) {}
record Prediction(InputData input, Map<String, Object> output, double confidence) {}
record ModelMetrics(double throughput, double latency, double memoryUsage) {}

enum ModelState { UNTRAINED, TRAINING, TRAINED, DEPLOYED }
```

### **Exercise 6: Reactive Streaming System**

**Task**: Build a reactive streaming system using virtual threads and pattern matching.

```java
public class ReactiveStreamingSystem {
    
    /**
     * Exercise 6a: Real-time data processing
     */
    public class RealTimeProcessor {
        
        public static StreamProcessingResult processRealTimeStream(DataStream stream) {
            // TODO: Process real-time data stream using virtual threads
            // Implement backpressure handling and flow control
            // Use pattern matching for different event types
            // Include metrics collection and alerting
            
            return null;
        }
        
        public static AggregationResult performStreamingAggregation(
                DataStream stream, AggregationConfig config) {
            // TODO: Implement streaming aggregation with windowing
            // Support tumbling, sliding, and session windows
            // Use virtual threads for parallel window processing
            // Handle late-arriving data and out-of-order events
            
            return null;
        }
    }
    
    /**
     * Exercise 6b: Event-driven architecture
     */
    public class EventDrivenSystem {
        
        public static EventProcessingResult processEventDrivenWorkflow(
                List<EventHandler> handlers, EventStream events) {
            // TODO: Implement event-driven workflow using virtual threads
            // Route events to appropriate handlers using pattern matching
            // Support event ordering, deduplication, and retry logic
            // Include circuit breaker and bulkhead patterns
            
            return null;
        }
        
        public static SagaResult executeSagaPattern(SagaDefinition saga, Map<String, Object> context) {
            // TODO: Implement distributed saga pattern
            // Use virtual threads for parallel saga step execution
            // Handle compensation actions for failures
            // Support both orchestration and choreography patterns
            
            return null;
        }
    }
    
    /**
     * Exercise 6c: Stream analytics engine
     */
    public class StreamAnalyticsEngine {
        
        public static AnalyticsResult performComplexEventProcessing(
                EventStream events, List<PatternDefinition> patterns) {
            // TODO: Implement complex event processing using pattern matching
            // Detect patterns across multiple event streams
            // Use virtual threads for parallel pattern matching
            // Generate alerts and derived events
            
            return null;
        }
        
        public static ForecastingResult performStreamForecasting(
                DataStream historicalData, ForecastingConfig config) {
            // TODO: Implement real-time forecasting using streaming data
            // Use online learning algorithms for model updates
            // Handle concept drift and model adaptation
            // Provide confidence intervals and uncertainty estimates
            
            return null;
        }
    }
}

// Supporting records for reactive streaming
record DataStream(String id, Stream<DataPoint> data, StreamMetadata metadata) {}
record DataPoint(String id, Map<String, Object> values, Instant timestamp) {}
record StreamMetadata(String source, Map<String, String> properties, Instant startTime) {}
record StreamProcessingResult(long processedCount, Duration processingTime, 
                             List<ProcessingError> errors, ProcessingMetrics metrics) {}
record ProcessingError(String errorType, String message, DataPoint failedPoint) {}
record ProcessingMetrics(double throughput, double latency, double memoryUsage) {}

record AggregationConfig(Duration windowSize, Duration slideSize, List<String> groupByFields, 
                        Map<String, String> aggregationFunctions) {}
record AggregationResult(Map<String, Object> aggregatedValues, WindowMetadata window, 
                        long inputCount, Duration computationTime) {}
record WindowMetadata(Instant startTime, Instant endTime, String windowType) {}

record EventHandler(String eventType, Function<Event, CompletableFuture<HandlerResult>> handler, 
                   HandlerConfig config) {}
record HandlerConfig(int maxRetries, Duration timeout, boolean parallel) {}
record HandlerResult(boolean success, String message, Map<String, Object> result) {}
record EventStream(Stream<Event> events, String source) {}
record EventProcessingResult(long processedEvents, Map<String, Long> handlerStats, 
                           List<ProcessingError> errors) {}

record SagaDefinition(String sagaId, List<SagaStep> steps, Map<String, Object> configuration) {}
record SagaStep(String stepId, Function<Map<String, Object>, StepResult> action, 
               Function<Map<String, Object>, CompensationResult> compensation) {}
record StepResult(boolean success, Map<String, Object> output, String message) {}
record CompensationResult(boolean success, String message) {}
record SagaResult(boolean completed, Map<String, Object> finalState, List<String> executedSteps, 
                 List<String> compensatedSteps) {}

record PatternDefinition(String patternId, String pattern, Duration timeWindow, 
                        Map<String, Object> conditions) {}
record AnalyticsResult(List<DetectedPattern> patterns, Map<String, Object> statistics, 
                      Duration processingTime) {}
record DetectedPattern(String patternId, List<Event> matchedEvents, Instant detectionTime, 
                      double confidence) {}

record ForecastingConfig(String algorithm, int forecastHorizon, Duration updateInterval, 
                        Map<String, Object> parameters) {}
record ForecastingResult(List<ForecastPoint> forecast, ModelAccuracy accuracy, 
                        ModelMetadata modelInfo) {}
record ForecastPoint(Instant timestamp, double value, double lowerBound, double upperBound) {}
record ModelAccuracy(double mae, double mse, double mape) {}
record ModelMetadata(String algorithm, Map<String, Object> parameters, Instant lastUpdated) {}
```

---

## 🧪 **Testing Your Solutions**

### **Unit Test Template**

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class LatestJavaFeaturesTest {
    
    @Test
    void testEnhancedPatternMatching() {
        // Test pattern matching with guards
        Object data = "Java 21 LTS";
        
        String result = switch (data) {
            case String s when s.contains("Java") -> "Java version: " + s;
            case String s -> "Regular string: " + s;
            case Integer i when i > 20 -> "Large number: " + i;
            default -> "Unknown type";
        };
        
        assertTrue(result.contains("Java version"));
    }
    
    @Test 
    void testRecordPatterns() {
        Shape circle = new Circle(5.0);
        
        String description = switch (circle) {
            case Circle(var radius) when radius > 3 -> 
                "Large circle with radius " + radius;
            case Circle(var radius) -> 
                "Small circle with radius " + radius;
            case Rectangle(var w, var h) -> 
                "Rectangle " + w + "x" + h;
            case Triangle(var a, var b, var c) ->
                "Triangle with vertices";
            case Point(var x, var y) ->
                "Point at (" + x + ", " + y + ")";
        };
        
        assertTrue(description.contains("Large circle"));
    }
    
    @Test
    void testVirtualThreads() throws InterruptedException {
        List<String> results = Collections.synchronizedList(new ArrayList<>());
        List<Thread> virtualThreads = new ArrayList<>();
        
        // Create virtual threads
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            Thread vthread = Thread.ofVirtual()
                                  .name("test-virtual-" + taskId)
                                  .start(() -> {
                                      results.add("Task " + taskId + " completed");
                                  });
            virtualThreads.add(vthread);
        }
        
        // Wait for completion
        for (Thread thread : virtualThreads) {
            thread.join();
        }
        
        assertEquals(10, results.size());
    }
    
    @Test
    void testStructuredConcurrency() {
        // Test structured concurrency pattern (simplified)
        List<CompletableFuture<String>> futures = List.of(
            CompletableFuture.supplyAsync(() -> "Task 1"),
            CompletableFuture.supplyAsync(() -> "Task 2"), 
            CompletableFuture.supplyAsync(() -> "Task 3")
        );
        
        CompletableFuture<List<String>> allResults = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        ).thenApply(v -> 
            futures.stream()
                   .map(CompletableFuture::join)
                   .collect(Collectors.toList())
        );
        
        List<String> results = allResults.join();
        assertEquals(3, results.size());
    }
}
```

---

## 🎯 **Success Criteria**

**Beginner Level**: ✅ Complete Exercises 1-2
- Master enhanced pattern matching with guards and record patterns
- Create and manage virtual threads effectively
- Handle basic concurrent operations

**Intermediate Level**: ✅ Complete Exercises 3-4  
- Implement structured concurrency patterns
- Build complex pattern matching applications
- Design advanced concurrent systems

**Advanced Level**: ✅ Complete Exercises 5-6
- Create high-performance computing systems
- Build reactive streaming applications  
- Implement production-ready concurrent architectures

---

## 💡 **Tips for Success**

1. **Pattern Matching**: Use guards for complex conditions and nested patterns for data extraction
2. **Virtual Threads**: Ideal for I/O-bound tasks, not CPU-intensive operations
3. **Structured Concurrency**: Ensures proper lifecycle management of concurrent tasks
4. **Performance**: Monitor virtual thread performance and resource usage
5. **Testing**: Use proper synchronization when testing concurrent code

---

**🚀 Ready to master the latest Java features? Build next-generation applications with cutting-edge performance and maintainability!**