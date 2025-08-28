# System Design Interview Questions (10+ Years Experience)

## 🏗️ **Microservices Architecture**

### **Question 1: Design a High-Throughput Order Processing System**
**Scenario**: Design a distributed order processing system that handles 100K orders/minute with sub-200ms response times.

**Expected Answer**:
```java
// Event-driven architecture with CQRS
@RestController
public class OrderController {
    
    private final OrderCommandService commandService;
    private final OrderQueryService queryService;
    private final EventPublisher eventPublisher;
    
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        // Command side - optimized for writes
        OrderId orderId = commandService.createOrder(request);
        
        // Publish event for async processing
        eventPublisher.publish(new OrderCreatedEvent(orderId, request));
        
        return ResponseEntity.accepted()
            .body(new OrderResponse(orderId, "PROCESSING"));
    }
    
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDetails> getOrder(@PathVariable String id) {
        // Query side - optimized for reads
        return queryService.findOrder(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}

// Async processing with backpressure handling
@Component
public class OrderProcessor {
    
    private final RateLimiter rateLimiter = RateLimiter.create(1000.0); // 1000 orders/sec
    private final CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("orderProcessing");
    
    @EventListener
    @Async("orderProcessingExecutor")
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Rate limiting
        rateLimiter.acquire();
        
        // Circuit breaker protection
        Supplier<Void> processOrder = () -> {
            processOrderInternal(event);
            return null;
        };
        
        circuitBreaker.executeSupplier(processOrder);
    }
    
    private void processOrderInternal(OrderCreatedEvent event) {
        // Saga pattern for distributed transactions
        OrderSaga saga = new OrderSaga(event.getOrderId());
        sagaManager.execute(saga);
    }
}

// Database partitioning strategy
@Configuration
public class DatabaseShardingConfig {
    
    @Bean
    public ShardingDataSourceFactory dataSourceFactory() {
        return ShardingDataSourceFactory.builder()
            .addDataSource("orders_0", createDataSource("orders_shard_0"))
            .addDataSource("orders_1", createDataSource("orders_shard_1"))
            .addDataSource("orders_2", createDataSource("orders_shard_2"))
            .shardingStrategy(new OrderIdHashShardingStrategy())
            .build();
    }
    
    // Consistent hashing for even distribution
    public class OrderIdHashShardingStrategy implements ShardingStrategy {
        @Override
        public String determineDataSource(String orderId) {
            int hash = orderId.hashCode();
            int shard = Math.abs(hash) % 3;
            return "orders_" + shard;
        }
    }
}
```

### **Question 2: Service Discovery and Load Balancing**
**Expected Answer**:
```java
// Custom service registry
@Component
public class ServiceRegistry {
    
    private final Map<String, List<ServiceInstance>> services = new ConcurrentHashMap<>();
    private final ScheduledExecutorService healthChecker = 
        Executors.newScheduledThreadPool(10);
    
    public void registerService(ServiceInstance instance) {
        services.computeIfAbsent(instance.getServiceName(), k -> new ArrayList<>())
               .add(instance);
        
        // Start health checking
        scheduleHealthCheck(instance);
    }
    
    public Optional<ServiceInstance> findService(String serviceName, LoadBalancingStrategy strategy) {
        List<ServiceInstance> instances = services.get(serviceName);
        if (instances == null || instances.isEmpty()) {
            return Optional.empty();
        }
        
        List<ServiceInstance> healthyInstances = instances.stream()
            .filter(ServiceInstance::isHealthy)
            .collect(Collectors.toList());
            
        return strategy.selectInstance(healthyInstances);
    }
}

// Load balancing strategies
public interface LoadBalancingStrategy {
    Optional<ServiceInstance> selectInstance(List<ServiceInstance> instances);
}

@Component
public class WeightedRoundRobinStrategy implements LoadBalancingStrategy {
    
    private final AtomicInteger counter = new AtomicInteger(0);
    
    @Override
    public Optional<ServiceInstance> selectInstance(List<ServiceInstance> instances) {
        if (instances.isEmpty()) return Optional.empty();
        
        int totalWeight = instances.stream().mapToInt(ServiceInstance::getWeight).sum();
        int targetWeight = counter.getAndIncrement() % totalWeight;
        
        int currentWeight = 0;
        for (ServiceInstance instance : instances) {
            currentWeight += instance.getWeight();
            if (targetWeight < currentWeight) {
                return Optional.of(instance);
            }
        }
        
        return Optional.of(instances.get(0)); // Fallback
    }
}
```

## 🔄 **Caching Strategies**

### **Question 3: Multi-Level Caching Architecture**
**Expected Answer**:
```java
@Component
public class MultiLevelCacheManager {
    
    private final Cache<String, Object> l1Cache; // Local cache (Caffeine)
    private final RedisTemplate<String, Object> l2Cache; // Distributed cache (Redis)
    private final Database database; // Source of truth
    
    public <T> Optional<T> get(String key, Class<T> type) {
        // L1 Cache (fastest)
        T value = (T) l1Cache.getIfPresent(key);
        if (value != null) {
            recordCacheHit("L1", key);
            return Optional.of(value);
        }
        
        // L2 Cache (fast)
        value = (T) l2Cache.opsForValue().get(key);
        if (value != null) {
            l1Cache.put(key, value); // Populate L1
            recordCacheHit("L2", key);
            return Optional.of(value);
        }
        
        // Database (slow)
        Optional<T> dbValue = database.findById(key, type);
        if (dbValue.isPresent()) {
            T dbResult = dbValue.get();
            l1Cache.put(key, dbResult);
            l2Cache.opsForValue().set(key, dbResult, Duration.ofHours(1));
            recordCacheMiss(key);
            return Optional.of(dbResult);
        }
        
        return Optional.empty();
    }
    
    public void invalidate(String key) {
        l1Cache.invalidate(key);
        l2Cache.delete(key);
        
        // Publish cache invalidation event for other nodes
        eventPublisher.publish(new CacheInvalidationEvent(key));
    }
    
    // Cache-aside pattern with write-through
    public <T> void put(String key, T value) {
        // Write to database first
        database.save(key, value);
        
        // Update caches
        l1Cache.put(key, value);
        l2Cache.opsForValue().set(key, value, Duration.ofHours(1));
    }
}

// Cache warming strategy
@Component
public class CacheWarmupService {
    
    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        CompletableFuture.runAsync(this::warmupCriticalData);
    }
    
    private void warmupCriticalData() {
        // Pre-load frequently accessed data
        List<String> criticalKeys = getCriticalDataKeys();
        
        criticalKeys.parallelStream().forEach(key -> {
            try {
                cacheManager.get(key, Object.class);
            } catch (Exception e) {
                logger.warn("Failed to warm up cache for key: " + key, e);
            }
        });
    }
}
```

## 📊 **Monitoring and Observability**

### **Question 4: Distributed Tracing and Metrics**
**Expected Answer**:
```java
// Custom metrics collection
@Component
public class BusinessMetricsCollector {
    
    private final MeterRegistry meterRegistry;
    private final Timer orderProcessingTimer;
    private final Counter orderSuccessCounter;
    private final Counter orderFailureCounter;
    private final Gauge activeOrdersGauge;
    
    public BusinessMetricsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.orderProcessingTimer = Timer.builder("order.processing.time")
            .description("Time taken to process orders")
            .register(meterRegistry);
        
        this.orderSuccessCounter = Counter.builder("order.success")
            .description("Successful order count")
            .register(meterRegistry);
            
        this.activeOrdersGauge = Gauge.builder("order.active.count")
            .description("Currently active orders")
            .register(meterRegistry, this, BusinessMetricsCollector::getActiveOrderCount);
    }
    
    public void recordOrderProcessing(Duration processingTime, boolean success) {
        orderProcessingTimer.record(processingTime);
        
        if (success) {
            orderSuccessCounter.increment();
        } else {
            orderFailureCounter.increment();
        }
    }
}

// Distributed tracing
@Service
public class OrderService {
    
    private final Tracer tracer;
    
    @NewSpan("order-creation")
    public OrderResult createOrder(@SpanTag("user.id") String userId, CreateOrderRequest request) {
        Span span = tracer.nextSpan()
            .name("order-validation")
            .tag("order.type", request.getType())
            .start();
            
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            // Validate order
            ValidationResult validation = validateOrder(request);
            span.tag("validation.result", validation.isValid() ? "success" : "failure");
            
            if (!validation.isValid()) {
                span.tag("error", validation.getErrorMessage());
                return OrderResult.failure(validation.getErrorMessage());
            }
            
            // Process order
            return processOrderWithTracing(request);
            
        } finally {
            span.end();
        }
    }
    
    private OrderResult processOrderWithTracing(CreateOrderRequest request) {
        return tracer.nextSpan()
            .name("order-processing")
            .tag("order.amount", String.valueOf(request.getAmount()))
            .wrap(() -> {
                // Actual order processing logic
                return doProcessOrder(request);
            }).call();
    }
}

// Health checks and circuit breakers
@Component
public class SystemHealthMonitor {
    
    private final Map<String, HealthIndicator> healthIndicators = new HashMap<>();
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    
    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void checkSystemHealth() {
        HealthStatus overallHealth = HealthStatus.UP;
        Map<String, HealthStatus> componentHealth = new HashMap<>();
        
        for (Map.Entry<String, HealthIndicator> entry : healthIndicators.entrySet()) {
            try {
                HealthStatus status = entry.getValue().health();
                componentHealth.put(entry.getKey(), status);
                
                if (status == HealthStatus.DOWN) {
                    overallHealth = HealthStatus.DOWN;
                }
            } catch (Exception e) {
                componentHealth.put(entry.getKey(), HealthStatus.DOWN);
                overallHealth = HealthStatus.DOWN;
            }
        }
        
        // Update circuit breakers based on health
        updateCircuitBreakers(componentHealth);
        
        // Publish health status
        eventPublisher.publish(new HealthStatusEvent(overallHealth, componentHealth));
    }
}
```

## 🏆 **Scalability Patterns**

### **Question 5: Horizontal Scaling Strategies**
**Expected Answer**:
```java
// Stateless service design
@RestController
public class StatelessOrderController {
    
    // Inject dependencies, avoid instance variables for request state
    private final OrderService orderService;
    private final CacheManager cacheManager;
    
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(
        @RequestBody CreateOrderRequest request,
        HttpServletRequest httpRequest) {
        
        // Extract user context from JWT token (stateless)
        UserContext userContext = jwtService.extractUserContext(
            httpRequest.getHeader("Authorization"));
        
        // Process without storing state in controller
        OrderResult result = orderService.createOrder(userContext, request);
        
        return ResponseEntity.ok(new OrderResponse(result));
    }
}

// Database connection pooling and optimization
@Configuration
public class DatabaseOptimizationConfig {
    
    @Bean
    @Primary
    public DataSource primaryDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(databaseUrl);
        
        // Connection pool optimization
        config.setMaximumPoolSize(50); // Based on load testing
        config.setMinimumIdle(10);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(60000);
        
        // Performance tuning
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        return new HikariDataSource(config);
    }
    
    @Bean
    public DataSource readOnlyDataSource() {
        // Read replica for read-heavy operations
        return createReadOnlyDataSource();
    }
}

// Auto-scaling based on metrics
@Component
public class AutoScalingController {
    
    private final KubernetesClient kubernetesClient;
    private final MetricsCollector metricsCollector;
    
    @Scheduled(fixedRate = 60000) // Every minute
    public void evaluateScaling() {
        SystemMetrics metrics = metricsCollector.getCurrentMetrics();
        
        ScalingDecision decision = makeScalingDecision(metrics);
        
        if (decision.shouldScale()) {
            executeScaling(decision);
        }
    }
    
    private ScalingDecision makeScalingDecision(SystemMetrics metrics) {
        // CPU-based scaling
        if (metrics.getCpuUtilization() > 70) {
            return ScalingDecision.scaleUp("High CPU utilization");
        }
        
        // Memory-based scaling
        if (metrics.getMemoryUtilization() > 80) {
            return ScalingDecision.scaleUp("High memory utilization");
        }
        
        // Request queue-based scaling
        if (metrics.getRequestQueueSize() > 100) {
            return ScalingDecision.scaleUp("High request queue");
        }
        
        // Scale down conditions
        if (metrics.getCpuUtilization() < 30 && 
            metrics.getMemoryUtilization() < 50 && 
            metrics.getCurrentInstances() > 2) {
            return ScalingDecision.scaleDown("Low resource utilization");
        }
        
        return ScalingDecision.noAction();
    }
}
```

## 📋 **System Design Checklist**

### **Key Design Principles**
- ✅ Single Responsibility Principle for services
- ✅ Stateless service design for horizontal scaling
- ✅ Idempotent operations for reliability
- ✅ Circuit breaker pattern for fault tolerance
- ✅ Bulkhead pattern for resource isolation

### **Performance Considerations**  
- ✅ Database connection pooling and optimization
- ✅ Read replicas for read-heavy workloads
- ✅ Caching strategies (multi-level)
- ✅ Async processing for non-critical operations
- ✅ Batch processing for bulk operations

### **Monitoring and Alerts**
- ✅ Business metrics and technical metrics
- ✅ Distributed tracing for request flow
- ✅ Health checks and alerting
- ✅ Log aggregation and analysis
- ✅ Performance benchmarking