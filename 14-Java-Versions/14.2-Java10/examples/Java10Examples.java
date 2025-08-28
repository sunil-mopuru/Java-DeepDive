/**
 * Java 10 Features Examples
 * 
 * Demonstrates the key features introduced in Java 10, focusing on
 * Local Variable Type Inference (var) and other enhancements.
 */

import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.nio.file.*;
import java.io.*;

public class Java10Examples {
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Java 10 Features Examples ===");
        
        demonstrateVarBasics();
        demonstrateVarInLoops();
        demonstrateVarWithComplexTypes();
        demonstrateUnmodifiableCollections();
        demonstrateOptionalEnhancements();
        demonstratePracticalUsage();
    }
    
    public static void demonstrateVarBasics() {
        System.out.println("\n1. Basic var Usage");
        System.out.println("==================");
        
        // Primitive types
        var name = "Java 10";              // String
        var version = 10;                  // int
        var price = 99.99;                 // double
        var isLatest = false;              // boolean
        
        System.out.println("Name: " + name + " (Type: " + name.getClass().getSimpleName() + ")");
        System.out.println("Version: " + version + " (Type: int)");
        System.out.println("Price: $" + price + " (Type: double)");
        System.out.println("Is Latest: " + isLatest + " (Type: boolean)");
        
        // Collection types - type inferred from right side
        var numbers = new ArrayList<Integer>();
        numbers.addAll(Arrays.asList(1, 2, 3, 4, 5));
        
        var userMap = new HashMap<String, String>();
        userMap.put("admin", "Administrator");
        userMap.put("user", "Regular User");
        
        System.out.println("\nCollections:");
        System.out.println("Numbers: " + numbers);
        System.out.println("User Map: " + userMap);
        
        // Factory methods - very clear type inference
        var fruits = List.of("apple", "banana", "cherry");
        var scores = Map.of("Alice", 95, "Bob", 87, "Charlie", 92);
        
        System.out.println("Fruits: " + fruits);
        System.out.println("Scores: " + scores);
    }
    
    public static void demonstrateVarInLoops() {
        System.out.println("\n2. var in Loop Constructs");
        System.out.println("==========================");
        
        var words = Arrays.asList("hello", "world", "java", "programming");
        
        // Enhanced for loop
        System.out.println("Enhanced for loop:");
        for (var word : words) {
            System.out.println("  Word: " + word + " (length: " + word.length() + ")");
        }
        
        // Traditional for loop
        System.out.println("\nTraditional for loop:");
        for (var i = 0; i < words.size(); i++) {
            var word = words.get(i);
            System.out.printf("  Index %d: %s%n", i, word);
        }
        
        // Stream operations with var
        System.out.println("\nStream with var:");
        var longWords = words.stream()
            .filter(word -> word.length() > 4)
            .collect(Collectors.toList());
        
        for (var longWord : longWords) {
            System.out.println("  Long word: " + longWord);
        }
    }
    
    public static void demonstrateVarWithComplexTypes() {
        System.out.println("\n3. var with Complex Types");
        System.out.println("========================");
        
        // Complex generic types - this is where var really shines!
        var ordersByCustomer = new HashMap<String, List<Order>>();
        var customerPreferences = new ConcurrentHashMap<CustomerId, Set<ProductCategory>>();
        var processingQueue = new LinkedList<ProcessingTask<CustomerData, OrderResult>>();
        
        // Populate some test data
        var customer1Orders = Arrays.asList(
            new Order("ORD001", "Laptop", 999.99),
            new Order("ORD002", "Mouse", 29.99)
        );
        ordersByCustomer.put("customer1", customer1Orders);
        
        System.out.println("Orders by customer:");
        ordersByCustomer.forEach((customer, orders) -> {
            System.out.printf("  %s has %d orders%n", customer, orders.size());
            orders.forEach(order -> 
                System.out.printf("    - %s: $%.2f%n", order.getProduct(), order.getPrice()));
        });
        
        // Complex stream operations
        var totalOrderValue = ordersByCustomer.values().stream()
            .flatMap(List::stream)
            .mapToDouble(Order::getPrice)
            .sum();
        
        System.out.printf("\nTotal order value: $%.2f%n", totalOrderValue);
        
        // Builder pattern with var
        var config = DatabaseConfig.builder()
            .host("localhost")
            .port(5432)
            .database("myapp")
            .username("admin")
            .build();
        
        System.out.println("Database config: " + config);
    }
    
    public static void demonstrateUnmodifiableCollections() {
        System.out.println("\n4. Unmodifiable Collections (Java 10 Enhancement)");
        System.out.println("================================================");
        
        var sourceList = Arrays.asList("apple", "banana", "cherry", "date");
        
        // Java 10 way - direct collectors
        var immutableList = sourceList.stream()
            .filter(fruit -> fruit.length() > 4)
            .collect(Collectors.toUnmodifiableList());
        
        var immutableSet = sourceList.stream()
            .collect(Collectors.toUnmodifiableSet());
        
        var immutableMap = sourceList.stream()
            .collect(Collectors.toUnmodifiableMap(
                Function.identity(),
                String::length
            ));
        
        System.out.println("Immutable list: " + immutableList);
        System.out.println("Immutable set: " + immutableSet);
        System.out.println("Immutable map: " + immutableMap);
        
        // Demonstrate immutability
        try {
            immutableList.add("elderberry");
        } catch (UnsupportedOperationException e) {
            System.out.println("✓ List is truly immutable: " + e.getClass().getSimpleName());
        }
        
        try {
            immutableMap.put("grape", 5);
        } catch (UnsupportedOperationException e) {
            System.out.println("✓ Map is truly immutable: " + e.getClass().getSimpleName());
        }
    }
    
    public static void demonstrateOptionalEnhancements() {
        System.out.println("\n5. Optional.orElseThrow() Enhancement");
        System.out.println("===================================");
        
        var users = Arrays.asList(
            new User("alice", "Alice Smith"),
            new User("bob", "Bob Jones"),
            new User("charlie", "Charlie Brown")
        );
        
        // Simulate user repository
        Function<String, Optional<User>> findUser = username -> 
            users.stream()
                 .filter(user -> user.getUsername().equals(username))
                 .findFirst();
        
        // Java 10 enhancement - orElseThrow() without parameter
        try {
            var existingUser = findUser.apply("alice").orElseThrow();
            System.out.println("Found user: " + existingUser.getDisplayName());
        } catch (NoSuchElementException e) {
            System.out.println("User not found");
        }
        
        try {
            var nonExistentUser = findUser.apply("dave").orElseThrow();
            System.out.println("This won't execute");
        } catch (NoSuchElementException e) {
            System.out.println("✓ User 'dave' not found - threw NoSuchElementException");
        }
        
        // Practical usage in service methods
        var userService = new UserService(users);
        try {
            var user = userService.getUserByUsername("bob");
            System.out.println("Service found user: " + user.getDisplayName());
        } catch (NoSuchElementException e) {
            System.out.println("Service couldn't find user");
        }
    }
    
    public static void demonstratePracticalUsage() throws Exception {
        System.out.println("\n6. Practical Usage Scenarios");
        System.out.println("============================");
        
        // File processing with var
        var tempFile = Files.createTempFile("java10-demo", ".txt");
        var content = Arrays.asList(
            "Java 10 introduces var keyword",
            "Type inference makes code more readable",
            "Unmodifiable collections are easier to create",
            "Performance improvements in G1GC"
        );
        
        Files.write(tempFile, content);
        
        try (var scanner = new Scanner(tempFile)) {
            var lineNumber = 1;
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                System.out.printf("%d: %s%n", lineNumber++, line);
            }
        }
        
        // Data processing pipeline
        var processor = new DataProcessor();
        var inputData = Arrays.asList(
            new DataPoint("sensor1", 23.5, System.currentTimeMillis()),
            new DataPoint("sensor2", 24.1, System.currentTimeMillis()),
            new DataPoint("sensor3", 22.8, System.currentTimeMillis())
        );
        
        var processedResults = processor.process(inputData);
        System.out.println("\nProcessed data:");
        processedResults.forEach(result -> 
            System.out.printf("  %s: %.2f (processed at %d)%n", 
                result.getSensorId(), result.getValue(), result.getTimestamp()));
        
        // Clean up
        Files.deleteIfExists(tempFile);
    }
    
    // Supporting classes for examples
    static class Order {
        private String orderId;
        private String product;
        private double price;
        
        public Order(String orderId, String product, double price) {
            this.orderId = orderId;
            this.product = product;
            this.price = price;
        }
        
        public String getOrderId() { return orderId; }
        public String getProduct() { return product; }
        public double getPrice() { return price; }
        
        @Override
        public String toString() {
            return String.format("Order{id='%s', product='%s', price=%.2f}", 
                               orderId, product, price);
        }
    }
    
    static class CustomerId {
        private String id;
        public CustomerId(String id) { this.id = id; }
        public String getId() { return id; }
        @Override public String toString() { return id; }
    }
    
    enum ProductCategory { ELECTRONICS, CLOTHING, BOOKS, HOME }
    
    static class ProcessingTask<T, R> {
        private T input;
        private Function<T, R> processor;
        
        public ProcessingTask(T input, Function<T, R> processor) {
            this.input = input;
            this.processor = processor;
        }
        
        public R execute() { return processor.apply(input); }
    }
    
    static class CustomerData {
        private String customerId;
        public CustomerData(String customerId) { this.customerId = customerId; }
        public String getCustomerId() { return customerId; }
    }
    
    static class OrderResult {
        private boolean success;
        public OrderResult(boolean success) { this.success = success; }
        public boolean isSuccess() { return success; }
    }
    
    static class DatabaseConfig {
        private String host, database, username;
        private int port;
        
        private DatabaseConfig(Builder builder) {
            this.host = builder.host;
            this.port = builder.port;
            this.database = builder.database;
            this.username = builder.username;
        }
        
        public static Builder builder() { return new Builder(); }
        
        @Override
        public String toString() {
            return String.format("DatabaseConfig{host='%s', port=%d, database='%s', username='%s'}", 
                               host, port, database, username);
        }
        
        static class Builder {
            private String host, database, username;
            private int port;
            
            public Builder host(String host) { this.host = host; return this; }
            public Builder port(int port) { this.port = port; return this; }
            public Builder database(String database) { this.database = database; return this; }
            public Builder username(String username) { this.username = username; return this; }
            public DatabaseConfig build() { return new DatabaseConfig(this); }
        }
    }
    
    static class User {
        private String username;
        private String displayName;
        
        public User(String username, String displayName) {
            this.username = username;
            this.displayName = displayName;
        }
        
        public String getUsername() { return username; }
        public String getDisplayName() { return displayName; }
        
        @Override
        public String toString() {
            return String.format("User{username='%s', displayName='%s'}", username, displayName);
        }
    }
    
    static class UserService {
        private List<User> users;
        
        public UserService(List<User> users) {
            this.users = new ArrayList<>(users);
        }
        
        public User getUserByUsername(String username) {
            return users.stream()
                       .filter(user -> user.getUsername().equals(username))
                       .findFirst()
                       .orElseThrow(); // Java 10 enhancement!
        }
    }
    
    static class DataPoint {
        private String sensorId;
        private double value;
        private long timestamp;
        
        public DataPoint(String sensorId, double value, long timestamp) {
            this.sensorId = sensorId;
            this.value = value;
            this.timestamp = timestamp;
        }
        
        public String getSensorId() { return sensorId; }
        public double getValue() { return value; }
        public long getTimestamp() { return timestamp; }
    }
    
    static class DataProcessor {
        public List<DataPoint> process(List<DataPoint> input) {
            return input.stream()
                       .map(point -> new DataPoint(
                           point.getSensorId(),
                           point.getValue() * 1.1, // Simple processing
                           System.currentTimeMillis()
                       ))
                       .collect(Collectors.toUnmodifiableList());
        }
    }
}