# Database Design for Senior Java Developers (10+ Years Experience)

## 🗄️ **Data Architecture Principles**

### **Scenario 1: Schema Design for High-Volume Applications**
**Situation**: Designing a database schema for an e-commerce platform handling millions of transactions daily.

**Key Considerations**:
```java
// Database design patterns for scalability
public class ECommerceDatabaseDesign {
    
    // Normalized schema for product catalog
    @Entity
    @Table(name = "products")
    public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @Column(nullable = false)
        private String sku;
        
        @Column(nullable = false)
        private String name;
        
        @Column(length = 1000)
        private String description;
        
        @Embedded
        private Price price;
        
        @ElementCollection
        @CollectionTable(name = "product_attributes")
        private Map<String, String> attributes;
        
        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
        private List<ProductImage> images;
        
        // Indexes for performance
        @Index(name = "idx_product_sku", columnList = "sku")
        @Index(name = "idx_product_category", columnList = "category_id")
    }
    
    // Denormalized schema for order analytics
    @Entity
    @Table(name = "order_analytics")
    public class OrderAnalytics {
        @Id
        private String orderId;
        
        // Pre-computed fields for fast queries
        private Long customerId;
        private BigDecimal totalAmount;
        private String currency;
        private LocalDateTime orderDate;
        private String status;
        
        // Denormalized customer info
        private String customerName;
        private String customerEmail;
        private String customerCountry;
        
        // Product summary
        @Column(length = 5000)
        private String productSummary; // JSON string of products
        
        // Indexes for analytics queries
        @Index(name = "idx_analytics_date", columnList = "order_date")
        @Index(name = "idx_analytics_customer", columnList = "customerId")
        @Index(name = "idx_analytics_status", columnList = "status")
    }
    
    // Partitioning strategy for large tables
    @Entity
    @Table(name = "orders")
    @Partitioning(
        name = "order_partition",
        partitionType = PartitionType.RANGE,
        column = "order_date"
    )
    public class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @Column(nullable = false)
        private LocalDateTime orderDate;
        
        @ManyToOne
        @JoinColumn(name = "customer_id")
        private Customer customer;
        
        @OneToMany(cascade = CascadeType.ALL)
        @JoinColumn(name = "order_id")
        private List<OrderItem> items;
        
        private BigDecimal totalAmount;
        
        @Enumerated(EnumType.STRING)
        private OrderStatus status;
    }
}
```

### **Scenario 2: Multi-Tenancy Database Design**
**Expected Answer**:
```java
// Multi-tenancy patterns implementation
public enum MultiTenancyStrategy {
    SEPARATE_DATABASE,     // Each tenant has its own database
    SEPARATE_SCHEMA,       // Each tenant has its own schema
    SHARED_SCHEMA          // All tenants share the same schema
}

// Shared schema approach with tenant isolation
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String email;
    
    // Tenant identifier
    @Column(name = "tenant_id", nullable = false)
    private String tenantId;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    // Ensure tenant isolation
    @Where(clause = "tenant_id = :tenantId")
    @Index(name = "idx_user_tenant", columnList = "tenant_id")
}

// Database configuration with tenant routing
@Component
public class TenantRoutingDataSource extends AbstractRoutingDataSource {
    
    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant();
    }
    
    public void addTenantDataSource(String tenantId, DataSource dataSource) {
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(tenantId, dataSource);
        this.setTargetDataSources(dataSources);
        this.afterPropertiesSet();
    }
}

// Repository with tenant awareness
@Repository
public class TenantAwareRepository<T> {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public T findById(Class<T> entityClass, Long id) {
        String tenantId = TenantContext.getCurrentTenant();
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        
        Predicate tenantPredicate = cb.equal(root.get("tenantId"), tenantId);
        Predicate idPredicate = cb.equal(root.get("id"), id);
        
        query.where(cb.and(tenantPredicate, idPredicate));
        
        return entityManager.createQuery(query).getSingleResult();
    }
}
```

## 🔄 **Data Consistency & Integrity**

### **Scenario 3: Distributed Data Consistency**
**Expected Answer**:
```java
// Event sourcing for data consistency
@Entity
@Table(name = "order_events")
public class OrderEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String orderId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderEventType eventType;
    
    @Column(length = 5000)
    private String eventData; // JSON payload
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    private String userId;
    
    @Version
    private Long version;
}

// Saga pattern for distributed transactions
@Component
public class OrderSagaOrchestrator {
    
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final NotificationService notificationService;
    
    public void processOrder(Order order) {
        SagaContext context = new SagaContext(order);
        
        try {
            // Step 1: Validate order
            orderService.validateOrder(order);
            context.addStep(new SagaStep("VALIDATE_ORDER", "COMPLETED"));
            
            // Step 2: Reserve inventory
            inventoryService.reserveInventory(order.getItems());
            context.addStep(new SagaStep("RESERVE_INVENTORY", "COMPLETED"));
            
            // Step 3: Process payment
            PaymentResult paymentResult = paymentService.processPayment(order);
            context.addStep(new SagaStep("PROCESS_PAYMENT", "COMPLETED"));
            
            // Step 4: Confirm order
            orderService.confirmOrder(order);
            context.addStep(new SagaStep("CONFIRM_ORDER", "COMPLETED"));
            
            // Step 5: Send notification
            notificationService.sendOrderConfirmation(order);
            context.addStep(new SagaStep("SEND_NOTIFICATION", "COMPLETED"));
            
            // Saga completed successfully
            context.setStatus(SagaStatus.COMPLETED);
            
        } catch (Exception e) {
            // Compensate for failed steps
            compensate(context);
            throw new OrderProcessingException("Order processing failed", e);
        }
    }
    
    private void compensate(SagaContext context) {
        List<SagaStep> completedSteps = context.getCompletedSteps();
        
        // Compensate in reverse order
        for (int i = completedSteps.size() - 1; i >= 0; i--) {
            SagaStep step = completedSteps.get(i);
            try {
                switch (step.getAction()) {
                    case "SEND_NOTIFICATION":
                        // Notification doesn't need compensation
                        break;
                    case "CONFIRM_ORDER":
                        orderService.cancelOrder(context.getOrder());
                        break;
                    case "PROCESS_PAYMENT":
                        paymentService.refundPayment(context.getOrder());
                        break;
                    case "RESERVE_INVENTORY":
                        inventoryService.releaseInventory(context.getOrder().getItems());
                        break;
                    case "VALIDATE_ORDER":
                        // Validation doesn't need compensation
                        break;
                }
                step.setStatus("COMPENSATED");
            } catch (Exception e) {
                log.error("Compensation failed for step: " + step.getAction(), e);
                // Log compensation failure but continue with other compensations
            }
        }
        
        context.setStatus(SagaStatus.FAILED);
    }
}
```

## 📊 **Performance Optimization**

### **Scenario 4: Query Optimization Strategies**
**Expected Answer**:
```java
// Query optimization techniques
@Repository
public class OptimizedOrderRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    // Use projection to avoid loading unnecessary data
    public List<OrderSummary> findOrderSummariesByCustomer(Long customerId) {
        String jpql = """
            SELECT new com.example.OrderSummary(
                o.id, o.orderDate, o.totalAmount, o.status
            ) 
            FROM Order o 
            WHERE o.customer.id = :customerId 
            ORDER BY o.orderDate DESC
            """;
            
        return entityManager.createQuery(jpql, OrderSummary.class)
            .setParameter("customerId", customerId)
            .setMaxResults(50)
            .getResultList();
    }
    
    // Batch processing for large datasets
    @Transactional
    public void processOrdersInBatch(OrderProcessingRequest request) {
        String jpql = """
            SELECT o FROM Order o 
            WHERE o.status = :status 
            AND o.orderDate BETWEEN :startDate AND :endDate
            """;
            
        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class)
            .setParameter("status", OrderStatus.PENDING)
            .setParameter("startDate", request.getStartDate())
            .setParameter("endDate", request.getEndDate())
            .setFirstResult(request.getOffset())
            .setMaxResults(request.getBatchSize());
            
        List<Order> orders = query.getResultList();
        
        // Process in smaller batches to avoid memory issues
        for (List<Order> batch : Lists.partition(orders, 100)) {
            processBatch(batch);
            entityManager.flush();
            entityManager.clear();
        }
    }
    
    // Connection pooling optimization
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/ecommerce");
        config.setUsername("user");
        config.setPassword("password");
        
        // Connection pool settings
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(60000);
        
        // Performance settings
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        
        return new HikariDataSource(config);
    }
}

// Read replica configuration for read-heavy workloads
@Configuration
public class ReadReplicaConfiguration {
    
    @Bean
    @Primary
    public DataSource writeDataSource() {
        // Primary database for writes
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://primary-db:5432/ecommerce");
        // ... other configurations
        return new HikariDataSource(config);
    }
    
    @Bean("readDataSource")
    public DataSource readDataSource() {
        // Read replica for reads
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://read-replica:5432/ecommerce");
        // ... other configurations
        return new HikariDataSource(config);
    }
    
    @Bean
    public DataSource routingDataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("write", writeDataSource());
        dataSourceMap.put("read", readDataSource());
        
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(writeDataSource());
        
        return routingDataSource;
    }
}

// Routing logic for read/write operations
public class RoutingDataSource extends AbstractRoutingDataSource {
    
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceType();
    }
}

// Annotation for read-only operations
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ReadOnly {
}

// Aspect to route to read replica for read-only operations
@Aspect
@Component
public class DataSourceRoutingAspect {
    
    @Around("@annotation(ReadOnly)")
    public Object routeToReadReplica(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            DataSourceContextHolder.setDataSourceType("read");
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.clear();
        }
    }
    
    @Around("execution(* com.example.service.*.save*(..)) || " +
            "execution(* com.example.service.*.update*(..)) || " +
            "execution(* com.example.service.*.delete*(..))")
    public Object routeToWriteDataSource(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            DataSourceContextHolder.setDataSourceType("write");
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.clear();
        }
    }
}
```

## 🛡️ **Security Considerations**

### **Scenario 5: Data Security Implementation**
**Expected Answer**:
```java
// Data encryption and security patterns
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    // Encrypted fields
    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "email_encrypted")
    private String email;
    
    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "phone_encrypted")
    private String phone;
    
    // Sensitive data with tokenization
    @Column(name = "credit_card_token")
    private String creditCardToken;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

// JPA converter for field-level encryption
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {
    
    private final AESEncryptionService encryptionService = new AESEncryptionService();
    
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return encryptionService.encrypt(attribute);
    }
    
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return encryptionService.decrypt(dbData);
    }
}

// Tokenization service for sensitive data
@Service
public class TokenizationService {
    
    private final TokenRepository tokenRepository;
    private final EncryptionService encryptionService;
    
    public String tokenizeSensitiveData(String sensitiveData) {
        // Generate secure token
        String token = UUID.randomUUID().toString();
        
        // Store mapping securely
        TokenMapping mapping = new TokenMapping();
        mapping.setToken(token);
        mapping.setEncryptedData(encryptionService.encrypt(sensitiveData));
        mapping.setCreatedAt(LocalDateTime.now());
        
        tokenRepository.save(mapping);
        
        return token;
    }
    
    public String detokenize(String token) {
        TokenMapping mapping = tokenRepository.findByToken(token);
        if (mapping == null) {
            throw new TokenNotFoundException("Token not found: " + token);
        }
        
        // Check if token has expired
        if (mapping.getCreatedAt().isBefore(LocalDateTime.now().minusDays(30))) {
            throw new TokenExpiredException("Token has expired");
        }
        
        return encryptionService.decrypt(mapping.getEncryptedData());
    }
}

// Audit logging for data access
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private String action;
    
    @Column(nullable = false)
    private String tableName;
    
    private Long recordId;
    
    @Column(length = 1000)
    private String oldValue;
    
    @Column(length = 1000)
    private String newValue;
    
    @CreationTimestamp
    private LocalDateTime timestamp;
    
    private String ipAddress;
    private String userAgent;
}

// Aspect for automatic audit logging
@Aspect
@Component
public class AuditLoggingAspect {
    
    private final AuditLogRepository auditLogRepository;
    
    @AfterReturning(pointcut = "execution(* com.example.repository.*.save(..))", 
                    returning = "result")
    public void logSaveOperation(JoinPoint joinPoint, Object result) {
        try {
            AuditLog auditLog = createAuditLog("SAVE", joinPoint, result);
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Failed to log audit event", e);
        }
    }
    
    @AfterReturning(pointcut = "execution(* com.example.repository.*.delete(..))")
    public void logDeleteOperation(JoinPoint joinPoint) {
        try {
            AuditLog auditLog = createAuditLog("DELETE", joinPoint, null);
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Failed to log audit event", e);
        }
    }
    
    private AuditLog createAuditLog(String action, JoinPoint joinPoint, Object result) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUserId(SecurityContext.getCurrentUserId());
        auditLog.setAction(action);
        auditLog.setTableName(extractTableName(joinPoint));
        auditLog.setRecordId(extractRecordId(joinPoint, result));
        auditLog.setIpAddress(RequestContext.getCurrentIpAddress());
        auditLog.setUserAgent(RequestContext.getCurrentUserAgent());
        return auditLog;
    }
}
```

## 🛡️ **Advanced Security Implementation Patterns**

### **Scenario 6: Comprehensive Data Protection Strategy**
**Expected Answer**:
```java
// Advanced data protection with multiple security layers
@Entity
@Table(name = "customer_profiles")
public class CustomerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Personal identifiers - field level encryption
    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "first_name_encrypted")
    private String firstName;
    
    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "last_name_encrypted")
    private String lastName;
    
    // Sensitive data - tokenization
    @Column(name = "ssn_token")
    private String ssnToken;
    
    // PII with search capability - format-preserving encryption
    @Convert(converter = FormatPreservingEncryptionConverter.class)
    @Column(name = "phone_fpe")
    private String phoneNumber;
    
    // Behavioral data - differential privacy
    @ElementCollection
    @CollectionTable(name = "customer_behavior_patterns")
    private List<BehavioralPattern> behaviorPatterns;
    
    // Audit trail - immutable logging
    @OneToMany(mappedBy = "customerProfile", cascade = CascadeType.ALL)
    private List<AuditRecord> auditRecords;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Version
    private Long version;
}

// Advanced encryption service with key rotation
@Service
public class AdvancedEncryptionService {
    
    private final KeyManagementService keyManagementService;
    private final Cache<String, SecretKey> keyCache;
    
    public String encrypt(String plaintext, String context) {
        // Get current encryption key for this context
        EncryptionKey currentKey = keyManagementService.getCurrentKey(context);
        
        // Perform encryption with authenticated encryption
        byte[] nonce = generateSecureNonce();
        byte[] ciphertext = performAuthenticatedEncryption(
            plaintext.getBytes(StandardCharsets.UTF_8), 
            currentKey.getKey(), 
            nonce
        );
        
        // Create encrypted data object with metadata
        EncryptedData encryptedData = new EncryptedData();
        encryptedData.setCiphertext(Base64.getEncoder().encodeToString(ciphertext));
        encryptedData.setNonce(Base64.getEncoder().encodeToString(nonce));
        encryptedData.setKeyId(currentKey.getId());
        encryptedData.setEncryptionAlgorithm("AES-256-GCM");
        encryptedData.setEncryptedAt(Instant.now());
        
        return serializeEncryptedData(encryptedData);
    }
    
    public String decrypt(String encryptedPayload, String context) {
        // Deserialize encrypted data
        EncryptedData encryptedData = deserializeEncryptedData(encryptedPayload);
        
        // Get decryption key
        SecretKey key = getKey(encryptedData.getKeyId());
        
        // Decode nonce and ciphertext
        byte[] nonce = Base64.getDecoder().decode(encryptedData.getNonce());
        byte[] ciphertext = Base64.getDecoder().decode(encryptedData.getCiphertext());
        
        // Perform decryption
        byte[] plaintext = performAuthenticatedDecryption(ciphertext, key, nonce);
        
        return new String(plaintext, StandardCharsets.UTF_8);
    }
    
    // Key rotation mechanism
    @Scheduled(cron = "0 0 2 * * SUN") // Weekly at 2 AM on Sundays
    public void rotateKeys() {
        List<String> contexts = keyManagementService.getAllContexts();
        
        for (String context : contexts) {
            // Generate new key
            EncryptionKey newKey = keyManagementService.generateNewKey(context);
            
            // Update key store
            keyManagementService.activateKey(newKey);
            
            // Re-encrypt sensitive data with new key (gradual process)
            reencryptDataWithNewKey(context, newKey);
        }
    }
    
    private void reencryptDataWithNewKey(String context, EncryptionKey newKey) {
        // Batch process data re-encryption to avoid performance impact
        int batchSize = 1000;
        int offset = 0;
        
        List<SensitiveDataRecord> records;
        do {
            records = dataRepository.findSensitiveDataByContext(context, offset, batchSize);
            
            for (SensitiveDataRecord record : records) {
                try {
                    // Decrypt with old key
                    String plaintext = decrypt(record.getEncryptedData(), context);
                    
                    // Encrypt with new key
                    String reencrypted = encrypt(plaintext, context);
                    
                    // Update record
                    record.setEncryptedData(reencrypted);
                    dataRepository.save(record);
                    
                } catch (Exception e) {
                    log.error("Failed to re-encrypt record: " + record.getId(), e);
                    // Add to failed queue for manual handling
                    failedReencryptionQueue.add(record.getId());
                }
            }
            
            offset += batchSize;
        } while (records.size() == batchSize);
    }
}

// Database activity monitoring and threat detection
@Component
public class DatabaseActivityMonitor {
    
    private final AuditLogRepository auditLogRepository;
    private final AlertService alertService;
    private final MachineLearningService mlService;
    
    // Monitor for suspicious query patterns
    @EventListener
    public void handleDatabaseQuery(DatabaseQueryEvent event) {
        // Check query complexity
        if (isComplexQuery(event.getQuery())) {
            logSuspiciousActivity(event, "Complex query detected");
        }
        
        // Check for potential injection attempts
        if (containsSuspiciousPatterns(event.getQuery())) {
            logSecurityThreat(event, "Potential SQL injection pattern detected");
        }
        
        // Check access patterns
        if (isUnusualAccessPattern(event)) {
            logAnomalousActivity(event, "Unusual access pattern detected");
        }
        
        // Apply machine learning for anomaly detection
        if (mlService.isAnomalousQuery(event)) {
            triggerSecurityAlert(event, "ML-detected anomalous query pattern");
        }
    }
    
    private boolean isComplexQuery(String query) {
        // Check for queries with excessive joins, subqueries, or unions
        String upperQuery = query.toUpperCase();
        
        long joinCount = upperQuery.split(" JOIN ").length - 1;
        long unionCount = upperQuery.split(" UNION ").length - 1;
        long subqueryCount = upperQuery.split("\\( SELECT ").length - 1;
        
        return joinCount > 10 || unionCount > 3 || subqueryCount > 5;
    }
    
    private boolean containsSuspiciousPatterns(String query) {
        // Check for common SQL injection patterns
        String upperQuery = query.toUpperCase();
        
        return upperQuery.contains(" OR 1=1") ||
               upperQuery.contains(" OR '1'='1'") ||
               upperQuery.contains(" DROP ") ||
               upperQuery.contains(" DELETE ") ||
               upperQuery.contains(" UPDATE ") ||
               upperQuery.contains(" EXEC ") ||
               query.contains(";") && !query.trim().endsWith(";");
    }
    
    private boolean isUnusualAccessPattern(DatabaseQueryEvent event) {
        String userId = event.getUserId();
        String tableName = extractTableName(event.getQuery());
        
        // Check if user is accessing tables they don't normally access
        Set<String> normalTables = getUserNormalAccessPatterns(userId);
        if (!normalTables.contains(tableName)) {
            return true;
        }
        
        // Check access frequency
        long accessCount = getRecentAccessCount(userId, tableName, Duration.ofHours(1));
        long normalAccessCount = getNormalAccessCount(userId, tableName);
        
        return accessCount > normalAccessCount * 5; // 5x normal access rate
    }
    
    private void triggerSecurityAlert(DatabaseQueryEvent event, String reason) {
        SecurityAlert alert = new SecurityAlert();
        alert.setEventType("DATABASE_SECURITY_THREAT");
        alert.setSeverity(AlertSeverity.HIGH);
        alert.setReason(reason);
        alert.setUserId(event.getUserId());
        alert.setQuery(event.getQuery());
        alert.setTimestamp(Instant.now());
        alert.setSourceIp(event.getSourceIp());
        
        alertService.sendAlert(alert);
        
        // Log for compliance and forensics
        auditLogRepository.save(createSecurityAuditLog(event, reason));
    }
}

// Database sharding implementation for horizontal scaling
@Component
public class ShardingStrategy {
    
    // Shard based on customer ID for even distribution
    public int determineShard(String customerId) {
        // Simple hash-based sharding
        return Math.abs(customerId.hashCode()) % getShardCount();
    }
    
    // Consistent hashing for better distribution and rebalancing
    public String determineShardConsistentHash(String key) {
        // Use consistent hashing algorithm
        return consistentHashRing.getNode(key).getShardId();
    }
    
    // Range-based sharding for time-series data
    public int determineShardByDate(LocalDate date) {
        // Shard by month/year for time-series data
        return (date.getYear() * 12 + date.getMonthValue()) % getShardCount();
    }
    
    // Shard routing for JPA repositories
    @Repository
    public class ShardedCustomerRepository {
        
        @PersistenceContext
        private EntityManager entityManager;
        
        public Customer findById(String customerId) {
            int shardId = determineShard(customerId);
            
            // Route to appropriate shard
            ShardRoutingContextHolder.setShardId(shardId);
            
            try {
                return entityManager.find(Customer.class, customerId);
            } finally {
                ShardRoutingContextHolder.clear();
            }
        }
        
        public List<Customer> findByRegion(String region) {
            // For region-based queries, may need to query multiple shards
            List<Customer> results = new ArrayList<>();
            
            for (int shardId = 0; shardId < getShardCount(); shardId++) {
                ShardRoutingContextHolder.setShardId(shardId);
                
                try {
                    String jpql = "SELECT c FROM Customer c WHERE c.region = :region";
                    List<Customer> shardResults = entityManager
                        .createQuery(jpql, Customer.class)
                        .setParameter("region", region)
                        .getResultList();
                        
                    results.addAll(shardResults);
                } finally {
                    ShardRoutingContextHolder.clear();
                }
            }
            
            return results;
        }
    }
}

// Database migration and versioning system
@Component
public class DatabaseMigrationService {
    
    private final List<MigrationScript> migrationScripts;
    private final MigrationRepository migrationRepository;
    
    @PostConstruct
    public void initializeMigrations() {
        // Load migration scripts in order
        migrationScripts = loadMigrationScripts();
    }
    
    public void applyMigrations() {
        // Get current database version
        int currentVersion = getCurrentDatabaseVersion();
        
        // Apply pending migrations
        for (MigrationScript script : migrationScripts) {
            if (script.getVersion() > currentVersion) {
                try {
                    // Execute migration
                    executeMigrationScript(script);
                    
                    // Record successful migration
                    recordSuccessfulMigration(script);
                    
                    log.info("Successfully applied migration: " + script.getName());
                    
                } catch (Exception e) {
                    log.error("Failed to apply migration: " + script.getName(), e);
                    
                    // Attempt rollback if possible
                    if (script.isRollbackSupported()) {
                        rollbackMigration(script);
                    }
                    
                    throw new MigrationException("Migration failed: " + script.getName(), e);
                }
            }
        }
    }
    
    private void executeMigrationScript(MigrationScript script) {
        // Execute DDL changes
        if (script.hasDdlChanges()) {
            for (String ddlStatement : script.getDdlStatements()) {
                entityManager.createNativeQuery(ddlStatement).executeUpdate();
            }
        }
        
        // Execute data migration
        if (script.hasDataChanges()) {
            script.getDataMigrationProcedure().accept(entityManager);
        }
        
        // Validate migration
        if (script.hasValidation()) {
            boolean isValid = script.getValidationProcedure().test(entityManager);
            if (!isValid) {
                throw new MigrationValidationException("Migration validation failed: " + script.getName());
            }
        }
    }
    
    // Zero-downtime migration strategy
    public void performZeroDowntimeMigration(MigrationScript script) {
        // Phase 1: Dual-write to old and new schemas
        enableDualWriteMode();
        
        // Phase 2: Backfill existing data
        backfillData(script);
        
        // Phase 3: Validate data consistency
        if (!validateDataConsistency()) {
            throw new MigrationException("Data consistency validation failed");
        }
        
        // Phase 4: Switch reads to new schema
        switchReadsToNewSchema();
        
        // Phase 5: Stop dual-write and cleanup
        disableDualWriteMode();
        cleanupOldSchema();
    }
}
```

## 📊 **Advanced Performance Optimization Patterns**

### **Scenario 7: Database Performance Optimization at Scale**
**Expected Answer**:
```java
// Advanced database optimization techniques
@Configuration
public class DatabaseOptimizationConfig {
    
    // Connection pool optimization for high-concurrency scenarios
    @Bean
    public DataSource optimizedDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/ecommerce");
        config.setUsername("user");
        config.setPassword("password");
        
        // Connection pool sizing based on CPU cores and workload
        int coreCount = Runtime.getRuntime().availableProcessors();
        config.setMaximumPoolSize(Math.max(10, coreCount * 4));
        config.setMinimumIdle(coreCount);
        config.setConnectionTimeout(5000); // 5 seconds
        config.setIdleTimeout(300000); // 5 minutes
        config.setMaxLifetime(1800000); // 30 minutes
        config.setLeakDetectionThreshold(60000); // 1 minute
        
        // Performance optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "500");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "4096");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        config.addDataSourceProperty("tcpKeepAlive", "true");
        config.addDataSourceProperty("socketTimeout", "30");
        
        return new HikariDataSource(config);
    }
    
    // Read-write splitting with intelligent routing
    @Bean
    public DataSource readWriteSplittingDataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("write", primaryDataSource());
        dataSourceMap.put("read1", readReplica1DataSource());
        dataSourceMap.put("read2", readReplica2DataSource());
        dataSourceMap.put("read3", readReplica3DataSource());
        
        ReadWriteSplittingDataSource routingDataSource = new ReadWriteSplittingDataSource();
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(primaryDataSource());
        
        return routingDataSource;
    }
}

// Intelligent read-write splitting with load balancing
public class ReadWriteSplittingDataSource extends AbstractRoutingDataSource {
    
    private final LoadBalancer loadBalancer = new RoundRobinLoadBalancer();
    private final List<String> readDataSourceKeys = Arrays.asList("read1", "read2", "read3");
    
    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceType dataSourceType = DataSourceContextHolder.getDataSourceType();
        
        if (dataSourceType == DataSourceType.WRITE) {
            return "write";
        } else {
            // Load balance between read replicas
            return loadBalancer.select(readDataSourceKeys);
        }
    }
    
    // Adaptive load balancing based on replica health and performance
    private class AdaptiveLoadBalancer implements LoadBalancer {
        private final Map<String, DataSourceMetrics> metrics = new ConcurrentHashMap<>();
        
        @Override
        public String select(List<String> candidates) {
            // Filter healthy replicas
            List<String> healthyReplicas = candidates.stream()
                .filter(this::isReplicaHealthy)
                .collect(Collectors.toList());
                
            if (healthyReplicas.isEmpty()) {
                // Fallback to primary if no healthy replicas
                return "write";
            }
            
            // Select based on performance metrics
            return healthyReplicas.stream()
                .min(Comparator.comparing(this::getReplicaLatency))
                .orElse(healthyReplicas.get(0));
        }
        
        private boolean isReplicaHealthy(String replicaKey) {
            DataSourceMetrics metrics = this.metrics.get(replicaKey);
            return metrics != null && 
                   metrics.getLastErrorTime().isBefore(Instant.now().minusSeconds(30)) &&
                   metrics.getLatencyAverage() < 1000; // 1 second threshold
        }
        
        private long getReplicaLatency(String replicaKey) {
            DataSourceMetrics metrics = this.metrics.get(replicaKey);
            return metrics != null ? metrics.getLatencyAverage() : Long.MAX_VALUE;
        }
    }
}

// Advanced caching strategies for database optimization
@Service
public class DatabaseCachingService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheManager cacheManager;
    private final CacheStatisticsService cacheStatsService;
    
    // Multi-level caching with different TTL strategies
    public <T> T getCachedEntity(String cacheKey, Class<T> entityType, 
                                Supplier<T> databaseLoader, CacheStrategy strategy) {
        // Level 1: In-memory cache (fastest)
        Cache l1Cache = cacheManager.getCache("l1-" + entityType.getSimpleName());
        if (l1Cache != null) {
            T cached = l1Cache.get(cacheKey, entityType);
            if (cached != null) {
                cacheStatsService.recordHit(CacheLevel.L1, entityType);
                return cached;
            }
        }
        
        // Level 2: Redis cache (distributed)
        try {
            T cached = (T) redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                cacheStatsService.recordHit(CacheLevel.L2, entityType);
                
                // Populate L1 cache
                if (l1Cache != null) {
                    l1Cache.put(cacheKey, cached);
                }
                
                return cached;
            }
        } catch (Exception e) {
            log.warn("Redis cache access failed", e);
        }
        
        // Cache miss - load from database
        cacheStatsService.recordMiss(entityType);
        T entity = databaseLoader.get();
        
        // Populate caches
        if (entity != null) {
            populateCaches(cacheKey, entity, strategy);
        }
        
        return entity;
    }
    
    private void populateCaches(String cacheKey, Object entity, CacheStrategy strategy) {
        // L1 cache population
        Cache l1Cache = cacheManager.getCache("l1-" + entity.getClass().getSimpleName());
        if (l1Cache != null) {
            l1Cache.put(cacheKey, entity);
        }
        
        // L2 cache population with appropriate TTL
        try {
            switch (strategy) {
                case SHORT_LIVED:
                    redisTemplate.opsForValue().set(cacheKey, entity, Duration.ofMinutes(5));
                    break;
                case MEDIUM_LIVED:
                    redisTemplate.opsForValue().set(cacheKey, entity, Duration.ofHours(1));
                    break;
                case LONG_LIVED:
                    redisTemplate.opsForValue().set(cacheKey, entity, Duration.ofDays(1));
                    break;
            }
        } catch (Exception e) {
            log.warn("Failed to populate Redis cache", e);
        }
    }
    
    // Cache warming for predictable access patterns
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void warmCaches() {
        // Identify frequently accessed entities
        List<CacheWarmingCandidate> candidates = cacheStatsService.getWarmingCandidates();
        
        for (CacheWarmingCandidate candidate : candidates) {
            try {
                // Pre-load into cache
                getCachedEntity(
                    candidate.getCacheKey(), 
                    candidate.getEntityType(), 
                    candidate.getDatabaseLoader(), 
                    candidate.getStrategy()
                );
                
                log.debug("Warmed cache for: " + candidate.getCacheKey());
            } catch (Exception e) {
                log.warn("Failed to warm cache for: " + candidate.getCacheKey(), e);
            }
        }
    }
    
    // Cache invalidation with proper consistency
    public void invalidateCache(String cacheKey, Object entity) {
        // Invalidate L1 cache
        Cache l1Cache = cacheManager.getCache("l1-" + entity.getClass().getSimpleName());
        if (l1Cache != null) {
            l1Cache.evict(cacheKey);
        }
        
        // Invalidate L2 cache
        try {
            redisTemplate.delete(cacheKey);
        } catch (Exception e) {
            log.warn("Failed to invalidate Redis cache", e);
        }
        
        // Invalidate related caches (cascade invalidation)
        invalidateRelatedCaches(entity);
    }
}

// Query optimization with execution plan analysis
@Repository
public class OptimizedQueryRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    // Query with execution plan analysis
    public List<OrderSummary> findOrderSummariesWithAnalysis(Customer customer, 
                                                           LocalDateTime startDate, 
                                                           LocalDateTime endDate) {
        String jpql = """
            SELECT new com.example.dto.OrderSummary(
                o.id, o.orderDate, o.totalAmount, o.status, 
                COUNT(oi), SUM(oi.quantity * oi.unitPrice)
            ) 
            FROM Order o 
            JOIN o.items oi 
            WHERE o.customer = :customer 
            AND o.orderDate BETWEEN :startDate AND :endDate 
            GROUP BY o.id, o.orderDate, o.totalAmount, o.status 
            ORDER BY o.orderDate DESC
            """;
            
        TypedQuery<OrderSummary> query = entityManager.createQuery(jpql, OrderSummary.class)
            .setParameter("customer", customer)
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .setMaxResults(100);
            
        // Analyze query execution plan
        QueryExecutionPlan plan = analyzeQueryPlan(query);
        logQueryPerformance(plan);
        
        return query.getResultList();
    }
    
    // Batch processing with cursor-based pagination
    @Transactional(readOnly = true)
    public void processOrdersInBatches(Consumer<List<Order>> batchProcessor, 
                                     LocalDateTime startDate, 
                                     LocalDateTime endDate) {
        // Use cursor-based pagination for large datasets
        String sql = """
            SELECT * FROM orders 
            WHERE order_date BETWEEN ? AND ? 
            AND id > ? 
            ORDER BY id 
            LIMIT ?
            """;
            
        Query query = entityManager.createNativeQuery(sql, Order.class);
        query.setParameter(1, startDate);
        query.setParameter(2, endDate);
        
        long lastProcessedId = 0;
        int batchSize = 1000;
        boolean hasMore = true;
        
        while (hasMore) {
            query.setParameter(3, lastProcessedId);
            query.setParameter(4, batchSize);
            
            List<Order> batch = query.getResultList();
            
            if (batch.isEmpty()) {
                hasMore = false;
            } else {
                // Process batch
                batchProcessor.accept(batch);
                
                // Update cursor
                lastProcessedId = batch.get(batch.size() - 1).getId();
                
                // Clear persistence context to avoid memory issues
                entityManager.clear();
            }
        }
    }
    
    // Materialized view for complex aggregations
    @Scheduled(cron = "0 */30 * * * *") // Every 30 minutes
    public void refreshOrderAnalyticsView() {
        String refreshSql = "REFRESH MATERIALIZED VIEW order_analytics_summary";
        
        try {
            entityManager.createNativeQuery(refreshSql).executeUpdate();
            log.info("Refreshed order analytics materialized view");
        } catch (Exception e) {
            log.error("Failed to refresh order analytics materialized view", e);
            
            // Fallback to incremental update
            updateOrderAnalyticsIncrementally();
        }
    }
}
```

## 🎯 **Database Design Best Practices Summary**

### **Advanced Design Principles**:
1. **Data Modeling**: Use domain-driven design principles for entity relationships
2. **Indexing Strategy**: Implement composite indexes for multi-column queries
3. **Partitioning**: Apply horizontal partitioning for large tables with clear partition keys
4. **Replication**: Configure read replicas for read-heavy workloads
5. **Sharding**: Implement consistent hashing for even data distribution
6. **Caching**: Design multi-level caching strategies with appropriate TTL
7. **Security**: Apply defense-in-depth with encryption, tokenization, and access controls
8. **Monitoring**: Implement comprehensive database monitoring and alerting

### **Performance Optimization Techniques**:
1. **Connection Management**: Optimize pool sizes based on workload characteristics
2. **Query Optimization**: Use execution plan analysis and indexing strategies
3. **Batch Processing**: Implement cursor-based pagination for large datasets
4. **Read-Write Splitting**: Route queries to appropriate database instances
5. **Materialized Views**: Pre-compute complex aggregations for faster access
6. **Query Caching**: Cache frequently accessed query results
7. **Database Tuning**: Configure database parameters for optimal performance
8. **Asynchronous Processing**: Handle non-critical operations in background

### **Scalability Patterns**:
1. **Vertical Scaling**: Optimize existing database resources before horizontal scaling
2. **Horizontal Scaling**: Implement sharding and replication strategies
3. **Database Federation**: Split databases by service or domain boundaries
4. **Caching Layers**: Reduce database load with application-level caching
5. **Data Archiving**: Move historical data to separate storage systems
6. **Microservices Data Patterns**: Database per service with event-driven consistency
7. **Load Balancing**: Distribute database connections and queries effectively
8. **Auto-scaling**: Implement dynamic scaling based on performance metrics

### **Security Implementation**:
1. **Data Encryption**: Apply field-level and transport encryption
2. **Access Controls**: Implement role-based and attribute-based access controls
3. **Audit Logging**: Maintain comprehensive logs of all database activities
4. **Threat Detection**: Monitor for suspicious activities and potential breaches
5. **Key Management**: Implement secure key rotation and management practices
6. **Compliance**: Ensure adherence to data protection regulations (GDPR, HIPAA, etc.)
7. **Network Security**: Use secure connections and network segmentation
8. **Backup Security**: Encrypt backups and implement secure recovery procedures
