# Java 11 LTS Features - Exercises

## 🎯 **Learning Objectives**

Master Java 11 LTS features through hands-on exercises covering HTTP Client API, enhanced String methods, Files methods, var enhancements, and performance improvements.

---

## 📋 **Exercise Categories**

### **🟢 Beginner Level**
Focus on basic usage of new String methods and var enhancements.

### **🟡 Intermediate Level** 
Work with HTTP Client API and Files methods for real-world scenarios.

### **🔴 Advanced Level**
Build complete applications using multiple Java 11 features together.

---

## 🟢 **Beginner Exercises**

### **Exercise 1: String Methods Mastery**

**Task**: Create a text processing utility using Java 11 String methods.

```java
public class StringProcessor {
    
    /**
     * Exercise 1a: Clean user input
     * - Remove leading/trailing whitespace using strip()
     * - Check if string is blank using isBlank()
     * - Return processed string or "INVALID" if blank
     */
    public static String cleanInput(String input) {
        // TODO: Implement using Java 11 String methods
        return null;
    }
    
    /**
     * Exercise 1b: Process multi-line text
     * - Split text into lines using lines()
     * - Filter out blank lines
     * - Number each remaining line (1. Line content)
     * - Join back into single string
     */
    public static String numberNonBlankLines(String text) {
        // TODO: Implement using lines() and other String methods
        return null;
    }
    
    /**
     * Exercise 1c: Create formatted output
     * - Use repeat() to create borders
     * - Center the title within the border
     * - Return formatted string with title in a box
     */
    public static String createTitleBox(String title, int width) {
        // TODO: Create a box around the title using repeat()
        return null;
    }
}

// Test your implementation
class StringProcessorTest {
    public static void main(String[] args) {
        // Test cleanInput
        System.out.println(StringProcessor.cleanInput("  Hello World  ")); // Expected: "Hello World"
        System.out.println(StringProcessor.cleanInput("   \t\n  ")); // Expected: "INVALID"
        
        // Test numberNonBlankLines
        String multiline = "First line\n\nSecond line\n   \nThird line";
        System.out.println(StringProcessor.numberNonBlankLines(multiline));
        
        // Test createTitleBox
        System.out.println(StringProcessor.createTitleBox("Java 11", 20));
    }
}
```

**Expected Output for createTitleBox:**
```
====================
      Java 11       
====================
```

### **Exercise 2: Var in Lambda Parameters**

**Task**: Rewrite existing lambda expressions to use var for better readability.

```java
public class VarLambdaExercises {
    
    /**
     * Exercise 2a: Transform the following lambdas to use var
     */
    public static void processEmployees() {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "Engineering", 75000),
            new Employee("Bob", "Marketing", 60000),
            new Employee("Charlie", "Engineering", 80000)
        );
        
        // TODO: Convert these to use var in lambda parameters
        
        // Original: .filter(emp -> emp.getDepartment().equals("Engineering"))
        // Convert to use var
        
        // Original: .map(emp -> emp.getName() + " earns $" + emp.getSalary())
        // Convert to use var
        
        // Original: .forEach(info -> System.out.println(info))
        // Convert to use var
    }
    
    /**
     * Exercise 2b: Create complex processing pipeline with var
     */
    public static List<String> processCustomerData(List<Customer> customers) {
        // TODO: Create a processing pipeline that:
        // 1. Filters customers with orders > 5 (use var in lambda)
        // 2. Maps to formatted string: "VIP: [name] ([orderCount] orders)" (use var)
        // 3. Sorts alphabetically by name (use var)
        // 4. Collects to List
        
        return null;
    }
}
```

---

## 🟡 **Intermediate Exercises**

### **Exercise 3: File Processing with Java 11**

**Task**: Build a configuration file processor using Files methods.

```java
public class ConfigurationManager {
    
    /**
     * Exercise 3a: Configuration file reader
     * - Read entire config file using Files.readString()
     * - Parse key-value pairs from each line
     * - Handle comments (lines starting with #)
     * - Return Map of configuration properties
     */
    public static Map<String, String> loadConfiguration(Path configFile) throws IOException {
        // TODO: Implement using Files.readString() and String.lines()
        return null;
    }
    
    /**
     * Exercise 3b: Configuration file writer
     * - Convert Map to formatted config string
     * - Add header comment with timestamp
     * - Write to file using Files.writeString()
     */
    public static void saveConfiguration(Map<String, String> config, Path configFile) throws IOException {
        // TODO: Implement using Files.writeString()
    }
    
    /**
     * Exercise 3c: Configuration validator
     * - Check if required keys exist
     * - Validate value formats (e.g., numbers, URLs)
     * - Return validation report
     */
    public static ValidationReport validateConfiguration(Map<String, String> config) {
        // TODO: Implement validation logic
        return null;
    }
}

// Sample config file format:
# Application Configuration
# Generated on 2024-01-15
app.name=My Application
app.version=1.0.0
server.port=8080
database.url=jdbc:mysql://localhost:3306/mydb
```

### **Exercise 4: HTTP Client Service**

**Task**: Create a REST API client using the HTTP Client API.

```java
public class WeatherService {
    private final HttpClient httpClient;
    private final String apiKey = "your-api-key";
    private final String baseUrl = "https://api.openweathermap.org/data/2.5";
    
    public WeatherService() {
        // TODO: Initialize HTTP client with appropriate configuration
        this.httpClient = null;
    }
    
    /**
     * Exercise 4a: Synchronous weather request
     * - Build GET request for current weather
     * - Include API key as query parameter
     * - Handle HTTP errors (status codes)
     * - Parse JSON response to WeatherData object
     */
    public WeatherData getCurrentWeather(String city) throws IOException, InterruptedException {
        // TODO: Implement synchronous HTTP GET request
        return null;
    }
    
    /**
     * Exercise 4b: Asynchronous weather request
     * - Same as above but return CompletableFuture
     * - Handle errors in the future chain
     */
    public CompletableFuture<WeatherData> getCurrentWeatherAsync(String city) {
        // TODO: Implement asynchronous HTTP GET request
        return null;
    }
    
    /**
     * Exercise 4c: Multiple city weather (concurrent)
     * - Request weather for multiple cities concurrently
     * - Return Map of city to weather data
     * - Handle individual request failures gracefully
     */
    public CompletableFuture<Map<String, WeatherData>> getWeatherForCities(List<String> cities) {
        // TODO: Implement concurrent requests
        return null;
    }
    
    /**
     * Exercise 4d: POST request with JSON body
     * - Create weather alert subscription
     * - Send POST request with JSON payload
     * - Handle different response codes
     */
    public boolean subscribeToAlerts(String email, List<String> cities) {
        // TODO: Implement POST request with JSON body
        return false;
    }
}

class WeatherData {
    private String city;
    private double temperature;
    private String description;
    private double humidity;
    
    // TODO: Add constructors, getters, and toString method
}
```

---

## 🔴 **Advanced Exercises**

### **Exercise 5: Web Scraper and Analyzer**

**Task**: Build a complete web content analyzer using multiple Java 11 features.

```java
public class WebContentAnalyzer {
    private final HttpClient httpClient;
    private final Path cacheDirectory;
    
    public WebContentAnalyzer(Path cacheDirectory) throws IOException {
        // TODO: Initialize HTTP client and create cache directory
        this.httpClient = null;
        this.cacheDirectory = cacheDirectory;
    }
    
    /**
     * Exercise 5a: Content fetcher with caching
     * - Fetch web page content using HTTP Client
     * - Cache content to file using Files.writeString()
     * - Check cache before making HTTP request
     * - Handle different content types and encodings
     */
    public String fetchContent(String url) throws IOException, InterruptedException {
        // TODO: Implement with caching logic
        return null;
    }
    
    /**
     * Exercise 5b: Content analyzer
     * - Extract text content from HTML
     * - Count words, lines, paragraphs
     * - Find most common words
     * - Calculate readability metrics
     */
    public ContentAnalysis analyzeContent(String content) {
        // TODO: Implement content analysis using String methods
        return null;
    }
    
    /**
     * Exercise 5c: Batch analyzer
     * - Analyze multiple URLs concurrently
     * - Generate comparative report
     * - Save results to JSON file
     */
    public CompletableFuture<BatchAnalysisReport> analyzeBatch(List<String> urls) {
        // TODO: Implement concurrent analysis
        return null;
    }
    
    /**
     * Exercise 5d: Report generator
     * - Generate formatted text report
     * - Include charts using ASCII art
     * - Save as both text and HTML formats
     */
    public void generateReport(BatchAnalysisReport report, Path outputDir) throws IOException {
        // TODO: Generate formatted reports
    }
}

class ContentAnalysis {
    private int wordCount;
    private int lineCount;
    private int paragraphCount;
    private Map<String, Integer> wordFrequency;
    private double readabilityScore;
    
    // TODO: Implement constructors and methods
}

class BatchAnalysisReport {
    private List<ContentAnalysis> analyses;
    private Map<String, ContentAnalysis> urlToAnalysis;
    private ContentAnalysis averageMetrics;
    
    // TODO: Implement constructors and methods
}
```

### **Exercise 6: Performance Testing Framework**

**Task**: Create a performance testing framework using Java 11 features.

```java
public class PerformanceTestFramework {
    
    /**
     * Exercise 6a: HTTP performance tester
     * - Test API endpoint performance
     * - Measure response times, throughput
     * - Test with different concurrency levels
     * - Generate performance report
     */
    public static PerformanceReport testHttpEndpoint(
            String url, 
            int requestCount, 
            int concurrencyLevel,
            Duration timeout) {
        // TODO: Implement HTTP performance testing
        return null;
    }
    
    /**
     * Exercise 6b: String operation benchmarks
     * - Compare String.concat() vs StringBuilder vs String.repeat()
     * - Test with different data sizes
     * - Measure memory usage
     */
    public static BenchmarkReport benchmarkStringOperations() {
        // TODO: Implement string operation benchmarks
        return null;
    }
    
    /**
     * Exercise 6c: File I/O performance test
     * - Compare Files.readString() vs traditional BufferedReader
     * - Test with different file sizes
     * - Test concurrent file operations
     */
    public static FileIOReport benchmarkFileOperations(List<Path> testFiles) {
        // TODO: Implement file I/O benchmarks
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

class Java11FeaturesTest {
    
    @Test
    void testStringProcessing() {
        // Test your string method implementations
        String input = "  Hello World  ";
        String result = StringProcessor.cleanInput(input);
        assertEquals("Hello World", result);
        
        String blankInput = "   \t\n  ";
        String blankResult = StringProcessor.cleanInput(blankInput);
        assertEquals("INVALID", blankResult);
    }
    
    @Test
    void testHttpClient() throws Exception {
        // Test HTTP client implementations
        // Note: Use mock server or httpbin.org for testing
    }
    
    @Test
    void testFileOperations() throws Exception {
        // Test file operations with temporary files
        Path tempFile = Files.createTempFile("test", ".txt");
        // ... test your file operations
        Files.deleteIfExists(tempFile);
    }
}
```

---

## 📚 **Additional Challenges**

### **Challenge 1: Chat Application**
Build a simple chat client using:
- HTTP Client for REST API communication
- Files methods for chat history storage
- String methods for message processing
- Concurrent requests for real-time updates

### **Challenge 2: Log Analyzer**
Create a log file analyzer that:
- Processes large log files efficiently
- Extracts patterns and statistics
- Generates reports in multiple formats
- Provides real-time monitoring capabilities

### **Challenge 3: API Gateway**
Build a simple API gateway using:
- HTTP Client for upstream services
- Request/response transformation
- Caching mechanisms
- Health checking and failover

---

## 🎯 **Success Criteria**

**Beginner Level**: ✅ Complete Exercises 1-2
- Master new String methods
- Understand var in lambda parameters
- Handle basic text processing tasks

**Intermediate Level**: ✅ Complete Exercises 3-4
- Build file processing applications
- Create HTTP client services
- Handle errors and edge cases properly

**Advanced Level**: ✅ Complete Exercises 5-6
- Build complete applications
- Implement performance optimizations
- Create comprehensive test suites

---

## 💡 **Tips for Success**

1. **Start Simple**: Begin with basic examples before complex applications
2. **Error Handling**: Always handle IOException and InterruptedException
3. **Resource Management**: Use try-with-resources for HTTP clients
4. **Performance**: Consider caching and concurrent processing
5. **Testing**: Write unit tests for your implementations

---

**🚀 Ready to master Java 11 LTS features? Start with the beginner exercises and work your way up!**