# Concurrency Problems for Senior Java Developers (10+ Years Experience)

## 🔧 **Advanced Concurrency Challenges**

### **Problem 1: Thread-Safe Producer-Consumer with Dynamic Buffer**
**Difficulty**: ⭐⭐⭐⭐⭐
**Scenario**: Implement a thread-safe producer-consumer system that dynamically adjusts buffer size based on load.

**Requirements**:
- Support multiple producers and consumers
- Dynamic buffer sizing based on throughput
- Backpressure handling without blocking
- Statistics collection and monitoring

**Solution**:
```java
public class DynamicBuffer<T> {
    
    private final AtomicInteger currentSize = new AtomicInteger(0);
    private final AtomicInteger maxSize;
    private final AtomicInteger minSize;
    private final AtomicReference<BufferStrategy> strategy;
    
    private final BlockingQueue<T> primaryBuffer;
    private final BlockingQueue<T> overflowBuffer;
    
    private final AtomicLong producedCount = new AtomicLong(0);
    private final AtomicLong consumedCount = new AtomicLong(0);
    private final AtomicLong droppedCount = new AtomicLong(0);
    
    public enum BufferStrategy {
        FIXED, ADAPTIVE, OVERFLOW
    }
    
    public DynamicBuffer(int initialSize, int minSize, int maxSize, BufferStrategy strategy) {
        this.maxSize = new AtomicInteger(maxSize);
        this.minSize = new AtomicInteger(minSize);
        this.strategy = new AtomicReference<>(strategy);
        
        this.primaryBuffer = new LinkedBlockingQueue<>(initialSize);
        this.overflowBuffer = new LinkedBlockingQueue<>(maxSize);
        
        // Start monitoring thread
        startMonitoring();
    }
    
    public boolean offer(T item) throws InterruptedException {
        producedCount.incrementAndGet();
        
        switch (strategy.get()) {
            case FIXED:
                return offerFixed(item);
            case ADAPTIVE:
                return offerAdaptive(item);
            case OVERFLOW:
                return offerWithOverflow(item);
            default:
                throw new IllegalStateException("Unknown strategy");
        }
    }
    
    private boolean offerFixed(T item) throws InterruptedException {
        return primaryBuffer.offer(item, 100, TimeUnit.MILLISECONDS);
    }
    
    private boolean offerAdaptive(T item) throws InterruptedException {
        if (primaryBuffer.offer(item)) {
            return true;
        }
        
        // Check if we should expand
        if (shouldExpand()) {
            expandBuffer();
            return primaryBuffer.offer(item);
        }
        
        // Drop item if buffer is full
        droppedCount.incrementAndGet();
        return false;
    }
    
    private boolean offerWithOverflow(T item) throws InterruptedException {
        if (primaryBuffer.offer(item)) {
            return true;
        }
        
        // Try overflow buffer
        return overflowBuffer.offer(item, 50, TimeUnit.MILLISECONDS);
    }
    
    public T take() throws InterruptedException {
        T item = primaryBuffer.poll();
        if (item != null) {
            consumedCount.incrementAndGet();
            return item;
        }
        
        // Check overflow buffer
        item = overflowBuffer.poll(100, TimeUnit.MILLISECONDS);
        if (item != null) {
            consumedCount.incrementAndGet();
        }
        
        return item;
    }
    
    private boolean shouldExpand() {
        long produced = producedCount.get();
        long consumed = consumedCount.get();
        long dropped = droppedCount.get();
        
        // Expand if drop rate is high or throughput is increasing
        double dropRate = (double) dropped / Math.max(1, produced);
        boolean highDropRate = dropRate > 0.05; // 5% drop rate threshold
        
        // Check throughput trend
        boolean increasingThroughput = isThroughputIncreasing();
        
        return highDropRate || increasingThroughput;
    }
    
    private void expandBuffer() {
        int currentMax = maxSize.get();
        int newMax = Math.min(currentMax * 2, 10000); // Cap at 10K
        
        if (maxSize.compareAndSet(currentMax, newMax)) {
            // Resize the buffer (simplified - in practice would need more complex logic)
            logger.info("Buffer expanded to {}", newMax);
        }
    }
    
    private void startMonitoring() {
        ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(this::collectMetrics, 30, 30, TimeUnit.SECONDS);
    }
    
    private void collectMetrics() {
        BufferMetrics metrics = new BufferMetrics(
            currentSize.get(),
            maxSize.get(),
            producedCount.get(),
            consumedCount.get(),
            droppedCount.get(),
            primaryBuffer.size(),
            overflowBuffer.size()
        );
        
        // Adjust strategy based on metrics
        adjustStrategy(metrics);
        
        // Log metrics
        logger.info("Buffer metrics: {}", metrics);
    }
    
    private void adjustStrategy(BufferMetrics metrics) {
        double utilization = (double) metrics.getCurrentSize() / metrics.getMaxSize();
        double dropRate = (double) metrics.getDroppedCount() / 
                         Math.max(1, metrics.getProducedCount());
        
        if (utilization > 0.9 && dropRate > 0.05) {
            strategy.set(BufferStrategy.OVERFLOW);
        } else if (utilization < 0.5) {
            strategy.set(BufferStrategy.ADAPTIVE);
        } else {
            strategy.set(BufferStrategy.FIXED);
        }
    }
}

// Producer implementation with adaptive rate control
public class AdaptiveProducer<T> implements Runnable {
    
    private final DynamicBuffer<T> buffer;
    private final Supplier<T> itemSupplier;
    private final AtomicDouble productionRate = new AtomicDouble(1.0);
    private final RateLimiter rateLimiter;
    
    public AdaptiveProducer(DynamicBuffer<T> buffer, Supplier<T> itemSupplier) {
        this.buffer = buffer;
        this.itemSupplier = itemSupplier;
        this.rateLimiter = RateLimiter.create(productionRate.get());
    }
    
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            rateLimiter.acquire();
            
            try {
                T item = itemSupplier.get();
                boolean accepted = buffer.offer(item);
                
                if (!accepted) {
                    // Item was dropped, adjust rate
                    adjustRate(-0.1);
                } else {
                    // Item accepted, potentially increase rate
                    adjustRate(0.01);
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void adjustRate(double adjustment) {
        double currentRate = productionRate.get();
        double newRate = Math.max(0.1, Math.min(10.0, currentRate + adjustment));
        
        if (productionRate.compareAndSet(currentRate, newRate)) {
            rateLimiter.setRate(newRate);
        }
    }
}
```

### **Problem 2: Distributed Lock Manager**
**Expected Answer**:
```java
public class DistributedLockManager {
    
    private final RedisTemplate<String, String> redisTemplate;
    private final String nodeId;
    private final ScheduledExecutorService renewalExecutor;
    
    private final Map<String, LockInfo> activeLocks = new ConcurrentHashMap<>();
    
    private static class LockInfo {
        String lockKey;
        String lockValue;
        long expirationTime;
        ScheduledFuture<?> renewalTask;
        
        LockInfo(String lockKey, String lockValue, long expirationTime) {
            this.lockKey = lockKey;
            this.lockValue = lockValue;
            this.expirationTime = expirationTime;
        }
    }
    
    public DistributedLockManager(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.nodeId = UUID.randomUUID().toString();
        this.renewalExecutor = Executors.newScheduledThreadPool(10);
    }
    
    public boolean acquireLock(String lockName, Duration timeout) {
        String lockKey = "lock:" + lockName;
        String lockValue = nodeId + ":" + System.currentTimeMillis();
        long expirationTime = System.currentTimeMillis() + timeout.toMillis();
        
        try {
            Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, timeout);
                
            if (Boolean.TRUE.equals(acquired)) {
                // Lock acquired, start renewal
                LockInfo lockInfo = new LockInfo(lockKey, lockValue, expirationTime);
                activeLocks.put(lockName, lockInfo);
                
                scheduleRenewal(lockInfo, timeout);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            logger.error("Failed to acquire lock: {}", lockName, e);
            return false;
        }
    }
    
    public void releaseLock(String lockName) {
        LockInfo lockInfo = activeLocks.remove(lockName);
        if (lockInfo == null) {
            return; // Lock not held by this node
        }
        
        // Cancel renewal task
        if (lockInfo.renewalTask != null) {
            lockInfo.renewalTask.cancel(false);
        }
        
        // Release lock in Redis
        String script = 
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "  return redis.call('del', KEYS[1]) " +
            "else " +
            "  return 0 " +
            "end";
            
        redisTemplate.execute(
            new DefaultRedisScript<>(script, Long.class),
            Collections.singletonList(lockInfo.lockKey),
            lockInfo.lockValue
        );
    }
    
    private void scheduleRenewal(LockInfo lockInfo, Duration originalTimeout) {
        long renewalInterval = originalTimeout.toMillis() / 3; // Renew at 1/3 of expiration
        
        ScheduledFuture<?> renewalTask = renewalExecutor.scheduleAtFixedRate(
            () -> renewLock(lockInfo),
            renewalInterval,
            renewalInterval,
            TimeUnit.MILLISECONDS
        );
        
        lockInfo.renewalTask = renewalTask;
    }
    
    private void renewLock(LockInfo lockInfo) {
        try {
            String script = 
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "  return redis.call('pexpire', KEYS[1], ARGV[2]) " +
                "else " +
                "  return 0 " +
                "end";
                
            Long result = redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(lockInfo.lockKey),
                lockInfo.lockValue,
                String.valueOf(30000) // 30 second renewal
            );
            
            if (result == null || result == 0) {
                // Lock was lost, remove from active locks
                activeLocks.values().removeIf(info -> info.lockKey.equals(lockInfo.lockKey));
                logger.warn("Lock renewal failed, lock was lost: {}", lockInfo.lockKey);
            }
            
        } catch (Exception e) {
            logger.error("Failed to renew lock: {}", lockInfo.lockKey, e);
        }
    }
    
    // Deadlock detection and resolution
    public void detectAndResolveDeadlocks() {
        // Implement distributed deadlock detection algorithm
        // This is a simplified version - real implementation would be more complex
        Set<String> lockedResources = getLockedResources();
        Map<String, Set<String>> resourceDependencies = buildDependencyGraph();
        
        Set<String> deadlockedNodes = findDeadlockCycles(resourceDependencies);
        if (!deadlockedNodes.isEmpty()) {
            resolveDeadlock(deadlockedNodes);
        }
    }
    
    private Set<String> getLockedResources() {
        // Query Redis for all lock keys
        Set<String> keys = redisTemplate.keys("lock:*");
        return keys != null ? keys : Collections.emptySet();
    }
    
    private Map<String, Set<String>> buildDependencyGraph() {
        // Build dependency graph from Redis lock information
        // This would require additional metadata storage
        return new HashMap<>();
    }
    
    private Set<String> findDeadlockCycles(Map<String, Set<String>> dependencies) {
        // Implement cycle detection algorithm (e.g., Tarjan's algorithm)
        return new HashSet<>();
    }
    
    private void resolveDeadlock(Set<String> deadlockedNodes) {
        // Resolve deadlock by forcing release of locks from one node
        // In practice, would use more sophisticated selection criteria
        String nodeToKill = deadlockedNodes.iterator().next();
        forceReleaseNodeLocks(nodeToKill);
    }
}
```

### **Problem 3: Concurrent Cache with Versioning**
**Expected Answer**:
```java
public class VersionedConcurrentCache<K, V> {
    
    private static class CacheEntry<V> {
        final V value;
        final long version;
        final long timestamp;
        final AtomicInteger referenceCount = new AtomicInteger(0);
        
        CacheEntry(V value, long version) {
            this.value = value;
            this.version = version;
            this.timestamp = System.currentTimeMillis();
        }
    }
    
    private final ConcurrentHashMap<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<K, Long> versions = new ConcurrentHashMap<>();
    private final int maxSize;
    private final EvictionPolicy evictionPolicy;
    
    public enum EvictionPolicy {
        LRU, LFU, FIFO
    }
    
    public VersionedConcurrentCache(int maxSize, EvictionPolicy evictionPolicy) {
        this.maxSize = maxSize;
        this.evictionPolicy = evictionPolicy;
    }
    
    public Optional<V> get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            return Optional.empty();
        }
        
        // Increment reference count for LFU policy
        entry.referenceCount.incrementAndGet();
        return Optional.of(entry.value);
    }
    
    public Optional<V> getIfNewer(K key, long minVersion) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null || entry.version <= minVersion) {
            return Optional.empty();
        }
        
        entry.referenceCount.incrementAndGet();
        return Optional.of(entry.value);
    }
    
    public void put(K key, V value) {
        long version = versions.compute(key, (k, v) -> v == null ? 1L : v + 1);
        CacheEntry<V> newEntry = new CacheEntry<>(value, version);
        
        CacheEntry<V> oldEntry = cache.put(key, newEntry);
        if (oldEntry != null) {
            // Decrement reference count of old entry
            oldEntry.referenceCount.decrementAndGet();
        }
        
        // Check size and evict if necessary
        if (cache.size() > maxSize) {
            evictEntry();
        }
    }
    
    private void evictEntry() {
        K keyToEvict = switch (evictionPolicy) {
            case LRU -> findLRUEntry();
            case LFU -> findLFUEntry();
            case FIFO -> findFIFOEntry();
        };
        
        if (keyToEvict != null) {
            CacheEntry<V> removed = cache.remove(keyToEvict);
            if (removed != null) {
                removed.referenceCount.decrementAndGet();
            }
        }
    }
    
    private K findLRUEntry() {
        return cache.entrySet().stream()
            .min(Comparator.comparing(e -> e.getValue().timestamp))
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    private K findLFUEntry() {
        return cache.entrySet().stream()
            .min(Comparator.comparing(e -> e.getValue().referenceCount.get()))
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    private K findFIFOEntry() {
        return cache.entrySet().stream()
            .min(Comparator.comparing(e -> e.getValue().version))
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    public long getVersion(K key) {
        return versions.getOrDefault(key, 0L);
    }
    
    public CacheStats getStats() {
        long hits = 0, misses = 0; // Would need to track these in real implementation
        return new CacheStats(cache.size(), maxSize, hits, misses);
    }
}

// Usage with concurrent access patterns
@Service
public class DataService {
    
    private final VersionedConcurrentCache<String, DataObject> cache = 
        new VersionedConcurrentCache<>(1000, VersionedConcurrentCache.EvictionPolicy.LRU);
    
    private final DataSource dataSource;
    private final EventBus eventBus;
    
    public DataObject getData(String id) {
        // Try cache first
        Optional<DataObject> cached = cache.get(id);
        if (cached.isPresent()) {
            return cached.get();
        }
        
        // Load from database
        DataObject data = dataSource.load(id);
        cache.put(id, data);
        
        // Subscribe to updates
        eventBus.subscribe(id, this::handleDataUpdate);
        
        return data;
    }
    
    private void handleDataUpdate(DataUpdateEvent event) {
        // Check if we have a newer version
        long currentVersion = cache.getVersion(event.getId());
        if (event.getVersion() > currentVersion) {
            // Update cache with new version
            DataObject newData = dataSource.load(event.getId());
            cache.put(event.getId(), newData);
        }
    }
    
    @EventListener
    public void handleCacheInvalidation(CacheInvalidationEvent event) {
        // Invalidate specific entries or entire cache
        if (event.isFullInvalidation()) {
            cache.clear();
        } else {
            event.getInvalidatedKeys().forEach(cache::remove);
        }
    }
}
```

## 📊 **Concurrency Best Practices**

### **Thread Safety Guidelines**:
1. **Immutable Objects**: Prefer immutable data structures
2. **Thread-Local Storage**: For per-thread state
3. **Atomic Operations**: For simple shared state
4. **Lock-Free Data Structures**: For high-performance scenarios
5. **Proper Synchronization**: When complex coordination is needed

### **Performance Optimization**:
- **Minimize Lock Scope**: Hold locks for shortest time possible
- **Avoid Nested Locks**: Prevent deadlock scenarios
- **Use Read-Write Locks**: When reads vastly outnumber writes
- **Consider Lock Striping**: For better concurrency
- **Profile Lock Contention**: Identify bottlenecks

### **Error Handling**:
- **Graceful Degradation**: Continue operation when possible
- **Fail-Fast Principles**: Detect and report errors quickly
- **Resource Cleanup**: Ensure proper resource release
- **Retry Mechanisms**: Handle transient failures
- **Circuit Breakers**: Prevent cascading failures