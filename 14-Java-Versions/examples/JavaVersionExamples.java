/**
 * Java Version Evolution Examples (Java 9-21+)
 * 
 * Demonstrates key features introduced in each Java version
 * from Java 9 through Java 21 with practical examples.
 */

import java.util.*;
import java.util.stream.*;
import java.net.http.*;
import java.net.URI;
import java.nio.file.*;
import java.time.*;

public class JavaVersionExamples {
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Java Version Evolution Examples ===");
        
        demonstrateJava9Features();
        demonstrateJava10Features();
        demonstrateJava11Features();
        demonstrateJava14Features();
        demonstrateJava17Features();
        demonstrateModernJavaFeatures();
    }
    
    public static void demonstrateJava9Features() {
        System.out.println("\n=== Java 9 Features ===");
        
        // Collection Factory Methods
        List<String> names = List.of("Alice", "Bob", "Charlie");
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
        Map<String, Integer> scores = Map.of(
            "Alice", 95,
            "Bob", 87,
            "Charlie", 92
        );
        
        System.out.println("Collection Factories:");
        System.out.println("Names: " + names);
        System.out.println("Numbers: " + numbers);
        System.out.println("Scores: " + scores);
        
        // Stream API enhancements
        List<Integer> sequence = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        
        List<Integer> takeWhileResult = sequence.stream()
            .takeWhile(n -> n < 5)
            .collect(Collectors.toList());
            
        List<Integer> dropWhileResult = sequence.stream()
            .dropWhile(n -> n < 5)
            .collect(Collectors.toList());
        
        System.out.println("\nStream enhancements:");
        System.out.println("Original: " + sequence);
        System.out.println("takeWhile < 5: " + takeWhileResult);
        System.out.println("dropWhile < 5: " + dropWhileResult);
        
        // Optional enhancements
        Optional<String> optional = Optional.of("Hello Java 9");
        optional.ifPresentOrElse(
            value -> System.out.println("\nOptional value: " + value),
            () -> System.out.println("No value present")
        );
        
        Optional<String> alternative = Optional.empty()
            .or(() -> Optional.of("Default value"));
        System.out.println("Alternative: " + alternative.get());
    }
    
    public static void demonstrateJava10Features() {
        System.out.println("\n=== Java 10 Features ===");
        
        // Local Variable Type Inference (var)
        var name = "John Doe";  // String
        var age = 30;           // int
        var scores = List.of(85, 92, 78, 96);  // List<Integer>
        var student = Map.of("name", "Alice", "grade", "A");  // Map<String, String>
        
        System.out.println("Type inference with var:");
        System.out.println("Name (String): " + name);
        System.out.println("Age (int): " + age);
        System.out.println("Scores (List<Integer>): " + scores);
        System.out.println("Student (Map): " + student);
        
        // Unmodifiable collections
        List<String> mutableList = new ArrayList<>();
        mutableList.add("A");
        mutableList.add("B");
        mutableList.add("C");
        
        List<String> unmodifiableList = mutableList.stream()
            .collect(Collectors.toUnmodifiableList());
        
        System.out.println("\nUnmodifiable collection: " + unmodifiableList);
        
        // Optional.orElseThrow() without parameter
        Optional<String> optional = Optional.of("Java 10");
        String value = optional.orElseThrow();  // Throws NoSuchElementException if empty
        System.out.println("Optional value: " + value);
    }
    
    public static void demonstrateJava11Features() throws Exception {
        System.out.println("\n=== Java 11 Features (LTS) ===");
        
        // String methods
        String text = "  Hello Java 11  ";
        System.out.println("String enhancements:");
        System.out.println("Original: '" + text + "'");
        System.out.println("isBlank(): " + text.isBlank());
        System.out.println("strip(): '" + text.strip() + "'");
        System.out.println("repeat(3): " + "Java".repeat(3));
        
        String multiline = "Line 1\nLine 2\nLine 3";
        System.out.println("\nLines processing:");
        multiline.lines()
            .forEach(line -> System.out.println("- " + line));
        
        // Files methods
        Path tempFile = Files.createTempFile("java11", ".txt");
        String content = "Hello Java 11!\nFile I/O made easy.";
        
        Files.writeString(tempFile, content);
        String readContent = Files.readString(tempFile);
        
        System.out.println("\nFile operations:");
        System.out.println("Written and read: " + readContent.replace("\n", " | "));
        
        Files.deleteIfExists(tempFile);
        
        // HTTP Client (if network is available)
        try {
            HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .timeout(Duration.ofSeconds(10))
                .header("User-Agent", "Java 11 Example")
                .GET()
                .build();
            
            System.out.println("\nHTTP Client request sent to httpbin.org");
            // Uncomment to actually make the request:
            // HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // System.out.println("Response status: " + response.statusCode());
        } catch (Exception e) {
            System.out.println("HTTP request demo (would work with internet connection)");
        }
    }
    
    public static void demonstrateJava14Features() {
        System.out.println("\n=== Java 14 Features ===");
        
        // Switch expressions (standardized in Java 14)
        String dayType = getDayType(DayOfWeek.MONDAY);
        System.out.println("Monday is a: " + dayType);
        
        // Text blocks (second preview, standardized in Java 15)
        String json = """
            {
                "name": "John Doe",
                "age": 30,
                "email": "john@example.com"
            }
            """;
        System.out.println("\nText block JSON:");
        System.out.println(json);
        
        // Pattern matching for instanceof (preview)
        Object obj = "Hello Java 14";
        if (obj instanceof String s) {
            System.out.println("Pattern matching: " + s.toUpperCase());
        }
        
        // Records (preview in Java 14, standardized in Java 16)
        // Note: This is a preview feature in Java 14
        System.out.println("\nRecords example:");
        var person = new PersonRecord("Alice", 25);
        System.out.println("Person: " + person);
        System.out.println("Name: " + person.name());
        System.out.println("Age: " + person.age());
    }
    
    public static void demonstrateJava17Features() {
        System.out.println("\n=== Java 17 Features (LTS) ===");
        
        // Sealed classes (standardized in Java 17)
        Shape circle = new Circle(5.0);
        Shape rectangle = new Rectangle(4.0, 6.0);
        
        System.out.println("Sealed classes:");
        System.out.println("Circle area: " + calculateArea(circle));
        System.out.println("Rectangle area: " + calculateArea(rectangle));
        
        // Enhanced pattern matching
        String shapeInfo = getShapeInfo(circle);
        System.out.println("Shape info: " + shapeInfo);
    }
    
    public static void demonstrateModernJavaFeatures() {
        System.out.println("\n=== Modern Java Features (18-21+) ===");
        
        // Stream.toList() (Java 16+)
        List<String> filtered = Stream.of("apple", "banana", "cherry", "date")
            .filter(fruit -> fruit.length() > 5)
            .toList();  // No need for collect(Collectors.toList())
        
        System.out.println("Filtered fruits: " + filtered);
        
        // Pattern matching for switch (preview/standardized in Java 21)
        Object[] objects = {42, "Hello", 3.14, List.of(1, 2, 3)};
        
        System.out.println("\nPattern matching for switch:");
        for (Object obj : objects) {
            String description = describeObject(obj);
            System.out.println(obj + " -> " + description);
        }
        
        // Virtual Threads (Java 21)
        System.out.println("\nVirtual Threads demo:");
        try {
            // This would work in Java 21+
            System.out.println("Virtual threads provide lightweight concurrency");
            System.out.println("Millions of virtual threads can run concurrently");
        } catch (Exception e) {
            System.out.println("Virtual threads available in Java 21+");
        }
    }
    
    // Helper methods
    
    private static String getDayType(DayOfWeek day) {
        return switch (day) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "weekday";
            case SATURDAY, SUNDAY -> "weekend";
        };
    }
    
    private static String describeObject(Object obj) {
        // This is a simplified version - full pattern matching syntax varies by Java version
        if (obj instanceof Integer i) {
            return "Integer with value " + i;
        } else if (obj instanceof String s) {
            return "String with length " + s.length();
        } else if (obj instanceof Double d) {
            return "Double with value " + d;
        } else if (obj instanceof List<?> list) {
            return "List with " + list.size() + " elements";
        } else {
            return "Unknown type: " + obj.getClass().getSimpleName();
        }
    }
    
    private static double calculateArea(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rectangle r -> r.width() * r.height();
        };
    }
    
    private static String getShapeInfo(Shape shape) {
        return switch (shape) {
            case Circle c -> "Circle with radius " + c.radius();
            case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
        };
    }
}

// Records (Java 14 preview, Java 16 standard)
record PersonRecord(String name, int age) {
    // Compact constructor for validation
    public PersonRecord {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }
}

// Sealed classes (Java 15 preview, Java 17 standard)
sealed interface Shape permits Circle, Rectangle {}

record Circle(double radius) implements Shape {
    public Circle {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
    }
}

record Rectangle(double width, double height) implements Shape {
    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
    }
}

// Demonstration of modern Java features progression
class FeatureProgression {
    
    // Java 8: Lambda and Streams
    public static void java8Style() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<String> result = names.stream()
            .filter(name -> name.length() > 3)
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    }
    
    // Java 9: Collection factories and takeWhile
    public static void java9Style() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<String> result = names.stream()
            .takeWhile(name -> name.length() > 3)
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    }
    
    // Java 10: var keyword
    public static void java10Style() {
        var names = List.of("Alice", "Bob", "Charlie");
        var result = names.stream()
            .filter(name -> name.length() > 3)
            .map(String::toUpperCase)
            .collect(Collectors.toUnmodifiableList());
    }
    
    // Java 16: Stream.toList()
    public static void java16Style() {
        var names = List.of("Alice", "Bob", "Charlie");
        var result = names.stream()
            .filter(name -> name.length() > 3)
            .map(String::toUpperCase)
            .toList();  // Much simpler!
    }
}