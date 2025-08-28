/**
 * Concurrent Utilities Examples - Async Programming with Guava
 * 
 * Demonstrates ListenableFuture, Service framework, and other concurrent
 * utilities for building robust asynchronous applications.
 */

import com.google.common.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import java.util.concurrent.*;
import java.time.Duration;

public class ConcurrentUtilsExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Guava Concurrent Utilities Examples ===");
        
        demonstrateListenableFuture();
        demonstrateServiceFramework();
        demonstrateRateLimiter();
        demonstrateAdvancedPatterns();
    }
    
    public static void demonstrateListenableFuture() {
        System.out.println("\n1. ListenableFuture Examples");
        System.out.println("============================");
        
        ListeningExecutorService executor = MoreExecutors.listeningDecorator(
            Executors.newFixedThreadPool(4)
        );
        
        try {
            // Basic ListenableFuture
            ListenableFuture<String> future = executor.submit(() -> {
                Thread.sleep(1000);
                return "Task completed";
            });
            
            // Add callback
            Futures.addCallback(future, new FutureCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("✓ Success: " + result);
                }
                
                @Override
                public void onFailure(Throwable t) {
                    System.out.println("✗ Failure: " + t.getMessage());
                }
            }, MoreExecutors.directExecutor());
            
            // Transform futures
            ListenableFuture<Integer> lengthFuture = Futures.transform(future,
                String::length, MoreExecutors.directExecutor());
            
            // Combine multiple futures
            demonstrateFutureCombination(executor);
            
        } finally {
            executor.shutdown();
        }
    }
    
    public static void demonstrateFutureCombination(ListeningExecutorService executor) {
        System.out.println("\nFuture Combination:");
        
        ListenableFuture<String> future1 = executor.submit(() -> {
            Thread.sleep(500);
            return "Result1";
        });
        
        ListenableFuture<String> future2 = executor.submit(() -> {
            Thread.sleep(700);
            return "Result2";
        });
        
        // Combine results
        ListenableFuture<String> combined = Futures.whenAllSucceed(future1, future2)
            .call(() -> future1.get() + " + " + future2.get(), executor);
        
        try {
            String result = combined.get(2, TimeUnit.SECONDS);
            System.out.println("Combined result: " + result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void demonstrateServiceFramework() {
        System.out.println("\n2. Service Framework");
        System.out.println("====================");
        
        // Custom service example
        DatabaseService dbService = new DatabaseService();
        CacheService cacheService = new CacheService();
        
        // Service manager
        ServiceManager manager = new ServiceManager(Arrays.asList(dbService, cacheService));
        
        try {
            // Start services
            manager.startAsync().awaitHealthy(5, TimeUnit.SECONDS);
            System.out.println("✓ All services started");
            
            // Check service states
            manager.servicesByState().forEach((state, services) -> {
                System.out.println(state + ": " + services.size() + " services");
            });
            
        } catch (TimeoutException e) {
            System.out.println("✗ Services failed to start: " + e.getMessage());
        } finally {
            // Stop services
            manager.stopAsync().awaitStopped(5, TimeUnit.SECONDS);
            System.out.println("✓ All services stopped");
        }
    }
    
    public static void demonstrateRateLimiter() {
        System.out.println("\n3. RateLimiter Examples");
        System.out.println("=======================");
        
        // Create rate limiter (2 permits per second)
        RateLimiter rateLimiter = RateLimiter.create(2.0);
        
        System.out.println("Processing requests with rate limiting:");
        for (int i = 1; i <= 5; i++) {
            long startTime = System.currentTimeMillis();
            rateLimiter.acquire(); // Blocks until permit available
            long endTime = System.currentTimeMillis();
            
            System.out.printf("Request %d processed (waited %d ms)%n", 
                            i, endTime - startTime);
        }
        
        // Try acquire with timeout
        System.out.println("\nTrying to acquire with timeout:");
        if (rateLimiter.tryAcquire(1, TimeUnit.SECONDS)) {
            System.out.println("✓ Acquired permit within timeout");
        } else {
            System.out.println("✗ Could not acquire permit within timeout");
        }
    }
    
    public static void demonstrateAdvancedPatterns() {
        System.out.println("\n4. Advanced Patterns");
        System.out.println("====================");
        
        demonstrateAsyncChaining();
        demonstrateErrorHandling();
        demonstrateTimeouts();
    }
    
    public static void demonstrateAsyncChaining() {
        System.out.println("Async Operation Chaining:");
        
        ListeningExecutorService executor = MoreExecutors.listeningDecorator(
            Executors.newFixedThreadPool(2)
        );
        
        try {
            // Chain async operations
            ListenableFuture<String> step1 = executor.submit(() -> {
                Thread.sleep(300);
                return "User Data";
            });
            
            ListenableFuture<String> step2 = Futures.transformAsync(step1, data -> 
                executor.submit(() -> {
                    Thread.sleep(200);
                    return data + " + Profile";
                }), executor);
            
            ListenableFuture<String> step3 = Futures.transformAsync(step2, enrichedData ->
                executor.submit(() -> {
                    Thread.sleep(100);
                    return enrichedData + " + Preferences";
                }), executor);
            
            String finalResult = step3.get(2, TimeUnit.SECONDS);
            System.out.println("Final result: " + finalResult);
            
        } catch (Exception e) {
            System.out.println("Error in chain: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
    
    public static void demonstrateErrorHandling() {
        System.out.println("\nError Handling:");
        
        ListeningExecutorService executor = MoreExecutors.listeningDecorator(
            Executors.newSingleThreadExecutor()
        );
        
        try {
            ListenableFuture<String> faultyFuture = executor.submit(() -> {
                Thread.sleep(100);
                throw new RuntimeException("Simulated error");
            });
            
            // Handle errors with fallback
            ListenableFuture<String> withFallback = Futures.catching(faultyFuture,
                Exception.class, 
                ex -> "Fallback result: " + ex.getMessage(),
                MoreExecutors.directExecutor());
            
            String result = withFallback.get(1, TimeUnit.SECONDS);
            System.out.println("Result with fallback: " + result);
            
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
    
    public static void demonstrateTimeouts() {
        System.out.println("\nTimeout Handling:");
        
        ListeningExecutorService executor = MoreExecutors.listeningDecorator(
            Executors.newSingleThreadExecutor()
        );
        
        try {
            ListenableFuture<String> slowFuture = executor.submit(() -> {
                Thread.sleep(2000); // Slow operation
                return "Slow result";
            });
            
            // Add timeout
            ListenableFuture<String> withTimeout = Futures.withTimeout(slowFuture,
                500, TimeUnit.MILLISECONDS, 
                Executors.newSingleThreadScheduledExecutor());
            
            try {
                String result = withTimeout.get();
                System.out.println("Result: " + result);
            } catch (ExecutionException e) {
                if (e.getCause() instanceof TimeoutException) {
                    System.out.println("✓ Operation timed out as expected");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
    
    // Supporting service classes
    static class DatabaseService extends AbstractIdleService {
        @Override
        protected void startUp() throws Exception {
            Thread.sleep(100); // Simulate startup
            System.out.println("Database service started");
        }
        
        @Override
        protected void shutDown() throws Exception {
            Thread.sleep(50); // Simulate cleanup
            System.out.println("Database service stopped");
        }
    }
    
    static class CacheService extends AbstractIdleService {
        @Override
        protected void startUp() throws Exception {
            Thread.sleep(80); // Simulate startup
            System.out.println("Cache service started");
        }
        
        @Override
        protected void shutDown() throws Exception {
            Thread.sleep(30); // Simulate cleanup
            System.out.println("Cache service stopped");
        }
    }
}