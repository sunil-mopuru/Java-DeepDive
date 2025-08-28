# Guava API Exercises - From Basic to Advanced

## 🎯 **Learning Objectives**

Master Google Guava through hands-on exercises covering collections, functional programming, caching, string processing, and real-world application development.

---

## 📋 **Exercise Categories**

### **🥉 Bronze Level (Beginner)**
Basic Guava utilities and collection operations

### **🥈 Silver Level (Intermediate)** 
Advanced collections, caching, and functional programming

### **🥇 Gold Level (Advanced)**
Complex real-world applications and performance optimization

---

## 🥉 **Bronze Level Exercises**

### **Exercise 1: Collection Basics**

**Task**: Master basic Guava collection utilities and operations.

```java
public class GuavaCollectionBasics {
    
    /**
     * Exercise 1a: Shopping Cart Management
     * Create a shopping cart system using Guava collections
     */
    public static class ShoppingCart {
        // TODO: Use appropriate Guava collections
        // - Items with quantities (use Multiset)
        // - Categories with items (use Multimap)
        // - Product ID to name mapping (use BiMap)
        
        public void addItem(String item, int quantity) {
            // TODO: Add items to cart
        }
        
        public void removeItem(String item, int quantity) {
            // TODO: Remove items from cart
        }
        
        public int getItemCount(String item) {
            // TODO: Get count of specific item
            return 0;
        }
        
        public double calculateTotal() {
            // TODO: Calculate total price
            return 0.0;
        }
        
        public List<String> getItemsByCategory(String category) {
            // TODO: Get all items in a category
            return null;
        }
    }
    
    /**
     * Exercise 1b: Student Grade Management
     * Create a grade management system using various collection types
     */
    public static class GradeManager {
        // TODO: Design data structures using:
        // - Student to courses mapping (Multimap)
        // - Course grade ranges (Range)
        // - Student ID to name mapping (BiMap)
        
        public void enrollStudent(String studentId, String course) {
            // TODO: Enroll student in course
        }
        
        public void addGrade(String studentId, String course, double grade) {
            // TODO: Add grade for student in course
        }
        
        public String getLetterGrade(double numericGrade) {
            // TODO: Convert numeric grade to letter grade using ranges
            // A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: 0-59
            return null;
        }
        
        public List<String> getStudentsInCourse(String course) {
            // TODO: Get all students enrolled in a course
            return null;
        }
        
        public double getAverageGrade(String studentId) {
            // TODO: Calculate average grade for student
            return 0.0;
        }
    }
    
    /**
     * Exercise 1c: Text Processing Utilities
     * Implement text processing functions using Guava string utilities
     */
    public static class TextProcessor {
        
        public static String joinWords(List<String> words, String separator) {
            // TODO: Use Joiner to join words, skip nulls
            return null;
        }
        
        public static List<String> splitSentence(String sentence) {
            // TODO: Use Splitter to split on spaces, trim and omit empty strings
            return null;
        }
        
        public static Map<String, String> parseProperties(String propertiesString) {
            // TODO: Parse "key1=value1,key2=value2" format using Splitter
            return null;
        }
        
        public static String removeSpecialCharacters(String text) {
            // TODO: Use CharMatcher to keep only letters, digits, and spaces
            return null;
        }
        
        public static boolean isValidEmail(String email) {
            // TODO: Basic email validation using CharMatcher
            // Check for @ symbol, valid characters, etc.
            return false;
        }
    }
    
    /**
     * Exercise 1d: Basic Data Validation
     * Implement validation using Preconditions
     */
    public static class UserValidator {
        
        public static void validateAge(int age) {
            // TODO: Use Preconditions to validate age (0-150)
        }
        
        public static void validateEmail(String email) {
            // TODO: Use Preconditions to validate email format
        }
        
        public static void validatePassword(String password) {
            // TODO: Use Preconditions to validate password strength
            // Minimum 8 characters, at least one uppercase, one lowercase, one digit
        }
        
        public static User createUser(String name, String email, int age, String password) {
            // TODO: Create user with validation using all above methods
            // Use Preconditions.checkNotNull for required fields
            return null;
        }
    }
}

// Supporting classes
class User {
    private final String name;
    private final String email;
    private final int age;
    
    public User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
    
    // Getters and toString method
}
```

### **Exercise 2: Optional and Null Safety**

**Task**: Master Guava's Optional for null-safe programming.

```java
public class OptionalExercises {
    
    /**
     * Exercise 2a: User Profile Service
     * Handle potentially null user data safely
     */
    public static class UserProfileService {
        private Map<String, UserProfile> profiles = new HashMap<>();
        
        public Optional<UserProfile> findProfile(String userId) {
            // TODO: Return Optional from nullable map lookup
            return null;
        }
        
        public String getDisplayName(String userId) {
            // TODO: Get display name or "Anonymous" if profile not found
            // Use Optional chain: profile -> name -> displayName
            return null;
        }
        
        public String getProfileSummary(String userId) {
            // TODO: Create profile summary with safe navigation
            // Format: "Name (Age: X, Location: Y)" or "Profile not found"
            return null;
        }
        
        public List<String> getAllDisplayNames() {
            // TODO: Get all display names, handling null names gracefully
            return null;
        }
        
        public Optional<String> getPreferredLanguage(String userId) {
            // TODO: Chain Optional calls to get nested property safely
            // profile -> preferences -> language
            return null;
        }
    }
    
    /**
     * Exercise 2b: Configuration Service
     * Handle configuration values that might be missing
     */
    public static class ConfigService {
        private Properties config = new Properties();
        
        public Optional<String> getConfigValue(String key) {
            // TODO: Return Optional from Properties lookup
            return null;
        }
        
        public int getIntConfig(String key, int defaultValue) {
            // TODO: Get integer config value with default
            return 0;
        }
        
        public boolean getBooleanConfig(String key, boolean defaultValue) {
            // TODO: Get boolean config value with default
            return false;
        }
        
        public Optional<Integer> getPort() {
            // TODO: Get port configuration, parse safely
            return null;
        }
        
        public String getDatabaseUrl() {
            // TODO: Build database URL from multiple config values
            // Use host, port, database name with defaults
            return null;
        }
    }
}

// Supporting classes
class UserProfile {
    private String name;
    private Integer age;
    private String location;
    private Preferences preferences;
    
    // Constructors, getters, setters
}

class Preferences {
    private String language;
    private String theme;
    
    // Constructors, getters, setters
}
```

---

## 🥈 **Silver Level Exercises**

### **Exercise 3: Advanced Collections**

**Task**: Build complex data structures using Guava's advanced collection types.

```java
public class AdvancedCollectionExercises {
    
    /**
     * Exercise 3a: Social Network Analysis
     * Model a social network using advanced Guava collections
     */
    public static class SocialNetwork {
        // TODO: Design using:
        // - User connections (Multimap for followers/following)
        // - User posts by date (Table<User, Date, List<Post>>)
        // - Hashtag usage frequency (Multiset)
        // - User handle to ID mapping (BiMap)
        
        public void addUser(String handle, String userId, String name) {
            // TODO: Add user to network
        }
        
        public void followUser(String followerHandle, String followeeHandle) {
            // TODO: Create following relationship
        }
        
        public void addPost(String userHandle, String content, Date date) {
            // TODO: Add post for user
        }
        
        public List<String> getFollowers(String userHandle) {
            // TODO: Get all followers of user
            return null;
        }
        
        public List<String> getFollowing(String userHandle) {
            // TODO: Get all users that this user follows
            return null;
        }
        
        public List<String> getPostsByDate(String userHandle, Date date) {
            // TODO: Get all posts by user on specific date
            return null;
        }
        
        public Map<String, Integer> getTrendingHashtags(int topN) {
            // TODO: Get top N hashtags by frequency
            return null;
        }
        
        public List<String> getMutualFollowers(String user1, String user2) {
            // TODO: Find mutual followers between two users
            return null;
        }
        
        public SocialNetworkStats getNetworkStats() {
            // TODO: Generate network statistics
            return null;
        }
    }
    
    /**
     * Exercise 3b: Inventory Management System
     * Create a comprehensive inventory system
     */
    public static class InventorySystem {
        // TODO: Use Table for (Location, Product, Quantity)
        // Use Range for reorder levels
        // Use Multimap for product categories
        
        public void addStock(String location, String productId, int quantity) {
            // TODO: Add stock to location
        }
        
        public void removeStock(String location, String productId, int quantity) {
            // TODO: Remove stock from location
        }
        
        public int getStockLevel(String location, String productId) {
            // TODO: Get current stock level
            return 0;
        }
        
        public List<String> getLowStockProducts(String location) {
            // TODO: Get products below reorder level
            return null;
        }
        
        public Map<String, Integer> getTotalStockByProduct() {
            // TODO: Get total stock across all locations for each product
            return null;
        }
        
        public List<String> findProductsInPriceRange(double minPrice, double maxPrice) {
            // TODO: Use Range to filter products by price
            return null;
        }
        
        public void transferStock(String fromLocation, String toLocation, 
                                String productId, int quantity) {
            // TODO: Transfer stock between locations
        }
        
        public InventoryReport generateReport() {
            // TODO: Generate comprehensive inventory report
            return null;
        }
    }
    
    /**
     * Exercise 3c: Event Management System
     * Handle complex event scheduling and attendee management
     */
    public static class EventManager {
        // TODO: Use:
        // - Table<Date, Location, Event> for scheduling
        // - Multimap<Event, Attendee> for registrations
        // - Range<Date> for event duration
        
        public void scheduleEvent(String eventId, String name, Date startDate, 
                                Date endDate, String location) {
            // TODO: Schedule event with validation
        }
        
        public void registerAttendee(String eventId, String attendeeId) {
            // TODO: Register attendee for event
        }
        
        public List<String> getEventsOnDate(Date date, String location) {
            // TODO: Get all events at location on specific date
            return null;
        }
        
        public List<String> getAttendees(String eventId) {
            // TODO: Get all attendees for event
            return null;
        }
        
        public List<String> getConflictingEvents(String eventId) {
            // TODO: Find events that conflict with given event
            return null;
        }
        
        public Map<String, Integer> getAttendeeStats() {
            // TODO: Get attendance statistics
            return null;
        }
        
        public boolean canScheduleEvent(Date startDate, Date endDate, String location) {
            // TODO: Check if time slot is available
            return false;
        }
    }
}
```

### **Exercise 4: Caching System**

**Task**: Implement sophisticated caching solutions using Guava Cache.

```java
public class CachingExercises {
    
    /**
     * Exercise 4a: Database Result Cache
     * Create a cache for expensive database operations
     */
    public static class DatabaseResultCache {
        
        // TODO: Create LoadingCache with:
        // - Maximum size of 1000 entries
        // - Expire after write: 30 minutes
        // - Expire after access: 10 minutes
        // - Enable statistics
        
        public String getUserById(String userId) {
            // TODO: Implement cache-through pattern for user lookup
            return null;
        }
        
        public List<String> getUsersByDepartment(String department) {
            // TODO: Cache department user lists
            return null;
        }
        
        public void invalidateUser(String userId) {
            // TODO: Invalidate specific user from cache
        }
        
        public CacheStats getCacheStatistics() {
            // TODO: Return cache performance statistics
            return null;
        }
        
        public void preloadCache(List<String> userIds) {
            // TODO: Preload cache with user data
        }
        
        private String loadUserFromDatabase(String userId) {
            // Simulate database call
            try {
                Thread.sleep(100); // Simulate latency
                return "User:" + userId;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }
    
    /**
     * Exercise 4b: Web Content Cache
     * Implement a multi-level cache for web content
     */
    public static class WebContentCache {
        
        // TODO: Create multiple caches:
        // - Fast cache: small size, short TTL for hot content
        // - Medium cache: medium size, medium TTL for warm content  
        // - Cold cache: large size, long TTL for cold content
        
        public String getContent(String url, ContentType type) {
            // TODO: Implement tiered cache lookup
            return null;
        }
        
        public void putContent(String url, String content, ContentType type) {
            // TODO: Store content in appropriate cache tier
        }
        
        public CacheMetrics getCacheMetrics() {
            // TODO: Aggregate metrics from all cache tiers
            return null;
        }
        
        public void refreshContent(String url) {
            // TODO: Refresh content across all cache levels
        }
        
        public enum ContentType {
            HOT, WARM, COLD
        }
    }
    
    /**
     * Exercise 4c: Computation Cache
     * Cache expensive mathematical computations
     */
    public static class ComputationCache {
        
        // TODO: Create cache with custom weigher and eviction listener
        
        public double calculateExpensiveFunction(double input) {
            // TODO: Cache mathematical computations
            return 0.0;
        }
        
        public Matrix multiplyMatrices(Matrix a, Matrix b) {
            // TODO: Cache matrix multiplication results
            return null;
        }
        
        public List<Long> generateFibonacciSequence(int n) {
            // TODO: Cache Fibonacci sequences
            return null;
        }
        
        public void optimizeCache() {
            // TODO: Implement cache optimization strategies
        }
    }
}

// Supporting classes
class CacheMetrics {
    private final double hitRate;
    private final long requestCount;
    private final long hitCount;
    
    // Constructor and getters
}

class Matrix {
    private final double[][] data;
    // Matrix implementation
}
```

---

## 🥇 **Gold Level Exercises**

### **Exercise 5: Real-World Applications**

**Task**: Build complete applications using multiple Guava features.

```java
public class RealWorldApplications {
    
    /**
     * Exercise 5a: E-commerce Analytics Platform
     * Build a comprehensive e-commerce analytics system
     */
    public static class EcommerceAnalytics {
        
        // TODO: Design complete system using:
        // - Table for sales data (Product, Date, Metrics)
        // - Multimap for customer behavior tracking
        // - Cache for expensive analytics computations
        // - Range for time-based queries
        // - Functional transformations for data processing
        
        public void recordSale(String productId, String customerId, double amount, Date date) {
            // TODO: Record sale transaction
        }
        
        public void recordCustomerAction(String customerId, String action, String productId) {
            // TODO: Track customer behavior
        }
        
        public SalesReport generateSalesReport(Date startDate, Date endDate) {
            // TODO: Generate comprehensive sales report
            return null;
        }
        
        public CustomerInsights analyzeCustomerBehavior(String customerId) {
            // TODO: Analyze individual customer behavior
            return null;
        }
        
        public List<String> getTopSellingProducts(int limit) {
            // TODO: Get top N selling products
            return null;
        }
        
        public Map<String, Double> getRevenueByCategory() {
            // TODO: Calculate revenue breakdown by category
            return null;
        }
        
        public TrendAnalysis analyzeTrends(String productId, int days) {
            // TODO: Analyze sales trends for product
            return null;
        }
        
        public List<String> getCustomerRecommendations(String customerId) {
            // TODO: Generate product recommendations
            return null;
        }
        
        public void optimizePerformance() {
            // TODO: Optimize caches and data structures
        }
    }
    
    /**
     * Exercise 5b: Content Management System
     * Build a CMS with caching, search, and analytics
     */
    public static class ContentManagementSystem {
        
        // TODO: Implement using:
        // - Cache for content retrieval
        // - Multimap for tagging system
        // - Table for content metrics
        // - String processing for search
        // - Functional programming for transformations
        
        public void publishContent(String contentId, String title, String body, 
                                 List<String> tags, String author) {
            // TODO: Publish new content
        }
        
        public Optional<Content> getContent(String contentId) {
            // TODO: Retrieve content with caching
            return null;
        }
        
        public List<Content> searchContent(String query) {
            // TODO: Search content by title, body, or tags
            return null;
        }
        
        public List<Content> getContentByTag(String tag) {
            // TODO: Get all content with specific tag
            return null;
        }
        
        public ContentMetrics getContentMetrics(String contentId) {
            // TODO: Get detailed content analytics
            return null;
        }
        
        public List<String> getTrendingTags(int limit) {
            // TODO: Get most popular tags
            return null;
        }
        
        public void updateContent(String contentId, String title, String body, 
                                List<String> tags) {
            // TODO: Update existing content
        }
        
        public SearchResults performAdvancedSearch(SearchCriteria criteria) {
            // TODO: Advanced search with multiple criteria
            return null;
        }
    }
    
    /**
     * Exercise 5c: Monitoring and Alerting System
     * Build a system monitoring solution
     */
    public static class MonitoringSystem {
        
        // TODO: Create comprehensive monitoring using:
        // - Time-series data storage (Table)
        // - Metric aggregation (functional programming)
        // - Alert rule evaluation (Range, Predicates)
        // - Performance optimization (Caching)
        
        public void recordMetric(String service, String metric, double value, Date timestamp) {
            // TODO: Store metric data point
        }
        
        public void defineAlert(String alertName, String service, String metric, 
                              Range<Double> threshold, Duration window) {
            // TODO: Define alert rule
        }
        
        public List<Alert> checkAlerts() {
            // TODO: Evaluate all alert rules
            return null;
        }
        
        public ServiceHealth getServiceHealth(String service) {
            // TODO: Calculate overall service health
            return null;
        }
        
        public Map<String, Double> getAggregatedMetrics(String service, Duration window) {
            // TODO: Aggregate metrics over time window
            return null;
        }
        
        public List<MetricTrend> analyzeTrends(String service, String metric, int days) {
            // TODO: Analyze metric trends
            return null;
        }
        
        public void optimizeStorage() {
            // TODO: Optimize metric storage and retrieval
        }
        
        public MonitoringDashboard generateDashboard(List<String> services) {
            // TODO: Generate monitoring dashboard
            return null;
        }
    }
}
```

### **Exercise 6: Performance Optimization**

**Task**: Optimize Guava applications for production use.

```java
public class PerformanceOptimization {
    
    /**
     * Exercise 6a: Cache Optimization
     * Optimize cache configurations for different scenarios
     */
    public static class CacheOptimizer {
        
        public Cache<String, String> createOptimalCache(CacheScenario scenario) {
            // TODO: Create optimally configured cache based on scenario
            // Consider: size, TTL, weigher, eviction policy, concurrency level
            return null;
        }
        
        public void benchmarkCachePerformance(Cache<String, String> cache) {
            // TODO: Benchmark cache performance
            // Measure: hit rate, throughput, latency, memory usage
        }
        
        public CacheConfiguration tuneCache(Cache<String, String> cache, 
                                          WorkloadPattern workload) {
            // TODO: Analyze workload and suggest optimal configuration
            return null;
        }
        
        public enum CacheScenario {
            HIGH_FREQUENCY_READS, LARGE_OBJECTS, SHORT_TTL, MEMORY_CONSTRAINED
        }
    }
    
    /**
     * Exercise 6b: Collection Performance
     * Optimize collection usage for different performance requirements
     */
    public static class CollectionOptimizer {
        
        public <T> Collection<T> chooseOptimalCollection(CollectionRequirement requirement) {
            // TODO: Choose optimal collection type based on requirements
            return null;
        }
        
        public void benchmarkCollectionOperations() {
            // TODO: Benchmark different collection types for various operations
            // Compare: insertion, lookup, iteration, memory usage
        }
        
        public Map<String, PerformanceMetrics> analyzeCollectionPerformance(
                List<CollectionType> types, int dataSize) {
            // TODO: Analyze performance characteristics
            return null;
        }
        
        public enum CollectionRequirement {
            FAST_LOOKUP, MEMORY_EFFICIENT, FAST_ITERATION, CONCURRENT_ACCESS
        }
        
        public enum CollectionType {
            ARRAY_LIST, HASH_SET, TREE_MAP, IMMUTABLE_LIST, MULTISET
        }
    }
    
    /**
     * Exercise 6c: Memory Optimization
     * Optimize memory usage in Guava applications
     */
    public static class MemoryOptimizer {
        
        public void analyzeMemoryUsage() {
            // TODO: Analyze memory footprint of different Guava collections
        }
        
        public Collection<String> createMemoryEfficientCollection(int expectedSize) {
            // TODO: Create collection optimized for memory usage
            return null;
        }
        
        public void optimizeForLowMemory() {
            // TODO: Configure Guava components for low-memory environments
        }
        
        public MemoryReport generateMemoryReport() {
            // TODO: Generate detailed memory usage report
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
import com.google.common.collect.*;

class GuavaExercisesTest {
    
    @Test
    void testShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        
        cart.addItem("Apple", 5);
        cart.addItem("Banana", 3);
        cart.addItem("Apple", 2);
        
        assertEquals(7, cart.getItemCount("Apple"));
        assertEquals(3, cart.getItemCount("Banana"));
        assertEquals(0, cart.getItemCount("Orange"));
        
        cart.removeItem("Apple", 2);
        assertEquals(5, cart.getItemCount("Apple"));
    }
    
    @Test
    void testTextProcessor() {
        List<String> words = Arrays.asList("Hello", null, "World");
        String result = TextProcessor.joinWords(words, " ");
        assertEquals("Hello World", result);
        
        List<String> split = TextProcessor.splitSentence("Hello   World  ");
        assertEquals(Arrays.asList("Hello", "World"), split);
    }
    
    @Test
    void testOptionalChaining() {
        UserProfileService service = new UserProfileService();
        String displayName = service.getDisplayName("nonexistent");
        assertEquals("Anonymous", displayName);
    }
    
    @Test
    void testCachePerformance() {
        DatabaseResultCache cache = new DatabaseResultCache();
        
        // First call - cache miss
        long start = System.nanoTime();
        String result1 = cache.getUserById("user1");
        long firstCall = System.nanoTime() - start;
        
        // Second call - cache hit  
        start = System.nanoTime();
        String result2 = cache.getUserById("user1");
        long secondCall = System.nanoTime() - start;
        
        assertEquals(result1, result2);
        assertTrue(secondCall < firstCall); // Cache should be faster
        
        CacheStats stats = cache.getCacheStatistics();
        assertEquals(1, stats.hitCount());
        assertEquals(1, stats.missCount());
    }
}
```

---

## 🎯 **Success Criteria**

### **Bronze Level**: ✅ Complete Exercises 1-2
- Master basic Guava collections and utilities
- Implement null-safe programming with Optional
- Use string processing utilities effectively
- Apply basic validation with Preconditions

### **Silver Level**: ✅ Complete Exercises 3-4  
- Design complex systems with advanced collections
- Implement sophisticated caching strategies
- Handle multi-dimensional data with Tables
- Create high-performance data structures

### **Gold Level**: ✅ Complete Exercises 5-6
- Build complete real-world applications
- Optimize performance for production use
- Design scalable and maintainable systems
- Master advanced Guava patterns and best practices

---

## 💡 **Tips for Success**

1. **Start Simple**: Begin with basic collections before moving to advanced types
2. **Measure Performance**: Always benchmark your solutions
3. **Handle Edge Cases**: Use Preconditions and Optional for robustness
4. **Cache Wisely**: Configure caches based on access patterns
5. **Memory Matters**: Consider memory impact of immutable collections
6. **Test Thoroughly**: Write comprehensive unit tests
7. **Profile Performance**: Use JProfiler or similar tools for optimization

---

## 🔍 **Common Pitfalls to Avoid**

- **Over-caching**: Don't cache everything - measure first
- **Wrong Collection Type**: Choose collections based on access patterns
- **Memory Leaks**: Configure cache eviction properly
- **Null Confusion**: Don't mix Guava Optional with Java 8+ Optional
- **Performance Assumptions**: Always measure, don't guess
- **Immutable Overuse**: Use mutable collections when appropriate

---

## 📊 **Performance Benchmarking**

```java
// Example benchmarking code
@Benchmark
public void benchmarkGuavaVsJava() {
    // Compare Guava vs standard Java collections
    // Measure throughput, latency, memory usage
}

@Benchmark  
public void benchmarkCacheConfigurations() {
    // Compare different cache configurations
    // Find optimal settings for your use case
}
```

---

**🚀 Ready to master Google Guava? Start with Bronze exercises and work your way up to building production-ready applications!**

Remember: The key to mastering Guava is understanding when and how to use each utility effectively. Focus on solving real problems rather than just using features for the sake of it.