/**
 * Java 15 Features Examples - Beginner to Advanced
 * 
 * Demonstrates Java 15 features including Sealed Classes (Preview), 
 * standardized Text Blocks, and enhanced Records (Second Preview).
 */

import java.time.*;
import java.util.*;
import java.util.function.*;

public class Java15Examples {
    
    public static void main(String[] args) {
        System.out.println("=== Java 15 Features Examples ===");
        
        // BEGINNER LEVEL
        demonstrateSealedClasses();
        showStandardizedTextBlocks();
        
        // INTERMEDIATE LEVEL
        exploreAdvancedSealing();
        buildTypeSafeSystems();
        
        // ADVANCED LEVEL
        createComplexHierarchies();
        performanceAnalysis();
    }
    
    public static void demonstrateSealedClasses() {
        System.out.println("\n1. Sealed Classes (Preview)");
        System.out.println("===========================");
        
        // Basic sealed interface usage
        List<Animal> animals = List.of(
            new Dog("Buddy", "Golden Retriever"),
            new Cat("Whiskers", 3),
            new Bird("Tweety", "Canary")
        );
        
        System.out.println("Animal sounds:");
        animals.forEach(animal -> {
            String sound = switch (animal) {
                case Dog dog -> dog.name() + " barks: Woof! (breed: " + dog.breed() + ")";
                case Cat cat -> cat.name() + " meows: Meow! (age: " + cat.age() + ")";
                case Bird bird -> bird.name() + " chirps: Tweet! (species: " + bird.species() + ")";
            };
            System.out.println(sound);
        });
        
        // Vehicle hierarchy
        List<Vehicle> vehicles = List.of(
            new Car("Toyota Camry", 4),
            new Motorcycle("Harley Davidson", 1200),
            new Truck("Ford F-150", 5000)
        );
        
        System.out.println("\nVehicle information:");
        vehicles.forEach(vehicle -> {
            String info = getVehicleInfo(vehicle);
            System.out.println(info);
        });
    }
    
    public static String getVehicleInfo(Vehicle vehicle) {
        return switch (vehicle) {
            case Car car -> String.format("Car: %s (%d doors)", car.model(), car.doors());
            case Motorcycle bike -> String.format("Motorcycle: %s (%d cc)", bike.model(), bike.engineSize());
            case Truck truck -> String.format("Truck: %s (%d kg capacity)", truck.model(), truck.capacity());
        };
    }
    
    public static void showStandardizedTextBlocks() {
        System.out.println("\n2. Standardized Text Blocks");
        System.out.println("============================");
        
        // Database query example
        String customerQuery = """
                SELECT c.customer_id, c.name, c.email, 
                       COUNT(o.order_id) as total_orders,
                       SUM(o.total_amount) as total_spent
                FROM customers c
                LEFT JOIN orders o ON c.customer_id = o.customer_id
                WHERE c.active = true
                  AND c.created_date >= '2023-01-01'
                GROUP BY c.customer_id, c.name, c.email
                HAVING COUNT(o.order_id) > 0
                ORDER BY total_spent DESC
                LIMIT 10
                """;
        
        System.out.println("Customer analytics query:");
        System.out.println(customerQuery);
        
        // Configuration file generation
        generateApplicationConfig();
        
        // Email template creation
        generateEmailTemplate();
        
        // API documentation
        generateApiDocumentation();
    }
    
    public static void generateApplicationConfig() {
        String appName = "MyApplication";
        int port = 8080;
        String environment = "production";
        
        String config = """
                # Application Configuration
                # Generated on: %s
                
                app:
                  name: %s
                  version: 1.5.0
                  environment: %s
                
                server:
                  port: %d
                  ssl:
                    enabled: true
                    keystore: /etc/ssl/keystore.jks
                  compression:
                    enabled: true
                    mime-types: text/html,application/json
                
                database:
                  url: jdbc:postgresql://localhost:5432/myapp
                  username: ${DB_USERNAME}
                  password: ${DB_PASSWORD}
                  hikari:
                    maximum-pool-size: 20
                    minimum-idle: 5
                    connection-timeout: 30000
                
                logging:
                  level:
                    com.mycompany: INFO
                    org.springframework: WARN
                  file:
                    name: /var/log/myapp.log
                    max-size: 100MB
                    max-history: 30
                """.formatted(LocalDateTime.now(), appName, environment, port);
        
        System.out.println("\nGenerated application configuration:");
        System.out.println(config);
    }
    
    public static void generateEmailTemplate() {
        String customerName = "John Doe";
        String orderNumber = "ORD-2024-001";
        double totalAmount = 129.99;
        
        String emailHtml = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Order Confirmation</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
                        .container { max-width: 600px; margin: 0 auto; background: #ffffff; }
                        .header { background: #007bff; color: white; padding: 30px; text-align: center; }
                        .content { padding: 30px; }
                        .order-details { background: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0; }
                        .footer { background: #6c757d; color: white; padding: 20px; text-align: center; }
                        .button { background: #28a745; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Order Confirmation</h1>
                            <p>Thank you for your purchase!</p>
                        </div>
                        
                        <div class="content">
                            <p>Dear %s,</p>
                            
                            <p>We're excited to confirm that your order has been received and is being processed.</p>
                            
                            <div class="order-details">
                                <h3>Order Details</h3>
                                <p><strong>Order Number:</strong> %s</p>
                                <p><strong>Order Total:</strong> $%.2f</p>
                                <p><strong>Order Date:</strong> %s</p>
                            </div>
                            
                            <p>You can track your order status by clicking the button below:</p>
                            
                            <p style="text-align: center;">
                                <a href="#" class="button">Track Your Order</a>
                            </p>
                            
                            <p>If you have any questions, please don't hesitate to contact our customer service team.</p>
                        </div>
                        
                        <div class="footer">
                            <p>&copy; 2024 Our Company. All rights reserved.</p>
                            <p>123 Business Street, City, State 12345</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(customerName, orderNumber, totalAmount, LocalDate.now());
        
        System.out.println("\nGenerated order confirmation email:");
        System.out.println(emailHtml.substring(0, Math.min(500, emailHtml.length())) + "...");
    }
    
    public static void generateApiDocumentation() {
        String apiVersion = "v1";
        String serviceName = "User Management";
        
        String apiDoc = """
                # %s API Documentation
                
                ## Overview
                The %s API provides endpoints for managing user accounts, authentication, and user profiles.
                
                **Base URL:** `https://api.example.com/%s`
                
                **Authentication:** Bearer token required in Authorization header
                
                ## Endpoints
                
                ### GET /users
                Retrieve a list of all users (admin only)
                
                **Parameters:**
                - `page` (optional): Page number for pagination (default: 1)
                - `limit` (optional): Number of users per page (default: 20)
                - `status` (optional): Filter by user status (active, inactive, suspended)
                
                **Response:**
                ```json
                {
                    "status": "success",
                    "data": {
                        "users": [
                            {
                                "id": 1,
                                "username": "john_doe",
                                "email": "john@example.com",
                                "status": "active",
                                "created_at": "2024-01-15T10:30:00Z"
                            }
                        ],
                        "pagination": {
                            "current_page": 1,
                            "total_pages": 5,
                            "total_users": 87
                        }
                    }
                }
                ```
                
                ### POST /users
                Create a new user account
                
                **Request Body:**
                ```json
                {
                    "username": "new_user",
                    "email": "newuser@example.com",
                    "password": "secure_password",
                    "profile": {
                        "first_name": "John",
                        "last_name": "Doe",
                        "phone": "+1234567890"
                    }
                }
                ```
                
                **Response:**
                ```json
                {
                    "status": "success",
                    "data": {
                        "user_id": 123,
                        "message": "User created successfully"
                    }
                }
                ```
                
                ### GET /users/{id}
                Retrieve specific user information
                
                **Path Parameters:**
                - `id`: User ID (integer)
                
                **Response:**
                ```json
                {
                    "status": "success",
                    "data": {
                        "user": {
                            "id": 123,
                            "username": "john_doe",
                            "email": "john@example.com",
                            "profile": {
                                "first_name": "John",
                                "last_name": "Doe",
                                "phone": "+1234567890",
                                "avatar_url": "https://example.com/avatars/john_doe.jpg"
                            },
                            "status": "active",
                            "last_login": "2024-01-20T15:45:00Z"
                        }
                    }
                }
                ```
                
                ## Error Responses
                
                All error responses follow this format:
                ```json
                {
                    "status": "error",
                    "error": {
                        "code": "VALIDATION_ERROR",
                        "message": "Invalid input data",
                        "details": {
                            "field": "email",
                            "issue": "Email format is invalid"
                        }
                    }
                }
                ```
                
                ## Rate Limiting
                
                API requests are limited to:
                - **Free tier:** 100 requests/hour
                - **Premium tier:** 1000 requests/hour
                - **Enterprise tier:** Unlimited
                
                Rate limit headers are included in all responses:
                - `X-RateLimit-Limit`: Maximum requests allowed
                - `X-RateLimit-Remaining`: Requests remaining in current window
                - `X-RateLimit-Reset`: Timestamp when the rate limit resets
                """.formatted(serviceName, serviceName, apiVersion);
        
        System.out.println("\nGenerated API documentation:");
        System.out.println(apiDoc.substring(0, Math.min(800, apiDoc.length())) + "...");
    }
    
    public static void exploreAdvancedSealing() {
        System.out.println("\n3. Advanced Sealed Class Patterns");
        System.out.println("==================================");
        
        // Result type pattern
        Result<String> successResult = Result.success("Operation completed");
        Result<String> errorResult = Result.failure("Network timeout");
        
        System.out.println("Result processing:");
        System.out.println(processResult(successResult));
        System.out.println(processResult(errorResult));
        
        // Expression evaluation
        Expression expr = new BinaryOp(
            new Literal(10),
            Operator.MULTIPLY,
            new BinaryOp(
                new Literal(5),
                Operator.ADD,
                new Literal(3)
            )
        );
        
        System.out.println("\nExpression evaluation:");
        System.out.println("Expression: " + formatExpression(expr));
        System.out.println("Result: " + expr.evaluate());
        
        // State machine example
        demonstrateStateMachine();
    }
    
    public static String processResult(Result<String> result) {
        return switch (result) {
            case Success<String> success -> "✓ Success: " + success.value();
            case Failure<String> failure -> "✗ Error: " + failure.error();
        };
    }
    
    public static String formatExpression(Expression expr) {
        return switch (expr) {
            case Literal lit -> String.valueOf(lit.value());
            case BinaryOp binOp -> String.format("(%s %s %s)", 
                formatExpression(binOp.left()), 
                getOperatorSymbol(binOp.operator()), 
                formatExpression(binOp.right()));
            case UnaryOp unOp -> getUnarySymbol(unOp.operator()) + formatExpression(unOp.operand());
        };
    }
    
    public static void demonstrateStateMachine() {
        System.out.println("\nOrder state machine:");
        
        OrderState state = new Pending();
        System.out.println("Initial state: " + formatState(state));
        
        state = state.nextState(OrderEvent.CONFIRM);
        System.out.println("After confirmation: " + formatState(state));
        
        state = state.nextState(OrderEvent.SHIP);
        System.out.println("After shipping: " + formatState(state));
        
        state = state.nextState(OrderEvent.DELIVER);
        System.out.println("After delivery: " + formatState(state));
    }
    
    public static String formatState(OrderState state) {
        return switch (state) {
            case Pending() -> "Order is pending";
            case Confirmed(var time) -> "Order confirmed at " + time;
            case Shipped(var tracking, var time) -> "Order shipped (tracking: " + tracking + ") at " + time;
            case Delivered(var time) -> "Order delivered at " + time;
            case Cancelled(var reason, var time) -> "Order cancelled: " + reason + " at " + time;
        };
    }
    
    public static void buildTypeSafeSystems() {
        System.out.println("\n4. Type-Safe System Design");
        System.out.println("===========================");
        
        // HTTP response handling
        List<HttpResponse> responses = List.of(
            new Success(200, "Request successful", "{\"data\": \"value\"}"),
            new ClientError(404, "Not found", "Resource does not exist"),
            new ServerError(500, "Internal error", "Database connection failed")
        );
        
        System.out.println("HTTP Response handling:");
        responses.forEach(response -> {
            String handling = handleResponse(response);
            System.out.println(handling);
        });
        
        // Payment processing
        processPayments();
    }
    
    public static String handleResponse(HttpResponse response) {
        return switch (response) {
            case Success success -> 
                "✓ Success (" + success.code() + "): " + success.body();
            case ClientError clientError -> 
                "⚠ Client Error (" + clientError.code() + "): " + clientError.message();
            case ServerError serverError -> 
                "✗ Server Error (" + serverError.code() + "): " + serverError.message();
        };
    }
    
    public static void processPayments() {
        System.out.println("\nPayment processing:");
        
        List<PaymentResult> results = List.of(
            new PaymentSuccess("TXN001", 99.99, Instant.now()),
            new PaymentFailure("TXN002", "INSUFFICIENT_FUNDS", "Not enough balance"),
            new PaymentPending("TXN003", "BANK_PROCESSING", Instant.now().plusSeconds(300))
        );
        
        results.forEach(result -> {
            String status = switch (result) {
                case PaymentSuccess success -> 
                    String.format("✓ Payment successful: $%.2f (ID: %s)", 
                                success.amount(), success.transactionId());
                case PaymentFailure failure -> 
                    String.format("✗ Payment failed: %s - %s (ID: %s)", 
                                failure.errorCode(), failure.message(), failure.transactionId());
                case PaymentPending pending -> 
                    String.format("⏳ Payment pending: %s (ID: %s, ETA: %s)", 
                                pending.reason(), pending.transactionId(), pending.expectedCompletion());
            };
            System.out.println(status);
        });
    }
    
    public static void createComplexHierarchies() {
        System.out.println("\n5. Complex Sealed Hierarchies");
        System.out.println("==============================");
        
        // File system hierarchy
        FileSystemEntry root = new Directory("root", List.of(
            new RegularFile("document.txt", 1024, "text/plain"),
            new Directory("images", List.of(
                new RegularFile("photo1.jpg", 2048576, "image/jpeg"),
                new RegularFile("photo2.png", 1536000, "image/png")
            )),
            new SymbolicLink("shortcut", "/path/to/target")
        ));
        
        System.out.println("File system structure:");
        printFileSystem(root, 0);
        
        System.out.println("\nTotal size: " + calculateTotalSize(root) + " bytes");
    }
    
    public static void printFileSystem(FileSystemEntry entry, int depth) {
        String indent = "  ".repeat(depth);
        String info = switch (entry) {
            case RegularFile file -> 
                String.format("%s📄 %s (%d bytes, %s)", 
                            indent, file.name(), file.size(), file.mimeType());
            case Directory dir -> {
                System.out.println(String.format("%s📁 %s/", indent, dir.name()));
                dir.contents().forEach(child -> printFileSystem(child, depth + 1));
                yield "";
            }
            case SymbolicLink link -> 
                String.format("%s🔗 %s -> %s", 
                            indent, link.name(), link.target());
        };
        
        if (!info.isEmpty()) {
            System.out.println(info);
        }
    }
    
    public static long calculateTotalSize(FileSystemEntry entry) {
        return switch (entry) {
            case RegularFile file -> file.size();
            case Directory dir -> dir.contents().stream()
                                    .mapToLong(Java15Examples::calculateTotalSize)
                                    .sum();
            case SymbolicLink link -> 0; // Symbolic links don't contribute to size
        };
    }
    
    public static void performanceAnalysis() {
        System.out.println("\n6. Performance Analysis");
        System.out.println("=======================");
        
        // Sealed class vs traditional polymorphism
        compareSealedVsTraditional();
        
        // Pattern matching performance
        comparePatternMatching();
    }
    
    public static void compareSealedVsTraditional() {
        System.out.println("Sealed classes vs traditional polymorphism:");
        
        List<Shape> shapes = IntStream.range(0, 1_000_000)
            .mapToObj(i -> switch (i % 3) {
                case 0 -> new Circle(i);
                case 1 -> new Rectangle(i, i + 1);
                default -> new Triangle(i, i + 1);
            })
            .collect(Collectors.toList());
        
        // Pattern matching approach
        long startTime = System.nanoTime();
        double totalArea1 = shapes.stream()
            .mapToDouble(shape -> switch (shape) {
                case Circle circle -> Math.PI * circle.radius() * circle.radius();
                case Rectangle rect -> rect.width() * rect.height();
                case Triangle tri -> 0.5 * tri.base() * tri.height();
            })
            .sum();
        long patternTime = System.nanoTime() - startTime;
        
        // Traditional polymorphic approach
        startTime = System.nanoTime();
        double totalArea2 = shapes.stream()
            .mapToDouble(Shape::area)
            .sum();
        long polymorphicTime = System.nanoTime() - startTime;
        
        System.out.printf("Pattern matching: %.2f ms (total area: %.2f)%n", 
                         patternTime / 1_000_000.0, totalArea1);
        System.out.printf("Polymorphic calls: %.2f ms (total area: %.2f)%n", 
                         polymorphicTime / 1_000_000.0, totalArea2);
    }
    
    public static void comparePatternMatching() {
        System.out.println("\nPattern matching performance comparison:");
        
        List<Object> mixed = IntStream.range(0, 500_000)
            .mapToObj(i -> switch (i % 4) {
                case 0 -> "String" + i;
                case 1 -> i;
                case 2 -> (double) i;
                default -> List.of(i);
            })
            .collect(Collectors.toList());
        
        // Pattern matching approach (Java 15)
        long startTime = System.nanoTime();
        long count1 = mixed.stream()
            .mapToLong(obj -> switch (obj) {
                case String str -> str.length();
                case Integer num -> num;
                case Double dbl -> dbl.longValue();
                case List<?> list -> list.size();
                default -> 0;
            })
            .sum();
        long patternTime = System.nanoTime() - startTime;
        
        // Traditional instanceof approach
        startTime = System.nanoTime();
        long count2 = 0;
        for (Object obj : mixed) {
            if (obj instanceof String str) {
                count2 += str.length();
            } else if (obj instanceof Integer num) {
                count2 += num;
            } else if (obj instanceof Double dbl) {
                count2 += dbl.longValue();
            } else if (obj instanceof List<?> list) {
                count2 += list.size();
            }
        }
        long instanceofTime = System.nanoTime() - startTime;
        
        System.out.printf("Pattern matching: %.2f ms (sum: %d)%n", 
                         patternTime / 1_000_000.0, count1);
        System.out.printf("Traditional instanceof: %.2f ms (sum: %d)%n", 
                         instanceofTime / 1_000_000.0, count2);
    }
    
    // Helper methods
    private static String getOperatorSymbol(Operator op) {
        return switch (op) {
            case ADD -> "+";
            case SUBTRACT -> "-";
            case MULTIPLY -> "*";
            case DIVIDE -> "/";
        };
    }
    
    private static String getUnarySymbol(UnaryOperator op) {
        return switch (op) {
            case NEGATE -> "-";
            case SQRT -> "√";
        };
    }
    
    private static String generateTrackingNumber() {
        return "TRK" + System.currentTimeMillis();
    }
    
    // Sealed class definitions
    public sealed interface Animal permits Dog, Cat, Bird {}
    public record Dog(String name, String breed) implements Animal {}
    public record Cat(String name, int age) implements Animal {}
    public record Bird(String name, String species) implements Animal {}
    
    public sealed interface Vehicle permits Car, Motorcycle, Truck {}
    public record Car(String model, int doors) implements Vehicle {}
    public record Motorcycle(String model, int engineSize) implements Vehicle {}
    public record Truck(String model, int capacity) implements Vehicle {}
    
    public sealed interface Shape permits Circle, Rectangle, Triangle {
        double area();
    }
    
    public record Circle(double radius) implements Shape {
        @Override
        public double area() { return Math.PI * radius * radius; }
    }
    
    public record Rectangle(double width, double height) implements Shape {
        @Override
        public double area() { return width * height; }
    }
    
    public record Triangle(double base, double height) implements Shape {
        @Override
        public double area() { return 0.5 * base * height; }
    }
    
    // Result type
    public sealed interface Result<T> permits Success, Failure {
        static <T> Result<T> success(T value) { return new Success<>(value); }
        static <T> Result<T> failure(String error) { return new Failure<>(error); }
    }
    
    public record Success<T>(T value) implements Result<T> {}
    public record Failure<T>(String error) implements Result<T> {}
    
    // Expression hierarchy
    public sealed interface Expression permits Literal, BinaryOp, UnaryOp {
        double evaluate();
    }
    
    public record Literal(double value) implements Expression {
        @Override
        public double evaluate() { return value; }
    }
    
    public record BinaryOp(Expression left, Operator operator, Expression right) implements Expression {
        @Override
        public double evaluate() {
            return switch (operator) {
                case ADD -> left.evaluate() + right.evaluate();
                case SUBTRACT -> left.evaluate() - right.evaluate();
                case MULTIPLY -> left.evaluate() * right.evaluate();
                case DIVIDE -> left.evaluate() / right.evaluate();
            };
        }
    }
    
    public record UnaryOp(UnaryOperator operator, Expression operand) implements Expression {
        @Override
        public double evaluate() {
            return switch (operator) {
                case NEGATE -> -operand.evaluate();
                case SQRT -> Math.sqrt(operand.evaluate());
            };
        }
    }
    
    public enum Operator { ADD, SUBTRACT, MULTIPLY, DIVIDE }
    public enum UnaryOperator { NEGATE, SQRT }
    
    // Order state machine
    public sealed interface OrderState permits Pending, Confirmed, Shipped, Delivered, Cancelled {
        default OrderState nextState(OrderEvent event) {
            return switch (this) {
                case Pending() -> switch (event) {
                    case CONFIRM -> new Confirmed(Instant.now());
                    case CANCEL -> new Cancelled("Customer cancellation", Instant.now());
                    default -> this;
                };
                case Confirmed(var time) -> switch (event) {
                    case SHIP -> new Shipped(generateTrackingNumber(), Instant.now());
                    case CANCEL -> new Cancelled("Cancelled after confirmation", Instant.now());
                    default -> this;
                };
                case Shipped(var tracking, var time) -> switch (event) {
                    case DELIVER -> new Delivered(Instant.now());
                    default -> this;
                };
                case Delivered(var time), Cancelled(var reason, var time2) -> this;
            };
        }
    }
    
    public record Pending() implements OrderState {}
    public record Confirmed(Instant confirmedAt) implements OrderState {}
    public record Shipped(String trackingNumber, Instant shippedAt) implements OrderState {}
    public record Delivered(Instant deliveredAt) implements OrderState {}
    public record Cancelled(String reason, Instant cancelledAt) implements OrderState {}
    
    public enum OrderEvent { CONFIRM, SHIP, DELIVER, CANCEL }
    
    // HTTP Response hierarchy
    public sealed interface HttpResponse permits Success, ClientError, ServerError {}
    public record Success(int code, String message, String body) implements HttpResponse {}
    public record ClientError(int code, String message, String details) implements HttpResponse {}
    public record ServerError(int code, String message, String details) implements HttpResponse {}
    
    // Payment result hierarchy
    public sealed interface PaymentResult permits PaymentSuccess, PaymentFailure, PaymentPending {}
    public record PaymentSuccess(String transactionId, double amount, Instant timestamp) implements PaymentResult {}
    public record PaymentFailure(String transactionId, String errorCode, String message) implements PaymentResult {}
    public record PaymentPending(String transactionId, String reason, Instant expectedCompletion) implements PaymentResult {}
    
    // File system hierarchy
    public sealed interface FileSystemEntry permits RegularFile, Directory, SymbolicLink {}
    public record RegularFile(String name, long size, String mimeType) implements FileSystemEntry {}
    public record Directory(String name, List<FileSystemEntry> contents) implements FileSystemEntry {}
    public record SymbolicLink(String name, String target) implements FileSystemEntry {}
}