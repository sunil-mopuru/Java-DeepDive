/**
 * I/O Utilities Examples - Efficient File and Stream Operations
 * 
 * Demonstrates Guava's I/O utilities including Files, Resources,
 * ByteStreams, and CharStreams for simplified file operations.
 */

import com.google.common.io.*;
import com.google.common.collect.*;
import com.google.common.base.Charsets;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IOUtilitiesExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Guava I/O Utilities Examples ===");
        
        try {
            demonstrateFileOperations();
            demonstrateResourceHandling();
            demonstrateStreamUtilities();
            demonstrateAdvancedIOPatterns();
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        }
    }
    
    public static void demonstrateFileOperations() throws IOException {
        System.out.println("\n1. File Operations");
        System.out.println("==================");
        
        // Create temporary files for demonstration
        File tempFile = File.createTempFile("guava-demo", ".txt");
        tempFile.deleteOnExit();
        
        // Write to file
        String content = "Hello, Guava I/O!\nThis is a test file.\nWith multiple lines.";
        Files.asCharSink(tempFile, StandardCharsets.UTF_8).write(content);
        System.out.println("✓ Written to file: " + tempFile.getName());
        
        // Read entire file
        String readContent = Files.asCharSource(tempFile, StandardCharsets.UTF_8).read();
        System.out.println("Read content:\n" + readContent);
        
        // Read lines
        List<String> lines = Files.asCharSource(tempFile, StandardCharsets.UTF_8).readLines();
        System.out.println("Lines read: " + lines);
        
        // Append to file
        Files.asCharSink(tempFile, StandardCharsets.UTF_8, FileWriteMode.APPEND)
             .write("\nAppended line");
        
        // Copy file
        File copyFile = File.createTempFile("guava-copy", ".txt");
        copyFile.deleteOnExit();
        Files.copy(tempFile, copyFile);
        System.out.println("✓ File copied to: " + copyFile.getName());
        
        // File information
        demonstrateFileInfo(tempFile);
    }
    
    public static void demonstrateFileInfo(File file) throws IOException {
        System.out.println("\nFile Information:");
        System.out.println("Size: " + Files.asByteSource(file).size() + " bytes");
        System.out.println("Exists: " + file.exists());
        System.out.println("Readable: " + file.canRead());
        System.out.println("Writable: " + file.canWrite());
        
        // Hash file content
        String hash = Files.asByteSource(file).hash(Hashing.sha256()).toString();
        System.out.println("SHA256 hash: " + hash.substring(0, 16) + "...");
    }
    
    public static void demonstrateResourceHandling() throws IOException {
        System.out.println("\n2. Resource Handling");
        System.out.println("====================");
        
        // Create a temporary resource file
        File resourceFile = File.createTempFile("resource", ".properties");
        resourceFile.deleteOnExit();
        
        String properties = "app.name=MyApplication\napp.version=1.0\napp.debug=true";
        Files.asCharSink(resourceFile, StandardCharsets.UTF_8).write(properties);
        
        // Read as resource (simulating classpath resource)
        try {
            String resourceContent = Files.asCharSource(resourceFile, StandardCharsets.UTF_8).read();
            System.out.println("Resource content:\n" + resourceContent);
            
            // Parse properties
            Properties props = new Properties();
            try (StringReader reader = new StringReader(resourceContent)) {
                props.load(reader);
            }
            
            System.out.println("Parsed properties:");
            props.forEach((key, value) -> 
                System.out.println("  " + key + " = " + value));
                
        } catch (IOException e) {
            System.out.println("Error reading resource: " + e.getMessage());
        }
    }
    
    public static void demonstrateStreamUtilities() throws IOException {
        System.out.println("\n3. Stream Utilities");
        System.out.println("===================");
        
        // ByteStreams utilities
        demonstrateByteStreams();
        
        // CharStreams utilities  
        demonstrateCharStreams();
    }
    
    public static void demonstrateByteStreams() throws IOException {
        System.out.println("ByteStreams Operations:");
        
        // Create sample data
        byte[] data = "Sample binary data for ByteStreams demo".getBytes(StandardCharsets.UTF_8);
        
        // Convert to InputStream
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        
        // Read all bytes
        byte[] allBytes = ByteStreams.toByteArray(input);
        System.out.println("Read " + allBytes.length + " bytes");
        
        // Copy streams
        input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long copied = ByteStreams.copy(input, output);
        System.out.println("Copied " + copied + " bytes");
        
        // Exhaust stream
        input = new ByteArrayInputStream(data);
        long exhausted = ByteStreams.exhaust(input);
        System.out.println("Exhausted " + exhausted + " bytes");
        
        // Read and ignore
        input = new ByteArrayInputStream(data);
        ByteStreams.skipFully(input, 7); // Skip "Sample "
        byte[] remaining = ByteStreams.toByteArray(input);
        System.out.println("After skip: " + new String(remaining, StandardCharsets.UTF_8));
    }
    
    public static void demonstrateCharStreams() throws IOException {
        System.out.println("\nCharStreams Operations:");
        
        String text = "Line 1\nLine 2\nLine 3\nFinal line";
        StringReader reader = new StringReader(text);
        
        // Read all text
        String allText = CharStreams.toString(reader);
        System.out.println("All text length: " + allText.length());
        
        // Read lines
        reader = new StringReader(text);
        List<String> lines = CharStreams.readLines(reader);
        System.out.println("Lines read: " + lines.size());
        lines.forEach(line -> System.out.println("  " + line));
        
        // Copy readers
        StringReader source = new StringReader(text);
        StringWriter destination = new StringWriter();
        long charsCopied = CharStreams.copy(source, destination);
        System.out.println("Copied " + charsCopied + " characters");
    }
    
    public static void demonstrateAdvancedIOPatterns() throws IOException {
        System.out.println("\n4. Advanced I/O Patterns");
        System.out.println("=========================");
        
        demonstrateFileProcessing();
        demonstrateStreamChaining();
        demonstrateCloserUtility();
    }
    
    public static void demonstrateFileProcessing() throws IOException {
        System.out.println("File Processing Pipeline:");
        
        // Create sample data file
        File dataFile = File.createTempFile("data", ".csv");
        dataFile.deleteOnExit();
        
        String csvData = "Name,Age,City\nAlice,30,Boston\nBob,25,New York\nCharlie,35,Chicago";
        Files.asCharSink(dataFile, StandardCharsets.UTF_8).write(csvData);
        
        // Process file line by line
        List<String> lines = Files.asCharSource(dataFile, StandardCharsets.UTF_8).readLines();
        
        System.out.println("Processing CSV data:");
        String header = lines.get(0);
        System.out.println("Header: " + header);
        
        for (int i = 1; i < lines.size(); i++) {
            String[] fields = lines.get(i).split(",");
            System.out.printf("Person %d: %s (%s years old) from %s%n", 
                            i, fields[0], fields[1], fields[2]);
        }
        
        // Transform and write to new file
        File outputFile = File.createTempFile("processed", ".txt");
        outputFile.deleteOnExit();
        
        List<String> processedLines = new ArrayList<>();
        processedLines.add("Processed Data:");
        for (int i = 1; i < lines.size(); i++) {
            String[] fields = lines.get(i).split(",");
            processedLines.add(String.format("%s is %s years old", fields[0], fields[1]));
        }
        
        Files.asCharSink(outputFile, StandardCharsets.UTF_8)
             .writeLines(processedLines, "\n");
        
        System.out.println("✓ Processed data written to: " + outputFile.getName());
    }
    
    public static void demonstrateStreamChaining() throws IOException {
        System.out.println("\nStream Chaining:");
        
        // Create a chain of operations
        File inputFile = File.createTempFile("input", ".txt");
        inputFile.deleteOnExit();
        
        String input = "The quick brown fox jumps over the lazy dog";
        Files.asCharSink(inputFile, StandardCharsets.UTF_8).write(input);
        
        // Chain: Read -> Transform -> Write
        String transformed = Files.asCharSource(inputFile, StandardCharsets.UTF_8)
            .read()
            .toUpperCase()
            .replace(" ", "_");
        
        File outputFile = File.createTempFile("output", ".txt");
        outputFile.deleteOnExit();
        
        Files.asCharSink(outputFile, StandardCharsets.UTF_8).write(transformed);
        
        String result = Files.asCharSource(outputFile, StandardCharsets.UTF_8).read();
        System.out.println("Transformed result: " + result);
    }
    
    public static void demonstrateCloserUtility() throws IOException {
        System.out.println("\nCloser Utility:");
        
        Closer closer = Closer.create();
        try {
            // Register multiple resources for automatic cleanup
            File tempFile = File.createTempFile("closer-demo", ".txt");
            tempFile.deleteOnExit();
            
            FileInputStream input = closer.register(new FileInputStream(tempFile));
            FileOutputStream output = closer.register(new FileOutputStream(tempFile));
            
            // Use the streams
            output.write("Test data".getBytes(StandardCharsets.UTF_8));
            output.flush();
            
            System.out.println("✓ Resources will be automatically closed");
            
        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close(); // Automatically closes all registered resources
            System.out.println("✓ All resources closed via Closer");
        }
    }
}