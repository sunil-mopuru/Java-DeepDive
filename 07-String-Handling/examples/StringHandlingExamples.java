/**
 * StringHandlingExamples.java - Comprehensive String Handling Examples
 * 
 * This program demonstrates various string operations, manipulations,
 * and real-world text processing scenarios.
 */

import java.util.*;
import java.util.regex.*;

public class StringHandlingExamples {
    
    public static void main(String[] args) {
        
        System.out.println("=== STRING HANDLING COMPREHENSIVE EXAMPLES ===\n");
        
        // 1. BASIC STRING OPERATIONS
        basicStringOperations();
        
        // 2. STRING MANIPULATION
        stringManipulation();
        
        // 3. STRING COMPARISON
        stringComparison();
        
        // 4. REGULAR EXPRESSIONS
        regularExpressions();
        
        // 5. STRING FORMATTING
        stringFormatting();
        
        // 6. PRACTICAL APPLICATIONS
        practicalApplications();
    }
    
    // ==================== BASIC STRING OPERATIONS ====================
    
    public static void basicStringOperations() {
        System.out.println("1. BASIC STRING OPERATIONS:");
        System.out.println("----------------------------");
        
        // String creation
        String str1 = "Hello World";           // String literal
        String str2 = new String("Hello");     // String object
        String str3 = "";                      // Empty string
        
        // String properties
        System.out.printf("String: '%s'%n", str1);
        System.out.printf("Length: %d%n", str1.length());
        System.out.printf("Is empty: %b%n", str1.isEmpty());
        System.out.printf("Is blank: %b%n", str1.isBlank());
        
        // Character access
        char firstChar = str1.charAt(0);       // 'H'
        char lastChar = str1.charAt(str1.length() - 1); // 'd'
        System.out.printf("First character: %c%n", firstChar);
        System.out.printf("Last character: %c%n", lastChar);
        
        // Substring operations
        String hello = str1.substring(0, 5);   // "Hello"
        String world = str1.substring(6);      // "World"
        System.out.printf("Substring(0,5): '%s'%n", hello);
        System.out.printf("Substring(6): '%s'%n", world);
        
        // Index operations
        int indexOfO = str1.indexOf('o');      // First occurrence of 'o'
        int lastIndexOfO = str1.lastIndexOf('o'); // Last occurrence of 'o'
        boolean contains = str1.contains("World");
        
        System.out.printf("Index of 'o': %d%n", indexOfO);
        System.out.printf("Last index of 'o': %d%n", lastIndexOfO);
        System.out.printf("Contains 'World': %b%n", contains);
        
        System.out.println();
    }
    
    // ==================== STRING MANIPULATION ====================
    
    public static void stringManipulation() {
        System.out.println("2. STRING MANIPULATION:");
        System.out.println("------------------------");
        
        String original = "  Java Programming Language  ";
        System.out.printf("Original: '%s'%n", original);
        
        // Case operations
        String upper = original.toUpperCase();
        String lower = original.toLowerCase();
        System.out.printf("Upper case: '%s'%n", upper);
        System.out.printf("Lower case: '%s'%n", lower);
        
        // Trimming whitespace
        String trimmed = original.trim();
        String stripped = original.strip();    // Java 11+, better Unicode support
        System.out.printf("Trimmed: '%s'%n", trimmed);
        System.out.printf("Stripped: '%s'%n", stripped);
        
        // Replacement operations
        String replaced = original.replace("Java", "Python");
        String replaceFirst = original.replaceFirst("a", "X");
        String replaceAll = original.replaceAll("[aeiou]", "*"); // Regex
        
        System.out.printf("Replace Java->Python: '%s'%n", replaced);
        System.out.printf("Replace first 'a': '%s'%n", replaceFirst);
        System.out.printf("Replace vowels: '%s'%n", replaceAll);
        
        // String splitting and joining
        String csv = "apple,banana,cherry,date";
        String[] fruits = csv.split(",");
        System.out.printf("Split CSV: %s%n", Arrays.toString(fruits));
        
        String joined = String.join(" | ", fruits);
        System.out.printf("Joined with ' | ': %s%n", joined);
        
        // StringBuilder for efficient string building
        StringBuilder sb = new StringBuilder();
        sb.append("Building");
        sb.append(" a");
        sb.append(" string");
        sb.insert(9, " complex");
        sb.reverse();
        
        System.out.printf("StringBuilder result: %s%n", sb.toString());
        
        // StringBuffer (thread-safe version)
        StringBuffer buffer = new StringBuffer("Thread-safe ");
        buffer.append("string building");
        System.out.printf("StringBuffer result: %s%n", buffer.toString());
        
        System.out.println();
    }
    
    // ==================== STRING COMPARISON ====================
    
    public static void stringComparison() {
        System.out.println("3. STRING COMPARISON:");
        System.out.println("----------------------");
        
        String str1 = "Hello";
        String str2 = "hello";
        String str3 = new String("Hello");
        String str4 = "Hello";
        
        // Reference equality vs content equality
        System.out.printf("str1 == str3: %b (reference)%n", str1 == str3);
        System.out.printf("str1.equals(str3): %b (content)%n", str1.equals(str3));
        System.out.printf("str1 == str4: %b (string pool)%n", str1 == str4);
        
        // Case-sensitive vs case-insensitive
        System.out.printf("str1.equals(str2): %b%n", str1.equals(str2));
        System.out.printf("str1.equalsIgnoreCase(str2): %b%n", str1.equalsIgnoreCase(str2));
        
        // Lexicographical comparison
        int comparison = str1.compareTo(str2);
        int comparisonIgnoreCase = str1.compareToIgnoreCase(str2);
        
        System.out.printf("str1.compareTo(str2): %d%n", comparison);
        System.out.printf("str1.compareToIgnoreCase(str2): %d%n", comparisonIgnoreCase);
        
        // String ordering
        List<String> names = Arrays.asList("Charlie", "alice", "Bob", "david");
        System.out.printf("Original: %s%n", names);
        
        names.sort(String::compareTo);
        System.out.printf("Sorted (case-sensitive): %s%n", names);
        
        names.sort(String.CASE_INSENSITIVE_ORDER);
        System.out.printf("Sorted (case-insensitive): %s%n", names);
        
        // Null-safe comparison
        String nullStr = null;
        String emptyStr = "";
        
        System.out.printf("Objects.equals(nullStr, emptyStr): %b%n", 
                         Objects.equals(nullStr, emptyStr));
        
        System.out.println();
    }
    
    // ==================== REGULAR EXPRESSIONS ====================
    
    public static void regularExpressions() {
        System.out.println("4. REGULAR EXPRESSIONS:");
        System.out.println("------------------------");
        
        // Email validation
        String email = "user@example.com";
        String emailPattern = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        
        boolean isValidEmail = email.matches(emailPattern);
        System.out.printf("Email '%s' is valid: %b%n", email, isValidEmail);
        
        // Phone number validation and formatting
        String phone = "1234567890";
        String phonePattern = "(\\d{3})(\\d{3})(\\d{4})";
        String formattedPhone = phone.replaceAll(phonePattern, "($1) $2-$3");
        System.out.printf("Phone '%s' formatted: %s%n", phone, formattedPhone);
        
        // Pattern compilation for efficiency
        Pattern pattern = Pattern.compile("\\b\\w+@\\w+\\.\\w+\\b");
        String text = "Contact us at info@company.com or support@help.org";
        
        Matcher matcher = pattern.matcher(text);
        System.out.println("Email addresses found:");
        while (matcher.find()) {
            System.out.printf("  Found: %s at position %d-%d%n", 
                            matcher.group(), matcher.start(), matcher.end());
        }
        
        // Text extraction with groups
        String logEntry = "2023-12-25 14:30:22 ERROR: Database connection failed";
        Pattern logPattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2}) (\\w+): (.+)");
        
        Matcher logMatcher = logPattern.matcher(logEntry);
        if (logMatcher.matches()) {
            System.out.println("Log entry parsed:");
            System.out.printf("  Date: %s%n", logMatcher.group(1));
            System.out.printf("  Time: %s%n", logMatcher.group(2));
            System.out.printf("  Level: %s%n", logMatcher.group(3));
            System.out.printf("  Message: %s%n", logMatcher.group(4));
        }
        
        // String replacement with regex
        String messyText = "The   quick    brown   fox";
        String cleaned = messyText.replaceAll("\\s+", " ").trim();
        System.out.printf("Cleaned text: '%s'%n", cleaned);
        
        System.out.println();
    }
    
    // ==================== STRING FORMATTING ====================
    
    public static void stringFormatting() {
        System.out.println("5. STRING FORMATTING:");
        System.out.println("----------------------");
        
        String name = "Alice";
        int age = 30;
        double salary = 75000.50;
        
        // printf-style formatting
        System.out.printf("Name: %s, Age: %d, Salary: $%.2f%n", name, age, salary);
        
        // String.format
        String formatted = String.format("Employee: %s (%d years old)", name, age);
        System.out.println(formatted);
        
        // Number formatting
        System.out.printf("Integer with padding: %05d%n", 42);
        System.out.printf("Float with precision: %.3f%n", Math.PI);
        System.out.printf("Scientific notation: %e%n", 1234567.89);
        
        // Text alignment
        System.out.printf("Left aligned:  '%-10s'%n", "Hello");
        System.out.printf("Right aligned: '%10s'%n", "Hello");
        System.out.printf("Center (approx): '%5s%5s'%n", "", "Hello");
        
        // Date formatting
        Date now = new Date();
        System.out.printf("Current date: %tF %tT%n", now, now);
        System.out.printf("Custom format: %1$tB %1$te, %1$tY%n", now);
        
        // StringBuilder with formatting
        StringBuilder report = new StringBuilder();
        report.append(String.format("--- EMPLOYEE REPORT ---%n"));
        report.append(String.format("Name: %s%n", name));
        report.append(String.format("Age: %d%n", age));
        report.append(String.format("Salary: $%,.2f%n", salary));
        
        System.out.println(report.toString());
        
        System.out.println();
    }
    
    // ==================== PRACTICAL APPLICATIONS ====================
    
    public static void practicalApplications() {
        System.out.println("6. PRACTICAL APPLICATIONS:");
        System.out.println("--------------------------");
        
        // Text analyzer
        TextAnalyzer analyzer = new TextAnalyzer();
        String sampleText = "The quick brown fox jumps over the lazy dog. " +
                           "This sentence contains every letter of the alphabet!";
        
        TextStatistics stats = analyzer.analyzeText(sampleText);
        System.out.println("Text Analysis:");
        System.out.printf("  Characters: %d%n", stats.getCharacterCount());
        System.out.printf("  Words: %d%n", stats.getWordCount());
        System.out.printf("  Sentences: %d%n", stats.getSentenceCount());
        System.out.printf("  Average word length: %.1f%n", stats.getAverageWordLength());
        
        // Password validator
        PasswordValidator validator = new PasswordValidator();
        String[] passwords = {"weak", "StrongPass123!", "NoNumbers!", "short"};
        
        System.out.println("\nPassword Validation:");
        for (String password : passwords) {
            ValidationResult result = validator.validatePassword(password);
            System.out.printf("  '%s': %s%n", password, 
                            result.isValid() ? "VALID" : result.getErrorMessage());
        }
        
        // URL parser
        URLParser urlParser = new URLParser();
        String url = "https://www.example.com:8080/path/to/resource?param1=value1&param2=value2";
        
        ParsedURL parsedURL = urlParser.parseURL(url);
        System.out.println("\nURL Parsing:");
        System.out.printf("  Protocol: %s%n", parsedURL.getProtocol());
        System.out.printf("  Host: %s%n", parsedURL.getHost());
        System.out.printf("  Port: %d%n", parsedURL.getPort());
        System.out.printf("  Path: %s%n", parsedURL.getPath());
        System.out.printf("  Parameters: %s%n", parsedURL.getParameters());
        
        // CSV processor
        CSVProcessor csvProcessor = new CSVProcessor();
        String csvData = "Name,Age,City\n" +
                        "\"John Doe\",30,\"New York\"\n" +
                        "\"Jane Smith\",25,\"Los Angeles\"\n" +
                        "\"Bob Johnson\",35,\"Chicago\"";
        
        List<Map<String, String>> records = csvProcessor.parseCSV(csvData);
        System.out.println("\nCSV Processing:");
        for (Map<String, String> record : records) {
            System.out.printf("  %s%n", record);
        }
        
        System.out.println();
    }
}

// ==================== UTILITY CLASSES ====================

class TextAnalyzer {
    
    public TextStatistics analyzeText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new TextStatistics(0, 0, 0, 0.0);
        }
        
        int charCount = text.length();
        int wordCount = countWords(text);
        int sentenceCount = countSentences(text);
        double avgWordLength = calculateAverageWordLength(text);
        
        return new TextStatistics(charCount, wordCount, sentenceCount, avgWordLength);
    }
    
    private int countWords(String text) {
        String[] words = text.trim().split("\\s+");
        return words.length == 1 && words[0].isEmpty() ? 0 : words.length;
    }
    
    private int countSentences(String text) {
        String[] sentences = text.split("[.!?]+");
        return sentences.length;
    }
    
    private double calculateAverageWordLength(String text) {
        String[] words = text.replaceAll("[^a-zA-Z\\s]", "").trim().split("\\s+");
        if (words.length == 0) return 0.0;
        
        int totalLength = 0;
        for (String word : words) {
            totalLength += word.length();
        }
        
        return (double) totalLength / words.length;
    }
}

class TextStatistics {
    private final int characterCount;
    private final int wordCount;
    private final int sentenceCount;
    private final double averageWordLength;
    
    public TextStatistics(int charCount, int wordCount, int sentenceCount, double avgWordLength) {
        this.characterCount = charCount;
        this.wordCount = wordCount;
        this.sentenceCount = sentenceCount;
        this.averageWordLength = avgWordLength;
    }
    
    public int getCharacterCount() { return characterCount; }
    public int getWordCount() { return wordCount; }
    public int getSentenceCount() { return sentenceCount; }
    public double getAverageWordLength() { return averageWordLength; }
}

class PasswordValidator {
    
    public ValidationResult validatePassword(String password) {
        if (password == null) {
            return new ValidationResult(false, "Password cannot be null");
        }
        
        if (password.length() < 8) {
            return new ValidationResult(false, "Password must be at least 8 characters long");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            return new ValidationResult(false, "Password must contain at least one uppercase letter");
        }
        
        if (!password.matches(".*[a-z].*")) {
            return new ValidationResult(false, "Password must contain at least one lowercase letter");
        }
        
        if (!password.matches(".*\\d.*")) {
            return new ValidationResult(false, "Password must contain at least one digit");
        }
        
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return new ValidationResult(false, "Password must contain at least one special character");
        }
        
        return new ValidationResult(true, "Password is valid");
    }
}

class ValidationResult {
    private final boolean valid;
    private final String errorMessage;
    
    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.errorMessage = message;
    }
    
    public boolean isValid() { return valid; }
    public String getErrorMessage() { return errorMessage; }
}

class URLParser {
    
    public ParsedURL parseURL(String url) {
        if (url == null || url.trim().isEmpty()) {
            return new ParsedURL("", "", -1, "", new HashMap<>());
        }
        
        // Simple URL parsing (not production-ready)
        String protocol = "";
        String host = "";
        int port = -1;
        String path = "";
        Map<String, String> parameters = new HashMap<>();
        
        // Extract protocol
        int protocolEnd = url.indexOf("://");
        if (protocolEnd != -1) {
            protocol = url.substring(0, protocolEnd);
            url = url.substring(protocolEnd + 3);
        }
        
        // Extract parameters
        int questionMarkIndex = url.indexOf('?');
        if (questionMarkIndex != -1) {
            String paramString = url.substring(questionMarkIndex + 1);
            url = url.substring(0, questionMarkIndex);
            
            for (String param : paramString.split("&")) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2) {
                    parameters.put(keyValue[0], keyValue[1]);
                }
            }
        }
        
        // Extract path
        int pathStart = url.indexOf('/');
        if (pathStart != -1) {
            path = url.substring(pathStart);
            url = url.substring(0, pathStart);
        }
        
        // Extract host and port
        int colonIndex = url.indexOf(':');
        if (colonIndex != -1) {
            host = url.substring(0, colonIndex);
            try {
                port = Integer.parseInt(url.substring(colonIndex + 1));
            } catch (NumberFormatException e) {
                // Invalid port, keep default
            }
        } else {
            host = url;
        }
        
        return new ParsedURL(protocol, host, port, path, parameters);
    }
}

class ParsedURL {
    private final String protocol;
    private final String host;
    private final int port;
    private final String path;
    private final Map<String, String> parameters;
    
    public ParsedURL(String protocol, String host, int port, String path, Map<String, String> params) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        this.parameters = new HashMap<>(params);
    }
    
    public String getProtocol() { return protocol; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getPath() { return path; }
    public Map<String, String> getParameters() { return new HashMap<>(parameters); }
}

class CSVProcessor {
    
    public List<Map<String, String>> parseCSV(String csvData) {
        List<Map<String, String>> records = new ArrayList<>();
        
        if (csvData == null || csvData.trim().isEmpty()) {
            return records;
        }
        
        String[] lines = csvData.split("\n");
        if (lines.length < 2) {
            return records;
        }
        
        // Parse header
        String[] headers = parseCSVLine(lines[0]);
        
        // Parse data rows
        for (int i = 1; i < lines.length; i++) {
            String[] values = parseCSVLine(lines[i]);
            
            if (values.length == headers.length) {
                Map<String, String> record = new HashMap<>();
                for (int j = 0; j < headers.length; j++) {
                    record.put(headers[j], values[j]);
                }
                records.add(record);
            }
        }
        
        return records;
    }
    
    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        
        fields.add(current.toString().trim());
        return fields.toArray(new String[0]);
    }
}