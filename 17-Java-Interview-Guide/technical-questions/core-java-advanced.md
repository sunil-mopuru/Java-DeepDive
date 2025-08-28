# Advanced Core Java Interview Questions (10+ Years Experience)

## 🔥 **Memory Management & JVM Internals**

### **Question 1: Memory Leak Investigation**
**Scenario**: Your production application is experiencing OutOfMemoryError after running for several hours. Walk me through your investigation process.

**Expected Answer**:
```java
// Step 1: Enable heap dumps on OOM (if not already done)
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/dumps/

// Step 2: Analyze heap dump with Eclipse MAT or similar tool
// Look for:
// - Dominator tree to find largest object retainers
// - Duplicate objects (especially Strings)
// - Suspicious object growth patterns

// Step 3: Common memory leak patterns to check
public class MemoryLeakPatterns {
    // Pattern 1: Static collections that grow indefinitely
    private static final Map<String, User> userCache = new HashMap<>();
    
    // Pattern 2: Listeners not properly removed
    public void addListener() {
        eventSource.addEventListener(this::handleEvent);
        // LEAK: Forgot to remove listener in cleanup
    }
    
    // Pattern 3: ThreadLocal not cleaned up
    private static final ThreadLocal<ExpensiveObject> threadLocal = new ThreadLocal<>();
    
    public void processRequest() {
        threadLocal.set(new ExpensiveObject());
        // LEAK: Should call threadLocal.remove() in finally block
    }
    
    // Pattern 4: Inner class holding reference to outer class
    public class OuterClass {
        private byte[] largeArray = new byte[1024 * 1024];
        
        public void createInnerClass() {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    // This inner class holds reference to OuterClass
                    // Even after OuterClass should be GC'd
                }
            };
            executor.submit(task);
        }
    }
    
    // Pattern 5: Unclosed resources
    public void readFile() throws IOException {
        FileInputStream fis = new FileInputStream("large-file.txt");
        // LEAK: Forgot to close the stream - use try-with-resources instead
        // Better approach:
        try (FileInputStream fis2 = new FileInputStream("large-file.txt")) {
            // Process file
        }
    }
    
    // Pattern 6: ClassLoader leaks
    public class CustomClassLoader extends ClassLoader {
        private static final List<Class<?>> loadedClasses = new ArrayList<>();
        
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            // LEAK: Storing references to loaded classes prevents GC of ClassLoader
            Class<?> clazz = super.findClass(name);
            loadedClasses.add(clazz); // Remove this line
            return clazz;
        }
    }
}

// Step 4: Monitoring and prevention
@Component
public class MemoryMonitor {
    private final MeterRegistry meterRegistry;
    
    @EventListener
    @Async
    public void onMemoryThreshold(MemoryThresholdEvent event) {
        if (event.getUsagePercent() > 90) {
            // Log detailed memory usage
            logMemoryPools();
            
            // Optional: Force GC and re-check
            System.gc();
            
            // Alert operations team
            alertingService.sendAlert("High memory usage: " + event.getUsagePercent() + "%");
        }
    }
    
    // Advanced monitoring with JMX
    public void logMemoryPools() {
        List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean pool : pools) {
            if (pool.getType() == MemoryType.HEAP) {
                MemoryUsage usage = pool.getUsage();
                logger.info("Pool: {} - Used: {} MB, Max: {} MB, Usage: {}%", 
                    pool.getName(),
                    bytesToMB(usage.getUsed()),
                    bytesToMB(usage.getMax()),
                    (usage.getUsed() * 100) / usage.getMax());
            }
        }
    }
    
    private long bytesToMB(long bytes) {
        return bytes / (1024 * 1024);
    }
}

// Advanced heap analysis utility
public class HeapAnalysisUtil {
    
    // Detect duplicate strings in heap
    public void analyzeStringDuplicates() {
        // Using Java Flight Recorder or custom agent to analyze heap
        // Look for strings that are duplicated unnecessarily
        // Consider using String.intern() for frequently used strings
    }
    
    // Detect classloader leaks
    public void detectClassLoaderLeaks() {
        // Monitor ClassLoader instances and their loaded classes
        // Look for ClassLoaders that should have been garbage collected
        List<ClassLoader> classLoaders = getAllClassLoaders();
        for (ClassLoader cl : classLoaders) {
            if (shouldHaveBeenGCed(cl) && !isGCed(cl)) {
                logger.warn("Potential ClassLoader leak detected: {}", cl);
            }
        }
    }
}
```

**Follow-up Questions**:
- How would you differentiate between a memory leak and just high memory usage?
- What's the difference between heap dumps taken with -XX:+HeapDumpOnOutOfMemoryError vs jmap -dump?
- How do you handle memory leaks in production without downtime?
- What are the different generations in the heap and how do they affect GC behavior?
- How would you tune the heap sizes for an application with known memory usage patterns?

---

### **Question 2: GC Algorithm Selection and Tuning**
**Scenario**: You're designing a high-frequency trading application that requires sub-5ms response times. How would you approach GC selection and tuning?

**Expected Answer**:
```java
// Analysis Framework
public class GCSelectionStrategy {
    
    // Application characteristics analysis
    private void analyzeWorkload() {
        /*
         * High-frequency trading requirements:
         * - Ultra-low latency (sub-5ms)
         * - Predictable performance
         * - High allocation rate
         * - Small to medium heap (2-8GB typically)
         */
    }
    
    // GC Algorithm recommendation
    private void selectGCAlgorithm() {
        /*
         * 1st Choice: ZGC (Java 11+)
         * - Sub-10ms pause times regardless of heap size
         * - Concurrent collection
         * - Suitable for latency-sensitive applications
         */
        String zgcConfig = """
            -XX:+UseZGC
            -XX:+UnlockExperimentalVMOptions
            -Xmx4g
            """;
            
        /*
         * 2nd Choice: Shenandoah (OpenJDK)
         * - Low pause times
         * - Good for high allocation rates
         * - More mature than ZGC in some versions
         */
        String shenandoahConfig = """
            -XX:+UseShenandoahGC
            -XX:+UnlockExperimentalVMOptions
            -Xmx4g
            """;
            
        /*
         * Fallback: G1GC with aggressive tuning
         * - More widely available
         * - Predictable pause times
         * - Requires careful tuning
         */
        String g1Config = """
            -XX:+UseG1GC
            -XX:MaxGCPauseMillis=5
            -XX:G1HeapRegionSize=16m
            -XX:G1NewSizePercent=30
            -XX:G1MaxNewSizePercent=40
            -XX:+G1UseAdaptiveIHOP
            """;
            
        /*
         * Legacy: For older Java versions
         * - CMS GC (deprecated in Java 9+)
         * - Parallel GC for throughput-focused applications
         */
        String cmsConfig = """
            -XX:+UseConcMarkSweepGC
            -XX:+CMSParallelRemarkEnabled
            -XX:CMSInitiatingOccupancyFraction=70
            -XX:+UseCMSInitiatingOccupancyOnly
            """;
    }
    
    // Advanced GC tuning parameters
    public class GCTuningParameters {
        /*
         * JVM tuning for ultra-low latency applications:
         * 
         * Memory Management:
         * -XX:+UseCompressedOops                // Reduce memory footprint
         * -XX:+UseCompressedClassPointers       // Reduce metaspace usage
         * -XX:NewRatio=1                        // Equal young/old generation
         * -XX:SurvivorRatio=8                   // Optimize survivor spaces
         * -XX:MaxTenuringThreshold=6            // Tune object aging
         * 
         * GC Specific:
         * -XX:+DisableExplicitGC                // Prevent System.gc() calls
         * -XX:+ScavengeALot                     // For testing GC behavior
         * -XX:+UseStringDeduplication           // Reduce string memory (G1 only)
         * 
         * JIT Compiler:
         * -XX:CompileThreshold=100              // Compile methods sooner
         * -XX:TieredStopAtLevel=1               // Disable C2 compiler if needed
         * -XX:+UseAESIntrinsics                 // Hardware acceleration for crypto
         */
    }
    
    // Monitoring and validation
    @Component
    public class GCLatencyMonitor {
        private final LatencyTracker latencyTracker;
        
        @EventListener
        public void onGCEvent(GCEvent event) {
            if (event.getPauseDuration().toMillis() > 5) {
                // Log detailed GC information
                logGCDetails(event);
                
                // Check if we need to adjust GC parameters
                if (shouldAdjustGCParameters(event)) {
                    suggestGCTuning(event);
                }
            }
        }
        
        // Advanced GC metrics collection
        public void collectAdvancedMetrics() {
            List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
            for (GarbageCollectorMXBean gcBean : gcBeans) {
                logger.info("GC: {} - Collections: {}, Time: {}ms, Avg: {}ms",
                    gcBean.getName(),
                    gcBean.getCollectionCount(),
                    gcBean.getCollectionTime(),
                    gcBean.getCollectionCount() > 0 ? 
                        (double) gcBean.getCollectionTime() / gcBean.getCollectionCount() : 0);
            }
        }
    }
}

// Additional optimizations for ultra-low latency
public class LatencyOptimizations {
    
    // Pre-allocate objects to avoid GC pressure
    private final ObjectPool<OrderProcessor> processorPool = 
        new ObjectPool<>(OrderProcessor::new, 100);
    
    // Use off-heap storage for large data structures
    private final Chronicle.Map<String, Order> orderCache = 
        ChronicleMap.of(String.class, Order.class)
            .entries(1_000_000)
            .create();
    
    // Minimize allocation in hot paths
    public void processOrder(Order order) {
        OrderProcessor processor = processorPool.borrow();
        try {
            // Process without allocating new objects
            processor.process(order);
        } finally {
            processorPool.return(processor);
        }
    }
    
    // Use primitive collections to reduce boxing/unboxing
    private final TIntLongMap orderIdToTimestamp = new TIntLongHashMap();
    
    // Use ThreadLocal for frequently used objects
    private final ThreadLocal<StringBuilder> threadLocalStringBuilder = 
        ThreadLocal.withInitial(() -> new StringBuilder(256));
}

// GC performance testing framework
public class GCPerformanceTest {
    
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(1)
    @Warmup(iterations = 5, time = 5)
    @Measurement(iterations = 10, time = 5)
    public void testAllocationRate() {
        // Simulate allocation-heavy workload
        for (int i = 0; i < 1000; i++) {
            // Create short-lived objects to measure allocation rate
            List<String> temp = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                temp.add("Item " + j);
            }
            // Process temp list
            processList(temp);
        }
    }
    
    private void processList(List<String> list) {
        // Simulate processing
        list.stream().map(String::toUpperCase).count();
    }
}
```

**Follow-up Questions**:
- How would you measure GC impact on application latency?
- What's the trade-off between throughput and latency in GC algorithms?
- How do you handle GC tuning in containerized environments?
- What are the key differences between ZGC, Shenandoah, and G1GC?
- How would you configure GC logging for production monitoring?

---

## 🧵 **Advanced Concurrency & Threading**

### **Question 3: Lock-Free Programming**
**Scenario**: Implement a high-performance counter that can be safely incremented by multiple threads without using synchronized or locks.

**Expected Answer**:
```java
public class LockFreeCounter {
    private final AtomicLong counter = new AtomicLong(0);
    
    // Basic atomic increment
    public long increment() {
        return counter.incrementAndGet();
    }
    
    // More complex atomic operation
    public long addAndGet(long delta) {
        long current;
        long updated;
        do {
            current = counter.get();
            updated = current + delta;
            // CAS loop - retry if value changed between read and update attempt
        } while (!counter.compareAndSet(current, updated));
        
        return updated;
    }
    
    // Non-blocking read
    public long get() {
        return counter.get(); // Atomic read, no CAS needed
    }
    
    // Advanced: Implement a lock-free bounded queue
    public static class LockFreeBoundedQueue<T> {
        private final AtomicInteger head = new AtomicInteger(0);
        private final AtomicInteger tail = new AtomicInteger(0);
        private final AtomicReferenceArray<T> buffer;
        private final int capacity;
        
        public LockFreeBoundedQueue(int capacity) {
            this.capacity = capacity;
            this.buffer = new AtomicReferenceArray<>(capacity);
        }
        
        public boolean offer(T item) {
            int currentTail = tail.get();
            int nextTail = (currentTail + 1) % capacity;
            
            // Check if queue is full
            if (nextTail == head.get()) {
                return false; // Queue full
            }
            
            // Try to update tail position
            if (tail.compareAndSet(currentTail, nextTail)) {
                buffer.set(currentTail % capacity, item);
                return true;
            }
            
            return false; // Another thread modified tail, retry needed
        }
        
        public T poll() {
            int currentHead = head.get();
            
            // Check if queue is empty
            if (currentHead == tail.get()) {
                return null; // Queue empty
            }
            
            // Try to update head position
            int nextHead = (currentHead + 1) % capacity;
            if (head.compareAndSet(currentHead, nextHead)) {
                T item = buffer.getAndSet(currentHead % capacity, null);
                return item;
            }
            
            return null; // Another thread modified head, retry needed
        }
    }
}

// More complex lock-free data structure: Stack
public class LockFreeStack<T> {
    private final AtomicReference<Node<T>> head = new AtomicReference<>();
    
    private static class Node<T> {
        final T data;
        final Node<T> next;
        
        Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }
    
    public void push(T item) {
        Node<T> newHead = new Node<>(item, null);
        Node<T> currentHead;
        
        do {
            currentHead = head.get();
            newHead.next = currentHead;
        } while (!head.compareAndSet(currentHead, newHead));
    }
    
    public T pop() {
        Node<T> currentHead;
        Node<T> newHead;
        
        do {
            currentHead = head.get();
            if (currentHead == null) {
                return null; // Stack is empty
            }
            newHead = currentHead.next;
        } while (!head.compareAndSet(currentHead, newHead));
        
        return currentHead.data;
    }
    
    // ABA problem demonstration and solution
    public static class ABASafeStack<T> {
        private final AtomicStampedReference<Node<T>> head = 
            new AtomicStampedReference<>(null, 0);
        
        public void push(T item) {
            Node<T> newHead = new Node<>(item, null);
            Node<T> currentHead;
            int[] stamp = new int[1];
            
            do {
                currentHead = head.get(stamp);
                newHead.next = currentHead;
            } while (!head.compareAndSet(currentHead, newHead, stamp[0], stamp[0] + 1));
        }
        
        public T pop() {
            Node<T> currentHead;
            Node<T> newHead;
            int[] stamp = new int[1];
            
            do {
                currentHead = head.get(stamp);
                if (currentHead == null) {
                    return null;
                }
                newHead = currentHead.next;
            } while (!head.compareAndSet(currentHead, newHead, stamp[0], stamp[0] + 1));
            
            return currentHead.data;
        }
    }
}

// Performance comparison framework
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class ConcurrencyBenchmark {
    
    private LockFreeCounter lockFreeCounter;
    private SynchronizedCounter synchronizedCounter;
    private ReentrantLockCounter lockCounter;
    
    @Setup
    public void setup() {
        lockFreeCounter = new LockFreeCounter();
        synchronizedCounter = new SynchronizedCounter();
        lockCounter = new ReentrantLockCounter();
    }
    
    @Benchmark
    @Threads(8)
    public long testLockFree() {
        return lockFreeCounter.increment();
    }
    
    @Benchmark
    @Threads(8)
    public long testSynchronized() {
        return synchronizedCounter.increment();
    }
    
    @Benchmark
    @Threads(8)
    public long testReentrantLock() {
        return lockCounter.increment();
    }
}

// When to use lock-free vs locks
public class ConcurrencyPatternSelection {
    
    /*
     * Use Lock-Free When:
     * - High contention scenarios
     * - Critical performance requirements
     * - Simple operations (increment, CAS)
     * - Need to avoid thread blocking
     * 
     * Use Locks When:
     * - Complex critical sections
     * - Multiple operations need atomicity
     * - Need condition variables (await/signal)
     * - Fairness is important
     * - Read-heavy workloads (use ReadWriteLock)
     */
    
    // Example: Complex operation requiring lock
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String, User> userCache = new HashMap<>();
    
    public User getUserWithCaching(String userId) {
        lock.readLock().lock();
        try {
            User cached = userCache.get(userId);
            if (cached != null && cached.isValid()) {
                return cached;
            }
        } finally {
            lock.readLock().unlock();
        }
        
        // Need write lock for cache update
        lock.writeLock().lock();
        try {
            // Double-check pattern
            User cached = userCache.get(userId);
            if (cached != null && cached.isValid()) {
                return cached;
            }
            
            User user = userService.loadUser(userId);
            userCache.put(userId, user);
            return user;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    // Advanced: StampedLock for optimistic reads
    private final StampedLock stampedLock = new StampedLock();
    private double x, y;
    
    public double distanceFromOrigin() {
        // Optimistic read - no blocking
        long stamp = stampedLock.tryOptimisticRead();
        double currentX = x, currentY = y;
        
        // Validate that no write occurred during read
        if (!stampedLock.validate(stamp)) {
            // Fallback to read lock
            stamp = stampedLock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}
```

**Follow-up Questions**:
- What is the ABA problem in lock-free programming and how do you solve it?
- How does memory ordering affect lock-free algorithms?
- When would you choose StampedLock over ReentrantReadWriteLock?
- What are the performance characteristics of Atomic vs synchronized vs volatile?
- How do you handle the ABA problem in practical lock-free implementations?

---

### **Question 4: Custom Thread Pool Design**
**Scenario**: Design a custom thread pool that supports priority-based task execution and dynamic resizing based on system load.

**Expected Answer**:
```java
public class PriorityAwareThreadPool implements ExecutorService {
    
    public enum Priority {
        LOW(1), NORMAL(5), HIGH(10), CRITICAL(20);
        
        final int value;
        Priority(int value) { this.value = value; }
    }
    
    // Priority-based task wrapper
    private static class PriorityTask implements Comparable<PriorityTask> {
        private final Runnable task;
        private final Priority priority;
        private final long submitTime;
        private final String taskId; // For tracking and debugging
        
        public PriorityTask(Runnable task, Priority priority, String taskId) {
            this.task = task;
            this.priority = priority;
            this.submitTime = System.nanoTime();
            this.taskId = taskId;
        }
        
        @Override
        public int compareTo(PriorityTask other) {
            // Higher priority first, then FIFO for same priority
            int priorityComparison = Integer.compare(other.priority.value, this.priority.value);
            return priorityComparison != 0 ? priorityComparison : 
                   Long.compare(this.submitTime, other.submitTime);
        }
        
        public void run() {
            task.run();
        }
        
        public String getTaskId() { return taskId; }
        public Priority getPriority() { return priority; }
        public long getSubmitTime() { return submitTime; }
    }
    
    private final PriorityBlockingQueue<PriorityTask> taskQueue = new PriorityBlockingQueue<>();
    private final ThreadFactory threadFactory;
    private final AtomicInteger activeThreads = new AtomicInteger(0);
    private final AtomicInteger coreThreads;
    private final AtomicInteger maxThreads;
    private final long keepAliveTimeNanos;
    private final String poolName;
    private final boolean allowCoreThreadTimeOut;
    
    // Dynamic sizing based on system metrics
    private final ScheduledExecutorService loadMonitor = 
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "LoadMonitor-" + poolName);
            t.setDaemon(true);
            return t;
        });
    
    // Statistics and monitoring
    private final AtomicLong completedTasks = new AtomicLong(0);
    private final AtomicLong rejectedTasks = new AtomicLong(0);
    private final AtomicLong totalExecutionTime = new AtomicLong(0);
    
    public PriorityAwareThreadPool(int corePoolSize, int maximumPoolSize, 
                                  long keepAliveTime, TimeUnit unit, String poolName) {
        this.coreThreads = new AtomicInteger(corePoolSize);
        this.maxThreads = new AtomicInteger(maximumPoolSize);
        this.keepAliveTimeNanos = unit.toNanos(keepAliveTime);
        this.poolName = poolName;
        this.threadFactory = new PriorityThreadFactory(poolName);
        this.allowCoreThreadTimeOut = false;
        
        // Start core threads
        for (int i = 0; i < corePoolSize; i++) {
            startNewThread();
        }
        
        // Start load monitoring
        loadMonitor.scheduleAtFixedRate(this::adjustPoolSize, 30, 30, TimeUnit.SECONDS);
    }
    
    // Submit task with priority
    public Future<?> submit(Runnable task, Priority priority, String taskId) {
        if (isShutdown()) {
            throw new RejectedExecutionException("ThreadPool is shut down");
        }
        
        FutureTask<Void> futureTask = new FutureTask<>(task, null);
        PriorityTask priorityTask = new PriorityTask(futureTask, priority, taskId);
        
        if (!taskQueue.offer(priorityTask)) {
            rejectedTasks.incrementAndGet();
            throw new RejectedExecutionException("Task queue is full");
        }
        
        // Check if we need more threads
        if (shouldStartNewThread()) {
            startNewThread();
        }
        
        return futureTask;
    }
    
    public Future<?> submit(Runnable task, Priority priority) {
        return submit(task, priority, "task-" + System.currentTimeMillis());
    }
    
    private boolean shouldStartNewThread() {
        int current = activeThreads.get();
        int queued = taskQueue.size();
        int max = maxThreads.get();
        int core = coreThreads.get();
        
        // Start new thread if:
        // 1. We have queued tasks and available capacity
        // 2. Current threads are likely busy (rough heuristic)
        // 3. We're below maximum thread limit
        return current < max && queued > current && queued > 5 && current < max;
    }
    
    private void startNewThread() {
        Thread worker = threadFactory.newThread(new Worker());
        worker.start();
        activeThreads.incrementAndGet();
    }
    
    private class Worker implements Runnable {
        @Override
        public void run() {
            try {
                while (!isShutdown() && !isTerminated()) {
                    PriorityTask task = taskQueue.poll(keepAliveTimeNanos, TimeUnit.NANOSECONDS);
                    
                    if (task != null) {
                        long startTime = System.nanoTime();
                        try {
                            task.run();
                            completedTasks.incrementAndGet();
                        } catch (Exception e) {
                            // Log but don't let worker thread die
                            logger.error("Task execution failed for task: " + task.getTaskId(), e);
                        } finally {
                            long executionTime = System.nanoTime() - startTime;
                            totalExecutionTime.addAndGet(executionTime);
                        }
                    } else if (shouldTerminateWorker()) {
                        break; // Timeout reached and can terminate
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                activeThreads.decrementAndGet();
            }
        }
        
        private boolean shouldTerminateWorker() {
            int current = activeThreads.get();
            int core = coreThreads.get();
            boolean timedOut = taskQueue.size() == 0; // Simplified check
            
            // Terminate if above core size and timed out, or if shutdown
            return (current > core && timedOut) || isShutdown();
        }
    }
    
    // Dynamic pool sizing based on system load
    private void adjustPoolSize() {
        SystemMetrics metrics = getSystemMetrics();
        
        if (metrics.getCpuUsage() > 80 && metrics.getMemoryUsage() < 70) {
            // High CPU, normal memory - can add more threads
            int current = maxThreads.get();
            int newMax = Math.min(current + 2, Runtime.getRuntime().availableProcessors() * 4);
            maxThreads.set(newMax);
            
        } else if (metrics.getCpuUsage() < 30 || metrics.getMemoryUsage() > 85) {
            // Low CPU or high memory - reduce thread count
            int current = maxThreads.get();
            int newMax = Math.max(current - 1, coreThreads.get());
            maxThreads.set(newMax);
        }
        
        // Adjust core threads based on sustained load
        int avgQueueSize = getAverageQueueSize();
        if (avgQueueSize > 20) {
            coreThreads.set(Math.min(coreThreads.get() + 1, maxThreads.get()));
        } else if (avgQueueSize < 5) {
            coreThreads.set(Math.max(coreThreads.get() - 1, 1));
        }
    }
    
    // Advanced: Task rejection handler with fallback strategies
    public enum RejectionStrategy {
        CALLER_RUNS, // Run in caller's thread
        DISCARD,     // Drop the task silently
        DISCARD_OLDEST, // Drop oldest task in queue
        ABORT        // Throw exception (default)
    }
    
    private volatile RejectionStrategy rejectionStrategy = RejectionStrategy.ABORT;
    
    public void setRejectionStrategy(RejectionStrategy strategy) {
        this.rejectionStrategy = strategy;
    }
    
    // Monitoring and metrics
    public ThreadPoolStats getStats() {
        return new ThreadPoolStats(
            activeThreads.get(),
            coreThreads.get(),
            maxThreads.get(),
            taskQueue.size(),
            completedTasks.get(),
            rejectedTasks.get(),
            getAverageExecutionTime()
        );
    }
    
    private double getAverageExecutionTime() {
        long completed = completedTasks.get();
        long totalTime = totalExecutionTime.get();
        return completed > 0 ? (double) totalTime / completed : 0;
    }
    
    // Graceful shutdown with timeout
    @Override
    public void shutdown() {
        loadMonitor.shutdown();
    }
    
    @Override
    public List<Runnable> shutdownNow() {
        loadMonitor.shutdownNow();
        List<Runnable> pendingTasks = new ArrayList<>();
        taskQueue.drainTo(pendingTasks);
        return pendingTasks;
    }
    
    @Override
    public boolean isShutdown() {
        return loadMonitor.isShutdown();
    }
    
    @Override
    public boolean isTerminated() {
        return loadMonitor.isTerminated();
    }
    
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return loadMonitor.awaitTermination(timeout, unit);
    }
    
    // ... other ExecutorService methods
}

// Usage example
@Service
public class TaskProcessingService {
    
    private final PriorityAwareThreadPool executor = 
        new PriorityAwareThreadPool(4, 16, 60, TimeUnit.SECONDS, "TaskProcessor");
    
    public void processUserRequest(UserRequest request) {
        Priority priority = determinePriority(request);
        String taskId = "request-" + request.getId();
        
        executor.submit(() -> {
            // Process user request
            processRequest(request);
        }, priority, taskId);
    }
    
    private Priority determinePriority(UserRequest request) {
        if (request.isPremiumUser()) {
            return Priority.HIGH;
        } else if (request.isUrgent()) {
            return Priority.NORMAL;
        } else {
            return Priority.LOW;
        }
    }
    
    // Monitoring endpoint
    @ManagedOperation
    public String getPoolStats() {
        ThreadPoolStats stats = executor.getStats();
        return String.format(
            "Active: %d, Core: %d, Max: %d, Queue: %d, Completed: %d, Rejected: %d, AvgTime: %.2f ns",
            stats.getActiveThreads(),
            stats.getCoreThreads(),
            stats.getMaxThreads(),
            stats.getQueueSize(),
            stats.getCompletedTasks(),
            stats.getRejectedTasks(),
            stats.getAverageExecutionTime()
        );
    }
}
```

**Follow-up Questions**:
- How would you handle thread pool exhaustion in a production system?
- What metrics would you monitor for thread pool health?
- How does this compare to ForkJoinPool for different workload types?
- What are the challenges in implementing work-stealing in custom thread pools?
- How would you implement task prioritization with fairness guarantees?

---

## 🔄 **Advanced Collections & Data Structures**

### **Question 5: Custom Concurrent Cache Implementation**
**Scenario**: Implement a thread-safe LRU cache that supports concurrent reads and writes with automatic expiration.

**Expected Answer**:
```java
public class ConcurrentLRUCache<K, V> {
    
    private static class CacheEntry<V> {
        final V value;
        final long accessTime;
        final long expireTime;
        volatile CacheEntry<V> before, after;
        
        CacheEntry(V value, long ttlMillis) {
            this.value = value;
            this.accessTime = System.currentTimeMillis();
            this.expireTime = ttlMillis > 0 ? accessTime + ttlMillis : Long.MAX_VALUE;
        }
        
        boolean isExpired() {
            return expireTime <= System.currentTimeMillis();
        }
    }
    
    private final int maxSize;
    private final ConcurrentHashMap<K, CacheEntry<V>> map;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    // Doubly linked list for LRU ordering (protected by write lock)
    private final CacheEntry<V> head = new CacheEntry<>(null, 0);
    private final CacheEntry<V> tail = new CacheEntry<>(null, 0);
    
    // Statistics
    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();
    private final AtomicLong evictions = new AtomicLong();
    private final AtomicLong expirations = new AtomicLong();
    
    // Background cleanup
    private final ScheduledExecutorService cleanupExecutor;
    
    public ConcurrentLRUCache(int maxSize) {
        this.maxSize = maxSize;
        this.map = new ConcurrentHashMap<>(maxSize);
        
        // Initialize doubly linked list
        head.after = tail;
        tail.before = head;
        
        // Schedule periodic cleanup
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "CacheCleanup");
            t.setDaemon(true);
            return t;
        });
        
        cleanupExecutor.scheduleAtFixedRate(this::cleanupExpired, 60, 60, TimeUnit.SECONDS);
    }
    
    public V get(K key) {
        CacheEntry<V> entry = map.get(key);
        
        if (entry == null || entry.isExpired()) {
            if (entry != null && entry.isExpired()) {
                map.remove(key, entry); // Remove expired entry
                expirations.incrementAndGet();
            }
            misses.incrementAndGet();
            return null;
        }
        
        // Move to front (most recently used)
        moveToFront(entry);
        hits.incrementAndGet();
        return entry.value;
    }
    
    public void put(K key, V value, long ttlMillis) {
        CacheEntry<V> newEntry = new CacheEntry<>(value, ttlMillis);
        CacheEntry<V> existing = map.put(key, newEntry);
        
        lock.writeLock().lock();
        try {
            if (existing != null) {
                // Replace existing entry
                removeFromList(existing);
            }
            
            addToFront(newEntry);
            
            // Check size limit
            while (map.size() > maxSize) {
                evictLeastRecentlyUsed();
            }
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public void put(K key, V value) {
        put(key, value, 0); // No expiration
    }
    
    private void moveToFront(CacheEntry<V> entry) {
        lock.writeLock().lock();
        try {
            removeFromList(entry);
            addToFront(entry);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private void addToFront(CacheEntry<V> entry) {
        entry.after = head.after;
        entry.before = head;
        head.after.before = entry;
        head.after = entry;
    }
    
    private void removeFromList(CacheEntry<V> entry) {
        if (entry.before != null && entry.after != null) {
            entry.before.after = entry.after;
            entry.after.before = entry.before;
        }
    }
    
    private void evictLeastRecentlyUsed() {
        CacheEntry<V> lru = tail.before;
        if (lru != head) {
            // Find the key for this entry (expensive operation)
            K keyToRemove = findKeyForEntry(lru);
            if (keyToRemove != null) {
                map.remove(keyToRemove);
                removeFromList(lru);
                evictions.incrementAndGet();
            }
        }
    }
    
    private K findKeyForEntry(CacheEntry<V> entry) {
        // This is O(n) - in production, consider maintaining reverse mapping
        return map.entrySet().stream()
            .filter(e -> e.getValue() == entry)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);
    }
    
    private void cleanupExpired() {
        List<K> expiredKeys = new ArrayList<>();
        
        // Collect expired keys
        map.entrySet().removeIf(entry -> {
            if (entry.getValue().isExpired()) {
                expiredKeys.add(entry.getKey());
                expirations.incrementAndGet();
                return true;
            }
            return false;
        });
        
        // Remove from linked list
        if (!expiredKeys.isEmpty()) {
            lock.writeLock().lock();
            try {
                expiredKeys.forEach(key -> {
                    CacheEntry<V> entry = map.get(key);
                    if (entry != null && entry.isExpired()) {
                        removeFromList(entry);
                    }
                });
            } finally {
                lock.writeLock().unlock();
            }
        }
    }
    
    public CacheStats getStats() {
        long totalRequests = hits.get() + misses.get();
        double hitRate = totalRequests > 0 ? (double) hits.get() / totalRequests : 0.0;
        
        return new CacheStats(
            map.size(),
            maxSize,
            hits.get(),
            misses.get(),
            evictions.get(),
            expirations.get(),
            hitRate
        );
    }
    
    public void shutdown() {
        cleanupExecutor.shutdown();
    }
    
    // Advanced: Soft references for memory-sensitive caching
    public static class SoftReferenceCache<K, V> {
        private final Map<K, SoftReference<CacheEntry<V>>> map = new ConcurrentHashMap<>();
        private final ReferenceQueue<CacheEntry<V>> refQueue = new ReferenceQueue<>();
        
        public V get(K key) {
            processQueue(); // Clean up garbage collected references
            
            SoftReference<CacheEntry<V>> ref = map.get(key);
            if (ref != null) {
                CacheEntry<V> entry = ref.get();
                if (entry != null && !entry.isExpired()) {
                    return entry.value;
                } else {
                    map.remove(key); // Remove expired or cleared entry
                }
            }
            return null;
        }
        
        public void put(K key, V value, long ttlMillis) {
            processQueue(); // Clean up first
            CacheEntry<V> entry = new CacheEntry<>(value, ttlMillis);
            map.put(key, new SoftReference<>(entry, refQueue));
        }
        
        @SuppressWarnings("unchecked")
        private void processQueue() {
            SoftReference<? extends CacheEntry<V>> ref;
            while ((ref = (SoftReference<? extends CacheEntry<V>>) refQueue.poll()) != null) {
                // Remove cleared references - requires maintaining reverse mapping in practice
            }
        }
    }
}

// Improved version using separate chaining for reverse lookup
public class OptimizedConcurrentLRUCache<K, V> {
    
    private static class Node<K, V> {
        final K key;
        volatile V value;
        final long expireTime;
        volatile Node<K, V> before, after;
        
        Node(K key, V value, long ttlMillis) {
            this.key = key;
            this.value = value;
            this.expireTime = ttlMillis > 0 ? 
                System.currentTimeMillis() + ttlMillis : Long.MAX_VALUE;
        }
        
        boolean isExpired() {
            return expireTime <= System.currentTimeMillis();
        }
    }
    
    private final int maxSize;
    private final ConcurrentHashMap<K, Node<K, V>> map;
    private final StampedLock lock = new StampedLock();
    
    private final Node<K, V> head = new Node<>(null, null, 0);
    private final Node<K, V> tail = new Node<>(null, null, 0);
    
    public OptimizedConcurrentLRUCache(int maxSize) {
        this.maxSize = maxSize;
        this.map = new ConcurrentHashMap<>(maxSize);
        head.after = tail;
        tail.before = head;
    }
    
    public V get(K key) {
        Node<K, V> node = map.get(key);
        
        if (node == null || node.isExpired()) {
            if (node != null) {
                map.remove(key, node);
            }
            return null;
        }
        
        // Use optimistic read lock for better performance
        long stamp = lock.tryOptimisticRead();
        if (lock.validate(stamp)) {
            // Fast path - no writers
            moveToFrontOptimistic(node);
        } else {
            // Fall back to write lock
            moveToFront(node);
        }
        
        return node.value;
    }
    
    private void moveToFrontOptimistic(Node<K, V> node) {
        // Try to acquire write lock with timeout
        long stamp = lock.tryWriteLock(100, TimeUnit.MICROSECONDS);
        if (stamp != 0) {
            try {
                removeFromList(node);
                addToFront(node);
            } finally {
                lock.unlockWrite(stamp);
            }
        }
        // If can't acquire lock quickly, skip LRU update for performance
    }
    
    private void moveToFront(Node<K, V> node) {
        long stamp = lock.writeLock();
        try {
            removeFromList(node);
            addToFront(node);
        } finally {
            lock.unlockWrite(stamp);
        }
    }
    
    private void addToFront(Node<K, V> node) {
        node.after = head.after;
        node.before = head;
        head.after.before = node;
        head.after = node;
    }
    
    private void removeFromList(Node<K, V> node) {
        if (node.before != null && node.after != null) {
            node.before.after = node.after;
            node.after.before = node.before;
        }
    }
    
    public void put(K key, V value, long ttlMillis) {
        Node<K, V> newNode = new Node<>(key, value, ttlMillis);
        Node<K, V> existing = map.put(key, newNode);
        
        long stamp = lock.writeLock();
        try {
            if (existing != null) {
                removeFromList(existing);
            }
            
            addToFront(newNode);
            
            // Evict if necessary
            while (map.size() > maxSize) {
                evictLRU();
            }
        } finally {
            lock.unlockWrite(stamp);
        }
    }
    
    private void evictLRU() {
        Node<K, V> lru = tail.before;
        if (lru != head) {
            map.remove(lru.key);
            removeFromList(lru);
        }
    }
    
    // ... rest of implementation similar but with StampedLock optimizations
}

// Usage example with monitoring
@Component
public class UserSessionCache {
    
    private final ConcurrentLRUCache<String, UserSession> cache = 
        new ConcurrentLRUCache<>(10000);
        
    private final MeterRegistry meterRegistry;
    
    public UserSessionCache(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Register cache metrics
        Gauge.builder("cache.size")
            .description("Current cache size")
            .register(meterRegistry, cache, c -> c.getStats().getCurrentSize());
            
        Gauge.builder("cache.hit.rate")
            .description("Cache hit rate")
            .register(meterRegistry, cache, c -> c.getStats().getHitRate());
            
        Gauge.builder("cache.evictions")
            .description("Cache evictions")
            .register(meterRegistry, cache, c -> c.getStats().getEvictions());
    }
    
    public UserSession getSession(String sessionId) {
        UserSession session = cache.get(sessionId);
        
        if (session == null) {
            session = loadSessionFromDatabase(sessionId);
            if (session != null) {
                cache.put(sessionId, session, TimeUnit.HOURS.toMillis(2));
            }
        }
        
        return session;
    }
    
    // Advanced: Cache warming strategy
    public void warmCache(List<String> sessionIds) {
        for (String sessionId : sessionIds) {
            try {
                UserSession session = loadSessionFromDatabase(sessionId);
                if (session != null) {
                    cache.put(sessionId, session, TimeUnit.HOURS.toMillis(2));
                }
            } catch (Exception e) {
                logger.warn("Failed to warm cache for session: " + sessionId, e);
            }
        }
    }
}
```

**Follow-up Questions**:
- How would you handle cache warming strategies?
- What are the trade-offs between write-through vs write-back caching?
- How would you implement cache coherence in a distributed system?
- What are the differences between LRU, LFU, and FIFO cache eviction policies?
- How do soft references and weak references affect cache behavior in JVM?

---