# Enterprise Design Patterns Interview Questions (10+ Years Experience)

## 🏗️ **Architectural Patterns**

### **Question 1: Microservices Communication Patterns**
**Scenario**: Design communication patterns between 20+ microservices handling 1M+ transactions/day.

**Expected Answer**:
```java
// Event-driven architecture with CQRS
@Component
public class EventDrivenMicroservicePattern {
    
    // Command side - optimized for writes
    @RestController
    public class OrderCommandController {
        
        @PostMapping("/orders")
        public ResponseEntity<CommandResult> createOrder(@RequestBody CreateOrderCommand command) {
            // Validate command
            ValidationResult validation = orderValidator.validate(command);
            if (!validation.isValid()) {
                return ResponseEntity.badRequest().body(CommandResult.invalid(validation.getErrors()));
            }
            
            // Store command
            OrderAggregate order = new OrderAggregate(command);
            orderRepository.save(order);
            
            // Publish domain event
            DomainEvent event = new OrderCreatedEvent(order.getId(), order.getCustomerId(), 
                                                    order.getItems(), Instant.now());
            eventPublisher.publish(event);
            
            return ResponseEntity.accepted().body(CommandResult.success(order.getId()));
        }
        
        // Advanced: Idempotent command handling
        @PostMapping("/orders")
        public ResponseEntity<CommandResult> createOrderIdempotent(
                @RequestBody CreateOrderCommand command,
                @RequestHeader("Idempotency-Key") String idempotencyKey) {
            
            // Check if command was already processed
            Optional<CommandResult> existingResult = idempotencyService.getResult(idempotencyKey);
            if (existingResult.isPresent()) {
                return ResponseEntity.accepted().body(existingResult.get());
            }
            
            // Process command
            CommandResult result = processCreateOrder(command);
            
            // Store result for idempotency
            idempotencyService.storeResult(idempotencyKey, result);
            
            return ResponseEntity.accepted().body(result);
        }
    }
    
    // Query side - optimized for reads
    @RestController
    public class OrderQueryController {
        
        @GetMapping("/orders/{id}")
        public ResponseEntity<OrderView> getOrder(@PathVariable String id) {
            return orderViewRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        }
        
        // Advanced: Cached query with versioning
        @GetMapping("/orders/{id}")
        public ResponseEntity<OrderView> getOrderCached(
                @PathVariable String id,
                @RequestHeader(value = "If-None-Match", required = false) String etag) {
            
            OrderView view = orderViewRepository.findById(id);
            if (view == null) {
                return ResponseEntity.notFound().build();
            }
            
            String currentEtag = generateETag(view);
            if (etag != null && etag.equals(currentEtag)) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .header("ETag", currentEtag)
                    .build();
            }
            
            return ResponseEntity.ok()
                .header("ETag", currentEtag)
                .body(view);
        }
        
        private String generateETag(OrderView view) {
            return "\"" + view.getVersion() + "-" + view.getLastModified().toEpochMilli() + "\"";
        }
    }
    
    // Event handlers for cross-service communication
    @EventHandler
    public class OrderEventHandler {
        
        @Async("eventProcessingExecutor")
        public void handle(OrderCreatedEvent event) {
            // Update read model
            OrderView view = OrderView.from(event);
            orderViewRepository.save(view);
            
            // Trigger downstream processes
            inventoryService.reserveItems(event.getOrderId(), event.getItems());
            paymentService.processPayment(event.getOrderId(), event.getTotal());
            notificationService.sendOrderConfirmation(event.getCustomerId(), event.getOrderId());
        }
        
        // Advanced: Event sourcing with snapshotting
        @EventHandler
        public void handleWithSnapshot(OrderCreatedEvent event) {
            // Apply event to aggregate
            OrderAggregate aggregate = orderRepository.load(event.getOrderId());
            aggregate.apply(event);
            
            // Create snapshot if needed
            if (aggregate.getVersion() % 100 == 0) { // Every 100 events
                orderRepository.saveSnapshot(aggregate);
            }
            
            // Save event
            orderRepository.saveEvent(event);
        }
    }
    
    // Advanced: Circuit breaker pattern for resilience
    @Component
    public class ResilientOrderService {
        
        private final CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("orderService");
        
        public OrderView getOrderResilient(String orderId) {
            Supplier<OrderView> orderSupplier = () -> {
                // This might fail
                return orderService.getOrder(orderId);
            };
            
            return circuitBreaker.executeSupplier(orderSupplier);
        }
        
        // Advanced: Bulkhead pattern for resource isolation
        private final Bulkhead bulkhead = Bulkhead.ofDefaults("orderService");
        
        public CompletableFuture<OrderView> getOrderBulkhead(String orderId) {
            Supplier<CompletionStage<OrderView>> supplier = () -> 
                CompletableFuture.supplyAsync(() -> orderService.getOrder(orderId));
                
            return bulkhead.executeSupplier(supplier)
                .toCompletableFuture();
        }
    }
}

// Saga pattern for distributed transactions
@Component
public class OrderSaga {
    
    private final SagaManager sagaManager;
    
    public void processOrder(OrderCreatedEvent event) {
        SagaDefinition saga = SagaDefinition.builder()
            .step("reserveInventory")
                .action(() -> inventoryService.reserve(event.getItems()))
                .compensate(() -> inventoryService.release(event.getItems()))
            .step("processPayment")
                .action(() -> paymentService.charge(event.getTotal()))
                .compensate(() -> paymentService.refund(event.getTotal()))
            .step("updateShipping")
                .action(() -> shippingService.schedule(event.getOrderId()))
                .compensate(() -> shippingService.cancel(event.getOrderId()))
            .build();
            
        sagaManager.execute(saga, event.getOrderId());
    }
    
    // Advanced: Saga with timeout and retry logic
    public void processOrderWithTimeout(OrderCreatedEvent event) {
        SagaDefinition saga = SagaDefinition.builder()
            .step("reserveInventory")
                .action(() -> withTimeout(inventoryService::reserve, event.getItems(), Duration.ofSeconds(30)))
                .compensate(() -> withRetry(inventoryService::release, event.getItems(), 3))
                .timeout(Duration.ofMinutes(5))
            .step("processPayment")
                .action(() -> withCircuitBreaker(paymentService::charge, event.getTotal()))
                .compensate(() -> withExponentialBackoff(paymentService::refund, event.getTotal()))
                .timeout(Duration.ofMinutes(2))
            .build();
            
        sagaManager.execute(saga, event.getOrderId());
    }
    
    private <T> T withTimeout(Supplier<T> operation, T param, Duration timeout) {
        // Implementation with timeout logic
        return operation.get();
    }
    
    private <T> T withRetry(Supplier<T> operation, T param, int maxRetries) {
        // Implementation with retry logic
        return operation.get();
    }
    
    private <T> T withCircuitBreaker(Supplier<T> operation, T param) {
        // Implementation with circuit breaker
        return operation.get();
    }
    
    private <T> T withExponentialBackoff(Supplier<T> operation, T param) {
        // Implementation with exponential backoff
        return operation.get();
    }
}

// Advanced: Event sourcing implementation
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
    
    // Advanced: Event metadata for tracing
    private String correlationId;
    private String causationId;
    private String source;
    
    // Advanced: Event validation
    public boolean isValid() {
        return orderId != null && eventType != null && eventData != null && timestamp != null;
    }
}

// Advanced: CQRS with event sourcing repository
@Repository
public class EventSourcedOrderRepository {
    
    private final EventStore eventStore;
    private final SnapshotStore snapshotStore;
    
    public OrderAggregate load(String orderId) {
        // Try to load from snapshot first
        Optional<Snapshot> snapshot = snapshotStore.loadLatestSnapshot(orderId);
        
        if (snapshot.isPresent()) {
            OrderAggregate aggregate = snapshot.get().getAggregate();
            long snapshotVersion = snapshot.get().getVersion();
            
            // Load events after snapshot
            List<OrderEvent> events = eventStore.loadEventsAfterVersion(orderId, snapshotVersion);
            aggregate.replay(events);
            
            return aggregate;
        } else {
            // Load all events from beginning
            List<OrderEvent> events = eventStore.loadEvents(orderId);
            OrderAggregate aggregate = new OrderAggregate();
            aggregate.replay(events);
            
            return aggregate;
        }
    }
    
    public void save(OrderAggregate aggregate) {
        // Save new events
        List<OrderEvent> uncommittedEvents = aggregate.getUncommittedEvents();
        eventStore.saveEvents(uncommittedEvents);
        
        // Clear uncommitted events
        aggregate.clearUncommittedEvents();
        
        // Create snapshot if needed
        if (aggregate.getVersion() % 100 == 0) {
            snapshotStore.saveSnapshot(new Snapshot(aggregate));
        }
    }
}
```

**Advanced Follow-up Questions**:
- How do you handle eventual consistency in event-driven architectures?
- What are the challenges of implementing CQRS in a microservices environment?
- How do you ensure idempotency in distributed event processing?
- What are the trade-offs between event sourcing and traditional CRUD approaches?
- How do you handle schema evolution in event-driven systems?
- How do you implement distributed tracing in event-driven microservices?

---

### **Question 2: Domain-Driven Design Implementation**
**Expected Answer**:
```java
// Bounded Context implementation
@Entity
@Table(name = "orders")
public class OrderAggregate {
    
    @EmbeddedId
    private OrderId id;
    
    @Embedded
    private CustomerId customerId;
    
    @ElementCollection
    @CollectionTable(name = "order_items")
    private List<OrderItem> items = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    @Embedded
    private Money totalAmount;
    
    // Version for optimistic locking
    @Version
    private Long version;
    
    // Domain events
    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    // Domain logic encapsulated in aggregate
    public void addItem(ProductId productId, Quantity quantity, Money unitPrice) {
        if (status != OrderStatus.DRAFT) {
            throw new DomainException("Cannot add items to non-draft order");
        }
        
        OrderItem item = new OrderItem(productId, quantity, unitPrice);
        items.add(item);
        recalculateTotal();
        
        // Raise domain event
        registerEvent(new OrderItemAddedEvent(id, productId, quantity));
    }
    
    public void confirm() {
        if (items.isEmpty()) {
            throw new DomainException("Cannot confirm empty order");
        }
        
        if (status != OrderStatus.DRAFT) {
            throw new DomainException("Order already confirmed");
        }
        
        status = OrderStatus.CONFIRMED;
        
        // Raise domain event
        registerEvent(new OrderConfirmedEvent(this.id));
    }
    
    public void cancel() {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED) {
            throw new DomainException("Cannot cancel shipped order");
        }
        
        status = OrderStatus.CANCELLED;
        
        // Raise domain event
        registerEvent(new OrderCancelledEvent(this.id));
    }
    
    private void recalculateTotal() {
        this.totalAmount = items.stream()
            .map(item -> item.getUnitPrice().multiply(item.getQuantity().getValue()))
            .reduce(Money.ZERO, Money::add);
    }
    
    // Event handling
    public void apply(OrderItemAddedEvent event) {
        // Apply event to rebuild state
        // This is used when replaying events
    }
    
    // Domain event registration
    protected void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }
    
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
    
    public void clearDomainEvents() {
        domainEvents.clear();
    }
}

// Value objects with validation
@Embeddable
public class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO, Currency.getInstance("USD"));
    
    @Column(name = "amount")
    private BigDecimal amount;
    
    @Column(name = "currency")
    private Currency currency;
    
    // Private constructor for JPA
    private Money() {}
    
    // Compact constructor with validation
    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Amount and currency cannot be null");
        }
        
        if (amount.scale() > currency.getDefaultFractionDigits()) {
            throw new IllegalArgumentException("Amount has too many decimal places for currency");
        }
        
        this.amount = amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP);
        this.currency = currency;
    }
    
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        
        return new Money(this.amount.add(other.amount), this.currency);
    }
    
    public Money multiply(int multiplier) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), this.currency);
    }
    
    public boolean isGreaterThanOrEqualTo(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare different currencies");
        }
        
        return this.amount.compareTo(other.amount) >= 0;
    }
    
    // Getters
    public BigDecimal getAmount() { return amount; }
    public Currency getCurrency() { return currency; }
}

// Repository pattern with domain focus
public interface OrderRepository {
    OrderAggregate findById(OrderId id);
    void save(OrderAggregate order);
    List<OrderAggregate> findByCustomerId(CustomerId customerId);
    
    // Advanced: Specification pattern for complex queries
    List<OrderAggregate> findBySpecification(OrderSpecification specification);
    
    // Advanced: Pagination support
    Page<OrderAggregate> findByCustomerId(CustomerId customerId, Pageable pageable);
}

// Advanced: Specification pattern implementation
public class OrderSpecification {
    private final CustomerId customerId;
    private final OrderStatus status;
    private final LocalDateTime dateFrom;
    private final LocalDateTime dateTo;
    private final BigDecimal minAmount;
    
    // Private constructor
    private OrderSpecification(Builder builder) {
        this.customerId = builder.customerId;
        this.status = builder.status;
        this.dateFrom = builder.dateFrom;
        this.dateTo = builder.dateTo;
        this.minAmount = builder.minAmount;
    }
    
    // Builder pattern
    public static class Builder {
        private CustomerId customerId;
        private OrderStatus status;
        private LocalDateTime dateFrom;
        private LocalDateTime dateTo;
        private BigDecimal minAmount;
        
        public Builder withCustomerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }
        
        public Builder withStatus(OrderStatus status) {
            this.status = status;
            return this;
        }
        
        public Builder withDateRange(LocalDateTime from, LocalDateTime to) {
            this.dateFrom = from;
            this.dateTo = to;
            return this;
        }
        
        public Builder withMinAmount(BigDecimal minAmount) {
            this.minAmount = minAmount;
            return this;
        }
        
        public OrderSpecification build() {
            return new OrderSpecification(this);
        }
    }
    
    // Getters
    public CustomerId getCustomerId() { return customerId; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getDateFrom() { return dateFrom; }
    public LocalDateTime getDateTo() { return dateTo; }
    public BigDecimal getMinAmount() { return minAmount; }
}

// Domain service for complex business logic
@DomainService
public class PricingService {
    
    public Money calculateOrderTotal(List<OrderItem> items, CustomerId customerId) {
        Money subtotal = items.stream()
            .map(this::calculateItemTotal)
            .reduce(Money.ZERO, Money::add);
            
        Discount discount = discountPolicy.calculateDiscount(customerId, subtotal);
        Tax tax = taxCalculator.calculateTax(subtotal.subtract(discount.getAmount()));
        
        return subtotal.subtract(discount.getAmount()).add(tax.getAmount());
    }
    
    private Money calculateItemTotal(OrderItem item) {
        return item.getUnitPrice().multiply(item.getQuantity().getValue());
    }
    
    // Advanced: Dynamic pricing based on demand
    public Money calculateDynamicPrice(ProductId productId, int quantity, LocalDateTime orderTime) {
        BigDecimal basePrice = priceRepository.getBasePrice(productId);
        BigDecimal demandMultiplier = demandService.getDemandMultiplier(productId, orderTime);
        BigDecimal timeMultiplier = timeService.getTimeMultiplier(orderTime);
        
        BigDecimal dynamicPrice = basePrice
            .multiply(demandMultiplier)
            .multiply(timeMultiplier);
            
        return new Money(dynamicPrice, Currency.getInstance("USD"));
    }
}

// Advanced: Aggregate factory for complex creation logic
@Component
public class OrderFactory {
    
    private final SequenceGenerator sequenceGenerator;
    private final CustomerService customerService;
    
    public OrderAggregate createOrder(CustomerId customerId, List<OrderItemRequest> itemRequests) {
        // Validate customer
        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            throw new DomainException("Customer not found: " + customerId);
        }
        
        // Validate items
        List<OrderItem> items = itemRequests.stream()
            .map(this::createOrderItem)
            .collect(Collectors.toList());
            
        if (items.isEmpty()) {
            throw new DomainException("Order must have at least one item");
        }
        
        // Create order ID
        OrderId orderId = new OrderId(sequenceGenerator.nextOrderId());
        
        // Create aggregate
        OrderAggregate order = new OrderAggregate();
        order.setId(orderId);
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.DRAFT);
        
        // Add items
        items.forEach(item -> order.addItem(item.getProductId(), item.getQuantity(), item.getUnitPrice()));
        
        return order;
    }
    
    private OrderItem createOrderItem(OrderItemRequest request) {
        Product product = productRepository.findById(request.getProductId());
        if (product == null) {
            throw new DomainException("Product not found: " + request.getProductId());
        }
        
        if (request.getQuantity().getValue() <= 0) {
            throw new DomainException("Quantity must be positive");
        }
        
        return new OrderItem(
            request.getProductId(),
            request.getQuantity(),
            product.getPrice()
        );
    }
}
```

**Advanced Follow-up Questions**:
- How do you handle distributed aggregates across multiple services?
- What are the challenges of implementing event sourcing with DDD?
- How do you ensure consistency between aggregates in the same bounded context?
- How do you handle cross-cutting concerns like security and auditing in DDD?
- What are the best practices for designing value objects?
- How do you implement domain events in a distributed system?

---

## 🔄 **Behavioral Patterns**

### **Question 3: Strategy Pattern with Dynamic Loading**
**Expected Answer**:
```java
// Strategy pattern with plugin architecture
public interface PaymentStrategy {
    PaymentResult process(PaymentRequest request);
    boolean supports(PaymentMethod method);
    String getName();
    
    // Advanced: Configuration support
    default void configure(Map<String, String> config) {
        // Default implementation
    }
    
    // Advanced: Health check
    default boolean isHealthy() {
        return true;
    }
    
    // Advanced: Metrics
    default PaymentMetrics getMetrics() {
        return PaymentMetrics.EMPTY;
    }
}

@Component
public class PaymentStrategyRegistry {
    
    private final Map<PaymentMethod, PaymentStrategy> strategies = new ConcurrentHashMap<>();
    private final ApplicationContext applicationContext;
    
    @PostConstruct
    public void loadStrategies() {
        // Load from Spring context
        applicationContext.getBeansOfType(PaymentStrategy.class)
            .values()
            .forEach(this::registerStrategy);
            
        // Load dynamic plugins
        loadPlugins();
    }
    
    public void registerStrategy(PaymentStrategy strategy) {
        // Register all supported payment methods
        for (PaymentMethod method : PaymentMethod.values()) {
            if (strategy.supports(method)) {
                strategies.put(method, strategy);
            }
        }
        
        logger.info("Registered payment strategy: {}", strategy.getName());
    }
    
    // Advanced: Dynamic plugin loading
    private void loadPlugins() {
        try {
            Path pluginDir = Paths.get("plugins");
            if (Files.exists(pluginDir)) {
                Files.list(pluginDir)
                    .filter(path -> path.toString().endsWith(".jar"))
                    .forEach(this::loadPlugin);
            }
        } catch (IOException e) {
            logger.warn("Failed to load plugins", e);
        }
    }
    
    private void loadPlugin(Path pluginPath) {
        try {
            URLClassLoader classLoader = new URLClassLoader(
                new URL[]{pluginPath.toUri().toURL()},
                getClass().getClassLoader()
            );
            
            // Load plugin descriptor
            Properties pluginDescriptor = loadPluginDescriptor(classLoader);
            
            // Instantiate strategy
            String strategyClass = pluginDescriptor.getProperty("strategy.class");
            Class<?> clazz = classLoader.loadClass(strategyClass);
            PaymentStrategy strategy = (PaymentStrategy) clazz.getDeclaredConstructor().newInstance();
            
            // Configure strategy
            Map<String, String> config = loadPluginConfig(pluginDescriptor);
            strategy.configure(config);
            
            // Register strategy
            registerStrategy(strategy);
            
            logger.info("Loaded plugin: {}", pluginPath.getFileName());
        } catch (Exception e) {
            logger.error("Failed to load plugin: {}", pluginPath, e);
        }
    }
    
    public PaymentStrategy getStrategy(PaymentMethod method) {
        PaymentStrategy strategy = strategies.get(method);
        if (strategy == null) {
            throw new PaymentException("No strategy found for payment method: " + method);
        }
        
        // Check health before returning
        if (!strategy.isHealthy()) {
            throw new PaymentException("Strategy is not healthy: " + strategy.getName());
        }
        
        return strategy;
    }
    
    // Advanced: Strategy selection with fallback
    public PaymentStrategy getStrategyWithFallback(PaymentMethod method) {
        PaymentStrategy primary = strategies.get(method);
        
        if (primary != null && primary.isHealthy()) {
            return primary;
        }
        
        // Try fallback strategies
        for (PaymentStrategy strategy : strategies.values()) {
            if (strategy.supports(method) && strategy.isHealthy()) {
                logger.warn("Using fallback strategy: {} for method: {}", 
                           strategy.getName(), method);
                return strategy;
            }
        }
        
        throw new PaymentException("No healthy strategy found for payment method: " + method);
    }
    
    // Advanced: Metrics collection
    @ManagedOperation
    public String getStrategyMetrics() {
        return strategies.values().stream()
            .map(strategy -> strategy.getName() + ": " + strategy.getMetrics())
            .collect(Collectors.joining(", "));
    }
    
    // Advanced: Runtime strategy reloading
    @ManagedOperation
    public void reloadStrategies() {
        strategies.clear();
        loadStrategies();
        logger.info("Reloaded all payment strategies");
    }
}

// Advanced: Composite strategy for complex payment processing
public class CompositePaymentStrategy implements PaymentStrategy {
    
    private final List<PaymentStrategy> strategies;
    
    public CompositePaymentStrategy(List<PaymentStrategy> strategies) {
        this.strategies = new ArrayList<>(strategies);
    }
    
    @Override
    public PaymentResult process(PaymentRequest request) {
        PaymentResult result = null;
        
        for (PaymentStrategy strategy : strategies) {
            try {
                result = strategy.process(request);
                if (result.isSuccess()) {
                    break; // Stop on first success
                }
            } catch (Exception e) {
                logger.warn("Strategy {} failed: {}", strategy.getName(), e.getMessage());
                // Continue with next strategy
            }
        }
        
        if (result == null || !result.isSuccess()) {
            throw new PaymentException("All payment strategies failed");
        }
        
        return result;
    }
    
    @Override
    public boolean supports(PaymentMethod method) {
        return strategies.stream().anyMatch(strategy -> strategy.supports(method));
    }
    
    @Override
    public String getName() {
        return "Composite(" + strategies.stream()
            .map(PaymentStrategy::getName)
            .collect(Collectors.joining(",")) + ")";
    }
}

// Advanced: State pattern for payment processing
public class PaymentProcessor {
    
    private PaymentState state;
    
    public PaymentProcessor() {
        this.state = new IdleState();
    }
    
    public void processPayment(PaymentRequest request) {
        state = state.processPayment(request, this);
    }
    
    public void setState(PaymentState state) {
        this.state = state;
    }
    
    // States
    interface PaymentState {
        PaymentState processPayment(PaymentRequest request, PaymentProcessor processor);
    }
    
    static class IdleState implements PaymentState {
        @Override
        public PaymentState processPayment(PaymentRequest request, PaymentProcessor processor) {
            logger.info("Processing payment: {}", request.getPaymentId());
            // Validate request
            if (request.getAmount().isGreaterThanOrEqualTo(Money.ZERO)) {
                processor.setState(new ValidatingState());
                return new ValidatingState();
            } else {
                processor.setState(new ErrorState("Invalid amount"));
                return new ErrorState("Invalid amount");
            }
        }
    }
    
    static class ValidatingState implements PaymentState {
        @Override
        public PaymentState processPayment(PaymentRequest request, PaymentProcessor processor) {
            // Perform validation
            if (validatePayment(request)) {
                processor.setState(new ProcessingState());
                return new ProcessingState();
            } else {
                processor.setState(new ErrorState("Validation failed"));
                return new ErrorState("Validation failed");
            }
        }
        
        private boolean validatePayment(PaymentRequest request) {
            // Validation logic
            return true;
        }
    }
    
    static class ProcessingState implements PaymentState {
        @Override
        public PaymentState processPayment(PaymentRequest request, PaymentProcessor processor) {
            try {
                PaymentStrategy strategy = strategyRegistry.getStrategy(request.getPaymentMethod());
                PaymentResult result = strategy.process(request);
                
                if (result.isSuccess()) {
                    processor.setState(new CompletedState(result));
                    return new CompletedState(result);
                } else {
                    processor.setState(new ErrorState(result.getErrorMessage()));
                    return new ErrorState(result.getErrorMessage());
                }
            } catch (Exception e) {
                processor.setState(new ErrorState(e.getMessage()));
                return new ErrorState(e.getMessage());
            }
        }
    }
    
    static class CompletedState implements PaymentState {
        private final PaymentResult result;
        
        public CompletedState(PaymentResult result) {
            this.result = result;
        }
        
        @Override
        public PaymentState processPayment(PaymentRequest request, PaymentProcessor processor) {
            logger.info("Payment already completed: {}", result.getPaymentId());
            return this;
        }
    }
    
    static class ErrorState implements PaymentState {
        private final String errorMessage;
        
        public ErrorState(String errorMessage) {
            this.errorMessage = errorMessage;
        }
        
        @Override
        public PaymentState processPayment(PaymentRequest request, PaymentProcessor processor) {
            logger.error("Payment processing error: {}", errorMessage);
            return this;
        }
    }
}
```

**Advanced Follow-up Questions**:
- How do you handle versioning in plugin-based architectures?
- What are the security considerations for dynamic class loading?
- How do you implement circuit breaker patterns with strategy patterns?
- How do you monitor and manage multiple payment strategies in production?
- What are the best practices for designing plugin interfaces?
- How do you handle transaction management across multiple strategy executions?

---

### **Question 4: Advanced Observer Pattern with Event Sourcing**
**Expected Answer**:
```java
// Advanced event system with observer pattern
public interface DomainEvent {
    String getEventType();
    LocalDateTime getTimestamp();
    String getAggregateId();
    
    // Advanced: Metadata support
    default Map<String, Object> getMetadata() {
        return Collections.emptyMap();
    }
    
    // Advanced: Serialization support
    default String toJson() {
        // Implementation for JSON serialization
        return "{}";
    }
}

// Event publisher with advanced features
@Component
public class AdvancedEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    private final EventStore eventStore;
    private final List<DomainEventListener> globalListeners = new CopyOnWriteArrayList<>();
    
    public void publish(DomainEvent event) {
        // Store event
        eventStore.save(event);
        
        // Publish to Spring event system
        eventPublisher.publishEvent(event);
        
        // Notify global listeners
        notifyGlobalListeners(event);
        
        // Advanced: Publish to external systems
        publishToExternalSystems(event);
    }
    
    private void notifyGlobalListeners(DomainEvent event) {
        for (DomainEventListener listener : globalListeners) {
            try {
                if (listener.supports(event)) {
                    listener.handle(event);
                }
            } catch (Exception e) {
                logger.error("Global listener failed: {}", listener.getClass().getName(), e);
                // Continue with other listeners
            }
        }
    }
    
    private void publishToExternalSystems(DomainEvent event) {
        // Publish to message queue
        messageQueue.publish("domain-events", event.toJson());
        
        // Publish to webhook endpoints
        webhookService.notifySubscribers(event);
        
        // Publish to analytics system
        analyticsService.trackEvent(event);
    }
    
    public void registerGlobalListener(DomainEventListener listener) {
        globalListeners.add(listener);
    }
    
    public void unregisterGlobalListener(DomainEventListener listener) {
        globalListeners.remove(listener);
    }
    
    // Advanced: Async event processing with ordering guarantees
    public void publishAsync(DomainEvent event) {
        CompletableFuture.runAsync(() -> publish(event))
            .exceptionally(throwable -> {
                logger.error("Async event processing failed: {}", event.getEventType(), throwable);
                // Handle failure (retry, dead letter queue, etc.)
                handleEventFailure(event, throwable);
                return null;
            });
    }
    
    private void handleEventFailure(DomainEvent event, Throwable throwable) {
        // Store failed event for retry
        failedEventStore.save(new FailedEvent(event, throwable));
        
        // Notify monitoring system
        monitoringService.alert("Event processing failed: " + event.getEventType());
    }
}

// Advanced event listener with filtering and batching
@Component
public class AdvancedEventListener implements DomainEventListener {
    
    private final EventProcessingService processingService;
    private final Queue<DomainEvent> eventBuffer = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    @PostConstruct
    public void init() {
        // Schedule batch processing
        scheduler.scheduleAtFixedRate(this::processBatch, 0, 1, TimeUnit.SECONDS);
    }
    
    @Override
    public boolean supports(DomainEvent event) {
        return "OrderCreatedEvent".equals(event.getEventType()) ||
               "OrderCancelledEvent".equals(event.getEventType());
    }
    
    @Override
    public void handle(DomainEvent event) {
        // Buffer event for batch processing
        eventBuffer.offer(event);
    }
    
    private void processBatch() {
        List<DomainEvent> batch = new ArrayList<>();
        DomainEvent event;
        
        // Collect events for batch processing
        while ((event = eventBuffer.poll()) != null) {
            batch.add(event);
        }
        
        if (!batch.isEmpty()) {
            try {
                processingService.processEvents(batch);
            } catch (Exception e) {
                logger.error("Batch processing failed", e);
                // Re-queue failed events
                batch.forEach(eventBuffer::offer);
            }
        }
    }
    
    // Advanced: Event filtering with complex conditions
    public void handleWithFiltering(DomainEvent event) {
        // Apply complex filtering logic
        if (shouldProcessEvent(event)) {
            handle(event);
        }
    }
    
    private boolean shouldProcessEvent(DomainEvent event) {
        // Complex filtering logic
        Map<String, Object> metadata = event.getMetadata();
        String region = (String) metadata.get("region");
        String priority = (String) metadata.get("priority");
        
        // Only process high priority events from specific regions
        return "high".equals(priority) && 
               ("us-east-1".equals(region) || "eu-west-1".equals(region));
    }
    
    // Advanced: Event transformation pipeline
    public void handleWithTransformation(DomainEvent event) {
        // Transform event
        DomainEvent transformedEvent = transformEvent(event);
        
        // Process transformed event
        processingService.processEvent(transformedEvent);
    }
    
    private DomainEvent transformEvent(DomainEvent event) {
        // Transformation logic
        if (event instanceof OrderCreatedEvent) {
            OrderCreatedEvent orderEvent = (OrderCreatedEvent) event;
            return new EnrichedOrderCreatedEvent(
                orderEvent,
                customerService.getCustomerDetails(orderEvent.getCustomerId()),
                inventoryService.getInventoryLevels(orderEvent.getItems())
            );
        }
        
        return event;
    }
}

// Advanced: Event sourcing with snapshotting
@Entity
@Table(name = "event_store")
public class StoredEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String aggregateId;
    
    @Column(nullable = false)
    private String eventType;
    
    @Column(length = 10000, nullable = false)
    private String eventData;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column
    private Long version;
    
    @Column(length = 1000)
    private String metadata;
    
    // Advanced: Compression for large events
    @Column(length = 10000)
    private String compressedEventData;
    
    // Advanced: Encryption for sensitive events
    @Column(length = 10000)
    private String encryptedEventData;
    
    // Getters and setters
}

// Advanced: Event store with advanced querying capabilities
@Repository
public class AdvancedEventStore {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public void save(DomainEvent event) {
        StoredEvent storedEvent = new StoredEvent();
        storedEvent.setAggregateId(event.getAggregateId());
        storedEvent.setEventType(event.getEventType());
        storedEvent.setTimestamp(event.getTimestamp());
        storedEvent.setEventData(serializeEvent(event));
        
        // Advanced: Add metadata
        storedEvent.setMetadata(serializeMetadata(event.getMetadata()));
        
        // Advanced: Compress large events
        if (storedEvent.getEventData().length() > 1000) {
            storedEvent.setCompressedEventData(compress(storedEvent.getEventData()));
            storedEvent.setEventData(null); // Clear uncompressed data
        }
        
        entityManager.persist(storedEvent);
    }
    
    public List<DomainEvent> findByAggregateId(String aggregateId) {
        TypedQuery<StoredEvent> query = entityManager.createQuery(
            "SELECT e FROM StoredEvent e WHERE e.aggregateId = :aggregateId ORDER BY e.timestamp",
            StoredEvent.class
        );
        query.setParameter("aggregateId", aggregateId);
        
        return query.getResultList().stream()
            .map(this::deserializeEvent)
            .collect(Collectors.toList());
    }
    
    // Advanced: Query by event type and time range
    public List<DomainEvent> findByTypeAndTimeRange(String eventType, 
                                                   LocalDateTime from, 
                                                   LocalDateTime to) {
        TypedQuery<StoredEvent> query = entityManager.createQuery(
            "SELECT e FROM StoredEvent e WHERE e.eventType = :eventType " +
            "AND e.timestamp BETWEEN :from AND :to ORDER BY e.timestamp",
            StoredEvent.class
        );
        query.setParameter("eventType", eventType);
        query.setParameter("from", from);
        query.setParameter("to", to);
        
        return query.getResultList().stream()
            .map(this::deserializeEvent)
            .collect(Collectors.toList());
    }
    
    // Advanced: Stream events for real-time processing
    public Stream<DomainEvent> streamEvents(String eventType, LocalDateTime from) {
        // Implementation for streaming events
        return Stream.empty();
    }
    
    private String serializeEvent(DomainEvent event) {
        // Serialization logic
        return "{}";
    }
    
    private DomainEvent deserializeEvent(StoredEvent storedEvent) {
        // Deserialization logic
        return null;
    }
    
    private String serializeMetadata(Map<String, Object> metadata) {
        // Metadata serialization
        return "{}";
    }
    
    private String compress(String data) {
        // Compression logic
        return data;
    }
}
```

**Advanced Follow-up Questions**:
- How do you handle event versioning in event sourcing systems?
- What are the challenges of implementing distributed event sourcing?
- How do you ensure ordering guarantees in distributed event systems?
- How do you implement event replay and projection rebuilding?
- What are the best practices for event schema evolution?
- How do you handle event deduplication in distributed systems?

---