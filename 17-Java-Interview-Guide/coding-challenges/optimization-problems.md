# Performance Optimization Problems (10+ Years Experience)

## ⚡ **System Performance Challenges**

### **Problem 1: High-Throughput Request Processor**
**Difficulty**: ⭐⭐⭐⭐⭐
**Scenario**: Optimize a request processing system handling 100K+ requests/second with strict latency requirements.

**Requirements**:
- Sub-10ms 99th percentile latency
- Zero GC pressure in hot paths
- Efficient memory usage
- Graceful degradation under load

**Solution**:
```java
// Object pooling for zero GC pressure
public class RequestProcessor {
    
    // Pre-allocated object pools
    private final ObjectPool<RequestContext> contextPool = 
        new ObjectPool<>(RequestContext::new, 10000);
        
    private final ObjectPool<Response> responsePool = 
        new ObjectPool<>(Response::new, 10000);
        
    private final RingBuffer<RequestEvent> eventBuffer = 
        new RingBuffer<>(1024 * 1024); // 1M slots
        
    // Off-heap storage for large objects
    private final ChronicleMap<String, SessionData> sessionCache = 
        ChronicleMap.of(String.class, SessionData.class)
            .name("session-cache")
            .entries(10_000_000)
            .create();
            
    private final Disruptor<RequestEvent> disruptor;
    private final ExecutorService executor;
    
    public RequestProcessor() {
        // Configure for maximum throughput
        this.executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2,
            new ThreadFactory() {
                private final AtomicInteger counter = new AtomicInteger();
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "RequestProcessor-" + counter.incrementAndGet());
                    t.setDaemon(true);
                    return t;
                }
            }
        );
        
        // Disruptor for lock-free event processing
        this.disruptor = new Disruptor<>(
            RequestEvent::new,
            1024 * 1024, // Ring buffer size
            executor,
            ProducerType.MULTI,
            new BusySpinWaitStrategy() // Ultra-low latency
        );
        
        // Set up event processors
        disruptor.handleEventsWith(this::processEvent);
        disruptor.start();
    }
    
    public CompletableFuture<Response> processRequest(Request request) {
        // Borrow objects from pools
        RequestContext context = contextPool.borrow();
        Response response = responsePool.borrow();
        
        // Set up completion handling
        CompletableFuture<Response> future = new CompletableFuture<>();
        
        try {
            // Populate context with request data (no allocation)
            context.setRequest(request);
            context.setResponse(response);
            context.setFuture(future);
            
            // Publish to disruptor (non-blocking)
            RingBuffer<RequestEvent> ringBuffer = disruptor.getRingBuffer();
            long sequence = ringBuffer.next();
            try {
                RequestEvent event = ringBuffer.get(sequence);
                event.setContext(context);
            } finally {
                ringBuffer.publish(sequence);
            }
            
        } catch (Exception e) {
            // Return objects to pools on error
            contextPool.release(context);
            responsePool.release(response);
            future.completeExceptionally(e);
        }
        
        return future;
    }
    
    private void processEvent(RequestEvent event, long sequence, boolean endOfBatch) {
        RequestContext context = event.getContext();
        Request request = context.getRequest();
        Response response = context.getResponse();
        CompletableFuture<Response> future = context.getFuture();
        
        try {
            // Hot path optimization - no object allocation
            processRequestInternal(request, response);
            future.complete(response);
            
        } catch (Exception e) {
            future.completeExceptionally(e);
        } finally {
            // Return objects to pools
            context.clear();
            contextPool.release(context);
            responsePool.release(response);
        }
    }
    
    private void processRequestInternal(Request request, Response response) {
        // Zero-allocation processing
        String sessionId = request.getSessionId();
        
        // Off-heap session lookup
        SessionData session = sessionCache.getUsing(sessionId, SessionData::clear);
        if (session == null) {
            session = new SessionData();
            session.initialize(request.getUserId());
            sessionCache.put(sessionId, session);
        }
        
        // Process without allocations
        response.setStatus(200);
        response.setMessage("OK");
        response.setTimestamp(System.currentTimeMillis());
    }
}

// Custom object pool implementation
public class ObjectPool<T> {
    
    private final Queue<T> pool = new ConcurrentLinkedQueue<>();
    private final Supplier<T> factory;
    private final int maxSize;
    private final AtomicInteger created = new AtomicInteger(0);
    
    public ObjectPool(Supplier<T> factory, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        
        // Pre-populate pool
        for (int i = 0; i < maxSize / 2; i++) {
            pool.offer(factory.get());
            created.incrementAndGet();
        }
    }
    
    public T borrow() {
        T object = pool.poll();
        if (object == null && created.get() < maxSize) {
            // Create new object if under limit
            object = factory.get();
            created.incrementAndGet();
        } else if (object == null) {
            // Pool exhausted, create temporary object
            return factory.get();
        }
        return object;
    }
    
    public void release(T object) {
        if (object instanceof PooledObject) {
            ((PooledObject) object).reset();
        }
        if (created.get() <= maxSize) {
            pool.offer(object);
        }
        // If over limit, let GC handle temporary objects
    }
}

// Zero-GC data structures
public class RequestContext implements PooledObject {
    private Request request;
    private Response response;
    private CompletableFuture<Response> future;
    
    public void clear() {
        this.request = null;
        this.response = null;
        this.future = null;
    }
    
    public void reset() {
        clear();
    }
    
    // Getters and setters...
}
```

### **Problem 2: Memory-Efficient Data Processing Pipeline**
**Expected Answer**:
```java
// Memory-efficient stream processing with backpressure
public class EfficientDataProcessor {
    
    // Flyweight pattern for data objects
    private final FlyweightFactory flyweightFactory = new FlyweightFactory();
    
    // Memory-mapped files for large datasets
    private final ChronicleQueue dataQueue = ChronicleQueue.singleBuilder()
        .path("/tmp/data-queue")
        .rollCycle(RollCycles.HOURLY)
        .build();
        
    public void processLargeDataset(String inputFile, String outputFile) {
        try (RandomAccessFile raf = new RandomAccessFile(inputFile, "r");
             FileChannel channel = raf.getChannel()) {
            
            // Memory-map the file for zero-copy access
            MappedByteBuffer buffer = channel.map(
                FileChannel.MapMode.READ_ONLY, 0, channel.size());
                
            // Process in chunks to avoid memory pressure
            int chunkSize = 1024 * 1024; // 1MB chunks
            for (int position = 0; position < buffer.capacity(); position += chunkSize) {
                int limit = Math.min(position + chunkSize, buffer.capacity());
                processChunk(buffer, position, limit);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to process file", e);
        }
    }
    
    private void processChunk(MappedByteBuffer buffer, int start, int end) {
        // Process without creating new strings
        for (int i = start; i < end; i++) {
            // Use flyweight objects for processing
            DataRecord record = flyweightFactory.getRecord(buffer, i);
            if (record.isValid()) {
                processRecord(record);
            }
        }
    }
    
    private void processRecord(DataRecord record) {
        // Process without allocations
        long userId = record.getUserId();
        long timestamp = record.getTimestamp();
        double value = record.getValue();
        
        // Update aggregations in-place
        updateAggregations(userId, timestamp, value);
    }
    
    // Efficient aggregation with primitive collections
    private final LongObjectMap<UserStats> userStats = new LongObjectHashMap<>();
    private final TLongDoubleMap hourlyAverages = new TLongDoubleHashMap();
    
    private void updateAggregations(long userId, long timestamp, double value) {
        // Get or create user stats
        UserStats stats = userStats.get(userId);
        if (stats == null) {
            stats = new UserStats();
            userStats.put(userId, stats);
        }
        
        // Update stats without allocations
        stats.update(value, timestamp);
        
        // Update hourly averages
        long hour = timestamp / 3600000; // Truncate to hour
        double currentAvg = hourlyAverages.get(hour);
        int count = stats.getHourlyCount(hour);
        double newAvg = (currentAvg * (count - 1) + value) / count;
        hourlyAverages.put(hour, newAvg);
    }
}

// Flyweight factory for zero-allocation data access
public class FlyweightFactory {
    
    private final ThreadLocal<DataRecord> recordCache = 
        ThreadLocal.withInitial(DataRecord::new);
        
    public DataRecord getRecord(ByteBuffer buffer, int offset) {
        DataRecord record = recordCache.get();
        record.wrap(buffer, offset);
        return record;
    }
}

// Zero-allocation data record wrapper
public class DataRecord {
    private ByteBuffer buffer;
    private int offset;
    
    public void wrap(ByteBuffer buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
    }
    
    public long getUserId() {
        return buffer.getLong(offset);
    }
    
    public long getTimestamp() {
        return buffer.getLong(offset + 8);
    }
    
    public double getValue() {
        return buffer.getDouble(offset + 16);
    }
    
    public boolean isValid() {
        // Validate without allocations
        return getUserId() != 0 && getTimestamp() > 0;
    }
}
```

### **Problem 3: Database Connection Pool Optimization**
**Expected Answer**:
```java
// High-performance connection pool with adaptive sizing
public class OptimizedConnectionPool implements DataSource {
    
    private final BlockingQueue<PooledConnection> availableConnections;
    private final List<PooledConnection> allConnections;
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    private final AtomicInteger totalConnections = new AtomicInteger(0);
    
    private final String url;
    private final String username;
    private final String password;
    private final PoolConfiguration config;
    
    // Adaptive sizing controller
    private final ScheduledExecutorService sizingController = 
        Executors.newSingleThreadScheduledExecutor();
        
    // Metrics collection
    private final MeterRegistry meterRegistry;
    private final Timer getConnectionTimer;
    private final Gauge poolUtilization;
    
    public OptimizedConnectionPool(String url, String username, String password, 
                                 PoolConfiguration config, MeterRegistry meterRegistry) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.config = config;
        this.meterRegistry = meterRegistry;
        
        this.availableConnections = new LinkedBlockingQueue<>(config.getMaxSize());
        this.allConnections = new CopyOnWriteArrayList<>();
        
        // Initialize metrics
        this.getConnectionTimer = Timer.builder("db.connection.get.time")
            .register(meterRegistry);
            
        this.poolUtilization = Gauge.builder("db.connection.pool.utilization")
            .register(meterRegistry, this, cp -> (double) activeConnections.get() / 
                                               Math.max(1, totalConnections.get()));
        
        // Start adaptive sizing
        sizingController.scheduleAtFixedRate(this::adjustPoolSize, 
                                           30, 30, TimeUnit.SECONDS);
        
        // Pre-populate with minimum connections
        initializePool();
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionTimer.recordCallable(this::getConnectionInternal);
    }
    
    private Connection getConnectionInternal() throws SQLException {
        PooledConnection connection = availableConnections.poll();
        
        if (connection == null) {
            // No available connections, create new one if under max
            if (totalConnections.get() < config.getMaxSize()) {
                connection = createNewConnection();
            } else {
                // Pool exhausted, wait with timeout
                try {
                    connection = availableConnections.poll(
                        config.getConnectionTimeout().toMillis(), TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Interrupted while waiting for connection", e);
                }
            }
        }
        
        if (connection == null) {
            throw new SQLException("Connection pool exhausted");
        }
        
        activeConnections.incrementAndGet();
        return connection.getProxyConnection();
    }
    
    private PooledConnection createNewConnection() throws SQLException {
        if (totalConnections.incrementAndGet() > config.getMaxSize()) {
            totalConnections.decrementAndGet();
            return null;
        }
        
        Connection rawConnection = DriverManager.getConnection(url, username, password);
        PooledConnection pooledConnection = new PooledConnection(rawConnection, this);
        allConnections.add(pooledConnection);
        
        return pooledConnection;
    }
    
    public void returnConnection(PooledConnection connection) {
        if (connection.isValid()) {
            availableConnections.offer(connection);
        } else {
            // Remove invalid connection
            allConnections.remove(connection);
            totalConnections.decrementAndGet();
            
            // Create replacement if needed
            if (totalConnections.get() < config.getMinSize()) {
                try {
                    createNewConnection();
                } catch (SQLException e) {
                    logger.warn("Failed to create replacement connection", e);
                }
            }
        }
        
        activeConnections.decrementAndGet();
    }
    
    private void adjustPoolSize() {
        int currentTotal = totalConnections.get();
        int currentActive = activeConnections.get();
        double utilization = (double) currentActive / Math.max(1, currentTotal);
        
        if (utilization > 0.8 && currentTotal < config.getMaxSize()) {
            // High utilization, expand pool
            int newSize = Math.min(currentTotal + 2, config.getMaxSize());
            expandPool(newSize);
        } else if (utilization < 0.3 && currentTotal > config.getMinSize()) {
            // Low utilization, shrink pool
            int newSize = Math.max(currentTotal - 1, config.getMinSize());
            shrinkPool(newSize);
        }
    }
    
    private void expandPool(int targetSize) {
        int connectionsToCreate = targetSize - totalConnections.get();
        for (int i = 0; i < connectionsToCreate; i++) {
            try {
                createNewConnection();
            } catch (SQLException e) {
                logger.warn("Failed to create connection during pool expansion", e);
                break;
            }
        }
    }
    
    private void shrinkPool(int targetSize) {
        int connectionsToRemove = totalConnections.get() - targetSize;
        for (int i = 0; i < connectionsToRemove; i++) {
            PooledConnection connection = availableConnections.poll();
            if (connection != null) {
                connection.close();
                allConnections.remove(connection);
                totalConnections.decrementAndGet();
            } else {
                break; // No more available connections to remove
            }
        }
    }
    
    // Connection proxy for tracking and validation
    private static class PooledConnection {
        private final Connection rawConnection;
        private final OptimizedConnectionPool pool;
        private final Connection proxyConnection;
        private volatile boolean closed = false;
        
        PooledConnection(Connection rawConnection, OptimizedConnectionPool pool) {
            this.rawConnection = rawConnection;
            this.pool = pool;
            this.proxyConnection = createProxy();
        }
        
        private Connection createProxy() {
            return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        close();
                        return null;
                    } else if ("isClosed".equals(method.getName())) {
                        return closed || rawConnection.isClosed();
                    }
                    return method.invoke(rawConnection, args);
                }
            );
        }
        
        Connection getProxyConnection() {
            return proxyConnection;
        }
        
        boolean isValid() {
            try {
                return !closed && !rawConnection.isClosed() && 
                       rawConnection.isValid(1); // 1 second timeout
            } catch (SQLException e) {
                return false;
            }
        }
        
        void close() {
            if (!closed) {
                closed = true;
                pool.returnConnection(this);
            }
        }
    }
}
```

## 📊 **Optimization Techniques**

### **Memory Optimization Strategies**:
1. **Object Pooling**: Reuse objects to eliminate GC pressure
2. **Flyweight Pattern**: Share common data between objects
3. **Primitive Collections**: Avoid boxing/unboxing overhead
4. **Off-Heap Storage**: Move large objects out of GC scope
5. **Memory-Mapped Files**: Zero-copy file access

### **CPU Optimization Techniques**:
- **Lock-Free Data Structures**: Eliminate synchronization overhead
- **Cache-Friendly Access Patterns**: Improve memory locality
- **Branch Prediction Optimization**: Reduce pipeline stalls
- **Vectorization**: Leverage SIMD instructions
- **JIT Optimization Hints**: Guide compiler optimizations

### **I/O Optimization Methods**:
- **Asynchronous I/O**: Non-blocking operations
- **Batching**: Reduce system call overhead
- **Connection Pooling**: Reuse expensive resources
- **Compression**: Reduce data transfer size
- **Caching**: Avoid redundant operations

### **Performance Monitoring**:
- **Latency Percentiles**: Track tail latencies
- **Throughput Metrics**: Measure request rates
- **Resource Utilization**: Monitor CPU, memory, disk, network
- **GC Statistics**: Track garbage collection impact
- **Application-Level Metrics**: Business KPIs