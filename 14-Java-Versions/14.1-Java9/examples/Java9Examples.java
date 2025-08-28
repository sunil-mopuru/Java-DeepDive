/**
 * Java 9 Features Examples
 * 
 * Demonstrates the key features introduced in Java 9, including
 * Collection Factory Methods, Stream API enhancements, and Optional improvements.
 * Note: Module System examples are in separate module-info.java files.
 */

import java.util.*;
import java.util.stream.*;

public class Java9Examples {
    
    public static void main(String[] args) {
        System.out.println("=== Java 9 Features Examples ===");
        
        demonstrateCollectionFactories();
        demonstrateStreamEnhancements();
        demonstrateOptionalImprovements();
        demonstratePrivateInterfaceMethods();
        demonstratePracticalUsage();
    }
    
    public static void demonstrateCollectionFactories() {
        System.out.println("\n1. Collection Factory Methods");
        System.out.println("============================");
        
        // List factory methods
        List<String> immutableList = List.of("Java", "Python", "JavaScript", "Go");
        System.out.println("Immutable List: " + immutableList);
        
        // Demonstrate immutability
        try {
            immutableList.add("C++");
        } catch (UnsupportedOperationException e) {
            System.out.println("✓ List is truly immutable: " + e.getClass().getSimpleName());
        }
        
        // Set factory methods
        Set<Integer> immutableSet = Set.of(1, 2, 3, 4, 5);
        System.out.println("Immutable Set: " + immutableSet);
        
        // Map factory methods - different overloads
        Map<String, Integer> smallMap = Map.of(
            "Alice", 25,
            "Bob", 30,
            "Charlie", 35
        );
        System.out.println("Small Map: " + smallMap);
        
        // For larger maps, use Map.ofEntries
        Map<String, String> largeMap = Map.ofEntries(
            Map.entry("country", "USA"),
            Map.entry("state", "California"),
            Map.entry("city", "San Francisco"),
            Map.entry("zipcode", "94102"),
            Map.entry("district", "SOMA")
        );
        System.out.println("Large Map: " + largeMap);
        
        // Comparison with traditional methods
        System.out.println("\nComparison with traditional approaches:");
        
        // Old way (verbose)
        List<String> oldList = new ArrayList<>();
        oldList.add("Item1");
        oldList.add("Item2");
        oldList.add("Item3");
        List<String> unmodifiableOldList = Collections.unmodifiableList(oldList);
        
        // New way (concise)
        List<String> newList = List.of("Item1", "Item2", "Item3");
        
        System.out.println("Old way result: " + unmodifiableOldList);
        System.out.println("New way result: " + newList);
        System.out.println("Both are equal: " + unmodifiableOldList.equals(newList));
    }
    
    public static void demonstrateStreamEnhancements() {
        System.out.println("\n2. Stream API Enhancements");
        System.out.println("===========================");
        
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // takeWhile - takes elements while condition is true
        System.out.println("Original list: " + numbers);
        List<Integer> takeWhileResult = numbers.stream()
            .takeWhile(n -> n < 6)
            .collect(Collectors.toList());
        System.out.println("takeWhile(n < 6): " + takeWhileResult);
        
        // dropWhile - drops elements while condition is true, then takes the rest
        List<Integer> dropWhileResult = numbers.stream()
            .dropWhile(n -> n < 6)
            .collect(Collectors.toList());
        System.out.println("dropWhile(n < 6): " + dropWhileResult);
        
        // ofNullable - creates stream from potentially null value
        String nullableValue = null;
        String nonNullValue = "Hello Java 9";
        
        long nullCount = Stream.ofNullable(nullableValue).count();
        long nonNullCount = Stream.ofNullable(nonNullValue).count();
        
        System.out.println("\nofNullable examples:");
        System.out.println("Stream from null value count: " + nullCount);
        System.out.println("Stream from non-null value count: " + nonNullCount);
        
        // Practical ofNullable usage
        List<String> words = Arrays.asList("apple", null, "banana", null, "cherry");
        List<String> nonNullWords = words.stream()
            .flatMap(Stream::ofNullable)
            .collect(Collectors.toList());
        System.out.println("Filtered non-null words: " + nonNullWords);
        
        // iterate with predicate (Java 9 enhancement)
        System.out.println("\nIterate with predicate:");
        List<Integer> evenNumbers = Stream.iterate(2, n -> n <= 20, n -> n + 2)
            .collect(Collectors.toList());
        System.out.println("Even numbers 2-20: " + evenNumbers);
        
        // Fibonacci sequence using iterate
        List<Integer> fibonacci = Stream.iterate(new int[]{0, 1}, 
                                               arr -> new int[]{arr[1], arr[0] + arr[1]})
            .limit(10)
            .map(arr -> arr[0])
            .collect(Collectors.toList());
        System.out.println("First 10 Fibonacci numbers: " + fibonacci);
    }
    
    public static void demonstrateOptionalImprovements() {
        System.out.println("\n3. Optional Enhancements");
        System.out.println("========================");
        
        Optional<String> presentValue = Optional.of("Java 9 is great!");
        Optional<String> emptyValue = Optional.empty();
        
        // ifPresentOrElse - execute action if present, else execute empty action
        System.out.println("ifPresentOrElse examples:");
        presentValue.ifPresentOrElse(
            value -> System.out.println("Present: " + value),
            () -> System.out.println("Value is empty")
        );
        
        emptyValue.ifPresentOrElse(
            value -> System.out.println("Present: " + value),
            () -> System.out.println("Value is empty")
        );
        
        // or - provides alternative Optional if current is empty
        Optional<String> result = emptyValue.or(() -> Optional.of("Default value"));
        System.out.println("\n'or' method result: " + result.get());
        
        // stream - converts Optional to Stream
        List<String> fromOptional = presentValue.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("Optional to Stream: " + fromOptional);
        
        // Practical usage: chaining operations
        Optional<String> finalResult = Optional.of("  java 9 features  ")
            .map(String::trim)
            .filter(s -> s.length() > 5)
            .map(String::toUpperCase)
            .or(() -> Optional.of("FALLBACK"));
        
        System.out.println("Chained operations result: " + finalResult.get());
        
        // Working with collections of Optionals
        List<Optional<String>> optionals = Arrays.asList(
            Optional.of("Apple"),
            Optional.empty(),
            Optional.of("Banana"),
            Optional.empty(),
            Optional.of("Cherry")
        );
        
        List<String> extractedValues = optionals.stream()
            .flatMap(Optional::stream)  // Java 9 feature
            .collect(Collectors.toList());
        
        System.out.println("Extracted from Optionals: " + extractedValues);
    }
    
    public static void demonstratePrivateInterfaceMethods() {
        System.out.println("\n4. Private Interface Methods");
        System.out.println("============================");
        
        Calculator calc = new BasicCalculator();
        
        System.out.println("Addition: " + calc.add(5, 3));
        System.out.println("Complex calculation: " + calc.complexCalculation(10, 5));
        System.out.println("Statistics: " + calc.getStatistics());
    }
    
    public static void demonstratePracticalUsage() {
        System.out.println("\n5. Practical Real-World Examples");
        System.out.println("================================");
        
        // Configuration management with immutable collections
        ConfigurationManager config = new ConfigurationManager();
        config.displayConfiguration();
        
        // Data processing pipeline
        DataProcessor processor = new DataProcessor();
        processor.processUserData();
        
        // Event handling system
        EventManager eventManager = new EventManager();
        eventManager.processEvents();
    }
}

// Interface demonstrating private methods (Java 9 feature)
interface Calculator {
    
    // Public abstract methods
    int add(int a, int b);
    int multiply(int a, int b);
    
    // Default method using private helper
    default int complexCalculation(int a, int b) {
        int sum = add(a, b);
        int product = multiply(a, b);
        return performComplexLogic(sum, product);
    }
    
    // Another default method
    default String getStatistics() {
        return formatResult("Calculator", "ready");
    }
    
    // Private helper methods (Java 9 feature)
    private int performComplexLogic(int sum, int product) {
        return (sum * 2) + (product / 2);
    }
    
    private String formatResult(String operation, String status) {
        return String.format("[%s] Status: %s", operation.toUpperCase(), status);
    }
}

class BasicCalculator implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
    
    @Override
    public int multiply(int a, int b) {
        return a * b;
    }
}

// Practical examples
class ConfigurationManager {
    private final Map<String, String> serverConfig = Map.of(
        "host", "localhost",
        "port", "8080",
        "protocol", "https"
    );
    
    private final List<String> allowedOrigins = List.of(
        "https://example.com",
        "https://api.example.com",
        "https://admin.example.com"
    );
    
    private final Set<String> supportedLanguages = Set.of(
        "en", "es", "fr", "de", "it"
    );
    
    public void displayConfiguration() {
        System.out.println("Server Config: " + serverConfig);
        System.out.println("Allowed Origins: " + allowedOrigins);
        System.out.println("Supported Languages: " + supportedLanguages);
        
        // These collections are immutable by design
        try {
            serverConfig.put("timeout", "30");
        } catch (UnsupportedOperationException e) {
            System.out.println("✓ Configuration is immutable");
        }
    }
}

class DataProcessor {
    public void processUserData() {
        List<User> users = Arrays.asList(
            new User("Alice", 25, "alice@example.com"),
            new User("Bob", 30, null), // null email
            new User("Charlie", 22, "charlie@example.com"),
            new User("David", 35, null), // null email
            new User("Eve", 28, "eve@example.com")
        );
        
        // Process users with email addresses using Java 9 features
        List<String> validEmails = users.stream()
            .map(User::getEmail)
            .flatMap(Stream::ofNullable)  // Filter out nulls
            .takeWhile(email -> !email.startsWith("z"))  // Take until we hit 'z'
            .collect(Collectors.toList());
        
        System.out.println("Valid emails: " + validEmails);
        
        // Group users by age ranges
        Map<String, List<User>> ageGroups = users.stream()
            .collect(Collectors.groupingBy(user -> {
                int age = user.getAge();
                if (age < 25) return "Young";
                else if (age < 30) return "Mid";
                else return "Senior";
            }));
        
        ageGroups.forEach((group, userList) -> 
            System.out.println(group + " users: " + userList.size()));
    }
}

class EventManager {
    public void processEvents() {
        List<Optional<String>> events = Arrays.asList(
            Optional.of("USER_LOGIN"),
            Optional.empty(),
            Optional.of("DATA_UPDATED"),
            Optional.empty(),
            Optional.of("USER_LOGOUT")
        );
        
        // Process only present events
        List<String> processedEvents = events.stream()
            .flatMap(Optional::stream)
            .map(event -> "PROCESSED_" + event)
            .collect(Collectors.toList());
        
        System.out.println("Processed events: " + processedEvents);
        
        // Use ifPresentOrElse for event handling
        events.forEach(event -> 
            event.ifPresentOrElse(
                e -> System.out.println("Handling event: " + e),
                () -> System.out.println("Skipping null event")
            )
        );
    }
}

// Supporting classes
class User {
    private final String name;
    private final int age;
    private final String email;
    
    public User(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
    
    @Override
    public String toString() {
        return String.format("User{name='%s', age=%d, email='%s'}", name, age, email);
    }
}