# Java 12 Features - Exercises

## 🎯 **Learning Objectives**

Master Java 12 features through hands-on exercises covering Switch Expressions, enhanced String methods (`indent()`, `transform()`), `Collectors.teeing()`, and JVM improvements.

---

## 📋 **Exercise Categories**

### **🟢 Beginner Level**
Focus on basic usage of switch expressions and new String methods.

### **🟡 Intermediate Level** 
Work with complex switch patterns and `Collectors.teeing()` for real-world scenarios.

### **🔴 Advanced Level**
Build complete applications using multiple Java 12 features together.

---

## 🟢 **Beginner Exercises**

### **Exercise 1: Switch Expression Fundamentals**

**Task**: Create utility methods using switch expressions to replace traditional switch statements.

```java
public class SwitchExpressionBasics {
    
    /**
     * Exercise 1a: Day type classifier
     * - Use switch expression with multiple values
     * - Return "Weekend" or "Weekday" based on DayOfWeek
     * - Handle all enum values
     */
    public static String classifyDay(DayOfWeek day) {
        // TODO: Implement using switch expression with arrow syntax
        return null;
    }
    
    /**
     * Exercise 1b: Grade calculator
     * - Convert numeric grade (0-100) to letter grade
     * - Use switch expression with ranges
     * - A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: 0-59
     */
    public static String calculateLetterGrade(int numericGrade) {
        // TODO: Implement using switch expression
        return null;
    }
    
    /**
     * Exercise 1c: Season detector
     * - Return season based on month
     * - Spring: Mar-May, Summer: Jun-Aug, Fall: Sep-Nov, Winter: Dec-Feb
     * - Use switch expression with block syntax for complex logic
     */
    public static String detectSeason(Month month) {
        // TODO: Implement using switch expression with yield
        return null;
    }
    
    /**
     * Exercise 1d: HTTP status message
     * - Return appropriate message for HTTP status codes
     * - Handle common codes: 200, 201, 400, 401, 404, 500
     * - Return "Unknown status" for others
     */
    public static String getHttpStatusMessage(int statusCode) {
        // TODO: Implement using switch expression
        return null;
    }
}

// Test your implementation
class SwitchExpressionTest {
    public static void main(String[] args) {
        // Test day classifier
        System.out.println(SwitchExpressionBasics.classifyDay(DayOfWeek.SATURDAY)); // Weekend
        System.out.println(SwitchExpressionBasics.classifyDay(DayOfWeek.MONDAY)); // Weekday
        
        // Test grade calculator
        System.out.println(SwitchExpressionBasics.calculateLetterGrade(95)); // A
        System.out.println(SwitchExpressionBasics.calculateLetterGrade(75)); // C
        
        // Test season detector
        System.out.println(SwitchExpressionBasics.detectSeason(Month.JUNE)); // Summer
        System.out.println(SwitchExpressionBasics.detectSeason(Month.DECEMBER)); // Winter
        
        // Test HTTP status
        System.out.println(SwitchExpressionBasics.getHttpStatusMessage(404)); // Not Found
    }
}
```

### **Exercise 2: String Methods Mastery**

**Task**: Create text processing utilities using Java 12 String methods.

```java
public class StringMethodsUtility {
    
    /**
     * Exercise 2a: Code formatter
     * - Take unformatted code and apply proper indentation
     * - Use indent() method to add specified levels of indentation
     * - Each level = 4 spaces
     */
    public static String formatCode(String code, int indentLevel) {
        // TODO: Implement using String.indent()
        return null;
    }
    
    /**
     * Exercise 2b: Text processor pipeline
     * - Apply multiple transformations using transform() method
     * - 1. Trim whitespace
     * - 2. Convert to title case
     * - 3. Remove special characters (keep only letters, numbers, spaces)
     * - 4. Limit to maxLength characters
     */
    public static String processText(String input, int maxLength) {
        // TODO: Chain transformations using String.transform()
        return null;
    }
    
    /**
     * Exercise 2c: Log entry formatter
     * - Create formatted log entries with timestamp and level
     * - Use transform() to apply formatting functions
     * - Format: "[TIMESTAMP] [LEVEL] MESSAGE"
     */
    public static String createLogEntry(String message, String level) {
        // TODO: Use transform() with custom formatting functions
        return null;
    }
    
    /**
     * Exercise 2d: Configuration file generator
     * - Generate indented configuration content
     * - Use both indent() and transform() methods
     * - Add comments and proper formatting
     */
    public static String generateConfig(Map<String, String> properties) {
        // TODO: Create formatted configuration using String methods
        return null;
    }
}

// Helper functions to implement
class TextTransformers {
    public static Function<String, String> toTitleCase() {
        // TODO: Return function that converts to title case
        return null;
    }
    
    public static Function<String, String> removeSpecialChars() {
        // TODO: Return function that removes special characters
        return null;
    }
    
    public static Function<String, String> limitLength(int maxLength) {
        // TODO: Return function that limits string length
        return null;
    }
    
    public static Function<String, String> addTimestamp() {
        // TODO: Return function that adds current timestamp
        return null;
    }
}
```

---

## 🟡 **Intermediate Exercises**

### **Exercise 3: Advanced Switch Expressions**

**Task**: Build complex decision systems using advanced switch expression patterns.

```java
public class AdvancedSwitchProcessor {
    
    /**
     * Exercise 3a: File processor dispatcher
     * - Determine processing action based on file type and size
     * - Use nested switch expressions or complex conditions
     * - Handle multiple file extensions and size categories
     */
    public static ProcessingAction determineAction(String filename, long fileSize) {
        // TODO: Implement complex switch logic
        // Consider both file extension and size
        // Small: < 1MB, Medium: 1MB-10MB, Large: > 10MB
        return null;
    }
    
    /**
     * Exercise 3b: Order processor
     * - Calculate shipping cost and delivery time based on order details
     * - Consider item type, quantity, destination, and priority
     * - Use switch expressions with complex business logic
     */
    public static ShippingInfo calculateShipping(Order order) {
        // TODO: Implement shipping calculation using switch expressions
        return null;
    }
    
    /**
     * Exercise 3c: API rate limiter
     * - Determine rate limit based on user type and API endpoint
     * - Different limits for different combinations
     * - Use switch expressions for clean logic
     */
    public static RateLimit determineRateLimit(UserType userType, ApiEndpoint endpoint) {
        // TODO: Implement rate limiting logic with switch expressions
        return null;
    }
    
    /**
     * Exercise 3d: Game character stats calculator
     * - Calculate final stats based on character class, level, and equipment
     * - Use switch expressions for class-specific calculations
     * - Apply level and equipment bonuses
     */
    public static CharacterStats calculateStats(CharacterClass charClass, int level, Equipment equipment) {
        // TODO: Implement character stats calculation
        return null;
    }
}

// Supporting enums and classes to implement
enum ProcessingAction {
    COMPRESS, ARCHIVE, PROCESS_IMMEDIATELY, QUEUE_FOR_LATER, REJECT
}

enum UserType {
    FREE, PREMIUM, ENTERPRISE
}

enum ApiEndpoint {
    PUBLIC_DATA, USER_DATA, ANALYTICS, ADMIN
}

class Order {
    // TODO: Implement Order class with necessary fields
}

class ShippingInfo {
    // TODO: Implement ShippingInfo class
}

class RateLimit {
    // TODO: Implement RateLimit class
}
```

### **Exercise 4: Collectors.teeing() Mastery**

**Task**: Create advanced data analysis tools using the teeing collector.

```java
public class DataAnalysisTools {
    
    /**
     * Exercise 4a: Sales analysis
     * - Calculate total revenue AND average sale amount in one pass
     * - Group by region AND calculate statistics for each region
     * - Use teeing to combine multiple aggregations
     */
    public static SalesReport analyzeSales(List<Sale> sales) {
        // TODO: Use Collectors.teeing() for comprehensive analysis
        return null;
    }
    
    /**
     * Exercise 4b: Student performance analyzer
     * - Calculate class average AND identify top/bottom performers
     * - Find subject-wise statistics AND overall rankings
     * - Use nested teeing for complex analysis
     */
    public static PerformanceReport analyzeStudentPerformance(List<StudentGrade> grades) {
        // TODO: Implement using nested teeing collectors
        return null;
    }
    
    /**
     * Exercise 4c: Inventory optimizer
     * - Find items to reorder AND calculate total inventory value
     * - Group by category AND calculate turnover rates
     * - Combine multiple business metrics in single pass
     */
    public static InventoryReport optimizeInventory(List<InventoryItem> inventory) {
        // TODO: Use teeing for inventory analysis
        return null;
    }
    
    /**
     * Exercise 4d: Website traffic analyzer
     * - Calculate peak hours AND geographic distribution
     * - Find most popular pages AND user behavior patterns
     * - Generate comprehensive traffic insights
     */
    public static TrafficReport analyzeWebTraffic(List<WebRequest> requests) {
        // TODO: Implement comprehensive traffic analysis
        return null;
    }
}

// Data classes to implement
class Sale {
    private String region;
    private double amount;
    private LocalDate date;
    
    // TODO: Implement constructors and getters
}

class SalesReport {
    // TODO: Implement with total revenue, average sale, regional breakdown
}

class StudentGrade {
    // TODO: Implement student grade data
}

class PerformanceReport {
    // TODO: Implement performance analysis results
}
```

---

## 🔴 **Advanced Exercises**

### **Exercise 5: Configuration Management System**

**Task**: Build a complete configuration management system using Java 12 features.

```java
public class ConfigurationManager {
    
    /**
     * Exercise 5a: Configuration parser
     * - Parse different configuration formats (YAML, JSON, Properties)
     * - Use switch expressions for format-specific parsing
     * - Apply text transformations for data cleaning
     */
    public static Configuration parseConfiguration(String content, ConfigFormat format) {
        return switch (format) {
            case YAML -> parseYaml(content);
            case JSON -> parseJson(content);
            case PROPERTIES -> parseProperties(content);
            default -> throw new UnsupportedOperationException("Format not supported: " + format);
        };
    }
    
    /**
     * Exercise 5b: Configuration validator
     * - Validate configuration using teeing collector
     * - Collect both errors AND warnings in single pass
     * - Generate comprehensive validation report
     */
    public static ValidationResult validateConfiguration(Configuration config) {
        // TODO: Use teeing to collect errors and warnings simultaneously
        return null;
    }
    
    /**
     * Exercise 5c: Configuration generator
     * - Generate formatted configuration files
     * - Use String methods for proper indentation and formatting
     * - Support multiple output formats
     */
    public static String generateConfigFile(Configuration config, ConfigFormat format, int indentSize) {
        // TODO: Generate properly formatted configuration using String methods
        return null;
    }
    
    /**
     * Exercise 5d: Configuration merger
     * - Merge multiple configurations with conflict resolution
     * - Use switch expressions for merge strategies
     * - Track merge statistics using teeing
     */
    public static MergeResult mergeConfigurations(List<Configuration> configs, MergeStrategy strategy) {
        // TODO: Implement configuration merging with comprehensive reporting
        return null;
    }
}

enum ConfigFormat {
    YAML, JSON, PROPERTIES, XML
}

enum MergeStrategy {
    OVERRIDE, MERGE, FAIL_ON_CONFLICT, INTERACTIVE
}

class Configuration {
    // TODO: Implement configuration data structure
}

class ValidationResult {
    // TODO: Implement validation result with errors and warnings
}

class MergeResult {
    // TODO: Implement merge result with statistics
}
```

### **Exercise 6: Web Request Analyzer**

**Task**: Create a comprehensive web request analyzer using all Java 12 features.

```java
public class WebRequestAnalyzer {
    
    /**
     * Exercise 6a: Request classifier
     * - Classify requests by type, source, and characteristics
     * - Use switch expressions for classification logic
     * - Handle different request patterns
     */
    public static RequestClassification classifyRequest(HttpRequest request) {
        // TODO: Implement comprehensive request classification
        return null;
    }
    
    /**
     * Exercise 6b: Security analyzer
     * - Analyze requests for security threats
     * - Calculate risk scores using teeing collector
     * - Generate security reports
     */
    public static SecurityReport analyzeSecurityThreats(List<HttpRequest> requests) {
        // TODO: Use teeing for simultaneous threat detection and statistics
        return null;
    }
    
    /**
     * Exercise 6c: Performance analyzer
     * - Analyze response times and throughput
     * - Identify performance bottlenecks
     * - Generate optimization recommendations
     */
    public static PerformanceAnalysis analyzePerformance(List<RequestResponse> requestResponses) {
        // TODO: Comprehensive performance analysis using teeing
        return null;
    }
    
    /**
     * Exercise 6d: Report generator
     * - Generate formatted analysis reports
     * - Use String methods for professional formatting
     * - Support multiple output formats (text, HTML, JSON)
     */
    public static String generateAnalysisReport(
            RequestClassification classification,
            SecurityReport security,
            PerformanceAnalysis performance,
            ReportFormat format) {
        
        // TODO: Generate comprehensive formatted reports
        return null;
    }
}

class HttpRequest {
    // TODO: Implement HTTP request data
}

class RequestResponse {
    // TODO: Implement request-response pair
}

class RequestClassification {
    // TODO: Implement classification results
}

class SecurityReport {
    // TODO: Implement security analysis results
}

class PerformanceAnalysis {
    // TODO: Implement performance analysis results
}

enum ReportFormat {
    TEXT, HTML, JSON, PDF
}
```

### **Exercise 7: Performance Testing Framework**

**Task**: Build a performance testing framework utilizing Java 12 features.

```java
public class PerformanceTestFramework {
    
    /**
     * Exercise 7a: Test case executor
     * - Execute different types of performance tests
     * - Use switch expressions for test type handling
     * - Measure execution metrics
     */
    public static TestResult executeTest(PerformanceTest test) {
        return switch (test.getType()) {
            case LOAD_TEST -> executeLoadTest(test);
            case STRESS_TEST -> executeStressTest(test);
            case SPIKE_TEST -> executeSpikeTest(test);
            case VOLUME_TEST -> executeVolumeTest(test);
            case ENDURANCE_TEST -> executeEnduranceTest(test);
        };
    }
    
    /**
     * Exercise 7b: Results analyzer
     * - Analyze test results using teeing collector
     * - Calculate statistics AND identify anomalies simultaneously
     * - Generate comprehensive analysis
     */
    public static AnalysisResult analyzeResults(List<TestResult> results) {
        // TODO: Use teeing for comprehensive result analysis
        return null;
    }
    
    /**
     * Exercise 7c: Report formatter
     * - Generate formatted test reports
     * - Use String methods for professional presentation
     * - Include charts using ASCII art
     */
    public static String formatTestReport(AnalysisResult analysis, ReportStyle style) {
        // TODO: Format comprehensive test reports using String methods
        return null;
    }
    
    /**
     * Exercise 7d: Comparison engine
     * - Compare performance across different test runs
     * - Use teeing to calculate differences and trends
     * - Generate improvement recommendations
     */
    public static ComparisonReport comparePerformance(List<AnalysisResult> historicalResults) {
        // TODO: Implement performance comparison using advanced collectors
        return null;
    }
}

enum TestType {
    LOAD_TEST, STRESS_TEST, SPIKE_TEST, VOLUME_TEST, ENDURANCE_TEST
}

enum ReportStyle {
    SUMMARY, DETAILED, EXECUTIVE, TECHNICAL
}

class PerformanceTest {
    // TODO: Implement performance test configuration
}

class TestResult {
    // TODO: Implement test execution results
}

class AnalysisResult {
    // TODO: Implement analysis results
}

class ComparisonReport {
    // TODO: Implement comparison results
}
```

---

## 🧪 **Testing Your Solutions**

### **Unit Test Template**

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class Java12FeaturesTest {
    
    @Test
    void testSwitchExpressions() {
        // Test switch expression implementations
        assertEquals("Weekend", SwitchExpressionBasics.classifyDay(DayOfWeek.SATURDAY));
        assertEquals("Weekday", SwitchExpressionBasics.classifyDay(DayOfWeek.MONDAY));
        
        assertEquals("A", SwitchExpressionBasics.calculateLetterGrade(95));
        assertEquals("F", SwitchExpressionBasics.calculateLetterGrade(45));
    }
    
    @Test
    void testStringMethods() {
        String code = "public class Test {\npublic void method() {\n}\n}";
        String indented = StringMethodsUtility.formatCode(code, 1);
        
        // Verify proper indentation
        assertTrue(indented.contains("    public class Test"));
        
        // Test text processing pipeline
        String processed = StringMethodsUtility.processText("  Hello, World! @#$  ", 20);
        assertEquals("Hello World", processed);
    }
    
    @Test
    void testTeeingCollector() {
        List<Sale> sales = Arrays.asList(
            new Sale("North", 1000.0, LocalDate.now()),
            new Sale("South", 1500.0, LocalDate.now()),
            new Sale("North", 800.0, LocalDate.now())
        );
        
        SalesReport report = DataAnalysisTools.analyzeSales(sales);
        
        assertNotNull(report);
        assertEquals(3300.0, report.getTotalRevenue(), 0.01);
        assertEquals(1100.0, report.getAverageRevenue(), 0.01);
    }
}
```

---

## 📚 **Additional Challenges**

### **Challenge 1: Multi-format Data Processor**
Build a data processing system that:
- Handles multiple input formats (CSV, JSON, XML)
- Uses switch expressions for format-specific processing
- Applies data transformations using String methods
- Generates statistics using teeing collectors

### **Challenge 2: API Gateway**
Create an API gateway that:
- Routes requests based on patterns (switch expressions)
- Transforms request/response data (String methods)
- Monitors traffic patterns (teeing collectors)
- Generates comprehensive reports

### **Challenge 3: Log Analytics System**
Build a log analysis system that:
- Parses different log formats
- Classifies log entries by severity and type
- Analyzes patterns and anomalies
- Generates actionable insights

---

## 🎯 **Success Criteria**

**Beginner Level**: ✅ Complete Exercises 1-2
- Master switch expression syntax and usage
- Understand new String methods (indent, transform)
- Handle basic text processing tasks

**Intermediate Level**: ✅ Complete Exercises 3-4
- Build complex decision systems with switch expressions
- Master Collectors.teeing() for advanced data analysis
- Handle real-world processing scenarios

**Advanced Level**: ✅ Complete Exercises 5-7
- Build complete systems using multiple Java 12 features
- Implement comprehensive analysis and reporting tools
- Create production-ready applications

---

## 💡 **Tips for Success**

1. **Switch Expressions**: Use arrow syntax for simple cases, blocks with yield for complex logic
2. **String Methods**: Chain transform() calls for readable processing pipelines
3. **Teeing Collector**: Perfect for calculating multiple statistics in a single pass
4. **Performance**: Switch expressions are often more efficient than if-else chains
5. **Testing**: Test edge cases and verify performance improvements

---

**🚀 Ready to master Java 12 features? Start with switch expressions and work your way up to advanced data processing!**