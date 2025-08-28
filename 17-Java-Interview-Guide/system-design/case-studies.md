# System Design Case Studies for Senior Java Developers (10+ Years Experience)

## 🏢 **Enterprise Architecture Case Studies**

### **Case Study 1: Migrating Monolith to Microservices**
**Scenario**: A large e-commerce company with a 10-year-old monolithic application needs to migrate to a microservices architecture to improve scalability and development velocity.

**Problem Analysis**:
```java
// Domain analysis and service decomposition
public class ECommerceDomainAnalysis {
    
    // Identify bounded contexts
    public enum BoundedContext {
        USER_MANAGEMENT,
        PRODUCT_CATALOG,
        ORDER_MANAGEMENT,
        PAYMENT_PROCESSING,
        INVENTORY_MANAGEMENT,
        CUSTOMER_SUPPORT,
        ANALYTICS,
        NOTIFICATION_SERVICE
    }
    
    // Service decomposition strategy
    public class ServiceDecompositionPlan {
        private final Map<BoundedContext, ServiceDefinition> services;
        private final List<InterContextDependency> dependencies;
        private final MigrationTimeline timeline;
        
        public ServiceDecompositionPlan() {
            this.services = new HashMap<>();
            this.dependencies = new ArrayList<>();
            this.timeline = new MigrationTimeline();
            
            // Define service boundaries
            defineServiceBoundaries();
            // Map dependencies
            mapDependencies();
            // Create migration plan
            createMigrationPlan();
        }
        
        private void defineServiceBoundaries() {
            services.put(BoundedContext.USER_MANAGEMENT, 
                new ServiceDefinition("user-service", 
                    Arrays.asList("User registration", "Authentication", "Profile management"),
                    Arrays.asList("MySQL", "Redis"),
                    ServiceType.CRUD));
                    
            services.put(BoundedContext.PRODUCT_CATALOG,
                new ServiceDefinition("catalog-service",
                    Arrays.asList("Product search", "Product details", "Category management"),
                    Arrays.asList("Elasticsearch", "MongoDB"),
                    ServiceType.QUERY));
                    
            services.put(BoundedContext.ORDER_MANAGEMENT,
                new ServiceDefinition("order-service",
                    Arrays.asList("Order creation", "Order status", "Order history"),
                    Arrays.asList("PostgreSQL", "Kafka"),
                    ServiceType.TRANSACTIONAL));
        }
        
        private void mapDependencies() {
            // Order service depends on User service for customer info
            dependencies.add(new InterContextDependency(
                BoundedContext.ORDER_MANAGEMENT, 
                BoundedContext.USER_MANAGEMENT,
                DependencyType.SYNC_API_CALL,
                "Get customer details for order"));
                
            // Order service depends on Inventory service
            dependencies.add(new InterContextDependency(
                BoundedContext.ORDER_MANAGEMENT,
                BoundedContext.INVENTORY_MANAGEMENT,
                DependencyType.ASYNC_EVENT,
                "Reserve inventory for order"));
        }
    }
}

// Strangler Fig pattern implementation for gradual migration
@Component
public class StranglerFigPattern {
    
    private final LegacySystemClient legacyClient;
    private final MicroserviceClient microserviceClient;
    private final MigrationStateService migrationStateService;
    
    public OrderResponse handleOrderRequest(OrderRequest request) {
        // Determine which system should handle the request
        SystemTarget target = determineTargetSystem(request);
        
        switch (target) {
            case LEGACY:
                return handleWithLegacySystem(request);
                
            case HYBRID:
                return handleWithHybridApproach(request);
                
            case MICROSERVICE:
                return handleWithMicroservice(request);
                
            default:
                throw new UnsupportedOperationException("Unknown target system");
        }
    }
    
    private SystemTarget determineTargetSystem(OrderRequest request) {
        // Check migration state
        MigrationState state = migrationStateService.getCurrentState();
        
        // Route based on migration progress and request characteristics
        if (state.getCompletedServices().contains("order-service") && 
            isEligibleForMicroservice(request)) {
            return SystemTarget.MICROSERVICE;
        }
        
        // Gradual rollout based on user segments
        if (state.getMigrationPercentage() > 50 && 
            isCanaryUser(request.getUserId())) {
            return SystemTarget.HYBRID;
        }
        
        return SystemTarget.LEGACY;
    }
    
    private OrderResponse handleWithHybridApproach(OrderRequest request) {
        // Start with legacy system
        OrderResponse legacyResponse = legacyClient.createOrder(request);
        
        // Sync data to microservice
        try {
            microserviceClient.syncOrder(legacyResponse.getOrderId());
        } catch (Exception e) {
            log.warn("Failed to sync order to microservice", e);
            // Continue with legacy response
        }
        
        return legacyResponse;
    }
    
    // Data synchronization between legacy and microservices
    @Scheduled(fixedDelay = 300000) // Every 5 minutes
    public void synchronizeData() {
        // Get recently modified data from legacy system
        List<DataChange> changes = legacyClient.getRecentChanges(
            lastSyncTimestamp, LocalDateTime.now());
            
        // Apply changes to microservices
        for (DataChange change : changes) {
            try {
                applyChangeToMicroservice(change);
            } catch (Exception e) {
                log.error("Failed to synchronize data change: " + change.getId(), e);
                // Store for retry
                failedChangesQueue.add(change);
            }
        }
    }
}

// API Gateway for managing traffic during migration
@RestController
public class MigrationGatewayController {
    
    private final ServiceDiscovery serviceDiscovery;
    private final LoadBalancer loadBalancer;
    private final CircuitBreaker circuitBreaker;
    private final RateLimiter rateLimiter;
    
    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request,
            @RequestHeader("X-User-Segment") String userSegment) {
        
        // Apply rate limiting
        if (!rateLimiter.tryAcquire()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        
        // Determine target service based on migration state
        ServiceInstance targetService = determineTargetService(
            "order-service", userSegment);
            
        // Route request with circuit breaker protection
        return circuitBreaker.executeSupplier(() -> {
            try {
                ResponseEntity<OrderResponse> response = routeToService(
                    targetService, request);
                return response;
            } catch (Exception e) {
                // Fallback to legacy system
                return fallbackToLegacy(request);
            }
        });
    }
    
    private ServiceInstance determineTargetService(String serviceName, String userSegment) {
        // Get available instances
        List<ServiceInstance> instances = serviceDiscovery.getInstances(serviceName);
        
        // Filter based on migration state and user segment
        List<ServiceInstance> eligibleInstances = instances.stream()
            .filter(instance -> isInstanceEligible(instance, userSegment))
            .collect(Collectors.toList());
            
        if (eligibleInstances.isEmpty()) {
            // Fallback to legacy system
            return new LegacyServiceInstance();
        }
        
        // Load balance among eligible instances
        return loadBalancer.choose(eligibleInstances);
    }
}
```

### **Case Study 2: High-Performance Trading Platform**
**Expected Answer**:
```java
// Low-latency trading platform design
public class HighPerformanceTradingPlatform {
    
    // Core trading engine with ultra-low latency
    @Component
    public class TradingEngine {
        
        private final RingBuffer<OrderEvent> orderRingBuffer;
        private final Disruptor<OrderEvent> disruptor;
        private final OrderBook orderBook;
        private final RiskManagementService riskService;
        
        public TradingEngine() {
            // Initialize Disruptor for lock-free event processing
            this.orderRingBuffer = RingBuffer.createSingleProducer(
                OrderEvent::new, 1024 * 1024, new YieldingWaitStrategy());
                
            this.disruptor = new Disruptor<>(
                OrderEvent::new, 
                1024 * 1024,
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE,
                new YieldingWaitStrategy());
                
            // Set up event processors
            disruptor.handleEventsWith(
                new OrderValidationProcessor(riskService),
                new OrderMatchingProcessor(orderBook))
                .then(new TradeExecutionProcessor());
                
            disruptor.start();
        }
        
        public long placeOrder(Order order) {
            // Non-blocking order placement
            long sequence = orderRingBuffer.next();
            try {
                OrderEvent event = orderRingBuffer.get(sequence);
                event.setOrder(order);
                event.setTimestamp(System.nanoTime());
            } finally {
                orderRingBuffer.publish(sequence);
            }
            
            return sequence;
        }
    }
    
    // Order book implementation with concurrent access
    public class OrderBook {
        private final ConcurrentSkipListMap<BigDecimal, List<Order>> buyOrders;
        private final ConcurrentSkipListMap<BigDecimal, List<Order>> sellOrders;
        private final AtomicLong lastTradeId = new AtomicLong(0);
        
        public OrderBook() {
            // Use concurrent data structures for thread safety
            this.buyOrders = new ConcurrentSkipListMap<>(Collections.reverseOrder());
            this.sellOrders = new ConcurrentSkipListMap<>();
        }
        
        public List<Trade> matchOrder(Order incomingOrder) {
            List<Trade> trades = new ArrayList<>();
            
            if (incomingOrder.getSide() == OrderSide.BUY) {
                trades.addAll(matchBuyOrder(incomingOrder));
            } else {
                trades.addAll(matchSellOrder(incomingOrder));
            }
            
            return trades;
        }
        
        private List<Trade> matchBuyOrder(Order buyOrder) {
            List<Trade> trades = new ArrayList<>();
            BigDecimal maxPrice = buyOrder.getPrice();
            
            // Iterate through sell orders (lowest price first)
            Iterator<Map.Entry<BigDecimal, List<Order>>> iterator = 
                sellOrders.entrySet().iterator();
                
            while (iterator.hasNext() && buyOrder.getRemainingQuantity() > 0) {
                Map.Entry<BigDecimal, List<Order>> entry = iterator.next();
                BigDecimal sellPrice = entry.getKey();
                
                // Stop if sell price is higher than buy price
                if (sellPrice.compareTo(maxPrice) > 0) {
                    break;
                }
                
                List<Order> sellOrdersAtPrice = entry.getValue();
                Iterator<Order> sellIterator = sellOrdersAtPrice.iterator();
                
                while (sellIterator.hasNext() && buyOrder.getRemainingQuantity() > 0) {
                    Order sellOrder = sellIterator.next();
                    
                    // Match orders
                    long tradeQuantity = Math.min(
                        buyOrder.getRemainingQuantity(), 
                        sellOrder.getRemainingQuantity());
                        
                    Trade trade = new Trade(
                        lastTradeId.incrementAndGet(),
                        buyOrder.getId(),
                        sellOrder.getId(),
                        sellPrice,
                        tradeQuantity,
                        System.currentTimeMillis());
                        
                    trades.add(trade);
                    
                    // Update order quantities
                    buyOrder.reduceQuantity(tradeQuantity);
                    sellOrder.reduceQuantity(tradeQuantity);
                    
                    // Remove filled sell order
                    if (sellOrder.getRemainingQuantity() == 0) {
                        sellIterator.remove();
                    }
                }
                
                // Remove price level if no orders remain
                if (sellOrdersAtPrice.isEmpty()) {
                    iterator.remove();
                }
            }
            
            // Add unfilled buy order to book
            if (buyOrder.getRemainingQuantity() > 0) {
                buyOrders.computeIfAbsent(buyOrder.getPrice(), k -> 
                    new CopyOnWriteArrayList<>()).add(buyOrder);
            }
            
            return trades;
        }
    }
    
    // Memory-mapped files for persistence
    @Component
    public class MmapPersistenceService {
        
        private final RandomAccessFile orderFile;
        private final MappedByteBuffer orderBuffer;
        private final AtomicLong position = new AtomicLong(0);
        
        public MmapPersistenceService() throws IOException {
            // Use memory-mapped files for ultra-fast persistence
            this.orderFile = new RandomAccessFile("/data/orders.dat", "rw");
            this.orderFile.setLength(1024L * 1024 * 1024); // 1GB
            this.orderBuffer = orderFile.getChannel().map(
                FileChannel.MapMode.READ_WRITE, 0, orderFile.length());
        }
        
        public void persistOrder(Order order) {
            try {
                // Serialize order to byte array
                byte[] orderBytes = serializeOrder(order);
                
                // Write to memory-mapped buffer
                long pos = position.getAndAdd(orderBytes.length);
                orderBuffer.position((int) pos);
                orderBuffer.put(orderBytes);
                
                // Force write to disk periodically
                if (pos % (1024 * 1024) == 0) {
                    orderBuffer.force();
                }
            } catch (Exception e) {
                log.error("Failed to persist order: " + order.getId(), e);
            }
        }
    }
}

// Market data distribution with multicast
@Component
public class MarketDataDistributionService {
    
    private final MulticastSocket multicastSocket;
    private final DatagramPacket multicastPacket;
    private final ExecutorService distributionExecutor;
    
    public MarketDataDistributionService() throws IOException {
        // Use multicast for efficient market data distribution
        this.multicastSocket = new MulticastSocket();
        this.multicastPacket = new DatagramPacket(new byte[8192], 8192);
        this.distributionExecutor = Executors.newFixedThreadPool(16);
    }
    
    public void distributeMarketData(MarketData data) {
        // Compress data for network efficiency
        byte[] compressedData = compressMarketData(data);
        
        // Distribute using multiple threads
        distributionExecutor.submit(() -> {
            try {
                multicastPacket.setData(compressedData);
                multicastPacket.setAddress(InetAddress.getByName("230.0.0.1"));
                multicastPacket.setPort(4447);
                multicastSocket.send(multicastPacket);
            } catch (IOException e) {
                log.error("Failed to distribute market data", e);
            }
        });
    }
}
```

## 🌐 **Distributed Systems Case Studies**

### **Case Study 3: Global Content Delivery Network**
**Expected Answer**:
```java
// CDN design for global content delivery
public class GlobalCDNDesign {
    
    // Intelligent request routing
    @RestController
    public class CDNRoutingController {
        
        private final GeoIPService geoIPService;
        private final EdgeNodeRegistry edgeNodeRegistry;
        private final LoadBalancer loadBalancer;
        private final HealthCheckService healthCheckService;
        
        @GetMapping("/content/{contentId}")
        public ResponseEntity<StreamingResponseBody> serveContent(
                @PathVariable String contentId,
                HttpServletRequest request) {
            
            // Determine client location
            String clientIP = getClientIP(request);
            GeoLocation clientLocation = geoIPService.getLocation(clientIP);
            
            // Find optimal edge nodes
            List<EdgeNode> candidateNodes = edgeNodeRegistry.getNodesByRegion(
                clientLocation.getRegion());
                
            // Filter healthy nodes
            List<EdgeNode> healthyNodes = candidateNodes.stream()
                .filter(node -> healthCheckService.isHealthy(node))
                .collect(Collectors.toList());
                
            // Select best node based on multiple factors
            EdgeNode selectedNode = loadBalancer.selectNode(
                healthyNodes, clientLocation, contentId);
                
            // Redirect to optimal edge node
            return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(selectedNode.getEndpoint() + "/content/" + contentId))
                .build();
        }
    }
    
    // Multi-tier caching strategy
    @Component
    public class MultiTierCacheManager {
        
        private final Cache<L1CacheKey, CachedContent> l1Cache; // In-memory (Redis)
        private final Cache<L2CacheKey, CachedContent> l2Cache; // SSD-based
        private final OriginServerClient originClient;
        private final CacheWarmer cacheWarmer;
        
        public CachedContent getContent(String contentId, CacheStrategy strategy) {
            // L1 cache lookup (fastest)
            L1CacheKey l1Key = new L1CacheKey(contentId, strategy.getVariant());
            CachedContent content = l1Cache.getIfPresent(l1Key);
            
            if (content != null) {
                content.setCacheHit(CacheTier.L1);
                return content;
            }
            
            // L2 cache lookup (SSD)
            L2CacheKey l2Key = new L2CacheKey(contentId);
            content = l2Cache.getIfPresent(l2Key);
            
            if (content != null) {
                content.setCacheHit(CacheTier.L2);
                // Promote to L1 cache
                l1Cache.put(l1Key, content);
                return content;
            }
            
            // Fetch from origin
            content = originClient.fetchContent(contentId, strategy);
            content.setCacheHit(CacheTier.ORIGIN);
            
            // Store in caches
            l2Cache.put(l2Key, content);
            l1Cache.put(l1Key, content);
            
            return content;
        }
        
        // Cache warming for popular content
        @Scheduled(fixedRate = 300000) // Every 5 minutes
        public void warmCaches() {
            // Get trending content
            List<String> trendingContent = analyticsService.getTrendingContent();
            
            // Pre-warm caches in edge locations
            for (String contentId : trendingContent) {
                cacheWarmer.warmContent(contentId);
            }
        }
    }
    
    // Edge node with adaptive compression
    @Component
    public class AdaptiveEdgeNode {
        
        private final CompressionService compressionService;
        private final ClientCapabilityService clientService;
        private final NetworkConditionMonitor networkMonitor;
        
        public ResponseEntity<byte[]> serveContent(
                String contentId, 
                HttpServletRequest request) {
            
            // Determine client capabilities
            ClientCapabilities capabilities = clientService.getCapabilities(
                request.getHeader("User-Agent"));
                
            // Assess network conditions
            NetworkConditions network = networkMonitor.getConditions(
                getClientIP(request));
                
            // Select optimal compression
            CompressionStrategy strategy = selectCompressionStrategy(
                capabilities, network, contentId);
                
            // Fetch content
            CachedContent content = cacheManager.getContent(contentId, strategy);
            
            // Apply adaptive compression
            byte[] compressedContent = compressionService.compress(
                content.getRawData(), strategy);
                
            // Set appropriate headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentEncoding(strategy.getEncoding());
            headers.setCacheControl("public, max-age=31536000"); // 1 year
            headers.setETag(content.getETag());
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(compressedContent);
        }
        
        private CompressionStrategy selectCompressionStrategy(
                ClientCapabilities capabilities,
                NetworkConditions network,
                String contentId) {
            
            // Brotli for modern browsers on fast connections
            if (capabilities.supportsBrotli() && network.getBandwidth() > 10_000_000) {
                return CompressionStrategy.BROTLI_HIGH_QUALITY;
            }
            
            // Gzip for older browsers
            if (capabilities.supportsGzip()) {
                // Lower compression for slower connections
                if (network.getLatency() > 100) {
                    return CompressionStrategy.GZIP_FAST;
                } else {
                    return CompressionStrategy.GZIP_BEST;
                }
            }
            
            // No compression for very slow connections
            if (network.getBandwidth() < 1_000_000) {
                return CompressionStrategy.NONE;
            }
            
            return CompressionStrategy.GZIP_DEFAULT;
        }
    }
}

// Anycast routing for global load distribution
@Component
public class AnycastRoutingService {
    
    private final BgpService bgpService;
    private final DnsService dnsService;
    private final HealthCheckService healthCheckService;
    
    public void updateRouting(String serviceName, List<DataCenter> dataCenters) {
        // Check health of all data centers
        List<DataCenter> healthyCenters = dataCenters.stream()
            .filter(dc -> healthCheckService.isHealthy(dc))
            .collect(Collectors.toList());
            
        // Update BGP announcements
        for (DataCenter dc : healthyCenters) {
            bgpService.announcePrefix(
                serviceName, 
                dc.getIpAddress(), 
                dc.getPriority());
        }
        
        // Update DNS records with healthy endpoints
        dnsService.updateRecords(
            serviceName + ".cdn.example.com",
            healthyCenters.stream()
                .map(DataCenter::getDnsName)
                .collect(Collectors.toList()));
    }
}
```

## 📱 **Mobile and IoT Case Studies**

### **Case Study 4: Real-time Ride-Sharing Platform**
**Expected Answer**:
```java
// Real-time location tracking system
public class RealTimeRideSharingPlatform {
    
    // Geospatial indexing for efficient location queries
    @Component
    public class GeospatialIndexService {
        
        private final RedisTemplate<String, String> redisTemplate;
        private final GeoOperations<String, String> geoOperations;
        
        public GeospatialIndexService(RedisTemplate<String, String> redisTemplate) {
            this.redisTemplate = redisTemplate;
            this.geoOperations = redisTemplate.opsForGeo();
        }
        
        // Index driver locations
        public void updateDriverLocation(String driverId, Location location) {
            Point point = new Point(location.getLongitude(), location.getLatitude());
            geoOperations.add("drivers:active", point, driverId);
            
            // Expire location after 30 seconds
            redisTemplate.expire("drivers:active:" + driverId, 30, TimeUnit.SECONDS);
        }
        
        // Find nearby drivers
        public List<Driver> findNearbyDrivers(Location pickupLocation, double radiusKm) {
            Point pickupPoint = new Point(pickupLocation.getLongitude(), pickupLocation.getLatitude());
            
            // Query geospatial index
            GeoResults<RedisGeoCommands.GeoLocation<String>> results = 
                geoOperations.radius("drivers:active", pickupPoint, 
                    new Distance(radiusKm, Metrics.KILOMETERS));
                    
            List<Driver> nearbyDrivers = new ArrayList<>();
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
                String driverId = result.getContent().getName();
                Driver driver = driverService.getDriver(driverId);
                if (driver != null && driver.isAvailable()) {
                    nearbyDrivers.add(driver);
                }
            }
            
            return nearbyDrivers;
        }
    }
    
    // WebSocket-based real-time communication
    @Component
    public class RealTimeCommunicationService {
        
        private final SimpMessagingTemplate messagingTemplate;
        private final RideMatchingService rideMatchingService;
        private final DriverLocationService driverLocationService;
        
        // Send real-time updates to passengers
        public void sendRideUpdates(String passengerId, RideUpdate update) {
            messagingTemplate.convertAndSendToUser(
                passengerId, 
                "/queue/ride-updates", 
                update);
        }
        
        // Handle driver location updates
        @MessageMapping("/driver/location")
        @SendTo("/topic/driver-locations")
        public DriverLocationUpdate handleDriverLocation(
                DriverLocationUpdate locationUpdate) {
            
            // Update driver location in geospatial index
            driverLocationService.updateLocation(
                locationUpdate.getDriverId(), 
                locationUpdate.getLocation());
                
            return locationUpdate;
        }
        
        // Match passengers with drivers in real-time
        @Scheduled(fixedRate = 5000) // Every 5 seconds
        public void performRideMatching() {
            List<RideRequest> pendingRequests = rideRequestService.getPendingRequests();
            
            for (RideRequest request : pendingRequests) {
                try {
                    // Find nearby drivers
                    List<Driver> nearbyDrivers = geospatialIndexService.findNearbyDrivers(
                        request.getPickupLocation(), 5.0); // 5km radius
                        
                    // Match with best driver
                    Driver matchedDriver = rideMatchingService.matchDriver(
                        request, nearbyDrivers);
                        
                    if (matchedDriver != null) {
                        // Create ride
                        Ride ride = rideService.createRide(request, matchedDriver);
                        
                        // Notify both parties
                        sendRideUpdates(request.getPassengerId(), 
                            new RideUpdate(RideStatus.MATCHED, ride));
                        sendRideUpdates(matchedDriver.getId(),
                            new RideUpdate(RideStatus.ASSIGNED, ride));
                    }
                } catch (Exception e) {
                    log.error("Failed to match ride request: " + request.getId(), e);
                }
            }
        }
    }
    
    // Event sourcing for ride state management
    @Component
    public class RideEventSourcingService {
        
        private final EventStore eventStore;
        private final RideStateReconstructor stateReconstructor;
        
        public Ride processRideEvent(RideEvent event) {
            // Store event
            eventStore.appendEvent(event);
            
            // Reconstruct current state
            Ride currentState = stateReconstructor.reconstruct(
                event.getRideId(), event.getTimestamp());
                
            // Apply business rules
            validateRideStateTransition(currentState, event);
            
            // Trigger side effects
            handleSideEffects(currentState, event);
            
            return currentState;
        }
        
        private void handleSideEffects(Ride ride, RideEvent event) {
            switch (event.getType()) {
                case RIDE_ACCEPTED:
                    // Notify passenger
                    notificationService.sendRideAcceptedNotification(
                        ride.getPassengerId(), ride);
                    break;
                    
                case RIDE_STARTED:
                    // Start trip tracking
                    tripTrackingService.startTracking(ride);
                    break;
                    
                case RIDE_COMPLETED:
                    // Process payment
                    paymentService.processRidePayment(ride);
                    // Update driver ratings
                    ratingService.updateDriverRating(
                        ride.getDriverId(), ride.getDriverRating());
                    break;
            }
        }
    }
}

// Push notification system for mobile devices
@Component
public class MobileNotificationService {
    
    private final FcmService fcmService;
    private final ApnsService apnsService;
    private final SmsService smsService;
    
    public void sendRideNotification(User user, RideNotification notification) {
        // Select appropriate notification channel
        switch (user.getPreferredNotificationChannel()) {
            case PUSH:
                sendPushNotification(user, notification);
                break;
                
            case SMS:
                sendSmsNotification(user, notification);
                break;
                
            case BOTH:
                sendPushNotification(user, notification);
                sendSmsNotification(user, notification);
                break;
        }
    }
    
    private void sendPushNotification(User user, RideNotification notification) {
        try {
            if (user.getDeviceType() == DeviceType.ANDROID) {
                fcmService.sendNotification(user.getFcmToken(), notification);
            } else if (user.getDeviceType() == DeviceType.IOS) {
                apnsService.sendNotification(user.getApnsToken(), notification);
            }
        } catch (Exception e) {
            log.error("Failed to send push notification to user: " + user.getId(), e);
            // Fallback to SMS
            sendSmsNotification(user, notification);
        }
    }
}
```

## 🏢 **Additional Enterprise Case Studies**

### **Case Study 5: Financial Services Core Banking Platform**
**Scenario**: Designing a core banking platform that needs to handle millions of transactions per day with strict regulatory compliance requirements.

**Key Requirements**:
- ACID compliance for all financial transactions
- Real-time processing with sub-second response times
- Regulatory compliance (PCI DSS, SOX, GDPR)
- Disaster recovery with RTO < 4 hours, RPO < 5 minutes
- Audit trail for all transactions

**Solution Architecture**:
```java
// Core banking platform with distributed transactions
public class CoreBankingPlatform {
    
    // Distributed transaction manager using Saga pattern
    @Component
    public class BankingTransactionSagaOrchestrator {
        
        private final AccountService accountService;
        private final LedgerService ledgerService;
        private final ComplianceService complianceService;
        private final AuditService auditService;
        
        public void executeTransfer(TransferRequest request) {
            SagaContext context = new SagaContext();
            context.setTransferRequest(request);
            
            try {
                // Step 1: Validate transfer
                ValidationResult validation = accountService.validateTransfer(request);
                if (!validation.isValid()) {
                    throw new ValidationException(validation.getErrorMessage());
                }
                context.addStep(new SagaStep("VALIDATE_TRANSFER", "COMPLETED"));
                
                // Step 2: Check compliance
                ComplianceResult compliance = complianceService.checkCompliance(request);
                if (!compliance.isCompliant()) {
                    throw new ComplianceException(compliance.getViolation());
                }
                context.addStep(new SagaStep("CHECK_COMPLIANCE", "COMPLETED"));
                
                // Step 3: Reserve funds
                accountService.reserveFunds(request.getFromAccount(), request.getAmount());
                context.addStep(new SagaStep("RESERVE_FUNDS", "COMPLETED"));
                
                // Step 4: Update ledger
                ledgerService.recordTransfer(request);
                context.addStep(new SagaStep("UPDATE_LEDGER", "COMPLETED"));
                
                // Step 5: Execute transfer
                accountService.executeTransfer(request);
                context.addStep(new SagaStep("EXECUTE_TRANSFER", "COMPLETED"));
                
                // Step 6: Generate audit trail
                auditService.logTransfer(request);
                context.addStep(new SagaStep("AUDIT_TRANSFER", "COMPLETED"));
                
                // Mark saga as completed
                context.setStatus(SagaStatus.COMPLETED);
                
            } catch (Exception e) {
                // Compensate for failed steps
                compensate(context);
                throw new TransactionException("Transfer failed", e);
            }
        }
        
        private void compensate(SagaContext context) {
            List<SagaStep> completedSteps = context.getCompletedSteps();
            
            // Compensate in reverse order
            for (int i = completedSteps.size() - 1; i >= 0; i--) {
                SagaStep step = completedSteps.get(i);
                try {
                    switch (step.getAction()) {
                        case "AUDIT_TRANSFER":
                            // Audit logs are immutable, no compensation needed
                            break;
                        case "EXECUTE_TRANSFER":
                            accountService.reverseTransfer(context.getTransferRequest());
                            break;
                        case "UPDATE_LEDGER":
                            ledgerService.reverseEntry(context.getTransferRequest());
                            break;
                        case "RESERVE_FUNDS":
                            accountService.releaseFunds(context.getTransferRequest().getFromAccount(), 
                                                       context.getTransferRequest().getAmount());
                            break;
                        case "CHECK_COMPLIANCE":
                        case "VALIDATE_TRANSFER":
                            // Validation and compliance checks don't need compensation
                            break;
                    }
                    step.setStatus("COMPENSATED");
                } catch (Exception e) {
                    log.error("Compensation failed for step: " + step.getAction(), e);
                }
            }
            
            context.setStatus(SagaStatus.FAILED);
        }
    }
    
    // High-availability account service with consensus
    @Service
    public class AccountService {
        
        private final List<AccountRepository> replicas;
        private final ConsensusAlgorithm consensusAlgorithm;
        
        public void executeTransfer(TransferRequest request) {
            // Use consensus algorithm to ensure all replicas agree
            ConsensusResult result = consensusAlgorithm.execute(() -> {
                // Execute transfer on all replicas
                replicas.parallelStream().forEach(repo -> 
                    repo.executeTransfer(request.getFromAccount(), 
                                       request.getToAccount(), 
                                       request.getAmount()));
            });
            
            if (!result.isConsensusReached()) {
                throw new ConsensusException("Failed to reach consensus on transfer execution");
            }
        }
        
        // Reserve funds with distributed locking
        public void reserveFunds(String accountId, BigDecimal amount) {
            // Use distributed lock to prevent race conditions
            try (DistributedLock lock = lockService.acquireLock("account:" + accountId)) {
                Account account = getAccount(accountId);
                if (account.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException(accountId, amount);
                }
                
                // Reserve funds by updating reserved balance
                account.setReservedBalance(account.getReservedBalance().add(amount));
                updateAccount(account);
            }
        }
    }
    
    // Immutable audit trail with blockchain-like structure
    @Entity
    @Table(name = "audit_trail")
    public class AuditTrailEntry {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @Column(nullable = false)
        private String transactionId;
        
        @Column(nullable = false)
        private String previousHash;
        
        @Column(nullable = false)
        private String currentHash;
        
        @Column(length = 5000, nullable = false)
        private String eventData;
        
        @Column(nullable = false)
        private LocalDateTime timestamp;
        
        @Column(nullable = false)
        private String userId;
        
        @Version
        private Long version;
        
        // Calculate hash based on previous entry and current data
        public String calculateHash() {
            String dataToHash = previousHash + eventData + timestamp.toString() + userId;
            return HashUtils.sha256(dataToHash);
        }
    }
}
```

### **Case Study 6: Healthcare Patient Management System**
**Scenario**: Designing a HIPAA-compliant patient management system for a large hospital network with strict data privacy requirements.

**Key Requirements**:
- HIPAA compliance for patient data protection
- Real-time access for medical staff across multiple locations
- Integration with medical devices and EHR systems
- Audit trails for all patient data access
- Disaster recovery and business continuity

**Solution Architecture**:
```java
// HIPAA-compliant healthcare system
public class HealthcarePatientManagementSystem {
    
    // Patient data with encryption and access controls
    @Entity
    @Table(name = "patients")
    public class Patient {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        // Encrypted patient identifiers
        @Convert(converter = EncryptedStringConverter.class)
        @Column(name = "first_name_encrypted")
        private String firstName;
        
        @Convert(converter = EncryptedStringConverter.class)
        @Column(name = "last_name_encrypted")
        private String lastName;
        
        @Convert(converter = EncryptedStringConverter.class)
        @Column(name = "ssn_encrypted")
        private String ssn;
        
        // Tokenized identifiers for search
        @Column(name = "first_name_token")
        private String firstNameToken;
        
        @Column(name = "last_name_token")
        private String lastNameToken;
        
        // Role-based access control
        @ElementCollection
        @CollectionTable(name = "patient_access_controls")
        private Set<AccessControl> accessControls;
        
        // Audit trail reference
        @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
        private List<AuditLog> auditLogs;
    }
    
    // Attribute-based access control (ABAC) for healthcare
    @Component
    public class HealthcareAccessControlService {
        
        private final PatientRepository patientRepository;
        private final UserRoleService userRoleService;
        private final AuditService auditService;
        
        public boolean canAccessPatientData(String userId, Long patientId, 
                                         HealthcareAction action) {
            User user = userRoleService.getUser(userId);
            Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
                
            // Check role-based permissions
            if (!hasRoleBasedAccess(user, action)) {
                return false;
            }
            
            // Check attribute-based permissions
            if (!hasAttributeBasedAccess(user, patient, action)) {
                return false;
            }
            
            // Log access attempt
            auditService.logAccessAttempt(userId, patientId, action);
            
            return true;
        }
        
        private boolean hasRoleBasedAccess(User user, HealthcareAction action) {
            Set<Role> userRoles = user.getRoles();
            
            switch (action) {
                case VIEW_PATIENT_RECORD:
                    return userRoles.contains(Role.DOCTOR) || 
                           userRoles.contains(Role.NURSE) || 
                           userRoles.contains(Role.ADMIN);
                           
                case MODIFY_PATIENT_RECORD:
                    return userRoles.contains(Role.DOCTOR) || 
                           userRoles.contains(Role.ADMIN);
                           
                case DELETE_PATIENT_RECORD:
                    return userRoles.contains(Role.ADMIN);
                    
                default:
                    return false;
            }
        }
        
        private boolean hasAttributeBasedAccess(User user, Patient patient, 
                                              HealthcareAction action) {
            // Check department alignment
            if (!user.getDepartment().equals(patient.getDepartment())) {
                // Allow access only for emergencies or specific permissions
                return user.hasEmergencyAccess() || user.hasCrossDepartmentPermission();
            }
            
            // Check time-based access restrictions
            if (user.hasTimeBasedRestrictions()) {
                LocalTime now = LocalTime.now();
                if (now.isBefore(user.getAccessStartTime()) || 
                    now.isAfter(user.getAccessEndTime())) {
                    return false;
                }
            }
            
            return true;
        }
    }
    
    // Medical device integration with real-time data processing
    @Component
    public class MedicalDeviceIntegrationService {
        
        private final KafkaTemplate<String, MedicalData> kafkaTemplate;
        private final PatientDataService patientDataService;
        private final AlertService alertService;
        
        // Handle real-time data from medical devices
        @KafkaListener(topics = "medical-device-data")
        public void processMedicalDeviceData(ConsumerRecord<String, MedicalData> record) {
            MedicalData data = record.value();
            
            try {
                // Validate data integrity
                if (!isValidMedicalData(data)) {
                    log.warn("Invalid medical data received: " + data.getDeviceId());
                    return;
                }
                
                // Store data with proper patient association
                patientDataService.storeMedicalData(data);
                
                // Check for critical values that require alerts
                if (isCriticalValue(data)) {
                    Alert alert = createAlert(data);
                    alertService.sendAlert(alert);
                }
                
                // Update patient dashboard in real-time
                updatePatientDashboard(data);
                
            } catch (Exception e) {
                log.error("Failed to process medical device data: " + data.getDeviceId(), e);
                // Send to dead letter queue for manual processing
                sendToDlq(data, e);
            }
        }
        
        private boolean isCriticalValue(MedicalData data) {
            // Check for life-threatening values
            switch (data.getDataType()) {
                case HEART_RATE:
                    return data.getValue() < 40 || data.getValue() > 180;
                case BLOOD_PRESSURE_SYSTOLIC:
                    return data.getValue() < 90 || data.getValue() > 180;
                case BLOOD_PRESSURE_DIASTOLIC:
                    return data.getValue() < 60 || data.getValue() > 120;
                case OXYGEN_SATURATION:
                    return data.getValue() < 90;
                default:
                    return false;
            }
        }
    }
}
```

## 🎯 **Case Study Analysis Framework**

### **System Design Evaluation Criteria**:
1. **Scalability**: Can the system handle growth in users, data, and traffic?
2. **Performance**: Does the system meet latency and throughput requirements?
3. **Reliability**: How does the system handle failures and maintain availability?
4. **Maintainability**: Is the system easy to understand, modify, and extend?
5. **Security**: Are data and communications adequately protected?
6. **Cost Effectiveness**: Does the solution provide good value for the investment?
7. **Operational Excellence**: Is the system easy to monitor, debug, and operate?

### **Key Design Patterns Applied**:
1. **Microservices Architecture**: Decompose system into independent services
2. **Event-Driven Architecture**: Use events for loose coupling and scalability
3. **CQRS**: Separate read and write operations for better performance
4. **Event Sourcing**: Store events as source of truth for audit and replay
5. **Strangler Fig**: Gradually replace legacy systems
6. **Circuit Breaker**: Prevent cascading failures
7. **Bulkhead**: Isolate failures to prevent system-wide impact
8. **Cache-Aside**: Improve read performance with caching

### **Common Challenges and Solutions**:
1. **Data Consistency**: Use eventual consistency, saga patterns, or distributed transactions
2. **Distributed Tracing**: Implement correlation IDs and tracing frameworks
3. **Service Discovery**: Use service registry and client-side/load-balancer discovery
4. **Configuration Management**: Externalize configuration with version control
5. **Security**: Implement zero-trust architecture and defense in depth
6. **Monitoring**: Use metrics, logs, and traces for observability
7. **Deployment**: Implement CI/CD with blue-green or canary deployments
8. **Testing**: Use contract testing, chaos engineering, and performance testing

### **Best Practices for Case Study Interviews**:
1. **Ask Clarifying Questions**: Understand requirements, constraints, and success criteria
2. **Think Aloud**: Explain your thought process and trade-off considerations
3. **Start Broad, Then Dive Deep**: Begin with high-level architecture, then focus on critical components
4. **Consider Non-Functional Requirements**: Don't forget about scalability, security, and reliability
5. **Address Failure Scenarios**: Show how your system handles failures gracefully
6. **Quantify Your Design**: Use numbers to justify your architectural decisions
7. **Iterate Based on Feedback**: Be open to adjusting your design based on interviewer input

## 💡 **Additional Interview Preparation Tips**

### **How to Approach Case Study Questions**:
1. **Listen Carefully**: Pay attention to all requirements and constraints mentioned
2. **Ask Questions**: Clarify ambiguous points and gather more information
3. **Think Before You Speak**: Take a moment to organize your thoughts
4. **Communicate Clearly**: Explain your design decisions and trade-offs
5. **Draw Diagrams**: Visual representations help both you and the interviewer
6. **Consider Edge Cases**: Think about failure scenarios and how to handle them
7. **Be Flexible**: Adapt your solution based on feedback

### **Common Mistakes to Avoid**:
1. **Jumping to Solutions**: Don't start designing before fully understanding the problem
2. **Ignoring Non-Functional Requirements**: Scalability, reliability, and security are crucial
3. **Over-Engineering**: Don't make the solution more complex than needed
4. **Forgetting Operations**: Consider monitoring, logging, and deployment
5. **Neglecting Trade-offs**: Every decision has pros and cons, acknowledge them
6. **Lack of Justification**: Always explain why you chose a particular approach
7. **Not Asking Questions**: Assumptions can lead you in the wrong direction

### **Key Metrics to Consider**:
- **Latency Requirements**: What are the response time expectations?
- **Throughput Needs**: How many requests per second must the system handle?
- **Data Volume**: How much data will be processed and stored?
- **Availability Targets**: What uptime is required (e.g., 99.9%, 99.99%)?
- **Consistency Requirements**: Strong consistency vs. eventual consistency?
- **Geographic Distribution**: Is global distribution needed?
- **Budget Constraints**: Are there cost limitations to consider?

### **Technology Selection Guidelines**:
1. **Choose the Right Tool for the Job**: Don't use a hammer for everything
2. **Consider Team Expertise**: Use technologies your team can maintain
3. **Evaluate Community Support**: Active communities mean better long-term support
4. **Think About Hiring**: Popular technologies make it easier to hire talent
5. **Plan for Evolution**: Can the technology grow with your needs?

## 📚 **Further Reading and Resources**

### **Essential Books for System Design**:
- "Designing Data-Intensive Applications" by Martin Kleppmann
- "Building Microservices" by Sam Newman
- "Site Reliability Engineering" by Google
- "The Art of Scalability" by Martin Abbott and Michael Fisher
- "Building Evolutionary Architectures" by Neal Ford

### **Online Resources**:
- High Scalability blog
- AWS Architecture Center
- Google Cloud Architecture Framework
- Microsoft Azure Well-Architected Framework

### **Practice Platforms**:
- LeetCode System Design
- Grokking the System Design Interview
- System Design Primer on GitHub
- InterviewBit System Design

## 🏁 **Summary**

Mastering system design case studies requires a combination of technical knowledge, practical experience, and effective communication skills. The key is to approach each problem methodically, considering both functional and non-functional requirements, while clearly articulating your design decisions and their trade-offs.

Remember that there's rarely one "correct" solution in system design. What matters most is your ability to analyze requirements, identify constraints, propose a well-reasoned architecture, and defend your choices while remaining open to feedback and alternative approaches.

With practice and preparation, you'll develop the confidence and skills needed to excel in system design interviews and become a more effective architect and technical leader.
