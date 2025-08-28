# Java 16 Features - Exercises

## 🎯 **Learning Objectives**

Master Java 16 features through hands-on exercises covering Records (Production Ready), Pattern Matching enhancements, Stream.toList() method, and JEP improvements.

---

## 📋 **Exercise Categories**

### **🟢 Beginner Level**
Focus on production-ready Records syntax and basic Stream.toList() usage.

### **🟡 Intermediate Level** 
Work with complex Record patterns and advanced stream operations for real-world scenarios.

### **🔴 Advanced Level**
Build complete data modeling systems using production-ready Records and enhanced features.

---

## 🟢 **Beginner Exercises**

### **Exercise 1: Production-Ready Records**

**Task**: Use standardized Records to build type-safe data models.

```java
public class ProductionRecords {
    
    /**
     * Exercise 1a: Customer management system
     * Create production-ready records with comprehensive validation
     */
    public record Customer(String id, String name, String email, Address address, LocalDate registrationDate) {
        // TODO: Add comprehensive validation in compact constructor
        // - id: non-null, non-empty, matches pattern "CUST-\\d{6}"
        // - name: non-null, length 2-100, no special characters except spaces and hyphens
        // - email: valid format with proper domain validation
        // - address: non-null
        // - registrationDate: not in future, not older than 50 years
        
        public Customer {
            // TODO: Implement validation logic
        }
        
        // TODO: Add business methods
        public int getCustomerAge() {
            // Calculate age based on some business logic
            return 0;
        }
        
        public boolean isPremiumCustomer() {
            // Premium if registered more than 2 years ago
            return false;
        }
        
        public String getDisplayName() {
            // Return formatted display name
            return null;
        }
    }
    
    public record Address(String street, String city, String state, String zipCode, String country) {
        // TODO: Add validation for each field
        // - street: non-null, length 5-200
        // - city: non-null, length 2-100, letters/spaces/hyphens only
        // - state: non-null, 2 characters for US, up to 50 for others
        // - zipCode: format validation based on country
        // - country: non-null, valid ISO country code
        
        public Address {
            // TODO: Implement validation
        }
        
        public boolean isUSAddress() {
            // TODO: Check if US address
            return false;
        }
        
        public String getFormattedAddress() {
            // TODO: Return properly formatted address
            return null;
        }
    }
    
    /**
     * Exercise 1b: Product catalog system
     */
    public record Product(String sku, String name, BigDecimal price, Category category, 
                         Set<String> tags, ProductStatus status) {
        
        // TODO: Add validation and business methods
        public boolean isOnSale() {
            // TODO: Check if product has sale tag
            return false;
        }
        
        public Product applyDiscount(BigDecimal discountPercent) {
            // TODO: Return new Product with discounted price
            return null;
        }
        
        public boolean isAvailable() {
            // TODO: Check if status is ACTIVE
            return false;
        }
    }
    
    public record Category(String id, String name, String description, Category parent) {
        // TODO: Add category hierarchy methods
        
        public List<Category> getAncestors() {
            // TODO: Return list of parent categories up to root
            return List.of();
        }
        
        public boolean isRootCategory() {
            return parent == null;
        }
    }
    
    public enum ProductStatus {
        ACTIVE, INACTIVE, DISCONTINUED, OUT_OF_STOCK
    }
}
```

### **Exercise 2: Stream.toList() Method**

**Task**: Use the new Stream.toList() method for cleaner stream operations.

```java
public class StreamToListExercises {
    
    /**
     * Exercise 2a: Data filtering and collection
     */
    public static List<String> filterActiveCustomerNames(List<Customer> customers) {
        // TODO: Use Stream.toList() to collect active customer names
        // Filter customers with status "ACTIVE" and collect their names
        return null;
    }
    
    public static List<Product> getExpensiveProducts(List<Product> products, BigDecimal threshold) {
        // TODO: Use Stream.toList() to collect products above price threshold
        return null;
    }
    
    public static List<String> extractUniqueCategories(List<Product> products) {
        // TODO: Extract unique category names using Stream.toList()
        // Use distinct() and map operations
        return null;
    }
    
    /**
     * Exercise 2b: Data transformation
     */
    public static List<CustomerSummary> createCustomerSummaries(List<Customer> customers, 
                                                               List<Order> orders) {
        // TODO: Create customer summaries using Stream.toList()
        // Join customer data with order statistics
        return null;
    }
    
    public static List<String> generateProductDescriptions(List<Product> products) {
        // TODO: Generate formatted product descriptions using Stream.toList()
        // Format: "ProductName (SKU: XXX) - $Price - Category"
        return null;
    }
    
    /**
     * Exercise 2c: Nested collection processing
     */
    public static List<String> extractAllTags(List<Product> products) {
        // TODO: Extract all tags from all products using flatMap and toList()
        return null;
    }
    
    public static List<OrderItem> getFlattenedOrderItems(List<Order> orders) {
        // TODO: Flatten all order items from all orders using Stream.toList()
        return null;
    }
}

// Supporting records
record CustomerSummary(String customerId, String name, int totalOrders, BigDecimal totalSpent) {}
record Order(String id, String customerId, List<OrderItem> items, LocalDateTime orderDate) {}
record OrderItem(String productSku, int quantity, BigDecimal unitPrice) {}
```

---

## 🟡 **Intermediate Exercises**

### **Exercise 3: Advanced Records Patterns**

**Task**: Build complex business systems using Records with inheritance and composition.

```java
public class AdvancedRecordsPatterns {
    
    /**
     * Exercise 3a: Financial transaction system
     */
    public sealed interface Transaction permits Payment, Refund, Transfer, Fee {}
    
    public record Payment(String id, String accountId, BigDecimal amount, 
                         String merchantId, PaymentMethod method, Instant timestamp) 
            implements Transaction {
        
        // TODO: Add validation and business logic
        public TransactionStatus getStatus() {
            // TODO: Determine transaction status based on business rules
            return null;
        }
        
        public boolean isHighValue() {
            // TODO: Check if amount exceeds threshold (e.g., $1000)
            return false;
        }
        
        public Payment withFee(BigDecimal feeAmount) {
            // TODO: Return new Payment with fee added to amount
            return null;
        }
    }
    
    public record Refund(String id, String originalPaymentId, BigDecimal amount, 
                        String reason, Instant timestamp) implements Transaction {
        
        // TODO: Add refund-specific validation and methods
        public boolean isPartialRefund(BigDecimal originalAmount) {
            // TODO: Check if refund amount is less than original
            return false;
        }
    }
    
    public record Transfer(String id, String fromAccountId, String toAccountId, 
                          BigDecimal amount, String description, Instant timestamp) 
            implements Transaction {
        
        // TODO: Add transfer validation
        public boolean isSameAccountTransfer() {
            return fromAccountId.equals(toAccountId);
        }
    }
    
    public record Fee(String id, String accountId, BigDecimal amount, 
                     FeeType feeType, Instant timestamp) implements Transaction {
        
        // TODO: Add fee calculation methods
    }
    
    public enum PaymentMethod { CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, DIGITAL_WALLET }
    public enum FeeType { TRANSACTION_FEE, MONTHLY_FEE, OVERDRAFT_FEE, FOREIGN_EXCHANGE }
    public enum TransactionStatus { PENDING, COMPLETED, FAILED, CANCELLED }
    
    /**
     * Exercise 3b: Event sourcing system
     */
    public sealed interface DomainEvent permits UserRegistered, OrderPlaced, PaymentProcessed, 
                                              OrderShipped, OrderDelivered {}
    
    public record UserRegistered(String userId, String email, Instant timestamp, 
                                Map<String, Object> metadata) implements DomainEvent {}
    
    public record OrderPlaced(String orderId, String userId, List<OrderItem> items, 
                             BigDecimal totalAmount, Instant timestamp) implements DomainEvent {}
    
    public record PaymentProcessed(String paymentId, String orderId, BigDecimal amount, 
                                  PaymentMethod method, Instant timestamp) implements DomainEvent {}
    
    // TODO: Complete other event records
    
    /**
     * Exercise 3c: Configuration management
     */
    public record DatabaseConfig(String host, int port, String database, 
                                ConnectionPool pool, SecuritySettings security) {
        
        public String getJdbcUrl() {
            // TODO: Build JDBC URL from components
            return null;
        }
        
        public Properties toProperties() {
            // TODO: Convert to Properties object
            return null;
        }
    }
    
    public record ConnectionPool(int minSize, int maxSize, Duration maxWait, 
                                Duration idleTimeout, boolean testOnBorrow) {}
    
    public record SecuritySettings(boolean sslEnabled, String keystore, 
                                  String truststore, Set<String> allowedHosts) {}
}
```

### **Exercise 4: Enhanced Stream Operations**

**Task**: Combine Stream.toList() with complex data processing patterns.

```java
public class EnhancedStreamOperations {
    
    /**
     * Exercise 4a: Analytics and reporting
     */
    public static List<SalesReport> generateMonthlySalesReports(List<Transaction> transactions) {
        // TODO: Group transactions by month and generate reports using Stream.toList()
        // Calculate total sales, transaction count, average transaction value per month
        return null;
    }
    
    public static List<ProductPerformance> analyzeProductPerformance(
            List<Product> products, List<OrderItem> orderItems) {
        // TODO: Analyze product sales performance using Stream.toList()
        // Include metrics: total sold, revenue, average order quantity
        return null;
    }
    
    public static List<CustomerSegment> segmentCustomers(
            List<Customer> customers, List<Order> orders) {
        // TODO: Segment customers based on purchase behavior using Stream.toList()
        // Categories: VIP (>$10k/year), Regular ($1k-$10k), New (<$1k)
        return null;
    }
    
    /**
     * Exercise 4b: Complex data transformations
     */
    public static List<InvoiceLineItem> generateInvoiceItems(Order order, 
                                                           List<Product> productCatalog,
                                                           TaxCalculator taxCalculator) {
        // TODO: Generate detailed invoice line items using Stream.toList()
        // Include product details, quantities, prices, taxes
        return null;
    }
    
    public static List<RecommendedProduct> generateRecommendations(
            Customer customer, List<Product> allProducts, 
            List<Order> customerOrderHistory) {
        // TODO: Generate product recommendations using Stream.toList()
        // Based on purchase history and product categories
        return null;
    }
    
    /**
     * Exercise 4c: Performance optimization
     */
    public static List<OptimizedQuery> optimizeDataQueries(List<DatabaseQuery> queries) {
        // TODO: Optimize database queries using Stream.toList()
        // Group similar queries, combine where possible, add indexes
        return null;
    }
    
    public static List<CacheEntry> buildOptimalCache(List<DataAccess> accessPatterns) {
        // TODO: Build cache entries based on access patterns using Stream.toList()
        // Prioritize frequently accessed data
        return null;
    }
}

// Supporting records for exercises
record SalesReport(YearMonth period, BigDecimal totalSales, int transactionCount, 
                  BigDecimal averageValue) {}
record ProductPerformance(String productSku, int totalSold, BigDecimal revenue, 
                         double averageOrderQuantity) {}
record CustomerSegment(String customerId, String segmentType, BigDecimal annualSpend, 
                      int orderCount) {}
record InvoiceLineItem(String productSku, String description, int quantity, 
                      BigDecimal unitPrice, BigDecimal tax, BigDecimal total) {}
record RecommendedProduct(String productSku, double score, String reason) {}
record OptimizedQuery(String originalQuery, String optimizedQuery, List<String> suggestedIndexes) {}
record CacheEntry(String key, Object data, Duration ttl, int priority) {}
record DatabaseQuery(String sql, Map<String, Object> parameters, Duration executionTime) {}
record DataAccess(String key, Instant accessTime, Duration responseTime) {}
```

---

## 🔴 **Advanced Exercises**

### **Exercise 5: Complete Application Architecture**

**Task**: Build a complete e-commerce system using Java 16 Records and enhanced features.

```java
public class ECommerceSystem {
    
    /**
     * Exercise 5a: Product catalog management
     */
    public class ProductCatalogService {
        
        public List<ProductListing> searchProducts(SearchCriteria criteria) {
            // TODO: Implement product search using Records and Stream.toList()
            // Support filtering by category, price range, ratings, availability
            return null;
        }
        
        public List<CategoryTree> buildCategoryHierarchy(List<Category> categories) {
            // TODO: Build hierarchical category tree using Stream.toList()
            return null;
        }
        
        public ProductRecommendationResult generateRecommendations(
                RecommendationRequest request) {
            // TODO: Generate product recommendations using advanced algorithms
            return null;
        }
    }
    
    /**
     * Exercise 5b: Order processing system
     */
    public class OrderProcessingService {
        
        public OrderValidationResult validateOrder(OrderRequest request) {
            // TODO: Comprehensive order validation using Records
            // Check inventory, pricing, customer limits, business rules
            return null;
        }
        
        public List<ShippingOption> calculateShippingOptions(ShippingRequest request) {
            // TODO: Calculate available shipping options using Stream.toList()
            return null;
        }
        
        public PaymentProcessingResult processPayment(PaymentRequest request) {
            // TODO: Process payment with comprehensive error handling
            return null;
        }
        
        public List<OrderFulfillmentStep> planFulfillment(Order order) {
            // TODO: Plan order fulfillment steps using Stream.toList()
            return null;
        }
    }
    
    /**
     * Exercise 5c: Customer management system
     */
    public class CustomerManagementService {
        
        public CustomerProfile createCompleteProfile(CustomerRegistration registration) {
            // TODO: Create comprehensive customer profile using Records
            return null;
        }
        
        public List<CustomerInsight> analyzeCustomerBehavior(
                String customerId, AnalysisPeriod period) {
            // TODO: Analyze customer behavior patterns using Stream.toList()
            return null;
        }
        
        public LoyaltyProgramResult calculateLoyaltyStatus(String customerId) {
            // TODO: Calculate customer loyalty status and benefits
            return null;
        }
    }
}

// System records
record SearchCriteria(List<String> categories, PriceRange priceRange, 
                     Double minRating, Boolean inStock, String keyword) {}
record ProductListing(Product product, BigDecimal currentPrice, Double rating, 
                     int reviewCount, boolean inStock) {}
record CategoryTree(Category category, List<CategoryTree> children, int productCount) {}
record RecommendationRequest(String customerId, List<String> excludeCategories, 
                           int maxResults, RecommendationType type) {}
record ProductRecommendationResult(List<RecommendedProduct> products, 
                                  String algorithm, double confidence) {}
record OrderRequest(String customerId, List<OrderItemRequest> items, 
                   Address shippingAddress, PaymentMethod paymentMethod) {}
record OrderItemRequest(String productSku, int quantity, BigDecimal maxPrice) {}
record OrderValidationResult(boolean valid, List<ValidationError> errors, 
                           BigDecimal estimatedTotal) {}
record ValidationError(String field, String code, String message) {}
record ShippingRequest(Address from, Address to, List<ShippingItem> items) {}
record ShippingItem(String sku, int quantity, BigDecimal weight, Dimensions dimensions) {}
record Dimensions(BigDecimal length, BigDecimal width, BigDecimal height) {}
record ShippingOption(String carrier, String service, BigDecimal cost, 
                     Duration estimatedDelivery) {}
```

### **Exercise 6: High-Performance Data Processing**

**Task**: Build high-performance data processing systems using Java 16 features.

```java
public class HighPerformanceDataProcessing {
    
    /**
     * Exercise 6a: Real-time analytics engine
     */
    public class AnalyticsEngine {
        
        public List<RealTimeMetric> processEventStream(Stream<BusinessEvent> events) {
            // TODO: Process real-time events and generate metrics using Stream.toList()
            // Use parallel processing for high throughput
            return null;
        }
        
        public List<AggregatedResult> performTimeWindowAnalysis(
                List<TimestampedData> data, Duration windowSize) {
            // TODO: Perform sliding window analysis using Stream.toList()
            return null;
        }
        
        public PerformanceBenchmark benchmarkOperations(List<Operation> operations) {
            // TODO: Benchmark various operations and return performance metrics
            return null;
        }
    }
    
    /**
     * Exercise 6b: Batch processing system
     */
    public class BatchProcessor {
        
        public BatchProcessingResult processBatch(BatchJob job) {
            // TODO: Process large batches efficiently using Records and streams
            return null;
        }
        
        public List<ProcessedRecord> transformData(List<RawData> rawData, 
                                                 List<TransformationRule> rules) {
            // TODO: Apply transformation rules using Stream.toList()
            return null;
        }
        
        public DataQualityReport validateDataQuality(List<ProcessedRecord> records) {
            // TODO: Validate data quality and generate comprehensive report
            return null;
        }
    }
    
    /**
     * Exercise 6c: Memory optimization
     */
    public class MemoryOptimizer {
        
        public List<OptimizedRecord> optimizeMemoryUsage(List<LargeRecord> records) {
            // TODO: Optimize memory usage while preserving functionality
            return null;
        }
        
        public CacheOptimizationResult optimizeCache(CacheConfiguration config, 
                                                   List<AccessPattern> patterns) {
            // TODO: Optimize cache configuration based on access patterns
            return null;
        }
    }
}

// Performance-oriented records
record RealTimeMetric(String metricName, BigDecimal value, Instant timestamp, 
                     Map<String, String> dimensions) {}
record AggregatedResult(String groupKey, StatisticalSummary summary, 
                       Duration timeWindow) {}
record StatisticalSummary(BigDecimal sum, BigDecimal average, BigDecimal min, 
                         BigDecimal max, long count) {}
record PerformanceBenchmark(Map<String, Duration> operationTimes, 
                           MemoryUsage memoryUsage, double throughput) {}
record MemoryUsage(long heapUsed, long heapMax, long nonHeapUsed) {}
```

---

## 🧪 **Testing Your Solutions**

### **Unit Test Template**

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class Java16FeaturesTest {
    
    @Test
    void testProductionRecords() {
        // Test record creation and validation
        Address address = new Address("123 Main St", "Anytown", "CA", "12345", "US");
        Customer customer = new Customer("CUST-123456", "John Doe", 
                                       "john@example.com", address, LocalDate.now().minusYears(3));
        
        assertTrue(customer.isPremiumCustomer());
        assertNotNull(customer.getDisplayName());
        assertTrue(customer.address().isUSAddress());
    }
    
    @Test
    void testStreamToList() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        
        // Test Stream.toList() returns immutable list
        List<String> upperNames = names.stream()
                                      .map(String::toUpperCase)
                                      .toList();
        
        assertThrows(UnsupportedOperationException.class, 
                    () -> upperNames.add("David"));
        assertEquals(3, upperNames.size());
    }
    
    @Test
    void testSealedInterfacesWithRecords() {
        Transaction payment = new Payment("PAY-001", "ACC-123", 
                                        BigDecimal.valueOf(100), "MERCH-456", 
                                        PaymentMethod.CREDIT_CARD, Instant.now());
        
        String description = switch (payment) {
            case Payment p -> "Payment of $" + p.amount();
            case Refund r -> "Refund of $" + r.amount();
            case Transfer t -> "Transfer of $" + t.amount();
            case Fee f -> "Fee of $" + f.amount();
        };
        
        assertTrue(description.contains("Payment of $100"));
    }
}
```

---

## 🎯 **Success Criteria**

**Beginner Level**: ✅ Complete Exercises 1-2
- Master production-ready Records with validation
- Use Stream.toList() effectively in simple scenarios
- Handle basic data modeling with Records

**Intermediate Level**: ✅ Complete Exercises 3-4
- Build complex business systems with Records
- Combine Stream.toList() with advanced operations
- Create sealed interface hierarchies with Records

**Advanced Level**: ✅ Complete Exercises 5-6
- Design complete application architectures
- Build high-performance data processing systems
- Optimize memory usage and performance

---

## 💡 **Tips for Success**

1. **Records**: Now production-ready, use for all data carriers
2. **Stream.toList()**: Returns immutable list, more efficient than collect()
3. **Validation**: Use compact constructors for comprehensive validation
4. **Performance**: Records are optimized for data transfer and processing
5. **Immutability**: Embrace immutable design patterns with Records

---

**🚀 Ready to master Java 16's production-ready features? Start with standardized Records and build enterprise-grade applications!**