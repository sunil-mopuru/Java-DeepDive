/**
 * Java 11 LTS Features Examples - Beginner to Advanced
 * 
 * Demonstrates Java 11 features including HTTP Client API, enhanced String methods,
 * Files methods, var keyword enhancements, and performance improvements.
 */

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

public class Java11Examples {
    
    public static void main(String[] args) {
        System.out.println("=== Java 11 LTS Features Examples ===");
        
        // BEGINNER LEVEL
        demonstrateStringMethods();
        showVarEnhancements();
        
        // INTERMEDIATE LEVEL  
        exploreFilesMethods();
        demonstrateHttpClient();
        
        // ADVANCED LEVEL
        showCollectionToArray();
        demonstrateOptionalMethods();
        performanceDemonstrations();
    }
    
    public static void demonstrateStringMethods() {
        System.out.println("\n1. Enhanced String Methods");
        System.out.println("==========================");
        
        // isBlank() - checks if string is empty or contains only whitespace
        String emptyString = "";
        String whitespaceString = "   \t\n  ";
        String contentString = "  Hello World  ";
        
        System.out.println("String.isBlank() examples:");
        System.out.println("Empty string is blank: " + emptyString.isBlank());
        System.out.println("Whitespace string is blank: " + whitespaceString.isBlank());
        System.out.println("Content string is blank: " + contentString.isBlank());
        
        // strip(), stripLeading(), stripTrailing()
        System.out.println("\nString.strip() methods:");
        System.out.println("Original: '" + contentString + "'");
        System.out.println("strip(): '" + contentString.strip() + "'");
        System.out.println("stripLeading(): '" + contentString.stripLeading() + "'");
        System.out.println("stripTrailing(): '" + contentString.stripTrailing() + "'");
        
        // lines() - returns stream of lines
        String multilineText = "Line 1\nLine 2\n\nLine 4\nLine 5";
        System.out.println("\nString.lines() example:");
        System.out.println("Original text:");
        System.out.println(multilineText);
        System.out.println("\nProcessed lines:");
        multilineText.lines()
                    .filter(line -> !line.isBlank())
                    .map(line -> "-> " + line)
                    .forEach(System.out::println);
        
        // repeat() - repeats string n times
        System.out.println("\nString.repeat() examples:");
        String pattern = "Java ";
        System.out.println("Pattern repeated 3 times: " + pattern.repeat(3));
        System.out.println("Border: " + "=".repeat(50));
        
        // Practical usage: Data cleaning
        System.out.println("\nPractical example: Data cleaning");
        List<String> rawData = Arrays.asList(
            "  John Doe  ",
            "",
            "   Jane Smith   ",
            "\t\n",
            "Bob Johnson"
        );
        
        List<String> cleanedData = rawData.stream()
                                         .filter(s -> !s.isBlank())
                                         .map(String::strip)
                                         .collect(Collectors.toList());
        
        System.out.println("Raw data: " + rawData);
        System.out.println("Cleaned data: " + cleanedData);
    }
    
    public static void showVarEnhancements() {
        System.out.println("\n2. Var Keyword Enhancements");
        System.out.println("============================");
        
        // var in lambda parameters (Java 11)
        System.out.println("var in lambda parameters:");
        
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana");
        
        // Without var (Java 10)
        names.stream()
             .filter(name -> name.length() > 4)
             .forEach(name -> System.out.println("Long name: " + name));
        
        // With var (Java 11) - allows annotations
        System.out.println("\nWith var (enables annotations):");
        names.stream()
             .map((var name) -> name.toUpperCase())
             .forEach(System.out::println);
        
        // Practical example: Processing with annotations
        List<Person> people = Arrays.asList(
            new Person("John", 25),
            new Person("Jane", 30),
            new Person("Bob", 35)
        );
        
        // Using var for better readability in complex lambdas
        people.stream()
              .filter((var person) -> person.getAge() >= 30)
              .map((var person) -> person.getName() + " is " + person.getAge())
              .forEach(System.out::println);
    }
    
    public static void exploreFilesMethods() {
        System.out.println("\n3. Enhanced Files Methods");
        System.out.println("=========================");
        
        try {
            // Create temporary files for demonstration
            Path tempFile = Files.createTempFile("java11-demo", ".txt");
            String content = "Line 1\nLine 2\nLine 3\nEmpty line:\n\nLine 6";
            Files.writeString(tempFile, content);
            
            // Files.writeString() - write string directly to file
            System.out.println("Files.writeString() - wrote content to: " + tempFile);
            
            // Files.readString() - read entire file as string
            String fileContent = Files.readString(tempFile);
            System.out.println("Files.readString() result:");
            System.out.println(fileContent);
            
            // Processing file content with new String methods
            System.out.println("\nProcessing file with String.lines():");
            fileContent.lines()
                      .filter(line -> !line.isBlank())
                      .map(line -> "-> " + line)
                      .forEach(System.out::println);
            
            // Working with different charsets
            Path utf8File = Files.createTempFile("utf8-demo", ".txt");
            String unicodeContent = "Hello 世界! 🌍 Café naïve résumé";
            Files.writeString(utf8File, unicodeContent, StandardCharsets.UTF_8);
            
            String readUnicode = Files.readString(utf8File, StandardCharsets.UTF_8);
            System.out.println("\nUnicode content: " + readUnicode);
            
            // Cleanup
            Files.deleteIfExists(tempFile);
            Files.deleteIfExists(utf8File);
            
        } catch (IOException e) {
            System.err.println("File operation error: " + e.getMessage());
        }
    }
    
    public static void demonstrateHttpClient() {
        System.out.println("\n4. HTTP Client API");
        System.out.println("==================");
        
        // Create HTTP client
        HttpClient client = HttpClient.newBuilder()
                                     .version(HttpClient.Version.HTTP_2)
                                     .connectTimeout(Duration.ofSeconds(10))
                                     .build();
        
        try {
            // Simple GET request
            System.out.println("1. Simple GET request:");
            HttpRequest getRequest = HttpRequest.newBuilder()
                                               .uri(URI.create("https://httpbin.org/get"))
                                               .timeout(Duration.ofSeconds(30))
                                               .build();
            
            HttpResponse<String> getResponse = client.send(getRequest, 
                                                          HttpResponse.BodyHandlers.ofString());
            
            System.out.println("Status Code: " + getResponse.statusCode());
            System.out.println("Response length: " + getResponse.body().length());
            
            // POST request with JSON body
            System.out.println("\n2. POST request with JSON:");
            String jsonBody = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
            
            HttpRequest postRequest = HttpRequest.newBuilder()
                                                .uri(URI.create("https://httpbin.org/post"))
                                                .timeout(Duration.ofSeconds(30))
                                                .header("Content-Type", "application/json")
                                                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                                                .build();
            
            HttpResponse<String> postResponse = client.send(postRequest, 
                                                           HttpResponse.BodyHandlers.ofString());
            
            System.out.println("POST Status: " + postResponse.statusCode());
            System.out.println("Response headers: " + postResponse.headers().map());
            
            // Asynchronous request
            System.out.println("\n3. Asynchronous request:");
            CompletableFuture<HttpResponse<String>> asyncResponse = 
                client.sendAsync(getRequest, HttpResponse.BodyHandlers.ofString());
            
            asyncResponse.thenAccept(response -> {
                System.out.println("Async response status: " + response.statusCode());
                System.out.println("Async response received asynchronously!");
            }).join(); // Wait for completion in demo
            
        } catch (IOException | InterruptedException e) {
            System.err.println("HTTP request error: " + e.getMessage());
        }
        
        // Multiple concurrent requests
        demonstrateConcurrentHttpRequests(client);
    }
    
    public static void demonstrateConcurrentHttpRequests(HttpClient client) {
        System.out.println("\n4. Concurrent HTTP Requests:");
        
        List<String> urls = Arrays.asList(
            "https://httpbin.org/delay/1",
            "https://httpbin.org/delay/2",
            "https://httpbin.org/delay/1"
        );
        
        long startTime = System.currentTimeMillis();
        
        // Send requests concurrently
        List<CompletableFuture<HttpResponse<String>>> futures = urls.stream()
            .map(url -> HttpRequest.newBuilder()
                                  .uri(URI.create(url))
                                  .timeout(Duration.ofSeconds(10))
                                  .build())
            .map(request -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
            .collect(Collectors.toList());
        
        // Wait for all to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );
        
        allOf.thenRun(() -> {
            long endTime = System.currentTimeMillis();
            System.out.println("All requests completed in: " + (endTime - startTime) + "ms");
            
            futures.forEach(future -> {
                try {
                    HttpResponse<String> response = future.get();
                    System.out.println("Response status: " + response.statusCode());
                } catch (Exception e) {
                    System.err.println("Error getting response: " + e.getMessage());
                }
            });
        }).join();
    }
    
    public static void showCollectionToArray() {
        System.out.println("\n5. Collection.toArray() Enhancement");
        System.out.println("===================================");
        
        List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry", "Date");
        
        // Java 8 way (verbose)
        System.out.println("Java 8 way:");
        String[] fruitsArray8 = fruits.toArray(new String[fruits.size()]);
        System.out.println("Array: " + Arrays.toString(fruitsArray8));
        
        // Java 11 way (simpler)
        System.out.println("\nJava 11 way:");
        String[] fruitsArray11 = fruits.toArray(String[]::new);
        System.out.println("Array: " + Arrays.toString(fruitsArray11));
        
        // With different collection types
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
        Integer[] numbersArray = numbers.toArray(Integer[]::new);
        System.out.println("Numbers array: " + Arrays.toString(numbersArray));
        
        // Practical example: Converting filtered stream to array
        String[] longNames = fruits.stream()
                                  .filter(name -> name.length() > 5)
                                  .toArray(String[]::new);
        System.out.println("Long names: " + Arrays.toString(longNames));
    }
    
    public static void demonstrateOptionalMethods() {
        System.out.println("\n6. Optional Enhancements");
        System.out.println("========================");
        
        // Optional.isEmpty() - complement to isPresent()
        Optional<String> emptyOpt = Optional.empty();
        Optional<String> valueOpt = Optional.of("Hello World");
        
        System.out.println("Optional.isEmpty() examples:");
        System.out.println("Empty optional isEmpty(): " + emptyOpt.isEmpty());
        System.out.println("Value optional isEmpty(): " + valueOpt.isEmpty());
        
        // Practical usage in validation
        System.out.println("\nPractical validation example:");
        List<Optional<String>> optionals = Arrays.asList(
            Optional.of("Valid data"),
            Optional.empty(),
            Optional.of("Another valid"),
            Optional.empty()
        );
        
        long validCount = optionals.stream()
                                 .filter(opt -> !opt.isEmpty())
                                 .count();
        
        System.out.println("Valid entries: " + validCount + " out of " + optionals.size());
        
        // Processing valid values only
        optionals.stream()
                .filter(opt -> !opt.isEmpty())
                .map(Optional::get)
                .forEach(value -> System.out.println("Processing: " + value));
    }
    
    public static void performanceDemonstrations() {
        System.out.println("\n7. Performance Improvements");
        System.out.println("===========================");
        
        // String concatenation performance
        System.out.println("String performance comparison:");
        
        int iterations = 10000;
        
        // StringBuilder approach
        long startTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("Java ").append(i).append(" ");
        }
        String sbResult = sb.toString();
        long sbTime = System.currentTimeMillis() - startTime;
        
        // String.repeat() approach for patterns
        startTime = System.currentTimeMillis();
        String pattern = "Java";
        String repeatResult = pattern.repeat(1000);
        long repeatTime = System.currentTimeMillis() - startTime;
        
        System.out.println("StringBuilder time: " + sbTime + "ms");
        System.out.println("String.repeat() time: " + repeatTime + "ms");
        System.out.println("Results length - SB: " + sbResult.length() + 
                          ", Repeat: " + repeatResult.length());
        
        // Epsilon Garbage Collector demonstration (conceptual)
        System.out.println("\nJava 11 GC improvements:");
        System.out.println("- Epsilon GC: No-op garbage collector for performance testing");
        System.out.println("- Z GC: Experimental low-latency garbage collector");
        System.out.println("- G1 GC improvements for better pause times");
        
        // Memory usage example
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Current memory usage:");
        System.out.println("Free memory: " + runtime.freeMemory() / 1024 / 1024 + " MB");
        System.out.println("Total memory: " + runtime.totalMemory() / 1024 / 1024 + " MB");
        System.out.println("Max memory: " + runtime.maxMemory() / 1024 / 1024 + " MB");
    }
    
    // Helper classes for examples
    static class Person {
        private String name;
        private int age;
        
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        public String getName() { return name; }
        public int getAge() { return age; }
        
        @Override
        public String toString() {
            return String.format("Person{name='%s', age=%d}", name, age);
        }
    }
}