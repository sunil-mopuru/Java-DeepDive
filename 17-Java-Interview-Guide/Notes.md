# Java Interview Guide - Quick Reference Cheat Sheet (10+ Years Experience)

## 🔥 **Core Java - Advanced Quick Reference**

### **Memory Management & GC**
```java
// Heap Memory Regions
Young Generation: Eden + S0 + S1 (Short-lived objects)
Old Generation: Tenured space (Long-lived objects)
Metaspace: Class metadata (Java 8+, replaced PermGen)

// GC Algorithm Selection
Serial GC:    Small apps (<100MB heap)
Parallel GC:  High throughput batch processing
G1 GC:        Large heaps (>6GB), low latency
ZGC:          Ultra-low latency (<10ms pauses)
Shenandoah:   Low pause times with high allocation rates

// Production GC Tuning
-Xms8g -Xmx8g                    // Set heap size
-XX:+UseG1GC                     // Use G1 collector
-XX:MaxGCPauseMillis=200         // Target pause time
-XX:+UseStringDeduplication      // Save memory on duplicate strings
-Xloggc:gc.log -XX:+PrintGCDetails  // Enable GC logging
```

### **Concurrency Mastery**
```java
// Thread Safety Levels
Immutable:     String, Integer (safest)
Thread-Safe:   Vector, Hashtable, Collections.synchronizedXxx()
Conditionally: HashMap (not thread-safe), needs external sync
Lock-Free:     AtomicInteger, ConcurrentHashMap

// Advanced Synchronization
volatile:      Visibility guarantee, no atomicity
synchronized:  Mutual exclusion + visibility
ReentrantLock: Explicit locking with tryLock(), timed locks
StampedLock:   Optimistic reads (Java 8+)

// Concurrent Collections Performance
ConcurrentHashMap:     Best for high concurrency reads/writes
CopyOnWriteArrayList: Best for mostly reads, few writes
BlockingQueue:        Producer-consumer patterns
```

### **JVM Performance Monitoring**
```bash
# Essential Commands for Production
jstat -gc <pid> 1s              # GC statistics
jmap -dump:live,format=b,file=heap.hprof <pid>  # Heap dump
jstack <pid>                    # Thread dump
jcmd <pid> VM.flags            # Current JVM flags

# Performance Analysis
Java Flight Recorder: -XX:+FlightRecorder
Application Metrics: Micrometer, Dropwizard Metrics
APM Tools: New Relic, AppDynamics, Datadog
```

---

## ⚡ **Modern Java Features (Java 8-21+)**

### **Stream API Advanced Patterns**
```java
// Custom Collectors
Collector<String, ?, Map<Integer, List<String>>> byLength = 
    Collectors.groupingBy(String::length);

// Parallel Streams - Use When
- CPU-intensive operations
- Large datasets (>10,000 elements)
- Independent operations (no shared state)

// Avoid Parallel Streams When
- I/O bound operations
- Small datasets
- Operations modify shared state
```

### **Functional Programming**
```java
// Function Composition
Function<String, String> trim = String::trim;
Function<String, String> upper = String::toUpperCase;
Function<String, String> process = trim.andThen(upper);

// Optional Best Practices
// Good
return users.stream()
    .filter(u -> u.getId().equals(id))
    .findFirst()
    .map(User::getName)
    .orElse("Unknown");

// Bad - Don't use Optional for fields or parameters
private Optional<String> name;  // DON'T DO THIS
public void setName(Optional<String> name) { } // DON'T DO THIS
```

### **Records & Pattern Matching (Java 14+)**
```java
// Records for DTOs
public record PersonDTO(String name, int age, List<String> skills) {
    // Compact constructor for validation
    public PersonDTO {
        if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
        skills = List.copyOf(skills); // Defensive copy
    }
}

// Pattern Matching (Java 17+)
public String formatValue(Object obj) {
    return switch (obj) {
        case Integer i -> "Integer: " + i;
        case String s when s.length() > 10 -> "Long string: " + s;
        case String s -> "Short string: " + s;
        case null -> "null value";
        default -> "Unknown type";
    };
}
```

### **Virtual Threads (Java 21+)**
```java
// Traditional Platform Threads
ExecutorService executor = Executors.newFixedThreadPool(200);

// Virtual Threads (Project Loom)
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

// Use Virtual Threads for:
- High-concurrency I/O bound tasks
- Blocking operations (file I/O, network calls)
- Request handling in web applications

// Stick with Platform Threads for:
- CPU-intensive tasks
- When you need precise thread control
- Legacy code with ThreadLocal heavy usage
```

---

## 🏗️ **System Design & Architecture**

### **Microservices Patterns**
```java
// Service Communication Patterns
Synchronous:  REST API, GraphQL
Asynchronous: Message queues, Event streaming

// Data Management Patterns
Database per Service: Each service owns its data
Shared Database Anti-pattern: Avoid shared databases
Event Sourcing: Store events, not current state
CQRS: Separate read and write models

// Resilience Patterns
Circuit Breaker: Prevent cascading failures
Bulkhead: Isolate critical resources
Timeout: Set reasonable timeouts for all calls
Retry with Backoff: Handle transient failures
```

### **Design Patterns for Scale**
```java
// Singleton for Configuration
public enum ConfigManager {
    INSTANCE;
    private Properties config = loadConfig();
    public String getProperty(String key) { return config.getProperty(key); }
}

// Factory for Complex Object Creation
public class DatabaseConnectionFactory {
    public static Connection create(DatabaseType type) {
        return switch (type) {
            case MYSQL -> new MySQLConnection();
            case POSTGRESQL -> new PostgreSQLConnection();
            case MONGODB -> new MongoConnection();
        };
    }
}

// Observer for Event-Driven Architecture
public class EventBus {
    private final Map<Class<?>, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();
    
    public <T> void subscribe(Class<T> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(handler);
    }
}
```

### **Performance Patterns**
```java
// Object Pool Pattern for Expensive Objects
public class DatabaseConnectionPool {
    private final BlockingQueue<Connection> pool;
    private final AtomicInteger createdConnections = new AtomicInteger(0);
    
    public Connection borrowConnection() throws InterruptedException {
        return pool.poll(5, TimeUnit.SECONDS);
    }
}

// Cache-Aside Pattern
@Service
public class UserService {
    @Autowired private UserRepository repository;
    @Autowired private CacheManager cacheManager;
    
    public User getUser(String id) {
        User cached = cacheManager.get(id, User.class);
        if (cached != null) return cached;
        
        User user = repository.findById(id);
        if (user != null) {
            cacheManager.put(id, user, Duration.ofMinutes(30));
        }
        return user;
    }
}
```

---

## 📊 **Performance & Scalability**

### **Database Optimization**
```java
// Connection Pool Configuration
HikariConfig config = new HikariConfig();
config.setMaximumPoolSize(20);           // CPU cores * 2
config.setMinimumIdle(5);               // Keep minimum connections
config.setConnectionTimeout(30000);      // 30 seconds
config.setIdleTimeout(600000);          // 10 minutes
config.setLeakDetectionThreshold(60000); // 1 minute leak detection

// N+1 Query Problem Solutions
// Bad
List<User> users = userRepository.findAll();
for (User user : users) {
    List<Order> orders = orderRepository.findByUserId(user.getId()); // N+1!
}

// Good - Use JOIN FETCH
@Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
List<User> findAllWithOrders();
```

### **Caching Strategy**
```java
// Multi-Level Caching
L1 Cache: Application memory (Caffeine, Guava)
L2 Cache: Distributed cache (Redis, Hazelcast)
L3 Cache: CDN (CloudFlare, AWS CloudFront)

// Cache Patterns
Cache-Aside:    Application manages cache
Write-Through:  Write to cache and DB simultaneously  
Write-Behind:   Write to cache first, DB asynchronously
Refresh-Ahead:  Proactively refresh before expiration

// Redis Configuration for High Performance
@Configuration
public class RedisConfig {
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        
        return new LettuceConnectionFactory(
            new RedisStandaloneConfiguration("localhost", 6379),
            LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .build()
        );
    }
}
```

### **Monitoring & Observability**
```java
// Custom Metrics with Micrometer
@RestController
public class OrderController {
    private final Counter orderCounter;
    private final Timer orderProcessingTime;
    
    public OrderController(MeterRegistry meterRegistry) {
        this.orderCounter = Counter.builder("orders.created")
            .description("Number of orders created")
            .register(meterRegistry);
            
        this.orderProcessingTime = Timer.builder("orders.processing.time")
            .description("Order processing time")
            .register(meterRegistry);
    }
    
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        return orderProcessingTime.recordCallable(() -> {
            Order order = orderService.createOrder(request);
            orderCounter.increment();
            return ResponseEntity.ok(order);
        });
    }
}

// Structured Logging with MDC
public class RequestLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            MDC.put("requestId", UUID.randomUUID().toString());
            MDC.put("userId", extractUserId(request));
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
```

---

## 🔧 **Production Best Practices**

### **Error Handling & Resilience**
```java
// Circuit Breaker Pattern (Resilience4j)
@Component
public class ExternalServiceClient {
    private final CircuitBreaker circuitBreaker;
    
    public ExternalServiceClient() {
        this.circuitBreaker = CircuitBreaker.ofDefaults("externalService");
        circuitBreaker.getEventPublisher()
            .onStateTransition(event -> 
                log.info("Circuit breaker state transition: {}", event));
    }
    
    public String callExternalService(String data) {
        return circuitBreaker.executeSupplier(() -> {
            // Actual service call
            return restTemplate.postForObject("/api/process", data, String.class);
        });
    }
}

// Retry with Exponential Backoff
@Retryable(
    value = {TransientException.class},
    maxAttempts = 3,
    backoff = @Backoff(delay = 1000, multiplier = 2)
)
public void processWithRetry() {
    // Processing logic that might fail transiently
}
```

### **Security Best Practices**
```java
// Input Validation
@Valid
public class CreateUserRequest {
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
    
    @Email
    private String email;
    
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String password;
}

// SQL Injection Prevention
@Repository
public class UserRepository {
    // Good - Parameterized query
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    
    // Bad - String concatenation (DON'T DO THIS)
    // "SELECT * FROM users WHERE email = '" + email + "'"
}

// JWT Token Handling
@Service
public class JwtTokenService {
    private final Key signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    public String generateToken(User user) {
        return Jwts.builder()
            .setSubject(user.getId())
            .setIssuedAt(new Date())
            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
            .signWith(signingKey)
            .compact();
    }
}
```

---

## 🎯 **Common Interview Questions & Answers**

### **Technical Deep Dive**
```
Q: Explain the difference between fail-fast and fail-safe iterators.
A: 
- Fail-fast: Throw ConcurrentModificationException if collection modified during iteration
  (ArrayList, HashMap iterators)
- Fail-safe: Work on copy of collection, don't throw exceptions
  (CopyOnWriteArrayList, ConcurrentHashMap iterators)

Q: How does ConcurrentHashMap achieve thread safety without synchronizing the entire map?
A:
- Uses segment-based locking (Java 7) / CAS operations (Java 8+)
- Different buckets can be accessed concurrently
- Read operations don't require locks
- Write operations lock only specific segments/buckets

Q: When would you use CompletableFuture over traditional Future?
A:
- Non-blocking operations with thenApply(), thenCompose()
- Exception handling with handle(), exceptionally()
- Combining multiple futures with allOf(), anyOf()
- Asynchronous pipeline creation
```

### **System Design Questions**
```
Q: Design a URL shortener like bit.ly
A: Key components:
- Load balancer (nginx/HAProxy)
- Application servers (Spring Boot)
- Database sharding strategy
- Cache layer (Redis)
- Rate limiting
- Analytics service
- CDN for global distribution

Q: How would you handle 1 million concurrent users?
A:
- Horizontal scaling with load balancers
- Database read replicas
- Distributed caching (Redis Cluster)
- Async processing with message queues
- CDN for static content
- Circuit breakers for resilience
```

### **Performance Questions**
```
Q: Your application has memory leaks. How do you identify and fix them?
A:
1. Enable heap dumps on OOM: -XX:+HeapDumpOnOutOfMemoryError
2. Take periodic heap dumps: jmap -dump:live,format=b,file=heap.hprof <pid>
3. Analyze with MAT (Eclipse Memory Analyzer)
4. Look for: Large object graphs, duplicate objects, retained objects
5. Common causes: Static collections, listeners not removed, ThreadLocal

Q: Database queries are slow. How do you optimize?
A:
1. Enable query logging and analyze slow queries
2. Add appropriate indexes
3. Optimize JOIN operations
4. Use connection pooling
5. Implement query result caching
6. Consider read replicas for read-heavy workloads
```

---

## 🏆 **Interview Success Framework**

### **STAR Method for Behavioral Questions**
```
Situation: Set the context
Task:      Describe your responsibility
Action:    Explain what you did
Result:    Share the outcome and impact

Example: "Tell me about a time you improved system performance"
S: E-commerce site experiencing 5-second response times during peak traffic
T: Reduce response time to under 500ms and handle 10x traffic
A: Implemented Redis caching, optimized database queries, added CDN
R: Achieved 200ms average response time, handled Black Friday traffic successfully
```

### **Technical Problem-Solving Approach**
```
1. Clarify Requirements
   - Ask questions about scale, constraints, priorities
   - Define success criteria and SLAs
   
2. High-Level Design
   - Start with simple solution
   - Identify major components
   - Define APIs and data flow
   
3. Deep Dive
   - Database schema design
   - Technology choices and trade-offs
   - Scalability and performance considerations
   
4. Address Edge Cases
   - Error handling and resilience
   - Security considerations
   - Monitoring and observability
```

### **Code Quality Checklist**
```java
// Clean, readable code
✅ Meaningful variable and method names
✅ Single responsibility principle
✅ Proper error handling
✅ Unit tests for edge cases
✅ Thread safety considerations
✅ Performance implications
✅ Security best practices

// Example of clean code
public class OrderProcessor {
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final NotificationService notificationService;
    
    public OrderResult processOrder(OrderRequest request) {
        validateOrderRequest(request);
        
        try {
            reserveInventory(request.getItems());
            PaymentResult payment = processPayment(request.getPayment());
            
            if (payment.isSuccessful()) {
                Order order = createOrder(request);
                sendConfirmationEmail(order);
                return OrderResult.success(order);
            } else {
                releaseInventory(request.getItems());
                return OrderResult.failure("Payment failed: " + payment.getErrorMessage());
            }
        } catch (InsufficientInventoryException e) {
            return OrderResult.failure("Insufficient inventory for items: " + e.getItems());
        } catch (PaymentProcessingException e) {
            releaseInventory(request.getItems());
            return OrderResult.failure("Payment processing error: " + e.getMessage());
        }
    }
}
```

---

## 🚀 **Final Preparation Checklist**

### **Technical Review (Day Before)**
```
✅ Review Java memory model and GC algorithms
✅ Practice stream API and lambda expressions  
✅ Refresh design patterns and SOLID principles
✅ Review concurrency and thread safety concepts
✅ Practice system design problems
```

### **Behavioral Preparation**
```
✅ Prepare STAR format examples for common questions
✅ Practice explaining technical concepts to non-technical audience
✅ Prepare questions to ask the interviewer
✅ Review company's tech stack and recent developments
✅ Practice whiteboarding and talking through solutions
```

### **Day of Interview**
```
✅ Arrive 15 minutes early (or join video call early)
✅ Bring notebook and pen for notes
✅ Have backup internet connection for remote interviews
✅ Test screen sharing and video setup beforehand
✅ Stay calm, think out loud, ask clarifying questions
```

---

**🎯 Remember: Senior interviews focus on depth, system thinking, and leadership. Show your experience through concrete examples and demonstrate how you approach complex technical challenges!**

---

*This cheat sheet is optimized for quick review before senior Java developer interviews. Focus on understanding concepts deeply rather than memorizing answers.*