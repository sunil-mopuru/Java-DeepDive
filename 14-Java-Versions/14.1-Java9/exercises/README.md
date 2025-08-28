# Java 9 Features - Practice Exercises

## 📝 Instructions
- Master Collection Factory Methods for creating immutable collections
- Practice Stream API enhancements (takeWhile, dropWhile, ofNullable)
- Learn Optional improvements with ifPresentOrElse and or methods
- Understand Module System concepts and modular application design
- Focus on immutability and safe programming practices

---

## Exercise 1: Collection Factory Methods Mastery
**Difficulty: Beginner-Intermediate**

Create a configuration management system using Java 9 collection factories.

### Requirements:
- **Immutable Configuration**: Use factory methods for all collections
- **Type Safety**: Ensure compile-time type checking
- **Performance**: Compare factory methods vs traditional approaches
- **Error Handling**: Handle attempts to modify immutable collections

### Implementation:
```java
public class ConfigurationSystem {
    
    // TODO: Create immutable application settings using Map.of()
    public Map<String, String> getApplicationSettings() {
        // Create settings for database, server, cache, etc.
    }
    
    // TODO: Create immutable list of supported features using List.of()
    public List<String> getSupportedFeatures() {
        // Features like "authentication", "logging", "monitoring"
    }
    
    // TODO: Create immutable set of allowed user roles using Set.of()
    public Set<String> getAllowedUserRoles() {
        // Roles like "admin", "user", "guest", "moderator"
    }
    
    // TODO: Handle large configurations using Map.ofEntries()
    public Map<String, Object> getLargeConfiguration() {
        // More than 10 key-value pairs
    }
    
    // TODO: Demonstrate immutability with error handling
    public void demonstrateImmutability() {
        // Try to modify collections and handle UnsupportedOperationException
    }
    
    // TODO: Performance comparison method
    public void comparePerformance() {
        // Measure creation time: factory methods vs Collections.unmodifiableXxx
    }
}
```

---

## Exercise 2: Stream API Enhancements Workshop
**Difficulty: Intermediate**

Build a data processing pipeline using Java 9 Stream enhancements.

### Requirements:
- **takeWhile/dropWhile**: Process ordered data efficiently
- **Stream.ofNullable**: Handle nullable values safely
- **iterate with predicate**: Generate controlled sequences
- **Real-world scenarios**: Log processing, data filtering, batch processing

### Implementation:
```java
public class StreamDataProcessor {
    
    // TODO: Process log entries using takeWhile
    public List<LogEntry> getRecentLogs(List<LogEntry> logs) {
        // Take logs while they are from today, stop at first old entry
        // Use takeWhile with timestamp comparison
    }
    
    // TODO: Skip initial setup logs using dropWhile
    public List<LogEntry> getOperationalLogs(List<LogEntry> logs) {
        // Drop initialization logs, keep only operational ones
        // Use dropWhile to skip setup phase
    }
    
    // TODO: Handle nullable configuration values
    public List<String> processConfigValues(List<String> configValues) {
        // Some values might be null - use Stream.ofNullable
        // Filter nulls and process valid values
    }
    
    // TODO: Generate test data using iterate with predicate
    public List<Integer> generateTestSequence(int start, int limit) {
        // Generate sequence: start, start+2, start+4, ... while <= limit
        // Use Stream.iterate with predicate
    }
    
    // TODO: Complex data pipeline
    public ProcessingResult processCustomerData(List<Customer> customers) {
        // Combine takeWhile, dropWhile, and ofNullable
        // Process active customers, skip inactive ones, handle null fields
    }
    
    // TODO: Batch processing with Stream enhancements
    public void processBatches(List<DataBatch> batches) {
        // Use takeWhile to process until error batch
        // Use ofNullable for optional batch metadata
    }
}
```

---

## Exercise 3: Optional Enhancements Implementation
**Difficulty: Intermediate**

Create a service layer that leverages Java 9 Optional improvements.

### Requirements:
- **ifPresentOrElse**: Handle both present and empty cases
- **or method**: Provide alternative Optional chains
- **stream method**: Convert Optional to Stream for processing
- **Service Integration**: Build robust service layer patterns

### Implementation:
```java
public class UserService {
    private UserRepository userRepository;
    private CacheService cacheService;
    
    // TODO: Implement user retrieval with ifPresentOrElse
    public void displayUserInfo(Long userId) {
        // Use ifPresentOrElse to either display user info or show "not found"
    }
    
    // TODO: Implement fallback user retrieval using 'or'
    public Optional<User> findUserWithFallback(Long userId) {
        // Try primary database, then cache, then default user
        // Chain multiple Optional sources using 'or' method
    }
    
    // TODO: Process multiple user lookups using stream()
    public List<UserProfile> getUserProfiles(List<Long> userIds) {
        // Convert Optional<User> to Stream<User>
        // Use Optional.stream() to flatten results
    }
    
    // TODO: Complex service integration
    public ServiceResult processUserRequest(UserRequest request) {
        // Combine ifPresentOrElse, or, and stream methods
        // Handle various optional fields in the request
    }
    
    // TODO: Notification system with Optional chaining
    public void notifyUser(Long userId, String message) {
        // Find user, get notification preferences, send notification
        // Use Optional enhancements for clean error handling
    }
}
```

---

## Exercise 4: Module System Design
**Difficulty: Advanced**

Design a modular application architecture using Java 9 Module System.

### Requirements:
- **Module Hierarchy**: Design layered module structure
- **Service Providers**: Implement service provider interfaces
- **Encapsulation**: Properly hide internal implementations
- **Module Dependencies**: Manage transitive dependencies

### Module Structure Design:
```java
// TODO: Design module-info.java files for each module

// Module 1: app.api (Public interfaces)
/*
module app.api {
    // Define what this module exports
}
*/

// Module 2: app.core (Business logic)
/*
module app.core {
    // Define dependencies and exports
}
*/

// Module 3: app.data (Data access)
/*
module app.data {
    // Define service providers and dependencies
}
*/

// Module 4: app.web (Web layer)
/*
module app.web {
    // Define web-specific dependencies
}
*/

// Module 5: app.main (Application entry point)
/*
module app.main {
    // Define main module dependencies
}
*/

// TODO: Implement service provider pattern
public interface DatabaseService {
    void connect();
    List<User> findAll();
}

// TODO: Create multiple implementations
public class MySQLDatabaseService implements DatabaseService {
    // MySQL-specific implementation
}

public class PostgreSQLDatabaseService implements DatabaseService {
    // PostgreSQL-specific implementation
}
```

### Implementation Tasks:
1. **Design Module Boundaries**: Define clear module responsibilities
2. **Create Service Interfaces**: Design service provider interfaces
3. **Implement Encapsulation**: Hide internal classes properly
4. **Handle Dependencies**: Manage module dependencies effectively
5. **Build and Test**: Create build scripts for modular application

---

## Exercise 5: Migration Project - Legacy to Modular
**Difficulty: Advanced**

Migrate a legacy monolithic application to use Java 9 modules.

### Migration Strategy:
```java
// Phase 1: Analyze existing codebase
public class LegacyAnalyzer {
    // TODO: Identify module boundaries
    public void analyzePackageStructure() {
        // Analyze existing packages for logical grouping
        // Identify circular dependencies
        // Map out public API vs internal classes
    }
    
    // TODO: Create dependency graph
    public void mapDependencies() {
        // Create visual representation of dependencies
        // Identify refactoring opportunities
    }
}

// Phase 2: Gradual modularization
public class ModularizationPlan {
    // TODO: Create bottom-up migration plan
    public void createMigrationPlan() {
        // Start with leaf modules (no dependencies)
        // Gradually work up the dependency chain
        // Plan for automatic module names
    }
    
    // TODO: Handle split packages
    public void resolveSplitPackages() {
        // Identify and resolve split package issues
        // Refactor conflicting package structures
    }
}

// Phase 3: Validation and testing
public class ModularValidation {
    // TODO: Validate module boundaries
    public void validateEncapsulation() {
        // Ensure proper encapsulation
        // Test module loading and dependencies
        // Verify service provider mechanisms
    }
}
```

---

## 🎯 Challenge Projects

### Project A: Microservices Configuration System
Build a modular configuration system:
- Use collection factories for immutable config objects
- Implement service discovery with module system
- Handle dynamic configuration updates safely
- Use Optional enhancements for config validation

### Project B: Stream Processing Engine
Create a real-time data processing engine:
- Use takeWhile/dropWhile for windowing operations
- Handle nullable sensor data with ofNullable
- Implement backpressure with Stream.iterate
- Build modular processing pipeline

### Project C: Plugin Architecture
Design a plugin-based application:
- Use module system for plugin isolation
- Implement service provider interfaces
- Handle plugin dependencies safely
- Support dynamic plugin loading/unloading

---

## 📚 Testing and Validation

### Collection Factory Testing:
- Verify immutability with exception testing
- Performance benchmarking vs traditional methods
- Memory usage analysis
- Thread safety validation

### Stream Enhancement Testing:
- Edge case testing for takeWhile/dropWhile
- Null handling verification with ofNullable
- Performance testing with large datasets
- Integration testing with existing streams

### Optional Enhancement Testing:
- Behavior verification for all new methods
- Chaining operation testing
- Performance impact analysis
- Error handling validation

### Module System Testing:
- Module loading and dependency resolution
- Service provider discovery testing
- Encapsulation boundary validation
- Runtime module introspection

---

## 💡 Best Practices Validation

### Code Quality Checklist:
✅ Use collection factories for small, immutable collections  
✅ Apply takeWhile/dropWhile for ordered data processing  
✅ Leverage Optional enhancements for cleaner error handling  
✅ Design modules with clear, minimal public APIs  
✅ Use service providers for pluggable architectures  
✅ Avoid circular module dependencies  
✅ Test module boundaries and encapsulation  

---

**Next:** [Java 10 Features Exercises](../../14.2-Java10/exercises/) | **Previous:** [Java 8 Features Exercises](../../13-Java8-Features/exercises/)