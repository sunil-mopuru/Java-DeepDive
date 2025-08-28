# Java 13 Features - Exercises

## 🎯 **Learning Objectives**

Master Java 13 preview features through hands-on exercises covering Text Blocks for multi-line strings and enhanced Switch Expressions with yield statements.

---

## 📋 **Exercise Categories**

### **🟢 Beginner Level**
Focus on basic text block syntax and simple switch expressions with yield.

### **🟡 Intermediate Level** 
Work with formatted text blocks and complex switch logic for real-world scenarios.

### **🔴 Advanced Level**
Build complete template systems and configuration generators using Java 13 features.

---

## 🟢 **Beginner Exercises**

### **Exercise 1: Text Block Fundamentals**

**Task**: Convert traditional string concatenations to text blocks for better readability.

```java
public class TextBlockBasics {
    
    /**
     * Exercise 1a: SQL Query conversion
     * Convert this traditional string to a text block
     */
    public static String createUserQuery() {
        // Original (convert this):
        String query = "SELECT u.id, u.name, u.email, p.name as profile_name\n" +
                      "FROM users u\n" +
                      "JOIN profiles p ON u.profile_id = p.id\n" +
                      "WHERE u.active = true\n" +
                      "  AND u.created_date > '2023-01-01'\n" +
                      "ORDER BY u.name";
        
        // TODO: Rewrite using text block
        return null;
    }
    
    /**
     * Exercise 1b: JSON configuration
     * Create a JSON configuration using text blocks
     */
    public static String createServerConfig() {
        // TODO: Create a JSON config with server settings
        // Include: host, port, ssl, database settings, logging level
        return null;
    }
    
    /**
     * Exercise 1c: HTML template
     * Create an HTML email template using text blocks
     */
    public static String createEmailTemplate() {
        // TODO: Create HTML email with header, content area, footer
        // Should be a complete HTML document
        return null;
    }
    
    /**
     * Exercise 1d: Multi-line code snippet
     * Create a Java class template using text blocks
     */
    public static String createClassTemplate(String className) {
        // TODO: Generate a basic Java class structure
        // Include package, imports, class declaration, basic methods
        return null;
    }
}

// Test your solutions
class TextBlockTest {
    public static void main(String[] args) {
        System.out.println("SQL Query:");
        System.out.println(TextBlockBasics.createUserQuery());
        
        System.out.println("\nServer Config:");
        System.out.println(TextBlockBasics.createServerConfig());
        
        System.out.println("\nEmail Template:");
        System.out.println(TextBlockBasics.createEmailTemplate());
        
        System.out.println("\nClass Template:");
        System.out.println(TextBlockBasics.createClassTemplate("UserService"));
    }
}
```

### **Exercise 2: Enhanced Switch Expressions**

**Task**: Create utility methods using switch expressions with yield statements.

```java
public class EnhancedSwitchBasics {
    
    /**
     * Exercise 2a: Grade calculator with yield
     * Calculate letter grade with detailed feedback
     */
    public static String calculateGrade(int score) {
        // TODO: Use switch expression with yield
        // Include score ranges and feedback messages
        // A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: below 60
        return null;
    }
    
    /**
     * Exercise 2b: Shipping calculator
     * Calculate shipping cost based on weight and destination
     */
    public static double calculateShipping(double weight, String destination) {
        // TODO: Use switch expression with yield for complex pricing
        // Local: $5 + $1 per kg
        // National: $10 + $2 per kg  
        // International: $25 + $5 per kg
        return 0.0;
    }
    
    /**
     * Exercise 2c: Order status processor
     * Generate status message based on order state and priority
     */
    public static String processOrderStatus(OrderStatus status, Priority priority) {
        // TODO: Use nested switch or complex yield logic
        // Consider both status and priority in the message
        return null;
    }
    
    /**
     * Exercise 2d: File type handler
     * Determine processing action based on file extension and size
     */
    public static String handleFile(String filename, long sizeInBytes) {
        // TODO: Use switch expression to determine action
        // Consider file extension (.jpg, .pdf, .txt, .zip) and size limits
        return null;
    }
}

enum OrderStatus {
    PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}

enum Priority {
    LOW, MEDIUM, HIGH, URGENT
}
```

---

## 🟡 **Intermediate Exercises**

### **Exercise 3: Template Generation System**

**Task**: Build a template generation system using text blocks and formatting.

```java
public class TemplateGenerator {
    
    /**
     * Exercise 3a: Configuration file generator
     * Generate environment-specific configuration files
     */
    public static String generateConfig(String environment, Map<String, Object> properties) {
        // TODO: Use text blocks and switch expressions
        // Support: development, staging, production environments
        // Include database, server, logging, security settings
        return null;
    }
    
    /**
     * Exercise 3b: REST API documentation generator
     * Generate API documentation for different endpoint types
     */
    public static String generateApiDoc(String method, String path, ApiSpec spec) {
        // TODO: Create comprehensive API documentation
        // Include: description, parameters, request/response examples
        // Support GET, POST, PUT, DELETE methods
        return null;
    }
    
    /**
     * Exercise 3c: Email template generator
     * Generate different types of email templates
     */
    public static String generateEmailTemplate(EmailType type, Map<String, String> variables) {
        // TODO: Create email templates for different purposes
        // Support: welcome, password_reset, order_confirmation, newsletter
        // Use text blocks for HTML structure
        return null;
    }
    
    /**
     * Exercise 3d: Test class generator
     * Generate JUnit test classes with boilerplate code
     */
    public static String generateTestClass(String className, List<String> methods) {
        // TODO: Generate complete test class structure
        // Include imports, class declaration, setup, test methods
        return null;
    }
}

class ApiSpec {
    private String description;
    private List<Parameter> parameters;
    private String requestExample;
    private String responseExample;
    
    // TODO: Implement constructors and getters
}

enum EmailType {
    WELCOME, PASSWORD_RESET, ORDER_CONFIRMATION, NEWSLETTER, REMINDER
}
```

### **Exercise 4: Advanced Switch Logic**

**Task**: Implement complex business logic using advanced switch expressions.

```java
public class BusinessLogicProcessor {
    
    /**
     * Exercise 4a: Insurance premium calculator
     * Calculate insurance premium based on multiple factors
     */
    public static double calculateInsurancePremium(
            int age, 
            String vehicleType, 
            int yearsOfExperience,
            boolean hasAccidents) {
        
        // TODO: Use complex switch expressions with yield
        // Consider age groups, vehicle categories, experience, accident history
        // Base rates: car=$500, motorcycle=$800, truck=$1200
        return 0.0;
    }
    
    /**
     * Exercise 4b: Tax calculator
     * Calculate tax based on income, filing status, and deductions
     */
    public static TaxResult calculateTax(
            double income, 
            FilingStatus status, 
            double deductions) {
        
        // TODO: Implement progressive tax calculation
        // Use switch for tax brackets and filing status adjustments
        return null;
    }
    
    /**
     * Exercise 4c: Loan approval system
     * Determine loan approval and terms based on applicant profile
     */
    public static LoanDecision processLoanApplication(LoanApplication application) {
        // TODO: Complex decision logic using switch expressions
        // Consider credit score, income, debt ratio, employment status
        return null;
    }
    
    /**
     * Exercise 4d: Product pricing engine
     * Calculate dynamic pricing based on demand, inventory, season
     */
    public static PricingResult calculateDynamicPrice(
            Product product,
            int inventoryLevel,
            Season season,
            DemandLevel demand) {
        
        // TODO: Sophisticated pricing algorithm using switch expressions
        return null;
    }
}

enum FilingStatus {
    SINGLE, MARRIED_JOINTLY, MARRIED_SEPARATELY, HEAD_OF_HOUSEHOLD
}

enum Season {
    SPRING, SUMMER, FALL, WINTER
}

enum DemandLevel {
    LOW, MEDIUM, HIGH, VERY_HIGH
}

// TODO: Implement supporting classes
class TaxResult {
    // federal tax, state tax, effective rate
}

class LoanDecision {
    // approved, amount, interest rate, term
}

class PricingResult {
    // final price, discount percentage, reasoning
}
```

---

## 🔴 **Advanced Exercises**

### **Exercise 5: Code Generation Framework**

**Task**: Build a comprehensive code generation framework using Java 13 features.

```java
public class CodeGenerationFramework {
    
    /**
     * Exercise 5a: Entity class generator
     * Generate JPA entity classes from database schema
     */
    public static String generateEntityClass(TableDefinition table) {
        // TODO: Generate complete JPA entity with:
        // - Package and imports
        // - Class annotations (@Entity, @Table)
        // - Fields with proper annotations
        // - Constructors, getters, setters
        // - equals, hashCode, toString methods
        return null;
    }
    
    /**
     * Exercise 5b: REST controller generator
     * Generate Spring Boot REST controllers
     */
    public static String generateRestController(String entityName, List<ApiEndpoint> endpoints) {
        // TODO: Generate complete REST controller with:
        // - Proper annotations
        // - CRUD operations
        // - Error handling
        // - Input validation
        return null;
    }
    
    /**
     * Exercise 5c: Database migration generator
     * Generate Flyway/Liquibase migration scripts
     */
    public static String generateMigrationScript(
            MigrationType type,
            String tableName,
            List<ColumnDefinition> columns) {
        
        // TODO: Generate SQL migration scripts
        // Support CREATE, ALTER, DROP operations
        // Include proper constraints and indexes
        return null;
    }
    
    /**
     * Exercise 5d: Docker configuration generator
     * Generate Docker and docker-compose files
     */
    public static DockerConfiguration generateDockerConfig(ApplicationProfile profile) {
        // TODO: Generate Dockerfile and docker-compose.yml
        // Support different application types and environments
        return null;
    }
}

class TableDefinition {
    private String name;
    private List<ColumnDefinition> columns;
    private List<String> indexes;
    // TODO: Implement
}

enum MigrationType {
    CREATE_TABLE, ALTER_TABLE, DROP_TABLE, CREATE_INDEX
}

class DockerConfiguration {
    private String dockerfile;
    private String dockerCompose;
    // TODO: Implement
}
```

### **Exercise 6: Report Generation System**

**Task**: Create a comprehensive report generation system.

```java
public class ReportGenerationSystem {
    
    /**
     * Exercise 6a: Financial report generator
     * Generate financial reports in multiple formats
     */
    public static String generateFinancialReport(
            List<Transaction> transactions,
            ReportPeriod period,
            ReportFormat format) {
        
        // TODO: Generate comprehensive financial reports
        // Support HTML, CSV, PDF formats
        // Include summaries, charts (ASCII), trends
        return null;
    }
    
    /**
     * Exercise 6b: Performance analytics report
     * Generate system performance analytics
     */
    public static String generatePerformanceReport(
            List<MetricData> metrics,
            TimeRange range) {
        
        // TODO: Create performance analytics report
        // Include charts, statistics, recommendations
        // Use text blocks for formatted output
        return null;
    }
    
    /**
     * Exercise 6c: User activity report
     * Generate user engagement and activity reports
     */
    public static String generateUserActivityReport(
            List<UserActivity> activities,
            AnalysisType analysisType) {
        
        // TODO: Comprehensive user activity analysis
        // Support different analysis types and visualizations
        return null;
    }
    
    /**
     * Exercise 6d: Security audit report
     * Generate security audit and compliance reports
     */
    public static String generateSecurityAuditReport(
            List<SecurityEvent> events,
            ComplianceFramework framework) {
        
        // TODO: Generate security compliance reports
        // Include threat analysis, recommendations, compliance status
        return null;
    }
}

enum ReportFormat {
    HTML, CSV, JSON, PDF, PLAIN_TEXT
}

enum AnalysisType {
    DAILY, WEEKLY, MONTHLY, QUARTERLY, CUSTOM
}

enum ComplianceFramework {
    GDPR, SOX, HIPAA, PCI_DSS, ISO_27001
}
```

### **Exercise 7: Configuration Management System**

**Task**: Build a complete configuration management system.

```java
public class ConfigurationManagementSystem {
    
    /**
     * Exercise 7a: Multi-environment config generator
     * Generate configurations for different environments
     */
    public static ConfigurationSet generateEnvironmentConfigs(
            ApplicationDefinition app,
            List<Environment> environments) {
        
        // TODO: Generate complete configuration sets
        // Support Kubernetes, Docker, traditional deployments
        // Include secrets management, scaling parameters
        return null;
    }
    
    /**
     * Exercise 7b: Infrastructure as Code generator
     * Generate Terraform/CloudFormation templates
     */
    public static String generateInfrastructureCode(
            InfrastructureSpec spec,
            CloudProvider provider) {
        
        // TODO: Generate infrastructure code
        // Support AWS, Azure, GCP
        // Include networking, security, monitoring
        return null;
    }
    
    /**
     * Exercise 7c: CI/CD pipeline generator
     * Generate CI/CD pipeline configurations
     */
    public static String generateCIPipeline(
            ProjectType projectType,
            List<BuildStage> stages,
            DeploymentStrategy strategy) {
        
        // TODO: Generate CI/CD pipeline configs
        // Support Jenkins, GitHub Actions, GitLab CI
        // Include testing, security scans, deployments
        return null;
    }
    
    /**
     * Exercise 7d: Monitoring configuration generator
     * Generate monitoring and alerting configurations
     */
    public static MonitoringConfig generateMonitoringConfig(
            List<ServiceDefinition> services,
            AlertingStrategy alerting) {
        
        // TODO: Generate comprehensive monitoring setup
        // Include metrics, logs, traces, alerts
        // Support Prometheus, Grafana, ELK stack
        return null;
    }
}
```

---

## 🧪 **Testing Your Solutions**

### **Unit Test Template**

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class Java13FeaturesTest {
    
    @Test
    void testTextBlockFormatting() {
        String query = TextBlockBasics.createUserQuery();
        
        assertThat(query).contains("SELECT u.id, u.name, u.email");
        assertThat(query).contains("FROM users u");
        assertThat(query).contains("JOIN profiles p");
        
        // Test formatting is preserved
        String[] lines = query.split("\n");
        assertTrue(lines.length > 3);
    }
    
    @Test
    void testEnhancedSwitchExpressions() {
        // Test grade calculation
        assertEquals("A", EnhancedSwitchBasics.calculateGrade(95));
        assertEquals("F", EnhancedSwitchBasics.calculateGrade(45));
        
        // Test shipping calculation
        double shipping = EnhancedSwitchBasics.calculateShipping(5.0, "local");
        assertEquals(10.0, shipping, 0.01);
    }
    
    @Test
    void testTemplateGeneration() {
        Map<String, Object> props = Map.of(
            "app.name", "TestApp",
            "server.port", 8080
        );
        
        String config = TemplateGenerator.generateConfig("development", props);
        
        assertThat(config).contains("TestApp");
        assertThat(config).contains("8080");
    }
}
```

---

## 📚 **Additional Challenges**

### **Challenge 1: Documentation Generator**
Build a system that generates complete project documentation including:
- API documentation from code annotations
- Database schema documentation
- Deployment guides
- User manuals

### **Challenge 2: Migration Tool**
Create a tool that helps migrate legacy applications:
- Convert old string concatenations to text blocks
- Modernize switch statements to expressions
- Generate modernized code patterns

### **Challenge 3: Template Engine**
Build a flexible template engine that:
- Supports multiple output formats
- Includes conditional logic
- Handles complex data structures
- Provides extensible template libraries

---

## 🎯 **Success Criteria**

**Beginner Level**: ✅ Complete Exercises 1-2
- Master text block syntax and basic formatting
- Understand switch expressions with yield
- Handle simple template generation

**Intermediate Level**: ✅ Complete Exercises 3-4
- Build template generation systems
- Implement complex business logic
- Create formatted documentation

**Advanced Level**: ✅ Complete Exercises 5-7
- Build complete code generation frameworks
- Create comprehensive reporting systems
- Implement configuration management tools

---

## 💡 **Tips for Success**

1. **Text Blocks**: Remember the triple quotes and proper indentation
2. **Formatting**: Use `.formatted()` method for dynamic content
3. **Switch Expressions**: Use yield for complex logic blocks
4. **Performance**: Text blocks are compiled efficiently
5. **Testing**: Verify formatting and content separately

---

**🚀 Ready to master Java 13's text processing capabilities? Start with text blocks and build your way up to complete code generation systems!**