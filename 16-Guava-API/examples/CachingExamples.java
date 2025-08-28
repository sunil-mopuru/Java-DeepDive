/**
 * Guava Caching Examples - Advanced Caching Strategies
 * 
 * This class demonstrates Guava's comprehensive caching capabilities including
 * LoadingCache, Cache configuration, eviction policies, and performance monitoring.
 */

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.*;
import java.util.concurrent.*;

public class CachingExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Guava Caching Examples ===");
        
        // Basic caching concepts
        demonstrateBasicCache();
        
        // Loading cache with automatic population
        demonstrateLoadingCache();
        
        // Advanced cache configuration
        demonstrateAdvancedConfiguration();
        
        // Cache statistics and monitoring
        demonstrateCacheStatistics();
        
        // Real-world caching scenarios
        demonstrateRealWorldScenarios();
    }
    
    /**
     * Demonstrates basic cache operations
     */
    public static void demonstrateBasicCache() {
        System.out.println("\n1. Basic Cache Operations");
        System.out.println("=========================");
        
        // Create a simple cache
        Cache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
        
        System.out.println("Cache created with max size 100, expire after 10 minutes");
        
        // Basic operations
        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        cache.put("user:3", "Charlie");
        
        System.out.println("Added 3 users to cache");
        
        // Retrieve values
        String user1 = cache.getIfPresent("user:1");
        String user4 = cache.getIfPresent("user:4"); // Will be null
        
        System.out.println("user:1 from cache: " + user1);
        System.out.println("user:4 from cache: " + user4);
        
        // Bulk operations
        Map<String, String> users = cache.getAllPresent(
            Arrays.asList("user:1", "user:2", "user:4")
        );
        System.out.println("Bulk retrieve: " + users);
        
        // Cache size and cleanup
        System.out.println("Cache size: " + cache.size());
        cache.invalidate("user:1");
        System.out.println("After invalidating user:1, size: " + cache.size());
        
        // Put all
        Map<String, String> newUsers = Map.of(
            "user:4", "David",
            "user:5", "Eve"
        );
        cache.putAll(newUsers);
        System.out.println("After bulk put, size: " + cache.size());
        
        // Clear all
        cache.invalidateAll();
        System.out.println("After clear all, size: " + cache.size());
    }
    
    /**
     * Demonstrates LoadingCache with automatic value loading
     */
    public static void demonstrateLoadingCache() {
        System.out.println("\n2. Loading Cache");
        System.out.println("================");
        
        // Create a loading cache for user data
        LoadingCache<String, User> userCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .refreshAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<String, User>() {
                @Override
                public User load(String userId) throws Exception {
                    System.out.println("  Loading user from database: " + userId);
                    return loadUserFromDatabase(userId);
                }
                
                @Override
                public ListenableFuture<User> reload(String key, User oldValue) throws Exception {
                    System.out.println("  Refreshing user: " + key);
                    return ListenableFuture.completedFuture(loadUserFromDatabase(key));
                }
            });
        
        System.out.println("Created LoadingCache for users");
        
        try {
            // First access - will trigger load
            System.out.println("First access to user:1");
            User user1 = userCache.get("user:1");
            System.out.println("Got user: " + user1);
            
            // Second access - cache hit
            System.out.println("\nSecond access to user:1");
            User user1Again = userCache.get("user:1");
            System.out.println("Got user (cached): " + user1Again);
            
            // Bulk loading
            System.out.println("\nBulk loading users");
            Map<String, User> users = userCache.getAll(
                Arrays.asList("user:2", "user:3", "user:4")
            );
            System.out.println("Loaded users: " + users.keySet());
            
            // Callable loading (alternative method)
            User user5 = userCache.get("user:5", () -> {
                System.out.println("  Loading user:5 with callable");
                return new User("user:5", "Frank", "frank@example.com");
            });
            System.out.println("User loaded with callable: " + user5);
            
        } catch (ExecutionException e) {
            System.err.println("Error loading from cache: " + e.getMessage());
        }
        
        System.out.println("Final cache size: " + userCache.size());
    }
    
    /**
     * Demonstrates advanced cache configuration options
     */
    public static void demonstrateAdvancedConfiguration() {
        System.out.println("\n3. Advanced Cache Configuration");
        System.out.println("===============================");
        
        // Cache with custom weigher for memory management
        System.out.println("Cache with custom weigher:");
        LoadingCache<String, String> weightedCache = CacheBuilder.newBuilder()
            .maximumWeight(10000)
            .weigher(new Weigher<String, String>() {
                @Override
                public int weigh(String key, String value) {
                    return key.length() + value.length();
                }
            })
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(CacheLoader.from(key -> "Value for " + key));
        
        // Add some data
        try {
            for (int i = 1; i <= 5; i++) {
                String key = "key" + i;
                String value = weightedCache.get(key);
                System.out.println("  " + key + " -> " + value + 
                                 " (weight: " + (key.length() + value.length()) + ")");
            }
        } catch (ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        // Cache with eviction listener
        System.out.println("\nCache with eviction listener:");
        Cache<String, String> evictionCache = CacheBuilder.newBuilder()
            .maximumSize(3)  // Small size to trigger evictions
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> notification) {
                    System.out.println("  Evicted: " + notification.getKey() + 
                                     " -> " + notification.getValue() + 
                                     " (Cause: " + notification.getCause() + ")");
                }
            })
            .build();
        
        // Fill cache beyond capacity to trigger evictions
        for (int i = 1; i <= 5; i++) {
            evictionCache.put("item" + i, "value" + i);
            System.out.println("Added item" + i + ", cache size: " + evictionCache.size());
        }
        
        // Cache with different expiration strategies
        demonstrateExpirationStrategies();
    }
    
    /**
     * Demonstrates different cache expiration strategies
     */
    public static void demonstrateExpirationStrategies() {
        System.out.println("\nExpiration Strategies:");
        
        // Expire after write
        Cache<String, String> expireAfterWrite = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build();
        
        // Expire after access
        Cache<String, String> expireAfterAccess = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.SECONDS)
            .build();
        
        expireAfterWrite.put("key1", "value1");
        expireAfterAccess.put("key2", "value2");
        
        System.out.println("Initial values:");
        System.out.println("  Expire after write: " + expireAfterWrite.getIfPresent("key1"));
        System.out.println("  Expire after access: " + expireAfterAccess.getIfPresent("key2"));
        
        try {
            // Wait 1 second, access the expire-after-access cache
            Thread.sleep(1000);
            System.out.println("\nAfter 1 second (accessing expire-after-access):");
            System.out.println("  Expire after write: " + expireAfterWrite.getIfPresent("key1"));
            System.out.println("  Expire after access: " + expireAfterAccess.getIfPresent("key2"));
            
            // Wait another 1.5 seconds
            Thread.sleep(1500);
            System.out.println("\nAfter 2.5 seconds total:");
            System.out.println("  Expire after write: " + expireAfterWrite.getIfPresent("key1"));
            System.out.println("  Expire after access: " + expireAfterAccess.getIfPresent("key2"));
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Demonstrates cache statistics and monitoring
     */
    public static void demonstrateCacheStatistics() {
        System.out.println("\n4. Cache Statistics and Monitoring");
        System.out.println("==================================");
        
        // Create cache with statistics enabled
        LoadingCache<String, String> monitoredCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .recordStats()  // Enable statistics
            .build(CacheLoader.from(key -> {
                // Simulate expensive operation
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "Expensive value for " + key;
            }));
        
        System.out.println("Created monitored cache");
        
        try {
            // Generate some cache activity
            for (int i = 1; i <= 10; i++) {
                monitoredCache.get("key" + (i % 5));  // Will cause some cache hits
            }
            
            // Access some existing keys for cache hits
            for (int i = 1; i <= 3; i++) {
                monitoredCache.get("key" + i);
            }
            
            // Check some non-existent keys
            monitoredCache.getIfPresent("nonexistent1");
            monitoredCache.getIfPresent("nonexistent2");
            
        } catch (ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        // Print statistics
        CacheStats stats = monitoredCache.stats();
        System.out.println("\nCache Statistics:");
        System.out.println("  Request count: " + stats.requestCount());
        System.out.println("  Hit count: " + stats.hitCount());
        System.out.println("  Miss count: " + stats.missCount());
        System.out.println("  Hit rate: " + String.format("%.2f%%", stats.hitRate() * 100));
        System.out.println("  Miss rate: " + String.format("%.2f%%", stats.missRate() * 100));
        System.out.println("  Load count: " + stats.loadCount());
        System.out.println("  Average load time: " + String.format("%.2f ms", stats.averageLoadPenalty() / 1_000_000.0));
        System.out.println("  Eviction count: " + stats.evictionCount());
        
        // Performance analysis
        if (stats.hitRate() > 0.8) {
            System.out.println("✓ Excellent cache performance (>80% hit rate)");
        } else if (stats.hitRate() > 0.6) {
            System.out.println("⚠ Good cache performance (>60% hit rate)");
        } else {
            System.out.println("⚠ Poor cache performance (<60% hit rate)");
        }
    }
    
    /**
     * Demonstrates real-world caching scenarios
     */
    public static void demonstrateRealWorldScenarios() {
        System.out.println("\n5. Real-World Caching Scenarios");
        System.out.println("================================");
        
        // Scenario 1: Database query result cache
        demonstrateDatabaseCache();
        
        // Scenario 2: Expensive computation cache
        demonstrateComputationCache();
        
        // Scenario 3: Web service response cache
        demonstrateWebServiceCache();
    }
    
    /**
     * Database query result caching
     */
    public static void demonstrateDatabaseCache() {
        System.out.println("\nDatabase Result Cache:");
        
        LoadingCache<String, List<String>> queryCache = CacheBuilder.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .refreshAfterWrite(5, TimeUnit.MINUTES)
            .recordStats()
            .build(new CacheLoader<String, List<String>>() {
                @Override
                public List<String> load(String query) throws Exception {
                    System.out.println("  Executing database query: " + query);
                    return executeQuery(query);
                }
            });
        
        try {
            // Simulate database queries
            String[] queries = {
                "SELECT * FROM users WHERE status = 'active'",
                "SELECT * FROM products WHERE category = 'electronics'",
                "SELECT * FROM users WHERE status = 'active'", // Duplicate
                "SELECT * FROM orders WHERE date > '2023-01-01'"
            };
            
            for (String query : queries) {
                List<String> result = queryCache.get(query);
                System.out.println("Query result size: " + result.size());
            }
            
            CacheStats stats = queryCache.stats();
            System.out.println("Database cache hit rate: " + String.format("%.2f%%", stats.hitRate() * 100));
            
        } catch (ExecutionException e) {
            System.err.println("Database cache error: " + e.getMessage());
        }
    }
    
    /**
     * Expensive computation result caching
     */
    public static void demonstrateComputationCache() {
        System.out.println("\nComputation Cache:");
        
        LoadingCache<Integer, Long> fibonacciCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(CacheLoader.from(this::calculateFibonacci));
        
        try {
            System.out.println("Computing Fibonacci numbers (with caching):");
            long start = System.nanoTime();
            
            // Calculate some Fibonacci numbers
            int[] numbers = {10, 15, 20, 10, 25, 15}; // Some duplicates
            for (int n : numbers) {
                Long result = fibonacciCache.get(n);
                System.out.println("  F(" + n + ") = " + result);
            }
            
            long end = System.nanoTime();
            System.out.println("Total time with cache: " + (end - start) / 1_000_000 + " ms");
            
        } catch (ExecutionException e) {
            System.err.println("Computation cache error: " + e.getMessage());
        }
    }
    
    /**
     * Web service response caching
     */
    public static void demonstrateWebServiceCache() {
        System.out.println("\nWeb Service Response Cache:");
        
        LoadingCache<String, String> webCache = CacheBuilder.newBuilder()
            .maximumSize(200)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .refreshAfterWrite(30, TimeUnit.MINUTES)
            .build(CacheLoader.from(this::fetchFromWebService));
        
        try {
            String[] urls = {
                "https://api.example.com/users/1",
                "https://api.example.com/products/popular",
                "https://api.example.com/users/1", // Duplicate
                "https://api.example.com/weather/current"
            };
            
            for (String url : urls) {
                String response = webCache.get(url);
                System.out.println("  " + url + " -> " + response.substring(0, Math.min(50, response.length())) + "...");
            }
            
        } catch (ExecutionException e) {
            System.err.println("Web service cache error: " + e.getMessage());
        }
    }
    
    // Helper methods for demonstrations
    private static User loadUserFromDatabase(String userId) {
        // Simulate database load time
        try {
            Thread.sleep(50 + (int)(Math.random() * 100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return new User(userId, "User " + userId, userId + "@example.com");
    }
    
    private static List<String> executeQuery(String query) {
        // Simulate query execution time
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Return mock results
        int resultCount = 5 + (int)(Math.random() * 10);
        List<String> results = new ArrayList<>();
        for (int i = 0; i < resultCount; i++) {
            results.add("Result " + i + " for query: " + query.substring(0, Math.min(20, query.length())));
        }
        return results;
    }
    
    private Long calculateFibonacci(Integer n) {
        System.out.println("    Computing F(" + n + ")");
        if (n <= 1) return (long) n;
        
        // Intentionally inefficient recursive calculation to show caching benefit
        return calculateFibonacci(n - 1) + calculateFibonacci(n - 2);
    }
    
    private String fetchFromWebService(String url) {
        System.out.println("    Fetching: " + url);
        
        // Simulate network delay
        try {
            Thread.sleep(300 + (int)(Math.random() * 200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return "Response from " + url + " at " + new Date();
    }
    
    // Supporting classes
    static class User {
        private final String id;
        private final String name;
        private final String email;
        
        public User(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        
        @Override
        public String toString() {
            return "User{id='" + id + "', name='" + name + "', email='" + email + "'}";
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            User user = (User) obj;
            return Objects.equals(id, user.id);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}