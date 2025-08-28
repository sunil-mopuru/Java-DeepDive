/**
 * String Processing Examples - Advanced String Manipulation with Guava
 * 
 * Demonstrates Joiner, Splitter, CharMatcher, and other string utilities
 * for efficient and clean string processing in Java applications.
 */

import com.google.common.base.*;
import com.google.common.collect.*;
import java.util.*;
import java.util.stream.Collectors;

public class StringProcessingExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Guava String Processing Examples ===");
        
        demonstrateJoiner();
        demonstrateSplitter();
        demonstrateCharMatcher();
        demonstrateAdvancedStringProcessing();
        demonstrateRealWorldScenarios();
    }
    
    /**
     * Comprehensive Joiner examples
     */
    public static void demonstrateJoiner() {
        System.out.println("\n1. Joiner Examples");
        System.out.println("==================");
        
        // Basic joining
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        String joined = Joiner.on(", ").join(names);
        System.out.println("Basic join: " + joined);
        
        // Handle nulls
        List<String> withNulls = Arrays.asList("Apple", null, "Banana", "Cherry");
        String skipNulls = Joiner.on(", ").skipNulls().join(withNulls);
        String replaceNulls = Joiner.on(", ").useForNull("Unknown").join(withNulls);
        System.out.println("Skip nulls: " + skipNulls);
        System.out.println("Replace nulls: " + replaceNulls);
        
        // Map joining
        Map<String, Integer> scores = ImmutableMap.of("Alice", 95, "Bob", 87);
        String mapJoined = Joiner.on(", ").withKeyValueSeparator("=").join(scores);
        System.out.println("Map joined: " + mapJoined);
        
        // StringBuilder appendTo
        StringBuilder sb = new StringBuilder("Scores: ");
        Joiner.on(", ").withKeyValueSeparator("=").appendTo(sb, scores);
        System.out.println("Append to StringBuilder: " + sb);
    }
    
    /**
     * Comprehensive Splitter examples
     */
    public static void demonstrateSplitter() {
        System.out.println("\n2. Splitter Examples");
        System.out.println("====================");
        
        // Basic splitting
        String csv = "Apple,Banana,Cherry";
        List<String> parts = Splitter.on(',').splitToList(csv);
        System.out.println("Basic split: " + parts);
        
        // Handle whitespace and empty strings
        String messy = "Apple, ,Cherry,  Date  , ,";
        List<String> cleaned = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(messy);
        System.out.println("Cleaned split: " + cleaned);
        
        // Limit splits
        String longString = "one,two,three,four,five";
        List<String> limited = Splitter.on(',').limit(3).splitToList(longString);
        System.out.println("Limited split: " + limited);
        
        // Pattern splitting
        String multiDelim = "one;two:three;four:five";
        List<String> pattern = Splitter.onPattern("[;:]").splitToList(multiDelim);
        System.out.println("Pattern split: " + pattern);
        
        // Map splitting
        String keyValues = "name=Alice,age=30,city=Boston";
        Map<String, String> dataMap = Splitter.on(',').withKeyValueSeparator('=').split(keyValues);
        System.out.println("Split to map: " + dataMap);
    }
    
    /**
     * CharMatcher demonstrations
     */
    public static void demonstrateCharMatcher() {
        System.out.println("\n3. CharMatcher Examples");
        System.out.println("=======================");
        
        String text = "Hello, World! 123 @#$";
        
        // Predefined matchers
        System.out.println("Original: " + text);
        System.out.println("Letters only: " + CharMatcher.javaLetter().retainFrom(text));
        System.out.println("Digits only: " + CharMatcher.javaDigit().retainFrom(text));
        System.out.println("Remove whitespace: " + CharMatcher.whitespace().removeFrom(text));
        
        // Custom matchers
        CharMatcher vowels = CharMatcher.anyOf("aeiouAEIOU");
        System.out.println("Remove vowels: " + vowels.removeFrom(text));
        
        // Replace operations
        System.out.println("Replace punctuation with *: " + 
                         CharMatcher.javaLetterOrDigit().negate().replaceFrom(text, '*'));
        
        // Collapse whitespace
        String multiSpace = "Hello    World   Test";
        System.out.println("Collapse spaces: " + 
                         CharMatcher.whitespace().collapseFrom(multiSpace, ' '));
    }
    
    /**
     * Advanced string processing patterns
     */
    public static void demonstrateAdvancedStringProcessing() {
        System.out.println("\n4. Advanced Processing");
        System.out.println("======================");
        
        // Chain operations
        String rawData = " Apple , , Banana  ,Cherry, ";
        List<String> processed = Splitter.on(',')
            .trimResults()
            .omitEmptyStrings()
            .splitToList(rawData);
        String result = Joiner.on(" | ").join(processed);
        System.out.println("Chained processing: " + result);
        
        // Process configuration
        demonstrateConfigurationProcessing();
        
        // CSV processing
        demonstrateCSVProcessing();
    }
    
    public static void demonstrateConfigurationProcessing() {
        System.out.println("\nConfiguration Processing:");
        
        String config = "database.host=localhost,database.port=5432,cache.enabled=true";
        Map<String, String> configMap = Splitter.on(',')
            .withKeyValueSeparator('=')
            .split(config);
        
        configMap.forEach((key, value) -> 
            System.out.println("Config: " + key + " -> " + value));
    }
    
    public static void demonstrateCSVProcessing() {
        System.out.println("\nCSV Processing:");
        
        List<String> csvRows = Arrays.asList(
            "Name,Age,City",
            "Alice,30,Boston",
            "Bob,25,New York",
            "Charlie,35,Chicago"
        );
        
        List<String> headers = Splitter.on(',').splitToList(csvRows.get(0));
        System.out.println("Headers: " + headers);
        
        for (int i = 1; i < csvRows.size(); i++) {
            List<String> values = Splitter.on(',').splitToList(csvRows.get(i));
            System.out.println("Row " + i + ": " + 
                             Joiner.on(" | ").join(values));
        }
    }
    
    /**
     * Real-world string processing scenarios
     */
    public static void demonstrateRealWorldScenarios() {
        System.out.println("\n5. Real-World Scenarios");
        System.out.println("=======================");
        
        // URL processing
        demonstrateURLProcessing();
        
        // Log processing
        demonstrateLogProcessing();
        
        // Data sanitization
        demonstrateDataSanitization();
    }
    
    public static void demonstrateURLProcessing() {
        System.out.println("URL Processing:");
        
        String url = "https://api.example.com/users?name=John&age=30&active=true";
        
        // Extract query parameters
        int queryStart = url.indexOf('?');
        if (queryStart != -1) {
            String queryString = url.substring(queryStart + 1);
            Map<String, String> params = Splitter.on('&')
                .withKeyValueSeparator('=')
                .split(queryString);
            
            System.out.println("URL parameters:");
            params.forEach((key, value) -> 
                System.out.println("  " + key + " = " + value));
        }
    }
    
    public static void demonstrateLogProcessing() {
        System.out.println("\nLog Processing:");
        
        String logEntry = "2023-08-28 10:30:15 [INFO] UserService - User login: alice@example.com";
        
        List<String> parts = Splitter.on(' ').limit(4).splitToList(logEntry);
        if (parts.size() >= 4) {
            System.out.println("Date: " + parts.get(0));
            System.out.println("Time: " + parts.get(1));
            System.out.println("Level: " + parts.get(2));
            System.out.println("Message: " + parts.get(3));
        }
    }
    
    public static void demonstrateDataSanitization() {
        System.out.println("\nData Sanitization:");
        
        String userInput = "  Hello@World!123  ";
        
        // Clean and validate
        String cleaned = CharMatcher.whitespace().trimFrom(userInput);
        String alphanumeric = CharMatcher.javaLetterOrDigit().retainFrom(cleaned);
        String safe = CharMatcher.javaLetterOrDigit()
            .or(CharMatcher.anyOf("-_"))
            .retainFrom(cleaned);
        
        System.out.println("Original: '" + userInput + "'");
        System.out.println("Trimmed: '" + cleaned + "'");
        System.out.println("Alphanumeric only: '" + alphanumeric + "'");
        System.out.println("Safe characters: '" + safe + "'");
    }
}