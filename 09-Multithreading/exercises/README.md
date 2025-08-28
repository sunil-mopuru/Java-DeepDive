# Multithreading - Practice Exercises

## 📝 Instructions
- Focus on thread safety and synchronization mechanisms
- Practice with different concurrency patterns
- Learn proper use of thread pools and executors
- Handle race conditions and deadlock prevention
- Work with concurrent collections and atomic operations

---

## Exercise 1: Producer-Consumer Pipeline
**Difficulty: Beginner-Intermediate**

Implement a multi-stage producer-consumer system with buffered processing.

### Requirements:
- **Multiple Producers**: Generate data at different rates
- **Processing Pipeline**: Multiple processing stages with queues
- **Multiple Consumers**: Process final results
- **Flow Control**: Handle fast producers and slow consumers
- **Monitoring**: Track throughput and queue sizes

### Implementation:
```java
class DataPipeline {
    private BlockingQueue<RawData> inputQueue;
    private BlockingQueue<ProcessedData> outputQueue;
    private ExecutorService producers;
    private ExecutorService processors;
    private ExecutorService consumers;
    
    public void start() {
        // Start producer threads
        for (int i = 0; i < 3; i++) {
            producers.submit(new DataProducer(inputQueue));
        }
        
        // Start processing threads
        for (int i = 0; i < 2; i++) {
            processors.submit(new DataProcessor(inputQueue, outputQueue));
        }
        
        // Start consumer threads
        consumers.submit(new DataConsumer(outputQueue));
    }
}
```

### Expected Output:
```
=== PIPELINE MONITOR ===
Producers: 3 active
Processors: 2 active  
Consumers: 1 active

Queue Status:
  Input Queue: 45/100 items (45% full)
  Output Queue: 12/50 items (24% full)

Throughput:
  Production Rate: 150 items/sec
  Processing Rate: 145 items/sec
  Consumption Rate: 140 items/sec

⚠️ Warning: Consumer bottleneck detected
```

---

## Exercise 2: Thread-Safe Cache System
**Difficulty: Intermediate**

Build a high-performance, thread-safe caching system with multiple eviction policies.

### Requirements:
- **Thread Safety**: Handle concurrent read/write operations
- **Eviction Policies**: LRU, LFU, Time-based expiration
- **Cache Statistics**: Hit ratio, miss ratio, eviction counts
- **Size Management**: Maximum size limits with automatic cleanup
- **Performance Monitoring**: Track access patterns and performance metrics

### Cache Features:
```java
class ThreadSafeCache<K, V> {
    private final ConcurrentHashMap<K, CacheEntry<V>> cache;
    private final ScheduledExecutorService cleanupExecutor;
    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);
    
    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            hits.incrementAndGet();
            entry.recordAccess(); // Update LRU/LFU data
            return entry.getValue();
        }
        misses.incrementAndGet();
        return null;
    }
    
    public void put(K key, V value, long ttlSeconds) {
        // Implementation with size management and eviction
    }
    
    public CacheStatistics getStatistics() {
        return new CacheStatistics(hits.get(), misses.get(), 
                                 cache.size(), getHitRatio());
    }
}
```

### Performance Test:
```java
// Concurrent cache testing
ExecutorService testThreads = Executors.newFixedThreadPool(10);
ThreadSafeCache<String, String> cache = new ThreadSafeCache<>(1000);

// Simulate concurrent access
for (int i = 0; i < 10; i++) {
    testThreads.submit(() -> {
        for (int j = 0; j < 1000; j++) {
            String key = "key-" + (j % 100);
            String value = cache.get(key);
            if (value == null) {
                cache.put(key, "value-" + j, 60);
            }
        }
    });
}
```

---

## Exercise 3: Parallel File Processor
**Difficulty: Intermediate-Advanced**

Create a system that processes multiple files concurrently with work distribution.

### Requirements:
- **Work Distribution**: Automatically distribute files among threads
- **Progress Tracking**: Real-time progress reporting for each file
- **Error Handling**: Continue processing other files if one fails
- **Resource Management**: Control memory usage and thread allocation
- **Result Aggregation**: Combine results from all processed files

### Parallel Processing System:
```java
class ParallelFileProcessor {
    private final ExecutorService executorService;
    private final CompletionService<FileProcessingResult> completionService;
    private final AtomicInteger completedFiles = new AtomicInteger(0);
    private final AtomicInteger failedFiles = new AtomicInteger(0);
    
    public ProcessingReport processFiles(List<File> files, 
                                       FileProcessor processor) {
        
        // Submit all files for processing
        for (File file : files) {
            completionService.submit(() -> {
                try {
                    return processor.process(file);
                } catch (Exception e) {
                    failedFiles.incrementAndGet();
                    throw new FileProcessingException(file, e);
                }
            });
        }
        
        // Collect results as they complete
        List<FileProcessingResult> results = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            try {
                Future<FileProcessingResult> future = completionService.take();
                FileProcessingResult result = future.get();
                results.add(result);
                completedFiles.incrementAndGet();
                reportProgress(completedFiles.get(), files.size());
            } catch (Exception e) {
                handleProcessingError(e);
            }
        }
        
        return new ProcessingReport(results, completedFiles.get(), failedFiles.get());
    }
}
```

### Usage Example:
```java
ParallelFileProcessor processor = new ParallelFileProcessor(4); // 4 threads

List<File> imageFiles = findImageFiles("/photos");
ProcessingReport report = processor.processFiles(imageFiles, 
    file -> {
        // Resize image, extract metadata, generate thumbnail
        return new ImageProcessingResult(file, resizedImage, metadata);
    });

System.out.printf("Processed: %d, Failed: %d, Total time: %s%n",
    report.getSuccessCount(), report.getFailureCount(), 
    report.getDuration());
```

---

## Exercise 4: Concurrent Web Crawler
**Difficulty: Advanced**

Build a multi-threaded web crawler that efficiently crawls websites while respecting rate limits.

### Requirements:
- **Concurrent Crawling**: Multiple threads crawling different pages
- **URL Queue Management**: Thread-safe URL queue with deduplication
- **Rate Limiting**: Respect robots.txt and implement politeness delays
- **Link Extraction**: Extract and normalize links from HTML content
- **Depth Control**: Limit crawling depth and implement breadth-first traversal
- **Content Processing**: Extract and analyze page content

### Web Crawler Architecture:
```java
class ConcurrentWebCrawler {
    private final ExecutorService crawlerThreads;
    private final BlockingQueue<CrawlTask> urlQueue;
    private final Set<String> visitedUrls;
    private final RateLimiter rateLimiter;
    private final AtomicInteger activeCrawlers = new AtomicInteger(0);
    private volatile boolean shutdown = false;
    
    public CrawlResults crawlWebsite(String startUrl, int maxDepth, int maxPages) {
        urlQueue.offer(new CrawlTask(startUrl, 0));
        
        // Start crawler threads
        for (int i = 0; i < THREAD_COUNT; i++) {
            crawlerThreads.submit(new CrawlerWorker());
        }
        
        // Monitor progress and shutdown condition
        while (!shouldShutdown() && getVisitedCount() < maxPages) {
            try {
                Thread.sleep(1000); // Check every second
                reportProgress();
            } catch (InterruptedException e) {
                shutdown();
                break;
            }
        }
        
        return new CrawlResults(visitedUrls, extractedData, crawlStatistics);
    }
    
    private class CrawlerWorker implements Runnable {
        @Override
        public void run() {
            activeCrawlers.incrementAndGet();
            try {
                while (!shutdown && !Thread.currentThread().isInterrupted()) {
                    CrawlTask task = urlQueue.poll(5, TimeUnit.SECONDS);
                    if (task != null) {
                        processUrl(task);
                    } else if (urlQueue.isEmpty() && activeCrawlers.get() == 1) {
                        // No more URLs and this is the last active crawler
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                activeCrawlers.decrementAndGet();
            }
        }
    }
}
```

### Crawl Results:
```
=== WEB CRAWL REPORT ===
Start URL: https://example.com
Max Depth: 3
Duration: 2 minutes 45 seconds

Pages Crawled: 1,234
Unique URLs Found: 5,678
Failed Requests: 15
Average Response Time: 245ms

Content Analysis:
  Images Found: 3,456
  Documents: 234 (PDF: 123, DOC: 89, PPT: 22)
  External Links: 2,890
  Email Addresses: 45

Top Domains:
  example.com: 892 pages
  blog.example.com: 231 pages  
  docs.example.com: 111 pages
```

---

## Exercise 5: Task Scheduler Framework
**Difficulty: Advanced**

Develop a sophisticated task scheduling framework with priorities, dependencies, and retry mechanisms.

### Requirements:
- **Task Dependencies**: Tasks can depend on completion of other tasks
- **Priority Scheduling**: High-priority tasks execute before low-priority ones
- **Retry Logic**: Automatic retry with exponential backoff for failed tasks
- **Resource Management**: Control concurrent resource usage
- **Monitoring**: Real-time monitoring of task execution and queue status

### Scheduler Framework:
```java
class TaskScheduler {
    private final PriorityBlockingQueue<ScheduledTask> taskQueue;
    private final Map<String, TaskResult> completedTasks;
    private final ExecutorService workerThreads;
    private final Map<String, Semaphore> resourceSemaphores;
    
    public TaskHandle submitTask(Task task, TaskOptions options) {
        ScheduledTask scheduledTask = new ScheduledTask(task, options);
        
        // Check dependencies
        if (options.getDependencies().isEmpty() || 
            allDependenciesCompleted(options.getDependencies())) {
            taskQueue.offer(scheduledTask);
        } else {
            // Add to dependency wait list
            dependencyManager.addWaitingTask(scheduledTask);
        }
        
        return new TaskHandle(scheduledTask.getTaskId());
    }
    
    private class TaskWorker implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    ScheduledTask task = taskQueue.take();
                    
                    // Acquire required resources
                    acquireResources(task.getRequiredResources());
                    
                    try {
                        TaskResult result = executeTaskWithRetry(task);
                        handleTaskCompletion(task, result);
                    } finally {
                        releaseResources(task.getRequiredResources());
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
```

### Task Definition Example:
```java
// Define tasks with dependencies and resources
TaskOptions emailTask = new TaskOptions()
    .setPriority(Priority.HIGH)
    .setRequiredResources("email-service")
    .setMaxRetries(3)
    .setTimeout(Duration.ofMinutes(5));

TaskOptions reportTask = new TaskOptions()
    .setPriority(Priority.MEDIUM)
    .addDependency("data-processing-task")
    .setRequiredResources("database", "file-system")
    .setMaxRetries(1);

// Submit tasks
TaskHandle emailHandle = scheduler.submitTask(new SendEmailTask(), emailTask);
TaskHandle reportHandle = scheduler.submitTask(new GenerateReportTask(), reportTask);

// Monitor progress
TaskStatus emailStatus = emailHandle.getStatus();
TaskResult reportResult = reportHandle.get(10, TimeUnit.MINUTES);
```

---

## Exercise 6: Distributed Lock Manager
**Difficulty: Expert**

Implement a distributed lock manager for coordinating access to shared resources across multiple JVMs.

### Requirements:
- **Distributed Locking**: Coordinate locks across multiple JVM instances
- **Lock Types**: Exclusive locks, shared (read) locks, upgradeable locks
- **Deadlock Detection**: Detect and resolve deadlock situations
- **Lock Leasing**: Automatic lock release after timeout
- **Fault Tolerance**: Handle node failures gracefully

### Lock Manager Implementation:
```java
class DistributedLockManager {
    private final Map<String, LockInfo> localLocks;
    private final NetworkCommunicator networkComm;
    private final DeadlockDetector deadlockDetector;
    private final ScheduledExecutorService leaseManager;
    
    public DistributedLock acquireLock(String resource, LockType type, 
                                     Duration timeout) throws LockException {
        
        LockRequest request = new LockRequest(resource, type, 
                                            generateRequestId(), timeout);
        
        // Try to acquire lock locally first
        if (tryAcquireLocal(request)) {
            // Broadcast lock acquisition to other nodes
            boolean globalSuccess = broadcastLockAcquisition(request);
            
            if (globalSuccess) {
                return new DistributedLock(resource, request.getRequestId());
            } else {
                releaseLocal(request);
                throw new LockException("Failed to acquire global lock");
            }
        } else {
            throw new LockException("Resource already locked locally");
        }
    }
    
    public void releaseLock(DistributedLock lock) {
        // Release local lock
        releaseLocal(lock.getResource());
        
        // Notify other nodes
        broadcastLockRelease(lock.getResource(), lock.getRequestId());
    }
}
```

---

## 🎯 Challenge Projects

### Project A: Real-time Data Processing Engine
Build a stream processing engine that:
- Processes continuous data streams with multiple operators
- Supports windowing operations (time-based, count-based)
- Implements backpressure handling for flow control
- Provides exactly-once processing guarantees
- Scales dynamically based on load

### Project B: Distributed Task Queue System
Create a distributed task queue that:
- Distributes tasks across multiple worker nodes
- Supports task priorities and deadlines
- Implements work stealing for load balancing
- Handles node failures with task redistribution
- Provides monitoring and administrative interfaces

### Project C: Parallel Computation Framework
Develop a framework for parallel computations that:
- Automatically parallelizes recursive algorithms
- Implements work-stealing thread pools
- Supports fork-join parallelism patterns
- Provides performance monitoring and tuning
- Handles large-scale data processing efficiently

---

## 📚 Testing Guidelines

### Concurrency Testing:
- Test with varying numbers of threads
- Use stress testing with high concurrency
- Test race condition scenarios
- Verify thread safety under load

### Performance Testing:
- Measure throughput under different loads
- Monitor CPU utilization and context switching
- Test scalability with increased parallelism
- Profile for hotspots and bottlenecks

### Reliability Testing:
- Test behavior under resource constraints
- Simulate thread interruptions
- Test exception handling in concurrent scenarios
- Verify proper resource cleanup

### Deadlock Testing:
- Create scenarios prone to deadlocks
- Test deadlock detection algorithms
- Verify automatic deadlock resolution
- Monitor for livelock situations

---

**Next:** [Advanced Topics Exercises](../../10-Advanced-Topics/exercises/)