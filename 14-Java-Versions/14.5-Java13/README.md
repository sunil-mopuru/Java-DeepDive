# Module 14.5: Java 13 Features

## 🎯 **Overview**

Java 13 (September 2019) introduced **Text Blocks** as a preview feature and enhanced **Switch Expressions**, making Java code more readable and expressive. This module explores these features that significantly improve code quality and developer productivity.

---

## 🚀 **Key Features**

### **🔥 Major Features**
- **Text Blocks (Preview)**: Multi-line string literals with improved readability
- **Switch Expressions (Second Preview)**: Enhanced with yield statements and better pattern support
- **Dynamic CDS Archives**: Improved application startup performance
- **ZGC Improvements**: Better memory management for large heaps

### **📈 Performance & JVM**
- **Dynamic Class-Data Sharing (CDS)**: Faster application startup
- **ZGC Enhancements**: Uncommit unused memory to the operating system
- **SocketImpl Reimplementation**: Better performance and maintainability

---

## 📚 **Detailed Feature Guide**

### **1. Text Blocks (Preview Feature)**

Revolutionary multi-line string literals that preserve formatting and improve readability.

#### **Basic Text Block Syntax**
```java
public class TextBlockExamples {
    
    public void basicTextBlocks() {
        // Traditional string concatenation (hard to read)
        String htmlOld = "<html>\n" +
                        "    <body>\n" +
                        "        <h1>Hello World!</h1>\n" +
                        "    </body>\n" +
                        "</html>";
        
        // Text block (Java 13 Preview)
        String htmlNew = """
                        <html>
                            <body>
                                <h1>Hello World!</h1>
                            </body>
                        </html>
                        """;
        
        System.out.println("Traditional approach:");
        System.out.println(htmlOld);
        System.out.println("\nText block approach:");
        System.out.println(htmlNew);
    }
}
```

#### **Text Block Features and Rules**
```java
public class TextBlockFeatures {
    
    public void demonstrateFeatures() {
        // 1. Automatic indentation management
        String query = """
                SELECT customer_id, name, email
                FROM customers
                WHERE status = 'ACTIVE'
                  AND created_date > '2020-01-01'
                ORDER BY name
                """;
        
        // 2. Escape sequences still work
        String textWithEscapes = """
                Line 1
                Line 2 with "quotes"
                Line 3 with \t tabs
                Line 4 with \n newlines (though not needed)
                """;
        
        // 3. String concatenation with text blocks
        String name = "John";
        String greeting = """
                Hello %s!
                Welcome to our application.
                Today is a great day to learn Java 13.
                """.formatted(name);
        
        // 4. Empty text block
        String empty = """
                """;
        
        // 5. Text block with variables
        String template = """
                {
                    "name": "%s",
                    "age": %d,
                    "active": %b
                }
                """.formatted("Alice", 30, true);
        
        System.out.println("SQL Query:");
        System.out.println(query);
        
        System.out.println("Formatted greeting:");
        System.out.println(greeting);
        
        System.out.println("JSON template:");
        System.out.println(template);
    }
    
    // Practical usage examples
    public void practicalExamples() {
        // JSON configuration
        String config = """
                {
                    "database": {
                        "host": "localhost",
                        "port": 5432,
                        "name": "myapp"
                    },
                    "features": {
                        "caching": true,
                        "logging": "DEBUG"
                    }
                }
                """;
        
        // HTML template
        String emailTemplate = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Welcome Email</title>
                </head>
                <body>
                    <h1>Welcome to Our Service!</h1>
                    <p>Dear %s,</p>
                    <p>Thank you for joining us. Your account has been created successfully.</p>
                    <p>Best regards,<br>The Team</p>
                </body>
                </html>
                """;
        
        // SQL with complex formatting
        String complexQuery = """
                WITH monthly_sales AS (
                    SELECT 
                        DATE_TRUNC('month', order_date) as month,
                        SUM(total_amount) as monthly_total
                    FROM orders 
                    WHERE order_date >= '2023-01-01'
                    GROUP BY DATE_TRUNC('month', order_date)
                ),
                growth_analysis AS (
                    SELECT 
                        month,
                        monthly_total,
                        LAG(monthly_total) OVER (ORDER BY month) as prev_month,
                        (monthly_total - LAG(monthly_total) OVER (ORDER BY month)) / 
                        LAG(monthly_total) OVER (ORDER BY month) * 100 as growth_rate
                    FROM monthly_sales
                )
                SELECT * FROM growth_analysis
                ORDER BY month DESC
                """;
        
        System.out.println("Configuration JSON:");
        System.out.println(config);
        
        System.out.println("\nEmail Template:");
        System.out.println(emailTemplate.formatted("John Doe"));
        
        System.out.println("\nComplex SQL Query:");
        System.out.println(complexQuery);
    }
}
```

### **2. Enhanced Switch Expressions (Second Preview)**

Improved switch expressions with better yield support and enhanced pattern matching capabilities.

#### **Switch Expression Improvements**
```java
public class EnhancedSwitchExpressions {
    
    public void demonstrateEnhancements() {
        // Multiple case labels with yield
        String dayCategory = getDayCategory(DayOfWeek.FRIDAY);
        System.out.println("Friday category: " + dayCategory);
        
        // Complex business logic with yield
        double discount = calculateDiscount("PREMIUM", 1500.0);
        System.out.printf("Premium discount for $1500: %.2f%%%n", discount * 100);
        
        // Nested switch expressions
        String recommendation = getProductRecommendation("ELECTRONICS", "PREMIUM");
        System.out.println("Product recommendation: " + recommendation);
    }
    
    public String getDayCategory(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> "Start of work week";
            case TUESDAY, WEDNESDAY, THURSDAY -> {
                System.out.println("Processing mid-week day: " + day);
                yield "Mid-week hustle";
            }
            case FRIDAY -> {
                System.out.println("TGIF!");
                yield "End of work week";
            }
            case SATURDAY, SUNDAY -> "Weekend relaxation";
        };
    }
    
    public double calculateDiscount(String membershipType, double purchaseAmount) {
        return switch (membershipType) {
            case "BASIC" -> {
                if (purchaseAmount > 100) yield 0.05;
                yield 0.02;
            }
            case "PREMIUM" -> {
                if (purchaseAmount > 1000) yield 0.15;
                if (purchaseAmount > 500) yield 0.10;
                yield 0.07;
            }
            case "VIP" -> {
                if (purchaseAmount > 2000) yield 0.25;
                if (purchaseAmount > 1000) yield 0.20;
                yield 0.15;
            }
            default -> 0.0;
        };
    }
    
    public String getProductRecommendation(String category, String membershipLevel) {
        return switch (category) {
            case "ELECTRONICS" -> switch (membershipLevel) {
                case "BASIC" -> "Budget smartphones and accessories";
                case "PREMIUM" -> "Latest laptops and tablets";
                case "VIP" -> "Cutting-edge gaming equipment";
                default -> "Popular electronics";
            };
            case "CLOTHING" -> switch (membershipLevel) {
                case "BASIC" -> "Seasonal basics and essentials";
                case "PREMIUM" -> "Designer collections";
                case "VIP" -> "Exclusive limited editions";
                default -> "Trending fashion items";
            };
            case "BOOKS" -> "Personalized reading recommendations";
            default -> "General product catalog";
        };
    }
}
```

### **3. Practical Applications**

Real-world usage patterns combining text blocks and enhanced switch expressions.

#### **Configuration and Template Management**
```java
public class ConfigurationTemplateManager {
    
    public String generateDatabaseConfig(String environment, Map<String, Object> params) {
        return switch (environment.toLowerCase()) {
            case "development" -> """
                    # Development Database Configuration
                    database:
                      host: localhost
                      port: 5432
                      name: %s_dev
                      user: dev_user
                      password: dev_pass
                      pool_size: 5
                      ssl: false
                    """.formatted(params.get("app_name"));
                    
            case "staging" -> """
                    # Staging Database Configuration
                    database:
                      host: staging-db.company.com
                      port: 5432
                      name: %s_staging
                      user: staging_user
                      password: ${STAGING_DB_PASSWORD}
                      pool_size: 10
                      ssl: true
                      connection_timeout: 30s
                    """.formatted(params.get("app_name"));
                    
            case "production" -> """
                    # Production Database Configuration
                    database:
                      host: prod-db.company.com
                      port: 5432
                      name: %s_prod
                      user: prod_user
                      password: ${PROD_DB_PASSWORD}
                      pool_size: 20
                      ssl: true
                      connection_timeout: 10s
                      read_timeout: 60s
                      monitoring: true
                    """.formatted(params.get("app_name"));
                    
            default -> throw new IllegalArgumentException("Unknown environment: " + environment);
        };
    }
    
    public String generateApiDocumentation(String serviceName, List<ApiEndpoint> endpoints) {
        String endpointDocs = endpoints.stream()
            .map(this::formatEndpointDoc)
            .collect(Collectors.joining("\n\n"));
            
        return """
                # %s API Documentation
                
                ## Overview
                This document describes the REST API endpoints for the %s service.
                
                ## Base URL
                ```
                https://api.company.com/v1/%s
                ```
                
                ## Authentication
                All requests require a valid API key in the header:
                ```
                Authorization: Bearer YOUR_API_KEY
                ```
                
                ## Endpoints
                
                %s
                
                ## Error Responses
                The API uses conventional HTTP response codes to indicate success or failure.
                
                - `200` - Success
                - `400` - Bad Request
                - `401` - Unauthorized
                - `404` - Not Found
                - `500` - Internal Server Error
                """.formatted(serviceName, serviceName, serviceName.toLowerCase(), endpointDocs);
    }
    
    private String formatEndpointDoc(ApiEndpoint endpoint) {
        return switch (endpoint.getMethod()) {
            case "GET" -> """
                    ### %s %s
                    
                    **Description:** %s
                    
                    **Parameters:**
                    %s
                    
                    **Response:**
                    ```json
                    %s
                    ```
                    """.formatted(
                        endpoint.getMethod(),
                        endpoint.getPath(),
                        endpoint.getDescription(),
                        formatParameters(endpoint.getParameters()),
                        endpoint.getExampleResponse()
                    );
                    
            case "POST", "PUT" -> """
                    ### %s %s
                    
                    **Description:** %s
                    
                    **Request Body:**
                    ```json
                    %s
                    ```
                    
                    **Response:**
                    ```json
                    %s
                    ```
                    """.formatted(
                        endpoint.getMethod(),
                        endpoint.getPath(),
                        endpoint.getDescription(),
                        endpoint.getExampleRequest(),
                        endpoint.getExampleResponse()
                    );
                    
            default -> "### " + endpoint.getMethod() + " " + endpoint.getPath() + "\n" +
                      endpoint.getDescription();
        };
    }
}
```

---

## ⚡ **Best Practices**

### **Text Block Guidelines**
- ✅ **Use for multi-line strings**: Perfect for SQL, JSON, HTML, XML
- ✅ **Maintain readable indentation**: Let Java handle the formatting
- ✅ **Combine with String.formatted()**: For dynamic content
- ❌ **Don't overuse for simple strings**: Single-line strings don't benefit

### **Enhanced Switch Guidelines**
- ✅ **Use yield for complex logic**: When simple expressions aren't enough
- ✅ **Combine with text blocks**: For complex string generation
- ✅ **Leverage exhaustiveness**: Compiler ensures all cases are covered
- ❌ **Avoid excessive nesting**: Keep switch logic readable

---

## 🧪 **Testing with Java 13 Features**

```java
@Test
public void testTextBlocks() {
    String query = """
            SELECT * FROM users 
            WHERE active = true
            """;
    
    assertThat(query).contains("SELECT * FROM users");
    assertThat(query).contains("WHERE active = true");
}

@Test
public void testEnhancedSwitchExpressions() {
    EnhancedSwitchExpressions processor = new EnhancedSwitchExpressions();
    
    String result = processor.getDayCategory(DayOfWeek.MONDAY);
    assertEquals("Start of work week", result);
    
    double discount = processor.calculateDiscount("PREMIUM", 1200.0);
    assertEquals(0.15, discount, 0.001);
}
```

---

## 📋 **Quick Reference**

### **Text Block Syntax**
```java
// Basic text block
String text = """
              Content here
              Multiple lines
              """;

// With formatting
String formatted = """
                   Hello %s!
                   Welcome to %s.
                   """.formatted(name, app);

// With variables (using formatted method)
String template = """
                  {
                    "name": "%s",
                    "value": %d
                  }
                  """.formatted(key, value);
```

### **Enhanced Switch Expression**
```java
// With yield for complex logic
String result = switch (value) {
    case A -> "Simple case";
    case B -> {
        // Complex logic here
        yield "Complex result";
    }
    default -> "Default case";
};
```

---

## 🎯 **What You'll Learn**

After completing this module, you'll understand:
- **Text Blocks**: Writing readable multi-line strings for SQL, JSON, HTML
- **Enhanced Switch Expressions**: Using yield statements for complex logic
- **Template Management**: Building configuration and documentation systems
- **Code Quality**: Improving readability and maintainability

---

## 📚 **Prerequisites**

- **Java 12 Features**: Switch expressions and string methods
- **Java 11 Features**: String processing and HTTP client
- **String Processing**: Understanding of string manipulation
- **JSON/XML**: Basic knowledge of structured data formats

---

**🚀 Ready to write cleaner, more readable Java code? Let's explore Java 13's powerful text processing features!**

**Next:** [Java 14 Features](../14.6-Java14/README.md) | **Previous:** [Java 12 Features](../14.4-Java12/README.md)