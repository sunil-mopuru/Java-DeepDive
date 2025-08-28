/**
 * MultithreadingExamples.java - Comprehensive Multithreading Examples
 * 
 * This program demonstrates various multithreading concepts including thread creation,
 * synchronization, concurrent collections, and practical parallel programming scenarios.
 */

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class MultithreadingExamples {
    
    public static void main(String[] args) throws InterruptedException {
        
        System.out.println("=== MULTITHREADING COMPREHENSIVE EXAMPLES ===\n");
        
        // 1. BASIC THREAD CREATION
        basicThreadCreation();
        
        // 2. THREAD SYNCHRONIZATION
        threadSynchronization();
        
        // 3. PRODUCER-CONSUMER PROBLEM
        producerConsumerDemo();
        
        // 4. THREAD POOLS
        threadPoolDemo();
        
        // 5. CONCURRENT COLLECTIONS
        concurrentCollectionsDemo();
        
        // 6. PRACTICAL APPLICATIONS
        practicalApplications();
    }
    
    // ==================== BASIC THREAD CREATION ====================
    
    public static void basicThreadCreation() throws InterruptedException {
        System.out.println("1. BASIC THREAD CREATION:");
        System.out.println("--------------------------");
        
        // Method 1: Extending Thread class
        class MyThread extends Thread {
            private final String taskName;
            
            public MyThread(String taskName) {
                this.taskName = taskName;
            }
            
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    System.out.printf("%s: Count %d%n", taskName, i);
                    try {
                        Thread.sleep(100); // Simulate work
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                System.out.printf("%s completed%n", taskName);
            }
        }
        
        // Method 2: Implementing Runnable interface
        class MyTask implements Runnable {
            private final String taskName;
            
            public MyTask(String taskName) {
                this.taskName = taskName;
            }
            
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    System.out.printf("%s: Task %d%n", taskName, i);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                System.out.printf("%s finished%n", taskName);
            }
        }
        
        // Create and start threads
        MyThread thread1 = new MyThread("Thread-A");
        Thread thread2 = new Thread(new MyTask("Thread-B"));
        
        // Lambda expression (Java 8+)
        Thread thread3 = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.printf("Lambda-Thread: Step %d%n", i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Lambda-Thread done");
        });
        
        // Start all threads
        thread1.start();
        thread2.start();
        thread3.start();
        
        // Wait for all threads to complete
        thread1.join();
        thread2.join();
        thread3.join();
        
        System.out.println("All threads completed\n");
    }
    
    // ==================== THREAD SYNCHRONIZATION ====================
    
    public static void threadSynchronization() throws InterruptedException {
        System.out.println("2. THREAD SYNCHRONIZATION:");
        System.out.println("---------------------------");
        
        // Shared counter without synchronization (race condition)
        UnsafeCounter unsafeCounter = new UnsafeCounter();
        
        // Create threads that increment counter
        Thread[] unsafeThreads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    unsafeCounter.increment();
                }
            });
        }
        
        // Start and wait for unsafe threads
        for (Thread thread : unsafeThreads) {
            thread.start();
        }
        for (Thread thread : unsafeThreads) {
            thread.join();
        }
        
        System.out.printf("Unsafe counter result: %d (expected: 5000)%n", 
                         unsafeCounter.getValue());
        
        // Safe counter with synchronization
        SafeCounter safeCounter = new SafeCounter();
        
        Thread[] safeThreads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            safeThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    safeCounter.increment();
                }
            });
        }
        
        // Start and wait for safe threads
        for (Thread thread : safeThreads) {
            thread.start();
        }
        for (Thread thread : safeThreads) {
            thread.join();
        }
        
        System.out.printf("Safe counter result: %d (expected: 5000)%n", 
                         safeCounter.getValue());
        
        // Atomic counter (lock-free)
        AtomicCounter atomicCounter = new AtomicCounter();
        
        Thread[] atomicThreads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            atomicThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    atomicCounter.increment();
                }
            });
        }
        
        // Start and wait for atomic threads
        for (Thread thread : atomicThreads) {
            thread.start();
        }
        for (Thread thread : atomicThreads) {
            thread.join();
        }
        
        System.out.printf("Atomic counter result: %d (expected: 5000)%n", 
                         atomicCounter.getValue());
        
        System.out.println();
    }
    
    // ==================== PRODUCER-CONSUMER PROBLEM ====================
    
    public static void producerConsumerDemo() throws InterruptedException {
        System.out.println("3. PRODUCER-CONSUMER PROBLEM:");
        System.out.println("------------------------------");
        
        // Using BlockingQueue
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        
        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String item = "Item-" + i;
                    queue.put(item);
                    System.out.printf("Produced: %s%n", item);
                    Thread.sleep(100);
                }
                queue.put("STOP"); // Signal to stop
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                String item;
                while (!(item = queue.take()).equals("STOP")) {
                    System.out.printf("Consumed: %s%n", item);
                    Thread.sleep(150);
                }
                System.out.println("Consumer finished");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        producer.start();
        consumer.start();
        
        producer.join();
        consumer.join();
        
        // Custom Buffer with wait/notify
        System.out.println("\nUsing custom buffer with wait/notify:");
        
        CustomBuffer buffer = new CustomBuffer(5);
        
        // Multiple producers and consumers
        Thread producer1 = new Thread(new Producer(buffer, "Producer-1", 5));
        Thread producer2 = new Thread(new Producer(buffer, "Producer-2", 3));
        Thread consumer1 = new Thread(new Consumer(buffer, "Consumer-1"));
        Thread consumer2 = new Thread(new Consumer(buffer, "Consumer-2"));
        
        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
        
        producer1.join();
        producer2.join();
        
        // Let consumers finish processing
        Thread.sleep(1000);
        consumer1.interrupt();
        consumer2.interrupt();
        
        System.out.println();
    }
    
    // ==================== THREAD POOLS ====================
    
    public static void threadPoolDemo() throws InterruptedException {
        System.out.println("4. THREAD POOLS:");
        System.out.println("----------------");
        
        // Fixed thread pool
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        
        System.out.println("Fixed thread pool (3 threads):");
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            fixedPool.submit(() -> {
                System.out.printf("Task %d executed by %s%n", 
                                taskId, Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        fixedPool.shutdown();
        fixedPool.awaitTermination(10, TimeUnit.SECONDS);
        
        // Cached thread pool
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        
        System.out.println("\nCached thread pool:");
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            cachedPool.submit(() -> {
                System.out.printf("Cached task %d by %s%n", 
                                taskId, Thread.currentThread().getName());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        cachedPool.shutdown();
        cachedPool.awaitTermination(5, TimeUnit.SECONDS);
        
        // Scheduled thread pool
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);
        
        System.out.println("\nScheduled thread pool:");
        
        // Schedule one-time task
        scheduledPool.schedule(() -> {
            System.out.println("One-time scheduled task executed");
        }, 1, TimeUnit.SECONDS);
        
        // Schedule repeating task
        ScheduledFuture<?> repeatingTask = scheduledPool.scheduleAtFixedRate(() -> {
            System.out.println("Repeating task: " + new Date());
        }, 0, 2, TimeUnit.SECONDS);
        
        // Let it run for a while
        Thread.sleep(7000);
        repeatingTask.cancel(false);
        
        scheduledPool.shutdown();
        
        // CompletableFuture demo
        System.out.println("\nCompletableFuture example:");
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Hello";
        });
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "World";
        });
        
        CompletableFuture<String> combined = future1.thenCombine(future2, 
            (s1, s2) -> s1 + " " + s2 + "!");
        
        System.out.println("Combined result: " + combined.get());
        
        System.out.println();
    }
    
    // ==================== CONCURRENT COLLECTIONS ====================
    
    public static void concurrentCollectionsDemo() throws InterruptedException {
        System.out.println("5. CONCURRENT COLLECTIONS:");
        System.out.println("---------------------------");
        
        // ConcurrentHashMap
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        
        // Multiple threads updating map
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    String key = "key-" + (j % 10);
                    concurrentMap.compute(key, (k, v) -> (v == null ? 0 : v) + 1);
                }
                System.out.printf("Thread %d completed map updates%n", threadId);
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("ConcurrentHashMap results:");
        concurrentMap.forEach((key, value) -> 
            System.out.printf("  %s: %d%n", key, value));
        
        // CopyOnWriteArrayList
        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();
        
        // Writer thread
        Thread writer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                cowList.add("Item-" + i);
                System.out.printf("Added: Item-%d%n", i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        // Reader threads
        Thread reader1 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.printf("Reader1 size: %d%n", cowList.size());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        writer.start();
        reader1.start();
        
        writer.join();
        Thread.sleep(500);
        reader1.interrupt();
        
        System.out.println("Final CopyOnWriteArrayList: " + cowList);
        
        System.out.println();
    }
    
    // ==================== PRACTICAL APPLICATIONS ====================
    
    public static void practicalApplications() throws InterruptedException {
        System.out.println("6. PRACTICAL APPLICATIONS:");
        System.out.println("--------------------------");
        
        // Parallel file processor
        ParallelFileProcessor fileProcessor = new ParallelFileProcessor();
        
        List<String> files = Arrays.asList(
            "file1.txt", "file2.txt", "file3.txt", "file4.txt", "file5.txt"
        );
        
        System.out.println("Processing files in parallel:");
        fileProcessor.processFiles(files);
        
        // Download manager
        DownloadManager downloadManager = new DownloadManager(3);
        
        List<String> urls = Arrays.asList(
            "http://example.com/file1.zip",
            "http://example.com/file2.zip",
            "http://example.com/file3.zip",
            "http://example.com/file4.zip"
        );
        
        System.out.println("\nSimulating concurrent downloads:");
        downloadManager.downloadFiles(urls);
        downloadManager.shutdown();
        
        // Web server simulation
        WebServerSimulator webServer = new WebServerSimulator(5);
        
        System.out.println("\nSimulating web server with thread pool:");
        webServer.start();
        
        // Simulate client requests
        for (int i = 1; i <= 10; i++) {
            final int requestId = i;
            new Thread(() -> webServer.handleRequest("Request-" + requestId)).start();
            Thread.sleep(100);
        }
        
        Thread.sleep(3000);
        webServer.stop();
        
        System.out.println();
    }
}

// ==================== COUNTER CLASSES ====================

class UnsafeCounter {
    private int count = 0;
    
    public void increment() {
        count++; // Not thread-safe
    }
    
    public int getValue() {
        return count;
    }
}

class SafeCounter {
    private int count = 0;
    
    public synchronized void increment() {
        count++; // Thread-safe with synchronization
    }
    
    public synchronized int getValue() {
        return count;
    }
}

class AtomicCounter {
    private final AtomicInteger count = new AtomicInteger(0);
    
    public void increment() {
        count.incrementAndGet(); // Atomic operation
    }
    
    public int getValue() {
        return count.get();
    }
}

// ==================== PRODUCER-CONSUMER CLASSES ====================

class CustomBuffer {
    private final Queue<String> buffer = new LinkedList<>();
    private final int capacity;
    
    public CustomBuffer(int capacity) {
        this.capacity = capacity;
    }
    
    public synchronized void put(String item) throws InterruptedException {
        while (buffer.size() == capacity) {
            wait(); // Wait until buffer has space
        }
        buffer.offer(item);
        System.out.printf("Produced: %s (buffer size: %d)%n", item, buffer.size());
        notifyAll(); // Notify waiting consumers
    }
    
    public synchronized String take() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait(); // Wait until buffer has items
        }
        String item = buffer.poll();
        System.out.printf("Consumed: %s (buffer size: %d)%n", item, buffer.size());
        notifyAll(); // Notify waiting producers
        return item;
    }
}

class Producer implements Runnable {
    private final CustomBuffer buffer;
    private final String name;
    private final int itemCount;
    
    public Producer(CustomBuffer buffer, String name, int itemCount) {
        this.buffer = buffer;
        this.name = name;
        this.itemCount = itemCount;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 1; i <= itemCount; i++) {
                String item = name + "-Item-" + i;
                buffer.put(item);
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private final CustomBuffer buffer;
    private final String name;
    
    public Consumer(CustomBuffer buffer, String name) {
        this.buffer = buffer;
        this.name = name;
    }
    
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String item = buffer.take();
                System.out.printf("%s processed %s%n", name, item);
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// ==================== PRACTICAL APPLICATION CLASSES ====================

class ParallelFileProcessor {
    
    public void processFiles(List<String> files) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        List<Future<String>> futures = new ArrayList<>();
        
        for (String file : files) {
            Future<String> future = executor.submit(() -> {
                try {
                    // Simulate file processing
                    Thread.sleep(1000 + (int)(Math.random() * 1000));
                    return "Processed " + file;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "Failed to process " + file;
                }
            });
            futures.add(future);
        }
        
        // Collect results
        for (Future<String> future : futures) {
            try {
                System.out.println("  " + future.get());
            } catch (Exception e) {
                System.err.println("  Error: " + e.getMessage());
            }
        }
        
        executor.shutdown();
    }
}

class DownloadManager {
    private final ExecutorService executor;
    
    public DownloadManager(int threadCount) {
        this.executor = Executors.newFixedThreadPool(threadCount);
    }
    
    public void downloadFiles(List<String> urls) {
        for (String url : urls) {
            executor.submit(() -> {
                try {
                    System.out.printf("Started downloading: %s by %s%n", 
                                    url, Thread.currentThread().getName());
                    
                    // Simulate download time
                    Thread.sleep(2000 + (int)(Math.random() * 2000));
                    
                    System.out.printf("Completed downloading: %s%n", url);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.printf("Download interrupted: %s%n", url);
                }
            });
        }
    }
    
    public void shutdown() throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }
    }
}

class WebServerSimulator {
    private final ExecutorService threadPool;
    private volatile boolean running = false;
    
    public WebServerSimulator(int threadPoolSize) {
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
    }
    
    public void start() {
        running = true;
        System.out.println("Web server started with " + 
                          ((ThreadPoolExecutor)threadPool).getCorePoolSize() + " threads");
    }
    
    public void handleRequest(String request) {
        if (!running) {
            System.out.println("Server not running, rejecting: " + request);
            return;
        }
        
        threadPool.submit(() -> {
            try {
                System.out.printf("Processing %s by %s%n", 
                                request, Thread.currentThread().getName());
                
                // Simulate request processing
                Thread.sleep(500 + (int)(Math.random() * 1000));
                
                System.out.printf("Completed %s%n", request);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.printf("Request processing interrupted: %s%n", request);
            }
        });
    }
    
    public void stop() throws InterruptedException {
        running = false;
        threadPool.shutdown();
        
        if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("Forcing server shutdown...");
            threadPool.shutdownNow();
        }
        
        System.out.println("Web server stopped");
    }
}