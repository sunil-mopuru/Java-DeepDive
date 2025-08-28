# Java 10 Features - Practice Exercises

## 📝 Instructions
- Master Local Variable Type Inference with the `var` keyword
- Practice creating unmodifiable collections efficiently
- Learn enhanced Optional usage patterns
- Focus on code readability and maintainability

---

## Exercise 1: var Keyword Mastery
**Difficulty: Beginner-Intermediate**

Refactor existing verbose code to use `var` appropriately while maintaining code clarity.

### Requirements:
- **Appropriate Usage**: Use `var` where it improves readability
- **Code Clarity**: Ensure variable types remain clear from context
- **Best Practices**: Follow established `var` usage guidelines
- **Performance**: Understand `var` has no runtime impact

### Implementation:
```java
public class VarRefactoringExercise {
    // TODO: Refactor these verbose declarations using var
    public void processCustomerOrders() {
        Map<String, List<CustomerOrder>> ordersByCustomer = new HashMap<>();
        List<String> activeCustomerIds = new ArrayList<>();
        Set<ProductCategory> availableCategories = new HashSet<>();
        
        // TODO: Use var in processing logic
        for (String customerId : activeCustomerIds) {
            List<CustomerOrder> customerOrders = ordersByCustomer.get(customerId);
            if (customerOrders != null) {
                Optional<CustomerOrder> latestOrder = customerOrders.stream()
                    .max(Comparator.comparing(CustomerOrder::getOrderDate));
                // Process latest order
            }
        }
        
        // TODO: Refactor stream operations with var
        List<ProductSummary> summaries = ordersByCustomer.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(
                CustomerOrder::getProductCategory,
                Collectors.summarizingDouble(CustomerOrder::getAmount)
            ))
            .entrySet().stream()
            .map(entry -> new ProductSummary(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }
    
    // TODO: Create methods demonstrating appropriate var usage
    // TODO: Create methods showing when NOT to use var
}
```

---

## Exercise 2: Unmodifiable Collections Workshop
**Difficulty: Intermediate**

Build a data analytics system using Java 10's enhanced unmodifiable collection support.

### Requirements:
- **Immutable Results**: Use new `toUnmodifiableXxx()` collectors
- **Performance**: Compare with previous approaches
- **Thread Safety**: Ensure collections are safe for concurrent access
- **API Design**: Design clean APIs returning immutable collections

### Implementation:
```java
public class AnalyticsEngine {
    
    public SalesReport generateSalesReport(List<Transaction> transactions) {
        // TODO: Use toUnmodifiableList() for filtered results
        // TODO: Use toUnmodifiableSet() for unique categories
        // TODO: Use toUnmodifiableMap() for aggregated data
        
        // Example structure:
        var filteredTransactions = transactions.stream()
            .filter(Transaction::isValid)
            .collect(/* Use appropriate unmodifiable collector */);
            
        var categorySummary = transactions.stream()
            .collect(/* Group by category using unmodifiable map */);
            
        return new SalesReport(filteredTransactions, categorySummary);
    }
    
    public CustomerInsights analyzeCustomerBehavior(List<Customer> customers) {
        // TODO: Create unmodifiable collections for:
        // - Top customers by revenue
        // - Customer segments
        // - Purchase patterns
    }
    
    // TODO: Demonstrate thread safety of unmodifiable collections
    public void demonstrateThreadSafety() {
        // Create collections that can be safely shared between threads
    }
}
```

---

## Exercise 3: Optional Enhancement Patterns
**Difficulty: Intermediate**

Implement service layer methods using Java 10's simplified `orElseThrow()`.

### Requirements:
- **Exception Handling**: Use `orElseThrow()` without parameters
- **Service Layer**: Build clean service APIs
- **Error Management**: Handle different error scenarios
- **Performance**: Measure improvement over traditional null checks

### Implementation:
```java
public class UserManagementService {
    private UserRepository userRepository;
    
    public User getUserById(Long id) {
        // TODO: Use Java 10 orElseThrow() without parameter
        return userRepository.findById(id).orElseThrow();
    }
    
    public UserProfile getUserProfile(Long userId) {
        // TODO: Chain multiple Optional operations with orElseThrow()
        var user = getUserById(userId);
        // Get profile, handle missing cases
    }
    
    public List<User> getActiveUsersInDepartment(String department) {
        // TODO: Use var with unmodifiable collections
        // TODO: Handle empty results appropriately
    }
    
    // TODO: Create methods comparing Java 8 vs Java 10 Optional usage
    // TODO: Implement error handling strategies
}
```

---

## Exercise 4: Type Inference Best Practices
**Difficulty: Intermediate-Advanced**

Create a comprehensive guide demonstrating when to use and avoid `var`.

### Requirements:
- **Code Examples**: Show good vs. bad `var` usage
- **Readability**: Ensure code remains maintainable
- **Team Guidelines**: Create coding standards
- **Performance Analysis**: Benchmark `var` vs explicit types

### Implementation:
```java
public class VarBestPracticesDemo {
    
    // TODO: Demonstrate GOOD var usage patterns
    public void goodVarUsage() {
        // Clear from factory methods
        // Complex generic types
        // Builder patterns
        // Stream operations
    }
    
    // TODO: Demonstrate POOR var usage patterns
    public void poorVarUsage() {
        // Unclear method return types
        // Generic method names
        // Primitive types where explicit is clearer
    }
    
    // TODO: Create refactoring examples
    public void refactoringExamples() {
        // Before: verbose declarations
        // After: appropriate var usage
        // Demonstrate improved readability
    }
    
    // TODO: Performance comparison methods
    public void performanceAnalysis() {
        // Measure compilation time
        // Analyze bytecode differences
        // Memory usage comparison
    }
}
```

---

## Exercise 5: Migration Workshop
**Difficulty: Advanced**

Migrate a legacy Java 8 application to Java 10, focusing on `var` adoption and unmodifiable collections.

### Migration Tasks:

#### Phase 1: Identify Candidates
```java
// TODO: Analyze existing codebase for var opportunities
public class MigrationAnalyzer {
    public void scanForVarCandidates(String sourceDirectory) {
        // Identify verbose generic declarations
        // Find complex type declarations
        // Locate appropriate refactoring opportunities
    }
    
    public void generateMigrationReport() {
        // Create report of potential improvements
        // Calculate readability improvements
        // Estimate maintenance benefits
    }
}
```

#### Phase 2: Gradual Refactoring
```java
// TODO: Implement step-by-step migration strategy
public class GradualMigration {
    
    // Step 1: Collections and generics
    public void migrateCollections() {
        // Convert HashMap<K,V> declarations to var
        // Update ArrayList<T> to var
        // Refactor complex generic types
    }
    
    // Step 2: Stream operations
    public void migrateStreamOperations() {
        // Use var for stream intermediate results
        // Apply unmodifiable collectors
        // Simplify Optional chains
    }
    
    // Step 3: Configuration and builders
    public void migrateBuilderPatterns() {
        // Use var with builder patterns
        // Simplify configuration code
        // Improve factory method usage
    }
}
```

#### Phase 3: Validation and Testing
```java
// TODO: Ensure migration maintains functionality
public class MigrationValidator {
    
    public void validateRefactoring() {
        // Run comprehensive test suite
        // Verify type safety maintained
        // Check performance characteristics
        // Validate code readability
    }
    
    public void measureImprovements() {
        // Lines of code reduction
        // Readability metrics
        // Developer productivity impact
        // Maintenance cost analysis
    }
}
```

---

## 🎯 Challenge Projects

### Project A: Configuration Management System
Build a type-safe configuration system:
- Use `var` for complex configuration hierarchies
- Create unmodifiable configuration objects
- Implement validation with Optional patterns
- Performance benchmark vs. traditional approaches

### Project B: Data Processing Pipeline
Create a high-performance data processor:
- Leverage `var` for stream operations
- Use unmodifiable collections for results
- Implement concurrent processing safely
- Measure type inference compilation impact

### Project C: API Gateway Service
Design a modern microservice gateway:
- Use `var` throughout the codebase
- Return unmodifiable collections from APIs
- Implement robust error handling with Optional
- Create developer-friendly APIs

---

## 📚 Testing and Validation

### var Usage Testing:
- Test type inference accuracy
- Verify IDE support and debugging
- Validate refactoring safety
- Performance regression testing

### Collection Immutability Testing:
- Verify UnsupportedOperationException throwing
- Test thread safety in concurrent scenarios
- Validate memory usage characteristics
- Performance comparison with previous methods

### Optional Enhancement Testing:
- Test exception throwing behavior
- Verify stack trace clarity
- Compare with custom exception throwing
- Performance impact analysis

---

## 💡 Best Practices Validation

### Code Quality Checklist:
✅ Use `var` only when type is clear from context  
✅ Prefer `var` for complex generic types  
✅ Use unmodifiable collectors for public API returns  
✅ Apply `orElseThrow()` for required values  
✅ Maintain consistent `var` usage across team  
✅ Document `var` usage guidelines in code standards  
✅ Use meaningful variable names with `var`  

---

**Next:** [Java 11 Features Exercises](../../14.3-Java11/exercises/) | **Previous:** [Java 9 Features Exercises](../../14.1-Java9/exercises/)