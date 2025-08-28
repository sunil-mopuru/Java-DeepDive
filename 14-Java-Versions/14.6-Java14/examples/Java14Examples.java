/**
 * Java 14 Features Examples - Beginner to Advanced
 * 
 * Demonstrates Java 14 features including Records (Preview), Pattern Matching for instanceof (Preview),
 * Text Blocks (Second Preview), and Helpful NullPointerExceptions.
 */

import java.util.*;
import java.util.stream.*;

public class Java14Examples {
    
    public static void main(String[] args) {
        System.out.println("=== Java 14 Features Examples ===");
        
        // BEGINNER LEVEL
        demonstrateRecords();
        showPatternMatching();
        
        // INTERMEDIATE LEVEL
        exploreTextBlocksEnhancements();
        showHelpfulNullPointers();
        
        // ADVANCED LEVEL
        buildComplexExamples();
        performanceAnalysis();
    }
    
    public static void demonstrateRecords() {
        System.out.println("\n1. Records (Preview Feature)");
        System.out.println("============================");
        
        // Basic record usage
        Person person = new Person("John Doe", 30, "john@example.com");
        System.out.println("Person: " + person);
        System.out.println("Name: " + person.name());
        System.out.println("Age: " + person.age());
        System.out.println("Email: " + person.email());
        
        // Records with validation
        try {
            BankAccount account = new BankAccount("ACC123", 1000.0);
            System.out.println("Account created: " + account);
            
            BankAccount invalidAccount = new BankAccount("", -500.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
        
        // Records in collections
        List<Product> products = List.of(
            new Product("Laptop", 999.99, "Electronics"),
            new Product("Book", 19.99, "Education"),
            new Product("Coffee", 4.50, "Food")
        );
        
        System.out.println("\nProducts:");
        products.forEach(System.out::println);
        
        // Finding expensive products
        products.stream()
               .filter(p -> p.price() > 50)
               .forEach(p -> System.out.println("Expensive: " + p.name() + " - $" + p.price()));
        
        // Records as Map keys
        Map<Coordinate, String> locations = Map.of(
            new Coordinate(0, 0), "Origin",
            new Coordinate(10, 20), "Point A",
            new Coordinate(-5, 15), "Point B"
        );
        
        System.out.println("\nLocation map:");
        locations.forEach((coord, name) -> 
            System.out.println(name + " at " + coord));
    }
    
    public static void showPatternMatching() {
        System.out.println("\n2. Pattern Matching for instanceof (Preview)");
        System.out.println("============================================");
        
        List<Object> objects = List.of(
            "Hello World",
            42,
            3.14,
            List.of(1, 2, 3),
            new Person("Alice", 25, "alice@test.com")
        );
        
        System.out.println("Processing objects with pattern matching:");
        objects.forEach(Java14Examples::processObject);
        
        // Practical usage in method
        System.out.println("\nGeometric shapes processing:");
        List<Shape> shapes = List.of(
            new Circle(5.0),
            new Rectangle(10.0, 8.0),
            new Square(6.0)
        );
        
        shapes.forEach(shape -> {
            double area = calculateArea(shape);
            String description = describeShape(shape);
            System.out.printf("%s - Area: %.2f%n", description, area);
        });
    }
    
    public static void processObject(Object obj) {
        // Traditional way (verbose)
        /*
        if (obj instanceof String) {
            String str = (String) obj;
            System.out.println("String with length: " + str.length());
        }
        */
        
        // Java 14 pattern matching (concise)
        if (obj instanceof String str) {
            System.out.println("String: '" + str + "' (length: " + str.length() + ")");
        } else if (obj instanceof Integer num) {
            System.out.println("Integer: " + num + " (even: " + (num % 2 == 0) + ")");
        } else if (obj instanceof Double dbl) {
            System.out.println("Double: " + dbl + " (rounded: " + Math.round(dbl) + ")");
        } else if (obj instanceof List<?> list) {
            System.out.println("List with " + list.size() + " elements: " + list);
        } else if (obj instanceof Person person) {
            System.out.println("Person: " + person.name() + " (" + person.age() + " years old)");
        } else {
            System.out.println("Unknown type: " + obj.getClass().getSimpleName());
        }
    }
    
    public static double calculateArea(Shape shape) {
        if (shape instanceof Circle circle) {
            return Math.PI * circle.radius() * circle.radius();
        } else if (shape instanceof Rectangle rect) {
            return rect.width() * rect.height();
        } else if (shape instanceof Square square) {
            return square.side() * square.side();
        }
        return 0.0;
    }
    
    public static String describeShape(Shape shape) {
        if (shape instanceof Circle circle) {
            return "Circle with radius " + circle.radius();
        } else if (shape instanceof Rectangle rect) {
            return String.format("Rectangle %.1f x %.1f", rect.width(), rect.height());
        } else if (shape instanceof Square square) {
            return "Square with side " + square.side();
        }
        return "Unknown shape";
    }
    
    public static void exploreTextBlocksEnhancements() {
        System.out.println("\n3. Text Blocks Enhancements (Second Preview)");
        System.out.println("=============================================");
        
        // Enhanced escape sequences
        String textBlock = """
                Line 1
                Line 2 \
                continues here
                Line 3 with "quotes"
                Line 4 with \s\s\sspaces
                """;
        
        System.out.println("Enhanced text block:");
        System.out.println(textBlock);
        
        // JSON with better formatting
        String jsonConfig = """
                {
                    "server": {
                        "host": "localhost",
                        "port": 8080,
                        "ssl": {
                            "enabled": true,
                            "keystore": "/path/to/keystore.jks"
                        }
                    },
                    "database": {
                        "url": "jdbc:postgresql://localhost:5432/mydb",
                        "username": "admin",
                        "pool": {
                            "min": 5,
                            "max": 20
                        }
                    }
                }
                """;
        
        System.out.println("JSON configuration:");
        System.out.println(jsonConfig);
        
        // SQL with complex formatting
        String analyticsQuery = """
                WITH monthly_sales AS (
                    SELECT 
                        DATE_TRUNC('month', order_date) as month,
                        product_category,
                        SUM(amount) as total_sales,
                        COUNT(*) as order_count,
                        AVG(amount) as avg_order_value
                    FROM orders o
                    JOIN products p ON o.product_id = p.id
                    WHERE order_date >= CURRENT_DATE - INTERVAL '12 months'
                    GROUP BY 1, 2
                ),
                growth_analysis AS (
                    SELECT 
                        month,
                        product_category,
                        total_sales,
                        LAG(total_sales) OVER (
                            PARTITION BY product_category 
                            ORDER BY month
                        ) as prev_month_sales,
                        (total_sales - LAG(total_sales) OVER (
                            PARTITION BY product_category 
                            ORDER BY month
                        )) / NULLIF(LAG(total_sales) OVER (
                            PARTITION BY product_category 
                            ORDER BY month
                        ), 0) * 100 as growth_rate
                    FROM monthly_sales
                )
                SELECT 
                    TO_CHAR(month, 'YYYY-MM') as period,
                    product_category,
                    total_sales,
                    ROUND(COALESCE(growth_rate, 0), 2) as growth_percentage
                FROM growth_analysis
                ORDER BY month DESC, product_category
                """;
        
        System.out.println("Complex analytics query:");
        System.out.println(analyticsQuery);
    }
    
    public static void showHelpfulNullPointers() {
        System.out.println("\n4. Helpful NullPointerExceptions");
        System.out.println("=================================");
        
        try {
            // This will generate a helpful NPE message
            Customer customer = null;
            String email = customer.getProfile().getEmail().toLowerCase();
        } catch (NullPointerException e) {
            System.out.println("Helpful NPE message:");
            System.out.println(e.getMessage());
        }
        
        try {
            // Array access NPE
            String[] names = null;
            String first = names[0];
        } catch (NullPointerException e) {
            System.out.println("Array access NPE:");
            System.out.println(e.getMessage());
        }
        
        try {
            // Method chain NPE
            List<String> items = Arrays.asList("a", null, "c");
            int length = items.get(1).length();
        } catch (NullPointerException e) {
            System.out.println("Method chain NPE:");
            System.out.println(e.getMessage());
        }
        
        // Demonstrating safer alternatives
        System.out.println("\nSafer alternatives:");
        demonstrateSaferApproaches();
    }
    
    public static void demonstrateSaferApproaches() {
        // Using Optional for null safety
        Optional<Customer> customerOpt = Optional.ofNullable(getCustomer());
        
        String email = customerOpt
            .map(Customer::getProfile)
            .map(Profile::getEmail)
            .map(String::toLowerCase)
            .orElse("no-email@example.com");
        
        System.out.println("Safe email extraction: " + email);
        
        // Using defensive programming
        List<String> items = Arrays.asList("hello", null, "world");
        items.stream()
             .filter(Objects::nonNull)
             .map(String::toUpperCase)
             .forEach(System.out::println);
    }
    
    public static void buildComplexExamples() {
        System.out.println("\n5. Complex Real-World Examples");
        System.out.println("==============================");
        
        // Order processing system
        OrderProcessor processor = new OrderProcessor();
        
        List<Order> orders = List.of(
            new Order("ORD001", List.of(
                new OrderItem("Laptop", 2, 999.99),
                new OrderItem("Mouse", 1, 29.99)
            )),
            new Order("ORD002", List.of(
                new OrderItem("Book", 3, 19.99),
                new OrderItem("Pen", 5, 2.50)
            ))
        );
        
        orders.forEach(processor::processOrder);
        
        // Data analysis with records
        DataAnalyzer analyzer = new DataAnalyzer();
        analyzer.analyzeSalesData();
    }
    
    public static void performanceAnalysis() {
        System.out.println("\n6. Performance Analysis");
        System.out.println("=======================");
        
        // Record vs Class performance
        compareRecordVsClass();
        
        // Pattern matching vs traditional instanceof
        comparePatternMatching();
    }
    
    public static void compareRecordVsClass() {
        System.out.println("Record vs Traditional Class performance:");
        
        int iterations = 1_000_000;
        
        // Record performance
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Point record = new Point(i, i * 2);
            int sum = record.x() + record.y();
        }
        long recordTime = System.nanoTime() - startTime;
        
        // Traditional class performance
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            PointClass traditional = new PointClass(i, i * 2);
            int sum = traditional.getX() + traditional.getY();
        }
        long classTime = System.nanoTime() - startTime;
        
        System.out.printf("Record: %.2f ms%n", recordTime / 1_000_000.0);
        System.out.printf("Class: %.2f ms%n", classTime / 1_000_000.0);
        System.out.printf("Ratio: %.2fx%n", (double) classTime / recordTime);
    }
    
    public static void comparePatternMatching() {
        System.out.println("\nPattern matching vs traditional instanceof:");
        
        List<Object> objects = IntStream.range(0, 100_000)
                                      .mapToObj(i -> i % 2 == 0 ? "String" + i : i)
                                      .collect(Collectors.toList());
        
        // Traditional instanceof
        long startTime = System.nanoTime();
        int count1 = 0;
        for (Object obj : objects) {
            if (obj instanceof String) {
                String str = (String) obj;
                count1 += str.length();
            }
        }
        long traditionalTime = System.nanoTime() - startTime;
        
        // Pattern matching
        startTime = System.nanoTime();
        int count2 = 0;
        for (Object obj : objects) {
            if (obj instanceof String str) {
                count2 += str.length();
            }
        }
        long patternTime = System.nanoTime() - startTime;
        
        System.out.printf("Traditional: %.2f ms (count: %d)%n", traditionalTime / 1_000_000.0, count1);
        System.out.printf("Pattern matching: %.2f ms (count: %d)%n", patternTime / 1_000_000.0, count2);
    }
    
    // Helper methods
    private static Customer getCustomer() {
        return null; // Simulating null return
    }
    
    // Record definitions (Java 14 Preview)
    public record Person(String name, int age, String email) {
        public Person {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            if (age < 0) {
                throw new IllegalArgumentException("Age cannot be negative");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Invalid email address");
            }
        }
    }
    
    public record Product(String name, double price, String category) {}
    
    public record Coordinate(int x, int y) {}
    
    public record Point(int x, int y) {}
    
    public record BankAccount(String accountNumber, double balance) {
        public BankAccount {
            if (accountNumber == null || accountNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Account number cannot be null or empty");
            }
            if (balance < 0) {
                throw new IllegalArgumentException("Balance cannot be negative");
            }
        }
    }
    
    // Shape hierarchy for pattern matching
    public sealed interface Shape permits Circle, Rectangle, Square {}
    
    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}
    public record Square(double side) implements Shape {}
    
    // Order processing records
    public record Order(String id, List<OrderItem> items) {}
    public record OrderItem(String name, int quantity, double price) {}
    
    // Supporting classes
    static class Customer {
        private Profile profile;
        public Profile getProfile() { return profile; }
    }
    
    static class Profile {
        private String email;
        public String getEmail() { return email; }
    }
    
    static class PointClass {
        private int x, y;
        public PointClass(int x, int y) { this.x = x; this.y = y; }
        public int getX() { return x; }
        public int getY() { return y; }
    }
    
    static class OrderProcessor {
        public void processOrder(Order order) {
            System.out.println("Processing order: " + order.id());
            
            double total = order.items().stream()
                              .mapToDouble(item -> item.quantity() * item.price())
                              .sum();
            
            System.out.printf("Total amount: $%.2f%n", total);
            
            order.items().forEach(item -> {
                if (item instanceof OrderItem orderItem && orderItem.quantity() > 1) {
                    System.out.printf("Bulk item: %s x %d%n", orderItem.name(), orderItem.quantity());
                }
            });
        }
    }
    
    static class DataAnalyzer {
        public void analyzeSalesData() {
            System.out.println("Analyzing sales data with records...");
            
            List<SaleRecord> sales = List.of(
                new SaleRecord("2024-01", "Electronics", 15000.0),
                new SaleRecord("2024-01", "Books", 5000.0),
                new SaleRecord("2024-02", "Electronics", 18000.0),
                new SaleRecord("2024-02", "Books", 4500.0)
            );
            
            Map<String, Double> monthlyTotals = sales.stream()
                .collect(Collectors.groupingBy(
                    SaleRecord::month,
                    Collectors.summingDouble(SaleRecord::amount)
                ));
            
            monthlyTotals.forEach((month, total) -> 
                System.out.printf("Month %s: $%.2f%n", month, total));
        }
    }
    
    public record SaleRecord(String month, String category, double amount) {}
}