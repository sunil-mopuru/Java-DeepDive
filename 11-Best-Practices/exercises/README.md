# Best Practices - Practice Exercises

## 📝 Instructions
- Apply SOLID principles and design patterns
- Focus on clean, maintainable code architecture
- Practice professional development patterns
- Emphasize code quality and testing

---

## Exercise 1: Design Pattern Implementation Suite
**Difficulty: Intermediate**

Implement a comprehensive set of design patterns in a unified application context.

### Requirements:
- **Creational Patterns**: Singleton, Factory, Builder, Prototype
- **Structural Patterns**: Adapter, Decorator, Facade, Proxy
- **Behavioral Patterns**: Observer, Strategy, Command, State

### Application Context: E-Commerce System
```java
// Singleton for configuration management
public class ConfigurationManager {
    private static volatile ConfigurationManager instance;
    private final Properties config;
    
    private ConfigurationManager() {
        config = loadConfiguration();
    }
    
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }
}

// Factory for creating different product types
public abstract class ProductFactory {
    public abstract Product createProduct(String type, Map<String, Object> attributes);
    
    public static ProductFactory getFactory(ProductCategory category) {
        switch (category) {
            case ELECTRONICS: return new ElectronicsFactory();
            case CLOTHING: return new ClothingFactory();
            case BOOKS: return new BookFactory();
            default: throw new IllegalArgumentException("Unknown category");
        }
    }
}

// Builder for complex order creation
public class OrderBuilder {
    private final Order order;
    
    public OrderBuilder(String customerId) {
        this.order = new Order(customerId);
    }
    
    public OrderBuilder addItem(Product product, int quantity) {
        order.addItem(new OrderItem(product, quantity));
        return this;
    }
    
    public OrderBuilder setShippingAddress(Address address) {
        order.setShippingAddress(address);
        return this;
    }
    
    public OrderBuilder applyDiscount(DiscountStrategy discount) {
        order.setDiscount(discount);
        return this;
    }
    
    public Order build() {
        order.validate();
        return order;
    }
}

// Observer pattern for order status updates
public interface OrderObserver {
    void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus);
}

public class Order {
    private final List<OrderObserver> observers = new ArrayList<>();
    private OrderStatus status = OrderStatus.PENDING;
    
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }
    
    public void setStatus(OrderStatus newStatus) {
        OrderStatus oldStatus = this.status;
        this.status = newStatus;
        notifyObservers(oldStatus, newStatus);
    }
    
    private void notifyObservers(OrderStatus oldStatus, OrderStatus newStatus) {
        observers.forEach(observer -> 
            observer.onOrderStatusChanged(this, oldStatus, newStatus));
    }
}

// Strategy pattern for payment processing
public interface PaymentStrategy {
    PaymentResult processPayment(Order order, PaymentDetails details);
}

public class CreditCardPayment implements PaymentStrategy {
    public PaymentResult processPayment(Order order, PaymentDetails details) {
        // Credit card processing logic
        return new PaymentResult(true, "Payment successful");
    }
}
```

---

## Exercise 2: Clean Architecture Implementation
**Difficulty: Advanced**

Build a multi-layer application following clean architecture principles.

### Requirements:
- **Domain Layer**: Business entities and rules
- **Application Layer**: Use cases and application services  
- **Infrastructure Layer**: Data access and external services
- **Presentation Layer**: Controllers and DTOs
- **Dependency Inversion**: All dependencies point inward

### Architecture Structure:
```java
// Domain Layer - Business Entities
public class User {
    private final UserId id;
    private String email;
    private String name;
    private UserStatus status;
    
    public void changeEmail(String newEmail, EmailValidator validator) {
        if (!validator.isValid(newEmail)) {
            throw new InvalidEmailException(newEmail);
        }
        this.email = newEmail;
    }
    
    public boolean canPlaceOrder() {
        return status == UserStatus.ACTIVE;
    }
}

// Domain Layer - Repository Interfaces
public interface UserRepository {
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(String email);
    void save(User user);
}

// Application Layer - Use Cases
public class CreateUserUseCase {
    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final UserFactory userFactory;
    
    public CreateUserUseCase(UserRepository userRepository, 
                           EmailValidator emailValidator,
                           UserFactory userFactory) {
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
        this.userFactory = userFactory;
    }
    
    public CreateUserResponse execute(CreateUserRequest request) {
        // Validate email uniqueness
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        
        // Create and save user
        User user = userFactory.createUser(request.getEmail(), request.getName());
        userRepository.save(user);
        
        return new CreateUserResponse(user.getId(), user.getEmail());
    }
}

// Infrastructure Layer - Repository Implementation
public class JpaUserRepository implements UserRepository {
    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;
    
    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.getValue())
                          .map(mapper::toDomain);
    }
    
    @Override
    public void save(User user) {
        UserEntity entity = mapper.toEntity(user);
        jpaRepository.save(entity);
    }
}

// Presentation Layer - Controller
@RestController
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    
    @PostMapping("/users")
    public ResponseEntity<CreateUserResponseDto> createUser(
            @RequestBody CreateUserRequestDto requestDto) {
        
        CreateUserRequest request = mapper.toRequest(requestDto);
        CreateUserResponse response = createUserUseCase.execute(request);
        CreateUserResponseDto responseDto = mapper.toDto(response);
        
        return ResponseEntity.ok(responseDto);
    }
}
```

---

## Exercise 3: Code Quality Refactoring Challenge
**Difficulty: Intermediate-Advanced**

Take legacy code and refactor it following best practices.

### Legacy Code Example:
```java
// BEFORE: Problematic legacy code
public class OrderProcessor {
    public void processOrder(String orderData) {
        String[] parts = orderData.split(",");
        String customerId = parts[0];
        String productId = parts[1];
        int quantity = Integer.parseInt(parts[2]);
        double price = Double.parseDouble(parts[3]);
        
        // Check customer
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db", "user", "pass");
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE id = ?");
        stmt.setString(1, customerId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            throw new RuntimeException("Customer not found");
        }
        
        // Check inventory
        stmt = conn.prepareStatement("SELECT stock FROM products WHERE id = ?");
        stmt.setString(1, productId);
        rs = stmt.executeQuery();
        if (!rs.next() || rs.getInt("stock") < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        // Process payment
        if (price > 1000) {
            // Special handling for large orders
            // ... complex payment logic
        } else {
            // Regular payment processing
            // ... regular payment logic
        }
        
        // Update inventory
        stmt = conn.prepareStatement("UPDATE products SET stock = stock - ? WHERE id = ?");
        stmt.setInt(1, quantity);
        stmt.setString(2, productId);
        stmt.executeUpdate();
        
        // Send confirmation email
        String email = rs.getString("email");
        sendEmail(email, "Order confirmed", "Your order has been processed");
        
        conn.close();
    }
}

// AFTER: Refactored code following best practices
public class OrderProcessingService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    private final OrderValidator orderValidator;
    
    public void processOrder(OrderRequest orderRequest) throws OrderProcessingException {
        try {
            // Validate order
            ValidationResult validation = orderValidator.validate(orderRequest);
            if (!validation.isValid()) {
                throw new InvalidOrderException(validation.getErrors());
            }
            
            // Process order steps
            Customer customer = findCustomer(orderRequest.getCustomerId());
            Product product = validateProductAvailability(
                orderRequest.getProductId(), 
                orderRequest.getQuantity()
            );
            
            Order order = createOrder(customer, product, orderRequest);
            PaymentResult payment = processPayment(order);
            
            if (payment.isSuccessful()) {
                updateInventory(product, orderRequest.getQuantity());
                sendConfirmation(customer, order);
            } else {
                throw new PaymentFailedException(payment.getErrorMessage());
            }
            
        } catch (Exception e) {
            handleOrderProcessingError(orderRequest, e);
            throw new OrderProcessingException("Failed to process order", e);
        }
    }
}
```

### Refactoring Tasks:
1. Extract methods and classes
2. Apply dependency injection
3. Add proper error handling
4. Implement validation
5. Add logging and monitoring
6. Write comprehensive tests

---

## Exercise 4: Testing Strategy Implementation
**Difficulty: Advanced**

Design and implement a comprehensive testing strategy.

### Requirements:
- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions
- **Contract Tests**: Test API contracts
- **Performance Tests**: Load and stress testing
- **Test Automation**: CI/CD pipeline integration

### Testing Framework:
```java
// Unit test example
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    
    @Mock private OrderRepository orderRepository;
    @Mock private PaymentService paymentService;
    @Mock private NotificationService notificationService;
    
    @InjectMocks private OrderService orderService;
    
    @Test
    @DisplayName("Should create order successfully when all conditions are met")
    void shouldCreateOrderSuccessfully() {
        // Given
        CreateOrderRequest request = new CreateOrderRequest("customer1", "product1", 2);
        Order expectedOrder = new Order("order1", "customer1", "product1", 2);
        
        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);
        when(paymentService.processPayment(any())).thenReturn(PaymentResult.success());
        
        // When
        OrderResult result = orderService.createOrder(request);
        
        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getOrder().getCustomerId()).isEqualTo("customer1");
        
        verify(orderRepository).save(any(Order.class));
        verify(paymentService).processPayment(any());
        verify(notificationService).sendOrderConfirmation(any());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "invalid-id"})
    @DisplayName("Should fail when customer ID is invalid")
    void shouldFailWithInvalidCustomerId(String customerId) {
        // Given
        CreateOrderRequest request = new CreateOrderRequest(customerId, "product1", 1);
        
        // When & Then
        assertThatThrownBy(() -> orderService.createOrder(request))
            .isInstanceOf(InvalidCustomerException.class)
            .hasMessageContaining("Invalid customer ID");
    }
}

// Integration test example
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderIntegrationTest {
    
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private OrderRepository orderRepository;
    
    @Test
    @Transactional
    @Rollback
    void shouldCreateOrderEndToEnd() {
        // Given
        CreateOrderRequestDto request = new CreateOrderRequestDto();
        request.setCustomerId("test-customer");
        request.setProductId("test-product");
        request.setQuantity(1);
        
        // When
        ResponseEntity<OrderResponseDto> response = restTemplate.postForEntity(
            "/api/orders", request, OrderResponseDto.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getOrderId()).isNotNull();
        
        // Verify in database
        Optional<Order> savedOrder = orderRepository.findById(response.getBody().getOrderId());
        assertThat(savedOrder).isPresent();
    }
}
```

---

## Exercise 5: Performance Optimization Project
**Difficulty: Advanced**

Optimize a system for performance while maintaining code quality.

### Optimization Areas:
- **Algorithm Efficiency**: Improve time complexity
- **Memory Management**: Reduce memory usage and GC pressure
- **Caching Strategy**: Implement multi-level caching
- **Database Optimization**: Query optimization and connection pooling
- **Concurrent Processing**: Parallel execution where appropriate

### Before/After Comparison:
```java
// BEFORE: Inefficient implementation
public class ProductSearchService {
    public List<Product> searchProducts(String keyword) {
        List<Product> allProducts = productRepository.findAll(); // Loads all products!
        
        return allProducts.stream()
            .filter(product -> 
                product.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                product.getDescription().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }
}

// AFTER: Optimized implementation
@Service
public class ProductSearchService {
    private final ProductRepository productRepository;
    private final SearchCache searchCache;
    private final FullTextSearchEngine searchEngine;
    
    @Cacheable(value = "product-search", key = "#keyword")
    public List<ProductDto> searchProducts(String keyword) {
        // Use full-text search engine for better performance
        SearchResult result = searchEngine.search(
            SearchQuery.builder()
                .keyword(keyword)
                .fields("name", "description")
                .maxResults(100)
                .build()
        );
        
        return result.getProductIds().stream()
            .map(this::loadProductWithCache)
            .filter(Objects::nonNull)
            .map(productMapper::toDto)
            .collect(Collectors.toList());
    }
    
    @Cacheable(value = "products", key = "#productId")
    private Product loadProductWithCache(String productId) {
        return productRepository.findById(productId).orElse(null);
    }
}
```

---

## 🎯 Challenge Projects

### Project A: Microservices Architecture Design
Design a complete microservices system with:
- Service decomposition strategy
- Inter-service communication patterns
- Data consistency patterns
- Monitoring and observability
- Deployment and scaling strategies

### Project B: Legacy System Modernization
Modernize a monolithic legacy system:
- Strangler Fig pattern implementation
- Database decomposition strategy
- API gateway and service mesh
- Gradual migration approach
- Risk mitigation strategies

### Project C: High-Performance Computing Framework
Build a framework for high-performance computations:
- Parallel processing algorithms
- Memory-efficient data structures
- Lock-free concurrent programming
- Performance monitoring and tuning
- Scalability testing and optimization

---

## 📚 Evaluation Criteria

### Code Quality (40%):
- Adherence to SOLID principles
- Proper use of design patterns
- Code readability and maintainability
- Error handling and edge cases

### Architecture (30%):
- System design and structure
- Separation of concerns
- Dependency management
- Scalability considerations

### Testing (20%):
- Test coverage and quality
- Test pyramid implementation
- Automated testing strategy
- Performance testing

### Documentation (10%):
- Code documentation
- Architecture documentation
- API documentation
- Deployment guides

---

**🎓 Congratulations on completing all Java Fundamentals exercises!**