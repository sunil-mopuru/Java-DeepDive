## 🎯 **Structural Patterns**

### **Question 5: Decorator Pattern with Dynamic Composition**
**Scenario**: Design a dynamic request processing pipeline that can apply different transformations based on runtime configuration.

**Expected Answer**:
```java
// Base component interface
public interface RequestProcessor {
    ProcessedRequest process(IncomingRequest request);
    
    // Advanced: Metrics collection
    default ProcessorMetrics getMetrics() {
        return ProcessorMetrics.EMPTY;
    }
    
    // Advanced: Conditional processing
    default boolean shouldProcess(IncomingRequest request) {
        return true;
    }
}

// Concrete component
public class BasicRequestProcessor implements RequestProcessor {
    private final Counter processedRequests = Counter.builder("requests.processed").register();
    
    @Override
    public ProcessedRequest process(IncomingRequest request) {
        // Basic processing logic
        ProcessedRequest result = new ProcessedRequest(
            request.getId(),
            request.getPayload().toUpperCase(),
            Instant.now()
        );
        
        processedRequests.increment();
        return result;
    }
}

// Advanced decorator with dynamic composition
public abstract class RequestProcessorDecorator implements RequestProcessor {
    protected final RequestProcessor wrappedProcessor;
    
    public RequestProcessorDecorator(RequestProcessor wrappedProcessor) {
        this.wrappedProcessor = wrappedProcessor;
    }
    
    @Override
    public boolean shouldProcess(IncomingRequest request) {
        return wrappedProcessor.shouldProcess(request);
    }
    
    @Override
    public ProcessorMetrics getMetrics() {
        return wrappedProcessor.getMetrics();
    }
}

// Authentication decorator
public class AuthenticationDecorator extends RequestProcessorDecorator {
    private final AuthenticationService authService;
    private final Counter authenticatedRequests = Counter.builder("requests.authenticated").register();
    private final Counter failedAuthentications = Counter.builder("requests.auth.failed").register();
    
    public AuthenticationDecorator(RequestProcessor wrappedProcessor, AuthenticationService authService) {
        super(wrappedProcessor);
        this.authService = authService;
    }
    
    @Override
    public ProcessedRequest process(IncomingRequest request) {
        if (!shouldProcess(request)) {
            throw new SecurityException("Request not authorized");
        }
        
        try {
            AuthenticationToken token = extractToken(request);
            AuthenticatedUser user = authService.authenticate(token);
            
            // Add user context to request
            IncomingRequest authenticatedRequest = request.withUserContext(user);
            authenticatedRequests.increment();
            
            return wrappedProcessor.process(authenticatedRequest);
        } catch (AuthenticationException e) {
            failedAuthentications.increment();
            throw new SecurityException("Authentication failed", e);
        }
    }
    
    @Override
    public boolean shouldProcess(IncomingRequest request) {
        return super.shouldProcess(request) && hasValidToken(request);
    }
    
    private AuthenticationToken extractToken(IncomingRequest request) {
        // Extract token from headers/parameters
        return new AuthenticationToken(request.getHeader("Authorization"));
    }
    
    private boolean hasValidToken(IncomingRequest request) {
        // Basic validation without full authentication
        return request.getHeader("Authorization") != null && 
               !request.getHeader("Authorization").isEmpty();
    }
}

// Rate limiting decorator
public class RateLimitingDecorator extends RequestProcessorDecorator {
    private final RateLimiter rateLimiter;
    private final Counter rateLimitedRequests = Counter.builder("requests.rate_limited").register();
    
    public RateLimitingDecorator(RequestProcessor wrappedProcessor, RateLimiter rateLimiter) {
        super(wrappedProcessor);
        this.rateLimiter = rateLimiter;
    }
    
    @Override
    public ProcessedRequest process(IncomingRequest request) {
        if (!shouldProcess(request)) {
            rateLimitedRequests.increment();
            throw new RateLimitExceededException("Rate limit exceeded for client");
        }
        
        // Apply rate limiting
        if (!rateLimiter.tryAcquire()) {
            rateLimitedRequests.increment();
            throw new RateLimitExceededException("Rate limit exceeded");
        }
        
        return wrappedProcessor.process(request);
    }
    
    @Override
    public boolean shouldProcess(IncomingRequest request) {
        return super.shouldProcess(request) && rateLimiter.availablePermits() > 0;
    }
}

// Caching decorator
public class CachingDecorator extends RequestProcessorDecorator {
    private final Cache<String, ProcessedRequest> cache;
    private final Counter cacheHits = Counter.builder("cache.hits").register();
    private final Counter cacheMisses = Counter.builder("cache.misses").register();
    
    public CachingDecorator(RequestProcessor wrappedProcessor, Cache<String, ProcessedRequest> cache) {
        super(wrappedProcessor);
        this.cache = cache;
    }
    
    @Override
    public ProcessedRequest process(IncomingRequest request) {
        // Try cache first
        String cacheKey = generateCacheKey(request);
        ProcessedRequest cachedResult = cache.getIfPresent(cacheKey);
        
        if (cachedResult != null) {
            cacheHits.increment();
            return cachedResult;
        }
        
        cacheMisses.increment();
        ProcessedRequest result = wrappedProcessor.process(request);
        
        // Cache the result
        cache.put(cacheKey, result);
        return result;
    }
    
    private String generateCacheKey(IncomingRequest request) {
        // Generate cache key based on request characteristics
        return request.getId() + ":" + request.getPayload().hashCode();
    }
}

// Dynamic pipeline builder
@Component
public class DynamicPipelineBuilder {
    private final AuthenticationService authService;
    private final RateLimiter rateLimiter;
    private final Cache<String, ProcessedRequest> cache;
    
    public DynamicPipelineBuilder(AuthenticationService authService, 
                                RateLimiter rateLimiter,
                                Cache<String, ProcessedRequest> cache) {
        this.authService = authService;
        this.rateLimiter = rateLimiter;
        this.cache = cache;
    }
    
    public RequestProcessor buildPipeline(PipelineConfiguration config) {
        RequestProcessor processor = new BasicRequestProcessor();
        
        // Dynamically compose decorators based on configuration
        if (config.isCachingEnabled()) {
            processor = new CachingDecorator(processor, cache);
        }
        
        if (config.isRateLimitingEnabled()) {
            processor = new RateLimitingDecorator(processor, rateLimiter);
        }
        
        if (config.isAuthenticationEnabled()) {
            processor = new AuthenticationDecorator(processor, authService);
        }
        
        return processor;
    }
    
    // Advanced: Runtime pipeline modification
    public RequestProcessor modifyPipeline(RequestProcessor currentProcessor, 
                                        PipelineModification modification) {
        // This is a simplified example - in practice, you'd need to traverse
        // the decorator chain and modify it appropriately
        return currentProcessor;
    }
}

// Configuration-driven pipeline
public class PipelineConfiguration {
    private boolean authenticationEnabled = true;
    private boolean rateLimitingEnabled = true;
    private boolean cachingEnabled = true;
    private int rateLimit = 1000;
    private Duration cacheTtl = Duration.ofMinutes(5);
    
    // Getters and setters
    public boolean isAuthenticationEnabled() { return authenticationEnabled; }
    public void setAuthenticationEnabled(boolean authenticationEnabled) { 
        this.authenticationEnabled = authenticationEnabled; 
    }
    
    public boolean isRateLimitingEnabled() { return rateLimitingEnabled; }
    public void setRateLimitingEnabled(boolean rateLimitingEnabled) { 
        this.rateLimitingEnabled = rateLimitingEnabled; 
    }
    
    public boolean isCachingEnabled() { return cachingEnabled; }
    public void setCachingEnabled(boolean cachingEnabled) { 
        this.cachingEnabled = cachingEnabled; 
    }
    
    public int getRateLimit() { return rateLimit; }
    public void setRateLimit(int rateLimit) { this.rateLimit = rateLimit; }
    
    public Duration getCacheTtl() { return cacheTtl; }
    public void setCacheTtl(Duration cacheTtl) { this.cacheTtl = cacheTtl; }
}

// Advanced: Self-monitoring decorator
public class MonitoringDecorator extends RequestProcessorDecorator {
    private final MeterRegistry meterRegistry;
    private final Timer processingTimer;
    private final Counter errorCounter;
    
    public MonitoringDecorator(RequestProcessor wrappedProcessor, MeterRegistry meterRegistry) {
        super(wrappedProcessor);
        this.meterRegistry = meterRegistry;
        this.processingTimer = Timer.builder("request.processing.time")
            .register(meterRegistry);
        this.errorCounter = Counter.builder("request.processing.errors")
            .register(meterRegistry);
    }
    
    @Override
    public ProcessedRequest process(IncomingRequest request) {
        long startTime = System.nanoTime();
        try {
            ProcessedRequest result = wrappedProcessor.process(request);
            recordSuccess(System.nanoTime() - startTime);
            return result;
        } catch (Exception e) {
            recordError(System.nanoTime() - startTime, e);
            throw e;
        }
    }
    
    private void recordSuccess(long durationNanos) {
        processingTimer.record(durationNanos, TimeUnit.NANOSECONDS);
    }
    
    private void recordError(long durationNanos, Exception e) {
        errorCounter.increment();
        processingTimer.record(durationNanos, TimeUnit.NANOSECONDS);
        
        // Advanced: Error categorization
        Tags errorTags = Tags.of(
            "error_type", e.getClass().getSimpleName(),
            "error_message", e.getMessage() != null ? e.getMessage() : "unknown"
        );
        Counter.builder("request.processing.errors")
            .tags(errorTags)
            .register(meterRegistry)
            .increment();
    }
}
```

**Advanced Follow-up Questions**:
- How do you handle decorator ordering in dynamic pipelines?
- What are the performance implications of deeply nested decorators?
- How do you implement context propagation through decorator chains?
- How do you handle exceptions in decorator chains?
- What are the best practices for testing decorated components?
- How do you implement conditional decoration based on runtime conditions?

---

### **Question 6: Flyweight Pattern with Memory Optimization**
**Scenario**: Optimize a document processing system that handles millions of similar objects to reduce memory consumption.

**Expected Answer**:
```java
// Flyweight interface
public interface DocumentElement {
    void render(RenderingContext context);
    Rectangle getBounds();
    boolean intersects(Rectangle area);
    
    // Advanced: Memory usage tracking
    long getMemoryUsage();
    
    // Advanced: Caching support
    default CacheKey getCacheKey() {
        return new CacheKey(this);
    }
}

// Flyweight factory with advanced features
public class DocumentElementFactory {
    // Intrinsic state shared among flyweights
    private final Map<ElementKey, DocumentElement> flyweights = new ConcurrentHashMap<>();
    
    // Advanced: Memory monitoring
    private final AtomicLong totalMemorySaved = new AtomicLong(0);
    private final AtomicLong flyweightCount = new AtomicLong(0);
    
    // Advanced: Weak references for garbage collection support
    private final Map<ElementKey, WeakReference<DocumentElement>> weakFlyweights = 
        new ConcurrentHashMap<>();
    
    public DocumentElement getElement(ElementType type, ElementProperties properties) {
        ElementKey key = new ElementKey(type, properties);
        
        // Try strong reference cache first
        DocumentElement element = flyweights.get(key);
        if (element != null) {
            return element;
        }
        
        // Try weak reference cache
        WeakReference<DocumentElement> weakRef = weakFlyweights.get(key);
        if (weakRef != null) {
            element = weakRef.get();
            if (element != null) {
                // Move back to strong reference cache
                flyweights.put(key, element);
                weakFlyweights.remove(key);
                return element;
            } else {
                // Clean up stale weak reference
                weakFlyweights.remove(key);
            }
        }
        
        // Create new flyweight
        element = createFlyweight(type, properties);
        flyweights.put(key, element);
        flyweightCount.incrementAndGet();
        
        // Track memory savings (simplified calculation)
        long memorySaved = estimateMemorySavings(type, properties);
        totalMemorySaved.addAndGet(memorySaved);
        
        return element;
    }
    
    private DocumentElement createFlyweight(ElementType type, ElementProperties properties) {
        switch (type) {
            case TEXT:
                return new TextElement(properties.getText(), properties.getFont(), 
                                     properties.getColor());
            case IMAGE:
                return new ImageElement(properties.getImagePath(), properties.getDimensions());
            case SHAPE:
                return new ShapeElement(properties.getShapeType(), properties.getColor(), 
                                      properties.getDimensions());
            default:
                throw new IllegalArgumentException("Unknown element type: " + type);
        }
    }
    
    private long estimateMemorySavings(ElementType type, ElementProperties properties) {
        // Simplified memory savings calculation
        return switch (type) {
            case TEXT -> 1024L; // Estimated savings per text element
            case IMAGE -> 2048L; // Estimated savings per image element
            case SHAPE -> 512L;   // Estimated savings per shape element
            default -> 0L;
        };
    }
    
    // Advanced: Memory pressure handling
    public void handleMemoryPressure() {
        // Move some flyweights to weak references to allow GC
        int elementsToWeaken = flyweights.size() / 4; // Move 25% to weak references
        Iterator<Map.Entry<ElementKey, DocumentElement>> iterator = 
            flyweights.entrySet().iterator();
        
        for (int i = 0; i < elementsToWeaken && iterator.hasNext(); i++) {
            Map.Entry<ElementKey, DocumentElement> entry = iterator.next();
            weakFlyweights.put(entry.getKey(), 
                             new WeakReference<>(entry.getValue()));
            iterator.remove();
        }
    }
    
    // Advanced: Statistics and monitoring
    public FlyweightStats getStats() {
        return new FlyweightStats(
            flyweights.size(),
            weakFlyweights.size(),
            flyweightCount.get(),
            totalMemorySaved.get()
        );
    }
    
    // Advanced: Cleanup unused flyweights
    public void cleanupUnused() {
        // Clean up stale weak references
        weakFlyweights.entrySet().removeIf(entry -> entry.getValue().get() == null);
    }
}

// Immutable intrinsic state key
public final class ElementKey {
    private final ElementType type;
    private final ElementProperties properties;
    private final int hashCode;
    
    public ElementKey(ElementType type, ElementProperties properties) {
        this.type = type;
        this.properties = properties;
        // Pre-compute hash code for performance
        this.hashCode = Objects.hash(type, properties);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ElementKey that = (ElementKey) obj;
        return type == that.type && Objects.equals(properties, that.properties);
    }
    
    @Override
    public int hashCode() {
        return hashCode;
    }
}

// Concrete flyweight implementations
class TextElement implements DocumentElement {
    // Intrinsic state - shared
    private final String text;
    private final Font font;
    private final Color color;
    
    // Cached computed values
    private final Rectangle bounds;
    private final long memoryUsage;
    
    TextElement(String text, Font font, Color color) {
        this.text = text;
        this.font = font;
        this.color = color;
        this.bounds = calculateBounds();
        this.memoryUsage = estimateMemoryUsage();
    }
    
    @Override
    public void render(RenderingContext context) {
        // Render using intrinsic state and extrinsic context
        context.drawText(text, font, color);
    }
    
    @Override
    public Rectangle getBounds() {
        return bounds;
    }
    
    @Override
    public boolean intersects(Rectangle area) {
        return bounds.intersects(area);
    }
    
    @Override
    public long getMemoryUsage() {
        return memoryUsage;
    }
    
    private Rectangle calculateBounds() {
        // Simplified bounds calculation
        int width = text.length() * font.getSize();
        int height = font.getSize();
        return new Rectangle(0, 0, width, height);
    }
    
    private long estimateMemoryUsage() {
        // Simplified memory usage estimation
        return 64L + // Object overhead
               (text != null ? text.length() * 2L : 0L) + // String characters
               32L + // Font reference
               16L;  // Color reference
    }
}

class ImageElement implements DocumentElement {
    // Intrinsic state
    private final String imagePath;
    private final Dimension dimensions;
    private final long memoryUsage;
    
    ImageElement(String imagePath, Dimension dimensions) {
        this.imagePath = imagePath;
        this.dimensions = dimensions;
        this.memoryUsage = estimateMemoryUsage();
    }
    
    @Override
    public void render(RenderingContext context) {
        context.drawImage(imagePath, dimensions);
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle(0, 0, dimensions.width, dimensions.height);
    }
    
    @Override
    public boolean intersects(Rectangle area) {
        return getBounds().intersects(area);
    }
    
    @Override
    public long getMemoryUsage() {
        return memoryUsage;
    }
    
    private long estimateMemoryUsage() {
        return 64L + // Object overhead
               (imagePath != null ? imagePath.length() * 2L : 0L) + // Path string
               32L; // Dimension object
    }
}

// Extrinsic state - passed to flyweight methods
public class RenderingContext {
    private final Graphics2D graphics;
    private final Point position;
    private final double scale;
    private final RenderingOptions options;
    
    public RenderingContext(Graphics2D graphics, Point position, double scale, 
                          RenderingOptions options) {
        this.graphics = graphics;
        this.position = position;
        this.scale = scale;
        this.options = options;
    }
    
    public void drawText(String text, Font font, Color color) {
        // Implementation using extrinsic state
        graphics.setFont(font);
        graphics.setColor(color);
        graphics.drawString(text, position.x, position.y);
    }
    
    public void drawImage(String imagePath, Dimension dimensions) {
        // Implementation using extrinsic state
        // Load and draw image at position with scaling
    }
    
    // Getters for extrinsic state
    public Point getPosition() { return position; }
    public double getScale() { return scale; }
    public RenderingOptions getOptions() { return options; }
}

// Advanced: Flyweight with lazy initialization
public class LazyDocumentElementFactory {
    private final Map<ElementKey, CompletableFuture<DocumentElement>> lazyFlyweights = 
        new ConcurrentHashMap<>();
    
    public CompletableFuture<DocumentElement> getElementAsync(ElementType type, 
                                                            ElementProperties properties) {
        ElementKey key = new ElementKey(type, properties);
        
        return lazyFlyweights.computeIfAbsent(key, k -> 
            CompletableFuture.supplyAsync(() -> createFlyweight(type, properties))
        );
    }
    
    private DocumentElement createFlyweight(ElementType type, ElementProperties properties) {
        // Expensive creation logic here
        return switch (type) {
            case COMPLEX_TEXT -> new ComplexTextElement(properties);
            case VECTOR_GRAPHIC -> new VectorGraphicElement(properties);
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }
}

// Advanced: Flyweight with versioning
public class VersionedDocumentElementFactory {
    private final Map<ElementKey, TreeMap<Version, DocumentElement>> versionedFlyweights = 
        new ConcurrentHashMap<>();
    
    public DocumentElement getElement(ElementType type, ElementProperties properties, 
                                    Version version) {
        ElementKey key = new ElementKey(type, properties);
        
        TreeMap<Version, DocumentElement> versions = versionedFlyweights.computeIfAbsent(
            key, k -> new TreeMap<>()
        );
        
        // Try to find exact version or closest previous version
        Map.Entry<Version, DocumentElement> entry = versions.floorEntry(version);
        if (entry != null) {
            return entry.getValue();
        }
        
        // Create new version
        DocumentElement element = createFlyweight(type, properties);
        versions.put(version, element);
        return element;
    }
    
    // Advanced: Garbage collection of old versions
    public void cleanupOldVersions(Version currentVersion) {
        versionedFlyweights.values().forEach(versions -> {
            // Remove versions older than 5 versions back
            Version cutoff = currentVersion.minus(5);
            versions.headMap(cutoff).clear();
        });
    }
}
```

**Advanced Follow-up Questions**:
- How do you handle thread safety in flyweight factories?
- What are the trade-offs between memory usage and performance in flyweight patterns?
- How do you implement flyweight patterns with mutable extrinsic state?
- How do you handle flyweight creation failures?
- What are the best practices for flyweight key design?
- How do you monitor and optimize flyweight cache performance?

---

## 🛠️ **Creational Patterns**

### **Question 7: Builder Pattern with Validation and Fluent API**
**Scenario**: Design a complex configuration builder for a distributed system with 50+ configuration options and strict validation rules.

**Expected Answer**:
```java
// Advanced builder with validation and fluent API
public class DistributedSystemConfig {
    // Required parameters
    private final String clusterName;
    private final int nodeCount;
    private final NetworkConfig networkConfig;
    
    // Optional parameters with defaults
    private final int maxRetries;
    private final Duration timeout;
    private final RetryPolicy retryPolicy;
    private final LoadBalancingStrategy loadBalancingStrategy;
    private final List<SecurityConfig> securityConfigs;
    private final MonitoringConfig monitoringConfig;
    private final CachingConfig cachingConfig;
    private final List<String> bootstrapServers;
    private final SerializationFormat serializationFormat;
    private final CompressionType compressionType;
    private final int maxConnectionsPerNode;
    private final Duration connectionTimeout;
    private final boolean enableMetrics;
    private final List<String> allowedOrigins;
    private final List<String> blockedIps;
    private final CircuitBreakerConfig circuitBreakerConfig;
    private final RateLimitingConfig rateLimitingConfig;
    
    // Private constructor to enforce builder usage
    private DistributedSystemConfig(Builder builder) {
        this.clusterName = builder.clusterName;
        this.nodeCount = builder.nodeCount;
        this.networkConfig = builder.networkConfig;
        this.maxRetries = builder.maxRetries;
        this.timeout = builder.timeout;
        this.retryPolicy = builder.retryPolicy;
        this.loadBalancingStrategy = builder.loadBalancingStrategy;
        this.securityConfigs = Collections.unmodifiableList(
            new ArrayList<>(builder.securityConfigs));
        this.monitoringConfig = builder.monitoringConfig;
        this.cachingConfig = builder.cachingConfig;
        this.bootstrapServers = Collections.unmodifiableList(
            new ArrayList<>(builder.bootstrapServers));
        this.serializationFormat = builder.serializationFormat;
        this.compressionType = builder.compressionType;
        this.maxConnectionsPerNode = builder.maxConnectionsPerNode;
        this.connectionTimeout = builder.connectionTimeout;
        this.enableMetrics = builder.enableMetrics;
        this.allowedOrigins = Collections.unmodifiableList(
            new ArrayList<>(builder.allowedOrigins));
        this.blockedIps = Collections.unmodifiableList(
            new ArrayList<>(builder.blockedIps));
        this.circuitBreakerConfig = builder.circuitBreakerConfig;
        this.rateLimitingConfig = builder.rateLimitingConfig;
    }
    
    // Getters
    public String getClusterName() { return clusterName; }
    public int getNodeCount() { return nodeCount; }
    public NetworkConfig getNetworkConfig() { return networkConfig; }
    public int getMaxRetries() { return maxRetries; }
    public Duration getTimeout() { return timeout; }
    public RetryPolicy getRetryPolicy() { return retryPolicy; }
    public LoadBalancingStrategy getLoadBalancingStrategy() { return loadBalancingStrategy; }
    public List<SecurityConfig> getSecurityConfigs() { return securityConfigs; }
    public MonitoringConfig getMonitoringConfig() { return monitoringConfig; }
    public CachingConfig getCachingConfig() { return cachingConfig; }
    public List<String> getBootstrapServers() { return bootstrapServers; }
    public SerializationFormat getSerializationFormat() { return serializationFormat; }
    public CompressionType getCompressionType() { return compressionType; }
    public int getMaxConnectionsPerNode() { return maxConnectionsPerNode; }
    public Duration getConnectionTimeout() { return connectionTimeout; }
    public boolean isEnableMetrics() { return enableMetrics; }
    public List<String> getAllowedOrigins() { return allowedOrigins; }
    public List<String> getBlockedIps() { return blockedIps; }
    public CircuitBreakerConfig getCircuitBreakerConfig() { return circuitBreakerConfig; }
    public RateLimitingConfig getRateLimitingConfig() { return rateLimitingConfig; }
    
    // Advanced: Configuration validation
    public void validate() throws ConfigurationException {
        List<String> errors = new ArrayList<>();
        
        // Required field validations
        if (clusterName == null || clusterName.trim().isEmpty()) {
            errors.add("Cluster name is required");
        }
        
        if (nodeCount <= 0) {
            errors.add("Node count must be positive");
        }
        
        if (networkConfig == null) {
            errors.add("Network configuration is required");
        }
        
        // Cross-field validations
        if (timeout != null && connectionTimeout != null && 
            timeout.compareTo(connectionTimeout) < 0) {
            errors.add("Overall timeout cannot be less than connection timeout");
        }
        
        if (maxConnectionsPerNode <= 0) {
            errors.add("Max connections per node must be positive");
        }
        
        // Collection validations
        if (bootstrapServers.isEmpty()) {
            errors.add("At least one bootstrap server is required");
        }
        
        if (securityConfigs.isEmpty()) {
            errors.add("At least one security configuration is required");
        }
        
        // Delegate to nested object validations
        try {
            networkConfig.validate();
        } catch (ValidationException e) {
            errors.add("Network config validation failed: " + e.getMessage());
        }
        
        for (SecurityConfig securityConfig : securityConfigs) {
            try {
                securityConfig.validate();
            } catch (ValidationException e) {
                errors.add("Security config validation failed: " + e.getMessage());
            }
        }
        
        if (!errors.isEmpty()) {
            throw new ConfigurationException("Configuration validation failed: " + 
                                           String.join(", ", errors));
        }
    }
    
    // Advanced: Configuration merging
    public DistributedSystemConfig mergeWith(DistributedSystemConfig other) {
        return new Builder(this)
            .mergeWith(other)
            .build();
    }
    
    // Advanced: Configuration diff
    public ConfigurationDiff diffWith(DistributedSystemConfig other) {
        return ConfigurationDiff.builder()
            .clusterNameChanged(!Objects.equals(this.clusterName, other.clusterName))
            .nodeCountChanged(this.nodeCount != other.nodeCount)
            .networkConfigChanged(!Objects.equals(this.networkConfig, other.networkConfig))
            .maxRetriesChanged(this.maxRetries != other.maxRetries)
            // ... other field comparisons
            .build();
    }
    
    // Builder class with fluent API
    public static class Builder {
        // Required parameters
        private String clusterName;
        private int nodeCount;
        private NetworkConfig networkConfig;
        
        // Optional parameters with defaults
        private int maxRetries = 3;
        private Duration timeout = Duration.ofSeconds(30);
        private RetryPolicy retryPolicy = RetryPolicy.EXPONENTIAL_BACKOFF;
        private LoadBalancingStrategy loadBalancingStrategy = LoadBalancingStrategy.ROUND_ROBIN;
        private List<SecurityConfig> securityConfigs = new ArrayList<>();
        private MonitoringConfig monitoringConfig = MonitoringConfig.DEFAULT;
        private CachingConfig cachingConfig = CachingConfig.DEFAULT;
        private List<String> bootstrapServers = new ArrayList<>();
        private SerializationFormat serializationFormat = SerializationFormat.JSON;
        private CompressionType compressionType = CompressionType.NONE;
        private int maxConnectionsPerNode = 100;
        private Duration connectionTimeout = Duration.ofSeconds(10);
        private boolean enableMetrics = true;
        private List<String> allowedOrigins = new ArrayList<>();
        private List<String> blockedIps = new ArrayList<>();
        private CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.DEFAULT;
        private RateLimitingConfig rateLimitingConfig = RateLimitingConfig.DEFAULT;
        
        // Constructor for building from scratch
        public Builder() {}
        
        // Constructor for building from existing config
        public Builder(DistributedSystemConfig config) {
            this.clusterName = config.clusterName;
            this.nodeCount = config.nodeCount;
            this.networkConfig = config.networkConfig;
            this.maxRetries = config.maxRetries;
            this.timeout = config.timeout;
            this.retryPolicy = config.retryPolicy;
            this.loadBalancingStrategy = config.loadBalancingStrategy;
            this.securityConfigs = new ArrayList<>(config.securityConfigs);
            this.monitoringConfig = config.monitoringConfig;
            this.cachingConfig = config.cachingConfig;
            this.bootstrapServers = new ArrayList<>(config.bootstrapServers);
            this.serializationFormat = config.serializationFormat;
            this.compressionType = config.compressionType;
            this.maxConnectionsPerNode = config.maxConnectionsPerNode;
            this.connectionTimeout = config.connectionTimeout;
            this.enableMetrics = config.enableMetrics;
            this.allowedOrigins = new ArrayList<>(config.allowedOrigins);
            this.blockedIps = new ArrayList<>(config.blockedIps);
            this.circuitBreakerConfig = config.circuitBreakerConfig;
            this.rateLimitingConfig = config.rateLimitingConfig;
        }
        
        // Required parameter setters
        public Builder clusterName(String clusterName) {
            this.clusterName = clusterName;
            return this;
        }
        
        public Builder nodeCount(int nodeCount) {
            this.nodeCount = nodeCount;
            return this;
        }
        
        public Builder networkConfig(NetworkConfig networkConfig) {
            this.networkConfig = networkConfig;
            return this;
        }
        
        // Optional parameter setters with fluent API
        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }
        
        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }
        
        public Builder retryPolicy(RetryPolicy retryPolicy) {
            this.retryPolicy = retryPolicy;
            return this;
        }
        
        public Builder loadBalancingStrategy(LoadBalancingStrategy strategy) {
            this.loadBalancingStrategy = strategy;
            return this;
        }
        
        public Builder addSecurityConfig(SecurityConfig securityConfig) {
            this.securityConfigs.add(securityConfig);
            return this;
        }
        
        public Builder monitoringConfig(MonitoringConfig monitoringConfig) {
            this.monitoringConfig = monitoringConfig;
            return this;
        }
        
        public Builder cachingConfig(CachingConfig cachingConfig) {
            this.cachingConfig = cachingConfig;
            return this;
        }
        
        public Builder addBootstrapServer(String server) {
            this.bootstrapServers.add(server);
            return this;
        }
        
        public Builder serializationFormat(SerializationFormat format) {
            this.serializationFormat = format;
            return this;
        }
        
        public Builder compressionType(CompressionType type) {
            this.compressionType = type;
            return this;
        }
        
        public Builder maxConnectionsPerNode(int maxConnections) {
            this.maxConnectionsPerNode = maxConnections;
            return this;
        }
        
        public Builder connectionTimeout(Duration timeout) {
            this.connectionTimeout = timeout;
            return this;
        }
        
        public Builder enableMetrics(boolean enable) {
            this.enableMetrics = enable;
            return this;
        }
        
        public Builder addAllowedOrigin(String origin) {
            this.allowedOrigins.add(origin);
            return this;
        }
        
        public Builder addBlockedIp(String ip) {
            this.blockedIps.add(ip);
            return this;
        }
        
        public Builder circuitBreakerConfig(CircuitBreakerConfig config) {
            this.circuitBreakerConfig = config;
            return this;
        }
        
        public Builder rateLimitingConfig(RateLimitingConfig config) {
            this.rateLimitingConfig = config;
            return this;
        }
        
        // Advanced: Conditional setters
        public Builder ifCondition(boolean condition, Consumer<Builder> consumer) {
            if (condition) {
                consumer.accept(this);
            }
            return this;
        }
        
        // Advanced: Batch setters
        public Builder withSecurityConfigs(List<SecurityConfig> configs) {
            this.securityConfigs.clear();
            this.securityConfigs.addAll(configs);
            return this;
        }
        
        public Builder withBootstrapServers(List<String> servers) {
            this.bootstrapServers.clear();
            this.bootstrapServers.addAll(servers);
            return this;
        }
        
        public Builder withAllowedOrigins(List<String> origins) {
            this.allowedOrigins.clear();
            this.allowedOrigins.addAll(origins);
            return this;
        }
        
        public Builder withBlockedIps(List<String> ips) {
            this.blockedIps.clear();
            this.blockedIps.addAll(ips);
            return this;
        }
        
        // Advanced: Configuration merging
        public Builder mergeWith(DistributedSystemConfig other) {
            if (other.clusterName != null) this.clusterName = other.clusterName;
            if (other.nodeCount > 0) this.nodeCount = other.nodeCount;
            if (other.networkConfig != null) this.networkConfig = other.networkConfig;
            if (other.maxRetries >= 0) this.maxRetries = other.maxRetries;
            if (other.timeout != null) this.timeout = other.timeout;
            if (other.retryPolicy != null) this.retryPolicy = other.retryPolicy;
            if (other.loadBalancingStrategy != null) this.loadBalancingStrategy = other.loadBalancingStrategy;
            if (other.securityConfigs != null && !other.securityConfigs.isEmpty()) {
                this.securityConfigs.addAll(other.securityConfigs);
            }
            if (other.monitoringConfig != null) this.monitoringConfig = other.monitoringConfig;
            if (other.cachingConfig != null) this.cachingConfig = other.cachingConfig;
            if (other.bootstrapServers != null && !other.bootstrapServers.isEmpty()) {
                this.bootstrapServers.addAll(other.bootstrapServers);
            }
            if (other.serializationFormat != null) this.serializationFormat = other.serializationFormat;
            if (other.compressionType != null) this.compressionType = other.compressionType;
            if (other.maxConnectionsPerNode > 0) this.maxConnectionsPerNode = other.maxConnectionsPerNode;
            if (other.connectionTimeout != null) this.connectionTimeout = other.connectionTimeout;
            this.enableMetrics = other.enableMetrics; // Boolean, always set
            if (other.allowedOrigins != null && !other.allowedOrigins.isEmpty()) {
                this.allowedOrigins.addAll(other.allowedOrigins);
            }
            if (other.blockedIps != null && !other.blockedIps.isEmpty()) {
                this.blockedIps.addAll(other.blockedIps);
            }
            if (other.circuitBreakerConfig != null) this.circuitBreakerConfig = other.circuitBreakerConfig;
            if (other.rateLimitingConfig != null) this.rateLimitingConfig = other.rateLimitingConfig;
            
            return this;
        }
        
        // Advanced: Template-based configuration
        public Builder fromTemplate(ConfigurationTemplate template) {
            return mergeWith(template.getDefaultConfig());
        }
        
        // Advanced: Environment-specific configuration
        public Builder forEnvironment(Environment environment) {
            switch (environment) {
                case DEVELOPMENT:
                    return this.maxRetries(1)
                               .timeout(Duration.ofSeconds(10))
                               .enableMetrics(false);
                case STAGING:
                    return this.maxRetries(3)
                               .timeout(Duration.ofSeconds(30))
                               .enableMetrics(true);
                case PRODUCTION:
                    return this.maxRetries(5)
                               .timeout(Duration.ofSeconds(60))
                               .enableMetrics(true);
                default:
                    return this;
            }
        }
        
        // Build method with validation
        public DistributedSystemConfig build() throws ConfigurationException {
            DistributedSystemConfig config = new DistributedSystemConfig(this);
            config.validate(); // Validate before returning
            return config;
        }
        
        // Advanced: Asynchronous building with validation
        public CompletableFuture<DistributedSystemConfig> buildAsync() {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return build();
                } catch (ConfigurationException e) {
                    throw new CompletionException(e);
                }
            });
        }
    }
}

// Supporting classes and enums
enum Environment {
    DEVELOPMENT, STAGING, PRODUCTION
}

enum RetryPolicy {
    FIXED, EXPONENTIAL_BACKOFF, FIBONACCI_BACKOFF
}

enum LoadBalancingStrategy {
    ROUND_ROBIN, LEAST_CONNECTIONS, WEIGHTED_ROUND_ROBIN
}

enum SerializationFormat {
    JSON, PROTOBUF, AVRO, CUSTOM
}

enum CompressionType {
    NONE, GZIP, SNAPPY, LZ4
}

// Advanced: Configuration templates
public class ConfigurationTemplate {
    private final String name;
    private final DistributedSystemConfig defaultConfig;
    private final Map<String, Object> metadata;
    
    public ConfigurationTemplate(String name, DistributedSystemConfig defaultConfig) {
        this.name = name;
        this.defaultConfig = defaultConfig;
        this.metadata = new HashMap<>();
    }
    
    public DistributedSystemConfig getDefaultConfig() {
        return defaultConfig;
    }
    
    public String getName() {
        return name;
    }
    
    public ConfigurationTemplate withMetadata(String key, Object value) {
        this.metadata.put(key, value);
        return this;
    }
    
    public Object getMetadata(String key) {
        return this.metadata.get(key);
    }
    
    // Predefined templates
    public static final ConfigurationTemplate HIGH_AVAILABILITY = 
        new ConfigurationTemplate("high-availability",
            new DistributedSystemConfig.Builder()
                .maxRetries(5)
                .timeout(Duration.ofSeconds(60))
                .retryPolicy(RetryPolicy.EXPONENTIAL_BACKOFF)
                .loadBalancingStrategy(LoadBalancingStrategy.LEAST_CONNECTIONS)
                .circuitBreakerConfig(CircuitBreakerConfig.STRICT)
                .build()
        ).withMetadata("reliability", "high")
         .withMetadata("performance", "medium");
    
    public static final ConfigurationTemplate HIGH_PERFORMANCE = 
        new ConfigurationTemplate("high-performance",
            new DistributedSystemConfig.Builder()
                .maxRetries(2)
                .timeout(Duration.ofSeconds(15))
                .retryPolicy(RetryPolicy.FIXED)
                .loadBalancingStrategy(LoadBalancingStrategy.ROUND_ROBIN)
                .compressionType(CompressionType.LZ4)
                .maxConnectionsPerNode(200)
                .build()
        ).withMetadata("reliability", "medium")
         .withMetadata("performance", "high");
}

// Advanced: Configuration validation framework
public class ConfigurationValidator {
    private final List<ValidationRule> rules = new ArrayList<>();
    
    public ConfigurationValidator addRule(ValidationRule rule) {
        this.rules.add(rule);
        return this;
    }
    
    public ValidationResult validate(DistributedSystemConfig config) {
        List<ValidationError> errors = new ArrayList<>();
        
        for (ValidationRule rule : rules) {
            try {
                rule.validate(config);
            } catch (ValidationException e) {
                errors.add(new ValidationError(rule.getName(), e.getMessage()));
            }
        }
        
        return new ValidationResult(errors);
    }
    
    // Predefined validation rules
    public static final ValidationRule CLUSTER_NAME_REQUIRED = 
        new ValidationRule("cluster-name-required") {
            @Override
            public void validate(DistributedSystemConfig config) throws ValidationException {
                if (config.getClusterName() == null || config.getClusterName().trim().isEmpty()) {
                    throw new ValidationException("Cluster name is required");
                }
            }
        };
        
    public static final ValidationRule TIMEOUT_CONSISTENCY = 
        new ValidationRule("timeout-consistency") {
            @Override
            public void validate(DistributedSystemConfig config) throws ValidationException {
                if (config.getTimeout() != null && config.getConnectionTimeout() != null &&
                    config.getTimeout().compareTo(config.getConnectionTimeout()) < 0) {
                    throw new ValidationException("Overall timeout cannot be less than connection timeout");
                }
            }
        };
}

// Usage examples
public class ConfigurationExamples {
    
    // Basic usage
    public DistributedSystemConfig createBasicConfig() throws ConfigurationException {
        return new DistributedSystemConfig.Builder()
            .clusterName("my-cluster")
            .nodeCount(5)
            .networkConfig(NetworkConfig.DEFAULT)
            .addBootstrapServer("server1:9092")
            .addBootstrapServer("server2:9092")
            .addSecurityConfig(SecurityConfig.TLS_DEFAULT)
            .build();
    }
    
    // Advanced usage with templates and environment
    public DistributedSystemConfig createProductionConfig() throws ConfigurationException {
        return new DistributedSystemConfig.Builder()
            .fromTemplate(ConfigurationTemplate.HIGH_AVAILABILITY)
            .forEnvironment(Environment.PRODUCTION)
            .clusterName("prod-cluster")
            .nodeCount(10)
            .networkConfig(NetworkConfig.PRODUCTION)
            .withBootstrapServers(Arrays.asList(
                "prod-server1:9092",
                "prod-server2:9092",
                "prod-server3:9092"
            ))
            .addSecurityConfig(SecurityConfig.TLS_STRICT)
            .monitoringConfig(MonitoringConfig.PRODUCTION)
            .build();
    }
    
    // Conditional configuration
    public DistributedSystemConfig createConditionalConfig(boolean enableCaching) 
            throws ConfigurationException {
        return new DistributedSystemConfig.Builder()
            .clusterName("conditional-cluster")
            .nodeCount(3)
            .networkConfig(NetworkConfig.DEFAULT)
            .addBootstrapServer("server:9092")
            .addSecurityConfig(SecurityConfig.TLS_DEFAULT)
            .ifCondition(enableCaching, builder -> 
                builder.cachingConfig(CachingConfig.HIGH_PERFORMANCE))
            .build();
    }
    
    // Configuration merging
    public DistributedSystemConfig createMergedConfig() throws ConfigurationException {
        DistributedSystemConfig baseConfig = createBasicConfig();
        DistributedSystemConfig overrideConfig = new DistributedSystemConfig.Builder()
            .timeout(Duration.ofSeconds(45))
            .maxRetries(5)
            .build();
            
        return baseConfig.mergeWith(overrideConfig);
    }
}
```

**Advanced Follow-up Questions**:
- How do you handle backward compatibility in builder patterns?
- What are the performance implications of complex builder validation?
- How do you implement builder patterns with inheritance hierarchies?
- How do you handle builder state management in multi-threaded environments?
- What are the best practices for builder error handling and reporting?
- How do you implement builder patterns with dependency injection frameworks?

---

## 🔄 **Integration Patterns**

### **Question 8: Adapter Pattern with Protocol Translation**
**Scenario**: Integrate a legacy payment system with a modern microservices architecture requiring multiple protocol translations.

**Expected Answer**:
```java
// Target interface for modern microservices
public interface PaymentService {
    CompletableFuture<PaymentResult> processPayment(PaymentRequest request);
    CompletableFuture<RefundResult> processRefund(RefundRequest request);
    CompletableFuture<PaymentStatus> getPaymentStatus(String paymentId);
    
    // Advanced: Health checking
    CompletableFuture<HealthStatus> checkHealth();
    
    // Advanced: Metrics collection
    PaymentMetrics getMetrics();
}

// Legacy system interface (incompatible with target)
public interface LegacyPaymentSystem {
    LegacyPaymentResponse processTransaction(LegacyPaymentRequest request);
    LegacyRefundResponse processRefund(LegacyRefundRequest request);
    LegacyStatusResponse checkTransactionStatus(String transactionId);
    
    // Different error handling
    boolean isSystemAvailable();
    String getLastErrorMessage();
}

// Adapter implementation with protocol translation
public class LegacyPaymentAdapter implements PaymentService {
    private final LegacyPaymentSystem legacySystem;
    private final MeterRegistry meterRegistry;
    private final Counter paymentCounter;
    private final Counter refundCounter;
    private final Timer paymentTimer;
    private final Timer refundTimer;
    
    public LegacyPaymentAdapter(LegacyPaymentSystem legacySystem, MeterRegistry meterRegistry) {
        this.legacySystem = legacySystem;
        this.meterRegistry = meterRegistry;
        this.paymentCounter = Counter.builder("payments.processed").register(meterRegistry);
        this.refundCounter = Counter.builder("refunds.processed").register(meterRegistry);
        this.paymentTimer = Timer.builder("payment.processing.time").register(meterRegistry);
        this.refundTimer = Timer.builder("refund.processing.time").register(meterRegistry);
    }
    
    @Override
    public CompletableFuture<PaymentResult> processPayment(PaymentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Timer.Sample sample = Timer.start(meterRegistry);
            try {
                // Translate modern request to legacy format
                LegacyPaymentRequest legacyRequest = translatePaymentRequest(request);
                
                // Process with legacy system
                LegacyPaymentResponse legacyResponse = legacySystem.processTransaction(legacyRequest);
                
                // Translate legacy response to modern format
                PaymentResult result = translatePaymentResponse(legacyResponse);
                
                // Record metrics
                paymentCounter.increment();
                sample.stop(paymentTimer);
                
                return result;
            } catch (Exception e) {
                // Handle and translate legacy exceptions
                throw translateLegacyException(e);
            }
        });
    }
    
    @Override
    public CompletableFuture<RefundResult> processRefund(RefundRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Timer.Sample sample = Timer.start(meterRegistry);
            try {
                // Translate modern request to legacy format
                LegacyRefundRequest legacyRequest = translateRefundRequest(request);
                
                // Process with legacy system
                LegacyRefundResponse legacyResponse = legacySystem.processRefund(legacyRequest);
                
                // Translate legacy response to modern format
                RefundResult result = translateRefundResponse(legacyResponse);
                
                // Record metrics
                refundCounter.increment();
                sample.stop(refundTimer);
                
                return result;
            } catch (Exception e) {
                // Handle and translate legacy exceptions
                throw translateLegacyException(e);
            }
        });
    }
    
    @Override
    public CompletableFuture<PaymentStatus> getPaymentStatus(String paymentId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Translate modern ID to legacy format
                String legacyId = translatePaymentId(paymentId);
                
                // Query legacy system
                LegacyStatusResponse legacyResponse = legacySystem.checkTransactionStatus(legacyId);
                
                // Translate legacy response to modern format
                return translateStatusResponse(legacyResponse);
            } catch (Exception e) {
                // Handle and translate legacy exceptions
                throw translateLegacyException(e);
            }
        });
    }
    
    // Protocol translation methods
    private LegacyPaymentRequest translatePaymentRequest(PaymentRequest request) {
        return new LegacyPaymentRequest.Builder()
            .withAmount(request.getAmount().getValue())
            .withCurrency(request.getAmount().getCurrency().getCurrencyCode())
            .withCardNumber(request.getPaymentMethod().getCardNumber())
            .withExpiryDate(request.getPaymentMethod().getExpiryDate())
            .withCvv(request.getPaymentMethod().getCvv())
            .withCustomerId(request.getCustomerId())
            .withOrderId(request.getOrderId())
            .withDescription(request.getDescription())
            .build();
    }
    
    private PaymentResult translatePaymentResponse(LegacyPaymentResponse response) {
        return PaymentResult.builder()
            .withPaymentId(response.getTransactionId())
            .withStatus(translateLegacyStatus(response.getStatus()))
            .withAmount(new Money(response.getAmount(), Currency.getInstance(response.getCurrency())))
            .withTimestamp(response.getTimestamp())
            .withReferenceNumber(response.getReferenceNumber())
            .withAuthorizationCode(response.getAuthorizationCode())
            .build();
    }
    
    private LegacyRefundRequest translateRefundRequest(RefundRequest request) {
        return new LegacyRefundRequest.Builder()
            .withTransactionId(request.getPaymentId())
            .withAmount(request.getAmount().getValue())
            .withCurrency(request.getAmount().getCurrency().getCurrencyCode())
            .withReason(request.getReason())
            .build();
    }
    
    private RefundResult translateRefundResponse(LegacyRefundResponse response) {
        return RefundResult.builder()
            .withRefundId(response.getRefundId())
            .withPaymentId(response.getOriginalTransactionId())
            .withStatus(translateLegacyStatus(response.getStatus()))
            .withAmount(new Money(response.getAmount(), Currency.getInstance(response.getCurrency())))
            .withTimestamp(response.getTimestamp())
            .withReferenceNumber(response.getReferenceNumber())
            .build();
    }
    
    private String translatePaymentId(String paymentId) {
        // Handle ID format translation if needed
        return paymentId;
    }
    
    private PaymentStatus translateLegacyStatus(LegacyPaymentStatus status) {
        return switch (status) {
            case APPROVED -> PaymentStatus.SUCCESS;
            case DECLINED -> PaymentStatus.FAILED;
            case PENDING -> PaymentStatus.PENDING;
            case CANCELLED -> PaymentStatus.CANCELLED;
            case REFUNDED -> PaymentStatus.REFUNDED;
            default -> PaymentStatus.UNKNOWN;
        };
    }
    
    private PaymentStatus translateStatusResponse(LegacyStatusResponse response) {
        return PaymentStatus.builder()
            .withStatus(translateLegacyStatus(response.getStatus()))
            .withTimestamp(response.getTimestamp())
            .withDetails(response.getDetails())
            .build();
    }
    
    private RuntimeException translateLegacyException(Exception e) {
        // Translate legacy exceptions to modern exceptions
        if (e instanceof LegacyPaymentException) {
            LegacyPaymentException legacyEx = (LegacyPaymentException) e;
            return switch (legacyEx.getErrorCode()) {
                case INVALID_CARD -> new InvalidPaymentMethodException(legacyEx.getMessage());
                case INSUFFICIENT_FUNDS -> new InsufficientFundsException(legacyEx.getMessage());
                case SYSTEM_UNAVAILABLE -> new PaymentServiceUnavailableException(legacyEx.getMessage());
                case DUPLICATE_TRANSACTION -> new DuplicateTransactionException(legacyEx.getMessage());
                default -> new PaymentProcessingException(legacyEx.getMessage());
            };
        }
        return new PaymentProcessingException("Unexpected error: " + e.getMessage(), e);
    }
    
    @Override
    public CompletableFuture<HealthStatus> checkHealth() {
        return CompletableFuture.supplyAsync(() -> {
            boolean available = legacySystem.isSystemAvailable();
            String errorMessage = available ? null : legacySystem.getLastErrorMessage();
            
            return HealthStatus.builder()
                .withStatus(available ? HealthStatus.Status.UP : HealthStatus.Status.DOWN)
                .withDetails(Map.of(
                    "legacy_system_available", available,
                    "last_error", errorMessage != null ? errorMessage : "none"
                ))
                .build();
        });
    }
    
    @Override
    public PaymentMetrics getMetrics() {
        // Collect and translate legacy system metrics
        return PaymentMetrics.builder()
            .withPaymentsProcessed((long) paymentCounter.count())
            .withRefundsProcessed((long) refundCounter.count())
            .withAveragePaymentTime(paymentTimer.mean(TimeUnit.MILLISECONDS))
            .withAverageRefundTime(refundTimer.mean(TimeUnit.MILLISECONDS))
            .build();
    }
}

// Advanced: Multi-adapter with failover capability
public class FailoverPaymentAdapter implements PaymentService {
    private final List<PaymentService> adapters;
    private final PaymentService primaryAdapter;
    private final List<PaymentService> fallbackAdapters;
    
    public FailoverPaymentAdapter(List<PaymentService> adapters) {
        this.adapters = new ArrayList<>(adapters);
        this.primaryAdapter = adapters.get(0);
        this.fallbackAdapters = adapters.subList(1, adapters.size());
    }
    
    @Override
    public CompletableFuture<PaymentResult> processPayment(PaymentRequest request) {
        // Try primary adapter first
        CompletableFuture<PaymentResult> future = primaryAdapter.processPayment(request);
        
        // Add fallback logic
        for (PaymentService fallback : fallbackAdapters) {
            future = future.exceptionally(throwable -> {
                // Log fallback attempt
                logger.warn("Primary payment adapter failed, trying fallback", throwable);
                try {
                    return fallback.processPayment(request).join();
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            });
        }
        
        return future;
    }
    
    // Similar implementation for other methods...
    @Override
    public CompletableFuture<RefundResult> processRefund(RefundRequest request) {
        // Implementation similar to processPayment
        return CompletableFuture.completedFuture(null); // Placeholder
    }
    
    @Override
    public CompletableFuture<PaymentStatus> getPaymentStatus(String paymentId) {
        // Implementation similar to processPayment
        return CompletableFuture.completedFuture(null); // Placeholder
    }
    
    @Override
    public CompletableFuture<HealthStatus> checkHealth() {
        // Check health of all adapters
        List<CompletableFuture<HealthStatus>> healthFutures = adapters.stream()
            .map(PaymentService::checkHealth)
            .collect(Collectors.toList());
            
        return CompletableFuture.allOf(healthFutures.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                List<HealthStatus> statuses = healthFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
                    
                boolean allUp = statuses.stream()
                    .allMatch(status -> status.getStatus() == HealthStatus.Status.UP);
                    
                return HealthStatus.builder()
                    .withStatus(allUp ? HealthStatus.Status.UP : HealthStatus.Status.DOWN)
                    .withDetails(Map.of(
                        "adapter_count", adapters.size(),
                        "healthy_adapters", statuses.stream()
                            .filter(s -> s.getStatus() == HealthStatus.Status.UP)
                            .count()
                    ))
                    .build();
            });
    }
    
    @Override
    public PaymentMetrics getMetrics() {
        // Aggregate metrics from all adapters
        return adapters.stream()
            .map(PaymentService::getMetrics)
            .reduce(PaymentMetrics.EMPTY, PaymentMetrics::merge);
    }
}

// Advanced: Protocol-aware adapter factory
@Component
public class PaymentAdapterFactory {
    private final Map<PaymentProvider, PaymentService> adapterMap = new ConcurrentHashMap<>();
    private final MeterRegistry meterRegistry;
    
    public PaymentAdapterFactory(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    public PaymentService getAdapter(PaymentProvider provider) {
        return adapterMap.computeIfAbsent(provider, this::createAdapter);
    }
    
    private PaymentService createAdapter(PaymentProvider provider) {
        return switch (provider) {
            case LEGACY_V1 -> new LegacyPaymentAdapter(
                new LegacyPaymentSystemV1Impl(), meterRegistry);
            case LEGACY_V2 -> new LegacyPaymentAdapter(
                new LegacyPaymentSystemV2Impl(), meterRegistry);
            case MODERN_API -> new ModernPaymentAdapter(
                new ModernPaymentApiClient(), meterRegistry);
            default -> throw new IllegalArgumentException("Unknown provider: " + provider);
        };
    }
    
    // Advanced: Dynamic adapter registration
    public void registerAdapter(PaymentProvider provider, PaymentService adapter) {
        adapterMap.put(provider, adapter);
    }
    
    // Advanced: Adapter configuration
    public PaymentService getAdapter(PaymentProvider provider, AdapterConfig config) {
        PaymentService adapter = getAdapter(provider);
        
        // Apply configuration (e.g., timeouts, retries)
        if (config.getTimeout() != null) {
            // Apply timeout configuration
        }
        
        if (config.getMaxRetries() > 0) {
            // Wrap with retry logic
            adapter = new RetryablePaymentAdapter(adapter, config.getMaxRetries());
        }
        
        return adapter;
    }
}

// Advanced: Retryable adapter decorator
public class RetryablePaymentAdapter implements PaymentService {
    private final PaymentService wrappedAdapter;
    private final int maxRetries;
    private final Duration retryDelay;
    
    public RetryablePaymentAdapter(PaymentService wrappedAdapter, int maxRetries) {
        this(wrappedAdapter, maxRetries, Duration.ofMillis(100));
    }
    
    public RetryablePaymentAdapter(PaymentService wrappedAdapter, int maxRetries, Duration retryDelay) {
        this.wrappedAdapter = wrappedAdapter;
        this.maxRetries = maxRetries;
        this.retryDelay = retryDelay;
    }
    
    @Override
    public CompletableFuture<PaymentResult> processPayment(PaymentRequest request) {
        return retryWithExponentialBackoff(
            () -> wrappedAdapter.processPayment(request),
            maxRetries,
            retryDelay
        );
    }
    
    private <T> CompletableFuture<T> retryWithExponentialBackoff(
            Supplier<CompletableFuture<T>> operation,
            int maxRetries,
            Duration initialDelay) {
        
        CompletableFuture<T> future = operation.get();
        
        for (int i = 0; i < maxRetries; i++) {
            final int retryNumber = i + 1;
            final Duration delay = initialDelay.multipliedBy((long) Math.pow(2, i));
            
            future = future.exceptionally(throwable -> {
                if (retryNumber >= maxRetries) {
                    throw new CompletionException(throwable);
                }
                
                // Schedule retry after delay
                return CompletableFuture.delayedExecutor(delay)
                    .execute(() -> operation.get().join());
            });
        }
        
        return future;
    }
    
    // Implement other methods similarly...
    @Override
    public CompletableFuture<RefundResult> processRefund(RefundRequest request) {
        return CompletableFuture.completedFuture(null); // Placeholder
    }
    
    @Override
    public CompletableFuture<PaymentStatus> getPaymentStatus(String paymentId) {
        return CompletableFuture.completedFuture(null); // Placeholder
    }
    
    @Override
    public CompletableFuture<HealthStatus> checkHealth() {
        return wrappedAdapter.checkHealth();
    }
    
    @Override
    public PaymentMetrics getMetrics() {
        return wrappedAdapter.getMetrics();
    }
}

// Advanced: Circuit breaker adapter
public class CircuitBreakerPaymentAdapter implements PaymentService {
    private final PaymentService wrappedAdapter;
    private final CircuitBreaker circuitBreaker;
    
    public CircuitBreakerPaymentAdapter(PaymentService wrappedAdapter) {
        this.wrappedAdapter = wrappedAdapter;
        this.circuitBreaker = CircuitBreaker.ofDefaults("payment-service");
    }
    
    @Override
    public CompletableFuture<PaymentResult> processPayment(PaymentRequest request) {
        Supplier<CompletionStage<PaymentResult>> supplier = 
            () -> wrappedAdapter.processPayment(request);
            
        return circuitBreaker.executeCompletionStage(supplier)
            .toCompletableFuture();
    }
    
    // Implement other methods similarly...
    @Override
    public CompletableFuture<RefundResult> processRefund(RefundRequest request) {
        return CompletableFuture.completedFuture(null); // Placeholder
    }
    
    @Override
    public CompletableFuture<PaymentStatus> getPaymentStatus(String paymentId) {
        return CompletableFuture.completedFuture(null); // Placeholder
    }
    
    @Override
    public CompletableFuture<HealthStatus> checkHealth() {
        return wrappedAdapter.checkHealth();
    }
    
    @Override
    public PaymentMetrics getMetrics() {
        PaymentMetrics baseMetrics = wrappedAdapter.getMetrics();
        CircuitBreaker.Metrics cbMetrics = circuitBreaker.getMetrics();
        
        return baseMetrics.toBuilder()
            .withCircuitBreakerState(circuitBreaker.getState().toString())
            .withCircuitBreakerFailureRate(cbMetrics.getFailureRate())
            .withCircuitBreakerSlowCallRate(cbMetrics.getSlowCallRate())
            .build();
    }
}
```

**Advanced Follow-up Questions**:
- How do you handle versioning in adapter patterns?
- What are the performance implications of protocol translation?
- How do you implement adapter patterns with streaming data?
- How do you handle transaction boundaries across adapters?
- What are the best practices for testing adapter implementations?
- How do you monitor and debug adapter chain performance?

---

## 🧪 **Testing Patterns**

### **Question 9: Test Double Patterns (Mocks, Stubs, Fakes)**
**Scenario**: Design a comprehensive testing framework for a complex distributed system with multiple external dependencies.

**Expected Answer**:
```java
// Base interface for the system under test
public interface OrderProcessingService {
    CompletableFuture<OrderResult> processOrder(Order order);
    CompletableFuture<InventoryStatus> checkInventory(String productId, int quantity);
    CompletableFuture<PaymentResult> processPayment(PaymentDetails paymentDetails);
    CompletableFuture<ShippingResult> arrangeShipping(ShippingDetails shippingDetails);
    
    // Advanced: Health and metrics
    ServiceHealth getHealth();
    ServiceMetrics getMetrics();
}

// Test double types implementation

// 1. Stub - Predefined responses for specific scenarios
public class OrderProcessingServiceStub implements OrderProcessingService {
    private final Map<StubScenario, StubResponse> responses = new EnumMap<>(StubScenario.class);
    private final ServiceHealth health;
    private final ServiceMetrics metrics;
    
    public enum StubScenario {
        VALID_ORDER, INSUFFICIENT_INVENTORY, PAYMENT_DECLINED, 
        SHIPPING_UNAVAILABLE, SYSTEM_ERROR
    }
    
    public static class StubResponse {
        private final OrderResult orderResult;
        private final InventoryStatus inventoryStatus;
        private final PaymentResult paymentResult;
        private final ShippingResult shippingResult;
        
        public StubResponse(OrderResult orderResult, InventoryStatus inventoryStatus,
                          PaymentResult paymentResult, ShippingResult shippingResult) {
            this.orderResult = orderResult;
            this.inventoryStatus = inventoryStatus;
            this.paymentResult = paymentResult;
            this.shippingResult = shippingResult;
        }
        
        // Getters
        public OrderResult getOrderResult() { return orderResult; }
        public InventoryStatus getInventoryStatus() { return inventoryStatus; }
        public PaymentResult getPaymentResult() { return paymentResult; }
        public ShippingResult getShippingResult() { return shippingResult; }
    }
    
    private OrderProcessingServiceStub(Builder builder) {
        this.responses.putAll(builder.responses);
        this.health = builder.health;
        this.metrics = builder.metrics;
    }
    
    @Override
    public CompletableFuture<OrderResult> processOrder(Order order) {
        StubResponse response = getResponseForOrder(order);
        if (response.getOrderResult() == null) {
            return CompletableFuture.failedFuture(
                new OrderProcessingException("No stub response configured"));
        }
        return CompletableFuture.completedFuture(response.getOrderResult());
    }
    
    @Override
    public CompletableFuture<InventoryStatus> checkInventory(String productId, int quantity) {
        StubResponse response = responses.get(StubScenario.VALID_ORDER);
        if (response != null && response.getInventoryStatus() != null) {
            return CompletableFuture.completedFuture(response.getInventoryStatus());
        }
        return CompletableFuture.completedFuture(
            new InventoryStatus(productId, quantity, true));
    }
    
    @Override
    public CompletableFuture<PaymentResult> processPayment(PaymentDetails paymentDetails) {
        StubResponse response = getResponseForPayment(paymentDetails);
        if (response.getPaymentResult() == null) {
            return CompletableFuture.failedFuture(
                new PaymentProcessingException("No stub response configured"));
        }
        return CompletableFuture.completedFuture(response.getPaymentResult());
    }
    
    @Override
    public CompletableFuture<ShippingResult> arrangeShipping(ShippingDetails shippingDetails) {
        StubResponse response = responses.get(StubScenario.VALID_ORDER);
        if (response != null && response.getShippingResult() != null) {
            return CompletableFuture.completedFuture(response.getShippingResult());
        }
        return CompletableFuture.completedFuture(
            new ShippingResult(shippingDetails.getOrderId(), "SHIPPED", "Tracking123"));
    }
    
    private StubResponse getResponseForOrder(Order order) {
        if (order.getTotalAmount().getValue() > 10000) {
            return responses.get(StubScenario.PAYMENT_DECLINED);
        }
        if (order.getItems().stream().anyMatch(item -> item.getQuantity() > 100)) {
            return responses.get(StubScenario.INSUFFICIENT_INVENTORY);
        }
        return responses.get(StubScenario.VALID_ORDER);
    }
    
    private StubResponse getResponseForPayment(PaymentDetails paymentDetails) {
        if ("4000000000000002".equals(paymentDetails.getCardNumber())) {
            return responses.get(StubScenario.PAYMENT_DECLINED);
        }
        if ("4000000000000119".equals(paymentDetails.getCardNumber())) {
            return responses.get(StubScenario.SYSTEM_ERROR);
        }
        return responses.get(StubScenario.VALID_ORDER);
    }
    
    @Override
    public ServiceHealth getHealth() {
        return health;
    }
    
    @Override
    public ServiceMetrics getMetrics() {
        return metrics;
    }
    
    // Builder for fluent API
    public static class Builder {
        private final Map<StubScenario, StubResponse> responses = new EnumMap<>(StubScenario.class);
        private ServiceHealth health = ServiceHealth.HEALTHY;
        private ServiceMetrics metrics = ServiceMetrics.EMPTY;
        
        public Builder withResponse(StubScenario scenario, StubResponse response) {
            this.responses.put(scenario, response);
            return this;
        }
        
        public Builder withValidOrderResponse(OrderResult result, 
                                            InventoryStatus inventory,
                                            PaymentResult payment,
                                            ShippingResult shipping) {
            this.responses.put(StubScenario.VALID_ORDER, 
                new StubResponse(result, inventory, payment, shipping));
            return this;
        }
        
        public Builder withInsufficientInventoryResponse(InventoryStatus status) {
            this.responses.put(StubScenario.INSUFFICIENT_INVENTORY,
                new StubResponse(null, status, null, null));
            return this;
        }
        
        public Builder withPaymentDeclinedResponse(PaymentResult result) {
            this.responses.put(StubScenario.PAYMENT_DECLINED,
                new StubResponse(null, null, result, null));
            return this;
        }
        
        public Builder withHealth(ServiceHealth health) {
            this.health = health;
            return this;
        }
        
        public Builder withMetrics(ServiceMetrics metrics) {
            this.metrics = metrics;
            return this;
        }
        
        public OrderProcessingServiceStub build() {
            return new OrderProcessingServiceStub(this);
        }
    }
}

// 2. Mock - Verification of interactions
public class OrderProcessingServiceMock implements OrderProcessingService {
    private final List<Invocation> invocations = new CopyOnWriteArrayList<>();
    private final Map<Method, Object> stubbedResponses = new ConcurrentHashMap<>();
    private final AtomicInteger invocationCount = new AtomicInteger(0);
    
    // Record invocations for verification
    public static class Invocation {
        private final String methodName;
        private final Object[] arguments;
        private final long timestamp;
        private final Thread thread;
        
        public Invocation(String methodName, Object[] arguments) {
            this.methodName = methodName;
            this.arguments = arguments != null ? arguments.clone() : null;
            this.timestamp = System.currentTimeMillis();
            this.thread = Thread.currentThread();
        }
        
        // Getters
        public String getMethodName() { return methodName; }
        public Object[] getArguments() { return arguments; }
        public long getTimestamp() { return timestamp; }
        public Thread getThread() { return thread; }
    }
    
    @Override
    public CompletableFuture<OrderResult> processOrder(Order order) {
        recordInvocation("processOrder", order);
        
        Object response = stubbedResponses.get("processOrder");
        if (response instanceof OrderResult) {
            return CompletableFuture.completedFuture((OrderResult) response);
        } else if (response instanceof Exception) {
            return CompletableFuture.failedFuture((Exception) response);
        }
        
        // Default response
        return CompletableFuture.completedFuture(
            new OrderResult(order.getId(), OrderStatus.CONFIRMED, "Order processed"));
    }
    
    @Override
    public CompletableFuture<InventoryStatus> checkInventory(String productId, int quantity) {
        recordInvocation("checkInventory", productId, quantity);
        
        Object response = stubbedResponses.get("checkInventory");
        if (response instanceof InventoryStatus) {
            return CompletableFuture.completedFuture((InventoryStatus) response);
        } else if (response instanceof Exception) {
            return CompletableFuture.failedFuture((Exception) response);
        }
        
        // Default response
        return CompletableFuture.completedFuture(
            new InventoryStatus(productId, quantity, true));
    }
    
    @Override
    public CompletableFuture<PaymentResult> processPayment(PaymentDetails paymentDetails) {
        recordInvocation("processPayment", paymentDetails);
        
        Object response = stubbedResponses.get("processPayment");
        if (response instanceof PaymentResult) {
            return CompletableFuture.completedFuture((PaymentResult) response);
        } else if (response instanceof Exception) {
            return CompletableFuture.failedFuture((Exception) response);
        }
        
        // Default response
        return CompletableFuture.completedFuture(
            new PaymentResult("payment123", PaymentStatus.SUCCESS, "Payment approved"));
    }
    
    @Override
    public CompletableFuture<ShippingResult> arrangeShipping(ShippingDetails shippingDetails) {
        recordInvocation("arrangeShipping", shippingDetails);
        
        Object response = stubbedResponses.get("arrangeShipping");
        if (response instanceof ShippingResult) {
            return CompletableFuture.completedFuture((ShippingResult) response);
        } else if (response instanceof Exception) {
            return CompletableFuture.failedFuture((Exception) response);
        }
        
        // Default response
        return CompletableFuture.completedFuture(
            new ShippingResult(shippingDetails.getOrderId(), "SHIPPED", "Tracking123"));
    }
    
    private void recordInvocation(String methodName, Object... args) {
        invocations.add(new Invocation(methodName, args));
        invocationCount.incrementAndGet();
    }
    
    // Mock verification methods
    public MockVerifier verify() {
        return new MockVerifier(this);
    }
    
    public static class MockVerifier {
        private final OrderProcessingServiceMock mock;
        private final List<Invocation> invocations;
        
        public MockVerifier(OrderProcessingServiceMock mock) {
            this.mock = mock;
            this.invocations = new ArrayList<>(mock.invocations);
        }
        
        public MockVerifier calledOnce() {
            if (invocations.size() != 1) {
                throw new AssertionError("Expected exactly 1 invocation, but was " + 
                                       invocations.size());
            }
            return this;
        }
        
        public MockVerifier calledTimes(int times) {
            if (invocations.size() != times) {
                throw new AssertionError("Expected exactly " + times + " invocations, but was " + 
                                       invocations.size());
            }
            return this;
        }
        
        public MockVerifier neverCalled() {
            if (!invocations.isEmpty()) {
                throw new AssertionError("Expected no invocations, but was " + 
                                       invocations.size());
            }
            return this;
        }
        
        public MockVerifier withArguments(Object... expectedArgs) {
            if (invocations.isEmpty()) {
                throw new AssertionError("No invocations to verify arguments against");
            }
            
            Invocation lastInvocation = invocations.get(invocations.size() - 1);
            Object[] actualArgs = lastInvocation.getArguments();
            
            if (actualArgs == null && expectedArgs == null) {
                return this;
            }
            
            if (actualArgs == null || expectedArgs == null || 
                actualArgs.length != expectedArgs.length) {
                throw new AssertionError("Argument count mismatch");
            }
            
            for (int i = 0; i < expectedArgs.length; i++) {
                if (!Objects.equals(expectedArgs[i], actualArgs[i])) {
                    throw new AssertionError("Argument mismatch at position " + i + 
                                           ": expected " + expectedArgs[i] + 
                                           ", but was " + actualArgs[i]);
                }
            }
            
            return this;
        }
        
        public MockVerifier methodCalled(String methodName) {
            boolean found = invocations.stream()
                .anyMatch(invocation -> methodName.equals(invocation.getMethodName()));
            
            if (!found) {
                throw new AssertionError("Method " + methodName + " was not called. " +
                                       "Actual invocations: " + 
                                       invocations.stream()
                                           .map(Invocation::getMethodName)
                                           .collect(Collectors.joining(", ")));
            }
            
            return this;
        }
    }
    
    // Stubbing methods
    public OrderProcessingServiceMock whenProcessOrderReturn(OrderResult result) {
        stubbedResponses.put("processOrder", result);
        return this;
    }
    
    public OrderProcessingServiceMock whenProcessOrderThrow(Exception exception) {
        stubbedResponses.put("processOrder", exception);
        return this;
    }
    
    public OrderProcessingServiceMock whenCheckInventoryReturn(InventoryStatus status) {
        stubbedResponses.put("checkInventory", status);
        return this;
    }
    
    // Getters for inspection
    public List<Invocation> getInvocations() {
        return new ArrayList<>(invocations);
    }
    
    public int getInvocationCount() {
        return invocationCount.get();
    }
    
    public List<Invocation> getInvocationsForMethod(String methodName) {
        return invocations.stream()
            .filter(invocation -> methodName.equals(invocation.getMethodName()))
            .collect(Collectors.toList());
    }
    
    @Override
    public ServiceHealth getHealth() {
        recordInvocation("getHealth");
        return ServiceHealth.HEALTHY;
    }
    
    @Override
    public ServiceMetrics getMetrics() {
        recordInvocation("getMetrics");
        return ServiceMetrics.EMPTY;
    }
    
    // Reset method for test isolation
    public void reset() {
        invocations.clear();
        stubbedResponses.clear();
        invocationCount.set(0);
    }
}

// 3. Fake - Simplified but functional implementation
public class OrderProcessingServiceFake implements OrderProcessingService {
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private final Map<String, InventoryItem> inventory = new ConcurrentHashMap<>();
    private final Set<String> processedPayments = ConcurrentHashMap.newKeySet();
    private final Map<String, ShippingRecord> shippingRecords = new ConcurrentHashMap<>();
    
    // In-memory data structures for fake implementation
    private static class InventoryItem {
        private final String productId;
        private final int availableQuantity;
        private final BigDecimal price;
        
        public InventoryItem(String productId, int availableQuantity, BigDecimal price) {
            this.productId = productId;
            this.availableQuantity = availableQuantity;
            this.price = price;
        }
        
        // Getters
        public String getProductId() { return productId; }
        public int getAvailableQuantity() { return availableQuantity; }
        public BigDecimal getPrice() { return price; }
    }
    
    private static class ShippingRecord {
        private final String orderId;
        private final String status;
        private final String trackingNumber;
        private final Instant shippedAt;
        
        public ShippingRecord(String orderId, String status, String trackingNumber, 
                            Instant shippedAt) {
            this.orderId = orderId;
            this.status = status;
            this.trackingNumber = trackingNumber;
            this.shippedAt = shippedAt;
        }
        
        // Getters
        public String getOrderId() { return orderId; }
        public String getStatus() { return status; }
        public String getTrackingNumber() { return trackingNumber; }
        public Instant getShippedAt() { return shippedAt; }
    }
    
    // Pre-populate with test data
    public OrderProcessingServiceFake() {
        // Add some inventory items
        inventory.put("PRODUCT_1", new InventoryItem("PRODUCT_1", 100, new BigDecimal("29.99")));
        inventory.put("PRODUCT_2", new InventoryItem("PRODUCT_2", 50, new BigDecimal("59.99")));
        inventory.put("PRODUCT_3", new InventoryItem("PRODUCT_3", 0, new BigDecimal("99.99")));
    }
    
    @Override
    public CompletableFuture<OrderResult> processOrder(Order order) {
        return CompletableFuture.supplyAsync(() -> {
            // Validate order
            if (order.getItems().isEmpty()) {
                throw new OrderProcessingException("Order must have at least one item");
            }
            
            // Check inventory for all items
            for (OrderItem item : order.getItems()) {
                InventoryStatus status = checkInventorySync(item.getProductId(), item.getQuantity());
                if (!status.isAvailable()) {
                    throw new InsufficientInventoryException(
                        "Insufficient inventory for product: " + item.getProductId());
                }
            }
            
            // Process payment
            PaymentResult paymentResult = processPaymentSync(order.getPaymentDetails());
            if (paymentResult.getStatus() != PaymentStatus.SUCCESS) {
                throw new PaymentProcessingException("Payment failed: " + paymentResult.getMessage());
            }
            
            // Arrange shipping
            ShippingResult shippingResult = arrangeShippingSync(order.getShippingDetails());
            
            // Save order
            Order processedOrder = new Order.Builder(order)
                .withStatus(OrderStatus.CONFIRMED)
                .withProcessedAt(Instant.now())
                .build();
            orders.put(order.getId(), processedOrder);
            
            return new OrderResult(order.getId(), OrderStatus.CONFIRMED, 
                                 "Order processed successfully");
        });
    }
    
    @Override
    public CompletableFuture<InventoryStatus> checkInventory(String productId, int quantity) {
        return CompletableFuture.supplyAsync(() -> checkInventorySync(productId, quantity));
    }
    
    private InventoryStatus checkInventorySync(String productId, int quantity) {
        InventoryItem item = inventory.get(productId);
        if (item == null) {
            return new InventoryStatus(productId, 0, false);
        }
        return new InventoryStatus(productId, item.getAvailableQuantity(), 
                                 item.getAvailableQuantity() >= quantity);
    }
    
    @Override
    public CompletableFuture<PaymentResult> processPayment(PaymentDetails paymentDetails) {
        return CompletableFuture.supplyAsync(() -> processPaymentSync(paymentDetails));
    }
    
    private PaymentResult processPaymentSync(PaymentDetails paymentDetails) {
        // Simple validation
        if (paymentDetails.getAmount().getValue().compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult("payment_" + System.currentTimeMillis(), 
                                   PaymentStatus.FAILED, "Invalid amount");
        }
        
        // Simulate payment processing
        String paymentId = "payment_" + System.currentTimeMillis();
        processedPayments.add(paymentId);
        
        // Simulate occasional failures for testing
        if ("4000000000000002".equals(paymentDetails.getCardNumber())) {
            return new PaymentResult(paymentId, PaymentStatus.FAILED, "Card declined");
        }
        
        return new PaymentResult(paymentId, PaymentStatus.SUCCESS, "Payment approved");
    }
    
    @Override
    public CompletableFuture<ShippingResult> arrangeShipping(ShippingDetails shippingDetails) {
        return CompletableFuture.supplyAsync(() -> arrangeShippingSync(shippingDetails));
    }
    
    private ShippingResult arrangeShippingSync(ShippingDetails shippingDetails) {
        String trackingNumber = "TRK" + System.currentTimeMillis();
        ShippingRecord record = new ShippingRecord(
            shippingDetails.getOrderId(), 
            "SHIPPED", 
            trackingNumber, 
            Instant.now()
        );
        shippingRecords.put(shippingDetails.getOrderId(), record);
        
        return new ShippingResult(shippingDetails.getOrderId(), "SHIPPED", trackingNumber);
    }
    
    // Advanced: Data inspection methods for testing
    public boolean orderExists(String orderId) {
        return orders.containsKey(orderId);
    }
    
    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }
    
    public int getInventoryLevel(String productId) {
        InventoryItem item = inventory.get(productId);
        return item != null ? item.getAvailableQuantity() : 0;
    }
    
    public boolean wasPaymentProcessed(String paymentId) {
        return processedPayments.contains(paymentId);
    }
    
    public ShippingRecord getShippingRecord(String orderId) {
        return shippingRecords.get(orderId);
    }
    
    // Advanced: Data manipulation for test setup
    public void addInventory(String productId, int quantity, BigDecimal price) {
        inventory.put(productId, new InventoryItem(productId, quantity, price));
    }
    
    public void setInventoryLevel(String productId, int quantity) {
        InventoryItem item = inventory.get(productId);
        if (item != null) {
            inventory.put(productId, new InventoryItem(productId, quantity, item.getPrice()));
        }
    }
    
    public void clearAllData() {
        orders.clear();
        inventory.clear();
        processedPayments.clear();
        shippingRecords.clear();
    }
    
    @Override
    public ServiceHealth getHealth() {
        return ServiceHealth.HEALTHY;
    }
    
    @Override
    public ServiceMetrics getMetrics() {
        return new ServiceMetrics.Builder()
            .withOrdersProcessed((long) orders.size())
            .withPaymentsProcessed((long) processedPayments.size())
            .withShippingArrangements((long) shippingRecords.size())
            .build();
    }
}

// Advanced: Test double factory with configuration
@Component
public class TestDoubleFactory {
    
    public enum TestDoubleType {
        STUB, MOCK, FAKE, SPY
    }
    
    public OrderProcessingService createTestDouble(TestDoubleType type) {
        return switch (type) {
            case STUB -> new OrderProcessingServiceStub.Builder().build();
            case MOCK -> new OrderProcessingServiceMock();
            case FAKE -> new OrderProcessingServiceFake();
            case SPY -> throw new UnsupportedOperationException("Spy not implemented");
        };
    }
    
    public OrderProcessingServiceStub createConfiguredStub() {
        return new OrderProcessingServiceStub.Builder()
            .withValidOrderResponse(
                new OrderResult("order123", OrderStatus.CONFIRMED, "Order confirmed"),
                new InventoryStatus("PRODUCT_1", 10, true),
                new PaymentResult("payment123", PaymentStatus.SUCCESS, "Payment approved"),
                new ShippingResult("order123", "SHIPPED", "TRK123456")
            )
            .withInsufficientInventoryResponse(
                new InventoryStatus("PRODUCT_1", 0, false)
            )
            .withPaymentDeclinedResponse(
                new PaymentResult("payment456", PaymentStatus.FAILED, "Card declined")
            )
            .build();
    }
    
    public OrderProcessingServiceMock createMockWithExpectations() {
        return new OrderProcessingServiceMock()
            .whenProcessOrderReturn(new OrderResult("order123", OrderStatus.CONFIRMED, 
                                                   "Order confirmed"))
            .whenCheckInventoryReturn(new InventoryStatus("PRODUCT_1", 10, true));
    }
    
    public OrderProcessingServiceFake createFakeWithData() {
        OrderProcessingServiceFake fake = new OrderProcessingServiceFake();
        fake.addInventory("TEST_PRODUCT", 25, new BigDecimal("19.99"));
        return fake;
    }
}

// Advanced: Test scenario builder
public class TestScenarioBuilder {
    private final OrderProcessingService service;
    
    public TestScenarioBuilder(OrderProcessingService service) {
        this.service = service;
    }
    
    // Complex test scenario: Complete order processing flow
    public CompletableFuture<OrderProcessingResult> executeCompleteOrderFlow(Order order) {
        return service.checkInventory(order.getItems().get(0).getProductId(), 
                                    order.getItems().get(0).getQuantity())
            .thenCompose(inventoryStatus -> {
                if (!inventoryStatus.isAvailable()) {
                    return CompletableFuture.failedFuture(
                        new InsufficientInventoryException("Product not available"));
                }
                return service.processPayment(order.getPaymentDetails());
            })
            .thenCompose(paymentResult -> {
                if (paymentResult.getStatus() != PaymentStatus.SUCCESS) {
                    return CompletableFuture.failedFuture(
                        new PaymentProcessingException("Payment failed"));
                }
                return service.processOrder(order);
            })
            .thenCompose(orderResult -> {
                if (orderResult.getStatus() != OrderStatus.CONFIRMED) {
                    return CompletableFuture.failedFuture(
                        new OrderProcessingException("Order processing failed"));
                }
                return service.arrangeShipping(order.getShippingDetails());
            })
            .thenApply(shippingResult -> 
                new OrderProcessingResult(order.getId(), OrderStatus.SHIPPED, 
                                        shippingResult.getTrackingNumber()));
    }
    
    // Error scenario: Payment failure handling
    public CompletableFuture<OrderProcessingResult> executePaymentFailureScenario(Order order) {
        return service.processPayment(order.getPaymentDetails())
            .handle((paymentResult, throwable) -> {
                if (throwable != null || 
                    paymentResult.getStatus() != PaymentStatus.SUCCESS) {
                    return new OrderProcessingResult(order.getId(), OrderStatus.PAYMENT_FAILED, 
                                                   "Payment processing failed");
                }
                return new OrderProcessingResult(order.getId(), OrderStatus.CONFIRMED, 
                                               "Order confirmed");
            });
    }
}

// Usage examples in tests
public class OrderProcessingServiceTest {
    
    private TestDoubleFactory testDoubleFactory;
    
    @BeforeEach
    void setUp() {
        testDoubleFactory = new TestDoubleFactory();
    }
    
    @Test
    void testSuccessfulOrderProcessingWithStub() {
        // Given
        OrderProcessingService service = testDoubleFactory.createConfiguredStub();
        Order order = createTestOrder();
        
        // When
        CompletableFuture<OrderResult> result = service.processOrder(order);
        
        // Then
        assertThat(result).isCompletedWithValueMatching(
            orderResult -> orderResult.getStatus() == OrderStatus.CONFIRMED);
    }
    
    @Test
    void testInsufficientInventoryWithMock() {
        // Given
        OrderProcessingServiceMock mock = 
            (OrderProcessingServiceMock) testDoubleFactory.createTestDouble(
                TestDoubleFactory.TestDoubleType.MOCK);
        mock.whenCheckInventoryReturn(new InventoryStatus("PRODUCT_1", 0, false));
        
        // When
        CompletableFuture<InventoryStatus> result = mock.checkInventory("PRODUCT_1", 5);
        
        // Then
        assertThat(result).isCompletedWithValueMatching(status -> !status.isAvailable());
        mock.verify().methodCalled("checkInventory").calledOnce().withArguments("PRODUCT_1", 5);
    }
    
    @Test
    void testOrderFlowWithFake() {
        // Given
        OrderProcessingServiceFake fake = 
            (OrderProcessingServiceFake) testDoubleFactory.createFakeWithData();
        Order order = createTestOrder();
        
        // When
        CompletableFuture<OrderResult> result = fake.processOrder(order);
        
        // Then
        assertThat(result).isCompletedWithValueMatching(
            orderResult -> orderResult.getStatus() == OrderStatus.CONFIRMED);
        assertThat(fake.orderExists(order.getId())).isTrue();
    }
    
    @Test
    void testComplexScenarioWithBuilder() {
        // Given
        OrderProcessingService service = testDoubleFactory.createConfiguredStub();
        Order order = createTestOrder();
        TestScenarioBuilder scenarioBuilder = new TestScenarioBuilder(service);
        
        // When
        CompletableFuture<OrderProcessingResult> result = 
            scenarioBuilder.executeCompleteOrderFlow(order);
        
        // Then
        assertThat(result).isCompletedWithValueMatching(
            processingResult -> processingResult.getStatus() == OrderStatus.SHIPPED);
    }
    
    private Order createTestOrder() {
        return new Order.Builder()
            .withId("order123")
            .withCustomerId("customer123")
            .addItem(new OrderItem("PRODUCT_1", 2, new BigDecimal("29.99")))
            .withPaymentDetails(new PaymentDetails("4111111111111111", "12/25", "123", 
                                                 new Money(new BigDecimal("59.98"), 
                                                          Currency.getInstance("USD"))))
            .withShippingDetails(new ShippingDetails("order123", "123 Main St", "New York", 
                                                   "NY", "10001", "USA"))
            .build();
    }
}
```

**Advanced Follow-up Questions**:
- How do you choose between mocks, stubs, and fakes for different testing scenarios?
- What are the performance implications of different test double implementations?
- How do you handle test data consistency across different test double types?
- How do you implement test doubles for streaming or reactive APIs?
- What are the best practices for maintaining test doubles as the system evolves?
- How do you test error handling and edge cases with test doubles?

---

## 📊 **Monitoring & Observability Patterns**

### **Question 10: Observer Pattern with Distributed Tracing**
**Scenario**: Implement comprehensive observability for a distributed microservices architecture with tracing, metrics, and logging.

**Expected Answer**:
```java
// Advanced observer pattern with distributed tracing
public interface ObservabilityObserver {
    void onEvent(ObservabilityEvent event);
    boolean supports(ObservabilityEventType eventType);
    
    // Advanced: Filtering capabilities
    default boolean shouldProcess(ObservabilityEvent event) {
        return true;
    }
    
    // Advanced: Priority handling
    default int getPriority() {
        return 0;
    }
}

// Core observability event types
public enum ObservabilityEventType {
    HTTP_REQUEST, HTTP_RESPONSE, DATABASE_QUERY, CACHE_OPERATION, 
    MESSAGE_PUBLISH, MESSAGE_CONSUME, BUSINESS_TRANSACTION, 
    ERROR, WARNING, INFO, METRIC_UPDATE
}

// Base observability event
public abstract class ObservabilityEvent {
    protected final String eventId;
    protected final ObservabilityEventType eventType;
    protected final Instant timestamp;
    protected final Map<String, Object> attributes;
    protected final TraceContext traceContext;
    
    protected ObservabilityEvent(Builder<?> builder) {
        this.eventId = builder.eventId != null ? builder.eventId : UUID.randomUUID().toString();
        this.eventType = builder.eventType;
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
        this.attributes = Collections.unmodifiableMap(
            new HashMap<>(builder.attributes));
        this.traceContext = builder.traceContext;
    }
    
    // Getters
    public String getEventId() { return eventId; }
    public ObservabilityEventType getEventType() { return eventType; }
    public Instant getTimestamp() { return timestamp; }
    public Map<String, Object> getAttributes() { return attributes; }
    public TraceContext getTraceContext() { return traceContext; }
    
    // Builder pattern for extensibility
    public abstract static class Builder<T extends Builder<T>> {
        private String eventId;
        private ObservabilityEventType eventType;
        private Instant timestamp;
        private Map<String, Object> attributes = new HashMap<>();
        private TraceContext traceContext;
        
        protected abstract T self();
        
        public T eventId(String eventId) {
            this.eventId = eventId;
            return self();
        }
        
        public T eventType(ObservabilityEventType eventType) {
            this.eventType = eventType;
            return self();
        }
        
        public T timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return self();
        }
        
        public T addAttribute(String key, Object value) {
            this.attributes.put(key, value);
            return self();
        }
        
        public T addAttributes(Map<String, Object> attributes) {
            this.attributes.putAll(attributes);
            return self();
        }
        
        public T traceContext(TraceContext traceContext) {
            this.traceContext = traceContext;
            return self();
        }
        
        public T fromTraceContext(TraceContext context) {
            this.traceContext = context;
            return eventId(context.getTraceId())
                       .addAttribute("spanId", context.getSpanId())
                       .addAttribute("parentSpanId", context.getParentSpanId());
        }
    }
}

// HTTP request event
public class HttpRequestEvent extends ObservabilityEvent {
    private final String method;
    private final String url;
    private final Map<String, String> headers;
    private final String body;
    private final String clientId;
    
    private HttpRequestEvent(Builder builder) {
        super(builder);
        this.method = builder.method;
        this.url = builder.url;
        this.headers = Collections.unmodifiableMap(new HashMap<>(builder.headers));
        this.body = builder.body;
        this.clientId = builder.clientId;
    }
    
    public static class Builder extends ObservabilityEvent.Builder<Builder> {
        private String method;
        private String url;
        private Map<String, String> headers = new HashMap<>();
        private String body;
        private String clientId;
        
        @Override
        protected Builder self() {
            return this;
        }
        
        public Builder method(String method) {
            this.method = method;
            return this;
        }
        
        public Builder url(String url) {
            this.url = url;
            return this;
        }
        
        public Builder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }
        
        public Builder body(String body) {
            this.body = body;
            return this;
        }
        
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }
        
        public HttpRequestEvent build() {
            this.eventType(ObservabilityEventType.HTTP_REQUEST);
            return new HttpRequestEvent(this);
        }
    }
    
    // Getters
    public String getMethod() { return method; }
    public String getUrl() { return url; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
    public String getClientId() { return clientId; }
}

// HTTP response event
public class HttpResponseEvent extends ObservabilityEvent {
    private final int statusCode;
    private final Map<String, String> headers;
    private final String body;
    private final long durationMillis;
    
    private HttpResponseEvent(Builder builder) {
        super(builder);
        this.statusCode = builder.statusCode;
        this.headers = Collections.unmodifiableMap(new HashMap<>(builder.headers));
        this.body = builder.body;
        this.durationMillis = builder.durationMillis;
    }
    
    public static class Builder extends ObservabilityEvent.Builder<Builder> {
        private int statusCode;
        private Map<String, String> headers = new HashMap<>();
        private String body;
        private long durationMillis;
        
        @Override
        protected Builder self() {
            return this;
        }
        
        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }
        
        public Builder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }
        
        public Builder body(String body) {
            this.body = body;
            return this;
        }
        
        public Builder durationMillis(long durationMillis) {
            this.durationMillis = durationMillis;
            return this;
        }
        
        public HttpResponseEvent build() {
            this.eventType(ObservabilityEventType.HTTP_RESPONSE);
            return new HttpResponseEvent(this);
        }
    }
    
    // Getters
    public int getStatusCode() { return statusCode; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
    public long getDurationMillis() { return durationMillis; }
}

// Trace context for distributed tracing
public class TraceContext {
    private final String traceId;
    private final String spanId;
    private final String parentSpanId;
    private final Instant startTime;
    private final Map<String, String> baggage;
    
    private TraceContext(Builder builder) {
        this.traceId = builder.traceId != null ? builder.traceId : UUID.randomUUID().toString();
        this.spanId = builder.spanId != null ? builder.spanId : UUID.randomUUID().toString();
        this.parentSpanId = builder.parentSpanId;
        this.startTime = builder.startTime != null ? builder.startTime : Instant.now();
        this.baggage = Collections.unmodifiableMap(new HashMap<>(builder.baggage));
    }
    
    public static class Builder {
        private String traceId;
        private String spanId;
        private String parentSpanId;
        private Instant startTime;
        private Map<String, String> baggage = new HashMap<>();
        
        public Builder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }
        
        public Builder spanId(String spanId) {
            this.spanId = spanId;
            return this;
        }
        
        public Builder parentSpanId(String parentSpanId) {
            this.parentSpanId = parentSpanId;
            return this;
        }
        
        public Builder startTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }
        
        public Builder addBaggage(String key, String value) {
            this.baggage.put(key, value);
            return this;
        }
        
        public Builder addBaggage(Map<String, String> baggage) {
            this.baggage.putAll(baggage);
            return this;
        }
        
        public TraceContext build() {
            return new TraceContext(this);
        }
    }
    
    // Getters
    public String getTraceId() { return traceId; }
    public String getSpanId() { return spanId; }
    public String getParentSpanId() { return parentSpanId; }
    public Instant getStartTime() { return startTime; }
    public Map<String, String> getBaggage() { return baggage; }
    
    // Utility methods
    public TraceContext createChildSpan() {
        return new Builder()
            .traceId(this.traceId)
            .parentSpanId(this.spanId)
            .addBaggage(this.baggage)
            .build();
    }
}

// Advanced observability publisher with multiple observers
@Component
public class ObservabilityPublisher {
    private final List<ObservabilityObserver> observers = new CopyOnWriteArrayList<>();
    private final MeterRegistry meterRegistry;
    private final Counter eventCounter;
    private final Timer eventProcessingTimer;
    
    public ObservabilityPublisher(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.eventCounter = Counter.builder("observability.events.published")
            .register(meterRegistry);
        this.eventProcessingTimer = Timer.builder("observability.event.processing.time")
            .register(meterRegistry);
    }
    
    public void addObserver(ObservabilityObserver observer) {
        observers.add(observer);
        // Sort by priority (highest first)
        observers.sort((o1, o2) -> Integer.compare(o2.getPriority(), o1.getPriority()));
    }
    
    public void removeObserver(ObservabilityObserver observer) {
        observers.remove(observer);
    }
    
    public void publish(ObservabilityEvent event) {
        eventCounter.increment();
        
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            // Filter and notify observers
            observers.parallelStream()
                .filter(observer -> observer.supports(event.getEventType()))
                .filter(observer -> observer.shouldProcess(event))
                .forEach(observer -> {
                    try {
                        observer.onEvent(event);
                    } catch (Exception e) {
                        // Log but don't let one observer failure affect others
                        logger.warn("Observer {} failed to process event {}", 
                                   observer.getClass().getSimpleName(), 
                                   event.getEventId(), e);
                    }
                });
        } finally {
            sample.stop(eventProcessingTimer);
        }
    }
    
    // Advanced: Batch publishing for high-throughput scenarios
    public void publishBatch(List<ObservabilityEvent> events) {
        events.forEach(this::publish);
    }
    
    // Advanced: Async publishing with backpressure handling
    public CompletableFuture<Void> publishAsync(ObservabilityEvent event) {
        return CompletableFuture.runAsync(() -> publish(event))
            .exceptionally(throwable -> {
                logger.error("Failed to publish observability event: {}", 
                            event.getEventId(), throwable);
                return null;
            });
    }
    
    // Advanced: Event filtering and routing
    public void publishWithRouting(ObservabilityEvent event, 
                                 Predicate<ObservabilityObserver> router) {
        observers.parallelStream()
            .filter(observer -> observer.supports(event.getEventType()))
            .filter(router)
            .forEach(observer -> {
                try {
                    observer.onEvent(event);
                } catch (Exception e) {
                    logger.warn("Observer {} failed to process event {}", 
                               observer.getClass().getSimpleName(), 
                               event.getEventId(), e);
                }
            });
    }
    
    // Advanced: Metrics about the publisher itself
    public PublisherMetrics getMetrics() {
        return new PublisherMetrics(
            observers.size(),
            (long) eventCounter.count(),
            eventProcessingTimer.mean(TimeUnit.MILLISECONDS)
        );
    }
}

// Specific observers for different concerns

// 1. Logging observer
@Component
public class LoggingObserver implements ObservabilityObserver {
    private final Logger logger = LoggerFactory.getLogger(LoggingObserver.class);
    private final LogLevel logLevel;
    
    public LoggingObserver(LogLevel logLevel) {
        this.logLevel = logLevel;
    }
    
    @Override
    public void onEvent(ObservabilityEvent event) {
        String message = formatEvent(event);
        switch (logLevel) {
            case TRACE -> logger.trace(message);
            case DEBUG -> logger.debug(message);
            case INFO -> logger.info(message);
            case WARN -> logger.warn(message);
            case ERROR -> logger.error(message);
        }
    }
    
    private String formatEvent(ObservabilityEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("Observability Event [")
          .append(event.getEventType())
          .append("] ID: ")
          .append(event.getEventId())
          .append(", Timestamp: ")
          .append(event.getTimestamp());
          
        if (event.getTraceContext() != null) {
            sb.append(", Trace: ")
              .append(event.getTraceContext().getTraceId())
              .append(", Span: ")
              .append(event.getTraceContext().getSpanId());
        }
        
        sb.append(", Attributes: ")
          .append(event.getAttributes());
          
        return sb.toString();
    }
    
    @Override
    public boolean supports(ObservabilityEventType eventType) {
        return true; // Log all events
    }
    
    @Override
    public int getPriority() {
        return -100; // Low priority, log after other processing
    }
}

// 2. Metrics observer
@Component
public class MetricsObserver implements ObservabilityObserver {
    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> counters = new ConcurrentHashMap<>();
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();
    private final Map<String, Gauge> gauges = new ConcurrentHashMap<>();
    
    public MetricsObserver(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    @Override
    public void onEvent(ObservabilityEvent event) {
        String eventType = event.getEventType().name();
        
        // Increment counter for event type
        counters.computeIfAbsent("observability.events." + eventType, 
            name -> Counter.builder(name).register(meterRegistry))
            .increment();
        
        // Record timing if available
        if (event instanceof HttpResponseEvent) {
            HttpResponseEvent httpResponse = (HttpResponseEvent) event;
            timers.computeIfAbsent("http.response.time", 
                name -> Timer.builder(name).register(meterRegistry))
                .record(httpResponse.getDurationMillis(), TimeUnit.MILLISECONDS);
                
            // Record status code distribution
            counters.computeIfAbsent("http.status." + httpResponse.getStatusCode(), 
                name -> Counter.builder(name).register(meterRegistry))
                .increment();
        }
        
        // Record custom metrics from attributes
        recordCustomMetrics(event);
    }
    
    private void recordCustomMetrics(ObservabilityEvent event) {
        event.getAttributes().forEach((key, value) -> {
            if (value instanceof Number) {
                // Record as gauge
                gauges.computeIfAbsent("custom." + key, 
                    name -> Gauge.builder(name)
                        .register(meterRegistry, value, v -> ((Number) v).doubleValue()));
            }
        });
    }
    
    @Override
    public boolean supports(ObservabilityEventType eventType) {
        return true; // Process all events for metrics
    }
    
    @Override
    public int getPriority() {
        return 100; // High priority, record metrics early
    }
    
    // Advanced: Custom metric registration
    public void registerCustomCounter(String name, String description) {
        counters.put(name, Counter.builder(name).description(description).register(meterRegistry));
    }
    
    public void registerCustomTimer(String name, String description) {
        timers.put(name, Timer.builder(name).description(description).register(meterRegistry));
    }
}

// 3. Distributed tracing observer
@Component
public class TracingObserver implements ObservabilityObserver {
    private final Tracer tracer;
    
    public TracingObserver(Tracer tracer) {
        this.tracer = tracer;
    }
    
    @Override
    public void onEvent(ObservabilityEvent event) {
        TraceContext traceContext = event.getTraceContext();
        if (traceContext == null) {
            return; // No trace context to work with
        }
        
        Span span = tracer.spanBuilder(event.getEventType().name())
            .setParent(Context.current().with(SpanContext.createFromRemoteParent(
                traceContext.getTraceId(),
                traceContext.getSpanId(),
                TraceFlags.getSampled(),
                TraceState.getDefault())))
            .startSpan();
            
        try (Scope scope = span.makeCurrent()) {
            // Add event attributes as span attributes
            event.getAttributes().forEach((key, value) -> {
                if (value != null) {
                    span.setAttribute(key, value.toString());
                }
            });
            
            // Special handling for HTTP events
            if (event instanceof HttpRequestEvent) {
                HttpRequestEvent httpRequest = (HttpRequestEvent) event;
                span.setAttribute("http.method", httpRequest.getMethod());
                span.setAttribute("http.url", httpRequest.getUrl());
                span.setAttribute("http.client_id", httpRequest.getClientId());
            } else if (event instanceof HttpResponseEvent) {
                HttpResponseEvent httpResponse = (HttpResponseEvent) event;
                span.setAttribute("http.status_code", httpResponse.getStatusCode());
                span.setAttribute("http.duration_ms", httpResponse.getDurationMillis());
            }
            
            // Add baggage items as span attributes
            traceContext.getBaggage().forEach(span::setAttribute);
            
        } finally {
            span.end();
        }
    }
    
    @Override
    public boolean supports(ObservabilityEventType eventType) {
        // Only process events that have trace context
        return true;
    }
    
    @Override
    public boolean shouldProcess(ObservabilityEvent event) {
        return event.getTraceContext() != null;
    }
    
    @Override
    public int getPriority() {
        return 50; // Medium priority
    }
}

// 4. Alerting observer
@Component
public class AlertingObserver implements ObservabilityObserver {
    private final AlertManager alertManager;
    private final Map<ObservabilityEventType, AlertRule> alertRules = new ConcurrentHashMap<>();
    
    public AlertingObserver(AlertManager alertManager) {
        this.alertManager = alertManager;
    }
    
    @Override
    public void onEvent(ObservabilityEvent event) {
        AlertRule rule = alertRules.get(event.getEventType());
        if (rule != null && rule.shouldAlert(event)) {
            Alert alert = rule.createAlert(event);
            alertManager.sendAlert(alert);
        }
    }
    
    @Override
    public boolean supports(ObservabilityEventType eventType) {
        return alertRules.containsKey(eventType);
    }
    
    // Advanced: Dynamic alert rule management
    public void addAlertRule(ObservabilityEventType eventType, AlertRule rule) {
        alertRules.put(eventType, rule);
    }
    
    public void removeAlertRule(ObservabilityEventType eventType) {
        alertRules.remove(eventType);
    }
    
    @Override
    public int getPriority() {
        return 75; // High priority for alerting
    }
}

// Alert rule abstraction
public abstract class AlertRule {
    private final String name;
    private final String description;
    private final Severity severity;
    
    protected AlertRule(String name, String description, Severity severity) {
        this.name = name;
        this.description = description;
        this.severity = severity;
    }
    
    public abstract boolean shouldAlert(ObservabilityEvent event);
    
    public Alert createAlert(ObservabilityEvent event) {
        return Alert.builder()
            .name(name)
            .description(description)
            .severity(severity)
            .timestamp(event.getTimestamp())
            .eventId(event.getEventId())
            .traceContext(event.getTraceContext())
            .details(extractAlertDetails(event))
            .build();
    }
    
    protected abstract Map<String, Object> extractAlertDetails(ObservabilityEvent event);
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Severity getSeverity() { return severity; }
}

// HTTP error alert rule
public class HttpErrorAlertRule extends AlertRule {
    private final int errorThreshold;
    private final Set<Integer> errorStatusCodes;
    
    public HttpErrorAlertRule(int errorThreshold, Set<Integer> errorStatusCodes) {
        super("HTTP Error Rate", "High rate of HTTP errors detected", Severity.HIGH);
        this.errorThreshold = errorThreshold;
        this.errorStatusCodes = Set.copyOf(errorStatusCodes);
    }
    
    @Override
    public boolean shouldAlert(ObservabilityEvent event) {
        if (event instanceof HttpResponseEvent) {
            HttpResponseEvent httpResponse = (HttpResponseEvent) event;
            return errorStatusCodes.contains(httpResponse.getStatusCode()) &&
                   httpResponse.getDurationMillis() > errorThreshold;
        }
        return false;
    }
    
    @Override
    protected Map<String, Object> extractAlertDetails(ObservabilityEvent event) {
        if (event instanceof HttpResponseEvent) {
            HttpResponseEvent httpResponse = (HttpResponseEvent) event;
            Map<String, Object> details = new HashMap<>();
            details.put("statusCode", httpResponse.getStatusCode());
            details.put("durationMs", httpResponse.getDurationMillis());
            details.put("url", httpResponse.getAttributes().get("url"));
            return details;
        }
        return Collections.emptyMap();
    }
}

// Advanced: Context-aware observability interceptor
@Aspect
@Component
public class ObservabilityInterceptor {
    private final ObservabilityPublisher publisher;
    private final Tracer tracer;
    
    public ObservabilityInterceptor(ObservabilityPublisher publisher, Tracer tracer) {
        this.publisher = publisher;
        this.tracer = tracer;
    }
    
    @Around("@annotation(Observable)")
    public Object observeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        // Create trace context
        Span span = tracer.spanBuilder(className + "." + methodName).startSpan();
        TraceContext traceContext = new TraceContext.Builder()
            .traceId(span.getSpanContext().getTraceId())
            .spanId(span.getSpanContext().getSpanId())
            .build();
        
        try (Scope scope = span.makeCurrent()) {
            // Record method entry
            ObservabilityEvent entryEvent = new ObservabilityEvent.Builder<>()
                .eventType(ObservabilityEventType.BUSINESS_TRANSACTION)
                .traceContext(traceContext)
                .addAttribute("class", className)
                .addAttribute("method", methodName)
                .addAttribute("phase", "entry")
                .build();
            publisher.publish(entryEvent);
            
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            // Record method exit
            ObservabilityEvent exitEvent = new ObservabilityEvent.Builder<>()
                .eventType(ObservabilityEventType.BUSINESS_TRANSACTION)
                .traceContext(traceContext)
                .addAttribute("class", className)
                .addAttribute("method", methodName)
                .addAttribute("phase", "exit")
                .addAttribute("durationMs", duration)
                .build();
            publisher.publish(exitEvent);
            
            return result;
        } catch (Exception e) {
            // Record exception
            ObservabilityEvent errorEvent = new ObservabilityEvent.Builder<>()
                .eventType(ObservabilityEventType.ERROR)
                .traceContext(traceContext)
                .addAttribute("class", className)
                .addAttribute("method", methodName)
                .addAttribute("exception", e.getClass().getSimpleName())
                .addAttribute("message", e.getMessage())
                .build();
            publisher.publish(errorEvent);
            
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}

// Annotation for marking observable methods
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Observable {
    String value() default "";
}

// Advanced: Observability configuration and management
@Configuration
@EnableAspectJAutoProxy
public class ObservabilityConfiguration {
    
    @Bean
    public ObservabilityPublisher observabilityPublisher(MeterRegistry meterRegistry) {
        return new ObservabilityPublisher(meterRegistry);
    }
    
    @Bean
    public LoggingObserver loggingObserver() {
        return new LoggingObserver(LogLevel.INFO);
    }
    
    @Bean
    public MetricsObserver metricsObserver(MeterRegistry meterRegistry) {
        return new MetricsObserver(meterRegistry);
    }
    
    @Bean
    public TracingObserver tracingObserver(Tracer tracer) {
        return new TracingObserver(tracer);
    }
    
    @Bean
    public AlertingObserver alertingObserver(AlertManager alertManager) {
        return new AlertingObserver(alertManager);
    }
    
    @PostConstruct
    public void configureObservers(ObservabilityPublisher publisher,
                                 LoggingObserver loggingObserver,
                                 MetricsObserver metricsObserver,
                                 TracingObserver tracingObserver,
                                 AlertingObserver alertingObserver) {
        // Register observers with appropriate priorities
        publisher.addObserver(tracingObserver);
        publisher.addObserver(metricsObserver);
        publisher.addObserver(alertingObserver);
        publisher.addObserver(loggingObserver);
        
        // Configure alert rules
        alertingObserver.addAlertRule(ObservabilityEventType.HTTP_RESPONSE,
            new HttpErrorAlertRule(5000, Set.of(500, 502, 503, 504)));
    }
}

// Usage example in a service
@Service
public class OrderService {
    
    private final ObservabilityPublisher publisher;
    
    public OrderService(ObservabilityPublisher publisher) {
        this.publisher = publisher;
    }
    
    @Observable
    public OrderResult processOrder(Order order) {
        TraceContext traceContext = TraceContext.getCurrent(); // Get from context
        
        // Record HTTP request
        HttpRequestEvent requestEvent = new HttpRequestEvent.Builder()
            .method("POST")
            .url("/api/orders")
            .addHeader("Content-Type", "application/json")
            .addAttribute("orderId", order.getId())
            .addAttribute("customerId", order.getCustomerId())
            .traceContext(traceContext)
            .build();
        publisher.publish(requestEvent);
        
        try {
            // Business logic here
            OrderResult result = doProcessOrder(order);
            
            // Record HTTP response
            HttpResponseEvent responseEvent = new HttpResponseEvent.Builder()
                .statusCode(200)
                .durationMillis(150)
                .addAttribute("orderId", order.getId())
                .addAttribute("result", result.getStatus().name())
                .traceContext(traceContext)
                .build();
            publisher.publish(responseEvent);
            
            return result;
        } catch (Exception e) {
            // Record error
            ObservabilityEvent errorEvent = new ObservabilityEvent.Builder<>()
                .eventType(ObservabilityEventType.ERROR)
                .addAttribute("orderId", order.getId())
                .addAttribute("exception", e.getClass().getSimpleName())
                .addAttribute("message", e.getMessage())
                .traceContext(traceContext)
                .build();
            publisher.publish(errorEvent);
            
            throw e;
        }
    }
    
    private OrderResult doProcessOrder(Order order) {
        // Implementation details
        return new OrderResult(order.getId(), OrderStatus.CONFIRMED, "Order processed");
    }
}
```

**Advanced Follow-up Questions**:
- How do you handle observability data volume and storage costs?
- What are the performance implications of extensive observability instrumentation?
- How do you implement sampling strategies for distributed tracing?
- How do you correlate events across different services and systems?
- What are the best practices for alert fatigue prevention?
- How do you implement observability patterns in reactive/async systems?

---

## 🏁 **Summary**

This enhanced design patterns document now covers:

1. **Architectural Patterns** - Microservices communication, CQRS, Event Sourcing, Saga patterns
2. **Domain-Driven Design** - Aggregates, Value Objects, Repositories, Domain Services
3. **Behavioral Patterns** - Strategy with dynamic loading, State pattern, Observer with event sourcing
4. **Structural Patterns** - Decorator with dynamic composition, Flyweight with memory optimization
5. **Creational Patterns** - Builder with validation and fluent API
6. **Integration Patterns** - Adapter with protocol translation
7. **Testing Patterns** - Comprehensive test doubles (Mocks, Stubs, Fakes)
8. **Monitoring & Observability** - Distributed tracing, metrics collection, alerting

Each pattern includes:
- Real-world scenarios and implementation examples
- Advanced features like dynamic configuration, validation, and error handling
- Performance considerations and best practices
- Integration with modern frameworks and libraries
- Comprehensive follow-up questions for deeper understanding

These patterns are essential for senior Java developers working on complex, distributed systems and preparing for technical interviews at top-tier companies.