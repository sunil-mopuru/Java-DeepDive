# Java 11 Features - First LTS After Java 8

## 🎯 **Overview**

Java 11 is a Long Term Support (LTS) release that includes significant improvements over Java 8, making it the preferred migration target for organizations moving from Java 8. It removes deprecated features while adding powerful new APIs.

---

## 📚 **Major Features**

### **1. HTTP Client API (Standard) - JEP 321**

Replaces legacy `HttpUrlConnection` and third-party libraries like Apache HttpClient.

#### **Basic HTTP Operations**
```java
import java.net.http.*;
import java.net.URI;
import java.time.Duration;

// Create HTTP client
HttpClient client = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(10))
    .build();

// GET request
HttpRequest getRequest = HttpRequest.newBuilder()
    .uri(URI.create(\"https://api.example.com/users\"))
    .timeout(Duration.ofSeconds(30))
    .header(\"Accept\", \"application/json\")
    .GET()
    .build();

// Synchronous response
HttpResponse<String> response = client.send(getRequest, 
    HttpResponse.BodyHandlers.ofString());

System.out.println(\"Status: \" + response.statusCode());
System.out.println(\"Body: \" + response.body());

// Asynchronous response
CompletableFuture<HttpResponse<String>> futureResponse = 
    client.sendAsync(getRequest, HttpResponse.BodyHandlers.ofString());

futureResponse.thenApply(HttpResponse::body)
    .thenAccept(System.out::println)
    .join();
```

#### **POST with JSON**
```java
// POST request with JSON body
String jsonData = \"{\\\"name\\\":\\\"John\\\",\\\"email\\\":\\\"john@example.com\\\"}\";

HttpRequest postRequest = HttpRequest.newBuilder()
    .uri(URI.create(\"https://api.example.com/users\"))
    .header(\"Content-Type\", \"application/json\")
    .POST(HttpRequest.BodyPublishers.ofString(jsonData))
    .build();

HttpResponse<String> postResponse = client.send(postRequest,
    HttpResponse.BodyHandlers.ofString());
```

#### **File Upload/Download**
```java
// Upload file
Path filePath = Paths.get(\"document.pdf\");
HttpRequest uploadRequest = HttpRequest.newBuilder()
    .uri(URI.create(\"https://api.example.com/upload\"))
    .POST(HttpRequest.BodyPublishers.ofFile(filePath))
    .build();

// Download file
HttpRequest downloadRequest = HttpRequest.newBuilder()
    .uri(URI.create(\"https://api.example.com/download/file.pdf\"))
    .build();

HttpResponse<Path> downloadResponse = client.send(downloadRequest,
    HttpResponse.BodyHandlers.ofFile(Paths.get(\"downloaded-file.pdf\")));
```

#### **Advanced HTTP Features**
```java
// Custom client configuration
HttpClient advancedClient = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)  // Prefer HTTP/2
    .followRedirects(HttpClient.Redirect.NORMAL)
    .connectTimeout(Duration.ofSeconds(20))
    .authenticator(new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(\"user\", \"password\".toCharArray());
        }
    })
    .build();

// WebSocket support
WebSocket webSocket = HttpClient.newHttpClient()
    .newWebSocketBuilder()
    .buildAsync(URI.create(\"ws://echo.websocket.org\"), new WebSocket.Listener() {
        @Override
        public void onOpen(WebSocket webSocket) {
            webSocket.sendText(\"Hello WebSocket!\", true);
            WebSocket.Listener.super.onOpen(webSocket);
        }
        
        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            System.out.println(\"Received: \" + data);
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }
    })
    .join();
```

### **2. String Methods Enhancements**

#### **New String Methods**
```java
String text = \"  Hello World  \";
String multiline = \"Line 1\nLine 2\nLine 3\";
String empty = \"   \";

// isBlank() - true if string is empty or contains only whitespace
boolean blank1 = \"\".isBlank();        // true
boolean blank2 = \"   \".isBlank();     // true  
boolean blank3 = \"Hello\".isBlank();   // false

// strip() - removes leading and trailing whitespace (Unicode-aware)
String stripped = text.strip();       // \"Hello World\"
String stripLeading = text.stripLeading();   // \"Hello World  \"
String stripTrailing = text.stripTrailing(); // \"  Hello World\"

// lines() - returns stream of lines
multiline.lines()
    .map(String::strip)
    .filter(line -> !line.isEmpty())
    .forEach(System.out::println);

// repeat() - repeats string n times
String repeated = \"Ha\".repeat(3);     // \"HaHaHa\"
String border = \"-\".repeat(20);       // \"--------------------\"

// Practical examples
public class StringUtilities {
    public static String formatTable(List<String> rows) {
        return rows.stream()
            .map(row -> \"| \" + row + \" |\")
            .collect(Collectors.joining(\"\n\"));
    }
    
    public static List<String> parseMultilineInput(String input) {
        return input.lines()
            .map(String::strip)
            .filter(line -> !line.isBlank())
            .collect(Collectors.toList());
    }
}
```

### **3. Files Methods Enhancements**

#### **String File Operations**
```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

// readString() - read entire file as string
Path configFile = Paths.get(\"config.txt\");
String content = Files.readString(configFile);
String contentWithCharset = Files.readString(configFile, StandardCharsets.UTF_8);

// writeString() - write string to file
String data = \"Configuration data\nLine 2\nLine 3\";
Files.writeString(configFile, data);
Files.writeString(configFile, data, StandardCharsets.UTF_8);

// Practical file processing
public class ConfigurationManager {
    private final Path configPath;
    
    public ConfigurationManager(String configFile) {
        this.configPath = Paths.get(configFile);
    }
    
    public Properties loadConfiguration() throws IOException {
        if (!Files.exists(configPath)) {
            return new Properties();
        }
        
        String content = Files.readString(configPath);
        Properties props = new Properties();
        
        content.lines()
            .map(String::strip)
            .filter(line -> !line.isBlank() && !line.startsWith(\"#\"))
            .filter(line -> line.contains(\"=\"))
            .forEach(line -> {
                String[] parts = line.split(\"=\", 2);
                props.setProperty(parts[0].strip(), parts[1].strip());
            });
            
        return props;
    }
    
    public void saveConfiguration(Properties props) throws IOException {
        StringBuilder content = new StringBuilder();
        content.append(\"# Configuration file\n\");
        content.append(\"# Generated on: \").append(LocalDateTime.now()).append(\"\n\n\");
        
        props.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> content.append(entry.getKey())
                .append(\" = \")
                .append(entry.getValue())
                .append(\"\n\"));
                
        Files.writeString(configPath, content.toString());
    }
}
```

### **4. Lambda Parameter Type Inference**

#### **var in Lambda Parameters**
```java
// Java 11 allows var in lambda parameters for consistency
List<String> names = List.of(\"Alice\", \"Bob\", \"Charlie\");

// Before Java 11 - explicit types or no types
names.stream()
    .filter((String name) -> name.length() > 3)
    .map((String name) -> name.toUpperCase())
    .forEach(System.out::println);

// Java 11 - var for consistency with annotations
names.stream()
    .filter((@NonNull var name) -> name.length() > 3)
    .map((@NonNull var name) -> name.toUpperCase())
    .forEach(System.out::println);

// Useful when you need annotations on lambda parameters
list.stream()
    .map((@NotNull var item) -> process(item))
    .filter((@Nullable var result) -> result != null)
    .collect(Collectors.toList());
```

### **5. Nest-Based Access Control - JEP 181**

#### **Improved Nested Class Access**
```java
public class OuterClass {
    private String outerField = \"outer\";
    
    public class InnerClass {
        private String innerField = \"inner\";
        
        public void accessOuter() {
            // Direct access without synthetic methods
            System.out.println(outerField); // No bridge methods needed
        }
    }
    
    public void accessInner(InnerClass inner) {
        // Direct access to private inner field
        System.out.println(inner.innerField); // Improved in Java 11
    }
    
    // Reflection improvements
    public void reflectionExample() throws Exception {
        InnerClass inner = new InnerClass();
        
        // Better reflection support for nested classes
        Field field = InnerClass.class.getDeclaredField(\"innerField\");
        field.setAccessible(true); // Works more reliably
        String value = (String) field.get(inner);
        
        System.out.println(\"Reflected value: \" + value);
    }
}
```

---

## 🗑️ **Removals and Deprecations**

### **Removed Features**
```java
// Java EE and CORBA modules removed
// java.xml.ws (JAX-WS)
// java.xml.bind (JAXB)  
// java.xml.ws.annotation (Common Annotations)
// java.corba (CORBA)
// java.transaction (JTA)
// java.se.ee (Aggregator module)

// Migration: Add dependencies explicitly
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>
```

### **Deprecated for Removal**
```java
// Nashorn JavaScript Engine deprecated
// Use GraalVM or other JavaScript engines

// Pack200 compression deprecated
// Use standard compression tools
```

---

## 🚀 **Performance Improvements**

### **JVM Enhancements**
```java
// Low-Overhead Heap Profiling - JEP 331
// Enable with: -XX:+FlightRecorder -XX:StartFlightRecording

// Epsilon Garbage Collector - JEP 318  
// No-op GC for performance testing
// Enable with: -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC

// ZGC (Experimental) - JEP 333
// Ultra-low latency garbage collector
// Enable with: -XX:+UnlockExperimentalVMOptions -XX:+UseZGC
```

### **Library Improvements**
```java
// Improved String performance
String text = \"Hello World\";
text.strip();    // Faster than trim() for Unicode
text.isBlank();  // Optimized blank checking

// Better HTTP/2 performance
HttpClient client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .build(); // Automatic HTTP/2 upgrade when available
```

---

## 📊 **Migration from Java 8**

### **Dependency Analysis**
```bash
# Check for removed modules
jdeps --jdk-internals your-application.jar

# Analyze module dependencies
jdeps --module-path lib --add-modules ALL-MODULE-PATH your-app.jar
```

### **Common Migration Issues**

#### **JAXB Removal**
```java
// Problem: NoClassDefFoundError for JAXB classes
// Solution: Add JAXB dependency

// Maven dependency
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.1</version>
</dependency>

// Gradle dependency
implementation 'org.glassfish.jaxb:jaxb-runtime:2.3.1'
```

#### **Font Rendering Changes**
```java
// Problem: Font rendering differences
// Solution: Use system property for compatibility
// -Dsun.java2d.renderer=sun.java2d.marlin.MarlinRenderingEngine
```

### **Testing Migration**
```java
// Test HTTP Client migration
public class HttpClientMigrationTest {
    
    @Test
    void testHttpClientBasicGet() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(\"https://httpbin.org/get\"))
            .build();
            
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
            
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains(\"httpbin\"));
    }
    
    @Test
    void testStringMethods() {
        assertTrue(\"   \".isBlank());
        assertEquals(\"Hello\", \"  Hello  \".strip());
        assertEquals(\"HaHaHa\", \"Ha\".repeat(3));
        
        String multiline = \"Line 1\nLine 2\nLine 3\";
        List<String> lines = multiline.lines().collect(Collectors.toList());
        assertEquals(3, lines.size());
    }
}
```

---

## 🎓 **Best Practices**

### **HTTP Client Usage**
1. **Reuse HttpClient instances** - they're thread-safe and expensive to create
2. **Set appropriate timeouts** - both connection and request timeouts
3. **Use async methods** for better scalability
4. **Handle HTTP/2** - configure version preference
5. **Implement retry logic** - for transient failures

### **String Processing**
1. **Use isBlank() over isEmpty()** - handles whitespace correctly
2. **Prefer strip() over trim()** - better Unicode support  
3. **Use lines()** for line-by-line processing
4. **Combine with Streams** - leverage functional processing

### **File Operations**
1. **Use Files.readString()** for small files only
2. **Specify charset explicitly** when needed
3. **Handle IOException** appropriately
4. **Consider memory usage** for large files

---

**Next:** [Java 17 LTS Features](../14.9-Java17/README.md)"