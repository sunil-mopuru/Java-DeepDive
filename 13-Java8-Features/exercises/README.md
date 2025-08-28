# Java 8 Features - Practice Exercises

## 📝 Instructions
- Master functional programming concepts with lambda expressions
- Practice Stream API for complex data processing
- Learn safe null handling with Optional
- Utilize method references for cleaner code
- Work with modern Date/Time API

---

## Exercise 1: Lambda Expressions Mastery
**Difficulty: Beginner-Intermediate**

Build a comprehensive data processing system using lambda expressions and functional interfaces.

### Requirements:
- **Custom Functional Interfaces**: Create domain-specific functional interfaces
- **Lambda Compositions**: Combine multiple lambda expressions
- **Performance Comparison**: Compare lambda vs anonymous class performance
- **Method Reference Usage**: Convert lambdas to method references where possible

### Implementation:
```java
// Custom functional interfaces
@FunctionalInterface
public interface TriPredicate<T, U, V> {
    boolean test(T t, U u, V v);
    
    default TriPredicate<T, U, V> and(TriPredicate<T, U, V> other) {
        return (t, u, v) -> test(t, u, v) && other.test(t, u, v);
    }
}

public class LambdaProcessor {
    public void processEmployees(List<Employee> employees) {
        // Use various lambda patterns
        // Compare performance with traditional approaches
    }
}
```

---

## Exercise 2: Stream API Advanced Processing
**Difficulty: Intermediate**

Create a sophisticated data analytics engine using Stream API for complex data transformations.

### Requirements:
- **Complex Pipelines**: Multi-stage data processing with intermediate operations
- **Custom Collectors**: Build domain-specific collectors
- **Parallel Processing**: Leverage parallel streams for performance
- **Statistics and Grouping**: Advanced grouping and statistical operations

### Example Scenarios:
```java
public class DataAnalytics {
    // Sales data analysis
    public SalesReport analyzeSales(List<Sale> sales) {
        Map<String, DoubleSummaryStatistics> salesByRegion = sales.stream()
            .collect(Collectors.groupingBy(
                Sale::getRegion,
                Collectors.summarizingDouble(Sale::getAmount)
            ));
        
        // Top products by revenue
        List<Product> topProducts = sales.stream()
            .collect(Collectors.groupingBy(
                Sale::getProduct,
                Collectors.summingDouble(Sale::getAmount)
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<Product, Double>comparingByValue().reversed())
            .limit(10)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
            
        return new SalesReport(salesByRegion, topProducts);
    }
}
```

---

## Exercise 3: Optional Pattern Implementation
**Difficulty: Intermediate**

Build a service layer that uses Optional extensively for safe null handling.

### Requirements:
- **Service Chain**: Chain multiple service calls using Optional
- **Validation Pipeline**: Use Optional for validation workflows
- **Error Handling**: Combine Optional with exception handling
- **Performance Analysis**: Measure Optional overhead vs null checks

### Service Design:
```java
public class UserService {
    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<Profile> getUserProfile(Long userId) {
        return findUser(userId)
            .filter(User::isActive)
            .map(User::getProfile)
            .filter(profile -> profile.getVisibility() == PUBLIC);
    }
    
    public String getUserDisplayName(Long userId) {
        return findUser(userId)
            .map(User::getProfile)
            .map(Profile::getDisplayName)
            .filter(name -> !name.trim().isEmpty())
            .orElseGet(() -> generateDefaultName(userId));
    }
}
```

---

## Exercise 4: Date/Time API Comprehensive Usage
**Difficulty: Intermediate-Advanced**

Create a scheduling and time management system using the new Date/Time API.

### Requirements:
- **Multi-timezone Support**: Handle events across different time zones
- **Recurring Events**: Calculate recurring event schedules
- **Business Days**: Calculate business days excluding weekends/holidays
- **Duration Calculations**: Complex duration and period calculations

### Features:
```java
public class SchedulingSystem {
    public List<LocalDateTime> generateRecurringEvents(
            LocalDateTime start, Duration interval, int count) {
        return Stream.iterate(start, time -> time.plus(interval))
            .limit(count)
            .collect(Collectors.toList());
    }
    
    public long calculateBusinessDays(LocalDate start, LocalDate end) {
        return Stream.iterate(start, date -> date.plusDays(1))
            .limit(ChronoUnit.DAYS.between(start, end) + 1)
            .filter(date -> !isWeekend(date) && !isHoliday(date))
            .count();
    }
    
    public ZonedDateTime convertToUserTimezone(
            LocalDateTime eventTime, ZoneId fromZone, ZoneId toZone) {
        return eventTime.atZone(fromZone)
            .withZoneSameInstant(toZone);
    }
}
```

---

## Exercise 5: Functional Programming Migration Project
**Difficulty: Advanced**

Refactor a legacy imperative codebase to use functional programming principles.

### Migration Tasks:

#### Phase 1: Identify Candidates
```java
// Before: Imperative style
public List<Customer> getActiveCustomers(List<Customer> customers) {
    List<Customer> result = new ArrayList<>();
    for (Customer customer : customers) {
        if (customer.isActive() && customer.getBalance() > 0) {
            result.add(customer);
        }
    }
    Collections.sort(result, new Comparator<Customer>() {
        @Override
        public int compare(Customer c1, Customer c2) {
            return c1.getName().compareTo(c2.getName());
        }
    });
    return result;
}

// After: Functional style
public List<Customer> getActiveCustomers(List<Customer> customers) {
    return customers.stream()
        .filter(Customer::isActive)
        .filter(customer -> customer.getBalance() > 0)
        .sorted(Comparator.comparing(Customer::getName))
        .collect(Collectors.toList());
}
```

#### Phase 2: Performance Comparison
- Measure execution time: imperative vs functional
- Analyze memory usage patterns
- Test with different data sizes
- Profile parallel stream performance

#### Phase 3: Advanced Refactoring
```java
// Complex business logic refactoring
public class OrderProcessor {
    // Before: Complex imperative processing
    public OrderSummary processOrders(List<Order> orders) {
        // 50+ lines of imperative code with nested loops
    }
    
    // After: Functional pipeline
    public OrderSummary processOrders(List<Order> orders) {
        Map<Status, List<Order>> ordersByStatus = orders.stream()
            .collect(Collectors.groupingBy(Order::getStatus));
            
        double totalRevenue = orders.stream()
            .filter(order -> order.getStatus() == COMPLETED)
            .mapToDouble(Order::getTotal)
            .sum();
            
        // More functional transformations...
    }
}
```

---

## 🎯 Challenge Projects

### Project A: Functional Data Processing Pipeline
Build a real-time data processing system:
- Stream processing with backpressure handling
- Custom collectors for aggregation
- Parallel processing with custom thread pools
- Performance monitoring and optimization

### Project B: Reactive Web Service
Create a web service using functional principles:
- Functional request/response processing
- Optional-based error handling
- Stream-based data transformation
- Asynchronous processing with CompletableFuture

### Project C: Time-Series Analysis System
Build a financial time-series analysis tool:
- Advanced Date/Time calculations
- Stream-based statistical analysis
- Custom time window processing
- Multi-timezone data handling

---

## 📚 Testing and Validation

### Lambda Expression Testing:
- Test different functional interface implementations
- Verify closure behavior and variable capture
- Performance test method references vs lambdas
- Test lambda serialization (if needed)

### Stream API Testing:
- Test with empty streams and edge cases
- Verify parallel stream thread safety
- Test custom collectors thoroughly
- Measure performance impact of stream operations

### Optional Testing:
- Test all Optional methods and combinations
- Verify proper exception handling
- Test Optional chaining scenarios
- Validate performance compared to null checks

---

## 💡 Best Practices Validation

### Code Quality Checklist:
✅ Prefer method references over lambda expressions where possible  
✅ Keep lambda expressions short and focused  
✅ Use appropriate functional interfaces (Predicate, Function, etc.)  
✅ Avoid side effects in lambda expressions  
✅ Use parallel streams only when beneficial  
✅ Handle Optional properly (avoid get() without isPresent())  
✅ Use immutable Date/Time operations correctly  

---

**Next:** [Java Version Evolution](../../14-Java-Versions/exercises/)"