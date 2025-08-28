/**
 * Java 13 Features Examples - Beginner to Advanced
 * 
 * Demonstrates Java 13 preview features including Text Blocks and
 * enhanced Switch Expressions with yield statements.
 */

import java.util.*;
import java.util.stream.*;
import java.time.*;

public class Java13Examples {
    
    public static void main(String[] args) {
        System.out.println("=== Java 13 Features Examples ===");
        
        // BEGINNER LEVEL
        demonstrateTextBlocks();
        showEnhancedSwitchExpressions();
        
        // INTERMEDIATE LEVEL
        exploreTextBlockFormatting();
        buildConfigurationTemplates();
        
        // ADVANCED LEVEL
        createComplexApplications();
        performanceComparisons();
    }
    
    public static void demonstrateTextBlocks() {
        System.out.println("\n1. Text Blocks (Preview)");
        System.out.println("========================");
        
        // Basic text block vs traditional strings
        String sqlOld = "SELECT customer_id, name, email\n" +
                       "FROM customers\n" +
                       "WHERE status = 'ACTIVE'\n" +
                       "ORDER BY name";
        
        String sqlNew = """
                SELECT customer_id, name, email
                FROM customers
                WHERE status = 'ACTIVE'
                ORDER BY name
                """;
        
        System.out.println("Traditional string concatenation:");
        System.out.println(sqlOld);
        
        System.out.println("\nText block (much cleaner):");
        System.out.println(sqlNew);
        
        // JSON example
        String jsonConfig = """
                {
                    "database": {
                        "host": "localhost",
                        "port": 5432,
                        "name": "myapp"
                    },
                    "cache": {
                        "enabled": true,
                        "ttl": 300
                    }
                }
                """;
        
        System.out.println("\nJSON configuration:");
        System.out.println(jsonConfig);
        
        // HTML template
        String htmlTemplate = """
                <html>
                    <head>
                        <title>Welcome Page</title>
                    </head>
                    <body>
                        <h1>Hello World!</h1>
                        <p>Welcome to Java 13 text blocks.</p>
                    </body>
                </html>
                """;
        
        System.out.println("\nHTML template:");
        System.out.println(htmlTemplate);
    }
    
    public static void showEnhancedSwitchExpressions() {
        System.out.println("\n2. Enhanced Switch Expressions");
        System.out.println("==============================");
        
        // Using yield for complex logic
        String dayType = classifyDay(DayOfWeek.WEDNESDAY);
        System.out.println("Wednesday classification: " + dayType);
        
        // Business logic with yield
        double price = calculatePrice("PREMIUM", 100.0, 2);
        System.out.println("Premium pricing: $" + price);
        
        // Processing with complex conditions
        String status = processOrder("EXPRESS", 1500.0, "VIP");
        System.out.println("Order status: " + status);
    }
    
    public static String classifyDay(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> "Start strong";
            case TUESDAY, WEDNESDAY, THURSDAY -> {
                System.out.println("Processing: " + day);
                yield "Keep going";
            }
            case FRIDAY -> {
                System.out.println("Almost weekend!");
                yield "Finish strong";
            }
            case SATURDAY, SUNDAY -> "Relax and recharge";
        };
    }
    
    public static double calculatePrice(String tier, double basePrice, int quantity) {
        return switch (tier) {
            case "BASIC" -> basePrice * quantity;
            case "PREMIUM" -> {
                double discount = quantity > 5 ? 0.15 : 0.10;
                yield basePrice * quantity * (1 - discount);
            }
            case "ENTERPRISE" -> {
                double discount = switch (quantity) {
                    case 1, 2 -> 0.05;
                    case 3, 4, 5 -> 0.12;
                    default -> 0.20;
                };
                yield basePrice * quantity * (1 - discount);
            }
            default -> basePrice * quantity;
        };
    }
    
    public static String processOrder(String priority, double amount, String customerType) {
        return switch (priority) {
            case "STANDARD" -> "Processing in 3-5 business days";
            case "EXPRESS" -> {
                if (amount > 1000 && "VIP".equals(customerType)) {
                    yield "Priority processing - 24 hours";
                }
                yield "Express processing - 1-2 business days";
            }
            case "URGENT" -> {
                String message = """
                        Urgent order processing initiated.
                        Customer: %s
                        Amount: $%.2f
                        Expected completion: Same day
                        """.formatted(customerType, amount);
                yield message.trim();
            }
            default -> "Standard processing";
        };
    }
    
    public static void exploreTextBlockFormatting() {
        System.out.println("\n3. Text Block Formatting");
        System.out.println("=========================");
        
        // Text blocks with formatting
        String name = "Alice";
        String department = "Engineering";
        double salary = 75000.0;
        
        String employeeReport = """
                Employee Report
                ===============
                Name: %s
                Department: %s
                Salary: $%,.2f
                
                Performance Rating: Excellent
                Next Review: %s
                """.formatted(name, department, salary, 
                             LocalDate.now().plusMonths(6));
        
        System.out.println(employeeReport);
        
        // Code generation with text blocks
        String className = "UserService";
        String fieldName = "userRepository";
        
        String javaCode = """
                public class %s {
                    
                    private final UserRepository %s;
                    
                    public %s(UserRepository %s) {
                        this.%s = %s;
                    }
                    
                    public User findById(Long id) {
                        return %s.findById(id)
                                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
                    }
                }
                """.formatted(className, fieldName, className, 
                             fieldName, fieldName, fieldName, fieldName);
        
        System.out.println("Generated Java code:");
        System.out.println(javaCode);
    }
    
    public static void buildConfigurationTemplates() {
        System.out.println("\n4. Configuration Templates");
        System.out.println("==========================");
        
        // Application configuration
        Map<String, Object> config = Map.of(
            "app_name", "MyApplication",
            "version", "1.3.0",
            "port", 8080,
            "db_host", "localhost"
        );
        
        String appConfig = generateAppConfig(config);
        System.out.println("Application Configuration:");
        System.out.println(appConfig);
        
        // API documentation
        String apiDoc = generateApiDocumentation("users", "GET", "/api/users");
        System.out.println("\nAPI Documentation:");
        System.out.println(apiDoc);
    }
    
    public static String generateAppConfig(Map<String, Object> config) {
        return switch (config.get("environment")) {
            case "development" -> """
                    # Development Configuration
                    app:
                      name: %s
                      version: %s
                      port: %d
                    
                    database:
                      host: %s
                      port: 5432
                      name: %s_dev
                      logging: true
                    """.formatted(
                        config.get("app_name"),
                        config.get("version"),
                        config.get("port"),
                        config.get("db_host"),
                        config.get("app_name")
                    );
            
            case "production" -> """
                    # Production Configuration
                    app:
                      name: %s
                      version: %s
                      port: %d
                    
                    database:
                      host: %s
                      port: 5432
                      name: %s_prod
                      ssl: true
                      pool_size: 20
                    """.formatted(
                        config.get("app_name"),
                        config.get("version"),
                        config.get("port"),
                        config.get("db_host"),
                        config.get("app_name")
                    );
            
            default -> """
                    # Default Configuration
                    app:
                      name: %s
                      version: %s
                      port: %d
                    """.formatted(
                        config.get("app_name"),
                        config.get("version"),
                        config.get("port")
                    );
        };
    }
    
    public static String generateApiDocumentation(String resource, String method, String path) {
        return switch (method.toUpperCase()) {
            case "GET" -> """
                    ## GET %s
                    
                    Retrieves %s information.
                    
                    **Request:**
                    ```
                    GET %s
                    Authorization: Bearer <token>
                    ```
                    
                    **Response:**
                    ```json
                    {
                        "status": "success",
                        "data": [...],
                        "count": 10
                    }
                    ```
                    """.formatted(path, resource, path);
            
            case "POST" -> """
                    ## POST %s
                    
                    Creates a new %s resource.
                    
                    **Request:**
                    ```
                    POST %s
                    Content-Type: application/json
                    Authorization: Bearer <token>
                    
                    {
                        "name": "example",
                        "description": "Example resource"
                    }
                    ```
                    
                    **Response:**
                    ```json
                    {
                        "status": "created",
                        "data": { "id": 123, "name": "example" }
                    }
                    ```
                    """.formatted(path, resource, path);
            
            default -> "## " + method + " " + path + "\n\nMethod not documented.";
        };
    }
    
    public static void createComplexApplications() {
        System.out.println("\n5. Complex Applications");
        System.out.println("======================");
        
        // Email template generator
        EmailTemplate template = new EmailTemplate();
        String welcomeEmail = template.generateWelcomeEmail("John Doe", "Premium");
        System.out.println("Welcome Email:");
        System.out.println(welcomeEmail);
        
        // SQL query builder
        QueryBuilder builder = new QueryBuilder();
        String complexQuery = builder.buildAnalyticsQuery("sales", 2023);
        System.out.println("\nComplex SQL Query:");
        System.out.println(complexQuery);
    }
    
    public static void performanceComparisons() {
        System.out.println("\n6. Performance Comparisons");
        System.out.println("==========================");
        
        // Compare text block vs string concatenation
        int iterations = 100_000;
        
        // Text block approach
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            String result = """
                    Line 1
                    Line 2
                    Line 3
                    """;
        }
        long textBlockTime = System.nanoTime() - startTime;
        
        // String concatenation approach
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            String result = "Line 1\n" + "Line 2\n" + "Line 3\n";
        }
        long concatTime = System.nanoTime() - startTime;
        
        System.out.printf("Text blocks: %.2f ms%n", textBlockTime / 1_000_000.0);
        System.out.printf("Concatenation: %.2f ms%n", concatTime / 1_000_000.0);
        System.out.printf("Performance ratio: %.2fx%n", (double) concatTime / textBlockTime);
    }
    
    // Supporting classes
    static class EmailTemplate {
        public String generateWelcomeEmail(String name, String plan) {
            return switch (plan.toLowerCase()) {
                case "basic" -> """
                        Dear %s,
                        
                        Welcome to our Basic plan!
                        
                        You now have access to:
                        • Basic features
                        • Email support
                        • Community access
                        
                        Best regards,
                        The Team
                        """.formatted(name);
                
                case "premium" -> """
                        Dear %s,
                        
                        Welcome to our Premium plan!
                        
                        You now have access to:
                        • All premium features
                        • Priority support
                        • Advanced analytics
                        • API access
                        
                        Thank you for choosing Premium!
                        
                        Best regards,
                        The Team
                        """.formatted(name);
                
                default -> "Welcome " + name + "!";
            };
        }
    }
    
    static class QueryBuilder {
        public String buildAnalyticsQuery(String table, int year) {
            return switch (table.toLowerCase()) {
                case "sales" -> """
                        WITH monthly_data AS (
                            SELECT 
                                DATE_TRUNC('month', sale_date) as month,
                                COUNT(*) as transaction_count,
                                SUM(amount) as total_revenue,
                                AVG(amount) as avg_transaction
                            FROM sales 
                            WHERE EXTRACT(YEAR FROM sale_date) = %d
                            GROUP BY DATE_TRUNC('month', sale_date)
                        ),
                        growth_calc AS (
                            SELECT 
                                month,
                                transaction_count,
                                total_revenue,
                                avg_transaction,
                                LAG(total_revenue) OVER (ORDER BY month) as prev_revenue,
                                (total_revenue - LAG(total_revenue) OVER (ORDER BY month)) /
                                NULLIF(LAG(total_revenue) OVER (ORDER BY month), 0) * 100 as growth_rate
                            FROM monthly_data
                        )
                        SELECT 
                            TO_CHAR(month, 'Mon YYYY') as period,
                            transaction_count,
                            total_revenue,
                            avg_transaction,
                            COALESCE(growth_rate, 0) as growth_percentage
                        FROM growth_calc
                        ORDER BY month
                        """.formatted(year);
                
                default -> "SELECT * FROM " + table + " WHERE year = " + year;
            };
        }
    }
}